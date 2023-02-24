package inovo.presence.respin;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import presence.Administrator;
import inovo.db.Database;
import inovo.servlet.InovoServletContextListener;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class Respin extends InovoHTMLPageWidget {

	public Respin(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

	@Override
	public void pageContent() throws Exception {
		this.startScript();
			this.respondString("function checkCheckBoxes(selectnamespace){$(selectnamespace).prop('checked', true);}\r\n");
			this.respondString("function uncheckCheckBoxes(selectnamespace){$(selectnamespace).prop('checked', false);}\r\n");
		this.endScript();
		this.startElement("div", "id=respinpage".split("[|]"), true);
			this.respinPage();
		this.endElement("div", true);
	}
	
	public void respinPage() throws Exception{
		ArrayList<String[]> respinActions=new ArrayList<String[]>();
		respinActions.add("caption=RESPIN SELECTION|COMMAND=respin_selection".split("[|]"));
		this.actions_tabs(respinActions, "respinactions", false);
	}
	
	private TreeMap<Object,String> _selectableCampaigns=null;
	
	public void respin_selection() throws Exception{
		this.startElement("div", "class=ui-widget-header".split("[|]"), true);
			this.respondString("RESPIN SELECTION");
		this.endElement("div", true);
		this.startTable();
			this.startRow();
				this.startColumn("");
					this.respondString("RESPINABLE CAMPAIGN(s)");
				this.endColumn();
			this.endRow();
			this.startRow();
				this.startCell();
					this.startTable();
						this.startRow();
							this.startCell();
								this.action("SELECT CAMPAIGN", "select_respin_campaign", "", "", "", "", "", "");
							this.endCell();
							this.startCell();
							
								
							_selectableCampaigns=new TreeMap<Object, String>();
								
								Database.executeDBRequest(null,"PRESENCE-VIEW","SELECT ID,NAME FROM <DBUSER>.SERVICE WHERE ID IN (SELECT DISTINCT SERVICEID FROM <DBUSER>.OUTBOUNDQUEUE) ORDER BY SERVICE.NAME",null,this,"rowAvailableOutboundCampaigns");
								
								this.fieldInput("SERVICE_TO_RESPIN", "", "select", true, null, _selectableCampaigns);
							this.endCell();
						this.endRow();
					this.endTable();
					this.startElement("div", "id=selectedcampaignsettings".split("[|]"), true);this.endElement("div", true);
				this.endCell();
			this.endRow();
		this.endTable();
		
	}
	
	public void rowAvailableOutboundCampaigns(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns) throws Exception{
		if(rowIndex>0){
			_selectableCampaigns.put(rowData.get(0),"("+rowData.get(0)+") "+rowData.get(1));
		}
	}
	
	public void select_respin_campaign() throws Exception{
		String campaignToRespin=this.requestParameter("SERVICE_TO_RESPIN");
		if(campaignToRespin.equals("")){
			this.showDialog("", "title=SELECT CAMPAIGN|contentid=dlgselect_respin_campaignfail".split("[|]"));
			this.replaceComponentContent("dlgselect_respin_campaignfail");
				this.respondString("NO CAMPAIGN SELECTED");
			this.endReplaceComponentContent();
		}
		else{
			this.replaceComponentContent("selectedcampaignsettings");
				this.selectCampaignQCodes();
			this.endReplaceComponentContent();
		}
	}

	public void selectCampaignQCodes() throws Exception{
		this.startTable();
			this.startRow();
				this.startColumn("");
					this.respondString("SYSTEM QCODE(s)");
				this.endColumn();
				this.startColumn("");
					this.respondString("BUSINESS QCODE(s)");
				this.endColumn();
			this.endRow();
			this.startRow();
				this.startCell("class=ui-widget-content|style=vertical-align:top".split("[|]"));
					this.selectCampaignSystemQCodes();
				this.endCell();
				this.startCell("class=ui-widget-content|style=vertical-align:top".split("[|]"));
					this.selectCampaignBusinessQCodes();
				this.endCell();
			this.endRow();
		this.endTable();
		this.action("ACTIVATE RESPIN", "activate_respin", "", "", "", "", "", "");
	}
	
	public void activate_respin() throws Exception{
		ArrayList<String> CAMPAIGNQCODE=this.requestParameterArray("CAMPAIGNQCODE");
		if(CAMPAIGNQCODE.isEmpty()){
			this.showDialog("", "title=SELECT CAMPAIGN QCODE(s)|contentid=dlgselect_respin_campaignfail".split("[|]"));
			this.replaceComponentContent("dlgselect_respin_campaignfail");
				this.respondString("NO CAMPAIGN QUALIFICATION CODE SELECTED");
			this.endReplaceComponentContent();
		}
		else{
			this.replaceComponentContent("selectedcampaignsettings");
				while(!CAMPAIGNQCODE.isEmpty()){
					this.fieldHidden("CAMPAIGNQCODETORESPIN", CAMPAIGNQCODE.remove(0));
				}
				this.startTable();
					this.startRow();
						this.startColumn("");
							this.respondString("SELECT LOAD(s) TO RESPINS");
						this.endColumn();
					this.endRow();
					this.startRow();
						this.startCell("class=ui-widget-content|style=vertical-align:top".split("[|]"));
						Database.executeDBRequest(null, "PRESENCE", "SELECT LOADID,DESCRIPTION,STATUS FROM <DBUSER>.PCO_LOAD WHERE SERVICEID="+this.requestParameter("SERVICE_TO_RESPIN"), null, this, "rowActiveLoads");
							this.constructCampaignLoadsView();
						this.endCell();
					this.endRow();
				this.endTable();
				this.startTable();
					this.startRow();
						this.startCell("style=vertical-align:top".split("[|]"));
							this.action("RESPIN LOAD(s)", "respin_loads", "", "", "", "", "", "");
						this.endCell();
						this.startCell("id=confirmrespin|style=vertical-align:top".split("[|]"));this.endCell();
					this.endRow();
			this.endReplaceComponentContent();
		}
	}
	
	public void respin_loads() throws Exception{
		ArrayList<String> CAMPAIGNLOAD=this.requestParameterArray("CAMPAIGNLOAD");
		if(CAMPAIGNLOAD.isEmpty()){
			this.showDialog("", "title=SELECT CAMPAIGN LOAD(s)|contentid=dlgselect_respin_campaignfail".split("[|]"));
			this.replaceComponentContent("dlgselect_respin_campaignfail");
				this.respondString("NO CAMPAIGN QUALIFICATION LOAD SELECTED");
			this.endReplaceComponentContent();
		}
		else{
			this.replaceComponentContent("confirmrespin");
				this.confirmRespinView();
			this.endReplaceComponentContent();
		}
	}
	
	public void confirmRespinView() throws Exception{
		HashMap<String,String> respinParams=new HashMap<String, String>();
		String RESPINREQUESTID=this.requestParameter("RESPINREQUESTID");
		respinParams.put("RESPINREQUESTID", (RESPINREQUESTID=(RESPINREQUESTID.equals("")?"0":RESPINREQUESTID.equals("0")?"0":RESPINREQUESTID)));
		respinParams.put("RESPINSERVICEID", this.requestParameter("SERVICE_TO_RESPIN"));
		
		respinParams.put("RESPINREQUESTORIP", this.requestHeader("REMOTE-HOST"));
		
		if(RESPINREQUESTID.equals("0")){
			respinParams.put("RESPINSERVICENAME", "");
			
			Database.executeDBRequest(null, "PRESENCE-VIEW", "SELECT NAME AS RESPINSERVICENAME FROM <DBUSER>.SERVICE WHERE ID=:RESPINSERVICEID", respinParams, null);
			
			Database.executeDBRequest(null, "RESPIN","INSERT INTO <DBUSER>.RESPIN_SERVICE_REQUEST (REQUESTORIP,SERVICEID,SERVICENAME) SELECT :RESPINREQUESTORIP,:RESPINSERVICEID,:RESPINSERVICENAME", respinParams, null);
			
			Database.executeDBRequest(null, "RESPIN","SELECT MAX(ID) AS RESPINREQUESTID FROM <DBUSER>.RESPIN_SERVICE_REQUEST WHERE REQUESTORIP=:RESPINREQUESTORIP AND SERVICEID=:RESPINSERVICEID", respinParams, null);
			
			RESPINREQUESTID=respinParams.get("RESPINREQUESTID");
		}
		
		this.fieldHidden("RESPINREQUESTID", RESPINREQUESTID);
		
		respinParams.put("RECORDSAFFECTED", "0");
		
		String CAMPAIGNQCODES="";
		ArrayList<String> campaignQCodes=this.requestParameterArray("CAMPAIGNQCODETORESPIN");
		while(!campaignQCodes.isEmpty()){
			CAMPAIGNQCODES+=(campaignQCodes.remove(0)+(campaignQCodes.isEmpty()?"":","));
		}
		
		String CAMPAIGNLOADS="";
		ArrayList<String> campaignLoads=this.requestParameterArray("CAMPAIGNLOAD");
		while(!campaignLoads.isEmpty()){
			CAMPAIGNLOADS+=(campaignLoads.remove(0)+(campaignLoads.isEmpty()?"":","));
		}
		
		if(!CAMPAIGNLOADS.equals("")&&!CAMPAIGNQCODES.equals("")){
			Database.executeDBRequest(null, "PRESENCE", "SELECT COUNT(*) AS RECORDSAFFECTED FROM <DBUSER>.PCO_OUTBOUNDQUEUE WHERE SERVICEID="+this.requestParameter("SERVICE_TO_RESPIN")+" AND LOADID IN ("+CAMPAIGNLOADS+") AND LASTQCODE IN ("+CAMPAIGNQCODES+") AND NOT (DAILYCOUNTER=0 AND TOTALCOUNTER=0 AND BUSYSIGNALCOUNTER=0 AND NOANSWERCOUNTER=0 AND ANSWERMACHINECOUNTER=0 AND FAXCOUNTER=0 AND INVGENREASONCOUNTER=0 AND STATUS IN (1,41))",respinParams,null);
			InovoServletContextListener.inovoServletListener().logDebug("confirmRespinView()["+this.requestParameter("REMOTE-HOST")+"]->SELECT COUNT(*) AS RECORDSAFFECTED FROM <DBUSER>.PCO_OUTBOUNDQUEUE WHERE SERVICEID="+this.requestParameter("SERVICE_TO_RESPIN")+" AND LOADID IN ("+CAMPAIGNLOADS+") AND LASTQCODE IN ("+CAMPAIGNQCODES+") AND NOT (DAILYCOUNTER=0 AND TOTALCOUNTER=0 AND BUSYSIGNALCOUNTER=0 AND NOANSWERCOUNTER=0 AND ANSWERMACHINECOUNTER=0 AND FAXCOUNTER=0 AND INVGENREASONCOUNTER=0 AND STATUS IN (1,41))");
			
			respinParams.put("RECORDSNOTAFFECTED", "0");
			Database.executeDBRequest(null, "PRESENCE", "SELECT COUNT(*) AS RECORDSNOTAFFECTED FROM <DBUSER>.PCO_OUTBOUNDQUEUE WHERE SERVICEID="+this.requestParameter("SERVICE_TO_RESPIN")+" AND LOADID IN ("+CAMPAIGNLOADS+") AND LASTQCODE IN ("+CAMPAIGNQCODES+") AND ((DAILYCOUNTER=0 AND TOTALCOUNTER=0 AND BUSYSIGNALCOUNTER=0 AND NOANSWERCOUNTER=0 AND ANSWERMACHINECOUNTER=0 AND FAXCOUNTER=0 AND INVGENREASONCOUNTER=0) AND STATUS IN (1,41))",respinParams,null);
			InovoServletContextListener.inovoServletListener().logDebug("confirmRespinView()["+this.requestParameter("REMOTE-HOST")+"]->SELECT COUNT(*) AS RECORDSNOTAFFECTED FROM <DBUSER>.PCO_OUTBOUNDQUEUE WHERE SERVICEID="+this.requestParameter("SERVICE_TO_RESPIN")+" AND LOADID IN ("+CAMPAIGNLOADS+") AND LASTQCODE IN ("+CAMPAIGNQCODES+") AND (DAILYCOUNTER=0 AND TOTALCOUNTER=0 AND BUSYSIGNALCOUNTER=0 AND NOANSWERCOUNTER=0 AND ANSWERMACHINECOUNTER=0 AND FAXCOUNTER=0 AND INVGENREASONCOUNTER=0 AND STATUS IN (1,41))");
		}
		this.startTable();
			this.startRow();
				this.startCell();
					this.respondString(respinParams.get("RECORDSAFFECTED") +" record(s) to be affected by respin");
					this.fieldHidden("RECORDSAFFECTED", respinParams.get("RECORDSAFFECTED"));
				this.endCell();
			this.endRow();
			this.startRow();
				this.startCell();
					this.respondString(respinParams.get("RECORDSNOTAFFECTED") +" record(s) not affected by respin");
					this.fieldHidden("RECORDSNOTAFFECTED", respinParams.get("RECORDSNOTAFFECTED"));
				this.endCell();
			this.endRow();
			if(!respinParams.get("RECORDSAFFECTED").equals("0")){
				this.startRow();
					this.startCell();
						int seconds=Integer.parseInt(respinParams.get("RECORDSAFFECTED"));
						seconds=seconds / 100;
						this.respondString("Estimated time to execute respin - " +String.valueOf((seconds / 60)) + " minute(s) and "+String.valueOf((seconds==0?1:seconds % 60))+" second(s)");
					this.endCell();
				this.endRow();
			}
		this.endTable();
		if(!respinParams.get("RECORDSAFFECTED").equals("0")){
			this.action("CONFIRM RESPIN", "execute_respin", "", "", "", "", "", "");
		}
		this.action("CANCEL RESPIN", "cancel_respin", "", "", "", "", "", "");
	}
	
	private TreeMap<Integer,String> _presenceServiceLoadsToRespin=new TreeMap<Integer, String>();
	private TreeMap<Integer,String> _presenceServiceQCodesToRespin=new TreeMap<Integer, String>();
	
	private TreeMap<Integer,ArrayList<String>[]> _presenceServiceQueueEntriesToRespin=new TreeMap<Integer, ArrayList<String>[]>();
	public void execute_respin() throws Exception{
		String RESPINREQUESTID=this.requestParameter("RESPINREQUESTID");
		if(!RESPINREQUESTID.equals("")){
			String affectedCount="0";
			ArrayList<String> loadsToRespin=this.requestParameterArray("CAMPAIGNLOAD");
			Database.executeDBRequest(null, "RESPIN", "UPDATE <DBUSER>.RESPIN_SERVICE_REQUEST SET RESPINCONFIRMATIONLABEL='RESPIN-STARTED',REQUESTTARTSTAMP=GETDATE() WHERE ID="+RESPINREQUESTID,null,null);
			if(!loadsToRespin.isEmpty()){
				String loadIdsToRespin="";
				while(!loadsToRespin.isEmpty()){
					loadIdsToRespin+=(loadsToRespin.remove(0)+(loadsToRespin.isEmpty()?"":","));
				}
				String serviceid=this.requestParameter("SERVICE_TO_RESPIN");
				
				Database.executeDBRequest(null, "PRESENCE", "SELECT LOADID,DESCRIPTION FROM <DBUSER>.PCO_LOAD WHERE SERVICEID="+this.requestParameter("SERVICE_TO_RESPIN")+" AND LOADID IN ("+loadIdsToRespin+")", null,this,"rowDataPresenceLoads");
				
				HashMap<String,String> respinAdhocParams=new HashMap<String, String>();
				
				while(!_presenceServiceLoadsToRespin.isEmpty()){
					respinAdhocParams.put("LOADID", _presenceServiceLoadsToRespin.firstKey().toString());
					respinAdhocParams.put("LOADNAME", _presenceServiceLoadsToRespin.remove(_presenceServiceLoadsToRespin.firstKey()));
					Database.executeDBRequest(null, "RESPIN", "INSERT INTO <DBUSER>.RESPIN_SERVICE_REQUEST_LOADS (RESPINREQUESTID,SERVICEID,LOADID,LOADNAME) SELECT "+RESPINREQUESTID+","+serviceid+",:LOADID,:LOADNAME",respinAdhocParams,null);
				}
				
				ArrayList<String> qcodesToRespin=this.requestParameterArray("CAMPAIGNQCODETORESPIN");
				if(!qcodesToRespin.isEmpty()){
					
					
					String qcodesBeingRespined="";
					while(!qcodesToRespin.isEmpty()){
						qcodesBeingRespined+=(qcodesToRespin.remove(0)+(qcodesToRespin.isEmpty()?"":","));
					}
					
					Database.executeDBRequest(null, "PRESENCE-VIEW", "SELECT QCODE,DESCRIPTION FROM <DBUSER>.SERVICEQCODE WHERE SERVICEID="+this.requestParameter("SERVICE_TO_RESPIN")+" AND QCODE IN ("+qcodesBeingRespined+")", null,this,"rowDataPresenceQCodes");
					
					while(!_presenceServiceQCodesToRespin.isEmpty()){
						Integer qcode=_presenceServiceQCodesToRespin.firstKey();
						respinAdhocParams.put("QCODE", qcode.toString());
						respinAdhocParams.put("QCODEDESCRIPTION",_presenceServiceQCodesToRespin.remove(_presenceServiceQCodesToRespin.firstKey()));
						Database.executeDBRequest(null, "RESPIN", "INSERT INTO <DBUSER>.RESPIN_SERVICE_REQUEST_QCODES (RESPINREQUESTID,SERVICEID,QCODE,QCODEDESCRIPTION) SELECT "+RESPINREQUESTID+","+serviceid+",:QCODE,:QCODEDESCRIPTION",respinAdhocParams,null);
					}
					
					Database.executeDBRequest(null, "PRESENCE", "SELECT ID,SERVICEID,LOADID,SOURCEID,LASTQCODE FROM <DBUSER>.PCO_OUTBOUNDQUEUE WHERE SERVICEID="+this.requestParameter("SERVICE_TO_RESPIN")+" AND LOADID IN ("+loadIdsToRespin+") AND LASTQCODE IN ("+qcodesBeingRespined+") AND NOT (DAILYCOUNTER=0 AND TOTALCOUNTER=0 AND BUSYSIGNALCOUNTER=0 AND NOANSWERCOUNTER=0 AND ANSWERMACHINECOUNTER=0 AND FAXCOUNTER=0 AND INVGENREASONCOUNTER=0 AND STATUS IN (1,41)) ORDER BY SERVICEID,LOADID",null,this,"rowDataPresenceQueueEntriesToRespin");
					
					
					ArrayList<String> respinableSourceIds=null;
					ArrayList<String> respinableSourceQCodes=null;
					while(!_presenceServiceQueueEntriesToRespin.isEmpty()){
						Integer loadid=_presenceServiceQueueEntriesToRespin.firstKey();
						
						ArrayList[]presenceServiceQueueEntry=_presenceServiceQueueEntriesToRespin.remove(_presenceServiceQueueEntriesToRespin.firstKey());
						
						String sourceids=(respinableSourceIds=presenceServiceQueueEntry[0]).toString();
						respinableSourceQCodes=presenceServiceQueueEntry[1];
						while(sourceids.startsWith("[")) sourceids=sourceids.substring(1, sourceids.length());
						while(sourceids.endsWith("]")) sourceids=sourceids.substring(0, sourceids.length()-1);
						if(sourceids.equals("")) continue;
						Database.executeDBRequest(null, "PRESENCE", "UPDATE <DBUSER>.PCO_OUTBOUNDQUEUE SET STATUS=(SELECT CASE PCO_LOAD.STATUS WHEN 'E' THEN 1 WHEN 'D' THEN 41 END FROM <DBUSER>.PCO_LOAD WHERE PCO_LOAD.SERVICEID="+serviceid+" AND PCO_LOAD.LOADID="+loadid+"),CURRENTPHONE=0,CURRENTPHONECOUNTER=0,DAILYCOUNTER=0, TOTALCOUNTER=0, BUSYSIGNALCOUNTER=0, NOANSWERCOUNTER=0, ANSWERMACHINECOUNTER=0, FAXCOUNTER=0, INVGENREASONCOUNTER=0 WHERE SERVICEID="+serviceid+" AND LOADID="+loadid+" AND SOURCEID IN ("+sourceids+")", null,null);
						affectedCount=String.valueOf(respinableSourceIds.size());
						while(!respinableSourceIds.isEmpty()){
							Database.executeDBRequest(null, "RESPIN", "INSERT INTO <DBUSER>.RESPIN_SERVICE_REQUEST_RESPINLEADS (RESPINREQUESTID,SERVICEID,LOADID,SOURCEID,LASTQCODE) SELECT "+RESPINREQUESTID+","+serviceid+","+loadid+","+respinableSourceIds.remove(0)+","+respinableSourceQCodes.remove(0),null,null);
						}
					}
					
					Administrator.administrator("", "").reloadServicesBuffers(serviceid);
				}
			}
			
			Database.executeDBRequest(null, "RESPIN", "UPDATE <DBUSER>.RESPIN_SERVICE_REQUEST SET RESPINCONFIRMATIONLABEL='RESPIN-COMPLETED',REQUESTCOMPLETEDSTAMP=GETDATE(),REQUESTDURATION=DATEDIFF(SECOND,REQUESTTARTSTAMP,GETDATE()),RECORDSAFFECTED="+affectedCount+" WHERE ID="+RESPINREQUESTID,null,null);
		}
		this.replaceComponentContent("respinpage");
			this.respinPage();
		this.endReplaceComponentContent();
	}
	
	private String _loadIdBeingRespined="";
	
	private ArrayList<String> _presenceSourceIds=null;
	private ArrayList<String> _preseourceSourceQCodes=null;
	public void rowDataPresenceQueueEntriesToRespin(Integer rowIindex,ArrayList<String> data,ArrayList<String> columns){
		if(rowIindex==0) return;
		if(!_loadIdBeingRespined.equals(data.get(2))){
			_loadIdBeingRespined=data.get(2);
			_presenceServiceQueueEntriesToRespin.put((Integer)Integer.parseInt(_loadIdBeingRespined),new ArrayList[]{_presenceSourceIds=new ArrayList<String>(),_preseourceSourceQCodes=new ArrayList<String>()});
		}
		_presenceSourceIds.add(data.get(3));
		_preseourceSourceQCodes.add(data.get(4));
	}
	
	
	public void rowDataPresenceQCodes(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns){
		if(rowIndex==0) return;
		_presenceServiceQCodesToRespin.put((Integer)Integer.parseInt(data.get(0)),data.get(1));
	}
	
	public void rowDataPresenceLoads(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns){
		if(rowIndex==0) return;
		_presenceServiceLoadsToRespin.put((Integer)Integer.parseInt(data.get(0)),data.get(1));
	}
	
	public void cancel_respin() throws Exception{
		String RESPINREQUESTID=this.requestParameter("RESPINREQUESTID");
		RESPINREQUESTID=RESPINREQUESTID.equals("")?"0":RESPINREQUESTID.equals("0")?RESPINREQUESTID:RESPINREQUESTID;
		
		String RECORDSAFFECTED=this.requestParameter("RECORDSAFFECTED");
		RECORDSAFFECTED=RECORDSAFFECTED.equals("")?"0":RECORDSAFFECTED.equals("0")?RECORDSAFFECTED:RECORDSAFFECTED;
		
		String RECORDSNOTAFFECTED=this.requestParameter("RECORDSNOTAFFECTED");
		RECORDSNOTAFFECTED=RECORDSNOTAFFECTED.equals("")?"0":RECORDSNOTAFFECTED.equals("0")?RECORDSNOTAFFECTED:RECORDSNOTAFFECTED;
		if(!RESPINREQUESTID.equals("0")){
			Database.executeDBRequest(null, "RESPIN", "UPDATE <DBUSER>.RESPIN_SERVICE_REQUEST SET RECORDSNOTAFFECTED="+RECORDSNOTAFFECTED+",RECORDSAFFECTED="+RECORDSAFFECTED+", RESPINCONFIRMATIONLABEL='CANCELED',REQUESTCOMPLETEDSTAMP=GETDATE() WHERE ID="+RESPINREQUESTID,null, null);
			Database.executeDBRequest(null, "RESPIN", "UPDATE <DBUSER>.RESPIN_SERVICE_REQUEST SET REQUESTDURATION=DATEDIFF(SECOND,REQUESTTARTSTAMP,REQUESTCOMPLETEDSTAMP) WHERE ID="+RESPINREQUESTID,null, null);
		}
		this.replaceComponentContent("respinpage");
			this.respinPage();
		this.endReplaceComponentContent();
	}
	
	public void rowActiveLoads(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns) throws Exception{
		if(rowIndex>0){
			_activeQCodes.put(Integer.parseInt(rowData.get(0)),"["+rowData.get(2)+"] ("+rowData.get(0)+") "+rowData.get(1));
		}
	}
	
	public void constructQCodeView(String className) throws Exception{
		if(!_activeQCodes.isEmpty()){
			//this.startElement("input",( "style=font-weight:bold;font-size:0.6em|type=button|value=SELECT ALL|onclick=checkCheckBoxes('."+className+"')").split("[|]"), true);
			//this.startElement("input",( "style=font-weight:bold;font-size:0.6em|type=button|value=SELECT ALL|onclick=checkCheckBoxes('."+className+"')").split("[|]"), false);this.endElement("input", false);
			//this.startElement("input",( "style=font-weight:bold;font-size:0.6em|type=button|value=UNSELECT ALL|onclick=uncheckCheckBoxes('."+className+"')").split("[|]"), false);this.endElement("input", false);
			
			this.startElement("input",( "id="+className.replaceAll("[-]", "_")+"_selectall|style=font-weight:bold;font-size:0.6em|type=button|value=SELECT ALL").split("[|]"), false);this.endElement("input", false);
			this.startElement("input",( "id="+className.replaceAll("[-]", "_")+"_unselectall|style=font-weight:bold;font-size:0.6em|type=button|value=UNSELECT ALL").split("[|]"), false);this.endElement("input", false);
			
			this.startScript();
				this.respondString("$('#"+className.replaceAll("[-]", "_")+"_selectall').unbind('click');");
				this.respondString("$('#"+className.replaceAll("[-]", "_")+"_selectall').click(function(){checkCheckBoxes('."+className+"');});");
				
				this.respondString("$('#"+className.replaceAll("[-]", "_")+"_unselectall').unbind('click');");
				this.respondString("$('#"+className.replaceAll("[-]", "_")+"_unselectall').click(function(){uncheckCheckBoxes('."+className+"');});");
			this.endScript();
			this.startTable();
				int recCount=0;
				int maxRowCount=15;
				this.startRow();
					this.startCell("style=vertical-align:top".split("[|]"));
						while(!_activeQCodes.isEmpty()){
							recCount++;
							this.startTable();
								this.startRow();
									this.startCell();
										this.fieldInput("CAMPAIGNQCODE",_activeQCodes.firstKey().toString(), "checkbox", true, ("class="+className).split("[|]"));
									this.endCell();
									this.startCell();
										this.fieldLabel(_activeQCodes.remove(_activeQCodes.firstKey()),true);
									this.endCell();									
								this.endRow();
							this.endTable();
							if(recCount==maxRowCount&&!_activeQCodes.isEmpty()){
								this.endCell();
								this.startCell("style=vertical-align:top".split("[|]"));
								recCount=0;
							}
						}
					this.endCell();
				this.endRow();
			this.endTable();
		}
	}

	private TreeMap<Integer,String> _activeQCodes=new TreeMap<Integer, String>();
	
	public void selectCampaignBusinessQCodes() throws Exception{
		_activeQCodes.clear();
		Database.executeDBRequest(null, "PRESENCE-VIEW", "SELECT QCODE,DESCRIPTION,TYPE FROM <DBUSER>.SERVICEQCODE WHERE QCODE>=100 AND SERVICEID="+this.requestParameter("SERVICE_TO_RESPIN"), null, this, "rowActiveQCodes");
		constructQCodeView("business-qcodes");
	}
	
	public void rowActiveQCodes(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns) throws Exception{
		if(rowIndex>0){
			_activeQCodes.put(Integer.parseInt(rowData.get(0)),"("+rowData.get(0)+") "+rowData.get(1));
		}
	}

	public void selectCampaignSystemQCodes() throws Exception{
		_activeQCodes.clear();
		Database.executeDBRequest(null, "PRESENCE-VIEW", "SELECT QCODE,DESCRIPTION,TYPE FROM <DBUSER>.SERVICEQCODE WHERE QCODE<100 AND SERVICEID="+this.requestParameter("SERVICE_TO_RESPIN"), null, this, "rowActiveQCodes");
		
		this.constructQCodeView("system-qcodes");
		
	}
	
	public void constructCampaignLoadsView() throws Exception{
		if(!_activeQCodes.isEmpty()){
			String className="campiagn-loads";
			//this.startElement("input",( "style=font-weight:bold;font-size:0.6em|type=button|value=SELECT ALL|onclick=checkCheckBoxes('."+className+"')").split("[|]"), false);
			//this.startElement("input",( "style=font-weight:bold;font-size:0.6em|type=button|value=UNSELECT ALL|onclick=uncheckCheckBoxes('."+className+"')").split("[|]"), false);
			
			this.startElement("input",( "id="+className.replaceAll("[-]", "_")+"_selectall|style=font-weight:bold;font-size:0.6em|type=button|value=SELECT ALL").split("[|]"), false);this.endElement("input", false);
			this.startElement("input",( "id="+className.replaceAll("[-]", "_")+"_unselectall|style=font-weight:bold;font-size:0.6em|type=button|value=UNSELECT ALL").split("[|]"), false);this.endElement("input", false);
			
			this.startScript();
				this.respondString("$('#"+className.replaceAll("[-]", "_")+"_selectall').unbind('click');");
				this.respondString("$('#"+className.replaceAll("[-]", "_")+"_selectall').click(function(){checkCheckBoxes('."+className+"');});");
				
				this.respondString("$('#"+className.replaceAll("[-]", "_")+"_unselectall').unbind('click');");
				this.respondString("$('#"+className.replaceAll("[-]", "_")+"_unselectall').click(function(){uncheckCheckBoxes('."+className+"');});");
			this.endScript();
			
			this.startTable();
				int recCount=0;
				int maxRowCount=15;
				this.startRow();
					this.startCell("style=vertical-align:top".split("[|]"));
						while(!_activeQCodes.isEmpty()){
							recCount++;
							this.startTable();
								this.startRow();
									this.startCell();
										this.fieldInput("CAMPAIGNLOAD",_activeQCodes.firstKey().toString(), "checkbox", true, ("class="+className).split("[|]"));
									this.endCell();
									this.startCell();
										this.fieldLabel(_activeQCodes.remove(_activeQCodes.firstKey()),true);
									this.endCell();									
								this.endRow();
							this.endTable();
							if(recCount==maxRowCount&&!_activeQCodes.isEmpty()){
								this.endCell();
								this.startCell("style=vertical-align:top".split("[|]"));
								recCount=0;
							}
						}
					this.endCell();
				this.endRow();
			this.endTable();
		}
	}
}
