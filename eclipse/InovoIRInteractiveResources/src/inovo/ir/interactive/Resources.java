package inovo.ir.interactive;

import java.io.InputStream;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class Resources extends InovoHTMLPageWidget {

	public Resources(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}
	
	@Override
	public void pageContent() throws Exception {
		this.resources();
	}

	public void resources() throws Exception{
		
		Database.executeDBRequest(null, "PRESENCE", "SELECT * FROM PREP.PIR_AUDIOFILE", null, null);
		
		this.startTable();
			this.startRow();
				this.startColumn();
					this.respondString("RESOURCES");
				this.endColumn();
				
			this.endRow();
		this.endTable();
	}
}
