package inovo.adhoc;
 
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
 
public class AdhocUtils
{
  private static Method internalfindMethod(Method[] methodsToSearch, String methodNameToFind, boolean caseSensative, int methodStartIndex, int methodEndIndex)
  {
    if ((caseSensative ? methodsToSearch[methodStartIndex].getName() : methodsToSearch[methodStartIndex].getName().toUpperCase()).equals(caseSensative ? methodNameToFind : methodNameToFind.toUpperCase())) {
      return methodsToSearch[methodStartIndex];
    }

    if ((caseSensative ? methodsToSearch[methodEndIndex].getName() : methodsToSearch[methodEndIndex].getName().toUpperCase()).equals(caseSensative ? methodNameToFind : methodNameToFind.toUpperCase())) {
      return methodsToSearch[methodEndIndex];
    }

    int methodMidIndex = (methodEndIndex + 1 - methodStartIndex) / 2 + methodStartIndex;

    if (methodMidIndex != methodEndIndex) {
      if ((methodMidIndex < methodEndIndex) && (methodMidIndex > methodStartIndex)) {
        if ((caseSensative ? methodsToSearch[methodMidIndex].getName() : methodsToSearch[methodMidIndex].getName().toUpperCase()).equals(caseSensative ? methodNameToFind : methodNameToFind.toUpperCase())) {
          return methodsToSearch[methodMidIndex];
        }
      }
      else {
        return null;
      }
    }

    methodStartIndex++;
    if (methodStartIndex == methodEndIndex) {
      return null;
    }

    Method methodFound = internalfindMethod(methodsToSearch, methodNameToFind, caseSensative, methodStartIndex, methodMidIndex);
    if (methodFound != null) {
      return methodFound;
    }

    methodEndIndex--;

    if (methodEndIndex == methodMidIndex) {
      return null;
    }

    Method methodNextFound = internalfindMethod(methodsToSearch, methodNameToFind, caseSensative, methodMidIndex, methodEndIndex);
    if (methodNextFound != null) {
      return methodNextFound;
    }

    return null;
  }

  public static Method findMethod(Method[] methodsToSearch, String methodNameToFind, boolean caseSensative) {
    return internalfindMethod(methodsToSearch, methodNameToFind, caseSensative, 0, methodsToSearch.length - 1);
  }

  public static LinkedHashMap<String, String> generateProperties(String[] propertiesInfo) {
    LinkedHashMap propertiesGenerated = new LinkedHashMap();
    String[] arrayOfString = propertiesInfo; int j = propertiesInfo.length; for (int i = 0; i < j; i++) { String propInfoItem = arrayOfString[i];
      if (propInfoItem.indexOf("=") > -1) {
        propertiesGenerated.put(propInfoItem.substring(0, propInfoItem.indexOf("=")), propInfoItem.substring(propInfoItem.indexOf("=") + 1));
      }
    }
    return propertiesGenerated;
  }

  public void readInputStreamIntoOutputStream(InputStream in, OutputStream out, int buffersize) throws Exception {
    if (in == null) {
      return;
    }
    if (out == null) {
      return;
    }

    byte[] bout = new byte[buffersize];

    int boutRead = 0;
    while ((boutRead = in.read(bout, 0, buffersize)) > -1)
      if (buffersize == 0)
        try {
          Thread.sleep(2L);
        }
        catch (InterruptedException localInterruptedException) {
        }
      else
        out.write(bout, 0, boutRead);
  }

  public static void inputStreamToOutputStream(InputStream in, OutputStream out)
    throws Exception
  {
	  inputStreamToOutputStream(in,out, 0);
  }

  public static void fileFromPathToOutputStream(String filepath, OutputStream out) throws Exception
  {
    fileFromPathToOutputStream(filepath, out, 0);
  }

  public static ByteArrayOutputStream inputSteamToBytesOutputStream(InputStream in) throws Exception
  {
    return inputSteamToBytesOutputStream(in, 0);
  }

