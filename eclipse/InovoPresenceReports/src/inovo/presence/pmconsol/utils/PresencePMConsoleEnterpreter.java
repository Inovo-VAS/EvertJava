package inovo.presence.pmconsol.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class PresencePMConsoleEnterpreter {
	
	public class cService{
		public String serviceID;
		public String serviceName;
		public String serviceStatus;
		public String serviceDirection;
		public String serviceType;
		
		public void clear(){
			serviceID = serviceName = serviceStatus = serviceDirection = serviceType = "";
		}
	}
	
	public class cAgentInfo{
		public String service;
		public String serviceType;
		public String serviceName;
		public String agentsAvailible;
		public String agentsInCall;
		public String agentsInACW;
		public String agentsStopped;
		public String agentsInOnter;
		public String agentsTotalConnected;
		
		public TreeMap<Integer,ArrayList<String>> agentsInfoSet=new TreeMap<Integer,ArrayList<String>>();
		protected ArrayList<String> currentAgentInfoRow=null;
		
		public void clear(){
			if(currentAgentInfoRow!=null){
				while(!currentAgentInfoRow.isEmpty()){
					currentAgentInfoRow.remove(0);
				}
				currentAgentInfoRow.clear();
				currentAgentInfoRow=null;
			}
			agentsInfoSet.clear();
			serviceName = "";
			service="";
			serviceType="";
			agentsAvailible = agentsInCall = agentsInACW = agentsStopped = agentsInOnter = agentsTotalConnected = "0";
		}
		
		public String toString(){
			return new StringBuilder().append("[ cAgentInfo ")
						.append("[ ").append("serviceName=").append(serviceName).append(" ]")
						.append("[ ").append("agentsAvailible=").append(agentsAvailible).append(" ]")
						.append("[ ").append("agentsInCall=").append(agentsInCall).append(" ]")
						.append("[ ").append("agentsInACW=").append(agentsInACW).append(" ]")
						.append("[ ").append("agentsStopped=").append(agentsStopped).append(" ]")
						.append("[ ").append("agentsInOnter=").append(agentsInOnter).append(" ]")
						.append("[ ").append("agentsTotalConnected=").append(agentsTotalConnected).append(" ]")
						.append("]").toString();
		}
	}
	public class cCallInfo{
		public String service="";
		public String serviceName = "";
		public String serviceType="";
		public String callsInProgress = "0";
		public String callsInQueue = "0";
		public String callsRinging = "0";
		public String callsPreviewInQueue = "0";
		public String callsAnswered = "0";
		public String callsInACW = "0";
		public String callsTotal = "0";
		
		public String callsAnsweredMaxT = "00:00:00";
		public String callsInProgressMaxT = "00:00:00";
		public String callsACWMaxT = "00:00:00";
		public String callsInQueueMaxT = "00:00:00";
		public String callsRingingMaxT = "00:00:00";
		
		public TreeMap<Integer, ArrayList<String>> agentCalls=new TreeMap<Integer,ArrayList<String>>();
		protected ArrayList<String> currentAgentCallRow=null;
		public void clear(){
			if(currentAgentCallRow!=null){
				while(!currentAgentCallRow.isEmpty()) currentAgentCallRow.remove(0);
				currentAgentCallRow.clear();
				currentAgentCallRow=null;
			}
			this.agentCalls.clear();
			serviceName = "";
			service="";
			serviceType="";
			callsInProgress = callsInQueue = callsRinging = callsPreviewInQueue = callsAnswered = callsInACW = callsTotal = "0";
			callsAnsweredMaxT = callsInProgressMaxT = callsInQueueMaxT = callsRingingMaxT = callsACWMaxT = "00:00:00";
		}
		
		public String toString(){
			return new StringBuilder().append("[ cCallInfo ")
			  			.append("[ ").append("serviceName=").append(serviceName).append(" ]")
						.append("[ ").append("callsInProgress=").append(callsInProgress).append(" ]")
						.append("[ ").append("callsInQueue=").append(callsInQueue).append(" ]")
						.append("[ ").append("callsPreviewInQueue=").append(callsPreviewInQueue).append(" ]")
						.append("[ ").append("callsAnswered=").append(callsAnswered).append(" ]")
						.append("[ ").append("callsInACW=").append(callsInACW).append(" ]")
						.append("[ ").append("callsTotal=").append(callsTotal).append(" ]")
						.append("[ ").append("callsAnsweredMaxT=").append(callsAnsweredMaxT).append(" ]")
						.append("[ ").append("callsInProgressMaxT=").append(callsInProgressMaxT).append(" ]")
						.append("[ ").append("callsACWMaxT=").append(callsACWMaxT).append(" ]")
						.append("[ ").append("callsInQueueMaxT=").append(callsInQueueMaxT).append(" ]")
						.append("[ ").append("callsRingingMaxT=").append(callsRingingMaxT).append(" ]")
						.append("]").toString();
		}
	}
	
	private static int PendingDBRequests = -1;
	private static int licConnAgents = -1;
	private static int licConnAgentsTotal = -1;
	private static int licPredictiveCalls = -1;
	private static int licPredictiveCallsTotal = -1;
	
	private cAgentInfo agentInfo;// = new cAgentInfo();
	private cCallInfo callInfo;// = new cCallInfo();
	private static List<cService> serviceList = new ArrayList<cService>();
	
	public cAgentInfo getAgentInfo(){
		return this.agentInfo;
	}
	public cCallInfo getCallInfo(){
		return this.callInfo;
	}
	public static List<cService> getServiceList(){
		return serviceList;
	}
	
	public void getServiceList(String data){
		String[] messageComponents = data.split("\n");
		boolean bListStarted = false;
		serviceList.clear();
		for (String string : messageComponents) {
			if(bListStarted && !string.contains("ENDTEXT")){
				String[] serviceComponents = string.split(";");
				cService service = new cService();
				service.serviceID = serviceComponents[0];
				service.serviceName = serviceComponents[1];
				service.serviceStatus = serviceComponents[2];
				service.serviceDirection = serviceComponents[3];
				service.serviceType=serviceComponents[3].toUpperCase();
				serviceList.add(service);
			}
			if(string.contains("Id;Name;Status;Type")){
				bListStarted = true;
			}
		}
	}
	
	public void getAgentsConnected(String data,String service,String serviceType){
		boolean bListStarted = false;
		String[] messageComponents = data.split("\n");
		String[] topStats;
		agentInfo = new cAgentInfo();
		agentInfo.clear();
		agentInfo.service=service;
		agentInfo.serviceType=serviceType;
		for (String string : messageComponents) {
			if(string.contains("Message: Agents connected to service")){
				topStats = string.split("#ENTER#");
				for (String string2 : topStats) {
					getAgentsStats(string2);
				}
			}
			else if(bListStarted && !string.contains("ENDTEXT")){
				agentInfo.currentAgentInfoRow=new ArrayList<String>();
				agentInfo.agentsInfoSet.put(agentInfo.agentsInfoSet.size(), agentInfo.currentAgentInfoRow);
				for(String agentInfoItem:string.split("[;]")){
					agentInfo.currentAgentInfoRow.add(agentInfoItem);
				}
				agentInfo.currentAgentInfoRow.add(agentInfo.service);
			}
			else if(!bListStarted&&string.equals("Id;Station;Host name;IP address;Requested calls")){
				bListStarted=true;
				agentInfo.currentAgentInfoRow=new ArrayList<String>();
				agentInfo.agentsInfoSet.put(0, agentInfo.currentAgentInfoRow);
				for(String agentInfoItem:string.split("[;]")){
					if(!(agentInfoItem=agentInfoItem.toUpperCase()).equals("")) agentInfo.currentAgentInfoRow.add((agentInfoItem.equals("ID")?"LOGIN": agentInfoItem.replaceAll(" ", "")));
				}
				agentInfo.currentAgentInfoRow.add("SERVICEID");
			}
		}
		System.out.println("agentInfo : " + agentInfo);
	}
	private void getAgentsStats(String statsString){
		if(statsString.contains("Stopped")){
			agentInfo.agentsStopped = statsString.substring(0,statsString.indexOf(' '));
		} else if(statsString.contains("Available")){
			agentInfo.agentsAvailible = statsString.substring(0,statsString.indexOf(' '));
		} else if(statsString.contains("Talking")){
			agentInfo.agentsInCall = statsString.substring(0,statsString.indexOf(' '));
		} else if(statsString.contains("After-call work")){
			agentInfo.agentsInACW = statsString.substring(0,statsString.indexOf(' '));
		} else if(statsString.contains("Other")){
			agentInfo.agentsInOnter = statsString.substring(0,statsString.indexOf(' '));
		} else if(statsString.contains("agent(s) connected")){
			agentInfo.agentsTotalConnected = statsString.substring(0,statsString.indexOf(' '));
		}
	}
	
	public void getCallsConnected(String data,String service,String serviceType){
		boolean bListStarted = false;
		String[] messageComponents = data.split("\n");
		String[] topStats;
		callInfo = new cCallInfo();
		callInfo.clear();
		callInfo.service=service;
		callInfo.serviceType=serviceType;
		for (String string : messageComponents) {
			if(string.contains("Message: Calls for service")){
				topStats = string.split("#ENTER#");
				for (String string2 : topStats) {
					getCallStats(string2);
				}
			}
			if(bListStarted && !string.contains("ENDTEXT")){
				
				try{
					//here we are working through the list
					String[] agentActivity = string.split(";");
					if(!agentActivity[4].contains("Queued")&&!agentActivity[4].contains("progress")&&!agentActivity[4].contains("VDN/CDN")){
						int itemCount=0;
						callInfo.agentCalls.put(callInfo.agentCalls.size(), callInfo.currentAgentCallRow=new ArrayList<String>());
						for(String agentActivityItem:agentActivity){
							itemCount++;
							if(itemCount==agentActivity.length){
								try{
									callInfo.currentAgentCallRow.add((agentActivityItem.indexOf(" - ")>-1?agentActivityItem.substring(0, agentActivityItem.indexOf(" - ")):agentActivityItem.substring(0, agentActivityItem.lastIndexOf(" "))).trim());
									callInfo.currentAgentCallRow.add(agentActivityItem.substring(agentActivityItem.lastIndexOf(" ")+1).trim());
								}
								catch(Exception es){
									new Exception("agentActivityItem:"+agentActivityItem +":"+es.getMessage()).printStackTrace();
								}
								
							}
							else{
								callInfo.currentAgentCallRow.add(agentActivityItem);
							}
						}
					}
	//				System.out.println(agentActivity[3] + ":" + agentActivity[4]);
					if(agentActivity[4].contains("Answered"))
						if(timeIsBefore(callInfo.callsAnsweredMaxT,agentActivity[3]))
							callInfo.callsAnsweredMaxT = agentActivity[3];
					if(agentActivity[4].contains("After-call work"))
						if(timeIsBefore(callInfo.callsACWMaxT,agentActivity[3]))
							callInfo.callsACWMaxT = agentActivity[3];
					if(agentActivity[4].contains("progress"))
						if(timeIsBefore(callInfo.callsInProgressMaxT,agentActivity[3]))
							callInfo.callsInProgressMaxT = agentActivity[3];
					if(agentActivity[4].contains("Queued"))
						if(timeIsBefore(callInfo.callsInQueueMaxT,agentActivity[3]))
							callInfo.callsInQueueMaxT = agentActivity[3];
					if(agentActivity[4].contains("Ringing"))
						if(timeIsBefore(callInfo.callsRingingMaxT,agentActivity[3]))
							callInfo.callsRingingMaxT = agentActivity[3];
	//				System.out.println(callInfo.callsAnsweredMaxT + " : " 
	//								+ callInfo.callsACWMaxT + " : "
	//								+ callInfo.callsInProgressMaxT + " : ");
				} catch (IndexOutOfBoundsException ex){
					ex.printStackTrace();
				}
			}
			if(string.contains("Id;Call id;Phone;Time;Status")){
				this.callInfo.agentCalls.clear();
				this.callInfo.agentCalls.put(0,this.callInfo.currentAgentCallRow = new ArrayList<String>());
				for(String stritem:string.split("[;]")){
					if(!(stritem=stritem.toUpperCase()).equals("")){
						this.callInfo.currentAgentCallRow.add(stritem.replaceAll(" ", ""));
					}
				}
				if(!this.callInfo.currentAgentCallRow.isEmpty()){
					this.callInfo.currentAgentCallRow.add("LOGIN");
				}
				bListStarted = true;
			}
		}
		//Now work out total calls
		Integer callsTotal = Integer.parseInt(callInfo.callsAnswered) + Integer.parseInt(callInfo.callsInACW) + Integer.parseInt(callInfo.callsInProgress) + Integer.parseInt(callInfo.callsInQueue) + Integer.parseInt(callInfo.callsPreviewInQueue) + Integer.parseInt(callInfo.callsRinging);
		callInfo.callsTotal = callsTotal.toString();
		
		System.out.println("callInfo : " + callInfo);
	}
	private void getCallStats(String statsString){
		statsString = statsString.trim();
		if(statsString.contains("in progress")){
			callInfo.callsInProgress = statsString.substring(0,statsString.indexOf(' '));
		} else if(statsString.contains("preview call(s) in queue")){
			callInfo.callsPreviewInQueue = statsString.substring(0,statsString.indexOf(' '));
		} else if(statsString.contains("call(s) in queue")){
			callInfo.callsInQueue = statsString.substring(0,statsString.indexOf(' '));
		} else if(statsString.contains("answered")){
			callInfo.callsAnswered = statsString.substring(0,statsString.indexOf(' '));
		} else if(statsString.contains("ringing")){
			callInfo.callsRinging = statsString.substring(0,statsString.indexOf(' '));
		} else if(statsString.contains("in after-call work")){
			callInfo.callsInACW = statsString.substring(0,statsString.indexOf(' '));
//		} else if(statsString.contains("agent(s) connected")){
//			agentInfo.agentsTotalConnected = statsString.substring(0,statsString.indexOf(' '));
		}
	}
	
	private static boolean timeIsBefore(String sd1, String sd2) {
		DateFormat f = new SimpleDateFormat("hh:mm:ss");
		try {
			Date d1 = f.parse(sd1);
			Date d2 = f.parse(sd2);
//			DateFormat f = new SimpleDateFormat("HH:mm:ss.SSS");
			return f.format(d1).compareTo(f.format(d2)) < 0;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return false;
	}
	
	public static void getSystemInfo(String data){
		if(getPendingDBRequests(data))
			return;
		if(getPredictiveCalls(data))
			return;
	}
	
	public static boolean getPendingDBRequests(String data){
		if(data.contains("Pending database requests")){
			try{
				PendingDBRequests = Integer.parseInt(data.substring(data.indexOf(":")+1).trim());
			return true;
			} catch (NumberFormatException ex){
				return false;
			}
		}
		return false;
	}
	public static boolean getPredictiveCalls(String data){
		String val;
		if(data.contains("Predictive:")){
			try{
				val = data.substring(data.indexOf(":")+1).trim();
				licPredictiveCalls = Integer.parseInt(val.substring(0,val.indexOf("/")));
				licPredictiveCallsTotal = Integer.parseInt(val.substring(val.indexOf("/")+1,val.length()));
//				licPredictiveCalls = val.substring(0,val.indexOf("/"))
			return true;
			} catch (NumberFormatException ex){
				return false;
			}
		}
		return false;
	}
	public static String print(){
		return new StringBuilder().append("System Info :\n")
								.append("\tPending DB Requests : ").append(PendingDBRequests).append("\n")
								.append("\tPredictive calls : ").append(licPredictiveCalls).append(" of ").append(licPredictiveCallsTotal).append("\n")
								.toString();
	}
}
