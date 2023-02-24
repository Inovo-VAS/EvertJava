package inovo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DatabaseBulkSourceDestinationTableTransferMonitor extends DatabaseBulkExecuteMonitor{

	private Database sourceDbAlias=null;
	private Database destinationDbAlias=null;
	private List<String> filterTables=null;
	
	private static List<String> stringArrToList(boolean allowDuplicates,String...strings){
		if(strings==null||strings.length==0) return null;
		List<String> stringArrToList=new ArrayList<String>();
		for(String string:strings){
			if(!allowDuplicates&&stringArrToList.contains(string)) continue;
			stringArrToList.add(string);
		}
		
		return stringArrToList.isEmpty()?null:stringArrToList;
	}
	
	public boolean validSourceTable(String sourceTableName){
		return !this.ignoreValidSourceTable(sourceTableName);
	}
	
	public boolean ignoreValidSourceTable(String sourceTableName) {
		return false;
	}

	public boolean validDestinationTable(String destinationTable){
		return this.validSourceTable(destinationTable);
	}
	
	public DatabaseBulkSourceDestinationTableTransferMonitor(Database sourceDbAlias,Database destinationDbAlias,String...filterTables){
		this(sourceDbAlias,destinationDbAlias, stringArrToList(false, filterTables));
	}
	
	public DatabaseBulkSourceDestinationTableTransferMonitor(Database sourceDbAlias,Database destinationDbAlias,List<String> filterTables){
		this.sourceDbAlias=sourceDbAlias;
		this.destinationDbAlias=destinationDbAlias;
		this.filterTables=filterTables;
	}
	
	private ThreadFactory threadFactory=new ThreadFactory() {
		
		@Override
		public Thread newThread(Runnable r) {
			Thread thrd=new  Thread(r){
				public void run() {
					super.run();
					System.gc();
				};
			};
			
			return thrd;
		}
	};
	
	private ExecutorService executorService=new ThreadPoolExecutor(0, Integer.MAX_VALUE,100	, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),threadFactory);
	
	public void requestTransfer(boolean blocked){
		if(blocked){
			executeTransfer();
		} else {
			this.executorService.execute(new Runnable() {
				@Override
				public void run() {
					executeTransfer();
					System.gc();
				}
			});
		}
	}
	
	public String finalSourceTableSqlStatement(String sourceSqlStatement,String sourceTableName,String sourceColumns,String destinationSqlInsertUniqueIndexColumns,Database sourceDbAlias){
		return sourceSqlStatement;
	}
	
	public String finalDestinationTableSqlStatement(String destinationSqlInsertStatement,String destinationSqlInsertUniqueIndexCondition,String destinationTableName,String destinationColumns,Database destinationDbAlias){
		return destinationSqlInsertStatement;
	}
	
	public void executeTransfer(){
		TreeMap<String, List<List<String>>> sourceTables;
		try {
			sourceTables = this.sourceDbAlias.tables(this.filterTables,this,this.getClass().getDeclaredMethod("validSourceTable", String.class));
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		TreeMap<String, List<List<String>>> destinationTables;
		try {
			destinationTables = this.destinationDbAlias.tables(this.filterTables,this,this.getClass().getMethod("validDestinationTable", String.class));
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		
		final long startStamp=Calendar.getInstance().getTimeInMillis();
		this.startMonitoringBulkStransfer(startStamp, this.sourceDbAlias,this.destinationDbAlias,sourceTables,destinationTables);
		final List<String> failedTables=new ArrayList<String>();
		while(!sourceTables.isEmpty()){
			
			final String sourceTableName=sourceTables.keySet().iterator().next();
			List<List<String>> sourceColDefs=sourceTables.remove(sourceTableName);
			if(destinationTables.containsKey(sourceTableName)){
				List<List<String>> destinationColDefs=destinationTables.remove(sourceTableName);
				
				String sourceSqlSelect="SELECT ";
				
				String destinationSqlInsert="INSERT INTO "+destinationDbAlias.dbsettings().get("DBUSER")+"."+sourceTableName+" (";
				String destinationSqlInsertUniqueIndexCondition="";
				String destinationSqlInsertUniqueIndexColumns="";
				String sourceColumns="";
				
				int destInsertColCount=0;
				boolean canExecute=false;
				boolean hasIdentiy=false;
				List<String> destinationParamNames=null;
				while(!sourceColDefs.isEmpty()){
					List<String> sourceColNames=sourceColDefs.remove(0);
					sourceColDefs.remove(0).clear();
					
					List<String> destColNames=destinationColDefs.remove(0);
					List<String> destColDefs=destinationColDefs.remove(0);
					
					destInsertColCount=0;
					destinationSqlInsertUniqueIndexCondition="";
					destinationSqlInsertUniqueIndexColumns="";
					sourceColumns="";
					hasIdentiy=false;
					if(destinationParamNames!=null){
						destinationParamNames.clear();
						destinationParamNames=null;
					}
					while(!sourceColNames.isEmpty()){
						if(destColNames.contains(sourceColNames.get(0))){
							String[] destColDef=destColDefs.remove(destColNames.indexOf(sourceColNames.get(0))).split("[|]");
							destColNames.remove(sourceColNames.get(0));
							if(destColDef.length>0&&destColDef[destColDef.length-1].equals("1")){
								if(destColDef[destColDef.length-2].equals("1")){
									if(!hasIdentiy){
										hasIdentiy=true;
									}
								}
								if(destinationParamNames==null){
									destinationParamNames=new ArrayList<String>();
								}
								destinationSqlInsertUniqueIndexColumns+=destinationSqlInsertUniqueIndexColumns+sourceColNames.get(0)+",";
								destinationParamNames.add(sourceColNames.get(0));
								destinationSqlInsertUniqueIndexCondition+=sourceColNames.get(0)+"=? AND ";
							}
							sourceSqlSelect+=sourceColNames.get(0)+(destColNames.isEmpty()?"":",");
							sourceColumns+=sourceColNames.get(0)+(destColNames.isEmpty()?"":",");
							destinationSqlInsert+=sourceColNames.remove(0)+(destColNames.isEmpty()?"":",");
							destInsertColCount++;
							if(destColNames.isEmpty()){
								
								destinationSqlInsert+=") SELECT ";
								while(destInsertColCount>0){
									destInsertColCount--;
									destinationSqlInsert+="?"+(destInsertColCount==0?"":",");
								}
								
								if(!destinationSqlInsertUniqueIndexCondition.equals("")){
									destinationSqlInsertUniqueIndexCondition=destinationSqlInsertUniqueIndexCondition.substring(0, destinationSqlInsertUniqueIndexCondition.length()-" AND ".length());
									destinationSqlInsertUniqueIndexColumns=destinationSqlInsertUniqueIndexColumns.substring(0, destinationSqlInsertUniqueIndexColumns.length()-1);
									if(destinationDbAlias.dbsettings().get("DBTYPE").equals("sqlserver")){
										destinationSqlInsertUniqueIndexCondition="WHERE NOT EXISTS(SELECT TOP 1 "+destinationSqlInsertUniqueIndexColumns+" FROM "+destinationDbAlias.dbsettings().get("DBUSER")+"."+sourceTableName+" WHERE "+destinationSqlInsertUniqueIndexCondition+")";
									}
									else{
										destinationSqlInsertUniqueIndexCondition="";
									}
									if(!destinationSqlInsertUniqueIndexCondition.equals("")){
										destinationSqlInsert+=" "+destinationSqlInsertUniqueIndexCondition;
									}
								}
								
								canExecute=true;
								sourceSqlSelect+=" FROM "+sourceDbAlias.dbsettings().get("DBUSER")+"."+sourceTableName;
								
								sourceSqlSelect=finalSourceTableSqlStatement(sourceSqlSelect, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias);
								destinationSqlInsert=finalDestinationTableSqlStatement(destinationSqlInsert,destinationSqlInsertUniqueIndexCondition, sourceTableName,sourceColumns, destinationDbAlias);
								break;
							}
						}
						else{
							sourceColNames.remove(0);
						}
					}
					destColNames.clear();
					destColDefs.clear();
				}
				
				
				if(canExecute){
					
					if(this.canTruncateTable(sourceTableName)){
						try {
							this.destinationDbAlias.executeDBRequest((TreeMap<Integer, ArrayList<Object>>)null, "TRUNCATE TABLE "+destinationDbAlias.dbsettings().get("DBUSER")+"."+sourceTableName, (HashMap<String, Object>)null, null,(String) null);
						} catch (Exception e) {
						}
					}
					
					final long startTableStamp=Calendar.getInstance().getTimeInMillis();
				    Database.bulkExecuteSqlDBAliasToDBAlias(sourceDbAlias, sourceSqlSelect,destinationDbAlias, destinationSqlInsert, this.batchExecuteTableCommitSize(10,sourceTableName),new DatabaseBulkExecuteMonitor(){
				    	
				    	@Override
				    	public void preBulkExecuteDestination(Connection destinationCn,
				    			PreparedStatement destinationPrepStatement,String[]adhoc) {
				    		if(destinationDbAlias.dbsettings().get("DBTYPE").equals("sqlserver")){
				    			if(adhoc!=null&&adhoc.length>0&&adhoc[0].equals("hasIdentity")&&applyIdentityInsert(sourceTableName)){
									try {
										destinationCn.createStatement().execute("SET IDENTITY_INSERT "+destinationDbAlias.dbsettings().get("DBUSER")+"."+sourceTableName+" ON");
										destinationCn.commit();
									} catch (Exception e) {
									}
				    			}
							}
				    	
				    		super.preBulkExecuteDestination(destinationCn, destinationPrepStatement,adhoc);
				    	}
				    	
						@Override
				    	public void postBulkExecuteDestination(Connection destinationCn,
				    			PreparedStatement destinationPrepStatement,String[]adhoc) {
				    		if(destinationDbAlias.dbsettings().get("DBTYPE").equals("sqlserver")){
				    			if(adhoc!=null&&adhoc.length>0&&adhoc[0].equals("hasIdentity")&&applyIdentityInsert(sourceTableName)){
									try {
										destinationCn.createStatement().execute("SET IDENTITY_INSERT "+destinationDbAlias.dbsettings().get("DBUSER")+"."+sourceTableName+" OFF");
										destinationCn.commit();
									} catch (Exception e) {
									}
				    			}
							}
				    		super.postBulkExecuteDestination(destinationCn, destinationPrepStatement,adhoc);
				    	}
				    	
				    	@Override
				    	public void startMonitoringBulkExecution() {
				    		startMonitoringBulkTableTransfer(startStamp,startTableStamp, sourceTableName);
//				    		startStamp=Calendar.getInstance().getTimeInMillis();
//				    		System.out.println("START");
//				    		System.out.println(startStamp);
				    	}
				    	
				    	@Override
				    	public void progressMonitor(long totalSourceRecords, long totalDestinationRequestsCompleted,
				    			long totalDestinationRequestsFailed,String[]adhoc) {
				    		progressTransferTableMonitor(startStamp,startTableStamp, sourceTableName,totalSourceRecords,totalDestinationRequestsCompleted,totalDestinationRequestsFailed);
//				    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//				    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//				    		System.out.println("CURRENT");
//				    		System.out.println(Calendar.getInstance().getTimeInMillis());
				    	}
				    	
				    	@Override
				    	public void endMonitoringBulkExecution(long totalSourceRecords, long totalDestinationRequestsCompleted,
				    			long totalDestinationRequestsFailed, Exception ex,String[]adhoc) {
				    		if(ex!=null){
				    			if(!failedTables.contains(sourceTableName)){
				    				failedTables.add(sourceTableName);
				    			}
				    		}
				    		endTransferTableMonitor(startStamp,startTableStamp,Calendar.getInstance().getTimeInMillis(), sourceTableName,totalSourceRecords,totalDestinationRequestsCompleted,totalDestinationRequestsFailed,ex);
//				    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//				    		System.out.print(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//				    		
//				    		System.out.println("DONE");
//				    		System.out.println((Calendar.getInstance().getTimeInMillis()-startStamp));
				    	}
				    	
				    },destinationParamNames,hasIdentiy?"hasIdentity".split("[|]"):null);
				    
//				    if(destinationDbAlias.dbsettings().get("DBTYPE").equals("sqlserver")){
//						try {
//							this.destinationDbAlias.executeDBRequest((TreeMap<Integer, ArrayList<Object>>)null, "SET IDENTITY_INSERT "+destinationDbAlias.dbsettings().get("DBUSER")+"."+sourceTableName+" OFF", (HashMap<String, Object>)null, null,(String) null);
//						} catch (Exception e) {
//						}
//					}
				}
			}
			else{
				while(!sourceColDefs.isEmpty()){
					sourceColDefs.remove(0).clear();
				}
				sourceColDefs.clear();
			}
			sourceColDefs=null;
		}
		this.endMonitoringBulkTransfer(startStamp, Calendar.getInstance().getTimeInMillis(), sourceDbAlias, destinationDbAlias, sourceTables, destinationTables,failedTables);
	}

	public boolean canTruncateTable(String sourceTableName) {
		return true;
	}
	
	public boolean applyIdentityInsert(String sourceTableName) {
		return true;
	}

	public int batchExecuteTableCommitSize(int commitSize, String sourceTableName) {
		return commitSize;
	}

	public void endTransferTableMonitor(long startStamp,long startTableStamp,long endTableStamp, String sourceTableName, long totalSourceRecords,
			long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed, Exception ex) {
	}

	public void startMonitoringBulkTableTransfer(long startStamp,long startTableStamp,String sourceTableName) {
	}

	public void progressTransferTableMonitor(long startStamp,long startTableStamp,String sourceTableName, long totalSourceRecords,
			long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed) {
	}

	public void startMonitoringBulkStransfer(long startStamp,Database sourceDbAlias, Database destinationDbAlias,
			TreeMap<String, List<List<String>>> sourceTables, TreeMap<String, List<List<String>>> destinationTables) {
	}
	
	public void endMonitoringBulkTransfer(long startStamp,long endStamp,Database sourceDbAlias, Database destinationDbAlias,
			TreeMap<String, List<List<String>>> sourceTables, TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
	}
}
