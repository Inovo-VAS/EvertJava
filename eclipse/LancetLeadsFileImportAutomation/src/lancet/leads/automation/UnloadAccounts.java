package lancet.leads.automation;

import inovo.db.Database;

import java.util.ArrayList;
import java.util.HashMap;

public class UnloadAccounts {
	
	private String _fileRequestDescription="";
	
	
	public UnloadAccounts(){
		this._unloadAccountsRef=this;
	}
	
	public void setFileRequestRescription(String fileRequestDescription){
		this._fileRequestDescription=fileRequestDescription;
	}
	
	private String _fileId="0";
	public void setfileId(String fileId){
		this._fileId=fileId;
	}
	
	private UnloadAccounts _unloadAccountsRef=null;
	public void readRowData(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> columns){
		if(rowIndex==0) return;
		HashMap<String,String> unloadAccounsInfo=Database.rowData(columns, rowData);
		unloadAccounsInfo.put("UNLOADREQUESTDESCRIPTION", _fileRequestDescription);
		unloadAccounsInfo.put("UNLOADPROJECTREQUEST", _projectRequest);
		unloadAccounsInfo.put("UNLOADFILEID", _fileId);
		
		if(unloadAccounsInfo.containsKey("ACCOUNT_NO")){
			try {
				Database.executeDBRequest(null, "LANCETLEADSAUTOMATION", "INSERT INTO <DBUSER>.UNLOAD_ACCOUNTS (ACCOUNT_NO,RECORDHANDLEFLAG,UNLOAD_PROJECT_NAME,UNLOADREASON,UNLOAD_FILEID) SELECT :ACCOUNT_NO,1,:UNLOADPROJECTREQUEST,:UNLOADREQUESTDESCRIPTION,:UNLOADFILEID"	,unloadAccounsInfo ,null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		unloadAccounsInfo.clear();
		unloadAccounsInfo=null;
		
		synchronized (_unloadAccountsRef) {
			try {
				_unloadAccountsRef.wait(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String _projectRequest="";
	public void setProjectRequest(String projectRequest) {
		this._projectRequest=projectRequest;
	}
}
