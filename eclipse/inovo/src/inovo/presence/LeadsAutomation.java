package inovo.presence;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import presence.Administrator;
import inovo.db.Database;
import inovo.json.simple.JSONArray;
import inovo.json.simple.JSONObject;
import inovo.servlet.InovoServletContextListener;

public class LeadsAutomation {
	
	private boolean _shutdownQueue=false;
	private boolean _canDBReloadServices=true;
	
	private static LeadsAutomation _leadsAutomation=null;
	private ArrayList<String> _registeredCallTaskKeys=new ArrayList<String>();
	private ArrayList<String> _callTasksKeysCompleted=new ArrayList<String>();
	private ArrayList<String> _serviceIDsToReload=new ArrayList<String>();
	private String _presenceServerIP="";
	private String _environmentPath="";
	
	public static boolean _queueEnabled=false;
	public LeadsAutomation(String environmentPath,String presenceServerIP,boolean queueEnabled){
		this._presenceServerIP=presenceServerIP;
		this._environmentPath=environmentPath;
		this._queueEnabled=queueEnabled;
	}
	
	private ArrayList<String> _environmentChecklist=new ArrayList<String>();
	public synchronized ArrayList<String> environmentChecklist(){
		return _environmentChecklist;
	}
	
	private boolean environmentSetupCheck(){
		ArrayList<String> environmentChecklist=new ArrayList<String>();
		try {
			Database.executeDBRequest(null, "LEADSAUTOMATION", "SELECT GETDATE() AS SYSDATE", null, null);
		} catch (Exception e) {
			environmentChecklist.add("DB CONNECTION ERROR:"+e.getMessage());
		}
		if(environmentChecklist.isEmpty()){
			try {
				Database.executeDBRequest(null, "LEADSAUTOMATION", "SELECT TOP 1 * FROM <DBUSER>.DYNAMICCALLERLIST", null, null);
			} catch (Exception e) {
				environmentChecklist.add("DB SETUP ERROR: "+e.getMessage());
				environmentChecklist.add("DB SETUP ERROR: POSSIBLE SOLUTION - RUN SQL INSTALL SCRIPT");
			}
		}
		boolean envCheckStatus=environmentChecklist.isEmpty();
		if(!envCheckStatus){
			for(String environmenterr:environmentChecklist){
				inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug(environmenterr);
			}
			synchronized (_environmentChecklist) {
				_environmentChecklist.clear();
				_environmentChecklist.addAll(environmentChecklist);
			}
			environmentChecklist.clear();
			environmentChecklist=null;
			try {
				Thread.sleep(30*1014);
			} catch (InterruptedException e) {
			}
		}
		return envCheckStatus;
	}
	
