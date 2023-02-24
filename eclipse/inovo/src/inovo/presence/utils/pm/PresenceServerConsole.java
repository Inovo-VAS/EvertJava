package inovo.presence.utils.pm;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.TreeMap;

public class PresenceServerConsole {
	public static TreeMap<Integer,ArrayList<Object>> listServices(String presenceServerIP) throws Exception{
		TreeMap<Integer,ArrayList<Object>> listedServicesSet=new TreeMap<Integer,ArrayList<Object>>();
		ByteArrayOutputStream bytesOutServices=new ByteArrayOutputStream();
		Console.executeRequest(new String[]{"LIST","SERVICES"},presenceServerIP, bytesOutServices);
		boolean startLoadingServiceListSet=false;
		ArrayList<Object> setData=null;
		Integer rowSetIndex=0;
		for(String requestLine:bytesOutServices.toString().split("\r\n")){
			if(requestLine.equals("Response: Error")) break;
			if(!startLoadingServiceListSet){
				if((startLoadingServiceListSet=requestLine.equals("Id;Name;Status;Type"))){
					listedServicesSet.put(rowSetIndex++, setData=new ArrayList<Object>());
					for(String dataItem:requestLine.split(";")){
						setData.add(dataItem.toUpperCase());
					}
				}
			}
			else if(startLoadingServiceListSet){
				if(!requestLine.equals(";ENDTEXT;")){
					listedServicesSet.put(rowSetIndex++, setData=new ArrayList<Object>());
					for(String dataItem:requestLine.split(";")){
						setData.add(dataItem.toUpperCase());
					}
				}
			}
		}
		return listedServicesSet;
	}

	public static TreeMap<Integer,ArrayList<Object>> listServiceAgents(String serviceid, String presenceServerIP,ArrayList<String> additionServiceInfo) throws Exception {
		TreeMap<Integer,ArrayList<Object>> listedServiceAgentsSet=new TreeMap<Integer,ArrayList<Object>>();
		ByteArrayOutputStream bytesOutServiceInfo=new ByteArrayOutputStream();
		Console.executeRequest(new String[]{"LIST","AGENTS",serviceid},presenceServerIP, bytesOutServiceInfo);
		boolean startLoadingServiceAgentsListSet=false;
		ArrayList<Object> setData=null;
		Integer rowSetIndex=0;
		for(String requestLine:bytesOutServiceInfo.toString().split("\r\n")){
			if(requestLine.equals("Response: Error")) break;
			if(requestLine.startsWith("Message: ")){
				additionServiceInfo.add("Agents "+requestLine);
			}
			if(!startLoadingServiceAgentsListSet){
				if((startLoadingServiceAgentsListSet=requestLine.equals("Id;Station;Host name;IP address;Requested calls"))){
					listedServiceAgentsSet.put(rowSetIndex++, setData=new ArrayList<Object>());
					String datacol="";
					for(String dataItem:requestLine.split(";")){
						datacol=dataItem.replaceAll(" ", "_").toUpperCase();
						if(datacol.equals("ID")) datacol="LOGIN";
						setData.add(datacol);
					}
				}
			}
			else if(startLoadingServiceAgentsListSet){
				if(!requestLine.equals(";ENDTEXT;")){
					listedServiceAgentsSet.put(rowSetIndex++, setData=new ArrayList<Object>());
					for(String dataItem:requestLine.split(";")){
						setData.add(dataItem.toUpperCase());
					}
				}
			}
		}
		return listedServiceAgentsSet;
	}

	public static TreeMap<Integer, ArrayList<Object>> listServiceAgentCalls(
			String serviceid, String presenceServerIP,
			ArrayList<String> additionServiceInfo) throws Exception {
		TreeMap<Integer,ArrayList<Object>> listedServiceAgentCallsSet=new TreeMap<Integer,ArrayList<Object>>();
		ByteArrayOutputStream bytesOutServiceCallsInfo=new ByteArrayOutputStream();
		Console.executeRequest(new String[]{"LIST","CALLS",serviceid},presenceServerIP, bytesOutServiceCallsInfo);
		boolean startLoadingServiceAgentCallsListSet=false;
		ArrayList<Object> setData=null;
		Integer rowSetIndex=0;
		
		for(String requestLine:bytesOutServiceCallsInfo.toString().split("\r\n")){
			if(requestLine.equals("Response: Error")) break;
			if(requestLine.startsWith("Message: ")){
				additionServiceInfo.add("Calls "+requestLine);
			}
			if(!startLoadingServiceAgentCallsListSet){
				if((startLoadingServiceAgentCallsListSet=requestLine.equals("Id;Call id;Phone;Time;Status"))){
					listedServiceAgentCallsSet.put(rowSetIndex++, setData=new ArrayList<Object>());
					String datacol="";
					for(String dataItem:requestLine.split(";")){
						datacol=dataItem.replaceAll(" ", "_").toUpperCase();
						setData.add(datacol);
					}
					setData.add("LOGIN");
				}
			}
			else if(startLoadingServiceAgentCallsListSet){
				if(!requestLine.equals(";ENDTEXT;")){
					listedServiceAgentCallsSet.put(rowSetIndex++, setData=new ArrayList<Object>());
					String dataRead="";
					for(String dataItem:requestLine.split(";")){
						dataRead=dataItem;
						if(dataRead.indexOf("- Agent")>-1){
							setData.add(dataRead.substring(0,dataRead.indexOf("- Agent")).trim());
						}
						else{
							setData.add(dataItem);
						}
					}
					if(dataRead.indexOf("- Agent")>-1){
						setData.add(dataRead.substring(dataRead.indexOf("- Agent")+"- Agent".length()).trim());
					}
					else{
						setData.add("");
					}
				}
			}
		}
		return listedServiceAgentCallsSet;
	}
}
