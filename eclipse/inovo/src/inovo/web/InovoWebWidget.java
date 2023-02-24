package inovo.web;

import inovo.adhoc.AdhocUtils;
import inovo.db.Database;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

public class InovoWebWidget
{
  private InputStream _inStream;
  private InovoWebWidget _parentWidget;
  private HashMap<String, String> _requestHeaders;
  private HashMap<String, String> _responseHeaders;
  private HashMap<String, ArrayList<String>> _requestParameters;
  private HashMap<String, HashMap<String, String>> _requestParameterProperties;
  private HashMap<String,File> _requestParameterFiles;
  
  private int _responseStatus = 200;
  private String _responseStatusMessage = "Ok";

  private ArrayList<ByteArrayOutputStream> _bytesArrPreOutput = new ArrayList();
  private ByteArrayOutputStream _currentResponseByteArrPreOut = null;
  private int _currentResponseBytePreLength = 0;
  
  private ArrayList<ByteArrayOutputStream> _bytesArrPostOutput = new ArrayList();
  private ByteArrayOutputStream _currentResponseByteArrPostOut = null;
  private int _currentResponseBytePostLength = 0;
  
  private WebRequestContentType _webrequestcontenttype;
  int _avaliableBytes = 0;
  int _currentByteIndex = 0;
  int _maxBytesBufferSize = 1048576;
  byte[] _bytesBufferAvailable = new byte[_maxBytesBufferSize];

  private ArrayList<ByteArrayOutputStream> _bytesArrOutput = new ArrayList();
  private ByteArrayOutputStream _currentResponseByteArrOut = null;
  private int _currentResponseByteLength = 0;
  private int _responseByteMaxLength = 1024;
  private long _responseBytesRead = 0L;
  
  private PrintWriter _respondPrinter=new PrintWriter(new Writer() {
	private OutputStream outwr=new OutputStream() {
		
		private byte[] ob=new byte[1];
		@Override
		public void write(int b) throws IOException {
			if(b>=0&&b<256){
				this.ob[0]=(byte)b;
				this.write(ob,0,1);
			}
			else{
				this.write(((char)b+"").getBytes());
			}
		}
		
		public void write(byte[] b) throws IOException {
			this.write(b, 0, b==null?0:b.length);
		};
		
		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			try {
				respondBytes(b, off, len);
			} catch (Exception e) {
				throw new IOException(e);
			}
		}	
	  };
	  
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		if(cbuf!=null&&cbuf.length>0&&(len=(cbuf.length-off)>=len?len:(cbuf.length-off))>0){
			while(len>0){
				outwr.write(cbuf[off++]);
				len--;
			}
		}
	}
	
