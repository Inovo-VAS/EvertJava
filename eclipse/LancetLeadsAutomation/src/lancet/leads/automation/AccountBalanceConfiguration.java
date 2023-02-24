package lancet.leads.automation;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import inovo.db.Database;
import inovo.servlet.InovoServletContextListener;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class AccountBalanceConfiguration extends InovoHTMLPageWidget{

	public AccountBalanceConfiguration(InovoWebWidget parentWidget,
			InputStream inStream) {
		super(parentWidget, inStream);
	}
	
	@Override
	public void pageContent() throws Exception {
		ArrayList<String[]> accountbalanceconfigactions=new ArrayList<String[]>();
		accountbalanceconfigactions.add(new String[]{"caption=UPLOAD ACCOUNTS FILE","command=uploadaccountbalancefile","formid=mainform","actiontarget=accountbalanceconfigsection"});
		accountbalanceconfigactions.add(new String[]{"caption=UPLOAD (UNLOAD) ACCOUNTS FILE","command=uploadunloadaccountfile","formid=mainform","actiontarget=accountbalanceconfigsection"});
		accountbalanceconfigactions.add(new String[]{"caption=BALANCE PRIORITY BRACKETS","command=balancepriorities","formid=mainform","actiontarget=accountbalanceconfigsection"});
		accountbalanceconfigactions.add(new String[]{"caption=BALANCE CAMPAIGN LEADS BRACKETS","command=balanceleads","formid=mainform","actiontarget=accountbalanceconfigsection"});
		//this.actions_tabs(accountbalanceconfigactions, "accountbalanceconfigsection", false);
		this.actions(accountbalanceconfigactions, false);
		this.startElement("div", new String[]{"id=accountbalanceconfigsection"}, true);this.endElement("div", true);
	}
	
	public void uploadaccountbalancefile() throws Exception{
		this.startTable(null);
			this.startRow(null);
				this.startCell(null);
					this.fieldLabel("ACCOUNTS FILE");
				this.endCell();
				this.startCell(null);
					this.fieldInput("ACCOUNTSFILE", "", "file", true, null);
				this.endCell();
				this.startCell(null);
					this.action("UPLOAD FILE", "uploadbalancefile", "mainform", "", "", "", "",	 "");
				this.endCell();
			this.endRow();
		this.endTable();
		this.startElement("div", new String[]{"id=uploadfileprogress"}, true);
			this.uploadfilesprogress();
		this.endElement("div", true);
	}
	
	public void uploadunloadaccountfile() throws Exception{
		this.startTable(null);
			this.startRow(null);
				this.startCell(null);
					this.fieldLabel("ACCOUNTS (UNLOAD) FILE");
				this.endCell();
				this.startCell(null);
					this.fieldInput("ACCOUNTSUNLOADFILE", "", "file", true, null);
				this.endCell();
				this.startCell(null);
				this.fieldLabel("UNLOAD REASON");
				this.endCell();
				this.startCell(null);
					this.fieldInput("ACCOUNTSUNLOADREASON", "", "text", true, null);
				this.endCell();
				this.startCell(null);
					this.action("UPLOAD (UNLOAD) FILE", "uploadaccunloadfile", "mainform", "", "", "", "",	 "");
				this.endCell();
			this.endRow();
		this.endTable();
	}
	
	public void uploadfilesprogress() throws Exception {
		TreeMap<Integer,ArrayList<String>> updateProgressSet=new TreeMap<Integer, ArrayList<String>>();
		Database.executeDBRequest(updateProgressSet,"LANCETLEADSAUTOMATION", "SELECT COUNT(*) AS RECORDS_TO_PROCESS, PROJECT_NAME FROM <DBUSER>.LANCET_ACCOUNTS WHERE RECORDHANDLEFLAG<3 GROUP BY PROJECT_NAME ORDER BY RECORDS_TO_PROCESS DESC", null,null);
		if(updateProgressSet.size()>1){
			this.startTable(null);
				this.startRow(null);
					this.startColumn(null);
						this.respondString("PROJECT NAME");
					this.endColumn();
					this.startColumn(null);
						this.respondString("RECORDS BEING PROCESSED");
					this.endColumn();
				this.endRow();
				for(int rowindex:updateProgressSet.keySet()){
					if (rowindex==0) continue;
					HashMap<String,String> progressData=Database.rowData(updateProgressSet, rowindex);
					this.startRow(null);
						this.startCell(null);this.respondString(progressData.get("PROJECT_NAME")); this.endCell();
						this.startCell(null);this.respondString(progressData.get("RECORDS_TO_PROCESS")); this.endCell();
					this.endRow();
				}
			this.endTable();
		}
		Database.cleanupDataset(updateProgressSet);
		updateProgressSet=null;
	}

	public void balancepriorities() throws Exception{
		this.startTable(null);
			this.startRow(null);
				this.startCell(null);
					this.fieldLabel("BALANCE");
				this.endCell();
				this.startCell(null);
					this.fieldInput("NEWBALANCE",(this.requestParameter("SELECTEDBALANCE").equals("")? "0":this.requestParameter("SELECTEDBALANCE")), "text", true, null);
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
					this.fieldLabel("PRIORITY");
				this.endCell();
				this.startCell(null);
					this.fieldInput("NEWPRIORITY", (this.requestParameter("SELECTEDPRIORITY").equals("")? "100":this.requestParameter("SELECTEDPRIORITY")), "text", true, null);
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startCell(new String[]{"colspan=2"});
					this.action("ADD NEW BALANCE PRIORITY",  "newbalancepriority", "mainform", "", "", "ui-icon-check", "", "");
				this.endCell();
			this.endRow();
		this.endTable();
		this.startTable(null);
			this.startRow(null);
				this.startCell(new String[]{"id=balanceprioritieslist","style=vertical-align:top"});this.balanceprioritieslist(); this.endCell();
			this.endRow();
		this.endTable();
		
	}
	
	public void uploadaccunloadfile() throws Exception{
		if(this.requestParameter("ACCOUNTSUNLOADFILE").equals("")){
			this.replaceComponentContent("accountbalanceconfigsection");
				this.respondString("NO FILE SELECTED");
			this.endReplaceComponentContent();
		}
		else{
			File uploadedFile=this.requestParameterFile("ACCOUNTSUNLOADFILE");
			String fileDestinationPath=InovoServletContextListener.inovoServletListener().configProperty("ACCOUNTUNLOADFILEPICKUPPATH");
			while(!fileDestinationPath.endsWith("/")) fileDestinationPath+="/";
			String accountsfilename=this.requestParameter("ACCOUNTSUNLOADFILE");
			if(accountsfilename.indexOf("\\")>-1){
				accountsfilename=accountsfilename.substring(accountsfilename.lastIndexOf("\\")+1);
			}
			else if(accountsfilename.indexOf("/")>-1){
				accountsfilename=accountsfilename.substring(accountsfilename.lastIndexOf("/")+1);
			}
			fileDestinationPath+=accountsfilename;
			uploadedFile.renameTo(new File(fileDestinationPath));
			
			if(this.requestParameter("ACCOUNTSUNLOADREASON").equals("")){
				this.setRequestParameter("ACCOUNTSUNLOADREASON", (fileDestinationPath.lastIndexOf("/")>-1?fileDestinationPath.substring(fileDestinationPath.lastIndexOf("/")+1):fileDestinationPath.lastIndexOf("\\")>-1?fileDestinationPath.substring(fileDestinationPath.lastIndexOf("\\")+1):fileDestinationPath), true);
			}
			
			HashMap<String,String> sqlUnloadFileParams=new HashMap<String, String>();
			sqlUnloadFileParams.put("FILEPATH", fileDestinationPath);
			sqlUnloadFileParams.put("UNLOAD_FILE_REQUEST",this.requestParameter("ACCOUNTSUNLOADREASON"));
			Database.executeDBRequest(null, "LANCETLEADSAUTOMATION", "INSERT INTO <DBUSER>.UNLOAD_ACCOUNTFILE (FILEPATH,UNLOAD_FILE_REQUEST,REQUESTHANDLEFLAG,CREATION_DATETIME) VALUES(:FILEPATH,:UNLOAD_FILE_REQUEST,1,GETDATE())", sqlUnloadFileParams, null);
			
			this.replaceComponentContent("accountbalanceconfigsection");
				this.respondString("FILE ("+this.requestParameter("ACCOUNTSUNLOADFILE")+") UPLOADED AND IS BEING PROCESSED (UNLOADED)");
			this.endReplaceComponentContent();
		}
	}
	
	public void uploadbalancefile() throws Exception{
		if(this.requestParameter("ACCOUNTSFILE").equals("")){
			this.replaceComponentContent("accountbalanceconfigsection");
				this.respondString("NO FILE SELECTED");
			this.endReplaceComponentContent();
		}
		else{
			File uploadedFile=this.requestParameterFile("ACCOUNTSFILE");
			String fileDestinationPath=InovoServletContextListener.inovoServletListener().configProperty("ACCOUNTFILEPICKUPPATH");
			while(!fileDestinationPath.endsWith("/")) fileDestinationPath+="/";
			String accountsfilename=this.requestParameter("ACCOUNTSFILE");
			if(accountsfilename.indexOf("\\")>-1){
				accountsfilename=accountsfilename.substring(accountsfilename.lastIndexOf("\\")+1);
			}
			else if(accountsfilename.indexOf("/")>-1){
				accountsfilename=accountsfilename.substring(accountsfilename.lastIndexOf("/")+1);
			}
			fileDestinationPath+=accountsfilename;
			uploadedFile.renameTo(new File(fileDestinationPath));
			this.replaceComponentContent("accountbalanceconfigsection");
				this.respondString("FILE ("+this.requestParameter("ACCOUNTSFILE")+") UPLOADED AND IS BEING PROCESSED");
			this.endReplaceComponentContent();
		}
		
	}
	
	public void balanceleads() throws Exception{
		this.startTable(null);
			this.startRow(null);
				this.startCell(null);
					this.fieldLabel("BALANCE");
				this.endCell();
				this.startCell(null);
					this.fieldInput("NEWBALANCE",(this.requestParameter("SELECTEDBALANCE").equals("")? "0":this.requestParameter("SELECTEDBALANCE")), "text", true, null);
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
					this.fieldLabel("CAMPAIGN");
				this.endCell();
				this.startCell(null);
					this.fieldInput("NEWSERVICEID", (this.requestParameter("SELECTEDSERVICEID").equals("")? "0":this.requestParameter("SELECTEDSERVICEID")), "text", true, null);
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
					this.fieldLabel("LOAD");
				this.endCell();
				this.startCell(null);
					this.fieldInput("NEWLOADID",(this.requestParameter("SELECTEDLOADID").equals("")? "0":this.requestParameter("SELECTEDLOADID")), "text", true, null);
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startCell(new String[]{"colspan=2"});
					this.action("ADD NEW BALANCE LEAD DESTINATION",  "newbalancelead", "mainform", "", "", "ui-icon-check", "", "");
				this.endCell();
			this.endRow();
		this.endTable();
		this.startTable(null);
			this.startRow(null);
				this.startCell(new String[]{"id=balanceleadslist","style=vertical-align:top"});this.balanceleadslist(); this.endCell();
			this.endRow();
		this.endTable();
		
	}

	public void balanceleadslist() throws Exception{
		TreeMap<Integer,ArrayList<String>> balancePrioritySet=new TreeMap<Integer, ArrayList<String>>();
		Database.executeDBRequest(balancePrioritySet,"LANCETLEADSAUTOMATION", "SELECT * FROM <DBUSER>.LANCET_ACCOUNT_BALANCE_CAMPAIGN_LOAD_MAP "+(this.requestParameter("ORDERBY").equals("")?" ORDER BY BALANCE DESC":this.requestParameter("ORDERBY")), null,null);
		if(balancePrioritySet!=null){
			this.startTable(new String[]{"cellpadding=0","cellspacing=0"});
				this.startRow(null);
					this.startColumn("");
					this.endColumn();
					this.startColumn("");
						this.startTable(null);
							this.startRow(null);
								this.startCell(null);
									this.action("asc", "", "balanceleadslist", "mainform", "", "balanceleadslist", "ui-icon-arrowstop-1-s", "", "ORDERBY=ORDER BY BALANCE ASC");
								this.endCell();
								this.startCell(new String[]{"style=font-size:0.6em"});
									this.respondString("BALANCE");
								this.endCell();
								this.startCell(null);
									this.action("desc", "", "balanceleadslist", "mainform", "", "balanceleadslist", "ui-icon-arrowstop-1-n", "", "ORDERBY=ORDER BY BALANCE DESC");
								this.endCell();
							this.endRow();
						this.endTable();
					this.endColumn();
					this.startColumn("");
						this.startTable(null);
							this.startRow(null);
								this.startCell(null);
									this.action("asc", "", "balanceleadslist", "mainform", "", "balanceleadslist", "ui-icon-arrowstop-1-s", "", "ORDERBY=ORDER BY SERVICEID ASC, BALANCE");
								this.endCell();
								this.startCell(new String[]{"style=font-size:0.6em"});
									this.respondString("CAMPAIGN NR");
								this.endCell();
								this.startCell(null);
									this.action("desc", "", "balanceleadslist", "mainform", "", "balanceleadslist", "ui-icon-arrowstop-1-n", "", "ORDERBY=ORDER BY SERVICEID DESC, BALANCE");
								this.endCell();
							this.endRow();
						this.endTable();
					this.endColumn();
					this.startColumn("");
					this.startTable(null);
						this.startRow(null);
							this.startCell(new String[]{"style=font-size:0.6em"});
								this.respondString("LOAD NR");
							this.endCell();
						this.endRow();
					this.endTable();
				this.endColumn();
				this.endRow();
				for(int rowindex:balancePrioritySet.keySet()){
					if(rowindex==0) continue;
					this.startRow(null);
						this.startCell(new String[]{""});
							action("EDIT BALANCE LEADS SETTINGS","..", "balanceleads", "mainform", "", "accountbalanceconfigsection", "", "", "SELECTEDBALANCE="+balancePrioritySet.get(rowindex).get(1)+"&SELECTEDSERVICEID="+balancePrioritySet.get(rowindex).get(2)+"&SELECTEDLOADID="+balancePrioritySet.get(rowindex).get(3));
						this.endCell();
						this.startCell(new String[]{"style=border:solid 1px"});
							this.respondString(encodeHTML(balancePrioritySet.get(rowindex).get(1)));
						this.endCell();
						this.startCell(new String[]{"style=border:solid 1px"});
							this.respondString(encodeHTML(balancePrioritySet.get(rowindex).get(2)));
						this.endCell();
						this.startCell(new String[]{"style=border:solid 1px"});
							this.respondString(encodeHTML(balancePrioritySet.get(rowindex).get(3)));
						this.endCell();
					this.endRow();
				}
			this.endTable();
		}
		Database.cleanupDataset(balancePrioritySet);
		balancePrioritySet=null;
	}

	public void balanceprioritieslist() throws Exception{
		TreeMap<Integer,ArrayList<String>> balancePrioritySet=new TreeMap<Integer, ArrayList<String>>();
		Database.executeDBRequest(balancePrioritySet,"LANCETLEADSAUTOMATION", "SELECT * FROM <DBUSER>.LANCET_ACCOUNT_BALANCE_PRIORITY "+(this.requestParameter("ORDERBY").equals("")?" ORDER BY BALANCE DESC":this.requestParameter("ORDERBY")), null,null);
		if(balancePrioritySet!=null){
			this.startTable(new String[]{"cellpadding=0","cellspacing=0"});
				this.startRow(null);
					this.startColumn("");
					this.endColumn();
					this.startColumn("");
						this.startTable(null);
							this.startRow(null);
								this.startCell(null);
									this.action("asc", "", "balanceprioritieslist", "mainform", "", "balanceprioritieslist", "ui-icon-arrowstop-1-s", "", "ORDERBY=ORDER BY BALANCE ASC");
								this.endCell();
								this.startCell(new String[]{"style=font-size:0.6em"});
									this.respondString("BALANCE");
								this.endCell();
								this.startCell(null);
									this.action("desc", "", "balanceprioritieslist", "mainform", "", "balanceprioritieslist", "ui-icon-arrowstop-1-n", "", "ORDERBY=ORDER BY BALANCE DESC");
								this.endCell();
							this.endRow();
						this.endTable();
					this.endColumn();
					this.startColumn("");
						this.startTable(null);
							this.startRow(null);
								this.startCell(null);
									this.action("asc", "", "balanceprioritieslist", "mainform", "", "balanceprioritieslist", "ui-icon-arrowstop-1-s", "", "ORDERBY=ORDER BY PRIORITY ASC");
								this.endCell();
								this.startCell(new String[]{"style=font-size:0.6em"});
									this.respondString("PRIORITY");
								this.endCell();
								this.startCell(null);
									this.action("desc", "", "balanceprioritieslist", "mainform", "", "balanceprioritieslist", "ui-icon-arrowstop-1-n", "", "ORDERBY=ORDER BY PRIORITY DESC");
								this.endCell();
							this.endRow();
						this.endTable();
					this.endColumn();
				this.endRow();
				for(int rowindex:balancePrioritySet.keySet()){
					if(rowindex==0) continue;
					this.startRow(null);
						this.startCell(new String[]{"style=vertical-ailgn:top"});
							this.action("EDIT BALANCE SETTINGS","..", "balancepriorities", "", "", "accountbalanceconfigsection", "", "", "SELECTEDBALANCE="+balancePrioritySet.get(rowindex).get(1)+"&SELECTEDPRIORITY="+balancePrioritySet.get(rowindex).get(2));
						this.endCell();
						this.startCell(new String[]{"style=border:solid 1px"});
							this.respondString(encodeHTML(balancePrioritySet.get(rowindex).get(1)));
						this.endCell();
						this.startCell(new String[]{"style=border:solid 1px"});
							this.respondString(encodeHTML(balancePrioritySet.get(rowindex).get(2)));
						this.endCell();
					this.endRow();
				}
			this.endTable();
		}
		Database.cleanupDataset(balancePrioritySet);
		balancePrioritySet=null;
	}

	public void newbalancepriority() throws Exception{
		HashMap<String,String> newBalancePriority=new HashMap<String,String>();
		this.importRequestParametersIntoMap(newBalancePriority, null);
		ArrayList<String> validationErrors=new ArrayList<String>();
		
		if(this.requestParameter("NEWBALANCE").equals("")){
			validationErrors.add("NO BALANCE ENTERED");
		}
		else if(this.requestParameter("NEWBALANCE").equals("0")){
			validationErrors.add("BALANCE MUST BE GREATER THAN 0");
		}
		if(this.requestParameter("NEWPRIORITY").equals("")){
			validationErrors.add("NO PRIORITY ENTERED");
		}
		
		newBalancePriority.put("BALANCE_PRIORITYCOUNT", "0");
		
		Database.executeDBRequest(null,"LANCETLEADSAUTOMATION", "SELECT COUNT(*) AS BALANCE_PRIORITYCOUNT FROM LANCET_ACCOUNT_BALANCE_PRIORITY WHERE BALANCE<>ROUND(:NEWBALANCE,2) AND PRIORITY=:NEWPRIORITY",newBalancePriority,null);
		if(!newBalancePriority.get("BALANCE_PRIORITYCOUNT").equals("0")){
			validationErrors.add("A BALANCE IS ALREADY ALLOCATED WITH THE SAME PRIORITY");
		}
		
		if(validationErrors.isEmpty()){
			Database.executeDBRequest(null,"LANCETLEADSAUTOMATION", "SELECT COUNT(*) AS BALANCE_PRIORITYCOUNT FROM LANCET_ACCOUNT_BALANCE_PRIORITY WHERE BALANCE=ROUND(:NEWBALANCE,2)",newBalancePriority,null);
			if(newBalancePriority.get("BALANCE_PRIORITYCOUNT").equals("0")){
				Database.executeDBRequest(null,"LANCETLEADSAUTOMATION", "INSERT INTO <DBUSER>.LANCET_ACCOUNT_BALANCE_PRIORITY (BALANCE,PRIORITY) VALUES(:NEWBALANCE,:NEWPRIORITY)", newBalancePriority,null);
			}
			else{
				Database.executeDBRequest(null,"LANCETLEADSAUTOMATION", "UPDATE <DBUSER>.LANCET_ACCOUNT_BALANCE_PRIORITY SET BALANCE=:NEWBALANCE ,PRIORITY=:NEWPRIORITY WHERE ID = (SELECT TOP 1 ID FROM <DBUSER>.LANCET_ACCOUNT_BALANCE_PRIORITY WHERE BALANCE=:NEWBALANCE )", newBalancePriority,null);
			}
		}
		
		
		if(!validationErrors.isEmpty()){
			this.showDialog("",new String[]{ "title=NEW BALANCE PRIORITY","contentid=newbalanceprioritydlg","BUTTON:CONFIRM="});
			this.replaceComponentContent("newbalanceprioritydlg");
				this.startElement("ul", new String[]{"class=ui-state-error"}, true);
					while(!validationErrors.isEmpty()){
						this.startElement("li", null, true);
							this.respondString(validationErrors.remove(0));
						this.endElement("li", true);
					}
				this.endElement("ul", true);
			this.endReplaceComponentContent();
		}
		else{
			this.replaceComponentContent("balanceprioritieslist");
				this.balanceprioritieslist();
			this.endReplaceComponentContent();
		}
		
	}
	
	public void newbalancelead() throws Exception{
		HashMap<String,String> newBalancePriority=new HashMap<String,String>();
		this.importRequestParametersIntoMap(newBalancePriority, null);
		ArrayList<String> validationErrors=new ArrayList<String>();
		
		if(this.requestParameter("NEWBALANCE").equals("")){
			validationErrors.add("NO BALANCE ENTERED");
		}
		else if(this.requestParameter("NEWBALANCE").equals("0")){
			validationErrors.add("BALANCE MUST BE GREATER THAN 0");
		}
		if(this.requestParameter("NEWSERVICEID").equals("")){
			validationErrors.add("NO CAMPAIGN NR ENTERED");
		}
		else if(this.requestParameter("NEWSERVICEID").equals("0")){
			validationErrors.add("CAMPAIGN NR MUST BE GREATER THAN 0");
		}
		if(this.requestParameter("NEWLOADID").equals("")){
			validationErrors.add("NO CAMPAIGN LOAD NR ENTERED");
		}
		else if(this.requestParameter("NEWLOADID").equals("0")){
			validationErrors.add("CAMPAIGN LOAD NR MUST BE GREATER THAN 0");
		}
		
		
		newBalancePriority.put("BALANCE_LOADCOUNT", "0");
		Database.executeDBRequest(null,"LANCETLEADSAUTOMATION", "SELECT COUNT(*) AS BALANCE_LOADCOUNT FROM LANCET_ACCOUNT_BALANCE_CAMPAIGN_LOAD_MAP WHERE BALANCE<>ROUND(:NEWBALANCE,2) AND SERVICEID=:NEWSERVICEID AND LOADID=:NEWLOADID",newBalancePriority,null);
		if(!newBalancePriority.get("BALANCE_LOADCOUNT").equals("0")){
			validationErrors.add("CAMPAIGN AND LOAD NR ALREADY ALLOCATED To ANOTHER BALANCE");
		}
		if(validationErrors.isEmpty()){
			Database.executeDBRequest(null,"LANCETLEADSAUTOMATION", "SELECT COUNT(*) AS BALANCE_LOADCOUNT FROM LANCET_ACCOUNT_BALANCE_CAMPAIGN_LOAD_MAP WHERE BALANCE=ROUND(:NEWBALANCE,2)",newBalancePriority,null);
			if(newBalancePriority.get("BALANCE_LOADCOUNT").equals("0")){
				Database.executeDBRequest(null,"LANCETLEADSAUTOMATION", "INSERT INTO <DBUSER>.LANCET_ACCOUNT_BALANCE_CAMPAIGN_LOAD_MAP (BALANCE,SERVICEID,LOADID) VALUES(:NEWBALANCE,:NEWSERVICEID,:NEWLOADID)", newBalancePriority,null);
			}
			else{
				Database.executeDBRequest(null,"LANCETLEADSAUTOMATION", "UPDATE <DBUSER>.LANCET_ACCOUNT_BALANCE_CAMPAIGN_LOAD_MAP SET BALANCE=:NEWBALANCE ,SERVICEID=:NEWSERVICEID ,LOADID=:NEWLOADID WHERE ID = (SELECT TOP 1 ID FROM <DBUSER>.LANCET_ACCOUNT_BALANCE_CAMPAIGN_LOAD_MAP WHERE BALANCE=:NEWBALANCE )", newBalancePriority,null);
			}
		}
		
		if(!validationErrors.isEmpty()){
			this.showDialog("",new String[]{ "title=NEW BALANCE CAMPAIGN LOAD","contentid=newbalanceprioritydlg","BUTTON:CONFIRM="});
			this.replaceComponentContent("newbalanceprioritydlg");
				this.startElement("ul", new String[]{"class=ui-state-error"}, true);
					while(!validationErrors.isEmpty()){
						this.startElement("li", null, true);
							this.respondString(validationErrors.remove(0));
						this.endElement("li", true);
					}
				this.endElement("ul", true);
			this.endReplaceComponentContent();
		}
		else{
			this.replaceComponentContent("balanceleadslist");
				this.balanceleadslist();
			this.endReplaceComponentContent();
		}
		
	}
}