	public void initiateAutomation(){
		new Thread(){
			@Override
			public void run() {
				while(!_shutdownQueue){
					if(_queueEnabled){
						if(environmentSetupCheck()){
							environmentChecklist();
							loadCalls();
						}
					}
					try {
						this.sleep(2*1024);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		new Thread(){
			@Override
			public void run() {
				while(!_shutdownQueue){
					if(_queueEnabled){
						cleanUpCallRequests();
					}
					try {
						this.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(_queueEnabled){
					cleanUpCallRequests();
				}
			};
		}.start();
		
		new Thread(){
			@Override
			public void run() {
				while(!_shutdownQueue&&_canDBReloadServices){
					if(_queueEnabled){
						try {
							this.sleep(10*1024);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if(_canDBReloadServices&&_queueEnabled){
						try {
							initFromDbReoadServiceRequests();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							_canDBReloadServices=false;
						}
					}
					else{
						_canDBReloadServices=false;
						break;
					}
				}
				if(_queueEnabled&&_canDBReloadServices){
					try {
						initFromDbReoadServiceRequests();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						_canDBReloadServices=false;
					}
				}
			};
		}.start();
	}
	
	private ArrayList<String> _initFromDbReloadService=new ArrayList<String>();
	private void initFromDbReoadServiceRequests() throws Exception{
		_initFromDbReloadService.clear();
		Database.executeDBRequest(null, "LEADSAUTOMATION", "SELECT SERVICEID ,LASTACTIONDATETIME FROM <DBUSER>.SERVICE_BUFFER_RELOAD WHERE DATEADD(SECOND,-30,LASTACTIONDATETIME)<GETDATE()", null,this, "initFromDbReoadServiceRequests_data");
		String svrids="";
		if(!_initFromDbReloadService.isEmpty()){
			this.reloadServices(_initFromDbReloadService);
			while(!_initFromDbReloadService.isEmpty()){
				svrids=_initFromDbReloadService.remove(0)+(_initFromDbReloadService.isEmpty()?"":",");
			}
			Database.executeDBRequest(null, "LEADSAUTOMATION", "DELETE FROM <DBUSER>.SERVICE_BUFFER_RELOAD WHERE SERVICEID IN ("+svrids+")", null,null);
		}
	}
	
	public void initFromDbReoadServiceRequests_data(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex>0){
			_initFromDbReloadService.add(data.get(0));
		}
	}
	
	private void cleanUpCallRequests(){
		boolean hasCallKeysToRemove=false;
		synchronized (_callTasksKeysCompleted) {
			hasCallKeysToRemove=!_callTasksKeysCompleted.isEmpty();
		}
		if(hasCallKeysToRemove){
			ArrayList<String> currentCallTasksKeysCompleted=new ArrayList<String>();
			synchronized (_callTasksKeysCompleted) {
				while(!_callTasksKeysCompleted.isEmpty()){
					currentCallTasksKeysCompleted.add(_callTasksKeysCompleted.remove(0));
				}
			}
			synchronized (_registeredCallTaskKeys) {
				while(!currentCallTasksKeysCompleted.isEmpty()){
					String callsTaskKey=currentCallTasksKeysCompleted.remove(0);
					_registeredCallTaskKeys.remove(callsTaskKey);
					System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())+" REMOVE TASK:"+callsTaskKey);
				}
			}
		}
		String svrIds="";
		synchronized (_serviceIDsToReload) {
			if(!_serviceIDsToReload.isEmpty()){
				String lastServiceIDAdded="";
				while(!_serviceIDsToReload.isEmpty()){
					String removedServiceID=_serviceIDsToReload.remove(0);
					if(lastServiceIDAdded.equals(removedServiceID)) continue;
					lastServiceIDAdded=removedServiceID;
					svrIds+=removedServiceID+(!_serviceIDsToReload.isEmpty()?",":"");
				}
			}
		}
		if(!svrIds.equals("")){
			//if(Administrator.administrator().connectedToPresence()){
				Administrator.administrator(this._environmentPath, this._presenceServerIP).reloadServicesBuffers(svrIds, true);
			//}
		}
	}
	
	private ExecutorService _leadsRequestsService=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*10);
	
	public void loadCalls(){
		try{
			Database.executeDBRequest(null,"LEADSAUTOMATION", "UPDATE <DBUSER>.[DYNAMICCALLERLIST] SET [RECORDHANDLEFLAG]=2,LASTACTIONDATETIME=NOW() WHERE [RECORDHANDLEFLAG]=1", null,null);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		TreeMap<Integer,ArrayList<Object>> loadsCallsSet=new TreeMap<Integer, ArrayList<Object>>();
		try{
			
			Database.executeDBRequest(loadsCallsSet,"LEADSAUTOMATION", "SELECT DISTINCT [SERVICEID],[LOADID],[LOADREQUESTTYPE] FROM <DBUSER>.[DYNAMICCALLERLIST] WHERE [RECORDHANDLEFLAG]=2", null,null);
			if(loadsCallsSet!=null){
				
				HashMap<String,Object> loadCallsParams=new HashMap<String,Object>();
				ArrayList<Thread> leadRequestThreads=new ArrayList<Thread>();
				ArrayList<Runnable> leadsRequestsToRun=new ArrayList<Runnable>();
				int totalTasksAdded=0;
				synchronized (_registeredCallTaskKeys) {
					for(int rowindex:loadsCallsSet.keySet()){
						if(rowindex==0) continue;
						loadCallsParams.clear();
						loadCallsParams.putAll(Database.rowData(loadsCallsSet, rowindex));
						String callsTaskKey=loadCallsParams.get("SERVICEID")+"|"+loadCallsParams.get("LOADID")+"|"+loadCallsParams.get("LOADREQUESTTYPE");
												
						if(_registeredCallTaskKeys.indexOf(callsTaskKey)==-1){
							_registeredCallTaskKeys.add(callsTaskKey);
							System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())+" ADD TASK:"+callsTaskKey);
							if(loadCallsParams.get("LOADREQUESTTYPE").equals("1")){
								leadsRequestsToRun.add(new LeadsAutomationNewCallsTask(loadCallsParams));
								//leadRequestThreads.add(new Thread(new LeadsAutomationNewCallsTask(loadCallsParams)));
							}
							else if(loadCallsParams.get("LOADREQUESTTYPE").equals("2")){
								leadsRequestsToRun.add(new LeadsAutomationRemoveCallsTask(loadCallsParams));
								//leadRequestThreads.add(new Thread(new LeadsAutomationRemoveCallsTask(loadCallsParams)));
							}
							else if(loadCallsParams.get("LOADREQUESTTYPE").equals("3")){
								leadsRequestsToRun.add(new LeadsAutomationModifyCallsTask(loadCallsParams));
								//leadRequestThreads.add(new Thread(new LeadsAutomationModifyCallsTask(loadCallsParams)));
							}
							else if(loadCallsParams.get("LOADREQUESTTYPE").equals("4")){
								leadsRequestsToRun.add(new LeadsAutomationClearServiceLoad(loadCallsParams));
								//leadRequestThreads.add(new Thread(new LeadsAutomationClearServiceLoad(loadCallsParams)));
							}
							totalTasksAdded++;
						}
					}
				}
				
				if(totalTasksAdded>0){
					System.out.println("TOTAL TASKS ADDED - "+totalTasksAdded);
				}
				
				while(!leadsRequestsToRun.isEmpty()){
					_leadsRequestsService.execute(leadsRequestsToRun.remove(0));
				}
				
				//while(!leadRequestThreads.isEmpty()) leadRequestThreads.remove(0).start();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		Database.cleanupDataset(loadsCallsSet);
		loadsCallsSet=null;
	}
	int totalTasksRemoved=0;
	public void removeCallsTasksKey(String callsTaskKey){
		synchronized (_callTasksKeysCompleted){
			_callTasksKeysCompleted.add(callsTaskKey);
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())+" REMOVED TASK:"+callsTaskKey);
		}
		totalTasksRemoved++;
		System.out.println("TOTAL TASKS REMOVED - "+totalTasksRemoved);
	}
	
	public static LeadsAutomation leadsAutomation(String environmentPath,String presenceServerIP){
		return leadsAutomation(environmentPath, presenceServerIP, _queueEnabled);
	}
	
	public static LeadsAutomation leadsAutomation(String environmentPath,String presenceServerIP,boolean queueEnabled){
		if(_leadsAutomation==null) _leadsAutomation=new LeadsAutomation(environmentPath, presenceServerIP,queueEnabled);
		return _leadsAutomation;
	}
	
	public void shutdownAutomation(){
		this._shutdownQueue=true;
		this._leadsRequestsService.shutdown();
		
		while(!_registeredCallTaskKeys.isEmpty()){
			try {
				Thread.sleep(1024);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String loadCall(HashMap<String, Object> dyncalldata){
		InovoServletContextListener.inovoServletListener().logDebug("LeadsAutomation.loadCall("+dyncalldata.toString()+")");
		//System.out.println("LeadsAutomation.loadCall("+dyncalldata.toString()+")");
		StringBuilder loadCallResponse=new StringBuilder();
		JSONObject jsonobj=new JSONObject();
		JSONArray jsondyncalldataErrors=null;
		jsonobj.put("ERRORS",jsondyncalldataErrors = new JSONArray());
		ArrayList<String> invalidparams=new ArrayList<String>();
		try{
			JSONObject jsonobjparams=null;
			jsonobj.put("LOADCALLPARAMS", jsonobjparams=new JSONObject());
			for(String dyncalldatakey:dyncalldata.keySet()){
				if("loadrequesttype,serviceid,loadid,sourceid,callername,comments,agentloginid,scheduleddate,scheduledate,priority,phone1,phone2,phone3,phone4,phone5,phone6,phone7,phone8,phone9,phone10,metafield1,metafield2,metafield3,metafield4,metafield5,metafield6,metafield7,metafield8,metafield9,metafield10,metafield11,metafield12,metafield13,metafield14,metafield16,metafield17,metafield18,metafield19,metafield20,fieldstomodify,".toUpperCase().contains((dyncalldatakey.toUpperCase()+","))){
					jsonobjparams.put(dyncalldatakey.toUpperCase(), dyncalldata.get(dyncalldatakey));
				}
				else{
					invalidparams.add(dyncalldatakey);
				}
			}
			if(jsondyncalldataErrors.isEmpty()){
				String loadRequestType=((String)jsonobjparams.get("LOADREQUESTTYPE")).toUpperCase().trim();
				loadRequestType=(loadRequestType==null?"":loadRequestType.equals("ADDCALL")?"1":loadRequestType.equals("REMOVECALL")?"2":loadRequestType.equals("MODIFYCALL")?"3":loadRequestType.equals("CLEARSERVICELOAD")?"4":loadRequestType);
				jsonobjparams.remove("LOADREQUESTTYPE");
				jsonobjparams.put("LOADREQUESTTYPE", loadRequestType);
				dyncalldata.remove("LOADREQUESTTYPE");
				dyncalldata.put("LOADREQUESTTYPE", loadRequestType);
				
				if(formatPhoneNr((String)jsonobjparams.get("SERVICEID")).equals("0")||formatPhoneNr((String)jsonobjparams.get("LOADID")).equals("0")||formatPhoneNr((String)jsonobjparams.get("SOURCEID")).equals("0")){
					if((loadRequestType.equals("4")&&formatPhoneNr((String)jsonobjparams.get("SOURCEID")).equals("0"))&&(((String)jsonobjparams.get("SERVICEID")).equals("0")||formatPhoneNr((String)jsonobjparams.get("LOADID")).equals("0"))){
						jsondyncalldataErrors.add("INVALID SERVICEID OR LOADID - [SERVICEID="+(String)jsonobjparams.get("SERVICEID")+" AND LOADID="+(String)jsonobjparams.get("LOADID")+"]");
					}
					else if(!loadRequestType.equals("4")){
						jsondyncalldataErrors.add("INVALID SERVICEID, LOADID OR SOURCEID - [SERVICEID="+(String)jsonobjparams.get("SERVICEID")+", LOADID="+(String)jsonobjparams.get("LOADID")+" AND SOURCEID="+(String)jsonobjparams.get("SOURCEID")+"]");
					}
				}
				if(!formatPhoneNr((String)jsonobjparams.get("SERVICEID")).equals("0")){
					if(!inovo.presence.PresenceDB.serviceExist((String)jsonobjparams.get("SERVICEID"))){
						jsondyncalldataErrors.add("SERVICE DOES NOT EXIST [SERVICEID="+(String)jsonobjparams.get("SERVICEID")+"]");
					}
					else{
						if(!formatPhoneNr((String)jsonobjparams.get("LOADID")).equals("0")){
							if(!inovo.presence.PresenceDB.serviceLoadExist(formatPhoneNr((String)jsonobjparams.get("SERVICEID")), formatPhoneNr((String)jsonobjparams.get("LOADID")), "")){
								jsondyncalldataErrors.add("LOAD DOES NOT EXIST FOR SERVICE [SERVICEID="+(String)jsonobjparams.get("SERVICEID")+" AND LOADID="+(String)jsonobjparams.get("LOADID")+"]");
							}
						}
					}
				}
				
				int phonenrsfoundcount=0;
				
				if(jsonobjparams.get("LOADREQUESTTYPE").equals("1")||jsonobjparams.get("LOADREQUESTTYPE").equals("3")){
					int invalidPhoneNrCount=0;
					StringBuilder invalidPhoneNrsString=new StringBuilder();
					for(int phonenr=1;phonenr<=10;phonenr++){
						if(jsonobjparams.containsKey("PHONE"+String.valueOf(phonenr))){
							if(((String)jsonobjparams.get("PHONE"+String.valueOf(phonenr))).equals("")) continue;
							if(formatPhoneNr((String)jsonobjparams.get("PHONE"+String.valueOf(phonenr))).equals("0")){
								invalidPhoneNrCount++;
								invalidPhoneNrsString.append("[PHONE"+String.valueOf(phonenr)+"="+(String)jsonobjparams.get("PHONE"+String.valueOf(phonenr))+"], ");
							}
							else{
								phonenrsfoundcount++;
							}
						}
					}
					if(invalidPhoneNrCount>0){
						jsondyncalldataErrors.add("INVALID PHONE NUMBER(s):"+invalidPhoneNrsString.substring(0,invalidPhoneNrsString.length()-2));
					}
					if(phonenrsfoundcount==0&&jsonobjparams.get("LOADREQUESTTYPE").equals("1")){
						jsondyncalldataErrors.add("NO PHONE NUMBER(s) SPECIFIED TO ADD CALL REQUEST");
					}
					invalidPhoneNrsString.setLength(0);
					invalidPhoneNrsString=null;
				}
				
				if(jsondyncalldataErrors.isEmpty()){
					if(jsonobjparams.get("LOADREQUESTTYPE").equals("1")){
						dyncalldata.put("VALIDRECORDCOUNT", "0");
						Database.executeDBRequest(null,"LEADSAUTOMATION","SELECT COUNT(*) AS VALIDRECORDCOUNT FROM <DBUSER>.[DYNAMICCALLERLIST] WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID AND ID=:SOURCEID",dyncalldata,null);
						Database.executeDBRequest(null,"PRESENCE","SELECT ISNULL(PRIORITYVALUE,100) AS DEFAULTPRIORITY FROM <DBUSER>.PCO_LOAD WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID",dyncalldata,null);
						if(dyncalldata.get("VALIDRECORDCOUNT").equals("0")){
							StringBuilder invalidFieldsToAddLabels=new StringBuilder();
							StringBuilder addCallRequestStatement=new StringBuilder();
							dyncalldata.put("DEFAULTPRIORITY", "100");
							addCallRequestStatement.append("INSERT INTO <DBUSER>.[DYNAMICCALLERLIST] (ID,SERVICEID,LOADID,CALLERNAME,COMMENTS,PHONE1,PHONE2,PHONE3,PHONE4,PHONE5,PHONE6,PHONE7,PHONE8,PHONE9,PHONE10,SCHEDULEDCALL,AGENTLOGINID,PRIORITY,LASTACTIONDATETIME,METAFIELD1,METAFIELD2,METAFIELD3,METAFIELD4,METAFIELD6,METAFIELD7,METAFIELD8,METAFIELD9,METAFIELD10,METAFIELD11,METAFIELD12,METAFIELD13,METAFIELD14,METAFIELD16,METAFIELD17,METAFIELD18,METAFIELD19,METAFIELD20) VALUES(");
							for(String fieldtoAdd:("ID,SERVICEID,LOADID,CALLERNAME,COMMENTS,PHONE1,PHONE2,PHONE3,PHONE4,PHONE5,PHONE6,PHONE7,PHONE8,PHONE9,PHONE10,SCHEDULEDCALL,AGENTLOGINID,PRIORITY,LASTACTIONDATETIME,METAFIELD1,METAFIELD2,METAFIELD3,METAFIELD4,METAFIELD6,METAFIELD7,METAFIELD8,METAFIELD9,METAFIELD10,METAFIELD11,METAFIELD12,METAFIELD13,METAFIELD14,METAFIELD16,METAFIELD17,METAFIELD18,METAFIELD19,METAFIELD20").split("[,]")){
								if(fieldtoAdd.equals("ID")){
									if(dyncalldata.containsKey("SOURCEID")){
										addCallRequestStatement.append(":SOURCEID");
									}
									else{
										invalidFieldsToAddLabels.append("SOURCEID,");
									}
								}
								else if(fieldtoAdd.equals("SERVICEID")){
									if(dyncalldata.containsKey("SERVICEID")){
										addCallRequestStatement.append(":SERVICEID");
									}
									else{
										invalidFieldsToAddLabels.append("SERVICEID,");
									}
								}
								else if(fieldtoAdd.equals("LOADID")){
									if(dyncalldata.containsKey("LOADID")){
										addCallRequestStatement.append(":LOADID");
									}
									else{
										invalidFieldsToAddLabels.append("LOADID,");
									}
								}
								else if(fieldtoAdd.equals("CALLERNAME")){
									if(dyncalldata.containsKey("NAME")){
										addCallRequestStatement.append(":NAME");
									}
									else if(dyncalldata.containsKey("CALLERNAME")){
										addCallRequestStatement.append(":CALLERNAME");
									}
									else{
										addCallRequestStatement.append("''");
									}
								}
								else if(fieldtoAdd.equals("COMMENTS")){
									if(dyncalldata.containsKey("COMMENTS")){
										addCallRequestStatement.append(":COMMENTS");
									}
									else{
										addCallRequestStatement.append("''");
									}
								}
								else if(fieldtoAdd.equals("PRIORITY")){
									if(dyncalldata.containsKey("PRIORITY")){
										if(((String)dyncalldata.get("PRIORITY")).equals("")||((String)dyncalldata.get("PRIORITY")).equals("0")){
											addCallRequestStatement.append(":DEFAULTPRIORITY");
										}
										else{
											addCallRequestStatement.append(":PRIORITY");
										}
									}
									else{
										addCallRequestStatement.append(":DEFAULTPRIORITY");
									}
								}
								else if(fieldtoAdd.equals("SCHEDULEDCALL")){
									if(dyncalldata.containsKey("SCHEDULEDCALL")){
										if(((String)dyncalldata.get("SCHEDULEDCALL")).equals("")){
											addCallRequestStatement.append("NULL");
										}
										else{
											addCallRequestStatement.append(":SCHEDULEDCALL");
										}
									}
									else if(dyncalldata.containsKey("SCHEDULEDATE")){
										if(((String)dyncalldata.get("SCHEDULEDATE")).equals("")){
											addCallRequestStatement.append("NULL");
										}
										else{
											addCallRequestStatement.append(":SCHEDULEDATE");
										}
									}
									else if(dyncalldata.containsKey("SCHEDULEDDATE")){
										if(((String)dyncalldata.get("SCHEDULEDDATE")).equals("")){
											addCallRequestStatement.append("NULL");
										}
										else{
											addCallRequestStatement.append(":SCHEDULEDDATE");
										}
									}
									else if(dyncalldata.containsKey("SCHEDULEDATETIME")){
										if(((String)dyncalldata.get("SCHEDULEDATETIME")).equals("")){
											addCallRequestStatement.append("NULL");
										}
										else{
											addCallRequestStatement.append(":SCHEDULEDATETIME");
										}
									}
									else{
										addCallRequestStatement.append("NULL");
									}
								}
								else if(fieldtoAdd.equals("AGENTLOGINID")){
									if(dyncalldata.containsKey("AGENTLOGINID")){
										addCallRequestStatement.append(":AGENTLOGINID");
									}
									else if(dyncalldata.containsKey("AGENTLOGIN")){
										addCallRequestStatement.append(":AGENTLOGIN");
									}
									else if(dyncalldata.containsKey("LOGIN")){
										addCallRequestStatement.append(":LOGIN");
									}
									else if(dyncalldata.containsKey("LOGINID")){
										addCallRequestStatement.append(":LOGINID");
									}
									else{
										addCallRequestStatement.append("NULL");
									}
								}
								else if(fieldtoAdd.equals("LASTACTIONDATETIME")){
									addCallRequestStatement.append("NOW()");
								}
								else if(fieldtoAdd.startsWith("PHONE")){
									if(",PHONE1,PHONE2,PHONE3,PHONE4,PHONE5,PHONE6,PHONE7,PHONE8,PHONE9,PHONE10,".indexOf(","+fieldtoAdd+",")>-1){
										addCallRequestStatement.append(":"+fieldtoAdd);
									}
									else{
										invalidFieldsToAddLabels.append(fieldtoAdd+",");
									}
								}
								else if(fieldtoAdd.startsWith("METAFIELD")){
									if(",METAFIELD1,METAFIELD2,METAFIELD3,METAFIELD4,METAFIELD5,METAFIELD6,METAFIELD7,METAFIELD8,METAFIELD9,METAFIELD10,METAFIELD11,METAFIELD12,METAFIELD13,METAFIELD14,METAFIELD15,METAFIELD16,METAFIELD17,METAFIELD18,METAFIELD19,METAFIELD20,".indexOf(","+fieldtoAdd+",")>-1){
										addCallRequestStatement.append(":"+fieldtoAdd);
									}
									else{
										addCallRequestStatement.append("''");
									}
								}
								addCallRequestStatement.append(",");
							}
							if(invalidFieldsToAddLabels.length()>0){
								jsondyncalldataErrors.add("MISSING FIELD(S) TO ADD SPECIFIED -["+invalidFieldsToAddLabels.substring(0,invalidFieldsToAddLabels.length()-2));
							}
							else{
								Database.executeDBRequest(null,"LEADSAUTOMATION",addCallRequestStatement.substring(0,addCallRequestStatement.length()-1)+")",dyncalldata,null);
							}
						}
						else{
							jsondyncalldataErrors.add("SERVICEID, LOADID AND SOURCEID ALREADY MATCH AN EXISTING CALL REQUEST - [SERVICEID="+(String)jsonobjparams.get("SERVICEID")+", LOADID="+(String)jsonobjparams.get("LOADID")+" AND SOURCEID="+(String)jsonobjparams.get("SOURCEID")+"]");
						}
					}
					else if(jsonobjparams.get("LOADREQUESTTYPE").equals("2")){
						dyncalldata.put("VALIDRECORDCOUNT", "0");
						Database.executeDBRequest(null,"LEADSAUTOMATION","SELECT COUNT(*) AS VALIDRECORDCOUNT FROM <DBUSER>.[DYNAMICCALLERLIST] WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID AND ID=:SOURCEID",dyncalldata,null);
						if(dyncalldata.get("VALIDRECORDCOUNT").equals("0")){
							jsondyncalldataErrors.add("INVALID SERVICEID, LOADID AND SOURCEID TO IDENTIFY RECORD TO REMOVE - [SERVICEID="+(String)jsonobjparams.get("SERVICEID")+", LOADID="+(String)jsonobjparams.get("LOADID")+" AND SOURCEID="+(String)jsonobjparams.get("SOURCEID")+"]");
						}
						else{
							Database.executeDBRequest(null,"LEADSAUTOMATION","UPDATE <DBUSER>.[DYNAMICCALLERLIST] SET [LOADREQUESTTYPE]=2, [RECORDHANDLEFLAG]=1,LASTACTIONDATETIME=NOW() WHERE ID=:SOURCEID AND LOADID=:LOADID AND SERVICEID=:SERVICEID",dyncalldata,null);
						}
					}
					else if(jsonobjparams.get("LOADREQUESTTYPE").equals("3")){
						dyncalldata.put("VALIDRECORDCOUNT", "0");
						Database.executeDBRequest(null,"LEADSAUTOMATION","SELECT COUNT(*) AS VALIDRECORDCOUNT FROM <DBUSER>.[DYNAMICCALLERLIST] WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID AND ID=:SOURCEID",dyncalldata,null);
						if(dyncalldata.get("VALIDRECORDCOUNT").equals("0")){
							jsondyncalldataErrors.add("INVALID SERVICEID, LOADID AND SOURCEID TO IDENTIFY RECORD TO MODIFY - [SERVICEID="+(String)jsonobjparams.get("SERVICEID")+", LOADID="+(String)jsonobjparams.get("LOADID")+" AND SOURCEID="+(String)jsonobjparams.get("SOURCEID")+"]");
						}
						else{
							dyncalldata.put("INVALIDMODIFYRECORDCOUNT", "0");
							Database.executeDBRequest(null,"LEADSAUTOMATION","SELECT COUNT(*) AS INVALIDMODIFYRECORDCOUNT FROM <DBUSER>.[DYNAMICCALLERLIST] WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID AND ID=:SOURCEID AND LOADREQUESTTYPE=2 AND RECORDHANDLEFLAG<=2",dyncalldata,null);
							if(!dyncalldata.get("INVALIDMODIFYRECORDCOUNT").equals("0")){
								jsondyncalldataErrors.add("PENDING REMOVE CALL REQUEST - [SERVICEID="+(String)jsonobjparams.get("SERVICEID")+", LOADID="+(String)jsonobjparams.get("LOADID")+" AND SOURCEID="+(String)jsonobjparams.get("SOURCEID")+"]");
							}
							else{
								if(((String)jsonobjparams.get("FIELDSTOMODIFY")).equals("")){
									jsondyncalldataErrors.add("NO FIELD(s) TO MODIFY");
								}
								else{
									StringBuilder invalidFieldsToModifyLabels=new StringBuilder();
									StringBuilder modifyCallRequestStatement=new StringBuilder();	
									modifyCallRequestStatement.append("UPDATE <DBUSER>.[DYNAMICCALLERLIST] SET ");
									boolean outboundRequestFieldChanged=false;
									for(String fieldtomodify:((String)jsonobjparams.get("FIELDSTOMODIFY")).split("[,]")){
										if(",SCHEDULEDCALL,SCHEDULEDDATE,SCHEDULEDATE,AGENTLOGIN,AGENTLOGINID,CALLERNAME,NAME,COMMENTS,PHONE1,PHONE2,PHONE3,PHONE4,PHONE5,PHONE6,PHONE7,PHONE8,PHONE9,PHONE10,METAFIELD1,METAFIELD2,METAFIELD3,METAFIELD4,METAFIELD5,METATFIELD6,METAFIELD7,METAFIELD8,METAFIELD9,METAFIELD10,METAFIELD11,METAFIELD12,METAFIELD13,METAFIELD14,METAFIELD15,METATFIELD16,METAFIELD17,METAFIELD18,METAFIELD19,METAFIELD20".indexOf(","+(fieldtomodify=fieldtomodify.toUpperCase())+",")==-1){
											invalidFieldsToModifyLabels.append(fieldtomodify+", ");
										}
										if(!outboundRequestFieldChanged){
											if(!fieldtomodify.toUpperCase().startsWith("METAFIELD")){
												outboundRequestFieldChanged=true;
											}
										}
										if(fieldtomodify.startsWith("PHONE")){
											modifyCallRequestStatement.append(fieldtomodify+"=:"+fieldtomodify+", ");
										}
										else if(fieldtomodify.equals("COMMENTS")){
											modifyCallRequestStatement.append(fieldtomodify+"=:"+fieldtomodify+", ");
										}
										else if(fieldtomodify.equals("SCHEDULEDCALL")||fieldtomodify.equals("SCHEDULEDATE")||fieldtomodify.equals("SCHEDULEDDATE")){
											modifyCallRequestStatement.append("SCHEDULEDCALL=:"+fieldtomodify+", ");
										}
										else if(fieldtomodify.equals("NAME")||fieldtomodify.equals("CALLERNAME")){
											modifyCallRequestStatement.append("CALLERNAME=:"+fieldtomodify+", ");
										}
										else if(fieldtomodify.equals("AGENTLOGIN")||fieldtomodify.equals("AGENTLOGINID")){
											modifyCallRequestStatement.append("AGENTLOGIN=:"+fieldtomodify+", ");
										}
										else if(fieldtomodify.startsWith("METAFIELD")){
											modifyCallRequestStatement.append(fieldtomodify+"=:"+fieldtomodify+", ");
										}
										
									}
									modifyCallRequestStatement.append((outboundRequestFieldChanged?" [LOADREQUESTTYPE]=3, [RECORDHANDLEFLAG]=1, ":"")+"LASTACTIONDATETIME=NOW(),FIELDSTOMODIFY=:FIELDSTOMODIFY WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID AND ID=:SOURCEID");
									
									if(invalidFieldsToModifyLabels.length()>0){
										jsondyncalldataErrors.add("INVALID FIELD(S) TO MODIFY SPECIFIED -["+invalidFieldsToModifyLabels.substring(0,invalidFieldsToModifyLabels.length()-2));
									}
									else{
										Database.executeDBRequest(null,"LEADSAUTOMATION",modifyCallRequestStatement.substring(0,modifyCallRequestStatement.length()),dyncalldata,null);
									}
								}
							}
						}
					}
					else if(jsonobjparams.get("LOADREQUESTTYPE").equals("4")){
						dyncalldata.put("VALIDRECORDCOUNT", "0");
						Database.executeDBRequest(null,"LEADSAUTOMATION","SELECT COUNT(*) AS VALIDRECORDCOUNT FROM <DBUSER>.[DYNAMICCALLERLIST] WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID AND ID=:SOURCEID",dyncalldata,null);
						if(dyncalldata.get("VALIDRECORDCOUNT").equals("0")){
							dyncalldata.put("VALIDPRESENCELOADCOUNT", "0");
							Database.executeDBRequest(null,"PRESENCE","SELECT COUNT(*) AS VALIDPRESENCELOADCOUNT FROM <DBUSER>.PCO_LOAD WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID",dyncalldata,null);
							if(dyncalldata.get("VALIDPRESENCELOADCOUNT").equals("0")){
								jsondyncalldataErrors.add("NO SUCH SERVICEID AND LOADID EXIST IN DIALER - [SERVICEID="+(String)jsonobjparams.get("SERVICEID")+", LOADID="+(String)jsonobjparams.get("LOADID")+"]");
							}
							else{
								//jsondyncalldataErrors.add("INVALID SERVICEID, LOADID AND SOURCEID TO IDENTIFY RECORD TO REMOVE - [SERVICEID="+(String)jsonobjparams.get("SERVICEID")+", LOADID="+(String)jsonobjparams.get("LOADID")+" AND SOURCEID="+(String)jsonobjparams.get("SOURCEID")+"]");
								Database.executeDBRequest(null,"LEADSAUTOMATION","INSERT INTO <DBUSER>.DYNAMICCALLERLIST (ID,SERVICEID,LOADID,CALLERNAME,PHONE1,LOADREQUESTTYPE,RECORDHANDLEFLAG,LASTACTIONDATETIME) VALUES(0,:SERVICEID,:LOADID,'','',4,1,NOW())",dyncalldata,null);
							}
						}
						else{
							jsondyncalldataErrors.add("CLEANUP REQUEST ALREADY REQUESTED - [SERVICEID="+(String)jsonobjparams.get("SERVICEID")+", LOADID="+(String)jsonobjparams.get("LOADID")+"]");
						}
					}
					else{
						jsondyncalldataErrors.add("INVALID LOADREQUESTTYPE [MUST BE 1-(addcall), 2-(removecall), 3-(modifycall) or 4-clearServiceLoad");
					}
				}
				//if(jsondyncalldataErrors.isEmpty()){
					
				//}
			}
		}
		catch(Exception ex){
			jsondyncalldataErrors.add(ex.getMessage());
		}
		
		if(!invalidparams.isEmpty()){
			jsondyncalldataErrors.add(new JSONObject());
			((JSONArray)((JSONObject) jsondyncalldataErrors.get(0)).put("INVALIDPARAMS", new JSONArray())).addAll(invalidparams);
		}
		
		if(!jsondyncalldataErrors.isEmpty()){
			InovoServletContextListener.inovoServletListener().logDebug("LeadsAutomation.loadCall()ERROR:"+jsondyncalldataErrors.toString());
			loadCallResponse.append("ERROR:"+jsondyncalldataErrors.toString());
		}
		else{
			loadCallResponse.append("REQUEST RECEIVED");
		}
		//loadCallResponse.append(jsonobj.toString(JSONStyle.NO_COMPRESS));
		jsonobj.clear();
		jsonobj=null;
		return loadCallResponse.toString();
	}

	public void addCall(HashMap<String, Object> dyncalldata) {
		dyncalldata.put("SOURCEID", dyncalldata.get("ID"));
		dyncalldata.put("STATUS", "1");
		dyncalldata.put("NAME", dyncalldata.get("CALLERNAME"));
		
		for(int phonenr=1;phonenr<=10;phonenr++){
			String phonenrfield="PHONE"+String.valueOf(phonenr);
			if(!dyncalldata.get(phonenrfield).equals("")){
				String phonenrval=this.formatPhoneNr(dyncalldata.get(phonenrfield).toString().toString());
				String phoneTestVal=phonenrval;
				while(phoneTestVal.startsWith("0")) phoneTestVal=phoneTestVal.substring(1);
				if(phoneTestVal.length()>=11&&phoneTestVal.startsWith("27")) phonenrval=phonenrval.substring(0,phonenrval.indexOf("27"))+phonenrval.substring(phonenrval.indexOf("27")+2);
				
				dyncalldata.put(phonenrfield,phonenrval);
				if(!dyncalldata.containsKey("PHONE")){
					dyncalldata.put("PHONE", phonenrval);
				}
			}
		}
		
		if(!dyncalldata.containsKey("PHONE")){
			dyncalldata.put("PHONE", "");
		}
		
		if(!dyncalldata.get("SCHEDULEDCALL").equals("")) dyncalldata.put("SCHEDULEDATE",dyncalldata.get("SCHEDULEDCALL"));
		if(!dyncalldata.get("AGENTLOGINID").equals("")) dyncalldata.put("CAPTURINGAGENT",dyncalldata.get("AGENTLOGINID"));
		InovoServletContextListener.inovoServletListener().logDebug("LeadsAutomation.addCall("+dyncalldata.toString()+")");
		try {
			PresenceDB.createPresenceCall(dyncalldata);
			Database.executeDBRequest(null,"LEADSAUTOMATION", "UPDATE <DBUSER>.[DYNAMICCALLERLIST] SET [RECORDHANDLEFLAG]=3,[LASTACTIONDATETIME]=NOW() WHERE [RECORDHANDLEFLAG]=2 AND [SERVICEID]=:SERVICEID AND [LOADID]=:LOADID AND [ID]=:ID", dyncalldata,null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Database.executeDBRequest(null,"LEADSAUTOMATION", "UPDATE <DBUSER>.[DYNAMICCALLERLIST] SET [RECORDHANDLEFLAG]=4,[LASTACTIONDATETIME]=NOW() WHERE [RECORDHANDLEFLAG]=2 AND [SERVICEID]=:SERVICEID AND [LOADID]=:LOADID AND [ID]=:ID", dyncalldata,null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private String formatPhoneNr(String phonenrToFormat) {
		String formattedPhoneNr="";
		String trailingzero="";
		boolean checkTrailingZero=true;
		for(char cp:(phonenrToFormat=(phonenrToFormat==null?"":phonenrToFormat).trim()).toCharArray()){
			if("0123456789".indexOf(cp)>-1){
				if(checkTrailingZero&&cp=='0'){
					trailingzero+=cp;
				}
				else if(checkTrailingZero&&cp!='0'){
					checkTrailingZero=false;
				}
				formattedPhoneNr+=cp;
			}
		}
		
		while(formattedPhoneNr.startsWith("0")) formattedPhoneNr=formattedPhoneNr.substring(1);
		if(formattedPhoneNr.equals("")){
			formattedPhoneNr="0";
		}
		else{
			formattedPhoneNr=(trailingzero.equals("")?formattedPhoneNr:trailingzero.length()>1?trailingzero.substring(1)+formattedPhoneNr:formattedPhoneNr);
		}
		return formattedPhoneNr;
	}

	public void removeCall(HashMap<String, Object> dyncalldata) {
		InovoServletContextListener.inovoServletListener().logDebug("LeadsAutomation.removeCall("+dyncalldata.toString()+")");
		try {
			PresenceDB.removePresenceCall(Integer.parseInt(dyncalldata.get("SERVICEID").toString()),Integer.parseInt(dyncalldata.get("LOADID").toString()),Long.parseLong(dyncalldata.get("ID").toString()));
			Database.executeDBRequest(null,"LEADSAUTOMATION", "DELETE FROM <DBUSER>.[DYNAMICCALLERLIST] WHERE [RECORDHANDLEFLAG]=2 AND [SERVICEID]=:SERVICEID AND [LOADID]=:LOADID AND [ID]=:ID", dyncalldata,null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Database.executeDBRequest(null,"LEADSAUTOMATION", "UPDATE <DBUSER>.[DYNAMICCALLERLIST] SET [RECORDHANDLEFLAG]=4,[LASTACTIONDATETIME]=NOW() WHERE [RECORDHANDLEFLAG]=2 AND [SERVICEID]=:SERVICEID AND [LOADID]=:LOADID AND [ID]=:ID", dyncalldata,null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void modifyCall(HashMap<String, Object> dyncalldata) {
		InovoServletContextListener.inovoServletListener().logDebug("LeadsAutomation.modifyCall("+dyncalldata.toString()+")");
		try {
			HashMap<String,Object> callDetailsToChange=new HashMap<String,Object>();
			for(String callFieldToChange:dyncalldata.get("FIELDSTOMODIFY").toString().toUpperCase().trim().split(",")){
				if(callFieldToChange.equals("SCHEDULEDCALL")||callFieldToChange.equals("SCHEDULEDATE")){
					callDetailsToChange.put("SCHEDULEDATE", dyncalldata.get("SCHEDULEDCALL"));
				}
				else if(callFieldToChange.equals("AGENTLOGIN")||callFieldToChange.equals("AGENTLOGINID")){
					callDetailsToChange.put("AGENTLOGIN", dyncalldata.get("AGENTLOGINID"));
				}
				else if(callFieldToChange.equals("COMMENTS")){
					callDetailsToChange.put("COMMENTS", dyncalldata.get("COMMENTS"));
				}
				else if(callFieldToChange.equals("CALLERNAME")){
					callDetailsToChange.put("NAME", dyncalldata.get("CALLERNAME"));
				}
				else if(callFieldToChange.startsWith("PHONE")){
					int phonenr=Integer.parseInt(callFieldToChange.substring("PHONE".length()));
					String phonenrval=this.formatPhoneNr(dyncalldata.get("PHONE"+String.valueOf(phonenr)).toString());
					String phoneTestVal=phonenrval;
					while(phoneTestVal.startsWith("0")) phoneTestVal=phoneTestVal.substring(1);
					if(phoneTestVal.length()>=11&&phoneTestVal.startsWith("27")) phonenrval=phonenrval.substring(0,phonenrval.indexOf("27"))+phonenrval.substring(phonenrval.indexOf("27")+2);
					
					dyncalldata.put("PHONE"+String.valueOf(phonenr),phonenrval.equals("0")?"":phonenrval);
					
					callDetailsToChange.put("PHONE"+String.valueOf(phonenr), dyncalldata.get("PHONE"+String.valueOf(phonenr)));
				}
			}
			
			PresenceDB.updatePresenceCallDetails(callDetailsToChange,Integer.parseInt(dyncalldata.get("SERVICEID").toString()),Integer.parseInt(dyncalldata.get("LOADID").toString()),Integer.parseInt(dyncalldata.get("ID").toString()));
			Database.executeDBRequest(null,"LEADSAUTOMATION", "UPDATE <DBUSER>.[DYNAMICCALLERLIST] SET [RECORDHANDLEFLAG]=3,[LASTACTIONDATETIME]=NOW() WHERE [RECORDHANDLEFLAG]=2 AND [SERVICEID]=:SERVICEID AND [LOADID]=:LOADID AND [ID]=:ID", dyncalldata,null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Database.executeDBRequest(null,"LEADSAUTOMATION", "UPDATE <DBUSER>.[DYNAMICCALLERLIST] SET [RECORDHANDLEFLAG]=4,[LASTACTIONDATETIME]=NOW() WHERE [RECORDHANDLEFLAG]=2 AND [SERVICEID]=:SERVICEID AND [LOADID]=:LOADID AND [ID]=:ID", dyncalldata,null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public void reloadServices(ArrayList<String> serviceIds) {
		synchronized (_serviceIDsToReload) {
			_serviceIDsToReload.addAll(serviceIds);
		}	
	}

	
	
	public void cleanupServiceLoad(HashMap<String, Object> cleanuploadparams) {
		String cleanupStage="presence";
		HashMap<String,Object> presenceCallInfo=new HashMap<String, Object>();
		TreeMap<Integer,ArrayList<Object>> pcooutboundqueueset=new TreeMap<Integer, ArrayList<Object>>();
		while(!cleanupStage.equals("done")){
			if(cleanupStage.equals("presence")){
				Database.cleanupDataset(pcooutboundqueueset);
				try {
					Database.executeDBRequest(pcooutboundqueueset, "PRESENCE", "SELECT SERVICEID,LOADID,SOURCEID FROM <DBUSER>.PCO_OUTBOUNDQUEUE WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID", cleanuploadparams, null);
					for(Integer rowindex:pcooutboundqueueset.keySet()){
						if(rowindex==0) continue;
						presenceCallInfo.clear();
						presenceCallInfo.putAll(Database.rowData(pcooutboundqueueset,rowindex));
						try{
							Database.executeDBRequest(null, "LEADSAUTOMATION", "INSERT INTO <DBUSER>.DYNAMICCALLERLIST (ID, SERVICEID, LOADID,CALLERNAME,PHONE1,LASTACTIONDATETIME,LOADREQUESTTYPE,RECORDHANDLEFLAG) SELECT :SOURCEID,:SERVICEID,:LOADID,'','',NOW(),2,1 WHERE (SELECT COUNT(*) AS DYNCALLCOUNT FROM <DBUSER>.DYNAMICCALLERLIST WHERE ID=:SOURCEID AND SERVICEID=:SERVICEID AND LOADID=:LOADID)=0",presenceCallInfo,null);
						}
						catch(Exception ed){
							inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("ERROR: cleanupServiceLoad()[LEADSAUTOMATION] - "+ed.getMessage());
						}
					}
				
				} catch (Exception e) {
				  inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("ERROR: cleanupServiceLoad()[PRESENCE] - "+e.getMessage());
				  try {
						Thread.sleep(2000);
					} catch (InterruptedException ie) {}
				}
				cleanupStage="lms";
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			if(cleanupStage.equals("lms")){
				try{
					Database.executeDBRequest(null, "LEADSAUTOMATION", "UPDATE <DBUSER>.DYNAMICCALLERLIST SET LOADREQUESTTYPE=2,RECORDHANDLEFLAG=1,LASTACTIONDATETIME=NOW() WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID AND ((LOADREQUESTTYPE=2 AND RECORDHANDLEFLAG>2) OR (LOADREQUESTTYPE<>2)) AND LOADREQUESTTYPE<>4",cleanuploadparams,null);
				}
				catch(Exception ed){
					inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("ERROR: cleanupServiceLoad()[LEADSAUTOMATION] - "+ed.getMessage());
				}
				cleanupStage="lmscheck";
			}
			if(cleanupStage.equals("lmscheck")){
				cleanuploadparams.put("DYNSERVICELOADCHECKCOUNT", "0");
				cleanuploadparams.put("PRESSERVICELOADCHECKCOUNT", "0");
				try{
					Database.executeDBRequest(null, "LEADSAUTOMATION", "SELECT COUNT(*) AS DYNSERVICELOADCHECKCOUNT FROM <DBUSER>.DYNAMICCALLERLIST WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID AND LOADREQUESTTYPE<>4",cleanuploadparams,null);
				}
				catch(Exception ed){
					inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("ERROR: cleanupServiceLoad()[LEADSAUTOMATION-DYNCALLERLISTCHECK] - "+ed.getMessage());
				}
				if(cleanuploadparams.get("DYNSERVICELOADCHECKCOUNT").equals("0")){
					try{
						Database.executeDBRequest(null, "PRESENCE", "SELECT COUNT(*) AS PRESSERVICELOADCHECKCOUNT FROM <DBUSER>.PCO_OUTBOUNDQUEUE WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID",cleanuploadparams,null);
					}
					catch(Exception ed){
						inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("ERROR: cleanupServiceLoad()[LEADSAUTOMATION-PCO_OUTBOUNDQUEUE] - "+ed.getMessage());
					}
					if(cleanuploadparams.get("PRESSERVICELOADCHECKCOUNT").equals("0")){
						cleanupStage="done";
					}
					else{
						cleanupStage="presence";
					}
				}
				else{
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
					}
					cleanupStage="lms";
				}
			}
		}
		try{
			Database.executeDBRequest(null, "LEADSAUTOMATION", "DELETE FROM <DBUSER>.DYNAMICCALLERLIST WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID AND LOADREQUESTTYPE=4",cleanuploadparams,null);
		}
		catch(Exception ed){
			inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("ERROR: cleanupServiceLoad()[LEADSAUTOMATION-DYNCALLERLISTCHECK] - "+ed.getMessage());
		}
		Database.cleanupDataset(pcooutboundqueueset);
		pcooutboundqueueset=null;
	}
}
