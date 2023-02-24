package za.co.woolworths.Indibano.WS_WFS_Multimedia;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.AttributeDesc;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class EmailMsg
  implements Serializable
{
  private String emailMsg;
  private String filename;
  private Object __equalsCalc = null;

  private boolean __hashCodeCalc = false;

  private static TypeDesc typeDesc = new TypeDesc(EmailMsg.class, true);

  static {
    typeDesc.setXmlType(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "EmailMsg"));
    AttributeDesc attrField = new AttributeDesc();
    attrField.setFieldName("filename");
    attrField.setXmlName(new QName("", "filename"));
    attrField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    typeDesc.addFieldDesc(attrField);
    ElementDesc elemField = new ElementDesc();
    elemField.setFieldName("emailMsg");
    elemField.setXmlName(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "EmailMsg"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
  }

  public EmailMsg()
  {
  }

  public EmailMsg(String emailMsg, String filename)
  {
    this.emailMsg = emailMsg;
    this.filename = filename;
  }

  public String getEmailMsg()
  {
    return this.emailMsg;
  }

  public void setEmailMsg(String emailMsg)
  {
    this.emailMsg = emailMsg;
  }

  public String getFilename()
  {
    return this.filename;
  }

  public void setFilename(String filename)
  {
    this.filename = filename;
  }

  public synchronized boolean equals(Object obj)
  {
    if (!(obj instanceof EmailMsg)) return false;
    EmailMsg other = (EmailMsg)obj;
    if (obj == null) return false;
    if (this == obj) return true;
    if (this.__equalsCalc != null) {
      return this.__equalsCalc == obj;
    }
    this.__equalsCalc = obj;

    boolean _equals = 
      ((this.emailMsg == null) && (other.getEmailMsg() == null)) || (
      (this.emailMsg != null) && 
      (this.emailMsg.equals(other.getEmailMsg())) && (
      ((this.filename == null) && (other.getFilename() == null)) || (
      (this.filename != null) && 
      (this.filename.equals(other.getFilename())))));
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
    if (getEmailMsg() != null) {
      _hashCode += getEmailMsg().hashCode();
    }
    if (getFilename() != null) {
      _hashCode += getFilename().hashCode();
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