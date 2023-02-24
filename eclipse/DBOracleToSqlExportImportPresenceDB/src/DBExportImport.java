import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import inovo.db.Database;
import inovo.db.DatabaseBulkSourceDestinationTableTransferMonitor;
import inovo.servlet.InovoCoreEnvironmentManager;

public class DBExportImport {

	private static ExecutorService executorService=new ThreadPoolExecutor(0, Integer.MAX_VALUE,10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()){
		protected void afterExecute(Runnable r, Throwable t) {
			super.afterExecute(r, t);
			if (t == null){
		    	 if(r instanceof Future<?>) {
		    		 try {
		    			 Object result = ((Future<?>) r).get();
		    			 if (result instanceof InovoCoreEnvironmentManager){
		    				 this.submit((Runnable)result);
		    			 }
		    		 } catch (CancellationException ce) {
			           t = ce;
		    		 } catch (ExecutionException ee) {
			           t = ee.getCause();
		    		 } catch (InterruptedException ie) {
			           Thread.currentThread().interrupt(); // ignore/reset
		    		 }
		    	 } else if(r instanceof Runnable){
		    		 new Thread(r).start();
		    		 //this.execute(r);
		    	 }
		    }
			if (t != null) System.out.println(t);
		}
	};
	
	private static List<InovoCoreEnvironmentManager> inovoCoreEnvironmentManagers=Collections.synchronizedList(new ArrayList<InovoCoreEnvironmentManager>());
	
	public static void main(String[] args) {
		//MAIN LIST OF PCO_ TABLES
		
		//exportPcoCoreTables();-done
		//exportPcoChangeLog();-done
		//exportPcoLog();-done
		//exportPcoInboundLog();-done
		//exportPcoOutboundLog();-done
		//exportPcoOutboundQueue();-done
		
		//TODO
		//exportPcoServiceWorkLog();
		//exportPcoSoftphoneCallLog();
		//exportPcoWorklog();-done
		
		//exportPMsg();
		
		//exportPir();
		//exportPirLog();
		//TODO
		
		//exportPrec();-done
		//exportPStat();-done
		//exportPRecRecording();-done
		
		exportWrapupExport();
	}

	/*
	 private static void exportPcoChangeLog() {
		executorService.submit((Callable<?>)new InovoCoreEnvironmentManager(){
			
			private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
			
			long startStamp=0;
			
			@Override
			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
				
			}
			
			@Override
			public String defaultServletContextName() {
				return "DBOracleToSqlExportImportPresenceDB";
			}
			
			@Override
			public String defaultLocalPath(String suggestedlocalpath) {
				System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
				return super.defaultLocalPath(suggestedlocalpath);
			}
		});
		
	}
	 */
	
