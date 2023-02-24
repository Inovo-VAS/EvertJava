package inovo.web.sendreceive;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

public class RemoteSendReceive {

	public static void send(String command,String url,StringBuilder sendIn,final StringBuilder responseOut,String...sendHeaders) throws Exception{
		send(command, url, new ByteArrayInputStream(sendIn.substring(0,sendIn.length()).getBytes()), new OutputStream() {
			
			byte[] _bo=new byte[1];
			@Override
			public void write(int b) throws IOException {
				_bo[0]=(byte)b;
				this.write(_bo);
			}
			
			@Override
			public void write(byte[] b) throws IOException {
				this.write(b,0,b.length);
			}
			
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				while(len>0){
					if(off<b.length){
						responseOut.append((char)b[off++]);
					}
					else{
						break;
					}
				}
			}
			
		}, sendHeaders);
	}
	
	public static void send(String command,String url,InputStream sendIn,OutputStream responseOut,String...sendHeaders) throws Exception{
		HashMap<String,String> headers=new HashMap<String, String>();
		if(sendHeaders!=null){
			if(sendHeaders.length>0){
				for(String header:sendHeaders){
					if(header.indexOf(":")==-1) continue;
					if(header.substring(0, header.indexOf(":")).trim().equals("")) continue;
					if(header.substring(header.indexOf(":")+1,header.length()).trim().equals("")) continue;
					headers.put(header.substring(0, header.indexOf(":")).trim().toUpperCase(), header.substring(header.indexOf(":")+1,header.length()).trim());
					
				}
			}
			sendHeaders=null;
		}
		send(command, url, headers, sendIn, responseOut);
	}
	
	public static void send(String command,String url,HashMap<String,String> sendHeaders,InputStream sendIn,OutputStream responseOut){
		URL oURL;
        String returnData = "";
        HttpURLConnection con = null;
        try {
        	
        	long contentLength=0;
            ArrayList<byte[]> responseBuffer=new ArrayList<byte[]>();
            byte[] readBytes=new byte[8192];
            int read=0;
            if(sendIn!=null){
            	while((read=sendIn.read(readBytes))>-1){
            		if(read>0){
            			byte[] bread=new byte[read];
            			System.arraycopy(readBytes, 0, bread, 0, read);
            			responseBuffer.add(bread);
            			bread=null;
            			contentLength+=read;
            		}
            	}
            }
            oURL = new URL(url);
            con = (HttpURLConnection) oURL.openConnection();
            con.setRequestMethod(command);
            for(String header:sendHeaders.keySet()){
            	con.setRequestProperty(header, sendHeaders.get(header));
            }
            con.setRequestProperty("CONTENT-LENGTH", String.valueOf(contentLength));
            
            //con.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
            con.setDoOutput(true);
            con.setDoInput(true);
            //con.setRequestProperty("SOAPAction", "");

            OutputStream conOut=con.getOutputStream();
            
            while(!responseBuffer.isEmpty()){
            	conOut.write(responseBuffer.remove(0));
            }
            
            String encoding = con.getHeaderField("Content-Encoding");
            InputStream resStream=null;
            if(encoding != null && encoding.equals("gzip")){
                resStream = new GZIPInputStream(con.getInputStream());
            }
            else {
                if(con.getResponseCode() == 200)
                    resStream = con.getInputStream();
                else
                	resStream = con.getErrorStream();
                
            }   
            
            read=0;
            if(resStream!=null){
            	while((read=resStream.read(readBytes))>-1){
            		if(read>0){
            			byte[] bread=new byte[read];
            			System.arraycopy(readBytes, 0, bread, 0, read);
            			responseBuffer.add(bread);
            			bread=null;
            			contentLength+=read;
            		}
            	}
            }
            while(!responseBuffer.isEmpty()){
            	responseOut.write(responseBuffer.remove(0));
            }
        }
        catch(Exception e){
        	
        }
	}
}
