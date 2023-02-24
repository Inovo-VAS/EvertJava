package inovo.presence.mail.archiver;

//import inovo.adhoc.Base64;
import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;

import inovo.db.Database;
import inovo.email.EMLMailExtractor;
import inovo.servlet.InovoServletContextListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import mailsoaclient.WFS_Multimedia;
import za.co.inovo.presence.mutimedia.utils.InovoOpenPGP;
import za.co.inovo.presence.mutimedia.utils.InovoOpenPGPException;
import za.co.woolworths.Indibano.WS_WFS_Multimedia.MailExportRequestMetadata;
import za.co.woolworths.Indibano.WS_WFS_Multimedia.WS_WFS_MultimediaSOAPStub;

public class MailExportQueue
{	
	private HashMap<String,ExportMailRequest> _exportMailItems=new HashMap<String, MailExportQueue.ExportMailRequest>();
	//private ExecutorService _extracteArchiveService=null;
	private ExecutorService _wsArchiveService=null;
	private ConcurrentLinkedQueue<ExportMailRequest> _queuedExportMailRequests=new ConcurrentLinkedQueue<ExportMailRequest>();
	private class ExportMailRequest implements Runnable{
		
		private ExportMailRequest _wsCallLock=null;
		private  Base64 _base64=new Base64();
		/*private class WebCallThread extends Thread{
			private WFS_Multimedia _wsCall=null;
			private Exception _wsex=null;
			private String _responseStatus="";
			
			private WebCallThread(WFS_Multimedia wsCall){
				this._wsCall=wsCall;
			}
			
			@Override
			public void run() {
				try{
					this._responseStatus=_wsCall.requestArchive(InovoServletContextListener.inovoServletListener().configProperty("WSMAILSERVICEURL"));
				}
				catch(Exception wsex){
					this._wsex=wsex;
				}
				if(_wsCallLock!=null){
					synchronized (_wsCallLock) {
						_wsCallLock.notifyAll();
					}
				}
			}
		}*/
		
		private String _mailReqID="";
		private HashMap<String,String> _archiveSettingsToProcess;
		private HashMap<String,String> _archivemasksettings;
		
		private ExportMailRequest(String mailReqID,HashMap<String,String> archiveSettingsToProcess,HashMap<String,String> archivemasksettings){
			this._archivemasksettings=new HashMap<String,String>(archivemasksettings);
			this._archiveSettingsToProcess=new HashMap<String,String>(archiveSettingsToProcess);
			this._mailReqID=mailReqID;
		}
		
		//private WebCallThread _webCallThread=null;
		private WFS_Multimedia _wfs_Multimedia=null;
		private String _responseStatusmessage ="";
        
        private String _urlRefWS="";
        private Exception _mwsex=null;
        
