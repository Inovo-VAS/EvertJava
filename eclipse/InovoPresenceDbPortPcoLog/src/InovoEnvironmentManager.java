import inovo.db.Database;
import inovo.db.DatabaseBulkExecuteMonitor;
import inovo.db.DatabaseBulkSourceDestinationTableTransferMonitor;
import inovo.servlet.InovoCoreEnvironmentManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.ServletContext;

public class InovoEnvironmentManager extends InovoCoreEnvironmentManager
{
	
	private DatabaseBulkSourceDestinationTableTransferMonitor databaseBulkSourceDestinationTableTransferMonitor=null;
	
	long startStamp=0;
	
  public void initializeServletContext(ServletContext sc)
  {
    super.initializeServletContext(sc);
    
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
	    		
//	    		System.out.println("CURRENT_TRANSFER_TABLE["+sourceTableName+"]:PROGRESS");
//	    		System.out.println(startTableStamp);
//	    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//	    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//	    		System.out.println(Calendar.getInstance().getTimeInMillis());
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
//    		System.out.println("END_TRANSFER_TABLE["+sourceTableName+"]:DONE");
//    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//    		System.out.println(startTableStamp);
//    		System.out.println(endTableStamp);
//    		System.out.println(endTableStamp-startTableStamp);
    		
    		super.endTransferTableMonitor(startStamp, startTableStamp, endTableStamp, sourceTableName, totalSourceRecords,
    				totalDestinationRequestsCompleted, totalDestinationRequestsFailed,ex);
    	}
    	
    	@Override
    	public void endMonitoringBulkTransfer(long startStamp, long endStamp, Database sourceDbAlias,
    			Database destinationDbAlias, TreeMap<String, List<List<String>>> sourceTables,
    			TreeMap<String, List<List<String>>> destinationTables,List<String> failedTables) {
    		
    		logDebug("END_TRANSFER:DONE");
    		logDebug("total-duration:"+(endStamp-startStamp));
//    		System.out.println("END_TRANSFER:DONE");
//    		System.out.println(startStamp);
//    		System.out.println(endStamp);
//    		System.out.println(endStamp-startStamp);
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
    this.databaseBulkSourceDestinationTableTransferMonitor.requestTransfer();
//    try {
//    	long startStamp=Calendar.getInstance().getTimeInMillis();
//		Database.executeDBRequest(null, "MSSQLPRES","SELECT TOP 100 * FROM PREP.PMSG_INBOUNDMAIL ORDER BY ID", null, this, "testRow");
//		System.out.println((Calendar.getInstance().getTimeInMillis()-startStamp));
//		System.out.println("DONE");
//	} catch (Exception e) {
//		e.printStackTrace();
//	}

//    try {
//		Database.executeDBRequest(null, "MSSQLPRES", "TRUNCATE TABLE PREP.PMSG_INBOUNDMAIL",null,null);
//	} catch (Exception e) {
//	}
//    
//    Database.bulkExecuteSqlDBAliasToDBAlias(Database.dballias("ORACLEPRES"), "SELECT * FROM PREP.PMSG_INBOUNDMAIL WHERE ROWNUM<1000 ORDER BY ID", Database.dballias("MSSQLPRES"), "INSERT INTO PREP.PMSG_INBOUNDMAIL (ID,MAILDATA) SELECT ?,?", 10,new DatabaseBulkExecuteMonitor(){
//    	@Override
//    	public void startMonitoringBulkExecution() {
//    		startStamp=Calendar.getInstance().getTimeInMillis();
//    		System.out.println("START");
//    		System.out.println(startStamp);
//    	}
//    	
//    	@Override
//    	public void progressMonitor(long totalSourceRecords, long totalDestinationRequestsCompleted,
//    			long totalDestinationRequestsFailed) {
//    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//    		System.out.println(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//    		System.out.println("CURRENT");
//    		System.out.println(Calendar.getInstance().getTimeInMillis());
//    	}
//    	
//    	@Override
//    	public void endMonitoringBulkExecution(long totalSourceRecords, long totalDestinationRequestsCompleted,
//    			long totalDestinationRequestsFailed, Exception ex) {
//    		System.out.println("totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed");
//    		System.out.print(totalSourceRecords+","+ totalDestinationRequestsCompleted+","+totalDestinationRequestsFailed);
//    		
//    		System.out.println("DONE");
//    		System.out.println((Calendar.getInstance().getTimeInMillis()-startStamp));
//    	}
//    	
//    });
//	
//    HashMap<String,List<List<String>>> mstables=Database.dballias("MSSQLPRES").tables();
//    
//    System.out.println(mstables.toString());
//    
//    HashMap<String,List<List<String>>> oratables=Database.dballias("ORACLEPRES").tables();
//    
//    System.out.println(oratables.toString());
    
  }
    

  public String defaultLocalPath(String suggestedlocalpah)
  {
	  //suggestedlocalpah="D:/projects/clients/inovo/java/";
	  return super.defaultLocalPath(suggestedlocalpah);
  }
   
  ByteArrayOutputStream bout=new ByteArrayOutputStream();
   
  public void testRow(Integer rowIndex,ArrayList<Object> data,ArrayList<Object> columns){
	 if(data.get(1) instanceof java.sql.Blob){
		 bout.reset();
		try {
			inputStream(((java.sql.Blob) data.get(1)).getBinaryStream(), bout);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(bout.toString());
	 }
  }
  
  private void inputStream(InputStream input,OutputStream output) throws IOException{
	  int readInLen=0;
	  byte [] readIn=new byte[8192];
	  while((readInLen=input.read(readIn, 0, readIn.length))>0){
		  output.write(readIn,0,readInLen);
	  }
	  output.flush();
  }

  public void disposeServletContext(ServletContext sc)
  {
	super.disposeServletContext(sc);
  }
}
