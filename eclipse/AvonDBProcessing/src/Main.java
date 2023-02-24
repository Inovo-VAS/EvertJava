import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import inovo.servlet.InovoCoreEnvironmentManager;

public class Main {
	
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
		    		 this.execute(r);
		    	 }
		    }
			if (t != null) System.out.println(t);
		}
	};
	
	public static String appNamePostfix="";

	public static void main(String[] args) {
		for (String arg:args){
			//System.out.println(arg);
			if (arg.equals("ACTIVE")||arg.equals("PASSIVE")) {
				appNamePostfix=arg;
			}
		}
		appNamePostfix="PASSIVE";
		executorService.submit((Callable) new DBProcessing(){
			@Override
			public void runIt(Runnable r) {
				new Thread(r,r.toString()).start();
			}
		});
	}

	public static String defaultLocalPath(String suggestedlocalpath){
		//suggestedlocalpath="D:/projects/clients/inovo/java/";
		return suggestedlocalpath;
	}
	
	public static String defaultServletContextName() {
		return "AvonDBProcessing"+appNamePostfix;
	}
}
