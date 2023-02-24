package presence;

import inovo.db.Database;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Administrator {
	
	private static Administrator _administrator=null;
	
	private String _presenceServerIP="";
	private String _environmentPath="";
	public Administrator(String environmentPath,String presenceServerIP){
		this._presenceServerIP=presenceServerIP;
		this._environmentPath=environmentPath;
	}
		
	private class AdministratorTask implements Runnable{
		
		private Method _adminTaskMethod=null;
		private HashMap<String,String> _adminTaskMethodParams=new HashMap<String,String>();
		private String _adminTaskResponse="";
		private boolean _adminTaskCompleted=false;
		private boolean _async=false;
		private AdministratorTask(String adminTaskName,String []taskParameters,boolean async){
			this._async=async;
			if(taskParameters!=null){
				for(String taskParam:taskParameters){
					if(taskParam.indexOf("=")>-1){
						_adminTaskMethodParams.put(taskParam.substring(0,taskParam.indexOf("=")).trim().toLowerCase(), taskParam.substring(taskParam.indexOf("=")+1).trim());
					}
				}
			}			
			
			this._adminTaskMethod=inovo.adhoc.AdhocUtils.findMethod(this.getClass().getMethods(), adminTaskName, false);
		}
		
		@Override
		public void run() {
			if(_async){
				_adminTaskCompleted=false;
				_async=false;
			}
			try{
				this._adminTaskResponse=(String)_adminTaskMethod.invoke(this,new Object[]{this._adminTaskMethodParams});
			}
			catch(Exception e){
				e.printStackTrace();
			}
			_adminTaskCompleted=true;
		}
		
		public String isServiceLoadEnabled(HashMap<String,String> taskParams){
			String serviceid="";
			String loadid="";
			if(taskParams!=null){
				if(taskParams.containsKey("serviceid")&&taskParams.containsKey("loadid")){
					serviceid=taskParams.get("serviceid");
					loadid=taskParams.get("loadid");
				}
			}
			if(serviceid.equals("")||loadid.equals("")){
				return "true";
			}			
			if(administrator("","").isServiceLoadEnabled(serviceid, loadid,_async)){
				return "true";
			}
			return "false";
		}
		
		public String reloadServicesBuffers(HashMap<String,String> taskParams){
			String serviceids="";
			if(taskParams!=null){
				if(taskParams.containsKey("serviceids")){
					serviceids=taskParams.get("serviceids");
				}
			}
			if(serviceids.equals("")){
				return "true";
			}			
			if(administrator("","").reloadServicesBuffers(serviceids,_async)){
				return "true";
			}
			return "false";
		}
	}
	
	public boolean reloadServicesBuffers(String serviceids){
		return reloadServicesBuffers(serviceids, false,false);
	}
	
	public boolean reloadServicesBuffers(String serviceids,boolean async){
		return reloadServicesBuffers(serviceids, false,async);
	}
	
	public boolean reloadServicesBuffers(String serviceids,boolean threaded,boolean async){
		if(async&&!threaded) threaded=true;
		if(threaded){
			AdministratorTask adminTask=new AdministratorTask("reloadServicesBuffers", new String[]{"serviceids="+serviceids},async);
			new Thread(adminTask).start();
			while(!adminTask._adminTaskCompleted){
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return adminTask._adminTaskResponse.equals("true");
		}
		else{
			boolean reloadedBuffers=true;
			if(serviceids!=null){
				ByteArrayOutputStream outputReloadBuffers=new ByteArrayOutputStream();
				boolean bufferReloaded=false;
				synchronized (_administrator) {
					for(String presenceSvrIp:this.presenceServerIP().split("[,]")){
						for(String serviceid:serviceids.split(",")){
							if(serviceid.equals("")) continue;
							outputReloadBuffers.reset();
							try {
								inovo.adhoc.AdhocUtils.executeConsoleCommand(new String[]{_environmentPath+File.separator+"pcoreloadbuffers.exe",presenceSvrIp,serviceid},outputReloadBuffers);
								if(reloadedBuffers){
									if(!(reloadedBuffers=(outputReloadBuffers.toString().equals(
											"Connection to Presence Server successfully opened. Address="+presenceSvrIp+":6100\r\n"+
											"Buffers for service "+serviceid+" reloaded successfully\r\n"+
											"Connection to Presence Server closed\r\n")))){
										if(outputReloadBuffers.toString().indexOf("Unable to reload buffers for service "+serviceid)==-1){
											new Exception(outputReloadBuffers.toString()).printStackTrace();
										}
										else{
											if(!bufferReloaded){
												bufferReloaded=true;
											}
										}
									}
								}
								else{
									break;
								}
								
							} catch (Exception e) {
								e.printStackTrace();
								//reloadedBuffers=false;
							}
						}
					}
				}
				outputReloadBuffers.reset();
				outputReloadBuffers=null;
				reloadedBuffers=bufferReloaded;
			}
			else{
				reloadedBuffers=false;
			}
			return reloadedBuffers;
		}
	}
	
	private String presenceServerIP() {
		return this._presenceServerIP;
	}

	public boolean isServiceLoadEnabled(String serviceid,String loadid){
		return isServiceLoadEnabled( serviceid, loadid, false);
	}
	
	public boolean isServiceLoadEnabled(String serviceid,String loadid,boolean threaded){
		if(threaded){
			AdministratorTask adminTask=new AdministratorTask("isServiceLoadEnabled", new String[]{"serviceid="+serviceid,"loadid="+loadid},false);
			new Thread(adminTask).start();
			while(!adminTask._adminTaskCompleted){
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return adminTask._adminTaskResponse.equals("true");
		}
		else{
			boolean enabledServiceLoad=true;
			if(serviceid!=null&&loadid!=null){
				HashMap<String, Object> sqlPrepParams=new HashMap<String,Object>();
				sqlPrepParams.put("STATUS", "D");
				sqlPrepParams.put("SERVICEID", serviceid);
				sqlPrepParams.put("LOADID", loadid);

				try {
					Database.executeDBRequest(null,"PRESENCE", "SELECT STATUS FROM <DBUSER>.PCO_LOAD WHERE SERVICEID=:SERVICEID AND LOADID=:LOADID", sqlPrepParams,null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				enabledServiceLoad=(sqlPrepParams.get("STATUS").equals("E"));
			}
			return enabledServiceLoad;
		}
	}
		
	public static Administrator administrator(String environmentPath,String presenceServerIP){
		if(_administrator==null){
			_administrator=new Administrator(environmentPath,presenceServerIP);
		}
		return _administrator;
	}
	
	
}
