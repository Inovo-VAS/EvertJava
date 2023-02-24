package inovo.automated.work.steps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import inovo.automated.work.AutomatedWorker;
import inovo.automated.work.AutomatedWorkerStep;
import inovo.db.Database;

public class ExportFileStep extends AutomatedWorkerStep {

	public ExportFileStep(AutomatedWorker automatedWorker,
			HashMap<String, String> stepProperties) {
		super(automatedWorker, stepProperties);
	}

	private File _fileDir=null;
	
	private ArrayList<String> _filemaskSections=new ArrayList<String>();
	private ArrayList<String> _currentFileFieldNames=new ArrayList<String>();	
	
	@Override
	public void performAutomationStep() {
		this.stepProperties().put("INLINESQLCOMMAND", "");
		this.stepProperties().put("COMADELIM", "");
		this.stepProperties().put("DESTINATIONPATH", "");
		this.stepProperties().put("CURRENTFILEFIELDS", "");
		this.stepProperties().put("FILEEXPORTMASK", "");
		
		try {
			Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT INLINESQLCOMMAND,COMADELIM,DESTINATIONPATH,CURRENTFILEFIELDS,FILEEXPORTMASK FROM <DBUSER>.AUTOMATED_STEP_TYPE_EXPORTFILE WHERE STEP_ID=:STEPID", this.stepProperties(), null);
			
		} catch (Exception e) {
		}
		this._currentFileFieldNames.clear();
		String[]currentFileFields=this.stepProperties().get("CURRENTFILEFIELDS").split("["+this.stepProperties().get("COMADELIM")+"]");
		if(currentFileFields!=null){
			if(currentFileFields.length>0){
				for(String currentfield:currentFileFields){
					if(this._currentFileFieldNames.indexOf(currentfield.toUpperCase())==-1) this._currentFileFieldNames.add(currentfield.toUpperCase());
				}
			}
		}
		
		_fileDir=new File(this.stepProperties().get("DESTINATIONPATH"));
		if(_fileDir.isDirectory()){
			String exportFileName=stepProperties().get("FILEEXPORTMASK");
			if(exportFileName.indexOf("<datetime")>-1){
				String dateTimeMask=exportFileName.substring(exportFileName.indexOf("<datetime")+"<datetime".length(),exportFileName.indexOf(">"));
				if(dateTimeMask.startsWith(":")){
					dateTimeMask=dateTimeMask.substring(1);
				}
				dateTimeMask=(dateTimeMask.equals("")?"yyyyMMdd_HHmmss.SSS":dateTimeMask);
				exportFileName=exportFileName.substring(0,exportFileName.indexOf("<datetime"))+new SimpleDateFormat(dateTimeMask).format(new Date())+exportFileName.substring(exportFileName.indexOf(">")+1);
			}
			
			File exportFile=new File(_fileDir.getAbsolutePath()+File.separator+exportFileName);
			if(!exportFile.exists()){
				try {
					exportFile.createNewFile();
					try {
						this.processExportFile(exportFile);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			if(_fileOut!=null){
				try {
					_fileOut.flush();
					_fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				_fileOut=null;
				
			}
		}
	}

	private HashMap<String,String> _fileexportrequestsettings=new HashMap<String, String>();
	
	private FileOutputStream _fileOut=null;
	
	
	private long _fileExportLineCount=0;
	private long _fileExportFailureLineCount=0;
	
	private void processExportFile(File fileToExport) throws Exception{
		_fileexportrequestsettings.clear();
		for(String fname:"STEPREQUESTID,STEPNAME,STEPDESCRIPTION,STEPTYPENAME,STEPTYPEDESCRIPTION,INLINESQLCOMMAND,COMADELIM,DESTINATIONPATH,CURRENTFILEFIELDS,FILEEXPORTMASK,REQUESTCREATIONDATE,REQUESTFILEPATH,REQUESTFILENAME,REQUESTHANDLEFLAG,REQUESTLASTHANDLEDATETIME,REQUESTFILEEXPORTSTATUS,REQUESTFILEEXPORTLINECOUNT,REQUESTFILEEXPORTACTIVE,REQUESTFILEEXPORTCONTENTSTATUS,REQUESTFILEEXPORTCONTENTCOMPLETIONDATETIME,REQUESTFILEEXPORTCONTENTSTATUSDESCRIPTION,REQUESTFILEEXPORTFAILURELINECOUNT".split("[,]")){
			_fileexportrequestsettings.put(fname, "");
		}
		
		_fileexportrequestsettings.putAll(this.stepProperties());
		_fileexportrequestsettings.put("REQUESTFILEPATH", fileToExport.getAbsolutePath().substring(0,fileToExport.getAbsolutePath().length()-fileToExport.getName().length()));
		_fileexportrequestsettings.put("REQUESTFILENAME", fileToExport.getName());
		
		Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT ID AS STEPREQUESTID,STEP_ID AS STEPID,STEPNAME,STEPDESCRIPTION,STEPTYPENAME,STEPTYPEDESCRIPTION,INLINESQLCOMMAND,COMADELIM,DESTINATIONPATH,CURRENTFILEFIELDS,FILEEXPORTMASK,REQUESTCREATIONDATE,REQUESTFILEPATH,REQUESTFILENAME,REQUESTHANDLEFLAG,REQUESTLASTHANDLEDATETIME,REQUESTFILEEXPORTSTATUS,REQUESTFILEEXPORTLINECOUNT,REQUESTFILEEXPORTACTIVE,REQUESTFILEEXPORTCONTENTSTATUS,REQUESTFILEEXPORTCONTENTCOMPLETIONDATETIME,REQUESTFILEEXPORTCONTENTSTATUSDESCRIPTION,REQUESTFILEEXPORTFAILURELINECOUNT FROM <DBUSER>.AUTOMATED_STEP_TYPE_EXPORTFILE_REQUEST WHERE STEP_ID=:STEPID AND REQUESTFILEEXPORTACTIVE=1", _fileexportrequestsettings,null);
		
		if(!_fileexportrequestsettings.get("STEPID").equals("")&&_fileexportrequestsettings.get("STEPREQUESTID").equals("")){
			//:REQUESTFILEPATH,:REQUESTFILENAME
			Database.executeDBRequest(null, "AUTOMATEDWORK", "INSERT INTO <DBUSER>.AUTOMATED_STEP_TYPE_EXPORTFILE_REQUEST (STEP_ID,STEPNAME,STEPDESCRIPTION,STEPTYPENAME,STEPTYPEDESCRIPTION,INLINESQLCOMMAND,COMADELIM,DESTINATIONPATH,CURRENTFILEFIELDS,FILEEXPORTMASK,REQUESTCREATIONDATE,REQUESTFILEPATH,REQUESTFILENAME,REQUESTHANDLEFLAG,REQUESTLASTHANDLEDATETIME,REQUESTFILEEXPORTSTATUS,REQUESTFILEEXPORTLINECOUNT,REQUESTFILEEXPORTACTIVE,REQUESTFILEEXPORTCONTENTSTATUS,REQUESTFILEEXPORTCONTENTCOMPLETIONDATETIME,REQUESTFILEEXPORTCONTENTSTATUSDESCRIPTION,REQUESTFILEEXPORTFAILURELINECOUNT,REQUESTSESSIONKEY)  SELECT :STEPID,:STEPNAME,:STEPDESCRIPTION,:STEPTYPENAME,:STEPTYPEDESCRIPTION,:INLINESQLCOMMAND,:COMADELIM,:DESTINATIONPATH,:CURRENTFILEFIELDS,:FILEEXPORTMASK,GETDATE(),:REQUESTFILEPATH,:REQUESTFILENAME,1,GETDATE(),1,0,1,0,NULL,'',0,:SESSIONKEY", _fileexportrequestsettings,null);
			Database.executeDBRequest(null, "AUTOMATEDWORK", "SELECT MAX(ID) AS STEPREQUESTID FROM <DBUSER>.AUTOMATED_STEP_TYPE_EXPORTFILE_REQUEST WHERE STEP_ID=:STEPID", this._fileexportrequestsettings,null);
		}
		
		_fileOut=new FileOutputStream(fileToExport);
		_fileExportLineCount=0;
		_fileExportFailureLineCount=0;
		
		try{
			this._fileexportrequestsettings.put("REQUESTFILEEXPORTCONTENTSTATUS", "0");
			this._fileexportrequestsettings.put("REQUESTFILEEXPORTCONTENTSTATUSDESCRIPTION","");
			if(_fileexportrequestsettings.get("INLINESQLCOMMAND").equals("")){
				this._fileexportrequestsettings.put("REQUESTFILEEXPORTCONTENTSTATUS", "3");
				this._fileexportrequestsettings.put("REQUESTFILEEXPORTCONTENTSTATUSDESCRIPTION","EMPTY INLINESQLCOMMAND");
			}
			else{
				Database.executeDBRequest(null, "AUTOMATEDWORK", _fileexportrequestsettings.get("INLINESQLCOMMAND"), null, this,"readSqlCommandToExport");
			}
			try {
				Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE <DBUSER>.AUTOMATED_STEP_TYPE_EXPORTFILE_REQUEST SET REQUESTHANDLEFLAG=3,REQUESTLASTHANDLEDATETIME=GETDATE(),REQUESTFILEEXPORTSTATUS=0,REQUESTFILEEXPORTLINECOUNT=(SELECT COUNT(*) FROM <DBUSER>.AUTOMATION_STEP_TYPE_EXPORTFILE_CONTENT WHERE REQUESTID=:STEPREQUESTID),REQUESTFILEEXPORTCONTENTSTATUS=:REQUESTFILEEXPORTCONTENTSTATUS,REQUESTFILEEXPORTCONTENTSTATUSDESCRIPTION=:REQUESTFILEEXPORTCONTENTSTATUSDESCRIPTION,REQUESTFILEEXPORTFAILURELINECOUNT=(SELECT COUNT(*) FROM <DBUSER>.AUTOMATION_STEP_TYPE_EXPORTFILE_CONTENT WHERE REQUESTID=:STEPREQUESTID AND EXPORTSTATUS=4),REQUESTFILEEXPORTCONTENTCOMPLETIONDATETIME=GETDATE(),REQUESTFILEEXPORTACTIVE=0 WHERE ID=:STEPREQUESTID", this._fileexportrequestsettings, null);
			} catch (Exception e) {
			}
		}
		catch(Exception e){
			try {
				this._fileexportrequestsettings.put("REQUESTFILEEXPORTCONTENTSTATUS", "4");
				this._fileexportrequestsettings.put("REQUESTFILEEXPORTCONTENTSTATUSDESCRIPTION", e.getMessage());
				Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE <DBUSER>.AUTOMATED_STEP_TYPE_EXPORTFILE_REQUEST SET REQUESTHANDLEFLAG=3,REQUESTLASTHANDLEDATETIME=GETDATE(),REQUESTFILEEXPORTSTATUS=0,REQUESTFILEEXPORTLINECOUNT=(SELECT COUNT(*) FROM <DBUSER>.AUTOMATION_STEP_TYPE_EXPORTFILE_CONTENT WHERE REQUESTID=:STEPREQUESTID),REQUESTFILEEXPORTCONTENTSTATUS=:REQUESTFILEEXPORTCONTENTSTATUS,REQUESTFILEEXPORTCONTENTSTATUSDESCRIPTION=:REQUESTFILEEXPORTCONTENTSTATUSDESCRIPTION,REQUESTFILEEXPORTFAILURELINECOUNT=(SELECT COUNT(*) FROM <DBUSER>.AUTOMATION_STEP_TYPE_EXPORTFILE_CONTENT WHERE REQUESTID=:STEPREQUESTID AND EXPORTSTATUS=4),REQUESTFILEEXPORTCONTENTCOMPLETIONDATETIME=GETDATE(),REQUESTFILEEXPORTACTIVE=0 WHERE ID=:STEPREQUESTID", this._fileexportrequestsettings, null);
			} catch (Exception ex) {
			}
		}
	}
	
	private ArrayList<String> _fileRecordColumns=new ArrayList<String>();
	private ArrayList<String> _fileRecordData=new ArrayList<String>();
	private StringBuilder _sqlFileContentExportCommand=new StringBuilder();
	private int _colIndex=0;
	private HashMap<String,String> _fileContentParams=new HashMap<String, String>();
	
	
	public void readSqlCommandToExport(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> rowColumns){
		_fileExportLineCount++;
		if(rowIndex==0){
			_fileRecordColumns.clear();
			_fileRecordColumns.addAll(rowColumns);
		}
		_fileRecordData.clear();
		_fileRecordData.addAll(rowData);
		
		_fileContentParams.clear();
		
		this._sqlFileContentExportCommand.setLength(0);
		this._sqlFileContentExportCommand.append("INSERT INTO <DBUSER>.AUTOMATION_STEP_TYPE_EXPORTFILE_CONTENT (");
		
		this._sqlFileContentExportCommand.append("REQUESTID,");
		_fileContentParams.put("REQUESTID", this._fileexportrequestsettings.get("STEPREQUESTID"));
		
		this._sqlFileContentExportCommand.append("EXPORTDATETIME,");
		this._sqlFileContentExportCommand.append("ROWTYPE,");
		_fileContentParams.put("ROWTYPE", rowIndex==0?"COLUMNS":"DATA");
		
		this._colIndex=0;
		while(this._colIndex<rowColumns.size()){
			this._sqlFileContentExportCommand.append("FIELD"+String.valueOf((this._colIndex+1))+",");
			this._colIndex++;
		}
		
		this._sqlFileContentExportCommand.append("EXPORTSTATUS,");
		this._sqlFileContentExportCommand.append("EXPORTSTATUSDESCRIPTION) ");
		
		this._sqlFileContentExportCommand.append("SELECT ");
		this._sqlFileContentExportCommand.append(":REQUESTID,");
		this._sqlFileContentExportCommand.append("GETDATE(),");
		this._sqlFileContentExportCommand.append(":ROWTYPE,");
		
		this._colIndex=0;
		while(this._colIndex<rowColumns.size()){
			this._sqlFileContentExportCommand.append(":FIELD"+String.valueOf((this._colIndex+1))+",");
			_fileContentParams.put("FIELD"+String.valueOf((this._colIndex+1)),rowData.get(_colIndex));
			this._colIndex++;
		}
		
		this._sqlFileContentExportCommand.append(":EXPORTSTATUS,");
		this._sqlFileContentExportCommand.append(":EXPORTSTATUSDESCRIPTION ");
		
		if(rowIndex==0){
			_fileContentParams.put("EXPORTSTATUS", "1");
			_fileContentParams.put("EXPORTSTATUSDESCRIPTION", "");
		}
		else{
			
		}
		
		try {
			int colIndex=0;
			String data="";
			if(rowIndex==0){
				_fileContentParams.put("EXPORTSTATUS", "1");
				_fileContentParams.put("EXPORTSTATUSDESCRIPTION", "");
				
				while(colIndex<rowData.size()){
					if((data=rowData.get(colIndex++)).indexOf(this.stepProperties().get("COMADELIM"))>-1){
						data="\""+data.replaceAll("\"", "\"\"")+"\"";
					}
					else{
						data=data.indexOf("\"")==-1?data:"\""+data.replaceAll("\"", "\"\"")+"\"";
					}
					this._fileOut.write(data.getBytes());
					if(colIndex<rowData.size()){
						this._fileOut.write(this.stepProperties().get("COMADELIM").getBytes());
					}
				}
			}
			else{
				_fileContentParams.put("EXPORTSTATUS", "2");
				_fileContentParams.put("EXPORTSTATUSDESCRIPTION", "");
				this._fileOut.write("\r\n".getBytes());
				while(colIndex<rowData.size()){
					if((data=rowData.get(colIndex++)).indexOf(this.stepProperties().get("COMADELIM"))>-1){
						data="\""+data.replaceAll("\"", "\"\"")+"\"";
					}
					else{
						data=data.indexOf("\"")==-1?data:"\""+data.replaceAll("\"", "\"\"")+"\"";
					}
					this._fileOut.write(data.getBytes());
					if(colIndex<rowData.size()){
						this._fileOut.write(this.stepProperties().get("COMADELIM").getBytes());
					}
				}
			}
			_fileContentParams.put("EXPORTSTATUS", "2");
			_fileContentParams.put("EXPORTSTATUSDESCRIPTION", "");
		} catch (Exception e) {
			_fileExportFailureLineCount++;
			_fileContentParams.put("EXPORTSTATUS", "4");
			_fileContentParams.put("EXPORTSTATUSDESCRIPTION", "ERROR:INLINESQLCOMMAND:"+e.getMessage());
		}
		
		try {
			Database.executeDBRequest(null, "AUTOMATEDWORK", this._sqlFileContentExportCommand.substring(0,this._sqlFileContentExportCommand.length()), this._fileContentParams, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(_fileExportLineCount % 1000==0){
			try {
				Database.executeDBRequest(null, "AUTOMATEDWORK", "UPDATE <DBUSER>.AUTOMATED_STEP_TYPE_EXPORTFILE_REQUEST SET REQUESTHANDLEFLAG=2,REQUESTLASTHANDLEDATETIME=GETDATE(),REQUESTFILEEXPORTSTATUS=1,REQUESTFILEEXPORTLINECOUNT="+String.valueOf(_fileExportLineCount)+",REQUESTFILEEXPORTCONTENTSTATUS=0,REQUESTFILEEXPORTCONTENTSTATUSDESCRIPTION='',REQUESTFILEEXPORTFAILURELINECOUNT="+String.valueOf(_fileExportFailureLineCount)+" WHERE ID=:REQUESTID", this._fileContentParams, null);
			} catch (Exception e) {
			}
		}
	}
	
	private StringBuilder _inlineSqlCommand=new StringBuilder();
	private String formatInlineSQLCommand(String inlinesqlCommand) {
		if(inlinesqlCommand==null) return "";
		if((inlinesqlCommand=inlinesqlCommand.trim()).equals("")) return inlinesqlCommand;
		this._inlineSqlCommand.setLength(0);
		String parameterName="";
		char prevChar=0;
		char endParamChar=' ';
		boolean readingParam=false;
		for(char isql:inlinesqlCommand.toCharArray()){
			if(readingParam){
				if(parameterName.length()>0&&endParamChar==']'&&isql==endParamChar){
					if(_currentFileFieldNames.indexOf(parameterName.toUpperCase().trim())>-1){
						this._inlineSqlCommand.append(":FIELD"+String.valueOf(_currentFileFieldNames.indexOf(parameterName.toUpperCase().trim())+1));
						
					}
					else{
						this._inlineSqlCommand.append(":["+parameterName+"]");
					}
					parameterName="";
					readingParam=false;
				}
				else if(parameterName.length()>0&&endParamChar!=']'){
					if((""+isql).trim().equals("")){
						this._inlineSqlCommand.append(":"+parameterName+isql);
						parameterName="";
						readingParam=false;
					}
					else{
						parameterName+=(""+isql);
					}
					if(_currentFileFieldNames.indexOf(parameterName.toUpperCase().trim())>-1){
						this._inlineSqlCommand.append(":FIELD"+String.valueOf(_currentFileFieldNames.indexOf(parameterName.toUpperCase().trim())+1));
						if(parameterName.trim().length()<parameterName.length()){
							this._inlineSqlCommand.append(parameterName.substring(parameterName.length()-1));
						}
						parameterName="";
						readingParam=false;
					}
				}
				else{
					if(prevChar==':'&&isql=='['){
						endParamChar=']';
					}
					else if(prevChar==':'&&isql!='['){
						endParamChar=' ';
						parameterName+=(""+isql);
					}
					else{
						parameterName+=(""+isql);
					}
				}
			}
			else{
				if(isql==':'){
					readingParam=true;
				}
				else{
					this._inlineSqlCommand.append(isql);
				}
			}
			prevChar=isql;
		}
		if(!parameterName.equals("")){
			this._inlineSqlCommand.append(":"+readingParam);
		}
		return this._inlineSqlCommand.substring(0, this._inlineSqlCommand.length());
	}
}
