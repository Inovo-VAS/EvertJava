package za.co.woolworths.Indibano.WS_WFS_Multimedia;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class MailExportRequestObjectin
  implements Serializable
{
  private MailExportRequestMetadata mailExportRequestMetadata;
  private EmailMsg emailMsg;
  private Attachment[] attachmentArray;
  private Object __equalsCalc = null;

  private boolean __hashCodeCalc = false;

  private static TypeDesc typeDesc = new TypeDesc(MailExportRequestObjectin.class, true);

  static {
    typeDesc.setXmlType(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "MailExportRequestObjectin"));
    ElementDesc elemField = new ElementDesc();
    elemField.setFieldName("mailExportRequestMetadata");
    elemField.setXmlName(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "MailExportRequestMetadata"));
    elemField.setXmlType(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "MailExportRequestMetadata"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("emailMsg");
    elemField.setXmlName(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "EmailMsg"));
    elemField.setXmlType(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "EmailMsg"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("attachmentArray");
    elemField.setXmlName(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "AttachmentArray"));
    elemField.setXmlType(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "Attachment"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    elemField.setItemQName(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "Attachment"));
    typeDesc.addFieldDesc(elemField);
  }

  public MailExportRequestObjectin()
  {
  }

  public MailExportRequestObjectin(MailExportRequestMetadata mailExportRequestMetadata, EmailMsg emailMsg, Attachment[] attachmentArray)
  {
    this.mailExportRequestMetadata = mailExportRequestMetadata;
    this.emailMsg = emailMsg;
    this.attachmentArray = attachmentArray;
  }

  public MailExportRequestMetadata getMailExportRequestMetadata()
  {
    return this.mailExportRequestMetadata;
  }

  public void setMailExportRequestMetadata(MailExportRequestMetadata mailExportRequestMetadata)
  {
    this.mailExportRequestMetadata = mailExportRequestMetadata;
  }

  public EmailMsg getEmailMsg()
  {
    return this.emailMsg;
  }

  public void setEmailMsg(EmailMsg emailMsg)
  {
    this.emailMsg = emailMsg;
  }

  public Attachment[] getAttachmentArray()
  {
    return this.attachmentArray;
  }

  public void setAttachmentArray(Attachment[] attachmentArray)
  {
    this.attachmentArray = attachmentArray;
  }

  public synchronized boolean equals(Object obj)
  {
    if (!(obj instanceof MailExportRequestObjectin)) return false;
    MailExportRequestObjectin other = (MailExportRequestObjectin)obj;
    if (obj == null) return false;
    if (this == obj) return true;
    if (this.__equalsCalc != null) {
      return this.__equalsCalc == obj;
    }
    this.__equalsCalc = obj;

    boolean _equals = 
      ((this.mailExportRequestMetadata == null) && (other.getMailExportRequestMetadata() == null)) || (
      (this.mailExportRequestMetadata != null) && 
      (this.mailExportRequestMetadata.equals(other.getMailExportRequestMetadata())) && (
      ((this.emailMsg == null) && (other.getEmailMsg() == null)) || (
      (this.emailMsg != null) && 
      (this.emailMsg.equals(other.getEmailMsg())) && (
      ((this.attachmentArray == null) && (other.getAttachmentArray() == null)) || (
      (this.attachmentArray != null) && 
      (Arrays.equals(this.attachmentArray, other.getAttachmentArray())))))));
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
    if (getMailExportRequestMetadata() != null) {
      _hashCode += getMailExportRequestMetadata().hashCode();
    }
    if (getEmailMsg() != null) {
      _hashCode += getEmailMsg().hashCode();
    }
    if (getAttachmentArray() != null) {
      for (int i = 0; 
        i < Array.getLength(getAttachmentArray()); 
        i++) {
        Object obj = Array.get(getAttachmentArray(), i);
        if ((obj != null) && 
          (!obj.getClass().isArray())) {
          _hashCode += obj.hashCode();
        }
      }
    }
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