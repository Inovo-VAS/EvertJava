/**
 * TelephonyService2Locator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public class TelephonyService2Locator extends org.apache.axis.client.Service implements org.tempuri.TelephonyService2 {

    public TelephonyService2Locator(java.lang.String...BasicHttpBinding_ITelephonyService_address) {
    	if(BasicHttpBinding_ITelephonyService_address!=null&&BasicHttpBinding_ITelephonyService_address.length==1){
    		this.BasicHttpBinding_ITelephonyService_address=BasicHttpBinding_ITelephonyService_address[0];
    	}
    }

    public TelephonyService2Locator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TelephonyService2Locator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BasicHttpBinding_ITelephonyService
    private java.lang.String BasicHttpBinding_ITelephonyService_address = "http://172.25.2.250:12345/TelephonyApiUAT/TelephonyService.svc";

    public java.lang.String getBasicHttpBinding_ITelephonyServiceAddress() {
        return BasicHttpBinding_ITelephonyService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BasicHttpBinding_ITelephonyServiceWSDDServiceName = "BasicHttpBinding_ITelephonyService";

    public java.lang.String getBasicHttpBinding_ITelephonyServiceWSDDServiceName() {
        return BasicHttpBinding_ITelephonyServiceWSDDServiceName;
    }

    public void setBasicHttpBinding_ITelephonyServiceWSDDServiceName(java.lang.String name) {
        BasicHttpBinding_ITelephonyServiceWSDDServiceName = name;
    }

    public org.tempuri.ITelephonyService getBasicHttpBinding_ITelephonyService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BasicHttpBinding_ITelephonyService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBasicHttpBinding_ITelephonyService(endpoint);
    }

    public org.tempuri.ITelephonyService getBasicHttpBinding_ITelephonyService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.BasicHttpBinding_ITelephonyServiceStub _stub = new org.tempuri.BasicHttpBinding_ITelephonyServiceStub(portAddress, this);
            _stub.setPortName(getBasicHttpBinding_ITelephonyServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBasicHttpBinding_ITelephonyServiceEndpointAddress(java.lang.String address) {
        BasicHttpBinding_ITelephonyService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.tempuri.ITelephonyService.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.BasicHttpBinding_ITelephonyServiceStub _stub = new org.tempuri.BasicHttpBinding_ITelephonyServiceStub(new java.net.URL(BasicHttpBinding_ITelephonyService_address), this);
                _stub.setPortName(getBasicHttpBinding_ITelephonyServiceWSDDServiceName());
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
        if ("BasicHttpBinding_ITelephonyService".equals(inputPortName)) {
            return getBasicHttpBinding_ITelephonyService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "TelephonyService2");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "BasicHttpBinding_ITelephonyService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("BasicHttpBinding_ITelephonyService".equals(portName)) {
            setBasicHttpBinding_ITelephonyServiceEndpointAddress(address);
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