	@Override
	public void flush() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}
});
  
  public PrintWriter respond=this._respondPrinter;
  
  public InovoWebWidget(InputStream inStream)
  {
    this(null, null, null, inStream);
  }

  public InovoWebWidget(InovoWebWidget parentWidget, InputStream inStream) {
    this(parentWidget, null, null, (inStream==null?(parentWidget==null?inStream:parentWidget.inputStream()):inStream));
  }
  
  public InputStream inputStream(){
	  return _inStream;
  }

  private InovoWebWidget(InovoWebWidget parentWidget, HashMap<String, String> requestHeaders, HashMap<String, ArrayList<String>> requestParameters, InputStream inStream) {
    this._parentWidget = parentWidget;
    this._responseHeaders = new HashMap<String,String>();
    this._requestHeaders = (requestHeaders == null ? new HashMap<String,String>() : requestHeaders);
    this._requestParameters = (requestParameters == null ? new HashMap<String,ArrayList<String>>() : requestParameters);
    this._requestParameterProperties = new HashMap<String, HashMap<String, String>>();
    this._requestParameterFiles=new HashMap<String,File>();
    this._inStream = inStream;
  }

  public void setResponseStatus(int status, String statusMessage) {
    if (this._parentWidget == null) {
      this._responseStatus = status;
      this._responseStatusMessage = statusMessage;
    }
    else {
      this._parentWidget.setResponseStatus(status, statusMessage);
    }
  }

  public HashMap<String, String> responseHeaders() {
    if (this._parentWidget == null) {
      return this._responseHeaders;
    }
    return this._parentWidget.responseHeaders();
  }

  public void setRequestHeader(String headerName, String headerValue) {
    if (this._parentWidget == null) {
      headerName = headerName.toUpperCase();
      this._requestHeaders.put(headerName, headerValue);
    }
    else {
      this._parentWidget.setRequestHeader(headerName, headerValue);
    }
  }

  public String requestHeader(String headerName) {
    if (this._parentWidget == null) {
      headerName = headerName.toUpperCase();
      return this._requestHeaders.containsKey(headerName) ? (String)this._requestHeaders.get(headerName) : "";
    }
    return this._parentWidget.requestHeader(headerName);
  }

  public void setResponseHeader(String headerName, String headerValue) {
    if (this._parentWidget == null) {
      headerName = headerName.toUpperCase();
      this._responseHeaders.put(headerName, headerValue);
    }
    else {
      this._parentWidget.setResponseHeader(headerName, headerValue);
    }
  }

  public String responseHeader(String headerName) {
    if (this._parentWidget == null) {
      headerName = headerName.toUpperCase();
      return this._responseHeaders.containsKey(headerName) ? (String)this._requestHeaders.get(headerName) : "";
    }
    return this._parentWidget.responseHeader(headerName);
  }

  public InovoWebWidget topWidget() {
    return this._parentWidget == null ? this : this._parentWidget.topWidget();
  }

  public InovoWebWidget parentWidget() {
    return this._parentWidget;
  }

  private void processInputStream() throws Exception {
    if (this._parentWidget == null) {
      byte[] bytesinbuffer = new byte[1024];

      for (int bytesReadOut = this._inStream.read(bytesinbuffer, 0, 1024); bytesReadOut > -1; bytesReadOut = this._inStream.read(bytesinbuffer, 0, 1024))
        processInputBytes(bytesinbuffer);
    }
    else
    {
      this._parentWidget.processInputStream();
    }
  }

  private byte[] _actualBytesAvailableBuffer=null;
  protected int readAvailableContentBytes()
    throws Exception
  {
    if (this._parentWidget == null) {
      if (this._avaliableBytes == this._currentByteIndex) {
        this._avaliableBytes = 0;
      }
      while ((this._avaliableBytes = this._inStream.read(this._bytesBufferAvailable, 0, this._maxBytesBufferSize)) > -1) {
        if (this._avaliableBytes > 0)
        {
        	_actualBytesAvailableBuffer=null;
        	_actualBytesAvailableBuffer=new byte[this._avaliableBytes];
        	System.arraycopy(_bytesBufferAvailable, 0, _actualBytesAvailableBuffer, 0, _avaliableBytes);
          break;
        }
        Thread.sleep(2L);
      }

      this._currentByteIndex = 0;
      return this._avaliableBytes;
    }
    return this._parentWidget.readAvailableContentBytes();
  }

  public void processMultipartInputStream() throws Exception {
    if (this._parentWidget != null) {
      this._parentWidget.processMultipartInputStream();
      return;
    }
    
    while (this._avaliableBytes > -1) {
        if (this._avaliableBytes == this._currentByteIndex) {
          readAvailableContentBytes();
          this._currentByteIndex = 0;
        }
        if (this._avaliableBytes > 0) {
        	System.arraycopy(this._bytesBufferAvailable, 0, this._actualBytesAvailableBuffer, 0, _avaliableBytes);
        	for(byte mb:_actualBytesAvailableBuffer) this.processMultiPartByte(mb,(char)mb);
           	this._avaliableBytes = 0;
        }
      }
  }
  
  private enum MultiPartStage {
	  boundary,
	  header,
	  content,
	  none,
	  done
  }
  
  private StringBuilder _markupString=new StringBuilder();
  private MultiPartStage _multiPartStage=MultiPartStage.none;
  private byte[] _boundarybytes=null;
  private int _boundarybytesindex=0;
  private byte _prevBoundByte=0;
  private HashMap<String,String> _multiPartHeaders=new HashMap<String,String>();
  private byte[] _contentBufferBytes=new byte[1024*1024];
  private int _contentBufferBytesIndex=0;
  private OutputStream _mcontentStream=null;
  
  private void processMultiPartByte(byte mb,char mc) throws Exception{
	  switch(_multiPartStage){
	case boundary:
		switch (mc) {
		case 10:
			_multiPartStage=MultiPartStage.header;
			break;
		case '-':
			return;
		default:
			break;
		}
		break;
	case content:
		if (_boundarybytesindex>0&&_boundarybytes[_boundarybytesindex-1]==this._prevBoundByte&&_boundarybytes[_boundarybytesindex]!=mb){
			int boundarybytesindex=0;
			while(_boundarybytesindex>0){
				_contentBufferBytes[_contentBufferBytesIndex++]=_boundarybytes[boundarybytesindex++];
				_boundarybytesindex--;
				if(_contentBufferBytesIndex==_contentBufferBytes.length){
					_mcontentStream.write(_contentBufferBytes,0,_contentBufferBytesIndex);
					_contentBufferBytesIndex=0;
					_mcontentStream.flush();
				}
			}
			_prevBoundByte=0;
		}
		if(_boundarybytes[_boundarybytesindex]==mb){
			_boundarybytesindex++;
			if(_boundarybytesindex==_boundarybytes.length){
				_boundarybytesindex=0;
				if(_contentBufferBytesIndex>0){
					_mcontentStream.write(_contentBufferBytes,0,_contentBufferBytesIndex);
					_contentBufferBytesIndex=0;
					_mcontentStream.flush();
				}
				if(_multiPartHeaders.containsKey("FILENAME")){
			    	  _mcontentStream.flush();
			    	  _mcontentStream.close();  
			      }
			      if(!_multiPartHeaders.isEmpty()) captureMultipartField(_multiPartHeaders, _mcontentStream);
			      _multiPartHeaders.clear();
			      _mcontentStream=null;
			      _multiPartStage=MultiPartStage.boundary;
			      _prevBoundByte=0;
			} else {
				_prevBoundByte=mb;
			}
		}
		else{
			if(_boundarybytesindex>0){
				int boundarybytesindex=0;
				while(_boundarybytesindex>0){
					_contentBufferBytes[_contentBufferBytesIndex++]=_boundarybytes[boundarybytesindex++];
					_boundarybytesindex--;
					if(_contentBufferBytesIndex==_contentBufferBytes.length){
						_mcontentStream.write(_contentBufferBytes,0,_contentBufferBytesIndex);
						_contentBufferBytesIndex=0;
						_mcontentStream.flush();
					}
				}
			}
			_contentBufferBytes[_contentBufferBytesIndex++]=mb;
			if(_contentBufferBytesIndex==_contentBufferBytes.length){
				_mcontentStream.write(_contentBufferBytes,0,_contentBufferBytesIndex);
				_contentBufferBytesIndex=0;
				_mcontentStream.flush();
			}
			_prevBoundByte=mb;
		}
		break;
	case header:
		switch(mb){
		case 10:
			if(_markupString.length()>0){
				String multiPartHeaderLine=_markupString.toString().trim();
				if (multiPartHeaderLine.startsWith("Content-Disposition:")) {
			          multiPartHeaderLine = multiPartHeaderLine.substring("Content-Disposition:".length()).trim();
			          for (String multiPartHeaderItem : multiPartHeaderLine.split(";"))
			            if (!multiPartHeaderItem.trim().equals("form-data")) {
			              String mheaderName = multiPartHeaderItem.substring(0, multiPartHeaderItem.indexOf("=")).trim().toUpperCase();
			              String mheaderValue = multiPartHeaderItem.substring(multiPartHeaderItem.indexOf("=") + 1).trim();
			              while ((mheaderValue.startsWith("\"")) && (mheaderValue.endsWith("\""))) {
			                mheaderValue = mheaderValue.substring(1, mheaderValue.length() - 1);
			              }
			              _multiPartHeaders.put(mheaderName, mheaderName.equals("NAME") ? mheaderValue.toUpperCase() : mheaderValue);
			            }
			        }
			        else
			        {
			          String mheaderName = multiPartHeaderLine.substring(0, multiPartHeaderLine.indexOf(":")).trim().toUpperCase();
			          String mheaderValue = multiPartHeaderLine.substring(multiPartHeaderLine.indexOf(":") + 1).trim();
			          _multiPartHeaders.put(mheaderName, mheaderValue);
			        }
			}
			else{
				 _mcontentStream= getMultiPartContentStream(_multiPartHeaders);
				_multiPartStage=MultiPartStage.content;
			}
			_markupString.setLength(0);
			break;
		case 13:
			break;
		default:
			_markupString.append(mc);
			break;
		}
		break;
	case none:
		switch (mb){
		case 10:
			if(this._markupString.length()>0){
				_boundarybytes=("\r\n"+this._markupString.toString()).getBytes();
				_multiPartStage=MultiPartStage.header;
				_markupString.setLength(0);
			}
			break;
		case 13:
			break;
		default:
			_markupString.append(mc);
			break;
		}
		break;
	default:
		break;
	  }
  }

