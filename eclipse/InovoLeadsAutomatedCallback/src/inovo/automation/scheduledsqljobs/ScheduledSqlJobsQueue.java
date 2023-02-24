package inovo.automation.scheduledsqljobs;

import inovo.db.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScheduledSqlJobsQueue implements Runnable{
	
	private static ScheduledSqlJobsQueue _scheduledSqlJobsQueue=null;
	
	private boolean _shutdownQueue=false;
	private boolean _totallyShutdown=false;
	private ExecutorService _sheduledJobsService=Executors.newCachedThreadPool();
	
	private HashMap<Integer,ScheduledSqlJob> _registeredScheduledJobs=new HashMap<Integer, ScheduledSqlJob>();
	
	private Object _scheduledSqlJobsQueueLock=new Object();
	
	public ScheduledSqlJobsQueue(){
		new Thread(this).start();
	}
	
	private void logDebug(String message){
		inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug(message);
	}
	
	public void readRowData(Integer rowindex,ArrayList<String> rowData,ArrayList<String> rowColumns){
		if(rowindex==0) return;
		
		HashMap<String,String> scheduledSqlJobSettings=Database.rowData(rowColumns, rowData);
		ScheduledSqlJob scheduledSqlJob=null;
		boolean shutdownSqlJob=false;
		synchronized (_registeredScheduledJobs) {
			if(_registeredScheduledJobs.containsKey((Integer)Integer.parseInt(scheduledSqlJobSettings.get("ID")))){
				if(scheduledSqlJobSettings.get("ACTIVE").equals("D")||_shutdownQueue){
					scheduledSqlJob=_registeredScheduledJobs.get((Integer)Integer.parseInt(scheduledSqlJobSettings.get("ID")));
					shutdownSqlJob=true;
				}
			}
			else if(!_registeredScheduledJobs.containsKey((Integer)Integer.parseInt(scheduledSqlJobSettings.get("ID")))&&scheduledSqlJobSettings.get("ACTIVE").equals("E")&&!_shutdownQueue){
				HashMap<String,String> sqlparams=new HashMap<String, String>();
				
				for(int paramindex=1;paramindex<=20;paramindex++){
					String paramfieldname=scheduledSqlJobSettings.get("PARAM"+String.valueOf(paramindex)+"NAME");
					if(!paramfieldname.equals("")){
						sqlparams.put(paramfieldname.toUpperCase(), scheduledSqlJobSettings.get("PARAM"+String.valueOf(paramindex)+"VALUE"));
					}
				}
				if(!scheduledSqlJobSettings.containsKey("DBALLIAS")){
					scheduledSqlJobSettings.put("DBALLIAS","SCHEDULEDSQLJOBS");
				}
				scheduledSqlJob=new ScheduledSqlJob(this, Integer.parseInt(scheduledSqlJobSettings.get("ID")),scheduledSqlJobSettings.get("DBALLIAS"), Integer.parseInt(scheduledSqlJobSettings.get("SECONDSINTERVAL"))*1024,sqlparams, scheduledSqlJobSettings.get("SQLCOMMAND"));
				sqlparams.clear();
				sqlparams=null;
				
				_registeredScheduledJobs.put((Integer)Integer.parseInt(scheduledSqlJobSettings.get("ID")), scheduledSqlJob);
			}
		}
		
		if(scheduledSqlJob!=null){
			if(shutdownSqlJob){
				scheduledSqlJob.shutdownJob();
			}
			else{
				this._sheduledJobsService.execute(scheduledSqlJob);
			}
		}
		
		scheduledSqlJobSettings.clear();
		scheduledSqlJobSettings=null;
	}

	@Override
	public void run() {
		this.logDebug("STARTED ScheduledSqlJobsQueue");
		synchronized (_scheduledSqlJobsQueueLock) {
			try {
				_scheduledSqlJobsQueueLock.wait((_shutdownQueue?2:10)*1024);
			} catch (InterruptedException e) {
			}
		}
		
		while(!_shutdownQueue){
			try {
				Database.executeDBRequest(null, "SCHEDULEDSQLJOBS", "SELECT ID,SQLCOMMAND,SECONDSINTERVAL,ACTIVE,PARAM1NAME ,PARAM1VALUE ,PARAM2NAME ,PARAM2VALUE ,PARAM3NAME ,PARAM3VALUE ,PARAM4NAME ,PARAM4VALUE ,PARAM5NAME ,PARAM5VALUE ,PARAM6NAME ,PARAM6VALUE ,PARAM7NAME ,PARAM7VALUE ,PARAM8NAME ,PARAM8VALUE ,PARAM9NAME ,PARAM9VALUE ,PARAM10NAME ,PARAM10VALUE ,PARAM11NAME ,PARAM11VALUE ,PARAM12NAME ,PARAM12VALUE ,PARAM13NAME ,PARAM13VALUE ,PARAM14NAME ,PARAM14VALUE ,PARAM15NAME ,PARAM15VALUE ,PARAM16NAME ,PARAM16VALUE ,PARAM17NAME ,PARAM17VALUE ,PARAM18NAME ,PARAM18VALUE ,PARAM19NAME ,PARAM19VALUE ,PARAM20NAME ,PARAM20VALUE FROM <DBUSER>.SCHEDULEDSQLJOBS", null, this);
			} catch (Exception e1) {
				
			}
			synchronized (_scheduledSqlJobsQueueLock) {
				try {
					_scheduledSqlJobsQueueLock.wait((_shutdownQueue?2:20)*1024);
				} catch (InterruptedException e) {
				}
			}
		}
		this.logDebug("STOPPED ScheduledSqlJobsQueue");
		this._totallyShutdown=true;
	}
	
	public static ScheduledSqlJobsQueue scheduledSqlJobsQueue(){
		if(_scheduledSqlJobsQueue==null){
			_scheduledSqlJobsQueue=new ScheduledSqlJobsQueue();
		}
		return _scheduledSqlJobsQueue;
	}

	public boolean removeSqlJob(ScheduledSqlJob scheduledSqlJob, int jobid) {
		boolean removedSqlJob=true;
		synchronized (_registeredScheduledJobs) {
			removedSqlJob=(scheduledSqlJob==_registeredScheduledJobs.remove((Integer)jobid));
		}
		return removedSqlJob;
	}

	public void shutdownSqlJobsQueue() {
		this._shutdownQueue=true;
		while(!_totallyShutdown){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		this._sheduledJobsService.shutdown();
		while(!this._sheduledJobsService.isShutdown()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	public void scheduledSqlJobRowDataException(
			ScheduledSqlJob scheduledSqlJob, int jobid,
			HashMap<String, String> scheduledSqlRowData, Exception ex) {
		this.logDebug("SQL JOB ROWDATA ERROR["+String.valueOf(jobid)+"]:"+ex.getMessage());
	}

	public void scheduledSqlJobException(ScheduledSqlJob scheduledSqlJob,
			int jobid, Exception ej) {
		this.logDebug("SQL JOB ERROR["+String.valueOf(jobid)+"]:"+ej.getMessage());
	}
}
