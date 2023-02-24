package inovo.monitoring.dashboards;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import inovo.web.InovoHTMLWebWidget;
import inovo.web.InovoWebWidget;

public class MonitorDashboard extends InovoHTMLWebWidget {

	private String _monitorLabel="";
	private HashMap<Integer,HashMap<String,Object>> _monitorStatusInfo=null;
	public MonitorDashboard(InovoWebWidget parentWidget, InputStream inStream,String monitorLabel,HashMap<Integer,HashMap<String,Object>> monitorStatusInfo) {
		super(parentWidget, inStream);
		_monitorLabel=monitorLabel;
		_monitorStatusInfo=monitorStatusInfo;
	}
	
	@Override
	public void executeContentWidget() throws Exception {
		this.startTable(null);
			this.startRow(null);
				this.startColumn("");
					this.respondString(_monitorLabel);
				this.endColumn();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
				
					this.startTable(null);
					boolean doneColumns=false;
					
					HashMap<String,Object> monReqValues=new HashMap<String,Object>();
					ArrayList<String> monReqValNames=new ArrayList<String>();
					for(Integer rowIndex:_monitorStatusInfo.keySet()){
						monReqValues.clear();
						monReqValues.putAll(_monitorStatusInfo.get(rowIndex));
						
						if(!doneColumns){
							this.startRow(null);
							for(String valName:monReqValues.keySet()){
								monReqValNames.add(valName);
								this.startColumn("font-size:0.8em");
									this.respondString(valName);
								this.endColumn();
							}
							this.endRow();
							doneColumns=true;
						}
						this.startRow(null);
						for(String monReqValKey:monReqValNames){
							this.startCell(new String[]{"style=font-size:0.8em"});
								Object monRegVal=monReqValues.get(monReqValKey);
								if(monRegVal instanceof String){
									this.respondString((String)monRegVal);
								}
								else{
									this.respondMonitorRequestValue(monReqValKey,monRegVal);
								}
							this.endCell();
						}
						this.endRow();
					}
					
					this.endTable();
				this.endCell();
			this.endRow();
		this.endTable();
	}

	public void respondMonitorRequestValue(String monReqValKey,
			Object monRegVal) {
		
	}
}
