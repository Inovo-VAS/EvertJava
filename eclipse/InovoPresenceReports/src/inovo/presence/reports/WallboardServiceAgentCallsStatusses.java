package inovo.presence.reports;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.TreeMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class WallboardServiceAgentCallsStatusses extends InovoHTMLPageWidget {

	public WallboardServiceAgentCallsStatusses(InovoWebWidget parentWidget,
			InputStream inStream) {
		super(parentWidget, inStream);
	}
	
	@Override
	public void pageContent() throws Exception {
		TreeMap<Integer,ArrayList<String>> wallboardServiceAgentCallsStatusses=new TreeMap<Integer, ArrayList<String>>();
		Database.executeDBRequest(wallboardServiceAgentCallsStatusses,"PMCONSOLREPORTS", "SELECT * FROM <DBUSER>.WALLBOARD_SERVICEAGENTCALLSSTATUS", null,null);
		
		this.startTable(null);
		
		ArrayList<String> rowData=null;
		for(int rowindex:wallboardServiceAgentCallsStatusses.keySet()){
			rowData=new ArrayList<String>(wallboardServiceAgentCallsStatusses.get(rowindex));
			this.startRow(null);
			if(rowindex==0){
				while (!rowData.isEmpty()){
					this.startColumn(null); this.respondString(rowData.remove(0).toUpperCase()); this.endColumn();
				}
			}
			else{
				while (!rowData.isEmpty()){
					this.startCell(new String[]{"class=ui-widget-content"}); this.respondString(rowData.remove(0));this.endCell();
				}
			}
			this.endRow();
			
		}
		Database.cleanupDataset(wallboardServiceAgentCallsStatusses);
		wallboardServiceAgentCallsStatusses=null;
		this.endTable();
	}
	
	@Override
	public void executeContentWidget() throws Exception {
		boolean csvresponse=this.requestParameter("CONTENT").equals("CSV");
		if(csvresponse){
		
			TreeMap<Integer,ArrayList<String>> wallboardServiceAgentCallsStatusses=new TreeMap<Integer, ArrayList<String>>();
			Database.executeDBRequest(wallboardServiceAgentCallsStatusses,"PMCONSOLREPORTS", "SELECT * FROM <DBUSER>.WALLBOARD_SERVICEAGENTCALLSSTATUS", null,null);
			String delim=this.requestParameter("DELIM");
			if(delim.equals("")) delim=",";
			this.setResponseHeader("CONTENT-TYPE", "text/plain");
			ArrayList<String> rowData=null;
			for(int rowindex:wallboardServiceAgentCallsStatusses.keySet()){
				rowData=new ArrayList<String>(wallboardServiceAgentCallsStatusses.get(rowindex));
				if(rowindex==0){
					while (!rowData.isEmpty()){
						this.respondString(rowData.remove(0).toUpperCase());
						if(rowData.isEmpty()){
							this.respondString("\r\n");
						}
						else{
							this.respondString(delim);
						}
					}
				}
				else{
					while (!rowData.isEmpty()){
						this.respondString(rowData.remove(0));
						if(rowData.isEmpty()){
							this.respondString("\r\n");
						}
						else{
							this.respondString(delim);
						}
					}
				}
			}
			Database.cleanupDataset(wallboardServiceAgentCallsStatusses);
			wallboardServiceAgentCallsStatusses=null;
		}
		else{
			super.executeContentWidget();
		}
	}

}
