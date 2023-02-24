import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;

import inovo.servlet.InovoCoreEnvironmentManager;

public class DBProcessing extends InovoCoreEnvironmentManager {
		
	private static ExecutorService execJobs=new ThreadPoolExecutor(0, Integer.MAX_VALUE, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),new ThreadPoolExecutor.DiscardPolicy()){
		protected void afterExecute(Runnable r, Throwable t) {
			super.afterExecute(r, t);
			if (t == null){
		    	 if(r instanceof Future<?>) {
		    		 try {
		    			 Object result = ((Future<?>) r).get();
		    			 if (result instanceof SubJobExecuteRecords){
		    				 //new Thread((Runnable)result).start();
		    				 this.submit((Runnable)result);
		    			 } else if (result instanceof SubJobCaptureRecords){
		    				 //new Thread((Runnable)result).start();
		    				 execCaptureData.submit((Callable)result);
		    			 }
		    		 } catch (CancellationException ce) {
			           t = ce;
		    		 } catch (ExecutionException ee) {
			           t = ee.getCause();
		    		 } catch (InterruptedException ie) {
			           Thread.currentThread().interrupt(); // ignore/reset
		    		 }
		    	 } else if(r instanceof Runnable){
		    		 //new Thread(r).start();
		    	 }
		    }
			if (t != null) System.out.println(t);
		}
	};
	
	private static ExecutorService execCaptureData=new ThreadPoolExecutor(0, Integer.MAX_VALUE, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),new ThreadPoolExecutor.DiscardPolicy()){
		protected void afterExecute(Runnable r, Throwable t) {
			super.afterExecute(r, t);
			if (t == null){
		    	 if(r instanceof Future<?>) {
		    		 try {
		    			 Object result = ((Future<?>) r).get();
		    			 if (result instanceof SubJobCaptureRecords){
		    				 //new Thread((Runnable)result).start();
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
		    		 //new Thread(r).start();
		    	 }
		    }
			if (t != null) System.out.println(t);
		}
	};
	
	@Override
	public void initializeServletContext(ServletContext sc) {
		super.initializeServletContext(sc);
	}
	
	public class SubJobExecuteRecords implements Callable<SubJobExecuteRecords>, Runnable{
		private DBProcessing dbProcessing;
		private String sqlToExecute="";
		private int procid;
		private long rowid=0;
		
		private ArrayList<List<List<Object>>> capturedRowsListed=new ArrayList<List<List<Object>>>();
		
		public SubJobExecuteRecords(int procid, DBProcessing dbProcessing,String sqlstatement){
			this.dbProcessing=dbProcessing;
			this.sqlToExecute=sqlstatement;
			this.procid=procid;
		}

		@Override
		public void run() {
			while(this.dbProcessing.attint.get()>0){
				if (!this.dbProcessing.queuedCapturedRows.isEmpty()){
					this.dbProcessing.queuedCapturedRows.drainTo(capturedRowsListed);
				}
				while(!this.capturedRowsListed.isEmpty()){
					List<List<Object>> capturedRows=this.capturedRowsListed.remove(0);
					while(!capturedRows.isEmpty()){
						this.dbProcessing.OracleRecord(this.procid,rowid++, (ArrayList<Object>) capturedRows.remove(0), (ArrayList<Object>) null);
					}
				}
			}			
		}

		@Override
		public SubJobExecuteRecords call() throws Exception {
			return this;
		}
	}
	
	
	
	public class SubJobCaptureRecords implements Callable<SubJobCaptureRecords>, Runnable{
		private DBProcessing dbProcessing;
		private String sqlToExecute="";
		private int procid;
		
		private List<List<Object>> capturedRows=new ArrayList<List<Object>>();
		
		public SubJobCaptureRecords(int procid, DBProcessing dbProcessing,String sqlstatement){
			this.dbProcessing=dbProcessing;
			this.sqlToExecute=sqlstatement;
			this.procid=procid;
		}
		
		public void OracleRecord(long rowid,ArrayList<Object> data,ArrayList<Object> columns){
			//dbProcessing.OracleRecord(procid, rowid, data, columns);
			/*if (rowid==0){
				dbProcessing.logDebug("["+String.valueOf(this.procid)+"]:Start Reading data");
			}
			else if (rowid%10000==0){
				dbProcessing.logDebug("["+String.valueOf(this.procid)+"]Read "+rowid+" rows");
			}*/
			if (rowid>0){
				List<Object> newdata=new ArrayList<Object>();
				newdata.addAll(data);
				this.capturedRows.add(newdata);
				newdata=null;
			}
			
			if (this.capturedRows.size()%1000==0){
				this.dbProcessing.flushCapturedRows(this.capturedRows);
			}
		}
		
		@Override
		public void run() {
			try {
				this.dbProcessing.dbAllias("GQASA").executeDBRequest(null, this.sqlToExecute,(HashMap<String, Object>) null, this, "OracleRecord");
				if (this.capturedRows.size()>0){
					this.dbProcessing.flushCapturedRows(this.capturedRows);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.dbProcessing.attint.decrementAndGet();
		}

		@Override
		public SubJobCaptureRecords call() throws Exception {
			return this;
		}
	}
	
	private AtomicInteger attint=new AtomicInteger(0);
	
	@Override
	public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
		
		/*File fdump=new File(this.rootPath()+"/dump.csv");
		if (!fdump.exists()){
			try {
				fdump.createNewFile();
			} catch (IOException e) {
			}
		}*/
		Connection sourcecn=null;
		PreparedStatement preppedSourceStatement=null;
		try {
			sourcecn = this.dbAllias("GQASA").nextSQLConnection();
			this.logDebug("CONNECTED TO SOURCE");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (sourcecn!=null){
			try {
				String sourceSql="";
				if (Main.appNamePostfix.equals("ACTIVE")){
					sourceSql="SELECT DATASET,ACCOUNTNUMBER,FIRSTNAME,LASTNAME,CELLPHONE,PHONE,EMAIL,CREATED_DATE,BALANCE,LATEST_INVOICEDATE,OVERDUEBAL,OLDEST_INVOICEDATE FROM ( select OMNI_ACCOUNTS.*,CASE WHEN COALESCE(LATEST_INVOICEDATE,CREATED_DATE) > CREATED_DATE THEN COALESCE(LATEST_INVOICEDATE,CREATED_DATE) ELSE CREATED_DATE END AS LOOKUPDATE from XAL_SRA.OMNI_ACCOUNTS) DUAL WHERE LOOKUPDATE >= TO_DATE(TO_CHAR(SYSDATE-120,'YYYY-MM-DD'),'YYYY-MM-DD') AND ROWNUM<=(SELECT MAX(ROWNUM) FROM ( select OMNI_ACCOUNTS.*,CASE WHEN COALESCE(LATEST_INVOICEDATE,CREATED_DATE) > CREATED_DATE THEN COALESCE(LATEST_INVOICEDATE,CREATED_DATE) ELSE CREATED_DATE END AS LOOKUPDATE from XAL_SRA.OMNI_ACCOUNTS) DUAL WHERE LOOKUPDATE >= TO_DATE(TO_CHAR(SYSDATE-120,'YYYY-MM-DD'),'YYYY-MM-DD'))";
				} else if (Main.appNamePostfix.equals("PASSIVE")) {
					//sourceSql="SELECT DATASET,ACCOUNTNUMBER,FIRSTNAME,LASTNAME,CELLPHONE,PHONE,EMAIL,CREATED_DATE,BALANCE,LATEST_INVOICEDATE,OVERDUEBAL,OLDEST_INVOICEDATE FROM ( select OMNI_ACCOUNTS.*,CASE WHEN COALESCE(LATEST_INVOICEDATE,CREATED_DATE) > CREATED_DATE THEN COALESCE(LATEST_INVOICEDATE,CREATED_DATE) ELSE CREATED_DATE END AS LOOKUPDATE from XAL_SRA.OMNI_ACCOUNTS) DUAL WHERE LOOKUPDATE < TO_DATE(TO_CHAR(SYSDATE-120,'YYYY-MM-DD'),'YYYY-MM-DD')";
					sourceSql="SELECT DATASET,ACCOUNTNUMBER,FIRSTNAME,LASTNAME,CELLPHONE,PHONE,EMAIL,CREATED_DATE,BALANCE,LATEST_INVOICEDATE,OVERDUEBAL,OLDEST_INVOICEDATE FROM ( select OMNI_ACCOUNTS.*,CASE WHEN COALESCE(LATEST_INVOICEDATE,CREATED_DATE) > CREATED_DATE THEN COALESCE(LATEST_INVOICEDATE,CREATED_DATE) ELSE CREATED_DATE END AS LOOKUPDATE from XAL_SRA.OMNI_ACCOUNTS) DUAL ORDER BY LOOKUPDATE DESC";
				}
				if (!sourceSql.equals("")) {
					preppedSourceStatement = sourcecn.prepareStatement(sourceSql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
					preppedSourceStatement.setFetchDirection(ResultSet.FETCH_FORWARD);
					preppedSourceStatement.setFetchSize(100);

					
					this.logDebug("PREPPED SOURCE STATEMENT");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		Connection destinationcn=null;
		PreparedStatement preppedDestinationStatement=null;
		try {
			destinationcn = this.dbAllias("COLLECTIONS").nextSQLConnection();
			this.logDebug("CONNECTED TO DESTINATION");
			if (Main.appNamePostfix=="ACTIVE") {
				destinationcn.createStatement().execute("TRUNCATE TABLE [COLLECTIONS].[OMNI_ACCOUNTS_ACTIVE]");
			} else if (Main.appNamePostfix=="PASSIVE") {
				destinationcn.createStatement().execute("TRUNCATE TABLE [COLLECTIONS].[OMNI_ACCOUNTS_PASSIVE]");
			}
			this.logDebug("TRUNCATE - "+Main.appNamePostfix);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(sourcecn!=null && destinationcn!=null){
			try {
				String destinationSql="";
				if (Main.appNamePostfix=="ACTIVE") {
					destinationSql="INSERT INTO [COLLECTIONS].[OMNI_ACCOUNTS_ACTIVE] ([DATASET],[ACCOUNTNUMBER],[FIRSTNAME],[LASTNAME],[CELLPHONE],[PHONE],[EMAIL],[CREATED_DATE],[BALANCE],[LATEST_INVOICEDATE],[OVERDUEBAL],[OLDEST_INVOICEDATE],[OMNI_LASTACTION]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,'INSERTED')";
				} else if (Main.appNamePostfix.equals("PASSIVE")){
					destinationSql="INSERT INTO [COLLECTIONS].[OMNI_ACCOUNTS_PASSIVE] ([DATASET],[ACCOUNTNUMBER],[FIRSTNAME],[LASTNAME],[CELLPHONE],[PHONE],[EMAIL],[CREATED_DATE],[BALANCE],[LATEST_INVOICEDATE],[OVERDUEBAL],[OLDEST_INVOICEDATE],[OMNI_LASTACTION]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,'INSERTED')";
				}
				System.out.println(destinationSql);
				preppedDestinationStatement=destinationcn.prepareStatement(destinationSql);
				this.logDebug("PREPPED DESTINATION STATEMENT");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (preppedSourceStatement!=null&&preppedDestinationStatement!=null){
			try {
				this.logDebug("RETRIEVING SOURCE DATA");
				ResultSet resultsets=preppedSourceStatement.executeQuery();
				this.logDebug("RETRIEVED SOURCE DATA");	
					try {
						ResultSetMetaData metaData = preppedSourceStatement.getMetaData();
						int fcount=metaData.getColumnCount();
						//resultsets = preppedSourceStatement.getResultSet();
						
						long startStamp=Calendar.getInstance().getTimeInMillis();
						long lastStamp=startStamp;
						this.logDebug("recs:0");
						this.logDebug("t:"+ String.valueOf(lastStamp-startStamp));
												
						//FileOutputStream fout=null;
						//BufferedOutputStream bufout=null;
						
						//ByteArrayOutputStream buf=new  ByteArrayOutputStream();
						
						/*try {
							if (fdump.exists()){
								fout=new FileOutputStream(fdump);
								bufout=new BufferedOutputStream(fout);
								
							}
						} catch (FileNotFoundException e) {
						}*/
						try {
							ArrayList<Object> data=new ArrayList<Object>();
							long maxCount=0;
							int colIndex=0;
							int paramCount=1;
							String rowstr="";
							while(resultsets.next()){
								while (!data.isEmpty()) data.remove(0);
								paramCount=1;
								while(colIndex<fcount){
									colIndex++;
									
									Object val=resultsets.getObject(colIndex);
									//System.out.println(colIndex+":"+val.getClass().getName());
									if (val == null){
										data.add(val);
									}
									else if (val instanceof String){
										data.add(((String)val).trim());
										//rowstr+=(((String)val).trim());
										/*try {
											//buf.write((((String)val).trim()).getBytes());
										} catch (IOException e) {
										}*/
										
									} else {
										data.add(val);
										//rowstr+=(String.valueOf(val));
										/*try {
											//buf.write(String.valueOf(val).getBytes());
										} catch (IOException e) {
										}*/
									}
									if (colIndex<fcount) {
										//rowstr+="|";
										/*try {
											//buf.write("|".getBytes());
										} catch (IOException e) {
										}*/
									} else if (colIndex==fcount){
										//rowstr+="\r\n";
										/*try {
											//buf.write("\r\n".getBytes());
										} catch (IOException e) {
										}*/
										/*preppedDestinationStatement.setObject(paramCount++, data.get(0));
										preppedDestinationStatement.setObject(paramCount++, data.get(1));
										
										colIndex=0;
										while(colIndex<data.size()){
											preppedDestinationStatement.setObject(paramCount++, data.get(colIndex));
											colIndex++;
										}
										
										preppedDestinationStatement.setObject(paramCount++, data.get(0));
										preppedDestinationStatement.setObject(paramCount++, data.get(1));
										*/
										colIndex=0;
										while(colIndex<data.size()){
											preppedDestinationStatement.setObject(paramCount++, data.get(colIndex));
											colIndex++;
										}
										
										preppedDestinationStatement.addBatch();
									}
								}
								
								//rowstr="";
								colIndex=0;
								maxCount++;
								
								if (maxCount%100==0){
									preppedDestinationStatement.executeBatch();
								}
								
								/*if (maxCount%5000==0){
									try {
										//buf.flush();
										//bufout.write(buf.toByteArray());
										//buf.reset();
									} catch (IOException e) {
										e.printStackTrace();
										break;
									}
								}*/
								if (maxCount%5000==0){
									try {
										Thread.currentThread().sleep(10);
									} catch (InterruptedException e) {
									}
									/*try {
										//bufout.flush();
									} catch (IOException e) {
										e.printStackTrace();
										break;
									}*/
								}
								if (maxCount%10000==0){
									this.logDebug("recs:"+maxCount);
									this.logDebug("t:"+ (Calendar.getInstance().getTimeInMillis()-lastStamp));
									lastStamp=Calendar.getInstance().getTimeInMillis();
								}
							}
							/*try {
								//buf.flush();
								//bufout.write(buf.toByteArray());
								//buf.reset();
							} catch (IOException e) {
								e.printStackTrace();
							}*/
							/*if (bufout!=null){
								try {
									bufout.flush();
								} catch (IOException e) {
								}
								try {
									bufout.close();
								} catch (IOException e) {
								}
								bufout=null;
							}
							if (fout!=null){
								try {
									fout.flush();
								} catch (IOException e) {
								}
								try {
									fout.close();
								} catch (IOException e) {
								}
								fout=null;
							}*/
							preppedDestinationStatement.executeBatch();
							this.logDebug(String.valueOf(maxCount));
							this.logDebug(String.valueOf(Calendar.getInstance().getTimeInMillis()-startStamp));
						} catch (SQLException rsnexte) {
							// TODO Auto-generated catch block
							rsnexte.printStackTrace();
						}
					} catch (SQLException rse) {
						rse.printStackTrace();
					}
					
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private LinkedBlockingQueue<List<List<Object>>> queuedCapturedRows=new LinkedBlockingQueue<List<List<Object>>>();
	
	public void flushCapturedRows(List<List<Object>> capturedRows) {
		List<List<Object>> newCapturedRows=new ArrayList<List<Object>>();
		while(!capturedRows.isEmpty()){
			newCapturedRows.add(capturedRows.remove(0));
		}
		while (!queuedCapturedRows.offer(newCapturedRows)){
			try {
				Thread.currentThread().sleep(10);
			} catch (InterruptedException e) {
			}
		}
		newCapturedRows=null;
	}

	public void runIt(Runnable r){
	
	}
	
	public void OracleRecord(int procid, long rowid,ArrayList<Object> data,ArrayList<Object> columns){
		if (rowid==0){
			this.logDebug("["+String.valueOf(procid)+"]:Start Reading data");
		}
		else if (rowid%100==1000){
			this.logDebug("["+String.valueOf(procid)+"]Read "+rowid+" rows");
		}
	}
	
	/*HashMap<String,File> mappedFiles=new HashMap<String, File>();
	
	@Override
	public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
		try {
			//this.dbAllias("PRESENCE").executeDBRequest(null, "SELECT GETDATE()",(HashMap<String, Object>) null, null, (String)null);
			File audioDir=new File("D:/projects/clients/inovo/avon/Audio Files Converted");
			
			if (audioDir.exists()&&audioDir.isDirectory()){
				for(File faudio:audioDir.listFiles()){
					if (faudio.getName().endsWith(".wav")){
						String fname=faudio.getName();
						fname=fname.substring(0, fname.length()-".wav".length());
						if (!mappedFiles.containsKey(fname)) {
							
							mappedFiles.put(fname, faudio);
							
						}
						//this.dbAllias("PRESENCE").executeDBRequest(null, "SELECT GETDATE()",(HashMap<String, Object>) null, null, (String)null);
					}
				}
			}
			HashMap<String, Object> params=new HashMap<String, Object>();
			for(String name:mappedFiles.keySet()){
				if (mappedFiles.containsKey(name)){
					params.put("NAME", name);
					params.put("AUDIOFILE", mappedFiles.get(name));
					this.dbAllias("PRESENCE").executeDBRequest(null, "UPDATE PREP.PIR_AUDIOFILE SET AUDIOFILE=:AUDIOFILE WHERE NAME=:NAME",params, null, (String)null);
					System.out.println(name);
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("DONE");
	}*/
	
	@Override
	public void disposeServletContext(ServletContext sc) {
		super.disposeServletContext(sc);
	}
	
	@Override
	public String defaultServletContextName() {
		return Main.defaultServletContextName();
	}
	
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		suggestedlocalpath=Main.defaultLocalPath(suggestedlocalpath);
		System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
		return super.defaultLocalPath(suggestedlocalpath);
	}
}
