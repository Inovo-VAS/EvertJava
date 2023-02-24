package inovo.automation;

import java.util.HashMap;

public class SqlTask extends AutomatedTask {

	public SqlTask(HashMap<String, String> taskProperties) {
		super(taskProperties);
	}
	
	public static HashMap<String,String> taskPropertiesDefinition(){
		HashMap<String,String> taskPropDef=AutomatedTask.taskPropertiesDefinition();
		taskPropDef.put("PRE_EXECUTE_SQL_COMMAND", "");
		taskPropDef.put("SQL_SELECT_COMMAND", "");
		taskPropDef.put("POST_EXECUTE_SQL_COMMAND", "");
		
		taskPropDef.put("FILE_PICKUP_PATH", "");
		taskPropDef.put("FILE_PUBLISH_PATH", "");
		taskPropDef.put("FILE_PUBLISH_NAME", "");
		taskPropDef.put("FILE_TYPE", "");
		
		taskPropDef.put("SMTPHOST", "");
		taskPropDef.put("SMTPPORT", "");
		taskPropDef.put("SMTPUSER", "");
		taskPropDef.put("SMTPPASSWORD", "");
		
		taskPropDef.put("MAIL_SUBJECT", "");
		taskPropDef.put("MAIL_FROM_ADDRESS", "");
		taskPropDef.put("MAIL_TO_ADDRESS", "");
		taskPropDef.put("MAIL_BODY", "");		
		
		taskPropDef.put("FTPPHOST", "");
		taskPropDef.put("FTPPORT", "");
		taskPropDef.put("FTPUSER", "");
		taskPropDef.put("FTPPASSWORD", "");		
		
		return taskPropDef;
	}
	
	@Override
	public void executeTask() throws Exception{
		
	}

}
