package inovo.email.smtp;
import java.io.IOException;


public class MailClient {

	public void sendBytes(byte[]byteToSend) throws IOException{
	}
	
	public String parseHeaderName(String headerName){
		char[]headerNameChars=headerName.toUpperCase().toCharArray();
		String newHeaderName="";
		char prevChar=0;
		for(char ch:headerNameChars){
			if("ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(prevChar)>-1){
				newHeaderName+=((char)ch+"").toLowerCase();
			}
			else{
				newHeaderName+=((char)ch+"");
			}
			prevChar=ch;
		}
		return newHeaderName;
	}
}
