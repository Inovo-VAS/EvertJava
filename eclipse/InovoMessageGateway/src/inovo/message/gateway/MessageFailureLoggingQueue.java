package inovo.message.gateway;

import inovo.adhoc.Base64;
import inovo.db.Database;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import javax.xml.crypto.Data;

public class MessageFailureLoggingQueue implements Runnable{
	private static MessageFailureLoggingQueue _messageFailureLoggingQueue=null;
	private static boolean _tryLoadingFailures=true;
	private static ArrayList<HashMap<String,String>> _messageFailureList=new ArrayList<HashMap<String,String>>();
	private boolean _shutdown=false;
	private ArrayList<HashMap<String,String>> _messageFailureListBeingLoaded=new ArrayList<HashMap<String,String>>();
	private static String _loggerFilePath="";
	
	private File _fileLoggingInput=null;
	
	@Override
	public void run() {
		_fileLoggingInput=new File(_loggerFilePath+"/MessageFailureLogging.log");
		if(!_fileLoggingInput.exists()){
			try {
				_fileLoggingInput.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		while(!_shutdown){
			//failures read into file
			try{
				synchronized (_messageFailureList) {
					while(!_messageFailureList.isEmpty()) _messageFailureListBeingLoaded.add(_messageFailureList.remove(0));
				}
				if(!_messageFailureListBeingLoaded.isEmpty()){
					if(!_fileLoggingInput.exists()){
						try {
							_fileLoggingInput.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if(_fileLoggingInput.exists()){
						FileOutputStream fileLoggingInputStream=new FileOutputStream(_fileLoggingInput,true);
						while(!_messageFailureListBeingLoaded.isEmpty()){
							HashMap<String, String> messageFailureToLogData=_messageFailureListBeingLoaded.remove(0);
							String messageFailureToLogLine="";
							for(String messageFailureToLogKey:messageFailureToLogData.keySet()){
								messageFailureToLogLine+="\""+messageFailureToLogKey.replaceAll("\"", "\"\"").toUpperCase()+"\",";
							}
							messageFailureToLogLine=messageFailureToLogLine.substring(0,messageFailureToLogLine.length()-1)+"\r\n";
							for(String messageFailureToLogKey:messageFailureToLogData.keySet()){
								messageFailureToLogLine+="\""+messageFailureToLogData.get(messageFailureToLogKey).replaceAll("\"", "\"\"").toUpperCase()+"\",";
							}
							messageFailureToLogLine=messageFailureToLogLine.substring(0,messageFailureToLogLine.length()-1)+"\r\n";
							
							byte[] messageFailureToLogLineBytes=(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+":"+Base64.encodeBytes(messageFailureToLogLine.getBytes())+"\r\n").getBytes();
							try{
								fileLoggingInputStream.write(messageFailureToLogLineBytes,0,messageFailureToLogLineBytes.length);
								fileLoggingInputStream.flush();
							}
							catch(Exception ex){
								ex.printStackTrace();
							}
						}
						try {
							fileLoggingInputStream.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						fileLoggingInputStream=null;
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			if(_tryLoadingFailures){
				_tryLoadingFailures=false;
				if(_fileLoggingInput.exists()){
					ArrayList<HashMap<String,String>> currentFailureEntriesToRetry=new ArrayList<HashMap<String,String>>();
					TreeMap<Integer, ArrayList<String>> dataSetFailureEntryToRetry=new TreeMap<Integer,ArrayList<String>>();
					
					try {
						
						FileInputStream fileLoggingOutputStream=new FileInputStream(_fileLoggingInput);
						
						byte[] bytesWrittenOutBuffer=new byte[8912];
						int bytesReadOutSize=fileLoggingOutputStream.read(bytesWrittenOutBuffer,0,bytesWrittenOutBuffer.length);
						String currentBase64String="";
						
						while(bytesReadOutSize>-1){
							if(bytesReadOutSize==0){
								Thread.sleep(2);
							}
							int bytesReadOutIndex=0;
							
							while(bytesReadOutIndex<bytesReadOutSize){
								byte br=bytesWrittenOutBuffer[bytesReadOutIndex++];
								if(br==13) continue;
								if(br==10){
									if(currentBase64String.indexOf(":")>-1){
										currentBase64String=new String(Base64.decode(currentBase64String.substring(currentBase64String.indexOf(":")+1)));
										Database.populateDatasetFromFlatFileStream(dataSetFailureEntryToRetry,"CSV",new ByteArrayInputStream(currentBase64String.getBytes()),null,',',null);
										HashMap<String,String> failureEntryMap=new HashMap<String,String>();
										failureEntryMap.putAll(Database.rowData(dataSetFailureEntryToRetry, 1));
										if(!failureEntryMap.isEmpty()){
											currentFailureEntriesToRetry.add(failureEntryMap);
										}
									}
									currentBase64String="";
								}
								else{
									currentBase64String+=((char)br+"");
								}
							}
							bytesReadOutSize=fileLoggingOutputStream.read(bytesWrittenOutBuffer,0,bytesWrittenOutBuffer.length);
						}
						
						fileLoggingOutputStream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					ArrayList<HashMap<String,String>> currentFailureEntriesThatFailed=new ArrayList<HashMap<String,String>>();
					if(!currentFailureEntriesToRetry.isEmpty()){
						_fileLoggingInput.delete();
						try {
							_fileLoggingInput.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					while(!currentFailureEntriesToRetry.isEmpty()){
						HashMap<String,String> messageParamsEntry=currentFailureEntriesToRetry.remove(0);
						try {
							MessagingQueue.messagingQueue().addNextMessageParams(messageParamsEntry);
						} catch (Exception e) {
							e.printStackTrace();
							currentFailureEntriesThatFailed.add(messageParamsEntry);
						}
					}
					
					if(!currentFailureEntriesThatFailed.isEmpty()){
						
						synchronized (_messageFailureList) {
							while(!currentFailureEntriesThatFailed.isEmpty()){
								_messageFailureList.add(currentFailureEntriesThatFailed.remove(0));
							}
						}
					}
					Database.cleanupDataset(dataSetFailureEntryToRetry);
				}
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void initMessageFailureLoggingQueue(String loggerFilePath){
		if(_messageFailureLoggingQueue==null){
			_loggerFilePath=loggerFilePath+"log/";
			new Thread(_messageFailureLoggingQueue=new MessageFailureLoggingQueue()).start();
		}
	}
	
	public static MessageFailureLoggingQueue messageFailureLoggingQueue(){
		initMessageFailureLoggingQueue(_loggerFilePath);
		return _messageFailureLoggingQueue;
	}
	
	public void shutdown(){
		this._shutdown=true;
	}
	
	public static void tryLoadingFailures(){
		_tryLoadingFailures=true;
	}
	
	public static void logFailureMessage(HashMap<String,String> failureMessageParams){
		HashMap<String,String> failureMessageEntry=new HashMap<String,String>();
		failureMessageEntry.putAll(failureMessageParams);
		synchronized (_messageFailureList) {
			_messageFailureList.add(failureMessageEntry);
		}
	}
}
