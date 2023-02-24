package za.co.woolworths.Indibano.WS_WFS_Multimedia;

import java.io.Serializable;
import java.util.Date;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class MailExportRequestMetadata
  implements Serializable
{
  private String channel;
  private String contactId;
  private String customerIDNumber;
  private String emailAddress;
  private Date emailDate;
  private String emailSubjectLine;
  private String documentType;
  private int attachmentCount;
  private Object __equalsCalc = null;

  private boolean __hashCodeCalc = false;

  private static TypeDesc typeDesc = new TypeDesc(MailExportRequestMetadata.class, true);

  static {
    typeDesc.setXmlType(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "MailExportRequestMetadata"));
    ElementDesc elemField = new ElementDesc();
    elemField.setFieldName("channel");
    elemField.setXmlName(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "Channel"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("contactId");
    elemField.setXmlName(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "ContactId"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("customerIDNumber");
    elemField.setXmlName(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "CustomerIDNumber"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("emailAddress");
    elemField.setXmlName(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "EmailAddress"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("emailDate");
    elemField.setXmlName(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "EmailDate"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "date"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("emailSubjectLine");
    elemField.setXmlName(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "EmailSubjectLine"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("documentType");
    elemField.setXmlName(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "DocumentType"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("attachmentCount");
    elemField.setXmlName(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "AttachmentCount"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
  }

  public MailExportRequestMetadata()
  {
  }

  public MailExportRequestMetadata(String channel, String contactId, String customerIDNumber, String emailAddress, Date emailDate, String emailSubjectLine, String documentType, int attachmentCount)
  {
    this.channel = channel;
    this.contactId = contactId;
    this.customerIDNumber = customerIDNumber;
    this.emailAddress = emailAddress;
    this.emailDate = emailDate;
    this.emailSubjectLine = emailSubjectLine;
    this.documentType = documentType;
    this.attachmentCount = attachmentCount;
  }

  public String getChannel()
  {
    return this.channel;
  }

  public void setChannel(String channel)
  {
    this.channel = channel;
  }

  public String getContactId()
  {
    return this.contactId;
  }

  public void setContactId(String contactId)
  {
    this.contactId = contactId;
  }

  public String getCustomerIDNumber()
  {
    return this.customerIDNumber;
  }

  public void setCustomerIDNumber(String customerIDNumber)
  {
    this.customerIDNumber = customerIDNumber;
  }

  public String getEmailAddress()
  {
    return this.emailAddress;
  }

  public void setEmailAddress(String emailAddress)
  {
    this.emailAddress = emailAddress;
  }

  public Date getEmailDate()
  {
    return this.emailDate;
  }

  public void setEmailDate(Date emailDate)
  {
    this.emailDate = emailDate;
  }

  public String getEmailSubjectLine()
  {
    return this.emailSubjectLine;
  }

  public void setEmailSubjectLine(String emailSubjectLine)
  {
    this.emailSubjectLine = emailSubjectLine;
  }

  public String getDocumentType()
  {
    return this.documentType;
  }

  public void setDocumentType(String documentType)
  {
    this.documentType = documentType;
  }

  public int getAttachmentCount()
  {
    return this.attachmentCount;
  }

  public void setAttachmentCount(int attachmentCount)
  {
    this.attachmentCount = attachmentCount;
  }

  public synchronized boolean equals(Object obj)
  {
    if (!(obj instanceof MailExportRequestMetadata)) return false;
    MailExportRequestMetadata other = (MailExportRequestMetadata)obj;
    if (obj == null) return false;
    if (this == obj) return true;
    if (this.__equalsCalc != null) {
      return this.__equalsCalc == obj;
    }
    this.__equalsCalc = obj;

    boolean _equals = 
      ((this.channel == null) && (other.getChannel() == null)) || (
      (this.channel != null) && 
      (this.channel.equals(other.getChannel())) && (
      ((this.contactId == null) && (other.getContactId() == null)) || (
      (this.contactId != null) && 
      (this.contactId.equals(other.getContactId())) && (
      ((this.customerIDNumber == null) && (other.getCustomerIDNumber() == null)) || (
      (this.customerIDNumber != null) && 
      (this.customerIDNumber.equals(other.getCustomerIDNumber())) && (
      ((this.emailAddress == null) && (other.getEmailAddress() == null)) || (
      (this.emailAddress != null) && 
      (this.emailAddress.equals(other.getEmailAddress())) && (
      ((this.emailDate == null) && (other.getEmailDate() == null)) || (
      (this.emailDate != null) && 
      (this.emailDate.equals(other.getEmailDate())) && (
      ((this.emailSubjectLine == null) && (other.getEmailSubjectLine() == null)) || (
      (this.emailSubjectLine != null) && 
      (this.emailSubjectLine.equals(other.getEmailSubjectLine())) && (
      ((this.documentType == null) && (other.getDocumentType() == null)) || (
      (this.documentType != null) && 
      (this.documentType.equals(other.getDocumentType())) && 
      (this.attachmentCount == other.getAttachmentCount()))))))))))))));
    this.__equalsCalc = null;
    return _equals;
  }

  public synchronized int hashCode()
  {
    if (this.__hashCodeCalc) {
      return 0;
    }
    this.__hashCodeCalc = true;
    int _hashCode = 1;
    if (getChannel() != null) {
      _hashCode += getChannel().hashCode();
    }
    if (getContactId() != null) {
      _hashCode += getContactId().hashCode();
    }
    if (getCustomerIDNumber() != null) {
      _hashCode += getCustomerIDNumber().hashCode();
    }
    if (getEmailAddress() != null) {
      _hashCode += getEmailAddress().hashCode();
    }
    if (getEmailDate() != null) {
      _hashCode += getEmailDate().hashCode();
    }
    if (getEmailSubjectLine() != null) {
      _hashCode += getEmailSubjectLine().hashCode();
    }
    if (getDocumentType() != null) {
      _hashCode += getDocumentType().hashCode();
    }
    _hashCode += getAttachmentCount();
    this.__hashCodeCalc = false;
    return _hashCode;
  }

  public static TypeDesc getTypeDesc()
  {
    return typeDesc;
  }

  public static Serializer getSerializer(String mechType, Class _javaType, QName _xmlType)
  {
    return 
      new BeanSerializer(
      _javaType, _xmlType, typeDesc);
  }

  public static Deserializer getDeserializer(String mechType, Class _javaType, QName _xmlType)
  {
    return 
      new BeanDeserializer(
      _javaType, _xmlType, typeDesc);
  }
}