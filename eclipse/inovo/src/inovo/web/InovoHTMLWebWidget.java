package inovo.web;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InovoHTMLWebWidget extends InovoWebWidget
{
  public InovoHTMLWebWidget(InputStream inStream)
  {
    this(null, inStream);
  }

  public InovoHTMLWebWidget(InovoWebWidget parentWidget, InputStream inStream) {
    super(parentWidget, inStream);
  }

  public void startPage() throws Exception {
    startElement("html",(String[]) null, true);
  }

  public void startHead() throws Exception {
    startElement("head",(String[]) null, true);
  }

  public void endHead() throws Exception {
    endElement("head", true);
  }
  
  public void startScript() throws Exception{
	  this.startScript("", "");
  }
  
  public void startScript(String scriptAttributes, String type) throws Exception {
	  this.startScript(scriptAttributes==null?(String[])null:(!(scriptAttributes=scriptAttributes.trim()).equals("")&&scriptAttributes.indexOf("|")>-1)?scriptAttributes.split("[|]"):scriptAttributes.equals("")?(String[])null:new String[]{scriptAttributes}, type);
  }

  public void startScript(String[] scriptAttributes, String type) throws Exception {
	  type=(type==null?"":type);
    if (type.equals("")) {
      type = "javascript";
    }
    ArrayList elemProps = new ArrayList();
    elemProps.add("type=text/" + type);
    if (scriptAttributes != null) {
      for (String scriptAttr : scriptAttributes) {
        elemProps.add(scriptAttr);
      }
    }
    String[] elemProperties = new String[elemProps.size()];
    elemProps.toArray(elemProperties);
    startElement("script", elemProperties, true);
  }

  public void endScript() throws Exception {
    endElement("script", true);
  }
  
  public void startStyling() throws Exception {
	  this.startStyling("", "");
  }
  
  public void startStyling(String stylehrefs, String type) throws Exception {
	  this.startStyling(stylehrefs==null?(String[])null:(!(stylehrefs=stylehrefs.trim()).equals("")&&stylehrefs.indexOf("|")>-1)?stylehrefs.split("[|]"):stylehrefs.equals("")?(String[])null:new String[]{stylehrefs}, type);
  }

  public void startStyling(String[] stylehrefs, String type) throws Exception {
    type = type.equals("") ? "css" : type;
    startElement("style", new String[] { "type=text/" + type }, true);
    if (stylehrefs != null)
      for (String stylehref : stylehrefs)
        respondString("\r\n@import url(\"" + stylehref + "\");\r\n");
  }

  public void endStyling()
    throws Exception
  {
    endElement("style", true);
  }

  public void startBody(String[] bodyproperties) throws Exception {
    startElement("body", bodyproperties, true);
  }

  public void endBody() throws Exception {
    endElement("body", true);
  }

  public void startForm(String formid, String enctype, String action) throws Exception {
    enctype = enctype.equals("") ? "mutlipart/form-data" : enctype;
    action = action.equals("") ? "?WIDGET=" + getClass().getName() : action;
    startElement("form", new String[] { "id=" + formid, "method=POST", "enctype=" + enctype }, true);
  }

  public void fieldHidden(String fieldid, String fieldValue) throws Exception {
    simpleElement("input", new String[] { "id=" + fieldid, "name=" + fieldid, "style=display:none", "value=" + fieldValue });
  }
  
  public void fieldInput(String fieldid, String fieldValue, String fieldType, boolean enabled) throws Exception{
	  this.fieldInput(fieldid, fieldValue, fieldType, enabled, "");
  }
  
  public void fieldInput(String fieldid, String fieldValue, String fieldType, boolean enabled, String fieldProperties) throws Exception{
	  this.fieldInput(fieldid, fieldValue, fieldType, enabled, fieldProperties==null?(String[])null:(fieldProperties=fieldProperties.trim()).equals("")?(String[])null:fieldProperties.indexOf("|")>-1?fieldProperties.split("[|]"):new String[]{fieldProperties});
  }
  
  public void fieldInput(String fieldid, String fieldValue, String fieldType, boolean enabled, String[] fieldProperties) throws Exception{
	  fieldInput(fieldid, fieldValue, fieldType, enabled, fieldProperties, (HashMap)null);
  }
  
  public void fieldInput(String fieldid, String fieldValue, String fieldType, boolean enabled, String fieldProperties,String values) throws Exception{
	  fieldInput(fieldid, fieldValue, fieldType, enabled, fieldProperties==null?(String[])null:(fieldProperties=fieldProperties.trim()).equals("")?(String[])null:fieldProperties.indexOf("|")>-1?fieldProperties.split("[|]"):new String[]{fieldProperties}, values==null?(String[])null:(values=values.trim()).equals("")?(String[])null:values.indexOf("|")>-1?values.split("[|]"):new String[]{values});
  }

  public void fieldInput(String fieldid, String fieldValue, String fieldType, boolean enabled, String[] fieldProperties,String[] values) throws Exception{
	if(values!=null){  
		fieldInput(fieldid, fieldValue, fieldType, enabled, fieldProperties,(Map) inovo.adhoc.AdhocUtils.generateProperties(values));
	}
	else{
		fieldInput(fieldid, fieldValue, fieldType, enabled, fieldProperties);
	}
  }
  
  public void fieldInput(String fieldid, String fieldValue, String fieldType, boolean enabled, String[] fieldProperties,Map<Object,String> values) throws Exception
  {
    fieldType = fieldType.toLowerCase();
    if (fieldType.equals("")) fieldType = "text";
    
    boolean isDate=fieldType.equals("date");
    boolean isTime=fieldType.equals("time");
    
    if(values!=null){
    	fieldType.equals("select");
    }
    ArrayList fieldProps = new ArrayList();
    fieldProps.add("id=" + fieldid);
    fieldProps.add("name=" + fieldid);
    fieldProps.add("style=font-size:0.8em");
    
    if (fieldProperties != null) {
      for (String fieldProperty : fieldProperties) {
        if (fieldProperty.indexOf("=") != -1) {
          String fieldPropName = fieldProperty.substring(0, fieldProperty.indexOf("=")).trim();
          String fieldPropVal = fieldProperty.substring(fieldProperty.indexOf("=") + 1);
          fieldProps.add(fieldPropName + "=" + fieldPropVal);
        }
      }
    }
    if (enabled) {
    	if(isDate||isTime){
    		this.startElement("span",(String[]) null, true);
    		fieldType="text";
    	}
    	
      if ("text,file,password,checkbox,radio,".contains(fieldType + ",")) {
        fieldProps.add("type=" + fieldType);
        fieldProps.add("value=" + fieldValue);
        fieldProperties = null;
        fieldProperties = new String[fieldProps.size()];
        fieldProps.toArray(fieldProperties);
        simpleElement("input", fieldProperties);
      }
      else if (fieldType.equals("multiline")) {
        fieldProperties = new String[fieldProps.size()];
        fieldProps.toArray(fieldProperties);
        startElement("textarea", fieldProperties, true);
        respondString(encodeHTML(fieldValue));
        endElement("textarea", true);
      
      }
      else if(fieldType.equals("select")){
    	  fieldProperties = new String[fieldProps.size()];
          fieldProps.toArray(fieldProperties);
    	this.startElement("select", fieldProperties, true);
    		this.startElement("option", new String[]{"value="}, true);this.respondString("");this.endElement("option", true);
    		for(Object valueKey:values.keySet()){
    			this.startElement("option", new String[]{"value="+valueKey,(valueKey.toString().equals(fieldValue)?"selected=selected":"")}, true);this.respondString(encodeHTML(values.get(valueKey)));this.endElement("option", true);
    		}
    	this.endElement("select", true);
      }
      
      if(isDate||isTime){
    	  this.endElement("span",true);
    	  if(isDate){ this.startScript((String[])null, "");this.respondString("$('#"+fieldid+"').datepicker({dateFormat:'yy-mm-dd'})");this.endScript();}
      }
    }
    else {
      startElement("span", new String[] { "class=ui-widget-content","style=font-size:0.8em" }, true); respondString(encodeHTML(fieldValue)); endElement("span", true);
      fieldHidden(fieldid, fieldValue);
    }

    fieldProps.clear();
  }

  public void fieldLabel(String caption) throws Exception {
    fieldLabel(caption, false);
  }
  
  public void fieldLabel(String caption,boolean ignoreEncoding) throws Exception {
    if(!caption.equals("")){
    	byte[]captionbytes=caption.getBytes();
    	caption="";
    	for(byte capb:captionbytes){
    		caption+=(char)(capb<=0?32:capb);
    	}
    	
    }
	  
	startElement("span", new String[] { "style=font-size:0.8em", "class=ui-widget-header" }, true);
    respondString( ignoreEncoding?caption:encodeHTML(caption));
    endElement("span", true);
  }

  public void endForm() throws Exception {
    endElement("form", true);
  }

  public void startTable(String[] tableProperties) throws Exception {
    startElement("table", tableProperties, true);
  }
  
  public void startTable(String tableProperties) throws Exception {
    startElement("table", tableProperties, true);
  }
  
  public void startTable() throws Exception {
	  this.startTable("");
  }
  
  public void startTHead(String[] theadProperties) throws Exception {
    startElement("thead", theadProperties, true);
  }
  
  public void startTHead(String theadProperties) throws Exception {
    startElement("thead", theadProperties, true);
  }
  
  public void startTHead() throws Exception {
	  this.startTHead("");
  }
  
  public void endTHead() throws Exception {
    endElement("thead", true);
  }
  
  public void startTBody(String[] tbodyProperties) throws Exception {
    startElement("tbody", tbodyProperties, true);
  }
  
  public void startTBody(String tbodyProperties) throws Exception {
    startElement("tbody", tbodyProperties, true);
  }
  
  public void startTBody() throws Exception {
	  this.startTBody("");
  }
  
  public void endTBody() throws Exception {
    endElement("tbody", true);
  }

  public void startTFoot(String[] tfootProperties) throws Exception {
    startElement("tfoot", tfootProperties, true);
  }
  
  public void startTFoot(String tfootProperties) throws Exception {
    startElement("tfoot", tfootProperties, true);
  }
  
  public void startTFoot() throws Exception {
	  this.startTFoot("");
  }
  
  public void endTFoot() throws Exception {
    endElement("tfoot", true);
  }


  public void startRow(String[] rowProperties) throws Exception {
    startElement("tr", rowProperties, true);
  }
  
  public void startRow(String rowProperties) throws Exception {
    startElement("tr", rowProperties, true);
  }
  
  public void startRow() throws Exception {
	  startRow("");
  }

  public void startCell(String cellProperties) throws Exception {
    startElement("td", cellProperties, true);
  }
  
  public void startCell() throws Exception {
	  this.startCell("");
  }
  
  public void startCell(String[] cellProperties) throws Exception {
    startElement("td", cellProperties, true);
  }

  public void endCell() throws Exception {
    endElement("td", true);
  }
  
  public void startColumn() throws Exception {
	  this.startColumn("");
  }

  public void startColumn(String style) throws Exception {
	  style=(style==null?"":style);
    startElement("td", new String[] { "class=ui-widget-header", "style="+style }, true);
  }

  public void endColumn() throws Exception {
    endElement("td", true);
  }

  public void endRow() throws Exception {
    endElement("tr", true);
  }

  public void endTable() throws Exception {
    endElement("table", true);
  }

  public void endPage() throws Exception {
    endElement("html", true);
  }
}
