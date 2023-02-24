package inovo.automated.work;

import inovo.db.Database;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

public class AutomatedWorker implements Runnable {
	
	private static long _automationworkersessioncount=0;
	private static String _automationworkersessiondatestamp="";
	
	private AutomatedWorkManager _automationManager=null;
	private HashMap<String, String> _automationSettings=new HashMap<String, String>();
	public AutomatedWorker(AutomatedWorkManager automationManager,HashMap<String,String> automationSettings){
		this._automationManager=automationManager;
		this._automationSettings.putAll(automationSettings);
	}

	private boolean _shutdownWorker=false;
	
	private boolean _canPerform=false;
	@Override
	public void run() {
		this.setScheduling();
		try {
			this.loadAutomationSteps();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Calendar currentTestStap=null;
		while(!this._shutdownWorker&&!_automationManager._shutdownWorkManager){
			if(!this._dailyStamps.isEmpty()){
				_canPerform=false;
				if(this._dailyStamps.get(0)<(currentTestStap=Calendar.getInstance()).getTimeInMillis()+10){
					while(!this._dailyStamps.isEmpty()){
						if(this._dailyStamps.get(0)<currentTestStap.getTimeInMillis()){
							this._dailyStamps.remove(0);
							_canPerform=true;
						}
						else{
							break;
						}
					}
				}
				if(_canPerform){
					_canPerform=false;
					this.performAutomatedWorker();
				}
			}
			else{
				try {
					this.resetDailyStamps();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			
		}
		_automationManager._automatedwork.remove(_automationSettings.get("AUTOMATEDID")).cleanupWorker();
	}
	
	private ArrayList<Long> _dailyStamps=new ArrayList<Long>();
	private Calendar _startUpStamp=Calendar.getInstance();
	private void setScheduling() {
		
		try {
			this.resetDailyStamps();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void resetDailyStamps() throws Exception{
		Calendar currentEndOfDayStamp=Calendar.getInstance();
		currentEndOfDayStamp.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())+" 23:59:59.999"));
		if(this._startUpStamp.getTimeInMillis()<currentEndOfDayStamp.getTimeInMillis()){
			this._startUpStamp.setTimeInMillis(currentEndOfDayStamp.getTimeInMillis());
			Calendar currentStamp=Calendar.getInstance();
			_dailyStamps.clear();
			Calendar nextTimeStamp=Calendar.getInstance();
			if(this._automationSettings.get("SCHEDULETYPE").equals("daily")){
				
				for(String timeStamp:this._automationSettings.get("SCHEDULE").split("[,]")){
					nextTimeStamp.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(new SimpleDateFormat("yyyy-MM-dd").format(this._startUpStamp.getTime())+" "+timeStamp+".000"));
					_dailyStamps.add(nextTimeStamp.getTimeInMillis());
				}
			}
			else{
				if(_dailyStamps.isEmpty()){
					long interval=this._automationSettings.get("SCHEDULETYPE").equals("seconds")?(nextTimeStamp.getTimeInMillis()+Long.parseLong(this._automationSettings.get("SCHEDULE"))*1000):this._automationSettings.get("SCHEDULETYPE").equals("minutes")?(nextTimeStamp.getTimeInMillis()+Long.parseLong(this._automationSettings.get("SCHEDULE"))*(60*1000)):this._automationSettings.get("SCHEDULETYPE").equals("hours")?(nextTimeStamp.getTimeInMillis()+Long.parseLong(this._automationSettings.get("SCHEDULE"))*(60*60*1000)):(nextTimeStamp.getTimeInMillis()+Long.parseLong(this._automationSettings.get("SCHEDULE"))*1000);
					this._dailyStamps.add((Long)interval);
				}
			}
			
			while(!_dailyStamps.isEmpty()){
				if(_dailyStamps.get(0).longValue()<currentStamp.getTimeInMillis()+10){
					_dailyStamps.remove(0);
				}
				else{
					break;
				}
			}
		}
		if(this._startUpStamp.getTimeInMillis()==currentEndOfDayStamp.getTimeInMillis()){
			if(!this._automationSettings.get("SCHEDULETYPE").equals("daily")){
				if(_dailyStamps.isEmpty()){
					if(_dailyStamps.isEmpty()){
						Calendar nextTimeStamp=Calendar.getInstance();
						long interval=this._automationSettings.get("SCHEDULETYPE").equals("seconds")?(nextTimeStamp.getTimeInMillis()+Long.parseLong(this._automationSettings.get("SCHEDULE"))*1000):this._automationSettings.get("SCHEDULETYPE").equals("minutes")?(nextTimeStamp.getTimeInMillis()+Long.parseLong(this._automationSettings.get("SCHEDULE"))*(60*1000)):this._automationSettings.get("SCHEDULETYPE").equals("hours")?(nextTimeStamp.getTimeInMillis()+Long.parseLong(this._automationSettings.get("SCHEDULE"))*(60*60*1000)):(nextTimeStamp.getTimeInMillis()+Long.parseLong(this._automationSettings.get("SCHEDULE"))*1000);
						this._dailyStamps.add((Long)interval);
					}
				}
			}
		}
		else{
			if(this._startUpStamp.getTimeInMillis()<currentEndOfDayStamp.getTimeInMillis()){
				this._startUpStamp.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(new SimpleDateFormat("yyyy-MM-dd").format(this._startUpStamp.getTime())+" 00:00:00.000"));
				this.resetDailyStamps();
			}
		}
	}
	
