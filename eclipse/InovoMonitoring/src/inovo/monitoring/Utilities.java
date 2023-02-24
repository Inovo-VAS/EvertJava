package inovo.monitoring;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class Utilities extends InovoHTMLPageWidget {

	public Utilities(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

	public void testecho() throws Exception{
		this.respondString("Hello "+new SimpleDateFormat("yyyy-MM-dd HH:mm.ss.SSS").format(new Date()));
		//System.out.println("Hello "+new SimpleDateFormat("yyyy-MM-dd HH:mm.ss.SSS").format(new Date()));
		//SELECT * FROM v$session WHERE type='USER' AND PROGRAM like'pco%'
	}
	
	public void retrieveDBSettings(){
		this.setResponseHeader("CONTENT-TYPE", "text/plain");
		try{
			HashMap<String,String> dbsettings=Database.dballias("DBMONITOR").dbsettings();
			this.respondString("DBTYPE="+(dbsettings.get("DBTYPE").equals("sqlserver")?"MSSQL":dbsettings.get("DBTYPE").equals("oracle")?"ORACLE":"")+"\r\n");
			if(dbsettings.get("DBHOST").indexOf(":")>-1){
				dbsettings.put("DBPORT",dbsettings.get("DBHOST").substring(dbsettings.get("DBHOST").indexOf(":")+1));
				dbsettings.put("DBHOST",dbsettings.get("DBHOST").substring(0,dbsettings.get("DBHOST").indexOf(":")));
			}
			else{
				dbsettings.put("DBPORT","");
			}
			if(dbsettings.get("DBNAME")==null){
				dbsettings.put("DBNAME","");
			}
			this.respondString("DBHOST="+dbsettings.get("DBHOST")+"\r\n");
			this.respondString("DBPORT="+dbsettings.get("DBPORT")+"\r\n");
			this.respondString("DBINSTANCE="+dbsettings.get("DBINSTANCE")+"\r\n");
			this.respondString("DBNAME="+dbsettings.get("DBNAME")+"\r\n");
			this.respondString("DBUSER="+(dbsettings.get("DBUSER")==null?"":dbsettings.get("DBUSER"))+"\r\n");
			this.respondString("DBPW="+dbsettings.get("DBPW")+"\r\n");
			this.respondString("DBPASSWORD="+dbsettings.get("DBPW"));
		}
		catch(Exception e){
			
		}
	}
}
