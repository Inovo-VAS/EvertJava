 package inovo.email;
 
 import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
 
 public class EMLMailExtractor
 {
   private HashMap<String, EMLMailAttachment> _emlMailAttachments = null;
 
   MimeMessage _msg = null;
 
   Multipart _multipart = null;
 
   public EMLMailExtractor(String inElmAsString) throws MessagingException, IOException {
     this(new ByteArrayInputStream(inElmAsString.getBytes()));
   }
 
   public EMLMailExtractor(InputStream inElm) throws MessagingException, IOException
   {
     Properties mailprop = new Properties();
 
     Session mySession = Session.getDefaultInstance(mailprop);
 
     this._msg = new MimeMessage(mySession, inElm);
     Object content = this._msg.getContent();
     if ((content instanceof Multipart))
       this._multipart = ((Multipart)content);
   }
 
   private void extractELMAttachments() throws MessagingException, IOException
   {
     if (this._emlMailAttachments == null) {
       this._emlMailAttachments = new HashMap<String,EMLMailAttachment>();
 
       if (this._multipart != null)
         internalMailContentarator(this._multipart);
     }
   }
 
   private void internalMailContentarator(Multipart mp) throws MessagingException, IOException
   {
     int count = mp.getCount();
     for (int i = 0; i < count; i++)
     {
       BodyPart bp = mp.getBodyPart(i);
       System.out.println(bp.getFileName());
       Object content = bp.getContent();
       if ((content instanceof String))
       {
         if (bp.getFileName() != null) {
           InputStream in = bp.getInputStream();
           ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
           int bytesRead = 0;
           byte[] bytesfound = new byte[1024];
           while ((bytesRead = in.read(bytesfound)) > -1) {
             bytesOut.write(bytesfound, 0, bytesRead);
           }
           this._emlMailAttachments.put(bp.getFileName(), new EMLMailAttachment(this, bp.getFileName(), bytesOut.toByteArray()));
         }
       }
       else if ((content instanceof InputStream))
       {
         InputStream in = bp.getInputStream();
 
         if (bp.getFileName() != null) {
           this._emlMailAttachments.put(bp.getFileName(), new EMLMailAttachment(this, bp.getFileName(), IOUtils.toByteArray(in)));
         }
       }
       else if ((content instanceof Multipart))
       {
         Multipart mp2 = (Multipart)content;
         internalMailContentarator(mp2);
       }
     }
   }
 
   public HashMap<String, String> retrieveAttachentsAsBase64(String attachmentFileNamePrefix) throws Exception {
     HashMap base64Attachments = new HashMap();
 
     extractELMAttachments();
     int attFileNr = 1;
     for (String attFileName : this._emlMailAttachments.keySet()) {
       base64Attachments.put(attachmentFileNamePrefix + attFileName, ((EMLMailAttachment)this._emlMailAttachments.get(attFileName)).getAttDataAsBase64());
     }
 
     return base64Attachments;
   }
 
   public Date getEmailDate() {
     try {
    	 Date dateToReturn=this._msg.getSentDate();
       return (dateToReturn==null?Calendar.getInstance().getTime():dateToReturn); 
     } catch (MessagingException e) {
     }
     return Calendar.getInstance().getTime();
   }
   
   public String getRecipientAddress(RecipientType reciepentType){
	  return this.getRecipientAddress(false,reciepentType); 
   }
   
   public String getToAddress(){
	   return this.getRecipientAddress(false, RecipientType.TO);
   }
   
   public String getToAddress(boolean fullAddress){
	   return this.getRecipientAddress(fullAddress, RecipientType.TO);
   }
   
   public String getCCAddress(){
	   return this.getRecipientAddress(false, RecipientType.CC);
   }
   
   public String getCCAddress(boolean fullAddress){
	   return this.getRecipientAddress(fullAddress, RecipientType.CC);
   }
   
   public String getBCCAddress(){
	   return this.getRecipientAddress(false, RecipientType.BCC);
   }
   
   public String getBCCAddress(boolean fullAddress){
	   return this.getRecipientAddress(fullAddress, (RecipientType) RecipientType.BCC);
   }
   
   public String getAllRecipientAddress(){
	   return this.getRecipientAddress(false, null);
   }
   
   public String getAllRecipientAddress(boolean fullAddress){
	   return this.getRecipientAddress(fullAddress, null);
   }
   
   public String getRecipientAddress(boolean fullAddress,RecipientType reciepentType){
	   try {
	       String recipientAddressesFound = "";
	       Address[] recipientAddresses=(reciepentType==null?this._msg.getAllRecipients():this._msg.getRecipients(reciepentType));
	       if(recipientAddresses!=null){
		       for (Address recipientEMailAdd : recipientAddresses) {
		         if (recipientEMailAdd == null) {
		        	 recipientAddressesFound = recipientAddressesFound + ";";
		         }
		         if (fullAddress) {
		        	 recipientAddressesFound = recipientAddressesFound + recipientEMailAdd.toString() + ";";
		         }
		         else {
		        	 recipientAddressesFound = recipientAddressesFound + ((InternetAddress)recipientEMailAdd).getAddress() + ";";
		         }
		       }
	       }
	       if (!recipientAddressesFound.equals("")){
	    	   return recipientAddressesFound.substring(0, recipientAddressesFound.length() - 1);
	       }
	     }
	     catch (MessagingException e)
	     {
	     }
	     return "";
   }
 
   public String getFromAddress()
   {
     return getFromAddress(false);
   }
 
   public String getFromAddress(boolean fullAddress)
   {
     try {
       String fromAddressesFound = "";
       Address[] fromAddresses=this._msg.getFrom();
       if(fromAddresses!=null){
	       for (Address fromEMailAdd : fromAddresses) {
	         if (fromEMailAdd == null) {
	           fromAddressesFound = fromAddressesFound + ";";
	         }
	         if (fullAddress) {
	           fromAddressesFound = fromAddressesFound + fromEMailAdd.toString() + ";";
	         }
	         else {
	           fromAddressesFound = fromAddressesFound + ((InternetAddress)fromEMailAdd).getAddress() + ";";
	         }
	       }
       }
       if (!fromAddressesFound.equals(""));
       return fromAddressesFound.substring(0, fromAddressesFound.length() - 1);
     }
     catch (MessagingException e)
     {
     }
     return "";
   }
 
   public String getSubject()
   {
     try {
       String subjectLineToReturn = this._msg.getSubject();
       if (subjectLineToReturn == null) {
         return "";
       }
       return subjectLineToReturn; } catch (MessagingException e) {
     }
     return "";
   }
 }