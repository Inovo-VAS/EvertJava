package inovo.flat.file.leads.importer.reports;

import inovo.db.Database;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class StatusReportQueue implements Runnable{

	private static StatusReportQueue _statusReportQueue=null;
	
	private ArrayList<String> _flatFileExportJobIds=new ArrayList<String>();
	private String _reportExportPath="";
	
	@Override
	public void run() {
		try {
			Thread.sleep(10*1024);
		} catch (InterruptedException e){
		}
		while(!this._shutdownQueue){
			try {
				this.generateReports();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(30*1024);
			} catch (InterruptedException e){
			}
		}
		this._busyShuttingDown=false;
	}
	
	private void generateReports() throws Exception{
		String todayDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT LEADSDATAFILEALLIASREQUEST.ID AS REQUESTJOBID,LEADSDATAFILEALLIASREQUEST.LEADSFILEALLIASID,LEADSDATAFILEALLIASREPORT.ID AS REPORTID,REPORTFILEPREFIX,REPORTFILEEXPORTPATH FROM <DBUSER>.LEADSDATAFILEALLIASREQUEST INNER JOIN <DBUSER>.LEADSDATAFILEALLIASREPORT ON REQUESTFILEIMPORTACTIVE=0 AND REQUESTHANDLEFLAG=3 AND REQUESTLASTHANDLEDATETIME BETWEEN '"+todayDate+" 00:00:00.000' AND '"+todayDate+" 23:59:59.999' AND LEADSDATAFILEALLIASREPORT.FLATFILEIMPORTALLIASID=LEADSDATAFILEALLIASREQUEST.LEADSFILEALLIASID AND LEADSDATAFILEALLIASREPORT.REPORTEXPORTENABLED='E' AND (SELECT COUNT(*) FROM <DBUSER>.LEADSDATAFILEALLIASREPORTREQUEST WHERE LEADSDATAFILEALLIASREPORTREQUEST.FLATFILEREQUESTID=LEADSDATAFILEALLIASREQUEST.ID AND LEADSDATAFILEALLIASREPORTREQUEST.FLATFILEALLIASREPORTID=LEADSDATAFILEALLIASREPORT.ID)=0",null,this,"readFlatFileExportJobs");
		
		Database.executeDBRequest(null,"FLATFILELEADSIMPORTER","SELECT [LEADSDATAFILEALLIASREPORTREQUEST].[ID],[REPORTREQUESTCREATEDATETIME],[FLATFILEREQUESTID] ,[FLATFILEALLIASREPORTID] ,[REPORTREQUESTACTIVE] ,[REPORTREQUESTLASTACTIONDATETIME] ,[REPORTEXPORTFILENAME] ,[REPORTEXPORTFILEPATH] ,[REPORTSTATUS],LEADSDATAFILEALLIASREQUEST.ALIAS,LEADSDATAFILEALLIASREQUEST.REQUESTFILENAME FROM <DBUSER>.[LEADSDATAFILEALLIASREPORTREQUEST] INNER JOIN <DBUSER>.LEADSDATAFILEALLIASREQUEST ON LEADSDATAFILEALLIASREPORTREQUEST.FLATFILEREQUESTID=LEADSDATAFILEALLIASREQUEST.ID AND REPORTREQUESTACTIVE=1",null,this,"readFlatFileExportReport");
	}
	
	private HashMap<String,String> _flatFileRequestJobSettings=new HashMap<String, String>();
	public void readFlatFileExportJobs(Integer rowindex,ArrayList<String> rowData,ArrayList<String> rowColumns){
		if(rowindex==0){
			return;
		}
		_flatFileRequestJobSettings.clear();
		_flatFileRequestJobSettings.putAll(Database.rowData(rowColumns, rowData));
		
		try {
			Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "INSERT INTO <DBUSER>.LEADSDATAFILEALLIASREPORTREQUEST (REPORTREQUESTCREATEDATETIME,FLATFILEREQUESTID,FLATFILEALLIASREPORTID,REPORTREQUESTACTIVE,REPORTEXPORTFILENAME,REPORTEXPORTFILEPATH,REPORTREQUESTLASTACTIONDATETIME,REPORTSTATUS) SELECT GETDATE() AS REPORTREQUESTCREATEDATETIME,:REQUESTJOBID AS FLATFILEREQUESTID,:REPORTID AS FLATFILEALLIASREPORTID,1 AS REPORTREQUESTACTIVE,:REPORTFILEPREFIX+'_'+REPLACE(REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR(23),GETDATE(),121),'-',''),' ',''),':',''),'.','')+'_'+UPPER(:REQUESTJOBID)+'.csv' AS REPORTEXPORTFILENAME,:REPORTFILEEXPORTPATH AS REPORTEXPORTFILEPATH,GETDATE() AS REPORTREQUESTLASTACTIONDATETIME,1 AS REPORTSTATUS",_flatFileRequestJobSettings,null,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private HashMap<String,String> _flatFileReportJobSettings=new HashMap<String, String>();
	private File _fmasterFile=null;
	private File _fduplicates=null;
	private File _fNoPhoneNumbers=null;
	
	private FileOutputStream _fout=null;
	private String _fileNameMaster="";
	private String _fileNameDuplicates="";
	private String _fileNameNoNumbers="";
	private String _extractFilePath="";
	public void readFlatFileExportReport(Integer rowindex,ArrayList<String> rowData,ArrayList<String> rowColumns) throws Exception{
		if(rowindex==0){
			return;
		}
		_flatFileReportJobSettings.clear();
		_flatFileReportJobSettings.putAll(Database.rowData(rowColumns, rowData));
		
		_flatFileReportJobSettings.put("ALIAS", "");
		_flatFileReportJobSettings.put("TOTALCONTENTRECORDSIMPORTED", "0");
		_flatFileReportJobSettings.put("TOTALCONTENTIMPORTDURATION", "0");
		
		_extractFilePath=_flatFileReportJobSettings.get("REPORTEXPORTFILEPATH").replaceAll("[\\\\]", "/");
		while(!_extractFilePath.endsWith("/")) _extractFilePath+="/";
		_fileNameMaster=_flatFileReportJobSettings.get("REPORTEXPORTFILENAME");
		_fileNameDuplicates=_fileNameMaster.substring(0,_fileNameMaster.lastIndexOf("."))+"_DUPLICATES"+_fileNameMaster.substring(_fileNameMaster.lastIndexOf("."));
		_fileNameNoNumbers=_fileNameMaster.substring(0,_fileNameMaster.lastIndexOf("."))+"_NO_PHONE_NUMBERS"+_fileNameMaster.substring(_fileNameMaster.lastIndexOf("."));
		
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT ALIAS FROM <DBUSER>.LEADSDATAFILEALLIASREQUEST WHERE ID=:FLATFILEREQUESTID", _flatFileReportJobSettings, null);
		
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(*)-1 AS TOTALCONTENTRECORDSIMPORTED,DATEDIFF(SECOND,MIN(LEADSDATAFILEALLIASREQUESTCONTENT.ENTRYCREATIONDATETIME),MAX(LEADSDATAFILEALLIASREQUESTCONTENT.ENTRYLASTACTIONDATETIME)) AS TOTALCONTENTIMPORTDURATION FROM <DBUSER>.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:FLATFILEREQUESTID", _flatFileReportJobSettings, null);
		
		_flatFileReportJobSettings.put("TOTALADDCALLREQUESTS", "0");
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(*) AS TOTALADDCALLREQUESTS FROM <DBUSER>.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:FLATFILEREQUESTID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=3", _flatFileReportJobSettings, null);
		
		_flatFileReportJobSettings.put("TOTALREMOVECALLREQUESTS", "0");
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(*) AS TOTALREMOVECALLREQUESTS FROM <DBUSER>.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:FLATFILEREQUESTID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=2 AND ENTRYHANDLEFLAG=3", _flatFileReportJobSettings, null);
		
		_flatFileReportJobSettings.put("TOTALDUPLICATES", "0");
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(*) AS TOTALDUPLICATES FROM SQLPR1.PTOOLS.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:FLATFILEREQUESTID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=4", _flatFileReportJobSettings, null);
		
		if(!_flatFileReportJobSettings.get("TOTALDUPLICATES").equals("0")){
			if(_fout!=null){
				try{
					_fout.flush();
					_fout.close();
				}
				catch(Exception e){
					_fout=null;
				}
			}
			_fout=new FileOutputStream(_extractFilePath+_fileNameDuplicates);
			Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT ENTRYSERVICEID,ENTRYLOADID,ENTRYSOURCEID,ENTRYCALLERNAME,ENTRYCOMMENTS FROM SQLPR1.PTOOLS.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:FLATFILEREQUESTID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=4", _flatFileReportJobSettings, this,"readGenerateReportDuplicates");
			if(_fout!=null){
				try{
					_fout.flush();
					_fout.close();
				}
				catch(Exception e){
					_fout=null;
				}
			}
		}
		
		_flatFileReportJobSettings.put("TOTALNONUMBERS","0");
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT COUNT(*) AS TOTALNONUMBERS FROM <DBUSER>.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:FLATFILEREQUESTID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=8", _flatFileReportJobSettings, null);
		if(!_flatFileReportJobSettings.get("TOTALNONUMBERS").equals("0")){
			if(_fout!=null){
				try{
					_fout.flush();
					_fout.close();
				}
				catch(Exception e){
					_fout=null;
				}
			}
			_fout=new FileOutputStream(_extractFilePath+_fileNameNoNumbers);
			Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "SELECT ENTRYSERVICEID,ENTRYLOADID,ENTRYSOURCEID FROM <DBUSER>.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=:FLATFILEREQUESTID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=8", _flatFileReportJobSettings, this,"readGenerateReportNoPhoneNumbers");
			if(_fout!=null){
				try{
					_fout.flush();
					_fout.close();
				}
				catch(Exception e){
					_fout=null;
				}
			}
		}
		
		if(_fout!=null){
			try{
				_fout.flush();
				_fout.close();
			}
			catch(Exception e){
				_fout=null;
			}
		}
		_fout=new FileOutputStream(_extractFilePath+_fileNameMaster);
		
		try{
			_colIndex=0;
			_fileColumnContent="";
				_fout.write("FILEALIAS,".getBytes());
				_fout.write("FILE,".getBytes());
				_fout.write("TOTAL_DURATION,".getBytes());
				_fout.write("TOTAL_RECORDS_IMPORTED,".getBytes());
				_fout.write("TOTAL_ADD_CALL_REQUESTS,".getBytes());
				_fout.write("TOTAL_REMOVED_CALL_REQUESTS,".getBytes());
				_fout.write("TOTAL_DUPLICATES,".getBytes());
				_fout.write("TOTAL_NO_PHONE_NUMBERS".getBytes());
				
				_fout.write("\r\n".getBytes());
			_fout.flush();
				_fout.write((_flatFileReportJobSettings.get("ALIAS")+",").getBytes());
				_fout.write((_flatFileReportJobSettings.get("REQUESTFILENAME")+",").getBytes());
				_fout.write((_flatFileReportJobSettings.get("TOTALCONTENTIMPORTDURATION")+",").getBytes());
				_fout.write((_flatFileReportJobSettings.get("TOTALCONTENTRECORDSIMPORTED")+",").getBytes());
				_fout.write((_flatFileReportJobSettings.get("TOTALADDCALLREQUESTS")+",").getBytes());
				_fout.write((_flatFileReportJobSettings.get("TOTALREMOVECALLREQUESTS")+",").getBytes());
				_fout.write((_flatFileReportJobSettings.get("TOTALDUPLICATES")+",").getBytes());
				_fout.write((_flatFileReportJobSettings.get("TOTALNONUMBERS")).getBytes());
				
				_fout.write("\r\n".getBytes());
			_fout.flush();
			_fout.close();
		}
		catch(Exception e){
			_fout=null;
		}
		
		if(_fout!=null){
			try{
				_fout.flush();
				_fout.close();
			}
			catch(Exception e){
				_fout=null;
			}
		}
		
		/*DECLARE @REQUESTID NUMERIC(18,0)=15

//done SELECT ALIAS FROM SQLPR1.PTOOLS.LEADSDATAFILEALLIASREQUEST WHERE ID=@REQUESTID

//done SELECT COUNT(*)-1 AS TOTALRECORDSIMPORTED,DATEDIFF(SECOND,MIN(LEADSDATAFILEALLIASREQUESTCONTENT.ENTRYCREATIONDATETIME),MAX(LEADSDATAFILEALLIASREQUESTCONTENT.ENTRYLASTACTIONDATETIME)) AS TOTALIMPORTDURATION FROM SQLPR1.PTOOLS.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=@REQUESTID

//done SELECT COUNT(*) AS ADDCALLREQUESTS FROM SQLPR1.PTOOLS.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=@REQUESTID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=3

//done SELECT COUNT(*) AS REMOVECALLREQUESTS FROM SQLPR1.PTOOLS.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=@REQUESTID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=2 AND ENTRYHANDLEFLAG=3

//done SELECT COUNT(*) AS DUPLICATES FROM SQLPR1.PTOOLS.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=@REQUESTID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=4
//done SELECT ENTRYSERVICEID,ENTRYLOADID,ENTRYSOURCEID,ENTRYCALLERNAME,ENTRYCOMMENTS FROM SQLPR1.PTOOLS.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=@REQUESTID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=4

//done SELECT COUNT(*) AS NONUMBERS FROM SQLPR1.PTOOLS.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=@REQUESTID AND ENTRYSTATUS=1 AND ENTRYLEADREQUESTTYPE=1 AND ENTRYHANDLEFLAG=8
//done SELECT ENTRYSERVICEID,ENTRYLOADID,ENTRYSOURCEID FROM SQLPR1.PTOOLS.LEADSDATAFILEALLIASREQUESTCONTENT WHERE FLATFILEALIASREQUESTID=@REQUESTID AND ENTRYSTATUS=1 AND ENTRYHANDLEFLAG=8
*/
		
		Database.executeDBRequest(null, "FLATFILELEADSIMPORTER", "UPDATE <DBUSER>.LEADSDATAFILEALLIASREPORTREQUEST SET REPORTREQUESTACTIVE=0,REPORTREQUESTLASTACTIONDATETIME=GETDATE() WHERE ID=:ID", _flatFileReportJobSettings, null);
	}
	
	private int _colIndex=0;
	private String _fileColumnContent="";
	public void readGenerateReportDuplicates(Integer rowindex,ArrayList<String> rowData,ArrayList<String> rowColumns){
		if(_fout!=null){
			try{
				_colIndex=0;
				_fileColumnContent="";
				while(_colIndex<rowColumns.size()){
					_fileColumnContent=((rowindex==0)?rowColumns:rowData).get(_colIndex++);
					_fileColumnContent=formatFileColumnValue(_fileColumnContent);
					if(_colIndex<rowColumns.size()){
						_fileColumnContent+=",";
					}
					_fout.write(_fileColumnContent.getBytes());
				}
				_fout.write("\r\n".getBytes());
				_fout.flush();
				_fout.close();
			}
			catch(Exception e){
				_fout=null;
			}
		}
	}
	
	private String formatFileColumnValue(String columnValue){
		if((columnValue.indexOf(",")>-1)||(columnValue.indexOf("\"")>-1)){
			columnValue="\""+columnValue+"\"";
		}
		return columnValue;
	}
	
	public void readGenerateReportNoPhoneNumbers(Integer rowindex,ArrayList<String> rowData,ArrayList<String> rowColumns){
		if(_fout!=null){
			try{
				_colIndex=0;
				_fileColumnContent="";
				while(_colIndex<rowColumns.size()){
					_fileColumnContent=((rowindex==0)?rowColumns:rowData).get(_colIndex++);
					_fileColumnContent=formatFileColumnValue(_fileColumnContent);
					if(_colIndex<rowColumns.size()){
						_fileColumnContent+=",";
					}
					_fout.write(_fileColumnContent.getBytes());
				}
				_fout.write("\r\n".getBytes());
				_fout.flush();
				_fout.close();
			}
			catch(Exception e){
				_fout=null;
			}
		}
	}

	private boolean _shutdownQueue=false;
	private boolean _busyShuttingDown=false;
	
	public static StatusReportQueue statusReportQueue(){
		if(_statusReportQueue==null){
			new Thread(_statusReportQueue=new StatusReportQueue()).start();
		}
		return _statusReportQueue;
	}

	public void shutdownQueue() {
		_statusReportQueue._shutdownQueue=true;
		_statusReportQueue._busyShuttingDown=true;
		while(_statusReportQueue._busyShuttingDown){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
}
