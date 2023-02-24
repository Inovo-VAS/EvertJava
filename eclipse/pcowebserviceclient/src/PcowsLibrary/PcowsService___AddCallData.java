/**
 * PcowsService___AddCallData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package PcowsLibrary;

public class PcowsService___AddCallData  implements java.io.Serializable {
    private int AServiceId;

    private int ACallId;

    private java.lang.String AKey;

    private java.lang.String AValue;

    public PcowsService___AddCallData() {
    }

    public PcowsService___AddCallData(
           int AServiceId,
           int ACallId,
           java.lang.String AKey,
           java.lang.String AValue) {
           this.AServiceId = AServiceId;
           this.ACallId = ACallId;
           this.AKey = AKey;
           this.AValue = AValue;
    }


    /**
     * Gets the AServiceId value for this PcowsService___AddCallData.
     * 
     * @return AServiceId
     */
    public int getAServiceId() {
        return AServiceId;
    }


    /**
     * Sets the AServiceId value for this PcowsService___AddCallData.
     * 
     * @param AServiceId
     */
    public void setAServiceId(int AServiceId) {
        this.AServiceId = AServiceId;
    }


    /**
     * Gets the ACallId value for this PcowsService___AddCallData.
     * 
     * @return ACallId
     */
    public int getACallId() {
        return ACallId;
    }


    /**
     * Sets the ACallId value for this PcowsService___AddCallData.
     * 
     * @param ACallId
     */
    public void setACallId(int ACallId) {
        this.ACallId = ACallId;
    }


    /**
     * Gets the AKey value for this PcowsService___AddCallData.
     * 
     * @return AKey
     */
    public java.lang.String getAKey() {
        return AKey;
    }


    /**
     * Sets the AKey value for this PcowsService___AddCallData.
     * 
     * @param AKey
     */
    public void setAKey(java.lang.String AKey) {
        this.AKey = AKey;
    }


    /**
     * Gets the AValue value for this PcowsService___AddCallData.
     * 
     * @return AValue
     */
    public java.lang.String getAValue() {
        return AValue;
    }


    /**
     * Sets the AValue value for this PcowsService___AddCallData.
     * 
     * @param AValue
     */
    public void setAValue(java.lang.String AValue) {
        this.AValue = AValue;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PcowsService___AddCallData)) return false;
        PcowsService___AddCallData other = (PcowsService___AddCallData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.AServiceId == other.getAServiceId() &&
            this.ACallId == other.getACallId() &&
            ((this.AKey==null && other.getAKey()==null) || 
             (this.AKey!=null &&
              this.AKey.equals(other.getAKey()))) &&
            ((this.AValue==null && other.getAValue()==null) || 
             (this.AValue!=null &&
              this.AValue.equals(other.getAValue())));
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
        _hashCode += getACallId();
        if (getAKey() != null) {
            _hashCode += getAKey().hashCode();
        }
        if (getAValue() != null) {
            _hashCode += getAValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PcowsService___AddCallData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___AddCallData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AServiceId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "AServiceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ACallId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "ACallId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AKey");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "AKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AValue");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "AValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
