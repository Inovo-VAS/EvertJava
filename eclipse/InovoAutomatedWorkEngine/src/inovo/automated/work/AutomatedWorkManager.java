package inovo.automated.work;

import inovo.db.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import com.sun.org.apache.regexp.internal.recompile;

public class AutomatedWorkManager implements Runnable{
	
	private static AutomatedWorkManager _automatedWorkManager=null;

	protected boolean _shutdownWorkManager=false;
	protected TreeMap<String,AutomatedWorker> _automatedwork=new TreeMap<String, AutomatedWorker>(); 
	
	@Override
	public void run() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while(!this._shutdownWorkManager){
			try {
				this.activateAutomatedWork();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static AutomatedWorkManager automatedWorkManager(){
		if(_automatedWorkManager==null){
			new Thread(_automatedWorkManager=new AutomatedWorkManager()).start();
		}
		return _automatedWorkManager;
	}

	private void activateAutomatedWork() throws Exception{
		Database.executeDBRequest(null, "AUTOMATEDWORK", "SElECT ID AS AUTOMATEDID,NAME AS AUTOMATEDNAME,DESCRIPTION AS AUTOMATEDDESCRITPION,STATUS,SCHEDULETYPE,SCHEDULE FROM <DBUSER>.AUTOMATION", null, this,"readAutomatedWorkData");
	}	
	
	private HashMap<String,String> _automatedWorkSettings=new HashMap<String, String>();
	private int _rindex=0;
	
	protected ArrayList<String> _workerKeysToShutdown=new ArrayList<String>();
	
	public void readAutomatedWorkData(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns){
		if(rowIndex==0){
			return;
		}
		_automatedWorkSettings.clear();
		_rindex=0;
		while(this._rindex<rowColumns.size()){
			_automatedWorkSettings.put(rowColumns.get(_rindex), rowData.get(_rindex++));
		}
		
		AutomatedWorker automationwork=null;
		if(this._automatedwork.containsKey(_automatedWorkSettings.get("AUTOMATEDID"))){
			if(!_automatedWorkSettings.get("STATUS").equals("E")){
				if(_workerKeysToShutdown.indexOf(_automatedWorkSettings.get("AUTOMATEDID"))==-1){
					_workerKeysToShutdown.add(_automatedWorkSettings.get("AUTOMATEDID"));
					this._automatedwork.get(_automatedWorkSettings.get("AUTOMATEDID")).shutdownWorker();
				}
			}
			return;
		}
		else{
			if(_automatedWorkSettings.get("STATUS").equals("E")){
				this._automatedwork.put(_automatedWorkSettings.get("AUTOMATEDID"), automationwork=new AutomatedWorker(this, _automatedWorkSettings));
			}
			if(automationwork!=null){
				new Thread(automationwork).start();
			}
		}
	}

	public void shutdown() {
		this._shutdownWorkManager=true;
		while(!_automatedwork.isEmpty()){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}
	}
}
