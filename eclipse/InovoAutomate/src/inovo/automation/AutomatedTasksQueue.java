package inovo.automation;

import inovo.db.Database;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.tomcat.dbcp.dbcp.DbcpException;

public class AutomatedTasksQueue implements Runnable{
	
	private static ExecutorService _executingTasks=Executors.newCachedThreadPool();
	private static AutomatedTasksQueue _automatedTasksQueue=null;
	
	private boolean _shutdownAutomatedTasks=false;
	private AutomatedTasksQueue(){
		
	}
	
	public static void initAutomatedTasksQueue(){
		if(_automatedTasksQueue==null){
			new Thread(_automatedTasksQueue=new AutomatedTasksQueue()).start();
		}
	}

	@Override
	public void run() {
		while(!_shutdownAutomatedTasks){
			try{
				TreeMap<Integer, ArrayList<String>> enabledAutomatedTasks = Database.executeDBRequest("AUTOMATEDTASKS", "SELECT * FROM <DBUSER>.AUTOMATED_TASK WHERE ENABLED='E' AND PUBLISHED<>'Y'", null);
				HashMap<String,String> enabledAutomatedTaskData=new HashMap<String,String>();
				HashMap<String,String> enabledAutomatedTaskPropertyData=new HashMap<String,String>();
				for(int rowindexEnabledAutomatedTasks:enabledAutomatedTasks.keySet()){
					if(rowindexEnabledAutomatedTasks==0) continue;
					enabledAutomatedTaskData.clear();
					enabledAutomatedTaskData.putAll(Database.rowData(enabledAutomatedTasks, rowindexEnabledAutomatedTasks));
					enabledAutomatedTaskData.put("AUTOTASKID", enabledAutomatedTaskData.remove("ID"));
				
					enabledAutomatedTaskData.put("JAVACLASS", "");
					Database.executeDBRequest("AUTOMATEDTASKS","SELECT JAVACLASS FROM <DBUSER>.AUTOMATED_TASK_TYPE WHERE ID=:TASKTYPEID",enabledAutomatedTaskData);
					
					TreeMap<Integer, ArrayList<String>> enabledAutomatedTaskProperties = Database.executeDBRequest("AUTOMATEDTASKS", "SELECT AUTOMATED_TASK_PROPERTY.ID,AUTOMATED_TASK_TYPE_PROPERTY.PROPERTY_NAME,ISNULL(AUTOMATED_TASK_PROPERTY.PROPERTY_VALUE,AUTOMATED_TASK_TYPE_PROPERTY.PROPERTY_DEFAULT_VALUE) AS PROPERTY_VALUE FROM <DBUSER>.AUTOMATED_TASK_PROPERTY INNER JOIN <DBUSER>.AUTOMATED_TASK_TYPE_PROPERTY ON AUTOMATED_TASK_PROPERTY.TASKID=:AUTOTASKID AND AUTOMATED_TASK_PROPERTY.TASKTYPEID=AUTOMATED_TASK_TYPE_PROPERTY.TASKTYPEID AND AUTOMATED_TASK_PROPERTY.TASKTYPEPROPID=AUTOMATED_TASK_TYPE_PROPERTY.ID", enabledAutomatedTaskData);
					HashMap<String,String> automatedTaskProperties=new HashMap<String,String>();
					
					for(int rowindexAutomatedTaskProperty:enabledAutomatedTaskProperties.keySet()){
						if(rowindexAutomatedTaskProperty==0) continue;
						enabledAutomatedTaskPropertyData.clear();
						enabledAutomatedTaskPropertyData=Database.rowData(enabledAutomatedTaskProperties, rowindexAutomatedTaskProperty);
						enabledAutomatedTaskPropertyData.put("AUTOTASKPROPID", enabledAutomatedTaskPropertyData.remove("ID"));
						String taskPropName=enabledAutomatedTaskPropertyData.get("PROPERTY_NAME").toUpperCase();
						String taskPropValue=enabledAutomatedTaskPropertyData.get("PROPERTY_VALUE");
						automatedTaskProperties.put(taskPropName, taskPropValue);
					}
					
					if(!automatedTaskProperties.isEmpty()){
						automatedTaskProperties.put("TASK_LABEL", enabledAutomatedTaskData.get("TASK_LABEL"));
						automatedTaskProperties.put("AUTOTASKID",enabledAutomatedTaskData.get("AUTOTASKID"));
						
						this.initiateAutomatedTask(enabledAutomatedTaskData.get("JAVACLASS"),automatedTaskProperties);
						
						Database.executeDBRequest("AUTOMATEDTASKS", "UPDATE <DBUSER>.AUTOMATED_TASK SET PUBLISHED='Y' WHERE ID=:AUTOTASKID AND ENABLED='E' AND PUBLISHED<>'Y'", enabledAutomatedTaskData);
					}
					else{
						Database.executeDBRequest("AUTOMATEDTASKS", "UPDATE <DBUSER>.AUTOMATED_TASK SET PUBLISHED='N',ENABLED='E' WHERE ID=:AUTOTASKID AND ENABLED='E' AND PUBLISHED<>'Y'", enabledAutomatedTaskData);
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			try {
				Thread.sleep(20*1024);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void initiateAutomatedTask(String automatedClassPath,
		HashMap<String, String> automatedTaskProperties) throws Exception {
		AutomatedTask autoTaskToAdd=(AutomatedTask)Class.forName(automatedClassPath).getConstructor(HashMap.class).newInstance(new Object[]{automatedTaskProperties});
		if(automatedTaskProperties.containsKey("AUTOMATION_SCHEDULE")){
			if(!automatedTaskProperties.get("AUTOMATION_SCHEDULE").equals("")){
				AutomatedCalendar.automatedCalendars(automatedTaskProperties.get("TASK_LABEL"), new AutomatedCalendar(automatedTaskProperties.get("TASK_LABEL"),automatedTaskProperties.get("AUTOMATION_SCHEDULE"),new AutomatedTask[]{autoTaskToAdd}), true);
			}
			else{
				
			}
		}
	}
	
	public static void executeAutomatedTask(AutomatedTask taskToExecute){
		_executingTasks.execute(taskToExecute);
	}
}
