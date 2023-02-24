package inovo.qa;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class QAGroupManager extends InovoHTMLPageWidget {

	public QAGroupManager(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}
	
	public void confirmlogin() throws Exception{
		HashMap<String, Object> settings=new HashMap<String,Object>();
		this.importRequestParametersIntoMap(settings, "USERNAME,PASSWORD");
		ArrayList<String> errors=new ArrayList<String>();
		if(settings.get("USERNAME").equals("")||settings.get("PASSWORD").equals("")) {
			errors.add("No username or password entered");
		}
		
		if(errors.isEmpty()) {
			settings.put("FIRSTNAME","");
			settings.put("LASTNAME","");
			settings.put("LOGIN","0");
			settings.put("ENABLED","N");
			this.dbquery("QAGROUPMANAGER", "SELECT top 1 id as LOGIN,USERNAME,PASSWORD,FIRSTNAME,LASTNAME,ENABLED FROM <DBUSER>.QA_SYSTEMLOGIN WHERE username=:USERNAME and password=:PASSWORD", settings,null);
			if(!settings.get("LOGIN").toString().equals("0")) {
				if(settings.get("ENABLED").toString().equals("Y")) {
					this.setRequestParameter("LOGIN", settings.get("LOGIN").toString(), true);
					this.setRequestParameter("LOGINNAME",settings.get("FIRSTNAME").toString()+" "+settings.get("LASTNAME").toString(),true);
					this.replaceComponentContent("mainsection");
						this.respond.print("Logged in as: "+settings.get("FIRSTNAME").toString()+" "+settings.get("LASTNAME").toString());
						this.pageContent();
					this.endReplaceComponentContent();
				} else {
					this.replaceComponentContent("loginstatus");
					this.respond.print("access denied login disabled");
				this.endReplaceComponentContent();
				}
			} else {
				this.replaceComponentContent("loginstatus");
					this.respond.print("access denied invalid login details");
				this.endReplaceComponentContent();
			}
		} else {
			this.replaceComponentContent("loginstatus");
				for(String err:errors) {
					this.respond.print(err);this.simpleElement("br");
				}
			this.endReplaceComponentContent();
		}
	}
	
	@Override
	public void pageContent() throws Exception {
		HashMap<String, Object> settings=new HashMap<String,Object>();
		this.importRequestParametersIntoMap(settings, "LOGIN,LOGINNAME");
		if(!settings.containsKey("LOGIN")||settings.get("LOGIN").equals("")) {
			this.startComplexElement("div","id=mainsection");
				this.startTable();
					this.startRow();
						this.startColumn();this.respond.print("USERNAME");this.endColumn();
						this.startCell();
							this.fieldInput("USERNAME", "", "text", true);
						this.endCell();
					this.endRow();
					this.startRow();
						this.startColumn();this.respond.print("PASSWORD");this.endColumn();
						this.startCell();
							this.fieldInput("PASSWORD", "", "password", true);
						this.endCell();
					this.endRow();
					this.startRow();
						this.startCell("colspan=2");
							this.action("LOGIN", "confirmlogin", "", "", "", "", "", "");
						this.endCell();
					this.endRow();
					this.startRow();
					this.startCell("colspan=2|id=loginstatus"); this.endCell();
				this.endRow();
				this.endTable();
			this.endComplexElement("div");
		} else {
			this.fieldHidden("REMOTEHOST",this.requestHeader("REMOTE-HOST"));
			this.fieldHidden("LOGIN", settings.get("LOGIN").toString());
			this.fieldHidden("LOGINNAME", settings.get("LOGINNAME").toString());
			this.startTable();
				this.startRow();
					this.startCell("id=qagroups|style=vertical-align:top");
						this.listGroups();
					this.endCell();
					this.startCell("id=qagroupagents|style=vertical-align:top");
					
					this.endCell();
				this.endRow();
			this.endTable();
		}
	}

	private boolean hasGroupData=false;
	public void listGroups() throws Exception{
		this.startTable();
			this.startRow();
				this.startColumn();
					this.respond.print("QA GROUPS");
				this.endColumn();
			this.endRow();
			this.dbquery("QAGROUPMANAGER", "SELECT id,GroupName FROM <DBUSER>.QA_GROUP", null, "listGroupsData");
		this.endTable();
	}
	
	public void listGroupsData(boolean lastRec,long recindex,List<Object> data,List<Object> columns) throws Exception{
		if(recindex>0) {
			if(!this.hasGroupData) {
				this.hasGroupData=true;
			}
			this.startRow();
				this.startCell();
					this.startTable();
						this.startRow();
							this.startCell();this.action("...", "qagroupagents", "", "", "qagroupagents", "", "", "ACTION=SELECT&QA_GROUPID="+data.get(0).toString()); this.endCell();this.startCell(); this.respond.print(data.get(1).toString()); this.endCell();
						this.endRow();	
					this.endTable();
				this.endCell();
			this.endRow();
		}
	}
	
	public void qagroupagents() throws Exception{
		HashMap<String, Object> settings=new HashMap<String,Object>();
		this.importRequestParametersIntoMap(settings, "QA_GROUPID");
		this.fieldHidden("QA_GROUPID",this.requestParameter("QA_GROUPID"));
		settings.put("GROUPNAME", "");
		this.dbquery("QAGROUPMANAGER", "SELECT ID,GROUPNAME FROM <DBUSER>.QA_GROUP WHERE ID=:QA_GROUPID", settings, "");
		this.startTable("width:100%");
			this.startRow();
				this.startColumn();
					this.respond.print("GROUP: "+settings.get("GROUPNAME"));
				this.endColumn();
			this.endRow();
			this.startRow();
				this.startCell("id=qagroupagentslayout");
					this.qagroupagentslayout();
				this.endCell();
			this.endRow();
		this.endTable();
	}
	
	public void qagroupagentslayout() throws Exception{
		this.startTable();
			this.startRow();
				this.startColumn();
					this.startTable();
						this.startRow();
							this.startCell("norwap=norwap");
								this.respond.print("AGENT(s) in GROUP "); this.action(">>", "unallocateselectedgroupagents", "", "", "", "", "", "");
							this.endCell();
						this.endRow();
					this.endTable();
				this.endColumn();
				this.startColumn();
					this.startTable();
						this.startRow();
							this.startCell("norwap=norwap");
								this.action("<<", "allocateselectedgroupagents", "", "", "", "", "", ""); this.respond.print(" REMAINING AGENT(s)");
							this.endCell();
						this.endRow();
					this.endTable();
				this.endColumn();
			this.endRow();
			this.startRow();
				this.startCell("style=vertical-align:top");
					this.allocatedgroupagents();
				this.endCell();
				this.startCell("style=vertical-align:top");
					this.startTable();
						this.startRow();
							this.startColumn();	this.respond.print("AVAILABLE"); this.endColumn();
							this.startColumn();	this.respond.print("ALLOCATED");this.endColumn();
						this.endRow();
						this.startRow();
							this.startCell("style=vertical-align:top");
								this.availablegroupagents();
							this.endCell();
							this.startCell("style=vertical-align:top");
								this.unallocatedgroupagents();
							this.endCell();
						this.endRow();
					this.endTable();
				this.endCell();
			this.endRow();
		this.endTable();	
	}
	
	public void allocatedgroupagents()  throws Exception{
		HashMap<String, Object> settings=new HashMap<String,Object>();
		this.importRequestParametersIntoMap(settings, "QA_GROUPID");
		this.hasGroupData=false;
		this.startTable();
			this.dbquery("QAGROUPMANAGER", "SELECT AGENTNAME,LOGIN,GROUPID,GROUPNAME,STATUS FROM PTOOLS.fnQA_AGENTS_RELATED_TO_QA_GROUP(:QA_GROUPID,'A','') ORDER BY AGENTNAME", settings, "allocatedgroupagentsdata");
		this.endTable();
	}
	
	public void allocatedgroupagentsdata(boolean lastRec,long recindex,List<Object> data,List<Object> columns) throws Exception{
		if (recindex==0) {
			
		} else {
			this.startRow();
				this.startCell();
					this.fieldInput("groupagent", data.get(1).toString(), "checkbox", true);
				this.endCell();
				this.startColumn("font-size:0.8em");
					this.startTable();this.startRow();
						this.startCell("nowrap=nowrap");
							this.respond.print("["+data.get(1)+"] "+ data.get(0));
						this.endCell();
					this.endRow();this.endTable();
				this.endColumn();
			this.endRow();
			if(!this.hasGroupData) {
				this.hasGroupData=true;
			}
		}
	}
	
	public void availablegroupagents()  throws Exception{
		HashMap<String, Object> settings=new HashMap<String,Object>();
		this.importRequestParametersIntoMap(settings, "QA_GROUPID");
		settings.put("AGENTS", "");
		this.dbquery("PRESENCE", "SELECT '<AGENTS>'+(SELECT PA.NAME AS AGENTNAME,PL.LOGIN FROM PREP.PCO_AGENT PA INNER JOIN PREP.PCO_LOGINAGENT PAL ON PA.ID=PAL.AGENTID INNER JOIN PREP.PCO_LOGIN PL ON PAL.LOGIN=PL.LOGIN ORDER BY AGENTNAME FOR XML RAW ('AGENT'),ELEMENTS)+'</AGENTS>' AS AGENTS", settings,"");
		
		this.hasGroupData=false;
		this.startTable();
			this.dbquery("QAGROUPMANAGER", "SELECT AGENTNAME,LOGIN,GROUPID,GROUPNAME,STATUS FROM PTOOLS.fnQA_AGENTS_RELATED_TO_QA_GROUP(:QA_GROUPID,'',:AGENTS) ORDER BY AGENTNAME", settings, "availablegroupagentsdata");
		this.endTable();
	}
	
	public void availablegroupagentsdata(boolean lastRec,long recindex,List<Object> data,List<Object> columns) throws Exception{
		if (recindex==0) {
			
		} else {
			if(!lastRec&&!this.hasGroupData) {
				this.hasGroupData=true;
				this.startTBody("style=display:block;height:"+String.valueOf(30*16)+"px;overflow:auto;overflow-y:scroll;");
			}
			this.startRow();
				this.startCell();
					this.fieldInput("availableagent", data.get(1).toString()+"|"+data.get(0).toString(), "checkbox", true);
				this.endCell();
				this.startColumn("font-size:0.8em");
				this.respond.print("["+data.get(1).toString()+"] "+data.get(0));
				this.endColumn();
			this.endRow();
			if(lastRec&&this.hasGroupData) {
				this.endTBody();
			}
		}
	}
	
	public void unallocatedgroupagents()  throws Exception{
		HashMap<String, Object> settings=new HashMap<String,Object>();
		this.importRequestParametersIntoMap(settings, "QA_GROUPID");
		this.hasGroupData=false;
		this.startTable("style=display:block;overflow:auto;height:"+String.valueOf(30*16)+"px;");
			this.startTHead();
				this.startRow();
					this.startColumn();
						this.respond.print("AGENT");
					this.endColumn();
					this.startColumn();
						this.respond.print("LOGIN");
					this.endColumn();
					this.startColumn();
					this.endColumn();
					this.startColumn();
						this.respond.print("QA GROUP");
					this.endColumn();
				this.endRow();
			this.endTHead();
			this.dbquery("QAGROUPMANAGER", "SELECT AGENTNAME,LOGIN,GROUPID,GROUPNAME,STATUS FROM PTOOLS.fnQA_AGENTS_RELATED_TO_QA_GROUP(:QA_GROUPID,'N','') ORDER BY GROUPNAME, AGENTNAME", settings, "unallocatedgroupagentsdata");
		this.endTable();
	}
	
	public void unallocatedgroupagentsdata(boolean lastRec,long recindex,List<Object> data,List<Object> columns) throws Exception{
		if (recindex==0) {
			
		} else {
			if(!lastRec&&!this.hasGroupData) {
				this.hasGroupData=true;
				this.startTBody("style=overflow:auto;overflow-y:scroll;");
			}
			this.startRow();
				this.startColumn("font-size:0.8em");
					this.respond.print(data.get(0));
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respond.print(data.get(1));
				this.endColumn();
				this.startColumn("font-size:0.8em");
					//this.respond.print(data.get(2));
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respond.print(data.get(3));
				this.endColumn();
			this.endRow();
			if(lastRec&&this.hasGroupData) {
				this.endTBody();
			}
		}
	}

	public void unallocateselectedgroupagents() throws Exception{
		HashMap<String,Object> settings=new HashMap<String, Object>();
		this.importRequestParametersIntoMap(settings, "QA_GROUPID,REMOTEHOST,LOGINNAME");
		List<String> ALLOCATEDLOGINS=this.requestParameterArray("GROUPAGENT");
		if(ALLOCATEDLOGINS!=null && !ALLOCATEDLOGINS.isEmpty()) {
			StringBuilder strbld=new StringBuilder();
			strbld.append("<AGENTS>");
			for(String alogin:ALLOCATEDLOGINS) {
				strbld.append("<AGENT><LOGIN>"+alogin+"</LOGIN></AGENT>");
			}
			strbld.append("</AGENTS>");
			settings.put("AGENTS", strbld.toString());
			strbld.delete(0, strbld.length());
			strbld=null;
			settings.put("ACTIONEDBY", settings.get("REMOTEHOST").toString()+" "+settings.get("LOGINNAME").toString());
			this.dbquery("QAGROUPMANAGER", "EXECUTE <DBUSER>.spREMOVE_QA_AGENTS :ACTIONEDBY,:QA_GROUPID,:AGENTS", settings,"");
		}
		this.replaceComponentContent("qagroupagentslayout");
			this.qagroupagentslayout();
		this.endReplaceComponentContent();
	}

	public void allocateselectedgroupagents() throws Exception{
		HashMap<String,Object> settings=new HashMap<String, Object>();
		this.importRequestParametersIntoMap(settings, "QA_GROUPID,REMOTEHOST,LOGINNAME");
		List<String> AVAILABLELOGINS=this.requestParameterArray("AVAILABLEAGENT");
		if(AVAILABLELOGINS!=null && !AVAILABLELOGINS.isEmpty()) {
			StringBuilder strbld=new StringBuilder();
			strbld.append("<AGENTS>");
			for(String alogin:AVAILABLELOGINS) {
				strbld.append("<AGENT><LOGIN>"+alogin.replace("|", "</LOGIN><AGENTNAME>")+"</AGENTNAME></AGENT>");
			}
			strbld.append("</AGENTS>");
			settings.put("AGENTS", strbld.toString());
			strbld.delete(0, strbld.length());
			strbld=null;
			settings.put("ACTIONEDBY", settings.get("REMOTEHOST").toString()+" "+settings.get("LOGINNAME").toString());
			this.dbquery("QAGROUPMANAGER", "EXECUTE <DBUSER>.spADD_QA_AGENTS :ACTIONEDBY,:QA_GROUPID,:AGENTS", settings,"");
		}
		this.replaceComponentContent("qagroupagentslayout");
			this.qagroupagentslayout();
		this.endReplaceComponentContent();
	}
}
