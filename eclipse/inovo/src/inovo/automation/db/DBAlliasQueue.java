package inovo.automation.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import inovo.db.Database;
import inovo.queues.Request;

public class DBAlliasQueue extends Queue {

	public DBAlliasQueue(String dbAlliasName) throws Exception {
		super(dbAlliasName);
	}

	@Override
	public void initiate() throws Exception {
		this.queueRequest(new inovo.automation.db.Request(null){
			
			@Override
			public void executeDBRequest(Database dballias) throws Exception {
				TreeMap<Integer,ArrayList<Object>> dbrequestsset=new TreeMap<Integer, ArrayList<Object>>();
				Database.executeDBRequest(dbrequestsset,dballias, "SELECT * FROM <DBUSER>.INOVO_AUTOMATION_REQUEST WHERE REQUEST_ACTIVE=1 AND REQUEST_TYPE='DBREQUEST' AND PARENT_REQUEST_ID IN (SELECT ID FROM <DBUSER>.INOVO_AUTOMATION_REQUEST WHERE PARENT_REQUEST_ID=0 AND REQUEST_TYPE='DBALLIASREQUEST')", null,null);
				if(dbrequestsset!=null){
					for(int rowindex:dbrequestsset.keySet()){
						if(rowindex==0) continue;
						HashMap<String,Object> dbrequestdata=Database.rowData(dbrequestsset, rowindex);
						
						if(!this.queue().requestAlliasExist(dbrequestdata.get("REQUEST_TYPE")+":"+ dbrequestdata.get("ID"))){
							this.queue().queueRequest(
								dbrequestdata.get("REQUEST_TYPE")+":"+ dbrequestdata.get("ID"),
								this.queue().newRequest(dbrequestdata, dbrequestdata.get("REQUEST_TYPE").toString(),dbrequestdata.get("REQUEST_ELEMENT_CLASS").toString(), dbrequestdata.get("REQUEST_ELEMENT_LIBRARY").toString()),
								false);
						}
						
						dbrequestdata.clear();
						dbrequestdata=null;
					}
				}
				
				Database.cleanupDataset(dbrequestsset);
				
				 Database.executeDBRequest(dbrequestsset,dballias, "SELECT * FROM <DBUSER>.INOVO_AUTOMATION_REQUEST WHERE REQUEST_ACTIVE=0 AND REQUEST_TYPE='DBREQUEST' AND PARENT_REQUEST_ID IN (SELECT ID FROM <DBUSER>.INOVO_AUTOMATION_REQUEST WHERE PARENT_REQUEST_ID=0 AND REQUEST_TYPE='DBALLIASREQUEST')", null,null);
				if(dbrequestsset!=null){
					for(int rowindex:dbrequestsset.keySet()){
						if(rowindex==0) continue;
						HashMap<String,Object> dbrequestdata=Database.rowData(dbrequestsset, rowindex);
							
						if(this.queue().requestAlliasExist(dbrequestdata.get("REQUEST_TYPE")+":"+ dbrequestdata.get("ID"))){
							this.queue().killRequest(dbrequestdata.get("REQUEST_TYPE")+":"+ dbrequestdata.get("ID"));
						}
						
						dbrequestdata.clear();
						dbrequestdata=null;
					}
				}
			}
			
			
			@Override
			public long requestDelay() {
				return 5*1024;
			}
			
			@Override
			public boolean canContinue() {
				return true;
			}
		}, true);
	}
	
	@Override
	public Request newRequest(HashMap<String, Object> requestProperties,
			String requestTypeName, String requestClassPath,
			String requestPackagePath) {
		if(requestTypeName.equals("DBREQUEST")){
			try {
				TreeMap<Integer,ArrayList<Object>> requestPropertiesSet=new TreeMap<Integer, ArrayList<Object>>();
				Database.executeDBRequest(requestPropertiesSet, this.dbAlliasName(),"SELECT * FROM <DBUSER>.INOVO_AUTOMATION_REQUEST_PROPERTIES WHERE REQUEST_ID=:ID",requestProperties,null);
				for(int rowindex:requestPropertiesSet.keySet()){
					if(rowindex==0) continue;
					HashMap<String,Object> requestProperty=Database.rowData(requestPropertiesSet, rowindex);
					requestProperties.put(requestProperty.get("PROPERTY_NAME").toString(), requestProperty.get("PROPERTY_VALUE"));
				}
				
				Database.cleanupDataset(requestPropertiesSet);
				
				return new DBRequest(requestProperties);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
