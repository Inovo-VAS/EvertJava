/**
 * PcowsService_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package PcowsLibrary;

public class PcowsService_ServiceLocator extends org.apache.axis.client.Service implements PcowsLibrary.PcowsService_Service {

    public PcowsService_ServiceLocator() {
    }


    public PcowsService_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PcowsService_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PcowsServicePort
    private java.lang.String PcowsServicePort_address = "http://192.168.1.62/pcoserverws/pcoserverws.dll/SOAP?service=PcowsService";

    public java.lang.String getPcowsServicePortAddress() {
        return PcowsServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PcowsServicePortWSDDServiceName = "PcowsServicePort";

    public java.lang.String getPcowsServicePortWSDDServiceName() {
        return PcowsServicePortWSDDServiceName;
    }

    public void setPcowsServicePortWSDDServiceName(java.lang.String name) {
        PcowsServicePortWSDDServiceName = name;
    }

    public PcowsLibrary.PcowsService_PortType getPcowsServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PcowsServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPcowsServicePort(endpoint);
    }

    public PcowsLibrary.PcowsService_PortType getPcowsServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            PcowsLibrary.PcowsServiceBindingStub _stub = new PcowsLibrary.PcowsServiceBindingStub(portAddress, this);
            _stub.setPortName(getPcowsServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPcowsServicePortEndpointAddress(java.lang.String address) {
        PcowsServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (PcowsLibrary.PcowsService_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                PcowsLibrary.PcowsServiceBindingStub _stub = new PcowsLibrary.PcowsServiceBindingStub(new java.net.URL(PcowsServicePort_address), this);
                _stub.setPortName(getPcowsServicePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("PcowsServicePort".equals(inputPortName)) {
            return getPcowsServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("PcowsServicePort".equals(portName)) {
            setPcowsServicePortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
