package inovo.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClient {
	
	HttpURLConnection connection;
	
	public HttpClient(String host) throws Exception{
		this.connection=(HttpURLConnection) new URL(host).openConnection(); 
	}
	
	public void send(HashMap<String, String> reqheaders,HashMap<String,List<String>> params,HashMap<String, List<String>> responseHeaders,StringBuilder responseStringBuilder) throws ProtocolException{
		String multipartBoundary="";
		
		if(!params.isEmpty()){
			if(!this.connection.getDoOutput()){
				this.connection.setDoOutput(true);
			}
			multipartBoundary="--"+(this.toString()+Calendar.getInstance().getTimeInMillis());
			if(multipartBoundary.length()>72){
				multipartBoundary=multipartBoundary.substring(0, 72);
			}
			this.connection.setRequestMethod("POST");
			reqheaders.put("CONTENT-TYPE", "multipart/form-data; boundary="+multipartBoundary);
		}
		
		if(!reqheaders.isEmpty()){
			for(String header:reqheaders.keySet()){
				this.connection.setRequestProperty(header.toUpperCase(), reqheaders.get(header));
			}
		}
		if(!params.isEmpty()){
			if(this.connection.getDoOutput()){
				try (PrintWriter writer=new PrintWriter(this.connection.getOutputStream(),true)){
					int paramCount=params.keySet().size();
					for(String name:params.keySet()){
						paramCount--;
						for(String pvalue:params.get(name)){
							writer.println("--" + multipartBoundary);
							writer.println("Content-Disposition: form-data; name=\""+name+"\"");
							writer.println("Content-Type: text/plain; charset=UTF-8");
							writer.println();
							writer.print(pvalue);
							writer.println();
						}
						if(paramCount==0){
							writer.println("--" + multipartBoundary+"--");
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(!this.connection.getDoInput()){
			this.connection.setDoInput(true);
		}
		if(this.connection.getDoInput()){
			try {
				if(this.connection.getResponseCode()==200){
					if(responseHeaders==null){
						responseHeaders=new HashMap<String, List<String>>();
					}
					
					Map<String,List<String>> respHeaders=this.connection.getHeaderFields();
					for(String header:respHeaders.keySet()){
						responseHeaders.put(header==null?"RESPONSE-STATUS":header.toUpperCase(), respHeaders.get(header));
					}
				}
				InputStream in=this.connection.getInputStream();
				
				int rl=0;
				byte[] rb=new byte[4096];
				while((rl=in.read(rb))>0){
					responseStringBuilder.append(new String(rb,0,rl));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	public static HashMap<String,String> settings(String unparsedSettings,String valSep,String settingSep){
		HashMap<String,String> mappedSettings=new HashMap<String, String>();
		while(!unparsedSettings.equals("")){
			
		}
		return mappedSettings;
	}
	
	public static HashMap<String,List<String>> parameters(String unparsedParameters,String valSep,String paramSep){
		HashMap<String,List<String>> parameters=new HashMap<String, List<String>>();
		String unparsedParam="";
		String pname="";
		String pval="";
		while(!unparsedParameters.equals("")){
			if(unparsedParameters.indexOf(paramSep)>-1){
				unparsedParam=unparsedParameters.substring(0, unparsedParameters.indexOf(paramSep)).trim();
				unparsedParameters=unparsedParameters.substring(unparsedParameters.indexOf(paramSep)+paramSep.length(), unparsedParameters.length());
			} else{
				unparsedParam=unparsedParameters.trim();
				unparsedParameters="";
			}
			if(!unparsedParam.equals("")&&unparsedParam.indexOf("=")>0){
				pname=unparsedParam.substring(0, unparsedParam.indexOf("="));
				unparsedParam=unparsedParam.substring(unparsedParam.indexOf("=")+1, unparsedParam.length()).trim();
				ArrayList<String> pvalues=new ArrayList<String>(); 
				while(!unparsedParam.equals("")){
					if(unparsedParam.indexOf(valSep)>-1){
						pvalues.add(unparsedParam.substring(0, unparsedParam.indexOf(valSep)).trim());
						unparsedParam=unparsedParam.substring(unparsedParam.indexOf(valSep)+valSep.length(), unparsedParam.length()).trim();
					} else{
						pvalues.add(unparsedParam.trim());
						unparsedParam="";
					}
				}
				parameters.put(pname, pvalues);
			}
		}
		return parameters;
	}
}
