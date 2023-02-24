package za.co.woolworths.Indibano.WS_WFS_Multimedia;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class Attachment
  implements Serializable
{
  private AttachmentFile attachmentFile;
  private int attachmentNumber;
  private Object __equalsCalc = null;

  private boolean __hashCodeCalc = false;

  private static TypeDesc typeDesc = new TypeDesc(Attachment.class, true);

  static {
    typeDesc.setXmlType(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "Attachment"));
    ElementDesc elemField = new ElementDesc();
    elemField.setFieldName("attachmentFile");
    elemField.setXmlName(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "AttachmentFile"));
    elemField.setXmlType(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "AttachmentFile"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("attachmentNumber");
    elemField.setXmlName(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "AttachmentNumber"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
  }

  public Attachment()
  {
  }

  public Attachment(AttachmentFile attachmentFile, int attachmentNumber)
  {
    this.attachmentFile = attachmentFile;
    this.attachmentNumber = attachmentNumber;
  }

  public AttachmentFile getAttachmentFile()
  {
    return this.attachmentFile;
  }

  public void setAttachmentFile(AttachmentFile attachmentFile)
  {
    this.attachmentFile = attachmentFile;
  }

  public int getAttachmentNumber()
  {
    return this.attachmentNumber;
  }

  public void setAttachmentNumber(int attachmentNumber)
  {
    this.attachmentNumber = attachmentNumber;
  }

  public synchronized boolean equals(Object obj)
  {
    if (!(obj instanceof Attachment)) return false;
    Attachment other = (Attachment)obj;
    if (obj == null) return false;
    if (this == obj) return true;
    if (this.__equalsCalc != null) {
      return this.__equalsCalc == obj;
    }
    this.__equalsCalc = obj;

    boolean _equals = 
      ((this.attachmentFile == null) && (other.getAttachmentFile() == null)) || (
      (this.attachmentFile != null) && 
      (this.attachmentFile.equals(other.getAttachmentFile())) && 
      (this.attachmentNumber == other.getAttachmentNumber()));
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
    if (getAttachmentFile() != null) {
      _hashCode += getAttachmentFile().hashCode();
    }
    _hashCode += getAttachmentNumber();
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