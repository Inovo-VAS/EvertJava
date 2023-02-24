package inovo.presence.web;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class SQLInstallScript extends InovoHTMLPageWidget {

	public SQLInstallScript(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

	@Override
	public void pageContent() throws Exception {
		ByteArrayOutputStream sqlinstallscriptout=new ByteArrayOutputStream();
		inovo.adhoc.AdhocUtils.inputStreamToOutputStream(this.getClass().getResourceAsStream(this.resourcePathOfSqlScript("")), sqlinstallscriptout);
		this.startTable("");
			this.startRow("");
				this.startColumn("");
					this.respondString("SQL INSTALL SCRIPT");
				this.endColumn();
			this.endRow();
			this.startRow("");
				this.startCell("");
					this.startElement("textarea", "id=sqlinstallscript|name=sqlinstallscript|style=width:1000px;height:400px".split("[|]"), true);
					this.respondString(sqlinstallscriptout.toString());
					this.endElement("textarea", true);
					sqlinstallscriptout.close();
					sqlinstallscriptout=null;
				this.endCell();
			this.endRow();
			this.startRow("");
				this.startCell("");
					this.action("INSTALL SQL SCRIPT", "installsqlscript", "", "", "", "", "", "");
				this.endCell();
			this.endRow();
		this.endTable();
	}
	
	public void installsqlscript() throws Exception{
		try{
			Database.executeDBRequest(null, this.installsqlscriptdballias(""), this.requestParameter("SQLINSTALLSCRIPT"),null,null);
			this.showDialog("", ("title=INSTALLING SQL SCRIPT|content=SUCCESFUL").split("[|]"));
		}
		catch(Exception esqli){
			this.showDialog("", ("title=ERROR INSTALLING SQL SCRIPT|content="+esqli.getMessage()).split("[|]"));
		}
	}

	public String installsqlscriptdballias(String dballias) {
		return dballias;
	}

	public String resourcePathOfSqlScript(String sqlScriptPath) {
		return sqlScriptPath;
	}
}
