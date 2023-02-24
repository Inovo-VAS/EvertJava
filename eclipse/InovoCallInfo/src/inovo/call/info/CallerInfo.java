package inovo.call.info;

import java.io.InputStream;
import java.util.HashMap;

import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class CallerInfo extends InovoHTMLPageWidget {

	public CallerInfo(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

	@Override
	public void pageContent() throws Exception {
		HashMap<String,Object> params=new HashMap<String, Object>();
		this.importRequestParametersIntoMap(params, null);
		
		if(!params.isEmpty()){
			this.startTable();
				this.startRow();
					this.startColumn("font-size:0.8em");
						this.respondString("LABEL");
					this.endColumn();
					this.startColumn("font-size:0.8em");
						this.respondString("INFO");
					this.endColumn();
				this.endRow();
			for(String param : params.keySet()){
				this.startRow();
					this.startColumn("font-size:0.8em");
						this.respondString(param.toUpperCase());
					this.endColumn();
					this.startCell("style=font-size;font-weight:bold:0.8em".split("[|]"));
						this.respondString(params.get(param)==null?"":params.get(param) instanceof String?(String)params.get(param):params.get(param).toString());
					this.endCell();
				this.endRow();
			}
			
			this.endTable();
		}
	}
}
