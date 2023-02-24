package inovo.presence.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.TreeMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class WallboardServiceAvailableAgents extends InovoHTMLPageWidget {

	public WallboardServiceAvailableAgents(InovoWebWidget parentWidget,
			InputStream inStream) {
		super(parentWidget, inStream);
	}

	@Override
	public void pageContent() throws Exception {
		TreeMap<Integer,ArrayList<Object>> wallboardServiceAgentCallsStatusses=new TreeMap<Integer, ArrayList<Object>>();
		Database.executeDBRequest(wallboardServiceAgentCallsStatusses,"PMCONSOLREPORTS", "SELECT * FROM <DBUSER>.WALLBOARD_SERVICE_AVAILABLE_AGENTS", null,null);
		
		this.startTable("");
		
		ArrayList<Object> rowData=null;
		for(int rowindex:wallboardServiceAgentCallsStatusses.keySet()){
			rowData=new ArrayList<Object>(wallboardServiceAgentCallsStatusses.get(rowindex));
			this.startRow("");
			if(rowindex==0){
				while (!rowData.isEmpty()){
					this.startColumn(null); this.respondString(rowData.remove(0).toString().toUpperCase()); this.endColumn();
				}
			}
			else{
				while (!rowData.isEmpty()){
					this.startCell(new String[]{"class=ui-widget-content"}); this.respondString(rowData.remove(0).toString());this.endCell();
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
		
			TreeMap<Integer,ArrayList<Object>> wallboardServiceAgentCallsStatusses=new TreeMap<Integer, ArrayList<Object>>();
			Database.executeDBRequest(wallboardServiceAgentCallsStatusses,"PMCONSOLREPORTS", "SELECT * FROM <DBUSER>.WALLBOARD_SERVICE_AVAILABLE_AGENTS", null,null);
			String delim=this.requestParameter("DELIM");
			if(delim.equals("")) delim=",";
			this.setResponseHeader("CONTENT-TYPE", "text/plain");
			ArrayList<Object> rowData=null;
			for(int rowindex:wallboardServiceAgentCallsStatusses.keySet()){
				rowData=new ArrayList<Object>(wallboardServiceAgentCallsStatusses.get(rowindex));
				if(rowindex==0){
					while (!rowData.isEmpty()){
						this.respondString(rowData.remove(0).toString().toUpperCase());
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
						this.respondString(rowData.remove(0).toString());
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
