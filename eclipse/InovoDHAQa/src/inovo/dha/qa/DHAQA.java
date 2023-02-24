package inovo.dha.qa;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class DHAQA extends InovoHTMLPageWidget {

	public DHAQA(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}
	
	@Override
	public String[] additionalLinks() {
		return "/jquery/datatables/jquery.dataTables.css".split("[|]");
	}
	
	@Override
	public String[] additionalScripts() {
		return "/jquery/datatables/jquery.dataTables.js".split("[|]");
	}

	@Override
	public void pageContent() throws Exception {
		this.startTable();
			this.startRow();
				this.startCell("class=ui-widget-header");this.respond.print("REPORTING"); this.endCell(); 
			this.endRow();
			this.startRow();
				this.startCell("style=vertical-align:top");
					List<String[]> actionsProperties=new ArrayList<String[]>();
					actionsProperties.add("CAPTION=VOICE CALLS|command=defaultvoicerpt".split("[|]"));
					this.actions(actionsProperties, false);
					actionsProperties.clear();
					actionsProperties=null;
				this.endCell();
			this.endRow();
			this.startRow();
				this.startCell();
					this.startComplexElement("div", "id=reportsearch");this.endComplexElement("div");
					this.startComplexElement("div", "id=reportview");this.endComplexElement("div"); this.endCell();
			this.endRow();
		this.endTable();
	}
	
	private HashMap<String,Object> currentSearchParams=new HashMap<String, Object>();	
	public void voicecallsearch() throws Exception {
		this.replaceComponentContent("reportsearch");
			this.importRequestParametersIntoMap(currentSearchParams, "voicefrom,voiceto,voiceqaagentlogins,voiceagentlogins,voiceexportformat");
			currentSearchParams.put("VOICEAGENTLOGINS",currentSearchParams.get("VOICEAGENTLOGINS").toString().trim());
			currentSearchParams.put("VOICEQAAGENTLOGINS",currentSearchParams.get("VOICEQAAGENTLOGINS").toString().trim());
			this.startTable();
				this.startRow();
					this.startColumn("font-size:0.8em");this.respond.print("FROM:"); this.endColumn();
					if (currentSearchParams.get("VOICEFROM").equals("")) {
						Calendar c = Calendar.getInstance();   // this takes current date
					    c.set(Calendar.DAY_OF_MONTH, 1);
					    String pattern = "yyyy-MM-dd";
					    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

					    String date = simpleDateFormat.format(c.getTime());
					    currentSearchParams.put("VOICEFROM",date);
					    if (currentSearchParams.get("VOICETO").equals("")) {
					    	c = Calendar.getInstance();   // this takes current date
						    simpleDateFormat = new SimpleDateFormat(pattern);

						    date = simpleDateFormat.format(c.getTime());
						    currentSearchParams.put("VOICETO",date);
					    }
					}
					this.startCell();this.fieldInput("voicefrom",currentSearchParams.get("VOICEFROM").toString() , "date", true, "", "");this.endCell();
					this.startColumn("font-size:0.8em");this.respond.print("TO:"); this.endColumn();
					if (currentSearchParams.get("VOICETO").equals("")) {
						currentSearchParams.put("VOICETO",currentSearchParams.get("VOICEFROM"));
					}
					this.startCell();this.fieldInput("voiceto",currentSearchParams.get("VOICETO").toString(), "date", true, "", "");this.endCell();
				this.endRow();
				this.startRow();
					this.startCell("colspan=4");
						this.startTable("width=100%");
							this.startRow();
								this.startColumn("font-size:0.8em");this.respond.print("AGENT  LOGIN(s):"); this.endColumn();
							this.endRow();
							this.startRow();
								this.startCell();this.fieldInput("voiceagentlogins", currentSearchParams.get("VOICEAGENTLOGINS").toString(), "multiline", true, "cols=100", "");this.endCell();
							this.endRow();
						this.endTable();
					this.endCell();
				this.endRow();
				this.startRow();
				this.startCell("colspan=4");
					this.startTable("width=100%");
						this.startRow();
							this.startColumn("font-size:0.8em");this.respond.print("QA USER LOGIN(s):"); this.endColumn();
						this.endRow();
						this.startRow();
							this.startCell();this.fieldInput("voiceqaagentlogins", currentSearchParams.get("VOICEQAAGENTLOGINS").toString(), "multiline", true, "cols=100", "");this.endCell();
						this.endRow();
					this.endTable();
				this.endCell();
			this.endRow();
				this.startRow();
					this.startCell("colspan=2");
						this.action("SEARCH", "applyvoicesearch", "", "", "", "", "", "");
					this.endCell();
					this.startCell("colspan=2");
					this.action("EXPORT", "applyvoicesearchexport", "", "", "", "", "", "");
					this.fieldInput("voiceexportformat", currentSearchParams.get("VOICEEXPORTFORMAT").toString(), "select", true, null, "CSV=csv");
				this.endCell();
				this.endRow();
			this.endTable();
		this.endReplaceComponentContent();
	}
	
	public void defaultvoicerpt() throws Exception{
		this.requestParameters().clear();
		this.voicecallsearch();
		this.voicecallsrpt();
	}
	
	public void applyvoicesearchexport() throws Exception{
		this.importRequestParametersIntoMap(currentSearchParams, "voicefrom,voiceto,voiceqaagentlogins,voiceagentlogins,voiceexportformat");
		if (!currentSearchParams.get("VOICEEXPORTFORMAT").equals("")) {
			this.exportFormat=currentSearchParams.get("VOICEEXPORTFORMAT").toString();
			this.dbquery("DHAQA", "EXECUTE <DBUSER>.spRPTDHAQA :VOICEFROM,:VOICETO,:VOICEAGENTLOGINS,:VOICEQAAGENTLOGINS, 'RECORDING'", currentSearchParams, "viewData");
		}
	}
	
	public void voicecallsrpt() throws Exception {
		this.replaceComponentContent("reportview");
		this.dbquery("DHAQA", "EXECUTE <DBUSER>.spRPTDHAQA :VOICEFROM,:VOICETO,:VOICEAGENTLOGINS,:VOICEQAAGENTLOGINS, 'RECORDING'", currentSearchParams, "viewData");
		this.endReplaceComponentContent();
	}
	
	private boolean hasData=false;
	private String exportFormat="";

	public void viewData(boolean lastRec, long rowid,List<Object> data,List<Object> cols) throws Exception {
		if (this.exportFormat.equals("")) {
			if (rowid==0) {
				
				this.startTable();
					this.startTHead();
						this.startRow();
							for (Object c:cols) {
								this.startColumn();this.respond.print(c);this.endColumn();
							}
						this.endRow();
					this.endTHead();
					this.startTBody();
				
			} else if (rowid>0){
				if (!hasData) {
					hasData=true;
				}
				this.startRow();
					for (Object d:data) {
						this.startCell();this.respond.print(d);this.endCell();
					}
				this.endRow();
			}
			
			if (lastRec&&hasData) {
					this.endTBody();
					this.startTFoot();
						this.startRow();
							for (Object c:cols) {
								this.startColumn();this.respond.print(c);this.endColumn();
							}
						this.endRow();
					this.endTFoot();
				this.endTable();
			}
		} else if (this.exportFormat.toLowerCase().equals("csv")) {
			if (rowid==0) {
				this.setResponseHeader("CONTENT-TYPE","text/csv");
				this.setResponseHeader("CONTENT-DISPOSITION", "attachment; filename=\"qadhaexport.csv\"");
				int coli=0;
				for (Object c:cols) {
					this.respond.print(c);
					coli++;
					if (coli<data.size()) {
						this.respond.print(";");
					}
				}
				this.respond.println();
			} else if (rowid>0){
				if (!hasData) {
					hasData=true;
				}
				int coli=0;
				for (Object d:data) {
					this.respond.print(d);
					coli++;
					if (coli<data.size()) {
						this.respond.print(";");
					}
				}
				this.respond.println();
			}
		}
	}
	
	public void applyvoicesearch() throws Exception {
		this.setRequestParameter("VOICEEXPORTFORMAT", "", true);
		this.voicecallsearch();
		this.voicecallsrpt();
	}
}
