package inovo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class DatabaseBulkExecuteMonitor{
	public boolean canExecute(){
		  return true;
	}
  
	public int columnIndex=0;
  
	public void executeNextSourceDataSetRecordOntoDestination(Database sourceDbAlias,Connection sourceCn,ResultSet sourceResultSet,List<String> sourceColumns,ResultSetMetaData sourceMetaResultSet,int sourceColumnCount,Database destinationDbAlias,Connection destinationCn,PreparedStatement destinationPrepStatement,List<Object> destinationParamValues,String[]adhoc) throws SQLException{
		columnIndex=0;
		Object colValue=null;
		while(columnIndex<sourceColumnCount){
			columnIndex++;
			colValue=sourceResultSet.getObject(columnIndex);
			destinationPrepStatement.setObject(columnIndex, colValue);
		}
		if(destinationParamValues!=null){
			while(!destinationParamValues.isEmpty()){
				columnIndex++;
				colValue=destinationParamValues.remove(0);
				destinationPrepStatement.setObject(columnIndex,colValue);
			}
		}
	}
	
	public void progressMonitor(long totalSourceRecords,long totalDestinationRequestsCompleted,long totalDestinationRequestsFailed,String[]adhoc){
	}
		
	public void startMonitoringBulkExecution() {
	}
	
	public void endMonitoringBulkExecution(long totalSourceRecords,long totalDestinationRequestsCompleted,long totalDestinationRequestsFailed,Exception ex,String[]adhoc) {
	}

	public void postBulkExecuteDestination(Connection destinationCn, PreparedStatement destinationPrepStatement,String[]adhoc) {
	}

	public void preBulkExecuteDestination(Connection destinationCn, PreparedStatement destinationPrepStatement,String[]adhoc) {
	}
}
