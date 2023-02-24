package inovo.web.servlet;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class MultipartContentInputStream extends ContentInputProcessor {

	public MultipartContentInputStream(Request request) {
		super(request);
	}

	private enum MultipartContentStage{
		none,
		header,
		content,
		boundary,
		done,
	}
	
	@Override
	public boolean isDone() {
		return (_multipartContentStage==MultipartContentStage.done);
	}
	
	private MultipartContentStage _multipartContentStage=MultipartContentStage.none;
	private StringBuilder _tempMultipartString=new StringBuilder();
	private byte[] _boundary=null;
	private int _boundaryIndex=0;	
	
	private HashMap<String,String> _multipartHeaders=new HashMap<String, String>();
	
	private String multipartHeader(String header){
		if((header=header==null?"":header.trim().toUpperCase()).equals("")) return "";
		return (header=this._multipartHeaders.get(header))==null?"":header;
	}
	
	private OutputStream  _contentValueStream=null;
	
	@Override
	public void processInputByte(byte b, boolean done) throws Exception {
		switch(_multipartContentStage){
		case boundary:
			switch(b){
			case 45:
				_multipartContentStage=MultipartContentStage.done;
				break;
			case 13:
				break;
			case 10:
				_multipartContentStage=MultipartContentStage.header;
				break;
			}
			break;
		case content:
			if(_boundary[_boundaryIndex]==b){
				_boundaryIndex++;
				if(_boundaryIndex==_boundary.length){
					_boundaryIndex=0;
					this.flushContentValuBytes();
					
					if(_multipartHeaders.containsKey("FILENAME")){
						this.request().setParameter(this.multipartHeader("NAME"), this.multipartHeader("FILENAME"));
					}
					else{
						this.request().setParameter(this.multipartHeader("NAME"), ((ByteArrayOutputStream)this._contentValueStream).toString());
					}
					
					try{
						_contentValueStream.close();
					}
					catch(Exception e){
						throw e;
					}
					
					_contentValueStream=null;
					
					this._multipartContentStage=MultipartContentStage.boundary;
					this._multipartHeaders.clear();
				}
			}
			else{
				if(_boundaryIndex>0){
					int boundaryIndex=0;
					while(boundaryIndex<_boundaryIndex){
						this.writeContentValueByte(_boundary[boundaryIndex++]);	
					}
					_boundaryIndex=0;
				}
				this.writeContentValueByte(b);
			}
			break;
		case header:
			switch(b){
			case 10:
				if(this._tempMultipartString.length()>0){
					if(this._tempMultipartString.indexOf(":")>-1){
						String headerName=_tempMultipartString.substring(0,_tempMultipartString.indexOf(":")).trim().toUpperCase();
						String headerValue=_tempMultipartString.substring(this._tempMultipartString.indexOf(":")+1,this._tempMultipartString.length()).trim();
						this.request().resetStringBuilder(_tempMultipartString);
						if(headerName.equals("CONTENT-DISPOSITION")){
							_tempMultipartString.append(headerValue);
							if(this.request().stringBuilderString(_tempMultipartString).startsWith("form-data;")){
								_tempMultipartString.delete(0,"form-data;".length());
								while((_tempMultipartString.charAt(0)+"").trim().equals("")) _tempMultipartString.delete(0, 1);
							}
							while(_tempMultipartString.length()>0){
								headerValue="";
								headerName="";
								headerName=_tempMultipartString.substring(0,this._tempMultipartString.indexOf("=")).trim().toUpperCase();
								_tempMultipartString.delete(0, this._tempMultipartString.indexOf("=")+1);
								while((_tempMultipartString.charAt(0)+"").trim().equals("")) _tempMultipartString.delete(0, 1);
								_tempMultipartString.delete(0, 1);
								headerValue=_tempMultipartString.substring(0,this._tempMultipartString.indexOf("\"")).trim();
								_tempMultipartString.delete(0,this._tempMultipartString.indexOf("\"")+1);
								this._multipartHeaders.put(headerName,headerName.equals("NAME")?headerValue.toUpperCase():headerValue);
								if(this._tempMultipartString.indexOf(";")>-1){
									this._tempMultipartString.delete(0, this._tempMultipartString.indexOf(";")+1);
									if(this._tempMultipartString.length()>0){
										while((_tempMultipartString.charAt(0)+"").trim().equals("")) _tempMultipartString.delete(0, 1);
									}
								}
							}
							
							this.request().resetStringBuilder(_tempMultipartString);
						}
						else{
							this._multipartHeaders.put(headerName, headerValue);
						}
					}
					this.request().resetStringBuilder(_tempMultipartString);
				}
				else{
					if(this.multipartHeader("FILENAME").equals("")){
						_contentValueStream=new ByteArrayOutputStream();
					}
					else{
						this.request().setFileParameter(this.multipartHeader("NAME"));
						_contentValueStream=new FileOutputStream(this.request().fileParameter(this.multipartHeader("NAME")));
					}
					this._multipartContentStage=MultipartContentStage.content;
				}
				break;
			case 13:break;
			default:
				this._tempMultipartString.append((char)b);
				break;
			}
			break;
		case none:
			switch(b){
			case 10:
				if(_tempMultipartString.length()>0){
					_boundary=this.request().stringBuilderString(_tempMultipartString).getBytes();
					_boundaryIndex=0;
					this.request().resetStringBuilder(_tempMultipartString);
					this._multipartContentStage=MultipartContentStage.header;
				}
				break;
			case 13:break;
			default:
				this._tempMultipartString.append((char)b);
			}
			break;
		}
	}
	
	
	private byte [] _contentValueBytes=new byte[8192];
	int _contentValueBytesIndex=0;
	private void writeContentValueByte(byte b) throws Exception{
		_contentValueBytes[_contentValueBytesIndex++]=b;
		if(_contentValueBytes.length==_contentValueBytesIndex){
			this.flushContentValuBytes();
		}
	}

	private void flushContentValuBytes() throws Exception{
		if(_contentValueBytesIndex>0){
			byte[] contentValueBytes=new byte[_contentValueBytesIndex];
			System.arraycopy(_contentValueBytes, 0, contentValueBytes, 0, _contentValueBytesIndex);
			_contentValueBytesIndex=0;
			_contentValueStream.write(contentValueBytes,0,contentValueBytes.length);
			_contentValueStream.flush();
		}
	}

	@Override
	public void cleanupContentProcessor() {
		this.request().resetStringBuilder(_tempMultipartString);
		super.cleanupContentProcessor();
		_tempMultipartString=null;
	}
}
