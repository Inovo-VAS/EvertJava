package inovo.web;

import inovo.adhoc.AdhocUtils;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class InovoHTMLPageWidget extends InovoHTMLWebWidget
{
  private int _actioncounter = 0;

  public InovoHTMLPageWidget(InovoWebWidget parentWidget, InputStream inStream)
  {
    super(parentWidget, inStream);
  }

  public void executeContentWidget() throws Exception
  {
    respondString("<!DOCTYPE html>\r\n");
    startPage();
    startHead();
    String useragent = requestHeader("USER-AGENT");
    //if (useragent.contains("MSIE")) {
    //  startStyling(null, "");
    //  String urlimageref = requestHeader("URLREQUEST") + "?embeddedresource=/jquery/images/";
    //  respondBytes(AdhocUtils.inputSteamToBytesOutputStream(getClass().getResourceAsStream("/jquery/jquery.ui.1.10.2.css")).toString().replaceAll("images/", urlimageref).getBytes());
    //  endStyling();
    //}
    //else {
      startElement("link", new String[] { "rel=stylesheet", "type=text/css", "href=" + requestHeader("URLREQUEST") + "?embeddedresource=jquery.ui.css" }, false); endElement("link", false);
    //}
      for (String addlink:this.additionalLinks()) {
    	  startElement("link", new String[] { "rel=stylesheet", "type=text/css", "href=" + requestHeader("URLREQUEST") + "?embeddedresource="+addlink }, false); endElement("link", false);
      }
    startScript(new String[] { "src=?embeddedresource=jquery.js" }, ""); endScript();

    startScript(new String[] { "src=?embeddedresource=jquery.ui.js" }, ""); endScript();
    for (String addscript:this.additionalScripts()) {
    	startScript(new String[] { "src=?embeddedresource="+addscript }, ""); endScript();
    }
    startStyling("", "");
    respondString("body {font-family:Arial;margin-top:0px}\r\n");
    this.additionalPageStyling();
    endStyling();
    endHead();
    startBody(null);
    String mainformid = getClass().getName().replaceAll("[.]", "_") + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    
    startScript("", "");
    respondString("\r\nvar mainformid=\"" + mainformid + "\";\r\n");
    endScript();
    
    startScript(new String[] { "src=?embeddedresource=inovo.web.script.js" }, "");
    endScript();
    
    startForm(mainformid, "multipart/form-data", "");
    
    pageTitle(getClass().getName());
    
    startScript("", "");
    	respondString("mainformid=\"" + mainformid + "\";");
    endScript();
    pageContent();
    
    startElement("div", new String[] { "id=maindialog", "style=display:none" }, true); endElement("div", true);
    endForm();

    
    endBody();
    endPage();
  }

  public String[] additionalLinks() {
	return new String[] {};
  }
  
  public String[] additionalScripts() {
	return new String[] {};
  }

public void additionalPageStyling() throws Exception{
  }

  public void showDialog(String dialogElementId, String properties) throws Exception {
	  this.showDialog(dialogElementId, properties.split("[|]"));
  }
  
	public void showDialog(String dialogElementId, String[] properties) throws Exception {
	    if (dialogElementId.equals("")) {
	      dialogElementId = "maindialog";
	    }
	    TreeMap<String,String> dialogProperties = new TreeMap<String,String>();
	    ArrayList<String> dialogButtons=new ArrayList<String>();
	    if (properties != null) {
	      for (String propitem : properties) {
	        if (propitem.indexOf("=") != -1) {
	        	if(propitem.trim().toUpperCase().startsWith("BUTTON:")){
	        		dialogButtons.add(propitem.trim());
	        	}
	        	else{
	        		dialogProperties.put(propitem.substring(0, propitem.indexOf("=")).trim(), propitem.substring(propitem.indexOf("=") + 1).trim());
	        	}
	        }
	      }
	    }
	    String dialogId = "";
	    if (dialogProperties == null) dialogProperties = new TreeMap<String,String>();
	    if (!dialogProperties.containsKey("id")) {
	      dialogProperties.put("id", dialogId = "dialog" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
	    }
	    else if (dialogProperties.containsKey("id")) {
	      dialogId = (String)dialogProperties.get("id");
	    }
	
	    dialogProperties.put("style", "font-size:0.8em");
	
	    replaceComponentContent(dialogElementId);
	
	    if (!((String)dialogProperties.get("id")).equals("maindialog"))
	    {
	      startElement("div", new String[] { "id=" + dialogId, dialogProperties.containsKey("title") ? "title=" + (String)dialogProperties.get("title") : "", dialogProperties.containsKey("style") ? "style=" + (String)dialogProperties.get("style") : "" }, true);
	      if (dialogProperties.containsKey("content")) {
	        startElement("p",(String[]) null, true); respondString(encodeHTML((String)dialogProperties.get("content"))); endElement("p", true);
	      }
	      else if (dialogProperties.containsKey("contentid")) {
	        startElement("p", new String[] { "id=" + (String)dialogProperties.get("contentid") }, true); endElement("p", true);
	      }
	      endElement("div", true);
	    }
	
	    String buttonsCode = "";
	    
	    String buttonsIconsCode="";
	    
	    int dialogbuttonindex=0;
	    for (String dialogButton : dialogButtons){
	        if (dialogButton.toUpperCase().startsWith("BUTTON:")) {
	        	dialogbuttonindex++;
	          String[] buttonproperties = dialogButton.substring(dialogButton.indexOf("=")+1) .split("\r\n");
	          
	            String currentButtonCode = "";
	            String currentButtonCaption = dialogButton.substring("BUTTON:".length(), dialogButton.indexOf("="));
	            String command = "";
	            String formid = "";
	            String altformid = "";
	            String widget = "";
	            String buttonicon="";
	
	            for (String buttonitem : buttonproperties) {
	              if (buttonitem.indexOf("=") != -1) {
	                String buttonprop = buttonitem.substring(0, buttonitem.indexOf("=")).trim().toLowerCase();
	                String buttonpropval = buttonitem.substring(buttonitem.indexOf("=") + 1).trim();
	                if (buttonprop.equals("command")) {
	                  command = buttonpropval;
	                }
	                else if (buttonprop.equals("form")) {
	                  formid = buttonpropval;
	                }
	                else if (buttonprop.equals("altform")) {
	                  altformid = buttonpropval;
	                }
	                else if (buttonprop.equals("widget")) {
	                  widget = buttonpropval;
	                  if (widget.equals("")) widget = getClass().getName();
	                }
	                else if (buttonprop.equals("icon")) {
	                    buttonicon = buttonpropval;
	                    buttonsIconsCode+=" $(\"#"+dialogId+"_"+String.valueOf(dialogbuttonindex)+ "\").button({ icons: { primary: \""+buttonicon+"\" } }); ";
	                  }
	              }
	            }
	            if (!command.equals("")) {
	              currentButtonCode = "webActionRequest(\"" + requestHeader("URLREQUEST") + "?" + (widget.equals("") ? "" : new StringBuilder("widget=").append(widget).append("&").toString()) + (command.equals("") ? "" : new StringBuilder("command=").append(command).toString()) + "\",\"" + (altformid.equals("") ? (formid.equals("")?command.equals("")?"":"mainform":formid) : altformid) + "\",\"\");";
	            }
	
	            if (!currentButtonCaption.equals(""))
	              buttonsCode = buttonsCode + "{style:\"font-size:0.6em;font-weight:bold\", text:\"" + currentButtonCaption + "\",id:\""+dialogId+"_"+String.valueOf(dialogbuttonindex)+"\", click:function(){" + currentButtonCode + " $( this ).dialog( \"close\" ); $(this).dialog( \"destroy\" ); } },";
	          }
	        
	      }
	
	    if (buttonsCode.equals("")) {
	      buttonsCode = "{style:\"font-size:0.6em;font-weight:bold\", text:\"Ok\",click:function(){$( this ).dialog( \"close\" ); $(this).dialog( \"destroy\" );} }";
	    }
	    else if (buttonsCode.endsWith(",")) {
	      buttonsCode = buttonsCode.substring(0, buttonsCode.length() - 1);
	    }
	
	    startScript("", "");
	    
	    respondString(
	      "$( \"#" + dialogId + "\" ).dialog({width:\"auto\",height:\"auto\" , autoOpen:true, modal:true," + 
	      "buttons:[" + 
	      buttonsCode + 
	      "]," +
	      "close: function(event, ui){$(this).dialog( \"destroy\" );},"+
	      "open: function(event, ui){ "+buttonsIconsCode+" $('.ui-dialog-title').css('font-size','0.6em');  $('.ui-dialog').css('z-index',1001); $('.ui-widget-overlay').css('z-index',1000); $(this).parent().appendTo($(\"#\"+mainformid));}"+
	      "}).position('left+10 top+10');");
	    endScript();
	
	    endReplaceComponentContent();
  }

  public void pageContent() throws Exception
  {
  }

  public String alternateEmbeddedResource(String embeddedResource) {
    if (embeddedResource.equals("inovo.web.script.js")) {
      return "/inovo/web/scripts/inovoweb.js";
    }

    return super.alternateEmbeddedResource(embeddedResource);
  }

  public String formatTitle(String title) {
    String formattedTitle = "";
    for (char ct : title.toCharArray()) {
      if (ct == '.') {
        formattedTitle = formattedTitle + ' ';
      }
      else
      {
        formattedTitle = formattedTitle + new String(new char[] { ct }).toUpperCase();
      }
    }

    return formattedTitle;
  }
  
  public String formatPageTitle(String title){
	  String formattedTitle = "";
	  	char prevct=0;
	    for (char ct : title.toCharArray()) {
	      if (("ABCDEFGHIJKLMNOPQRSTUVWXYZ").indexOf(ct)==-1 &&("ABCDEFGHIJKLMNOPQRSTUVWXYZ").indexOf(prevct)>-1) {
	        formattedTitle = formattedTitle.substring(0,formattedTitle.length()-1) +' '+ (formattedTitle.substring(formattedTitle.length()-1)+ct).toUpperCase();
	      }
	      else
	      {
	        formattedTitle = formattedTitle + new String(new char[] { ct }).toUpperCase();
	      }
	      prevct=ct;
	    }

	    return formattedTitle;  
  }

  public void pageTitle(String title) throws Exception {
    startElement("div", new String[] { "style=background-image: url(?embeddedresource=/inovo/images/inovo_new_header_r1_c3.jpg)" }, true);
	    startElement("div", new String[] { "style=width:990px;height:140px;background-repeat:no-repeat;background-image: url(?embeddedresource=/inovo/images/inovo_new_header_r1_c2.jpg);padding-left: 190px" }, true);
		    startElement("div", new String[] { "style=position:relative;top:50px;font-weight:bold" }, true); 
			    String preTitle="";
			    String postTitle="";
			    if(title.indexOf(".")>0){
			    	preTitle=formatTitle(title.substring(0,title.lastIndexOf(".")));
			    	postTitle=formatPageTitle(title.substring(title.lastIndexOf(".")+1));
			    	this.startElement("span", new String[]{"style=font-size:0.6em"},true);
			    		respondString(encodeHTML(preTitle)); 
			    	this.endElement("span", true);
			    	this.simpleElement("br", null);
			    }
			    else{
			    	postTitle=formatPageTitle(title);
			    }
		    
	    
			    this.startElement("span", new String[]{"style=font-size:1.2em"},true);
			    	respondString(encodeHTML(postTitle));
			    this.endElement("span", true);
	    	endElement("div", true);
	    endElement("div", true);
    endElement("div", true);
  }

  public void action(String caption, String command, String formid, String widget, String actiontarget, String starticon, String endicon, String urlparams) throws Exception{
	  this.action("", caption, command, formid, widget, actiontarget, starticon, endicon, urlparams);
  }
  
  public void action(String title,String caption, String command, String formid, String widget, String actiontarget, String starticon, String endicon, String urlparams)
    throws Exception
  {
	  title=(title==null?caption:title.equals("")?caption:title);
    String actionid = "cellaction" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + String.valueOf(++this._actioncounter);
    startElement("span", new String[] { "style=font-size:0.6em;font-weight:bold", "title=" + title, "id=" + actionid, "command=" + command, "formid=" + formid, "widget=" + widget, "actiontarget=" + actiontarget, "starticon=" + starticon, "endicon=" + endicon, "urlparams=" + urlparams }, true); respondString(encodeHTML(caption)); endElement("span", true);
    attachWebAction(actionid,false);
  }
  
  public void cellaction(String caption, String command, String formid, String widget, String actiontarget, String starticon, String endicon, String urlparams) throws Exception{
	  this.cellaction("", caption, command, formid, widget, actiontarget, starticon, endicon, urlparams);
  }
  
  public void cellaction(String title,String caption, String command, String formid, String widget, String actiontarget, String starticon, String endicon, String urlparams)
    throws Exception
  {
	  title=(title==null?caption:title.equals("")?caption:title);
    String actionid = "cellaction" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + String.valueOf(++this._actioncounter);
    startElement("td", new String[] { "style=font-size:0.8em;font-weight:bold;cursor:pointer", "title=" + title, "id=" + actionid, "command=" + command, "formid=" + formid, "widget=" + widget, "actiontarget=" + actiontarget, "starticon=" + starticon, "endicon=" + endicon, "urlparams=" + urlparams }, true); respondString(encodeHTML(caption)); endElement("td", true);
    attachWebAction(actionid,true);
  }

  public void actions(List<String[]> actionsProperties, boolean alignvertical) throws Exception {
    String caption = "";
    String title="";
    String command = "";
    String formid = "";
    String widget = "";
    String actiontarget = "";
    String starticon = "";
    String endicon = "";
    String urlparams = "";

    String actionPropName = "";
    String actionPropValue = "";
    if ((alignvertical) && (!actionsProperties.isEmpty())) {
      startTable("");
    }

    for (String[] actionproperties : actionsProperties) {
      caption = "";
      title="";
      command = "";
      widget = "";
      actiontarget = "";
      starticon = "";
      endicon = "";
      urlparams = "";

      for (String actionProperty : actionproperties) {
        if (actionProperty.indexOf("=") > -1) {
          actionPropName = actionProperty.substring(0, actionProperty.indexOf("=")).trim().toLowerCase();
          actionPropValue = actionProperty.substring(actionProperty.indexOf("=") + 1).trim();
          if (actionPropName.equals("caption")) {
            caption = actionPropValue;
          }
          else if (actionPropName.equals("title")) {
              title = actionPropValue;
          }
          else if (actionPropName.equals("command")) {
            command = actionPropValue;
          }
          else if (actionPropName.equals("widget")) {
            widget = actionPropValue;
          }
          else if (actionPropName.equals("actiontarget")) {
            actiontarget = actionPropValue;
          }
          else if (actionPropName.equals("starticon")) {
            starticon = actionPropValue;
          }
          else if (actionPropName.equals("endicon")) {
            endicon = actionPropValue;
          }
          else if (actionPropName.equals("urlparams")) {
            urlparams = actionPropValue;
          }
        }
      }
      if (alignvertical) {
        startRow("");
        startCell(new String[] { "style=verical-align:top", "align=right" });
      }
      action(title, caption, command, formid, widget, actiontarget, starticon, endicon, urlparams);
      if (alignvertical) {
        endCell();
        endRow();
      }
    }

    if ((alignvertical) && (!actionsProperties.isEmpty()))
      endTable();
  }
  
  public void actions_tabs(ArrayList<String[]> actionsProperties,String mastertargetelemid, boolean alignvertical) throws Exception {
	    String caption = "";
	    String title="";
	    String command = "";
	    String formid = "";
	    String widget = "";
	    String actiontarget = "";
	    String starticon = "";
	    String endicon = "";
	    String urlparams = "";

	    String actionPropName = "";
	    String actionPropValue = "";
	    
	    String tabsactioncontainer = "tabsactioncontainer" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + String.valueOf(++this._actioncounter);
	    boolean firsttab=false;
	    
	    this.startElement("div", new String[]{"id="+tabsactioncontainer}, true);

	    	this.startElement("ul",(String[])null, true);
	    
	    for (String[] actionproperties : actionsProperties) {
	      caption = "";
	      title="";
	      command = "";
	      widget = "";
	      actiontarget = mastertargetelemid;
	      starticon = "";
	      endicon = "";
	      urlparams = "";

	      for (String actionProperty : actionproperties) {
	        if (actionProperty.indexOf("=") > -1) {
	          actionPropName = actionProperty.substring(0, actionProperty.indexOf("=")).trim().toLowerCase();
	          actionPropValue = actionProperty.substring(actionProperty.indexOf("=") + 1).trim();
	          if (actionPropName.equals("caption")) {
	            caption = actionPropValue;
	          }
	          else if (actionPropName.equals("title")) {
	              title = actionPropValue;
	          }
	          else if (actionPropName.equals("command")) {
	            command = actionPropValue;
	          }
	          else if (actionPropName.equals("widget")) {
	            widget = actionPropValue;
	          }
	          else if (actionPropName.equals("actiontarget")) {
	            actiontarget = actionPropValue;
	          }
	          else if (actionPropName.equals("starticon")) {
	            starticon = actionPropValue;
	          }
	          else if (actionPropName.equals("endicon")) {
	            endicon = actionPropValue;
	          }
	          else if (actionPropName.equals("urlparams")) {
	            urlparams = actionPropValue;
	          }
	        }
	      }
	      if (alignvertical) {
	        //startRow(null);
	        //startCell(new String[] { "style=verical-align:top", "align=right" });
	      }
	      if(!firsttab){
	    	  firsttab=true;
	    	  if(title.equals("")){
	    		  this.startElement("li", new String[]{"class=firsttab_"+tabsactioncontainer,"style=font-size:0.6em;font-weight:bold","command="+command,"formid="+formid,"actiontarget="+actiontarget,"urlparams="+urlparams}, true);
	    	  }
	    	  else{
	    		  this.startElement("li", new String[]{"title="+title, "class=firsttab_"+tabsactioncontainer,"style=font-size:0.6em;font-weight:bold","command="+command,"formid="+formid,"actiontarget="+actiontarget,"urlparams="+urlparams}, true);
	    	  }
	      }
	      else{
	    	  if(title.equals("")){
	    		  this.startElement("li", new String[]{"style=font-size:0.6em;font-weight:bold","command="+command,"formid="+formid,"actiontarget="+actiontarget,"urlparams="+urlparams}, true);
	    	  }
	    	  else{
	    		  this.startElement("li", new String[]{"title="+title, "style=font-size:0.6em;font-weight:bold","command="+command,"formid="+formid,"actiontarget="+actiontarget,"urlparams="+urlparams}, true);
	    	  }
	      }
	      	this.startElement("a", new String[]{"href=#"+mastertargetelemid}, true);
	      		this.startTable(new String[]{"cellpadding=0","cellspacing=1"});
	      			this.startRow("");
				      	if(!starticon.equals("")){
				      		this.startElement("td", new String[]{"class=ui-icon "+starticon}, true);this.endElement("td", true);
				      	}
				      	this.startElement("td",(String[])null, true);	this.respondString(caption); this.endElement("td", true);
				      	if(!endicon.equals("")){
				      		this.startElement("td", new String[]{"class=ui-icon "+endicon}, true);this.endElement("td", true);
				      	}
				     this.endRow();
				 this.endTable();
	      	this.endElement("a", true);
	      this.endElement("li", true);
	      //action(title, caption, command, formid, widget, actiontarget, starticon, endicon, urlparams);
	      if (alignvertical) {
	        //endCell();
	        //endRow();
	      }
	    }

    	this.endElement("ul", true);
    	this.startElement("div", new String[]{"id="+mastertargetelemid}, true); this.endElement("div", true);
    this.endElement("div", true);
    this.startScript("", "");
    	this.respondString("$(\"#"+tabsactioncontainer+"\").inovoActionTabs();\r\n");
    	this.respondString("inovo_action($(\".firsttab_"+tabsactioncontainer+"\"));");
    this.endScript();
  }

  public void attachWebAction(String actionid,boolean actionOnly) throws Exception
  {
    startScript("", "");
    if(actionOnly) {
    	respondString("$(\"#" + actionid + "\").click(function(event){inovo_action($(\"#" + actionid + "\"));});");
    } else {
    	respondString("$(\"#" + actionid + "\").inovoAction();\r\n");
    }
    endScript();
  }

  private ArrayList<String> _replaceComponents=new ArrayList<String>();
  public void replaceComponentContent(String targetelementid) throws Exception {
	  _replaceComponents.add(targetelementid);
	  if(!this.requestParameter("actiontarget").equals(targetelementid)){  
		  respondString("replacecomponent||" + targetelementid + "||");
	  }
  }

  public void endReplaceComponentContent() throws Exception {
	  if(!_replaceComponents.isEmpty()&&!this.requestParameter("actiontarget").equals(_replaceComponents.remove(_replaceComponents.size()-1))){
		  respondString("||replacecomponent");
	  }
  }
  
  public void flushReplaceComponentContent(String targetelementid){
	  if(!this.requestParameter("actiontarget").equals(targetelementid)){
		  this.appendString("replacecomponent||" + targetelementid + "||", "||replacecomponent");
	  }
  }
}
