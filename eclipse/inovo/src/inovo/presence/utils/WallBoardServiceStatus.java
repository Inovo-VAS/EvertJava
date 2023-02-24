package inovo.presence.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.TreeMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class WallBoardServiceStatus extends InovoHTMLPageWidget {

	public WallBoardServiceStatus(InovoWebWidget parentWidget,
			InputStream inStream) {
		super(parentWidget, inStream);
	}
	
	@Override
	public void pageContent() throws Exception {
		TreeMap<Integer,ArrayList<Object>> wallboardServiceStatusses=new TreeMap<Integer, ArrayList<Object>>();
		Database.executeDBRequest(wallboardServiceStatusses,"PMCONSOLREPORTS", "SELECT * FROM <DBUSER>.WALLBOARD_SERVICESTATUS", null,null);
		
		this.startTable("");
		
		ArrayList<Object> rowData=null;
		for(int rowindex:wallboardServiceStatusses.keySet()){
			rowData=new ArrayList<Object>(wallboardServiceStatusses.get(rowindex));
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
		
		this.endTable();
		Database.cleanupDataset(wallboardServiceStatusses);
	}
	
	@Override
	public void executeContentWidget() throws Exception {
		boolean csvresponse=this.requestParameter("CONTENT").equals("CSV");
		if(csvresponse){
		
			TreeMap<Integer,ArrayList<Object>> wallboardServiceStatusses=new TreeMap<Integer, ArrayList<Object>>();
			Database.executeDBRequest(wallboardServiceStatusses,"PMCONSOLREPORTS", "SELECT * FROM <DBUSER>.WALLBOARD_SERVICESTATUS", null,null);
			String delim=this.requestParameter("DELIM");
			if(delim.equals("")) delim=",";
			
			this.setResponseHeader("CONTENT-TYPE", "text/plain");
			ArrayList<Object> rowData=null;
			for(int rowindex:wallboardServiceStatusses.keySet()){
				rowData=new ArrayList<Object>(wallboardServiceStatusses.get(rowindex));
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
			Database.cleanupDataset(wallboardServiceStatusses);
			wallboardServiceStatusses=null;
		}
		else{
			super.executeContentWidget();
		}
	}

}
