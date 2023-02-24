package inovo.presence;
import inovo.db.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class PresenceDB{

 public static TreeMap<Integer, ArrayList<Object>> outboundServices()
   throws Exception
 {
	 TreeMap<Integer, ArrayList<Object>> outboundServices=new TreeMap<Integer, ArrayList<Object>>(); 
	 Database.executeDBRequest(outboundServices,"PRESENCE", "SELECT ID AS SERVICEID,NAME AS SERVICENAME FROM <DBUSER>.PCO_OUTBOUNDSERVICE", null,null);
	 return outboundServices;
 }
 
 public static boolean serviceExist(String serviceid) throws Exception {
	    HashMap<String,Object> loadParams = new HashMap<String,Object>();
	    loadParams.put("SERVICEID", serviceid);
	    loadParams.put("SERVICECOUNT", "0");
	    Database.executeDBRequest(null,"PRESENCE","SELECT COUNT(*) AS SERVICECOUNT FROM <DBUSER>.PCO_OUTBOUNDSERVICE WHERE ID=:SERVICEID", loadParams,null);
	    
	    if(loadParams.get("SERVICECOUNT").equals("0")){
	    	return false;
	    }
	    else{
	    	return true;
	    }
 }
 
 public static boolean serviceLoadExist(String serviceid, String loadid, String loaddescription) throws Exception {
	    if(!serviceExist(serviceid)) return false;
	 	HashMap<String,Object> loadParams = new HashMap<String,Object>();
	    loadParams.put("SERVICEID", serviceid);
	    loadParams.put("LOADID", loadid);
	    loadParams.put("DESCRIPTION", loaddescription);
	    loadParams.put("LOADCOUNT", "0");

	    if ((loadParams.get("LOADID").equals("")) && (loadParams.get("DESCRIPTION")).equals("")) {
	      return false;
	    }
	    	    
	    if (!loadParams.get("LOADID").equals("")) {
	    	Database.executeDBRequest(null,"PRESENCE","SELECT COUNT(*) AS LOADCOUNT FROM <DBUSER>.PCO_LOAD WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID", loadParams,null);
	    }
	    else if (loadParams.get("LOADID").equals("") && !loadParams.get("DESCRIPTION").equals("")) {
	    	Database.executeDBRequest(null,"PRESENCE","SELECT COUNT(*) AS LOADCOUNT FROM <DBUSER>.PCO_LOAD WHERE SERVICEID=:SERVICEID AND DESCRIPTION=:DESCRIPTION", loadParams,null);
	    }
	    else if (!loadParams.get("LOADID").equals("") && !loadParams.get("DESCRIPTION").equals("")) {
	    	Database.executeDBRequest(null,"PRESENCE","SELECT COUNT(*) AS LOADCOUNT FROM <DBUSER>.PCO_LOAD WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID AND DESCRIPTION=:DESCRIPTION", loadParams,null);
	    }
	    return !loadParams.get("LOADCOUNT").equals("0");
	  }

	  public static boolean serviceLoadIsEnabled(String serviceid, String loadid) throws Exception {
	    HashMap<String,Object> loadParams = new HashMap<String,Object>();
	    loadParams.put("SERVICEID", serviceid);
	    loadParams.put("LOADID", loadid);
	    loadParams.put("LOADSTATUS", "R");
	    Database.executeDBRequest(null,"PRESENCE","SELECT STATUS AS LOADSTATUS FROM <DBUSER>.PCO_LOAD WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID", loadParams,null);
	    if (loadParams.get("LOADSTATUS").equals("E")) {
	      return true;
	    }

	    return false;
	  }

	  public static boolean createServiceLoad(String serviceid, String loadid, String loaddescription, boolean loadEnabled, int loadPriority) throws Exception
	  {
		if(!serviceExist(serviceid)) return false;
	    if (serviceLoadExist(serviceid, loadid, loaddescription)) return true;
	    HashMap<String,Object> loadParams = new HashMap<String,Object>();
	    loadParams.put("SERVICEID", serviceid);
	    loadid=(loadid==null?"0":(loadid.equals("")?"0":loadid));
	    loadParams.put("LOADID", loadid);
	    /*if (loadid.equals("")) {
	    	Database.executeDBRequest("PRESENCE","SELECT ISNULL(MAX(LOADID),0)+1 AS LOADID FROM <DBUSER>.PCO_LOAD WHERE SERVICEID=:SERVICEID", loadParams);
	    }*/
	    if (loadEnabled) {
	      loadParams.put("STATUS", "E");
	    }
	    else {
	      loadParams.put("STATUS", "D");
	    }
	    loadParams.put("DESCRIPTION", loaddescription);
	    loadParams.put("PRIORITY", String.valueOf(loadPriority));

	    Database.executeDBRequest(null,"PRESENCE","INSERT INTO <DBUSER>.PCO_LOAD (RDATE,SERVICEID,LOADID,STATUS,DESCRIPTION,RECORDCOUNT,PRIORITYTYPE,PRIORITYVALUE) VALUES(NOW(),:SERVICEID ,CASE :LOADID WHEN 0 THEN (SELECT ISNULL(MAX(LOADID),0)+1 AS LOADID FROM <DBUSER>.PCO_LOAD WHERE SERVICEID=:SERVICEID) ELSE :LOADID END ,:STATUS ,:DESCRIPTION ,0 ,0 ,:PRIORITY )", loadParams,null);
	    return serviceLoadExist(serviceid, loadid, loaddescription);
	  }

	  public static String serviceLoadIDByLoadDescription(String serviceid, String loaddescription) throws Exception {
		HashMap<String,Object> loadParams = new HashMap<String,Object>();
	    loadParams.put("SERVICEID", serviceid);
	    loadParams.put("DESCRIPTION", loaddescription);
	    loadParams.put("LOADIDFOUND", "0");

	    Database.executeDBRequest(null,"PRESENCE","SELECT LOADID AS LOADIDFOUND FROM <DBUSER>.PCO_LOAD WHERE SERVICEID=:SERVICEID AND DESCRIPTION=:DESCRIPTION", loadParams,null);
	    return loadParams.get("LOADIDFOUND").toString();
	  }
	  
	  public static void removePresenceCall(int serviceid,int loadid,long sourceid) throws Exception{
		  Database.executeDBRequest(null,"PRESENCE","DELETE FROM <DBUSER>.[PCO_OUTBOUNDQUEUE] WHERE [SERVICEID]="+String.valueOf(serviceid)+" AND [LOADID]="+String.valueOf(loadid)+" AND [SOURCEID]="+String.valueOf(sourceid),null,null);
	  }
	  
	  public static void updatePresenceCallDetails(HashMap<String, Object> callParams,int serviceid,int loadid,int sourceid) throws Exception
	  {
		  String sqlUpdateCallDetails="";
		  sqlUpdateCallDetails="UPDATE <DBUSER>.[PCO_OUTBOUNDQUEUE] SET ";
		  
		  if(callParams.containsKey("NAME")){
			  sqlUpdateCallDetails+=" NAME=:NAME, ";
		  }
		  
		  if(callParams.containsKey("COMMENTS")){
			  sqlUpdateCallDetails+=" COMMENTS=:COMMENTS, ";
		  }
		  
		  TreeMap<Integer,ArrayList<Object>> datServicePhoneDesc=new TreeMap<Integer, ArrayList<Object>>();
		  Database.executeDBRequest(datServicePhoneDesc, "PRESENCE", "SELECT PHONEDESCCODE FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID="+serviceid+" ORDER BY PHONEDESCCODE", callParams, null);
	    	
	      if(datServicePhoneDesc.size()>1){
	    		for(Integer rowindex:datServicePhoneDesc.keySet()){
	    			if(rowindex==0) continue;
	    			if(callParams.containsKey("PHONE"+String.valueOf(rowindex))){
	    				if(!(callParams.get("PHONE"+String.valueOf(rowindex)).equals("0")&&callParams.get("PHONE"+String.valueOf(rowindex)).equals(""))){
	    					callParams.put("PHONEDESC"+String.valueOf(rowindex),datServicePhoneDesc.get((Integer)rowindex).get(0));
	    				}
	    			}
	    		}
	      }
	    	
	      Database.cleanupDataset(datServicePhoneDesc);
		  
		  if(callParams.containsKey("SCHEDULEDATE")){
			  sqlUpdateCallDetails+="STATUS=(SELECT CASE STATUS WHEN 'E' THEN 2 ELSE 42 END FROM <DBUSER>.PCO_LOAD WHERE SERVICEID="+String.valueOf(serviceid)+" AND LOADID="+String.valueOf(loadid)+"),";
			  sqlUpdateCallDetails+=" SCHEDULEDATE=:SCHEDULEDATE, ";
			  sqlUpdateCallDetails+="DAILYCOUNTER=0,";
			  sqlUpdateCallDetails+="TOTALCOUNTER=0,";
			  sqlUpdateCallDetails+="BUSYSIGNALCOUNTER=0,";
			  sqlUpdateCallDetails+="NOANSWERCOUNTER=0,";
			  sqlUpdateCallDetails+="ANSWERMACHINECOUNTER=0,";
			  sqlUpdateCallDetails+="FAXCOUNTER=0,";
			  sqlUpdateCallDetails+="INVGENREASONCOUNTER=0,";
			  sqlUpdateCallDetails+="LASTQCODE=90,";
			  if(callParams.containsKey("CAPTURINGAGENT")){
				  sqlUpdateCallDetails+=" CAPTURINGAGENT=:CAPTURINGAGENT, ";
			  }
			  else if(callParams.containsKey("AGENTLOGIN")){
				  sqlUpdateCallDetails+=" CAPTURINGAGENT=:AGENTLOGIN, ";
			  }
			  else{
				  sqlUpdateCallDetails+=" CAPTURINGAGENT=NULL, ";
			  }
		  }
		  String phone="";
		  boolean modifiedAnyPhone=false;
		  for(int phonenr=1;phonenr<=10;phonenr++){
			  if(callParams.containsKey("PHONE"+String.valueOf(phonenr))){
				  if(!modifiedAnyPhone) modifiedAnyPhone=true;
				  if(callParams.get("PHONE"+String.valueOf(phonenr)).equals("0")) callParams.put("PHONE"+String.valueOf(phonenr),"");
				  
				  if(callParams.get("PHONE"+String.valueOf(phonenr)).equals("")){
					  sqlUpdateCallDetails+=" PHONE"+String.valueOf(phonenr)+"=NULL, ";
					  //sqlUpdateCallDetails+=" PHONEDESC"+String.valueOf(phonenr)+"=NULL, ";
					  sqlUpdateCallDetails+=" PHONETIMEZONEID"+String.valueOf(phonenr)+"=NULL,";
					  continue;
				  }
				  
				  sqlUpdateCallDetails+=" PHONE"+String.valueOf(phonenr)+"=:PHONE"+String.valueOf(phonenr)+", ";
				  sqlUpdateCallDetails+=" PHONETIMEZONEID"+String.valueOf(phonenr)+"='Presence_Server',";
				  if(phone.equals("")){
					  phone=callParams.get("PHONE"+String.valueOf(phonenr)).toString();  
				  }
				  
				  if(callParams.containsKey("PHONEDESC"+String.valueOf(phonenr))){
					  //sqlUpdateCallDetails+=" PHONEDESC"+String.valueOf(phonenr)+"=ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM="+String.valueOf(phonenr)+"),NULL), ";
				  }
				  else{
					  //sqlUpdateCallDetails+=" PHONEDESC"+String.valueOf(phonenr)+"=NULL, "; 
				  }
			  }
		  }
		  //sqlUpdateCallDetails+=" PHONETIMEZONEID='Presence_Server',";
		  if(modifiedAnyPhone){
			  sqlUpdateCallDetails+=" PHONETIMEZONEID='Presence_Server',";
			  
			  for(int phonenr=1;phonenr<=10;phonenr++){
				  sqlUpdateCallDetails+=" PHONEDESC"+String.valueOf(phonenr)+"=ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID="+String.valueOf(serviceid)+") SERVICEPHONEDESCCODES WHERE RNUM="+String.valueOf(phonenr)+"),NULL), ";
			  }
		  }
		  
		  if(phone.equals("")){
			  sqlUpdateCallDetails+=" PHONE=PHONE ";
		  }
		  else{
			  callParams.put("PHONE", phone);
			  sqlUpdateCallDetails+=" PHONE=:PHONE ";
		  }
		  sqlUpdateCallDetails+=" WHERE SERVICEID="+String.valueOf(serviceid)+" AND LOADID="+String.valueOf(loadid)+" AND SOURCEID="+String.valueOf(sourceid);
		  Database.executeDBRequest(null,"PRESENCE",sqlUpdateCallDetails, callParams,null);
	  }
	  
	  public static void createPresenceCall(HashMap<String, Object> newCallParams) throws Exception
	  {
		 
	    newCallParams.put("SERVICELOADSOURCECOUNT", "0");
	    Database.executeDBRequest(null,"PRESENCE","SELECT COUNT(*) AS SERVICELOADSOURCECOUNT FROM <DBUSER>.PCO_OUTBOUNDQUEUE WHERE SERVICEID IN (SELECT ID FROM <DBUSER>.PCO_OUTBOUNDSERVICE WHERE PCO_OUTBOUNDSERVICE.ID=:SERVICEID) AND SERVICEID=:SERVICEID AND LOADID=:LOADID AND SOURCEID=:SOURCEID", newCallParams,null);

	    
	    
	    if (newCallParams.get("SERVICELOADSOURCECOUNT").equals("0"))
	    {
	    	
	    	/*TreeMap<Integer,ArrayList<String>> datServicePhoneDesc=new TreeMap<Integer, ArrayList<String>>();
	    	Database.executeDBRequest(datServicePhoneDesc, "PRESENCE", "SELECT PHONEDESCCODE FROM PREP.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID ORDER BY PHONEDESCCODE", newCallParams, null);
	    	
	    	if(datServicePhoneDesc.size()>1){
	    		for(Integer rowindex:datServicePhoneDesc.keySet()){
	    			if(rowindex==0) continue;
	    			if(newCallParams.containsKey("PHONE"+String.valueOf(rowindex))){
	    				if(!(newCallParams.get("PHONE"+String.valueOf(rowindex)).equals("0")&&newCallParams.get("PHONE"+String.valueOf(rowindex)).equals(""))){
	    					newCallParams.put("PHONEDESC"+String.valueOf(rowindex),datServicePhoneDesc.get((Integer)rowindex).get(0));
	    				}
	    				else{
	    					newCallParams.put("PHONE"+String.valueOf(rowindex),"");
	    				}
	    			}
	    		}
	    	}
	    	
	    	Database.cleanupDataset(datServicePhoneDesc);*/
	    	
	    	for(Integer rowindex:new int[]{1,2,3,4,5,6,7,8,9,10}){
    			if(rowindex==0) continue;
    			if(newCallParams.containsKey("PHONE"+String.valueOf(rowindex))){
    				if((newCallParams.get("PHONE"+String.valueOf(rowindex)).equals("0")||newCallParams.get("PHONE"+String.valueOf(rowindex)).equals(""))){
    					newCallParams.put("PHONE"+String.valueOf(rowindex),"");
    				}
    			}
    		}
	    	
	      String sqlCallPriority = "";

	      if (newCallParams.containsKey("PRIORITY")) {
	        sqlCallPriority = ":PRIORITY ,";
	      }
	      else {
	        sqlCallPriority = "(SELECT PRIORITYVALUE FROM <DBUSER>.PCO_LOAD WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID),";
	      }

	      String sqlStatusSection = "";
	      if(!newCallParams.containsKey("STATUS")) newCallParams.put("STATUS", "1");
	      if(newCallParams.get("STATUS").equals("1")) newCallParams.put("STATUS", (newCallParams.containsKey("SCHEDULEDATE")? "2": "1"));
	      
	      if ((newCallParams.get("STATUS")).equals("1")) {
	        sqlStatusSection = "(SELECT CASE STATUS WHEN 'E' THEN 1 ELSE 41 END FROM <DBUSER>.PCO_LOAD WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID),";
	      }
	      else if ((newCallParams.get("STATUS")).equals("2")) {
	        sqlStatusSection = "(SELECT CASE STATUS WHEN 'E' THEN 2 ELSE 42 END FROM <DBUSER>.PCO_LOAD WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID),";
	      }

	      if(serviceExist(newCallParams.get("SERVICEID").toString())){
		      String sqlNewCallStatement = "INSERT INTO <DBUSER>.PCO_OUTBOUNDQUEUE (SERVICEID,NAME,PHONE,SOURCEID,STATUS,LOADID,SCHEDULEDATE,DAILYCOUNTER,TOTALCOUNTER,BUSYSIGNALCOUNTER,NOANSWERCOUNTER,ANSWERMACHINECOUNTER,FAXCOUNTER,INVGENREASONCOUNTER,PRIORITY,PHONE1,PHONE2,PHONE3,PHONE4,PHONE5,PHONE6,PHONE7,PHONE8,PHONE9,PHONE10,PHONETIMEZONEID,PHONETIMEZONEID1,PHONETIMEZONEID2,PHONETIMEZONEID3,PHONETIMEZONEID4,PHONETIMEZONEID5,PHONETIMEZONEID6,PHONETIMEZONEID7,PHONETIMEZONEID8,PHONETIMEZONEID9,PHONETIMEZONEID10,PHONEDESC1,PHONEDESC2,PHONEDESC3,PHONEDESC4,PHONEDESC5,PHONEDESC6,PHONEDESC7,PHONEDESC8,PHONEDESC9,PHONEDESC10,PHONESTATUS1,CAPTURINGAGENT,COMMENTS,CURRENTPHONECOUNTER) SELECT :SERVICEID ,:NAME ,:PHONE ,:SOURCEID ," + 
		        sqlStatusSection + 
		        ":LOADID ," + 
		        (newCallParams.containsKey("SCHEDULEDATE")? ":SCHEDULEDATE ,": " NULL,") +
		        (newCallParams.containsKey("DAILYCOUNTER")? ":DAILYCOUNTER ,": "0,") + 
		        (newCallParams.containsKey("TOTALCOUNTER")? ":TOTALCOUNTER ,": "0,") + 
		        (newCallParams.containsKey("BUSYSIGNALCOUNTER")? ":BUSYSIGNALCOUNTER ,": "0,") + 
		        (newCallParams.containsKey("NOANSWERCOUNTER")? ":NOANSWERCOUNTER ,": "0,") + 
		        (newCallParams.containsKey("ANSWERMACHINECOUNTER")? ":ANSWERMACHINECOUNTER ,": "0,") + 
		        (newCallParams.containsKey("FAXCOUNTER")? ":FAXCOUNTER ,": "0,") + 
		        (newCallParams.containsKey("INVGENREASONCOUNTER")? ":INVGENREASONCOUNTER ,": "0,") + 
		        sqlCallPriority + 
		        //":PHONE1 ," +
		        (newCallParams.containsKey("PHONE1")?newCallParams.get("PHONE1").equals("")?"'',":":PHONE1 ,": "'',") +
		        (newCallParams.containsKey("PHONE2")?newCallParams.get("PHONE2").equals("")?"'',":":PHONE2 ,": "'',") + 
		        (newCallParams.containsKey("PHONE3")?newCallParams.get("PHONE3").equals("")?"'',":":PHONE3 ,": "'',") + 
		        (newCallParams.containsKey("PHONE4")?newCallParams.get("PHONE4").equals("")?"'',":":PHONE4 ,": "'',") + 
		        (newCallParams.containsKey("PHONE5")?newCallParams.get("PHONE5").equals("")?"'',":":PHONE5 ,": "'',") + 
		        (newCallParams.containsKey("PHONE6")?newCallParams.get("PHONE6").equals("")?"'',":":PHONE6 ,": "'',") + 
		        (newCallParams.containsKey("PHONE7")?newCallParams.get("PHONE7").equals("")?"'',":":PHONE7 ,": "'',") + 
		        (newCallParams.containsKey("PHONE8")?newCallParams.get("PHONE8").equals("")?"'',":":PHONE8 ,": "'',") + 
		        (newCallParams.containsKey("PHONE9")?newCallParams.get("PHONE9").equals("")?"'',":":PHONE9 ,": "'',") + 
		        (newCallParams.containsKey("PHONE10")?newCallParams.get("PHONE10").equals("")?"'',":":PHONE10 ,": "'',") + 
		        //Presence_Server
		        "'Presence_Server',"+
		        (newCallParams.containsKey("PHONE1")?newCallParams.get("PHONE1").equals("")?"'',":"'Presence_Server' ,": "'',") +
		        (newCallParams.containsKey("PHONE2")?newCallParams.get("PHONE2").equals("")?"'',":"'Presence_Server' ,": "'',") + 
		        (newCallParams.containsKey("PHONE3")?newCallParams.get("PHONE3").equals("")?"'',":"'Presence_Server' ,": "'',") + 
		        (newCallParams.containsKey("PHONE4")?newCallParams.get("PHONE4").equals("")?"'',":"'Presence_Server' ,": "'',") + 
		        (newCallParams.containsKey("PHONE5")?newCallParams.get("PHONE5").equals("")?"'',":"'Presence_Server' ,": "'',") + 
		        (newCallParams.containsKey("PHONE6")?newCallParams.get("PHONE6").equals("")?"'',":"'Presence_Server' ,": "'',") + 
		        (newCallParams.containsKey("PHONE7")?newCallParams.get("PHONE7").equals("")?"'',":"'Presence_Server' ,": "'',") + 
		        (newCallParams.containsKey("PHONE8")?newCallParams.get("PHONE8").equals("")?"'',":"'Presence_Server' ,": "'',") + 
		        (newCallParams.containsKey("PHONE9")?newCallParams.get("PHONE9").equals("")?"'',":"'Presence_Server' ,": "'',") + 
		        (newCallParams.containsKey("PHONE10")?newCallParams.get("PHONE10").equals("")?"'',":"'Presence_Server' ,": "'',") +
		        "ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=1),NULL)," +
		        "ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=2),NULL)," +
		        "ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=3),NULL)," +
		        "ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=4),NULL)," +
		        "ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=5),NULL)," +
		        "ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=6),NULL)," +
		        "ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=7),NULL)," +
		        "ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=8),NULL)," +
		        "ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=9),NULL)," +
		        "ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=10),NULL)," +
		        
		        /*(newCallParams.get("PHONE1").equals("")?"NULL,":"ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=1),NULL),") +
		        (newCallParams.get("PHONE2").equals("")?"NULL,":"ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=2),NULL),") +
		        (newCallParams.get("PHONE3").equals("")?"NULL,":"ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=3),NULL),") +
		        (newCallParams.get("PHONE4").equals("")?"NULL,":"ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>P.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=4),NULL),") +
		        (newCallParams.get("PHONE5").equals("")?"NULL,":"ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=5),NULL),") +
		        (newCallParams.get("PHONE6").equals("")?"NULL,":"ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=6),NULL),") +
		        (newCallParams.get("PHONE7").equals("")?"NULL,":"ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=7),NULL),") +
		        (newCallParams.get("PHONE8").equals("")?"NULL,":"ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=8),NULL),") +
		        (newCallParams.get("PHONE9").equals("")?"NULL,":"ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>P.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=9),NULL),") +
		        (newCallParams.get("PHONE10").equals("")?"NULL,":"ISNULL((SELECT ISNULL(PHONEDESCCODE,NULL) FROM (SELECT row_number() OVER (ORDER BY (SELECT 0)) AS RNUM,PHONEDESCCODE,SERVICEID FROM <DBUSER>.PCO_SERVICEPHONEDESCRIPTION WHERE SERVICEID=:SERVICEID) SERVICEPHONEDESCCODES WHERE RNUM=10),NULL),") +*/
		        
		        (newCallParams.containsKey("PHONESTATUS1")? ":PHONESTATUS1 ,": "0,") + 
		        (newCallParams.containsKey("CAPTURINGAGENT")? ":CAPTURINGAGENT, ": "NULL, ")+
		        (newCallParams.containsKey("COMMENTS")? ":COMMENTS, ": "'', ")+
		      (newCallParams.containsKey("CURRENTPHONECOUNTER")? ":CURRENTPHONECOUNTER ": "0 ");
		      Database.executeDBRequest(null,"PRESENCE",sqlNewCallStatement, newCallParams,null);
	      }
	      else{
	    	 throw new Exception("ERROR: SERVICE [SERVICEID-+"+newCallParams.get("SERVICEID")+"] DOES NOT EXIST");
	      }
	    }
	  }

}
