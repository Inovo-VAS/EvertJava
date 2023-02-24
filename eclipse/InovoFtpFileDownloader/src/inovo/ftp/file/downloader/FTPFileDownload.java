package inovo.ftp.file.downloader;

import java.io.InputStream;

import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class FTPFileDownload extends InovoHTMLPageWidget {

	public FTPFileDownload(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

}
