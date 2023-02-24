package inovo.presence.administrator;

import inovo.web.InovoWebWidget;

import java.io.InputStream;


public class SQLInstallScript extends inovo.presence.web.SQLInstallScript {

	public SQLInstallScript(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}
	
	@Override
	public String resourcePathOfSqlScript(String sqlScriptPath) {
		return "/inovo/presence/install/leadsautomationcore.sql";
	}
	
	@Override
	public String installsqlscriptdballias(String dballias) {
		return super.installsqlscriptdballias("LEADSAUTOMATION");
	}
}
