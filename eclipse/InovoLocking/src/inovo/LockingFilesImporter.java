package inovo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
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
import inovo.servlet.InovoCoreEnvironmentManager;
import inovo.servlet.InovoServletContextListener;

public class LockingFilesImporter implements Callable<LockingFilesImporter>,Runnable{

	private static String listeningPath="";
	
	/*private static ExecutorService execService=new ThreadPoolExecutor(0,Integer.MAX_VALUE,10,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>()){
	
		protected void afterExecute(Runnable r, Throwable t) {
			super.afterExecute(r, t);
			if (t==null){
				if (r instanceof Future<?>){
					try {
						Object result=((Future<?>)r).get();
						if (result!=null){
							if (result instanceof LockingFilesImporter){
								this.submit((Runnable)result);
							} if (result instanceof FileLockUnlock){
								this.submit((Runnable)result);
							}
						}
					} catch(CancellationException e){
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt(); 
					}
				}
			} else {
				System.out.println(t.getMessage());
			}
		};
	};*/
	
	protected class FileLockUnlock implements Callable<FileLockUnlock>,Runnable{
		private File f;
		private String lockunlock="";
		private String orgFileName="";
		private LockingFilesImporter lockingFilesImporter;
		private String userName;
		private String remoteip;
		private String fpathToProcess="";
		private FileLockUnlock(LockingFilesImporter lockingFilesImporter,String fpathToProcess,String lockunlock,String orgFileName,String userName,String remoteip){
			
			/*this.f=new File(f.getAbsolutePath().replaceAll("sep", "lck"));
			f.renameTo(this.f);*/
			this.fpathToProcess=fpathToProcess;
			this.lockunlock=lockunlock;
			this.orgFileName=orgFileName;
			this.lockingFilesImporter=lockingFilesImporter;
			this.userName=userName;
			this.remoteip=remoteip;
		}
		
		private HashMap<String, Object> lockAccountsFileParams=new HashMap<String, Object>();
		@Override
		public void run() {
			FileInputStream fin;
			try {
				fin = new FileInputStream(this.f=new File(this.fpathToProcess));
				this.lockingFilesImporter.debug("START LOCK/UNLOCK FILE:"+this.orgFileName);
				this.lockingFilesImporter.debug("RAW-FILE:"+this.fpathToProcess);
				lockAccountsFileParams.clear();
				lockAccountsFileParams.put("ORGFILENAME", this.orgFileName);
				lockAccountsFileParams.put("CIMUSERNAME",this.userName);
				lockAccountsFileParams.put("CIMUSERREMOTEIP",this.remoteip);
				lockAccountsFileParams.put("LOCKUNLOCKSTATE", this.lockunlock);
				
				try {
					Database.executeDBRequest(null, "LOCKING", "EXECUTE dbo.REQUESTLOCKUNLOCK :ORGFILENAME,:CIMUSERNAME,:CIMUSERREMOTEIP,:LOCKUNLOCKSTATE,'ACCOUNT','START'", lockAccountsFileParams, null);
					try{
						Database.populateDatasetFromFlatFileStream("CSV", fin, ',', this, "lockAccountsFromFileData");
						try{fin.close();}catch(Exception fe){}
					} catch(Exception e){
						try{fin.close();}catch(Exception fe){}
						e.printStackTrace();
						this.lockingFilesImporter.debug("ERROR:"+e.getMessage());
					}
					Database.executeDBRequest(null, "LOCKING", "EXECUTE dbo.REQUESTLOCKUNLOCK :ORGFILENAME,:CIMUSERNAME,:CIMUSERREMOTEIP,:LOCKUNLOCKSTATE,'ACCOUNT','STOP'", lockAccountsFileParams, null);
				} catch (Exception dbe) {
					this.lockingFilesImporter.debug("ERROR-dbe:"+dbe.getMessage());
				}
				this.lockingFilesImporter.debug("STOP LOCK/UNLOCK FILE:"+this.orgFileName);
				try{fin.close();}catch(Exception fe){}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				this.lockingFilesImporter.debug("ERROR:"+e1.getMessage());
			}
			this.f.delete();
		}

		@Override
		public FileLockUnlock call() throws Exception {
			return this;
		}
		
		private Database currentdb=null;
		private HashMap<String, Object> lockAccountsParams=new HashMap<String, Object>();
		public void lockAccountsFromFileData(boolean doneRecs,long rowindex,ArrayList<Object> data,ArrayList<Object> cols) throws Exception{
			if (rowindex>0){
				//this.lockingFilesImporter.debug("NEXTREC");
				lockAccountsParams.clear();
				lockAccountsParams.put("ACCOUNTREF", data.get(0));
				lockAccountsParams.put("LOCKED",this.lockunlock);
				lockAccountsParams.put("LOGGEDINUSER", userName);
				lockAccountsParams.put("FILENAME", this.orgFileName);
				if (currentdb==null) {
					currentdb=Database.dballias("LOCKING");
				}
				
				currentdb.executeDBRequest(null,"EXECUTE dbo.LOCKACCOUNT :ACCOUNTREF,:LOCKED,:FILENAME", lockAccountsParams, null,null);
			}
		}
	}
	
	private LinkedBlockingQueue<FileLockUnlock> filesToLockUnlock=new LinkedBlockingQueue<FileLockUnlock>();
	
	public void importLockUnlockFile(File ftoLock,String lockunlock,String orgFilename,String userName,String remoteip){
		String newFileName=ftoLock.getAbsolutePath();
		File destFile=new File(newFileName);
		destFile.renameTo(new File((newFileName=(newFileName).replaceAll("sep", "lck"))));
		
		
		FileLockUnlock fileLockUnlock=new FileLockUnlock(this,newFileName,lockunlock,orgFilename,userName,remoteip);
		while(!filesToLockUnlock.offer(fileLockUnlock)){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private static InovoCoreEnvironmentManager envManager;
	private static LockingFilesImporter lockingFilesImporter=null;
	public static LockingFilesImporter lockingFilesImporter(){
		if (lockingFilesImporter==null){
			envManager=(InovoCoreEnvironmentManager) InovoServletContextListener.inovoServletListener();
			new Thread((lockingFilesImporter=new LockingFilesImporter())).start();
		}
		return lockingFilesImporter;
	}
	
	private boolean active=true;
	public void shutdown() {
		active=false;
	}
	
	@Override
	public void run() {
		ArrayList<FileLockUnlock> fileLockUnlocksArr=new ArrayList<LockingFilesImporter.FileLockUnlock>();
		while(active){
			if(this.filesToLockUnlock.isEmpty()){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			} else {
				this.filesToLockUnlock.drainTo(fileLockUnlocksArr);
			}
			while(active&&!fileLockUnlocksArr.isEmpty()){
				new Thread(fileLockUnlocksArr.remove(0)).start();
			}
		}
	}
	
	@Override
	public LockingFilesImporter call() throws Exception {
		return this;
	}
	
	public void debug(String message){
		if(envManager!=null){
			envManager.logDebug(message);
		}
	}
}
