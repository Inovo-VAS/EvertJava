package inovo.presence.administrator.engine;

import java.io.InputStream;
import java.util.HashMap;

import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class UrlTestPage extends InovoHTMLPageWidget {

	public UrlTestPage(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

	@Override
	public void pageContent() throws Exception {
		HashMap<String,String> params=new HashMap<String, String>();
		this.importRequestParametersIntoMap(params, null);
		this.startTable(null);
			this.startRow(null);
				this.startColumn(null);
					this.respondString("PARAMETERS RECEIVED");
				this.endColumn();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
					this.startTable(null);
						for(String paramName:params.keySet()){
							this.startRow(null);
								this.startColumn(null);this.respondString(paramName);this.endColumn();
								this.startCell(null);this.respondString(params.get(paramName));this.endCell();
							this.endRow();
						}
					this.endTable();
				this.endCell();
			this.endRow();
		this.endTable();
	}
}
