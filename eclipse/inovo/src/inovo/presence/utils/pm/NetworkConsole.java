package inovo.presence.utils.pm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkConsole {
	
	private String _presenceServerIP="";
	private Socket _socket=null;
	
	public NetworkConsole(String presenceServerIP) throws Exception{
		this._presenceServerIP=presenceServerIP;
		this.connectConsole();
	}
	
	public void connectConsole() throws Exception{
		this._socket=new Socket((this._presenceServerIP.indexOf(":")>-1?this._presenceServerIP.substring(0,this._presenceServerIP.indexOf(":")):this._presenceServerIP),(this._presenceServerIP.indexOf(":")>-1?Integer.parseInt(this._presenceServerIP.substring(this._presenceServerIP.indexOf(":")+1)):6800));
	}

	public void executeRequest(String args[],OutputStream consolOutput) throws IOException{
		//"c:list\n1:services\n\n"
		String pmcommandtosend="";
		int pmcmdargcount=0;
		for(String arg:args){
			if(pmcmdargcount==0){
				pmcommandtosend+="c:"+arg+"\n";
			}
			else{
				pmcommandtosend+=String.valueOf(pmcmdargcount)+":"+arg+"\n";
			}
			pmcmdargcount++;
		}
		if(!pmcommandtosend.equals("")){
			pmcommandtosend+="\n";
			ByteArrayInputStream bytesInputArg=new ByteArrayInputStream(pmcommandtosend.getBytes());
			this.executeRequest(bytesInputArg, consolOutput);
		}
	}
	
	private void executeRequest(InputStream argsInput,OutputStream consolOutput) throws IOException{
		OutputStream socketOutput= _socket.getOutputStream();
		byte[] bytesRead=new byte[8912];
		int bytesReadSize=0;
		while((bytesReadSize=argsInput.read(bytesRead,0,8912))>-1){
			if(bytesReadSize==0){
				continue;
			}
			socketOutput.write(bytesRead,0,bytesReadSize);
		}
		InputStream socketInput=_socket.getInputStream();
		int newLineByteCount=0;
		
		byte[] bytesActuallyRead=null;
		while((bytesReadSize=socketInput.read(bytesRead,0,8912))>-1){
			if(bytesReadSize==0){
				continue;
			}
			
			bytesActuallyRead=new byte[bytesReadSize];
			System.arraycopy(bytesRead, 0, bytesActuallyRead, 0, bytesReadSize);
			for(byte ba:bytesActuallyRead){
				if(ba==10){
					newLineByteCount++;
				}
				else if(ba!=13){
					newLineByteCount=0;
				}
				if(newLineByteCount==2){
					break;
				}
			}
			bytesActuallyRead=null;
			
			consolOutput.write(bytesRead,0,bytesReadSize);
			
			if(newLineByteCount==2){
				break;
			}
		}
	}
	
	public void cleanUpConsole() throws Exception{
		try{
			if(this._socket!=null){
				this._socket.close();
			}
		}
		catch(Exception ex){
			this._socket=null;
			throw ex;
		}
		this._socket=null;
	}
}
