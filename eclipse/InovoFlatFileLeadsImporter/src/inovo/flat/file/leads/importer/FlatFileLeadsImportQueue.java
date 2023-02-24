package inovo.flat.file.leads.importer;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class FlatFileLeadsImportQueue extends InovoHTMLPageWidget {

	public FlatFileLeadsImportQueue(InovoWebWidget parentWidget,
			InputStream inStream) {
		super(parentWidget, inStream);
	}

	@Override
	public void pageContent() throws Exception {
		this.startTable(null);
			this.startRow(null);
				this.startColumn(null);
					this.respondString("FROM (YYYY-MM-DD)");
				this.endColumn();
				
				this.startColumn(null);
					this.respondString("TO (YYYY-MM-DD)");
				this.endColumn();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
					this.fieldInput("SEARCH_FROM", "", "text", true, null);
				this.endCell();
				this.startCell(null);
					this.fieldInput("SEARCH_TO", "", "text", true, null);
				this.endCell();
			this.endRow();
		this.endTable();
		this.action("SEARCH", "searchimportedrequests", "mainform", "", "", "", "ui-icon-search", "");
		this.startElement("div", new String[]{"id=viewimportedrequests"}, true);this.endElement("div", true);
	}
	
	public void searchimportedrequests() throws Exception{
		HashMap<String,String> importedrequestsParams=new HashMap<String,String>();
		
		String fromDate=this.requestParameter("SEARCH_FROM");
		
		String toDate=this.requestParameter("SEARCH_TO");
		
		if(fromDate.equals("")){
			fromDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}
		
		Date dateFrom=new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		
		String todateMaskValue=new SimpleDateFormat("yyyy-MM-dd").format(dateFrom)+" 23:59:59.999";
		
		if(toDate.length()<=todateMaskValue.length()){
			toDate+=todateMaskValue.substring(toDate.length());
		}
		
		Date dateTo=(toDate.equals("")?new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())+" 23:59.59.999"):new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(toDate));
		
		importedrequestsParams.put("FROMDATE", fromDate);
		importedrequestsParams.put("TODATE", toDate);
		
		String sqlimportedrequests="SELECT COUNT(*) AS TOTAL_ENTRIES, METAFIELD19 AS LOADEDFILE,(SELECT MIN(DLIST.LASTACTIONDATETIME) FROM <DBUSER>.DYNAMICCALLERLIST DLIST WHERE DLIST.METAFIELD19=DYNLIST.METAFIELD19) AS LOADED_FILE_AT, METAFIELD20 AS FILELEADSALLIAS,CONVERT(VARCHAR(10),LASTACTIONDATETIME,121) AS DATELOADED,(SELECT COUNT(*) FROM PREP.PCO_OUTBOUNDQUEUE OUTQUEUE INNER JOIN <DBUSER>.DYNAMICCALLERLIST DSLIST ON DSLIST.SERVICEID=OUTQUEUE.SERVICEID AND DSLIST.LOADID=OUTQUEUE.LOADID AND DSLIST.ID=OUTQUEUE.SOURCEID AND DSLIST.METAFIELD19=DYNLIST.METAFIELD19)  AS TOTAL_LEADS_IN_PRESENCE FROM <DBUSER>.DYNAMICCALLERLIST DYNLIST WHERE DYNLIST.LASTACTIONDATETIME BETWEEN :FROMDATE AND :TODATE GROUP BY METAFIELD19,METAFIELD20,CONVERT(VARCHAR(10),LASTACTIONDATETIME,121) ORDER BY CONVERT(VARCHAR(10),LASTACTIONDATETIME,121) DESC,METAFIELD20,METAFIELD19";
		TreeMap<Integer, ArrayList<String>> importedrequestsset=new TreeMap<Integer, ArrayList<String>>();
		
		Database.executeDBRequest(importedrequestsset,"FLATFILELEADSIMPORTER", sqlimportedrequests, importedrequestsParams,null);
		
		this.replaceComponentContent("viewimportedrequests");
		
		this.startTable(new String[]{"cellpadding=0","cellspacing=0"}); 		
		for(int rowindex:importedrequestsset.keySet()){
			this.startRow(null);
				for(String rowData:importedrequestsset.get(rowindex)){
					if(rowindex==0){
						this.startColumn("");
					}
					else{
						this.startCell(new String[]{"style=border: solid #A9A9A9 1px"});
					}
					this.respondString(encodeHTML(rowData));
										
					if(rowindex==0){
						this.endColumn();
					}
					else{
						this.endCell();
					}
				}
			this.endRow();
		}
		
		this.endTable();
		
		this.endReplaceComponentContent();
		Database.cleanupDataset(importedrequestsset);
		importedrequestsset=null;
	}
}
