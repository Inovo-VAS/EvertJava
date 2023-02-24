package inovo.mail.reports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class ScheduledTask implements Runnable{
	
	private String _subject="";
	private String _from="";
	private String _to="";
	private String _body="";
	private String _reportName="";
	private String _smtpaddress="";
	private String _mailPort="";
	private String _smtpUser="";
	private String _smtpPassword="";
	private String _scheduleType="DAILY";
	private String _suggestedDate="";
	
	private ArrayList<Long> _schedule=new ArrayList<Long>();
	
	public ScheduledTask(String subject,String from,String to,String body,String reportName,String suggestedDate,String smtpaddress,String mailPort,String smtpUser,String smtpPassword,String scheduleType,String[]schedule){
		this.initScheduledTask(subject, from, to, body, reportName,suggestedDate, smtpaddress, mailPort, smtpUser, smtpPassword, scheduleType, schedule);
	}
	
	public void initScheduledTask(String subject,String from,String to,String body,String reportName,String suggestedDate,String smtpaddress,String mailPort,String smtpUser,String smtpPassword,String scheduleType,String[]schedule){
		if(schedule!=null){
			if(schedule.length>0){
				ArrayList<Long> newSchedule=new ArrayList<Long>();
				if((scheduleType=(scheduleType==null?"":scheduleType.trim().toUpperCase())).equals("DAILY")){
					String dateToCkeck=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
					for(String time:schedule){
						if((time=time.trim()).equals("")) continue;
						if(time.length()==5)time=time+":00";
						if(VolvoReportsQueue.isTime(time)){
							Calendar calNextTimestamp=Calendar.getInstance();
							try {
								calNextTimestamp.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateToCkeck+" "+time));
								Long nextTimeStamp=(Long)calNextTimestamp.getTimeInMillis();
								if(newSchedule.contains(nextTimeStamp)) continue;
								newSchedule.add(nextTimeStamp);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
				}
				this.initScheduledTask(subject, from, to, body, reportName,suggestedDate, smtpaddress, mailPort, smtpUser, smtpPassword, scheduleType, newSchedule);
			}
		}
	}
	
	public ScheduledTask(String subject,String from,String to,String body,String reportName,String suggestedDate,String smtpaddress,String mailPort,String smtpUser,String smtpPassword,String scheduleType,ArrayList<Long> schedule){
		this.initScheduledTask(subject, from, to, body, reportName,suggestedDate, smtpaddress, mailPort, smtpUser, smtpPassword, scheduleType, schedule);
	}
	
	public void initScheduledTask(String subject,String from,String to,String body,String reportName,String suggestedDate,String smtpaddress,String mailPort,String smtpUser,String smtpPassword,String scheduleType,ArrayList<Long> schedule){
		this._subject=subject;
		this._body=body;
		this._from=from;
		this._to=to;
		this._reportName=reportName;
		this._suggestedDate=suggestedDate;
		this._smtpaddress=smtpaddress;
		this._mailPort=mailPort;
		this._smtpUser=smtpUser;
		this._smtpPassword=smtpPassword;
		this._scheduleType=(scheduleType=(scheduleType==null?"":scheduleType.trim().toUpperCase())).equals("")?"DAILY":scheduleType;
		if(schedule!=null){
			if(!schedule.isEmpty()){
				Collections.sort(schedule);
				for(Long nextSchedule:schedule){
					if(this._schedule.contains(nextSchedule)) continue;
					this._schedule.add(nextSchedule);
				}
				//_currentActiveSchedule.addAll(_schedule);
			}
		}
	}
	
	private ArrayList<Long> _currentActiveSchedule=new ArrayList<Long>();
	private boolean firstStartup=true;
	
	@Override
	public void run() {
		while(!this._schedule.isEmpty()){
			this.buildActiveSchedule();
			if(!_currentActiveSchedule.isEmpty()){
				long testTimeStamp=Calendar.getInstance().getTimeInMillis();
				long lastTestTimestamp=-1;
				while(testTimeStamp>=this._currentActiveSchedule.get(0).longValue()){
					lastTestTimestamp=this._currentActiveSchedule.remove(0);
					if(this._currentActiveSchedule.isEmpty()) break;
				}
				if(lastTestTimestamp<testTimeStamp&&(testTimeStamp-lastTestTimestamp)<=10000&&lastTestTimestamp>-1){
					try {
						VolvoReportsQueue.sendMail(this._subject,this._from,this._to,this._body, this._smtpaddress,this._mailPort,this._smtpUser,this._smtpPassword,this._reportName,this._suggestedDate);
					} catch (Exception e) {
						inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("err:"+e.getMessage());
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			if(VolvoReportsQueue._shutdownQueue){
				this._schedule.clear();
			}
		}
		VolvoReportsQueue._scheduledReports.remove(this._reportName);
	}

	private void buildActiveSchedule() {
		if(_currentActiveSchedule.isEmpty()){
			if(this._scheduleType.equals("DAILY")){
				Calendar testCalVal=Calendar.getInstance();
				Calendar dateNow=Calendar.getInstance();
				while(_currentActiveSchedule.size()<this._schedule.size()){
					testCalVal.setTimeInMillis(_schedule.get(_currentActiveSchedule.size()));
					testCalVal.set(dateNow.get(Calendar.YEAR),dateNow.get(Calendar.MONTH),dateNow.get(Calendar.DAY_OF_MONTH));
					if(firstStartup){
						firstStartup=false;
					}
					else{
						testCalVal.add(Calendar.DATE, 1);
					}
					_currentActiveSchedule.add((Long)testCalVal.getTimeInMillis());
				}
			}
		}
	}
	
	private void reAdjustSchedule(){
		if(this._currentActiveSchedule.isEmpty()){
			if(this._scheduleType.equals("DAILY")){
				Calendar nextCalcDay=Calendar.getInstance();
				for(int i=0;i<this._schedule.size();i++){
					nextCalcDay.setTimeInMillis(this._schedule.get(i).longValue());
					nextCalcDay.add(Calendar.DATE, 1);
					this._schedule.set(i, (Long)nextCalcDay.getTimeInMillis());
				}
			}
		}
	}

	public boolean isValid() {
		return !_schedule.isEmpty();
	}
}
