package inovo.flat.file.leads.importer.reports;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class FileImportReportConfiguration extends InovoHTMLPageWidget {

	public FileImportReportConfiguration(InovoWebWidget parentWidget,
			InputStream inStream) {
		super(parentWidget, inStream);
	}

	@Override
	public void pageContent() throws Exception {
		this.action("REGISTER NEW FILE IMPORT ALIAS", "registerNewFileImportAllias", "", "", "", "", "", "");
		this.startElement("div", "id=registeredReports".split("[|]"), true);
			this.registeredReports();
		this.endElement("div", true);
	}
	
	public void registeredReports() throws Exception{
		this.startTable(null);
			this.startRow(null);
				this.startColumn(null);
					this.respondString("REGISTER REPORTS");
				this.endColumn();
				this.startColumn(null);
					this.respondString("REPORT");
				this.endColumn();
			this.endRow();
			this.startRow(null);
				this.startCell("style=vertical-align:top".split("[|]"));
					this.startTable(null);
						Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT LEADSDATAFILEALLIASREPORT.ID AS REPORTID,LEADSDATAFILEALLIASREPORT.FLATFILEIMPORTALLIASID,ALIAS FROM <DBUSER>.LEADSDATAFILEALLIASREPORT INNER JOIN <DBUSER>.LEADSDATAFILEALIAS ON LEADSDATAFILEALLIASREPORT.FLATFILEIMPORTALLIASID=LEADSDATAFILEALIAS.ID" , null, this, "readRegisteredReports");	
					this.endTable();
				this.endCell();
				this.startCell("id=selectedRegisteredReport|style=vertical-align:top".split("[|]"));
				this.endCell();
			this.endRow();
		this.endTable();
	}
	
	public void readRegisteredReports(Integer rowindex,ArrayList<String> rowData,ArrayList<String> rowColumns) throws Exception{
		if(rowindex==0) return;
		this.startRow(null);
			this.startCell(null);
				this.action(rowData.get(2), "selectedRegisteredReport", "", "", "selectedRegisteredReport", "", "", "REPORTID="+rowData.get(0)+"&FLATFILEALLIASID="+rowData.get(1));
			this.endCell();
		this.endRow();
	}
	
	public void selectedRegisteredReport() throws Exception{
		_currentSelectedReportSettings.clear();
		this.importRequestParametersIntoMap(_currentSelectedReportSettings, null);
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT LEADSDATAFILEALLIASREPORT.ID AS REPORTID,LEADSDATAFILEALLIASREPORT.FLATFILEIMPORTALLIASID,ALIAS,LEADSDATAFILEALLIASREPORT.REPORTEXPORTENABLED,LEADSDATAFILEALLIASREPORT.REPORTFILEEXPORTPATH,LEADSDATAFILEALLIASREPORT.REPORTFILEPREFIX FROM <DBUSER>.LEADSDATAFILEALLIASREPORT INNER JOIN <DBUSER>.LEADSDATAFILEALIAS ON LEADSDATAFILEALLIASREPORT.FLATFILEIMPORTALLIASID=LEADSDATAFILEALIAS.ID AND LEADSDATAFILEALLIASREPORT.ID=:REPORTID", _currentSelectedReportSettings, this, "readSelectedRegisteredReport");
	}
	
	private HashMap<String,String> _currentSelectedReportSettings=new HashMap<String, String>();
	public void readSelectedRegisteredReport(Integer rowindex,ArrayList<String> rowData,ArrayList<String> rowColumns) throws Exception{
		if(rowindex==0) return;
		_currentSelectedReportSettings.clear();
		_currentSelectedReportSettings.putAll(Database.rowData(rowColumns, rowData));
		
		this.startElement("div", "class=ui-widget-header".split("[|]"), true);
			this.action((_currentSelectedReportSettings.get("REPORTEXPORTENABLED").equals("E")?"DISABLE REPORT":"ENABLE REPORT"), "enabledisableselectedreport", "", "", "", "", "", "REPORTID="+rowData.get(0)+"&FLATFILEALLIASID="+rowData.get(1)+"&REPORTEXPORTENABLED="+(_currentSelectedReportSettings.get("REPORTEXPORTENABLED").equals("E")?"D":"E"));
			this.respondString("FILE EXPORT SELECTED - "+_currentSelectedReportSettings.get("ALIAS"));
		this.endElement("div", true);
		
		this.startTable(null);
			this.startRow(null);
				this.startColumn("font-size:0.8em");
					this.respondString("REPORT FILE EXPORT PATH");
				this.endColumn();
				this.startCell(null);
					this.fieldInput("REPORTFILEEXPORTPATH", _currentSelectedReportSettings.get("REPORTFILEEXPORTPATH"), "text",! _currentSelectedReportSettings.get("REPORTEXPORTENABLED").equals("E"),null);
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startColumn("font-size:0.8em");
					this.respondString("REPORT FILE PREFIX");
				this.endColumn();
				this.startCell(null);
					this.fieldInput("REPORTFILEPREFIX", _currentSelectedReportSettings.get("REPORTFILEPREFIX"), "text",! _currentSelectedReportSettings.get("REPORTEXPORTENABLED").equals("E"),null);
				this.endCell();
			this.endRow();
		this.endTable();
	}
	
	public void enabledisableselectedreport() throws Exception{
		//String enableDisableReport=this.requestParameter("REPORTEXPORTENABLED");
		//String emportFilePath=this.requestParameter("REPORTFILEEXPORTPATH");
		//String emportFilePrefixe=this.requestParameter("REPORTFILEEXPORTPATH");
		
		this._currentSelectedReportSettings.clear();
		
		this.importRequestParametersIntoMap(_currentSelectedReportSettings, "REPORTID,FLATFILEALLIASID,REPORTEXPORTENABLED,REPORTFILEEXPORTPATH,REPORTFILEPREFIX");
		
		if((!this._currentSelectedReportSettings.get("REPORTFILEEXPORTPATH").equals(""))&&(!this._currentSelectedReportSettings.get("REPORTFILEPREFIX").equals(""))&&this._currentSelectedReportSettings.get("REPORTEXPORTENABLED").equals("E")){
			File ftest=null;
			if((ftest=new File(this._currentSelectedReportSettings.get("REPORTFILEEXPORTPATH"))).isDirectory()){
				if(ftest.exists()){
					Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "UPDATE <DBUSER>.LEADSDATAFILEALLIASREPORT SET REPORTEXPORTENABLED=:REPORTEXPORTENABLED,REPORTFILEPREFIX=:REPORTFILEPREFIX,REPORTFILEEXPORTPATH=:REPORTFILEEXPORTPATH WHERE ID=:REPORTID",this._currentSelectedReportSettings , null);
				}
			}
		}
		else if(this._currentSelectedReportSettings.get("REPORTEXPORTENABLED").equals("D")){
			Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "UPDATE <DBUSER>.LEADSDATAFILEALLIASREPORT SET REPORTEXPORTENABLED=:REPORTEXPORTENABLED WHERE ID=:REPORTID",this._currentSelectedReportSettings , null);
		}
		this._currentSelectedReportSettings.clear();
		this.replaceComponentContent("selectedRegisteredReport");
			this.selectedRegisteredReport();
		this.endReplaceComponentContent();
	}
	
	public void registerNewFileImportAllias() throws Exception{
		this.showDialog("", "title=REGISTER NEW FILE IMPORT ALIAS|contentid=registerNewFileImportAlliasdlg|BUTTON:CONFIRM=command=confirmRegisterNewFileImportAllias|BUTTON:CANCEL=".split("[|]"));
		this.replaceComponentContent("registerNewFileImportAlliasdlg");
			this.startTable(null);
				this.startRow(null);
					this.startCell("style=text-align:right;font-size:0.8em|class=ui-widget-header".split("[|]"));
						this.respondString("FILE LEADS IMPORT ALIAS");
					this.endCell();
					this.startCell("style=vertical-align:top".split("[|]"));
						this.fieldInput("NEWFILEIMPORTALIAS", "", "select", true,null, this.availableFileAliasses());
					this.endCell();
				this.endRow();
			this.endTable();
		this.endReplaceComponentContent();
	}
	
	private HashMap<String,String> _availableFileAliasses=new HashMap<String, String>();
	private HashMap<String,String> availableFileAliasses() throws Exception{
		_availableFileAliasses.clear();
		try {
			Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT ID,LEADSDATAFILEALIAS.ALIAS FROM <DBUSER>.LEADSDATAFILEALIAS WHERE (SELECT COUNT(*) FROM <DBUSER>.LEADSDATAFILEALLIASREPORT WHERE FLATFILEIMPORTALLIASID=LEADSDATAFILEALIAS.ID)=0", null, this, "readUnregisteredFileImportsData");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _availableFileAliasses;
	}
	
	public void readUnregisteredFileImportsData(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns) throws Exception{
		if(rowIndex==0) return;
		_availableFileAliasses.put(rowData.get(0), rowData.get(1));
	}
	
	public void confirmRegisterNewFileImportAllias() throws Exception{
		String NEWFILEIMPORTALIAS="";
		if((NEWFILEIMPORTALIAS=this.requestParameter("NEWFILEIMPORTALIAS")).equals("")){
			return;
		}
		else{
			Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "INSERT INTO PTOOLS.LEADSDATAFILEALLIASREPORT (FLATFILEIMPORTALLIASID) SELECT "+NEWFILEIMPORTALIAS+" AS FLATFILEIMPORTALLIASID WHERE (SELECT COUNT(*) FROM  PTOOLS.LEADSDATAFILEALLIASREPORT WHERE FLATFILEIMPORTALLIASID="+NEWFILEIMPORTALIAS+")=0", null, null, null);
		}
		this.replaceComponentContent("registeredReports");
			this.registeredReports();
		this.endReplaceComponentContent();
	}
}
