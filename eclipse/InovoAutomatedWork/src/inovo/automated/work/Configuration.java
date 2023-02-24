package inovo.automated.work;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class Configuration extends InovoHTMLPageWidget {

	public Configuration(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

	
	@Override
	public void pageContent() throws Exception {
		this.action("ADD WORK", "addwork", "", "", "", "", "", "");
		this.startElement("div", "id=automatedworks".split("[|]"), true);
		this.automatedWorks();
		this.endElement("div", true);
	}
	
	public void automatedWorks() throws Exception{
		this.startTable(null);
			this.startRow(null);
				this.startCell("style=vertical-align:top".split("[|]"));
					this.startTable(null);
						Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT ID, NAME FROM <DBUSER>.AUTOMATION" ,null, this,"readAutomatedWorkListData");
					this.endTable();
				this.endCell();
				this.startCell("id=automatedwork|style=vertical-align:top".split("[|]"));
				this.endCell();
			this.endRow();	
		this.endTable();
	}
	
	public void readAutomatedWorkListData(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns) throws Exception{
		if(rowIndex==0) return;
		this.startRow(null);
			this.startCell(null);
				this.action(rowData.get(1), "selectAutomatedWork", "", "", "automatedwork", "", "", "selectedautomatedid="+rowData.get(0));
			this.endRow();
		this.endRow();
	}
	
	public void selectAutomatedWork() throws Exception{
		String AUTOMATEDID=this.requestParameter("SELECTEDAUTOMATEDID");
		
		HashMap<String,String> automatedworkProperties=new HashMap<String, String>();
		this.setRequestParameter("AUTOMATEDID", AUTOMATEDID=(AUTOMATEDID.equals("")?this.requestParameter("AUTOMATEDID"):AUTOMATEDID), true);
		
		automatedworkProperties.put("AUTOMATEDID", AUTOMATEDID);
		automatedworkProperties.put("AUTOMATEDWORKNAME", "");
		automatedworkProperties.put("AUTOMATEDWORKDESCRIPTION", "");
		automatedworkProperties.put("AUTOMATEDWORKSTATUS", "");
		Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT NAME AS AUTOMATEDWORKNAME,DESCRIPTION AS AUTOMATEDWORKDESCRIPTION,STATUS AS AUTOMATEDWORKSTATUS FROM <DBUSER>.AUTOMATION WHERE ID=:AUTOMATEDID" , automatedworkProperties,null);
		
		this.fieldHidden("AUTOMATEDID",this.requestParameter("AUTOMATEDID"));
		this.startElement("div", "class=ui-widget-header|style=font-size:1.0em".split("[|]"), true);
			this.startTable(null);
				this.startRow(null);
					this.startCell(null);
						boolean automatedworkstatus=automatedworkProperties.get("AUTOMATEDWORKSTATUS").equals("E"); 
						if(automatedworkstatus){
							this.action("DISABLE","disableautomatedwork", "", "","", "", "","");
						}
						else if(!automatedworkstatus){
							this.action("ENABLE","enableautomatedwork", "", "","", "", "","");
							this.action("UPDATE","updateautomatedwork", "", "","", "", "","");
						}
					this.endCell();
					this.startCell(null);
						this.respondString(automatedworkProperties.get("AUTOMATEDWORKDESCRIPTION"));
					this.endCell();
				this.endRow();
			this.endTable();
		this.endElement("div", true);
		ArrayList<String[]> automatedworktabssettings=new ArrayList<String[]>();
		automatedworktabssettings.add("caption=SETTINGS|command=automatedworksettings".split("[|]"));
		automatedworktabssettings.add("caption=STEPS|command=automatedworksteps".split("[|]"));
		this.actions_tabs(automatedworktabssettings, "automatedworksection", true);
	}
	
	public void automatedworksettings() throws Exception{
		Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT ID,SCHEDULETYPE,SCHEDULE FROM <DBUSER>.AUTOMATION WHERE ID="+this.requestParameter("AUTOMATEDID"), null,this, "readAutomatedWorkSettings");
	}
	
	public void readAutomatedWorkSettings(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns) throws Exception{
		if(rowIndex==1){
			this.startTable(null);
				this.startRow(null);
					this.startColumn("font-size:0.8em");
						this.respondString("SCHEDULE TYPE");
					this.endColumn();
					
					HashMap<String,String> scheduleTypes=new HashMap<String, String>();
					scheduleTypes.put("seconds", "seconds");
					scheduleTypes.put("minutes", "minutes");
					scheduleTypes.put("hours", "hours");
					scheduleTypes.put("daily", "daily");
					
				this.endRow();
				this.startRow(null);
					this.startCell(null);
						this.fieldInput("SCHEDULETYPE", rowData.get(1), "select", true, null, scheduleTypes);
					this.endRow();
				this.endRow();
				
				this.startRow(null);
					this.startColumn("font-size:0.8em");
						this.respondString("SCHEDULE");
					this.endColumn();
				this.endRow();
				this.startRow(null);
					this.startCell(null);
						this.fieldInput("SCHEDULE", rowData.get(2), "multiline", true, "rows=5|cols=100".split("[|]"));
					this.endRow();
				this.endRow();
				
			this.endTable();
			this.action("UPDATE SCHEDULE", "updateautomatedworksettings", "", "", "", "", "", "");
		}
	}
	
	public void updateautomatedworksettings() throws Exception{
		HashMap<String,String> automatedworksettings=new HashMap<String, String>();
		this.importRequestParametersIntoMap(automatedworksettings, "AUTOMATEDID,SCHEDULETYPE,SCHEDULE");
		automatedworksettings.put("SCHEDULE",automatedworksettings.get("SCHEDULE").trim());
		ArrayList<String> settingserrors=new ArrayList<String>();
		if(automatedworksettings.get("SCHEDULETYPE").equals("")){
			settingserrors.add("NO SCHEDULE TYPE SELECTED");
		}
		if(settingserrors.isEmpty()){
			if(automatedworksettings.get("SCHEDULE").equals("")){
				settingserrors.add("NO SCHEDULE ENTERED");
			}
			if(settingserrors.isEmpty()){
				if(!automatedworksettings.get("SCHEDULETYPE").equals("daily")){
					try{
						long intervalVal=Long.parseLong(automatedworksettings.get("SCHEDULE"));
					}
					catch(Exception el){
						settingserrors.add("INVALID "+automatedworksettings.get("SCHEDULETYPE").toUpperCase()+" SCHEDULE ENTERED");
					}
				}
				else if(automatedworksettings.get("SCHEDULETYPE").equals("daily")){
					ArrayList<String> timeEntries=new ArrayList<String>();
					for(String timeValue:automatedworksettings.get("SCHEDULE").split("[,]")){
						if(timeValue.indexOf(":")>-1){
							if(timeValue.length()==5){
								timeValue=timeValue+":00";
							}
							if (timeValue.length()==8){
								try{
									new SimpleDateFormat("HH:mm:ss").parse(timeValue);
									timeEntries.add(timeValue);
								}
								catch(Exception ed){
									settingserrors.add("INVALID "+automatedworksettings.get("SCHEDULETYPE").toUpperCase()+" SCHEDULE ENTERED");
									break;
								}
							}
							else{
								settingserrors.add("INVALID "+automatedworksettings.get("SCHEDULETYPE").toUpperCase()+" SCHEDULE ENTERED");
								break;
							}
						}
						else{
							settingserrors.add("INVALID "+automatedworksettings.get("SCHEDULETYPE").toUpperCase()+" SCHEDULE ENTERED");
							break;
						}
					}
					if(settingserrors.isEmpty()){
						automatedworksettings.put("SCHEDULE","");
						while(!timeEntries.isEmpty()){
							automatedworksettings.put("SCHEDULE",automatedworksettings.get("SCHEDULE")+timeEntries.remove(0));
							if(!timeEntries.isEmpty()){
								automatedworksettings.put("SCHEDULE",automatedworksettings.get("SCHEDULE")+",");
							}
						}
					}
				}
			}
		}
		if(settingserrors.isEmpty()){
			Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE <DBUSER>.AUTOMATION SET SCHEDULETYPE=:SCHEDULETYPE,SCHEDULE=:SCHEDULE WHERE ID=:AUTOMATEDID", automatedworksettings, null);
			this.replaceComponentContent("automatedworksection");
				this.automatedworksettings();
			this.endReplaceComponentContent();
		}
		else{
			this.showDialog("", "title=UPDATE SETTINGS|contentid=automatedworksettingserrors".split("[|]"));
			this.replaceComponentContent("automatedworksettingserrors");
				this.startTable(null);
					while(!settingserrors.isEmpty()){
						this.startRow(null);
							this.startCell(null);
								this.respondString(settingserrors.remove(0));
							this.endCell();
						this.endRow();
					}
				this.endTable();
			this.endReplaceComponentContent();
		}
	}
	
	public void automatedworksteps() throws Exception{
		this.action("ADD STEP", "addautomatedworkstep", "", "", "", "", "", "");
		this.startElement("div", "id=automatedstepsflow".split("[|]"), true);
			this.automatedstepsflow();
		this.endElement("div", true);
	}
	
	public void automatedstepsflow() throws Exception{
		this.startTable(null);
			Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT ID AS STEPID, NAME AS STEPNAME,DESCRIPTION AS STEPDESCRIPTION,STEP_TYPE_ID,(SELECT DESCRIPTION FROM <DBUSER>.AUTOMATION_STEP_TYPE WHERE AUTOMATION_STEP_TYPE.ID=AUTOMATION_STEP.STEP_TYPE_ID) AS STEPTYPEDESCRIPTION,PREV_STEP_ID,NEXT_STEP_ID FROM (SELECT * FROM <DBUSER>.AUTOMATION_STEPS_SET("+this.requestParameter("AUTOMATEDID")+")) AUTOMATION_STEP" , null, this,"readAutomatedStepFlowData");
			for(Integer rowIndex:_automatedStepFlowData.keySet()){
				this.startRow(null);
					this.startCell(null);
						if(!_automatedStepFlowData.get((Integer)rowIndex).get(1).equals("START")&&!_automatedStepFlowData.get((Integer)rowIndex).get(1).equals("STOP")){
							this.action("up ^", "moveupautomatedworkStep", "", "", "", "", "", "AUTOMATEDSTEPID="+_automatedStepFlowData.get((Integer)rowIndex).get(0)+"&AUTOMATEDSTEPTYPE="+_automatedStepFlowData.get((Integer)rowIndex).get(3));
						}
					this.endCell();
					this.startColumn("font-size:1.0em");
						if(!_automatedStepFlowData.get((Integer)rowIndex).get(1).equals("START")&&!_automatedStepFlowData.get((Integer)rowIndex).get(1).equals("STOP")){
							this.action(_automatedStepFlowData.get((Integer)rowIndex).get(1), "editautomatedworkStep", "", "", "", "", "", "AUTOMATEDSTEPID="+_automatedStepFlowData.get((Integer)rowIndex).get(0)+"&AUTOMATEDSTEPTYPE="+_automatedStepFlowData.get((Integer)rowIndex).get(3));
						}
						else{
							this.respondString(_automatedStepFlowData.get((Integer)rowIndex).get(1));
						}
					this.endColumn();
					this.startCell(null);
						if(!_automatedStepFlowData.get((Integer)rowIndex).get(1).equals("START")&&!_automatedStepFlowData.get((Integer)rowIndex).get(1).equals("STOP")){
							this.action("down V", "movedownautomatedworkStep", "", "", "", "", "", "AUTOMATEDSTEPID="+_automatedStepFlowData.get((Integer)rowIndex).get(0)+"&AUTOMATEDSTEPTYPE="+_automatedStepFlowData.get((Integer)rowIndex).get(3));
						}
					this.endCell();
				this.endRow();
			}
		this.endTable();
	}
	
	private HashMap<String,String> _automatedworkStepParams=new HashMap<String, String>();
	public void moveupautomatedworkStep() throws Exception{
		_automatedworkStepParams.clear();
		this.importRequestParametersIntoMap(_automatedworkStepParams, "AUTOMATEDSTEPID,AUTOMATEDID");
		
		Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT ID AS STEPID, NAME AS STEPNAME,DESCRIPTION AS STEPDESCRIPTION,STEP_TYPE_ID,(SELECT DESCRIPTION FROM <DBUSER>.AUTOMATION_STEP_TYPE WHERE AUTOMATION_STEP_TYPE.ID=AUTOMATION_STEP.STEP_TYPE_ID) AS STEPTYPEDESCRIPTION,PREV_STEP_ID,NEXT_STEP_ID FROM (SELECT * FROM <DBUSER>.AUTOMATION_STEPS_SET("+this.requestParameter("AUTOMATEDID")+")) AUTOMATION_STEP" , null, this,"readMoveUpAutomatedStepFlowData");
		this.replaceComponentContent("automatedstepsflow");
			this.automatedstepsflow();
		this.endReplaceComponentContent();
	}
	
	private ArrayList<String> _prevMoveUpRowData=new ArrayList<String>();
	private ArrayList<String> _swapMoveUpData=new ArrayList<String>();
	private boolean _moveUpFound=false;
	private boolean _foundFirstRecord=false;
	public void readMoveUpAutomatedStepFlowData(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns) throws Exception{
		if(rowIndex==0){
			return;
		}
		
		if(!_moveUpFound){
			if(this._moveUpFound=(rowData.get(0).equals(_automatedworkStepParams.get("AUTOMATEDSTEPID")))){
				if(!_foundFirstRecord){
					Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE <DBUSER>.AUTOMATION_STEP SET NEXT_STEP_ID="+rowData.get(0)+" WHERE ID="+_prevMoveUpRowData.get(0),null, null);
					_foundFirstRecord=true;
					_swapMoveUpData.clear();
					_swapMoveUpData.addAll(rowData);
					Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE <DBUSER>.AUTOMATION_STEP SET PREV_STEP_ID="+_prevMoveUpRowData.get(0)+" WHERE ID="+rowData.get(0),null, null);
				}
			}
			else{
				_prevMoveUpRowData.clear();
				_prevMoveUpRowData.addAll(rowData);
			}
		}
		else if(_moveUpFound){
			if(_foundFirstRecord){
				
				Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE <DBUSER>.AUTOMATION_STEP SET NEXT_STEP_ID="+rowData.get(0)+" WHERE ID="+rowData.get(0),null, null);
				
				_foundFirstRecord=false;
			}
			else{
				
			}
		}
	}
	
	public void editautomatedworkStep() throws Exception{
		HashMap<String,String> automatedworkStepParams=new HashMap<String, String>();
		this.importRequestParametersIntoMap(automatedworkStepParams, "AUTOMATEDSTEPTYPE,AUTOMATEDID,AUTOMATEDSTEPID");
		automatedworkStepParams.put("AUTOMATEDSTEPTYPEDESCRIPTION", "");
		automatedworkStepParams.put("AUTOMATEDSTEPDESCRIPTION", "");
		
		Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT DESCRIPTION AS AUTOMATEDSTEPTYPEDESCRIPTION FROM <DBUSER>.AUTOMATION_STEP_TYPE WHERE ID=:AUTOMATEDSTEPTYPE", automatedworkStepParams, null);
		
		Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT DESCRIPTION AS AUTOMATEDSTEPDESCRIPTION FROM <DBUSER>.AUTOMATION_STEP WHERE ID=:AUTOMATEDSTEPID", automatedworkStepParams, null);
		
		if(!automatedworkStepParams.get("AUTOMATEDSTEPTYPEDESCRIPTION").equals("")){
			automatedworkStepParams.put("AUTOMATEDSTEPTYPEDESCRIPTION",automatedworkStepParams.get("AUTOMATEDSTEPTYPEDESCRIPTION").replaceAll("[ ]", ""));
			Method editStepMethod=inovo.adhoc.AdhocUtils.findMethod(this.getClass().getDeclaredMethods(),"editstep_"+automatedworkStepParams.get("AUTOMATEDSTEPTYPEDESCRIPTION"), false);
			if(editStepMethod!=null){
				this.showDialog("", ("contentid=editautomatedworkstep|BUTTON:ACTIVATE=command=activatestep_"+automatedworkStepParams.get("AUTOMATEDSTEPTYPEDESCRIPTION")+"|BUTTON:UPDATE=command=updatestep_"+automatedworkStepParams.get("AUTOMATEDSTEPTYPEDESCRIPTION")+"|BUTTON:CANCEL=").split("[|]"));
				this.replaceComponentContent("editautomatedworkstep");
					this.startElement("div", null, true);
						this.fieldHidden("EDITAUTOMATEDSTEPID", automatedworkStepParams.get("AUTOMATEDSTEPID"));
						this.fieldHidden("EDITAUTOMATEDSTEPTYPE", automatedworkStepParams.get("AUTOMATEDSTEPID"));
						this.startElement("div", "class=ui-widget-header".split("[|]"), true);
						this.respondString("EDIT AUTOMATED STEP - "); this.respondString(automatedworkStepParams.get("AUTOMATEDSTEPDESCRIPTION"));
						this.endElement("div", true);
						editStepMethod.invoke(this, new Object[]{automatedworkStepParams.get("AUTOMATEDSTEPID")});
					this.endElement("div", true);
				this.endReplaceComponentContent();
			}
		}
		automatedworkStepParams.clear();
	}
	
	private HashMap<String,String> _importfilesettings=new HashMap<String,String>();
	
	public void activatestep_importfile() throws Exception{
		this._importfilesettings.clear();
		this.importRequestParametersIntoMap(this._importfilesettings, "EDITAUTOMATEDSTEPID,COMADELIM,INLINESQLCOMMAND,SOURCEPATH,CURRENTFILEFIELDS,FILELOOKUPMASK");
		Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE <DBUSER>.AUTOMATION_STEP SET CAN_ACTIVATE=1 WHERE ID=:EDITAUTOMATEDSTEPID", this._importfilesettings, null);
	}
	
	public void editstep_importfile(String stepid) throws Exception{
		this._importfilesettings.put("SET_ID", stepid);
		this._importfilesettings.put("INLINESQLCOMMAND","");
		this._importfilesettings.put("COMADELIM",",");
		this._importfilesettings.put("SOURCEPATH","");
		this._importfilesettings.put("CURRENTFILEFIELDS","");
		this._importfilesettings.put("LOADNAMEMASK","");
		this._importfilesettings.put("FILELOOKUPMASK","");
		
		Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT * FROM <DBUSER>.AUTOMATED_STEP_TYPE_IMPORTFILE WHERE STEP_ID="+stepid, _importfilesettings, null);
		this.startTable(null);
			this.startRow(null);
				this.startCell(null);
				
					this.startTable(null);
						this.startRow(null);
							this.startColumn("font-size:0.8em");
								this.respondString("COLUMN DELIMITER");
							this.endColumn();
						this.endRow();
						this.startRow(null);
							this.startCell(null);
								this.fieldInput("COMADELIM", this._importfilesettings.get("COMADELIM"), "text", true, "size=1".split("[|]"));
							this.endColumn();
						this.endRow();
					this.endTable();
					
				this.endCell();
				
				this.startCell(null);
				
				this.startTable(null);
					this.startRow(null);
						this.startColumn("font-size:0.8em");
							this.respondString("INLINE SQLCOMMAND");
						this.endColumn();
					this.endRow();
					this.startRow(null);
						this.startCell(null);
							this.fieldInput("INLINESQLCOMMAND", this._importfilesettings.get("INLINESQLCOMMAND"), "multiline", true, "cols=50|rows=5".split("[|]"));
						this.endColumn();
					this.endRow();
				this.endTable();
				
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
				
					this.startTable(null);
						this.startRow(null);
							this.startColumn("font-size:0.8em");
								this.respondString("FILE PICKUP PATH");
							this.endColumn();
						this.endRow();
						this.startRow(null);
							this.startCell(null);
								this.fieldInput("SOURCEPATH", this._importfilesettings.get("SOURCEPATH"), "multiline", true, "cols=50|rows=5".split("[|]"));
							this.endColumn();
						this.endRow();
					this.endTable();
				
				this.endCell();
				this.startCell(null);
				
					this.startTable(null);
						this.startRow(null);
							this.startColumn("font-size:0.8em");
								this.respondString("FILE FIELDS");
							this.endColumn();
						this.endRow();
						this.startRow(null);
							this.startCell(null);
								this.fieldInput("CURRENTFILEFIELDS", this._importfilesettings.get("CURRENTFILEFIELDS"), "multiline", true, "cols=50|rows=5".split("[|]"));
							this.endColumn();
						this.endRow();
					this.endTable();
				
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startCell("colspan=2".split("[|]"));
				
					this.startTable(null);
						this.startRow(null);
							this.startColumn("font-size:0.8em");
								this.respondString("FILE NAME MASK");
							this.endColumn();
						this.endRow();
						this.startRow(null);
							this.startCell(null);
								this.fieldInput("FILELOOKUPMASK", this._importfilesettings.get("FILELOOKUPMASK"), "text", true, "size=100".split("[|]"));
							this.endColumn();
						this.endRow();
					this.endTable();
				
				this.endCell();
			this.endRow();
		this.endTable();
	}
	
	
	public void updatestep_importfile() throws Exception{
		this._importfilesettings.clear();
		this.importRequestParametersIntoMap(this._importfilesettings, "EDITAUTOMATEDSTEPID,COMADELIM,INLINESQLCOMMAND,SOURCEPATH,CURRENTFILEFIELDS,FILELOOKUPMASK");
		this._importfilesettings.put("FILEIMPORTSTEPCOUNT","0");
		
		Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT COUNT(*) AS FILEIMPORTSTEPCOUNT FROM <DBUSER>.AUTOMATED_STEP_TYPE_IMPORTFILE WHERE STEP_ID=:EDITAUTOMATEDSTEPID", this._importfilesettings, null);
		if(this._importfilesettings.get("FILEIMPORTSTEPCOUNT").equals("0")){
			Database.executeDBRequest(null, "AUTOMATEDWORK", "INSERT INTO AUTOMATED_STEP_TYPE_IMPORTFILE (STEP_ID,COMADELIM,INLINESQLCOMMAND,SOURCEPATH,CURRENTFILEFIELDS,FILELOOKUPMASK) SELECT :EDITAUTOMATEDSTEPID,:COMADELIM,:INLINESQLCOMMAND,:SOURCEPATH,:CURRENTFILEFIELDS,:FILELOOKUPMASK WHERE (SELECT COUNT(*) FROM <DBUSER>.AUTOMATED_STEP_TYPE_IMPORTFILE WHERE STEP_ID=:EDITAUTOMATEDSTEPID)=0", this._importfilesettings, null);
		}
		else{
			Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE AUTOMATED_STEP_TYPE_IMPORTFILE SET COMADELIM=:COMADELIM ,INLINESQLCOMMAND=:INLINESQLCOMMAND,SOURCEPATH=:SOURCEPATH,CURRENTFILEFIELDS=:CURRENTFILEFIELDS,FILELOOKUPMASK=:FILELOOKUPMASK WHERE STEP_ID=:EDITAUTOMATEDSTEPID", this._importfilesettings, null);
		}
	}
	
	private HashMap<String,String> _exportfilesettings=new HashMap<String,String>();
	
	public void activatestep_exportfile() throws Exception{
		this._exportfilesettings.clear();
		this.importRequestParametersIntoMap(this._exportfilesettings, "EDITAUTOMATEDSTEPID,COMADELIM,INLINESQLCOMMAND,DESTINATIONPATH,CURRENTFILEFIELDS,FILEEXPORTMASK");
		Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE <DBUSER>.AUTOMATION_STEP SET CAN_ACTIVATE=1 WHERE ID=:EDITAUTOMATEDSTEPID", this._exportfilesettings, null);
	}
	
	public void editstep_exportfile(String stepid) throws Exception{
		this._exportfilesettings.put("SET_ID", stepid);
		this._exportfilesettings.put("INLINESQLCOMMAND","");
		this._exportfilesettings.put("COMADELIM",",");
		this._exportfilesettings.put("DESTINATIONPATH","");
		this._exportfilesettings.put("CURRENTFILEFIELDS","");
		this._exportfilesettings.put("FILEEXPORTMASK","");
		
		Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT * FROM <DBUSER>.AUTOMATED_STEP_TYPE_EXPORTFILE WHERE STEP_ID="+stepid, _exportfilesettings, null);
		this.startTable(null);
			this.startRow(null);
				this.startCell(null);
				
					this.startTable(null);
						this.startRow(null);
							this.startColumn("font-size:0.8em");
								this.respondString("COLUMN DELIMITER");
							this.endColumn();
						this.endRow();
						this.startRow(null);
							this.startCell(null);
								this.fieldInput("COMADELIM", this._exportfilesettings.get("COMADELIM"), "text", true, "size=1".split("[|]"));
							this.endColumn();
						this.endRow();
					this.endTable();
					
				this.endCell();
				
				this.startCell(null);
				
				this.startTable(null);
					this.startRow(null);
						this.startColumn("font-size:0.8em");
							this.respondString("INLINE SQLCOMMAND");
						this.endColumn();
					this.endRow();
					this.startRow(null);
						this.startCell(null);
							this.fieldInput("INLINESQLCOMMAND", this._exportfilesettings.get("INLINESQLCOMMAND"), "multiline", true, "cols=50|rows=5".split("[|]"));
						this.endColumn();
					this.endRow();
				this.endTable();
				
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
				
					this.startTable(null);
						this.startRow(null);
							this.startColumn("font-size:0.8em");
								this.respondString("FILE EXPORT PATH");
							this.endColumn();
						this.endRow();
						this.startRow(null);
							this.startCell(null);
								this.fieldInput("DESTINATIONPATH", this._exportfilesettings.get("DESTINATIONPATH"), "multiline", true, "cols=50|rows=5".split("[|]"));
							this.endColumn();
						this.endRow();
					this.endTable();
				
				this.endCell();
				this.startCell(null);
				
					this.startTable(null);
						this.startRow(null);
							this.startColumn("font-size:0.8em");
								this.respondString("FILE EXPORT FIELDS");
							this.endColumn();
						this.endRow();
						this.startRow(null);
							this.startCell(null);
								this.fieldInput("CURRENTFILEFIELDS", this._exportfilesettings.get("CURRENTFILEFIELDS"), "multiline", true, "cols=50|rows=5".split("[|]"));
							this.endColumn();
						this.endRow();
					this.endTable();
				
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startCell("colspan=2".split("[|]"));
				
					this.startTable(null);
						this.startRow(null);
							this.startColumn("font-size:0.8em");
								this.respondString("FILE NAME MASK");
							this.endColumn();
						this.endRow();
						this.startRow(null);
							this.startCell(null);
								this.fieldInput("FILEEXPORTMASK", this._exportfilesettings.get("FILEEXPORTMASK"), "text", true, "size=100".split("[|]"));
							this.endColumn();
						this.endRow();
					this.endTable();
				
				this.endCell();
			this.endRow();
		this.endTable();
	}
	
	
	public void updatestep_exportfile() throws Exception{
		this._exportfilesettings.clear();
		this.importRequestParametersIntoMap(this._exportfilesettings, "EDITAUTOMATEDSTEPID,COMADELIM,INLINESQLCOMMAND,DESTINATIONPATH,CURRENTFILEFIELDS,FILEEXPORTMASK");
		this._importfilesettings.put("FILEEXPORTSTEPCOUNT","0");
		
		Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT COUNT(*) AS FILEEXPORTSTEPCOUNT FROM <DBUSER>.AUTOMATED_STEP_TYPE_EXPORTFILE WHERE STEP_ID=:EDITAUTOMATEDSTEPID", this._exportfilesettings, null);
		if(this._importfilesettings.get("FILEEXPORTSTEPCOUNT").equals("0")){
			Database.executeDBRequest(null, "AUTOMATEDWORK", "INSERT INTO AUTOMATED_STEP_TYPE_EXPORTFILE (STEP_ID,COMADELIM,INLINESQLCOMMAND,DESTINATIONPATH,CURRENTFILEFIELDS,FILEEXPORTMASK) SELECT :EDITAUTOMATEDSTEPID,:COMADELIM,:INLINESQLCOMMAND,:DESTINATIONPATH,:CURRENTFILEFIELDS,:FILEEXPORTMASK WHERE (SELECT COUNT(*) FROM <DBUSER>.AUTOMATED_STEP_TYPE_EXPORTFILE WHERE STEP_ID=:EDITAUTOMATEDSTEPID)=0", this._exportfilesettings, null);
		}
		else{
			Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE AUTOMATED_STEP_TYPE_EXPORTFILE SET COMADELIM=:COMADELIM ,INLINESQLCOMMAND=:INLINESQLCOMMAND,DESTINATIONPATH=:DESTINATIONPATH,CURRENTFILEFIELDS=:CURRENTFILEFIELDS,FILEEXPORTMASK=:FILEEXPORTMASK WHERE STEP_ID=:EDITAUTOMATEDSTEPID", this._exportfilesettings, null);
		}
	}
	
	private HashMap<String,String> _sqlsettings=new HashMap<String,String>();
	
	public void activatestep_sql() throws Exception{
		this._exportfilesettings.clear();
		this.importRequestParametersIntoMap(this._sqlsettings, "EDITAUTOMATEDSTEPID");
		Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE <DBUSER>.AUTOMATION_STEP SET CAN_ACTIVATE=1 WHERE ID=:EDITAUTOMATEDSTEPID", this._sqlsettings, null);
	}
	
	public void editstep_sql(String stepid) throws Exception{
		this._sqlsettings.put("SET_ID", stepid);
		this._sqlsettings.put("SQLCOMMAND","");
		
		Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT * FROM <DBUSER>.AUTOMATED_STEP_TYPE_SQL WHERE STEP_ID="+stepid, _sqlsettings, null);
		this.startTable(null);
			this.startRow(null);
				this.startCell(null);
				
				this.startTable(null);
					this.startRow(null);
						this.startColumn("font-size:0.8em");
							this.respondString("SQLCOMMAND");
						this.endColumn();
					this.endRow();
					this.startRow(null);
						this.startCell(null);
							this.fieldInput("SQLCOMMAND", this._sqlsettings.get("SQLCOMMAND"), "multiline", true, "cols=100|rows=5".split("[|]"));
						this.endColumn();
					this.endRow();
				this.endTable();
				
				this.endCell();
			this.endRow();
		this.endTable();
	}
	
	
	public void updatestep_sql() throws Exception{
		this._sqlsettings.clear();
		this.importRequestParametersIntoMap(this._sqlsettings, "EDITAUTOMATEDSTEPID,SQLCOMMAND");
		this._sqlsettings.put("SQLSTEPCOUNT","0");
		
		Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT COUNT(*) AS SQLSTEPCOUNT FROM <DBUSER>.AUTOMATED_STEP_TYPE_SQL WHERE STEP_ID=:EDITAUTOMATEDSTEPID", this._sqlsettings, null);
		if(this._sqlsettings.get("SQLSTEPCOUNT").equals("0")){
			Database.executeDBRequest(null, "AUTOMATEDWORK", "INSERT INTO AUTOMATED_STEP_TYPE_SQL (STEP_ID,SQLCOMMAND) SELECT :EDITAUTOMATEDSTEPID,:SQLCOMMAND WHERE (SELECT COUNT(*) FROM <DBUSER>.AUTOMATED_STEP_TYPE_SQL WHERE STEP_ID=:EDITAUTOMATEDSTEPID)=0", this._sqlsettings, null);
		}
		else{
			Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE AUTOMATED_STEP_TYPE_SQL SET SQLCOMMAND=:SQLCOMMAND WHERE STEP_ID=:EDITAUTOMATEDSTEPID", this._sqlsettings, null);
		}
	}
	
	private TreeMap<Integer, ArrayList<String>> _automatedStepFlowData=new TreeMap<Integer, ArrayList<String>>();
	public void readAutomatedStepFlowData(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns) throws Exception{
		if(rowIndex==0){
			_automatedStepFlowData.clear();
			return;
		}
		_automatedStepFlowData.put((Integer)Integer.parseInt(rowData.get(5)),new ArrayList<String>(rowData));
	}
	
	HashMap<String,String> _automatedworksteptypes=new HashMap<String, String>();
	public void addautomatedworkstep() throws Exception{
		this.showDialog("", "contentid=addautomatedworkstep|title=ADD STEP|BUTTON:ADD STEP=command=addingsteptype|BUTTON:CANCEL=".split("[|]"));
		this.replaceComponentContent("addautomatedworkstep");
			_automatedworksteptypes.clear();
			Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT ID,NAME,DESCRIPTION FROM <DBUSER>.AUTOMATION_STEP_TYPE", null, this,"readAutomatedStepTypes");
			this.startTable(null);
				this.startRow(null);
					this.startColumn("font-size:0.8em");
					this.endColumn();
					this.startColumn("font-size:0.8em");
						this.respondString("STEP [TYPE]");
					this.endColumn();
					this.startCell(null);
						this.fieldInput("NEWAUTOMATEDSTEP", "", "select", true, null,_automatedworksteptypes);
					this.endCell();
				this.endRow();
			this.endTable();
		this.endReplaceComponentContent();
	}
	
	public void addingsteptype() throws Exception{
		HashMap<String,String> addingsteptypeparams=new HashMap<String, String>();
		addingsteptypeparams.put("NEWAUTOMATEDSTEP", this.requestParameter("NEWAUTOMATEDSTEP"));
		ArrayList<String> addingsteptypeerrors=new ArrayList<String>();
		if(addingsteptypeparams.get("NEWAUTOMATEDSTEP").equals("")){
			addingsteptypeerrors.add("NO STEP TYPE SELECTED");
		}
		else{
			addingsteptypeparams.put("AUTOMATEDID",this.requestParameter("AUTOMATEDID"));
			addingsteptypeparams.put("STARTSTEPCOUNT", "0");
			Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT COUNT(*) AS STARTSTEPCOUNT FROM <DBUSER>.AUTOMATION_STEP WHERE AUTOMATIONID=:AUTOMATEDID AND STEP_TYPE_ID IN (SELECT ID FROM <DBUSER>.AUTOMATION_STEP_TYPE WHERE UPPER(AUTOMATION_STEP_TYPE.NAME)='START')", addingsteptypeparams, null);
			if(addingsteptypeparams.get("STARTSTEPCOUNT").equals("0")){
				Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT COUNT(*) AS STARTSTEPCOUNT FROM <DBUSER>.AUTOMATION_STEP_TYPE WHERE UPPER(AUTOMATION_STEP_TYPE.NAME)='START' AND ID=:NEWAUTOMATEDSTEP", addingsteptypeparams, null);
				if(addingsteptypeparams.get("STARTSTEPCOUNT").equals("1")){
					Database.executeDBRequest(null,"AUTOMATEDWORK","INSERT INTO <DBUSER>.AUTOMATION_STEP (AUTOMATIONID,NAME,DESCRIPTION,STEP_TYPE_ID) SELECT :AUTOMATEDID,NAME,DESCRIPTION,:NEWAUTOMATEDSTEP FROM <DBUSER>.AUTOMATION_STEP_TYPE WHERE ID=:NEWAUTOMATEDSTEP",addingsteptypeparams,null);
					this.replaceComponentContent("automatedstepsflow");
						this.automatedstepsflow();
					this.endReplaceComponentContent();
				}
				else{
					addingsteptypeerrors.add("FIRST STEP MUST BE A START STEP");
				}
			}
			else{
				Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT COUNT(*) AS STARTSTEPCOUNT FROM <DBUSER>.AUTOMATION_STEP_TYPE WHERE UPPER(AUTOMATION_STEP_TYPE.NAME)='START' AND ID=:NEWAUTOMATEDSTEP", addingsteptypeparams, null);
				if(addingsteptypeparams.get("STARTSTEPCOUNT").equals("1")){
					addingsteptypeerrors.add("START STEP ALREADY ALLOCATED");
				}
				else{
					Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT COUNT(*) AS STARTSTEPCOUNT FROM <DBUSER>.AUTOMATION_STEP WHERE AUTOMATIONID=:AUTOMATEDID AND STEP_TYPE_ID IN (SELECT ID FROM <DBUSER>.AUTOMATION_STEP_TYPE WHERE UPPER(AUTOMATION_STEP_TYPE.NAME)='START')", addingsteptypeparams, null);
					if(addingsteptypeparams.get("STARTSTEPCOUNT").equals("0")){
						addingsteptypeerrors.add("NO START STEP ALLOCATED");
					}
					else{
						Database.executeDBRequest(null,"AUTOMATEDWORK","INSERT INTO <DBUSER>.AUTOMATION_STEP (AUTOMATIONID,NAME,DESCRIPTION,STEP_TYPE_ID,PREV_STEP_ID,NEXT_STEP_ID) SELECT :AUTOMATEDID,NAME,DESCRIPTION,:NEWAUTOMATEDSTEP,ISNULL((SELECT MAX(ID) FROM <DBUSER>.AUTOMATION_STEP WHERE AUTOMATIONID=:AUTOMATEDID AND NEXT_STEP_ID=0),0),0 FROM <DBUSER>.AUTOMATION_STEP_TYPE WHERE ID=:NEWAUTOMATEDSTEP",addingsteptypeparams,null);
						addingsteptypeparams.put("PREVAUTOMATIONSTEPID", "0");
						
						Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT ISNULL(MAX(ID),0) AS PREVAUTOMATIONSTEPID FROM <DBUSER>.AUTOMATION_STEP WHERE ID < (SELECT ISNULL(MAX(ID),0) FROM <DBUSER>.AUTOMATION_STEP WHERE AUTOMATIONID=:AUTOMATEDID) AND AUTOMATIONID=:AUTOMATEDID", addingsteptypeparams, null);
						
						if(!addingsteptypeparams.get("PREVAUTOMATIONSTEPID").equals("0")){
							Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE <DBUSER>.AUTOMATION_STEP SET NEXT_STEP_ID=(SELECT ISNULL(MAX(ID),0) FROM <DBUSER>.AUTOMATION_STEP WHERE AUTOMATIONID=:AUTOMATEDID) WHERE ID=:PREVAUTOMATIONSTEPID", addingsteptypeparams, null);
						}
						
						
						this.replaceComponentContent("automatedstepsflow");
							this.automatedstepsflow();
						this.endReplaceComponentContent();
					}	
				}
			}
		}
		if(addingsteptypeerrors.isEmpty()){
			
		}
		else{
			this.showDialog("", "title=ADD STEP FAILED|contentid=addingsteptypeerrors".split("[|]"));
			this.replaceComponentContent("addingsteptypeerrors");
				this.startTable(null);
					while(!addingsteptypeerrors.isEmpty()){
						this.startRow(null);
							this.startCell(null);
								this.respondString(addingsteptypeerrors.remove(0));
							this.endCell();
						this.endRow();
					}
				this.endTable();
			this.endReplaceComponentContent();
		}
	}
	
	public void readAutomatedStepTypes(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns) throws Exception{
		if(rowIndex==0){
			this._automatedworksteptypes.clear();
			return;
		}
		this._automatedworksteptypes.put(rowData.get(0), rowData.get(1)+" ["+rowData.get(2)+"]");
	}
	
	public void disableautomatedwork() throws Exception{
		Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE <DBUSER>.AUTOMATION SET STATUS='D' WHERE ID="+this.requestParameter("AUTOMATEDID"), null, null);
		
		this.replaceComponentContent("automatedwork");
			this.selectAutomatedWork();
		this.endReplaceComponentContent();
	}
	
	public void enableautomatedwork() throws Exception{
		Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE <DBUSER>.AUTOMATION SET STATUS='E' WHERE ID="+this.requestParameter("AUTOMATEDID"), null, null);
		
		this.replaceComponentContent("automatedwork");
			this.selectAutomatedWork();
		this.endReplaceComponentContent();
	}
	
	public void addwork() throws Exception{
		this.showDialog("", "contentid=addworkdialog|title=ADD WORK|BUTTON:CONFIRM=command=confirmaddwork|BUTTON:CANCEL=".split("[|]"));
		this.replaceComponentContent("addworkdialog");
			this.startTable(null);
				this.startRow(null);
					this.startColumn("font-size:0.8em");
						this.respondString("NAME");
					this.endColumn();
					this.startCell(null);
						this.fieldInput("NEWWORKNAME", "", "text", true, null);
					this.endCell();
				this.endRow();
				this.startRow(null);
					this.startColumn("font-size:0.8em");
						this.respondString("DESCRIPTION");
					this.endColumn();
					this.startCell(null);
						this.fieldInput("NEWWORKDESCRIPTION", "", "text", true, null);
					this.endCell();
				this.endRow();
			this.endTable();
		this.endReplaceComponentContent();
	}
	
	public void confirmaddwork() throws Exception{
		ArrayList<String> addworkerrors=new ArrayList<String>();
		if(this.requestParameter("NEWWORKNAME").equals("")){
			addworkerrors.add("NO WORK NAME ENTERED");
		}
		if(addworkerrors.isEmpty()){
			HashMap<String,String> addworkparams=new HashMap<String,String>();
			addworkparams.put("NEWWORKNAME", this.requestParameter("NEWWORKNAME"));
			addworkparams.put("NEWWORKDESCRIPTION", this.requestParameter("NEWWORKDESCRIPTION"));
			addworkparams.put("NEWWORKCOUNT", "0");
			Database.executeDBRequest(null,"AUTOMATEDWORK","SELECT COUNT(*) AS NEWWORKCOUNT FROM <DBUSER>.AUTOMATION WHERE UPPER(NAME) LIKE UPPER(:NEWWORKNAME)",addworkparams,this);
			if(!addworkparams.get("NEWWORKCOUNT").equals("0")){
				addworkerrors.add("WORK NAME ["+this.requestParameter("NEWWORKNAME").toUpperCase()+"] ALREADY USED");
			}
			else{
				if(addworkparams.get("NEWWORKDESCRIPTION").equals("")) addworkparams.put("NEWWORKDESCRIPTION", addworkparams.get("NEWWORKNAME"));
				Database.executeDBRequest(null,"AUTOMATEDWORK","INSERT INTO <DBUSER>.AUTOMATION (NAME,DESCRIPTION) SELECT UPPER(:NEWWORKNAME), UPPER(:NEWWORKDESCRIPTION) WHERE (SELECT COUNT(*) FROM <DBUSER>.AUTOMATION WHERE UPPER(NAME) LIKE UPPER(:NEWWORKNAME))=0",addworkparams,null);
				this.replaceComponentContent("automatedworks");
					this.automatedWorks();
				this.endReplaceComponentContent();
			}
		}
		if(!addworkerrors.isEmpty()){
			showDialog("", "contentid=addworkerrorsdialog|title=ADD WORK ERRORS".split("[|]"));
			this.replaceComponentContent("addworkerrorsdialog");
				this.startTable(null);
					while(!addworkerrors.isEmpty()){
						this.startRow(null);
							this.startCell(null);
								this.respondString(addworkerrors.remove(0));
							this.endCell();
						this.endRow();
					}
				this.endTable();
			this.endReplaceComponentContent();
		}
	}
}