	private static void exportWrapupExport() {
		executorService.submit((Callable<?>)new InovoCoreEnvironmentManager(){
			
			private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
			
			long startStamp=0;
			
			@Override
			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
				
			}
			
			@Override
			public String defaultServletContextName() {
				return "DBOracleToSqlExportImportPresenceDB";
			}
			
			@Override
			public String defaultLocalPath(String suggestedlocalpath) {
				System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
				return super.defaultLocalPath(suggestedlocalpath);
			}
		});
		
	}
	
	
	private static void exportPMsg() {
		executorService.submit((Callable<?>)new InovoCoreEnvironmentManager(){
			
			private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
			
			long startStamp=0;
			
			@Override
			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
				this.databaseBulkSourceDestinationTableTransferMonitor=new DatabaseBulkSourceDestinationTableTransferMonitor(Database.dballias("ORACLEPRES"),Database.dballias("MSSQLPRES")){
			    	@Override
			    	public String finalSourceTableSqlStatement(String sourceSqlStatement, String sourceTableName,String sourceColumns,String destinationSqlInsertUniqueIndexColumns,
			    			Database sourceDbAlias) {
			    		return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias);
			    	}
			    	
			    	@Override
			    	public boolean validSourceTable(String sourceTableName) {
			    		if(super.validSourceTable(sourceTableName)&&sourceTableName.startsWith("PMSG_")){
			    			return !sourceTableName.startsWith("PMSG_INBOUND")&&!sourceTableName.startsWith("PMSG_OUTBOUND");
			    		}
			    		return false;
			    	}
			    	
			    	@Override
			    	public boolean validDestinationTable(String destinationTable) {
			    		// TODO Auto-generated method stub
			    		return super.validDestinationTable(destinationTable);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkStransfer(long startStamp, Database sourceDbAlias, Database destinationDbAlias,
			    			TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables) {
			    		
			    		System.out.println("START_TRANSFER");
			    		System.out.println(startStamp);
			    		
			    		super.startMonitoringBulkStransfer(startStamp, sourceDbAlias, destinationDbAlias, sourceTables, destinationTables);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkTableTransfer(long startStamp, long startTableStamp, String sourceTableName) {
			    		if(sourceTableName.equals("PCO_AGENT")){
			    			sourceTableName=sourceTableName+"";
			    		}
			    		
			    		logDebug("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		
			    		System.out.println("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		System.out.println(startTableStamp);
			    		super.startMonitoringBulkTableTransfer(startStamp, startTableStamp, sourceTableName);
			    	}
			    	
			    	@Override
			    	public void progressTransferTableMonitor(long startStamp, long startTableStamp, String sourceTableName,
			    			long totalSourceRecords, long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed) {
			    		if(totalDestinationRequestsCompleted%10000==0){
				    		logDebug("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
				    		System.out.println(startTableStamp);
				    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
				    		
//				    		System.out.println("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
//				    		System.out.println(startTableStamp);
//				    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//				    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//				    		System.out.println(Calendar.getInstance().getTimeInMillis());
			    		}
			    		super.progressTransferTableMonitor(startStamp, startTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed);
			    	}
			    	
			    	@Override
			    	public void endTransferTableMonitor(long startStamp, long startTableStamp, long endTableStamp,
			    			String sourceTableName, long totalSourceRecords, long totalDestinationRequestsCompleted,
			    			long totalDestinationRequestsFailed,Exception ex) {
			    		if(ex!=null){
			    			ex.printStackTrace();
			    		}
			    		logDebug("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
			    		
			    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
			    		logDebug("total-duration:"+(endTableStamp-startTableStamp));
//			    		System.out.println("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
//			    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//			    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//			    		System.out.println(startTableStamp);
//			    		System.out.println(endTableStamp);
//			    		System.out.println(endTableStamp-startTableStamp);
			    		
			    		super.endTransferTableMonitor(startStamp, startTableStamp, endTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed,ex);
			    	}
			    	
			    	@Override
			    	public void endMonitoringBulkTransfer(long startStamp, long endStamp, Database sourceDbAlias,
			    			Database destinationDbAlias, TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
			    		
			    		logDebug("END_TRANSFER:DONE");
			    		logDebug("total-duration:"+(endStamp-startStamp));
//			    		System.out.println("END_TRANSFER:DONE");
//			    		System.out.println(startStamp);
//			    		System.out.println(endStamp);
//			    		System.out.println(endStamp-startStamp);
//			    		
			    		if(failedTables!=null&&!failedTables.isEmpty()){
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    			while(!failedTables.isEmpty()){
			    				logDebug(failedTables.remove(0));
			    			}
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    		}
			    		
			    		super.endMonitoringBulkTransfer(startStamp, endStamp, sourceDbAlias, destinationDbAlias, sourceTables,
			    				destinationTables,failedTables);
			    	}
			    	
			    	@Override
			    	public int batchExecuteTableCommitSize(int commitSize, String sourceTableName) {
			    		return super.batchExecuteTableCommitSize(1000,sourceTableName);//sourceTableName.equals("PCO_OUTBOUNDQUEUE")?1:commitSize, sourceTableName);
			    	}
			    	
			    };
			    this.databaseBulkSourceDestinationTableTransferMonitor.requestTransfer(true);
			}
			
			@Override
			public String defaultServletContextName() {
				return "DBOracleToSqlExportImportPresenceDB";
			}
			
			@Override
			public String defaultLocalPath(String suggestedlocalpath) {
				System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
				return super.defaultLocalPath(suggestedlocalpath);
			}
		});
		
	}
	
	private static void exportPrec() {
		InovoCoreEnvironmentManager inovoCoreEnvironmentManager=new InovoCoreEnvironmentManager(){
			
			private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
			
			long startStamp=0;
			
			@Override
			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
				this.databaseBulkSourceDestinationTableTransferMonitor=new DatabaseBulkSourceDestinationTableTransferMonitor(Database.dballias("ORACLEPRES"),Database.dballias("MSSQLPRES")){
			    	@Override
			    	public String finalSourceTableSqlStatement(String sourceSqlStatement, String sourceTableName,String sourceColumns,String destinationSqlInsertUniqueIndexColumns,
			    			Database sourceDbAlias) {
			    		return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias)+((","+sourceColumns+",").contains(",ID,")?" WHERE ID>=0":"")+((","+sourceColumns+",").contains(",RDATE,")?" ORDER BY RDATE DESC":((","+sourceColumns+",").contains(",ID,")?" ORDER BY ID DESC":""));
			    	}
			    	
			    	@Override
			    	public boolean canTruncateTable(String sourceTableName) {
			    		return true;
			    	}
			    	
			    	@Override
			    	public boolean validSourceTable(String sourceTableName) {
			    		if (sourceTableName.startsWith("PREC_")){
			    			return !sourceTableName.equals("PREC_RECORDING");
			    		}
			    		return false;
			    	}
			    	
			    	@Override
			    	public boolean validDestinationTable(String destinationTable) {
			    		if (destinationTable.startsWith("PREC_")){
			    			return !destinationTable.equals("PREC_RECORDING");
			    		}
			    		return false;
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkStransfer(long startStamp, Database sourceDbAlias, Database destinationDbAlias,
			    			TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables) {
			    		
			    		System.out.println("START_TRANSFER");
			    		System.out.println(startStamp);
			    		
			    		super.startMonitoringBulkStransfer(startStamp, sourceDbAlias, destinationDbAlias, sourceTables, destinationTables);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkTableTransfer(long startStamp, long startTableStamp, String sourceTableName) {
			    		if(sourceTableName.equals("PCO_AGENT")){
			    			sourceTableName=sourceTableName+"";
			    		}
			    		
			    		logDebug("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		
			    		System.out.println("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		System.out.println(startTableStamp);
			    		super.startMonitoringBulkTableTransfer(startStamp, startTableStamp, sourceTableName);
			    	}
			    	
			    	@Override
			    	public void progressTransferTableMonitor(long startStamp, long startTableStamp, String sourceTableName,
			    			long totalSourceRecords, long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed) {
			    		if(totalDestinationRequestsCompleted%10000==0){
				    		logDebug("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
				    		System.out.println(startTableStamp);
				    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
				    		
//				    		System.out.println("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
//				    		System.out.println(startTableStamp);
//				    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//				    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//				    		System.out.println(Calendar.getInstance().getTimeInMillis());
			    		}
			    		super.progressTransferTableMonitor(startStamp, startTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed);
			    	}
			    	
			    	@Override
			    	public void endTransferTableMonitor(long startStamp, long startTableStamp, long endTableStamp,
			    			String sourceTableName, long totalSourceRecords, long totalDestinationRequestsCompleted,
			    			long totalDestinationRequestsFailed,Exception ex) {
			    		if(ex!=null){
			    			ex.printStackTrace();
			    		}
			    		logDebug("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
			    		
			    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
			    		logDebug("total-duration:"+(endTableStamp-startTableStamp));
//			    		System.out.println("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
//			    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//			    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//			    		System.out.println(startTableStamp);
//			    		System.out.println(endTableStamp);
//			    		System.out.println(endTableStamp-startTableStamp);
			    		
			    		super.endTransferTableMonitor(startStamp, startTableStamp, endTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed,ex);
			    	}
			    	
			    	@Override
			    	public void endMonitoringBulkTransfer(long startStamp, long endStamp, Database sourceDbAlias,
			    			Database destinationDbAlias, TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
			    		
			    		logDebug("END_TRANSFER:DONE");
			    		logDebug("total-duration:"+(endStamp-startStamp));
//			    		System.out.println("END_TRANSFER:DONE");
//			    		System.out.println(startStamp);
//			    		System.out.println(endStamp);
//			    		System.out.println(endStamp-startStamp);
//			    		
			    		if(failedTables!=null&&!failedTables.isEmpty()){
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    			while(!failedTables.isEmpty()){
			    				logDebug(failedTables.remove(0));
			    			}
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    		}
			    		
			    		super.endMonitoringBulkTransfer(startStamp, endStamp, sourceDbAlias, destinationDbAlias, sourceTables,
			    				destinationTables,failedTables);
			    	}
			    	
			    	@Override
			    	public int batchExecuteTableCommitSize(int commitSize, String sourceTableName) {
			    		return super.batchExecuteTableCommitSize(1000,sourceTableName);//sourceTableName.equals("PCO_OUTBOUNDQUEUE")?1:commitSize, sourceTableName);
			    	}
			    	
			    };
			    this.databaseBulkSourceDestinationTableTransferMonitor.requestTransfer(true);
			}
			
			@Override
			public String defaultServletContextName() {
				return "DBOracleToSqlExportImportPresenceDB";
			}
			
			@Override
			public String defaultLocalPath(String suggestedlocalpath) {
				System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
				return super.defaultLocalPath(suggestedlocalpath);
			}
		};
		
		
		inovoCoreEnvironmentManager.run();
	}
	
	private static void exportPStat() {
		InovoCoreEnvironmentManager inovoCoreEnvironmentManager=new InovoCoreEnvironmentManager(){
			
			private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
			
			long startStamp=0;
			
			@Override
			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
				this.databaseBulkSourceDestinationTableTransferMonitor=new DatabaseBulkSourceDestinationTableTransferMonitor(Database.dballias("ORACLEPRES"),Database.dballias("MSSQLPRES")){
			    	@Override
			    	public String finalSourceTableSqlStatement(String sourceSqlStatement, String sourceTableName,String sourceColumns,String destinationSqlInsertUniqueIndexColumns,
			    			Database sourceDbAlias) {
			    		return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias)+((","+sourceColumns+",").contains(",ID,")?" WHERE ID>=0":"")+((","+sourceColumns+",").contains(",RDATE,")?" ORDER BY RDATE DESC":((","+sourceColumns+",").contains(",ID,")?" ORDER BY ID DESC":""));
			    	}
			    	
			    	@Override
			    	public boolean canTruncateTable(String sourceTableName) {
			    		return true;
			    	}
			    	
			    	@Override
			    	public boolean validSourceTable(String sourceTableName) {
			    		if (sourceTableName.startsWith("PSTAT_")){
			    			return true;
			    		}
			    		return false;
			    	}
			    	
			    	@Override
			    	public boolean validDestinationTable(String destinationTable) {
			    		if (destinationTable.startsWith("PSTAT_")){
			    			return true;
			    		}
			    		return false;
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkStransfer(long startStamp, Database sourceDbAlias, Database destinationDbAlias,
			    			TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables) {
			    		
			    		System.out.println("START_TRANSFER");
			    		System.out.println(startStamp);
			    		
			    		super.startMonitoringBulkStransfer(startStamp, sourceDbAlias, destinationDbAlias, sourceTables, destinationTables);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkTableTransfer(long startStamp, long startTableStamp, String sourceTableName) {
			    		if(sourceTableName.equals("PCO_AGENT")){
			    			sourceTableName=sourceTableName+"";
			    		}
			    		
			    		logDebug("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		
			    		System.out.println("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		System.out.println(startTableStamp);
			    		super.startMonitoringBulkTableTransfer(startStamp, startTableStamp, sourceTableName);
			    	}
			    	
			    	@Override
			    	public void progressTransferTableMonitor(long startStamp, long startTableStamp, String sourceTableName,
			    			long totalSourceRecords, long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed) {
			    		if(totalDestinationRequestsCompleted%10000==0){
				    		logDebug("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
				    		System.out.println(startTableStamp);
				    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
				    		
//				    		System.out.println("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
//				    		System.out.println(startTableStamp);
//				    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//				    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//				    		System.out.println(Calendar.getInstance().getTimeInMillis());
			    		}
			    		super.progressTransferTableMonitor(startStamp, startTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed);
			    	}
			    	
			    	@Override
			    	public void endTransferTableMonitor(long startStamp, long startTableStamp, long endTableStamp,
			    			String sourceTableName, long totalSourceRecords, long totalDestinationRequestsCompleted,
			    			long totalDestinationRequestsFailed,Exception ex) {
			    		if(ex!=null){
			    			ex.printStackTrace();
			    		}
			    		logDebug("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
			    		
			    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
			    		logDebug("total-duration:"+(endTableStamp-startTableStamp));
//			    		System.out.println("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
//			    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//			    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//			    		System.out.println(startTableStamp);
//			    		System.out.println(endTableStamp);
//			    		System.out.println(endTableStamp-startTableStamp);
			    		
			    		super.endTransferTableMonitor(startStamp, startTableStamp, endTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed,ex);
			    	}
			    	
			    	@Override
			    	public void endMonitoringBulkTransfer(long startStamp, long endStamp, Database sourceDbAlias,
			    			Database destinationDbAlias, TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
			    		
			    		logDebug("END_TRANSFER:DONE");
			    		logDebug("total-duration:"+(endStamp-startStamp));
//			    		System.out.println("END_TRANSFER:DONE");
//			    		System.out.println(startStamp);
//			    		System.out.println(endStamp);
//			    		System.out.println(endStamp-startStamp);
//			    		
			    		if(failedTables!=null&&!failedTables.isEmpty()){
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    			while(!failedTables.isEmpty()){
			    				logDebug(failedTables.remove(0));
			    			}
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    		}
			    		
			    		super.endMonitoringBulkTransfer(startStamp, endStamp, sourceDbAlias, destinationDbAlias, sourceTables,
			    				destinationTables,failedTables);
			    	}
			    	
			    	@Override
			    	public int batchExecuteTableCommitSize(int commitSize, String sourceTableName) {
			    		return super.batchExecuteTableCommitSize(1000,sourceTableName);//sourceTableName.equals("PCO_OUTBOUNDQUEUE")?1:commitSize, sourceTableName);
			    	}
			    	
			    };
			    this.databaseBulkSourceDestinationTableTransferMonitor.requestTransfer(true);
			}
			
			@Override
			public String defaultServletContextName() {
				return "DBOracleToSqlExportImportPresenceDB";
			}
			
			@Override
			public String defaultLocalPath(String suggestedlocalpath) {
				System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
				return super.defaultLocalPath(suggestedlocalpath);
			}
		};
		
		
		inovoCoreEnvironmentManager.run();
	}

	private static void exportPirLog() {
		executorService.submit((Callable<?>)new InovoCoreEnvironmentManager(){
			
			private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
			
			long startStamp=0;
			
			@Override
			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
				this.databaseBulkSourceDestinationTableTransferMonitor=new DatabaseBulkSourceDestinationTableTransferMonitor(Database.dballias("ORACLEPRES"),Database.dballias("MSSQLPRES")){
			    	@Override
			    	public String finalSourceTableSqlStatement(String sourceSqlStatement, String sourceTableName,String sourceColumns,String destinationSqlInsertUniqueIndexColumns,
			    			Database sourceDbAlias) {
			    		if(sourceTableName.equals("PIR_STARTSTRATEGYLOG")||sourceTableName.equals("PIR_ENDSTRATEGYLOG")){
			    			return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias)+" ORDER BY RDATE DESC";
			    		}
			    		else if(sourceTableName.equals("PIR_ENDSTRATEGYLOGDATA")){
			    			return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias)+" ORDER BY STARTSTRATEGYLOGID DESC";
			    		}
			    		return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias);
			    	}
			    	
			    	@Override
			    	public boolean canTruncateTable(String sourceTableName) {
			    		return false;
			    	}
			    	
			    	@Override
			    	public boolean validSourceTable(String sourceTableName) {
			    		if(super.validSourceTable(sourceTableName)&&sourceTableName.startsWith("PIR_")){
			    			return sourceTableName.indexOf("LOG")>0&&!sourceTableName.equals("PIR_EVENTLOG");
			    		}
			    		return false;
			    	}
			    	
			    	@Override
			    	public boolean validDestinationTable(String destinationTable) {
			    		// TODO Auto-generated method stub
			    		return super.validDestinationTable(destinationTable);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkStransfer(long startStamp, Database sourceDbAlias, Database destinationDbAlias,
			    			TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables) {
			    		
			    		System.out.println("START_TRANSFER");
			    		System.out.println(startStamp);
			    		
			    		super.startMonitoringBulkStransfer(startStamp, sourceDbAlias, destinationDbAlias, sourceTables, destinationTables);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkTableTransfer(long startStamp, long startTableStamp, String sourceTableName) {
			    		if(sourceTableName.equals("PCO_AGENT")){
			    			sourceTableName=sourceTableName+"";
			    		}
			    		
			    		logDebug("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		
			    		System.out.println("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		System.out.println(startTableStamp);
			    		super.startMonitoringBulkTableTransfer(startStamp, startTableStamp, sourceTableName);
			    	}
			    	
			    	@Override
			    	public void progressTransferTableMonitor(long startStamp, long startTableStamp, String sourceTableName,
			    			long totalSourceRecords, long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed) {
			    		if(totalDestinationRequestsCompleted%10000==0){
				    		logDebug("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
				    		System.out.println(startTableStamp);
				    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
				    		
//				    		System.out.println("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
//				    		System.out.println(startTableStamp);
//				    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//				    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//				    		System.out.println(Calendar.getInstance().getTimeInMillis());
			    		}
			    		super.progressTransferTableMonitor(startStamp, startTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed);
			    	}
			    	
			    	@Override
			    	public void endTransferTableMonitor(long startStamp, long startTableStamp, long endTableStamp,
			    			String sourceTableName, long totalSourceRecords, long totalDestinationRequestsCompleted,
			    			long totalDestinationRequestsFailed,Exception ex) {
			    		if(ex!=null){
			    			ex.printStackTrace();
			    		}
			    		logDebug("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
			    		
			    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
			    		logDebug("total-duration:"+(endTableStamp-startTableStamp));
//			    		System.out.println("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
//			    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//			    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//			    		System.out.println(startTableStamp);
//			    		System.out.println(endTableStamp);
//			    		System.out.println(endTableStamp-startTableStamp);
			    		
			    		super.endTransferTableMonitor(startStamp, startTableStamp, endTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed,ex);
			    	}
			    	
			    	@Override
			    	public void endMonitoringBulkTransfer(long startStamp, long endStamp, Database sourceDbAlias,
			    			Database destinationDbAlias, TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
			    		
			    		logDebug("END_TRANSFER:DONE");
			    		logDebug("total-duration:"+(endStamp-startStamp));
//			    		System.out.println("END_TRANSFER:DONE");
//			    		System.out.println(startStamp);
//			    		System.out.println(endStamp);
//			    		System.out.println(endStamp-startStamp);
//			    		
			    		if(failedTables!=null&&!failedTables.isEmpty()){
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    			while(!failedTables.isEmpty()){
			    				logDebug(failedTables.remove(0));
			    			}
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    		}
			    		
			    		super.endMonitoringBulkTransfer(startStamp, endStamp, sourceDbAlias, destinationDbAlias, sourceTables,
			    				destinationTables,failedTables);
			    	}
			    	
			    	@Override
			    	public int batchExecuteTableCommitSize(int commitSize, String sourceTableName) {
			    		return super.batchExecuteTableCommitSize(1000,sourceTableName);//sourceTableName.equals("PCO_OUTBOUNDQUEUE")?1:commitSize, sourceTableName);
			    	}
			    	
			    };
			    this.databaseBulkSourceDestinationTableTransferMonitor.requestTransfer(true);
			}
			
			@Override
			public String defaultServletContextName() {
				return "DBOracleToSqlExportImportPresenceDB";
			}
			
			@Override
			public String defaultLocalPath(String suggestedlocalpath) {
				System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
				return super.defaultLocalPath(suggestedlocalpath);
			}
		});
		
	}
	
	private static void exportPir() {
		executorService.submit((Callable<?>)new InovoCoreEnvironmentManager(){
			
			private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
			
			long startStamp=0;
			
			@Override
			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
				this.databaseBulkSourceDestinationTableTransferMonitor=new DatabaseBulkSourceDestinationTableTransferMonitor(Database.dballias("ORACLEPRES"),Database.dballias("MSSQLPRES")){
			    	@Override
			    	public String finalSourceTableSqlStatement(String sourceSqlStatement, String sourceTableName,String sourceColumns,String destinationSqlInsertUniqueIndexColumns,
			    			Database sourceDbAlias) {
			    		return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias);
			    	}
			    	
			    	@Override
			    	public boolean validSourceTable(String sourceTableName) {
			    		if(super.validSourceTable(sourceTableName)&&sourceTableName.startsWith("PIR_")){
			    			return sourceTableName.indexOf("LOG")==-1;
			    		}
			    		return false;
			    	}
			    	
			    	@Override
			    	public boolean validDestinationTable(String destinationTable) {
			    		// TODO Auto-generated method stub
			    		return super.validDestinationTable(destinationTable);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkStransfer(long startStamp, Database sourceDbAlias, Database destinationDbAlias,
			    			TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables) {
			    		
			    		System.out.println("START_TRANSFER");
			    		System.out.println(startStamp);
			    		
			    		super.startMonitoringBulkStransfer(startStamp, sourceDbAlias, destinationDbAlias, sourceTables, destinationTables);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkTableTransfer(long startStamp, long startTableStamp, String sourceTableName) {
			    		if(sourceTableName.equals("PCO_AGENT")){
			    			sourceTableName=sourceTableName+"";
			    		}
			    		
			    		logDebug("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		
			    		System.out.println("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		System.out.println(startTableStamp);
			    		super.startMonitoringBulkTableTransfer(startStamp, startTableStamp, sourceTableName);
			    	}
			    	
			    	@Override
			    	public void progressTransferTableMonitor(long startStamp, long startTableStamp, String sourceTableName,
			    			long totalSourceRecords, long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed) {
			    		if(totalDestinationRequestsCompleted%10000==0){
				    		logDebug("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
				    		System.out.println(startTableStamp);
				    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
				    		
//				    		System.out.println("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
//				    		System.out.println(startTableStamp);
//				    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//				    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//				    		System.out.println(Calendar.getInstance().getTimeInMillis());
			    		}
			    		super.progressTransferTableMonitor(startStamp, startTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed);
			    	}
			    	
			    	@Override
			    	public void endTransferTableMonitor(long startStamp, long startTableStamp, long endTableStamp,
			    			String sourceTableName, long totalSourceRecords, long totalDestinationRequestsCompleted,
			    			long totalDestinationRequestsFailed,Exception ex) {
			    		if(ex!=null){
			    			ex.printStackTrace();
			    		}
			    		logDebug("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
			    		
			    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
			    		logDebug("total-duration:"+(endTableStamp-startTableStamp));
//			    		System.out.println("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
//			    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//			    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//			    		System.out.println(startTableStamp);
//			    		System.out.println(endTableStamp);
//			    		System.out.println(endTableStamp-startTableStamp);
			    		
			    		super.endTransferTableMonitor(startStamp, startTableStamp, endTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed,ex);
			    	}
			    	
			    	@Override
			    	public void endMonitoringBulkTransfer(long startStamp, long endStamp, Database sourceDbAlias,
			    			Database destinationDbAlias, TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
			    		
			    		logDebug("END_TRANSFER:DONE");
			    		logDebug("total-duration:"+(endStamp-startStamp));
//			    		System.out.println("END_TRANSFER:DONE");
//			    		System.out.println(startStamp);
//			    		System.out.println(endStamp);
//			    		System.out.println(endStamp-startStamp);
//			    		
			    		if(failedTables!=null&&!failedTables.isEmpty()){
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    			while(!failedTables.isEmpty()){
			    				logDebug(failedTables.remove(0));
			    			}
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    		}
			    		
			    		super.endMonitoringBulkTransfer(startStamp, endStamp, sourceDbAlias, destinationDbAlias, sourceTables,
			    				destinationTables,failedTables);
			    	}
			    	
			    	@Override
			    	public int batchExecuteTableCommitSize(int commitSize, String sourceTableName) {
			    		return super.batchExecuteTableCommitSize(1000,sourceTableName);//sourceTableName.equals("PCO_OUTBOUNDQUEUE")?1:commitSize, sourceTableName);
			    	}
			    	
			    };
			    this.databaseBulkSourceDestinationTableTransferMonitor.requestTransfer(true);
			}
			
			@Override
			public String defaultServletContextName() {
				return "DBOracleToSqlExportImportPresenceDB";
			}
			
			@Override
			public String defaultLocalPath(String suggestedlocalpath) {
				System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
				return super.defaultLocalPath(suggestedlocalpath);
			}
		});
		
	}
	
	private static void exportPcoSoftphoneCallLog() {
		executorService.submit((Callable<?>)new InovoCoreEnvironmentManager(){
			
			private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
			
			long startStamp=0;
			
			@Override
			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
				this.databaseBulkSourceDestinationTableTransferMonitor=new DatabaseBulkSourceDestinationTableTransferMonitor(Database.dballias("ORACLEPRES"),Database.dballias("MSSQLPRES")){
			    	@Override
			    	public String finalSourceTableSqlStatement(String sourceSqlStatement, String sourceTableName,String sourceColumns,String destinationSqlInsertUniqueIndexColumns,
			    			Database sourceDbAlias) {
			    		return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias)+((","+sourceColumns+",").contains(",ID,")?" WHERE ID>=0":"")+((","+sourceColumns+",").contains(",RDATE,")?" ORDER BY RDATE DESC":"");
			    	}
				
			    	@Override
			    	public boolean validSourceTable(String sourceTableName) {
			    		return super.validSourceTable(sourceTableName)&&sourceTableName.equals("PCO_SOFTPHONECALLLOG");
			    	}
			    	
			    	@Override
			    	public boolean validDestinationTable(String destinationTable) {
			    		// TODO Auto-generated method stub
			    		return super.validDestinationTable(destinationTable);
			    	}
			    	
			    	@Override
			    	public boolean canTruncateTable(String sourceTableName) {
			    		return false;
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkStransfer(long startStamp, Database sourceDbAlias, Database destinationDbAlias,
			    			TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables) {
			    		
			    		System.out.println("START_TRANSFER");
			    		System.out.println(startStamp);
			    		
			    		super.startMonitoringBulkStransfer(startStamp, sourceDbAlias, destinationDbAlias, sourceTables, destinationTables);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkTableTransfer(long startStamp, long startTableStamp, String sourceTableName) {
			    		if(sourceTableName.equals("PCO_AGENT")){
			    			sourceTableName=sourceTableName+"";
			    		}
			    		
			    		logDebug("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		
			    		System.out.println("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		System.out.println(startTableStamp);
			    		super.startMonitoringBulkTableTransfer(startStamp, startTableStamp, sourceTableName);
			    	}
			    	
			    	@Override
			    	public void progressTransferTableMonitor(long startStamp, long startTableStamp, String sourceTableName,
			    			long totalSourceRecords, long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed) {
			    		if(totalDestinationRequestsCompleted%10000==0){
				    		logDebug("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
				    		System.out.println(startTableStamp);
				    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
				    		
//				    		System.out.println("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
//				    		System.out.println(startTableStamp);
//				    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//				    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//				    		System.out.println(Calendar.getInstance().getTimeInMillis());
			    		}
			    		super.progressTransferTableMonitor(startStamp, startTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed);
			    	}
			    	
			    	@Override
			    	public void endTransferTableMonitor(long startStamp, long startTableStamp, long endTableStamp,
			    			String sourceTableName, long totalSourceRecords, long totalDestinationRequestsCompleted,
			    			long totalDestinationRequestsFailed,Exception ex) {
			    		if(ex!=null){
			    			ex.printStackTrace();
			    		}
			    		logDebug("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
			    		
			    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
			    		logDebug("total-duration:"+(endTableStamp-startTableStamp));
//			    		System.out.println("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
//			    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//			    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//			    		System.out.println(startTableStamp);
//			    		System.out.println(endTableStamp);
//			    		System.out.println(endTableStamp-startTableStamp);
			    		
			    		super.endTransferTableMonitor(startStamp, startTableStamp, endTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed,ex);
			    	}
			    	
			    	@Override
			    	public void endMonitoringBulkTransfer(long startStamp, long endStamp, Database sourceDbAlias,
			    			Database destinationDbAlias, TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
			    		
			    		logDebug("END_TRANSFER:DONE");
			    		logDebug("total-duration:"+(endStamp-startStamp));
//			    		System.out.println("END_TRANSFER:DONE");
//			    		System.out.println(startStamp);
//			    		System.out.println(endStamp);
//			    		System.out.println(endStamp-startStamp);
//			    		
			    		if(failedTables!=null&&!failedTables.isEmpty()){
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    			while(!failedTables.isEmpty()){
			    				logDebug(failedTables.remove(0));
			    			}
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    		}
			    		
			    		super.endMonitoringBulkTransfer(startStamp, endStamp, sourceDbAlias, destinationDbAlias, sourceTables,
			    				destinationTables,failedTables);
			    	}
			    	
			    	@Override
			    	public int batchExecuteTableCommitSize(int commitSize, String sourceTableName) {
			    		return super.batchExecuteTableCommitSize(1000,sourceTableName);//sourceTableName.equals("PCO_OUTBOUNDQUEUE")?1:commitSize, sourceTableName);
			    	}
			    	
			    };
			    this.databaseBulkSourceDestinationTableTransferMonitor.requestTransfer(true);
			}
			
			@Override
			public String defaultServletContextName() {
				return "DBOracleToSqlExportImportPresenceDB";
			}
			
			@Override
			public String defaultLocalPath(String suggestedlocalpath) {
				System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
				return super.defaultLocalPath(suggestedlocalpath);
			}
		});
		
	}
	
	private static void exportPcoServiceWorkLog() {
		executorService.submit((Callable<?>)new InovoCoreEnvironmentManager(){
			
			private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
			
			long startStamp=0;
			
			@Override
			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
				this.databaseBulkSourceDestinationTableTransferMonitor=new DatabaseBulkSourceDestinationTableTransferMonitor(Database.dballias("ORACLEPRES"),Database.dballias("MSSQLPRES")){
			    	@Override
			    	public String finalSourceTableSqlStatement(String sourceSqlStatement, String sourceTableName,String sourceColumns,String destinationSqlInsertUniqueIndexColumns,
			    			Database sourceDbAlias) {
			    		return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias)+((","+sourceColumns+",").contains(",STARTDATE,")?" ORDER BY STARTDATE DESC":"");
			    	}
			    	
			    	@Override
			    	public boolean canTruncateTable(String sourceTableName) {
			    		return true;
			    	}
			    	
			    	@Override
			    	public boolean validSourceTable(String sourceTableName) {
			    		return super.validSourceTable(sourceTableName)&&sourceTableName.equals("PCO_SERVICEWORKLOG");
			    	}
			    	
			    	@Override
			    	public boolean validDestinationTable(String destinationTable) {
			    		// TODO Auto-generated method stub
			    		return super.validDestinationTable(destinationTable);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkStransfer(long startStamp, Database sourceDbAlias, Database destinationDbAlias,
			    			TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables) {
			    		
			    		System.out.println("START_TRANSFER");
			    		System.out.println(startStamp);
			    		
			    		super.startMonitoringBulkStransfer(startStamp, sourceDbAlias, destinationDbAlias, sourceTables, destinationTables);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkTableTransfer(long startStamp, long startTableStamp, String sourceTableName) {
			    		if(sourceTableName.equals("PCO_AGENT")){
			    			sourceTableName=sourceTableName+"";
			    		}
			    		
			    		logDebug("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		
			    		System.out.println("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		System.out.println(startTableStamp);
			    		super.startMonitoringBulkTableTransfer(startStamp, startTableStamp, sourceTableName);
			    	}
			    	
			    	@Override
			    	public void progressTransferTableMonitor(long startStamp, long startTableStamp, String sourceTableName,
			    			long totalSourceRecords, long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed) {
			    		if(totalDestinationRequestsCompleted%10000==0){
				    		logDebug("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
				    		System.out.println(startTableStamp);
				    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
				    		
//				    		System.out.println("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
//				    		System.out.println(startTableStamp);
//				    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//				    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//				    		System.out.println(Calendar.getInstance().getTimeInMillis());
			    		}
			    		super.progressTransferTableMonitor(startStamp, startTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed);
			    	}
			    	
			    	@Override
			    	public void endTransferTableMonitor(long startStamp, long startTableStamp, long endTableStamp,
			    			String sourceTableName, long totalSourceRecords, long totalDestinationRequestsCompleted,
			    			long totalDestinationRequestsFailed,Exception ex) {
			    		if(ex!=null){
			    			ex.printStackTrace();
			    		}
			    		logDebug("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
			    		
			    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
			    		logDebug("total-duration:"+(endTableStamp-startTableStamp));
//			    		System.out.println("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
//			    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//			    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//			    		System.out.println(startTableStamp);
//			    		System.out.println(endTableStamp);
//			    		System.out.println(endTableStamp-startTableStamp);
			    		
			    		super.endTransferTableMonitor(startStamp, startTableStamp, endTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed,ex);
			    	}
			    	
			    	@Override
			    	public void endMonitoringBulkTransfer(long startStamp, long endStamp, Database sourceDbAlias,
			    			Database destinationDbAlias, TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
			    		
			    		logDebug("END_TRANSFER:DONE");
			    		logDebug("total-duration:"+(endStamp-startStamp));
//			    		System.out.println("END_TRANSFER:DONE");
//			    		System.out.println(startStamp);
//			    		System.out.println(endStamp);
//			    		System.out.println(endStamp-startStamp);
//			    		
			    		if(failedTables!=null&&!failedTables.isEmpty()){
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    			while(!failedTables.isEmpty()){
			    				logDebug(failedTables.remove(0));
			    			}
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    		}
			    		
			    		super.endMonitoringBulkTransfer(startStamp, endStamp, sourceDbAlias, destinationDbAlias, sourceTables,
			    				destinationTables,failedTables);
			    	}
			    	
			    	@Override
			    	public int batchExecuteTableCommitSize(int commitSize, String sourceTableName) {
			    		return super.batchExecuteTableCommitSize(10000,sourceTableName);//sourceTableName.equals("PCO_OUTBOUNDQUEUE")?1:commitSize, sourceTableName);
			    	}
			    	
			    };
			    this.databaseBulkSourceDestinationTableTransferMonitor.requestTransfer(true);
			}
			
			@Override
			public String defaultServletContextName() {
				return "DBOracleToSqlExportImportPresenceDB";
			}
			
			@Override
			public String defaultLocalPath(String suggestedlocalpath) {
				System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
				return super.defaultLocalPath(suggestedlocalpath);
			}
		});
		
	}
	
	private static void exportPRecRecording() {
		InovoCoreEnvironmentManager inovoCoreEnvironmentManager=new InovoCoreEnvironmentManager(){
			
			private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
			
			long startStamp=0;
			
			@Override
			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
				this.databaseBulkSourceDestinationTableTransferMonitor=new DatabaseBulkSourceDestinationTableTransferMonitor(Database.dballias("ORACLEPRES"),Database.dballias("MSSQLPRES")){
			    	@Override
			    	public String finalSourceTableSqlStatement(String sourceSqlStatement, String sourceTableName,String sourceColumns,String destinationSqlInsertUniqueIndexColumns,
			    			Database sourceDbAlias) {
			    		return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias)+" WHERE ID >(12181252) "+((","+sourceColumns+",").contains(",RDATE,")?" ORDER BY RDATE DESC":((","+sourceColumns+",").contains(",ID,")?" ORDER BY ID ASC":""));
			    	}
			    	
			    	@Override
			    	public boolean canTruncateTable(String sourceTableName) {
			    		return false;//super.canTruncateTable(sourceTableName);
			    	}
			    	
			    	@Override
			    	public boolean validSourceTable(String sourceTableName) {
			    		return sourceTableName.equals("PREC_RECORDING");
			    	}
			    	
			    	@Override
			    	public boolean validDestinationTable(String destinationTable) {
			    		// TODO Auto-generated method stub
			    		return super.validDestinationTable(destinationTable);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkStransfer(long startStamp, Database sourceDbAlias, Database destinationDbAlias,
			    			TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables) {
			    		
			    		System.out.println("START_TRANSFER");
			    		System.out.println(startStamp);
			    		
			    		super.startMonitoringBulkStransfer(startStamp, sourceDbAlias, destinationDbAlias, sourceTables, destinationTables);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkTableTransfer(long startStamp, long startTableStamp, String sourceTableName) {
			    		if(sourceTableName.equals("PCO_AGENT")){
			    			sourceTableName=sourceTableName+"";
			    		}
			    		
			    		logDebug("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		
			    		System.out.println("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		System.out.println(startTableStamp);
			    		super.startMonitoringBulkTableTransfer(startStamp, startTableStamp, sourceTableName);
			    	}
			    	
			    	@Override
			    	public void progressTransferTableMonitor(long startStamp, long startTableStamp, String sourceTableName,
			    			long totalSourceRecords, long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed) {
			    		if(totalDestinationRequestsCompleted%10000==0){
				    		logDebug("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
				    		System.out.println(startTableStamp);
				    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
				    		
//				    		System.out.println("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
//				    		System.out.println(startTableStamp);
//				    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//				    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//				    		System.out.println(Calendar.getInstance().getTimeInMillis());
			    		}
			    		super.progressTransferTableMonitor(startStamp, startTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed);
			    	}
			    	
			    	@Override
			    	public void endTransferTableMonitor(long startStamp, long startTableStamp, long endTableStamp,
			    			String sourceTableName, long totalSourceRecords, long totalDestinationRequestsCompleted,
			    			long totalDestinationRequestsFailed,Exception ex) {
			    		if(ex!=null){
			    			ex.printStackTrace();
			    		}
			    		logDebug("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
			    		
			    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
			    		logDebug("total-duration:"+(endTableStamp-startTableStamp));
//			    		System.out.println("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
//			    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//			    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//			    		System.out.println(startTableStamp);
//			    		System.out.println(endTableStamp);
//			    		System.out.println(endTableStamp-startTableStamp);
			    		
			    		super.endTransferTableMonitor(startStamp, startTableStamp, endTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed,ex);
			    	}
			    	
			    	@Override
			    	public void endMonitoringBulkTransfer(long startStamp, long endStamp, Database sourceDbAlias,
			    			Database destinationDbAlias, TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
			    		
			    		logDebug("END_TRANSFER:DONE");
			    		logDebug("total-duration:"+(endStamp-startStamp));
//			    		System.out.println("END_TRANSFER:DONE");
//			    		System.out.println(startStamp);
//			    		System.out.println(endStamp);
//			    		System.out.println(endStamp-startStamp);
//			    		
			    		if(failedTables!=null&&!failedTables.isEmpty()){
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    			while(!failedTables.isEmpty()){
			    				logDebug(failedTables.remove(0));
			    			}
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    		}
			    		
			    		super.endMonitoringBulkTransfer(startStamp, endStamp, sourceDbAlias, destinationDbAlias, sourceTables,
			    				destinationTables,failedTables);
			    	}
			    	
			    	@Override
			    	public int batchExecuteTableCommitSize(int commitSize, String sourceTableName) {
			    		return super.batchExecuteTableCommitSize(1000,sourceTableName);//sourceTableName.equals("PCO_OUTBOUNDQUEUE")?1:commitSize, sourceTableName);
			    	}
			    	
			    };
			    this.databaseBulkSourceDestinationTableTransferMonitor.requestTransfer(true);
			}
			
			@Override
			public String defaultServletContextName() {
				return "DBOracleToSqlExportImportPresenceDB";
			}
			
			@Override
			public String defaultLocalPath(String suggestedlocalpath) {
				//suggestedlocalpath="D:/projects/clients/inovo/java/";
				System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
				return super.defaultLocalPath(suggestedlocalpath);
			}
			
			@Override
			public String defaultLogFileNamePostFix() {
				return "_PREC_RECORDING";
			}
		};
		
		executorService.submit((Callable)inovoCoreEnvironmentManager);
	}
	
	private static void exportPcoOutboundQueue(){
		executorService.submit((Callable<?>)new InovoCoreEnvironmentManager(){
				
				private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
				
				long startStamp=0;
				
				@Override
				public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
					this.databaseBulkSourceDestinationTableTransferMonitor=new DatabaseBulkSourceDestinationTableTransferMonitor(Database.dballias("ORACLEPRES"),Database.dballias("MSSQLPRES")){
				    	@Override
				    	public String finalSourceTableSqlStatement(String sourceSqlStatement, String sourceTableName,String sourceColumns,String destinationSqlInsertUniqueIndexColumns,
				    			Database sourceDbAlias) {
				    		return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias)+((","+sourceColumns+",").contains(",ID,")?" ORDER BY ID DESC":"");
				    	}
				    	
				    	@Override
				    	public boolean validSourceTable(String sourceTableName) {
				    		return super.validSourceTable(sourceTableName)&&sourceTableName.equals("PCO_OUTBOUNDQUEUE");
				    	}
				    	
				    	@Override
				    	public boolean canTruncateTable(String sourceTableName) {
				    		return true;
				    	}
				    	
				    	@Override
				    	public boolean validDestinationTable(String destinationTable) {
				    		// TODO Auto-generated method stub
				    		return super.validDestinationTable(destinationTable);
				    	}
				    	
				    	@Override
				    	public void startMonitoringBulkStransfer(long startStamp, Database sourceDbAlias, Database destinationDbAlias,
				    			TreeMap<String, List<List<String>>> sourceTables,
				    			TreeMap<String, List<List<String>>> destinationTables) {
				    		
				    		System.out.println("START_TRANSFER");
				    		System.out.println(startStamp);
				    		
				    		super.startMonitoringBulkStransfer(startStamp, sourceDbAlias, destinationDbAlias, sourceTables, destinationTables);
				    	}
				    	
				    	@Override
				    	public void startMonitoringBulkTableTransfer(long startStamp, long startTableStamp, String sourceTableName) {
				    		if(sourceTableName.equals("PCO_AGENT")){
				    			sourceTableName=sourceTableName+"";
				    		}
				    		
				    		logDebug("START_TRANSFER_TABLE["+sourceTableName+"]");
				    		
				    		System.out.println("START_TRANSFER_TABLE["+sourceTableName+"]");
				    		System.out.println(startTableStamp);
				    		super.startMonitoringBulkTableTransfer(startStamp, startTableStamp, sourceTableName);
				    	}
				    	
				    	@Override
				    	public void progressTransferTableMonitor(long startStamp, long startTableStamp, String sourceTableName,
				    			long totalSourceRecords, long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed) {
				    		if(totalDestinationRequestsCompleted%10000==0){
					    		logDebug("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
					    		System.out.println(startTableStamp);
					    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
					    		
//					    		System.out.println("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
//					    		System.out.println(startTableStamp);
//					    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//					    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//					    		System.out.println(Calendar.getInstance().getTimeInMillis());
				    		}
				    		super.progressTransferTableMonitor(startStamp, startTableStamp, sourceTableName, totalSourceRecords,
				    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed);
				    	}
				    	
				    	@Override
				    	public void endTransferTableMonitor(long startStamp, long startTableStamp, long endTableStamp,
				    			String sourceTableName, long totalSourceRecords, long totalDestinationRequestsCompleted,
				    			long totalDestinationRequestsFailed,Exception ex) {
				    		if(ex!=null){
				    			ex.printStackTrace();
				    		}
				    		logDebug("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
				    		
				    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
				    		logDebug("total-duration:"+(endTableStamp-startTableStamp));
//				    		System.out.println("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
//				    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//				    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//				    		System.out.println(startTableStamp);
//				    		System.out.println(endTableStamp);
//				    		System.out.println(endTableStamp-startTableStamp);
				    		
				    		super.endTransferTableMonitor(startStamp, startTableStamp, endTableStamp, sourceTableName, totalSourceRecords,
				    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed,ex);
				    	}
				    	
				    	@Override
				    	public void endMonitoringBulkTransfer(long startStamp, long endStamp, Database sourceDbAlias,
				    			Database destinationDbAlias, TreeMap<String, List<List<String>>> sourceTables,
				    			TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
				    		
				    		logDebug("END_TRANSFER:DONE");
				    		logDebug("total-duration:"+(endStamp-startStamp));
//				    		System.out.println("END_TRANSFER:DONE");
//				    		System.out.println(startStamp);
//				    		System.out.println(endStamp);
//				    		System.out.println(endStamp-startStamp);
//				    		
				    		if(failedTables!=null&&!failedTables.isEmpty()){
				    			logDebug("[FAILED_TRANSFER_TABLES]");
				    			while(!failedTables.isEmpty()){
				    				logDebug(failedTables.remove(0));
				    			}
				    			logDebug("[FAILED_TRANSFER_TABLES]");
				    		}
				    		
				    		super.endMonitoringBulkTransfer(startStamp, endStamp, sourceDbAlias, destinationDbAlias, sourceTables,
				    				destinationTables,failedTables);
				    	}
				    	
				    	@Override
				    	public int batchExecuteTableCommitSize(int commitSize, String sourceTableName) {
				    		return super.batchExecuteTableCommitSize(1000,sourceTableName);//sourceTableName.equals("PCO_OUTBOUNDQUEUE")?1:commitSize, sourceTableName);
				    	}
				    	
				    };
				    this.databaseBulkSourceDestinationTableTransferMonitor.requestTransfer(true);
				}
				
				@Override
				public String defaultServletContextName() {
					return "DBOracleToSqlExportImportPresenceDB";
				}
				
				@Override
				public String defaultLocalPath(String suggestedlocalpath) {
					System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
					return super.defaultLocalPath(suggestedlocalpath);
				}
		});
	}
	
	private static void exportPcoOutboundLog() {
		executorService.submit((Callable<?>)new InovoCoreEnvironmentManager(){
			
			private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
			
			long startStamp=0;
			
			@Override
			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
				this.databaseBulkSourceDestinationTableTransferMonitor=new DatabaseBulkSourceDestinationTableTransferMonitor(Database.dballias("ORACLEPRES"),Database.dballias("MSSQLPRES")){
			    	@Override
			    	public String finalSourceTableSqlStatement(String sourceSqlStatement, String sourceTableName,String sourceColumns,String destinationSqlInsertUniqueIndexColumns,
			    			Database sourceDbAlias) {
			    		return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias)+((","+sourceColumns+",").contains(",ID,")?" WHERE ID>=0":"")+((","+sourceColumns+",").contains(",RDATE,")?" ORDER BY RDATE DESC":"");
			    	}
			    	
			    	@Override
			    	public boolean canTruncateTable(String sourceTableName) {
			    		return true;
			    	}
			    	
			    	@Override
			    	public boolean validSourceTable(String sourceTableName) {
			    		return super.validSourceTable(sourceTableName)&&sourceTableName.equals("PCO_OUTBOUNDLOG");
			    	}
			    	
			    	@Override
			    	public boolean validDestinationTable(String destinationTable) {
			    		// TODO Auto-generated method stub
			    		return super.validDestinationTable(destinationTable);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkStransfer(long startStamp, Database sourceDbAlias, Database destinationDbAlias,
			    			TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables) {
			    		
			    		System.out.println("START_TRANSFER");
			    		System.out.println(startStamp);
			    		
			    		super.startMonitoringBulkStransfer(startStamp, sourceDbAlias, destinationDbAlias, sourceTables, destinationTables);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkTableTransfer(long startStamp, long startTableStamp, String sourceTableName) {
			    		if(sourceTableName.equals("PCO_AGENT")){
			    			sourceTableName=sourceTableName+"";
			    		}
			    		
			    		logDebug("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		
			    		System.out.println("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		System.out.println(startTableStamp);
			    		super.startMonitoringBulkTableTransfer(startStamp, startTableStamp, sourceTableName);
			    	}
			    	
			    	@Override
			    	public void progressTransferTableMonitor(long startStamp, long startTableStamp, String sourceTableName,
			    			long totalSourceRecords, long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed) {
			    		if(totalDestinationRequestsCompleted%10000==0){
				    		logDebug("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
				    		System.out.println(startTableStamp);
				    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
				    		
//				    		System.out.println("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
//				    		System.out.println(startTableStamp);
//				    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//				    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//				    		System.out.println(Calendar.getInstance().getTimeInMillis());
			    		}
			    		super.progressTransferTableMonitor(startStamp, startTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed);
			    	}
			    	
			    	@Override
			    	public void endTransferTableMonitor(long startStamp, long startTableStamp, long endTableStamp,
			    			String sourceTableName, long totalSourceRecords, long totalDestinationRequestsCompleted,
			    			long totalDestinationRequestsFailed,Exception ex) {
			    		if(ex!=null){
			    			ex.printStackTrace();
			    		}
			    		logDebug("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
			    		
			    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
			    		logDebug("total-duration:"+(endTableStamp-startTableStamp));
//			    		System.out.println("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
//			    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//			    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//			    		System.out.println(startTableStamp);
//			    		System.out.println(endTableStamp);
//			    		System.out.println(endTableStamp-startTableStamp);
			    		
			    		super.endTransferTableMonitor(startStamp, startTableStamp, endTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed,ex);
			    	}
			    	
			    	@Override
			    	public void endMonitoringBulkTransfer(long startStamp, long endStamp, Database sourceDbAlias,
			    			Database destinationDbAlias, TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
			    		
			    		logDebug("END_TRANSFER:DONE");
			    		logDebug("total-duration:"+(endStamp-startStamp));
//			    		System.out.println("END_TRANSFER:DONE");
//			    		System.out.println(startStamp);
//			    		System.out.println(endStamp);
//			    		System.out.println(endStamp-startStamp);
//			    		
			    		if(failedTables!=null&&!failedTables.isEmpty()){
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    			while(!failedTables.isEmpty()){
			    				logDebug(failedTables.remove(0));
			    			}
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    		}
			    		
			    		super.endMonitoringBulkTransfer(startStamp, endStamp, sourceDbAlias, destinationDbAlias, sourceTables,
			    				destinationTables,failedTables);
			    	}
			    	
			    	@Override
			    	public int batchExecuteTableCommitSize(int commitSize, String sourceTableName) {
			    		return super.batchExecuteTableCommitSize(1000,sourceTableName);//sourceTableName.equals("PCO_OUTBOUNDQUEUE")?1:commitSize, sourceTableName);
			    	}
			    	
			    };
			    this.databaseBulkSourceDestinationTableTransferMonitor.requestTransfer(true);
			}
			
			@Override
			public String defaultServletContextName() {
				return "DBOracleToSqlExportImportPresenceDB";
			}
			
			@Override
			public String defaultLocalPath(String suggestedlocalpath) {
				System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
				return super.defaultLocalPath(suggestedlocalpath);
			}
		});
		
	}
	
	private static void exportPcoInboundLog() {
		executorService.submit((Callable<?>)new InovoCoreEnvironmentManager(){
			
			private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
			
			long startStamp=0;
			
			@Override
			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
				this.databaseBulkSourceDestinationTableTransferMonitor=new DatabaseBulkSourceDestinationTableTransferMonitor(Database.dballias("ORACLEPRES"),Database.dballias("MSSQLPRES")){
			    	@Override
			    	public String finalSourceTableSqlStatement(String sourceSqlStatement, String sourceTableName,String sourceColumns,String destinationSqlInsertUniqueIndexColumns,
			    			Database sourceDbAlias) {
			    		return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias)+((","+sourceColumns+",").contains(",ID,")?" WHERE ID>=0":"")+((","+sourceColumns+",").contains(",RDATE,")?" ORDER BY RDATE DESC":"");
			    	}
			    	
			    	@Override
			    	public boolean validSourceTable(String sourceTableName) {
			    		return super.validSourceTable(sourceTableName)&&sourceTableName.equals("PCO_INBOUNDLOG");
			    	}
			    	
			    	@Override
			    	public boolean canTruncateTable(String sourceTableName) {
			    		return true;
			    	}
			    	
			    	@Override
			    	public boolean validDestinationTable(String destinationTable) {
			    		// TODO Auto-generated method stub
			    		return super.validDestinationTable(destinationTable);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkStransfer(long startStamp, Database sourceDbAlias, Database destinationDbAlias,
			    			TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables) {
			    		
			    		System.out.println("START_TRANSFER");
			    		System.out.println(startStamp);
			    		
			    		super.startMonitoringBulkStransfer(startStamp, sourceDbAlias, destinationDbAlias, sourceTables, destinationTables);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkTableTransfer(long startStamp, long startTableStamp, String sourceTableName) {
			    		if(sourceTableName.equals("PCO_AGENT")){
			    			sourceTableName=sourceTableName+"";
			    		}
			    		
			    		logDebug("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		
			    		System.out.println("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		System.out.println(startTableStamp);
			    		super.startMonitoringBulkTableTransfer(startStamp, startTableStamp, sourceTableName);
			    	}
			    	
			    	@Override
			    	public void progressTransferTableMonitor(long startStamp, long startTableStamp, String sourceTableName,
			    			long totalSourceRecords, long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed) {
			    		if(totalDestinationRequestsCompleted%10000==0){
				    		logDebug("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
				    		System.out.println(startTableStamp);
				    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
				    		
//				    		System.out.println("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
//				    		System.out.println(startTableStamp);
//				    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//				    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//				    		System.out.println(Calendar.getInstance().getTimeInMillis());
			    		}
			    		super.progressTransferTableMonitor(startStamp, startTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed);
			    	}
			    	
			    	@Override
			    	public void endTransferTableMonitor(long startStamp, long startTableStamp, long endTableStamp,
			    			String sourceTableName, long totalSourceRecords, long totalDestinationRequestsCompleted,
			    			long totalDestinationRequestsFailed,Exception ex) {
			    		if(ex!=null){
			    			ex.printStackTrace();
			    		}
			    		logDebug("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
			    		
			    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
			    		logDebug("total-duration:"+(endTableStamp-startTableStamp));
//			    		System.out.println("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
//			    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//			    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//			    		System.out.println(startTableStamp);
//			    		System.out.println(endTableStamp);
//			    		System.out.println(endTableStamp-startTableStamp);
			    		
			    		super.endTransferTableMonitor(startStamp, startTableStamp, endTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed,ex);
			    	}
			    	
			    	@Override
			    	public void endMonitoringBulkTransfer(long startStamp, long endStamp, Database sourceDbAlias,
			    			Database destinationDbAlias, TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
			    		
			    		logDebug("END_TRANSFER:DONE");
			    		logDebug("total-duration:"+(endStamp-startStamp));
//			    		System.out.println("END_TRANSFER:DONE");
//			    		System.out.println(startStamp);
//			    		System.out.println(endStamp);
//			    		System.out.println(endStamp-startStamp);
//			    		
			    		if(failedTables!=null&&!failedTables.isEmpty()){
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    			while(!failedTables.isEmpty()){
			    				logDebug(failedTables.remove(0));
			    			}
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    		}
			    		
			    		super.endMonitoringBulkTransfer(startStamp, endStamp, sourceDbAlias, destinationDbAlias, sourceTables,
			    				destinationTables,failedTables);
			    	}
			    	
			    	@Override
			    	public int batchExecuteTableCommitSize(int commitSize, String sourceTableName) {
			    		return super.batchExecuteTableCommitSize(1000,sourceTableName);//sourceTableName.equals("PCO_OUTBOUNDQUEUE")?1:commitSize, sourceTableName);
			    	}
			    	
			    };
			    this.databaseBulkSourceDestinationTableTransferMonitor.requestTransfer(true);
			}
			
			@Override
			public String defaultServletContextName() {
				return "DBOracleToSqlExportImportPresenceDB";
			}
			
			@Override
			public String defaultLocalPath(String suggestedlocalpath) {
				System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
				return super.defaultLocalPath(suggestedlocalpath);
			}
		});
		
	}
	
	private static void exportPcoLog() {
		executorService.submit((Callable<?>)new InovoCoreEnvironmentManager(){
			
			private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
			
			long startStamp=0;
			
			@Override
			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
				this.databaseBulkSourceDestinationTableTransferMonitor=new DatabaseBulkSourceDestinationTableTransferMonitor(Database.dballias("ORACLEPRES"),Database.dballias("MSSQLPRES")){
			    	@Override
			    	public String finalSourceTableSqlStatement(String sourceSqlStatement, String sourceTableName,String sourceColumns,String destinationSqlInsertUniqueIndexColumns,
			    			Database sourceDbAlias) {
			    		return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias)+((","+sourceColumns+",").contains(",RDATE,")?" ORDER BY RDATE DESC":((","+sourceColumns+",").contains(",ID,")?" ORDER BY ID DESC":""));
			    	}
			    	
			    	@Override
			    	public boolean validSourceTable(String sourceTableName) {
			    		if(super.validSourceTable(sourceTableName)&&sourceTableName.startsWith("PCO_")&&sourceTableName.endsWith("LOG")){
			    			return !sourceTableName.equals("PCO_INBOUNDLOG")&&!sourceTableName.equals("PCO_OUTBOUNDLOG")&&!sourceTableName.equals("PCO_CHANGELOG")&&!sourceTableName.equals("PCO_SERVICEWORKLOG")&&!sourceTableName.equals("PCO_SOFTPHONECALLLOG")&&!sourceTableName.equals("PCO_WORKLOG");
			    		}
			    		return false;
			    	}
			    	
			    	@Override
			    	public boolean validDestinationTable(String destinationTable) {
			    		// TODO Auto-generated method stub
			    		return super.validDestinationTable(destinationTable);
			    	}
			    	
			    	@Override
			    	public boolean canTruncateTable(String sourceTableName) {
			    		return true;
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkStransfer(long startStamp, Database sourceDbAlias, Database destinationDbAlias,
			    			TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables) {
			    		
			    		System.out.println("START_TRANSFER");
			    		System.out.println(startStamp);
			    		
			    		super.startMonitoringBulkStransfer(startStamp, sourceDbAlias, destinationDbAlias, sourceTables, destinationTables);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkTableTransfer(long startStamp, long startTableStamp, String sourceTableName) {
			    		if(sourceTableName.equals("PCO_AGENT")){
			    			sourceTableName=sourceTableName+"";
			    		}
			    		
			    		logDebug("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		
			    		System.out.println("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		System.out.println(startTableStamp);
			    		super.startMonitoringBulkTableTransfer(startStamp, startTableStamp, sourceTableName);
			    	}
			    	
			    	@Override
			    	public void progressTransferTableMonitor(long startStamp, long startTableStamp, String sourceTableName,
			    			long totalSourceRecords, long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed) {
			    		if(totalDestinationRequestsCompleted%10000==0){
				    		logDebug("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
				    		System.out.println(startTableStamp);
				    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
				    		
//				    		System.out.println("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
//				    		System.out.println(startTableStamp);
//				    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//				    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//				    		System.out.println(Calendar.getInstance().getTimeInMillis());
			    		}
			    		super.progressTransferTableMonitor(startStamp, startTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed);
			    	}
			    	
			    	@Override
			    	public void endTransferTableMonitor(long startStamp, long startTableStamp, long endTableStamp,
			    			String sourceTableName, long totalSourceRecords, long totalDestinationRequestsCompleted,
			    			long totalDestinationRequestsFailed,Exception ex) {
			    		if(ex!=null){
			    			ex.printStackTrace();
			    		}
			    		logDebug("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
			    		
			    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
			    		logDebug("total-duration:"+(endTableStamp-startTableStamp));
//			    		System.out.println("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
//			    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//			    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//			    		System.out.println(startTableStamp);
//			    		System.out.println(endTableStamp);
//			    		System.out.println(endTableStamp-startTableStamp);
			    		
			    		super.endTransferTableMonitor(startStamp, startTableStamp, endTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed,ex);
			    	}
			    	
			    	@Override
			    	public void endMonitoringBulkTransfer(long startStamp, long endStamp, Database sourceDbAlias,
			    			Database destinationDbAlias, TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
			    		
			    		logDebug("END_TRANSFER:DONE");
			    		logDebug("total-duration:"+(endStamp-startStamp));
//			    		System.out.println("END_TRANSFER:DONE");
//			    		System.out.println(startStamp);
//			    		System.out.println(endStamp);
//			    		System.out.println(endStamp-startStamp);
//			    		
			    		if(failedTables!=null&&!failedTables.isEmpty()){
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    			while(!failedTables.isEmpty()){
			    				logDebug(failedTables.remove(0));
			    			}
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    		}
			    		
			    		super.endMonitoringBulkTransfer(startStamp, endStamp, sourceDbAlias, destinationDbAlias, sourceTables,
			    				destinationTables,failedTables);
			    	}
			    	
			    	@Override
			    	public int batchExecuteTableCommitSize(int commitSize, String sourceTableName) {
			    		return super.batchExecuteTableCommitSize(1000,sourceTableName);//sourceTableName.equals("PCO_OUTBOUNDQUEUE")?1:commitSize, sourceTableName);
			    	}
			    	
			    };
			    this.databaseBulkSourceDestinationTableTransferMonitor.requestTransfer(true);
			}
			
			@Override
			public String defaultServletContextName() {
				return "DBOracleToSqlExportImportPresenceDB";
			}
			
			@Override
			public String defaultLocalPath(String suggestedlocalpath) {
				System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
				return super.defaultLocalPath(suggestedlocalpath);
			}
		});
		
	}
	
	private static void exportPcoChangeLog() {
		executorService.submit((Callable<?>)new InovoCoreEnvironmentManager(){
			
			private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
			
			long startStamp=0;
			
			@Override
			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
				this.databaseBulkSourceDestinationTableTransferMonitor=new DatabaseBulkSourceDestinationTableTransferMonitor(Database.dballias("ORACLEPRES"),Database.dballias("MSSQLPRES")){
			    	@Override
			    	public String finalSourceTableSqlStatement(String sourceSqlStatement, String sourceTableName,String sourceColumns,String destinationSqlInsertUniqueIndexColumns,
			    			Database sourceDbAlias) {
			    		return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias)+((","+sourceColumns+",").contains(",ID,")?" WHERE ID>=0":"")+((","+sourceColumns+",").contains(",RDATE,")?" ORDER BY RDATE DESC":"");
			    	}
			    	
			    	@Override
			    	public boolean canTruncateTable(String sourceTableName) {
			    		return true;
			    	}
			    	
			    	@Override
			    	public boolean validSourceTable(String sourceTableName) {
			    		return super.validSourceTable(sourceTableName)&&sourceTableName.equals("PCO_CHANGELOG");
			    	}
			    	
			    	@Override
			    	public boolean validDestinationTable(String destinationTable) {
			    		// TODO Auto-generated method stub
			    		return super.validDestinationTable(destinationTable);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkStransfer(long startStamp, Database sourceDbAlias, Database destinationDbAlias,
			    			TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables) {
			    		
			    		System.out.println("START_TRANSFER");
			    		System.out.println(startStamp);
			    		
			    		super.startMonitoringBulkStransfer(startStamp, sourceDbAlias, destinationDbAlias, sourceTables, destinationTables);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkTableTransfer(long startStamp, long startTableStamp, String sourceTableName) {
			    		if(sourceTableName.equals("PCO_AGENT")){
			    			sourceTableName=sourceTableName+"";
			    		}
			    		
			    		logDebug("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		
			    		System.out.println("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		System.out.println(startTableStamp);
			    		super.startMonitoringBulkTableTransfer(startStamp, startTableStamp, sourceTableName);
			    	}
			    	
			    	@Override
			    	public void progressTransferTableMonitor(long startStamp, long startTableStamp, String sourceTableName,
			    			long totalSourceRecords, long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed) {
			    		if(totalDestinationRequestsCompleted%10000==0){
				    		logDebug("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
				    		System.out.println(startTableStamp);
				    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
				    		
			    		}
			    		super.progressTransferTableMonitor(startStamp, startTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed);
			    	}
			    	
			    	@Override
			    	public void endTransferTableMonitor(long startStamp, long startTableStamp, long endTableStamp,
			    			String sourceTableName, long totalSourceRecords, long totalDestinationRequestsCompleted,
			    			long totalDestinationRequestsFailed,Exception ex) {
			    		if(ex!=null){
			    			ex.printStackTrace();
			    		}
			    		logDebug("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
			    		
			    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
			    		logDebug("total-duration:"+(endTableStamp-startTableStamp));
			    		
			    		super.endTransferTableMonitor(startStamp, startTableStamp, endTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed,ex);
			    	}
			    	
			    	@Override
			    	public void endMonitoringBulkTransfer(long startStamp, long endStamp, Database sourceDbAlias,
			    			Database destinationDbAlias, TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
			    		
			    		logDebug("END_TRANSFER:DONE");
			    		logDebug("total-duration:"+(endStamp-startStamp));
			    		
			    		if(failedTables!=null&&!failedTables.isEmpty()){
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    			while(!failedTables.isEmpty()){
			    				logDebug(failedTables.remove(0));
			    			}
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    		}
			    		
			    		super.endMonitoringBulkTransfer(startStamp, endStamp, sourceDbAlias, destinationDbAlias, sourceTables,
			    				destinationTables,failedTables);
			    	}
			    	
			    	@Override
			    	public int batchExecuteTableCommitSize(int commitSize, String sourceTableName) {
			    		return super.batchExecuteTableCommitSize(1000,sourceTableName);//sourceTableName.equals("PCO_OUTBOUNDQUEUE")?1:commitSize, sourceTableName);
			    	}
			    	
			    };
			    this.databaseBulkSourceDestinationTableTransferMonitor.requestTransfer(true);
			}
			
			@Override
			public String defaultServletContextName() {
				return "DBOracleToSqlExportImportPresenceDB";
			}
			
			@Override
			public String defaultLocalPath(String suggestedlocalpath) {
				System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
				return super.defaultLocalPath(suggestedlocalpath);
			}
		});
	}
	
	private static void exportPcoWorklog(){
		executorService.submit((Callable<?>)new InovoCoreEnvironmentManager(){
				
				private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
				
				long startStamp=0;
				
				@Override
				public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
					this.databaseBulkSourceDestinationTableTransferMonitor=new DatabaseBulkSourceDestinationTableTransferMonitor(Database.dballias("ORACLEPRES"),Database.dballias("MSSQLPRES")){
				    	@Override
				    	public String finalSourceTableSqlStatement(String sourceSqlStatement, String sourceTableName,String sourceColumns,String destinationSqlInsertUniqueIndexColumns,
				    			Database sourceDbAlias) {
				    		return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias)+((","+sourceColumns+",").contains(",ID,")?" ORDER BY ID DESC":"");
				    	}
				    	
				    	@Override
				    	public boolean validSourceTable(String sourceTableName) {
				    		return super.validSourceTable(sourceTableName)&&sourceTableName.equals("PCO_WORKLOG");
				    	}
				    	
				    	@Override
				    	public boolean canTruncateTable(String sourceTableName) {
				    		return true;
				    	}
				    	
				    	@Override
				    	public boolean validDestinationTable(String destinationTable) {
				    		// TODO Auto-generated method stub
				    		return super.validDestinationTable(destinationTable);
				    	}
				    	
				    	@Override
				    	public void startMonitoringBulkStransfer(long startStamp, Database sourceDbAlias, Database destinationDbAlias,
				    			TreeMap<String, List<List<String>>> sourceTables,
				    			TreeMap<String, List<List<String>>> destinationTables) {
				    		
				    		System.out.println("START_TRANSFER");
				    		System.out.println(startStamp);
				    		
				    		super.startMonitoringBulkStransfer(startStamp, sourceDbAlias, destinationDbAlias, sourceTables, destinationTables);
				    	}
				    	
				    	@Override
				    	public void startMonitoringBulkTableTransfer(long startStamp, long startTableStamp, String sourceTableName) {
				    		if(sourceTableName.equals("PCO_AGENT")){
				    			sourceTableName=sourceTableName+"";
				    		}
				    		
				    		logDebug("START_TRANSFER_TABLE["+sourceTableName+"]");
				    		
				    		System.out.println("START_TRANSFER_TABLE["+sourceTableName+"]");
				    		System.out.println(startTableStamp);
				    		super.startMonitoringBulkTableTransfer(startStamp, startTableStamp, sourceTableName);
				    	}
				    	
				    	@Override
				    	public void progressTransferTableMonitor(long startStamp, long startTableStamp, String sourceTableName,
				    			long totalSourceRecords, long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed) {
				    		if(totalDestinationRequestsCompleted%10000==0){
					    		logDebug("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
					    		System.out.println(startTableStamp);
					    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
					    		
//					    		System.out.println("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
//					    		System.out.println(startTableStamp);
//					    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//					    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//					    		System.out.println(Calendar.getInstance().getTimeInMillis());
				    		}
				    		super.progressTransferTableMonitor(startStamp, startTableStamp, sourceTableName, totalSourceRecords,
				    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed);
				    	}
				    	
				    	@Override
				    	public void endTransferTableMonitor(long startStamp, long startTableStamp, long endTableStamp,
				    			String sourceTableName, long totalSourceRecords, long totalDestinationRequestsCompleted,
				    			long totalDestinationRequestsFailed,Exception ex) {
				    		if(ex!=null){
				    			ex.printStackTrace();
				    		}
				    		logDebug("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
				    		
				    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
				    		logDebug("total-duration:"+(endTableStamp-startTableStamp));
//				    		System.out.println("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
//				    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//				    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//				    		System.out.println(startTableStamp);
//				    		System.out.println(endTableStamp);
//				    		System.out.println(endTableStamp-startTableStamp);
				    		
				    		super.endTransferTableMonitor(startStamp, startTableStamp, endTableStamp, sourceTableName, totalSourceRecords,
				    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed,ex);
				    	}
				    	
				    	@Override
				    	public void endMonitoringBulkTransfer(long startStamp, long endStamp, Database sourceDbAlias,
				    			Database destinationDbAlias, TreeMap<String, List<List<String>>> sourceTables,
				    			TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
				    		
				    		logDebug("END_TRANSFER:DONE");
				    		logDebug("total-duration:"+(endStamp-startStamp));
//				    		System.out.println("END_TRANSFER:DONE");
//				    		System.out.println(startStamp);
//				    		System.out.println(endStamp);
//				    		System.out.println(endStamp-startStamp);
//				    		
				    		if(failedTables!=null&&!failedTables.isEmpty()){
				    			logDebug("[FAILED_TRANSFER_TABLES]");
				    			while(!failedTables.isEmpty()){
				    				logDebug(failedTables.remove(0));
				    			}
				    			logDebug("[FAILED_TRANSFER_TABLES]");
				    		}
				    		
				    		super.endMonitoringBulkTransfer(startStamp, endStamp, sourceDbAlias, destinationDbAlias, sourceTables,
				    				destinationTables,failedTables);
				    	}
				    	
				    	@Override
				    	public int batchExecuteTableCommitSize(int commitSize, String sourceTableName) {
				    		return super.batchExecuteTableCommitSize(1000,sourceTableName);//sourceTableName.equals("PCO_OUTBOUNDQUEUE")?1:commitSize, sourceTableName);
				    	}
				    	
				    };
				    this.databaseBulkSourceDestinationTableTransferMonitor.requestTransfer(true);
				}
				
				@Override
				public String defaultServletContextName() {
					return "DBOracleToSqlExportImportPresenceDB";
				}
				
				@Override
				public String defaultLocalPath(String suggestedlocalpath) {
					System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
					return super.defaultLocalPath(suggestedlocalpath);
				}
		});
	}

	private static void exportPcoCoreTables() {
		//MAIN LIST OF PCO_ TABLES
		executorService.submit((Callable<?>)new InovoCoreEnvironmentManager(){
			
			private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
			
			long startStamp=0;
			
			@Override
			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager)
					throws Exception {
				this.databaseBulkSourceDestinationTableTransferMonitor=new DatabaseBulkSourceDestinationTableTransferMonitor(Database.dballias("ORACLEPRES"),Database.dballias("MSSQLPRES")){
			    	@Override
			    	public String finalSourceTableSqlStatement(String sourceSqlStatement, String sourceTableName,String sourceColumns,String destinationSqlInsertUniqueIndexColumns,
			    			Database sourceDbAlias) {
			    		return super.finalSourceTableSqlStatement(sourceSqlStatement, sourceTableName,sourceColumns,destinationSqlInsertUniqueIndexColumns, sourceDbAlias)+((","+sourceColumns+",").contains(",ID,")?" ORDER BY ID DESC":"");
			    	}
			    	
			    	@Override
			    	public boolean validSourceTable(String sourceTableName) {
			    		if(super.validSourceTable(sourceTableName)&&sourceTableName.startsWith("PCO_")){
			    			return !sourceTableName.endsWith("LOG")&&!sourceTableName.equals("PCO_OUTBOUNDQUEUE");
			    		}
			    		return false;
			    	}
			    	
			    	@Override
			    	public boolean canTruncateTable(String sourceTableName) {
			    		return true;
			    	}
			    	
			    	@Override
			    	public boolean validDestinationTable(String destinationTable) {
			    		// TODO Auto-generated method stub
			    		return super.validDestinationTable(destinationTable);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkStransfer(long startStamp, Database sourceDbAlias, Database destinationDbAlias,
			    			TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables) {
			    		
			    		System.out.println("START_TRANSFER");
			    		System.out.println(startStamp);
			    		
			    		super.startMonitoringBulkStransfer(startStamp, sourceDbAlias, destinationDbAlias, sourceTables, destinationTables);
			    	}
			    	
			    	@Override
			    	public void startMonitoringBulkTableTransfer(long startStamp, long startTableStamp, String sourceTableName) {
			    		logDebug("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		
			    		System.out.println("START_TRANSFER_TABLE["+sourceTableName+"]");
			    		System.out.println(startTableStamp);
			    		super.startMonitoringBulkTableTransfer(startStamp, startTableStamp, sourceTableName);
			    	}
			    	
			    	@Override
			    	public void progressTransferTableMonitor(long startStamp, long startTableStamp, String sourceTableName,
			    			long totalSourceRecords, long totalDestinationRequestsCompleted, long totalDestinationRequestsFailed) {
			    		if(totalDestinationRequestsCompleted%10000==0){
				    		logDebug("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
				    		System.out.println(startTableStamp);
				    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
				    		
//						    		System.out.println("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
//						    		System.out.println(startTableStamp);
//						    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//						    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//						    		System.out.println(Calendar.getInstance().getTimeInMillis());
			    		}
			    		super.progressTransferTableMonitor(startStamp, startTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed);
			    	}
			    	
			    	@Override
			    	public void endTransferTableMonitor(long startStamp, long startTableStamp, long endTableStamp,
			    			String sourceTableName, long totalSourceRecords, long totalDestinationRequestsCompleted,
			    			long totalDestinationRequestsFailed,Exception ex) {
			    		if(ex!=null){
			    			ex.printStackTrace();
			    		}
			    		logDebug("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
			    		
			    		logDebug("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed-"+totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
			    		logDebug("total-duration:"+(endTableStamp-startTableStamp));
//					    		System.out.println("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
//					    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//					    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//					    		System.out.println(startTableStamp);
//					    		System.out.println(endTableStamp);
//					    		System.out.println(endTableStamp-startTableStamp);
			    		
			    		super.endTransferTableMonitor(startStamp, startTableStamp, endTableStamp, sourceTableName, totalSourceRecords,
			    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed,ex);
			    	}
			    	
			    	@Override
			    	public void endMonitoringBulkTransfer(long startStamp, long endStamp, Database sourceDbAlias,
			    			Database destinationDbAlias, TreeMap<String, List<List<String>>> sourceTables,
			    			TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
			    		
			    		logDebug("END_TRANSFER:DONE");
			    		logDebug("total-duration:"+(endStamp-startStamp));
//					    		System.out.println("END_TRANSFER:DONE");
//					    		System.out.println(startStamp);
//					    		System.out.println(endStamp);
//					    		System.out.println(endStamp-startStamp);
//					    		
			    		if(failedTables!=null&&!failedTables.isEmpty()){
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    			while(!failedTables.isEmpty()){
			    				logDebug(failedTables.remove(0));
			    			}
			    			logDebug("[FAILED_TRANSFER_TABLES]");
			    		}
			    		
			    		super.endMonitoringBulkTransfer(startStamp, endStamp, sourceDbAlias, destinationDbAlias, sourceTables,
			    				destinationTables,failedTables);
			    	}
			    	
			    	@Override
			    	public int batchExecuteTableCommitSize(int commitSize, String sourceTableName) {
			    		return super.batchExecuteTableCommitSize(1000,sourceTableName);//sourceTableName.equals("PCO_OUTBOUNDQUEUE")?1:commitSize, sourceTableName);
			    	}
			    	
			    };
			    this.databaseBulkSourceDestinationTableTransferMonitor.requestTransfer(true);
			}
			
			@Override
			public String defaultServletContextName() {
				return "DBOracleToSqlExportImportPresenceDB";
			}
			
			@Override
			public String defaultLocalPath(String suggestedlocalpath) {
				System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
				return super.defaultLocalPath(suggestedlocalpath);
			}
		});
	}

}
