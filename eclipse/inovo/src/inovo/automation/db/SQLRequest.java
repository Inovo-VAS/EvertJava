package inovo.automation.db;

import inovo.db.Database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;

public class SQLRequest extends Request {
	
	private HashMap<String,Object> _sqlParams=new HashMap<String,Object>();
	private String _sqlStatement="";
	
	public SQLRequest() {
		this(null,null);
	}
	
	public SQLRequest(HashMap<String,Object> requestProperties) {
		this(null,requestProperties);
	}
	
	public SQLRequest(ArrayList<Calendar> schedules,HashMap<String,Object> requestProperties) {
		super(schedules,requestProperties);
	}
	
	public void setSqlStatement(String sqlStatement,HashMap<String,Object> sqlparams){
		this._sqlParams.clear();
		if(sqlparams!=null){
			this._sqlParams.putAll(sqlparams);
		}
		this._sqlStatement=sqlStatement;
	}
	
	public void setSqlStatement(String sqlStatement){
		this._sqlStatement=sqlStatement;
	}
	
	public String sqlStatement(){
		return this._sqlStatement;
	}
	
	public HashMap<String,Object> sqlParameters(){
		return this._sqlParams;
	}
	
	@Override
	public void executeDBRequest(Database dballias) throws Exception{
	}
		
	TreeMap<Integer,ArrayList<Object>> executeDBRequest(String dbAllias,String sqlStatement,HashMap<String,Object> sqlParams) throws Exception{
		return this.executeDBRequest(Database.dballias(dbAllias), sqlStatement, sqlParams);
	}
	
	TreeMap<Integer,ArrayList<Object>> executeDBRequest(Database dbAllias,String sqlStatement,HashMap<String,Object> sqlParams) throws Exception{
		TreeMap<Integer,ArrayList<Object>> executeDBRequest=new TreeMap<Integer, ArrayList<Object>>();
		Database.executeDBRequest(executeDBRequest,dbAllias, sqlStatement, sqlParams,null);
		return executeDBRequest;
	}	
}
