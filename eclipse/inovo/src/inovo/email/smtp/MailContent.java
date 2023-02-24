package inovo.email.smtp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import inovo.adhoc.Base64;


public class MailContent {
	private MailClient _mailClient=null;
	
	private String _contentBoundary="";
	public MailContent( MailClient mailClient){
		this._mailClient=mailClient;
		this.setContentBoundary(this.nextContentBoundary());
		this.setupMailContent();
	}
	
	public void setContentBoundary(String contentBoundary) {
		this._contentBoundary=contentBoundary;
	}
	
	public String contentBoundary(){
		return this._contentBoundary;
	}

	public String nextContentBoundary() {
		return "contentBoundary"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	}
	
	public MailClient mailClient(){
		return this._mailClient;
	}
	
	public void setupMailContent() {	
	}

	private String _fromAddress="";
		
	public void setFromAddress(String fromAddress){
		this._fromAddress=fromAddress;
	}
	
	public String fromAddress(){
		return this._fromAddress;
	}
	
	
	private ArrayList<String> _recipients=new ArrayList<String>();
	
	public void addRecipient(String recipient){
		if(this._recipients.indexOf(recipient)==-1){
			this._recipients.add(recipient);
		}
	}
	
	public ArrayList<String> recipients(){
		return this._recipients;
	}
	
	private ArrayList<String> _bccRecipients=new ArrayList<String>();
	
	public void addBccRecipient(String bccRecipient){
		if(this._bccRecipients.indexOf(bccRecipient)==-1){
			this._bccRecipients.add(bccRecipient);
		}
	}
	
	public ArrayList<String> bbRecipients(){
		return this._bccRecipients;
	}
	
	private String _subject="";
	
	public void setSubject(String subject){
		this._subject=subject;
	}
	
	public String subject(){
		return this._subject;
	}
	
	public HashMap<String,String> _bodyProperties=new HashMap<String,String>();
	public InputStream _bodyInputStream=null;
	
	public void setBodyProperties(String[]properties){
		if(properties!=null){
			for(String propitem:properties){
				if(propitem.indexOf("=")>-1){
					String propName=propitem.substring(0,propitem.indexOf("=")).toUpperCase().trim();
					String propValue=propitem.substring(propitem.indexOf("=")+1).trim();
					_bodyProperties.put(propName, propValue);
				}
				else if(propitem.indexOf(":")>-1){
					String propName=propitem.substring(0,propitem.indexOf(":")).toUpperCase().trim();
					String propValue=propitem.substring(propitem.indexOf(":")+1).trim();
					_bodyProperties.put(propName, propValue);
				}
			}
		}
	}
	
	public void setBody(String[]properties,InputStream bodyInputStream){
		this.setBodyProperties(properties);
		this.setBodyInputStream(bodyInputStream);
	}
	
	public HashMap<String,String> bodyProperties(){
		return this._bodyProperties;
	}
	
	public void setBodyInputStream(InputStream bodyInputStream){
		this._bodyInputStream=bodyInputStream;
	}
	
	public InputStream bodyInputStream(){
		return this._bodyInputStream;
	}
	
	public void base64FlushInputStream(InputStream inputStreamToFlush) throws Exception{
		ByteArrayOutputStream bytesStreamOut=new ByteArrayOutputStream();
		
		byte [] bytesinput=new byte[8912];
		int bytesinputavailable=0;
		int bytesLength=0;
		
		while((bytesinputavailable=inputStreamToFlush.read(bytesinput,0,8912))>-1){
			if(bytesinputavailable>0){
				bytesLength+=bytesinputavailable;
				bytesStreamOut.write(bytesinput,0,bytesinputavailable);
			}
		}
		
		ByteArrayInputStream bytesStreamIn=new ByteArrayInputStream(bytesStreamOut.toByteArray());
		bytesStreamOut.reset();
		
		Base64.encodeInputStreamToOutputStream(bytesLength, bytesStreamIn,bytesStreamOut);
		
		this._mailClient.sendBytes(bytesStreamOut.toByteArray());
		//encodeInputStreamToOutputMethod(inputStreamToFlush, this._mailClient, this._mailClient.getClass().getMethod("sendBytes", byte[].class), true);
	}
	
	private HashMap<String,InputStream> _attachments=new HashMap<String,InputStream>();
	private HashMap<String,HashMap<String,String>> _attachmentProperties=new HashMap<String,HashMap<String,String>>();
		
	public void addAttachment(String name,String[] properties,
			FileInputStream fileInputStream) {
		this.addAttachment(name, properties,(InputStream) fileInputStream);
	}
	
	public void addAttachment(String name,String[] properties,
			InputStream attachmentInputStream) {
		if(!this._attachments.containsKey(name)){
			this._attachments.put(name, attachmentInputStream);
			HashMap<String,String> attProperties=new HashMap<String,String>();
			this._attachmentProperties.put(name, attProperties);
			if(properties!=null){
				for(String propitem:properties){
					if(propitem.indexOf("=")>-1){
						String propName=propitem.substring(0,propitem.indexOf("=")).toUpperCase().trim();
						String propValue=propitem.substring(propitem.indexOf("=")+1).trim();
						attProperties.put(propName, propValue);
					}
					else if(propitem.indexOf(":")>-1){
						String propName=propitem.substring(0,propitem.indexOf(":")).toUpperCase().trim();
						String propValue=propitem.substring(propitem.indexOf(":")+1).trim();
						attProperties.put(propName, propValue);
					}
				}
			}
		}
	}
	
	public HashMap<String,String> attachentProperties(String name){
		return this._attachmentProperties.get(name);
	}
	
	public HashMap<String,InputStream> attachments(){
		return this._attachments;
	}
	
} 
