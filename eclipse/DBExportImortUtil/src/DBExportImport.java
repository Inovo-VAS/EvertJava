import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
		    	 }
		    }
			if (t != null) System.out.println(t);
		}
	};
	
	public static void main(String[] args) {
		
		final String[] extArgs=args;
		InovoCoreEnvironmentManagerExtended InovoCoreEnvironmentManagerExtended=new InovoCoreEnvironmentManagerExtended(){
			@Override
			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager)
					throws Exception {
				/*HashMap<String, Object> params=new HashMap<String, Object>();
				params.put("THEDATE", "");
				params.put("NAME", "");
				this.dbAllias("DESTINATION").executeDBRequest(null, "SELECT GETDATE() AS THEDATE",params,null,(String)null);
				
				this.dbAllias("DESTINATION").executeDBRequest(null, "SELECT TOP 1 NAME FROM SQLPR1.PREP.PCO_OUTBOUNDSERVICE",params,null,(String)null);
				
				this.logDebug("THEDATE:"+params.get("THEDATE"));
				this.logDebug("NAME:"+params.get("NAME"));
				*/
				HashMap<String, Object> params=new HashMap<String, Object>();
				String sqlStatement="";
				String dbAlias="";
				
				for(String arg:extArgs){
					if(arg.startsWith("sql=")){
						sqlStatement+=arg.substring("sql=".length(), arg.length());
					}
					else if(arg.startsWith("sql-param=")){
						arg=arg.substring("sql-param".length(), arg.length());
						if(arg.indexOf("=")>0){
							params.put(arg.substring(0, arg.indexOf("=")), arg.substring(arg.indexOf("=")+1, arg.length()));
						}
					} else if(arg.startsWith("db-alias=")){
						dbAlias=arg.substring("db-alias=".length(), arg.length());
					}
				}
				try{
					this.dbAllias(dbAlias).executeDBRequest(null,sqlStatement,params,this,"readRowData");
				} catch(Exception e){
					e.printStackTrace();
				}
				
				super.processCoreEnvironment(inovoCoreEnvironmentManager);
			}
			
			@Override
			public String defaultServletContextName() {
				return DBExportImport.class.getSimpleName();
			}
		};
		
//		InovoCoreEnvironmentManagerExtended InovoCoreEnvironmentManagerExtended=new InovoCoreEnvironmentManagerExtended(){
//			
//			
//			@Override
//			public String defaultLocalPath(String suggestedlocalpath) {
//				//suggestedlocalpath="D:/projects/clients/inovo/java/";
//				return super.defaultLocalPath(suggestedlocalpath);
//			}
//			
//			@Override
//			public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager)
//					throws Exception {
//				/*HashMap<String, Object> params=new HashMap<String, Object>();
//				params.put("THEDATE", "");
//				params.put("NAME", "");
//				this.dbAllias("DESTINATION").executeDBRequest(null, "SELECT GETDATE() AS THEDATE",params,null,(String)null);
//				
//				this.dbAllias("DESTINATION").executeDBRequest(null, "SELECT TOP 1 NAME FROM SQLPR1.PREP.PCO_OUTBOUNDSERVICE",params,null,(String)null);
//				
//				this.logDebug("THEDATE:"+params.get("THEDATE"));
//				this.logDebug("NAME:"+params.get("NAME"));
//				*/
//				HashMap<String, Object> params=new HashMap<String, Object>();
//				String sqlStatement="";
//				String dbAlias="";
//				
//				for(String arg:extArgs){
//					if(arg.startsWith("sql=")){
//						sqlStatement+=arg.substring("sql=".length(), arg.length());
//					}
//					else if(arg.startsWith("sql-param=")){
//						arg=arg.substring("sql-param".length(), arg.length());
//						if(arg.indexOf("=")>0){
//							params.put(arg.substring(0, arg.indexOf("=")), arg.substring(arg.indexOf("=")+1, arg.length()));
//						}
//					} else if(arg.startsWith("db-alias=")){
//						dbAlias=arg.substring("db-alias=".length(), arg.length());
//					}
//				}
//				try{
//					this.dbAllias(dbAlias).executeDBRequest(null,sqlStatement,params,this,"readRowData");
//				} catch(Exception e){
//					e.printStackTrace();
//				}
//				
//				super.processCoreEnvironment(inovoCoreEnvironmentManager);
//			}
//			
//			@Override
//			public String defaultServletContextName() {
//				return DBExportImport.class.getSimpleName();
//			}
//			
//			
//		};
		
		executorService.submit((Callable)InovoCoreEnvironmentManagerExtended);
	}

	
	public static class InovoCoreEnvironmentManagerExtended extends InovoCoreEnvironmentManager{
		
		public InovoCoreEnvironmentManagerExtended() {
			super();
		}
		
		public void readRowData(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns){
			if(rowIndex==0){
				for(Object col:columns){
					System.out.print(col+"\t");
				}
				System.out.println();
			} else {
				for(Object dat:data){
					System.out.print(dat+"\t");
				}
				System.out.println();
			}
		}
	}
}
