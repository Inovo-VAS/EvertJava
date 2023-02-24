import javax.telephony.JtapiPeer;
import javax.telephony.JtapiPeerFactory;
import javax.telephony.JtapiPeerUnavailableException;

public class JTApiAvayaClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		JtapiPeer peer=JtapiPeerFactory.getJtapiPeer(null);
		} catch (JtapiPeerUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
