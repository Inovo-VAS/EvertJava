package inovo.automation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import inovo.queues.Request;

public class Queue extends inovo.queues.Queue {

	public Queue(String queueAllias) throws Exception {
		super(queueAllias);
	}
	
	public Request queueRequest(Request requestToQueue,boolean queueModal) throws Exception{
		return super.queueRequest(requestToQueue, queueModal);
	}
	
	public static ArrayList<Calendar> generateSchedules(Calendar startTimeStamp,Calendar endTimeStamp,int seconds) throws ParseException{
		ArrayList<Calendar> schedules=new ArrayList<Calendar>();
		long startMilliSeconds=startTimeStamp.getTimeInMillis();
		long endMilliSeconds=endTimeStamp.getTimeInMillis();
		
		while((startMilliSeconds+=(seconds*1000))<endMilliSeconds){
			schedules.add(startTimeStamp=startTimeStamp.getInstance());
			startTimeStamp.setTimeInMillis(startMilliSeconds);
		}
		return schedules;
	}
	
	public static ArrayList<Calendar> generateSchedules(String[] schedulesInfo,String datetimeMask) throws ParseException{
		ArrayList<Calendar> schedules=new ArrayList<Calendar>();
		if(schedulesInfo!=null){
			if(schedulesInfo.length>0){
				for(String scheduleInf:schedulesInfo){
					if(scheduleInf.equals("")) continue;
					Calendar nextSchedule=Calendar.getInstance();
					String additionalFormatMask="";
					String additionalFormatDate="";
					if(datetimeMask.indexOf("y")==-1){
						additionalFormatMask="yyyy-";
						additionalFormatDate=new SimpleDateFormat("yyyy").format(nextSchedule.getTime())+"-";
					}
					if(additionalFormatMask.indexOf("M")==-1){
						additionalFormatMask+="MM-";
						additionalFormatDate+=new SimpleDateFormat("MM").format(nextSchedule.getTime())+"-";
					}
					if(additionalFormatMask.indexOf("d")==-1){
						additionalFormatMask+="dd";
						additionalFormatDate+=new SimpleDateFormat("dd").format(nextSchedule.getTime());
					}
					nextSchedule.setTime(new SimpleDateFormat((additionalFormatMask.equals("")?"":additionalFormatMask+" ")+ datetimeMask).parse((additionalFormatDate.equals("")?"":additionalFormatDate+" ")+scheduleInf));
					//System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(nextSchedule.getTime()));
					schedules.add(nextSchedule);
				}
			}
		}
		return schedules;
	}
	
	public static ArrayList<Calendar> generateSchedulesHHmmss(String[] schedulesInfo) throws ParseException{
		return generateSchedules(schedulesInfo,"HH:mm:ss");
	}
	
	public static ArrayList<Calendar> generateSchedulesHHmmssMilli(String[] schedulesInfo) throws ParseException{
		return generateSchedules(schedulesInfo,"HH:mm:ss.SSS");
	}
}