		public void executeMailExportRequest()
		{
			if(this._archiveSettingsToProcess.isEmpty()) return;
			_wsCallLock=this;
			TreeMap<Integer,ArrayList<String>> _inboundoutboundmailsset=null;
			HashMap<String,String> _sqlinputparamsMailExport = new HashMap<String,String>();
			
			//log_debug("MAILSTAGING REQUEST START [executeMailExportRequest()]");
		    long millisecondsStart = Calendar.getInstance().getTimeInMillis();
		    String requestStartDatetime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(Long.valueOf(Calendar.getInstance().getTimeInMillis()));
		    
		    _sqlinputparamsMailExport.clear();
		   
		    _sqlinputparamsMailExport.put("INBOUNDMAILID", this._archiveSettingsToProcess.get("INBOUNDMAILID"));
		    _sqlinputparamsMailExport.put("AGENTID", this._archiveSettingsToProcess.get("AGENTID"));

		    String metaFieldsToAdd = "";
		    int metafieldIndex = 1;

		    while (metafieldIndex > 0) {
		      if ((this._archiveSettingsToProcess.containsKey("METAFIELD" + String.valueOf(metafieldIndex) + "NAME")) && (this._archiveSettingsToProcess.containsKey("METAFIELD" + String.valueOf(metafieldIndex) + "VALUE")))
		      {
		        metaFieldsToAdd = metaFieldsToAdd + ":METAFIELD" + String.valueOf(metafieldIndex) + "NAME" + " AS " + "METAFIELD" + String.valueOf(metafieldIndex) + "NAME,";
		        _sqlinputparamsMailExport.put("METAFIELD" + String.valueOf(metafieldIndex) + "NAME", this._archiveSettingsToProcess.get("METAFIELD" + String.valueOf(metafieldIndex) + "NAME"));

		        metaFieldsToAdd = metaFieldsToAdd + ":METAFIELD" + String.valueOf(metafieldIndex) + "VALUE" + " AS " + "METAFIELD" + String.valueOf(metafieldIndex) + "VALUE,";
		        _sqlinputparamsMailExport.put("METAFIELD" + String.valueOf(metafieldIndex) + "VALUE", this._archiveSettingsToProcess.get("METAFIELD" + String.valueOf(metafieldIndex) + "VALUE"));

		        metafieldIndex++;
		      }
		      else {
		        metafieldIndex = 0;
		      }
		    }

		    String sqlSelectRelevantMails = "SELECT :AGENTID AS AGENTID," + metaFieldsToAdd + " INBOUNDMAILID,OUTBOUNDMAILID,MAILDIRECTION,MAILSTATUS,CASE OUTBOUNDMAILID WHEN 0 THEN (SELECT MAILDATA FROM PREP.PMSG_INBOUNDMAIL WHERE ID=INBOUNDMAILID) ELSE (SELECT MAILDATA FROM PREP.PMSG_OUTBOUNDMAIL WHERE ID=OUTBOUNDMAILID) END AS MAILMESSAGE FROM " + 
		      "(" + 
		      "SELECT INBOUNDMAILID,0 AS OUTBOUNDMAILID,'UNDELIVERED' AS MAILSTATUS,'INCOMING' AS MAILDIRECTION FROM PREP.PMSG_INBOUNDMAILUNFINISHED WHERE INBOUNDMAILID=:INBOUNDMAILID " + 
		      "UNION " + 
		      "SELECT INBOUNDMAILID,0 AS OUTBOUNDMAILID,'DELIVERED' AS MAILSTATUS,'INCOMING' AS MAILDIRECTION FROM PREP.PMSG_INBOUNDMAILCOMPLETED WHERE INBOUNDMAILID=:INBOUNDMAILID " + 
		      "UNION " + 
		      "SELECT INBOUNDMAILID,0 AS OUTBOUNDMAILID,'DELETED' AS MAILSTATUS,'INCOMING' AS MAILDIRECTION FROM PREP.PMSG_INBOUNDMAILDELETED WHERE INBOUNDMAILID=:INBOUNDMAILID " + 
		      "UNION " + 
		      "SELECT INBOUNDMAILID,OUTBOUNDMAILID,'UNSENT' AS MAILSTATUS,'OUTGOING' AS MAILDIRECTION FROM PREP.PMSG_OUTBOUNDMAILUNSENT WHERE INBOUNDMAILID=:INBOUNDMAILID " + 
		      "UNION " + 
		      "SELECT INBOUNDMAILID,OUTBOUNDMAILID,'SENT' AS MAILSTATUS,'OUTGOING' AS MAILDIRECTION FROM PREP.PMSG_OUTBOUNDMAILSENT WHERE INBOUNDMAILID=:INBOUNDMAILID " + 
		      "UNION " + 
		      "SELECT INBOUNDMAILID,OUTBOUNDMAILID,'DELETED' AS MAILSTATUS,'OUTGOING' AS MAILDIRECTION FROM PREP.PMSG_OUTBOUNDMAILDELETED WHERE INBOUNDMAILID=:INBOUNDMAILID) " + 
		      "MAILMOVEMENT ORDER BY INBOUNDMAILID,OUTBOUNDMAILID";
		    try
		    {
		    	//log_debug("SQL UNION COMMAND:"+sqlSelectRelevantMails);
		    	//InovoOpenPGP inovoOpenPGP=new InovoOpenPGP(_pgpFilePath);
		      _inboundoutboundmailsset=new TreeMap<Integer, ArrayList<String>>();
		      Database.executeDBRequest(_inboundoutboundmailsset,"PRESENCE",sqlSelectRelevantMails, _sqlinputparamsMailExport,null);
		      HashMap<String,String> inboundoutboundmailrow = new HashMap<String,String>();
		      int rowindex = -1;
		      int rowscount = _inboundoutboundmailsset.keySet().size();
		      if(rowscount>1){
			      while (rowindex < rowscount) {
			        rowindex++;
		
			        if (rowindex != 0)
			        {
			          if (rowindex >= rowscount) {
			            break;
			          }
			          inboundoutboundmailrow.clear();
			          inboundoutboundmailrow.putAll(Database.rowData(_inboundoutboundmailsset, rowindex));
		
			          if (inboundoutboundmailrow.get("OUTBOUNDMAILID").equals("0")) {
			            inboundoutboundmailrow.put("MAILID", "I" + (String)inboundoutboundmailrow.get("INBOUNDMAILID"));
			          }
			          else {
			            inboundoutboundmailrow.put("MAILID", "O" + (String)inboundoutboundmailrow.get("OUTBOUNDMAILID"));
			          }
			          String archiveFileNamePrefix = extractStringFromMask(this._archivemasksettings.get("ARCHIVEFILEMASKLAYOUT"), inboundoutboundmailrow);
		
			          String sqlMetaFieldsFoundToUpdate = "";
			          metafieldIndex = 1;
			          
			          while (metafieldIndex > 0) {
			            if (inboundoutboundmailrow.containsKey("METAFIELD" + String.valueOf(metafieldIndex) + "NAME")) {
			              sqlMetaFieldsFoundToUpdate = sqlMetaFieldsFoundToUpdate + "METAFIELD" + String.valueOf(metafieldIndex) + "NAME=" + ":METAFIELD" + String.valueOf(metafieldIndex) + "NAME,";
			              sqlMetaFieldsFoundToUpdate = sqlMetaFieldsFoundToUpdate + "METAFIELD" + String.valueOf(metafieldIndex) + "VALUE=" + ":METAFIELD" + String.valueOf(metafieldIndex) + "VALUE,";
			              metafieldIndex++;
			            }
			            else {
			              metafieldIndex = 0;
			            }
			          }
		
			          if (!sqlMetaFieldsFoundToUpdate.equals("")) {
			            sqlMetaFieldsFoundToUpdate = "," + sqlMetaFieldsFoundToUpdate.substring(0, sqlMetaFieldsFoundToUpdate.length() - 1);
			          }
		
			          String sqlInsertMailArchiveRequest = "INSERT INTO <DBUSER>.MAILEXPORTSTAGING (INBOUNDMAILID,OUTBOUNDMAILID,RECORDHANDLEFLAG) SELECT INBOUNDMAILID,OUTBOUNDMAILID,2 FROM (SELECT :INBOUNDMAILID AS INBOUNDMAILID,:OUTBOUNDMAILID AS OUTBOUNDMAILID, (SELECT COUNT(*) FROM <DBUSER>.MAILEXPORTSTAGING WHERE INBOUNDMAILID=:INBOUNDMAILID AND OUTBOUNDMAILID=:OUTBOUNDMAILID) AS MAILIDCOUNT) MAILMATCHCOUNT WHERE MAILMATCHCOUNT.MAILIDCOUNT=0";
			          
			          try {
			            String fieldsInfo = "";
		
			            Database.executeDBRequest(null,"MAILEXPORTER", sqlInsertMailArchiveRequest, inboundoutboundmailrow,null);
		
			            inboundoutboundmailrow.put("MAILREQUESTQKEYID", "0");
		
			            inboundoutboundmailrow.put("MAILMESSAGEBASE64", "");
		
			            Database.executeDBRequest(null,"MAILEXPORTER","SELECT ID AS MAILREQUESTQKEYID FROM <DBUSER>.MAILEXPORTSTAGING WHERE INBOUNDMAILID=:INBOUNDMAILID AND OUTBOUNDMAILID=:OUTBOUNDMAILID AND RECORDHANDLEFLAG=2", inboundoutboundmailrow,null);
			            //Database.executeDBRequest(null,"MAILEXPORTER","SELECT ID AS MAILREQUESTQKEYID, ISNULL(MAILDATA,'') AS MAILMESSAGEBASE64 FROM <DBUSER>.MAILEXPORTSTAGING WHERE INBOUNDMAILID=:INBOUNDMAILID AND OUTBOUNDMAILID=:OUTBOUNDMAILID AND RECORDHANDLEFLAG=2", inboundoutboundmailrow,null);
		
			            if (!((String)inboundoutboundmailrow.get("MAILREQUESTQKEYID")).equals("0")) {
			              log_debug("MAILREQUESTQKEYID=" + (String)inboundoutboundmailrow.get("MAILREQUESTQKEYID"));
		
			              inboundoutboundmailrow.put("ARCHIVEFILEMASK", this._archivemasksettings.get("ARCHIVEFILEMASKLAYOUT"));
			              inboundoutboundmailrow.put("ARCHIVEFILEMASKNAMEPREFIX", archiveFileNamePrefix);
		
			              Database.executeDBRequest(null,"MAILEXPORTER","UPDATE <DBUSER>.MAILEXPORTSTAGING SET AGENTID=:AGENTID,MAILID=:MAILID,ARCHIVEFILEMASK=:ARCHIVEFILEMASK,ARCHIVEFILEMASKNAMEPREFIX=:ARCHIVEFILEMASKNAMEPREFIX" + sqlMetaFieldsFoundToUpdate + ",MAILBOUNDRY=:MAILDIRECTION,LASTACTIONDATETIME=NOW() WHERE ID=:MAILREQUESTQKEYID", inboundoutboundmailrow,null);
		
			              String emailBase64Message = "";
		
			              /*if (((String)inboundoutboundmailrow.get("MAILMESSAGEBASE64")).equals("")) {
			                emailBase64Message =new String(_base64.encode(inboundoutboundmailrow.get("MAILMESSAGE").getBytes()));
			                //emailBase64Message = Base64.encodeBytes(inboundoutboundmailrow.get("MAILMESSAGE").getBytes(),Base64.NO_OPTIONS);
		
			                inboundoutboundmailrow.put("MAILMESSAGEBASE64", emailBase64Message);
			              }
			              else {
			                emailBase64Message = (String)inboundoutboundmailrow.get("MAILMESSAGEBASE64");
			                //inboundoutboundmailrow.put("MAILMESSAGE", Base64.decode(emailBase64Message.getBytes())));
			                inboundoutboundmailrow.put("MAILMESSAGE",new String( _base64.decode(emailBase64Message.getBytes())));
			              }*/
			              
			              emailBase64Message =new String(_base64.encode(inboundoutboundmailrow.get("MAILMESSAGE").getBytes()));
			              //inboundoutboundmailrow.put("MAILMESSAGE",new String( _base64.decode(emailBase64Message.getBytes())));
			              
			              //Database.executeDBRequest(null,"MAILEXPORTER","UPDATE <DBUSER>.MAILEXPORTSTAGING SET MAILDATA=:MAILMESSAGEBASE64,LASTACTIONDATETIME=NOW() WHERE ID=:MAILREQUESTQKEYID AND ISNULL(MAILDATA,'')=''", inboundoutboundmailrow,null);
			              if (!((String)inboundoutboundmailrow.get("MAILMESSAGE")).equals("")) {
			                log_debug("MAILSTAGING REQUEST initiate [EMLMailExtractor]("+String.valueOf(((String)inboundoutboundmailrow.get("MAILMESSAGE")).length())+")");
			                EMLMailExtractor emlMail = new EMLMailExtractor((String)inboundoutboundmailrow.get("MAILMESSAGE"));
			                log_debug("MAILSTAGING REQUEST initiated [EMLMailExtractor]");
			                HashMap<String,String> mailBase64Attachments = new HashMap<String,String>();
		
			                mailBase64Attachments.putAll(emlMail.retrieveAttachentsAsBase64(archiveFileNamePrefix + "_"));
			                
			                if(!mailBase64Attachments.isEmpty()&&(_pgpEnabled)){
			                	HashMap<String,String> mailBase64AttachmentsPGP=new HashMap<String,String>();
			                    for(String attachementFileName:mailBase64Attachments.keySet()){
				                	String currentAttachBase64Content=mailBase64Attachments.get(attachementFileName);
				                	//currentAttachBase64Content=Base64.encodeBytes(_inovopgpopen.encrypt(Base64.decode(currentAttachBase64Content)),Base64.NO_OPTIONS);
				                	currentAttachBase64Content=new String(_base64.encode(_inovopgpopen.encrypt(_base64.decode(currentAttachBase64Content))));
				                	mailBase64AttachmentsPGP.put(attachementFileName+".pgp", currentAttachBase64Content);
				                	log_debug("ATT-FILENAME:"+attachementFileName+".pgp");
				                }
				                mailBase64Attachments.clear();
				                mailBase64Attachments.putAll(mailBase64AttachmentsPGP);
				                mailBase64AttachmentsPGP.clear();
				                mailBase64AttachmentsPGP=null;
			                }
		
			                MailExportRequestMetadata mailExportRequestMetadata = new MailExportRequestMetadata();
		
			                _wfs_Multimedia = new WFS_Multimedia();
			                log_debug("MAILSTAGING REQUEST initiated [WFS_Multimedia]");
			                String currentemailBase64Message=emailBase64Message;
			                if(_pgpEnabled&&!currentemailBase64Message.equals("")){
			                	log_debug("PGP ENCRYPT mail["+String.valueOf(currentemailBase64Message.length())+"]");
			                	//byte[]decodedEMialMessageBytes=Base64.decode(currentemailBase64Message.getBytes());
			                	byte[]decodedEMialMessageBytes=_base64.decode(currentemailBase64Message.getBytes());
			                	decodedEMialMessageBytes=_inovopgpopen.encrypt(decodedEMialMessageBytes);
			                	log_debug("PGP ENCRYPT mail [DECRYPTED BASE64-BYTES] "+String.valueOf(decodedEMialMessageBytes.length)+"]");
			                	//currentemailBase64Message=Base64.encodeBytes(decodedEMialMessageBytes,Base64.NO_OPTIONS);
			                	currentemailBase64Message=new String(_base64.encode(decodedEMialMessageBytes));
			                	log_debug("PGP ENCRYPTED mail");
			                }
			                
			                log_debug("EML-FILENAME:"+archiveFileNamePrefix + "_Email.eml"+(_pgpEnabled?".pgp":""));
			                _wfs_Multimedia.addEmailMessage(archiveFileNamePrefix + "_Email.eml"+(_pgpEnabled?".pgp":""),currentemailBase64Message);
		
			                int attIndex = 1;
		
			                log_debug("MAILSTAGING REQUEST add attachments [WFS_Multimedia]");
			                for (String attFileName : mailBase64Attachments.keySet()) {
			                  String formattedAttFilename = archiveFileNamePrefix + "_Attachment" + String.valueOf(attIndex);
			                  if (attFileName.indexOf(".") > -1) {
			                    formattedAttFilename = formattedAttFilename + attFileName.substring(attFileName.indexOf("."));
			                  }
			                  log_debug("FORMATTED ATT-FILENAME:"+attFileName);
			                  _wfs_Multimedia.addAttachment((String)mailBase64Attachments.get(attFileName), formattedAttFilename, attIndex);
			                  attIndex++;
			                }
		
			                log_debug("MAILSTAGING REQUEST attachments added [WFS_Multimedia]");
		
			                
			                String emailContactId = (String)inboundoutboundmailrow.get("MAILID");
			                
			                String emailDirection=emailContactId.substring(0,1).toUpperCase();
			                
			                emailContactId=(archiveFileNamePrefix.indexOf("_")>-1?archiveFileNamePrefix.substring(0,archiveFileNamePrefix.indexOf("_")+1):"")+emailContactId;
			                String customerIDNumber = this._archiveSettingsToProcess.get("METAFIELD1VALUE");
			                if(customerIDNumber==null){
			                	customerIDNumber="0000000000000";
			                }
			                String emailAddress = emlMail.getFromAddress();
			                String emailSubjectLine = emlMail.getSubject();
			                String documentType = "";
		
			                if (((String)inboundoutboundmailrow.get("MAILDIRECTION")).equals("INCOMING")) {
			                  documentType = "Customer Email";
			                }
			                else if (((String)inboundoutboundmailrow.get("MAILDIRECTION")).equals("OUTGOING")) {
			                  documentType = "Reply to Customer Email";
			                }
			                log_debug("MAILSTAGING REQUEST attachments MAILDIRECTION " + documentType);
			                Date emailDate = emlMail.getEmailDate();
			                
			                if(emailSubjectLine.equals("")) emailSubjectLine="[no subject]";
			                
			                if(!emailSubjectLine.equals("")&&_pgpEnabled){
			                	//emailSubjectLine=Base64.encodeBytes(_inovopgpopen.encrypt(emailSubjectLine),Base64.NO_OPTIONS);
			                	emailSubjectLine=new String(_base64.encode(_inovopgpopen.encrypt(emailSubjectLine)));
			                }
			                
			                String emailChannel=(emailDirection.equals("O")?emailAddress:emlMail.getToAddress());
			                
			                if(emailChannel.equals("")&&emailDirection.equals("I")){
			                	log_debug("WARNING [INBOUND MAIL ID - " + (String)inboundoutboundmailrow.get("INBOUNDMAILID") + "] RECIPIENT(TO) ADDRESS EMPTY");
			                }
			                
			                _wfs_Multimedia.addMetaData(emailChannel, emailContactId, customerIDNumber, emailAddress, emailDate, emailSubjectLine, documentType);
			                log_debug("MAILSTAGING REQUEST archive mail requested");
			                /*_webCallThread=new WebCallThread(wfs_Multimedia);
			                
			                _webCallThread.start();
			                synchronized (_wsCallLock) {
								_wsCallLock.wait(3*60*1024);
								if(_webCallThread._responseStatus.equals("")){
									_webCallThread._wsex=new Exception("INTERNAL CALL TIMEOUT");
								}
							}
			                
			                if(_webCallThread._wsex!=null){
			                	throw _webCallThread._wsex;
			                }
			                
			                String responseStatusmessage = _webCallThread._responseStatus; //responseStatusmessage=wfs_Multimedia.requestArchive(InovoServletContextListener.inovoServletListener().configProperty("WSMAILSERVICEURL"));
			                _webCallThread=null;
			                */
			                _responseStatusmessage ="";
			                _mwsex=null;
			                _urlRefWS=InovoServletContextListener.inovoServletListener().configProperty("WSMAILSERVICEURL");
			                /*_wsArchiveService.execute(new Runnable(){
			                	public void run() {
			                		try{
		                				_responseStatusmessage=_wfs_Multimedia.requestArchive(_urlRefWS);
		                				
			                		}
			                		catch(Exception ext){
			                			_mwsex=ext;
			                		}
			                		synchronized (_wsCallLock) {
				                		
			                			_wsCallLock.notifyAll();
			                		}
			                	};
			                });*/
			                
			                if(_wsCallLock!=null){
			                	/*Thread executeThread=new Thread(new Runnable(){
					                	public void run() {
					                		try{
				                				_responseStatusmessage=_wfs_Multimedia.requestArchive(_urlRefWS);
				                				
					                		}
					                		catch(Exception ext){
					                			_mwsex=ext;
					                		}
					                		synchronized (_wsCallLock) {
						                		
					                			_wsCallLock.notifyAll();
					                		}
					                	};
					                });
			                	executeThread.start();*/
			                	_wsArchiveService.execute(new Runnable(){
					                	public void run() {
					                		try{
				                				_responseStatusmessage=_wfs_Multimedia.requestArchive(_urlRefWS);
				                				
					                		}
					                		catch(Exception ext){
					                			_mwsex=ext;
					                		}
					                		synchronized (_wsCallLock) {
						                		
					                			_wsCallLock.notifyAll();
					                		}
					                	};
					                });
				                synchronized (_wsCallLock) {
				                	_wsCallLock.wait(WS_WFS_MultimediaSOAPStub._requestTimeoutMilliseconds+(10*1000));
				                	if(_responseStatusmessage.equals("")){
				                		_mwsex=new Exception("[REQUEST TIME OUT INTERNAL]:"+this._mailReqID);
				                		//executeThread.interrupt();
				                		//executeThread=null;
				                		_wfs_Multimedia=null;
				                	}
				                	_wsCallLock.notifyAll();
								}
			                }
			                else{
			                	try{
	                				_responseStatusmessage=_wfs_Multimedia.requestArchive(_urlRefWS);
		                		}
		                		catch(Exception ext){
		                			_mwsex=ext;
		                		}
			                }
			                if(_mwsex!=null){
			                	_wfs_Multimedia=null;
			                	throw _mwsex;
			                }
			                _wfs_Multimedia=null;
			                log_debug("MAILSTAGING REQUEST archive mail response - " + _responseStatusmessage);
			                mailBase64Attachments.clear();
		
			                inboundoutboundmailrow.put("LASTREQUESTDURATION", String.valueOf(Calendar.getInstance().getTimeInMillis() - millisecondsStart));
			                inboundoutboundmailrow.put("LASTREQUESTSTARTDATESTAMP", requestStartDatetime);
		
			                if (_responseStatusmessage.startsWith("ERROR:")) {
			                  log_debug("UPDATE MAILREQUEST IN DB -" + _responseStatusmessage);
		
			                  inboundoutboundmailrow.put("LASTREQUESTOUTCOMEMESSAGE", _responseStatusmessage);
			                  Database.executeDBRequest(null,"MAILEXPORTER","UPDATE <DBUSER>.MAILEXPORTSTAGING SET LASTREQUESTDURATION=:LASTREQUESTDURATION,LASTREQUESTSTARTDATESTAMP=:LASTREQUESTSTARTDATESTAMP,LASTACTIONDATETIME=NOW(),RECORDHANDLEFLAG=5,LASTREQUESTOUTCOMEMESSAGE=:LASTREQUESTOUTCOMEMESSAGE WHERE RECORDHANDLEFLAG=2 AND INBOUNDMAILID=:INBOUNDMAILID", inboundoutboundmailrow,null);
			                }
			                else {
			                  log_debug("UPDATE MAILREQUEST IN DB -" + _responseStatusmessage);
			                  inboundoutboundmailrow.put("LASTREQUESTOUTCOMEMESSAGE", "COMPLETED:" + _responseStatusmessage);
			                  Database.executeDBRequest(null,"MAILEXPORTER","UPDATE <DBUSER>.MAILEXPORTSTAGING SET LASTREQUESTDURATION=:LASTREQUESTDURATION,LASTREQUESTSTARTDATESTAMP=:LASTREQUESTSTARTDATESTAMP,LASTACTIONDATETIME=NOW(),RECORDHANDLEFLAG=3,LASTREQUESTOUTCOMEMESSAGE=:LASTREQUESTOUTCOMEMESSAGE WHERE RECORDHANDLEFLAG=2 AND INBOUNDMAILID=:INBOUNDMAILID", inboundoutboundmailrow,null);
			                }
			              }
			              else {
			                log_debug("UPDATE MAILREQUEST IN DB - EMPTYMAILMESSAGE=" + (String)inboundoutboundmailrow.get("EMPTYMAILMESSAGE"));
			                inboundoutboundmailrow.put("LASTREQUESTOUTCOMEMESSAGE", "COMPLETED:" + (String)inboundoutboundmailrow.get("MAILDIRECTION") + ":EMPTYMAILMESSAGE");
			                Database.executeDBRequest(null,"MAILEXPORTER","UPDATE <DBUSER>.MAILEXPORTSTAGING SET LASTREQUESTDURATION=:LASTREQUESTDURATION,LASTREQUESTSTARTDATESTAMP=:LASTREQUESTSTARTDATESTAMP,LASTACTIONDATETIME=NOW(),RECORDHANDLEFLAG=6,LASTREQUESTOUTCOMEMESSAGE=:LASTREQUESTOUTCOMEMESSAGE WHERE RECORDHANDLEFLAG=2 AND INBOUNDMAILID=:INBOUNDMAILID", inboundoutboundmailrow,null);
			              }
			            }
			          } catch (Exception e) {
			            e.printStackTrace();
			            log_debug("ERROR [INBOUND MAIL ID - " + (String)inboundoutboundmailrow.get("INBOUNDMAILID") + "]" + e.getMessage());
			            inboundoutboundmailrow.put("LASTREQUESTDURATION", String.valueOf(Calendar.getInstance().getTimeInMillis() - millisecondsStart));
			            inboundoutboundmailrow.put("LASTREQUESTSTARTDATESTAMP", requestStartDatetime);
			            inboundoutboundmailrow.put("LASTREQUESTOUTCOMEMESSAGE", e.getMessage());
			            Database.executeDBRequest(null,"MAILEXPORTER","UPDATE <DBUSER>.MAILEXPORTSTAGING SET LASTREQUESTDURATION=:LASTREQUESTDURATION,LASTREQUESTSTARTDATESTAMP=:LASTREQUESTSTARTDATESTAMP,RECORDHANDLEFLAG=4,LASTREQUESTOUTCOMEMESSAGE=:LASTREQUESTOUTCOMEMESSAGE WHERE RECORDHANDLEFLAG=2 AND INBOUNDMAILID=:INBOUNDMAILID", inboundoutboundmailrow,null);
			          }
			        }
			      }
		      }
		      else{
		    	  _sqlinputparamsMailExport.put("LASTREQUESTOUTCOMEMESSAGE", "NO MAIL CONTENT FOUND IN PRECENCE");
	              Database.executeDBRequest(null,"MAILEXPORTER","UPDATE <DBUSER>.MAILEXPORTSTAGING SET LASTREQUESTDURATION=0,LASTREQUESTSTARTDATESTAMP=GETDATE(),LASTACTIONDATETIME=NOW(),RECORDHANDLEFLAG=7,LASTREQUESTOUTCOMEMESSAGE=:LASTREQUESTOUTCOMEMESSAGE WHERE RECORDHANDLEFLAG=2 AND INBOUNDMAILID=:INBOUNDMAILID AND METAFIELD1NAME=:METAFIELD1NAME AND METAFIELD1VALUE=:METAFIELD1VALUE AND AGENTID=:AGENTID", _sqlinputparamsMailExport,null);
		    	  log_debug("MAILSTAGING REQUEST ERROR [executeMailExportRequest()]: NO MAIL CONTENT FOUND IN PRECENCE");
		    	  return;
		      }
		    } catch (Exception e) {
		    	log_debug("MAILSTAGING REQUEST ERROR [executeMailExportRequest()]:"+e.getMessage());
		    	e.printStackTrace(); }
		    if(_inboundoutboundmailsset!=null){
		    	Database.cleanupDataset(_inboundoutboundmailsset);
		    	_inboundoutboundmailsset=null;
		    }
		    if(_sqlinputparamsMailExport!=null){
		    	_sqlinputparamsMailExport.clear();
		    	_sqlinputparamsMailExport=null;
		    }
		}

