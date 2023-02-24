package inovo.message.gateway;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.TreeMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class QueueStatus extends InovoHTMLPageWidget {

	public QueueStatus(InovoWebWidget parentWidget,
			InputStream inStream) {
		super(parentWidget, inStream);
	}

	
	@Override
	public void pageContent() throws Exception {
		/*this.startTable(null);
			this.startRow(null);
				this.startColumn("");
					this.respondString("QUEUE STATUS FILTER");
				this.endColumn();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
					this.queueFilterSelection();
				this.endCell();
			this.endRow();
		this.endTable();*/
		this.startTable(null);
			this.startRow(null);
				this.startColumn("");
					this.respondString("MESSAGE GATEWAY QUEUE");
				this.endColumn();
			this.endRow();
			this.startRow(null);
				this.startCell(new String[]{"id=messagegatewayqueueset"});
					this.messagegatewayqueueset();
				this.endCell();
			this.endRow();
		this.endTable();
		
	}


	public void messagegatewayqueueset() throws Exception {
		
		TreeMap<Integer,ArrayList<String>> selectedmessagegatewayqueueset=new TreeMap<Integer, ArrayList<String>>();
				
				Database.executeDBRequest(selectedmessagegatewayqueueset,"MESSAGEGATEWAY", 
				"SELECT "+(this.requestParameter("SHOW").toUpperCase().equals("ALL")?" ":"TOP 1000 ")+
						"[KEYPHRASE] AS [ORIGINAL KEYPHRASE], "+
						"[SOURCE] AS [MESSAGE SOURCE],"+
						"[PHONENR] AS [PHONE NR],"+
						"[NAME] AS [REQUEST FROM(NAME)],"+
						"[RECEIVEDDATETIME] AS [RECEIVED ON],"+
						"[REQUESTKEYPHRASESELECTED] AS [SELECTED KEYPHRASE], "+
						"[REQUESTSERVICEID] AS [SERVICE NR],"+
						"[REQUESTLOADID] AS [LOAD NR],"+
						"[REQUESTSOURCEID] AS [SOURCEID],"+
						"[REQUESTCREATEDDATETIME] AS [QUEUED ON],"+
						"[REQUESTCREATESTATUSOUTCOME] AS [QUEUEDSTATUS]"+
						" FROM <DBUSER>.[MESSAGE_GATEWAY_QUEUE] ORDER BY RECEIVEDDATETIME DESC", null,null);
		if(selectedmessagegatewayqueueset!=null){
			boolean isError=false;
			this.startTable(new String[]{"class=ui-widget-content"});
				for(int rowindex:selectedmessagegatewayqueueset.keySet()){
					ArrayList<String> currentRow=selectedmessagegatewayqueueset.get(rowindex);
					if(rowindex==0){
						isError=false;
					}
					else{
						isError=!currentRow.get(10).equals("SUCCESS");
					}
					this.startRow(null);
						for(String colData:currentRow){
							if(rowindex==0){
								this.startColumn("");
									this.respondString(encodeHTML(colData));
								this.endColumn();
							}
							else{
								this.startCell(new String[]{"style=border:solid 1px " +(isError?"#A90000":"#A9A9A9")+(isError?";font-weight:solid;color:#A90000":"")});
									this.respondString(encodeHTML(colData));
								this.endCell();
							}
						}
					
					this.endRow();
				}
			this.endTable();
			Database.cleanupDataset(selectedmessagegatewayqueueset);
			selectedmessagegatewayqueueset=null;
		}
	}


	private void queueFilterSelection() throws Exception{
		this.startTable(new String[]{"class=ui-widget-content"});
			this.startRow(null);
				this.startCell(new String[]{"class=ui-widget-header","colspan=3"});
					this.respondString("RECEIVED");
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
					this.fieldLabel("ORIGINAL KEYPHRASE");
				this.endCell();
				this.startCell(null);
					this.fieldLabel("MESSAGE SOURCE");
				this.endCell();
				this.startCell(null);
					this.fieldLabel("RECEIVED ON");
			this.endRow();
			this.startRow(null);
				this.startCell(null);
					this.fieldInput("KEYPHRASE", "", "text", true, null);
				this.endCell();
				this.startCell(null);
					this.fieldInput("SOURCE", "", "text", true, null);
				this.endCell();
				this.startCell(null);
					this.fieldInput("RECEIVEDDATETIME", "", "text", true, null);
				this.endCell();
			this.endRow();
		this.endTable();
		
		this.startTable(new String[]{"class=ui-widget-content"});
			this.startRow(null);
				this.startCell(new String[]{"class=ui-widget-header","colspan=5"});
					this.respondString("QUEUED");
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
					this.fieldLabel("SELECTED KEYPHRASE");
				this.endCell();
				this.startCell(null);
					this.fieldLabel("SERVICE NR");
				this.endCell();
				this.startCell(null);
					this.fieldLabel("LOAD NR");
				this.endCell();
				this.startCell(null);
					this.fieldLabel("SOURCEID");
				this.endCell();
				this.startCell(null);
					this.fieldLabel("QUEUED ON");
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
					this.fieldInput("REQUESTKEYPHRASESELECTED", "", "text", true, null);
				this.endCell();
				this.startCell(null);
					this.fieldInput("SERVICEID", "", "text", true, null);
				this.endCell();
				this.startCell(null);
					this.fieldInput("LOADID", "", "text", true, null);
				this.endCell();
				this.startCell(null);
					this.fieldInput("SOURCEID", "", "text", true, null);
				this.endCell();
				this.startCell(null);
					this.fieldInput("REQUESTCREATEDDATETIME", "", "text", true, null);
				this.endCell();
			this.endRow();
		this.endTable();
		this.action("FILTER", "filter", "", this.getClass().getName(), "", "", "ui-icon-search", "");
	}
}
