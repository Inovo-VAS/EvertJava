/**
 * PcowsService___UnloadOutboundRecord.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package PcowsLibrary;

public class PcowsService___UnloadOutboundRecord  implements java.io.Serializable {
    private int AServiceId;

    private int ALoadId;

    private int ASourceId;

    public PcowsService___UnloadOutboundRecord() {
    }

    public PcowsService___UnloadOutboundRecord(
           int AServiceId,
           int ALoadId,
           int ASourceId) {
           this.AServiceId = AServiceId;
           this.ALoadId = ALoadId;
           this.ASourceId = ASourceId;
    }


    /**
     * Gets the AServiceId value for this PcowsService___UnloadOutboundRecord.
     * 
     * @return AServiceId
     */
    public int getAServiceId() {
        return AServiceId;
    }


    /**
     * Sets the AServiceId value for this PcowsService___UnloadOutboundRecord.
     * 
     * @param AServiceId
     */
    public void setAServiceId(int AServiceId) {
        this.AServiceId = AServiceId;
    }


    /**
     * Gets the ALoadId value for this PcowsService___UnloadOutboundRecord.
     * 
     * @return ALoadId
     */
    public int getALoadId() {
        return ALoadId;
    }


    /**
     * Sets the ALoadId value for this PcowsService___UnloadOutboundRecord.
     * 
     * @param ALoadId
     */
    public void setALoadId(int ALoadId) {
        this.ALoadId = ALoadId;
    }


    /**
     * Gets the ASourceId value for this PcowsService___UnloadOutboundRecord.
     * 
     * @return ASourceId
     */
    public int getASourceId() {
        return ASourceId;
    }


    /**
     * Sets the ASourceId value for this PcowsService___UnloadOutboundRecord.
     * 
     * @param ASourceId
     */
    public void setASourceId(int ASourceId) {
        this.ASourceId = ASourceId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PcowsService___UnloadOutboundRecord)) return false;
        PcowsService___UnloadOutboundRecord other = (PcowsService___UnloadOutboundRecord) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.AServiceId == other.getAServiceId() &&
            this.ALoadId == other.getALoadId() &&
            this.ASourceId == other.getASourceId();
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
        _hashCode += getALoadId();
        _hashCode += getASourceId();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PcowsService___UnloadOutboundRecord.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___UnloadOutboundRecord"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AServiceId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "AServiceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ALoadId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "ALoadId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ASourceId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "ASourceId"));
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
