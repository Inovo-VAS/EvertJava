package inovo.automation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Request extends inovo.queues.Request {
	
	private ArrayList<Long> _schedules=new ArrayList<Long>();
	
	private ArrayList<Long> _schedulesMovedToTheNextDay=new ArrayList<Long>();
	
	public Request(){
		this(null,null);
	}
	
	public Request(HashMap<String,Object> requestProperties){
		this(null,requestProperties);
	}
	
	public Request(ArrayList<Calendar> schedules,HashMap<String,Object> requestProperties){
		super(requestProperties);
		if(schedules!=null){
			for(Calendar schedule:schedules){
				this._schedules.add(schedule.getTimeInMillis());
			}
		}
	}

	@Override
	public boolean canExecute() {
		if(this._schedules.isEmpty()){
			return true;
		}
		else{
			long now=Calendar.getInstance().getTimeInMillis();
			
			if(this._schedules.get(0)<now){
				while(this._schedules.get(0)<now){
					Long millisecondsStampRemoved=this._schedules.remove(0);
					if(this.continuingRequest()){
						millisecondsStampRemoved+=(60*60*1000*24);
						this._schedules.add(millisecondsStampRemoved);
					}
				}
				if(!this._schedules.isEmpty()){
					long nextInterval=now-this._schedules.get(0);
					this.setRequestDelay((nextInterval>2?nextInterval/2:nextInterval));
				}
				else{
					this.setRequestDelay(2);
				}
				return true;
			}
			return false;
		}
	}
	
	@Override
	public boolean canContinue() {
		return !this._schedules.isEmpty();
	}
	
	public  boolean continuingRequest(){
		return true;
	}
}
