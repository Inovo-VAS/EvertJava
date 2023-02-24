package collections.master.backend;

public class MonitorProcessor{

	private Monitor monitor=null;
	public boolean done=false;
	
	public MonitorProcessor(Monitor monitor){
		this.monitor=monitor;
	}
	
	public Monitor monitor(){
		return this.monitor;
	}
	
	public boolean done(){
		return this.done;
	}
}
