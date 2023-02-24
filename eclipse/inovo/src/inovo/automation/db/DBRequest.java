package inovo.automation.db;

import inovo.db.Database;
import inovo.email.smtp.MailContent;
import inovo.email.smtp.SMTPClient;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

public class DBRequest extends SQLRequest {
	public DBRequest(HashMap<String,Object> requestProperties) throws Exception{
		super(inovo.automation.Queue.generateSchedules(requestProperties.get("DAILY_SCHEDULE").toString().indexOf(";")>-1?requestProperties.get("DAILY_SCHEDULE").toString().substring(0,requestProperties.get("DAILY_SCHEDULE").toString().indexOf(";")).split("[,]"):requestProperties.get("DAILY_SCHEDULE").toString().split("[,]"), "HH:mm"), requestProperties);
		if(requestProperties.get("DAILY_SCHEDULE").toString().indexOf(";")>-1){
			this.setRequestDelay(Long.parseLong(requestProperties.get("DAILY_SCHEDULE").toString().substring(requestProperties.get("DAILY_SCHEDULE").toString().indexOf(";")+1))*1024);
		}
		ArrayList<String> dbRequestActions=new ArrayList<String>();
		dbRequestActions.add("PRE_SQL_SELECT_COMMAND");
		dbRequestActions.add("SQL_SELECT_COMMAND");
		dbRequestActions.add("CSV_EXPORT_FILENAME_MASK");
		dbRequestActions.add("MAIL_EXPORT_ADDRESSES");
		dbRequestActions.add("POST_SQL_SELECT_COMMAND");
		
		this.setRequestActions(dbRequestActions);
	}
	
	private String _preSqlStatement="";
	private String _mainSqlStatement="";
	private String _postSqlStatement="";
	private String _exportCSVFileNameMask="";
	private String _exportCSVFileMailingAddresses="";
	private String _exportCSVFileMailingSmtpAddress="";
	private String _exportCSVFileMailingFromAddress="";
	private String _exportCSVFileMailingSmtpAccount="";
	
	private Database _dbAllias=null;
	@Override
	public void executeDBRequest(Database dballias) throws Exception {
		this._preSqlStatement=this.property("PRE_SQL_SELECT_COMMAND");
		this._mainSqlStatement=this.property("SQL_SELECT_COMMAND");
		this._postSqlStatement=this.property("POST_SQL_SELECT_COMMAND");
		this._exportCSVFileNameMask=this.property("CSV_EXPORT_FILENAME_MASK");
		this._exportCSVFileMailingAddresses=this.property("MAIL_EXPORT_ADDRESSES");	
		this._exportCSVFileMailingSmtpAddress=this.property("MAIL_EXPORT_SMTP_ADDRESS");
		this._exportCSVFileMailingSmtpAccount=this.property("MAIL_EXPORT_SMTP_ACCOUNT");
		
		this._dbAllias=dballias;
		this.executeActions();
	}
	
	private TreeMap<Integer,ArrayList<Object>> _mainSqlStatementDataSet=null;
	
	public void PRE_SQL_SELECT_COMMAND(Database dbAllias) throws Exception{
		dbAllias.executeDBRequest(null,dbAllias, _preSqlStatement, null,null);
	}
	
	public void SQL_SELECT_COMMAND(Database dbAllias) throws Exception{
		if(_mainSqlStatementDataSet!=null) Database.cleanupDataset(_mainSqlStatementDataSet);
		dbAllias.executeDBRequest(_mainSqlStatementDataSet,dbAllias, _mainSqlStatement, null,null);
	}
	
	private File _csvExportedFile=null;
	private File _csvExportedTmpFile=null;
	public void CSV_EXPORT_FILENAME_MASK(TreeMap<Integer,ArrayList<Object>> mainSqlStatementDataset) throws Exception{
		_csvExportedFile=null;
		if(!_exportCSVFileNameMask.equals("")){
			
			_csvExportedTmpFile=File.createTempFile("csvexport", ".csv");
			System.out.println(_csvExportedTmpFile.getAbsolutePath());
			FileOutputStream csvFileOutputDatasetSteam=new FileOutputStream(_csvExportedTmpFile);
			if(_mainSqlStatementDataSet!=null){
				Database.populateFlatFileStreamFromDataset(_mainSqlStatementDataSet, "CSV", csvFileOutputDatasetSteam, ',',true);
			}
			csvFileOutputDatasetSteam.flush();
			csvFileOutputDatasetSteam.close();
			
			if(_exportCSVFileMailingAddresses.equals("")){
				_csvExportedTmpFile.delete();
			}
		}
	}
	
