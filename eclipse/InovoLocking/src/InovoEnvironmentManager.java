import inovo.LockingFilesImporter;
import inovo.db.Database;
import inovo.http.HttpClient;
import inovo.servlet.InovoCoreEnvironmentManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

public class InovoEnvironmentManager extends InovoCoreEnvironmentManager{
	@Override
	public void initializeServletContext(ServletContext sc) {
		super.initializeServletContext(sc);
		LockingFilesImporter.lockingFilesImporter();
		
		
	}
	
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		return super.defaultLocalPath(suggestedlocalpath);
	}
	
	@Override
	public void disposeServletContext(ServletContext sc) {
		LockingFilesImporter.lockingFilesImporter().shutdown();
		
		super.disposeServletContext(sc);
	}
}
