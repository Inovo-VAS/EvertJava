package inovo.avon.cim;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class Ptp {
	
	private HttpClient httpClient=null;
	
	private String url="";
	
	public Ptp(String url){
		this.url=url;
	}
	
	 /// <summary>
    /// CaptureNewPTP: Call (REST service) when an agent captures a new PTP from the client. An entry will be added to the 
    /// PTP table which will be updated when a new import with payment information is processed by the PaymentUpdates() function.
    /// </summary>
    /// <param name="CIMClientRefID">Must be a valid CIM client reference number, call "/integration/CIDSearchClientsByIDNumber" function to obtain</param>
    /// <param name="paymentDueDate">Date string indicating the day when the client will make the payment</param>
    /// <param name="amountPromisedInCents">Amount that the client will pay in cents</param>
    /// <param name="paymentMethod">String describing the payment method. Must be one of the following: Single, Multiple x/y, Debit order. Where x is this payment's number of total y payments</param>
    /// <param name="agentName">Name or login of the agent that captured the PTP</param>
    /// <param name="agreementReferenceNr">Optional - Any reference number or string used by the customer (will show in reports)</param>
    /// <param name="currencySymbol">Optional - Currency used, will default to "R" when omitted</param>
    /// <param name="campaignID">Optional - ID of the campaign under which the PTP was captured (used for reporting)</param>
    /// <param name="accountReferenceID">Optional - The account ID (as string) that can be used to link this PTP to the customer's account</param>
    /// <param name="accountReferenceID2">Optional - An additional string that can be used to link this PTP to the customer's account, in case a single column does not uniquely identify the account</param>
    /// <param name="notes">Optional - Free text note area for the agent</param>
    /// <returns>String: "OK" on success, or else "ERROR" followed by description</returns>
	
	String lastError="";
	
	public boolean CaptureNewPTP(
			String CIMClientRefID,
			String paymentDueDate,
			String amountPromisedInCents,
			String paymentMethod,
			String agentName,
			String agreementReferenceNr,
			String currencySymbol,
			String accountReferenceID) throws Exception{
		
		if (paymentMethod==null||(paymentMethod=paymentMethod.trim()).equals("")){
			paymentMethod="Single";
		}
		
		if (currencySymbol==null||(currencySymbol=currencySymbol.trim()).equals("")){
			currencySymbol="R";
		}
		
		URL obj;
		try {
			obj = new URL(url);
		} catch (Exception e) {
			throw e;
		}
		HttpURLConnection con;
		try {
			con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "INOVO AVON");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		} catch (Exception e) {
			throw e;
		}
		
		StringBuilder urlParams=new StringBuilder();
		urlParams.append(urlParam("CIMClientRefID",CIMClientRefID,false));
		urlParams.append(urlParam("paymentDueDate", paymentDueDate,false));
		urlParams.append(urlParam("amountPromisedInCents", amountPromisedInCents,false));
		urlParams.append(urlParam("paymentMethod", paymentMethod,false));
		urlParams.append(urlParam("agentName", agentName,false));
		urlParams.append(urlParam("agreementReferenceNr",agreementReferenceNr,false));
		urlParams.append(urlParam("currencySymbol", currencySymbol,false));
		urlParams.append(urlParam("accountReferenceID", accountReferenceID,true));
		
		con.setDoOutput(true);
		DataOutputStream wr;
		try {
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParams.toString());
			wr.flush();
			wr.close();
		} catch (IOException e) {
			throw e;
		}
		
		int responseCode;
		try {
			responseCode = con.getResponseCode();
			//System.out.println("\nSending 'POST' request to URL : " + url);
			//System.out.println("Post parameters : " + urlParams.toString());
			//System.out.println("Response Code : " + responseCode);
		} catch (IOException e) {
			throw e;
		}
		

		BufferedReader in;
		try {
			in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			if (response.toString().equals("OK")){
				return true;
			} else {
				lastError=response.toString();
				return false;
			}
			
		} catch (IOException e) {
			throw e;
		}
	}
	
	private static String urlParam(String name,String value,boolean lastParam){
		return name+"="+value+(lastParam?"":"&");
	}
}
