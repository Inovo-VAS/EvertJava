package inovo.flat.file.leads.importer;

import inovo.db.Database;
import inovo.presence.PresenceDB;
import inovo.servlet.InovoServletContextListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import com.sun.org.apache.regexp.internal.recompile;

public class FlatFileLeadsImportRequest {

	private final HashMap<String, String> _flatfileleadsimportproperties=new HashMap<String, String>();
	private final FlatFileLeadsImportQueueManager _flatFileLeadsImportQueueManager;
	private boolean _enabled=false;
	private String _flatFileAlias="";
	private String _flatFileLookupMask="";
	
	public FlatFileLeadsImportRequest(FlatFileLeadsImportQueueManager flatFileLeadsImportQueueManager,HashMap<String, String> flatfileleadsimportproperties){
		this._flatFileLeadsImportQueueManager=flatFileLeadsImportQueueManager;
		this.setFlatFileLeadsImportProperties(flatfileleadsimportproperties);
	}
	
	public void initiateLeadsImportRequest(){
		new Thread(){
			@Override
			public void run() {
				while(!_flatFileLeadsImportQueueManager._shutdownQueue&&_enabled){
					performImportTask();
					try {
						this.sleep(1024);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				removeFlatFileImportTaskFromQueue();
			}
		}.start();
	}
	
	public synchronized void setFlatFileLeadsImportProperties(HashMap<String, String> flatfileleadsimportproperties){
		for(String reqpropname:flatfileleadsimportproperties.keySet()){
			this._flatfileleadsimportproperties.put(reqpropname,flatfileleadsimportproperties.get(reqpropname));
		}
		
		this._enabled=(this._flatfileleadsimportproperties.get("ENABLEALIAS").equals("TRUE"));
		this._flatFileAlias=this._flatfileleadsimportproperties.get("ALIAS").toUpperCase();
		this._flatFileLookupMask=this._flatfileleadsimportproperties.get("FILELOOKUPMASK");
		
	}
	
	public void disable(){
		this._enabled=false;
	}
	
	protected void removeFlatFileImportTaskFromQueue(){
		_flatFileLeadsImportQueueManager.removeFlatFileImportTaskFromQueue(this,_flatFileAlias);
	}
	
	private HashMap<String,String> _currentFlatFileImportProperties=new HashMap<String,String>();
	
	public void performImportTask(){
		_currentFlatFileImportProperties.clear();
		_currentFlatFileImportProperties.putAll(_flatfileleadsimportproperties);
		
		String sourcePath=this.currentFlatFileImportProperty("SOURCEPATH");
		while(!sourcePath.endsWith(File.separator)){
			sourcePath+=File.separator;
		}
			
		File fsourcepath=new File(sourcePath);
		if(fsourcepath.exists()){
			final String[]currentRegisteredFileAliases=_flatFileLeadsImportQueueManager.registeredFileAliases(_flatFileAlias);
			
			File[] aliasFilesFound=fsourcepath.listFiles(new FilenameFilter() {				
					@Override
					public boolean accept(File dir, String name) {
						if(_flatFileLookupMask.equals("")){
							if(name.toUpperCase().startsWith(_flatFileAlias)){
								for(String testAlias:currentRegisteredFileAliases){
									if(testAlias.toUpperCase().equals(_flatFileAlias)) continue;
									if(name.toUpperCase().startsWith(testAlias.toUpperCase())) return false;
								}
								
								return true;
							}
						}
						else{
							int sectionindex=0;
							for(String fnamemasks:_flatFileLookupMask.split("[*]")){
								if(fnamemasks.equals("")) continue;
								sectionindex=name.toUpperCase().substring(sectionindex).indexOf(fnamemasks.toUpperCase());
								if(sectionindex==-1) return false;
								sectionindex=sectionindex+fnamemasks.length();
							}
							return true;
						}
						return false;
					}					
				});
			if(aliasFilesFound!=null){
				if(aliasFilesFound.length>0){
					try {
						Thread.currentThread().sleep(1024*10);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					for(File fileAliasFound: aliasFilesFound){
						this.debug("START Importing File ["+_flatFileAlias+":"+fileAliasFound.getName()+"]");
						try{
							fileAliasFound.renameTo(fileAliasFound);
							try{
								FileInputStream fin=new FileInputStream(fileAliasFound);
								TreeMap<Integer,ArrayList<String>> fileAliasFoundCSVDataSet=new TreeMap<Integer,ArrayList<String>>();
								/*ArrayList<String> aliasColumns=new ArrayList<String>();
								Database.populateColumnsArray(aliasColumns,"CSV",this._currentFlatFileImportProperties.get("CURRENTFILEFIELDS"),this._currentFlatFileImportProperties.get("COMADELIM").charAt(0));
								Database.populateDatasetFromFlatFileStream(fileAliasFoundCSVDataSet,"CSV",fin,aliasColumns,this._currentFlatFileImportProperties.get("COMADELIM").charAt(0),null);
								this.importFileDataset(fileAliasFound.getName(), fileAliasFoundCSVDataSet);*/
								this.importFileDatasetFromFileStream(fileAliasFound.getName(), fin);
								fin.close();
								fin=null;
								Database.cleanupDataset(fileAliasFoundCSVDataSet);
								this.debug("END Importing File ["+_flatFileAlias+":"+fileAliasFound.getName()+"]");
							}
							catch(Exception ed){
								this.debug("WARNING ENDING Importing File ["+_flatFileAlias+":"+fileAliasFound.getName()+"]:"+ed.getMessage());
							}
							fileAliasFound.delete();
						}
						catch(Exception e){
							this.debug("ERROR ENDING Importing File ["+_flatFileAlias+":"+fileAliasFound.getName()+"]:"+e.getMessage());
						}
					}
				}
				aliasFilesFound=null;
			}
		}
		
		_currentFlatFileImportProperties.clear();
	}

	
	private enum CSVContentStage{
		none,
		string
	}
	
	private void readRowsFromStream(InputStream dataStream, String streamformat,
			ArrayList<String> rowColumns,Object rowReadMethodOwner,String readRowMethod,String...contentProperties) throws Exception{
		if(",FLAT,CSV,".indexOf(","+(streamformat=(streamformat==null?"CSV":streamformat.toUpperCase()).trim())+",")==-1){
			streamformat="CSV";
		}
		boolean disposeColumnsWhenDone=(rowColumns==null);
		int currentRowIndex=0;
		Method rowDataMethod=(rowReadMethodOwner==null?null:inovo.adhoc.AdhocUtils.findMethod(rowReadMethodOwner.getClass().getMethods(), readRowMethod, false));
		ArrayList<String> rowData=new ArrayList<String>();
		
		HashMap<String,String> additionalProperties=new HashMap<String, String>();
		
		if(contentProperties!=null){
			for(String propItem:contentProperties){
				if(propItem.isEmpty()) continue;
				if(propItem.indexOf("=")>-1){
					additionalProperties.put(propItem.substring(0,propItem.indexOf("=")).trim().toUpperCase(), propItem.substring(propItem.indexOf("=")+1).trim());
				}
			}
		}
		
		StringBuilder tempContent=new StringBuilder();
		if(streamformat.equals("CSV")||streamformat.equals("FLAT")){
			CSVContentStage csvContentStage=CSVContentStage.none;
			if(rowColumns==null){
				rowColumns=new ArrayList<String>();
			}
			int inputRead=0;
			int inputBufferIndex=0;
			byte br=0;
			char cr=0;
			byte[] inputBuffer=new byte[8192];
			
			char colDelim=',';
			char prevStringChar=0;
			char stringParenthis='\"';
			if(additionalProperties.containsKey("TEXTPAR")){
				if(!additionalProperties.get("TEXTPAR").equals("")) stringParenthis=additionalProperties.get("TEXTPAR").charAt(0);
			}
			
			if(additionalProperties.containsKey("COLDELIM")){
				if(!additionalProperties.get("COLDELIM").equals("")) colDelim=additionalProperties.get("COLDELIM").charAt(0);
			}
			
			while((inputRead=dataStream.read(inputBuffer, 0, inputBuffer.length))>-1){
				if(inputRead>0){
					inputBufferIndex=0;
					while(inputBufferIndex<inputRead){						
						cr=(char)(br=inputBuffer[inputBufferIndex++]);
						if(streamformat.equals("CSV")){
							switch(csvContentStage){
							case none:
								switch(cr){
								case 10:
										prevStringChar=0;
										
										rowData.add(tempContent.substring(0,tempContent.length()).trim());
										if(currentRowIndex==0){
											rowColumns.isEmpty();
											rowColumns.addAll(rowData);
										}
										if(rowDataMethod!=null&&rowReadMethodOwner!=null){
											try{
												rowDataMethod.invoke(rowReadMethodOwner,new Object[]{currentRowIndex,rowColumns,rowData});
											}
											catch(Exception rce){
												rce.printStackTrace();
												//TODO
											}
										}
										currentRowIndex++;
										tempContent.setLength(0);
										rowData.clear();
									break;
								case 13:continue;
								default:
									if(cr==colDelim){
										prevStringChar=0;
										rowData.add(tempContent.substring(0,tempContent.length()).trim());
										tempContent.setLength(0);
									}
									else if(cr==stringParenthis){
										csvContentStage=CSVContentStage.string;
									}
									else{
										tempContent.append(cr);
									}
									break;
								}
								break;
							case string:
								if(cr==stringParenthis){
									if(prevStringChar==stringParenthis){
										tempContent.append(cr);
										prevStringChar=0;
									}
									else{
										prevStringChar=0;
										csvContentStage=CSVContentStage.none;
									}
								}
								else{
									tempContent.append(cr);
									prevStringChar=cr;
								}
								break;
							default:
								break;							
							}
						}
					}
				}
				else{
					Thread.sleep(10);
				}
			}
		}
		System.out.print(Integer.MAX_VALUE);
		tempContent.setLength(0);
		tempContent=null;
		additionalProperties.clear();
		additionalProperties=null;	
		if(disposeColumnsWhenDone){
			rowColumns.clear();
			rowColumns=null;
		}
	}

	private void debug(String message) {
		if(InovoServletContextListener.inovoServletListener().logger()!=null){
			InovoServletContextListener.inovoServletListener().logger().debug(message);
		}
	}

	private String formattedFieldParamValue(String fieldParamValueToFormat,ArrayList<String> rowData,ArrayList<String> columnData,String fileName){
		String formattedFieldValue="";
		String possibleParamValue="";
		boolean possibleParamFound=false;
		int columnDataindex=0;
		int maxColLength=0;
		int currentColLength=0;
		Iterator<String> coli=columnData.iterator();
		while(coli.hasNext()){
			if((currentColLength=coli.next().length())>maxColLength) maxColLength=currentColLength;
		}
		if(maxColLength<14) maxColLength=14;
		for(char cf:fieldParamValueToFormat.toCharArray()){
			if(cf==':'){
				if(!possibleParamFound){
					possibleParamFound=true;
				}
				else{
					possibleParamFound=false;
					formattedFieldValue+=":"+possibleParamValue+":";
					possibleParamValue="";
				}
			}
			else if(possibleParamFound){
					if(maxColLength>=possibleParamValue.trim().length()){
						if(columnData.contains(possibleParamValue.trim().toUpperCase())){
							formattedFieldValue+=rowData.get(columnData.indexOf(possibleParamValue.trim().toUpperCase()))+(cf==']'?"":cf);
							possibleParamValue="";
							possibleParamFound=false;
						}
						else {
							if(possibleParamValue.trim().toUpperCase().equals("ALLIASFILENAME")){
								formattedFieldValue+=fileName.indexOf(".")>-1?fileName.substring(0,fileName.lastIndexOf(".")):fileName+cf;
								possibleParamValue="";
								possibleParamFound=false;
							}
							else if(possibleParamValue.trim().toUpperCase().equals("IMPORTERALIAS")){
								formattedFieldValue+=_flatFileAlias+cf;
								possibleParamValue="";
								possibleParamFound=false;
							}
							else{
								if(cf!='['&&cf!=']') possibleParamValue+=cf;
							}
						}
					}
					else{
						formattedFieldValue+=":"+possibleParamValue+cf;
						possibleParamValue="";
						possibleParamFound=false;
					}
			}
			else{
				if(possibleParamFound){
					possibleParamValue+=cf;
				}
				else{
					formattedFieldValue+=cf;
				}
			}
		}
		if(possibleParamFound){
			if(maxColLength>=possibleParamValue.trim().length()){
				if(columnData.contains(possibleParamValue.trim().toUpperCase())){
					formattedFieldValue+=rowData.get(columnData.indexOf(possibleParamValue.trim().toUpperCase()));
				}
				else {
					if(possibleParamValue.trim().toUpperCase().equals("ALLIASFILENAME")){
						formattedFieldValue+=fileName.indexOf(".")>-1?fileName.substring(0,fileName.lastIndexOf(".")):fileName;
					}
					else if(possibleParamValue.trim().toUpperCase().equals("IMPORTERALIAS")){
						formattedFieldValue+=_flatFileAlias;
					}
				}
			}
			else{
				formattedFieldValue+=":"+possibleParamValue;
			}
		}
		return formattedFieldValue;
	}
	
	private void importFileDataset(String filename,
			TreeMap<Integer, ArrayList<String>> fileAliasFoundCSVDataSet) {
		
		ArrayList<String> fileAliasDataSetColumns=fileAliasFoundCSVDataSet.get(0);
		
		HashMap<String,String> sourceidparams=new HashMap<String,String>();
		TreeMap<String,String> callparams=new TreeMap<String,String>();
		
		boolean usefilenameasloadname=this.currentFlatFileImportProperty("USEFILENAMEASLOADNAME").equals("TRUE");
		boolean enablenewloadwhencreated=this.currentFlatFileImportProperty("ENABLENEWLOAD").equals("TRUE");
		boolean forcecreatingload=this.currentFlatFileImportProperty("FORCECREATINGLOAD").equals("TRUE");
		
		HashMap<String,String> mergedMasterParams=new HashMap<String, String>();
		
		//this._currentFlatFileImportProperties.put("IMPORTFILENAME", filename.indexOf(".")>-1?filename.substring(0,filename.lastIndexOf(".")):filename);
		
		this.debug("START Importing File Dataset ["+_flatFileAlias+":"+filename+"]");
		int recordProcessed=0;
		int recordPocessedRef=0;
		try{
			this.debug("TOTAL Records IN File Dataset ["+_flatFileAlias+":"+filename+"] - "+(fileAliasFoundCSVDataSet.size()-1));
			String maxSourceID="0";
			
			String masterServiceid=this.currentFlatFileImportProperty("DEFAULTSERVICEID");
			String masterLoadID=this.currentFlatFileImportProperty("DEFAULTLOADID");
			String masterPriority=this.currentFlatFileImportProperty("DEFAULTPRIORITY");
			if(masterPriority.equals("")||masterPriority.equals("0")) masterPriority="100";
			
			TreeMap<Integer,ArrayList<String>> fileFinalImportDataSet=new TreeMap<Integer,ArrayList<String>>();
			
			for(int rowindex:fileAliasFoundCSVDataSet.keySet()){
				if(rowindex==0) continue;
				callparams.clear();
				
				ArrayList<String> selectedRowData=fileAliasFoundCSVDataSet.get(rowindex);
				
				String serviceid = this.formattedFieldParamValue(":"+this.currentFlatFileImportProperty("SERVICEIDFIELD"),selectedRowData, fileAliasDataSetColumns,filename);
				mergedMasterParams.clear();
				mergedMasterParams.putAll(Database.rowData(fileAliasDataSetColumns, selectedRowData));
				if(serviceid.equals("")||serviceid.equals(masterServiceid)){// serviceid.equals(":"+this.currentFlatFileImportProperty("SERVICEIDFIELD"))){
					serviceid=(PresenceDB.serviceExist(masterServiceid)?masterServiceid:"");
				}
				else{
					if(!PresenceDB.serviceExist(serviceid)){
						if(!PresenceDB.serviceExist(serviceid=masterServiceid)){
							serviceid="";
						}
					}
				}
				if(serviceid.equals("")){
					continue;
				}
				
				
				
				callparams.put("SERVICEID", serviceid);
				
				String loadid = this.formattedFieldParamValue(":"+this.currentFlatFileImportProperty("LOADIDFIELD"),selectedRowData, fileAliasDataSetColumns,filename);
				if(loadid.equals("")||loadid.equals(":"+this.currentFlatFileImportProperty("LOADIDFIELD"))){
					loadid=masterLoadID;
				}
				
				if((loadid=loadid.equals("0")?"":loadid).equals("")||forcecreatingload){
					if(forcecreatingload){
						String loadname="";
						if(usefilenameasloadname){
							loadname=filename.toUpperCase();
						}
						else{
							loadname=this.formattedFieldParamValue(this.currentFlatFileImportProperty("LOADNAMEMASK"),selectedRowData, fileAliasDataSetColumns,filename);
						}
						
						if(loadname.length()>50){
							loadname=loadname.substring(0,50);
						}
						
						if(PresenceDB.serviceLoadExist(serviceid,loadid, loadname.toUpperCase())){
							loadid=(loadid.equals("")?PresenceDB.serviceLoadIDByLoadDescription(serviceid, loadname.toUpperCase()):loadid);
						}
						else{
							if(PresenceDB.createServiceLoad(serviceid, loadid, loadname.toUpperCase(), enablenewloadwhencreated, Integer.parseInt(masterPriority))){
								loadid=(loadid.equals("")?PresenceDB.serviceLoadIDByLoadDescription(serviceid, loadname.toUpperCase()):loadid);
								inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("PRESENCE LOAD CREATED - [SERVICEID="+serviceid+", LOADID="+loadid+", LOADNAME="+loadname.toUpperCase());
							}
							else{
								if(masterLoadID.equals("")||masterLoadID.equals("0")){
									masterLoadID="0";
								}
								if(PresenceDB.createServiceLoad(serviceid=masterServiceid, loadid, loadname, enablenewloadwhencreated, Integer.parseInt(masterPriority))){
									if(masterLoadID.equals("0")){
										loadid=(loadid.equals("")?PresenceDB.serviceLoadIDByLoadDescription(serviceid, loadname.toUpperCase()):loadid);
									}
									inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("PRESENCE LOAD CREATED - [SERVICEID="+serviceid+", LOADID="+loadid+", LOADNAME="+loadname.toUpperCase());
								}
								else{
									inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("FAILED CREATING PRESENCE LOAD - [SERVICEID="+serviceid+", LOADID="+loadid+", LOADNAME="+loadname.toUpperCase());
								}
							}
						}
					}
					if(loadid.equals("")||loadid.equals("0")) continue;
				}
				else{
					if(forcecreatingload){
						String loadname="";
						if(usefilenameasloadname){
							loadname=filename.toUpperCase();
						}
						else{
							loadname=_flatFileAlias.toUpperCase();
						}
						
						if(loadname.length()>50){
							loadname=loadname.substring(0,50);
						}
						
						if(PresenceDB.serviceLoadExist(serviceid,loadid, loadname.toUpperCase())){
							loadid=PresenceDB.serviceLoadIDByLoadDescription(serviceid, loadname.toUpperCase());
						}
						else{
							if(PresenceDB.createServiceLoad(masterServiceid, loadid, loadname.toUpperCase(), enablenewloadwhencreated, Integer.parseInt(masterPriority))){
								loadid=PresenceDB.serviceLoadIDByLoadDescription(serviceid, loadname.toUpperCase());
								inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("PRESENCE LOAD CREATED - [SERVICEID="+serviceid+", LOADID="+loadid+", LOADNAME="+loadname.toUpperCase());
							}
							else{
								if(PresenceDB.createServiceLoad(masterServiceid, loadid=masterLoadID, loadname.toUpperCase(), enablenewloadwhencreated, Integer.parseInt(masterPriority))){
									loadid=PresenceDB.serviceLoadIDByLoadDescription(serviceid, loadname.toUpperCase());
									inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("PRESENCE LOAD CREATED - [SERVICEID="+serviceid+", LOADID="+loadid+", LOADNAME="+loadname.toUpperCase());
								}
								else{
									inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("FAILED CREATING PRESENCE LOAD - [SERVICEID="+serviceid+", LOADID="+loadid+", LOADNAME="+loadname.toUpperCase());
								}
							}
						}
					}
					if(loadid.equals("")) continue;
				}
				callparams.put("LOADID", loadid);
				
				String priority = this.formattedFieldParamValue(":"+this.currentFlatFileImportProperty("PRIORITYIDFIELD"),selectedRowData, fileAliasDataSetColumns,filename);
				if(priority.equals("")||priority.equals(":"+this.currentFlatFileImportProperty("PRIORITYIDFIELD"))){
					priority=masterPriority;
				}
				
				if(priority.equals("")){
					priority="100";
				}
				callparams.put("PRIORITY", priority);
				
				
				/*if(sourceid.equals("")||sourceid.equals(":"+this.currentFlatFileImportProperty("SOURCEIDFIELD"))){
					sourceidparams.put("SOURCEID", "0");
					sourceidparams.put("SERVICEID", serviceid);
					sourceidparams.put("LOADID", loadid);
					synchronized (_flatFileLeadsImportQueueManager) {
						Database.executeDBRequest("FLATFILELEADSIMPORTER", "SELECT ISNULL(MAX([ID]),0) AS SOURCEID FROM <DBUSER>.[DYNAMICCALLERLIST] WHERE [SERVICEID]=:SERVICEID AND [LOADID]=:LOADID", sourceidparams);
						maxSourceID=sourceidparams.get("SOURCEID");
					}
					sourceid=String.valueOf(Long.parseLong(maxSourceID)+1);
				}*/
				//sourceid=String.valueOf(_flatFileLeadsImportQueueManager.adjustSourceID(Long.parseLong(serviceid),Long.parseLong(loadid),Long.parseLong(sourceid)));
				
				String phonenumfirst="";
				for(int phonenr=1;phonenr<=10;phonenr++){
					String phonenrfield="PHONE"+String.valueOf(phonenr)+"FIELD";
					String phonenum = this.formattedFieldParamValue(":["+this.currentFlatFileImportProperty(phonenrfield)+"]",selectedRowData, fileAliasDataSetColumns,filename);
					if(priority.equals("")||phonenum.equals(":"+this.currentFlatFileImportProperty(phonenrfield))){
						phonenum="";
					}
					if(!phonenum.equals("")){
						String newPhoneNum="";
						char prevcpn=0;
						for(char cpn:phonenum.toCharArray()){
							if("0123456789".indexOf(cpn)>-1&&"0123456789".indexOf(prevcpn)==-1){
								newPhoneNum+=cpn;
							}
						}
						phonenum=(newPhoneNum.startsWith("27")&&newPhoneNum.length()>=11?newPhoneNum.substring(newPhoneNum.length()-9):newPhoneNum);
					}
					if(!phonenum.equals("")){
						if(phonenumfirst.equals("")) phonenumfirst=phonenum;
					}
					callparams.put("PHONE"+String.valueOf(phonenr), phonenum);
					callparams.put("PHONE"+String.valueOf(phonenr)+"EXT", "");
				}
				
				if(phonenumfirst.equals("")){
					//TODO
					continue;
				}
				
				String callername=this.formattedFieldParamValue(this.currentFlatFileImportProperty("CALLERNAMEFIELDS"),selectedRowData, fileAliasDataSetColumns,filename);
				callparams.put("CALLERNAME", (callername.length()<40?callername:callername.substring(0, 40)));

				String comments=this.formattedFieldParamValue(this.currentFlatFileImportProperty("COMMENTSFIELDS"),selectedRowData, fileAliasDataSetColumns,filename);
				callparams.put("COMMENTS", (comments.length()<100?comments:comments.substring(0, 100)));
				
				String agentlogin=this.formattedFieldParamValue(":AGENTLOGIN",selectedRowData, fileAliasDataSetColumns,filename);
				if(agentlogin.equals(":AGENTLOGIN")){
					agentlogin="";
				}
				
				callparams.put("AGENTLOGINID",agentlogin);
				
				String scheduledcall=this.formattedFieldParamValue(":SCHEDULEDATE",selectedRowData, fileAliasDataSetColumns,filename);
				if(scheduledcall.equals(":SCHEDULEDATE")){
					scheduledcall="";
				}
				
				callparams.put("SCHEDULEDCALL",scheduledcall);
				
				for(int metafieldnr=1;metafieldnr<=20;metafieldnr++){
					String metafield="METAFIELD"+String.valueOf(metafieldnr);
					callparams.put(metafield,this.formattedFieldParamValue(this.currentFlatFileImportProperty(metafield),selectedRowData, fileAliasDataSetColumns,filename));
				}
				
				String loadrequesttype=this.formattedFieldParamValue(":["+this.currentFlatFileImportProperty("LEADREQUESTTYPEFIELD")+"]", selectedRowData, fileAliasDataSetColumns, filename).toUpperCase();
				loadrequesttype=loadrequesttype.equals("ADDCALL")?"1":loadrequesttype.equals("REMOVECALL")?"2":loadrequesttype.equals("MODIFYCALL")?"3":(",1,2,3").indexOf(","+loadrequesttype+",")>-1?loadrequesttype:"1";
				
				callparams.put("LOADREQUESTTYPE",(loadrequesttype.equals("")?"1":loadrequesttype));
				callparams.put("RECORDHANDLEFLAG", "1");
				callparams.put("FIELDSTOMODIFY", currentFlatFileImportProperty("FIELDSTOMODIFY"));
				
				String sourceid = this.formattedFieldParamValue(":"+this.currentFlatFileImportProperty("SOURCEIDFIELD"),selectedRowData, fileAliasDataSetColumns,filename);
				
				callparams.put("SOURCEID", sourceid);
				
				if(!this.currentFlatFileImportProperty("ALTERNATESOURCEIDSQLCOMMAND").equals("")){
					mergedMasterParams.putAll(callparams);
					String ALTERNATESOURCEIDSQLCOMMAND=this.currentFlatFileImportProperty("ALTERNATESOURCEIDSQLCOMMAND");
					
					if(ALTERNATESOURCEIDSQLCOMMAND.indexOf("CALLSOURCEID")>-1){
						mergedMasterParams.put("CALLSOURCEID", callparams.get("SOURCEID"));
						this.debug("Alternate Source ID Sql Command - SET INITIAL SOURCEID -"+mergedMasterParams.get("CALLSOURCEID"));
					}
					
					if(ALTERNATESOURCEIDSQLCOMMAND.indexOf("CALLLOADID")>-1){
						mergedMasterParams.put("CALLLOADID", callparams.get("LOADID"));
					}
					
					if(ALTERNATESOURCEIDSQLCOMMAND.indexOf("CALLSERVICEID")>-1){
						mergedMasterParams.put("CALLSERVICEID", callparams.get("SERVICEID"));
					}
					
					Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", ALTERNATESOURCEIDSQLCOMMAND, mergedMasterParams, this);
					if(mergedMasterParams.containsKey("CALLSOURCEID")){
						callparams.put("SOURCEID", mergedMasterParams.get("CALLSOURCEID"));
						this.debug("Alternate Source ID Sql Command - SET SOURCEID -"+mergedMasterParams.get("CALLSOURCEID"));
					}
					if(mergedMasterParams.containsKey("CALLLOADID")) callparams.put("LOADID", mergedMasterParams.get("CALLLOADID"));
					if(mergedMasterParams.containsKey("CALLSERVICEID")) callparams.put("SERVICEID", mergedMasterParams.get("CALLSERVICEID"));
					this.debug("Alternate Source ID Sql Command - [SERVICEID - "+ callparams.get("SERVICEID")+", LOADID - "+ callparams.get("LOADID")+",SOURCEID - "+ callparams.get("SOURCEID"));
					this.debug("Alternate Source ID Sql Command[MERGED] - [CALLSERVICEID - "+ (mergedMasterParams.containsKey("CALLSERVICEID")?mergedMasterParams.get("CALLSERVICEID"):"")+", CALLLOADID - "+ (mergedMasterParams.containsKey("CALLLOADID")?mergedMasterParams.get("CALLLOADEID"):"")+",CALLSOURCEID - "+ (mergedMasterParams.containsKey("CALLSOURCEID")?mergedMasterParams.get("CALLSOURCEID"):""));
				}
				if(callparams.get("SOURCEID").equals("")){
					continue;
				}
				
				try{
					
					//callparams.put("CALLEXISTCOUNT", "0");
					/*if(!callparams.get("METAFIELD1").equals("")){
						Database.executeDBRequest("FLATFILELEADSIMPORTER","SELECT COUNT(*) AS CALLEXISTCOUNT FROM <DBUSER>.DYNAMICCALLERLIST WHERE UPPER(METAFIELD1)=UPPER(:METAFIELD1)",callparams);
					}
					if(callparams.get("CALLEXISTCOUNT").equals("0")){
						Database.executeDBRequest("FLATFILELEADSIMPORTER", "INSERT INTO <DBUSER>.[DYNAMICCALLERLIST]([ID],[SERVICEID],[LOADID],[CALLERNAME],[PHONE1],[PHONE1EXT],[PHONE2],[PHONE2EXT],[PHONE3],[PHONE3EXT],[PHONE4],[PHONE4EXT],[PHONE5],[PHONE5EXT],[PHONE6],[PHONE6EXT],[PHONE7],[PHONE7EXT],[PHONE8],[PHONE8EXT],[PHONE9],[PHONE9EXT],[PHONE10],[PHONE10EXT],[SCHEDULEDCALL],[COMMENTS],[LOADREQUESTTYPE],[RECORDHANDLEFLAG],[PRIORITY],[AGENTLOGINID],[LASTACTIONDATETIME],[METAFIELD1],[METAFIELD2],[METAFIELD3],[METAFIELD4],[METAFIELD5],[METAFIELD6],[METAFIELD7],[METAFIELD8],[METAFIELD9],[METAFIELD10],[METAFIELD11],[METAFIELD12],[METAFIELD13],[METAFIELD14],[METAFIELD15],[METAFIELD16],[METAFIELD17],[METAFIELD18],[METAFIELD19],[METAFIELD20],[FIELDSTOMODIFY]) VALUES (:SOURCEID,:SERVICEID,:LOADID,:CALLERNAME,:PHONE1,:PHONE1EXT,:PHONE2,:PHONE2EXT,:PHONE3,:PHONE3EXT,:PHONE4,:PHONE4EXT,:PHONE5,:PHONE5EXT,:PHONE6,:PHONE6EXT,:PHONE7,:PHONE7EXT,:PHONE8,:PHONE8EXT,:PHONE9,:PHONE9EXT,:PHONE10,:PHONE10EXT,"+(callparams.get("SCHEDULEDCALL").equals("")?"NULL":":SCHEDULEDCALL")+",:COMMENTS,:LOADREQUESTTYPE,:RECORDHANDLEFLAG,:PRIORITY,:AGENTLOGINID,GETDATE(),:METAFIELD1,:METAFIELD2,:METAFIELD3,:METAFIELD4,:METAFIELD5,:METAFIELD6,:METAFIELD7,:METAFIELD8,:METAFIELD9,:METAFIELD10,:METAFIELD11,:METAFIELD12,:METAFIELD13,:METAFIELD14,:METAFIELD15,:METAFIELD16,:METAFIELD17,:METAFIELD18,:METAFIELD19,:METAFIELD20,:FIELDSTOMODIFY)", callparams);
					}
					else{
						this.debug("WARNING Duplicate record found but not inserted [METAFIELD1="+callparams.get("METAFIELD1")+"][METAFIELD19="+callparams.get("METAFIELD19")+"][METAFIELD20="+callparams.get("METAFIELD20")+"]");
					}*/
					if(recordProcessed==0){
						ArrayList<String> callParamsColumns=new ArrayList<String>();
						for(String callParamsColumn:callparams.keySet()){
							callParamsColumns.add(callParamsColumn.toUpperCase());
						}
						fileFinalImportDataSet.put(0, callParamsColumns);
					}
					
					ArrayList<String> callParamsData=new ArrayList<String>();
					for(int callParamColIndex=0;callParamColIndex<fileFinalImportDataSet.get(0).size();callParamColIndex++){
						callParamsData.add(callparams.get(callparams.keySet().toArray()[callParamColIndex]));
					}
					recordProcessed++;
					recordPocessedRef++;
					fileFinalImportDataSet.put(recordPocessedRef, callParamsData);
					if(recordPocessedRef==1000){
						TreeMap<Integer,ArrayList<String>> nextCallsBatch=new TreeMap<Integer, ArrayList<String>>();
						nextCallsBatch.put((Integer)0,new ArrayList<String>(fileFinalImportDataSet.get((Integer)0)));
						recordPocessedRef=1;
						while(recordPocessedRef<=1000){
							nextCallsBatch.put((Integer)recordPocessedRef,new ArrayList<String>(fileFinalImportDataSet.remove((Integer)recordPocessedRef++)));
						}
						recordPocessedRef=0;
						_flatFileLeadsImportQueueManager.appendCallImportDataset(nextCallsBatch);
					}
				}
				catch(Exception e){
					this.debug("ERROR Importing record ["+_flatFileAlias+":"+filename+"]:"+e.getMessage());
					e.printStackTrace();
				}
				
				callparams.clear();
			}
			
			/*if(!fileFinalImportDataSet.isEmpty()){
				HashMap<String,String> currentCallData=new HashMap<String,String>();
				for(int rowindex:fileFinalImportDataSet.keySet()){
					if(rowindex==0) continue;
					currentCallData.clear();
					currentCallData.putAll(Database.rowData(fileFinalImportDataSet, rowindex));
					currentCallData.put("CALLEXISTCOUNT", "0");
					
					if(currentCallData.get("SOURCEID").equals("0")){
						Database.
					}
					
					if(!currentCallData.get("METAFIELD1").equals("")){
						Database.executeDBRequest("FLATFILELEADSIMPORTER","SELECT COUNT(*) AS CALLEXISTCOUNT FROM <DBUSER>.DYNAMICCALLERLIST WHERE UPPER(METAFIELD1)=UPPER(:METAFIELD1)",currentCallData);
					}
					if(currentCallData.get("CALLEXISTCOUNT").equals("0")){
						Database.executeDBRequest("FLATFILELEADSIMPORTER", "INSERT INTO <DBUSER>.[DYNAMICCALLERLIST]([ID],[SERVICEID],[LOADID],[CALLERNAME],[PHONE1],[PHONE1EXT],[PHONE2],[PHONE2EXT],[PHONE3],[PHONE3EXT],[PHONE4],[PHONE4EXT],[PHONE5],[PHONE5EXT],[PHONE6],[PHONE6EXT],[PHONE7],[PHONE7EXT],[PHONE8],[PHONE8EXT],[PHONE9],[PHONE9EXT],[PHONE10],[PHONE10EXT],[SCHEDULEDCALL],[COMMENTS],[LOADREQUESTTYPE],[RECORDHANDLEFLAG],[PRIORITY],[AGENTLOGINID],[LASTACTIONDATETIME],[METAFIELD1],[METAFIELD2],[METAFIELD3],[METAFIELD4],[METAFIELD5],[METAFIELD6],[METAFIELD7],[METAFIELD8],[METAFIELD9],[METAFIELD10],[METAFIELD11],[METAFIELD12],[METAFIELD13],[METAFIELD14],[METAFIELD15],[METAFIELD16],[METAFIELD17],[METAFIELD18],[METAFIELD19],[METAFIELD20],[FIELDSTOMODIFY]) VALUES (:SOURCEID,:SERVICEID,:LOADID,:CALLERNAME,:PHONE1,:PHONE1EXT,:PHONE2,:PHONE2EXT,:PHONE3,:PHONE3EXT,:PHONE4,:PHONE4EXT,:PHONE5,:PHONE5EXT,:PHONE6,:PHONE6EXT,:PHONE7,:PHONE7EXT,:PHONE8,:PHONE8EXT,:PHONE9,:PHONE9EXT,:PHONE10,:PHONE10EXT,"+(callparams.get("SCHEDULEDCALL").equals("")?"NULL":":SCHEDULEDCALL")+",:COMMENTS,:LOADREQUESTTYPE,:RECORDHANDLEFLAG,:PRIORITY,:AGENTLOGINID,GETDATE(),:METAFIELD1,:METAFIELD2,:METAFIELD3,:METAFIELD4,:METAFIELD5,:METAFIELD6,:METAFIELD7,:METAFIELD8,:METAFIELD9,:METAFIELD10,:METAFIELD11,:METAFIELD12,:METAFIELD13,:METAFIELD14,:METAFIELD15,:METAFIELD16,:METAFIELD17,:METAFIELD18,:METAFIELD19,:METAFIELD20,:FIELDSTOMODIFY)", currentCallData);
					}
					else{
						this.debug("WARNING Duplicate record found but not inserted [METAFIELD1="+currentCallData.get("METAFIELD1")+"][METAFIELD19="+callparams.get("METAFIELD19")+"][METAFIELD20="+callparams.get("METAFIELD20")+"]");
					}
				}
			}*/
			
			if(fileFinalImportDataSet.size()>1){
				_flatFileLeadsImportQueueManager.appendCallImportDataset(fileFinalImportDataSet);
			}
			this.debug("TOTAL records imported ["+_flatFileAlias+":"+filename+"] - "+recordProcessed);
			this.debug("END Importing File Dataset ["+_flatFileAlias+":"+filename+"]");
		}
		catch(Exception maine){
			this.debug("TOTAL records imported ["+_flatFileAlias+":"+filename+"] - "+recordProcessed);
			this.debug("ERROR ENDING Importing File Dataset ["+_flatFileAlias+":"+filename+"]:"+maine.getMessage());
			maine.printStackTrace();
			//TODO
		}
		sourceidparams.clear();
	}
	
	protected void readFileEntryData(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns){
		
	}
	
	private String _maxSourceID="0";
	private String _masterServiceid="";
	private String _masterLoadID="";
	private String _masterPriority="";
	
	private HashMap<String,String> _sourceidparams=new HashMap<String,String>();
	private HashMap<String,String> _callparams=new HashMap<String,String>();
	
	private boolean _usefilenameasloadname=false;
	private boolean _enablenewloadwhencreated=false;
	private boolean _forcecreatingload=false;
	
	private HashMap<String,String> _mergedMasterParams=new HashMap<String, String>();
	
	private int _recordProcessed=0;
	private int _recordPocessedRef=0;
	private TreeMap<Integer,ArrayList<String>> _fileFinalImportDataSet=new TreeMap<Integer,ArrayList<String>>();
	private String _filename="";
	
	private void importFileDatasetFromFileStream(String filename,InputStream fin) {
		this._filename=filename;
		
		//TreeMap<Integer, ArrayList<String>> fileAliasFoundCSVDataSet=new TreeMap<Integer,ArrayList<String>>();
		
		//ArrayList<String> fileAliasDataSetColumns=fileAliasFoundCSVDataSet.get(0);
		
		_sourceidparams.clear();
		_callparams.clear();
		
		_usefilenameasloadname=this.currentFlatFileImportProperty("USEFILENAMEASLOADNAME").equals("TRUE");
		_enablenewloadwhencreated=this.currentFlatFileImportProperty("ENABLENEWLOAD").equals("TRUE");
		_forcecreatingload=this.currentFlatFileImportProperty("FORCECREATINGLOAD").equals("TRUE");
		
		_mergedMasterParams=new HashMap<String, String>();
		
		_recordProcessed=0;
		_recordPocessedRef=0;
		
		this.debug("START Importing File Dataset ["+_flatFileAlias+":"+filename+"]");
				
		try{
			//this.debug("TOTAL Records IN File Dataset ["+_flatFileAlias+":"+filename+"] - "+(fileAliasFoundCSVDataSet.size()-1));
			_maxSourceID="0";
			
			_masterServiceid=this.currentFlatFileImportProperty("DEFAULTSERVICEID");
			_masterLoadID=this.currentFlatFileImportProperty("DEFAULTLOADID");
			_masterPriority=this.currentFlatFileImportProperty("DEFAULTPRIORITY");
			if(_masterPriority.equals("")||_masterPriority.equals("0")) _masterPriority="100";
			
			_fileFinalImportDataSet=new TreeMap<Integer,ArrayList<String>>();
			
			this.readRowsFromStream(fin, "CSV", null, this, "readRowData", "COLDELIM="+this._currentFlatFileImportProperties.get("COMADELIM"));
			
			/*for(int rowindex:fileAliasFoundCSVDataSet.keySet()){
				if(rowindex==0) fileAliasDataSetColumns=fileAliasFoundCSVDataSet.get(0);
				this.readRowData(rowindex,fileAliasDataSetColumns,fileAliasFoundCSVDataSet.get((Integer)rowindex));
			}*/
			
			if(_fileFinalImportDataSet.size()>1){
				_flatFileLeadsImportQueueManager.appendCallImportDataset(_fileFinalImportDataSet);
			}
			this.debug("TOTAL records imported ["+_flatFileAlias+":"+filename+"] - "+_recordProcessed);
			this.debug("END Importing File Dataset ["+_flatFileAlias+":"+filename+"]");
		}
		catch(Exception maine){
			this.debug("TOTAL records imported ["+_flatFileAlias+":"+filename+"] - "+_recordProcessed);
			this.debug("ERROR ENDING Importing File Dataset ["+_flatFileAlias+":"+filename+"]:"+maine.getMessage());
			maine.printStackTrace();
			//TODO
		}
		_sourceidparams.clear();
	}
	
	private ArrayList<String> _fileAliasDataSetColumns=null;
	
	public void readRowData(Integer rowindex,ArrayList<String> rowColumns,ArrayList<String> rowData){
		if(rowindex==0){
			if(_fileAliasDataSetColumns!=null){
				_fileAliasDataSetColumns.clear();
				_fileAliasDataSetColumns=null;
			}
			_fileAliasDataSetColumns=new ArrayList<String>(rowColumns);
			return;
		}
		
		if(_recordProcessed % 1000 == 0){
			this.debug("Records IN File Dataset ["+_flatFileAlias+":"+_filename+"] processed thus far - "+_recordProcessed);
		}
		_recordProcessed++;
		
		_callparams.clear();
		
		ArrayList<String> selectedRowData=rowData;
		
		String serviceid = this.formattedFieldParamValue(":"+this.currentFlatFileImportProperty("SERVICEIDFIELD"),selectedRowData, _fileAliasDataSetColumns,_filename);
		_mergedMasterParams.clear();
		_mergedMasterParams.putAll(Database.rowData(_fileAliasDataSetColumns, selectedRowData));
		try{
			if(serviceid.equals("")||serviceid.equals(_masterServiceid)){// serviceid.equals(":"+this.currentFlatFileImportProperty("SERVICEIDFIELD"))){
				serviceid=(PresenceDB.serviceExist(_masterServiceid)?_masterServiceid:"");
			}
			else{
				if(!PresenceDB.serviceExist(serviceid)){
					if(!PresenceDB.serviceExist(serviceid=_masterServiceid)){
						serviceid="";
					}
				}
			}
		}
		catch(Exception svre){
			//TODO
			return;
		}
		if(serviceid.equals("")){
			return;
		}
		
		_callparams.put("SERVICEID", serviceid);
		
		String loadid = this.formattedFieldParamValue(":"+this.currentFlatFileImportProperty("LOADIDFIELD"),selectedRowData, _fileAliasDataSetColumns,_filename);
		if(loadid.equals("")||loadid.equals(":"+this.currentFlatFileImportProperty("LOADIDFIELD"))){
			loadid=_masterLoadID;
		}
		
		if((loadid=loadid.equals("0")?"":loadid).equals("")||_forcecreatingload){
			if(_forcecreatingload){
				String loadname="";
				if(_usefilenameasloadname){
					loadname=_filename.toUpperCase();
				}
				else{
					loadname=this.formattedFieldParamValue(this.currentFlatFileImportProperty("LOADNAMEMASK"),selectedRowData, _fileAliasDataSetColumns,_filename).toUpperCase();
				}
				
				if(loadname.length()>50){
					loadname=loadname.substring(0,50).toUpperCase();
				}
				
				try {
					if(PresenceDB.serviceLoadExist(serviceid,loadid, loadname.toUpperCase())){
						loadid=(loadid.equals("")?PresenceDB.serviceLoadIDByLoadDescription(serviceid, loadname.toUpperCase()):loadid);
					}
					else{
						if(PresenceDB.createServiceLoad(serviceid, loadid, loadname.toUpperCase(), _enablenewloadwhencreated, Integer.parseInt(_masterPriority))){
							loadid=(loadid.equals("")?PresenceDB.serviceLoadIDByLoadDescription(serviceid, loadname.toUpperCase()):loadid);
							inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("PRESENCE LOAD CREATED - [SERVICEID="+serviceid+", LOADID="+loadid+", LOADNAME="+loadname.toUpperCase());
						}
						else{
							if(_masterLoadID.equals("")||_masterLoadID.equals("0")){
								_masterLoadID="0";
							}
							if(PresenceDB.createServiceLoad(serviceid=_masterServiceid, loadid, loadname, _enablenewloadwhencreated, Integer.parseInt(_masterPriority))){
								if(_masterLoadID.equals("0")){
									loadid=(loadid.equals("")?PresenceDB.serviceLoadIDByLoadDescription(serviceid, loadname.toUpperCase()):loadid);
								}
								inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("PRESENCE LOAD CREATED - [SERVICEID="+serviceid+", LOADID="+loadid+", LOADNAME="+loadname.toUpperCase());
							}
							else{
								inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("FAILED CREATING PRESENCE LOAD - [SERVICEID="+serviceid+", LOADID="+loadid+", LOADNAME="+loadname.toUpperCase());
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
			}
			if(loadid.equals("")||loadid.equals("0")) return;
		}
		else{
			if(_forcecreatingload){
				String loadname="";
				if(_usefilenameasloadname){
					loadname=_filename.toUpperCase();
				}
				else{
					loadname=_flatFileAlias.toUpperCase();
				}
				
				if(loadname.length()>50){
					loadname=loadname.substring(0,50);
				}
				
				try {
					if(PresenceDB.serviceLoadExist(serviceid,loadid, loadname.toUpperCase())){
						loadid=PresenceDB.serviceLoadIDByLoadDescription(serviceid, loadname.toUpperCase());
					}
					else{
						if(PresenceDB.createServiceLoad(_masterServiceid, loadid, loadname.toUpperCase(), _enablenewloadwhencreated, Integer.parseInt(_masterPriority))){
							loadid=PresenceDB.serviceLoadIDByLoadDescription(serviceid, loadname.toUpperCase());
							inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("PRESENCE LOAD CREATED - [SERVICEID="+serviceid+", LOADID="+loadid+", LOADNAME="+loadname.toUpperCase());
						}
						else{
							if(PresenceDB.createServiceLoad(_masterServiceid, loadid=_masterLoadID, loadname.toUpperCase(), _enablenewloadwhencreated, Integer.parseInt(_masterPriority))){
								loadid=PresenceDB.serviceLoadIDByLoadDescription(serviceid, loadname.toUpperCase());
								inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("PRESENCE LOAD CREATED - [SERVICEID="+serviceid+", LOADID="+loadid+", LOADNAME="+loadname.toUpperCase());
							}
							else{
								inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("FAILED CREATING PRESENCE LOAD - [SERVICEID="+serviceid+", LOADID="+loadid+", LOADNAME="+loadname.toUpperCase());
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
			}
			if(loadid.equals("")) return;
		}
		_callparams.put("LOADID", loadid);
		
		String priority = this.formattedFieldParamValue(":"+this.currentFlatFileImportProperty("PRIORITYIDFIELD"),selectedRowData, _fileAliasDataSetColumns,_filename);
		if(priority.equals("")||priority.equals(":"+this.currentFlatFileImportProperty("PRIORITYIDFIELD"))){
			priority=_masterPriority;
		}
		
		if(priority.equals("")){
			priority="100";
		}
		_callparams.put("PRIORITY", priority);
		
		String phonenumfirst="";
		for(int phonenr=1;phonenr<=10;phonenr++){
			String phonenrfield="PHONE"+String.valueOf(phonenr)+"FIELD";
			String phonenum = this.formattedFieldParamValue(":["+this.currentFlatFileImportProperty(phonenrfield)+"]",selectedRowData, _fileAliasDataSetColumns,_filename);
			if(priority.equals("")||phonenum.equals(":"+this.currentFlatFileImportProperty(phonenrfield))){
				phonenum="";
			}
			if(!phonenum.equals("")){
				String newPhoneNum="";
				char prevcpn=0;
				for(char cpn:phonenum.toCharArray()){
					if("0123456789".indexOf(cpn)>-1&&"0123456789".indexOf(prevcpn)==-1){
						newPhoneNum+=cpn;
					}
				}
				phonenum=(newPhoneNum.startsWith("27")&&newPhoneNum.length()>=11?newPhoneNum.substring(newPhoneNum.length()-9):newPhoneNum);
			}
			if(!phonenum.equals("")){
				if(phonenumfirst.equals("")) phonenumfirst=phonenum;
			}
			_callparams.put("PHONE"+String.valueOf(phonenr), phonenum);
			_callparams.put("PHONE"+String.valueOf(phonenr)+"EXT", "");
		}
		
		if(phonenumfirst.equals("")){
			//TODO
			return;
		}
		
		String callername=this.formattedFieldParamValue(this.currentFlatFileImportProperty("CALLERNAMEFIELDS"),selectedRowData, _fileAliasDataSetColumns,_filename);
		_callparams.put("CALLERNAME", (callername.length()<40?callername:callername.substring(0, 40)));

		String comments=this.formattedFieldParamValue(this.currentFlatFileImportProperty("COMMENTSFIELDS"),selectedRowData, _fileAliasDataSetColumns,_filename);
		_callparams.put("COMMENTS", (comments.length()<100?comments:comments.substring(0, 100)));
		
		String agentlogin=this.formattedFieldParamValue(":AGENTLOGIN",selectedRowData, _fileAliasDataSetColumns,_filename);
		if(agentlogin.equals(":AGENTLOGIN")){
			agentlogin="";
		}
		
		_callparams.put("AGENTLOGINID",agentlogin);
		
		String scheduledcall=this.formattedFieldParamValue(":SCHEDULEDATE",selectedRowData, _fileAliasDataSetColumns,_filename);
		if(scheduledcall.equals(":SCHEDULEDATE")){
			scheduledcall="";
		}
		
		_callparams.put("SCHEDULEDCALL",scheduledcall);
		
		for(int metafieldnr=1;metafieldnr<=20;metafieldnr++){
			String metafield="METAFIELD"+String.valueOf(metafieldnr);
			_callparams.put(metafield,this.formattedFieldParamValue(this.currentFlatFileImportProperty(metafield),selectedRowData, _fileAliasDataSetColumns,_filename));
		}
		
		String loadrequesttype=this.formattedFieldParamValue(":["+this.currentFlatFileImportProperty("LEADREQUESTTYPEFIELD")+"]", selectedRowData, _fileAliasDataSetColumns, _filename).toUpperCase();
		loadrequesttype=loadrequesttype.equals("ADDCALL")?"1":loadrequesttype.equals("REMOVECALL")?"2":loadrequesttype.equals("MODIFYCALL")?"3":(",1,2,3").indexOf(","+loadrequesttype+",")>-1?loadrequesttype:"1";
		
		_callparams.put("LOADREQUESTTYPE",(loadrequesttype.equals("")?"1":loadrequesttype));
		_callparams.put("RECORDHANDLEFLAG", "1");
		_callparams.put("FIELDSTOMODIFY", currentFlatFileImportProperty("FIELDSTOMODIFY"));
		
		String sourceid = this.formattedFieldParamValue(":"+this.currentFlatFileImportProperty("SOURCEIDFIELD"),selectedRowData, _fileAliasDataSetColumns,_filename);
		
		_callparams.put("SOURCEID", sourceid);
		
		if(!this.currentFlatFileImportProperty("ALTERNATESOURCEIDSQLCOMMAND").equals("")){
			_mergedMasterParams.putAll(_callparams);
			String ALTERNATESOURCEIDSQLCOMMAND=this.currentFlatFileImportProperty("ALTERNATESOURCEIDSQLCOMMAND");
			
			if(ALTERNATESOURCEIDSQLCOMMAND.indexOf("CALLSOURCEID")>-1){
				_mergedMasterParams.put("CALLSOURCEID", _callparams.get("SOURCEID"));
				this.debug("Alternate Source ID Sql Command - SET INITIAL SOURCEID -"+_mergedMasterParams.get("CALLSOURCEID"));
			}
			
			if(ALTERNATESOURCEIDSQLCOMMAND.indexOf("CALLLOADID")>-1){
				_mergedMasterParams.put("CALLLOADID", _callparams.get("LOADID"));
			}
			
			if(ALTERNATESOURCEIDSQLCOMMAND.indexOf("CALLSERVICEID")>-1){
				_mergedMasterParams.put("CALLSERVICEID", _callparams.get("SERVICEID"));
			}
			
			try {
				Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", ALTERNATESOURCEIDSQLCOMMAND, _mergedMasterParams, this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			if(_mergedMasterParams.containsKey("CALLSOURCEID")){
				_callparams.put("SOURCEID", _mergedMasterParams.get("CALLSOURCEID"));
				this.debug("Alternate Source ID Sql Command - SET SOURCEID -"+_mergedMasterParams.get("CALLSOURCEID"));
			}
			if(_mergedMasterParams.containsKey("CALLLOADID")) _callparams.put("LOADID", _mergedMasterParams.get("CALLLOADID"));
			if(_mergedMasterParams.containsKey("CALLSERVICEID")) _callparams.put("SERVICEID", _mergedMasterParams.get("CALLSERVICEID"));
			this.debug("Alternate Source ID Sql Command - [SERVICEID - "+ _callparams.get("SERVICEID")+", LOADID - "+ _callparams.get("LOADID")+",SOURCEID - "+ _callparams.get("SOURCEID"));
			this.debug("Alternate Source ID Sql Command[MERGED] - [CALLSERVICEID - "+ (_mergedMasterParams.containsKey("CALLSERVICEID")?_mergedMasterParams.get("CALLSERVICEID"):"")+", CALLLOADID - "+ (_mergedMasterParams.containsKey("CALLLOADID")?_mergedMasterParams.get("CALLLOADEID"):"")+",CALLSOURCEID - "+ (_mergedMasterParams.containsKey("CALLSOURCEID")?_mergedMasterParams.get("CALLSOURCEID"):""));
		}
		if(_callparams.get("SOURCEID").equals("")){
			return;
		}
		
		try{
			
			//callparams.put("CALLEXISTCOUNT", "0");
			/*if(!callparams.get("METAFIELD1").equals("")){
				Database.executeDBRequest("FLATFILELEADSIMPORTER","SELECT COUNT(*) AS CALLEXISTCOUNT FROM <DBUSER>.DYNAMICCALLERLIST WHERE UPPER(METAFIELD1)=UPPER(:METAFIELD1)",callparams);
			}
			if(callparams.get("CALLEXISTCOUNT").equals("0")){
				Database.executeDBRequest("FLATFILELEADSIMPORTER", "INSERT INTO <DBUSER>.[DYNAMICCALLERLIST]([ID],[SERVICEID],[LOADID],[CALLERNAME],[PHONE1],[PHONE1EXT],[PHONE2],[PHONE2EXT],[PHONE3],[PHONE3EXT],[PHONE4],[PHONE4EXT],[PHONE5],[PHONE5EXT],[PHONE6],[PHONE6EXT],[PHONE7],[PHONE7EXT],[PHONE8],[PHONE8EXT],[PHONE9],[PHONE9EXT],[PHONE10],[PHONE10EXT],[SCHEDULEDCALL],[COMMENTS],[LOADREQUESTTYPE],[RECORDHANDLEFLAG],[PRIORITY],[AGENTLOGINID],[LASTACTIONDATETIME],[METAFIELD1],[METAFIELD2],[METAFIELD3],[METAFIELD4],[METAFIELD5],[METAFIELD6],[METAFIELD7],[METAFIELD8],[METAFIELD9],[METAFIELD10],[METAFIELD11],[METAFIELD12],[METAFIELD13],[METAFIELD14],[METAFIELD15],[METAFIELD16],[METAFIELD17],[METAFIELD18],[METAFIELD19],[METAFIELD20],[FIELDSTOMODIFY]) VALUES (:SOURCEID,:SERVICEID,:LOADID,:CALLERNAME,:PHONE1,:PHONE1EXT,:PHONE2,:PHONE2EXT,:PHONE3,:PHONE3EXT,:PHONE4,:PHONE4EXT,:PHONE5,:PHONE5EXT,:PHONE6,:PHONE6EXT,:PHONE7,:PHONE7EXT,:PHONE8,:PHONE8EXT,:PHONE9,:PHONE9EXT,:PHONE10,:PHONE10EXT,"+(callparams.get("SCHEDULEDCALL").equals("")?"NULL":":SCHEDULEDCALL")+",:COMMENTS,:LOADREQUESTTYPE,:RECORDHANDLEFLAG,:PRIORITY,:AGENTLOGINID,GETDATE(),:METAFIELD1,:METAFIELD2,:METAFIELD3,:METAFIELD4,:METAFIELD5,:METAFIELD6,:METAFIELD7,:METAFIELD8,:METAFIELD9,:METAFIELD10,:METAFIELD11,:METAFIELD12,:METAFIELD13,:METAFIELD14,:METAFIELD15,:METAFIELD16,:METAFIELD17,:METAFIELD18,:METAFIELD19,:METAFIELD20,:FIELDSTOMODIFY)", callparams);
			}
			else{
				this.debug("WARNING Duplicate record found but not inserted [METAFIELD1="+callparams.get("METAFIELD1")+"][METAFIELD19="+callparams.get("METAFIELD19")+"][METAFIELD20="+callparams.get("METAFIELD20")+"]");
			}*/
			
			_flatFileLeadsImportQueueManager.importCallData(_callparams);
			
			/*
			if(_recordProcessed==0){
				ArrayList<String> callParamsColumns=new ArrayList<String>();
				for(String callParamsColumn:_callparams.keySet()){
					callParamsColumns.add(callParamsColumn.toUpperCase());
				}
				_fileFinalImportDataSet.put(0, callParamsColumns);
			}
			
			ArrayList<String> callParamsData=new ArrayList<String>();
			for(int callParamColIndex=0;callParamColIndex<_fileFinalImportDataSet.get(0).size();callParamColIndex++){
				callParamsData.add(_callparams.get(_callparams.keySet().toArray()[callParamColIndex]));
			}
			
			_recordPocessedRef++;
			_fileFinalImportDataSet.put(_recordPocessedRef, callParamsData);
			if(_recordPocessedRef==1000){
				TreeMap<Integer,ArrayList<String>> nextCallsBatch=new TreeMap<Integer, ArrayList<String>>();
				nextCallsBatch.put((Integer)0,new ArrayList<String>(_fileFinalImportDataSet.get((Integer)0)));
				_recordPocessedRef=1;
				while(_recordPocessedRef<=1000){
					nextCallsBatch.put((Integer)_recordPocessedRef,new ArrayList<String>(_fileFinalImportDataSet.remove((Integer)_recordPocessedRef++)));
				}
				_recordPocessedRef=0;
				_flatFileLeadsImportQueueManager.appendCallImportDataset(nextCallsBatch);
			}*/
		}
		catch(Exception e){
			this.debug("ERROR Importing record ["+_flatFileAlias+":"+_filename+"]:"+e.getMessage());
			e.printStackTrace();
		}
		
		_callparams.clear();
	}

	private String currentFlatFileImportProperty(String propname) {
		propname=propname.toUpperCase();
		if(_currentFlatFileImportProperties.containsKey(propname)){
			return _currentFlatFileImportProperties.get(propname);
		}
		return "";
	}
}
