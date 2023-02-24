package za.co.woolworths.Indibano.WS_WFS_Multimedia;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import mailsoaclient.WFS_Multimedia;

import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

public class WS_WFS_Multimedia_ServiceLocator extends Service
  implements WS_WFS_Multimedia_Service
{
  private String WS_WFS_MultimediaSOAP_address = "https://165.4.12.13:443/WFSMailExportService";

  private String WS_WFS_MultimediaSOAPWSDDServiceName = "WS_WFS_MultimediaSOAP";

  private HashSet ports = null;

  public WS_WFS_Multimedia_ServiceLocator()
  {
  }

  public WS_WFS_Multimedia_ServiceLocator(EngineConfiguration config)
  {
    super(config);
  }

  public WS_WFS_Multimedia_ServiceLocator(String wsdlLoc, QName sName) throws ServiceException {
    super(wsdlLoc, sName);
  }

  public String getWS_WFS_MultimediaSOAPAddress()
  {
    return this.WS_WFS_MultimediaSOAP_address;
  }

  public String getWS_WFS_MultimediaSOAPWSDDServiceName()
  {
    return this.WS_WFS_MultimediaSOAPWSDDServiceName;
  }

  public void setWS_WFS_MultimediaSOAPWSDDServiceName(String name) {
    this.WS_WFS_MultimediaSOAPWSDDServiceName = name;
  }

  public WS_WFS_Multimedia_PortType getWS_WFS_MultimediaSOAP() throws ServiceException
  {
	URL endpoint;
    try {
      endpoint = new URL(this.WS_WFS_MultimediaSOAP_address);
    }
    catch (MalformedURLException e)
    {      
      throw new ServiceException(e);
    }
    return getWS_WFS_MultimediaSOAP(endpoint);
  }

  public WS_WFS_Multimedia_PortType getWS_WFS_MultimediaSOAP(URL portAddress) throws ServiceException {
    try {
      WS_WFS_MultimediaSOAPStub _stub = new WS_WFS_MultimediaSOAPStub(portAddress, this);
      _stub.setTimeout(WS_WFS_MultimediaSOAPStub._requestTimeoutMilliseconds);
      _stub.setPortName(getWS_WFS_MultimediaSOAPWSDDServiceName());
      return _stub;
    } catch (AxisFault e) {
    	
    }
    return null;
  }

  public void setWS_WFS_MultimediaSOAPEndpointAddress(String address)
  {
    this.WS_WFS_MultimediaSOAP_address = address;
  }

  public Remote getPort(Class serviceEndpointInterface)
    throws ServiceException
  {
    try
    {
      if (WS_WFS_Multimedia_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
        WS_WFS_MultimediaSOAPStub _stub = new WS_WFS_MultimediaSOAPStub(new URL(this.WS_WFS_MultimediaSOAP_address), this);
        _stub.setPortName(getWS_WFS_MultimediaSOAPWSDDServiceName());
        return _stub;
      }
    }
    catch (Throwable t) {
      throw new ServiceException(t);
    }
    throw new ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
  }

  public Remote getPort(QName portName, Class serviceEndpointInterface)
    throws ServiceException
  {
    if (portName == null) {
      return getPort(serviceEndpointInterface);
    }
    String inputPortName = portName.getLocalPart();
    if ("WS_WFS_MultimediaSOAP".equals(inputPortName)) {
      return getWS_WFS_MultimediaSOAP();
    }

    Remote _stub = getPort(serviceEndpointInterface);
    ((Stub)_stub).setPortName(portName);
    return _stub;
  }

  public QName getServiceName()
  {
    return new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "WS_WFS_Multimedia");
  }

  public Iterator getPorts()
  {
    if (this.ports == null) {
      this.ports = new HashSet();
      this.ports.add(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "WS_WFS_MultimediaSOAP"));
    }
    return this.ports.iterator();
  }

  public void setEndpointAddress(String portName, String address)
    throws ServiceException
  {
    if ("WS_WFS_MultimediaSOAP".equals(portName)) {
      setWS_WFS_MultimediaSOAPEndpointAddress(address);
    }
    else
    {
      throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
    }
  }

  public void setEndpointAddress(QName portName, String address)
    throws ServiceException
  {
    setEndpointAddress(portName.getLocalPart(), address);
  }
}