package inovo.web.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Enumeration;

public class ServletRequestHandler extends Request {
	
	@Override
	public String responseContentType(String resource_url) {
		if(resource_url.endsWith(".js")){
			return "text/javascript";
		}
		else if(resource_url.endsWith(".htm")||resource_url.endsWith(".html")){
			return "text/html";
		}
		else if(resource_url.endsWith(".xml")){
			return "text/xml";
		}
		else if(resource_url.endsWith(".svg")){
			return "text/xml";
		}
		else{
			return "text/html";
		}
	}
	
	@Override
	public void parseResourceUrlParameters(String resource_url_params) throws Exception{
		if((resource_url_params=resource_url_params==null?"":resource_url_params.trim()).equals("")) return;
		resource_url_params+="&";
		String[]urlparams=resource_url_params.split("[&]");
		for(String urlParam:urlparams){
			if(urlParam.indexOf("=")>-1){
				String paramName=urlParam.substring(0,urlParam.indexOf("=")).trim();
				String paramValue=urlParam.substring(urlParam.indexOf("=")+1).trim();
				if(paramName.equals("")) continue;
				this.setParameter(URLDecoder.decode(paramName,"UTF-8"),URLDecoder.decode(paramValue,"UTF-8") , false);
			}
		}
	}
	
	@Override
	public String parseResourceUrl(String resource_url) {
		if(resource_url.startsWith("/")) resource_url=resource_url.substring(1,resource_url.length());
		String parsedResourceUrl="";
		String[] resources_url=resource_url.split("[/]");
		int resources_url_index=0;
		while(resources_url_index<resources_url.length){
			String sub_resoure_url=resources_url[resources_url_index++];
			if(resources_url_index==1){
				parsedResourceUrl+=this.formatResourcePathSection(sub_resoure_url).replaceAll("[.]", "/");
			}
			else{
				parsedResourceUrl+=sub_resoure_url;
			}
			if(resources_url_index<resources_url.length){
				parsedResourceUrl+="/";
			}
		}
		
		return super.parseResourceUrl(parsedResourceUrl);
	}
	
	@Override
	public void finallizeResponseHeaders() {
		this.responseHeaders().put("CONNECTION", "Close");
		this.responseHeaders().put("CONTENT-LENGTH", String.valueOf(this.responseContentLength()));
	}
	
	@Override
	public void outputResponseHeaders(OutputStream outputStream)
			throws Exception {
		
		super.outputResponseHeaders(outputStream);
	}

	private String formatResourcePathSection(String sub_resoure_url) {
		String formatedResourcePathSection="";
		char[]sub_resoure_urlchars=sub_resoure_url.toCharArray();
		int sub_resoure_urlcharsindex=0;
		while(sub_resoure_urlcharsindex<sub_resoure_urlchars.length){
			char cs=sub_resoure_urlchars[sub_resoure_urlcharsindex++];
			if(sub_resoure_urlcharsindex==1){
				formatedResourcePathSection+=(cs+"").toLowerCase();
			}
			else{
				if(("ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(cs)>-1)){
					formatedResourcePathSection+=".";
				}
				formatedResourcePathSection+=(cs+"").toLowerCase();
			}
		}
		return formatedResourcePathSection;
	}
	
	public void doRequest(Object request,Object response) throws Exception{
		this.setCommand((String)invokeMethod("getMethod", request));
		String queryString=(String)invokeMethod("getQueryString", request);
		this.setResource_Url((String)invokeMethod("getRequestURI", request)+(queryString==null?"":("?"+queryString)));
		this.setProtocol((String)invokeMethod("getProtocol", request));
		
		Enumeration<String> requestHeaders=(Enumeration<String>)invokeMethod("getHeaderNames",request);
		while(requestHeaders.hasMoreElements()){
			String reqHeaderName=requestHeaders.nextElement();
			String reqHeaderValue=(String)invokeMethod("getHeader",request,reqHeaderName);
			this.requestHeaders().put(reqHeaderName.toUpperCase(), reqHeaderValue);
		}
		
		if(this.requestHeaders().containsKey("CONTENT-TYPE")){
			if(this.requestHeaders().get("CONTENT-TYPE").indexOf("multipart/form-data")>-1){
				_contentInputProcessor=new MultipartContentInputStream(this);
			}
		}
		
		if(_contentInputProcessor!=null){
			_contentInputProcessor.processInputStream(new BufferedInputStream((InputStream)invokeMethod("getInputStream", request)), 8192);
			_contentInputProcessor.cleanupContentProcessor();
			_contentInputProcessor=null;
		}
		else{
			this.inputRequest((InputStream)invokeMethod("getInputStream", request));
		}
		
		try{
			this.executeRequest();
		}
		catch(Exception e){
			this.log("executerequest-failure", this, e);
		}
		
		this.flushResponseContent();
		
		this.finallizeResponseHeaders();
		
		invokeMethod("setStatus", response,200);
		
		while(!this.responseHeaders().isEmpty()){
			String responseHeader=this.responseHeaders().keySet().iterator().next();
			invokeMethod("setHeader",response, responseHeader,responseHeaders().remove(responseHeader));
		}
		
		this.outputResponse(new BufferedOutputStream((OutputStream)invokeMethod("getOutputStream", response)));
		
	}
	
	private ContentInputProcessor _contentInputProcessor=null;
	
	@Override
	public void readInputByte(byte b) {
	}
	
	@Override
	public void cleanupRequest() {
		super.cleanupRequest();
		if(_contentInputProcessor!=null){
			_contentInputProcessor.cleanupContentProcessor();
			_contentInputProcessor=null;
		}
	}
}
