package inovo.leads.automated.callback;

import java.io.InputStream;

import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class Callback extends InovoHTMLPageWidget {

	public Callback(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

}
