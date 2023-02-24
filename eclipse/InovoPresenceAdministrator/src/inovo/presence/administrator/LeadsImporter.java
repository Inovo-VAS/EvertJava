package inovo.presence.administrator;

import java.io.InputStream;
import java.util.ArrayList;

import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class LeadsImporter extends InovoHTMLPageWidget {

	public LeadsImporter(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

	@Override
	public void pageContent() throws Exception {
		ArrayList<String[]> pageOptions=new ArrayList<String[]>();
		pageOptions.add(new String[]{"caption=FLAT FILE LEADS","command=flatfileleads"});
		pageOptions.add(new String[]{"caption=MANUAL BATCH LEADS","command=manualbatchleads"});
		this.actions_tabs(pageOptions, "leadsimportssection", false);
	}
	
	public void flatfileleads() throws Exception{
		this.startTable(null);
			this.startRow(null);
				this.startColumn(null);
					this.respondString("FLAT FILE LEADS");
				this.endColumn();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
					this.flatfileimportwizard();
				this.endCell();
			this.endRow();
		this.endTable();
	}
	
	public void flatfileimportwizard() throws Exception{
		this.startTable(null);
			this.startRow(null);
				this.startColumn("font-size:0.6em");
					this.respondString("STEP");
				this.endColumn();
				this.startColumn("font-size:0.6em");
					this.respondString("INSTRUCTIONS");
				this.endColumn();
			this.endRow();
		this.endTable();
	}

	public void manualbatchleads() throws Exception{
		this.startTable(null);
			this.startRow(null);
				this.startColumn(null);
					this.respondString("MANUAL BATCH LEADS");
				this.endColumn();
			this.endRow();
		this.endTable();
	}
}
