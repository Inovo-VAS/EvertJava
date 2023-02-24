package inovo.flat.file.leads.importer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class FlatFileLeadsShortcodeConfiguration extends InovoHTMLPageWidget {

	public FlatFileLeadsShortcodeConfiguration(InovoWebWidget parentWidget,
			InputStream inStream) {
		super(parentWidget, inStream);
	}

	@Override
	public void pageContent() throws Exception {
		this.action("ADD SHORTCODE", "addshortcode", "", "", "", "", "", "");
		this.startElement("div", "id=mappedshortcodes".split("[|]"), true);
			this.mappedshortcodes();
		this.endElement("div", true);
	}
	
	public void mappedshortcodes() throws Exception{
		this.startTable(null);
			this.startRow(null);
				this.startCell("style=vertical-align:top".split("[|]"));
					this.startTable(null);
						this.startRow(null);
							this.startColumn(null);this.endColumn();
							this.startColumn("font-size:0.8em");
								this.respondString("SHORTCODE");
							this.endColumn();
							this.startColumn("font-size:0.8em");
								this.respondString("CAMPAIGN");
							this.endColumn();
							this.startColumn("font-size:0.8em");
								this.respondString("LOAD");
							this.endColumn();
						this.endRow();
						Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT ID,SHORTCODE,DEFAULTSERVICEID,DEFAULTLOADID FROM <DBUSER>.LEADSDATAFILESHORTCODEMAP",null, this,"rowMappedShortcodes");
					this.endTable();
				this.endCell();
				this.startCell("id=leadsimportshortcode|style=vertical-align:top".split("[|]"));
				this.endCell();
			this.endRow();
		this.endTable();
	}
	
	private HashMap<String,String> _selectedShortCodeSettings=new HashMap<String, String>();
	public void leadsimportshortcode() throws Exception{
		
		//this.importRequestParametersIntoMap(selectedShortCodeSettings, "SELECTEDSHORTCODEID");
		if(!this.requestParameter("SELECTEDSHORTCODEID").equals("")){
			_selectedShortCodeSettings.put("SHORTCODEID", this.requestParameter("SELECTEDSHORTCODEID"));
			this.setRequestParameter("SHORTCODEID", this.requestParameter("SELECTEDSHORTCODEID"), true);
		}
		else if(!this.requestParameter("SHORTCODEID").equals("")){
			_selectedShortCodeSettings.put("SHORTCODEID", this.requestParameter("SHORTCODEID"));
		}
		
		this.fieldHidden("SHORTCODEID", _selectedShortCodeSettings.get("SHORTCODEID"));
		
		this._selectedShortCodeSettings.put("SHORTCODE", "");
		this._selectedShortCodeSettings.put("SHORTCODESERVICEID", "");
		this._selectedShortCodeSettings.put("SHORTCODELOADID", "");
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT ID AS SHORTCODEID,SHORTCODE,DEFAULTSERVICEID AS SHORTCODESERVICEID,DEFAULTLOADID AS SHORTCODELOADID FROM <DBUSER>.LEADSDATAFILESHORTCODEMAP WHERE ID=:SHORTCODEID", this._selectedShortCodeSettings, null);
		
		this.startElement("div", "class=ui-widget-header|style=font-size:1.0".split("[|]"), true);
			this.respondString("SHORTCODE ["+this._selectedShortCodeSettings.get("SHORTCODE")+"]");
		this.endElement("div", true);
		this.startElement("div", "id=shortcodekeywords".split("[|]"), true);
			this.shortcodekeywords();
		this.endElement("div", true);
	}
	
	public void shortcodekeywords() throws Exception{
		this.action("ADD KEYWORD", "addshortcodekeyword" , "", "", "", "", "", "");
		this.startTable(null);
			this.startRow(null);
				this.startColumn("font-size:0.8em");
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respondString("KEYWORD");
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respondString("CAMPAIGN (SERVICEID)");
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respondString("LOAD (LOADID)");
				this.endColumn();
			this.endRow();
			Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT ID , KEYWORD, DEFAULTSERVICEID AS KEYWORDSERVICEID,DEFAULTLOADID AS KEYWORDLOADID,ENABLED FROM <DBUSER>.LEADSDATAFILESHORTCODEKEYWORDMAP WHERE SHORTCODEID="+this.requestParameter("SHORTCODEID"), null, this,"readshortCodeKeyWords");
		this.endTable();
	}
	
	public void addshortcodekeyword() throws Exception{
		this.showDialog("", "title=ADD KEYWORD|contentid=addingkeyword|BUTTON:ADD=command=confirmaddingkeyword|BUTTON:CANCEL=".split("[|]"));
		this.replaceComponentContent("addingkeyword");
			this.startTable(null);
				this.startRow(null);
					this.startColumn("font-size:0.8em");
						this.respondString("KEYWORD");
					this.endColumn();
					this.startCell(null);
						this.fieldInput("NEWKEYWORD", "", "text", true, null);
					this.endCell();
				this.endRow();
			this.endTable();
		this.endReplaceComponentContent();
	}
	
	public void confirmaddingkeyword() throws Exception{
		this._selectedShortCodeSettings.clear();
		this.importRequestParametersIntoMap(_selectedShortCodeSettings, "SHORTCODEID,NEWKEYWORD");
		if(this._selectedShortCodeSettings.get("NEWKEYWORD").equals("")){
			this.showDialog("", "title=ADD KEYWORD|content=NO KEYWORD ENTERED".split("[|]"));
		}
		else{
			this._selectedShortCodeSettings.put("KEYWORDCOUNT", "0");
			Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(*) AS KEYWORDCOUNT FROM <DBUSER>.LEADSDATAFILESHORTCODEKEYWORDMAP WHERE SHORTCODEID=:SHORTCODEID AND UPPER(KEYWORD)=UPPER(:NEWKEYWORD)",_selectedShortCodeSettings,this);
			if(this._selectedShortCodeSettings.get("KEYWORDCOUNT").equals("0")){
				Database.executeDBRequest(null, "FLATFILELEADSIMPORTER","INSERT INTO <DBUSER>.LEADSDATAFILESHORTCODEKEYWORDMAP (SHORTCODEID,KEYWORD) SELECT :SHORTCODEID,UPPER(:NEWKEYWORD)",this._selectedShortCodeSettings,null);
				this.replaceComponentContent("leadsimportshortcode");
					this.leadsimportshortcode();
				this.endReplaceComponentContent();
			}
			else{
				this.showDialog("", "title=ADD KEYWORD|content=KEYWORD ALREADY EXIST FOR SHORTCODE".split("[|]"));
			}
		}
	}
	
	public void readshortCodeKeyWords(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns) throws Exception{
		if(rowIndex==0) return;
		this.startRow(null);
			this._selectedShortCodeSettings.put("KEYWORDSERVICEID", rowData.get(2).equals("")?"0":rowData.get(2));
			this._selectedShortCodeSettings.put("KEYWORDLOADID", rowData.get(3).equals("")?"0":rowData.get(3));
			this._selectedShortCodeSettings.put("KEYWORDENABLED", rowData.get(4).equals("")?"0":rowData.get(4));
			
			this._selectedShortCodeSettings.put("KEYWORDSERVICENAME", "");
			this._selectedShortCodeSettings.put("KEYWORDLOADNAME", "");
			
			this._selectedShortCodeSettings.put("KEYWORDLOADSTATUS", "");
			
			Database.executeDBRequest(null, "PRESENCE", "SELECT PCO_OUTBOUNDSERVICE.NAME AS KEYWORDSERVICENAME,ISNULL(PCO_LOAD.DESCRIPTION,'') AS KEYWORDLOADNAME,ISNULL(PCO_LOAD.STATUS,'') AS KEYWORDLOADSTATUS FROM (SELECT ID,NAME FROM <DBUSER>.PCO_OUTBOUNDSERVICE WHERE ID=:KEYWORDSERVICEID) PCO_OUTBOUNDSERVICE LEFT JOIN <DBUSER>.PCO_LOAD ON PCO_OUTBOUNDSERVICE.ID=PCO_LOAD.SERVICEID AND PCO_LOAD.LOADID=:KEYWORDLOADID"   , this._selectedShortCodeSettings, null);

			this.startColumn(null);
				if(this._selectedShortCodeSettings.get("KEYWORDENABLED").equals("E")){
					this.action("DISABLE", "disablekeyword", "", "", "", "", "", "selectedkeywordid="+rowData.get(0));
				}
				else{
					this.action("ENABLE", "enablekeyword", "", "", "", "", "", "selectedkeywordid="+rowData.get(0));
					this.action("EDIT", "editkeyword", "", "", "", "", "", "selectedkeywordid="+rowData.get(0));
				}
			this.endColumn();
			this.startColumn("font-size:0.8em");
				this.respondString(rowData.get(1)); //KEYWORD
			this.endColumn();
			this.startColumn("font-size:0.8em");
				this.respondString(this._selectedShortCodeSettings.get("KEYWORDSERVICENAME")); //SERVICENAME
			this.endColumn();
			this.startColumn("font-size:0.8em");
				this.respondString(this._selectedShortCodeSettings.get("KEYWORDLOADNAME")); //LOADNAME
			this.endColumn();
		this.endRow();
	}
	
	public void enablekeyword() throws Exception{
		this._selectedShortCodeSettings.clear();
		this.importRequestParametersIntoMap(_selectedShortCodeSettings, "SHORTCODEID,SELECTEDKEYWORDID");
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "UPDATE <DBUSER>.LEADSDATAFILESHORTCODEKEYWORDMAP SET ENABLED='E' WHERE DEFAULTSERVICEID<>0 AND DEFAULTLOADID<>0 AND SHORTCODEID=:SHORTCODEID AND ID=:SELECTEDKEYWORDID", _selectedShortCodeSettings, null);
		this._selectedShortCodeSettings.clear();
		this.replaceComponentContent("shortcodekeywords");
			this.shortcodekeywords();
		this.endReplaceComponentContent();
	}
	
	public void disablekeyword() throws Exception{
		this._selectedShortCodeSettings.clear();
		this.importRequestParametersIntoMap(_selectedShortCodeSettings, "SHORTCODEID,SELECTEDKEYWORDID");
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "UPDATE <DBUSER>.LEADSDATAFILESHORTCODEKEYWORDMAP SET ENABLED='D' WHERE SHORTCODEID=:SHORTCODEID AND ID=:SELECTEDKEYWORDID", _selectedShortCodeSettings, null);
		this.replaceComponentContent("shortcodekeywords");
			this.shortcodekeywords();
		this.endReplaceComponentContent();
	}
	
	public void editkeyword() throws Exception{
		this._selectedShortCodeSettings.clear();
		this.importRequestParametersIntoMap(_selectedShortCodeSettings, "SHORTCODEID,SELECTEDKEYWORDID");
		Database.executeDBRequest(null, "PRESENCE", "SELECT ID,'('+UPPER(ID) +') ' +NAME AS SERVICENAME FROM <DBUSER>.PCO_OUTBOUNDSERVICE", null, this,"rowNewServicesData");
		
		this._selectedShortCodeSettings.put("KEYWORD", "");
		this._selectedShortCodeSettings.put("KEYWORDSERVICEID", "0");
		this._selectedShortCodeSettings.put("KEYWORDLOADID", "0");
		
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT KEYWORD, DEFAULTSERVICEID AS KEYWORDSERVICEID,DEFAULTLOADID AS KEYWORDLOADID FROM <DBUSER>.LEADSDATAFILESHORTCODEKEYWORDMAP WHERE ID=:SELECTEDKEYWORDID", _selectedShortCodeSettings, null);
		
		if(this._selectedShortCodeSettings.get("KEYWORDSERVICEID").equals("")){
			this._selectedShortCodeSettings.put("KEYWORDSERVICEID", "0");
		}
		if(this._selectedShortCodeSettings.get("KEYWORDLOADID").equals("")){
			this._selectedShortCodeSettings.put("KEYWORDLOADID", "0");
		}
		
		this.showDialog("", "title=EDIT KEYWORD|contentid=editkeword|BUTTON:CONFIRM=command=confirmkeyword|BUTTON:CANCEL=".split("[|]"));
		this.replaceComponentContent("editkeword");
			this.fieldHidden("SELECTEDKEYWORDID", this.requestParameter("SELECTEDKEYWORDID"));
			this.startTable(null);
				this.startRow(null);
					this.startColumn("font-size:0.8em");this.respondString("KEYWORD");this.endColumn();
					this.startCell(null);this.fieldInput("KEYWORD", this._selectedShortCodeSettings.get("KEYWORD"), "text", true, null);
				this.endRow();
				this.startRow(null);
					this.startColumn("font-size:0.8em");this.respondString("PRESENCE CAMPAIGN");this.endColumn();
					this.startCell(null);this.fieldInput("KEYWORDSERVICEID", this._selectedShortCodeSettings.get("KEYWORDSERVICEID"), "select", true, null,this._newServices);
				this.endRow();
				this.startRow(null);
					this.startColumn("font-size:0.8em");this.respondString("PRESENCE CAMPAIGN LOAD");this.endColumn();
					this.startCell("id=keyserviceloadid".split("[|]"));
						Database.executeDBRequest(null, "PRESENCE", "SELECT LOADID,'('+UPPER(LOADID)+':'+STATUS+')'+ DESCRIPTION AS LOADNAME FROM <DBUSER>.PCO_LOAD WHERE SERVICEID=:KEYWORDSERVICEID AND LOADID=:KEYWORDLOADID",this._selectedShortCodeSettings, this,"rowNewServiceLoadsData");
						if(!_newServiceLoads.isEmpty()){
							this.respondString(this._newServiceLoads.get(this._newServiceLoads.keySet().toArray()[0]));
						}
					this.endCell();
					this.startCell(null);
						this.action("LOADS", "keywordserviceloads", "", "", "keyserviceloadid", "", "", "");
					this.endCell();
				this.endRow();
			this.endTable();
		this.endReplaceComponentContent();
	}
	
	public void confirmkeyword() throws Exception{
		ArrayList<String> errors=new ArrayList<String>();
		this._selectedShortCodeSettings.clear();
		this.importRequestParametersIntoMap(_selectedShortCodeSettings, "SHORTCODEID,SELECTEDKEYWORDID,KEYWORDSERVICEID,KEYWORDLOADID");
		if(this._selectedShortCodeSettings.get("KEYWORDSERVICEID").equals("")){
			errors.add("NO CAMPAIGN SELECTED");
		}
		else if(this._selectedShortCodeSettings.get("KEYWORDLOADID").equals("")){
			errors.add("NO CAMPAIGN LOAD SELECTED");
		}
		else{
			this._selectedShortCodeSettings.put("SHORTCODECOUNT", "0");
			Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(*) AS SHORTCODECOUNT FROM <DBUSER>.LEADSDATAFILESHORTCODEMAP WHERE DEFAULTSERVICEID=:KEYWORDSERVICEID AND DEFAULTLOADID=:KEYWORDLOADID", this._selectedShortCodeSettings, null);
			if(this._selectedShortCodeSettings.get("SHORTCODECOUNT").equals("0")){
				Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(*) AS SHORTCODECOUNT FROM <DBUSER>.LEADSDATAFILESHORTCODEMAP WHERE ID=:SHORTCODEID AND DEFAULTSERVICEID=:KEYWORDSERVICEID AND DEFAULTLOADID=:KEYWORDLOADID", this._selectedShortCodeSettings, null);
				if(this._selectedShortCodeSettings.get("SHORTCODECOUNT").equals("0")){
					this._selectedShortCodeSettings.put("ALTERNATESHORTCODEKEYWORDCOUNT", "0");
					Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(*) AS ALTERNATESHORTCODEKEYWORDCOUNT FROM <DBUSER>.LEADSDATAFILESHORTCODEKEYWORDMAP WHERE SHORTCODEID<>:SHORTCODEID AND DEFAULTSERVICEID=:KEYWORDSERVICEID AND DEFAULTLOADID=:KEYWORDLOADID", this._selectedShortCodeSettings, null);
					if(this._selectedShortCodeSettings.get("ALTERNATESHORTCODEKEYWORDCOUNT").equals("0")){
						this._selectedShortCodeSettings.put("SHORTCODEALTERNATEKEYWORDCOUNT", "0");
						Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(*) AS SHORTCODEALTERNATEKEYWORDCOUNT FROM <DBUSER>.LEADSDATAFILESHORTCODEKEYWORDMAP WHERE SHORTCODEID=:SHORTCODEID AND DEFAULTSERVICEID=:KEYWORDSERVICEID AND DEFAULTLOADID=:KEYWORDLOADID AND ID<>:SELECTEDKEYWORDID", this._selectedShortCodeSettings, null);
						if(this._selectedShortCodeSettings.get("SHORTCODEALTERNATEKEYWORDCOUNT").equals("0")){
							Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "UPDATE <DBUSER>.LEADSDATAFILESHORTCODEKEYWORDMAP SET DEFAULTSERVICEID=:KEYWORDSERVICEID,DEFAULTLOADID=:KEYWORDLOADID WHERE SHORTCODEID=:SHORTCODEID AND ID=:SELECTEDKEYWORDID", this._selectedShortCodeSettings, null);
							this._selectedShortCodeSettings.clear();
							this.replaceComponentContent("shortcodekeywords");
								this.shortcodekeywords();
							this.endReplaceComponentContent();
						}
						else{
							errors.add("SELECTED CAMPAIGN AND LOAD ALLOCATED TO ANOTHER KEYWORD OF THIS SHORTCODE");
						}
					}
					else{
						errors.add("SELECTED CAMPAIGN AND LOAD ALLOCATED TO KEYWORD OF A DIFFERENT SHORTCODE");
					}
				}
				else{
					errors.add("SELECTED CAMPAIGN AND LOAD ALLOCATED TO SHORTCODE");
				}
			}
			else{
				errors.add("SELECTED CAMPAGN AND LOAD ALLOCATED TO A SHORTCODE");
			}
		}
		if(!errors.isEmpty()){
			this.showDialog("", "title=EDIT KEYWORD CAMPAIGN SETTINGS|contentid=confirmkeyword".split("[|]"));
			this.replaceComponentContent("confirmkeyword");
				this.startTable(null);
					while(!errors.isEmpty()){
						this.startRow(null);
							this.startCell(null);this.respondString(errors.remove(0));this.endCell();
						this.endRow();
					}
				this.endTable();
			this.endReplaceComponentContent();
		}
	}
	
	public void rowMappedShortcodes(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns) throws Exception{
		if(rowIndex==0) return;
		this.startRow(null);
			this.startColumn(null);
				this.action("EDIT", "leadsimportshortcode", "", "", "leadsimportshortcode", "", "", "selectedshortcodeid="+rowData.get(0));
			this.endColumn();
			this.startColumn("font-size:0.8em");
				this.respondString(rowData.get(1)); //SHORTCODE
			this.endColumn();
			this.startColumn("font-size:0.8em");
				this.respondString(rowData.get(2)); //SERVICEID
			this.endColumn();
			this.startColumn("font-size:0.8em");
				this.respondString(rowData.get(3)); //LOADID
			this.endColumn();
		this.endRow();
	}
	
	public void addshortcode() throws Exception{
		Database.executeDBRequest(null, "PRESENCE", "SELECT ID,'('+UPPER(ID) +') ' +NAME AS SERVICENAME FROM <DBUSER>.PCO_OUTBOUNDSERVICE", null, this,"rowNewServicesData");
		
		this.showDialog("", "contentid=addchortcodefields|contenttitle=ADD SHORT CODE|BUTTON:CONFIRM ADD SHORT CODE=command=confirmaddingshortcode|BUTTON:CANCEL=".split("[|]"));
		this.replaceComponentContent("addchortcodefields");
			this.startTable(null);
				this.startRow(null);
					this.startColumn("font-size:0.8em");this.respondString("SHORT CODE");this.endColumn();
					this.startCell(null);this.fieldInput("NEWSHORTCODE", "", "text", true, null);
				this.endRow();
				this.startRow(null);
					this.startColumn("font-size:0.8em");this.respondString("PRESENCE CAMPAIGN");this.endColumn();
					this.startCell(null);this.fieldInput("NEWSERVICEID", "", "select", true, null,this._newServices);
					this.startCell(null);
						this.action("LOADS", "newserviceloads", "", "", "newserviceloadid", "", "", "");
					this.endCell();
				this.endRow();
				this.startRow(null);
					this.startCell("id=newserviceloadid|colspan=3".split("[|]"));
					this.endCell();
				this.endRow();
			this.endTable();
		this.endReplaceComponentContent();
	}
	
	public void newserviceloads() throws Exception{
		if(this.requestParameter("NEWSERVICEID").equals("")){
			this.respondString("NO CAMPAIGN SELECTED");
		}
		else{
			Database.executeDBRequest(null, "PRESENCE", "SELECT LOADID,'('+UPPER(LOADID)+':'+STATUS+')'+ DESCRIPTION AS LOADNAME FROM <DBUSER>.PCO_LOAD WHERE SERVICEID="+this.requestParameter("NEWSERVICEID"), null, this,"rowNewServiceLoadsData");
			this.startCell(null);this.fieldInput("NEWLOADID", "", "select", true, null,_newServiceLoads);
		}
	}
	
	private HashMap<String,String> _newServices=new HashMap<String, String>();
	
	public void rowNewServicesData(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns){
		if(rowIndex==0) return;
		_newServices.put(rowData.get(0), rowData.get(1));
	}
	
	public void keywordserviceloads() throws Exception{
		if(this.requestParameter("KEYWORDSERVICEID").equals("")){
			this.respondString("NO CAMPAIGN SELECTED");
		}
		else{
			Database.executeDBRequest(null, "PRESENCE", "SELECT LOADID,'('+UPPER(LOADID)+':'+STATUS+')'+ DESCRIPTION AS LOADNAME FROM <DBUSER>.PCO_LOAD WHERE SERVICEID="+this.requestParameter("KEYWORDSERVICEID"), null, this,"rowNewServiceLoadsData");
			this.startCell(null);this.fieldInput("KEYWORDLOADID", "", "select", true, null,_newServiceLoads);
		}
	}
	
	public void rowNewServiceLoadsData(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns){
		if(rowIndex==0) return;
		_newServiceLoads.put(rowData.get(0), rowData.get(1));
	}
	
	private HashMap<String,String> _newServiceLoads=new HashMap<String, String>();
	
	public void confirmaddingshortcode() throws Exception{
		HashMap<String,String> shortcodeParameters=new HashMap<String, String>();
		ArrayList<String> errors=new ArrayList<String>();
		if(this.requestParameter("NEWSHORTCODE").equals("")){
			errors.add("NO SHORTCODE ENTERED");
		}
		else{
			shortcodeParameters.put("NEWSHORTCODECOUNT", "0");
			shortcodeParameters.put("NEWSHORTCODE", this.requestParameter("NEWSHORTCODE"));
			Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(*) AS NEWSHORTCODECOUNT FROM <DBUSER>.LEADSDATAFILESHORTCODEMAP WHERE UPPER(SHORTCODE)=UPPER(:NEWSHORTCODE)" , shortcodeParameters, this);
			if(!shortcodeParameters.get("NEWSHORTCODECOUNT").equals("0")){
				errors.add("SHORT CODE ["+this.requestParameter("NEWSHORTCODE")+"] ALREADY EXIST");
			}
		}
		if(!this.requestParameter("NEWSHORTCODE").equals("")&&this.requestParameter("NEWSERVICEID").equals("")){
			errors.add("NO CAMPIAGN SELECTED");
		}
		if(!this.requestParameter("NEWSHORTCODE").equals("")&&!this.requestParameter("NEWSERVICEID").equals("")&&this.requestParameter("NEWLOADID").equals("")){
			errors.add("NO LOAD SELECTED");
		}
		if(errors.isEmpty()){
			HashMap<String,String> addshortcodeparams=new HashMap<String, String>();
			addshortcodeparams.put("NEWSHORTCODE",this.requestParameter("NEWSHORTCODE"));
			addshortcodeparams.put("NEWSERVICEID",this.requestParameter("NEWSERVICEID"));
			addshortcodeparams.put("NEWLOADID",this.requestParameter("NEWLOADID"));
			Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "INSERT INTO <DBUSER>.LEADSDATAFILESHORTCODEMAP (SHORTCODE,DEFAULTSERVICEID,DEFAULTLOADID) SELECT UPPER(:NEWSHORTCODE) AS SHORTCODE,:NEWSERVICEID AS DEFAULTSERVICEID,:NEWLOADID AS DEFAULTLOADID WHERE (SELECT COUNT(*) FROM <DBUSER>.LEADSDATAFILESHORTCODEMAP WHERE UPPER(SHORTCODE)=UPPER(:NEWSHORTCODE))=0 AND (SELECT COUNT(*) FROM <DBUSER>.LEADSDATAFILESHORTCODEKEYWORDMAP WHERE DEFAULTSERVICEID=:NEWSERVICEID AND DEFAULTLOADID=:NEWLOADID)=0 AND (SELECT COUNT(*) FROM <DBUSER>.LEADSDATAFILESHORTCODEMAP WHERE UPPER(SHORTCODE)<>UPPER(:NEWSHORTCODE) AND DEFAULTSERVICEID=:NEWSERVICEID AND DEFAULTLOADID=:NEWLOADID)=0", addshortcodeparams	, this);
			this.replaceComponentContent("mappedshortcodes");
				this.mappedshortcodes();
			this.endReplaceComponentContent();
		}else{
			showDialog("", "contentid=unabletoaddshortcode|contenttitle=UNABLE TO ADD SHORTCODE".split("[|]"));
			this.replaceComponentContent("unabletoaddshortcode");
				this.startTable(null);
					while(!errors.isEmpty()){
						this.startRow(null);
							this.startCell(null);this.respondString(errors.remove(0));this.endCell();
						this.endRow();
					}
				this.endTable();
			this.endReplaceComponentContent();
			
		}
	}
}
