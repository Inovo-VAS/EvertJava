/**
 * PcowsService___InsertOutboundRecord3.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package PcowsLibrary;

public class PcowsService___InsertOutboundRecord3  implements java.io.Serializable {
    private int serviceId;

    private int loadId;

    private int sourceId;

    private java.lang.String name;

    private java.lang.String timeZone;

    private int status;

    private java.lang.String phone;

    private java.lang.String phoneTimeZone;

    private java.lang.String alternativePhones;

    private java.lang.String alternativePhoneDescriptions;

    private java.lang.String alternativePhoneTimeZones;

    private java.util.Calendar scheduleDate;

    private long capturingAgent;

    private int priority;

    private java.lang.String comments;

    private boolean automaticTimeZoneDetection;

    public PcowsService___InsertOutboundRecord3() {
    }

    public PcowsService___InsertOutboundRecord3(
           int serviceId,
           int loadId,
           int sourceId,
           java.lang.String name,
           java.lang.String timeZone,
           int status,
           java.lang.String phone,
           java.lang.String phoneTimeZone,
           java.lang.String alternativePhones,
           java.lang.String alternativePhoneDescriptions,
           java.lang.String alternativePhoneTimeZones,
           java.util.Calendar scheduleDate,
           long capturingAgent,
           int priority,
           java.lang.String comments,
           boolean automaticTimeZoneDetection) {
           this.serviceId = serviceId;
           this.loadId = loadId;
           this.sourceId = sourceId;
           this.name = name;
           this.timeZone = timeZone;
           this.status = status;
           this.phone = phone;
           this.phoneTimeZone = phoneTimeZone;
           this.alternativePhones = alternativePhones;
           this.alternativePhoneDescriptions = alternativePhoneDescriptions;
           this.alternativePhoneTimeZones = alternativePhoneTimeZones;
           this.scheduleDate = scheduleDate;
           this.capturingAgent = capturingAgent;
           this.priority = priority;
           this.comments = comments;
           this.automaticTimeZoneDetection = automaticTimeZoneDetection;
    }


    /**
     * Gets the serviceId value for this PcowsService___InsertOutboundRecord3.
     * 
     * @return serviceId
     */
    public int getServiceId() {
        return serviceId;
    }


    /**
     * Sets the serviceId value for this PcowsService___InsertOutboundRecord3.
     * 
     * @param serviceId
     */
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }


    /**
     * Gets the loadId value for this PcowsService___InsertOutboundRecord3.
     * 
     * @return loadId
     */
    public int getLoadId() {
        return loadId;
    }


    /**
     * Sets the loadId value for this PcowsService___InsertOutboundRecord3.
     * 
     * @param loadId
     */
    public void setLoadId(int loadId) {
        this.loadId = loadId;
    }


    /**
     * Gets the sourceId value for this PcowsService___InsertOutboundRecord3.
     * 
     * @return sourceId
     */
    public int getSourceId() {
        return sourceId;
    }


    /**
     * Sets the sourceId value for this PcowsService___InsertOutboundRecord3.
     * 
     * @param sourceId
     */
    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }


    /**
     * Gets the name value for this PcowsService___InsertOutboundRecord3.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this PcowsService___InsertOutboundRecord3.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the timeZone value for this PcowsService___InsertOutboundRecord3.
     * 
     * @return timeZone
     */
    public java.lang.String getTimeZone() {
        return timeZone;
    }


    /**
     * Sets the timeZone value for this PcowsService___InsertOutboundRecord3.
     * 
     * @param timeZone
     */
    public void setTimeZone(java.lang.String timeZone) {
        this.timeZone = timeZone;
    }


    /**
     * Gets the status value for this PcowsService___InsertOutboundRecord3.
     * 
     * @return status
     */
    public int getStatus() {
        return status;
    }


    /**
     * Sets the status value for this PcowsService___InsertOutboundRecord3.
     * 
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }


    /**
     * Gets the phone value for this PcowsService___InsertOutboundRecord3.
     * 
     * @return phone
     */
    public java.lang.String getPhone() {
        return phone;
    }


    /**
     * Sets the phone value for this PcowsService___InsertOutboundRecord3.
     * 
     * @param phone
     */
    public void setPhone(java.lang.String phone) {
        this.phone = phone;
    }


    /**
     * Gets the phoneTimeZone value for this PcowsService___InsertOutboundRecord3.
     * 
     * @return phoneTimeZone
     */
    public java.lang.String getPhoneTimeZone() {
        return phoneTimeZone;
    }


    /**
     * Sets the phoneTimeZone value for this PcowsService___InsertOutboundRecord3.
     * 
     * @param phoneTimeZone
     */
    public void setPhoneTimeZone(java.lang.String phoneTimeZone) {
        this.phoneTimeZone = phoneTimeZone;
    }


    /**
     * Gets the alternativePhones value for this PcowsService___InsertOutboundRecord3.
     * 
     * @return alternativePhones
     */
    public java.lang.String getAlternativePhones() {
        return alternativePhones;
    }


    /**
     * Sets the alternativePhones value for this PcowsService___InsertOutboundRecord3.
     * 
     * @param alternativePhones
     */
    public void setAlternativePhones(java.lang.String alternativePhones) {
        this.alternativePhones = alternativePhones;
    }


    /**
     * Gets the alternativePhoneDescriptions value for this PcowsService___InsertOutboundRecord3.
     * 
     * @return alternativePhoneDescriptions
     */
    public java.lang.String getAlternativePhoneDescriptions() {
        return alternativePhoneDescriptions;
    }


    /**
     * Sets the alternativePhoneDescriptions value for this PcowsService___InsertOutboundRecord3.
     * 
     * @param alternativePhoneDescriptions
     */
    public void setAlternativePhoneDescriptions(java.lang.String alternativePhoneDescriptions) {
        this.alternativePhoneDescriptions = alternativePhoneDescriptions;
    }


    /**
     * Gets the alternativePhoneTimeZones value for this PcowsService___InsertOutboundRecord3.
     * 
     * @return alternativePhoneTimeZones
     */
    public java.lang.String getAlternativePhoneTimeZones() {
        return alternativePhoneTimeZones;
    }


    /**
     * Sets the alternativePhoneTimeZones value for this PcowsService___InsertOutboundRecord3.
     * 
     * @param alternativePhoneTimeZones
     */
    public void setAlternativePhoneTimeZones(java.lang.String alternativePhoneTimeZones) {
        this.alternativePhoneTimeZones = alternativePhoneTimeZones;
    }


    /**
     * Gets the scheduleDate value for this PcowsService___InsertOutboundRecord3.
     * 
     * @return scheduleDate
     */
    public java.util.Calendar getScheduleDate() {
        return scheduleDate;
    }


    /**
     * Sets the scheduleDate value for this PcowsService___InsertOutboundRecord3.
     * 
     * @param scheduleDate
     */
    public void setScheduleDate(java.util.Calendar scheduleDate) {
        this.scheduleDate = scheduleDate;
    }


    /**
     * Gets the capturingAgent value for this PcowsService___InsertOutboundRecord3.
     * 
     * @return capturingAgent
     */
    public long getCapturingAgent() {
        return capturingAgent;
    }


    /**
     * Sets the capturingAgent value for this PcowsService___InsertOutboundRecord3.
     * 
     * @param capturingAgent
     */
    public void setCapturingAgent(long capturingAgent) {
        this.capturingAgent = capturingAgent;
    }


    /**
     * Gets the priority value for this PcowsService___InsertOutboundRecord3.
     * 
     * @return priority
     */
    public int getPriority() {
        return priority;
    }


    /**
     * Sets the priority value for this PcowsService___InsertOutboundRecord3.
     * 
     * @param priority
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }


    /**
     * Gets the comments value for this PcowsService___InsertOutboundRecord3.
     * 
     * @return comments
     */
    public java.lang.String getComments() {
        return comments;
    }


    /**
     * Sets the comments value for this PcowsService___InsertOutboundRecord3.
     * 
     * @param comments
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }


    /**
     * Gets the automaticTimeZoneDetection value for this PcowsService___InsertOutboundRecord3.
     * 
     * @return automaticTimeZoneDetection
     */
    public boolean isAutomaticTimeZoneDetection() {
        return automaticTimeZoneDetection;
    }


    /**
     * Sets the automaticTimeZoneDetection value for this PcowsService___InsertOutboundRecord3.
     * 
     * @param automaticTimeZoneDetection
     */
    public void setAutomaticTimeZoneDetection(boolean automaticTimeZoneDetection) {
        this.automaticTimeZoneDetection = automaticTimeZoneDetection;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PcowsService___InsertOutboundRecord3)) return false;
        PcowsService___InsertOutboundRecord3 other = (PcowsService___InsertOutboundRecord3) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.serviceId == other.getServiceId() &&
            this.loadId == other.getLoadId() &&
            this.sourceId == other.getSourceId() &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.timeZone==null && other.getTimeZone()==null) || 
             (this.timeZone!=null &&
              this.timeZone.equals(other.getTimeZone()))) &&
            this.status == other.getStatus() &&
            ((this.phone==null && other.getPhone()==null) || 
             (this.phone!=null &&
              this.phone.equals(other.getPhone()))) &&
            ((this.phoneTimeZone==null && other.getPhoneTimeZone()==null) || 
             (this.phoneTimeZone!=null &&
              this.phoneTimeZone.equals(other.getPhoneTimeZone()))) &&
            ((this.alternativePhones==null && other.getAlternativePhones()==null) || 
             (this.alternativePhones!=null &&
              this.alternativePhones.equals(other.getAlternativePhones()))) &&
            ((this.alternativePhoneDescriptions==null && other.getAlternativePhoneDescriptions()==null) || 
             (this.alternativePhoneDescriptions!=null &&
              this.alternativePhoneDescriptions.equals(other.getAlternativePhoneDescriptions()))) &&
            ((this.alternativePhoneTimeZones==null && other.getAlternativePhoneTimeZones()==null) || 
             (this.alternativePhoneTimeZones!=null &&
              this.alternativePhoneTimeZones.equals(other.getAlternativePhoneTimeZones()))) &&
            ((this.scheduleDate==null && other.getScheduleDate()==null) || 
             (this.scheduleDate!=null &&
              this.scheduleDate.equals(other.getScheduleDate()))) &&
            this.capturingAgent == other.getCapturingAgent() &&
            this.priority == other.getPriority() &&
            ((this.comments==null && other.getComments()==null) || 
             (this.comments!=null &&
              this.comments.equals(other.getComments()))) &&
            this.automaticTimeZoneDetection == other.isAutomaticTimeZoneDetection();
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
        _hashCode += getServiceId();
        _hashCode += getLoadId();
        _hashCode += getSourceId();
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getTimeZone() != null) {
            _hashCode += getTimeZone().hashCode();
        }
        _hashCode += getStatus();
        if (getPhone() != null) {
            _hashCode += getPhone().hashCode();
        }
        if (getPhoneTimeZone() != null) {
            _hashCode += getPhoneTimeZone().hashCode();
        }
        if (getAlternativePhones() != null) {
            _hashCode += getAlternativePhones().hashCode();
        }
        if (getAlternativePhoneDescriptions() != null) {
            _hashCode += getAlternativePhoneDescriptions().hashCode();
        }
        if (getAlternativePhoneTimeZones() != null) {
            _hashCode += getAlternativePhoneTimeZones().hashCode();
        }
        if (getScheduleDate() != null) {
            _hashCode += getScheduleDate().hashCode();
        }
        _hashCode += new Long(getCapturingAgent()).hashCode();
        _hashCode += getPriority();
        if (getComments() != null) {
            _hashCode += getComments().hashCode();
        }
        _hashCode += (isAutomaticTimeZoneDetection() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PcowsService___InsertOutboundRecord3.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecord3"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "ServiceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loadId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "LoadId"));
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
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeZone");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "TimeZone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "Status"));
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
        elemField.setFieldName("phoneTimeZone");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PhoneTimeZone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("alternativePhones");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "AlternativePhones"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("alternativePhoneDescriptions");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "AlternativePhoneDescriptions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("alternativePhoneTimeZones");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "AlternativePhoneTimeZones"));
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
        elemField.setFieldName("capturingAgent");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "CapturingAgent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("priority");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "Priority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comments");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "Comments"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("automaticTimeZoneDetection");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:PcowsLibrary", "AutomaticTimeZoneDetection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
