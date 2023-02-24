package lancet.leads.automation;

import inovo.db.Database;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountsLeadsExportQueue{
	
	private static AccountsLeadsExportQueue _accountLeadsExportQueue=null;
	
	private ExecutorService _exportAccounts=Executors.newCachedThreadPool();
	private boolean _killExportQueue;
	private Object _exportCheckLock=new Object();
	public AccountsLeadsExportQueue(){
		this._exportAccounts.execute(new Runnable() {
			public void run() {
				while(!_killExportQueue){
					synchronized (_exportCheckLock) {
						try {
							_exportCheckLock.wait(10*1024);
						} catch (InterruptedException e) {
						}
					}
					try {
						checkExportFilesExists();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private ExportAccountFiles _exportAccountFiles=new ExportAccountFiles();
	public void checkExportFilesExists() throws Exception{
		Database.executeDBRequest(null, "LANCETLEADSAUTOMATION", "UPDATE <DBUSER>.UNLOAD_ACCOUNTFILE SET REQUESTHANDLEFLAG=2 WHERE REQUESTHANDLEFLAG=1", null, null);
		
		_exportAccountFiles.startExportingFiles();
		Database.executeDBRequest(null, "LANCETLEADSAUTOMATION", "SELECT ID,FILEPATH,UNLOAD_FILE_REQUEST FROM <DBUSER>.UNLOAD_ACCOUNTFILE WHERE REQUESTHANDLEFLAG=2",null, _exportAccountFiles);
		
	}
	
	public void killExportQueue(){
		this._killExportQueue=true;
		this._exportAccounts.shutdown();
		while(!this._exportAccounts.isShutdown()){
			try {
				_exportCheckLock.wait(100);
			} catch (InterruptedException e) {
			}
		}
	}

	public static AccountsLeadsExportQueue accountsLeadsExportQueue() {
		if(_accountLeadsExportQueue==null){
			_accountLeadsExportQueue=new AccountsLeadsExportQueue();
		}
		return _accountLeadsExportQueue;
	}
}
