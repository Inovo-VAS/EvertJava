package pcowebserviceclient;

import java.rmi.RemoteException;
import java.util.Calendar;

import PcowsLibrary.PcowsServiceProxy;
import PcowsLibrary.PcowsService___InsertOutboundRecord4;
import PcowsLibrary.PcowsService___InsertOutboundRecord4Response;

public class PcoWebServiceMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PcowsServiceProxy pcowsServiceProxy=new PcowsServiceProxy();
		PcowsLibrary.PcowsService___InsertOutboundRecord4 parameters=new PcowsService___InsertOutboundRecord4();
		parameters.setServiceId(8002);
		parameters.setLoadId(1);
		parameters.setSourceId(10000);
		parameters.setStatus(1);
		parameters.setComments("TEST COMMENTS");
		parameters.setName("BERTRAM");
		parameters.setScheduleDate(Calendar.getInstance());
		parameters.setCapturingAgent(0);
		parameters.setPhone("0810497863");
		parameters.setPhoneTimeZone("");
		
		parameters.setTimeZone("");
		
		parameters.setAlternativePhoneDescriptions("1,2");
		parameters.setAlternativePhones("0810497863,0810497863");
		parameters.setAlternativePhoneTimeZones("");
		parameters.setAutomaticTimeZoneDetection(true);
		
		parameters.setCustomData1("");
		parameters.setCustomData2("");
		parameters.setCustomData3("");		
		
		parameters.setCallerId("");
		parameters.setCallerName("");
		parameters.setPriority(100);
		
		try {
			PcowsService___InsertOutboundRecord4Response response=pcowsServiceProxy.insertOutboundRecord4(parameters);
			System.out.println(response.getResult());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
