/**
 * PcowsService___RequestOutboundACDCall.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package PcowsLibrary;

public class PcowsService___RequestOutboundACDCall  implements java.io.Serializable {
    private int ALoginId;

    private int AServiceId;

    private java.lang.String APhone;

    private java.lang.String AName;

    private java.util.Calendar AScheduleDate;

    private java.lang.String AComments;

    public PcowsService___RequestOutboundACDCall() {
    }

    public PcowsService___RequestOutboundACDCall(
           int ALoginId,
           int AServiceId,
           java.lang.String APhone,
           java.lang.String AName,
           java.util.Calendar AScheduleDate,
           java.lang.String AComments) {
           this.ALoginId = ALoginId;
           this.AServiceId = AServiceId;
           this.APhone = APhone;
           this.AName = AName;
           this.AScheduleDate = AScheduleDate;
           this.AComments = AComments;
    }


    /**
     * Gets the ALoginId value for this PcowsService___RequestOutboundACDCall.
     * 
     * @return ALoginId
     */
    public int getALoginId() {
        return ALoginId;
    }


    /**
     * Sets the ALoginId value for this PcowsService___RequestOutboundACDCall.
     * 
     * @param ALoginId
     */
    public void setALoginId(int ALoginId) {
        this.ALoginId = ALoginId;
    }


    /**
     * Gets the AServiceId value for this PcowsService___RequestOutboundACDCall.
     * 
     * @return AServiceId
     */
    public int getAServiceId() {
        return AServiceId;
    }


    /**
     * Sets the AServiceId value for this PcowsService___RequestOutboundACDCall.
     * 
     * @param AServiceId
     */
    public void setAServiceId(int AServiceId) {
        this.AServiceId = AServiceId;
    }


    /**
     * Gets the APhone value for this PcowsService___RequestOutboundACDCall.
     * 
     * @return APhone
     */
    public java.lang.String getAPhone() {
        return APhone;
    }


    /**
     * Sets the APhone value for this PcowsService___RequestOutboundACDCall.
     * 
     * @param APhone
     */
    public void setAPhone(java.lang.String APhone) {
        this.APhone = APhone;
    }


    /**
     * Gets the AName value for this PcowsService___RequestOutboundACDCall.
     * 
     * @return AName
     */
    public java.lang.String getAName() {
        return AName;
    }


    /**
     * Sets the AName value for this PcowsService___RequestOutboundACDCall.
     * 
     * @param AName
     */
    public void setAName(java.lang.String AName) {
        this.AName = AName;
    }


    /**
     * Gets the AScheduleDate value for this PcowsService___RequestOutboundACDCall.
     * 
     * @return AScheduleDate
     */
    public java.util.Calendar getAScheduleDate() {
        return AScheduleDate;
    }


    /**
     * Sets the AScheduleDate value for this PcowsService___RequestOutboundACDCall.
     * 
     * @param AScheduleDate
     */
    public void setAScheduleDate(java.util.Calendar AScheduleDate) {
        this.AScheduleDate = AScheduleDate;
    }


    /**
     * Gets the AComments value for this PcowsService___RequestOutboundACDCall.
     * 
     * @return AComments
     */
    public java.lang.String getAComments() {
        return AComments;
    }


    /**
     * Sets the AComments value for this PcowsService___RequestOutboundACDCall.
     * 
     * @param AComments
     */
    public void setAComments(java.lang.String AComments) {
        this.AComments = AComments;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PcowsService___RequestOutboundACDCall)) return false;
        PcowsService___RequestOutboundACDCall other = (PcowsService___RequestOutboundACDCall) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.ALoginId == other.getALoginId() &&
            this.AServiceId == other.getAServiceId() &&
            ((this.APhone==null && other.getAPhone()==null) || 
             (this.APhone!=null &&
              this.APhone.equals(other.getAPhone()))) &&
            ((this.AName==null && other.getAName()==null) || 
             (this.AName!=null &&
              this.AName.equals(other.getAName()))) &&
            ((this.AScheduleDate==null && other.getAScheduleDate()==null) || 
             (this.AScheduleDate!=null &&
              this.AScheduleDate.equals(other.getAScheduleDate()))) &&
            ((this.AComments==null && other.getAComments()==null) || 
             (this.AComments!=null &&
              this.AComments.equals(other.getAComments())));
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
        _hashCode += getALoginId();
        _hashCode += getAServiceId();
        if (getAPhone() != null) {
            _hashCode += getAPhone().hashCode();
        }
        if (getAName() != null) {
            _hashCode += getAName().hashCode();
        }
        if (getAScheduleDate() != null) {
            _hashCode += getAScheduleDate().hashCode();
        }
        if (getAComments() != null) {
            _hashCode += getAComments().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PcowsService___RequestOutboundACDCall.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RequestOutboundACDCall"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ALoginId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "ALoginId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AServiceId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "AServiceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("APhone");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "APhone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AName");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "AName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AScheduleDate");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "AScheduleDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AComments");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "AComments"));
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
