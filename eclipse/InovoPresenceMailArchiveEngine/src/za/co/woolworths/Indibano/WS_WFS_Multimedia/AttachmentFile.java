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

public class AttachmentFile
  implements Serializable
{
  private String attachmentFile;
  private String filename;
  private Object __equalsCalc = null;

  private boolean __hashCodeCalc = false;

  private static TypeDesc typeDesc = new TypeDesc(AttachmentFile.class, true);

  static {
    typeDesc.setXmlType(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "AttachmentFile"));
    AttributeDesc attrField = new AttributeDesc();
    attrField.setFieldName("filename");
    attrField.setXmlName(new QName("", "filename"));
    attrField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    typeDesc.addFieldDesc(attrField);
    ElementDesc elemField = new ElementDesc();
    elemField.setFieldName("attachmentFile");
    elemField.setXmlName(new QName("http://WS_WFS_Multimedia.Indibano.woolworths.co.za", "AttachmentFile"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
  }

  public AttachmentFile()
  {
  }

  public AttachmentFile(String attachmentFile, String filename)
  {
    this.attachmentFile = attachmentFile;
    this.filename = filename;
  }

  public String getAttachmentFile()
  {
    return this.attachmentFile;
  }

  public void setAttachmentFile(String attachmentFile)
  {
    this.attachmentFile = attachmentFile;
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
    if (!(obj instanceof AttachmentFile)) return false;
    AttachmentFile other = (AttachmentFile)obj;
    if (obj == null) return false;
    if (this == obj) return true;
    if (this.__equalsCalc != null) {
      return this.__equalsCalc == obj;
    }
    this.__equalsCalc = obj;

    boolean _equals = 
      ((this.attachmentFile == null) && (other.getAttachmentFile() == null)) || (
      (this.attachmentFile != null) && 
      (this.attachmentFile.equals(other.getAttachmentFile())) && (
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
    if (getAttachmentFile() != null) {
      _hashCode += getAttachmentFile().hashCode();
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