package inovo.presence.utils.pm;

import java.io.OutputStream;
import java.util.HashMap;

public class Console {
	
	private static HashMap<String,NetworkConsole> _pmconsoles=new HashMap<String,NetworkConsole>();
		
	public static void executeRequest(String args[],String presenceserverip,OutputStream consolOutput) throws Exception{
		NetworkConsole networkConsole=networkConsole(presenceserverip);
		if(networkConsole!=null){
			networkConsole.executeRequest(args, consolOutput);
		}
	}
	
	public static void executeRequest(String args[],NetworkConsole networkConsole,OutputStream consolOutput) throws Exception{
		if(networkConsole!=null){
			networkConsole.executeRequest(args, consolOutput);
		}
	}
	
	public static void disconnectNetworkConsole(String presenceserverip) throws Exception{
		Exception ex=null;
		NetworkConsole networkConsole=null;
		synchronized (_pmconsoles) {
			if(!_pmconsoles.containsKey(presenceserverip)){
				try {
					networkConsole=_pmconsoles.remove(presenceserverip);
					networkConsole.cleanUpConsole();
					networkConsole=null;
				} catch (Exception e) {
					ex=e;
				}
			}
		}
		if(ex!=null){
			throw ex;
		}
	}
	
	public static NetworkConsole networkConsole(String presenceserverip) throws Exception{
		Exception ex=null;
		NetworkConsole networkConsole=null;
		synchronized (_pmconsoles) {
			if(!_pmconsoles.containsKey(presenceserverip)){
				try {
					_pmconsoles.put(presenceserverip,(networkConsole = new NetworkConsole(presenceserverip)));
				} catch (Exception e) {
					ex=e;
				}
			}
			else{
				networkConsole=_pmconsoles.get(presenceserverip);
			}
		}
		
		if(ex!=null){
			ex.printStackTrace();
			throw ex;
		}
		return networkConsole;
	}
	
	public static void cleanUpConsoles() throws Exception{
		synchronized (_pmconsoles) {
			while(!_pmconsoles.isEmpty()){
				String presenceServerIP=(String)_pmconsoles.keySet().toArray()[0];
				_pmconsoles.remove(presenceServerIP).cleanUpConsole();
				_pmconsoles.keySet().remove(presenceServerIP);
			}
		}
	}
}
