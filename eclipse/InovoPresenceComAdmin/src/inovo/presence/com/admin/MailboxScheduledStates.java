package inovo.presence.com.admin;

import java.io.InputStream;
import java.util.ArrayList;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class MailboxScheduledStates extends InovoHTMLPageWidget {

	public MailboxScheduledStates(InovoWebWidget parentWidget,
			InputStream inStream) {
		super(parentWidget, inStream);
	}
	
	@Override
	public void pageContent() throws Exception {
		this.startTable();
			Database.executeDBRequest(null, Database.dballias("ADMINAO"), "SELECT MAILID,CURRENTSTATUS,REQUIREDSTATUS,CANCHANGE FROM <DBUSER>.ACTIVE_MAILBOXES()", null, this,"enableDisableMailBoxesData");
		this.endTable();
	}
	
	public void enableDisableMailBoxesData(int rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex==0){
			this.startRow();
			for(String val:columns){
				this.startColumn("font-size:0.6em");
					this.respondString(val);
				this.endColumn();
			}
			this.endRow();
		}
		else{
			this.startRow();
			for(String val:data){
				this.startColumn("font-size:0.6em");
					this.respondString(val);
				this.endColumn();
			}
			this.endRow();
		}
	}
}
