package inovo.presence.web;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import inovo.presence.LeadsAutomation;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class Administrator extends InovoHTMLPageWidget {

	public Administrator(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}
	
	@Override
	public void pageContent() throws Exception {
		ArrayList<String> adminInterfaceActions=new ArrayList<String>();
		
		adminInterfaceActions.add("isServiceLoadEnabled=serviceid:int;loadid:int;return:boolean");
		
		adminInterfaceActions.add("reloadServicesBuffers=serviceids:string;return:boolean");
		
		adminInterfaceActions.add("clearServiceLoad=serviceid:string;loadid:string;return:string");
		
		adminInterfaceActions.add("asyncReloadServicesBuffers=serviceids:string;return:boolean");
		
		adminInterfaceActions.add("loadCall=loadrequesttype:int;serviceid:int;loadid:int;sourceid:int;callername:string;comments:string;agentloginid:int;scheduledate:datetime;priority:int;phone1:string;phone2:string;phone3:string;phone4:string;phone5:string;phone6:string;phone7:string;phone8:string;phone9:string;phone10:string;metafield1:string;metafield2:string;metafield3:string;metafield4:string;metafield5:string;metafield6:string;metafield7:string;metafield8:string;metafield9:string;metafield10:string;metafield11:string;metafield12:string;metafield13:string;metafield14:string;metafield16:string;metafield17:string;metafield18:string;metafield19:string;metafield20:string;fieldstomodify:string;return:string".toUpperCase());
		
		adminInterfaceActions.add("addCall=serviceid:int;loadid:int;sourceid:int;callername:string;comments:string;agentloginid:int;scheduledate:datetime;priority:int;phone1:string;phone2:string;phone3:string;phone4:string;phone5:string;phone6:string;phone7:string;phone8:string;phone9:string;phone10:string;metafield1:string;metafield2:string;metafield3:string;metafield4:string;metafield5:string;metafield6:string;metafield7:string;metafield8:string;metafield9:string;metafield10:string;metafield11:string;metafield12:string;metafield13:string;metafield14:string;metafield16:string;metafield17:string;metafield18:string;metafield19:string;metafield20:string;return:string".toUpperCase());
		
		adminInterfaceActions.add("modifyCall=serviceid:int;loadid:int;sourceid:int;callername:string;comments:string;agentloginid:int;scheduledate:datetime;phone1:string;phone2:string;phone3:string;phone4:string;phone5:string;phone6:string;phone7:string;phone8:string;phone9:string;phone10:string;metafield1:string;metafield2:string;metafield3:string;metafield4:string;metafield5:string;metafield6:string;metafield7:string;metafield8:string;metafield9:string;metafield10:string;metafield11:string;metafield12:string;metafield13:string;metafield14:string;metafield16:string;metafield17:string;metafield18:string;metafield19:string;metafield20:string;fieldstomodify:string;return:string".toUpperCase());
		
		adminInterfaceActions.add("removeCall=serviceid:int;loadid:int;sourceid:int;return:string".toUpperCase());
		
		this.startTable("");
			this.startRow("");
				this.startColumn("");
					this.respondString("PRESENCE ADMINISTRATOR INTERFACE ACTIONS");
				this.endCell();
			this.endRow();
			this.startRow("");
				this.startCell("");
					this.startTable("");
						startRow("");
							this.startCell(new String[]{"style=vertical-align:top"});
							this.startForm("action_", "", "");this.endForm();
							int adminInterfaceActionsSize=(adminInterfaceActions.size()%2==0?adminInterfaceActions.size():adminInterfaceActions.size()+1);
								while(!adminInterfaceActions.isEmpty()){
									String adminInterfaceAction=adminInterfaceActions.remove(0);
									String actionName=adminInterfaceAction.substring(0,adminInterfaceAction.indexOf("="));
									adminInterfaceAction=adminInterfaceAction.substring(adminInterfaceAction.indexOf("=")+1);
									this.startForm("action_"+actionName, "", "");
										this.startTable("");
											this.startRow("");
												this.startCell("");
													this.action("","TEST", "testaction", "action_"+actionName, this.getClass().getName(), "return_"+actionName, "", "", "adminaction="+actionName);
												this.endCell();
												this.startColumn("");
													this.respondString("ACTION:"+encodeHTML(actionName));
												this.endColumn();
											this.endRow();
										this.endTable();
										this.startTable("");
											this.startRow("");
												this.startCell(new String[]{"style=vertical-align:top"});
													String responseType="";
														this.startTable(new String[]{"class=ui-widget-content"});																
															for(String actionParam:adminInterfaceAction.split(";")){
																if(actionParam.indexOf(":")>-1){
																	if(actionParam.startsWith("return:")){
																		responseType=actionParam.substring(actionParam.indexOf(":")+1).trim();
																	}
																	else{
																		this.startRow("");
																			this.startCell("");
																				this.fieldLabel(actionParam.substring(0,actionParam.indexOf(":")).trim().toLowerCase());
																			this.endCell();
																			this.startCell("");
																				this.fieldInput(actionName+"_"+actionParam.substring(0,actionParam.indexOf(":")).trim().toLowerCase(), "", "text", true);
																			this.endCell();
																		this.endRow();
																	}
																}
															}
															this.startRow("");
																this.startColumn("");
																	this.respondString("return");
																this.endColumn();
																this.startCell(new String[]{"class=ui-widget-header","id=return_"+actionName});
																	this.respondString(responseType);
																this.endCell();
															this.endRow();
														this.endTable();
													
												this.endCell();
											this.endRow();
										this.endTable();
									this.endForm();
									if(adminInterfaceActions.size()%2==0){
											this.endCell();
										this.endRow();
										this.startRow("");
											this.startCell("");
									}
									else{
										this.endCell();
										this.startCell("");
									}
								}
								if(adminInterfaceActionsSize==1){
									this.endCell();
									this.startCell("");
								}
						adminInterfaceActions.clear();
						this.endCell();
					this.endRow();
				
				this.endTable();
			this.endCell();
		this.endRow();
		this.endTable();
	}
	
	public void testaction() throws Exception{
		HashMap<String,Object> adminActionParams=new HashMap<String,Object>();
		importRequestParametersIntoMap(adminActionParams, "");
		String adminaction=this.requestParameter("adminaction");
		for(String possibaleActionParam:adminActionParams.keySet()){
			if(possibaleActionParam.toUpperCase().startsWith(adminaction.toUpperCase()+"_")){
				this.setRequestParameter(possibaleActionParam.toUpperCase().substring((adminaction+"_").length()), adminActionParams.get(possibaleActionParam).toString(), true);
			}
		}
		this.executeWidgetMethod(adminaction);
	}
	
	public void reloadServicesBuffers() throws Exception{
		
		if(this.requestParameter("SERVICEIDS").equals("")){
			this.respondString("false");
		}
		else{
			try{
				if(presence.Administrator.administrator(this.requestParameter("ENVIRONMENTPATH"), this.requestParameter("PRESENCESERVERIP")).reloadServicesBuffers(this.requestParameter("SERVICEIDS"), true)){
					this.respondString("true");
				}
				else{
					this.respondString("false");
				}
			}
			catch(Exception e){
				this.respondString("error:"+e.getMessage());
			}
		}
	}
	
	public void isServiceLoadEnabled() throws Exception{
		if(this.requestParameter("SERVICEID").equals("")||this.requestParameter("LOADID").equals("")){
			this.respondString("false");
		}
		else{
			try{
				if(presence.Administrator.administrator("","").isServiceLoadEnabled(this.requestParameter("SERVICEID"), this.requestParameter("LOADID"),true)){
					this.respondString("true");
				}
				else{
					this.respondString("false");
				}
			}
			catch(Exception e){
				this.respondString("error:"+e.getMessage());
			}
		}
	}
	
	//ASYNC CALLS
	public void asyncReloadServicesBuffers() throws Exception{
		if(this.requestParameter("SERVICEIDS").equals("")){
			this.respondString("false");
		}
		else{
			try{
				if(presence.Administrator.administrator(this.requestParameter("ENVIRONMENTPATH"),this.requestParameter("PRESENCESERVERIP")).reloadServicesBuffers(this.requestParameter("SERVICEIDS"), true,true)){
					this.respondString("true");
				}
				else{
					this.respondString("false");
				}
			}
			catch(Exception e){
				this.respondString("error:"+e.getMessage());
			}
		}
	}
	
	public void addCall() throws Exception{
		this.setRequestParameter("LOADREQUESTTYPE", "1", true);
		this.loadCall();
	}
	
	public void clearServiceLoad() throws Exception{
		this.setRequestParameter("LOADREQUESTTYPE", "4", true);
		this.setRequestParameter("SOURCEID", "0", true);
		this.loadCall();
	}
	
	public void modifyCall() throws Exception{
		this.setRequestParameter("LOADREQUESTTYPE", "3", true);
		this.loadCall();
	}
	
	public void removeCall() throws Exception{
		this.setRequestParameter("LOADREQUESTTYPE", "2", true);
		this.loadCall();
	}
	
	public void loadCall() throws Exception{
		HashMap<String,Object> uploadCallparams=new HashMap<String,Object>();
		if(!this.requestParameter("SCHEDULEDDATE").equals("")){
			uploadCallparams.put("SCHEDULEDATE", this.requestParameter("SCHEDULEDDATE"));
		}
		if(!this.requestParameter("SCHEDULEDCALL").equals("")){
			uploadCallparams.put("SCHEDULEDATE", this.requestParameter("SCHEDULEDCALL"));
		}
		this.importRequestParametersIntoMap(uploadCallparams, "loadrequesttype,serviceid,loadid,sourceid,callername,comments,agentloginid,scheduledate,scheduleddate,priority,phone1,phone2,phone3,phone4,phone5,phone6,phone7,phone8,phone9,phone10,metafield1,metafield2,metafield3,metafield4,metafield5,metafield6,metafield7,metafield8,metafield9,metafield10,metafield11,metafield12,metafield13,metafield14,metafield16,metafield17,metafield18,metafield19,metafield20,fieldstomodify");
		this.respondString(LeadsAutomation.leadsAutomation("", "").loadCall(uploadCallparams));
	}
}