public void captureMultipartField(HashMap<String, String> multiPartHeaders, OutputStream mcontentStream)
  {
    if (this._parentWidget != null) {
      this._parentWidget.captureMultipartField(multiPartHeaders, mcontentStream);
      return;
    }
    
    HashMap reqparamProperties = new HashMap();
    reqparamProperties.putAll(multiPartHeaders);
    reqparamProperties.remove("NAME");
    reqparamProperties.keySet().remove("NAME");
    
    if (multiPartHeaders.containsKey("FILENAME")) {
      setRequestParameter((String)multiPartHeaders.get("NAME"), (String)multiPartHeaders.get("FILENAME"), true);
    }
    else {
      setRequestParameter((String)multiPartHeaders.get("NAME"), ((ByteArrayOutputStream)mcontentStream).toString(), false);
    }
    this._requestParameterProperties.put((String)multiPartHeaders.get("NAME"), reqparamProperties);
    //reqparamProperties.clear();
    reqparamProperties=null;
  }

  public OutputStream getMultiPartContentStream( HashMap<String, String> multiPartHeaders) throws IOException
  {
    if (this._parentWidget == null) {
    	if(multiPartHeaders.containsKey("FILENAME")){
    		if(!multiPartHeaders.get("FILENAME").equals("")){
    			File nextRequestFile=File.createTempFile("sep", "tmp");
    			this._requestParameterFiles.put(multiPartHeaders.get("NAME").toUpperCase(), nextRequestFile);
    			return new FileOutputStream(nextRequestFile);
    		}
    	}
    	
      return new ByteArrayOutputStream();
    }
    return this._parentWidget.getMultiPartContentStream(multiPartHeaders);
  }

  protected OutputStream readInputIntoStream(OutputStream outputstream, byte[] endphrasebytes) throws Exception
  {
    if (this._parentWidget != null) {
      return this._parentWidget.readInputIntoStream(outputstream, endphrasebytes);
    }
    byte[] currentReadBuffer = new byte[this._maxBytesBufferSize];
    int currentReadBufferindex = 0;
    boolean endPhrasefound = false;
    byte bc = 0;
    int endphraseindex = 0;
    int endphraselength = endphrasebytes.length;
    while (!endPhrasefound) {
      if ((this._avaliableBytes == this._currentByteIndex) || (this._avaliableBytes == -1)) {
        if (this._avaliableBytes == -1) {
          break;
        }
        if (readAvailableContentBytes() == -1) {
          break;
        }
      }
      if ((bc = this._bytesBufferAvailable[(this._currentByteIndex++)]) == endphrasebytes[endphraseindex]) {
        endphraseindex++;
        if ((endPhrasefound = (endphraseindex == endphraselength))) {
          if ((currentReadBufferindex > 0) && (outputstream != null)){
        	  outputstream.write(currentReadBuffer, 0, currentReadBufferindex);
          }
          currentReadBufferindex = 0;
        }
      }
      else
      {
        if (endphraseindex > 0) {
          for (int currentEndPhraseIndex = 0; currentEndPhraseIndex < endphraseindex; currentEndPhraseIndex++) {
            currentReadBuffer[(currentReadBufferindex++)] = endphrasebytes[currentEndPhraseIndex];
            if (currentReadBufferindex == this._maxBytesBufferSize) {
              if (outputstream != null) outputstream.write(currentReadBuffer, 0, currentReadBufferindex);
              currentReadBufferindex = 0;
            }
          }
          endphraseindex = 0;
        }
        currentReadBuffer[(currentReadBufferindex++)] = bc;
        if (currentReadBufferindex == this._maxBytesBufferSize) {
          if (outputstream != null) outputstream.write(currentReadBuffer, 0, currentReadBufferindex);
          currentReadBufferindex = 0;
        }
      }
    }

    if ((!endPhrasefound) && (currentReadBufferindex > 0)) {
      if (outputstream != null) outputstream.write(currentReadBuffer, 0, currentReadBufferindex);
      currentReadBufferindex = 0;
    }
    return outputstream;
  }

  public void processInputBytes(byte[] bytesinbuffer) {
    int bytesReadOut = bytesinbuffer.length;
    for (int byteindex = 0; byteindex < bytesReadOut; byteindex++)
      processInputByte(bytesinbuffer[byteindex]);
  }

  public void processInputByte(byte bin)
  {
  }

  public void executeWidget(boolean ignoreContentType) throws Exception
  {
    startExecutingWidget();
    if (this._requestParameters.containsKey("EMBEDDEDRESOURCE")) {
      executeEmbeddedResource((String)((ArrayList)this._requestParameters.get("EMBEDDEDRESOURCE")).get(0));
      return;
    }

    if (!requestHeader("CONTENT-LENGTH").equals("")) {
      if ((requestHeader("CONTENT-TYPE").startsWith("multipart/")) && (!ignoreContentType)) {
        processMultipartInputStream();
      }
      else if ((requestHeader("CONTENT-TYPE").contains("www-form-urlencoded")) && (!ignoreContentType)) {
        processwwwUrlEncodedInputStream();
      }
      else {
        processInputStream();
      }
    }
    InovoWebWidget currentWidget = this;
    String widget = requestParameter("WIDGET");
    try {
      currentWidget = widget.equals("") ? currentWidget : (InovoWebWidget)Class.forName(widget).getConstructor(new Class[] { InputStream.class }).newInstance(new Object[] { currentWidget, currentWidget._inStream });
    }
    catch (Exception e) {
      currentWidget = this;
    }

    if (currentWidget.requestParameter("COMMAND").equals("")) {
      currentWidget.executeContentWidget();
    }
    else {
      Method currentWidgetMethod = AdhocUtils.findMethod(currentWidget.getClass().getMethods(), requestParameter("COMMAND"), false);
      if (currentWidgetMethod != null) {
        currentWidgetMethod.invoke(currentWidget, null);
      }
    }

    stopExecutingWidget();
  }
  
  public void executeWidgetMethod(String method) throws Exception{
	  Method currentWidgetMethod = AdhocUtils.findMethod(this.getClass().getMethods(), method, false);
      if (currentWidgetMethod != null) {
        currentWidgetMethod.invoke(this, null);
      }
  }
  
  public void stopExecutingWidget() throws Exception
  {
  }

  public void executeContentWidget() throws Exception {
  }

  public void startExecutingWidget() throws Exception {
  }

  public void processwwwUrlEncodedInputStream() throws Exception {
    ByteArrayOutputStream bytesurlencoded = new ByteArrayOutputStream();
    while (this._avaliableBytes > -1) {
      if (this._avaliableBytes == this._currentByteIndex) {
        readAvailableContentBytes();
        this._currentByteIndex = 0;
      }
      if (this._avaliableBytes > 0) {
        bytesurlencoded.write(this._bytesBufferAvailable, 0, this._avaliableBytes);
        this._avaliableBytes = 0;
      }
    }
    parseUrlEncodedParams(bytesurlencoded.toString().toCharArray());
  }

  public void parseUrlEncodedParams(char[] urlencodedchars) throws Exception {
    String urlLineToParse = "";
    for (char cu : urlencodedchars)
      if (cu == '&') {
        if (urlLineToParse.length() > 0) {
          String paramName = URLDecoder.decode(urlLineToParse.substring(0, urlLineToParse.indexOf("=")), "UTF-8").trim();
          String paramValue = URLDecoder.decode(urlLineToParse.substring(urlLineToParse.indexOf("=") + 1), "UTF-8").trim();
          setRequestParameter(paramName, paramValue, false);
        }
        urlLineToParse = "";
      }
      else {
        urlLineToParse = urlLineToParse + cu;
      }
    if (urlLineToParse.length() > 0) {
    	if(urlLineToParse.indexOf("=")>-1){
	      String paramName = URLDecoder.decode(urlLineToParse.substring(0, urlLineToParse.indexOf("=")), "UTF-8").trim();
	      String paramValue = URLDecoder.decode(urlLineToParse.substring(urlLineToParse.indexOf("=") + 1), "UTF-8").trim();
	      setRequestParameter(paramName, paramValue, false);
    	}
      urlLineToParse = "";
    }
  }

  public void executeEmbeddedResource(String embeddedResource) throws Exception {
    String suggestedContentType = embeddedResource.lastIndexOf(".") > -1 ? embeddedResource.substring(embeddedResource.lastIndexOf(".")) : "";
    if (suggestedContentType.equals(".js")) suggestedContentType = "text/javascript";
    else if (suggestedContentType.equals(".css")) suggestedContentType = "text/css";
    else if ("PNG,GIF,JPG,".contains(suggestedContentType.toUpperCase())) suggestedContentType = "image/" + suggestedContentType.toUpperCase();
    if (embeddedResource.equals("jquery.js")) {
      setResponseHeader("CONTENT-TYPE", "text/javascript");
      respondBytes(AdhocUtils.inputSteamToBytesOutputStream(getClass().getResourceAsStream("/jquery/jquery.3.4.1.js")).toByteArray());
      respondString("\r\n");
      respondBytes(AdhocUtils.inputSteamToBytesOutputStream(getClass().getResourceAsStream("/jquery/jquery.form.js")).toByteArray());
    }
    else if (embeddedResource.equals("jquery.ui.js")) {
      setResponseHeader("CONTENT-TYPE", "text/javascript");
      //respondBytes(AdhocUtils.inputSteamToBytesOutputStream(getClass().getResourceAsStream("/jquery/jquery.ui.1.10.2.js")).toByteArray());
      respondBytes(AdhocUtils.inputSteamToBytesOutputStream(getClass().getResourceAsStream("/jquery/ui/jquery-ui.js")).toByteArray());
      respondString("\r\n");
      respondBytes(AdhocUtils.inputSteamToBytesOutputStream(getClass().getResourceAsStream("/jquery/jquery.blockui.js")).toByteArray());
    }
    else if (embeddedResource.equals("jquery.ui.css")) {
      setResponseHeader("CONTENT-TYPE", "text/CSS");
//      String urlimageref = requestHeader("URLREQUEST") + "?embeddedresource=/jquery/images/";
//      respondBytes(AdhocUtils.inputSteamToBytesOutputStream(getClass().getResourceAsStream("/jquery/jquery.ui.1.10.2.css")).toString().replaceAll("images/", urlimageref).getBytes());
      String urlimageref = requestHeader("URLREQUEST") + "?embeddedresource=/jquery/ui/images/";
      respondBytes(AdhocUtils.inputSteamToBytesOutputStream(getClass().getResourceAsStream("/jquery/ui/jquery-ui.css")).toString().replaceAll("images/", urlimageref).getBytes());
    }
    else if (embeddedResource.contains("/jquery/ui/images/")) {
        setResponseHeader("CONTENT-TYPE", "image/" + embeddedResource.substring(embeddedResource.lastIndexOf(".") + 1).toUpperCase());
        respondBytes(AdhocUtils.inputSteamToBytesOutputStream(getClass().getResourceAsStream(embeddedResource)).toByteArray());
      }
    else if (embeddedResource.contains("/jquery/images/")) {
      setResponseHeader("CONTENT-TYPE", "image/" + embeddedResource.substring(embeddedResource.lastIndexOf(".") + 1).toUpperCase());
      respondBytes(AdhocUtils.inputSteamToBytesOutputStream(getClass().getResourceAsStream(embeddedResource)).toByteArray());
    }
    else if (!embeddedResource.equals("")) {
      setResponseHeader("CONTENT-TYPE", suggestedContentType);
      respondBytes(AdhocUtils.inputSteamToBytesOutputStream(getClass().getResourceAsStream(alternateEmbeddedResource(embeddedResource))).toByteArray());
    }
  }

  public String alternateEmbeddedResource(String embeddedResource) {
    return embeddedResource;
  }

  public String responseStatusLine() {
    return (String)this._requestHeaders.get("PROTOCOL") + " " + String.valueOf(this._responseStatus) + " " + this._responseStatusMessage;
  }

  public void outputResponseHeaders(OutputStream out) throws Exception {
    if (out != null) {
      byte[] responsestatusbytes = (responseStatusLine() + "\r\n").getBytes();

      out.write(responsestatusbytes, 0, responsestatusbytes.length);

      while (!this._responseHeaders.isEmpty()) {
        String headerName = (String)this._responseHeaders.keySet().toArray()[0];
        String headerValue = (String)this._responseHeaders.remove(headerName);
        this._requestHeaders.keySet().remove(headerName);
        byte[] headerbytes = (headerName + ": " + headerValue + "\r\n").getBytes();
        out.write(headerbytes, 0, headerbytes.length);
        headerbytes = null;
      }
      out.write("\r\n".getBytes(), 0, 2);
    }
    out.flush();
  }

  public int responseStatus() {
    return this._responseStatus;
  }
  
  public void respondStream(InputStream input) throws Exception{
	  if(input!=null){
		  int readLen=0;
		  byte[] readBytes=new byte[8192];
		  int retryReadCount=0;
		  while((readLen=input.read(readBytes, 0, readBytes.length))>-1){
			  if(readLen>0){
				  retryReadCount=0;				  
				  this.respondBytes(readBytes, 0, readLen);
			  }
			  else if(retryReadCount<3){
				  Thread.currentThread().sleep(100);
				  retryReadCount++;
				  if(retryReadCount>=3) break;
			  }
		  }
	  }
  }
  
  public void respondBytes(byte[] bytes) throws Exception {
	 this.respondBytes(bytes, 0, bytes==null?0:bytes.length);
  }

  public void respondBytes(byte[] bytes,int off,int len)
    throws Exception
    {
    if(bytes==null||bytes.length==0||(len=(bytes.length-off)>=len?len:(bytes.length-off))==0) return;
    if(this._parentWidget!=null){
    	this._parentWidget.respondBytes(bytes);
    	return;
  	}
    if (this._currentResponseByteArrOut == null) {
      this._currentResponseByteArrOut = new ByteArrayOutputStream();
      this._bytesArrOutput.add(this._currentResponseByteArrOut);
    }
    int actualReadLen=0;
    while(len>0){
    	if (this._currentResponseByteLength++ >= this._responseByteMaxLength) {
    		this._currentResponseByteArrOut = new ByteArrayOutputStream();
    		this._bytesArrOutput.add(this._currentResponseByteArrOut);
    		this._currentResponseByteLength = 0;
    	}
    	this._currentResponseByteArrOut.write(bytes[off++]);
    	len--;
    	actualReadLen++;
    }
    this._responseBytesRead += actualReadLen;
  }
  
  public void appendString(String preAppendString,String postAppendString){
	  this.appendBytes(preAppendString==null?null:preAppendString.equals("")?null:preAppendString.getBytes(), postAppendString==null?null:postAppendString.equals("")?null:postAppendString.getBytes());
  }
  
  public void appendBytes(byte[] preAppendBytes,byte[] postAppendBytes){
	  if(this._parentWidget!=null){
	    	this._parentWidget.appendBytes(preAppendBytes,postAppendBytes);
	    	return;
	  	}
	    
	  
	  if(preAppendBytes!=null){
		  if(preAppendBytes.length>0){
			  if (this._currentResponseByteArrPreOut == null) {
			      this._currentResponseByteArrPreOut = new ByteArrayOutputStream();
			      this._bytesArrPreOutput.add(this._currentResponseByteArrPreOut);
			    }

			    for (byte b : preAppendBytes) {
			      if (this._currentResponseBytePreLength++ >= this._responseByteMaxLength) {
			        this._currentResponseByteArrPreOut = new ByteArrayOutputStream();
			        this._bytesArrPreOutput.add(this._currentResponseByteArrPreOut);
			        this._currentResponseByteLength = 0;
			      }
			      this._currentResponseByteArrPreOut.write(b);
			    }
			    this._responseBytesRead += preAppendBytes.length;
		  }
	  }
	  if(postAppendBytes!=null){
		  if(postAppendBytes.length>0){
			  if (this._currentResponseByteArrPostOut == null) {
			      this._currentResponseByteArrPostOut = new ByteArrayOutputStream();
			      this._bytesArrPostOutput.add(this._currentResponseByteArrPostOut);
			    }

			    for (byte b : postAppendBytes) {
			      if (this._currentResponseBytePostLength++ >= this._responseByteMaxLength) {
			        this._currentResponseByteArrPostOut = new ByteArrayOutputStream();
			        this._bytesArrPostOutput.add(this._currentResponseByteArrPostOut);
			        this._currentResponseByteLength = 0;
			      }
			      this._currentResponseByteArrPostOut.write(b);
			    }
			    this._responseBytesRead += postAppendBytes.length;
		  }
	  }
	  
  }

  public long responseBytesRead() {
    return this._responseBytesRead;
  }

  public void respondString(String string) throws Exception {
	  if(string==null) return;
	  if(string.length()==0) return;
	  this.respond.print(string);
  }

  public void outputResponseContent(OutputStream outresponse) throws Exception {
    if (this._bytesArrOutput.isEmpty()) return;
    
    BufferedOutputStream buffwrite = new BufferedOutputStream(outresponse);
    while (!this._bytesArrPreOutput.isEmpty()) {
	    this._currentResponseByteArrOut = ((ByteArrayOutputStream)this._bytesArrPreOutput.remove(0));
	    buffwrite.write(this._currentResponseByteArrOut.toByteArray());
	    buffwrite.flush();
	}
    
    while (!this._bytesArrOutput.isEmpty()) {
	    this._currentResponseByteArrOut = ((ByteArrayOutputStream)this._bytesArrOutput.remove(0));
	    buffwrite.write(this._currentResponseByteArrOut.toByteArray());
	    buffwrite.flush();
	}
    
    while (!this._bytesArrPostOutput.isEmpty()) {
      this._currentResponseByteArrOut = ((ByteArrayOutputStream)this._bytesArrPostOutput.remove(0));
      buffwrite.write(this._currentResponseByteArrOut.toByteArray());
      buffwrite.flush();
    }
    buffwrite.flush();
    buffwrite.close();
    buffwrite = null;
  }

  public void setRequestParameter(String paramName, String paramValue, boolean override)
  {
    paramName = paramName.toUpperCase();
    paramValue = paramValue == null ? "" : paramValue;
    ArrayList paramValueArray = (ArrayList)this._requestParameters.get(paramName);
    if (override) {
      if (paramValueArray != null) {
        while (!paramValueArray.isEmpty()) paramValueArray.remove(0);
      }
      else {
        this._requestParameters.put(paramName, paramValueArray = new ArrayList());
      }

    }
    else if (paramValueArray == null) {
      this._requestParameters.put(paramName, paramValueArray = new ArrayList());
    }

    if (!paramValue.equals(""))
      paramValueArray.add(paramValue);
  }
  
  public HashMap<String,ArrayList<String>> requestParameters(){
	  return this._requestParameters;
  }

  public ArrayList<String> requestParameterArray(String paramName)
  {
    paramName = paramName.toUpperCase();
    ArrayList paramValueArray = (ArrayList)this._requestParameters.get(paramName);
    if (paramValueArray == null) {
      paramValueArray = new ArrayList();
    }
    return paramValueArray;
  }

  public String requestParameter(String paramName, String defaultValue) {
    String paramValue = requestParameter(paramName);
    return paramValue.equals("") ? defaultValue : defaultValue == null ? "" : paramValue;
  }

  public String requestParameterProperty(String paramName,String paramProperty){
	  if(this._requestParameterProperties.containsKey(paramName=paramName.toUpperCase())){
		  if(this._requestParameterProperties.get(paramName).containsKey(paramProperty=paramProperty.toUpperCase())){
			  return this._requestParameterProperties.get(paramName).get(paramProperty);
		  }
	  }
	  return "";
  }
  
  public File requestParameterFile(String paramName){
	  if(this.requestParameterProperty(paramName=paramName.toUpperCase(),"FILENAME").equals("")) return null;
	  return this._requestParameterFiles.get(paramName);
  }
  
  public String requestParameter(String paramName) {
    ArrayList paramValueArray = requestParameterArray(paramName);
    return paramValueArray.isEmpty() ? "" : (String)paramValueArray.get(0);
  }

  public void cleanUpWidget() {
    while (!this._requestHeaders.keySet().isEmpty()) {
      String reqHeaderName = (String)this._requestHeaders.keySet().toArray()[0];
      this._requestHeaders.remove(reqHeaderName);
      this._requestHeaders.keySet().remove(reqHeaderName);
    }
    this._requestHeaders.keySet().clear();
    this._requestHeaders.clear();
    this._requestHeaders = null;

    while (!this._responseHeaders.isEmpty()) {
      String respHeaderName = (String)this._responseHeaders.keySet().toArray()[0];
      this._responseHeaders.remove(respHeaderName);
      this._responseHeaders.keySet().remove(respHeaderName);
    }
    this._responseHeaders.keySet().clear();
    this._responseHeaders.clear();
    this._responseHeaders = null;

    while (!this._requestParameters.isEmpty()) {
      String reqParamName = (String)this._requestParameters.keySet().toArray()[0];
      ArrayList reqParmValues = (ArrayList)this._requestParameters.remove(reqParamName);
      while (!reqParmValues.isEmpty()) reqParmValues.remove(0);
      this._requestParameters.keySet().remove(reqParamName);
      reqParmValues = null;
    }
    this._requestParameters.keySet().clear();
    this._requestParameters.clear();
    this._requestParameters = null;

    String reqParamPropName = "";
    while (!this._requestParameterProperties.isEmpty()) {
      HashMap reqProperties = (HashMap)this._requestParameterProperties.remove(reqParamPropName = (String)this._requestParameterProperties.keySet().toArray()[0]);
      String reqPropName = "";
      while (!reqProperties.isEmpty()) {
        reqProperties.remove(reqPropName = (String)reqProperties.keySet().toArray()[0]);
        reqProperties.keySet().remove(reqPropName);
      }
      reqProperties.clear();
      reqProperties = null;
      this._requestParameterProperties.keySet().remove(reqParamPropName);
    }
    this._requestParameterProperties.keySet().clear();
    this._requestParameterProperties.clear();
    this._requestParameterProperties = null;

    while(!this._requestParameterFiles.isEmpty()){
    	String fileReqParamName=(String)this._requestParameterFiles.keySet().toArray()[0];
    	this._requestParameterFiles.remove(fileReqParamName).delete();
    	this._requestParameterFiles.keySet().remove(fileReqParamName);
    }
    
    while (!this._bytesArrOutput.isEmpty()) {
      ((ByteArrayOutputStream)this._bytesArrOutput.remove(0)).reset();
    }
    this._bytesArrOutput.clear();
    this._bytesArrOutput = null;
  }
  
  public void simpleElement(String elemName) throws Exception {
    startElement(elemName, (String[])null, false); endElement(elemName, false);
  }

  public void simpleElement(String elemName, String[] elemProperties) throws Exception {
    startElement(elemName, elemProperties, false); endElement(elemName, false);
  }

  public void startElement(String elemName,boolean isComplextElement) throws Exception {
	  this.startElement(elemName, "", isComplextElement);
  }
  
  public void startElement(String elemName, String elemProperties, boolean isComplextElement) throws Exception {
	  this.startElement(elemName,(String[])(elemProperties==null?null:elemProperties.indexOf("=")>-1?elemProperties.split("[|]"):null), isComplextElement);
  }
  
  private ArrayList<String> _complexElementsByLevel=new ArrayList<String>();
  public void startElement(String elemName, String[] elemProperties, boolean isComplextElement) throws Exception {
    this.respond.print("<" + elemName);
    if (elemProperties != null) {
      for (String elemProp : elemProperties) {
        elemProp = elemProp.trim();
        if (elemProp.indexOf("=") > -1) {
        	this.respond.print(" " + encodeHTML(elemProp.substring(0, elemProp.indexOf("="))) + "=\"" + encodeHTML(elemProp.substring(elemProp.indexOf("=") + 1)) + "\"");
        }
      }
    }
    if (isComplextElement){
    	this.respond.print(">");
    	_complexElementsByLevel.add(elemName);
    }
  }
  
  public void startComplexElement(String elemName) throws Exception {
	  this.startElement(elemName, "", true);
  }
  
  public void startComplexElement(String elemName, String elemProperties) throws Exception {
	  this.startElement(elemName,(String[])(elemProperties==null?null:elemProperties.indexOf("=")>-1?elemProperties.split("[|]"):null), true);
  }
  
  public void startComplexElement(String elemName, String []elemProperties) throws Exception {
	  this.startElement(elemName, elemProperties, true);
  }

  public static String encodeHTML(String s)
  {
    StringBuffer out = new StringBuffer();

    for (char c : s.toCharArray()) {
      switch (c) { case '&':
        out.append("&amp;"); break;
      case '<':
        out.append("&lt;"); break;
      case '>':
        out.append("&gt;"); break;
      default:
    	  switch(c){
    	  case 10:
    		  	out.append((c + ""));
    		  break;
    	  case 13:
    		  out.append((c + ""));
    		 break;
    	  case 32:
    		  out.append((c + ""));
    		  break;
    	  default:
    		  if ((c>=127) || (c <31))
    	        {
    	          out.append("&#" + String.valueOf((byte)c) + ";");
    	        }
    	        else
    	        {
    	          out.append(c);
    	        }
    		  break;
    	  }
        
        break;
      }
    }
    return out.toString();
  }
  
  public void endComplexElement(String elemName) throws Exception {
	  this.endElement(elemName, true);
  }

  public void endElement(String elemName, boolean isComplextElement) throws Exception {
	  if(isComplextElement){
		  String currentElemName="";
		  if(_complexElementsByLevel.isEmpty()||!(currentElemName=_complexElementsByLevel.remove(_complexElementsByLevel.size()-1)).equals(elemName)){
			  throw new Exception("Invalid end element ["+elemName+"]"+(currentElemName.equals("")?"":(" should be ["+currentElemName+"]")));
		  }
	  }
	  this.respond.print(isComplextElement ? "</" + elemName + ">" : "/>");
  }

  public void importRequestParametersIntoMap(HashMap<String, Object> mapToPopulate, String parameterNames) {
    if (mapToPopulate != null) {
      String [] paramNamesArr=null;
      parameterNames = parameterNames == null ? "" : parameterNames.trim();
      if(parameterNames.equals("")){
    	  paramNamesArr=new String[_requestParameters.keySet().size()];
    	  _requestParameters.keySet().toArray(paramNamesArr);
      }
      else{
    	  paramNamesArr=parameterNames.split(","); 
      }
      for (String reqParamName : paramNamesArr)
        mapToPopulate.put(reqParamName.toUpperCase(), requestParameter(reqParamName));
    }
  }
  
  public Database db(String dbalias) {
	  return Database.dballias(dbalias);
  }
  
  public void dbquery(String dbalias,String query,HashMap<String,Object> sqlparams,String methodName) throws Exception {
	  Database db=this.db(dbalias);
	  if (db!=null) {
		  db.executeDBRequest(null, query, sqlparams,(methodName=methodName==null?"":methodName.trim()).equals("")?null:this, methodName.equals("")?null:methodName);
	  }
  }
  
  static enum WebRequestContentType
  {
    MULTIPARTCONTENT, 
    URLENCODED,
    JSON,
    CUSTOM;
  }
}
