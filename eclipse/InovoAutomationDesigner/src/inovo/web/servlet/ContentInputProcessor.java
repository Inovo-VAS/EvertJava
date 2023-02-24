package inovo.web.servlet;

import java.io.InputStream;

public class ContentInputProcessor {

	private Request _request=null;
	
	public ContentInputProcessor(Request request){
		this._request=request;
	}
	
	public Request request(){
		return this._request;
	}
	
	public void processInputStream(InputStream stream,int bufferSize) throws Exception{
		int inputBytesIndex=0;
		int inputRead=0;
		byte[] inputBytes=new byte[bufferSize];
		while((inputRead=stream.read(inputBytes, 0, bufferSize))>-1){
			inputBytesIndex=0;
			while(inputBytesIndex<inputRead){
				processInputByte(inputBytes[inputBytesIndex++], this.isDone());
			}
		}
	}
	
	public boolean isDone(){
		return false;
	}
	
	public void processInputBytes(byte[]bytes) throws Exception{
		if(bytes!=null){
			if(bytes.length>0){
				for(byte b:bytes){
					processInputByte(b, this.isDone());
				}
			}
		}
	}
	
	public void processInputByte(byte b,boolean done) throws Exception{		
	}
	
	public void cleanupContentProcessor(){
		this._request=null;
	}
}
