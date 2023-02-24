package inovo.email;

import org.apache.commons.codec.binary.Base64;

public class EMLMailAttachment
{
  private String _attFileName;
  private byte[] _attdata;
  
  private Base64 _base64=new Base64();

  public EMLMailAttachment(EMLMailExtractor paramEMLMailExtractor, String filename, byte[] attdata)
  {
    this._attFileName = filename;
    this._attdata = attdata;
  }

  public String getAttFileName() {
    return this._attFileName;
  }

  public byte[] getAttData() {
    return this._attdata;
  }

  public String getAttDataAsBase64() throws Exception {
	return new String(_base64.encode(this._attdata));
  }
}