		@Override
		public void run() {
			try{
				this.executeMailExportRequest();
			}
			catch(Exception exe){
				if(_wsCallLock!=null)_wsCallLock.notifyAll();
				log_debug("ERROR:[executeMailExportRequest()]:"+this._mailReqID+":"+exe.getMessage());
			}
			if(_wsCallLock!=null){
				_wsCallLock=null;
			}
			synchronized (_exportMailItems) {
				_exportMailItems.remove(_mailReqID);
			}
			
			/*if(_webCallThread!=null){
				_webCallThread=null;
			}*/
			this._archivemasksettings.clear();
			this._archivemasksettings=null;
			this._archiveSettingsToProcess.clear();
			this._archiveSettingsToProcess=null;
		}
	}
	
	protected class MailExportCleanupStage{
		
		private int _columnDataIndex=0;
		public void readRowData(Integer rowindex,ArrayList<String> rowData,ArrayList<String> columnData){
			if(rowindex==0) return;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
			}
			_incompleteArchiveSettings.clear();
			_columnDataIndex=0;
			while(_columnDataIndex<columnData.size()){
				_incompleteArchiveSettings.put(columnData.get(_columnDataIndex), rowData.get(_columnDataIndex++));
			}
            
			HashMap<String,String> archiveRequestSettings = new HashMap<String,String>();
            archiveRequestSettings.put("AGENTID", (String)_incompleteArchiveSettings.get("AGENTID"));

