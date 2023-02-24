package inovo.automation.db;

import inovo.db.Database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Request extends inovo.automation.Request {

	public Request(){
		this(null,null);
	}
	
	public Request(HashMap<String,Object> requestProperties){
		this(null,requestProperties);
	}
	
	public Request(ArrayList<Calendar> schedules,HashMap<String,Object> requestProperties) {
		super(schedules,requestProperties);
	}
	
	public Queue dbQueue(){
		return (Queue) this.queue();
	}
	
	@Override
	public void executeRequest() throws Exception{
		this.executeDBRequest(Database.dballias(this.dbQueue().dbAlliasName()));
	}

	public void executeDBRequest(Database dballias) throws Exception{
	}
}
