import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import collections.master.backend.EnvironmentManager;
import collections.master.backend.FileMonitor;
import collections.master.backend.FileMonitorProcessor;
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
		    		 new Thread(r,r.toString()).start();
		    		 //this.execute(r);
		    	 }
		    }
			if (t != null) System.out.println(t);
		}
	};
	
	public static void main(String[] args) {
		executorService.submit((Callable<?>)new EnvironmentManager(){
			@Override
			public void executeFileMonitor(FileMonitor fileMon) {
				executorService.submit(fileMon);
				//new Thread(fileMon).start();
			}
			
			@Override
			public void continueMontitoring(FileMonitor fileMon) {
				executorService.submit(fileMon);
				//new Thread(fileMon).start();				
			}
			
			@Override
			public void continueProcessingMonitor(FileMonitorProcessor fileMonitorProcessor) {
				executorService.submit(fileMonitorProcessor);
				//new Thread(fileMonitorProcessor).start();
			}
		});
	}

}
