package inovo.monitoring.monitors.db;

import inovo.db.Database;
import inovo.monitoring.monitors.Monitor;

public class DBMonitor extends Monitor {

	public Database database(String dballias){
		return Database.dballias(dballias);
	}
}
