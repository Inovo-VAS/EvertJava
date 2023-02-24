/**
 * PcowsService___RequestOutboundACDCall3.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package PcowsLibrary;

public class PcowsService___RequestOutboundACDCall3  implements java.io.Serializable {
    private long loginId;

    private int serviceId;

    private int sourceId;

    private java.lang.String phone;

    private java.lang.String name;

    private java.util.Calendar scheduleDate;

    private java.lang.String comments;

    public PcowsService___RequestOutboundACDCall3() {
    }

    public PcowsService___RequestOutboundACDCall3(
           long loginId,
           int serviceId,
           int sourceId,
           java.lang.String phone,
           java.lang.String name,
           java.util.Calendar scheduleDate,
           java.lang.String comments) {
           this.loginId = loginId;
           this.serviceId = serviceId;
           this.sourceId = sourceId;
           this.phone = phone;
           this.name = name;
           this.scheduleDate = scheduleDate;
           this.comments = comments;
    }


    /**
     * Gets the loginId value for this PcowsService___RequestOutboundACDCall3.
     * 
     * @return loginId
     */
    public long getLoginId() {
        return loginId;
    }


    /**
     * Sets the loginId value for this PcowsService___RequestOutboundACDCall3.
     * 
     * @param loginId
     */
    public void setLoginId(long loginId) {
        this.loginId = loginId;
    }


    /**
     * Gets the serviceId value for this PcowsService___RequestOutboundACDCall3.
     * 
     * @return serviceId
     */
    public int getServiceId() {
        return serviceId;
    }


    /**
     * Sets the serviceId value for this PcowsService___RequestOutboundACDCall3.
     * 
     * @param serviceId
     */
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }


    /**
     * Gets the sourceId value for this PcowsService___RequestOutboundACDCall3.
     * 
     * @return sourceId
     */
    public int getSourceId() {
        return sourceId;
    }


    /**
     * Sets the sourceId value for this PcowsService___RequestOutboundACDCall3.
     * 
     * @param sourceId
     */
    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }


    /**
     * Gets the phone value for this PcowsService___RequestOutboundACDCall3.
     * 
     * @return phone
     */
    public java.lang.String getPhone() {
        return phone;
    }


    /**
     * Sets the phone value for this PcowsService___RequestOutboundACDCall3.
     * 
     * @param phone
     */
    public void setPhone(java.lang.String phone) {
        this.phone = phone;
    }


    /**
     * Gets the name value for this PcowsService___RequestOutboundACDCall3.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this PcowsService___RequestOutboundACDCall3.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the scheduleDate value for this PcowsService___RequestOutboundACDCall3.
     * 
     * @return scheduleDate
     */
    public java.util.Calendar getScheduleDate() {
        return scheduleDate;
    }


    /**
     * Sets the scheduleDate value for this PcowsService___RequestOutboundACDCall3.
     * 
     * @param scheduleDate
     */
    public void setScheduleDate(java.util.Calendar scheduleDate) {
        this.scheduleDate = scheduleDate;
    }


    /**
     * Gets the comments value for this PcowsService___RequestOutboundACDCall3.
     * 
     * @return comments
     */
    public java.lang.String getComments() {
        return comments;
    }


    /**
     * Sets the comments value for this PcowsService___RequestOutboundACDCall3.
     * 
     * @param comments
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PcowsService___RequestOutboundACDCall3)) return false;
        PcowsService___RequestOutboundACDCall3 other = (PcowsService___RequestOutboundACDCall3) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.loginId == other.getLoginId() &&
            this.serviceId == other.getServiceId() &&
            this.sourceId == other.getSourceId() &&
            ((this.phone==null && other.getPhone()==null) || 
             (this.phone!=null &&
              this.phone.equals(other.getPhone()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.scheduleDate==null && other.getScheduleDate()==null) || 
             (this.scheduleDate!=null &&
              this.scheduleDate.equals(other.getScheduleDate()))) &&
            ((this.comments==null && other.getComments()==null) || 
             (this.comments!=null &&
              this.comments.equals(other.getComments())));
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
        _hashCode += new Long(getLoginId()).hashCode();
        _hashCode += getServiceId();
        _hashCode += getSourceId();
        if (getPhone() != null) {
            _hashCode += getPhone().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getScheduleDate() != null) {
            _hashCode += getScheduleDate().hashCode();
        }
        if (getComments() != null) {
            _hashCode += getComments().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PcowsService___RequestOutboundACDCall3.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RequestOutboundACDCall3"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "LoginId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "ServiceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "SourceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("phone");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "Phone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduleDate");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "ScheduleDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comments");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "Comments"));
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
