package inovo.message.gateway;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import inovo.web.InovoWebWidget;

public class Messaging extends InovoWebWidget {

	private ByteArrayOutputStream _bytesoutMessageReceived = new ByteArrayOutputStream();
	
	public Messaging(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}
	
	private String formattedNumber(char[] stringToNumber){
		String numberToReturn="";
		for(char cn:stringToNumber){
			if("0123456789".indexOf((cn+""))>-1){
				numberToReturn+=(cn+"");
			}
		}
		return numberToReturn;
	}
	
	@Override
	public void executeContentWidget() throws Exception {
		
		HashMap<String,String> messageParams=new HashMap<String,String>();
		this.importRequestParametersIntoMap(messageParams, null);
		
		ArrayList<String> messageParamNamesFound=new ArrayList<String>();
	
		boolean paramAreBlank=true;
		boolean cellNumberBlank=true;
		ArrayList<String> messageBlankParamNamesFound=new ArrayList<String>();
		ArrayList<String> cellnumbers=new ArrayList<String>();
		ArrayList<String> cellnumberNames=new ArrayList<String>();
		int bankCellNumberCount=0;
		for(String paramName:messageParams.keySet()){
			 if(paramName.startsWith("CELLNUMBER")){
				 cellnumbers.add(messageParams.get(paramName));
				 cellnumberNames.add(paramName);
				 if(messageParams.get(paramName).equals("")) bankCellNumberCount++; 
			 }
			 
			 if(paramAreBlank){
				 paramAreBlank=messageParams.get(paramName).equals("");
			 }
			 if(messageParams.get(paramName).equals("")){
				 messageBlankParamNamesFound.add(paramName);
			 }
			messageParamNamesFound.add(paramName);
		}
		
		cellNumberBlank=(bankCellNumberCount==cellnumbers.size());
		
		this.setRequestParameter("RECEIVEDDATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()), true);
		this.setRequestParameter("KEYPHRASE", requestParameter("KEYWORD", "N/A"), true);
		
		if(messageParams.containsKey("FIRSTNAME")){
			this.setRequestParameter("NAME", requestParameter("FIRSTNAME"), true);
		}
		else if(messageParams.containsKey("NAME")){
			this.setRequestParameter("NAME", requestParameter("NAME"), true);
		}
		
		this.setRequestParameter("NAME", requestParameter("NAME"), true);
		
		int cellnumberindex=0;
		if(!cellnumbers.isEmpty()){
			this.setRequestParameter("PHONENR",cellnumbers.get(cellnumbers.size()-1),true);
		}
		
		
		this.setRequestParameter("IDNUMBER", requestParameter("IDNUMBER"),true);
		this.setRequestParameter("POSTALCODE", requestParameter("POSTALCODE"),true);
		this.setRequestParameter("EMAIL", requestParameter("EMAIL"),true);
		this.setRequestParameter("SOURCE", requestParameter("SOURCE"),true);
		
		this.importRequestParametersIntoMap(messageParams, null);
		if(messageParams.containsKey("LEADTYPE")){
			String leadType=messageParams.get("LEADTYPE");
			messageParams.remove("LEADTYPE");
			messageParams.put("LEADTYPE2", leadType);
		}
		
		for(String paramNameToRemove:"CELLNUMBER,KEYWORD,FIRSTNAME".split("[,]")){
			if(messageParams.containsKey(paramNameToRemove)){
				messageParams.remove(paramNameToRemove);
				messageParams.keySet().remove(paramNameToRemove);
			}
		}
		
		String debugParametersInfo="";
		if(this.requestParameter("DEBUG").toLowerCase().equals("show-params")){
			for(String paramName:messageParams.keySet()){
				this.respondString(paramName+"="+messageParams.get(paramName)+", ");
			}
			this.respondString("\r\n");
			for(String cellNumber:cellnumbers){
				this.respondString(cellNumber+", ");
			}
		}
		
		String receivedMessage = "";
		/*receivedMessage = receivedMessage + "RECEIVEDDATETIME=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()) + "\r\n";
	    receivedMessage = receivedMessage + "KEYPHRASE=" + requestParameter("KEYWORD", "N/A") + "\r\n";
	    receivedMessage = receivedMessage + "NAME=" + requestParameter("FIRSTNAME") + "\r\n";
	    receivedMessage = receivedMessage + "PHONENR=" + requestParameter("CELLNUMBER") + "\r\n";
	    receivedMessage = receivedMessage + "IDNUMBER=" + requestParameter("IDNUMBER") + "\r\n";
	    receivedMessage = receivedMessage + "POSTALCODE=" + requestParameter("POSTALCODE") + "\r\n";
	    receivedMessage = receivedMessage + "EMAIL=" + requestParameter("EMAIL") + "\r\n";
	    receivedMessage = receivedMessage + "SOURCE=" + requestParameter("SOURCE");*/
		this.setResponseHeader("CONTENT-TYPE", "text/plain");
	    respondString("[INOVO MESSAGING GATEWAY] Reached at : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())+"\r\n");
	    
	    try {
	    	MessagingQueue.messagingQueue().addNextMessageParams(messageParams);
	    	
		    if(messageParamNamesFound.isEmpty()){
		    	respondString("ERROR: NO PARAMETERS SENT\r\n");
		    }
		    else{
		    	if(paramAreBlank){
		    		respondString("ERROR: BLANK PARAMETERS SENT\r\n");
		    	}
		    	if(cellNumberBlank){
		    		respondString("ERROR: AT LEAST CELLNUMBER [PARAMETER] MUST BE SENT\r\n");
		    	}
		    	else{
		    		
		    		cellnumberindex=0;
		    		for(String cellNumber:cellnumbers){
		    			String actualCellNumber=formattedNumber(cellNumber.toCharArray());
		    			cellnumberindex++;
			    		if(actualCellNumber.length()<10){
			    			respondString("ERROR: "+cellnumberNames.get(cellnumberindex-1)+" [PARAMETER] INVALID [SENT:"+cellNumber+" - FORMATTED:"+actualCellNumber+"]\r\n");
			    		}
		    		}
		    	}
		    	respondString("LIST OF ALL PARAMATER NAMES RECEIVED:\r\n");
		    	while(!messageParamNamesFound.isEmpty()) respondString(messageParamNamesFound.remove(0).toUpperCase()+";");
		    	if(!messageBlankParamNamesFound.isEmpty()){
			    	respondString("\r\nLIST OF BLANK PARAMATERS RECEIVED:\r\n");
			    	while(!messageBlankParamNamesFound.isEmpty()) respondString(messageBlankParamNamesFound.remove(0).toUpperCase()+";");
		    	}
		    }
		    MessageFailureLoggingQueue.tryLoadingFailures();
	    }
	    catch (Exception e) {
	    	respondString("ERROR: SYSTEM FAILURE - NO REQUEST LOGGED\r\n");
	    	e.printStackTrace();
	    	MessageFailureLoggingQueue.logFailureMessage(messageParams);
	    }
	}
	
	@Override
	public void cleanUpWidget() {
		this._bytesoutMessageReceived.reset();
		this._bytesoutMessageReceived=null;
		super.cleanUpWidget();
	}

}
