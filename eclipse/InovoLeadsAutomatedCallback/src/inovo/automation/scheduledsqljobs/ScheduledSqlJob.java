package inovo.automation.scheduledsqljobs;

import inovo.db.Database;

import java.util.ArrayList;
import java.util.HashMap;

public class ScheduledSqlJob implements Runnable{

	private int _interval=0;
	private int _jobid=0;
	private HashMap<String,String> _sqlParams=new HashMap<String, String>();
	private String _sqlCommand="";
	private ScheduledSqlJobsQueue _schedulingSqlJobsQueue=null;
	private Object _jobLock=new Object();
	private String _dballias="";
	public ScheduledSqlJob(ScheduledSqlJobsQueue schedulingSqlJobsQueue, int jobid,String dballias, int interval,HashMap<String, String> sqlparams,String sqlCommand){
		this._interval=interval;
		this._jobid=jobid;
		if(sqlparams!=null){
			this._sqlParams.putAll(sqlparams);
		}
		this._sqlCommand=sqlCommand;
		this._schedulingSqlJobsQueue=schedulingSqlJobsQueue;
		this._dballias=dballias;
	}
	
	private boolean _shutdownJob=false;
	@Override
	public void run() {
		while(!_shutdownJob){
			try{
				this.executeSqlJob();
			}
			catch(Exception e){
				this._schedulingSqlJobsQueue.scheduledSqlJobException(this, _jobid, e);
			}
			synchronized (_jobLock) {
				try {
					_jobLock.wait(_interval);
				} catch (InterruptedException e) {
				}
			}
		}
		this._schedulingSqlJobsQueue.removeSqlJob(this,this._jobid);
	}
	
	public void executeSqlJob() throws Exception{
		Database.executeDBRequest(null, _dballias, _sqlCommand=_sqlCommand.trim(), _sqlParams,(_sqlCommand.toUpperCase().startsWith("SELECT ")?this:null));
	}
	
	public void readRowData(Integer rowindex,ArrayList<String> rowData,ArrayList<String> rowColumns){
		if(rowindex==0) return;
		HashMap<String,String> scheduledSqlRowData=Database.rowData(rowColumns, rowData);
		try{
			this.executeSchduledRowData(scheduledSqlRowData);
		}
		catch(Exception ex){
			this._schedulingSqlJobsQueue.scheduledSqlJobRowDataException(this,_jobid,scheduledSqlRowData,ex);
		}
		scheduledSqlRowData.clear();
		scheduledSqlRowData=null;
	}

	public void executeSchduledRowData(
			HashMap<String, String> scheduledSqlRowData) throws Exception {
	}

	public void shutdownJob() {
		this._shutdownJob=true;
		synchronized (_jobLock) {
			this._jobLock.notifyAll();
		}
	}
}
