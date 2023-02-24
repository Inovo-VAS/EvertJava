package inovo.message.gateway.statusfilter.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Client
{
  public static void sendRequest(TreeMap<Integer, ArrayList<String>> datasetResponse, String url, HashMap<String, String> headerParams, HashMap<String, String> postParams, boolean isMultipartPostedParams, HashMap<String, String> uriqueryParams,ArrayList<String> datasetColumns)
    throws Exception
  {
    StringBuilder markupResponse = new StringBuilder();
    sendRequest(markupResponse, url, headerParams, postParams, isMultipartPostedParams, uriqueryParams);
    ByteArrayInputStream bytesinStream=new ByteArrayInputStream(markupResponse.toString().getBytes());
    markupResponse.delete(0, markupResponse.length());
   
    populateDatasetFromFlatFileStream(datasetResponse, "CSV",	bytesinStream, datasetColumns,',',new DatasetReader(){
    	@Override
    	public void rowDataRead(int rowindex, ArrayList<String> rowData,
    			ArrayList<String> rowColumns) {
    		//TODO
    		//EXPORT RULES HERE
    	}
    });
  }

  public static void sendRequest(StringBuilder markupResponse, String url, HashMap<String, String> headerParams, HashMap<String, String> postParams, boolean isMultipartPostedParams, HashMap<String, String> uriqueryParams) throws ClientProtocolException, IOException, URISyntaxException {
    HttpClient markupClient = HttpClients.createDefault();
    if (uriqueryParams == null) uriqueryParams = new HashMap<String,String>();
    if (postParams == null) postParams = new HashMap<String,String>();
    if (headerParams == null) headerParams = new HashMap<String,String>();

    URIBuilder uribuilder = new URIBuilder();
    String uritype = url.split("://")[0].toLowerCase();
    uribuilder.setScheme(uritype);
    url = url.split("://")[1];

    if (url.split("[?]").length > 1) {
      for (String urlqueryparamstring : url.split("[?]")[1].split("[&]")) {
        if (urlqueryparamstring.split("=").length == 2) {
          uriqueryParams.put(urlqueryparamstring.split("=")[0], urlqueryparamstring.split("=")[1]);
        }
      }
      url = url.split("[?]")[0];
    }
    String uridomain = url.split("/")[0];
    if (uridomain.indexOf(":") == -1) {
      uribuilder.setHost(uridomain);
    }
    else
    {
      uribuilder.setPort(Integer.parseInt(uridomain.split(":")[1]));
      url = url.substring(uridomain.split(":")[0].length());
      url = url.substring(uridomain.split(":")[1].length() + 1);
      uridomain = uridomain.split(":")[0];

      uribuilder.setHost(uridomain);
    }
    String uriresource = "/";
    if (url.split("/").length > 0) {
      uriresource = url;
    }
    uribuilder.setPath(uriresource);
    
    for (Iterator<String> uriqparami = uriqueryParams.keySet().iterator(); uriqparami.hasNext(); ) { 
    	String uriQueryParamName = uriqparami.next();
    	uribuilder.setParameter(uriQueryParamName, uriqueryParams.get(uriQueryParamName));
    }
    URI uritoget = uribuilder.build();
    HttpPost requestMethod = new HttpPost(uritoget);

    for (String headerName : headerParams.keySet()) {
      requestMethod.addHeader(headerName.toUpperCase(), (String)headerParams.get(headerName));
    }

    if (postParams.size() > 0) {
      if (isMultipartPostedParams) {
    	  MultipartEntityBuilder requestMultiPartParams = MultipartEntityBuilder.create();
          for (String postParamName : postParams.keySet()) {
            requestMultiPartParams.addPart(postParamName, new StringBody((String)postParams.get(postParamName),ContentType.TEXT_PLAIN));
          }
          requestMethod.setEntity((HttpEntity) requestMultiPartParams);
      }
      else
      {
        ArrayList parameters = new ArrayList();
        for (String postParamName : postParams.keySet())
        {
          parameters.add(new BasicNameValuePair(postParamName, (String)postParams.get(postParamName)));
        }
        UrlEncodedFormEntity requesStandardFormPartParams = new UrlEncodedFormEntity(parameters);
        requestMethod.setEntity(requesStandardFormPartParams);
      }
    }

    HttpResponse markupResponseRecieved = markupClient.execute(requestMethod);
    HttpEntity responseEntity = markupResponseRecieved.getEntity();

    int bytesreadlength = responseEntity.getContent().available();
    while (bytesreadlength > 0) {
      byte[] bytesread = new byte[bytesreadlength];
      bytesreadlength = responseEntity.getContent().read(bytesread);
      markupResponse.append(new String(bytesread));
    }
    EntityUtils.consume(responseEntity);
  }
  
  public static void populateDatasetFromFlatFileStream(
			TreeMap<Integer, ArrayList<String>> dataSetToFill,
			String flatfileFormat,InputStream inputDatasetSteam,ArrayList<String> datasetColumns,char colSep,Object rowOutputMethodOwner) throws Exception{
		
		Method rowOutputMethod=(rowOutputMethodOwner==null?null:rowOutputMethodOwner.getClass().getDeclaredMethod("rowDataRead",new Class<?>[]{int.class,Array.class,Array.class}));
		
		boolean readColumnsOut=false;
		if(datasetColumns==null&&rowOutputMethodOwner==null){
			if(dataSetToFill.size()==0){
				datasetColumns = new ArrayList<String>();
			}
			else if(dataSetToFill.size()==1){
				datasetColumns=dataSetToFill.get(0);
			}
			else{
				datasetColumns = new ArrayList<String>();
			}
		}
		else{
			if(datasetColumns==null){
				datasetColumns=new ArrayList<String>();
			}
		}
		
		if(datasetColumns.isEmpty()){
			readColumnsOut=true;
		}
		
		int bavailable=0;
		boolean intextData=false;
		byte[] bytesreadout=new byte[1024];
		char prevc=0;
		String linefield="";
		ArrayList<String> rowArray=new ArrayList<String>();
		int rowIndex=0;
		
		int colindex=0;
		int rowcolindex=0;
		int totalColumns=0;
		if(!readColumnsOut){
			if(rowOutputMethodOwner==null) dataSetToFill.put(rowIndex++, datasetColumns);
			totalColumns=datasetColumns.size();
		}
		
		
		while((bavailable=inputDatasetSteam.read(bytesreadout))>-1){
			if(bavailable>0){
				int bcount=0;
				while(bcount<bavailable){
					byte bf=bytesreadout[bcount++];
					char cstrm=((bf==0?' ':(char)bf));
					if(flatfileFormat.equals("CSV")){
						if(cstrm==colSep){
							if(intextData){
								prevc=cstrm;
								linefield+=cstrm;
								continue;
							}
							if(readColumnsOut){
								linefield=linefield.trim().toUpperCase();
								if(!linefield.equals("")){
									datasetColumns.add(linefield);
								}
							}
							else{
								linefield=linefield.trim();
								if(datasetColumns.get(colindex).equals(linefield.toUpperCase())) colindex++;
								rowArray.add(linefield.trim());
								rowcolindex++;
							}
							linefield="";
							prevc=cstrm;
						}
						else if(cstrm=='\"'){
							if(!intextData&&prevc!='\"'){
								intextData=true;
								linefield="";
								prevc=0;
							}
							else{
								if(prevc==cstrm&&prevc=='\"'){
									linefield+=cstrm;
									prevc=0;
								}
								else if(prevc!=cstrm&&cstrm=='\"'){
									intextData=false;
								}
								else{
									linefield+=cstrm;
									prevc=cstrm;
								}
							}
						}
						else if(cstrm==13){				
							if(!intextData){prevc=cstrm; continue; }
							linefield+=cstrm;
							prevc=cstrm;
						}
						else if(cstrm==10){
							if(intextData){
								linefield+=cstrm;
								prevc=cstrm;
								continue;
							}
							else if(readColumnsOut){
								linefield=linefield.trim().toUpperCase();
								if(!linefield.equals("")){
									datasetColumns.add(linefield);
								}
								if(rowOutputMethodOwner==null){
									dataSetToFill.put(rowIndex++, datasetColumns);
								}
								else{
									if(rowOutputMethodOwner!=null&&rowOutputMethod!=null){
										rowOutputMethod.invoke(rowOutputMethodOwner, new Object[]{Integer.valueOf(rowIndex++),datasetColumns,datasetColumns});
						            }
								}
								totalColumns=datasetColumns.size();
								readColumnsOut=false;
							}
							else{
								linefield=linefield.trim();
								if(datasetColumns.get(colindex).equals(linefield.toUpperCase())) colindex++;
								rowArray.add(linefield);
								
								rowcolindex++;								
								if(rowcolindex>=totalColumns&&totalColumns>colindex){
										while(rowArray.size()>totalColumns) rowArray.remove(rowArray.size()-1);
										if(rowOutputMethodOwner==null){
											dataSetToFill.put(rowIndex++,rowArray);
										}
										else{
											if(rowOutputMethodOwner!=null&&rowOutputMethod!=null){
												rowOutputMethod.invoke(rowOutputMethodOwner, new Object[]{Integer.valueOf(rowIndex++),rowArray,datasetColumns});
								            }
										}
								}
								else{
									//TODO
								}
								rowArray=new ArrayList<String>();
							}	
							rowcolindex=0;
							colindex=0;
							linefield="";
							prevc=cstrm;
						}
						else{
							linefield+=cstrm;
							prevc=cstrm;
						}
					}
				}
			}
		}
	}
  
}
