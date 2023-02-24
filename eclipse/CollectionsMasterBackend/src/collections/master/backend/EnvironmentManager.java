package collections.master.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import inovo.servlet.InovoCoreEnvironmentManager;

public class EnvironmentManager extends InovoCoreEnvironmentManager {
	
	long startStamp=0;
	
	@Override
	public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager){
		//this.dbAllias("CONNECTIONS").executeDBRequest(null, sqlStatement, sqlparams, rowOutputMethodOwner, rowOuputMethodName);
		this.executeFileMonitor(new FileMonitor("D:/projects/clients/inovo/avon/collections/testmonitor/completed","D:/projects/clients/inovo/avon/collections/testmonitor/working","D:/projects/clients/inovo/avon/collections/testmonitor", "OMNI data- Master File_DAT"){
			@Override
			public void doneMonitoring(FileMonitor fileMonitor,boolean done,long timestamp) {
				if (done){
					completedMonitoring(fileMonitor);
					System.out.println("DONE:"+(timestamp-startStamp));
				} else {
					continueMontitoring(fileMonitor);
				}
			}
			
			@Override
			public void executeFileMonitorProcessor(FileMonitorProcessor fileMonitorProcessor, boolean doneMonitoring) {
				super.executeFileMonitorProcessor(fileMonitorProcessor, doneMonitoring);
				if (doneMonitoring&&fileMonitorProcessor.done||!fileMonitorProcessor.done){
					continueProcessingMonitor(fileMonitorProcessor);
				}
			}
			
			@Override
			public void startProcessingFile(long timestamp) {
				startStamp=timestamp;
				super.startProcessingFile(timestamp);
			}
		});
		
		while(!this.fileMonitors.isEmpty()){
			try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void continueProcessingMonitor(FileMonitorProcessor fileMonitorProcessor) {
	}

	public void continueMontitoring(FileMonitor fileMonitor) {
	}

	protected void completedMonitoring(FileMonitor fileMonitor) {
		if (this.fileMonitors.contains(fileMonitor)){
			this.fileMonitors.remove(fileMonitor);
		}
	}

	@Override
	public String defaultServletContextName() {
		return "CollectionsMasterBackend";
	}
	
	private List<FileMonitor> fileMonitors=Collections.synchronizedList(new ArrayList<FileMonitor>());
	
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		suggestedlocalpath="D:/projects/clients/inovo/java/";
		System.out.println("suggestedlocalpath:->"+suggestedlocalpath);// TODO Auto-generated method stub
		return super.defaultLocalPath(suggestedlocalpath);
	}
	
	public void executeFileMonitor(FileMonitor fileMon){
	}
}
