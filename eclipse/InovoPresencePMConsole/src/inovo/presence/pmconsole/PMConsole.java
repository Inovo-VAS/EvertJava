package inovo.presence.pmconsole;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class PMConsole extends InovoHTMLPageWidget {
	
	public PMConsole(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

	@Override
	public void pageContent() throws Exception {
		
		OutputStream out=new OutputStream() {
			
			byte[] pb=new byte[1];
			@Override
			public void write(int b) throws IOException {
				pb[0]=(byte)b;
				this.write(pb);
			}
			
			@Override
			public void write(byte[] b) throws IOException {
				this.write(b,0,b.length);
			}

			byte prevB=0;
			
			@Override
			public void write(byte[] b, int off, int len)
					throws IOException {
				try {
					int rectLen=0;
					
					
					String outString=new String(b,off,len);
					System.out.print(outString);
					respondString(outString);
				} catch (Exception e) {
					throw new IOException(e);
				}
			} 
		};
		this.startElement("pre", true);
		
		out.write("VALID COMMANDS:\r\n".getBytes());
		out.write("registerConsole\r\n".getBytes());
		out.write("pmConsoleApp\r\n".getBytes());
		
		out.write("\r\n".getBytes());
		
		PMConsoleServiceQueue.pmConsoleServiceQueue().ouputServiceQueue(out);
		
		this.endElement("pre", true);
	}
	
	public void registerConsole() throws Exception{
		HashMap<String, String> params=new HashMap<String, String>();
		this.importRequestParametersIntoMap(params, "SERVERIP,APPNAME,POOLSIZE,FORMAT");
		ArrayList<String> errors=new ArrayList<String>();
		if(params.get("SERVERIP").equals("")){
			errors.add("[NO SERVER IP]");
			errors.add("serverip=<serverip>");
		}
		if(params.get("APPNAME").equals("")){
			errors.add("[NO APP NAME]");
			errors.add("appname=<appname>");
		}
		if(params.get("POOLSIZE").equals("")){
			params.put("POOLSIZE", "1");
		}
		if(errors.isEmpty()){
			PMConsoleServiceQueue.pmConsoleServiceQueue().registerPMApp(params.get("SERVERIP"),params.get("APPNAME"),Integer.parseInt(params.get("POOLSIZE")));
			
			OutputStream out=new OutputStream() {
				
				byte[] pb=new byte[1];
				@Override
				public void write(int b) throws IOException {
					pb[0]=(byte)b;
					this.write(pb);
				}
				
				@Override
				public void write(byte[] b) throws IOException {
					this.write(b,0,b.length);
				}

				byte prevB=0;
				
				@Override
				public void write(byte[] b, int off, int len)
						throws IOException {
					try {
						int rectLen=0;
						
						
						String outString=new String(b,off,len);
						System.out.print(outString);
						respondString(outString);
					} catch (Exception e) {
						throw new IOException(e);
					}
				} 
			};
			
			//out.write("EXECUTE COMMAND\r\n".getBytes());
			try{
				PMConsoleServiceQueue.pmConsoleServiceQueue().pmCommand(params.get("APPNAME"),"HELP",out,params.get("FORMAT"));
				//out.write("\r\n".getBytes());
			}
			catch(Exception e){
				if(params.equals("json")){
					
				}
				else{
					out.write("ERROR:".getBytes());
					out.write((e.getMessage()+"\r\n").getBytes());
				}
			}
		}
		else{
			while(!errors.isEmpty()){
				this.respondString(errors.remove(0)+"\r\n");
			}
			this.setResponseHeader("CONTENT-TYPE", "text/plain");
		}
		//
	}
	
	public void pmConsoleApp() throws Exception{
		HashMap<String, String> params=new HashMap<String, String>();
		this.importRequestParametersIntoMap(params, "APPNAME,PMCOMMAND,FORMAT");
		
		ArrayList<String> errors=new ArrayList<String>();
		if(params.get("APPNAME").equals("")){
			errors.add("[NO APP NAME]");
			errors.add("appname=<appname>");
		}
		if(params.get("PMCOMMAND").equals("")){
			errors.add("[NO PM COMMAND]");
			errors.add("pmcommand=<pmcommand>");
		}
		if(errors.isEmpty()){
			
			
			
			OutputStream out=new OutputStream() {
				
				byte[] pb=new byte[1];
				@Override
				public void write(int b) throws IOException {
					pb[0]=(byte)b;
					this.write(pb);
				}
				
				@Override
				public void write(byte[] b) throws IOException {
					this.write(b,0,b.length);
				}

				byte prevB=0;
				
				@Override
				public void write(byte[] b, int off, int len)
						throws IOException {
					try {
						int rectLen=0;
						String outString=new String(b,off,len);
						respondString(outString);
					} catch (Exception e) {
						throw new IOException(e);
					}
				} 
			};
			
			//out.write("EXECUTE COMMAND\r\n".getBytes());
			
			ByteArrayOutputStream bout=new ByteArrayOutputStream();
			
			PMConsoleServiceQueue.pmConsoleServiceQueue().pmCommand(params.get("APPNAME"),params.get("PMCOMMAND"),bout,params.get("FORMAT"));
			//bout.write("\r\n".getBytes());	
			bout.flush();
			
			this.respond.print(bout.toString());
			this.setResponseHeader("CONTENT-TYPE",params.get("FORMAT").equals("")||params.get("FORMAT").equals("text")?"text/plain":params.get("FORMAT").equals("xml")?"text/xml":params.get("FORMAT").equals("json")?"application/json":"text/plain");
		}
		else{
			while(!errors.isEmpty()){
				this.respondString(errors.remove(0)+"\r\n");
			}
			this.setResponseHeader("CONTENT-TYPE",params.get("FORMAT").equals("")||params.get("FORMAT").equals("text")?"text/plain":params.get("FORMAT").equals("xml")?"text/xml":params.get("FORMAT").equals("json")?"application/json":"text/plain");
		}
	}
}
