package inovo.automation.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Queue extends inovo.automation.Queue{

	public Queue(String dbAlliasName) throws Exception {
		super(dbAlliasName);
	}
	
	public String dbAlliasName(){
		return this.queueAllias();
	}
	
	public SQLRequest queueSQLRequest(ArrayList<Calendar> schedules, String sqlRequestAllias,String sqlStatement,HashMap<String,Object> sqlparams,boolean useSqlParamsAsRequestProperties,boolean queueModal) throws Exception{
		SQLRequest sqlRequest=new SQLRequest(schedules,(useSqlParamsAsRequestProperties?sqlparams:null));
		sqlRequest.setSqlStatement(sqlStatement, sqlparams);
		sqlRequest=(SQLRequest) this.queueRequest(sqlRequestAllias, sqlRequest, queueModal);
		return sqlRequest;
	}
}
