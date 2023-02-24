package org.tempuri;

public class ITelephonyServiceProxy implements org.tempuri.ITelephonyService {
  private String _endpoint = null;
  private org.tempuri.ITelephonyService iTelephonyService = null;
  
  public ITelephonyServiceProxy() {
    _initITelephonyServiceProxy();
  }
  
  public ITelephonyServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initITelephonyServiceProxy();
  }
  
  private void _initITelephonyServiceProxy() {
    try {
      iTelephonyService = (new org.tempuri.TelephonyService2Locator()).getBasicHttpBinding_ITelephonyService();
      if (iTelephonyService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iTelephonyService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iTelephonyService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iTelephonyService != null)
      ((javax.xml.rpc.Stub)iTelephonyService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.tempuri.ITelephonyService getITelephonyService() {
    if (iTelephonyService == null)
      _initITelephonyServiceProxy();
    return iTelephonyService;
  }
  
  public org.datacontract.schemas._2004._07.TelephonyApi.SerialNoDetails findServiceItem(java.lang.String serialNo) throws java.rmi.RemoteException{
    if (iTelephonyService == null)
      _initITelephonyServiceProxy();
    return iTelephonyService.findServiceItem(serialNo);
  }
  
  
}