  public static byte[] inputStreamToByteArray(InputStream in) throws Exception {
    return inputStreamToByteArray(in, 0);
  }

  public static String inputStreamToString(InputStream in) throws Exception {
    return inputStreamToString(in, 0);
  }

  private static int defaultInputStreamToOutputStreamBufferSize() {
    return 8912;
  }

  public static void inputStreamToOutputStream(BufferedReader inr, OutputStream out) throws Exception {
	  inputStreamToOutputStream(inr, out, 0);
  }

  public static void inputStreamToOutputStream(BufferedReader inr, OutputStream out, int buffersize) throws Exception
  {
    if ((inr == null) && (out == null)) return;
    int bufferbinoutsize = buffersize <= 0 ? defaultInputStreamToOutputStreamBufferSize() : buffersize;

    char[] charbufferbinout = new char[bufferbinoutsize = bufferbinoutsize < 8912 ? 8912 : bufferbinoutsize];
    byte[] bytebufferbinout = new byte[bufferbinoutsize = bufferbinoutsize < 8912 ? 8912 : bufferbinoutsize];
    int bufferbinoutread = 0;

    while ((bufferbinoutread = inr.read(charbufferbinout, 0, bufferbinoutsize)) > -1) {
      System.arraycopy(charbufferbinout, 0, bytebufferbinout, 0, bufferbinoutsize);
      out.write(bytebufferbinout, 0, bufferbinoutread);
    }
  }

  public static void inputStreamToOutputStream(InputStream in, OutputStream out, int buffersize) throws Exception
  {
    if ((in == null) && (out == null)) return;
    int bufferbinoutsize = buffersize <= 0 ? defaultInputStreamToOutputStreamBufferSize() : buffersize;

    byte[] bufferbinout = new byte[bufferbinoutsize = bufferbinoutsize < 8912 ? 8912 : bufferbinoutsize];
    int bufferbinoutread = 0;

    for (bufferbinoutread = in.read(bufferbinout); bufferbinoutread != -1; bufferbinoutread = in.read(bufferbinout))
    {
      out.write(bufferbinout, 0, bufferbinoutread);
    }
  }

  public static void fileFromPathToOutputStream(String filepath, OutputStream out, int buffersize) throws Exception
  {
    if (out == null) return;
    FileInputStream fin = new FileInputStream(filepath);
    inputStreamToOutputStream(fin, out, buffersize);
  }
  
  public static void fileInputStreamToOutputStream(File fin,OutputStream out) throws Exception{
	  fileInputStreamToOutputStream(fin, out,0);
  }
  
  public static void fileInputStreamToOutputStream(File fin,OutputStream out,int buffersize) throws Exception{
	  if (out == null) return;
	  FileInputStream finstream = new FileInputStream(fin);
	    inputStreamToOutputStream(finstream, out, buffersize);
  }

  public static ByteArrayOutputStream inputSteamToBytesOutputStream(InputStream in, int buffersize) throws Exception
  {
    if (in == null) return null;
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    inputStreamToOutputStream(in, bout, buffersize);
    return bout;
  }

  public static byte[] inputStreamToByteArray(InputStream in, int buffersize) throws Exception
  {
    if (in == null) return new byte[0];
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    return inputSteamToBytesOutputStream(in, buffersize).toByteArray();
  }

  public static String inputStreamToString(InputStream in, int buffersize) throws Exception
  {
    if (in == null) return "";
    return inputSteamToBytesOutputStream(in, buffersize).toString();
  }
  
  public static void executeConsoleCommand(String shellcommand,
			OutputStream output) throws Exception {
	  executeConsoleCommand(new String[]{shellcommand}, output);
  }

  public static void executeConsoleCommand(String[] shellcommand,
			OutputStream output) throws Exception {
	  Process p=Runtime.getRuntime().exec(shellcommand);
	  inputStreamToOutputStream(p.getInputStream(), output, 8912);
  }
}
