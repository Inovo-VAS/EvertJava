/**
 * PcowsService___ReloadOutboundQueues.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package PcowsLibrary;

public class PcowsService___ReloadOutboundQueues  implements java.io.Serializable {
    private int AServiceId;

    public PcowsService___ReloadOutboundQueues() {
    }

    public PcowsService___ReloadOutboundQueues(
           int AServiceId) {
           this.AServiceId = AServiceId;
    }


    /**
     * Gets the AServiceId value for this PcowsService___ReloadOutboundQueues.
     * 
     * @return AServiceId
     */
    public int getAServiceId() {
        return AServiceId;
    }


    /**
     * Sets the AServiceId value for this PcowsService___ReloadOutboundQueues.
     * 
     * @param AServiceId
     */
    public void setAServiceId(int AServiceId) {
        this.AServiceId = AServiceId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PcowsService___ReloadOutboundQueues)) return false;
        PcowsService___ReloadOutboundQueues other = (PcowsService___ReloadOutboundQueues) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.AServiceId == other.getAServiceId();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += getAServiceId();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PcowsService___ReloadOutboundQueues.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___ReloadOutboundQueues"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AServiceId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "AServiceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
