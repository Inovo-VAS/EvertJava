package inovo.db;

import inovo.adhoc.AdhocUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.CharBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

//import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Database 
{
 //private ComboPooledDataSource _cpds = new ComboPooledDataSource();
  private static IDatabase _idatabase=null;	
  private static LinkedHashMap<String, Database> _databases = new LinkedHashMap<String, Database>();
  private static LinkedHashMap<String, LinkedHashMap<String, String>> _databaseconfigs = new LinkedHashMap<String, LinkedHashMap<String, String>>();

  private ArrayList<Connection> _activeConnections=new ArrayList<Connection>();
  private ArrayList<Connection> _connectionsInUse=new ArrayList<Connection>();
  private int _activeConnectionsCount=0;
  private int _connectionsInUseCount=0;
  
  private Connection _masterCn=null;
  
  private int _maxDBConnections = 20;
  private String _dbname;
  private String _dbuser;
  private String _dbaltuser="";
  private String _dbpw;
  private String _dbhost;
  private String _dbinstance;
  private String _dbtype;
  private String _dbconstring = "";
  private String _dbtestQuery="";

  private static final Object _dbAlliasLock=new Object(); 
  
  public Database() throws Exception {
	this._maxDBConnections = defaultConnectionPoolSize();
  }

  public Database(String[] dbproperties) throws Exception
  {
    this._maxDBConnections = defaultConnectionPoolSize();
    setDBConnection(dbproperties);
  }

  public void reloadDbSettings(LinkedHashMap<String, String> dbproperties) throws Exception {
    String[] dbpropertiesarr = new String[dbproperties.size()];
    int dbpropertiesarrindex = 0;
    for (String dbpropname : dbproperties.keySet()) {
      dbpropertiesarr[dbpropertiesarrindex] = (dbpropname + "=" + (String)dbproperties.get(dbpropname));
      dbpropertiesarrindex++;
    }
    setDBConnection(dbpropertiesarr);
  }

  private void setDBConnection(String[] dbproperties) throws Exception {
    String dbtype = "";
    String dbhost = "";
    String dbinstance = "";
    String dbname = "";
    String dbuser = "";
    String dbpw = "";

    for (String dbprop : dbproperties) {
      if (dbprop.indexOf("=") > -1) {
        String dbpropName = dbprop.substring(0, dbprop.indexOf("=")).trim().toUpperCase();
        String dbpropValue = dbprop.substring(dbprop.indexOf("=") + 1).trim();
        if ((!dbpropValue.equals("")) || (!dbpropName.equals(""))) {
          if ((dbpropName.equals("DBTYPE")) && (!dbpropValue.equals(""))) {
            dbtype = dbpropValue;
          }
          else if ((dbpropName.equals("DBHOST")) && (!dbpropValue.equals(""))) {
            dbhost = dbpropValue;
          }
          else if ((dbpropName.equals("DBINSTANCE")) && (!dbpropValue.equals(""))) {
            dbinstance = dbpropValue;
          }
          else if ((dbpropName.equals("DBUSER")) && (!dbpropValue.equals(""))) {
            dbuser = dbpropValue;
          }
          else if ((dbpropName.equals("DBALTUSER")) && (!dbpropValue.equals(""))) {
              _dbaltuser = dbpropValue;
            }
          else if ((dbpropName.equals("DBNAME")) && (!dbpropValue.equals(""))) {
            dbname = dbpropValue;
          }
          else if ((dbpropName.equals("DBPW")) && (!dbpropValue.equals("")))
            dbpw = dbpropValue;
        }
      }
    }
    setupdbconnection(dbtype, dbhost, dbinstance, dbname, dbuser, dbpw);
  }

  public HashMap<String,String> dbsettings(){
	  HashMap<String,String> dbsettings=new HashMap<String,String>();
	  
	  dbsettings.put("DBTYPE", _dbtype);
	  dbsettings.put("DBHOST",_dbhost);
	  dbsettings.put("DBINSTANCE", _dbinstance);
	  dbsettings.put("DBUSER", _dbuser);
	  dbsettings.put("DBPW", _dbpw);
	  
	  return dbsettings;
  }
  
  private void setupdbconnection(String dbtype, String dbhost, String dbinstance, String dbname, String dbuser, String dbpw) throws Exception
  {
    synchronized (this) {
      this._dbtype = dbtype.toLowerCase();
      this._dbhost = dbhost;
      this._dbinstance = dbinstance;
      this._dbname = dbname;
      this._dbuser = dbuser;
      this._dbpw = dbpw;
      	
      // the settings below are optional -- c3p0 can work with defaults
      int minCon=this._maxDBConnections/10;
      if(minCon<=1) {
    	  if(minCon+1<this._maxDBConnections){
    		  
    	  }
    	  else{
    		  minCon=this._maxDBConnections;
    	  }
      }
      if(minCon<=0) minCon=1;
      int incCon=this._maxDBConnections/minCon;
      if(incCon<minCon) incCon=minCon;
      
      if ("sqlserver,postgresql,oracle,".contains(this._dbtype+",")) {
        this._dbconstring = ("jdbc:" + this._dbtype + "://" + this._dbhost);
        if (this._dbtype.equals("sqlserver")) {
          if (!this._dbinstance.equals("")) this._dbconstring = (this._dbconstring + ";instanceName=" + this._dbinstance);
          this._dbconstring = (this._dbconstring + ";databaseName=" + this._dbname + ";");
          this._dbtestQuery="SELECT 1";
          Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
          
        }
        else if (this._dbtype.equals("postgresql")) {
          this._dbconstring = (this._dbconstring + "/" + this._dbname);
          this._dbtestQuery="SELECT 1";
          Class.forName("org.postgresql.Driver");
          //_cpds.setDriverClass( "org.postgresql.Driver" ); //loads the jdbc driver
        }
        else if(this._dbtype.equals("oracle")){
        	this._dbconstring ="jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(HOST="+_dbhost.substring(0, _dbhost.indexOf(":")==-1?_dbhost.length():_dbhost.indexOf(":"))+")(PORT="+(_dbhost.indexOf(":")==-1?"1521":_dbhost.substring(_dbhost.indexOf(":")+1,_dbhost.length()))+")(PROTOCOL=tcp))(CONNECT_DATA=(SERVICE_NAME="+_dbinstance+")))";
            this._dbtestQuery="SELECT 1 from dual";
        	Class.forName("oracle.jdbc.driver.OracleDriver");
        }
      }
      else {
        this._dbconstring = "";
      }
    }
    
    //_cpds.setJdbcUrl( this._dbconstring);
    //_cpds.setUser(this._dbuser);                                  
    //_cpds.setPassword(this._dbpw);
    //_cpds.setPreferredTestQuery(_dbtestQuery);
    resizeConnectionPool();
  }

  public int maximumConnectionPoolSize() {
    return this._maxDBConnections;
  }

  public synchronized int totalPooledConnectionsInUse() {
    return 1;//return this._dbconnectionsInUse.size();
  }

  public synchronized int totalPooledConnectionsAvailable() {
    return 1;//return this._dbconnectionsInUse.size();
  }

  private void resizeConnectionPool() throws Exception {
    /*synchronized (this) {
      int totalConnectionsToAdd = this._dbconnections.size() + this._dbconnectionsInUse.size();
      while (totalConnectionsToAdd++ < this._maxDBConnections)
        this._dbconnections.add(newSQLConnection());
    }*/
  }

  public void ajustConnectionPoolSize(int newMaxPoolSize) throws Exception
  {
    synchronized (this) {
      this._maxDBConnections = (newMaxPoolSize < defaultConnectionPoolSize() ? defaultConnectionPoolSize() : newMaxPoolSize);
      resizeConnectionPool();
    }
  }

  public int defaultConnectionPoolSize() {
    return 5;
  }
  
  private void executeDBRequest(TreeMap<Integer, ArrayList<Object>> resultSetToReturn, String sqlStatement, HashMap<String, Object> sqlparams,Object rowOutputMethodOwner) throws Exception {
	  this.executeDBRequest(resultSetToReturn, sqlStatement, sqlparams, rowOutputMethodOwner, null);
  }

  public void executeDBRequest(TreeMap<Integer, ArrayList<Object>> resultSetToReturn, String sqlStatement, HashMap<String, Object> sqlparams,Object rowOutputMethodOwner,String rowOuputMethodName) throws Exception {
    ArrayList<Character> sqlStatementarr = new ArrayList<Character>();
    boolean createdDbResultLocal=(resultSetToReturn==null);
    
    rowOuputMethodName=(rowOuputMethodName==null?"readRowData":(rowOuputMethodName=rowOuputMethodName.trim()).equals("")?"readRowData":rowOuputMethodName);
    
    for (char csql : sqlStatement.toCharArray()) {
      sqlStatementarr.add(Character.valueOf(csql));
    }

    if (!sqlStatementarr.isEmpty()) { 
    	if (sqlparams == null) sqlparams = new HashMap<String,Object>();
    }
    ArrayList<Object> sqlParamFound = new ArrayList<Object>();

    ArrayList<InputStream> sqlParamInputStreams=new ArrayList<InputStream>();
    
    String finalSqlStatement = "";
    String testParamString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdexfgijklmnopqrstuvwxyz0123456789_";
    Iterator<Character> sqlstmnti = sqlStatementarr.iterator();
    while (sqlstmnti.hasNext()) {
      char csql = sqlstmnti.next().charValue();
      sqlstmnti.remove();
      if (csql == '\'') {
        finalSqlStatement = finalSqlStatement + csql;
        char lastcsql = 0;
        while (sqlstmnti.hasNext()) {
          csql = ((Character)sqlstmnti.next()).charValue();
          sqlstmnti.remove();
          if ((csql == lastcsql) && (csql == '\'')) {
            finalSqlStatement = finalSqlStatement + csql;
            lastcsql = '\'';
          }
          else {
            if (csql == '\'') {
              finalSqlStatement = finalSqlStatement + csql;
              break;
            }

            finalSqlStatement = finalSqlStatement + csql;

            lastcsql = csql;
          }
        }
      } else if (csql == ':') {
        String possibleParamName = "";
        while (sqlstmnti.hasNext()) {
          csql = ((Character)sqlstmnti.next()).charValue();
          sqlstmnti.remove();
          if (testParamString.indexOf(csql) <= -1) break;
          possibleParamName = possibleParamName + csql;
        }

        if (sqlparams.containsKey(possibleParamName.toUpperCase())) {
          finalSqlStatement = finalSqlStatement + "?";
          //sqlParamFound.add((String)sqlparams.get(possibleParamName.toUpperCase()));
          sqlParamFound.add(sqlparams.get(possibleParamName.toUpperCase()));
        }
        else {
          finalSqlStatement = finalSqlStatement + ":" + possibleParamName;
        }
        if (!testParamString.contains((csql+""))) {
          finalSqlStatement = finalSqlStatement + csql;
        }
      }
      else if (csql == '<') {
        String possibleFlagName = "";
        boolean endOfsttmnt = false;
        while (sqlstmnti.hasNext()) {
          csql = ((Character)sqlstmnti.next()).charValue();
          sqlstmnti.remove();
          if ("[]() ".indexOf(csql) == -1) {
            if (csql == '>') break;
            possibleFlagName = possibleFlagName + csql;
          }
          else
          {
            endOfsttmnt = true;
            break;
          }
        }
        if (possibleFlagName.toLowerCase().trim().equals("dbuser")) {
          finalSqlStatement = finalSqlStatement + (this._dbaltuser.equals("")?this._dbuser:this._dbaltuser);
        }
        else if (possibleFlagName.toLowerCase().trim().equals("dbname")) {
          finalSqlStatement = finalSqlStatement + this._dbname;
        }
        else {
          finalSqlStatement = finalSqlStatement + "<" + possibleFlagName + (possibleFlagName.indexOf(csql) == -1 ? Character.valueOf(csql) : "");
        }
      }
      else if (csql == '|') {
        if (sqlstmnti.hasNext()) {
          csql = ((Character)sqlstmnti.next()).charValue();
          sqlstmnti.remove();
          if (csql == '|') {
            if (this._dbtype.equals("sqlserver")) {
              finalSqlStatement = finalSqlStatement + "+";
            }
            else
              finalSqlStatement = finalSqlStatement + "||";
          }
          else
          {
            finalSqlStatement = finalSqlStatement + "|";
            finalSqlStatement = finalSqlStatement + csql;
          }
        }
      }
      else if (csql == '[') {
        finalSqlStatement = finalSqlStatement + (this._dbtype.equals("postgresql") ? '"' : this._dbtype.equals("sqlserver") ? csql : csql);
      }
      else if (csql == ']') {
        finalSqlStatement = finalSqlStatement + (this._dbtype.equals("postgresql") ? '"' : this._dbtype.equals("sqlserver") ? csql : csql);
      }
      else {
        finalSqlStatement = finalSqlStatement + csql;
      }
    }
    Connection cn = nextSQLConnection();
    PreparedStatement sqlPrepared=null;
    try
    {
    
    	String now=(_dbtype.equals("sqlserver")?"GETDATE()":"NOW()");
    	
    	while(finalSqlStatement.indexOf("NOW()")>-1){
    		finalSqlStatement=finalSqlStatement.substring(0,finalSqlStatement.indexOf("NOW()"))+now+finalSqlStatement.substring(finalSqlStatement.indexOf("NOW()")+"NOW()".length());
    	}
    	
      sqlPrepared = cn.prepareStatement(finalSqlStatement);
      
      int sqlParamCount = 0;
      while (!sqlParamFound.isEmpty()) {
    	  Object nextParamVal=sqlParamFound.remove(0);
    	  //String paramVal=(String)sqlParamFound.remove(0);
    	  if (nextParamVal instanceof File){
    		  FileInputStream fi=new FileInputStream((File)nextParamVal);
    		  sqlPrepared.setBinaryStream(++sqlParamCount,fi,((File)nextParamVal).length());
    		  sqlParamInputStreams.add(fi);
    	  } else {
    		  sqlPrepared.setObject(++sqlParamCount, nextParamVal);
    	  }
    	  //sqlPrepared.setString(++sqlParamCount, paramVal);
      }

      ResultSet sqlsetreturned=null;
      if (sqlPrepared.execute()) {
    	
      }
      do{
	      if((sqlsetreturned = sqlPrepared.getResultSet())!=null){
	    	  if(resultSetToReturn==null){  
	  	    	resultSetToReturn = sqlStatement.toUpperCase().startsWith("SELECT ") ?(rowOutputMethodOwner==null?new TreeMap<Integer,ArrayList<Object>>():null) : null;
	  	    	resultSetToReturn = resultSetToReturn == null ?(rowOutputMethodOwner==null? new TreeMap<Integer,ArrayList<Object>>():null) : resultSetToReturn;
	      	}
	        ResultSetMetaData sqlmetasetreturned = sqlsetreturned.getMetaData();
	
	        long rowindex = 0;
	        boolean foundRecs=false;
	
	        int colCount = sqlmetasetreturned.getColumnCount();
	        int colindex = 0;
	        
	        ArrayList<String> currentColumns = new ArrayList<String>();
	        
	        foundRecs=sqlsetreturned.next();
	        
	        while(colindex<colCount)
	        {
	        	currentColumns.add(sqlmetasetreturned.getColumnName((colindex+=1)));
	        } 
	
	        Method rowOutputMethod=(rowOutputMethodOwner==null?null:inovo.adhoc.AdhocUtils.findMethod(((rowOutputMethodOwner instanceof Class<?>)?(Class<?>)rowOutputMethodOwner:rowOutputMethodOwner.getClass()).getMethods(), rowOuputMethodName, false));
	        if(resultSetToReturn!=null){
	        	resultSetToReturn.put(Long.valueOf(rowindex++).intValue(),new ArrayList<Object>(currentColumns));
	        }
	        else{
	        	if(rowOutputMethodOwner!=null&&rowOutputMethod!=null){
	          	  rowOutputMethod.invoke(rowOutputMethodOwner, new Object[]{!foundRecs,Long.valueOf(rowindex++),currentColumns,currentColumns});
	            }
	        }
	        
	        while (foundRecs) {
	        	
	          colindex = 0;
	          ArrayList<Object> currentColumnsData = new ArrayList<Object>();
	          while(colindex<colCount) {
	            Object colValue = sqlsetreturned.getObject((colindex+=1));
	            colValue = (colValue==null ? "" :  colValue);
	            currentColumnsData.add(colValue);
	            if (sqlparams.keySet().contains(currentColumns.get(colindex - 1)))  sqlparams.put(currentColumns.get(colindex - 1), colValue);
	            
	          }
	          foundRecs=sqlsetreturned.next();
	          if(rowOutputMethodOwner!=null&&rowOutputMethod!=null){
	        	  rowOutputMethod.invoke(rowOutputMethodOwner, new Object[]{!foundRecs,Long.valueOf(rowindex++),currentColumnsData,currentColumns});
	        	  currentColumnsData.clear();
	        	  currentColumnsData=null;
	          }
	          if(resultSetToReturn!=null) resultSetToReturn.put(Long.valueOf(rowindex++).intValue(),new ArrayList<Object>(currentColumnsData));
	        }	        
	      }
      }
      while((sqlPrepared.getMoreResults()?(sqlsetreturned=sqlPrepared.getResultSet()):null)!=null||sqlPrepared.getUpdateCount()!=-1);
      sqlPrepared.close();
      if(createdDbResultLocal&&resultSetToReturn!=null){
    	  Database.cleanupDataset(resultSetToReturn);
    	  resultSetToReturn=null;
      }
      putConnectionBackInPool(cn);
    }
    catch (Exception e) {
    	if(_idatabase!=null) _idatabase.log_debug("[E]"+e.getMessage());
    	
    	if(_idatabase!=null) _idatabase.log_debug("[E:SQL]"+sqlStatement);
    	if(_idatabase!=null) _idatabase.log_debug("[E:SQL-FINAL]"+finalSqlStatement);
    	if(_idatabase!=null) _idatabase.log_debug("[E:SQL]"+sqlStatement);
    	if(sqlparams!=null){
    		for(String sqlParamName:sqlparams.keySet()){
    			if(_idatabase!=null) _idatabase.log_debug("[E:SQL-PARAM("+sqlParamName+")]"+sqlparams.get(sqlParamName));
    		}
    	}
    	
    	if(createdDbResultLocal&&resultSetToReturn!=null){
      	  Database.cleanupDataset(resultSetToReturn);
      	  resultSetToReturn=null;
        }
      putConnectionBackInPool(cn);
      
      sqlParamFound.clear();
      sqlParamFound=null;
      if(sqlPrepared!=null){
    	  sqlPrepared.close();
    	  sqlPrepared=null;
      }
      while (!sqlParamInputStreams.isEmpty()){
     	 InputStream inToClose=sqlParamInputStreams.remove(0);
     	 try{
     		 inToClose.close();
     	 } 
     	 catch(Exception ioe){
     	 }
     	 inToClose=null;
       }
      throw e;
    }
    sqlParamFound.clear();
    sqlParamFound=null;
    if(sqlPrepared!=null){
  	  sqlPrepared.close();
  	  sqlPrepared=null;
    }
    while (!sqlParamInputStreams.isEmpty()){
   	 InputStream inToClose=sqlParamInputStreams.remove(0);
   	 try{
   		 inToClose.close();
   	 } 
   	 catch(Exception ioe){
   	 }
   	 inToClose=null;
     }
  }
  
  	public static void bulkExecuteSqlDBAliasToDBAlias(Database sourceDbAlias,String sourceSqlStatement,Database destinationDbAlias,String destinationSqlStatement,int destinationCommitSize,DatabaseBulkExecuteMonitor bulkExecuteMonitor,List<String> destinationParamNames,String[]adhoc){
  		Connection sourceCn=null;
		try {
			sourceCn=sourceDbAlias.nextSQLConnection();
			System.out.println("sourceCn.nextSQLConnection()");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PreparedStatement sourcePrepStatement=null;
		
		try {
			sourcePrepStatement=sourceCn==null?null:sourceCn.prepareStatement(sourceSqlStatement);
			System.out.println("sourceCn.prepareStatement(sourceSqlStatement)");
			System.out.println(sourceSqlStatement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Connection destinationCn=null;
		try {
			destinationCn=destinationDbAlias.nextSQLConnection();
			System.out.println("destinationDbAlias.nextSQLConnection()");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PreparedStatement destinationPrepStatement=null;
		
		try {
			destinationPrepStatement=destinationCn==null?null:destinationCn.prepareStatement(destinationSqlStatement);
			System.out.println("destinationCn.prepareStatement(destinationSqlStatement)");
			System.out.println(destinationSqlStatement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(sourceCn!=null&&sourcePrepStatement!=null&&destinationCn!=null&&destinationPrepStatement!=null){
			(bulkExecuteMonitor==null?(bulkExecuteMonitor=new DatabaseBulkExecuteMonitor()):bulkExecuteMonitor).startMonitoringBulkExecution();
			long totalSourceRecords=0;
			long totalDestinationRequestsCompleted=0;
			long totalDestinationRequestsFailed=0;
			
			ResultSet sourceResultSet=null;
			try {
				if(sourcePrepStatement.execute()){
					System.out.println("sourcePrepStatement.execute()");
					
					sourceResultSet=sourcePrepStatement.getResultSet();
					
					ResultSetMetaData sourceResultMetaSet = sourceResultSet.getMetaData();
					
					int columnCount=sourceResultMetaSet.getColumnCount();
					
					List<String> sourceColumns=new ArrayList<String>();
					
					int columnIndex=0;
					int execBatchTotal=0;
					int destinationCommitCount=0;
					int[] executeBatch=null;
					
					while(columnIndex<columnCount){
						columnIndex++;
						
						sourceColumns.add(sourceResultMetaSet.getColumnLabel(columnIndex));
						if(columnIndex==columnCount){
							columnIndex=0;
							break;
						}
					}
					
					bulkExecuteMonitor.preBulkExecuteDestination(destinationCn,destinationPrepStatement,adhoc);
					System.out.println("bulkExecuteMonitor.preBulkExecuteDestination(destinationCn,destinationPrepStatement,adhoc)");
					
					List<Object> destinationParamValues=new ArrayList<Object>();
					boolean hasNext=false;
					try {
						Thread.currentThread().sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					while(hasNext=sourceResultSet.next()){
						//System.out.println("CAN EXECUTE 1.1:PPREP["+destinationPrepStatement.toString()+"]");
						while(!destinationParamValues.isEmpty()){
							Object destParVal=destinationParamValues.remove(0);
							destParVal=null;
						}
						if(destinationParamNames!=null){
							destinationParamValues.clear();
							for(String destParamName:destinationParamNames){
								if(sourceColumns.contains(destParamName)){
									destinationParamValues.add(sourceResultSet.getObject(sourceColumns.indexOf(destParamName)+1));
								}
							}
						}
						bulkExecuteMonitor.executeNextSourceDataSetRecordOntoDestination(sourceDbAlias, sourceCn, sourceResultSet,sourceColumns, sourceResultMetaSet, columnCount, destinationDbAlias, destinationCn, destinationPrepStatement,destinationParamValues,adhoc);
						
						destinationPrepStatement.addBatch();
						destinationCommitCount++;
						if(destinationCommitCount==destinationCommitSize){
							execBatchTotal=0;
							if(destinationCommitCount==1){
								if(destinationPrepStatement.execute()){
									execBatchTotal+=destinationCommitCount;
								}
							}
							else{
								executeBatch=destinationPrepStatement.executeBatch();
								for(int execBatch:executeBatch){
									execBatchTotal+=execBatch;
								}
							}

							totalDestinationRequestsCompleted+=execBatchTotal;
							
							totalSourceRecords+=destinationCommitCount;
							
							totalDestinationRequestsFailed+=(destinationCommitCount-execBatchTotal);
							bulkExecuteMonitor.progressMonitor(totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed,adhoc);
							
							destinationCommitCount=0;							
							destinationCn.commit();
						}
					}
					if(destinationCommitCount>0){
						executeBatch=destinationPrepStatement.executeBatch();
						execBatchTotal=0;
						for(int execBatch:executeBatch){
							execBatchTotal+=execBatch;
						}
						totalDestinationRequestsCompleted+=execBatchTotal;
						
						totalSourceRecords+=destinationCommitCount;
						
						totalDestinationRequestsFailed+=(destinationCommitCount-execBatchTotal);
						bulkExecuteMonitor.endMonitoringBulkExecution(totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed,null,adhoc);
						
						destinationCommitCount=0;							
						destinationCn.commit();
					}
					else{
						bulkExecuteMonitor.endMonitoringBulkExecution(totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed,null,adhoc);
					}
					
					bulkExecuteMonitor.postBulkExecuteDestination(destinationCn,destinationPrepStatement,adhoc);
				}
				
			} catch (SQLException e) {
				bulkExecuteMonitor.postBulkExecuteDestination(destinationCn,destinationPrepStatement,adhoc);
				bulkExecuteMonitor.endMonitoringBulkExecution(totalSourceRecords, totalDestinationRequestsCompleted, totalDestinationRequestsFailed,e,adhoc);
			}
		}
  	}

  private boolean _pooled=true;
  
  private ArrayList<Connection> _pooledConnections=new ArrayList<Connection>();
  private int _pooledConnectionSize=10;
  private int _pooledConnectionIndex=0;
  
  private Connection newSQLConnection(Connection cnToDispose) throws Exception {
	  Connection newsqlConnection=null;
	  Exception newdbex=null;
	  
	  if(_pooled){
		  if(cnToDispose!=null){
			  if(_pooledConnections.contains(cnToDispose)){
				  int pooledConnectionsIndex=_pooledConnections.indexOf(cnToDispose);
				  try{
					  cnToDispose.close();
				  }
				  catch(Exception e){
					  cnToDispose.close();
				  }
				  try{
					  _pooledConnections.set(pooledConnectionsIndex,cnToDispose=DriverManager.getConnection(this._dbconstring, this._dbuser, this._dbpw));
					  _pooledConnectionIndex=pooledConnectionsIndex;
				  }
				  catch(Exception ecn){
					  newdbex=ecn; 
				  }
			  }
		  }
		  try{
			  if(newdbex==null){
				  while(_pooledConnections.size()<_pooledConnectionSize){
					  _pooledConnections.add(DriverManager.getConnection(this._dbconstring, this._dbuser, this._dbpw));
				  }
			  }
		  }
		  catch(Exception sizePoolEx){
			  newdbex=new Exception("DB POOL RESIZING: "+sizePoolEx.getMessage());
		  }
		  if(newdbex==null){
			  if(_pooledConnectionIndex>=_pooledConnectionSize){
				  _pooledConnectionIndex=0;
			  }
			  newsqlConnection=_pooledConnections.get(_pooledConnectionIndex);
			  try{
				  if(newsqlConnection.isClosed()){
					  newsqlConnection=DriverManager.getConnection(this._dbconstring, this._dbuser, this._dbpw);
					  _pooledConnections.set(_pooledConnectionIndex, newsqlConnection);
				  }
				  _pooledConnectionIndex++;
			  }
			  catch(Exception getNextCnEx){
				  newdbex=new Exception("DB RETRIEVE CONNECTION: "+getNextCnEx.getMessage()); 
			  }
		  }
		  if(newdbex!=null){
			  throw newdbex;
		  }
		  return newsqlConnection;
	  }
	  else{
		  if(_masterCn==null){
			  _masterCn=DriverManager.getConnection(this._dbconstring, this._dbuser, this._dbpw);
		  }
		  if(_masterCn!=null){
			  return _masterCn;
		  }
		  else{
			  return _masterCn;
		  }
	  }
	  /*
	  int activeCnsDiv=(_maxDBConnections-_connectionsInUseCount)-_activeConnectionsCount;
	  
	  if(activeCnsDiv>0){
		  synchronized (_activeConnections) {
			  while(activeCnsDiv>0){
				  activeCnsDiv--;
				  try{
					  this._activeConnections.add(DriverManager.getConnection(this._dbconstring, this._dbuser, this._dbpw));
					  _activeConnectionsCount++;
				  }
				  catch(Exception ecna){
					  newdbex=ecna; 
				  }
			  }
		  }
		  if(newdbex!=null) throw newdbex;
	  }
	  
	  synchronized (_activeConnections) {
		  _newsqlConnection=this._activeConnections.remove(0);
		  this._activeConnectionsCount--;
	  }
	  
	  synchronized (_connectionsInUse) {
		  try{
			this._connectionsInUse.add(_newsqlConnection);
			this._connectionsInUseCount++;
		  }
		  catch(Exception ecna){
			  newdbex=ecna; 
		  }
	  }
	*/
	  
	/*if(_masterCn==null){
		
		return (_masterCn=_cpds.getConnection());//DriverManager.getConnection(this._dbconstring, this._dbuser, this._dbpw));
	}
	else{
		return _masterCn;
	}*/
	  //if(newdbex!=null) throw newdbex;
	  
	  //return _newsqlConnection;
  }

  public Connection nextSQLConnection() throws Exception {
    Connection cn = this.newSQLConnection(null);// null;
    try{
    	cn.prepareCall(_dbtestQuery).execute();
    }
    catch(Exception ex){
    	try{
    		cn=newSQLConnection(ex.getMessage().contains("Connection reset by peer")?cn:null);
    	}
    	catch(Exception e){
    		throw ex;
    	}
    }
    return cn;
  }

  public void putConnectionBackInPool(Connection cn) throws Exception {
	//cn.close();
    //synchronized (this) {
    //  this._dbconnectionsInUse.remove(cn);
    //  this._dbconnections.add(cn);
    //}
	/*synchronized (_connectionsInUse) {
		if(this._connectionsInUse.contains(cn)){
			this._connectionsInUse.remove(cn);
			this._connectionsInUseCount--;
		}
	}
	synchronized (_activeConnections) {
		if(!this._activeConnections.contains(cn)){
			this._activeConnections.add(cn);
			this._activeConnectionsCount++;
		}
	}*/
  }

  public static void executeDBRequest(TreeMap<Integer, ArrayList<Object>> dbResultMap,String dbAllias, String sqlStatement, HashMap<String, Object> sqlparams,Object rowOutputMethodOwner) throws Exception{
	  executeDBRequest(dbResultMap, dbAllias, sqlStatement, sqlparams, rowOutputMethodOwner, null);
  }
  
  public static void executeDBRequest(TreeMap<Integer, ArrayList<Object>> dbResultMap,String dbAllias, String sqlStatement, HashMap<String, Object> sqlparams,Object rowOutputMethodOwner,String rowOuputMethodName) throws Exception
  {
    Database dbToUse = null;
    //synchronized (_dbAlliasLock) {
      dbToUse = (Database)_databases.get(dbAllias.toUpperCase());
      if(dbToUse!=null){
      	executeDBRequest(dbResultMap, dbToUse, sqlStatement, sqlparams,rowOutputMethodOwner,rowOuputMethodName);
      }
    //}
    if(dbToUse==null){
    	throw new Exception("NO DBALLIAS:["+dbAllias+"] CONFIGURED");
    }
  }

  public static void executeDBRequest(TreeMap<Integer, ArrayList<Object>> dbResultMap,Database dbToUse, String sqlStatement, HashMap<String, Object> sqlparams,Object rowOutputMethodOwner) throws Exception {
	  executeDBRequest(dbResultMap, dbToUse, sqlStatement, sqlparams, rowOutputMethodOwner, null);
  }
  
  public static void executeDBRequest(TreeMap<Integer, ArrayList<Object>> dbResultMap,Database dbToUse, String sqlStatement, HashMap<String, Object> sqlparams,Object rowOutputMethodOwner,String rowOuputMethodName) throws Exception {
    if (dbToUse == null) return;
    
    Exception edb=null;
    try{
    	dbToUse.executeDBRequest(dbResultMap, sqlStatement, sqlparams,rowOutputMethodOwner,rowOuputMethodName);
    }
    catch(Exception e){
    	edb=e;
    }
    if(edb!=null){
    	throw edb;
    }
  }
  
  public static Set<String> registeredDBAlliasses(){
	  return _databases.keySet();
  }
  
  public static void killAllDatabases(){
	  while(!_databases.isEmpty()){
		  String dbAlliasToKill=(String)_databases.keySet().toArray()[0];
		  _databases.remove(dbAlliasToKill).killDatabase();
		 if(_databaseconfigs.containsKey(dbAlliasToKill)) _databaseconfigs.remove(dbAlliasToKill).clear();
	  }
  }

  private void killDatabase() {
	//synchronized (_cpds) {
	//	_cpds.close();
	//}
	//_databaseconfigs.values().remove(this);
  }

  public static void registerDBAlliasFromConfigStream(InputStream configStream) throws Exception{
	  StringBuilder dbconfigs = new StringBuilder();
	    dbconfigs.append(AdhocUtils.inputStreamToString(configStream));
    if (dbconfigs.length() > 0)
      for (String dbconfigitem : dbconfigs.toString().split("\n"))
        if ((!dbconfigitem.startsWith("--")) && 
          (dbconfigitem.indexOf("=") > -1)) {
          StringBuilder dbconfigitemSettings = new StringBuilder(dbconfigitem.substring(dbconfigitem.indexOf("=") + 1).trim());
          String dbconfigallias = dbconfigitem.substring(0, dbconfigitem.indexOf("="));
          LinkedHashMap tempDBSettings = (_databaseconfigs.containsKey(dbconfigallias.toUpperCase())) && (_databases.containsKey(dbconfigallias.toUpperCase())) ? (LinkedHashMap)_databaseconfigs.get(dbconfigallias.toUpperCase()) : new LinkedHashMap();

          while (dbconfigitemSettings.length() > 0) {
            String dbsettingname = "";
            String dbsettingValue = "";

            if (dbconfigitemSettings.indexOf(";") > -1) {
              if (dbconfigitemSettings.substring(0, dbconfigitemSettings.indexOf(";")).indexOf("=") > -1) {
                dbsettingname = dbconfigitemSettings.substring(0, dbconfigitemSettings.indexOf("=")).trim().toUpperCase();
                dbsettingValue = dbconfigitemSettings.substring(dbconfigitemSettings.indexOf("=") + 1, dbconfigitemSettings.indexOf(";")).trim();
              }
              dbconfigitemSettings.delete(0, dbconfigitemSettings.indexOf(";") + 1);
            }
            else if (dbconfigitemSettings.length() > 0) {
              if (dbconfigitemSettings.substring(0, dbconfigitemSettings.length()).indexOf("=") > -1) {
                dbsettingname = dbconfigitemSettings.substring(0, dbconfigitemSettings.indexOf("=")).trim().toUpperCase();
                dbsettingValue = dbconfigitemSettings.substring(dbconfigitemSettings.indexOf("=") + 1, dbconfigitemSettings.length()).trim();
              }
              dbconfigitemSettings.delete(0, dbconfigitemSettings.length());
            }

            if ((!dbsettingname.equals("")) && (!dbsettingValue.equals("")) && 
              ("DBTYPE,DBHOST,DBINSTANCE,DBNAME,DBUSER,DBPW,DBALTUSER".contains(dbsettingname))) {
              tempDBSettings.put(dbsettingname, dbsettingValue);
            }
          }

          if (!tempDBSettings.isEmpty())
            if (_databaseconfigs.containsKey(dbconfigallias.toUpperCase())) {
              ((Database)_databases.get(dbconfigallias.toUpperCase())).reloadDbSettings(tempDBSettings);
            }
            else {
              Database newdb = new Database();
              newdb.reloadDbSettings(tempDBSettings);
              _databases.put(dbconfigallias.toUpperCase(), newdb);
              _databaseconfigs.put(dbconfigallias.toUpperCase(), tempDBSettings);
            }
        }
	    
  }
  
  
public static void registerDBAlliasesFromConfigFile(String dballiassesSettingsPath) throws Exception
  {
    File f = new File(dballiassesSettingsPath);
    if (!f.exists()) {
      f.createNewFile();
    }
    FileInputStream fi = new FileInputStream(dballiassesSettingsPath);
    try{
    	registerDBAlliasFromConfigStream(fi);
    }
    catch(Exception e){
    	fi.close();
    }
    fi.close();
  }

  public static LinkedHashMap<String, LinkedHashMap<String, String>> dbEnvironmentConfig()
  {
    return _databaseconfigs;
  }
  
  public static HashMap<String, Object> rowData(ArrayList<Object> columns,ArrayList<Object> data){
	  HashMap<String,Object> rowDataMap = new HashMap<String,Object>();
	  if(columns==null||data==null) return rowDataMap;
      if(columns.size()==0||data.size()==0) return rowDataMap;
      if(columns.size()!=data.size()) return rowDataMap;
      int colcount = columns.size();
      for (int colindex = 0; colindex < colcount; colindex++) {
        rowDataMap.put(((String)columns.get(colindex)).toUpperCase(), data.get(colindex));
      }
    return rowDataMap;
  }

  public static HashMap<String, Object> rowData(TreeMap<Integer, ArrayList<Object>> dbDataSetMap, int rownr)
  {
    if ((dbDataSetMap != null) && 
      (dbDataSetMap.get(Integer.valueOf(0)) != null) && (dbDataSetMap.get(Integer.valueOf(rownr)) != null)) {
      int colcount = ((ArrayList)dbDataSetMap.get(Integer.valueOf(0))).size();
      return rowData(dbDataSetMap.get(Integer.valueOf(0)), dbDataSetMap.get(Integer.valueOf(rownr)));
    }

    return null;
  }

  public static Database dballias(String dballias) {
    return (Database)_databases.get(dballias.toUpperCase());
  }

  public static void cleanupDataset(TreeMap<Integer, ArrayList<Object>> dataset)
  {
    if (dataset == null) return;
    int rowindex = 0;
    while (!dataset.isEmpty()) {
      dataset.remove(Integer.valueOf(rowindex = ((Integer)dataset.firstKey()).intValue()));
      dataset.keySet().remove(Integer.valueOf(rowindex));
    }
    dataset.keySet().clear();
    dataset.clear();
  }
  
  public static String rowField(ArrayList<String> rowColumns,ArrayList<String> rowData,String fieldName){
	  String rowField=rowData.get(rowColumns.indexOf(fieldName));
	  return (rowField==null?"":rowField).trim();
  }

  public static String rowField(TreeMap<Integer, ArrayList<String>> dataset, int rowindex, String fieldName)
  {
    if (dataset.isEmpty()) return "";
    if (dataset.size() == 1) {
      return "";
    }
    int colindex = dataset.get(0).indexOf(fieldName.toUpperCase());
    if (colindex > -1) {
      return  dataset.get(rowindex).get(colindex);
    }
    return "";
  }

  public static String rowField(TreeMap<Integer, ArrayList<String>> dataset, int rowindex, int fieldIndex)
  {
    if (dataset.isEmpty()) return "";
    if (dataset.size() == 1) {
      return "";
    }
    int colindex = fieldIndex;
    if ((colindex > -1) && (colindex < dataset.get(0).size())) {
      return dataset.get(rowindex).get(colindex);
    }
    return "";
  }
  
  public static void populateFlatFileStreamFromDataset(
			TreeMap<Integer, ArrayList<Object>> dataSetToReadFrom,
			String flatfileFormat,OutputStream outputDatasetSteam,char colSep,boolean columnsInFirstRow) throws Exception{
	  for(int rowindex:dataSetToReadFrom.keySet()){
		  ArrayList<Object> dataRow=new ArrayList<Object>(dataSetToReadFrom.get(rowindex));
		  if(columnsInFirstRow){
			  columnsInFirstRow=false;
			int columnNamesNotEmpty=0;  
			for(Object dataCol:dataRow){
				if(!dataCol.equals("")){
					columnNamesNotEmpty++;
				}
			}
			if(columnNamesNotEmpty<dataRow.size()) continue;
			boolean isFirstColumn=false;
			while(!dataRow.isEmpty()){
					String colData=dataRow.remove(0).toString().trim().toUpperCase();
					if(isFirstColumn){
						isFirstColumn=false;
						if(colData.startsWith("ID")){
							colData="i"+colData.substring(1);
						}
					}
				  byte[] bytesToWrite=("\""+colData.replaceAll("[\"]", "\"\"")+"\"").getBytes();
				  outputDatasetSteam.write(bytesToWrite);
				  if(dataRow.size()>0){
					  outputDatasetSteam.write((byte)colSep);
				  }
			  }
			  
			  outputDatasetSteam.write("\r\n".getBytes());
			
		  }
		  else{
			  while(!dataRow.isEmpty()){
				  String cellData=dataRow.remove(0).toString().trim();
				  cellData=(cellData.indexOf("\"")>-1?cellData.replaceAll("[\"]", "\"\""):cellData);
				  byte[] bytesToWrite=(cellData.equals("")?"":(cellData.indexOf("\"")>-1||cellData.indexOf(",")>-1 ? "\""+cellData.replaceAll("[\"]", "\"\"")+"\"":cellData)).getBytes();
				  outputDatasetSteam.write(bytesToWrite);
				  if(dataRow.size()>0){
					  outputDatasetSteam.write((byte)colSep);
				  }
			  }
			  
			  outputDatasetSteam.write("\r\n".getBytes());
		  }		  
	  }
	  outputDatasetSteam.flush();
  }
  
  public static void populateDatasetFromFlatFileStream(
			String flatfileFormat,InputStream inputDatasetSteam,char colSep,Object rowOutputMethodOwner,String rowOutputMethodName) throws Exception{
	  ArrayList<Object> datasetColumns=new ArrayList<Object>();
	  try{
		  populateDatasetFromFlatFileStream(null, flatfileFormat, inputDatasetSteam, datasetColumns, colSep, rowOutputMethodOwner,rowOutputMethodName);
		  datasetColumns.clear();
		  datasetColumns=null;
	  } catch(Exception e){
		  datasetColumns.clear();
		  datasetColumns=null;
		  throw e;
	  }
  }
  
  public static void populateDatasetFromFlatFileStream(
			TreeMap<Long, ArrayList<Object>> dataSetToFill,
			String flatfileFormat,InputStream inputDatasetSteam,ArrayList<Object> datasetColumns,char colSep,Object rowOutputMethodOwner) throws Exception{
	  populateDatasetFromFlatFileStream(dataSetToFill, flatfileFormat, inputDatasetSteam, datasetColumns, colSep, rowOutputMethodOwner,null);
  }

	public static void populateDatasetFromFlatFileStream(
			TreeMap<Long, ArrayList<Object>> dataSetToFill,
			String flatfileFormat,InputStream inputDatasetSteam,ArrayList<Object> datasetColumns,char colSep,Object rowOutputMethodOwner,String rowOutputMethodName) throws Exception{
		if((rowOutputMethodName=(rowOutputMethodName==null?"readRowData":rowOutputMethodName)).equals("")){
			rowOutputMethodName="readRowData";
		}
		Method rowOutputMethod=(rowOutputMethodOwner==null?null:inovo.adhoc.AdhocUtils.findMethod(rowOutputMethodOwner.getClass().getMethods(), rowOutputMethodName, false));
		
		boolean readColumnsOut=false;
		if(datasetColumns==null&&rowOutputMethodOwner==null){
			if(dataSetToFill.size()==0){
				datasetColumns = new ArrayList<Object>();
			}
			else if(dataSetToFill.size()==1){
				datasetColumns=dataSetToFill.get(0);
			}
			else{
				datasetColumns = new ArrayList<Object>();
			}
		}
		else{
			if(datasetColumns==null){
				datasetColumns=new ArrayList<Object>();
			}
		}
		
		if(datasetColumns.isEmpty()){
			readColumnsOut=true;
		}
		
		int bavailable=0;
		boolean intextData=false;
		byte[] bytesreadout=new byte[8192];
		char prevc=0;
		String linefield="";
		ArrayList<Object> rowArray=new ArrayList<Object>();
		long rowIndex=0;
		
		int colindex=0;
		int rowcolindex=0;
		int totalColumns=0;
		if(!readColumnsOut){
			if(rowOutputMethodOwner==null) dataSetToFill.put(rowIndex++, datasetColumns);
			totalColumns=datasetColumns.size();
		}
		
		boolean doneProcessing=false;
		
		if(flatfileFormat.equals("CSV")) {
			java.io.InputStreamReader rdrin = new java.io.InputStreamReader(inputDatasetSteam);
			char[] rdrchars=new char[8192];
			int rdrcharsl=0;
			int rdrchri=0;
			while((rdrcharsl=rdrin.read(rdrchars,0,rdrchars.length))>0) {
				
				while(rdrchri<rdrcharsl){
					char cstrm=rdrchars[rdrchri++];
					if(cstrm==colSep){
						if(intextData){
							prevc=cstrm;
							linefield+=cstrm;
							continue;
						}
						if(readColumnsOut){
							linefield=linefield.trim().toUpperCase();
							if(!linefield.equals("")){
								datasetColumns.add(linefield);
							}
						}
						else{
							linefield=linefield.trim();
							if(datasetColumns.get(colindex).equals(linefield.toUpperCase())) colindex++;
							rowArray.add(linefield.trim());
							rowcolindex++;
						}
						linefield="";
						prevc=cstrm;
					}
					else if(cstrm=='\"'){
						if(!intextData&&prevc!='\"'){
							intextData=true;
							linefield="";
							prevc=0;
						}
						else{
							if(prevc==cstrm&&prevc=='\"'){
								linefield+=cstrm;
								prevc=0;
							}
							else if(prevc!=cstrm&&cstrm=='\"'){
								intextData=false;
							}
							else{
								linefield+=cstrm;
								prevc=cstrm;
							}
						}
					}
					else if(cstrm==13){				
						if(!intextData){prevc=cstrm; continue; }
						linefield+=cstrm;
						prevc=cstrm;
					}
					else if(cstrm==10){
						if(intextData){
							linefield+=cstrm;
							prevc=cstrm;
							continue;
						}
						else if(readColumnsOut){
							linefield=linefield.trim().toUpperCase();
							if(!linefield.equals("")){
								datasetColumns.add(linefield);
							}
							if(rowOutputMethodOwner==null){
								dataSetToFill.put(rowIndex++, datasetColumns);
							}
							else{
								if(rowOutputMethodOwner!=null&&rowOutputMethod!=null){
									rowOutputMethod.invoke(rowOutputMethodOwner, new Object[]{doneProcessing, Long.valueOf(rowIndex++),datasetColumns,datasetColumns});
					            }
							}
							totalColumns=datasetColumns.size();
							readColumnsOut=false;
						}
						else{
							linefield=linefield.trim();
							if(datasetColumns.get(colindex).equals(linefield.toUpperCase())) colindex++;
							rowArray.add(linefield);								
							rowcolindex++;								
							if(rowcolindex>=totalColumns&&totalColumns>colindex){
								while(rowArray.size()>totalColumns) rowArray.remove(rowArray.size()-1);
								if(rowOutputMethodOwner==null){
									dataSetToFill.put(rowIndex++,rowArray);
								}
								else{
									if(rowOutputMethodOwner!=null&&rowOutputMethod!=null){
										rowOutputMethod.invoke(rowOutputMethodOwner, new Object[]{doneProcessing, Long.valueOf(rowIndex++),rowArray,datasetColumns});
						            }
								}
							}
							else{
								//TODO
							}
							rowArray.clear();
						}	
						rowcolindex=0;
						colindex=0;
						linefield="";
						prevc=cstrm;
					}
					else{
						linefield+=cstrm;
						prevc=cstrm;
					}
				}
			}
		} else {
			while((bavailable=inputDatasetSteam.read(bytesreadout))>-1){
				if(bavailable>0){
					int bcount=0;
					while(bcount<bavailable){
						byte bf=bytesreadout[bcount++];
						char cstrm=((bf==0?' ':(char)bf));
						if(flatfileFormat.equals("CSV")){
							if(cstrm==colSep){
								if(intextData){
									prevc=cstrm;
									linefield+=cstrm;
									continue;
								}
								if(readColumnsOut){
									linefield=linefield.trim().toUpperCase();
									if(!linefield.equals("")){
										datasetColumns.add(linefield);
									}
								}
								else{
									linefield=linefield.trim();
									if(datasetColumns.get(colindex).equals(linefield.toUpperCase())) colindex++;
									rowArray.add(linefield.trim());
									rowcolindex++;
								}
								linefield="";
								prevc=cstrm;
							}
							else if(cstrm=='\"'){
								if(!intextData&&prevc!='\"'){
									intextData=true;
									linefield="";
									prevc=0;
								}
								else{
									if(prevc==cstrm&&prevc=='\"'){
										linefield+=cstrm;
										prevc=0;
									}
									else if(prevc!=cstrm&&cstrm=='\"'){
										intextData=false;
									}
									else{
										linefield+=cstrm;
										prevc=cstrm;
									}
								}
							}
							else if(cstrm==13){				
								if(!intextData){prevc=cstrm; continue; }
								linefield+=cstrm;
								prevc=cstrm;
							}
							else if(cstrm==10){
								if(intextData){
									linefield+=cstrm;
									prevc=cstrm;
									continue;
								}
								else if(readColumnsOut){
									linefield=linefield.trim().toUpperCase();
									if(!linefield.equals("")){
										datasetColumns.add(linefield);
									}
									if(rowOutputMethodOwner==null){
										dataSetToFill.put(rowIndex++, datasetColumns);
									}
									else{
										if(rowOutputMethodOwner!=null&&rowOutputMethod!=null){
											rowOutputMethod.invoke(rowOutputMethodOwner, new Object[]{doneProcessing, Long.valueOf(rowIndex++),datasetColumns,datasetColumns});
							            }
									}
									totalColumns=datasetColumns.size();
									readColumnsOut=false;
								}
								else{
									linefield=linefield.trim();
									if(datasetColumns.get(colindex).equals(linefield.toUpperCase())) colindex++;
									rowArray.add(linefield);								
									rowcolindex++;								
									if(rowcolindex>=totalColumns&&totalColumns>colindex){
										while(rowArray.size()>totalColumns) rowArray.remove(rowArray.size()-1);
										if(rowOutputMethodOwner==null){
											dataSetToFill.put(rowIndex++,rowArray);
										}
										else{
											if(rowOutputMethodOwner!=null&&rowOutputMethod!=null){
												rowOutputMethod.invoke(rowOutputMethodOwner, new Object[]{doneProcessing, Long.valueOf(rowIndex++),rowArray,datasetColumns});
								            }
										}
									}
									else{
										//TODO
									}
									rowArray.clear();
								}	
								rowcolindex=0;
								colindex=0;
								linefield="";
								prevc=cstrm;
							}
							else{
								linefield+=cstrm;
								prevc=cstrm;
							}
						}
					}
				}
			}
		}
	}

	public static void populateColumnsArray(ArrayList<String> columnsArray,
			String datalineFormat, String dataline,char colSep) {
		String linefield="";
		boolean intextData=false;
		char prevc=0;
		for(char cf:dataline.toCharArray()){
			if(cf==colSep){
				if(intextData){
					prevc=cf;
					linefield+=cf;
					continue;
				}
				linefield=linefield.trim().toUpperCase();
				if(!linefield.equals("")) columnsArray.add(linefield);
				linefield="";
				prevc=cf;
			}
			else if(cf=='\"'){
				if(!intextData&&prevc!='\"'){
					intextData=true;
					linefield="";
					prevc=0;
				}
				else{
					if(prevc==cf&&prevc=='\"'){
						linefield+=cf;
						prevc=0;
					}
					else if(prevc!=cf&&cf=='\"'){
						intextData=false;
					}
					else{
						linefield+=cf;
						prevc=cf;
					}
				}
			}
			else if(cf==13){				
				if(!intextData){prevc=cf; continue; }
				linefield+=cf;
				prevc=cf;
			}
			else if(cf==10){
				if(intextData){
					linefield+=cf;
					prevc=cf;
					continue;
				}
				linefield=linefield.trim().toUpperCase();
				if(!linefield.equals("")) columnsArray.add(linefield);
				linefield="";
				prevc=cf;
			}
			else{
				linefield+=cf;
				prevc=cf;
			}
		}
		linefield=linefield.trim().toUpperCase();
		if(!linefield.equals("")) columnsArray.add(linefield);
	}
	
	public TreeMap<String,List<List<String>>> tables(List<String> filterTables,Object filtelOwner,Method filterMethod){
		if(filterMethod!=null){
			filterMethod.setAccessible(true);
		}
		TreeMap<String,List<List<String>>> tables=new TreeMap<String,List<List<String>>>();
		
		TreeMap<Integer, ArrayList<Object>> listedTables=new TreeMap<Integer, ArrayList<Object>>();
		
		String sqlSelectTablesDefs="";
		String sqlSelectTableColumns="";
		
		if(this._dbtype.equals("oracle")){
			sqlSelectTablesDefs="select owner,table_name,column_name,column_id,data_type,case nullable when 'Y' then 1 else 0 end as nullable,data_default,0 as identity_col,COALESCE((SELECT 1 FROM all_cons_columns WHERE constraint_name = (SELECT constraint_name FROM user_constraints WHERE UPPER(table_name) = UPPER(all_cons_columns.table_name) AND CONSTRAINT_TYPE = 'P') and column_name=all_tab_columns.column_name and all_cons_columns.table_name=all_tab_columns.table_name),0) as indexed_column from all_tab_columns where owner='"+this._dbuser+"' order by owner,table_name,column_id";
		}
		else if(this._dbtype.equals("sqlserver")){
			sqlSelectTablesDefs="SELECT tables.TABLE_SCHEMA,tables.TABLE_NAME,COLUMNS.COLUMN_NAME,COLUMNS.ORDINAL_POSITION,DATA_TYPE,IS_NULLABLE,COLUMN_DEFAULT,COLUMNPROPERTY(object_id(COLUMNS.TABLE_SCHEMA+'.'+COLUMNS.TABLE_NAME), COLUMNS.COLUMN_NAME, 'IsIdentity') AS IS_IDENTITY_COLUMN,COALESCE(OBJECTPROPERTY(OBJECT_ID(CONSTRAINT_SCHEMA + '.' + QUOTENAME(CONSTRAINT_NAME)), 'IsPrimaryKey'),0) AS IS_PRIMARY_COLUMN FROM information_schema.tables INNER JOIN INFORMATION_SCHEMA.COLUMNS ON TABLE_TYPE='BASE TABLE' AND tables.TABLE_SCHEMA='"+_dbuser+"' and tables.TABLE_SCHEMA=COLUMNS.TABLE_SCHEMA and TABLES.TABLE_NAME=COLUMNS.TABLE_NAME LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE ON OBJECTPROPERTY(OBJECT_ID(CONSTRAINT_SCHEMA + '.' + QUOTENAME(CONSTRAINT_NAME)), 'IsPrimaryKey') = 1 AND KEY_COLUMN_USAGE.TABLE_NAME=COLUMNS.TABLE_NAME AND KEY_COLUMN_USAGE.TABLE_SCHEMA = COLUMNS.TABLE_SCHEMA AND KEY_COLUMN_USAGE.COLUMN_NAME=COLUMNS.COLUMN_NAME ORDER BY TABLE_SCHEMA,TABLE_NAME,ORDINAL_POSITION";
		}
		
		if(!sqlSelectTablesDefs.equals("")){
			try {
				executeDBRequest(listedTables, sqlSelectTablesDefs, null, null);
				List<String> columnsNames=null;
				List<String> columnDefs=null;
				String tabelName="";
				if(!listedTables.isEmpty()){
					ArrayList<Object> columns=listedTables.remove(listedTables.firstKey());
					while(!listedTables.isEmpty()){
						Integer rowIndex=listedTables.firstKey();
						ArrayList<Object> data=listedTables.remove(rowIndex);
						data.remove(0);
						if((filterTables==null||filterTables.contains(data.get(0)))&&(filtelOwner==null||(boolean)filterMethod.invoke(filtelOwner,new Object[]{data.get(0).toString()}))){
							if(!tabelName.equals(data.get(0))){
								if(columnsNames!=null&&columnDefs!=null){
									columnsNames=null;
									columnDefs=null;
								}
								List<List<String>> cols=new ArrayList<List<String>>();
								cols.add(columnsNames=new ArrayList<String>());
								cols.add(columnDefs=new ArrayList<String>());
								tabelName=(String)data.remove(0);
								tables.put(tabelName, cols);
							}
							else if(tabelName.equals(data.get(0))){
								data.remove(0);
							}
							columnsNames.add((String)data.remove(0));
							String colDef="";
							while(!data.isEmpty()){
								colDef+=data.remove(0).toString()+(data.isEmpty()?"":"|");
							}
							columnDefs.add(colDef);	
						}
						else{
							data.clear();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(!listedTables.isEmpty()){
				this.cleanupDataset(listedTables);
				listedTables.clear();
				listedTables=null;
			}
		}
		
		return tables;
	}
	
	public static void attachIDabase(IDatabase idatabase){
		_idatabase=idatabase;
	}
}
