package za.co.woolworths.Indibano.WS_WFS_Multimedia;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;

public class WS_WFS_MultimediaProxy
  implements WS_WFS_Multimedia_PortType
{
  private String _endpoint = null;
  private WS_WFS_Multimedia_PortType wS_WFS_Multimedia_PortType = null;

  public WS_WFS_MultimediaProxy() {
    _initWS_WFS_MultimediaProxy();
  }

  public WS_WFS_MultimediaProxy(String endpoint) {
    this._endpoint = endpoint;
    _initWS_WFS_MultimediaProxy();
  }

  private void _initWS_WFS_MultimediaProxy() {
    try {
      this.wS_WFS_Multimedia_PortType = new WS_WFS_Multimedia_ServiceLocator().getWS_WFS_MultimediaSOAP();
      if (this.wS_WFS_Multimedia_PortType != null)
        if (this._endpoint != null)
          ((Stub)this.wS_WFS_Multimedia_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", this._endpoint);
        else
          this._endpoint = ((String)((Stub)this.wS_WFS_Multimedia_PortType)._getProperty("javax.xml.rpc.service.endpoint.address"));
    }
    catch (ServiceException localServiceException)
    {
    }
  }

  public String getEndpoint() {
    return this._endpoint;
  }

  public void setEndpoint(String endpoint) {
    this._endpoint = endpoint;
    if (this.wS_WFS_Multimedia_PortType != null)
      ((Stub)this.wS_WFS_Multimedia_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", this._endpoint);
  }

  public WS_WFS_Multimedia_PortType getWS_WFS_Multimedia_PortType()
  {
    if (this.wS_WFS_Multimedia_PortType == null)
      _initWS_WFS_MultimediaProxy();
    return this.wS_WFS_Multimedia_PortType;
  }

  public MailExportResponseObjectout requestSendMultiMediaMsg(MailExportRequestObjectin mailExportRequestObjectin) throws RemoteException {
    if (this.wS_WFS_Multimedia_PortType == null)
      _initWS_WFS_MultimediaProxy();
    return this.wS_WFS_Multimedia_PortType.requestSendMultiMediaMsg(mailExportRequestObjectin);
  }
}