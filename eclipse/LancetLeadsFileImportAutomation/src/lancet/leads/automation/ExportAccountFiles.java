package lancet.leads.automation;

import inovo.db.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class ExportAccountFiles {

	public void startExportingFiles() {
		
	}
	
	private UnloadAccounts _unloadAccounts=new UnloadAccounts();
	public void readRowData(Integer rowIndex,ArrayList<String> rowData,ArrayList<String> columns){
		if(rowIndex==0) return;
		HashMap<String,String> fileExportInfo=Database.rowData(columns, rowData);
		//inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("ExportAccountFiles[readRowData()] params:"+fileExportInfo.toString());
		File exportFile=new File(fileExportInfo.get("FILEPATH"));
		
		if(exportFile.exists()){
			
			FileInputStream accFileIn=null;
			try {
				accFileIn = new FileInputStream(exportFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			_unloadAccounts.setFileRequestRescription(fileExportInfo.get("UNLOAD_FILE_REQUEST"));
			String fileProjectName=exportFile.getName();
			while(fileProjectName.indexOf("\\")>-1) fileProjectName=fileProjectName.substring(0,fileProjectName.indexOf("\\"))+"/"+fileProjectName.substring(fileProjectName.indexOf("\\")+1);
			if(fileProjectName.indexOf("/")>-1) fileProjectName=fileProjectName.substring(fileProjectName.lastIndexOf("/")+1);
			
			_unloadAccounts.setProjectRequest(fileProjectName.toUpperCase());
			
			_unloadAccounts.setfileId(fileExportInfo.get("ID"));
			if(accFileIn!=null){
				try{
					Database.populateDatasetFromFlatFileStream(null, "CSV", accFileIn, null,(exportFile.getName().toUpperCase().endsWith(".CSV")?'.': '|'),_unloadAccounts);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			try {
				Database.executeDBRequest(null, "LANCETLEADSAUTOMATION", "UPDATE <DBUSER>.UNLOAD_ACCOUNTFILE SET REQUESTHANDLEFLAG=3 WHERE ID=:ID", fileExportInfo, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			try {
				Database.executeDBRequest(null, "LANCETLEADSAUTOMATION", "UPDATE <DBUSER>.UNLOAD_ACCOUNTFILE SET REQUESTHANDLEFLAG=4 WHERE ID=:ID", fileExportInfo, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		fileExportInfo.clear();
		fileExportInfo=null;
	}

}