	private FileInputStream smtpFin=null;
	public void MAIL_EXPORT_ADDRESSES() throws Exception{
		if(!_exportCSVFileMailingAddresses.equals("")){
			if(!this._exportCSVFileMailingAddresses.equals("")){
				String exportCSVFileMailingSmtpAddress=_exportCSVFileMailingSmtpAddress;
				int smtpPort = (exportCSVFileMailingSmtpAddress.indexOf(":")>-1?Integer.parseInt(exportCSVFileMailingSmtpAddress.substring(exportCSVFileMailingSmtpAddress.indexOf(":")+1)):25);
				exportCSVFileMailingSmtpAddress=(exportCSVFileMailingSmtpAddress.indexOf(":")>-1?exportCSVFileMailingSmtpAddress.substring(0,exportCSVFileMailingSmtpAddress.indexOf(":")):exportCSVFileMailingSmtpAddress);
				SMTPClient smtpSMSClient=new SMTPClient(exportCSVFileMailingSmtpAddress,smtpPort);
				smtpSMSClient.setOutputWriter(System.out);
				try {
					smtpSMSClient.connect();
					smtpSMSClient.cmdHelo();
					if(_exportCSVFileMailingSmtpAccount.indexOf("/")>-1){
						smtpSMSClient.cmdAuthPlain(_exportCSVFileMailingSmtpAccount.substring(0,_exportCSVFileMailingSmtpAccount.indexOf("/")), _exportCSVFileMailingSmtpAccount.substring(_exportCSVFileMailingSmtpAccount.indexOf("/")+1));
					}
					try {
						smtpFin=null;
						smtpSMSClient.cmdMailContent(new MailContent(smtpSMSClient){
							@Override
							public void setupMailContent() {
								
								if(_exportCSVFileMailingSmtpAccount.indexOf("/")>-1){
									_exportCSVFileMailingFromAddress=(_exportCSVFileMailingSmtpAccount.substring(0,_exportCSVFileMailingSmtpAccount.indexOf("/")));
								}
								else{
									_exportCSVFileMailingFromAddress=_exportCSVFileMailingSmtpAccount;
								}
								
								this.setFromAddress(_exportCSVFileMailingFromAddress);//this.setFromAddress("leadsmanager@lancet.co.za");
								
								String exportCSVFileMailingAddresses=_exportCSVFileMailingAddresses;
								while(!exportCSVFileMailingAddresses.equals("")){
									String exportFileMailingAddress="";
									if(exportCSVFileMailingAddresses.indexOf(";")>-1){
										exportFileMailingAddress=exportCSVFileMailingAddresses.substring(0,exportCSVFileMailingAddresses.indexOf(";"));
										exportCSVFileMailingAddresses=exportCSVFileMailingAddresses.substring(exportCSVFileMailingAddresses.indexOf(";")+1);
									}
									else{
										exportFileMailingAddress=exportCSVFileMailingAddresses;
										exportCSVFileMailingAddresses="";
									}
									if(!exportFileMailingAddress.equals("")){
										this.addRecipient(exportFileMailingAddress);
									}
								}
								
								//this.addRecipient("ebrahaim.adams@lancet.co.za");
								//this.addRecipient("sunjin.sadanan@lancet.co.za");
								//this.addRecipient("nick.orange@lancet.co.za");
								//this.addRecipient("ashley.volmink@lancet.co.za");
								//this.addRecipient("shaheen.halim@lancet.co.za");
								this.setSubject(property("REQUEST_NAME")+" - "+new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date()));
								this.setBody(new String[]{"Content-Type:text/plain"}, new ByteArrayInputStream((property("REQUEST_NAME")+" - "+new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date())).getBytes()));								
								try {
									//for(File smtpF:smsAttFiles){
										smtpFin=new FileInputStream(_csvExportedTmpFile);
										//smsFilesInputArr.add(smtpFin);
										try{
											String csvFileName=new File(_exportCSVFileNameMask).getName();
											csvFileName=(csvFileName.indexOf(".")>-1?csvFileName.substring(0,csvFileName.indexOf("."))+new SimpleDateFormat("yyyyMMdd_HHmmss.SSS").format(new Date())+csvFileName.substring(csvFileName.indexOf(".")):csvFileName+new SimpleDateFormat("yyyyMMdd_HHmmss.SSS").format(new Date()));
											this.addAttachment(csvFileName, new String[]{"filename="+csvFileName,"Content-Type:text/csv"},smtpFin);
										}
										catch(Exception ein){
											ein.printStackTrace();
											System.out.print("ERROR SMTP ATT WRITE:" +ein.getMessage());
										}
									//}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
					smtpSMSClient.sendCommand("QUIT", false);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try{
					if(smtpFin!=null){
						smtpFin.close();
					}
				}
				catch(Exception smtpFinE){
					smtpFinE.printStackTrace();
				}
				smtpFin=null;
				_csvExportedTmpFile.delete();
			}
		}
	}
	
	public void POST_SQL_SELECT_COMMAND(Database dbAllias) throws Exception{
		dbAllias.executeDBRequest(null,dbAllias, _postSqlStatement, null,null);
	}
	
	@Override
	public void executeAction(String requestAction) {
		Method dbrequestActionMethod=inovo.adhoc.AdhocUtils.findMethod(this.getClass().getMethods(), requestAction, false);
		if(dbrequestActionMethod!=null){
			try {
				dbrequestActionMethod.invoke(this, this.dbRequestActionParams(requestAction));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Object[] dbRequestActionParams(String requestAction){
		if(requestAction.equals("PRE_SQL_SELECT_COMMAND")||requestAction.equals("SQL_SELECT_COMMAND")||requestAction.equals("POST_SQL_SELECT_COMMAND")){
			return new Object[]{this._dbAllias};
		}
		else if(requestAction.equals("CSV_EXPORT_FILENAME_MASK")){
			return new Object[]{this._mainSqlStatementDataSet};
		}
		return null;
	}
	
	@Override
	public boolean canContinue() {
		return true;
	}
}
