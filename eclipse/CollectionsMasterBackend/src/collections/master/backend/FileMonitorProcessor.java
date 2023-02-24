package collections.master.backend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.sun.xml.internal.messaging.saaj.packaging.mime.Header;

public class FileMonitorProcessor extends MonitorProcessor implements Runnable{

	public FileMonitorProcessor(FileMonitor monitor) {
		super(monitor);
	}
	
	private LinkedBlockingQueue<String> appendedLines=new LinkedBlockingQueue<String>();
	
	public void appendLineToProcess(String line){
		while(!this.appendedLines.offer(line)){
			try {
				Thread.currentThread().sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void doneAppendingLines(){
		this.done=true;
	}

	public FileMonitor monitor(){
		return (FileMonitor)super.monitor();
	}
	
	private List<String> linesToProcess=Collections.synchronizedList(new ArrayList<String>());
	
	private boolean isheader=true;
	
	private AtomicLong recCount=new AtomicLong();
	
	private String nextLine="";
	
	private long startStamp=0;
	private long endStamp=0;
	@Override
	public void run() {
	
		if(!this.appendedLines.isEmpty()){
			//this.appendedLines.drainTo(linesToProcess);
			while(!this.appendedLines.isEmpty()){
				while((this.nextLine=this.appendedLines.poll())==null){
					try {
						Thread.currentThread().sleep(10);
					} catch (InterruptedException e) {
					}
				}
				if (isheader){
					startStamp=Calendar.getInstance().getTimeInMillis();
				}
				this.processLine((isheader?nextLine.toUpperCase():nextLine).split("[,]"),isheader?(!(isheader=false)):isheader);
				recCount.incrementAndGet();
			}
			
		}
		while(!linesToProcess.isEmpty()){
			this.processLine((isheader?linesToProcess.get(0).toUpperCase():linesToProcess.get(0)).split("[,]"),isheader?(!(isheader=false)):isheader);
			this.linesToProcess.remove(0);
		}
		
		if(this.done){
			if(!this.monitor().done){
				this.monitor().done=true;
			}
			this.monitor().doneMonitoring(this.monitor(), this.monitor().done,Calendar.getInstance().getTimeInMillis());
			endStamp=Calendar.getInstance().getTimeInMillis();
			System.out.println("["+String.valueOf((endStamp-startStamp)/1000)+"]Total lines processed:"+recCount.toString());
		} else {
			this.monitor().executeFileMonitorProcessor(this, this.done());
		}
	}
	
	public void processLine(String[] vals, boolean header) {
	}

	@Override
	public boolean done() {
		return super.done()?(this.appendedLines.isEmpty()&&this.linesToProcess.isEmpty()&&this.monitor().done):false;
	}
}
