package inovo.automation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutomatedCalendar implements Runnable{
	
	private static Object _calendarsLock=new Object();
	private static ExecutorService _automatedCalendarExecutor=Executors.newCachedThreadPool();
	private static ArrayList<AutomatedCalendar> _automatedCalendars=new ArrayList<AutomatedCalendar>(); 
	private static HashMap<Integer,String> _automatedCalendarLabelIndexMap=new HashMap<Integer,String>(); 
	private static HashMap<String,AutomatedCalendar> _automatedCalendarLabelMap=new HashMap<String,AutomatedCalendar>();
	
	private ArrayList<AutomatedTask> _automatedTasks=new ArrayList<AutomatedTask>();
	private String _calendarName="";
	private ArrayList<Calendar> _calendar=new ArrayList<Calendar>();
	private ArrayList<Calendar> _calendarDaily=new ArrayList<Calendar>();
	private int _calendarDailyIndex=0;
	private boolean _calendarActive=true;
	
	public AutomatedCalendar(String calendarName,String intervalScript,AutomatedTask[]automatedTasks){
		this._calendarName=calendarName;
		for(AutomatedTask autoTask:automatedTasks){
			autoTask._automatedCalendar=this;
			this._automatedTasks.add(autoTask);
		}
		this.interprateIntervalScript(intervalScript);
	}
	
	public void interprateIntervalScript(String intervalScript) {
		intervalScript=intervalScript.toUpperCase();
		String todayDateString=new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		String intervalType=(intervalScript.substring(0,intervalScript.indexOf("=")));
		intervalScript=intervalScript.substring(intervalScript.indexOf("=")+1);
		if(intervalType.equals("INTERVAL:DAILY")){
			intervalScript+=",";
			while(intervalScript.indexOf(",")>-1){
				String inTime=intervalScript.substring(0,intervalScript.indexOf(","));
				if(!inTime.equals("")){
					if(inTime.length()<8){
						inTime=inTime+"00:00:00".substring(inTime.length());
					}
					Calendar nextCalendar=Calendar.getInstance();
					try {
						nextCalendar.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(todayDateString+" "+inTime));
						this._calendarDaily.add(nextCalendar);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
				}
				intervalScript=intervalScript.substring(intervalScript.indexOf(",")+1);
			}
		}
		else if(intervalType.equals("INTERVAL:HOURS")){
			
		}
		else if(intervalType.equals("INTERVAL:MINUTES")){
			
		}
		else if(intervalType.equals("INTERVAL:SECONDS")){
			
		}
	}

	@Override
	public void run() {
		while(_calendarActive){
			Calendar calNow=Calendar.getInstance();
			boolean canExecuteCalTasks=false;
			if(!_calendarDaily.isEmpty()){
				while(_calendarDaily.get(_calendarDailyIndex).getTimeInMillis()<calNow.getTimeInMillis()){
					_calendarDaily.get(_calendarDailyIndex).add(Calendar.DATE, 1);
					canExecuteCalTasks=true;
					_calendarDailyIndex++;
					if(_calendarDaily.size()>=_calendarDailyIndex) _calendarDailyIndex=0;
				}
			}
			if(canExecuteCalTasks){
				for(int taskIndex=0;taskIndex<this._automatedTasks.size();taskIndex++){
					AutomatedTasksQueue.executeAutomatedTask(this._automatedTasks.get(taskIndex));
				}
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void automatedCalendars(String calendarName,AutomatedCalendar automatedCalendar,boolean override){
		calendarName=(calendarName==null?"":calendarName.trim().toUpperCase());
		
		synchronized (_calendarsLock) {
			Integer mapIndex=-1;
			if(_automatedCalendarLabelMap.containsKey(calendarName)){
				automatedCalendar=_automatedCalendarLabelMap.get(calendarName);
				mapIndex=_automatedCalendars.indexOf(automatedCalendar);
			}
			if(mapIndex==-1){
				if(_automatedCalendars.add(automatedCalendar)){
					mapIndex=_automatedCalendars.indexOf(automatedCalendar);
					_automatedCalendarLabelMap.put(calendarName, automatedCalendar);
					_automatedCalendarLabelIndexMap.put(mapIndex, calendarName);
				}
			}
		}
		
		_automatedCalendarExecutor.execute(automatedCalendar);
	}
}
