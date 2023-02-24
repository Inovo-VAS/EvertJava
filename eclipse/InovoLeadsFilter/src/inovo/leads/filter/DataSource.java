package inovo.leads.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class DataSource extends InovoHTMLPageWidget {

	public DataSource(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

	@Override
	public void pageContent() throws Exception {
		this.startTable(null);
			this.startRow(null);
				this.startColumn("");
					respondString("DATA SOURCE");this.action("[add-datasource]", "add_datasource", "", "", "", "", "", "");
				this.endColumn();
			this.endRow();
			this.startRow(null);
				this.startCell("id=datasource_details|style=vertical-align:top".split("[|]"));
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startColumn("");
					this.respondString("REGISTERED DATASOURCE(s)");
				this.endColumn();
			this.endRow();
			this.startRow(null);
				this.startCell("id=registered_datasources|style=vertical-align:top".split("[|]"));
					this.registered_datasources();
				this.endCell();
			this.endRow();
		this.endTable();
	}

	public void registered_datasources() throws Exception{
		this.startTable(null);
			Database.executeDBRequest(null, "INOVOLEADSFILTER", "SELECT ID,ALIAS,USAGE_DESCRIPTION,STATUS FROM <DBUSER>.LEADS_FILTER_DATASOURCE", null, this,"registered_datasource_row");
		this.endTable();
	}
	
	public void registered_datasource_row(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex>0){
			this.startRow(null);
				this.startCell(null);
					if(data.get(3).equals("D")){
						this.action("[edit]","edit_datasource", "", "", "", "", "", "datasource_id="+data.get(0));
					}
				this.endCell();
				this.startCell(null);
					this.action(data.get(3).equals("E")?"[disable]":"[enable]",data.get(3).equals("E")?"disable_datasource":"enable_datasource", "", "", "", "", "", "datasource_id="+data.get(0));
				this.endCell();
				this.startColumn("font-size:0.6em");
					this.respondString(data.get(1)+" ("+data.get(2)+")");
				this.endColumn();
			this.endRow();
		}
	}
	
	public void add_datasource() throws Exception{
		this.replaceComponentContent("datasource_details");
			this.datasource_details("ADD DATASOURCE");
		this.endReplaceComponentContent();
	}
	
	public void edit_datasource() throws Exception{
		this.replaceComponentContent("datasource_details");
			this.datasource_details("EDIT DATASOURCE");
		this.endReplaceComponentContent();
	}
	
	public void enable_datasource() throws Exception{
		if(!this.requestParameter("DATASOURCE_ID").equals("")){
			HashMap<String,String> dataSourceSettings=new HashMap<String, String>();
			//ALIAS
			dataSourceSettings.put("ALIAS", "");
			//,SERVERNETNAME,DB,USERNAME,PASSWORD
			dataSourceSettings.put("SERVERNETNAME", "");
			dataSourceSettings.put("DB", "");
			dataSourceSettings.put("USERNAME", "");
			dataSourceSettings.put("PASSWORD", "");
			
			Database.executeDBRequest(null, "INOVOLEADSFILTER", "SELECT ALIAS,SERVERNETNAME,DB,USERNAME,PASSWORD FROM <DBUSER>.LEADS_FILTER_DATASOURCE WHERE ID="+this.requestParameter("DATASOURCE_ID"), dataSourceSettings, null);
			try{
				loadDataSourceDBCN(dataSourceSettings.get("ALIAS"), dataSourceSettings.get("SERVERNETNAME"), dataSourceSettings.get("DB"), dataSourceSettings.get("USERNAME"), dataSourceSettings.get("PASSWORD"));
				Database.executeDBRequest(null, "INOVOLEADSFILTER", "UPDATE <DBUSER>.LEADS_FILTER_DATASOURCE SET STATUS='E',LASTACTIONSTAMP=GETDATE() WHERE ID="+this.requestParameter("DATASOURCE_ID"), null, null);
				this.replaceComponentContent("registered_datasources");
					this.registered_datasources();
				this.endReplaceComponentContent();
			}
			catch(Exception e){
				this.showDialog("", "title=FAILED TO ENABLE DATASOURCE|contentid=enable_datasource_error".split("[|]"));
				this.replaceComponentContent("enable_datasource_error");
					this.startElement("div", "style=font-color:#FF0000;font-weight:bold;font-size:0.6em".split("[|]"),true);
						this.respondString("FAILED TO ENABLE ["+dataSourceSettings+"]:"+e.getMessage());
					this.endElement("div", true);
				this.endReplaceComponentContent();
			}
		}
	}
	
	public void disable_datasource() throws Exception{
		if(!this.requestParameter("DATASOURCE_ID").equals("")){
			Database.executeDBRequest(null, "INOVOLEADSFILTER", "UPDATE <DBUSER>.LEADS_FILTER_DATASOURCE SET STATUS='D',LASTACTIONSTAMP=GETDATE() WHERE ID="+this.requestParameter("DATASOURCE_ID"), null, null);
			this.replaceComponentContent("registered_datasources");
				this.registered_datasources();
			this.endReplaceComponentContent();
		}
		
	}
	
	public void datasource_details(String heading) throws Exception{
		HashMap<String,String> dataSourceSettings=new HashMap<String, String>();
		//ALIAS
		dataSourceSettings.put("ALIAS", "");
		dataSourceSettings.put("USAGE_DESCRIPTION","");
		//,SERVERNETNAME,DB,USERNAME,PASSWORD
		dataSourceSettings.put("SERVERNETNAME", "");
		dataSourceSettings.put("DBNAME", "");
		dataSourceSettings.put("USERNAME", "");
		dataSourceSettings.put("PASSWORD", "");
		dataSourceSettings.put("DB", "");
		dataSourceSettings.put("DBSCHEMA", "");
		
		if(!this.requestParameter("DATASOURCE_ID").equals("")){
			Database.executeDBRequest(null, "INOVOLEADSFILTER", "SELECT ALIAS,USAGE_DESCRIPTION,SERVERNETNAME,DB,USERNAME,PASSWORD,DBSCHEMA FROM <DBUSER>.LEADS_FILTER_DATASOURCE WHERE ID="+this.requestParameter("DATASOURCE_ID"), dataSourceSettings, null);
			this.fieldHidden("DATASOURCE_ID", this.requestParameter("DATASOURCE_ID"));
		}
		this.startTable(null);
			this.startRow(null);
				this.startColumn("");
					this.respondString(heading.toUpperCase());
				this.endColumn();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
					this.startTable(null);
					/*ALIAS  VARCHAR(900) NOT NULL,
					USAGE_DESCRIPTION VARCHAR(2000) NOT NULL,
					USERNAME VARCHAR(1000),
					PASSWORD VARCHAR(1000),
					DB VARCHAR(1000),
					SERVERNETNAME VARCHAR(1000),
					SCHEMA_DBLINK VARCHAR(2000)*/
						this.startRow(null);
							this.startColumn("font-size:0.6em");
								this.respondString("ALIAS");
							this.endColumn();
							this.startCell(null);
								this.fieldInput("ALIAS", dataSourceSettings.get("ALIAS"), "text", true,null);
							this.endCell();
						this.endRow();
						this.startRow(null);
							this.startColumn("font-size:0.6em");
								this.respondString("USAGE DESCRIPTION");
							this.endColumn();
							this.startCell(null);
								this.fieldInput("USAGE_DESCRIPTION", dataSourceSettings.get("USAGE_DESCRIPTION"), "text", true,null);
							this.endCell();
						this.endRow();
						this.startRow(null);
							this.startColumn("font-size:0.6em");
								this.respondString("USERNAME");
							this.endColumn();
							this.startCell(null);
								this.fieldInput("USERNAME", dataSourceSettings.get("USERNAME"), "text", true,null);
							this.endCell();
						this.endRow();
						this.startRow(null);
							this.startColumn("font-size:0.6em");
								this.respondString("PASSWORD");
							this.endColumn();
							this.startCell(null);
								this.fieldInput("PASSWORD", dataSourceSettings.get("PASSWORD"), "text", true,null);
							this.endCell();
						this.endRow();
						this.startRow(null);
							this.startColumn("font-size:0.6em");
								this.respondString("DATABASE (DB)");
							this.endColumn();
							this.startCell(null);
								this.fieldInput("DB", dataSourceSettings.get("DB"), "text", true,null);
							this.endCell();
						this.endRow();
						this.startRow(null);
							this.startColumn("font-size:0.6em");
								this.respondString("SERVER");
							this.endColumn();
							this.startCell(null);
								this.fieldInput("SERVER", dataSourceSettings.get("SERVERNETNAME"), "text", true,null);
							this.endCell();
						this.endRow();
						this.startRow(null);
							this.startColumn("font-size:0.6em");
								this.respondString("SCHEMA");
							this.endColumn();
							this.startCell(null);
								this.fieldInput("DBSCHEMA",dataSourceSettings.get("DBSCHEMA"), "text", true,null);
							this.endCell();
						this.endRow();
					this.endTable();
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
				this.action(heading.toUpperCase()+" (CONFIRM)", "confirm_"+heading.toLowerCase().replaceAll("[ ]", "_").toLowerCase()+"_details", "", "", "", "", "", "");
				this.action("CANCEL", "cancel_datasource_details", "", "", "datasource_details", "", "", "");
				this.endCell();
			this.endRow();
		this.endTable();
	}
	
	public void confirm_add_datasource_details() throws Exception{
		HashMap<String,String> datasourceDetails=new HashMap<String, String>();
		this.importRequestParametersIntoMap(datasourceDetails, null);
		ArrayList<String> errors=new ArrayList<String>();
		if(datasourceDetails.get("ALIAS").equals("")){
			errors.add("NO ALIAS ENTERED");
		}
		if(datasourceDetails.get("USAGE_DESCRIPTION").equals("")){
			errors.add("NO USAGE DESCRIPTION ENTERED");
		}
		if(datasourceDetails.get("USERNAME").equals("")){
			errors.add("NO USERNAME ENTERED");
		}
		if(datasourceDetails.get("PASSWORD").equals("")){
			errors.add("NO PASSWORD ENTERED");
		}
		if(datasourceDetails.get("DB").equals("")){
			errors.add("NO DATABASE (DB) ENTERED");
		}
		if(datasourceDetails.get("SERVER").equals("")){
			errors.add("NO SERVER ENTERED");
		}
		if(datasourceDetails.get("DBSCHEMA").equals("")){
			errors.add("NO SCHEMA ENTERED");
		}
		
		if(!errors.isEmpty()){
			this.showDialog("", "contentid=datasource_details_errors|title=UNABLE TO ADD DATASOURCE".split("[|]"));
			this.replaceComponentContent("datasource_details_errors");
				this.startElement("ul", "style=color:#FF0000".split("[|]"), true);
					while(!errors.isEmpty()){
						this.startElement("li", "style=color:#FF0000".split("[|]"), true);
							this.respondString(errors.remove(0));
						this.endElement("li", true);
					}
				this.endElement("ul", true);
			this.endReplaceComponentContent();
		}
		else{
			Database.executeDBRequest(null, "INOVOLEADSFILTER", "INSERT INTO <DBUSER>.[LEADS_FILTER_DATASOURCE] "+
					"([ALIAS],[USAGE_DESCRIPTION],[USERNAME],[PASSWORD],[DB],[SERVERNETNAME],[DBSCHEMA],[DBTYPE],[LASTUSERIP],[STATUS],[LASTACTIONSTAMP],[CREATESTAMP]) "+
					"SELECT :ALIAS,:USAGE_DESCRIPTION,:USERNAME,:PASSWORD,:DB,:SERVER,:DBSCHEMA,'sqlserver','','D',GETDATE(),GETDATE() WHERE (SELECT COUNT(*) FROM <DBUSER>.[LEADS_FILTER_DATASOURCE] WHERE ALIAS=:ALIAS)=0", datasourceDetails, null);
			this.replaceComponentContent("datasource_details");
				this.cancel_datasource_details();
			this.endReplaceComponentContent();
			this.replaceComponentContent("registered_datasources");
				this.registered_datasources();
			this.endReplaceComponentContent();
		}
	}
	
	public void confirm_edit_datasource_details() throws Exception{
		HashMap<String,String> datasourceDetails=new HashMap<String, String>();
		this.importRequestParametersIntoMap(datasourceDetails, null);
		ArrayList<String> errors=new ArrayList<String>();
		if(datasourceDetails.get("ALIAS").equals("")){
			errors.add("NO ALIAS ENTERED");
		}
		if(datasourceDetails.get("USAGE_DESCRIPTION").equals("")){
			errors.add("NO USAGE DESCRIPTION ENTERED");
		}
		if(datasourceDetails.get("USERNAME").equals("")){
			errors.add("NO USERNAME ENTERED");
		}
		if(datasourceDetails.get("PASSWORD").equals("")){
			errors.add("NO PASSWORD ENTERED");
		}
		if(datasourceDetails.get("DB").equals("")){
			errors.add("NO DATABASE (DB) ENTERED");
		}
		if(datasourceDetails.get("SERVER").equals("")){
			errors.add("NO SERVER ENTERED");
		}
		if(datasourceDetails.get("DBSCHEMA").equals("")){
			errors.add("NO SCHEMA ENTERED");
		}
		
		if(!errors.isEmpty()){
			this.showDialog("", "contentid=datasource_details_errors|title=UNABLE TO EDIT DATASOURCE".split("[|]"));
			this.replaceComponentContent("datasource_details_errors");
				this.startElement("ul", "style=color:#FF0000".split("[|]"), true);
					while(!errors.isEmpty()){
						this.startElement("li", "style=color:#FF0000".split("[|]"), true);
							this.respondString(errors.remove(0));
						this.endElement("li", true);
					}
				this.endElement("ul", true);
			this.endReplaceComponentContent();
		}
		else{
			try{
				Database.executeDBRequest(null, "INOVOLEADSFILTER", "UPDATE <DBUSER>.[LEADS_FILTER_DATASOURCE] "+
						"SET ALIAS=UPPER(:ALIAS), USAGE_DESCRIPTION=:USAGE_DESCRIPTION,USERNAME=:USERNAME,PASSWORD=:PASSWORD,DB=:DB,SERVERNETNAME=:SERVER,DBSCHEMA=:DBSCHEMA WHERE ID=:DATASOURCE_ID", datasourceDetails, null);
				this.replaceComponentContent("datasource_details");
					this.cancel_datasource_details();
				this.endReplaceComponentContent();
				this.replaceComponentContent("registered_datasources");
					this.registered_datasources();
				this.endReplaceComponentContent();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void cancel_datasource_details(){
	}
	
	public static void loadEnabledDatasources() throws Exception{
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "SELECT * FROM <DBUSER>.LEADS_FILTER_DATASOURCE WHERE STATUS='E'", null, DataSource.class,"loadDataSourceSettings");
	}
	
	private static StringBuilder _loadDataSourceSettings=new StringBuilder();
	
	public static void loadDataSourceSettings(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex>0){
			try{
				loadDataSourceDBCN(data.get(1), data.get(6), data.get(5), data.get(3), data.get(4));
			}
			catch(Exception e){
				Database.executeDBRequest(null, "INOVOLEADSFILTER", "UPDATE <DBUSER>.LEADS_FILTER_DATASOURCE SET STATUS='D' WHERE ID="+data.get(0),null,null);
			}
		}
	}
	
	public static void loadDataSourceDBCN(String alias,String dbhost,String dbname,String username,String password) throws Exception{
		if(_loadDataSourceSettings.length()>0){
			_loadDataSourceSettings.delete(0, _loadDataSourceSettings.length());
			_loadDataSourceSettings.setLength(0);
		}
		_loadDataSourceSettings.append(alias+"=DBTYPE=sqlserver;DBHOST="+(dbhost.indexOf("\\")==-1?dbhost:dbhost.substring(0, dbhost.lastIndexOf("\\")))+";DBINSTANCE="+(dbhost.indexOf("\\")==-1?"":dbhost.substring(dbhost.lastIndexOf("\\")+1,dbhost.length()))+";DBNAME="+dbname+";DBUSER="+username +";DBPW="+password);
		Database.registerDBAlliasFromConfigStream(new ByteArrayInputStream(_loadDataSourceSettings.substring(0,_loadDataSourceSettings.length()).getBytes()));
		Database.executeDBRequest(null, alias,"SELECT GETDATE()", null, null);
	}
}
