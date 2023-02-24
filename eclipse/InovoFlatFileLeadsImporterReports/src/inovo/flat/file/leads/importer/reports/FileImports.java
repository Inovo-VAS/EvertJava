package inovo.flat.file.leads.importer.reports;

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

public class FileImports extends InovoHTMLPageWidget {

	public FileImports(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

	@Override
	public void pageContent() throws Exception {
		this.dateFromDateToSearchGUI();
	}
	
	public void dateFromDateToSearchGUI() throws Exception{
		this.setRequestParameter("FROMDATE", new SimpleDateFormat("yyyy-MM-dd").format(new Date()), true);
		this.setRequestParameter("TODATE", new SimpleDateFormat("yyyy-MM-dd").format(new Date()), true);
		
		this.startTable(null);
			this.startRow(null);
				this.startCell(null);
					this.action("SEARCH","searchFileImports","","","searchFileImports","","","");
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
				
					this.startTable(null);
						this.startRow(null);
							this.startCell(null);
								this.fieldLabel("FROM");
							this.endCell();
							this.startCell(null);
								this.fieldInput("FROMDATE", this.requestParameter("FROMDATE"), "date", true, null);
							this.endCell();
							
							this.startCell(null);
								this.fieldLabel("TO");
							this.endCell();
							this.startCell(null);
								this.fieldInput("TODATE",  this.requestParameter("TODATE") , "date", true, null);
							this.endCell();
						this.endRow();
					this.endTable();
						
				this.endCell();
			this.endRow();
		this.endTable();
		this.startElement("div", "id=searchFileImports".split("[|]"), true);
			this.searchFileImports();
		this.endElement("div", true);
	}
	
	private HashMap<String,String> _fileImportJobsFound=new HashMap<String, String>();
	public void searchFileImports() throws Exception{
		this._fileImportJobsFound.clear();
		this.importRequestParametersIntoMap(this._fileImportJobsFound, "FROMDATE,TODATE");
		//this.respondString(this._fileImportJobsFound.get("FROMDATE")+" "+this._fileImportJobsFound.get("TODATE"));
		this._fileImportJobsFound.put("TODATE", this._fileImportJobsFound.get("TODATE")+" 23:59:59.999");
		
		this.startTable(null);	
			this.startRow(null);
				this.startColumn("font-size:0.8em");
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respondString("FILEALIAS");
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respondString("FILE");
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respondString("START TIME");
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respondString("TOTAL_DURATION (in seconds)");
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respondString("TOTAL_RECORDS_IMPORTED");
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respondString("TOTAL_ADD_CALL_REQUESTS");
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respondString("TOTAL_REMOVED_CALL_REQUESTS");
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respondString("TOTAL_DUPLICATES");
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respondString("TOTAL_NO_PHONE_NUMBERS");
				this.endColumn();
			this.endRow();
			TreeMap<Integer,ArrayList<String>> flatFileImportJobsSet=new TreeMap<Integer, ArrayList<String>>();
			//Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT * FROM <DBUSER>.LEADSDATAFILEALLIASREQUEST WHERE REQUESTCREATIONDATE BETWEEN :FROMDATE AND :TODATE", this._fileImportJobsFound, this, "readFileImportJobRequests");
			Database.executeDBRequest(flatFileImportJobsSet, "FLATFILELEADSIMPORTER", "SELECT * FROM <DBUSER>.LEADSDATAFILEALLIASREQUEST WHERE REQUESTCREATIONDATE BETWEEN :FROMDATE AND :TODATE", this._fileImportJobsFound, null,null);
			ArrayList<String> columns=new ArrayList<String>();
			ArrayList<String> rowData=new ArrayList<String>();
			Integer rowIndex=0;
			while(!flatFileImportJobsSet.isEmpty()){
				rowIndex=flatFileImportJobsSet.firstKey();
				rowData.clear();
				rowData.addAll(flatFileImportJobsSet.remove((Integer)rowIndex));
				if(rowIndex==0){
					columns.clear();
					columns.addAll(rowData);
				}
				this.readFileImportJobRequests(rowIndex, rowData, columns);
			}
			flatFileImportJobsSet.clear();
			
		this.endTable();
		//this.respondString("searchFileImports");
	}
	
	private HashMap<String,String> _flatFileReportJobSettings=new HashMap<String, String>();
	
	private long mintimestamp=0;
	private long maxlasttimestamp=0;
	
	private long totalrecimported=0;
	private long totalrecadded=0;
	private long totalremoved=0;
	private long totalduplicates=0;
	private long totalnonumbers=0;
	
	public void readFileImportJobRequests(Integer rowindex,ArrayList<String> rowData,ArrayList<String> rowColumns) throws Exception{
		if(rowindex==0){
			return;
		}
		_flatFileReportJobSettings.clear();
		_flatFileReportJobSettings.putAll(Database.rowData(rowColumns, rowData));
		
		//Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT ALIAS FROM <DBUSER>.LEADSDATAFILEALLIASREQUEST WHERE ID=:ID", _flatFileReportJobSettings, null);
		
		_flatFileReportJobSettings.put("ALIAS", "");
		_flatFileReportJobSettings.put("TOTALCONTENTRECORDSIMPORTED", "0");
		_flatFileReportJobSettings.put("TOTALCONTENTIMPORTDURATION", "0");
		
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT ALIAS FROM <DBUSER>.LEADSDATAFILEALLIASREQUEST WHERE ID=:ID", _flatFileReportJobSettings, null);
		
		//Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT DATEDIFF(SECOND,MIN(LEADSDATAFILEALLIASREQUESTCONTENT.ENTRYCREATIONDATETIME),MAX(LEADSDATAFILEALLIASREQUESTCONTENT.ENTRYLASTACTIONDATETIME)) AS TOTALCONTENTIMPORTDURATION FROM <DBUSER>.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:ID", _flatFileReportJobSettings, null);
		
		//Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT SUM(VALCOL) AS TOTALCONTENTRECORDSIMPORTED FROM (SELECT 1 AS VALCOL FROM <DBUSER>.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:ID) VALSET", _flatFileReportJobSettings, null);
		
		//_flatFileReportJobSettings.put("MINCREATIONDATE", "0");
		//_flatFileReportJobSettings.put("MAXCREATIONDATE", "0");
		//Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT DATEDIFF(SECOND,MINCREATIONDATE,MAXCREATIONDATE) AS TOTALCONTENTIMPORTDURATION FROM (SELECT MIN(LEADSDATAFILEALLIASREQUESTCONTENT.ENTRYCREATIONDATETIME) AS MINCREATIONDATE, MAX(LEADSDATAFILEALLIASREQUESTCONTENT.ENTRYLASTACTIONDATETIME) AS MAXCREATIONDATE FROM <DBUSER>.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:ID) MINMAXDATE", _flatFileReportJobSettings, null);
		
		/*
		
		
		_flatFileReportJobSettings.put("TOTALADDCALLREQUESTS", "0");
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(*) AS TOTALADDCALLREQUESTS FROM <DBUSER>.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:ID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=3", _flatFileReportJobSettings, null);
		
		_flatFileReportJobSettings.put("TOTALREMOVECALLREQUESTS", "0");
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(*) AS TOTALREMOVECALLREQUESTS FROM <DBUSER>.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:ID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=2 AND ENTRYHANDLEFLAG=3", _flatFileReportJobSettings, null);
		
		_flatFileReportJobSettings.put("TOTALDUPLICATES", "0");
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(*) AS TOTALDUPLICATES FROM SQLPR1.PTOOLS.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:ID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=4", _flatFileReportJobSettings, null);
		
		_flatFileReportJobSettings.put("TOTALNONUMBERS","0");
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(*) AS TOTALNONUMBERS FROM SQLPR1.PTOOLS.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:ID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=8", _flatFileReportJobSettings, null);
		*/
		
		_flatFileReportJobSettings.put("TOTALADDCALLREQUESTS", "0");
		_flatFileReportJobSettings.put("TOTALREMOVECALLREQUESTS", "0");
		_flatFileReportJobSettings.put("TOTALDUPLICATES", "0");
		_flatFileReportJobSettings.put("TOTALNONUMBERS","0");
		
		//Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT SUM(VALCOL) AS TOTALADDCALLREQUESTS FROM (SELECT 1 AS VALCOL FROM <DBUSER>.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:ID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=3) VALSET", _flatFileReportJobSettings, null);
		//Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(ID)  AS TOTALREMOVECALLREQUESTS FROM <DBUSER>.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:ID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=2 AND ENTRYHANDLEFLAG=3", _flatFileReportJobSettings, null);
		//Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(ID) AS TOTALDUPLICATES FROM SQLPR1.PTOOLS.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:ID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=4", _flatFileReportJobSettings, null);
		//Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(ID) AS TOTALNONUMBERS FROM SQLPR1.PTOOLS.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:ID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=8", _flatFileReportJobSettings, null);

		totalrecadded=0;
		totalremoved=0;
		totalduplicates=0;
		totalnonumbers=0;
		
		mintimestamp=0;
		maxlasttimestamp=0;
		
		
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT ENTRYCREATIONDATETIME,ENTRYLASTACTIONDATETIME,ENTRYSTATUS,ENTRYLEADREQUESTTYPE,ENTRYHANDLEFLAG FROM <DBUSER>.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:ID", _flatFileReportJobSettings,this,"rowDataProcessReport");

		totalrecimported=totalrecadded+totalremoved+totalduplicates+totalnonumbers;
		
		_flatFileReportJobSettings.put("TOTALADDCALLREQUESTS",String.valueOf(totalrecadded));
		_flatFileReportJobSettings.put("TOTALREMOVECALLREQUESTS",String.valueOf(totalremoved));
		_flatFileReportJobSettings.put("TOTALDUPLICATES",String.valueOf(totalduplicates));
		_flatFileReportJobSettings.put("TOTALNONUMBERS",String.valueOf(totalnonumbers));
		
		_flatFileReportJobSettings.put("TOTALCONTENTRECORDSIMPORTED",String.valueOf(totalrecimported));
		
		maxlasttimestamp=(maxlasttimestamp-mintimestamp)/1000;
		
		_flatFileReportJobSettings.put("TOTALCONTENTIMPORTDURATION",String.valueOf(maxlasttimestamp));
		
		this.startRow(null);
			this.startColumn("font-size:0.8em");
				this.respondString(_flatFileReportJobSettings.get("REQUESTFILEIMPORTSTATUS").equals("0")?"DONE":"BUSY PROCESSING");
			this.endColumn();
			this.startColumn("font-size:0.8em");
				this.respondString(_flatFileReportJobSettings.get("ALIAS"));
			this.endColumn();
			this.startColumn("font-size:0.8em");
				this.respondString(_flatFileReportJobSettings.get("REQUESTFILENAME"));
			this.endColumn();
			this.startColumn("font-size:0.8em");
				this.respondString(_flatFileReportJobSettings.get("REQUESTCREATIONDATE"));
			this.endColumn();
			this.startColumn("font-size:0.8em");
				this.respondString(_flatFileReportJobSettings.get("TOTALCONTENTIMPORTDURATION"));
			this.endColumn();
			this.startColumn("font-size:0.8em");
				this.respondString(_flatFileReportJobSettings.get("TOTALCONTENTRECORDSIMPORTED"));
			this.endColumn();
			this.startColumn("font-size:0.8em");
				this.respondString(_flatFileReportJobSettings.get("TOTALADDCALLREQUESTS"));
			this.endColumn();
			this.startColumn("font-size:0.8em");
				this.respondString(_flatFileReportJobSettings.get("TOTALREMOVECALLREQUESTS"));
			this.endColumn();
			this.startColumn("font-size:0.8em");
				this.respondString(_flatFileReportJobSettings.get("TOTALDUPLICATES"));
			this.endColumn();
			this.startColumn("font-size:0.8em");
				this.respondString(_flatFileReportJobSettings.get("TOTALNONUMBERS"));
			this.endColumn();
		this.endRow();
		
	}
	
	public void rowDataProcessReport(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex==0) return;
		
		
		
		Calendar currentcreatetimestamp=Calendar.getInstance();
		currentcreatetimestamp.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(data.get(0)));
		
