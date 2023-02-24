package inovo.web;

import inovo.db.Database;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class InovoHttpClient
{
  public static void sendRequest(TreeMap<Long, ArrayList<Object>> datasetResponse, String url, HashMap<String, String> headerParams, HashMap<String, String> postParams, boolean isMultipartPostedParams, HashMap<String, String> uriqueryParams,ArrayList<Object> datasetColumns)
    throws Exception
  {
    StringBuilder markupResponse = new StringBuilder();
    sendRequest(markupResponse, url, headerParams, postParams, isMultipartPostedParams, uriqueryParams);
    ByteArrayInputStream bytesinStream=new ByteArrayInputStream(markupResponse.toString().getBytes());
    markupResponse.delete(0, markupResponse.length());
    Database.populateDatasetFromFlatFileStream(datasetResponse, "CSV",	bytesinStream, datasetColumns,',',null);
  }

  public static void sendRequest(StringBuilder markupResponse, String url, HashMap<String, String> headerParams, HashMap<String, String> postParams, boolean isMultipartPostedParams, HashMap<String, String> uriqueryParams) throws ClientProtocolException, IOException, URISyntaxException {
    HttpClient markupClient = new DefaultHttpClient();
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
        MultipartEntity requestMultiPartParams = new MultipartEntity();
        for (String postParamName : postParams.keySet()) {
          requestMultiPartParams.addPart(postParamName, new StringBody((String)postParams.get(postParamName)));
        }
        requestMethod.setEntity(requestMultiPartParams);
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
}