            String archiveLabels = "AGENTID,INBOUNDMAILID";

            int metaFieldIndex = 1;
            while (metaFieldIndex > 0) {
              if (((String)_incompleteArchiveSettings.get("METAFIELD" + String.valueOf(metaFieldIndex) + "NAME")).equals("")) {
                metaFieldIndex = 0;
              }
              else {
                archiveLabels = archiveLabels + ",METAFIELD" + String.valueOf(metaFieldIndex) + "VALUE";
                archiveRequestSettings.put("METAFIELD" + String.valueOf(metaFieldIndex) + "NAME", (String)_incompleteArchiveSettings.get("METAFIELD" + String.valueOf(metaFieldIndex) + "NAME"));
                archiveRequestSettings.put("METAFIELD" + String.valueOf(metaFieldIndex) + "VALUE", (String)_incompleteArchiveSettings.get("METAFIELD" + String.valueOf(metaFieldIndex) + "VALUE"));
                metaFieldIndex++;
              }
            }
            archiveRequestSettings.put("INBOUNDMAILID", (String)_incompleteArchiveSettings.get("INBOUNDMAILID"));

            try {
				requestMailArchive(archiveRequestSettings, archiveLabels.split(","), (String)_incompleteArchiveSettings.get("ARCHIVEFILEMASK"), "".split(","),false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private MailExportCleanupStage _mailExportCleanupStage=new MailExportCleanupStage();
	
	private static String _pgpFilePath="";
	private static boolean _pgpEnabled=false;
	private InovoOpenPGP _inovopgpopen=null;
	
	private HashMap<String,ArrayList<HashMap<String,String>>> _mailExportRequests=new HashMap<String,ArrayList<HashMap<String,String>>>();
	protected static boolean _shutdownQueue=false;
	private static Object _queueLock=new Object();
	public MailExportQueue(){
	}
	
	private int _shutdownLevels=0;
	public void initiateQueue(){
		_pgpFilePath=inovo.servlet.InovoServletContextListener.inovoServletListener().configProperty("PGPFILE");
		if(!_pgpFilePath.equals("")){
			try {
				_inovopgpopen=new InovoOpenPGP(_pgpFilePath);
				_pgpEnabled=true;
				log_debug("PGP INITIATED["+_pgpFilePath+"]");
			} catch (InovoOpenPGPException e) {
				log_debug("PGP INITIATE ERROR["+_pgpFilePath+"]:"+e.getMessage());
			}
		}
		
		
		new Thread(){
			public void run() {
				log_debug("START MAILEXPORT DB CLEANUP");
				
				while(!_shutdownQueue){
					mailExportDbCleanup();
					try {
						Thread.sleep((_shutdownQueue?5:2)*1024);
					} catch (InterruptedException e) {

					}
				
					processMailExportRequests();
					try {
						Thread.sleep(2*1024);
					} catch (InterruptedException e) {

					}
				}
				log_debug("SHUTDOWN MAILEXPORT DB CLEANUP");
			};
		}.start();
		
		/*new Thread(){
			public void run() {
				log_debug("START MAILEXPORT ITEM EXPORTQUEUE");
				
				while(!_shutdownQueue){
					ExportMailRequest exportMailRequest=null;
					if((exportMailRequest=_queuedExportMailRequests.poll())!=null)
						_extracteArchiveService.execute(exportMailRequest);
					try {
						Thread.sleep((_shutdownQueue?10:2)*100);
					} catch (InterruptedException e) {
					}
				}
				try {
					Thread.sleep((_shutdownQueue?10:2)*1024);
				} catch (InterruptedException e) {

				}
				while(_shutdownQueue&&!_queuedExportMailRequests.isEmpty()){
					new Thread(_queuedExportMailRequests.poll()).start();
				}
				log_debug("SHUTDOWN MAILEXPORT ITEM EXPORTQUEUE");
			};
		}.start();*/
		
		//_extracteArchiveService=new ThreadPoolExecutor(0, 5, WS_WFS_MultimediaSOAPStub._requestTimeoutMilliseconds+(10*1000),TimeUnit.MILLISECONDS , new SynchronousQueue<Runnable>());
		_wsArchiveService=new ThreadPoolExecutor(0, 5, WS_WFS_MultimediaSOAPStub._requestTimeoutMilliseconds+(10*1000),TimeUnit.MILLISECONDS , new SynchronousQueue<Runnable>());
	}
	
	private HashMap<String,ArrayList<HashMap<String,String>>> _currentMailExportRequests=new HashMap<String,ArrayList<HashMap<String,String>>>();
	//private ArrayList<String> _currentArchiveKeys=new ArrayList<String>();
	
	public void processMailExportRequests(){
		if(_mailExportRequests.isEmpty()) return;
		while(!_mailExportRequests.isEmpty()){
			String currentArchiveKey=(String)_mailExportRequests.keySet().toArray()[0];
			_currentMailExportRequests.put(currentArchiveKey, _mailExportRequests.remove(currentArchiveKey));
		}
		
		while(!_currentMailExportRequests.isEmpty()){
			final String archivekey=(String)_currentMailExportRequests.keySet().toArray()[0];
			ArrayList<HashMap<String,String>> mailExportSettingsList=_currentMailExportRequests.remove(archivekey);
			
			HashMap<String,String> archiveSettingsToProcess=mailExportSettingsList.remove(0);
		    HashMap<String,String> archiveMaskSettings=mailExportSettingsList.remove(0);
    		
		    ExportMailRequest exportMailRequest=null;
		    synchronized (_exportMailItems) {
		    	if(!_exportMailItems.containsKey(archivekey)){
		    		_exportMailItems.put(archivekey, exportMailRequest=new ExportMailRequest(archivekey, archiveSettingsToProcess, archiveMaskSettings));
		    	}
			}
    		if(exportMailRequest!=null){
    			//_queuedExportMailRequests.add(exportMailRequest);
    			//_extracteArchiveService.execute(exportMailRequest);
    			exportMailRequest.run();
    		}
    		    		
    		//internalArchiveSettingsToProcess.clear();
    		//internalArchiveSettingsToProcess=null;
    		//internalArchiveMaskSettings.clear();
    		//internalArchiveMaskSettings=null;
    		
			archiveSettingsToProcess.clear();
			archiveSettingsToProcess=null;
			archiveMaskSettings.clear();
			archiveMaskSettings=null;
			
			mailExportSettingsList.clear();
			mailExportSettingsList=null;
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
	}
	
	/*public void executeMailExportRequest(String mailReqID,HashMap<String,String> archiveSettingsToProcess,HashMap<String,String> archivemasksettings)
	{
		if(archiveSettingsToProcess.isEmpty()) return;
		
		TreeMap<Integer,ArrayList<String>> _inboundoutboundmailsset=null;
		HashMap<String,String> _sqlinputparamsMailExport = new HashMap<String,String>();
		
		//log_debug("MAILSTAGING REQUEST START [executeMailExportRequest()]");
	    long millisecondsStart = Calendar.getInstance().getTimeInMillis();
	    String requestStartDatetime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(Long.valueOf(Calendar.getInstance().getTimeInMillis()));
	    
	    _sqlinputparamsMailExport.clear();
	   
	    _sqlinputparamsMailExport.put("INBOUNDMAILID", archiveSettingsToProcess.get("INBOUNDMAILID"));
	    _sqlinputparamsMailExport.put("AGENTID", archiveSettingsToProcess.get("AGENTID"));

	    String metaFieldsToAdd = "";
	    int metafieldIndex = 1;

	    while (metafieldIndex > 0) {
	      if ((archiveSettingsToProcess.containsKey("METAFIELD" + String.valueOf(metafieldIndex) + "NAME")) && (archiveSettingsToProcess.containsKey("METAFIELD" + String.valueOf(metafieldIndex) + "VALUE")))
	      {
	        metaFieldsToAdd = metaFieldsToAdd + ":METAFIELD" + String.valueOf(metafieldIndex) + "NAME" + " AS " + "METAFIELD" + String.valueOf(metafieldIndex) + "NAME,";
	        _sqlinputparamsMailExport.put("METAFIELD" + String.valueOf(metafieldIndex) + "NAME", archiveSettingsToProcess.get("METAFIELD" + String.valueOf(metafieldIndex) + "NAME"));

	        metaFieldsToAdd = metaFieldsToAdd + ":METAFIELD" + String.valueOf(metafieldIndex) + "VALUE" + " AS " + "METAFIELD" + String.valueOf(metafieldIndex) + "VALUE,";
	        _sqlinputparamsMailExport.put("METAFIELD" + String.valueOf(metafieldIndex) + "VALUE", archiveSettingsToProcess.get("METAFIELD" + String.valueOf(metafieldIndex) + "VALUE"));

	        metafieldIndex++;
	      }
	      else {
	        metafieldIndex = 0;
	      }
	    }

	    String sqlSelectRelevantMails = "SELECT :AGENTID AS AGENTID," + metaFieldsToAdd + " INBOUNDMAILID,OUTBOUNDMAILID,MAILDIRECTION,MAILSTATUS,CASE OUTBOUNDMAILID WHEN 0 THEN (SELECT MAILDATA FROM PREP.PMSG_INBOUNDMAIL WHERE ID=INBOUNDMAILID) ELSE (SELECT MAILDATA FROM PREP.PMSG_OUTBOUNDMAIL WHERE ID=OUTBOUNDMAILID) END AS MAILMESSAGE FROM " + 
	      "(" + 
	      "SELECT INBOUNDMAILID,0 AS OUTBOUNDMAILID,'UNDELIVERED' AS MAILSTATUS,'INCOMING' AS MAILDIRECTION FROM PREP.PMSG_INBOUNDMAILUNFINISHED WHERE INBOUNDMAILID=:INBOUNDMAILID " + 
	      "UNION " + 
	      "SELECT INBOUNDMAILID,0 AS OUTBOUNDMAILID,'DELIVERED' AS MAILSTATUS,'INCOMING' AS MAILDIRECTION FROM PREP.PMSG_INBOUNDMAILCOMPLETED WHERE INBOUNDMAILID=:INBOUNDMAILID " + 
	      "UNION " + 
	      "SELECT INBOUNDMAILID,0 AS OUTBOUNDMAILID,'DELETED' AS MAILSTATUS,'INCOMING' AS MAILDIRECTION FROM PREP.PMSG_INBOUNDMAILDELETED WHERE INBOUNDMAILID=:INBOUNDMAILID " + 
	      "UNION " + 
	      "SELECT INBOUNDMAILID,OUTBOUNDMAILID,'UNSENT' AS MAILSTATUS,'OUTGOING' AS MAILDIRECTION FROM PREP.PMSG_OUTBOUNDMAILUNSENT WHERE INBOUNDMAILID=:INBOUNDMAILID " + 
	      "UNION " + 
	      "SELECT INBOUNDMAILID,OUTBOUNDMAILID,'SENT' AS MAILSTATUS,'OUTGOING' AS MAILDIRECTION FROM PREP.PMSG_OUTBOUNDMAILSENT WHERE INBOUNDMAILID=:INBOUNDMAILID " + 
	      "UNION " + 
	      "SELECT INBOUNDMAILID,OUTBOUNDMAILID,'DELETED' AS MAILSTATUS,'OUTGOING' AS MAILDIRECTION FROM PREP.PMSG_OUTBOUNDMAILDELETED WHERE INBOUNDMAILID=:INBOUNDMAILID) " + 
	      "MAILMOVEMENT ORDER BY INBOUNDMAILID,OUTBOUNDMAILID";
	    try
	    {
	    	//log_debug("SQL UNION COMMAND:"+sqlSelectRelevantMails);
	    	//InovoOpenPGP inovoOpenPGP=new InovoOpenPGP(_pgpFilePath);
	      _inboundoutboundmailsset=new TreeMap<Integer, ArrayList<String>>();
	      Database.executeDBRequest(_inboundoutboundmailsset,"PRESENCE",sqlSelectRelevantMails, _sqlinputparamsMailExport,null);
	      HashMap<String,String> inboundoutboundmailrow = new HashMap<String,String>();
	      int rowindex = -1;
	      int rowscount = _inboundoutboundmailsset.keySet().size();
	      if(rowscount>1){
		      while (rowindex < rowscount) {
		        rowindex++;
	
		        if (rowindex != 0)
		        {
		          if (rowindex >= rowscount) {
		            break;
		          }
		          inboundoutboundmailrow.clear();
		          inboundoutboundmailrow.putAll(Database.rowData(_inboundoutboundmailsset, rowindex));
	
		          if (inboundoutboundmailrow.get("OUTBOUNDMAILID").equals("0")) {
		            inboundoutboundmailrow.put("MAILID", "I" + (String)inboundoutboundmailrow.get("INBOUNDMAILID"));
		          }
		          else {
		            inboundoutboundmailrow.put("MAILID", "O" + (String)inboundoutboundmailrow.get("OUTBOUNDMAILID"));
		          }
		          String archiveFileNamePrefix = extractStringFromMask(archivemasksettings.get("ARCHIVEFILEMASKLAYOUT"), inboundoutboundmailrow);
	
		          String sqlMetaFieldsFoundToUpdate = "";
		          metafieldIndex = 1;
		          
		          while (metafieldIndex > 0) {
		            if (inboundoutboundmailrow.containsKey("METAFIELD" + String.valueOf(metafieldIndex) + "NAME")) {
		              sqlMetaFieldsFoundToUpdate = sqlMetaFieldsFoundToUpdate + "METAFIELD" + String.valueOf(metafieldIndex) + "NAME=" + ":METAFIELD" + String.valueOf(metafieldIndex) + "NAME,";
		              sqlMetaFieldsFoundToUpdate = sqlMetaFieldsFoundToUpdate + "METAFIELD" + String.valueOf(metafieldIndex) + "VALUE=" + ":METAFIELD" + String.valueOf(metafieldIndex) + "VALUE,";
		              metafieldIndex++;
		            }
		            else {
		              metafieldIndex = 0;
		            }
		          }
	
		          if (!sqlMetaFieldsFoundToUpdate.equals("")) {
		            sqlMetaFieldsFoundToUpdate = "," + sqlMetaFieldsFoundToUpdate.substring(0, sqlMetaFieldsFoundToUpdate.length() - 1);
		          }
	
		          String sqlInsertMailArchiveRequest = "INSERT INTO <DBUSER>.MAILEXPORTSTAGING (INBOUNDMAILID,OUTBOUNDMAILID,RECORDHANDLEFLAG) SELECT INBOUNDMAILID,OUTBOUNDMAILID,2 FROM (SELECT :INBOUNDMAILID AS INBOUNDMAILID,:OUTBOUNDMAILID AS OUTBOUNDMAILID, (SELECT COUNT(*) FROM PTOOLS.MAILEXPORTSTAGING WHERE INBOUNDMAILID=:INBOUNDMAILID AND OUTBOUNDMAILID=:OUTBOUNDMAILID) AS MAILIDCOUNT) MAILMATCHCOUNT WHERE MAILMATCHCOUNT.MAILIDCOUNT=0";
		          
		          try {
		            String fieldsInfo = "";
	
		            Database.executeDBRequest(null,"MAILEXPORTER", sqlInsertMailArchiveRequest, inboundoutboundmailrow,null);
	
		            inboundoutboundmailrow.put("MAILREQUESTQKEYID", "0");
	
		            inboundoutboundmailrow.put("MAILMESSAGEBASE64", "");
	
		            Database.executeDBRequest(null,"MAILEXPORTER","SELECT ID AS MAILREQUESTQKEYID, ISNULL(MAILDATA,'') AS MAILMESSAGEBASE64 FROM <DBUSER>.MAILEXPORTSTAGING WHERE INBOUNDMAILID=:INBOUNDMAILID AND OUTBOUNDMAILID=:OUTBOUNDMAILID AND RECORDHANDLEFLAG=2", inboundoutboundmailrow,null);
	
		            if (!((String)inboundoutboundmailrow.get("MAILREQUESTQKEYID")).equals("0")) {
		              log_debug("MAILREQUESTQKEYID=" + (String)inboundoutboundmailrow.get("MAILREQUESTQKEYID"));
	
		              inboundoutboundmailrow.put("ARCHIVEFILEMASK", archivemasksettings.get("ARCHIVEFILEMASKLAYOUT"));
		              inboundoutboundmailrow.put("ARCHIVEFILEMASKNAMEPREFIX", archiveFileNamePrefix);
	
		              Database.executeDBRequest(null,"MAILEXPORTER","UPDATE <DBUSER>.MAILEXPORTSTAGING SET AGENTID=:AGENTID,MAILID=:MAILID,ARCHIVEFILEMASK=:ARCHIVEFILEMASK,ARCHIVEFILEMASKNAMEPREFIX=:ARCHIVEFILEMASKNAMEPREFIX" + sqlMetaFieldsFoundToUpdate + ",MAILBOUNDRY=:MAILDIRECTION,LASTACTIONDATETIME=NOW() WHERE ID=:MAILREQUESTQKEYID", inboundoutboundmailrow,null);
	
		              String emailBase64Message = "";
	
		              if (((String)inboundoutboundmailrow.get("MAILMESSAGEBASE64")).equals("")) {
		                //emailBase64Message = Base64.encodeBytes(inboundoutboundmailrow.get("MAILMESSAGE").getBytes(),Base64.NO_OPTIONS);
		            	  emailBase64Message = new String(_base64.encode(inboundoutboundmailrow.get("MAILMESSAGE").getBytes()));
		                inboundoutboundmailrow.put("MAILMESSAGEBASE64", emailBase64Message);
		              }
		              else {
		                emailBase64Message = (String)inboundoutboundmailrow.get("MAILMESSAGEBASE64");
		                //inboundoutboundmailrow.put("MAILMESSAGE", new String(Base64.decode(emailBase64Message.getBytes())));
		                inboundoutboundmailrow.put("MAILMESSAGE", new String(_base64.decode(emailBase64Message.getBytes())));
		              }
	
		              Database.executeDBRequest(null,"MAILEXPORTER","UPDATE <DBUSER>.MAILEXPORTSTAGING SET MAILDATA=:MAILMESSAGEBASE64,LASTACTIONDATETIME=NOW() WHERE ID=:MAILREQUESTQKEYID AND ISNULL(MAILDATA,'')=''", inboundoutboundmailrow,null);
		              if (!((String)inboundoutboundmailrow.get("MAILMESSAGE")).equals("")) {
		                log_debug("MAILSTAGING REQUEST initiate [EMLMailExtractor]");
		                EMLMailExtractor emlMail = new EMLMailExtractor((String)inboundoutboundmailrow.get("MAILMESSAGE"));
	
		                HashMap<String,String> mailBase64Attachments = new HashMap<String,String>();
	
		                mailBase64Attachments.putAll(emlMail.retrieveAttachentsAsBase64(archiveFileNamePrefix + "_"));
		                
		                if(!mailBase64Attachments.isEmpty()&&(_pgpEnabled)){
		                	HashMap<String,String> mailBase64AttachmentsPGP=new HashMap<String,String>();
		                    for(String attachementFileName:mailBase64Attachments.keySet()){
			                	String currentAttachBase64Content=mailBase64Attachments.get(attachementFileName);
			                	//currentAttachBase64Content=Base64.encodeBytes(_inovopgpopen.encrypt(Base64.decode(currentAttachBase64Content)),Base64.NO_OPTIONS);
			                	currentAttachBase64Content=new String(_base64.encode(_inovopgpopen.encrypt(_base64.decode(currentAttachBase64Content))));
			                	mailBase64AttachmentsPGP.put(attachementFileName+".pgp", currentAttachBase64Content);
			                	this.log_debug("ATT-FILENAME:"+attachementFileName+".pgp");
			                }
			                mailBase64Attachments.clear();
			                mailBase64Attachments.putAll(mailBase64AttachmentsPGP);
			                mailBase64AttachmentsPGP.clear();
			                mailBase64AttachmentsPGP=null;
		                }
	
		                MailExportRequestMetadata mailExportRequestMetadata = new MailExportRequestMetadata();
	
		                log_debug("MAILSTAGING REQUEST initiate [WFS_Multimedia]");
		                WFS_Multimedia wfs_Multimedia = new WFS_Multimedia();
		                
		                String currentemailBase64Message=emailBase64Message;
		                if(_pgpEnabled&&!currentemailBase64Message.equals("")){
		                	//currentemailBase64Message=Base64.encodeBytes(_inovopgpopen.encrypt(Base64.decode(currentemailBase64Message.getBytes())),Base64.NO_OPTIONS);
		                	currentemailBase64Message=new String(_base64.encode(_inovopgpopen.encrypt(_base64.decode(currentemailBase64Message.getBytes()))));
		                }
		                
		                this.log_debug("EML-FILENAME:"+archiveFileNamePrefix + "_Email.eml"+(_pgpEnabled?".pgp":""));
		                wfs_Multimedia.addEmailMessage(archiveFileNamePrefix + "_Email.eml"+(_pgpEnabled?".pgp":""),currentemailBase64Message);
	
		                int attIndex = 1;
	
		                log_debug("MAILSTAGING REQUEST add attachments [WFS_Multimedia]");
		                for (String attFileName : mailBase64Attachments.keySet()) {
		                  String formattedAttFilename = archiveFileNamePrefix + "_Attachment" + String.valueOf(attIndex);
		                  if (attFileName.indexOf(".") > -1) {
		                    formattedAttFilename = formattedAttFilename + attFileName.substring(attFileName.indexOf("."));
		                  }
		                  this.log_debug("FORMATTED ATT-FILENAME:"+attFileName);
		                  wfs_Multimedia.addAttachment((String)mailBase64Attachments.get(attFileName), formattedAttFilename, attIndex);
		                  attIndex++;
		                }
	
		                log_debug("MAILSTAGING REQUEST attachments added [WFS_Multimedia]");
	
		                
		                String emailContactId = (String)inboundoutboundmailrow.get("MAILID");
		                
		                String emailDirection=emailContactId.substring(0,1).toUpperCase();
		                
		                emailContactId=(archiveFileNamePrefix.indexOf("_")>-1?archiveFileNamePrefix.substring(0,archiveFileNamePrefix.indexOf("_")+1):"")+emailContactId;
		                String customerIDNumber = archiveSettingsToProcess.get("METAFIELD1VALUE");
		                if(customerIDNumber==null){
		                	customerIDNumber="0000000000000";
		                }
		                String emailAddress = emlMail.getFromAddress();
		                String emailSubjectLine = emlMail.getSubject();
		                String documentType = "";
	
		                if (((String)inboundoutboundmailrow.get("MAILDIRECTION")).equals("INCOMING")) {
		                  documentType = "Customer Email";
		                }
		                else if (((String)inboundoutboundmailrow.get("MAILDIRECTION")).equals("OUTGOING")) {
		                  documentType = "Reply to Customer Email";
		                }
		                log_debug("MAILSTAGING REQUEST attachments MAILDIRECTION " + documentType);
		                Date emailDate = emlMail.getEmailDate();
		                
		                if(emailSubjectLine.equals("")) emailSubjectLine="<no subject>";
		                
		                if(!emailSubjectLine.equals("")&&_pgpEnabled){
		                	//emailSubjectLine=Base64.encodeBytes(_inovopgpopen.encrypt(emailSubjectLine),Base64.NO_OPTIONS);
		                	emailSubjectLine=new String(_base64.encode(_inovopgpopen.encrypt(emailSubjectLine)));
		                }
		                
		                String emailChannel=(emailDirection.equals("O")?emailAddress:emlMail.getToAddress());
		                
		                if(emailChannel.equals("")&&emailDirection.equals("I")){
		                	log_debug("WARNING [INBOUND MAIL ID - " + (String)inboundoutboundmailrow.get("INBOUNDMAILID") + "] RECIPIENT(TO) ADDRESS EMPTY");
		                }
		                
		                wfs_Multimedia.addMetaData(emailChannel, emailContactId, customerIDNumber, emailAddress, emailDate, emailSubjectLine, documentType);
		                log_debug("MAILSTAGING REQUEST archive mail requested");
		                String responseStatusmessage = responseStatusmessage=wfs_Multimedia.requestArchive(InovoServletContextListener.inovoServletListener().configProperty("WSMAILSERVICEURL"));
		                
		                log_debug("MAILSTAGING REQUEST archive mail response - " + responseStatusmessage);
		                mailBase64Attachments.clear();
	
		                inboundoutboundmailrow.put("LASTREQUESTDURATION", String.valueOf(Calendar.getInstance().getTimeInMillis() - millisecondsStart));
		                inboundoutboundmailrow.put("LASTREQUESTSTARTDATESTAMP", requestStartDatetime);
	
		                if (responseStatusmessage.startsWith("ERROR:")) {
		                  log_debug("UPDATE MAILREQUEST IN DB -" + responseStatusmessage);
	
		                  inboundoutboundmailrow.put("LASTREQUESTOUTCOMEMESSAGE", responseStatusmessage);
		                  Database.executeDBRequest(null,"MAILEXPORTER","UPDATE <DBUSER>.MAILEXPORTSTAGING SET LASTREQUESTDURATION=:LASTREQUESTDURATION,LASTREQUESTSTARTDATESTAMP=:LASTREQUESTSTARTDATESTAMP,LASTACTIONDATETIME=NOW(),RECORDHANDLEFLAG=5,LASTREQUESTOUTCOMEMESSAGE=:LASTREQUESTOUTCOMEMESSAGE WHERE RECORDHANDLEFLAG=2 AND INBOUNDMAILID=:INBOUNDMAILID", inboundoutboundmailrow,null);
		                }
		                else {
		                  log_debug("UPDATE MAILREQUEST IN DB -" + responseStatusmessage);
		                  inboundoutboundmailrow.put("LASTREQUESTOUTCOMEMESSAGE", "COMPLETED:" + responseStatusmessage);
		                  Database.executeDBRequest(null,"MAILEXPORTER","UPDATE <DBUSER>.MAILEXPORTSTAGING SET LASTREQUESTDURATION=:LASTREQUESTDURATION,LASTREQUESTSTARTDATESTAMP=:LASTREQUESTSTARTDATESTAMP,LASTACTIONDATETIME=NOW(),RECORDHANDLEFLAG=3,LASTREQUESTOUTCOMEMESSAGE=:LASTREQUESTOUTCOMEMESSAGE WHERE RECORDHANDLEFLAG=2 AND INBOUNDMAILID=:INBOUNDMAILID", inboundoutboundmailrow,null);
		                }
		              }
		              else {
		                log_debug("UPDATE MAILREQUEST IN DB - EMPTYMAILMESSAGE=" + (String)inboundoutboundmailrow.get("EMPTYMAILMESSAGE"));
		                inboundoutboundmailrow.put("LASTREQUESTOUTCOMEMESSAGE", "COMPLETED:" + (String)inboundoutboundmailrow.get("MAILDIRECTION") + ":EMPTYMAILMESSAGE");
		                Database.executeDBRequest(null,"MAILEXPORTER","UPDATE <DBUSER>.MAILEXPORTSTAGING SET LASTREQUESTDURATION=:LASTREQUESTDURATION,LASTREQUESTSTARTDATESTAMP=:LASTREQUESTSTARTDATESTAMP,LASTACTIONDATETIME=NOW(),RECORDHANDLEFLAG=6,LASTREQUESTOUTCOMEMESSAGE=:LASTREQUESTOUTCOMEMESSAGE WHERE RECORDHANDLEFLAG=2 AND INBOUNDMAILID=:INBOUNDMAILID", inboundoutboundmailrow,null);
		              }
		            }
		          } catch (Exception e) {
		            e.printStackTrace();
		            log_debug("ERROR [INBOUND MAIL ID - " + (String)inboundoutboundmailrow.get("INBOUNDMAILID") + "]" + e.getMessage());
		            inboundoutboundmailrow.put("LASTREQUESTDURATION", String.valueOf(Calendar.getInstance().getTimeInMillis() - millisecondsStart));
		            inboundoutboundmailrow.put("LASTREQUESTSTARTDATESTAMP", requestStartDatetime);
		            inboundoutboundmailrow.put("LASTREQUESTOUTCOMEMESSAGE", e.getMessage());
		            Database.executeDBRequest(null,"MAILEXPORTER","UPDATE <DBUSER>.MAILEXPORTSTAGING SET LASTREQUESTDURATION=:LASTREQUESTDURATION,LASTREQUESTSTARTDATESTAMP=:LASTREQUESTSTARTDATESTAMP,RECORDHANDLEFLAG=4,LASTREQUESTOUTCOMEMESSAGE=:LASTREQUESTOUTCOMEMESSAGE WHERE RECORDHANDLEFLAG=2 AND INBOUNDMAILID=:INBOUNDMAILID", inboundoutboundmailrow,null);
		          }
		        }
		      }
	      }
	      else{
	    	  _sqlinputparamsMailExport.put("LASTREQUESTOUTCOMEMESSAGE", "NO MAIL CONTENT FOUND IN PRECENCE");
              Database.executeDBRequest(null,"MAILEXPORTER","UPDATE <DBUSER>.MAILEXPORTSTAGING SET LASTREQUESTDURATION=0,LASTREQUESTSTARTDATESTAMP=GETDATE(),LASTACTIONDATETIME=NOW(),RECORDHANDLEFLAG=7,LASTREQUESTOUTCOMEMESSAGE=:LASTREQUESTOUTCOMEMESSAGE WHERE RECORDHANDLEFLAG=2 AND INBOUNDMAILID=:INBOUNDMAILID AND METAFIELD1NAME=:METAFIELD1NAME AND METAFIELD1VALUE=:METAFIELD1VALUE AND AGENTID=:AGENTID", _sqlinputparamsMailExport,null);
	    	  log_debug("MAILSTAGING REQUEST ERROR [executeMailExportRequest()]: NO MAIL CONTENT FOUND IN PRECENCE");
	    	  return;
	      }
	    } catch (Exception e) {
	    	log_debug("MAILSTAGING REQUEST ERROR [executeMailExportRequest()]:"+e.getMessage());
	    	e.printStackTrace(); }
	    if(_inboundoutboundmailsset!=null){
	    	Database.cleanupDataset(_inboundoutboundmailsset);
	    	_inboundoutboundmailsset=null;
	    }
	    if(_sqlinputparamsMailExport!=null){
	    	_sqlinputparamsMailExport.clear();
	    	_sqlinputparamsMailExport=null;
	    }
	}*/
	
	public String extractStringFromMask(String maskToFormat, HashMap<String, String> possibleValuePair) {
	     StringBuilder formattedMaskValue = new StringBuilder(maskToFormat);
	     String valuePairName="";
	     
	     for (Iterator<String> localIterator = possibleValuePair.keySet().iterator(); localIterator.hasNext();){ 
	    	 valuePairName = (String)localIterator.next();
		     if(formattedMaskValue.indexOf("[" + valuePairName + "]") == -1)
		     {
		        continue;
		     }
	        formattedMaskValue.replace(formattedMaskValue.indexOf("[" + valuePairName + "]"), formattedMaskValue.indexOf("[" + valuePairName + "]") + ("[" + valuePairName + "]").length(), (String)possibleValuePair.get(valuePairName));
	     }
	 
	     return formattedMaskValue.toString();
	}
	
	TreeMap<Integer,ArrayList<String>> _incompleteArchiveRequestsSet=null;
	
	HashMap<String,String> _incompleteArchiveSettings = new HashMap<String,String>();
	private StringBuilder _metaFieldSqlCols = new StringBuilder();
	
	private ArrayList<String> _mailJobsHangging=new ArrayList<String>();
	
	public void mailExportDbCleanup(){
		_mailJobsHangging.clear();
		
		try {
    	  Database.executeDBRequest(null,"MAILEXPORTER", "UPDATE <DBUSER>.MAILEXPORTSTAGING SET RECORDHANDLEFLAG=2 WHERE RECORDHANDLEFLAG=1", null,null);

    	  _metaFieldSqlCols.setLength(0);

	        for (int metaColindex = 1; metaColindex <= 20; metaColindex++) {
	          _metaFieldSqlCols.append("METAFIELD" + String.valueOf(metaColindex) + "NAME, METAFIELD" + String.valueOf(metaColindex) + "VALUE,");
	        }
	        
	        Database.executeDBRequest(null,"MAILEXPORTER", "SELECT TOP 20 ARCHIVEFILEMASK,AGENTID," + _metaFieldSqlCols.toString() + "INBOUNDMAILID FROM <DBUSER>.MAILEXPORTSTAGING WHERE RECORDHANDLEFLAG=2 AND ISNULL(AGENTID,0) > 0 ORDER BY ID DESC",null,_mailExportCleanupStage,"readRowData");
		} catch (Exception e) {
			e.printStackTrace();
		}  
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		
	        /*
	        if(_incompleteArchiveRequestsSet==null) _incompleteArchiveRequestsSet=new TreeMap<Integer, ArrayList<String>>();
	        Database.executeDBRequest(_incompleteArchiveRequestsSet,"MAILEXPORTER", "SELECT ARCHIVEFILEMASK,AGENTID," + _metaFieldSqlCols.toString() + "INBOUNDMAILID FROM <DBUSER>.MAILEXPORTSTAGING WHERE RECORDHANDLEFLAG=2",null,null);
	        if(_incompleteArchiveRequestsSet==null) return;
	        if(_incompleteArchiveRequestsSet.size()==1){
	        	Database.cleanupDataset(_incompleteArchiveRequestsSet);
	        	_incompleteArchiveRequestsSet=null;
	        	return;
	        }
	        
	        for(int rowindex:_incompleteArchiveRequestsSet.keySet()){
	        	if(rowindex==0) continue;
	            _incompleteArchiveSettings.clear();
	            _incompleteArchiveSettings.putAll(Database.rowData(_incompleteArchiveRequestsSet, rowindex));
	            HashMap<String,String> archiveRequestSettings = new HashMap<String,String>();
	            archiveRequestSettings.put("AGENTID", (String)_incompleteArchiveSettings.get("AGENTID"));

	            String archiveLabels = "AGENTID,INBOUNDMAILID";

	            int metaFieldIndex = 1;
	            while (metaFieldIndex > 0) {
	              if (((String)_incompleteArchiveSettings.get("METAFIELD" + String.valueOf(metaFieldIndex) + "NAME")).equals("")) {
	                metaFieldIndex = 0;
	              }
	              else {
	                archiveLabels = archiveLabels + ",METAFIELD" + String.valueOf(metaFieldIndex) + "VALUE";
	                archiveRequestSettings.put("METAFIELD" + String.valueOf(metaFieldIndex) + "NAME", (String)_incompleteArchiveSettings.get("METAFIELD" + String.valueOf(metaFieldIndex) + "NAME"));
	                archiveRequestSettings.put("METAFIELD" + String.valueOf(metaFieldIndex) + "VALUE", (String)_incompleteArchiveSettings.get("METAFIELD" + String.valueOf(metaFieldIndex) + "VALUE"));
	                metaFieldIndex++;
	              }
	            }
	            archiveRequestSettings.put("INBOUNDMAILID", (String)_incompleteArchiveSettings.get("INBOUNDMAILID"));

	            this.requestMailArchive(archiveRequestSettings, archiveLabels.split(","), (String)_incompleteArchiveSettings.get("ARCHIVEFILEMASK"), "".split(","),false);
	            //synchronized (_mailExportQueue) {
					try {
					//	_mailExportQueue.wait(2);
						Thread.sleep(50);
					} catch (InterruptedException e) {
					}
				//}
	          } 
		} catch (Exception e) {
			e.printStackTrace();
		}
		_incompleteArchiveSettings.clear();
		
		if(_incompleteArchiveRequestsSet!=null){
			Database.cleanupDataset(_incompleteArchiveRequestsSet);
			_incompleteArchiveRequestsSet=null;
		}*/
	}
	
	private static MailExportQueue _mailExportQueue=null;
	public static MailExportQueue mailEportQueue(){
		if(_mailExportQueue==null) _mailExportQueue=new MailExportQueue();
		return _mailExportQueue;
	}
	
	public static Boolean killMainExportQueueScheduler=false;
	private static Object _mainExportQueueSchedulerLock=new Object();
	
	public static void initiateMailExportQueue(final int polingmilliseconds){
		new Thread(){
			public void run() {
				while(!killMainExportQueueScheduler){
					if(_mailExportQueue==null){
						(_mailExportQueue=new MailExportQueue()).initiateQueue();
					}
					synchronized (_mainExportQueueSchedulerLock) {
						try {
							_mainExportQueueSchedulerLock.wait(polingmilliseconds);
						} catch (InterruptedException e) {
						}
					}
					if(_mailExportQueue!=null){
						_mailExportQueue.shutdownQueue();
						_mailExportQueue=null;
					}
				}
				if(_mailExportQueue!=null){
					_mailExportQueue.shutdownQueue();
				}
			};
		}.start();
	}
	
	public static void killMainExportQueueScheduler(){
		killMainExportQueueScheduler=true;
		synchronized (_mainExportQueueSchedulerLock) {
			_mainExportQueueSchedulerLock.notifyAll();
		}
		if(_mailExportQueue!=null){
			_mailExportQueue.shutdownQueue();
		}
	}
	
	public void shutdownQueue(){
		this._shutdownQueue=true;
		
		/*this._extracteArchiveService.shutdown();
		while(!this._extracteArchiveService.isShutdown()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}*/
		
		//while(!_mailExportRequests.isEmpty()){
		//	try {
		//		Thread.sleep(1*1024);
		//	} catch (InterruptedException e) {
		//		e.printStackTrace();
		//	}
		//}
		
		this._wsArchiveService.shutdown();
		while(!this._wsArchiveService.isShutdown()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		
		while(!this._currentMailExportRequests.isEmpty()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		
		if(_pgpEnabled){
			_inovopgpopen=null;
		}
	}
	
	public String requestMailArchive(HashMap<String, String> archiveRequestSettings, String[] archiveMailIDLayout, String archiveFileMaskNameLayout, String[] requiredMetaFields,boolean insertIntoDbDirect)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
    String validationError = "";

    if (archiveRequestSettings.containsKey("AGENTID")) {
		if (((String)archiveRequestSettings.get("AGENTID")).equals("")) {
			validationError = "AGENT ID NOT PROVIDED,";
		}
    }
    else {
    	validationError = "AGENT ID NOT PROVIDED,";
    }
    if (archiveRequestSettings.containsKey("INBOUNDMAILID")) {
	    if (((String)archiveRequestSettings.get("INBOUNDMAILID")).equals("")) {
	    	validationError = validationError + "INBOUNDMAILID ID NOT PROVIDED,";
	    }
    }
    else {
      validationError = validationError + "INBOUNDMAILID ID NOT PROVIDED,";
    }

    for (String metaFieldRef : requiredMetaFields) {
      if (archiveRequestSettings.containsKey(metaFieldRef + "NAME")) {
        if (archiveRequestSettings.containsKey(metaFieldRef + "VALUE")) {
          if (((String)archiveRequestSettings.get(metaFieldRef + "VALUE")).equals("")) {
            validationError = validationError + (String)archiveRequestSettings.get(new StringBuilder(String.valueOf(metaFieldRef)).append("NAME").toString()) + " NOT PROVIDED,";
          }
        }
        else {
          validationError = validationError + (String)archiveRequestSettings.get(new StringBuilder(String.valueOf(metaFieldRef)).append("NAME").toString()) + " NOT PROVIDED,";
        }
      }
    }

    if (validationError.equals("")) {
    	if(insertIntoDbDirect){
    		try {
    	    	  Database.executeDBRequest(null,"MAILEXPORTER","INSERT INTO <DBUSER>.MAILEXPORTSTAGING (ARCHIVEFILEMASK, AGENTID,INBOUNDMAILID,METAFIELD1NAME,METAFIELD1VALUE,RECORDHANDLEFLAG) SELECT 'PRSC_[AGENTID]_[METAFIELD1VALUE]_[MAILID]',:AGENTID,:INBOUNDMAILID,:METAFIELD1NAME,:METAFIELD1VALUE,1",archiveRequestSettings,null);
    		}
    		catch(Exception edb){
    			validationError=edb.getMessage();
    			edb.printStackTrace();
    		}
    	}
    	else{
	      ArrayList<HashMap<String,String>> customactionsettings = new ArrayList<HashMap<String,String>>();
	      HashMap<String,String> archivemasksettings = new HashMap<String,String>();
	      archivemasksettings.put("ARCHIVEFILEMASKLAYOUT", archiveFileMaskNameLayout);
	      customactionsettings.add(archiveRequestSettings);
	      customactionsettings.add(archivemasksettings);
	      
	      String archiveMailID = "";
	      for (String archiveMailIDLayoutItem : archiveMailIDLayout) {
	        if (archiveRequestSettings.containsKey(archiveMailIDLayoutItem)) {
	          archiveMailID = archiveMailID + (String)archiveRequestSettings.get(archiveMailIDLayoutItem) + "_";
	        }
	      }
	      if (!archiveMailID.equals("")) {
	        archiveMailID = archiveMailID.substring(0, archiveMailID.length() - 1);
	      }
	      
	      synchronized (_exportMailItems) {
			if(!_exportMailItems.containsKey(archiveMailID)){
				_mailExportRequests.put(archiveMailID, customactionsettings);
			}
	      } 
     }
      return "";
    }

    validationError = validationError.substring(0, validationError.length() - 1);
    log_debug("[E][requestMailArchive()]" + validationError);
    return validationError;
  }

  private static void log_debug(String message) {
	 InovoServletContextListener.inovoServletListener().logger().debug(message);
  }
}