		Calendar currentlasttimestamp=Calendar.getInstance();
		currentlasttimestamp.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(data.get(1)));
		
		if(mintimestamp==0){
			mintimestamp=currentcreatetimestamp.getTimeInMillis();
		}
		else if(mintimestamp>currentcreatetimestamp.getTimeInMillis()){
			mintimestamp=currentcreatetimestamp.getTimeInMillis();
		}
		
		if(maxlasttimestamp==0){
			maxlasttimestamp=currentlasttimestamp.getTimeInMillis();
		}
		else if(maxlasttimestamp<currentlasttimestamp.getTimeInMillis()){
			maxlasttimestamp=currentlasttimestamp.getTimeInMillis();
		}
		
		//totalrecimported++;
		if(data.get(2).equals("1")&&data.get(3).equals("1")&&data.get(4).equals("3")){
			//ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=3
			totalrecadded++;
		}
		else if(data.get(2).equals("1")&&data.get(3).equals("2")&&data.get(4).equals("3")){
			//ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=2 AND ENTRYHANDLEFLAG=3
			totalremoved++;
		}
		else if(data.get(2).equals("1")&&data.get(3).equals("1")&&data.get(4).equals("4")){
			//ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=4
			totalduplicates++;
		}
		else if(data.get(2).equals("1")&&data.get(3).equals("1")&&data.get(4).equals("8")){
			//ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=8
			totalnonumbers++;
		}
	}
}
