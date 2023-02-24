package inovo.web.servlet;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Request {
	
	private static final ScriptEngineManager _scriptEngineManager=new ScriptEngineManager();

	private ArrayList<Request> _siblingRequests=new ArrayList<Request>();
	private Request _ownerRequest=null;
	private Request _topRequest=null;
	
	private HashMap<String,String> _requestHeaders=new HashMap<String, String>();
	private HashMap<String,String> _responseHeaders=new HashMap<String,String>();
	
	private ScriptEngine _scriptEngine=null;
	
	public Request(Request ownerRequest){
		this._ownerRequest=ownerRequest;
		this._topRequest=(this._ownerRequest==null?this:this._ownerRequest._topRequest);
		if(this._topRequest==this){
			_responseContent=new ArrayList<byte[]>();
		}
		else{
			_responseContent=this._topRequest._responseContent;
		}
		/*synchronized (_scriptEngineManager) {
			_scriptEngine=_scriptEngineManager.getEngineByName("js");
		}
		
		_scriptEngine.put("request", this);
		*/
	}
	
	public void log(String message,Request request,Exception e){
		if(request._topRequest==request){
			request.log(message, request,e);
		}
		else{
			request._topRequest.log(message, request,e);
		}
	}
	
	public Request(){
		this(null);
	}
	
	public Request ownerRequest(){
		return this._ownerRequest;
	}
	
	public Request topRequest(){
		return this._topRequest;
	}
	
	private String _command="";
	public String command(){
		return this._command;
	}
	
	public void setCommand(String command){
		this._command=command;
	}
	
	private String _resource_url="";
	public String resource_url(){
		return this._resource_url;
	}
	
	private String _protocol="";
	public String protocol(){
		return this._protocol;
	}
	
	public void setProtocol(String protocol){
		this._protocol=protocol;
	}
	
	public void setResource_Url(String resource_url) throws Exception{
		if(resource_url.indexOf("?")>-1){
			this.parseResourceUrlParameters(resource_url.substring(resource_url.indexOf("?")+1,resource_url.length()));
			resource_url=resource_url.substring(0,resource_url.indexOf("?"));
		}
		this._resource_url=this.parseResourceUrl(resource_url);
		this._responseHeaders.put("CONTENT-TYPE", this.responseContentType(this._resource_url));
	}
	
	private HashMap<String,ArrayList<String>> _parameters=new HashMap<String,ArrayList<String>>();
	
	private HashMap<String,File> _parametersFiles=new HashMap<String, File>();
	
	public void setFileParameter(String name) throws Exception{
		if((name=name==null?"":name.trim().toUpperCase()).equals("")) return;
		if(!_parametersFiles.containsKey(name)){
			_parametersFiles.put(name, File.createTempFile("ses", ".ses"));
		}
	}
	
	public File fileParameter(String name){
		if((name=name==null?"":name.trim().toUpperCase()).equals("")) return null;
		if(_parametersFiles.containsKey(name)){
			return _parametersFiles.get(name);
		}
		return null;
	}
	
	public void setParameter(String name,String value){
		this.setParameter(name, value, true);
	}
	
	public void setParameter(String name,String value,boolean resetParameter){
		if((name=name==null?"":name.trim().toUpperCase()).equals("")) return;
		if(this._topRequest==this){
			ArrayList<String> paramValues=this.parameter(name);
			if(paramValues==null){
				this._parameters.put(name, paramValues=new ArrayList<String>());
			}
			if(resetParameter) paramValues.clear();
			paramValues.add(value=value==null?"":value.trim());
		}
		else{
			this._topRequest.setParameter(name, value, resetParameter);
		}
		
	}
	
	public Object[] castArrayListToArray(ArrayList listToCast){
		Object[] castedArray=(Object[]) Array.newInstance(Object.class, listToCast.size());
		System.arraycopy(listToCast.toArray(), 0, castedArray, 0, castedArray.length);
		return castedArray;
	}
	
	public ArrayList<String> testValues(){
		ArrayList<String> testValues=new ArrayList<String>();
		testValues.add("ONE");
		testValues.add("TWO"); 
		return testValues;
	}
	
	public ArrayList<String> parameter(String name){
		if(this._topRequest==this){
			if((name=name==null?"":name.trim().toUpperCase()).equals("")) return null;
			return this._parameters.get(name);
		}
		else{
			return this._topRequest.parameter(name);
		}
	}
	
	public String parameterString(String name){
		return parameterString(name,"");
	}
	
	public String parameterString(String name,String sep){
		if((name=name==null?"":name.trim().toUpperCase()).equals("")) return "";
		if(this._parameters.containsKey(name)){
			int index=0;
			String parameterString="";
			ArrayList<String> paramValues=this._parameters.get(name);
			for(String paramValue:paramValues){
				parameterString+=paramValue;
				index++;
				if(index<paramValues.size()){
					parameterString+=sep;
				}
			}
			return parameterString;
		}
		return "";
	}
	
	public void parseResourceUrlParameters(String resource_url_params) throws Exception{
	}

	public String responseContentType(String resource_url) {
		String extension=resource_url.indexOf(".")==-1?"":resource_url.substring(resource_url.lastIndexOf(".")+1,resource_url.length());
		if(extension.equals("html")){
			return "text/html";
		}
		else if(extension.equals("js")){
			return "text/javascript";
		}
		else if(extension.equals("css")){
			return "text/CSS";
		}
		else if(extension.equals("xml")){
			return "text/xml";
		}
		else if(extension.equals("svg")){
			return "text/xml";
		}
		else if(extension.equals("png")){
			return "image/PNG";
		}
		else if(extension.equals("gif")){
			return "image/GIF";
		}
		else if(extension.equals("jpg")){
			return "image/JPEG";
		}
		return "text/plain";
	}

	public String parseResourceUrl(String resource_url) {
		return resource_url;
	}
	
	public static Object invokeMethod(String method,Object methodOwner,Object...parameters) throws Exception{
		if((method=method==null?"":method.trim().toLowerCase()).equals("")) return null;
		
		Method methodFound=null;
		int possibleParameterCount=0;
		int parameterCount=(parameters==null?0:parameters.length);
		int validParamCount=0;
		Class<?>[] possibleParamTypes=null;
		for(Method possibleMethod:methodOwner.getClass().getMethods()){
			if(possibleMethod.getName().toLowerCase().equals(method.toLowerCase())){
				if((possibleParameterCount=(possibleParamTypes=possibleMethod.getParameterTypes())==null?0:possibleParamTypes.length)==parameterCount){
					validParamCount=0;
					while(possibleParameterCount>0){
						possibleParameterCount--;
						if(isInheritedFrom(parameters[possibleParameterCount].getClass(),possibleParamTypes[possibleParameterCount], Object.class)){
							validParamCount++;
						}
					}
					if(validParamCount==parameterCount){
						methodFound=possibleMethod;
						break;
					}
				}
			}
		}
		return methodFound==null?null:methodFound.invoke(methodOwner, parameters);
	}
	
	public static boolean isInheritedFrom(Class<?> testClass,Class<?> inheritesFrom,Class<?> baseEnd){
		if(testClass==inheritesFrom) return true;
		if(testClass==baseEnd) return false;
		while((testClass=testClass.getSuperclass())!=baseEnd){
			if(testClass==inheritesFrom){
				return true;
			}
		}
		return false;
	}

	public void executeRequest() throws Exception{
		boolean processingEmbeddedResource=false;
		InputStream resourceInput=this.getClass().getResourceAsStream("/"+((processingEmbeddedResource=this.parameterString("embedded-resource").equals(""))?_resource_url:this.parameterString("embedded-resource")));
		
		if(processingEmbeddedResource){
			this._responseHeaders.put("CONTENT-TYPE", this.responseContentType(this.parameterString("embedded-resource")));
		}
		
		if(resourceInput!=null){
			
			this.respondStream(resourceInput);
		}
		else{
			Class<?> requestClass=null;
			try{
				requestClass=this.getClass().getClassLoader().loadClass(_resource_url.replaceAll("[////]", "."));
			}
			catch(Exception e){
				
			}
			if(requestClass!=null){
				Request request=(Request)requestClass.getConstructor(Request.class).newInstance(this);
				if(this._parameters.isEmpty()&&this._parametersFiles.isEmpty()){
					String requestTemplatePath=request.getClass().getName().replaceAll("[.]", "/");
					requestTemplatePath=requestTemplatePath.substring(0,requestTemplatePath.lastIndexOf("/")+1)+"html/"+requestTemplatePath.substring(requestTemplatePath.lastIndexOf("/")+1,requestTemplatePath.length())+".html";
					if((resourceInput=this.getClass().getResourceAsStream("/"+requestTemplatePath))!=null){
						synchronized (_scriptEngineManager) {
							request._scriptEngine=_scriptEngineManager.getEngineByName("js");
						}
						while(isInheritedFrom(requestClass, Request.class, Object.class)){
							request._scriptEngine.put(requestClass.getSimpleName(), request);
							requestClass=requestClass.getSuperclass();
						}
						request.respondStream(resourceInput);
					}
					else{
						request.processRequest(request);
					}
				}
				else{
					ArrayList<String> commands=this.parameter("COMMAND")==null?new ArrayList<String>():new ArrayList<String>(this.parameter("COMMAND"));
					if(commands.isEmpty()){
						request.processRequest(request);
					}
					else{
						int commandsIndex=0;
						ArrayList<Method> commandsFound=new ArrayList<Method>();
						for(Method reqMethod:requestClass.getMethods()){
							if(commands.isEmpty()){
								break;
							}
							else{
								commandsIndex=commands.size();
								while(commandsIndex>0){
									commandsIndex--;
									if(reqMethod.getName().toLowerCase().equals(commands.get(commandsIndex).toLowerCase())){
										commandsFound.add(reqMethod);
										commands.remove(commandsIndex);
									}
								}
							}
						}
						
						while(!commandsFound.isEmpty()){
							commandsFound.remove(0).invoke(request, null);
						}
					}
				}
			}
		}
	}
	
	public void processRequest(Request request) throws Exception{
		
	}
	
	public void respondStream(InputStream stream) throws Exception{
		byte [] streamBytes=new byte[8192];
		int streamBytesIndex=0;
		int streamRead=0;
		while((streamRead=stream.read(streamBytes,0,8192))>-1){
			streamBytesIndex=0;
			while(streamBytesIndex<streamRead){
				this.respondByte(streamBytes[streamBytesIndex++]);
			}
		}
	}

	public HashMap<String,String> requestHeaders(){
		return this._requestHeaders;
	}
	
	public HashMap<String,String> responseHeaders(){
		return this._responseHeaders;
	}
	
	private long _responseContentLength=0;
	
	public long responseContentLength(){
		return _responseContentLength;
	}
	
	public void resetRequest(){
		this._requestHeaders.clear();
		this._responseHeaders.clear();
		while(!_parameters.isEmpty()){
			_parameters.remove(_parameters.keySet().iterator().next()).clear();
		}
		
		this._command="";
		this._resource_url="";
		this._protocol="";
		while(!_siblingRequests.isEmpty()){
			_siblingRequests.get(0).cleanupRequest();
		}
		if(this==this._topRequest){
			while(!_responseContent.isEmpty()){
				_responseContent.remove(0);
			}
		}
		else{
			_responseContent=null;
		}
		_responseContentLength=0;
		if(this._responseContentBytes!=null){
			this._responseContentBytes=null;
		}
		this._responseContentBytesIndex=0;
		
		while(!_parametersFiles.isEmpty()){
			String parameter=_parametersFiles.keySet().iterator().next();
			try{
				_parametersFiles.remove(parameter).delete();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void cleanupRequest(){
		this.resetRequest();
		this._scriptEngine=null;
		this._requestHeaders=null;
		this._responseHeaders=null;
		this._parameters=null;
		this._siblingRequests=null;
		if(this._ownerRequest!=null){
			this._ownerRequest._siblingRequests.remove(this);
		}
		this._ownerRequest=null;
		this._topRequest=null;
		System.gc();
	}
	
	private ArrayList<byte[]> _responseContent=null;
	private byte[] _responseContentBytes=null;
	private int _responseContentBytesIndex=0;
	
	public void flushResponseContent(){
		if(this._topRequest==this){
			if(this._responseContentBytesIndex>0){
				byte[] responseBytes=new byte[this._responseContentBytesIndex];
				System.arraycopy(this._responseContentBytes, 0, responseBytes, 0, _responseContentBytesIndex);
				_responseContentBytesIndex=0;
				this._responseContent.add(responseBytes);
				responseBytes=null;
			}
		}
	}
	
	public void finallizeResponseHeaders(){
	}
	
	public void respondString(String string) throws Exception{
		this.respondBytes(string.getBytes());
	}
	
	public void respondBytes(byte[]bytes) throws Exception{
		if(bytes!=null){
			if(bytes.length>0){
				if(this._topRequest==this){
					for(byte b:bytes){
						this.respondByte(b);
					}
					
				}
				else{
					this._topRequest.respondBytes(bytes);
				}
			}
		}
	}
	
	public void respondByte(byte b) throws Exception{
		if(this._topRequest==this){
			if(this._responseContentBytes==null){
				this._responseContentBytes=new byte[8192];
				this._responseContentBytesIndex=0;
			}
			this._responseContentBytes[this._responseContentBytesIndex++]=b;
			if(this._responseContentBytes.length==this._responseContentBytesIndex){
				this.flushResponseContent();
				this._responseContentBytesIndex=0;
			}
			this._responseContentLength++;
		}
		else{
			this._topRequest.respondByte(b);
		}
	}
	
	public void readInputByte(byte b){
		
	}

	public void inputRequest(InputStream inputStream) throws Exception{
		if(this._topRequest==this){
			byte[] inputRequestBytes=new byte[8192];
			int inputRequestBytesIndex=0;
			int inputRequestRead=0;
			while((inputRequestRead=inputStream.read(inputRequestBytes, 0, inputRequestBytes.length))>-1){
				inputRequestBytesIndex=0;
				while(inputRequestBytesIndex<inputRequestRead){
					readInputByte(inputRequestBytes[inputRequestBytesIndex++]);
				}
			}
		}
		else{
			this._topRequest.inputRequest(inputStream);
		}
	}
	
	public void outputResponseStatus(OutputStream outputStream) throws Exception{
		if(this._topRequest==this){
			if(outputStream!=null){
				outputStream.write((this._protocol+" 200 Ok\r\n").getBytes());
				outputStream.flush();
			}
		}
	}
	
	public void outputResponseHeaders(OutputStream outputStream) throws Exception{
		if(this._topRequest==this){
			if(outputStream!=null){
				while(!this._responseHeaders.isEmpty()){
					String responseHeader=this._responseHeaders.keySet().iterator().next();
					outputStream.write((responseHeader+": "+this._responseHeaders.remove(responseHeader)+(this._responseHeaders.isEmpty()?"\r\n":"")+"\r\n").getBytes());
					outputStream.flush();
				}
				outputStream.flush();
			}
		}
	}
	
	public void outputResponse(OutputStream outputStream) throws Exception{
		if(this._topRequest==this){
			if(outputStream!=null){
				while(!this._responseContent.isEmpty()){
					outputStream.write(this._responseContent.remove(0));
				}
				outputStream.flush();
			}
		}
	}

	public void resetStringBuilder(StringBuilder stringBuilder) {
		if(stringBuilder.length()>0){
			stringBuilder.delete(0, stringBuilder.length());
		}
	}
	
	public String stringBuilderString(StringBuilder stringBuilder){
		return stringBuilder.length()==0?"":stringBuilder.substring(0,stringBuilder.length());
	}
	
}
