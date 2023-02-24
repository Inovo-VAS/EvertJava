package PcowsLibrary;

public class PcowsServiceProxy implements PcowsLibrary.PcowsService_PortType {
  private String _endpoint = null;
  private PcowsLibrary.PcowsService_PortType pcowsService_PortType = null;
  
  public PcowsServiceProxy() {
    _initPcowsServiceProxy();
  }
  
  public PcowsServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initPcowsServiceProxy();
  }
  
  private void _initPcowsServiceProxy() {
    try {
      pcowsService_PortType = (new PcowsLibrary.PcowsService_ServiceLocator()).getPcowsServicePort();
      if (pcowsService_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)pcowsService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)pcowsService_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (pcowsService_PortType != null)
      ((javax.xml.rpc.Stub)pcowsService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public PcowsLibrary.PcowsService_PortType getPcowsService_PortType() {
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType;
  }
  
  public PcowsLibrary.PcowsService___AddCallDataResponse addCallData(PcowsLibrary.PcowsService___AddCallData parameters) throws java.rmi.RemoteException{
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType.addCallData(parameters);
  }
  
  public PcowsLibrary.PcowsService___GetCallDataResponse getCallData(PcowsLibrary.PcowsService___GetCallData parameters) throws java.rmi.RemoteException{
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType.getCallData(parameters);
  }
  
  public PcowsLibrary.PcowsService___GetErrorMessageResponse getErrorMessage(PcowsLibrary.PcowsService___GetErrorMessage parameters) throws java.rmi.RemoteException{
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType.getErrorMessage(parameters);
  }
  
  public PcowsLibrary.PcowsService___GetServerTimeResponse getServerTime(PcowsLibrary.PcowsService___GetServerTime parameters) throws java.rmi.RemoteException{
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType.getServerTime(parameters);
  }
  
  public PcowsLibrary.PcowsService___InsertOutboundRecordResponse insertOutboundRecord(PcowsLibrary.PcowsService___InsertOutboundRecord parameters) throws java.rmi.RemoteException{
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType.insertOutboundRecord(parameters);
  }
  
  public PcowsLibrary.PcowsService___InsertOutboundRecord2Response insertOutboundRecord2(PcowsLibrary.PcowsService___InsertOutboundRecord2 parameters) throws java.rmi.RemoteException{
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType.insertOutboundRecord2(parameters);
  }
  
  public PcowsLibrary.PcowsService___InsertOutboundRecord3Response insertOutboundRecord3(PcowsLibrary.PcowsService___InsertOutboundRecord3 parameters) throws java.rmi.RemoteException{
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType.insertOutboundRecord3(parameters);
  }
  
  public PcowsLibrary.PcowsService___InsertOutboundRecord4Response insertOutboundRecord4(PcowsLibrary.PcowsService___InsertOutboundRecord4 parameters) throws java.rmi.RemoteException{
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType.insertOutboundRecord4(parameters);
  }
  
  public PcowsLibrary.PcowsService___RefreshServiceResponse refreshService(PcowsLibrary.PcowsService___RefreshService parameters) throws java.rmi.RemoteException{
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType.refreshService(parameters);
  }
  
  public PcowsLibrary.PcowsService___ReloadOutboundQueuesResponse reloadOutboundQueues(PcowsLibrary.PcowsService___ReloadOutboundQueues parameters) throws java.rmi.RemoteException{
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType.reloadOutboundQueues(parameters);
  }
  
  public PcowsLibrary.PcowsService___RequestOutboundACDCallResponse requestOutboundACDCall(PcowsLibrary.PcowsService___RequestOutboundACDCall parameters) throws java.rmi.RemoteException{
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType.requestOutboundACDCall(parameters);
  }
  
  public PcowsLibrary.PcowsService___RequestOutboundACDCall2Response requestOutboundACDCall2(PcowsLibrary.PcowsService___RequestOutboundACDCall2 parameters) throws java.rmi.RemoteException{
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType.requestOutboundACDCall2(parameters);
  }
  
  public PcowsLibrary.PcowsService___RequestOutboundACDCall3Response requestOutboundACDCall3(PcowsLibrary.PcowsService___RequestOutboundACDCall3 parameters) throws java.rmi.RemoteException{
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType.requestOutboundACDCall3(parameters);
  }
  
  public PcowsLibrary.PcowsService___SumResponse sum(PcowsLibrary.PcowsService___Sum parameters) throws java.rmi.RemoteException{
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType.sum(parameters);
  }
  
  public PcowsLibrary.PcowsService___UnloadOutboundRecordResponse unloadOutboundRecord(PcowsLibrary.PcowsService___UnloadOutboundRecord parameters) throws java.rmi.RemoteException{
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType.unloadOutboundRecord(parameters);
  }
  
  public PcowsLibrary.PcowsService___RefreshTracersResponse refreshTracers(PcowsLibrary.PcowsService___RefreshTracers parameters) throws java.rmi.RemoteException{
    if (pcowsService_PortType == null)
      _initPcowsServiceProxy();
    return pcowsService_PortType.refreshTracers(parameters);
  }
  
  
}