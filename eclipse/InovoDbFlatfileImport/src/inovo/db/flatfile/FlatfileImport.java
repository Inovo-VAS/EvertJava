package inovo.db.flatfile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class FlatfileImport extends InovoHTMLPageWidget {

	public FlatfileImport(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

	private TreeMap<Integer,String> _exportExceptions=new TreeMap<Integer, String>();
	@Override
	public void pageContent() throws Exception {
		this.startTable();
			this.startRow();
				this.startColumn("font-size:0.6em");this.respondString("DESTINATION TABLE");this.endColumn();
				this.startCell();
					this.simpleElement("input", "type=text|name=destinationtable".split("[|]"));
				this.endCell();
				this.startColumn("font-size:0.6em");this.respondString("CREATE DESTINATION TABLE");this.endColumn();
				this.startCell();
					this.simpleElement("input", "type=checkbox|name=createdestinationtable|value=YES".split("[|]"));
				this.endCell();
				this.startColumn("font-size:0.6em");this.respondString("FILE");this.endColumn();
				this.startCell();
					this.simpleElement("input", "type=file|name=filetoimport".split("[|]"));
				this.endCell();
				this.startColumn("font-size:0.6em");this.respondString("DELIMITER");this.endColumn();
				this.startCell();
					this.simpleElement("input", "type=text|value=|name=FDELIM".split("[|]"));
				this.endCell();
				this.startCell();
					this.action("UPLOAD FLAT FILE", "importDbFile", "", "", "uploadresponse", "", "", "");
				this.endCell();
			this.endRow();
		this.endTable();
		this.startElement("span", "id=uploadresponse".split("[|]"), true);this.endElement("span", true);
	}
	
	private String _destinationtable="";
	public void importDbFile() throws Exception{
		FileInputStream filein=new FileInputStream(this.requestParameterFile("filetoimport"));
		Database.populateDatasetFromFlatFileStream(null, "CSV", filein, null,this.requestParameter("FDELIM").charAt(0), this);
		filein.close();
		if(!_exportExceptions.isEmpty()){
			for(Integer errLine:_exportExceptions.keySet()){
				this.respondString("[line "+String.valueOf((errLine+1))+"]"+_exportExceptions.get(errLine)+"\r\n");
			}
		}
		else{
			respondString("SUCCESS");
		}
	}
	
	private boolean _stopEverything=false;
	
	public void readRowData(Integer rowindex,ArrayList<String> rowData,ArrayList<String> rowColumn){
		String importSqlStatement="";
		if(rowindex==0){
			if(this.requestParameter("CREATEDESTINATIONTABLE").equals("YES")){
				importSqlStatement="CREATE TABLE "+this.requestParameter("DESTINATIONTABLE")+" (";
				for(int colindex=0;colindex<rowColumn.size();colindex++){
					importSqlStatement+="["+rowColumn.get(colindex).toUpperCase()+"] varchar(1000)"+(colindex<(rowColumn.size()-1)?",":"");
				}
				try{
					Database.executeDBRequest(null, "FLATFILEIMPORT", importSqlStatement+")", null, null);
				}
				catch(Exception dbe){
					_exportExceptions.put(rowindex, dbe.getMessage());
					_stopEverything=true;
				}
			}
			return;
		}
		if(_stopEverything) return;
		HashMap<String, Object> currentFlatFileParams=new HashMap<String, Object>();
		importSqlStatement="INSERT INTO "+this.requestParameter("DESTINATIONTABLE")+" SELECT ";
		for(int colindex=0;colindex<rowColumn.size();colindex++){
			importSqlStatement+=":COLUMN"+String.valueOf(colindex)+(colindex<(rowColumn.size()-1)?",":"");
			currentFlatFileParams.put("COLUMN"+String.valueOf(colindex), rowData.get(colindex));
		}
		try{
			Database.executeDBRequest(null, "FLATFILEIMPORT", importSqlStatement, currentFlatFileParams, null);
		}
		catch(Exception dbe){
			_exportExceptions.put(rowindex, dbe.getMessage());
		}
		currentFlatFileParams.clear();
		currentFlatFileParams=null;
	}
}
