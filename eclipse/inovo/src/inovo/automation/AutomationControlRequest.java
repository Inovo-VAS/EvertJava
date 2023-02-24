package inovo.automation;

import inovo.db.Database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;

public class AutomationControlRequest extends Request {
	
	private static ArrayList<String> queuerequestsQueued=new ArrayList<String>();
	
	public AutomationControlRequest() {
		super();
		this.setRequestDelay(5*1024);
	}
	
	@Override
	public boolean canContinue() {
		return true;
	}
	
	@Override
	public void executeRequest() throws Exception {
		Database dballiasautomation=Database.dballias("INOVOAUTOMATION");
		if(dballiasautomation!=null){
			TreeMap<Integer,ArrayList<Object>> registedDBAlliasQueues=new TreeMap<Integer, ArrayList<Object>>();
			Database.executeDBRequest(registedDBAlliasQueues,dballiasautomation, "SELECT * FROM <DBUSER>.INOVO_AUTOMATION_REQUEST WHERE REQUEST_TYPE='DBALLIASREQUEST'", null,null);
			
			for(int rowindex:registedDBAlliasQueues.keySet()){
				if(rowindex==0) continue;
				HashMap<String,Object> registedDBAlliasQueueSettings=Database.rowData(registedDBAlliasQueues, rowindex);
				if(queuerequestsQueued.indexOf(registedDBAlliasQueueSettings.get("REQUEST_NAME"))==-1){
					if(!this.queue().requestAlliasExist(registedDBAlliasQueueSettings.get("REQUEST_NAME").toString())){
						if(registedDBAlliasQueueSettings.get("REQUEST_TYPE").equals("DBALLIASREQUEST")){
							new inovo.automation.db.DBAlliasQueue(registedDBAlliasQueueSettings.get("REQUEST_NAME").toString());
						}
						queuerequestsQueued.add(registedDBAlliasQueueSettings.get("REQUEST_NAME").toString());
					}
				}
			}
		
			Database.cleanupDataset(registedDBAlliasQueues);
		}
	}
}
