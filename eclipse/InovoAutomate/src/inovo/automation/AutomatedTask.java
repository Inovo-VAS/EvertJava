package inovo.automation;

import java.util.ArrayList;
import java.util.HashMap;

public class AutomatedTask implements Runnable{
	
	private boolean _pauseTask=false;
	private boolean _killTask=false;
	private HashMap<String, String> _taskProperties=null;
	
	protected AutomatedCalendar _automatedCalendar=null;
	public AutomatedTask(HashMap<String,String> taskProperties){
		this._taskProperties=new HashMap<String,String>(taskProperties);
	}
	
	public String taskProperty(String propName){
		if(this._taskProperties.containsKey(propName)){
			return this._taskProperties.get(propName);
		}
		return "";
	}
	
	@Override
	public void run() {
		try {
			this.executeTask();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void executeTask() throws Exception{
		
	}
	
	public static HashMap<String,String> taskPropertiesDefinition(){
		HashMap<String,String> taskPropDef=new HashMap<String,String>();
		taskPropDef.put("AUTOMATION_SCHEDULE", "");
		return taskPropDef;
	}

}
