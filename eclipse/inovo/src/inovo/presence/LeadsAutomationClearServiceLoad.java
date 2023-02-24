package inovo.presence;

import inovo.db.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class LeadsAutomationClearServiceLoad implements Runnable {

	HashMap<String, Object> _callsTaskParams=new HashMap<String,Object>();
	private HashMap<String,Object> _dyncalldata=new HashMap<String,Object>();
	private ArrayList<String> _serviceIds=new ArrayList<String>();
	
	public LeadsAutomationClearServiceLoad(HashMap<String,Object> callsTaskParams){
		this._callsTaskParams.clear();
		this._callsTaskParams.putAll(callsTaskParams);
	}
	@Override
	public void run() {
		
		TreeMap<Integer,ArrayList<Object>> dynamiclist=new TreeMap<Integer, ArrayList<Object>>();
		_serviceIds.clear();
		_rowsExecutedCount=0;
		try{
			
			_callsTaskParams.put("SUGGESTEDTOPCOUNT", "1000");
			
			Database.executeDBRequest(null,"LEADSAUTOMATION", "SELECT COUNT(*) AS SUGGESTEDTOPCOUNT FROM <DBUSER>.[DYNAMICCALLERLIST] WHERE [SERVICEID]=:SERVICEID AND [LOADID]=:LOADID AND [LOADREQUESTTYPE]=:LOADREQUESTTYPE AND [RECORDHANDLEFLAG]=2" , _callsTaskParams,null);
			
			/*Database.executeDBRequest(dynamiclist,"LEADSAUTOMATION", "SELECT TOP "+_callsTaskParams.get("SUGGESTEDTOPCOUNT")+" * FROM <DBUSER>.[DYNAMICCALLERLIST] WHERE [SERVICEID]=:SERVICEID AND [LOADID]=:LOADID AND [LOADREQUESTTYPE]=:LOADREQUESTTYPE AND [RECORDHANDLEFLAG]=2" , _callsTaskParams,null);
			
			HashMap<String,String> dyncalldata=new HashMap<String,String>();
			
			if(dynamiclist!=null){
				for(int rowindex:dynamiclist.keySet()){
					if(rowindex==0) continue;
					dyncalldata.clear();
					dyncalldata.putAll(Database.rowData(dynamiclist, rowindex));					
					LeadsAutomation.leadsAutomation("","").cleanupServiceLoad(dyncalldata);
					if(serviceIds.indexOf(dyncalldata.get("SERVICEID"))==-1){
						serviceIds.add(dyncalldata.get("SERVICEID"));
					}
				}
			}*/
			
			Database.executeDBRequest(null,"LEADSAUTOMATION", "SELECT TOP "+_callsTaskParams.get("SUGGESTEDTOPCOUNT")+" * FROM <DBUSER>.[DYNAMICCALLERLIST] WHERE [SERVICEID]=:SERVICEID AND [LOADID]=:LOADID AND [LOADREQUESTTYPE]=:LOADREQUESTTYPE AND [RECORDHANDLEFLAG]=2" , _callsTaskParams,this);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		Database.cleanupDataset(dynamiclist);
		LeadsAutomation.leadsAutomation("","").reloadServices(_serviceIds);
		_serviceIds.clear();
		
		LeadsAutomation.leadsAutomation("","").removeCallsTasksKey(_callsTaskParams.get("SERVICEID")+"|"+_callsTaskParams.get("LOADID")+"|"+_callsTaskParams.get("LOADREQUESTTYPE"));
	}

	private int _rowColumnIndex=0;
	private int _rowColumnCount=0;
	public void readRowData(Integer rowindex,ArrayList<String> rowData,ArrayList<String> rowColumns) throws Exception{
		if(rowindex==0) return;
		
		this._dyncalldata.clear();
		_rowColumnIndex=0;
		_rowColumnCount=rowColumns.size();
		while(_rowColumnIndex<_rowColumnCount){
			this._dyncalldata.put(rowColumns.get(_rowColumnIndex).toUpperCase(), rowData.get(_rowColumnIndex++));
		}
		LeadsAutomation.leadsAutomation("","").cleanupServiceLoad(_dyncalldata);
		if(_serviceIds.indexOf(_dyncalldata.get("SERVICEID"))==-1){
			_serviceIds.add(_dyncalldata.get("SERVICEID").toString());
		}
		Thread.sleep(2);
	}
	
	private int _rowsExecutedCount=0;
}