	private static long nextAutomationWorkerSessionStamp(){
		String nextAutomationWorkerSessionStamp="";
		String datestamp=new SimpleDateFormat("yyyyMMdd").format(new Date());
		if(!datestamp.equals(_automationworkersessiondatestamp)){
			Calendar startCal=Calendar.getInstance();
			try {
				startCal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())+" 00:00:00.000"));
			} catch (ParseException e) {
			}
			_automationworkersessioncount=Calendar.getInstance().getTimeInMillis()-startCal.getTimeInMillis();
			_automationworkersessiondatestamp=datestamp;
		}
		_automationworkersessioncount++;
		nextAutomationWorkerSessionStamp=datestamp+String.valueOf(_automationworkersessioncount);
		
		return Long.parseLong(nextAutomationWorkerSessionStamp);
	}

	public void performAutomatedWorker() {
		if(this._automationSteps.isEmpty()){
			return;
		}
		long nextSessionKey=nextAutomationWorkerSessionStamp();
		this._automationSettings.put("SESSIONKEY", String.valueOf(nextSessionKey));
		for (Integer stepIndex:this._automationSteps.keySet()){
			this._automationSteps.get((Integer)stepIndex).stepProperties().put("SESSIONKEY", this._automationSettings.get("SESSIONKEY"));
			this._automationSteps.get((Integer)stepIndex).performAutomationStep();
		}
	}

	private TreeMap<Integer,HashMap<String,String>> _automationStepsSettings=new TreeMap<Integer, HashMap<String,String>>();
	private TreeMap<Integer,AutomatedWorkerStep> _automationSteps=new TreeMap<Integer, AutomatedWorkerStep>();
	
	private void loadAutomationSteps() throws Exception{
		Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT AUTOMATION_STEP.ID AS STEPID,AUTOMATION_STEP.NAME AS STEPNAME,AUTOMATION_STEP.DESCRIPTION AS STEPDESCRIPTION,AUTOMATION_STEP_TYPE.NAME AS STEPTYPENAME,AUTOMATION_STEP_TYPE.DESCRIPTION AS STEPTYPEDESCRIPTION FROM <DBUSER>.AUTOMATION_STEP INNER JOIN <DBUSER>.AUTOMATION_STEP_TYPE ON AUTOMATION_STEP_TYPE.ID=AUTOMATION_STEP.STEP_TYPE_ID AND AUTOMATIONID=:AUTOMATEDID",_automationSettings	, this,"readAutomationStepsData");
	}
	
	private HashMap<String,String> _workStepSettings=new HashMap<String, String>();
	private int _irowindex=0;
	public void readAutomationStepsData(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns){
		if(rowIndex==0) return;
		_workStepSettings.clear();
		_irowindex=0;
		while(_irowindex<rowColumns.size()){
			_workStepSettings.put(rowColumns.get(_irowindex), rowData.get(_irowindex++));
		}
		
		if(!_automationStepsSettings.containsKey((Integer)rowIndex)){
			try {
				Class<?> workstepclass=findAutomationWorkStepClass("/inovo/automated/work/steps",_workStepSettings.get("STEPTYPENAME")+"Step");
				if(workstepclass!=null){
					_automationStepsSettings.put((Integer)rowIndex, new HashMap<String,String>(this._workStepSettings));
					_automationSteps.put((Integer)rowIndex,(AutomatedWorkerStep)workstepclass.getConstructor(AutomatedWorker.class,HashMap.class).newInstance(this,_workStepSettings));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private Class<?> findAutomationWorkStepClass(String workerClassPath,String workerClassName) throws Exception{
		Class<?> findAutomationWorkStepClass=null;
		InputStream inputPaths=this.getClass().getResourceAsStream(workerClassPath);
		if(inputPaths!=null){
			StringBuilder tempClassName=new StringBuilder();
			for(byte b:inovo.adhoc.AdhocUtils.inputStreamToByteArray(inputPaths, 8192)){
				switch(b){
				case 10:
					if(tempClassName.substring(0,tempClassName.length()).toLowerCase().equals(workerClassName.toLowerCase()+".class")){
						workerClassPath=(workerClassPath+"/"+tempClassName.substring(0,tempClassName.length()-".class".length())).replaceAll("[////]", ".");
						tempClassName.setLength(0);
						return this.getClass().forName(workerClassPath.substring(1));
					}
					tempClassName.setLength(0);
					break;
				case 13:break;
				default:
					tempClassName.append((char)b);
				}
			}
		}
		return findAutomationWorkStepClass;
	}
	
	private void cleanupWorker() {
		_automationManager._workerKeysToShutdown.remove(_automationSettings.get("AUTOMATEDID"));
		System.out.println("CLEANEDUP:WORK["+this._automationSettings.get("AUTOMATEDID")+"]");
	}
	
	public void shutdownWorker(){
		this._shutdownWorker=true;
	}
	
	
}
