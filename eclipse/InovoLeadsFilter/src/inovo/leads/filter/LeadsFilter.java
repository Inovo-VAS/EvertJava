package inovo.leads.filter;

import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.sun.xml.internal.ws.api.pipe.NextAction;

import inovo.db.Database;
import inovo.servlet.InovoServletContextListener;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class LeadsFilter extends InovoHTMLPageWidget {

	public LeadsFilter(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

	@Override
	public void pageContent() throws Exception{
		this.startScript(null, null);
			this.respondString("function checkCheckBoxes(selectnamespace){$(selectnamespace).prop('checked', true);}\r\n");
			this.respondString("function uncheckCheckBoxes(selectnamespace){$(selectnamespace).prop('checked', false);}\r\n");
		this.endScript();
		ArrayList<String[]> actionsProperties=new ArrayList<String[]>();
		actionsProperties.add("caption=CONFIGURE FILTER(s)|actiontarget=leads_filter_section|command=configure_filters|urlparams=master_action=configure-filters".split("[|]"));
		actionsProperties.add("caption=APPLY FILTER(s)|actiontarget=leads_filter_section|command=apply_filters|urlparams=master_action=apply-filters".split("[|]"));
		this.actions(actionsProperties, false);
		
		this.startElement("div", "id=leads_filter_section|class=ui-widget-content".split("[|]"), true);this.endElement("div", true);
	}
	
	//CONFIGURE FILTER(s)
	public void configure_filters() throws Exception{
		this.startElement("div", "id=configure_filters".split("[|]"), true);
			this.startElement("div", "class=ui-widget-header".split("[|]"),true);
				this.respondString("CONFIGURE FILTER(s)");
			this.endElement("div", true);
			this.action("NEW (LEADS FILTER)", "add_new_filter", "", "", "configure_filters_view", "", "", "");
			this.startElement("div", "class=ui-widget|id=configure_filters_view".split("[|]"),true);
				this.setRequestParameter("MASTER_ACTION", "configure-filters", true);
				this.registered_configured_filters();
			this.endElement("div", true);
		this.endElement("div", true);
	}
	
	public void add_new_filter() throws Exception{
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "DELETE FROM <DBUSER>.CONFIGURE_LEADS_FILTER_CONDITION_ELEMENTS WHERE CONFIGURE_LEADS_FILTER_ID IN(SELECT ID FROM <DBUSER>.CONFIGURE_LEADS_FILTER WHERE STATUS='PENDING' AND CREATESTAMP<DATEADD(MINUTE,-30,GETDATE()) AND LIVE_LEADS_FILTER_ID=0) DELETE FROM <DBUSER>.CONFIGURE_LEADS_FILTER_ORDER_BY WHERE CONFIGURE_LEADS_FILTER_ID IN(SELECT ID FROM <DBUSER>.CONFIGURE_LEADS_FILTER WHERE STATUS='PENDING' AND CREATESTAMP<DATEADD(MINUTE,-30,GETDATE()) AND LIVE_LEADS_FILTER_ID=0) DELETE FROM <DBUSER>.CONFIGURE_LEADS_FILTER WHERE STATUS='PENDING' AND CREATESTAMP<DATEADD(MINUTE,-30,GETDATE()) AND LIVE_LEADS_FILTER_ID=0 INSERT INTO <DBUSER>.CONFIGURE_LEADS_FILTER (LASTUSERIP) SELECT '"+this.requestHeader("REMOTE-HOST")+"' WHERE '"+this.requestParameter("LEADS_CONFIG_ID")+"'=''", null, null);
		
		//Database.executeDBRequest(null, "INOVOLEADSFILTER", "INSERT INTO <DBUSER>.CONFIGURE_LEADS_FILTER (LASTUSERIP) VALUES('"+this.requestHeader("REMOST-HOST")+"')", null, null);
		HashMap<String,String> nextConfigFilterSettings=new HashMap<String, String>();
		nextConfigFilterSettings.put("ID", "");
		nextConfigFilterSettings.put("STEP", "");
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "SELECT TOP 1 ID , STEP,DATASOURCE_ID FROM <DBUSER>.CONFIGURE_LEADS_FILTER WHERE LASTUSERIP='"+this.requestHeader("REMOTE-HOST")+"' ORDER BY ID DESC ",nextConfigFilterSettings , null);
		this.setRequestParameter("LEADS_CONFIG_ID", nextConfigFilterSettings.get("ID"), true);
		this.setRequestParameter("LEADS_CONFIG_STEP", nextConfigFilterSettings.get("STEP"), true);
		this.construct_new_filter();
	}
	
	public void edit_live_filter() throws Exception{
		//Database.executeDBRequest(null, "INOVOLEADSFILTER", "DELETE FROM <DBUSER>.CONFIGURE_LEADS_FILTER_CONDITION_ELEMENTS WHERE CONFIGURE_LEADS_FILTER_ID IN(SELECT ID FROM <DBUSER>.CONFIGURE_LEADS_FILTER WHERE STATUS='PENDING' AND CREATESTAMP<DATEADD(MINUTE,-30,GETDATE()) AND LIVE_LEADS_FILTER_ID=0) DELETE FROM <DBUSER>.CONFIGURE_LEADS_FILTER_ORDER_BY WHERE CONFIGURE_LEADS_FILTER_ID IN(SELECT ID FROM <DBUSER>.CONFIGURE_LEADS_FILTER WHERE STATUS='PENDING' AND CREATESTAMP<DATEADD(MINUTE,-30,GETDATE()) AND LIVE_LEADS_FILTER_ID=0) DELETE FROM <DBUSER>.CONFIGURE_LEADS_FILTER WHERE STATUS='PENDING' AND CREATESTAMP<DATEADD(MINUTE,-30,GETDATE()) AND LIVE_LEADS_FILTER_ID=0 INSERT INTO <DBUSER>.CONFIGURE_LEADS_FILTER (LASTUSERIP) SELECT '"+this.requestHeader("REMOTE-HOST")+"' WHERE '"+this.requestParameter("LEADS_CONFIG_ID")+"'=''", null, null);
		
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "INSERT INTO <DBUSER>.CONFIGURE_LEADS_FILTER (LASTUSERIP) VALUES('"+this.requestHeader("REMOST-HOST")+"')", null, null);
		HashMap<String,String> nextConfigFilterSettings=new HashMap<String, String>();
		nextConfigFilterSettings.put("ID", "");
		nextConfigFilterSettings.put("STEP", "");
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "SELECT TOP 1 ID , STEP,DATASOURCE_ID FROM <DBUSER>.CONFIGURE_LEADS_FILTER WHERE LASTUSERIP='"+this.requestHeader("REMOTE-HOST")+"' ORDER BY ID DESC ",nextConfigFilterSettings , null);
		this.setRequestParameter("LEADS_CONFIG_ID", nextConfigFilterSettings.get("ID"), true);
		this.setRequestParameter("LEADS_CONFIG_STEP", nextConfigFilterSettings.get("STEP"), true);
		this.construct_new_filter();
	}
	
	private boolean _flush_configure_filters_view=false;
	
	public void construct_new_filter() throws Exception{
		HashMap<String,String> nextConfigFilterSettings=new HashMap<String, String>();
		this.importRequestParametersIntoMap(nextConfigFilterSettings,"");
		this.fieldHidden("LIVE_LEADS_FILTER_ID",nextConfigFilterSettings.get("LIVE_LEADS_FILTER_ID"));
		this.fieldHidden("LEADS_CONFIG_ID", nextConfigFilterSettings.get("LEADS_CONFIG_ID"));
		this.fieldHidden("LEADS_CONFIG_STEP", nextConfigFilterSettings.get("LEADS_CONFIG_STEP"));
		String[] stepLabels="DATASOURCE|DATASET (TABLE or VIEW)|MAP SOURCEID FIELD|DATASET FIELD(s) CONDITION(s)|ORDER BY LIST|FILTER ALIAS".split("[|]");
		int maxStepLevel=Integer.parseInt(nextConfigFilterSettings.get("LEADS_CONFIG_STEP"));
		int stepLevel=0;
		int nextConfigStep=this.requestParameter("NEXT_LEADS_CONFIG_STEP").equals("")?1:Integer.parseInt(this.requestParameter("NEXT_LEADS_CONFIG_STEP"));
		_flush_configure_filters_view=false;
		while(stepLevel<maxStepLevel){
			this.startElement("div","class==ui-widget-heading".split("[|]") , true);
				this.respondString(String.valueOf(stepLevel+1)+" - "+stepLabels[stepLevel++]);
			this.endElement("div", true);
			this.startElement("div","class==ui-widget".split("[|]") , true);
			if(stepLevel==1){
				//DATASOURCE
				if(nextConfigStep==stepLevel){
					this.construct_new_filter_datasource();
				}
				else{
					_flush_configure_filters_view=false;
					boolean updateDatasourceDetails=false;
					if(!this.requestParameter("CONFIG_FILTER_DATASOURCE_ID").equals("")){
						Database.executeDBRequest(null, "INOVOLEADSFILTER", "UPDATE <DBUSER>.CONFIGURE_LEADS_FILTER SET DATASOURCE_ID=:CONFIG_FILTER_DATASOURCE_ID,STEP=1 WHERE ID=:LEADS_CONFIG_ID", nextConfigFilterSettings, null);
						nextConfigFilterSettings.put("DATASOURCE_ID", nextConfigFilterSettings.get("CONFIG_FILTER_DATASOURCE_ID"));
						updateDatasourceDetails=true;
					}
					if(nextConfigFilterSettings.containsKey("DATASOURCE_ID")){
						this.fieldHidden("DATASOURCE_ID", nextConfigFilterSettings.get("DATASOURCE_ID"));
						nextConfigFilterSettings.put("DATASOURCE_LABEL", "");
						nextConfigFilterSettings.put("DATASOURCE_ALIAS", "");
						nextConfigFilterSettings.put("DATASOURCE_DB", "");
						nextConfigFilterSettings.put("DATASOURCE_SCHEMA", "");
						Database.executeDBRequest(null, "INOVOLEADSFILTER", "SELECT ALIAS AS DATASOURCE_ALIAS,DB AS DATASOURCE_DB,DBSCHEMA AS DATASOURCE_SCHEMA,(UPPER(ALIAS)+' - ['+UPPER(USAGE_DESCRIPTION)+']') AS DATASOURCE_LABEL FROM <DBUSER>.LEADS_FILTER_DATASOURCE WHERE ID=:DATASOURCE_ID", nextConfigFilterSettings, null);
						this.respondString(nextConfigFilterSettings.get("DATASOURCE_LABEL"));
						this.fieldHidden("DATASOURCE_ALIAS", nextConfigFilterSettings.get("DATASOURCE_ALIAS"));
						this.setRequestParameter("DATASOURCE_ID", nextConfigFilterSettings.get("DATASOURCE_ID"), true);
						this.setRequestParameter("DATASOURCE_ALIAS", nextConfigFilterSettings.get("DATASOURCE_ALIAS"), true);
						this.setRequestParameter("DATASOURCE_DB", nextConfigFilterSettings.get("DATASOURCE_DB"), true);
						this.fieldHidden("DATASOURCE_DB", nextConfigFilterSettings.get("DATASOURCE_DB"));
						this.setRequestParameter("DATASOURCE_SCHEMA", nextConfigFilterSettings.get("DATASOURCE_SCHEMA"), true);
						this.fieldHidden("DATASOURCE_SCHEMA", nextConfigFilterSettings.get("DATASOURCE_SCHEMA"));
						if(updateDatasourceDetails){
							Database.executeDBRequest(null, "INOVOLEADSFILTER", "UPDATE <DBUSER>.CONFIGURE_LEADS_FILTER SET DB=:DATASOURCE_DB,DBSCHEMA=:DATASOURCE_SCHEMA WHERE ID=:LEADS_CONFIG_ID", nextConfigFilterSettings, null);
						}
					}
					else{
						this.showDialog("", "title=CONFIG ERROR|content=NO DATASOURCE SELECTED".split("[|]"));
						return;
					}
					_flush_configure_filters_view=true;
					if(maxStepLevel<nextConfigStep){
						maxStepLevel=nextConfigStep;
					}
				}
			}
			else if(stepLevel==2){
				//DATASET
				if(nextConfigStep==stepLevel){
					this.construct_new_filter_dataset();
					_flush_configure_filters_view=true;
				}
				else{
					_flush_configure_filters_view=false;
					if(!this.requestParameter("CONFIG_FILTER_DATASET").equals("")){
						Database.executeDBRequest(null, "INOVOLEADSFILTER", "UPDATE <DBUSER>.CONFIGURE_LEADS_FILTER SET DATASET=:CONFIG_FILTER_DATASET,STEP=2 WHERE ID=:LEADS_CONFIG_ID", nextConfigFilterSettings, null);
						nextConfigFilterSettings.put("DATASET", nextConfigFilterSettings.get("CONFIG_FILTER_DATASET"));
						
					}
					if(nextConfigFilterSettings.containsKey("DATASET")){
						this.setRequestParameter("DATASET", nextConfigFilterSettings.get("DATASET"), true);
						this.fieldHidden("DATASET", nextConfigFilterSettings.get("DATASET"));
						this.respondString( nextConfigFilterSettings.get("DATASET"));
					}
					else{
						this.showDialog("", "title=CONFIG ERROR|content=NO DATASET (TABLE or VIEW) SELECTED".split("[|]"));
						return;
					}
					_flush_configure_filters_view=true;
				}
				if(maxStepLevel<nextConfigStep){
					maxStepLevel=nextConfigStep;
				}
			}
			else if(stepLevel==3){
				//SOURCE_ID
				if(stepLevel==nextConfigStep){
					this.construct_new_filter_dataset_map_sourceid_field();
					_flush_configure_filters_view=true;
				}
				else{
					if(!this.requestParameter("CONFIG_FILTER_SOURCEID").equals("")){
						nextConfigFilterSettings.put("DATASET_SOURCEID_FIELD",nextConfigFilterSettings.get("CONFIG_FILTER_SOURCEID").substring(0, nextConfigFilterSettings.get("CONFIG_FILTER_SOURCEID").indexOf("|")));
						Database.executeDBRequest(null, "INOVOLEADSFILTER", "UPDATE <DBUSER>.CONFIGURE_LEADS_FILTER SET SOURCEID_FIELD=:DATASET_SOURCEID_FIELD,STEP=3 WHERE ID=:LEADS_CONFIG_ID", nextConfigFilterSettings, null);
					}
					if(nextConfigFilterSettings.containsKey("DATASET_SOURCEID_FIELD")){
						this.fieldLabel("SOURCEID[FIELD]");this.fieldInput("DATASET_SOURCEID_FIELD", nextConfigFilterSettings.get("DATASET_SOURCEID_FIELD"),"text",false,null);
						_datasetFields.clear();
						Database.executeDBRequest(null, this.requestParameter("DATASOURCE_ALIAS"), "SELECT COLUMN_NAME,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE FROM  INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA LIKE '%"+this.requestParameter("DATASOURCE_SCHEMA")+"%' AND TABLE_NAME ='"+this.requestParameter("DATASET")+"' ORDER BY TABLE_SCHEMA,TABLE_NAME",null,this,"configDatasetFields_Data");
						boolean startFieldSettings=false;
						for(String field:_datasetFields.keySet()){
							String datasetFieldEntry=field;
							this.fieldHidden("DATASET_FIELD",datasetFieldEntry.substring(0, datasetFieldEntry.indexOf("|")));
							this.setRequestParameter("DATASET_FIELD",datasetFieldEntry.substring(0, datasetFieldEntry.indexOf("|")), !startFieldSettings);
							this.fieldHidden("DATASET_FIELD_TYPE",datasetFieldEntry.substring(datasetFieldEntry.indexOf("|")+1,datasetFieldEntry.length()));
							this.setRequestParameter("DATASET_FIELD_TYPE",datasetFieldEntry.substring(datasetFieldEntry.indexOf("|")+1,datasetFieldEntry.length()), !startFieldSettings);
							if(!startFieldSettings) startFieldSettings=true;
						}
					}
					else{
						this.showDialog("", "title=CONFIG ERROR|content=NO SOURCEID FIELD MAPPED".split("[|]"));
						return;
					}
					_flush_configure_filters_view=true;
				}
				if(maxStepLevel<nextConfigStep){
					maxStepLevel=nextConfigStep;
				}
			}
			else if(stepLevel==4){
				//DATASET FIELD(s) CONDITION(s)
				_flush_configure_filters_view=true;
				this.startElement("div", "id=construct_new_filter_dataset_fields_conditions".split("[|]"), true);
				this.construct_new_filter_dataset_fields_conditions();
				//if(!this.validCheckedConditionFilter()){
				//	maxStepLevel=nextConfigStep=stepLevel;
				//}
				_flush_configure_filters_view=true;
				this.endElement("div", true);
				if(maxStepLevel<nextConfigStep){
					maxStepLevel=nextConfigStep;
				}
			}
			else if(stepLevel==5){
				//DATASET FIELD(s) ORDER BY(s)
				_flush_configure_filters_view=true;
				this.startElement("div", "id=construct_new_filter_dataset_fields_orderbys".split("[|]"), true);
				this.construct_new_filter_dataset_fields_order_bys();
				
				_flush_configure_filters_view=true;
				this.endElement("div", true);
				if(maxStepLevel<nextConfigStep){
					maxStepLevel=nextConfigStep;
				}
			}
			else if(stepLevel==6){
				_flush_configure_filters_view=true;
				this.startTable(null);
					this.startRow(null);
						this.startColumn("font-size:0.8em");
							this.respondString("FILTER NAME");
						this.endColumn();
						this.startCell("style=vertical-align:top".split("[|]"));
							this.fieldInput("NEW_FILTER_NAME", "", "text", true, null);
						this.endCell();
						this.startRow(null);
						this.startColumn("font-size:0.8em");
							this.respondString("FILTER USAGE");
						this.endColumn();
						this.startCell("style=vertical-align:top".split("[|]"));
							this.fieldInput("NEW_FILTER_USAGE", "", "text", true, null);
						this.endCell();
					this.endRow();
					this.endRow();
				this.endTable();
				if(maxStepLevel<nextConfigStep){
					maxStepLevel=nextConfigStep;
				}
			}
			this.endElement("div", true);
		}
		this.fieldHidden("NEXT_LEADS_CONFIG_STEP", String.valueOf(maxStepLevel<stepLabels.length?(maxStepLevel+1):stepLabels.length));
		
		if(maxStepLevel<stepLabels.length){//||!_validCheckedConditionFilter){
			this.action("START OVER", "start_new_config_over", "", "", "configure_filters_view", "", "", ""); this.action("NEXT STEP ["+String.valueOf(maxStepLevel+1)+" - "+stepLabels[maxStepLevel]+"] ->>", "construct_new_filter", "", "", "", "", "", "");
		}
		else{
			this.action("START OVER", "start_new_config_over", "", "", "configure_filters_view", "", "", ""); this.action("SETUP FILTER", "apply_new_filter", "", "", "", "", "", "applyfilter=setup");
		}
		if(_flush_configure_filters_view){
			this.flushReplaceComponentContent("configure_filters_view");
		}
	}
	
	public void apply_new_filter() throws Exception{
		if(this.requestParameter("applyfilter").equals("setup")){
			if(this.requestParameter("NEW_FILTER_NAME").trim().equals("")){
				this.showDialog("", "title=FAILED TO PUBLISH CONFIG|content=NO FILTER NAME PROVIDED".split("[|]"));
			}
			else{
				HashMap<String,String> newFilterNameDetails=new HashMap<String, String>();
				newFilterNameDetails.put("FILTER_NAME", this.requestParameter("NEW_FILTER_NAME").trim().toUpperCase());
				newFilterNameDetails.put("FILTER_USAGE", this.requestParameter("NEW_FILTER_USAGE").trim().equals("")?newFilterNameDetails.get("FILTER_NAME"):this.requestParameter("NEW_FILTER_USAGE").trim());
				newFilterNameDetails.put("LEADS_CONFIG_ID", this.requestParameter("LEADS_CONFIG_ID"));
				newFilterNameDetails.put("EXISTING_FILTER_COUNT", "0");
				Database.executeDBRequest(null,"INOVOLEADSFILTER", "SELECT COUNT(*) AS EXISTING_FILTER_COUNT FROM LIVE_LEADS_FILTER WHERE UPPER(FILTER_NAME)=UPPER(:FILTER_NAME) AND CONFIGURE_LEADS_FILTER_ID<>:LEADS_CONFIG_ID", newFilterNameDetails, null);
				if(newFilterNameDetails.get("EXISTING_FILTER_COUNT").equals("0")){
					Database.executeDBRequest(null, "INOVOLEADSFILTER", "EXECUTE <DBUSER>.PUBLISH_NEW_CONFIGURED_LEADS_FILTER :LEADS_CONFIG_ID,:FILTER_NAME,:FILTER_USAGE", newFilterNameDetails, null);
					this.configure_filters();
					this.flushReplaceComponentContent("leads_filter_section");
				}
				else{
					this.showDialog("",("title=FAILED TO PUBLISH CONFIG|content=FILTER NAME ["+newFilterNameDetails.get("FILTER_NAME")+"] PROVIDED ALREADY EXIST").split("[|]"));
				}
			}
		}
	}
	
	public void construct_new_filter_dataset_fields_order_bys() throws Exception{
		if(!this.requestParameter("NEXT_DATASET_FIELD_ORDER_BY_OPTION").equals("")){
			this.applyNextDatasetFieldOrderByOption(this.requestParameter("NEXT_DATASET_FIELD_ORDER_BY_OPTION"));
		}
		if(this.requestParameter("APPLYFILTERORDERBYS").equals("reset")){
			Database.executeDBRequest(null, "INOVOLEADSFILTER", "DELETE FROM <DBUSER>.CONFIGURE_LEADS_FILTER_ORDER_BY WHERE CONFIGURE_LEADS_FILTER_ID="+this.requestParameter("LEADS_CONFIG_ID"), null, null);
		}
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "SELECT ID,CONFIGURE_LEADS_FILTER_ID,ELEM_TYPE,ELEM_NAME,ELEM_VALUE,ELEM_VALUE_TYPE,ELEM_ORDERING,PREV_ELEM_ID,LASTUSERIP,LASTACTIONSTAMP,CREATESTAMP FROM <DBUSER>.CONFIGURE_LEADS_FILTER_ORDER_BY WHERE CONFIGURE_LEADS_FILTER_ID="+this.requestParameter("LEADS_CONFIG_ID"), null, this,"construct_new_filter_dataset_fields_orderby_data");
		
		/*this.startElement("div", "id=construct_new_filter_dataset_fields_orderby_nextaction", true);
		_appendNextAction=true;
		if(!this.requestParameter("command").equals("construct_new_filter_dataset_fields_conditions_nextaction")){
			this.construct_new_filter_dataset_fields_conditions_nextaction();
		}
		if(_lastFilterConditionType.equals("")) _lastFilterConditionType="";
		_appendNextAction=false;
		this.endElement("div", true);*/
		this.prepNextDataSetFieldOrderBy(this._lastFilterOrderByType);		
		this.action("", "construct_new_filter_dataset_fields_orderby_nextaction", "", "", "","", "ui-icon-check", "applyfilterorderbys=yes");
		if(!_flush_configure_filters_view) this.flushReplaceComponentContent("construct_new_filter_dataset_fields_orderbys");
	}

	private void prepNextDataSetFieldOrderBy(String lastFilterOrderByType) throws Exception{
		_possibleFieldOrderByTypes.clear();
		
		this.populatePossibleFieldOrderByTypes(true,true);
		if(!_possibleFieldOrderByTypes.isEmpty()){
			this.fieldInput("NEXT_DATASET_FIELD_ORDER_BY_OPTION", ""	, "select", true,"class=NEXT_DATASET_FIELD_ORDER_BY_OPTION".split("[|]"),(Map)_possibleFieldOrderByTypes);
		}
		if(!_possibleFieldOrderByFields.isEmpty()){
			this.fieldInput("NEXT_DATASET_FIELD_ORDER_BY_FIELD", ""	, "select", true,"class=NEXT_DATASET_FIELD_ORDER_BY_FIELD".split("[|]"),(Map)_possibleFieldOrderByFields);
			this.fieldInput("NEXT_DATASET_FIELD_ORDER_BY_VALUE", ""	, "text", true,"class=NEXT_DATASET_FIELD_ORDER_BY_VALUE".split("[|]"));
			this.fieldInput("NEXT_DATASET_FIELD_ORDER_BY_CUSTOM", ""	, "multiline", true,"class=NEXT_DATASET_FIELD_ORDER_BY_CUSTOM".split("[|]"));
			this.startScript(null, "text/javascript");
			this.respondString("$('.NEXT_DATASET_FIELD_ORDER_BY_FIELD').hide();");
			this.respondString("$('.NEXT_DATASET_FIELD_ORDER_BY_VALUE').hide();");
			this.respondString("$('.NEXT_DATASET_FIELD_ORDER_BY_CUSTOM').hide();");
			this.respondString("$('.NEXT_DATASET_FIELD_ORDER_BY_OPTION').on('change', function() {\r\n");
			this.respondString("	  $('.NEXT_DATASET_FIELD_ORDER_BY_FIELD').hide();");
			this.respondString("	  $('.NEXT_DATASET_FIELD_ORDER_BY_VALUE').hide();");
			this.respondString("	  $('.NEXT_DATASET_FIELD_ORDER_BY_CUSTOM').hide();");
			this.respondString("	  if(this.value=='FIELD'||this.value=='VALUE'||this.value=='CUSTOM'){$('.NEXT_DATASET_FIELD_ORDER_BY_'+this.value).show();}\r\n");
			this.respondString("	});\r\n");
			this.endScript();
		}
		HashMap<String,String> orderByDirections=new HashMap<String, String>();
		orderByDirections.put("ASC", "ASC");
		orderByDirections.put("DESC", "DESC");
		this.fieldInput("NEXT_DATASET_FIELD_ORDER_BY_DIRECTION", "ASC"	, "select", true,"class=NEXT_DATASET_FIELD_ORDER_BY_DIRECTION".split("[|]"),(Map)orderByDirections);
	}

	private void applyNextDatasetFieldOrderByOption(String requestOrderByType) throws Exception{
		// TODO Auto-generated method stub
		if(this.requestParameter("APPLYFILTERORDERBYS").equals("")) return;
		if(this.requestParameter("APPLYFILTERORDERBYS").equals("reset")){
			Database.executeDBRequest(null, "INOVOLEADSFILTER", "DELETE FROM <DBUSER>.CONFIGURE_LEADS_FILTER_ORDER_BY WHERE CONFIGURE_LEADS_FILTER_ID="+this.requestParameter("LEADS_CONFIG_ID"), null, null);
			return;
		}
		HashMap<String,String> actionProperties=new HashMap<String, String>();
		actionProperties.put("ELEM_TYPE", requestOrderByType);
		actionProperties.put("LASTUSERIP", this.requestHeader("REMOTE-HOST"));
		actionProperties.put("ELEM_NAME", "");
		actionProperties.put("ELEM_VALUE", "");
		actionProperties.put("ELEM_VALUE_TYPE", "");
		actionProperties.put("ELEM_ORDERING", this.requestParameter("NEXT_DATASET_FIELD_ORDER_BY_DIRECTION"));
		if(requestOrderByType.equals("FIELD")){
			String fieldSelected=this.requestParameter("NEXT_DATASET_FIELD_ORDER_BY_FIELD");
			if(!fieldSelected.equals("")){
				actionProperties.put("ELEM_NAME", fieldSelected);
			}
		}
		else if(requestOrderByType.equals("VALUE")){
			String fieldSelectedValue=this.requestParameter("NEXT_DATASET_FIELD_ORDER_BY_VALUE");
			if(!fieldSelectedValue.equals("")){
				actionProperties.put("ELEM_VALUE", fieldSelectedValue);
				if(this.isNumeric(fieldSelectedValue.toCharArray())){
					actionProperties.put("ELEM_VALUE_TYPE", "numeric");
				}
				else{
					actionProperties.put("ELEM_VALUE_TYPE", "text");
				}
			}
		}
		else if(requestOrderByType.equals("CUSTOM")){
			String fieldSelectedCustom=this.requestParameter("NEXT_DATASET_FIELD_CONDITION_CUSTOM");
			if(!fieldSelectedCustom.equals("")){
				actionProperties.put("ELEM_VALUE", fieldSelectedCustom);
				actionProperties.put("ELEM_VALUE_TYPE", "custom");
			}
		}
		if(!((actionProperties.get("ELEM_TYPE").equals("FIELD")&&actionProperties.get("ELEM_NAME").equals(""))||((actionProperties.get("ELEM_TYPE").equals("VALUE")||actionProperties.get("ELEM_TYPE").equals("CUSTOM"))&&actionProperties.get("ELEM_VALUE").equals(""))))
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "INSERT INTO <DBUSER>.CONFIGURE_LEADS_FILTER_ORDER_BY (CONFIGURE_LEADS_FILTER_ID,ELEM_TYPE,ELEM_NAME,ELEM_VALUE,ELEM_VALUE_TYPE,ELEM_ORDERING,LASTUSERIP) SELECT "+this.requestParameter("LEADS_CONFIG_ID")+",:ELEM_TYPE,:ELEM_NAME,:ELEM_VALUE,:ELEM_VALUE_TYPE,:ELEM_ORDERING,:LASTUSERIP WHERE ISNULL((SELECT ID FROM <DBUSER>.CONFIGURE_LEADS_FILTER_ORDER_BY WHERE CONFIGURE_LEADS_FILTER_ID="+this.requestParameter("LEADS_CONFIG_ID")+" AND LASTACTIONSTAMP=(SELECT MAX(LASTACTIONSTAMP) FROM <DBUSER>.CONFIGURE_LEADS_FILTER_ORDER_BY WHERE CONFIGURE_LEADS_FILTER_ID="+this.requestParameter("LEADS_CONFIG_ID")+") AND ELEM_TYPE=:ELEM_TYPE AND ELEM_NAME=:ELEM_NAME AND ELEM_VALUE=:ELEM_VALUE AND ELEM_ORDERING=:ELEM_ORDERING),0)=0", actionProperties, null);
	}

	private HashMap<String,String> _datasetFields=new HashMap<String, String>();
	public void construct_new_filter_dataset_map_sourceid_field() throws Exception{
		_datasetFields.clear();
		_numericOnlyField=true;
		Database.executeDBRequest(null, this.requestParameter("DATASOURCE_ALIAS"), "SELECT COLUMN_NAME,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE FROM  INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA LIKE '%"+this.requestParameter("DATASOURCE_SCHEMA")+"%' AND TABLE_NAME ='"+this.requestParameter("DATASET")+"' ORDER BY TABLE_SCHEMA,TABLE_NAME",null,this,"configDatasetFields_Data");
		this.fieldInput("CONFIG_FILTER_SOURCEID", "", "select", true, null,(Map) _datasetFields);		
	}
	
	private boolean _numericOnlyField=false;
	public void configDatasetFields_Data(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex>0){
			//COLUMN_NAME,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE
			String dataType=data.get(1);
			if(!data.get(2).equals("")){
				dataType+="("+data.get(2)+")";
			}
			else if(!data.get(3).equals("")&&!data.get(4).equals("")){
				dataType+="("+data.get(3)+","+data.get(4)+")";
			}
			if(_numericOnlyField&&(dataType.toUpperCase().startsWith("NUMERIC")||dataType.toUpperCase().startsWith("INT"))){
				_datasetFields.put(data.get(0)+"|"+dataType,data.get(0)+" ["+dataType+"]");
			}
			else{
				if(!_numericOnlyField) _datasetFields.put(data.get(0)+"|"+dataType,data.get(0)+" ["+dataType+"]");
			}
		}
	}

	private boolean _appendNextAction=false;
	private String _lastFilterConditionType="";
	private String _prevFilterConditionType="";
	private boolean _filterConditionValid=false;
	
	private ArrayList<String> _checkedFilterConditions=new ArrayList<String>();
	public void construct_new_filter_dataset_fields_conditions() throws Exception{
		if(this.requestParameter("APPLYFILTERCONDITIONS").equals("reset")){
			Database.executeDBRequest(null, "INOVOLEADSFILTER", "DELETE FROM <DBUSER>.CONFIGURE_LEADS_FILTER_CONDITION_ELEMENTS WHERE CONFIGURE_LEADS_FILTER_ID="+this.requestParameter("LEADS_CONFIG_ID"), null, null);
		}
		if(!this.requestParameter("NEXT_DATASET_FIELD_CONDITION_OPTION").equals("")){
			this.applyNextDatasetFieldConditionOption(this.requestParameter("NEXT_DATASET_FIELD_CONDITION_OPTION"));
		}
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "SELECT ID,CONFIGURE_LEADS_FILTER_ID,ELEM_TYPE,ELEM_NAME,ELEM_VALUE,ELEM_VALUE_TYPE,PREV_ELEM_ID,LASTUSERIP,LASTACTIONSTAMP,CREATESTAMP FROM <DBUSER>.CONFIGURE_LEADS_FILTER_CONDITION_ELEMENTS WHERE CONFIGURE_LEADS_FILTER_ID="+this.requestParameter("LEADS_CONFIG_ID"), null, this,"construct_new_filter_dataset_fields_conditions_data");
		//this.validCheckedConditionFilter();
		this.startElement("div", "id=construct_new_filter_dataset_fields_conditions_nextaction", true);
		_appendNextAction=true;
		if(!this.requestParameter("command").equals("construct_new_filter_dataset_fields_conditions_nextaction")){
			this.construct_new_filter_dataset_fields_conditions_nextaction();
		}
		if(_lastFilterConditionType.equals("")) _lastFilterConditionType="";
		_appendNextAction=false;
		this.endElement("div", true);
		this.prepNextDataSetFieldCondition(this._lastFilterConditionType);		
		this.action("", "construct_new_filter_dataset_fields_conditions_nextaction", "", "", "","", "ui-icon-check", "applyfilterconditions=yes");
		if(!_flush_configure_filters_view) this.flushReplaceComponentContent("construct_new_filter_dataset_fields_conditions");
	}
	
	private void applyNextDatasetFieldConditionOption(String requestConditionType) throws Exception{
		if(this.requestParameter("APPLYFILTERCONDITIONS").equals("")) return;
		if(this.requestParameter("APPLYFILTERCONDITIONS").equals("reset")){
			//Database.executeDBRequest(null, "INOVOLEADSFILTER", "DELETE FROM <DBUSER>.CONFIGURE_LEADS_FILTER_CONDITION_ELEMENTS WHERE CONFIGURE_LEADS_FILTER_ID="+this.requestParameter("LEADS_CONFIG_ID"), null, this,"construct_new_filter_dataset_fields_conditions_data");
			return;
		}
		HashMap<String,String> actionProperties=new HashMap<String, String>();
		actionProperties.put("ELEM_TYPE", requestConditionType);
		actionProperties.put("LASTUSERIP", this.requestHeader("REMOTE-HOST"));
		actionProperties.put("ELEM_NAME", "");
		actionProperties.put("ELEM_VALUE", "");
		actionProperties.put("ELEM_VALUE_TYPE", "");
		if(requestConditionType.equals("FIELD")){
			String fieldSelected=this.requestParameter("NEXT_DATASET_FIELD_CONDITION_FIELD");
			if(!fieldSelected.equals("")){
				actionProperties.put("ELEM_NAME", fieldSelected);
			}
		}
		else if(requestConditionType.equals("VALUE")){
			String fieldSelectedValue=this.requestParameter("NEXT_DATASET_FIELD_CONDITION_VALUE");
			if(!fieldSelectedValue.equals("")){
				actionProperties.put("ELEM_VALUE", fieldSelectedValue);
				if(this.isNumeric(fieldSelectedValue.toCharArray())){
					actionProperties.put("ELEM_VALUE_TYPE", "numeric");
				}
				else{
					actionProperties.put("ELEM_VALUE_TYPE", "text");
				}
			}
		}
		else if(requestConditionType.equals("CUSTOM")){
			String fieldSelectedCustom=this.requestParameter("NEXT_DATASET_FIELD_CONDITION_CUSTOM");
			if(!fieldSelectedCustom.equals("")){
				actionProperties.put("ELEM_VALUE", fieldSelectedCustom);
				actionProperties.put("ELEM_VALUE_TYPE", "custom");
			}
		}
		if(!((actionProperties.get("ELEM_TYPE").equals("FIELD")&&actionProperties.get("ELEM_NAME").equals(""))||((actionProperties.get("ELEM_TYPE").equals("VALUE")||actionProperties.get("ELEM_TYPE").equals("CUSTOM"))&&actionProperties.get("ELEM_VALUE").equals(""))))
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "INSERT INTO <DBUSER>.CONFIGURE_LEADS_FILTER_CONDITION_ELEMENTS (CONFIGURE_LEADS_FILTER_ID,ELEM_TYPE,ELEM_NAME,ELEM_VALUE,ELEM_VALUE_TYPE,LASTUSERIP) SELECT "+this.requestParameter("LEADS_CONFIG_ID")+",:ELEM_TYPE,:ELEM_NAME,:ELEM_VALUE,:ELEM_VALUE_TYPE,:LASTUSERIP WHERE ISNULL((SELECT ID FROM <DBUSER>.CONFIGURE_LEADS_FILTER_CONDITION_ELEMENTS WHERE CONFIGURE_LEADS_FILTER_ID="+this.requestParameter("LEADS_CONFIG_ID")+" AND LASTACTIONSTAMP=(SELECT MAX(LASTACTIONSTAMP) FROM <DBUSER>.CONFIGURE_LEADS_FILTER_CONDITION_ELEMENTS WHERE CONFIGURE_LEADS_FILTER_ID="+this.requestParameter("LEADS_CONFIG_ID")+") AND ELEM_TYPE=:ELEM_TYPE AND ELEM_NAME=:ELEM_NAME AND ELEM_VALUE=:ELEM_VALUE),0)=0", actionProperties, null);
	}

	
	private boolean isNumeric(char[] chars) {
		
		int indexTest=0;
		int intDeciIndex=-1;
		int numberStartIndex=-1;
		int numberEndIndex=-1;
		int aboveZeroIndex=-1;
		int lastZeroIndex=-1;
		
		char prevCt=0;
		
		while(indexTest<chars.length){
			char ct=chars[indexTest];
			if(!"0123456789.-".contains(""+ct)){
				return false;
			}			
			switch(ct){
			case '-':
				if(indexTest>0){
					return false;
				}
				break;
			case '.':
				if(intDeciIndex==-1){
					if(numberStartIndex>-1&&aboveZeroIndex==-1&&(lastZeroIndex-numberStartIndex)>1){
						return false;
					}
					if(numberStartIndex>-1&&numberStartIndex<indexTest){
						intDeciIndex=indexTest;
						lastZeroIndex=-1;
						aboveZeroIndex=-1;
					}
					else{
						return false;
					}
				}
				else{
					return false;
				}
				break;
			default:
				if(numberStartIndex==-1){
					numberStartIndex=indexTest;
				}
				if("123456789".contains(""+ct)){
					if(intDeciIndex==-1&&lastZeroIndex>0&&aboveZeroIndex==-1){
						return false;
					}
					aboveZeroIndex=indexTest;
				}
				else if(ct==48){
					lastZeroIndex=indexTest;
				}
				numberEndIndex=indexTest;
				break;
			}
			prevCt=ct;
			indexTest++;
		}
		
		if(aboveZeroIndex==-1){
			if(intDeciIndex>0){
				if(lastZeroIndex<=intDeciIndex){
					return false;
				}
			}
			else{
				return false;
			}
		}
		else {
			if(intDeciIndex>0){
				if(aboveZeroIndex<lastZeroIndex){
					return false;
				}
			}
		}
		return (numberStartIndex>=0&&numberEndIndex>=numberStartIndex);
	}

	private HashMap<String,String> _possibleFieldConditionTypes=new HashMap<String, String>();
	private HashMap<String,String> _possibleFieldConditionFields=new HashMap<String, String>();
	
	private void populateDatasetFields(HashMap<String,String> datasetFieldsToPopulate){
		if(this.requestParameterArray("DATASET_FIELD").isEmpty()) return;
		ArrayList<String> datasetFields=(ArrayList<String>)this.requestParameterArray("DATASET_FIELD").clone();
		ArrayList<String> datasetFieldTypes=(ArrayList<String>)this.requestParameterArray("DATASET_FIELD_TYPE").clone();
		while(!datasetFields.isEmpty()){
			datasetFieldsToPopulate.put(datasetFields.get(0)+"|"+datasetFieldTypes.remove(0), datasetFields.remove(0));
		}
	}

	private String _bindConditionFilters="AND:AND|OR:OR|";
	private String _evaluationConditionFilters="LIKE:LIKE|=:=|>:>|<:<|>=:>=|<=:<=|<>:<>|";
	private String _openBraseConditionFilters="(:(|";
	private String _closeBraseConditionFilters="):)|";
	private String _customConditionFilters="CUSTOM:<CUSTOM>|";
	
	private void prepNextDataSetFieldCondition(String lastFilterConditionType) throws Exception{
		_possibleFieldConditionTypes.clear();
		
		String nextSetOfSelectableConditions=((_openBraseConditionFilters.contains(_lastFilterConditionType+":"+lastFilterConditionType+"|")?"":_openBraseConditionFilters) +(_closeBraseConditionFilters.contains(_lastFilterConditionType+":"+lastFilterConditionType+"|")?"":_closeBraseConditionFilters)+(_evaluationConditionFilters.contains(_lastFilterConditionType+":"+lastFilterConditionType+"|")?"":_evaluationConditionFilters)+(_bindConditionFilters.contains(_lastFilterConditionType+":"+lastFilterConditionType+"|")?"":_bindConditionFilters));
		this.populatePossibleFieldConditionTypes(true,true,nextSetOfSelectableConditions.split("[|]"));
		if(!_possibleFieldConditionTypes.isEmpty()){
			this.fieldInput("NEXT_DATASET_FIELD_CONDITION_OPTION", this._lastFilterConditionType	, "select", true,"class=NEXT_DATASET_FIELD_CONDITION_OPTION".split("[|]"),(Map)_possibleFieldConditionTypes);
		}
		if(!_possibleFieldConditionFields.isEmpty()){
			this.fieldInput("NEXT_DATASET_FIELD_CONDITION_FIELD", this._lastFilterConditionType	, "select", true,"class=NEXT_DATASET_FIELD_CONDITION_FIELD".split("[|]"),(Map)_possibleFieldConditionFields);
			this.fieldInput("NEXT_DATASET_FIELD_CONDITION_VALUE", ""	, "text", true,"class=NEXT_DATASET_FIELD_CONDITION_VALUE".split("[|]"));
			this.fieldInput("NEXT_DATASET_FIELD_CONDITION_CUSTOM", ""	, "multiline", true,"class=NEXT_DATASET_FIELD_CONDITION_CUSTOM".split("[|]"));
			this.startScript(null, "text/javascript");
			this.respondString("$('.NEXT_DATASET_FIELD_CONDITION_FIELD').hide();");
			this.respondString("$('.NEXT_DATASET_FIELD_CONDITION_VALUE').hide();");
			this.respondString("$('.NEXT_DATASET_FIELD_CONDITION_CUSTOM').hide();");
			this.respondString("$('.NEXT_DATASET_FIELD_CONDITION_OPTION').on('change', function() {\r\n");
			this.respondString("	  $('.NEXT_DATASET_FIELD_CONDITION_FIELD').hide();");
			this.respondString("	  $('.NEXT_DATASET_FIELD_CONDITION_VALUE').hide();");
			this.respondString("	  $('.NEXT_DATASET_FIELD_CONDITION_CUSTOM').hide();");
			this.respondString("	  if(this.value=='FIELD'||this.value=='VALUE'||this.value=='CUSTOM'){$('.NEXT_DATASET_FIELD_CONDITION_'+this.value).show();}\r\n");
			this.respondString("	});\r\n");
			this.endScript();
		}
	}

	private void populatePossibleFieldConditionTypes(boolean andFields,boolean andCustom,String...possibleTypes) throws Exception{
		for(String possibleType:possibleTypes){
			if(possibleType.equals("")) continue;
			if(possibleType.indexOf(":")==-1) continue;
			_possibleFieldConditionTypes.put(possibleType.substring(0, possibleType.indexOf(":")), possibleType.substring(possibleType.indexOf(":")+1, possibleType.length()));
		}
		this._possibleFieldConditionFields.clear();
		if(andCustom&&!_lastFilterConditionType.equals("CUSTOM")){
			this.populateDatasetFields(_possibleFieldConditionFields);
			_possibleFieldConditionTypes.put("CUSTOM","<CUSTOM>");
		}
		if(andFields){
			this.populateDatasetFields(_possibleFieldConditionFields);
			_possibleFieldConditionTypes.put(",",",");
			if(!_lastFilterConditionType.equals("FIELD")) _possibleFieldConditionTypes.put("FIELD","<FIELD>");
			if(!_lastFilterConditionType.equals("VALUE")) _possibleFieldConditionTypes.put("VALUE","<VALUE>");
		}
	}
	
	private HashMap<String,String> _possibleFieldOrderByTypes=new HashMap<String, String>();
	private HashMap<String,String> _possibleFieldOrderByFields=new HashMap<String, String>();
	private String _lastFilterOrderByType="";
	
	private void populatePossibleFieldOrderByTypes(boolean andFields,boolean andCustom,String...possibleTypes) throws Exception{
		for(String possibleType:possibleTypes){
			if(possibleType.equals("")) continue;
			if(possibleType.indexOf(":")==-1) continue;
			_possibleFieldOrderByTypes.put(possibleType.substring(0, possibleType.indexOf(":")), possibleType.substring(possibleType.indexOf(":")+1, possibleType.length()));
		}
		this._possibleFieldOrderByFields.clear();
		if(andCustom){//&&!_lastFilterOrderByType.equals("CUSTOM")){
			this.populateDatasetFields(_possibleFieldOrderByFields);
			_possibleFieldOrderByTypes.put("CUSTOM","<CUSTOM>");
		}
		if(andFields){
			this.populateDatasetFields(_possibleFieldOrderByFields);
			/*if(!_lastFilterOrderByType.equals("FIELD"))*/ _possibleFieldOrderByTypes.put("FIELD","<FIELD>");
			//if(!_lastFilterOrderByType.equals("VALUE")) _possibleFieldOrderByTypes.put("VALUE","<VALUE>");
		}
	}

	public void construct_new_filter_dataset_fields_conditions_nextaction() throws Exception{
		if(!_appendNextAction){
			construct_new_filter_dataset_fields_conditions();
		}
	}
	
	public void construct_new_filter_dataset_fields_orderby_nextaction() throws Exception{
		if(!_appendNextAction){
			construct_new_filter_dataset_fields_order_bys();
		}
	}
	
	private int _conditionBracelevel=0;
	private ArrayList<Boolean> _conditionBraceLevelState=new ArrayList<Boolean>();
	
	public void construct_new_filter_dataset_fields_conditions_data(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex==0){
			_lastFilterConditionType="";
			_prevFilterConditionType="";
			_filterConditionValid=false;
			_checkedFilterConditions.clear();
			_conditionBracelevel=0;
		}
		else if(rowIndex>0){
			if(rowIndex==1){
				this.action("", "construct_new_filter_dataset_fields_conditions_nextaction", "", "", "","", "ui-icon-refresh", "applyfilterconditions=reset");
			}
			//ID,CONFIGURE_LEADS_FILTER_ID,ELEM_TYPE,ELEM_NAME,ELEM_VALUE,PREV_ELEM_ID,LASTUSERIP,LASTACTIONSTAMP,CREATESTAMP
			
			_lastFilterConditionType=data.get(2);
			
			this._checkedFilterConditions.add(_lastFilterConditionType);
						
			if(_lastFilterConditionType.equals("FIELD")){
				
				this.respondString(" "+data.get(3).substring(0, data.get(3).indexOf("|"))+" ");
			}
			else if(_lastFilterConditionType.equals("VALUE")||_lastFilterConditionType.equals("CUSTOM")){
				this.respondString(" "+(data.get(5).equals("text")?"\"":"")+ data.get(4)+(data.get(5).equals("text")?"\"":"")+" ");
			}
			else{
				this.respondString(" "+data.get(2)+" ");
			}
			_prevFilterConditionType=_lastFilterConditionType;
		}
	}
	
	public void construct_new_filter_dataset_fields_orderby_data(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex==0){
			_lastFilterOrderByType="";
		}
		else if(rowIndex>0){
			if(rowIndex==1){
				this.action("", "construct_new_filter_dataset_fields_orderby_nextaction", "", "", "","", "ui-icon-refresh", "applyfilterorderbys=reset");
			}
			//ID,CONFIGURE_LEADS_FILTER_ID,ELEM_TYPE,ELEM_NAME,ELEM_VALUE,ELEM_ORDERBY,PREV_ELEM_ID,LASTUSERIP,LASTACTIONSTAMP,CREATESTAMP
			
			_lastFilterOrderByType=data.get(2);
						
			if(_lastFilterOrderByType.equals("FIELD")){
				
				this.respondString(" "+data.get(3).substring(0, data.get(3).indexOf("|"))+" ");
			}
			else if(_lastFilterOrderByType.equals("VALUE")||_lastFilterOrderByType.equals("CUSTOM")){
				this.respondString(" "+(data.get(5).equals("text")?"\"":"")+ data.get(4)+(data.get(5).equals("text")?"\"":"")+" ");
			}
			
			this.respondString(" "+data.get(6)+" ");
		}
	}
	
	private boolean _lastBraseConditionFilterState=false;
	private boolean _validCheckedConditionFilter=false;
	
	/*private boolean validCheckedConditionFilter(){
		_conditionBracelevel=0;
		_lastBraseConditionFilterState=false;
		if(_checkedFilterConditions.isEmpty()) return false;
		boolean validCheckedConditionFilter=true;
		String prevFilterConditionType="";
		String lastFilterConditionType="";
		ArrayList<String> checkedFilterConditions=new ArrayList<String>();
		checkedFilterConditions.addAll(_checkedFilterConditions);
		_prevFilterConditionType=_checkedFilterConditions.size()>1?_checkedFilterConditions.get(_checkedFilterConditions.size()-2):"";
		int checkedFilterConditionsIndex=checkedFilterConditions.size()-1;
		if(checkedFilterConditionsIndex<2){
			validCheckedConditionFilter=false;
		}
		else{
			while(checkedFilterConditionsIndex>=0){
				lastFilterConditionType=checkedFilterConditions.get(checkedFilterConditionsIndex);
				if(lastFilterConditionType.equals("FIELD")||lastFilterConditionType.equals("VALUE")){
					if(_openBraseConditionFilters.equals(prevFilterConditionType+":"+prevFilterConditionType+"|")){
						validCheckedConditionFilter=false;
						break;
					}
				}
				else{
					lastFilterConditionType=lastFilterConditionType+":"+lastFilterConditionType+"|";
					if(_openBraseConditionFilters.equals(lastFilterConditionType)){
						_conditionBracelevel--;
						_lastBraseConditionFilterState=false;
					}
					else if(_closeBraseConditionFilters.equals(lastFilterConditionType)){
						_conditionBracelevel++;
						_lastBraseConditionFilterState=true;
					}
					else if(_evaluationConditionFilters.indexOf(lastFilterConditionType)>-1){
						if((prevFilterConditionType.equals("FIELD")||prevFilterConditionType.equals("VALUE"))&&checkedFilterConditionsIndex>=2&&((this._evaluationConditionFilters.indexOf(checkedFilterConditions.get(checkedFilterConditionsIndex-2)+":"+checkedFilterConditions.get(checkedFilterConditionsIndex-2)+"|")>-1))){
							validCheckedConditionFilter=false;
							break;
						}
						else if(this._evaluationConditionFilters.indexOf(prevFilterConditionType+":"+prevFilterConditionType+"|")>-1){
							validCheckedConditionFilter=false;
							break;
						}
						
					}
					else if(_bindConditionFilters.indexOf(lastFilterConditionType)>-1){
						if(_closeBraseConditionFilters.equals(prevFilterConditionType+":"+prevFilterConditionType+"|")){
							validCheckedConditionFilter=false;
							break;
						}
						else if(_evaluationConditionFilters.indexOf(prevFilterConditionType+":"+prevFilterConditionType+"|")>-1){
							validCheckedConditionFilter=false;
							break;
						}
						else if(_bindConditionFilters.indexOf(prevFilterConditionType+":"+prevFilterConditionType+"|")>-1){
							validCheckedConditionFilter=false;
							break;
						}
					}
				}
				prevFilterConditionType=checkedFilterConditions.remove(checkedFilterConditionsIndex--);
			}
		}
		if(validCheckedConditionFilter&&_conditionBracelevel!=0){
			validCheckedConditionFilter=false;
		}
		if(!validCheckedConditionFilter){
			checkedFilterConditions.clear();
		}
		else if(checkedFilterConditions.size()==1){
			checkedFilterConditions.clear();
		}
		return (_validCheckedConditionFilter=validCheckedConditionFilter);
	}*/

	public void start_new_config_over() throws Exception{
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "DELETE FROM <DBUSER>.CONFIGURE_LEADS_FILTER WHERE STATUS='PENDING' AND ID="+this.requestParameter("LEADS_CONFIG_ID")+" DELETE FROM <DBUSER>.CONFIGURE_LEADS_FILTER_CONDITION_ELEMENTS WHERE CONFIGURE_LEADS_FILTER_ID="+this.requestParameter("LEADS_CONFIG_ID")+" DELETE FROM <DBUSER>.CONFIGURE_LEADS_FILTER_ORDER_BY WHERE CONFIGURE_LEADS_FILTER_ID="+this.requestParameter("LEADS_CONFIG_ID"),	 null, null);
	}
	
	private HashMap<String,String> _configDataSources=new HashMap<String, String>();
	
	public void construct_new_filter_datasource() throws Exception{
		_configDataSources.clear();
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "SELECT ID AS DATASOURCE_ID,ALIAS AS DATASOURCE_ALIAS,USAGE_DESCRIPTION FROM <DBUSER>.LEADS_FILTER_DATASOURCE WHERE STATUS='E'",null,this,"configDataSource_Data");
		this.fieldInput("CONFIG_FILTER_DATASOURCE_ID", "", "select", true, null,(Map) _configDataSources);
	}
	
	public void configDataSource_Data(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex>0){
			_configDataSources.put(data.get(0),data.get(1)+" ["+data.get(2)+"]");
		}
	}
	
	private TreeMap<String,String> _configDatasets=new TreeMap<String, String>();
	
	public void construct_new_filter_dataset() throws Exception{
		_configDatasets.clear();
		Database.executeDBRequest(null, this.requestParameter("DATASOURCE_ALIAS"), "SELECT TABLE_NAME AS DATASET,TABLE_NAME FROM "+this.requestParameter("DATASOURCE_DB")+".INFORMATION_SCHEMA.Tables WHERE TABLE_SCHEMA <> 'sysdiagrams' and TABLE_SCHEMA like'%"+this.requestParameter("DATASOURCE_SCHEMA")+"%' ORDER BY TABLE_NAME",null,this,"configDataset_Data");
		this.fieldInput("CONFIG_FILTER_DATASET", "", "select", true, null,(Map) _configDatasets);
	}
	
	public void configDataset_Data(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex>0){
			_configDatasets.put(data.get(0),data.get(1));
		}
	}
	
	public void view_live_filter() throws Exception{
		this.replaceComponentContent("configure_filters");
			this.fieldHidden("LIVE_LEADS_FILTER_ID", this.requestParameter("LIVE_LEADS_FILTER_ID"));
			this.startElement("div", "class=ui-widget-header".split("[|]"),true);
				this.respondString("CONFIGURE FILTER(s)");
			this.endElement("div", true);
			this.simpleElement("br", null);
			
			this.startElement("div", "class=ui-widget-header|style=font-size:0.8em", true);
				this.respondString("FILTER DETAILS");
			this.endElement("div", true);
			if(this.requestParameter("viewaction").equals("delete")){
				Database.executeDBRequest(null, "INOVOLEADSFILTER", "UPDATE <DBUSER>.[LIVE_LEADS_FILTER] SET STATUS='BOOKED-OUT',LASTUSERIP='"+this.requestHeader("REMOTE-HOST")+"' WHERE ID="+this.requestParameter("LIVE_LEADS_FILTER_ID"), null,null);
			}
			Database.executeDBRequest(null, "INOVOLEADSFILTER", "SELECT [FILTER_NAME],[FILTER_USAGE] ,[LASTUSERIP] ,[STATUS] ,[STEP] ,[DATASOURCE_ID] ,[DB]  ,[DBSCHEMA]  ,[DATASET] ,[SOURCEID_FIELD],[CONFIGURE_LEADS_FILTER_ID] ,[CREATESTAMP],[SOURCE_SQLSELECT_STATEMENT] AS [FILTER SOURCE SELECTION] FROM <DBUSER>.[LIVE_LEADS_FILTER] WHERE ID="+this.requestParameter("LIVE_LEADS_FILTER_ID"), null,this,"view_live_filter_data");
			ArrayList<String[]> actionsProperties=new ArrayList<String[]>();
			actionsProperties.add("caption=RETURN TO CONFIGURE FILTER(s)|actiontarget=leads_filter_section|command=configure_filters|urlparams=master_action=configure-filters".split("[|]"));
			if(this.requestParameter("viewaction").equals("delete")){
				actionsProperties.add(("caption=DELETE FILTER|command=confirm_delete_filter|urlparams=live_filter_id="+requestParameter("LIVE_LEADS_FILTER_ID")).split("[|]"));
			}			
			this.actions(actionsProperties, false);
		this.endReplaceComponentContent();
	}
	
	public void confirm_delete_filter() throws Exception{
		this.showDialog("", "title=CONFIRM DELETING FILTER|content=DELETE THE SELECTED FILTER|BUTTON:DELETE=COMMAND=delete_filter|BUTTON:CANCEL=COMMAND=cancel_delete_filter".split("[|]"));
	}
	
	public void delete_filter() throws Exception{
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "EXECUTE <DBUSER>.DELETE_PUBLISHED_LIVE_LEADS_FILTER "+this.requestParameter("LIVE_LEADS_FILTER_ID")+",'"+this.requestHeader("REMOTE-HOST")+"'", null, null);
		cancel_delete_filter();
	}
	
	public void cancel_delete_filter() throws Exception{
		this.replaceComponentContent("leads_filter_section");
			this.setRequestParameter("master_action", "configure-filters", true);
			this.configure_filters();
		this.endReplaceComponentContent();
	}
	
	public void view_live_filter_data(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex==1){
			this.startTable(null);
				this.startRow(null);
					this.startColumn("font-size:0.8em");
						this.respondString(columns.get(0));
					this.endColumn();
					this.startColumn("font-size:0.8em");
						this.respondString(columns.get(1));
					this.endColumn();
				this.endRow();
				this.startRow(null);
					this.startCell("class=ui-widget-content|style=font-size:0.8em;font-weight:bold".split("[|]"));
						this.respondString(data.get(0));
					this.endColumn();
					this.startCell("class=ui-widget-content|style=font-size:0.8em;font-weight:bold".split("[|]"));
						this.respondString(data.get(1));
					this.endCell();
				this.endRow();
			this.endTable();
				
			this.startTable(null);
				this.startRow(null);
					this.startColumn("font-size:0.8em");
						this.respondString(columns.get(12));
					this.endColumn();
				this.endRow();
				this.startRow(null);
					this.startCell("class=ui-widget-content|style=font-size:0.8em;font-weight:bold".split("[|]"));
						this.respondString(data.get(12));
					this.endCell();
				this.endRow();
			this.endTable();
		}		
	}
	
	private TreeMap<String,String> _configDatasetFields=new TreeMap<String, String>();
	private ArrayList<String> _configuredDatasetFields=new ArrayList<String>();
	public void registered_configured_filters() throws Exception{
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "EXECUTE <DBUSER>.CANCEL_BOOKED_OUT_PREP_LIVE_LEAD_FILTERS_BY_LASTUSERIP @LASTUSERIP='"+this.requestHeader("REMOTE-HOST")+"'",null,null);
		this.startTable(null);
			Database.executeDBRequest(null, "INOVOLEADSFILTER", "SELECT LIVE_LEADS_FILTER.ID ,LIVE_LEADS_FILTER.FILTER_NAME,LIVE_LEADS_FILTER.FILTER_USAGE,LIVE_LEADS_FILTER.LASTUSERIP,ISNULL((SELECT TOP 1 STATUS FROM <DBUSER>.PREP_LIVE_LEADS_FILTER WHERE LIVE_FILTER_ID=LIVE_LEADS_FILTER.ID AND STATUS='APPLY'),LIVE_LEADS_FILTER.STATUS) AS STATUS,LIVE_LEADS_FILTER.STEP,LIVE_LEADS_FILTER.DB,LIVE_LEADS_FILTER.DBSCHEMA,LIVE_LEADS_FILTER.DATASET,LIVE_LEADS_FILTER.SOURCEID_FIELD,LIVE_LEADS_FILTER.CONFIGURE_LEADS_FILTER_ID,LIVE_LEADS_FILTER.CREATESTAMP,LIVE_LEADS_FILTER.SOURCE_SQLSELECT_STATEMENT FROM <DBUSER>.LIVE_LEADS_FILTER ORDER BY FILTER_NAME", null, this,"registered_configured_filters_data");
		this.endTable();
	}
	
	public void registered_configured_filters_data(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex==0){
			this.startRow(null);
				this.startCell(null);this.endCell();
				this.startColumn("font-size:0.8em");
					this.respondString("FILTER");
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respondString("FILTER USAGE");
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respondString("FILTER SOURCE SELECTION");
				this.endColumn();
			this.endRow();
		}
		else{
			this.startRow(null);
				this.startCell("style=vertical-align:top;display:inline".split("[|]"));
					if(this.requestParameter("master_action").equals("configure-filters")){
						if(data.get(4).equals("PUBLISHED")||data.get(3).equals(this.requestHeader("REMOTE-HOST"))){
							if(data.get(4).equals("APPLY")){
								this.action("[buzy-applying]","busy_processing_filter", "", "", "", "ui-icon-gear", "", "leads_filter_id="+data.get(0));
							}
							else{
								//this.action("[view]","view_live_filter", "", "", "", "", "", "live_leads_filter_id="+data.get(0));
								this.action("[delete]","view_live_filter", "", "", "", "", "", "live_leads_filter_id="+data.get(0)+"&viewaction=delete");
							}
						}
						else{
							if(data.get(4).equals("APPLY")){
								this.action("[buzy-applying]","busy_processing_filter", "", "", "", "ui-icon-gear", "", "leads_filter_id="+data.get(0));
							}
							else if(data.get(4).equals("BOOKED-OUT")){
								this.action("[booked]","busy_bookedout_filter", "", "", "", "ui-icon-gear", "", "leads_filter_id="+data.get(0));
							}
						}
					}
					else if(this.requestParameter("master_action").equals("apply-filters")){
						if(data.get(4).equals("PUBLISHED")||data.get(3).equals(this.requestHeader("REMOTE-HOST"))){
							if(data.get(4).equals("APPLY")){
								this.action("[buzy-applying]","busy_processing_filter", "", "", "", "ui-icon-gear", "", "leads_filter_id="+data.get(0));
							}
							else{
								this.action("[apply]","apply_live_filter", "", "", "", "", "", "leads_filter_id="+data.get(0));
							}
						}
						else{
							if(data.get(4).equals("APPLY")){
								this.action("[buzy-applying]","busy_processing_filter", "", "", "", "ui-icon-gear", "", "leads_filter_id="+data.get(0));
							}
							else{
								this.action("[booked]","busy_bookedout_filter", "", "", "", "ui-icon-gear", "", "leads_filter_id="+data.get(0));//this.respondString("[booked]");//data.get(4));
							}
						}
					}
				this.endCell();
				this.startColumn("font-size:0.8em");
					this.respondString(data.get(1));
				this.endColumn();
				this.startColumn("font-size:0.8em");
					this.respondString(data.get(2));
				this.endColumn();
				this.startCell("class=ui-widget-content|style=font-size:0.8em;font-weight:bold".split("[|]"));
					this.respondString(data.get(12));
				this.endCell();
			this.endRow();
		}
	}
	
	public void busy_processing_filter() throws Exception{
		
	}
	
	public void busy_bookedout_filter() throws Exception{
		
	}
	
	//APPLY FILTER(s)
	public void apply_filters() throws Exception{
		this.setRequestParameter("MASTER_ACTION", "apply-filters", true);
		this.startElement("div", "id=aply_filters".split("[|]"), true);
			this.startElement("div", "class=ui-widget-header".split("[|]"),true);
				this.respondString("APPLY FILTER(s)");
			this.endElement("div", true);
			this.startElement("div", "class=ui-widget|id=apply_filters_view".split("[|]"),true);
				this.registered_configured_filters();
			this.endElement("div", true);
		this.endElement("div", true);
	}
	
	public void apply_live_filter() throws Exception{
		this.fieldHidden("leads_filter_id", this.requestParameter("leads_filter_id"));
		this.fieldHidden("PREPSTATUS", "BOOKED-OUT");
		
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "UPDATE <DBUSER>.LIVE_LEADS_FILTER SET STATUS='BOOKED-OUT',LASTUSERIP='"+this.requestHeader("REMOTE-HOST")+"' WHERE ID="+ this.requestParameter("leads_filter_id"), null,null);
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "SELECT FILTER_NAME,FILTER_USAGE FROM <DBUSER>.LIVE_LEADS_FILTER  WHERE ID="+ this.requestParameter("leads_filter_id"), null, this,"apply_live_filter_data");
		this.startElement("div", "id=applied_live_filter_priority", true);
			this.applied_live_filter_priority();
		this.endElement("div", true);
		this.startElement("div", "id=applied_live_filter_campaign", true);
			this.applied_live_filter_campaign();
		this.endElement("div", true);
		this.flushReplaceComponentContent("apply_filters_view");
	}
	
	private TreeMap<Long,String> _selecedCampaigns=new TreeMap<Long, String>();
	public void applied_live_filter_campaign() throws Exception{
		String selectedCampaignid=this.requestParameter("SELECTED_CAMPAIGN_ID");
		if(selectedCampaignid.equals("")){
			Database.executeDBRequest(null, "PRESENCE", "SELECT ID,NAME FROM <DBUSER>.PCO_OUTBOUNDSERVICE ORDER BY NAME", null,this, "applied_live_filter_campaign_data");
			this.fieldInput("SELECTED_CAMPAIGN_ID", "", "select", true, null,(Map)_selecedCampaigns);
			this.action("SELECT CAMPAIGN", "applied_live_filter_campaign", "", "", "applied_live_filter_campaign", "", "", "");
		}
		else if(!selectedCampaignid.equals("")){
			Database.executeDBRequest(null, "PRESENCE", "SELECT ID,NAME FROM <DBUSER>.PCO_OUTBOUNDSERVICE ORDER BY NAME", null,this, "applied_live_filter_campaign_data");
			this.fieldInput("SELECTED_CAMPAIGN_ID", selectedCampaignid, "select", true, null,(Map)_selecedCampaigns);
			this.action("SELECT CAMPAIGN", "applied_live_filter_campaign", "", "", "applied_live_filter_campaign", "", "", "");
			//this.fieldHidden("SELECTED_CAMPAIGN_ID", selectedCampaignid);
			HashMap<String,String> selectedServiceDetails=new HashMap<String, String>();
			selectedServiceDetails.put("SERVICEID", selectedCampaignid);
			selectedServiceDetails.put("SERVICENAME", "");
			if(!selectedServiceDetails.get("SERVICEID").equals("")){
				Database.executeDBRequest(null, "PRESENCE", "SELECT NAME AS SERVICENAME FROM <DBUSER>.PCO_OUTBOUNDSERVICE WHERE ID=:SERVICEID", selectedServiceDetails, null);
				//this.respondString("CAMPAIGN:"+selectedServiceDetails.get("SERVICENAME"));
				this.constructSelectUnSelect("SELECTED_CAMPAIGN_LOAD");
				this.startTable(null);
					Database.executeDBRequest(null, "PRESENCE", "SELECT LOADID,STATUS,DESCRIPTION,RDATE FROM <DBUSER>.PCO_LOAD WHERE SERVICEID=:SERVICEID ORDER BY RDATE DESC",selectedServiceDetails, this,"applied_live_filter_campaign_load_data");
					if(_loadCellCount>=0){
						if(_loadCellCount<_maxLoadCellCount){
							this.startCell(("cols="+String.valueOf((_maxLoadCellCount-_loadCellCount))).split("[|]"));this.endCell();
						}
						this.endRow();
					}
				this.endTable();
				this.startElement("div", "id=confirm_applying_filter", true);
					this.action("CONFIRM APPLYING FILTER", "confirm_applying_filter", "", "", "", "", "", "");
					this.action("CANCEL APPLYING FILTER", "cancel_confirm_applying_filter", "", "", "", "", "", "");
				this.endElement("div", true);
			}
			this.flushReplaceComponentContent("applied_live_filter_campaign");
		}
	}
	
	public void cancel_confirm_applying_filter() throws Exception{
		this.setRequestParameter("PREPSTATUS", "CANCEL", true);
		this.confirm_applying_filter();
		
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "UPDATE <DBUSER>.LIVE_LEADS_FILTER SET STATUS='PUBLISHED' WHERE ID="+ this.requestParameter("leads_filter_id"), null,null);
		this.setRequestParameter("master_action","apply-filters",true);
		this.apply_filters();//this.registered_configured_filters();
		this.flushReplaceComponentContent("leads_filter_section");
	}
	
	public void apply_prepped_filter() throws Exception{
		this.setRequestParameter("PREPSTATUS", "APPLY", true);
		this.confirm_applying_filter();
		
	}
	
	public void constructSelectUnSelect(String className) throws Exception{
		this.startElement("input",( "id="+className.replaceAll("[-]", "_")+"_selectall|style=font-weight:bold;font-size:0.8em|type=button|value=SELECT ALL").split("[|]"), false);this.endElement("input", false);
		this.startElement("input",( "id="+className.replaceAll("[-]", "_")+"_unselectall|style=font-weight:bold;font-size:0.8em|type=button|value=UNSELECT ALL").split("[|]"), false);this.endElement("input", false);
		
		this.startScript(null, null);
			this.respondString("$('#"+className.replaceAll("[-]", "_")+"_selectall').unbind('click');");
			this.respondString("$('#"+className.replaceAll("[-]", "_")+"_selectall').click(function(){checkCheckBoxes('."+className+"');});");
			
			this.respondString("$('#"+className.replaceAll("[-]", "_")+"_unselectall').unbind('click');");
			this.respondString("$('#"+className.replaceAll("[-]", "_")+"_unselectall').click(function(){uncheckCheckBoxes('."+className+"');});");
		this.endScript();
	}
	
	public void confirm_applying_filter() throws Exception{
		String prepstatus=this.requestParameter("PREPSTATUS");
		StringBuilder loadsXml=new StringBuilder();
		HashMap<String,String> prepLeadsFilterSettings=new HashMap<String, String>();
		prepLeadsFilterSettings.put("LEADS_FILTER_ID", this.requestParameter("leads_filter_id"));
		
		if(prepstatus.equals("BOOKED-OUT")||prepstatus.equals("APPLY")){
			ArrayList<String> selectedCampaignLoad=this.requestParameterArray("SELECTED_CAMPAIGN_LOAD");
			
			loadsXml.append("<Loads>");
			int loadsIndex=0;
			
			while(loadsIndex<selectedCampaignLoad.size()){
				loadsXml.append("<Load>");
					loadsXml.append("<id>"+selectedCampaignLoad.get(loadsIndex++)+"</id>");
				loadsXml.append("</Load>");
			}
			loadsXml.append("</Loads>");
		}
		
		prepLeadsFilterSettings.put("LOADSCONFIG", loadsXml.substring(0,loadsXml.length()));
		prepLeadsFilterSettings.put("PREPSTATUS", prepstatus);
		prepLeadsFilterSettings.put("SERVICEID", this.requestParameter("SELECTED_CAMPAIGN_ID"));
		prepLeadsFilterSettings.put("PRIORITY_OFFSET", this.requestParameter("DEFAULT_PRIORITY"));
		prepLeadsFilterSettings.put("PRIORITY_DIRECTION", this.requestParameter("PRIORITY_DIRECTION"));
		Database.executeDBRequest(null, "INOVOLEADSFILTER", "EXECUTE <DBUSER>.[PREP_NEXT_LEADS_FILTER] @PREPSTATUS=:PREPSTATUS, @LEADS_FILTER_ID=:LEADS_FILTER_ID,@PRIORITY_OFFSET=:PRIORITY_OFFSET,@PRIORITY_DIRECTION=:PRIORITY_DIRECTION,@SERVICEID=:SERVICEID,@LOADS=:LOADSCONFIG", prepLeadsFilterSettings, null);
		
		if(prepstatus.equals("BOOKED-OUT")||prepstatus.equals("APPLY")){
			prepLeadsFilterSettings.put("PREP_LIVE_LEADS_FILTER_ID","0");
			prepLeadsFilterSettings.put("TOTAL_LEADS_AFFECTED","0");
			Database.executeDBRequest(null, "INOVOLEADSFILTER","SELECT ISNULL(MAX(ID),0) AS PREP_LIVE_LEADS_FILTER_ID FROM <DBUSER>.PREP_LIVE_LEADS_FILTER WHERE PREP_LIVE_LEADS_FILTER.LIVE_FILTER_ID=:LEADS_FILTER_ID AND PREP_LIVE_LEADS_FILTER.LEAD_SERVICE_ID=:SERVICEID AND LEAD_DEFAULT_PRIORITY=:PRIORITY_OFFSET AND  LEAD_DEFAULT_PRIORITY_DIRECTION=:PRIORITY_DIRECTION AND STATUS=:PREPSTATUS",prepLeadsFilterSettings,null);
			
			Database.executeDBRequest(null,"INOVOLEADSFILTER","SELECT COUNT(*) AS TOTAL_LEADS_AFFECTED FROM <DBUSER>.PREP_LIVE_LEADS_FILTER_SERVICE_LOAD_SOURCE_LEADS WHERE PREP_LIVE_LEADS_FILTER_ID=:PREP_LIVE_LEADS_FILTER_ID",prepLeadsFilterSettings,null);
			if(!prepstatus.equals("APPLY")&&((prepstatus.equals("BOOKED-OUT")||prepLeadsFilterSettings.get("TOTAL_LEADS_AFFECTED").equals("0")))){
				this.startTable(null);
					this.startRow(null);
						this.startColumn("font-size:0.8em");
							this.respondString("TOTAL LEADS AFFECTED");
						this.endColumn();
						this.startCell(null);
							this.respondString(prepLeadsFilterSettings.get("TOTAL_LEADS_AFFECTED"));
						this.endCell();
					this.endRow();
				this.endTable();
				this.action("CONFIRM APPLYING FILTER", "confirm_applying_filter", "", "", "", "", "", "");
				if(!prepLeadsFilterSettings.get("TOTAL_LEADS_AFFECTED").equals("0")&&!prepLeadsFilterSettings.get("TOTAL_LEADS_AFFECTED").equals("")){
					this.action("APPLY FILTER", "apply_prepped_filter", "", "", "", "", "", "");
				}
				this.action("CANCEL APPLYING FILTER", "cancel_confirm_applying_filter", "", "", "", "", "", "");
				this.flushReplaceComponentContent("confirm_applying_filter");
				InovoServletContextListener.inovoServletListener().logDebug("prepLeadsFilterSettings -"+prepLeadsFilterSettings.toString());
			}
			else if(prepstatus.equals("APPLY")){
				Database.executeDBRequest(null, "INOVOLEADSFILTER", "UPDATE <DBUSER>.LIVE_LEADS_FILTER SET STATUS='PUBLISHED' WHERE ID="+ this.requestParameter("leads_filter_id"), null,null);
				this.setRequestParameter("master_action","apply-filters",true);
				this.apply_filters();//this.registered_configured_filters();
				this.flushReplaceComponentContent("leads_filter_section");
			}
		}
	}

	public void applied_live_filter_campaign_data(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex==0){
			
		}
		else{
			_selecedCampaigns.put(Long.parseLong(data.get(0)),""+data.get(0)+" - "+data.get(1));
		}
	}
	
	private int _loadCellCount=0;
	private int _maxLoadCellCount=6;
	public void applied_live_filter_campaign_load_data(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex==0){
			_loadCellCount=0;
			this.startRow(null);
		}
		else{
			if((_loadCellCount+=1)==_maxLoadCellCount){
				this.endRow();
				this.startRow(null);
				_loadCellCount=0;
			}
			//else{
				//LOADID,STATUS.DESCRIPTION,RDATE
				//this.startCell(null);
				//	this.startTable(null);
				//		this.startRow(null);
							this.startColumn("font-size:0.8em");
								this.startTable(null);
									this.startRow(null);
										this.startCell("style=width:10px".split("[|]")); 
											this.fieldInput("SELECTED_CAMPAIGN_LOAD", data.get(0), "checkbox", true, "class=SELECTED_CAMPAIGN_LOAD".split("[|]"));
										this.endCell();
										this.startCell(null);
											this.respondString("["+data.get(1)+"]"+" "+data.get(0));
										this.endCell();
									this.endRow();
									this.startRow(null);
										this.startCell("colspan=2".split("[|]"));
											this.respondString(data.get(2));
										this.endCell();
									this.endRow();
								this.endTable();	
								
							this.endColumn();
				//		this.endRow();
				//	this.endTable();
				//this.endCell();
			//}
		}
	}

	public void applied_live_filter_priority() throws Exception{
		this.fieldLabel("DEFAULT PRIORITY");
		this.fieldInput("DEFAULT_PRIORITY", this.requestParameter("DEFAULT_PRIORITY").equals("")?"1000":this.requestParameter("DEFAULT_PRIORITY"),	"text", true, null);
		HashMap<String,String> priorityDirection=new HashMap<String, String>();
		priorityDirection.put("ASC", "ASC");
		priorityDirection.put("DESC", "DESC");
		this.fieldInput("PRIORITY_DIRECTION", this.requestParameter("PRIORITY_DIRECTION").equals("")?"ASC":this.requestParameter("PRIORITY_DIRECTION"), "select", true, null,(Map)priorityDirection);
		
		this.action("UPDATE PRIORITY", "update_live_filter_priority", "", "", "", "", "", "");
	}
	
	public void update_live_filter_priority() throws Exception{
		String default_priority=this.requestParameter("DEFAULT_PRIORITY");
		if(this.isNumeric(default_priority.toCharArray())){
			if(default_priority.equals("0")){
				this.setRequestParameter("DEFAULT_PRIORITY","10",true);
			}
			this.applied_live_filter_priority();
			this.flushReplaceComponentContent("applied_live_filter_priority");
		}
		else{
			this.showDialog("", "title=INVALID DEFAULT PRIORITY|content=INVALID DEFAULT PRIORITY ENTRY".split("[|]"));
		}
	}
	
	public void apply_live_filter_data(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex==1){
			this.startTable(null);
				this.startRow(null);
					this.startColumn("");
						this.respondString(data.get(0).equals(data.get(1))?data.get(0):(data.get(0)+"["+data.get(1)+"]"));
					this.endColumn();
				this.endRow();
				this.startRow(null);
					this.startCell(null);
						Database.executeDBRequest(null, "INOVOLEADSFILTER", "SELECT * FROM <DBUSER>.LIVE_LEADS_FILTER_CONDITION_ELEMENTS WHERE LIVE_LEADS_FILTER_ID="+this.requestParameter("leads_filter_id"), null, this,"apply_live_filter_conditions_data");
						Database.executeDBRequest(null, "INOVOLEADSFILTER", "SELECT * FROM <DBUSER>.LIVE_LEADS_FILTER_ORDER_BY WHERE LIVE_LEADS_FILTER_ID="+this.requestParameter("leads_filter_id"), null, this,"apply_live_filter_order_by_data");
					this.endCell();
				this.endRow();
			this.endTable();
		
		}
	}
	
	public void apply_live_filter_conditions_data(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex==0){
			_lastFilterConditionType="";
			_prevFilterConditionType="";
			_filterConditionValid=false;
			_checkedFilterConditions.clear();
			_conditionBracelevel=0;
		}
		else if(rowIndex>0){
			
			_lastFilterConditionType=data.get(3);
			
			if(_lastFilterConditionType.equals("FIELD")){
				
				this.respondString(" "+data.get(4).substring(0, data.get(4).indexOf("|"))+" ");
			}
			else if(_lastFilterConditionType.equals("VALUE")||_lastFilterConditionType.equals("CUSTOM")){
				this.respondString(" "+(data.get(6).equals("text")?"\"":"")+ data.get(5)+(data.get(6).equals("text")?"\"":"")+" ");
			}
			else{
				this.respondString(" "+data.get(3)+" ");
			}
		}
	}
	
	public void apply_live_filter_order_by_data(Integer rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex==0){
			_lastFilterOrderByType="";
		}
		else if(rowIndex>0){
			
			if(rowIndex==1){
				this.respondString(" ORDER BY ");
			}
			
			//ID,CONFIGURE_LEADS_FILTER_ID,ELEM_TYPE,ELEM_NAME,ELEM_VALUE,ELEM_ORDERBY,PREV_ELEM_ID,LASTUSERIP,LASTACTIONSTAMP,CREATESTAMP
			
			_lastFilterOrderByType=data.get(3);
						
			if(_lastFilterOrderByType.equals("FIELD")){
				
				this.respondString(" "+data.get(4).substring(0, data.get(4).indexOf("|"))+" ");
			}
			else if(_lastFilterOrderByType.equals("VALUE")||_lastFilterOrderByType.equals("CUSTOM")){
				this.respondString(" "+(data.get(6).equals("text")?"\"":"")+ data.get(5)+(data.get(6).equals("text")?"\"":"")+" ");
			}
			
			this.respondString(" "+data.get(7)+" ");
		}
	}

	private static boolean _shutdownLeadsPrioritisation=false;
	public static void startupLeadsPrioritisation() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!_shutdownLeadsPrioritisation){
					try {
						this.prioritiseActiveLeads();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					try {
						Thread.sleep(10*1024);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			public void prioritiseActiveLeads() throws Exception{
				Database.executeDBRequest(null, "INOVOLEADSFILTER", "EXECUTE <DBUSER>.APPLY_PRIORITY_NEXT_ACTIVE_FILTER_LEADS", null,null);
			}
			
		}).start();
	}

	public static void shutdownLeadsPrioritisation() {
		_shutdownLeadsPrioritisation=true;
	}
	
}
