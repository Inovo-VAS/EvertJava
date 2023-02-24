/**
 * PcowsService___InsertOutboundRecord.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package PcowsLibrary;

public class PcowsService___InsertOutboundRecord  implements java.io.Serializable {
    private int AServiceId;

    private int ALoadId;

    private int ASourceId;

    private java.lang.String AName;

    private java.lang.String APhone;

    private int AStatus;

    private java.util.Calendar AScheduleDate;

    private int ACapturingAgent;

    private int APriority;

    private java.lang.String AComments;

    public PcowsService___InsertOutboundRecord() {
    }

    public PcowsService___InsertOutboundRecord(
           int AServiceId,
           int ALoadId,
           int ASourceId,
           java.lang.String AName,
           java.lang.String APhone,
           int AStatus,
           java.util.Calendar AScheduleDate,
           int ACapturingAgent,
           int APriority,
           java.lang.String AComments) {
           this.AServiceId = AServiceId;
           this.ALoadId = ALoadId;
           this.ASourceId = ASourceId;
           this.AName = AName;
           this.APhone = APhone;
           this.AStatus = AStatus;
           this.AScheduleDate = AScheduleDate;
           this.ACapturingAgent = ACapturingAgent;
           this.APriority = APriority;
           this.AComments = AComments;
    }


    /**
     * Gets the AServiceId value for this PcowsService___InsertOutboundRecord.
     * 
     * @return AServiceId
     */
    public int getAServiceId() {
        return AServiceId;
    }


    /**
     * Sets the AServiceId value for this PcowsService___InsertOutboundRecord.
     * 
     * @param AServiceId
     */
    public void setAServiceId(int AServiceId) {
        this.AServiceId = AServiceId;
    }


    /**
     * Gets the ALoadId value for this PcowsService___InsertOutboundRecord.
     * 
     * @return ALoadId
     */
    public int getALoadId() {
        return ALoadId;
    }


    /**
     * Sets the ALoadId value for this PcowsService___InsertOutboundRecord.
     * 
     * @param ALoadId
     */
    public void setALoadId(int ALoadId) {
        this.ALoadId = ALoadId;
    }


    /**
     * Gets the ASourceId value for this PcowsService___InsertOutboundRecord.
     * 
     * @return ASourceId
     */
    public int getASourceId() {
        return ASourceId;
    }


    /**
     * Sets the ASourceId value for this PcowsService___InsertOutboundRecord.
     * 
     * @param ASourceId
     */
    public void setASourceId(int ASourceId) {
        this.ASourceId = ASourceId;
    }


    /**
     * Gets the AName value for this PcowsService___InsertOutboundRecord.
     * 
     * @return AName
     */
    public java.lang.String getAName() {
        return AName;
    }


    /**
     * Sets the AName value for this PcowsService___InsertOutboundRecord.
     * 
     * @param AName
     */
    public void setAName(java.lang.String AName) {
        this.AName = AName;
    }


    /**
     * Gets the APhone value for this PcowsService___InsertOutboundRecord.
     * 
     * @return APhone
     */
    public java.lang.String getAPhone() {
        return APhone;
    }


    /**
     * Sets the APhone value for this PcowsService___InsertOutboundRecord.
     * 
     * @param APhone
     */
    public void setAPhone(java.lang.String APhone) {
        this.APhone = APhone;
    }


    /**
     * Gets the AStatus value for this PcowsService___InsertOutboundRecord.
     * 
     * @return AStatus
     */
    public int getAStatus() {
        return AStatus;
    }


    /**
     * Sets the AStatus value for this PcowsService___InsertOutboundRecord.
     * 
     * @param AStatus
     */
    public void setAStatus(int AStatus) {
        this.AStatus = AStatus;
    }


    /**
     * Gets the AScheduleDate value for this PcowsService___InsertOutboundRecord.
     * 
     * @return AScheduleDate
     */
    public java.util.Calendar getAScheduleDate() {
        return AScheduleDate;
    }


    /**
     * Sets the AScheduleDate value for this PcowsService___InsertOutboundRecord.
     * 
     * @param AScheduleDate
     */
    public void setAScheduleDate(java.util.Calendar AScheduleDate) {
        this.AScheduleDate = AScheduleDate;
    }


    /**
     * Gets the ACapturingAgent value for this PcowsService___InsertOutboundRecord.
     * 
     * @return ACapturingAgent
     */
    public int getACapturingAgent() {
        return ACapturingAgent;
    }


    /**
     * Sets the ACapturingAgent value for this PcowsService___InsertOutboundRecord.
     * 
     * @param ACapturingAgent
     */
    public void setACapturingAgent(int ACapturingAgent) {
        this.ACapturingAgent = ACapturingAgent;
    }


    /**
     * Gets the APriority value for this PcowsService___InsertOutboundRecord.
     * 
     * @return APriority
     */
    public int getAPriority() {
        return APriority;
    }


    /**
     * Sets the APriority value for this PcowsService___InsertOutboundRecord.
     * 
     * @param APriority
     */
    public void setAPriority(int APriority) {
        this.APriority = APriority;
    }


    /**
     * Gets the AComments value for this PcowsService___InsertOutboundRecord.
     * 
     * @return AComments
     */
    public java.lang.String getAComments() {
        return AComments;
    }


    /**
     * Sets the AComments value for this PcowsService___InsertOutboundRecord.
     * 
     * @param AComments
     */
    public void setAComments(java.lang.String AComments) {
        this.AComments = AComments;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PcowsService___InsertOutboundRecord)) return false;
        PcowsService___InsertOutboundRecord other = (PcowsService___InsertOutboundRecord) obj;
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
            this.ASourceId == other.getASourceId() &&
            ((this.AName==null && other.getAName()==null) || 
             (this.AName!=null &&
              this.AName.equals(other.getAName()))) &&
            ((this.APhone==null && other.getAPhone()==null) || 
             (this.APhone!=null &&
              this.APhone.equals(other.getAPhone()))) &&
            this.AStatus == other.getAStatus() &&
            ((this.AScheduleDate==null && other.getAScheduleDate()==null) || 
             (this.AScheduleDate!=null &&
              this.AScheduleDate.equals(other.getAScheduleDate()))) &&
            this.ACapturingAgent == other.getACapturingAgent() &&
            this.APriority == other.getAPriority() &&
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
        _hashCode += getAServiceId();
        _hashCode += getALoadId();
        _hashCode += getASourceId();
        if (getAName() != null) {
            _hashCode += getAName().hashCode();
        }
        if (getAPhone() != null) {
            _hashCode += getAPhone().hashCode();
        }
        _hashCode += getAStatus();
        if (getAScheduleDate() != null) {
            _hashCode += getAScheduleDate().hashCode();
        }
        _hashCode += getACapturingAgent();
        _hashCode += getAPriority();
        if (getAComments() != null) {
            _hashCode += getAComments().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PcowsService___InsertOutboundRecord.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecord"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AName");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "AName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("APhone");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "APhone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "AStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AScheduleDate");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "AScheduleDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ACapturingAgent");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "ACapturingAgent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("APriority");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "APriority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
