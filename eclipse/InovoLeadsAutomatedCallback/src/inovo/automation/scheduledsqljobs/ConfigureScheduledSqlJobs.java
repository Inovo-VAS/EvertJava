package inovo.automation.scheduledsqljobs;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class ConfigureScheduledSqlJobs extends InovoHTMLPageWidget {

	private ConfigureScheduledSqlJobs _configureScheduledSqlJobsRef=null;
	public ConfigureScheduledSqlJobs(InovoWebWidget parentWidget,
			InputStream inStream) {
		super(parentWidget, inStream);
		this._configureScheduledSqlJobsRef=this;
	}
	
	protected class ScheduledSqlJobSettingsData{
		public void readRowData(Integer rowindex,ArrayList<String> rowData,ArrayList<String> rowColumns){
			if(rowindex==0) return;
			HashMap<String,String> configureScheduledSqlJobSettings=Database.rowData(rowColumns, rowData);
			try{
				_configureScheduledSqlJobsRef.startRow(null);
					_configureScheduledSqlJobsRef.startCell(null);
						_configureScheduledSqlJobsRef.action("[JOBID:"+configureScheduledSqlJobSettings.get("ID")+"] "+configureScheduledSqlJobSettings.get("JOBLABEL"),"", "","", "", "", "", "");
					_configureScheduledSqlJobsRef.endCell();
				_configureScheduledSqlJobsRef.endRow();
			}
			catch(Exception e){
			}
		}
	}
	
	private ScheduledSqlJobSettingsData _scheduledSqlJobSettingsData=new ScheduledSqlJobSettingsData();
	
	@Override
	public void pageContent() throws Exception {
		this.startTable(null);
			this.startRow(null);
				this.startColumn("");
					this.respondString("SQL JOBS");
				this.endColumn();
				this.startColumn("");
				
				this.endColumn();
			this.endRow();
			this.startRow(null);
				this.startCell(new String[]{"id=scheduledSqlJobs"});
					this.scheduledSqlJobs();
				this.endTable();
				this.startCell(new String[]{"id=scheduledSqlJob"});
				this.endCell();
			this.endRow();
		this.endTable();
	}

	public void scheduledSqlJobs() throws Exception{
		this.startTable(null);
			Database.executeDBRequest(null, "SCHEDULEDSQLJOBS", "SELECT * FROM <DBUSER>.SCHEDULEDSQLJOBS", null, _scheduledSqlJobSettingsData);
		this.endTable();
	}

}
