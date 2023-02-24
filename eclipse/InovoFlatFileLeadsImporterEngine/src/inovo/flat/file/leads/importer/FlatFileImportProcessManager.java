package inovo.flat.file.leads.importer;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import inovo.db.Database;

public class FlatFileImportProcessManager {
	
	private  static FlatFileImportProcessManager _flatFileImportProcessManager=null;
	private static FlatFileImportProcessManagerThread _flatFileImportProcessManagerThread=null;
	private static Object FlatFileImportProcessManagerLock=new Object();
	private static boolean _flatFileImportProcessManagerShuttedDown=false;
	
	private static ConcurrentHashMap<String, FlatFileRegisterRequest> _flatFileRegisterRequestMap=new ConcurrentHashMap<String, FlatFileRegisterRequest>();
	
	private static class FlatFileImportProcessManagerThread extends Thread{
		
		public FlatFileImportProcessManagerThread(){
			super();
			this.setDaemon(true);
		}
		
		@Override
		public void run() {
			while(!_flatFileImportProcessManager.manageFlatfileImportProcesses()){
				synchronized (FlatFileImportProcessManagerLock) {
					try {
						FlatFileImportProcessManagerLock.wait(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			_flatFileImportProcessManagerShuttedDown=true;
		}
	}
	
	private ArrayList<FlatFileRegisterRequest> _flatFileLeadsImportRequestsToRemove=new ArrayList<FlatFileRegisterRequest>();
	private ArrayList<FlatFileRegisterRequest> _flatFileLeadsImportRequestsToInitiate=new ArrayList<FlatFileRegisterRequest>();
	
	public FlatFileImportProcessManager(){
		
	}
	
	public static FlatFileImportProcessManager flatFileImportProcessManager(){
		if(_flatFileImportProcessManager==null){
			_flatFileImportProcessManager=new FlatFileImportProcessManager();
			(_flatFileImportProcessManagerThread=new FlatFileImportProcessManagerThread()).start();
		}
		return _flatFileImportProcessManager;
	}
	
	private boolean _shutdownFlatfileImportProcessManager=false;
	
	public boolean manageFlatfileImportProcesses() {
		try {
			Database.executeDBRequest(null,"FLATFILELEADSIMPORTER", "SELECT [ID],[ALIAS],[ENABLEALIAS],[PREBATCHIMPORTSQLCOMMAND],[POSTBATCHIMPORTSQLCOMMAND],[ALTERNATESOURCEIDSQLCOMMAND],[POSTLEADREQUESTSQLCOMMAND],[SOURCEIDFIELD],[SERVICEIDFIELD],[DEFAULTSERVICEID],[LOADIDFIELD],[DEFAULTLOADID],[PRIORITYFIELD],[DEFAULTPRIORITY],[LEADREQUESTTYPEFIELD],[DEFAULTLEADLEADREQUESTTYPE],[CALLERNAMEFIELDS],[COMMENTSFIELDS],[COMADELIM],[USEFILENAMEASLOADNAME],[FORCECREATINGLOAD],[PHONE1FIELD],[PHONE2FIELD],[PHONE3FIELD],[PHONE4FIELD],[PHONE5FIELD],[PHONE6FIELD],[PHONE7FIELD],[PHONE8FIELD],[PHONE9FIELD],[PHONE10FIELD],[METAFIELD1],[METAFIELD2],[METAFIELD3],[METAFIELD4],[METAFIELD5],[METAFIELD6],[METAFIELD7],[METAFIELD8],[METAFIELD9],[METAFIELD10],[METAFIELD11],[METAFIELD12],[METAFIELD13],[METAFIELD14],[METAFIELD15],[METAFIELD16],[METAFIELD17],[METAFIELD18],[METAFIELD19],[METAFIELD20],[SOURCEPATH],[CURRENTFILEFIELDS],[ENABLENEWLOAD],[LOADNAMEMASK],[FILELOOKUPMASK] FROM <DBUSER>.[LEADSDATAFILEALIAS]", null,this);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(!_flatFileLeadsImportRequestsToInitiate.isEmpty()){
			((FlatFileRegisterRequest)_flatFileLeadsImportRequestsToInitiate.remove(0)).initiateFlatFileRegisterRequest();
		}
		
		while(!_flatFileLeadsImportRequestsToRemove.isEmpty()){
			((FlatFileRegisterRequest)_flatFileLeadsImportRequestsToRemove.remove(0)).shutdownFlatFileRegisterRequest();
		}
		
		return _shutdownFlatfileImportProcessManager;
	}
	
	public static void shutdownFlatFileImportProcessManager(){
		if(_flatFileImportProcessManager!=null){
			_flatFileImportProcessManager._shutdownFlatfileImportProcessManager=true;
			while(!_flatFileImportProcessManagerShuttedDown){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void readRowData(Integer rowindex,ArrayList<String> rowData,ArrayList<String> rowColumns){
		if(rowindex==0) return;
		synchronized (_flatFileRegisterRequestMap) {
			String alias=rowDataFieldValue(rowData, rowColumns, "ALIAS");
			if(this.rowDataFieldValue(rowData, rowColumns, "ENABLEALIAS").toUpperCase().equals("TRUE")){
				if(!_flatFileRegisterRequestMap.containsKey(alias)){
					this._flatFileLeadsImportRequestsToInitiate.add(new FlatFileRegisterRequest(Database.rowData(rowColumns, rowData)));
					_flatFileRegisterRequestMap.put(alias, this._flatFileLeadsImportRequestsToInitiate.get(0));
				}
			}
			
			if(this.rowDataFieldValue(rowData, rowColumns, "ENABLEALIAS").toUpperCase().equals("FALSE")){
				if(_flatFileRegisterRequestMap.containsKey(alias)){
					this._flatFileLeadsImportRequestsToRemove.add(this._flatFileRegisterRequestMap.remove(alias));
				}
			}
		}
	}
	
	private String rowDataFieldValue(ArrayList<String> rowData,ArrayList<String> rowColumns,String field){
		String rowDataFieldValue=rowData.get(rowColumns.indexOf(field));
		return (rowDataFieldValue==null?"":rowDataFieldValue);
	}
}
