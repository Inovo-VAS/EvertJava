package collections.master.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileMonitor extends Monitor implements Runnable{

	private FileMonitorProcessor fileMonitorProcessor=null;
	
	public FileMonitor(String fileCompletedPath,String fileWorkingPath, String filePath,String fileNamePattern){
		this.filePath=filePath;
		this.fileNamePattern=fileNamePattern;
		this.fileWorkingPath=fileWorkingPath;
		this.fileCompletedPath=fileCompletedPath;
	}
	
	private FileMonitorProcessor fileMonitorProcessor(){
		if (this.fileMonitorProcessor==null){
			this.fileMonitorProcessor=new FileMonitorProcessor(this);
			this.startProcessingFile(Calendar.getInstance().getTimeInMillis());
			this.executeFileMonitorProcessor(this.fileMonitorProcessor,this.done);
		}
		return this.fileMonitorProcessor;
	}
	
	public void executeFileMonitorProcessor(FileMonitorProcessor fileMonitorProcessor, boolean doneMonitoring) {	
	}

	private String filePath="";
	private String fileWorkingPath="";
	private String fileCompletedPath="";
	private String fileNamePattern="";
	
	boolean done=false;
	public boolean done(){
		return this.done?this.fileMonitorProcessor==null?true:this.fileMonitorProcessor.done():false;
	}
	
	private File fileDir=null;
	private File fileWorkingDir=null;
	private File fileCompletedDir=null;
	
	@Override
	public void run() {
		if (fileDir==null){
			fileDir=new File(filePath);
		}
		if (fileWorkingDir==null){
			fileWorkingDir=new File(fileWorkingPath);
		}
		if (fileCompletedDir==null){
			fileCompletedDir=new File(fileWorkingPath);
		}
		if (!this.done){
			if (fileDir.exists() && (fileWorkingDir.exists() && fileCompletedDir.exists() && (fileNamePattern!=""))) {
				if (fileDir.isDirectory()) {
					for(File f:fileDir.listFiles()){
						if (f.isFile() && f.getName().indexOf(fileNamePattern)>-1){
							if (f.renameTo(f)){
								this.fileMonitorProcessor();
								try {
									FileInputStream fFrom=new FileInputStream(f);
									File workingFile=new File(fileWorkingDir.getAbsolutePath().replaceAll("[\\\\]", "/")+"/"+ "PROCESSED"+new SimpleDateFormat("yyyyMMdd.HHmmss").format(Calendar.getInstance().getTime())+f.getName());
									if (!workingFile.exists()){
										try {
											if (workingFile.createNewFile()){
												System.out.println("Create processing file "+workingFile.getAbsolutePath());
											}
										} catch (IOException e) {
										}
									}
									if (workingFile.exists()){
										System.out.println(workingFile.toString());
										
										this.fileMonitorProcessor();
										
										FileOutputStream fTo=new FileOutputStream(workingFile);
										byte [] bytesRead=new byte[8192];
										int bytesReadLen=0;
										int bytesReadi=0;	
										StringBuilder stringBuider=new StringBuilder();
										String colVal="";
										byte pb=0;
										try {
											while((bytesReadLen=fFrom.read(bytesRead))>0){
												while(bytesReadi<bytesReadLen){
													if (bytesRead[bytesReadi]!=10 && bytesRead[bytesReadi]!=13){
														if ((((char)bytesRead[bytesReadi])+"").trim().equals("")){
															bytesRead[bytesReadi]=' ';
														}
													}
													switch(bytesRead[bytesReadi]){
													case 10:
														if (pb!=13){
															stringBuider.append(colVal.trim());
															this.fileMonitorProcessor().appendLineToProcess(stringBuider.substring(0, stringBuider.length()));
															stringBuider.append("\n");
															colVal="";
														} else {
															stringBuider.append("\n");
														}
														colVal="";
														if (stringBuider.length()>0){
															fTo.write(stringBuider.substring(0,stringBuider.length()).getBytes());
															stringBuider.delete(0, stringBuider.length());
															stringBuider.setLength(0);
														}
														break;
													case 13:
														stringBuider.append(colVal.trim());
														this.fileMonitorProcessor().appendLineToProcess(stringBuider.substring(0, stringBuider.length()));
														stringBuider.append("\r");
														colVal="";
														break;
													case ',':
														stringBuider.append(colVal.trim()+",");
														colVal="";
														break;
													default:
														colVal+=((char)bytesRead[bytesReadi]);
														break;
													}
												
													pb=bytesRead[bytesReadi];
													bytesReadi++;
													if(bytesReadi==bytesReadLen){
														bytesReadi=0;
														break;
													}
												}											
											}
											if (stringBuider.length()>0){
												this.fileMonitorProcessor().appendLineToProcess(stringBuider.substring(0, stringBuider.length()));
												fTo.write(stringBuider.substring(0,stringBuider.length()).getBytes());
												stringBuider.delete(0, stringBuider.length());
												stringBuider.setLength(0);
											}
											this.fileMonitorProcessor().doneAppendingLines();
											
											fFrom.close();
											fTo.flush();
											fTo.close();
											f.delete();
											while(f.exists()){
												try {
													Thread.currentThread().sleep(10);
												} catch (InterruptedException e) {
												}
											}
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
									workingFile=null;								
								} catch (FileNotFoundException e) {
								}
							}
						}
					}
				}
			}
		} else {
			this.done=true;
		}
		
		if (!this.done){
			try {
				Thread.currentThread().sleep(10);
			} catch (InterruptedException e) {
			}
		}
		this.doneMonitoring(this,this.done(),Calendar.getInstance().getTimeInMillis());
	}

	public void startProcessingFile(long timestamp) {
	}

	public void doneMonitoring(FileMonitor fileMonitor,boolean done,long timestamp) {
	}
}
