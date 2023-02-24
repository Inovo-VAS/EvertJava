package inovo.automated.work.steps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import inovo.automated.work.AutomatedWorker;
import inovo.automated.work.AutomatedWorkerStep;
import inovo.db.Database;

public class SQLStep extends AutomatedWorkerStep {

	public SQLStep(AutomatedWorker automatedWorker,
			HashMap<String, String> stepProperties) {
		super(automatedWorker, stepProperties);
	}

	@Override
	public void performAutomationStep() {
		this.stepProperties().put("SQLCOMMAND", "");
		
		try {
			Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT SQLCOMMAND FROM <DBUSER>.AUTOMATED_STEP_TYPE_SQL WHERE STEP_ID=:STEPID", this.stepProperties(), null);
			
		} catch (Exception e) {
		}
		
		try {
			this.processSql();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private HashMap<String,String> _sqlrequestsettings=new HashMap<String, String>();
	
	private FileOutputStream _fileOut=null;
	
	
	private void processSql() throws Exception{
		_sqlrequestsettings.clear();
		for(String fname:"STEPREQUESTID,STEPNAME,STEPDESCRIPTION,STEPTYPENAME,STEPTYPEDESCRIPTION,SQLCOMMAND,REQUESTCREATIONDATE,REQUESTHANDLEFLAG,REQUESTLASTHANDLEDATETIME,REQUESTSQLSTATUS,REQUESTSQLACTIVE,REQUESTSQLCOMPLETIONDATETIME,REQUESTSQLSTATUSDESCRIPTION".split("[,]")){
			_sqlrequestsettings.put(fname, "");
		}
		
		_sqlrequestsettings.putAll(this.stepProperties());
		
		Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT ID AS STEPREQUESTID,STEP_ID AS STEPID,STEPNAME,STEPDESCRIPTION,STEPTYPENAME,STEPTYPEDESCRIPTION,SQLCOMMAND,REQUESTCREATIONDATE,REQUESTHANDLEFLAG,REQUESTLASTHANDLEDATETIME,REQUESTSQLSTATUS,REQUESTSQLACTIVE,REQUESTSQLCOMPLETIONDATETIME,REQUESTSQLSTATUSDESCRIPTION FROM <DBUSER>.AUTOMATED_STEP_TYPE_SQL_REQUEST WHERE STEP_ID=:STEPID AND REQUESTSQLACTIVE=1", _sqlrequestsettings,null);
		
		if(!_sqlrequestsettings.get("STEPID").equals("")&&_sqlrequestsettings.get("STEPREQUESTID").equals("")){
			//:REQUESTFILEPATH,:REQUESTFILENAME
			Database.executeDBRequest(null, "AUTOMATEDWORK", "INSERT INTO <DBUSER>.AUTOMATED_STEP_TYPE_SQL_REQUEST (STEP_ID,STEPNAME,STEPDESCRIPTION,STEPTYPENAME,STEPTYPEDESCRIPTION,SQLCOMMAND,REQUESTCREATIONDATE,REQUESTHANDLEFLAG,REQUESTLASTHANDLEDATETIME,REQUESTSQLSTATUS,REQUESTSQLACTIVE,REQUESTSQLCOMPLETIONDATETIME,REQUESTSQLSTATUSDESCRIPTION,REQUESTSESSIONKEY)  SELECT :STEPID,:STEPNAME,:STEPDESCRIPTION,:STEPTYPENAME,:STEPTYPEDESCRIPTION,:SQLCOMMAND,GETDATE(),1,GETDATE(),1,0,NULL,'',:SESSIONKEY", _sqlrequestsettings,null);
			Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT MAX(ID) AS STEPREQUESTID FROM <DBUSER>.AUTOMATED_STEP_TYPE_SQL_REQUEST WHERE STEP_ID=:STEPID", this._sqlrequestsettings,null);
		}
		
		try{
			this._sqlrequestsettings.put("REQUESTSQLSTATUS", "0");
			this._sqlrequestsettings.put("REQUESTSQLSTATUSDESCRIPTION","");
			if(_sqlrequestsettings.get("SQLCOMMAND").equals("")){
				this._sqlrequestsettings.put("REQUESTSQLSTATUS", "3");
				this._sqlrequestsettings.put("REQUESTSQLSTATUSDESCRIPTION","EMPTY SQLCOMMAND");
			}
			else{
				Database.executeDBRequest(null, "AUTOMATEDWORK", _sqlrequestsettings.get("SQLCOMMAND"), null, null,null);
			}
			try {
				Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE <DBUSER>.AUTOMATED_STEP_TYPE_SQL_REQUEST SET REQUESTHANDLEFLAG=3,REQUESTLASTHANDLEDATETIME=GETDATE(),REQUESTSQLSTATUS=:REQUESTSQLSTATUS,REQUESTSQLSTATUSDESCRIPTION=:REQUESTSQLSTATUSDESCRIPTION,REQUESTSQLCOMPLETIONDATETIME=GETDATE(),REQUESTSQLACTIVE=0 WHERE ID=:STEPREQUESTID", this._sqlrequestsettings, null);
			} catch (Exception e) {
			}
		}
		catch(Exception e){
			try {
				this._sqlrequestsettings.put("REQUESTSQLSTATUS", "4");
				this._sqlrequestsettings.put("REQUESTSQLSTATUSDESCRIPTION", e.getMessage());
				Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE <DBUSER>.AUTOMATED_STEP_TYPE_SQL_REQUEST SET REQUESTHANDLEFLAG=3,REQUESTLASTHANDLEDATETIME=GETDATE(),REQUESTSQLSTATUS=:REQUESTSQLSTATUS,REQUESTSQLSTATUSDESCRIPTION=:REQUESTSQLSTATUSDESCRIPTION,REQUESTSQLCOMPLETIONDATETIME=GETDATE(),REQUESTSQLACTIVE=0 WHERE ID=:STEPREQUESTID", this._sqlrequestsettings, null);
			} catch (Exception ex) {
			}
		}
	}
}
