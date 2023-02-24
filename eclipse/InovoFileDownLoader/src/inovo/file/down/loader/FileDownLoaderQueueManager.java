package inovo.file.down.loader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;

//import ftp4j.FTPClient;
//import ftp4j.FTPFile;

public class FileDownLoaderQueueManager {

	private static FileDownLoaderQueueManager _fileDownLoaderQueueManager=null;
	
	protected boolean _shutdownQueue=false;
	private String _fileDownloadPath="";
	private String _fileSourcePath="";
	private String _fspuser="presence";
	private String _fsppassword="u6moic";
	private String _fspdomain="f.clicksgroup.co.za";
	private FileDownLoaderQueueManager(String fileDownloadPath,String fileSourcePath,String[]filePrefixes){
		this._fileDownloadPath=fileDownloadPath.replaceAll("[\\\\]", "/");
		this._fileSourcePath=fileSourcePath;
		while(!this._fileDownloadPath.endsWith("/")) this._fileDownloadPath=this._fileDownloadPath+"/";
		if(filePrefixes!=null){
			if(filePrefixes.length>0){
				for(String filePrefix:filePrefixes){
					if(_fileAlliasPrefixes.indexOf(filePrefix)==-1){
						this._fileAlliasPrefixes.add(filePrefix);
						this.log_debug("LOAD PREFIX:"+filePrefix);
					}
				}
			}
		}
	}
	
	public static FileDownLoaderQueueManager fileDownLoaderQueueManager(String fileDownloadPath,String fileSourcePath,String[]filePrefixes) {
		return (_fileDownLoaderQueueManager==null?_fileDownLoaderQueueManager=new FileDownLoaderQueueManager(fileDownloadPath,fileSourcePath, filePrefixes):_fileDownLoaderQueueManager);
	}

	public void initiateFileDownLoaderQueue(){
		new Thread(){
			public void run() {
				while(!_shutdownQueue){
					try{
						processFileDownLoadProcess();
					}
					catch(Exception e){
						log_debug("ERROR[processFileDownLoadProcess()]:"+e.getMessage());
					}
					try {
						synchronized (_fileDownLoaderQueueManager) {
							_fileDownLoaderQueueManager.wait(10*1024);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}				
			};
		}.start();
	}
	
	private ArrayList<String> _fileAlliasPrefixes=new ArrayList<String>();
	private JSch _jsch = null;
	protected void processFileDownLoadProcess() throws Exception{
		com.jcraft.jsch.Session session = null;
		try{
			if(_jsch==null) _jsch=new JSch();
			session = _jsch.getSession(_fspuser,_fspdomain,22);
			session.setPassword(_fsppassword);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			com.jcraft.jsch.Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp channelSftp = (ChannelSftp)channel;
			channelSftp.cd(this._fileSourcePath);//"/Home/biztalk/PresenceAutoDialler");
			
			//fileAlliasPrefixes.add("Baby-YesResponse*18months");
			//fileAlliasPrefixes.add("Baby-YesResponse*9months");
			//fileAlliasPrefixes.add("Baby-YesResponse*14Weeks");
			//fileAlliasPrefixes.add("Baby-YesResponse*10Weeks");
			//fileAlliasPrefixes.add("Baby-YesResponse*6Weeks");
			//fileAlliasPrefixes.add("43868_Daily*");
			//fileAlliasPrefixes.add("43515_Daily*");
			//fileAlliasPrefixes.add("43500_Daily*");
			//fileAlliasPrefixes.add("RTNM*");
					
			for(String fileAlliasPrefix:_fileAlliasPrefixes){
				fileAlliasPrefix=fileAlliasPrefix+".*";
				Vector<ChannelSftp.LsEntry> list = channelSftp.ls(fileAlliasPrefix);
				for(int findex=0;findex<list.size();findex++) {
					String fileNameFull=list.get(findex).getFilename();
					channelSftp.get(fileNameFull,/*"D:\\Software\\Inovo\\downloadfolder\\"*/this._fileDownloadPath+ fileNameFull);
					this.log_debug("DOWNLOADED FILE:"+this._fileDownloadPath+ fileNameFull);
				}
				
				for(int findex=0;findex<list.size();findex++) {
					String fileNameFull=list.get(findex).getFilename();
					String processedFileName="processed"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+fileNameFull;
					channelSftp.rename(fileNameFull,processedFileName);
					this.log_debug("RENAMED FILE ON SOURCE SERVER:[from]"+fileNameFull+" [to]"+processedFileName);
				}
			}
			
			session.disconnect();
		}
		catch(Exception ex){
			this.log_debug("ERROR:"+ex.getMessage());
			if(session!=null){
				try{
					session.disconnect();
					session=null;
				}
				catch(Exception es){
					this.log_debug("SESSION-DISCONNECT-ERROR:"+ex.getMessage());
					session=null;
				}
			}
			Thread.sleep(30*1024);
		}
	}
	
	/*protected void downloadFiles(){
		TreeMap<Integer, ArrayList<String>> activefilealliases=null;
		ArrayList<String> listedAlliasses=new ArrayList<String>();
		try{
			activefilealliases=Database.executeDBRequest("FLATFILELEADSIMPORTER","SELECT [ALLIAS] FROM <DBUSER>.[LEADSDATAFILEALLIAS] WHERE UPPER([ENABLEALLIAS])='TRUE'", null);
			for(int rowindex:activefilealliases.keySet()){
				if(rowindex==0) continue;
				listedAlliasses.add(activefilealliases.get(rowindex).get(0).toUpperCase());
			}
		}
		catch(Exception e){
		}
		try{
			
			File flocaldir=new File(_ftpsettingsfound.get("LOCALFOLDER"));
			if(flocaldir.exists()){
				
				String ftplocaldir=flocaldir.getPath();
				String ftpprocessedfolder=_ftpsettingsfound.get("PROCESSEDFOLDER");
				while(!ftplocaldir.endsWith(File.separator)){ftplocaldir+=File.separator;}
				FTPClient ftpcn=new FTPClient();
				ftpcn.connect(_ftpsettingsfound.get("HOST"),21);
				ftpcn.login(_ftpsettingsfound.get("USERNAME"), _ftpsettingsfound.get("PASSWORD"));
								
				FTPFile[]listedfiles=ftpcn.list();
				String flocalSessionPrefix=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
				
				for(FTPFile ftpf:listedfiles){
					if(ftpf.getType()==FTPFile.TYPE_FILE){
						String ftpfname=ftpf.getName();
						String maxFAllias="";
						for(String fallias:listedAlliasses){
							if(ftpfname.toUpperCase().startsWith(fallias)){
								maxFAllias=fallias;
							}
						}
						if(maxFAllias.equals("")) continue;
						
						String ftpflname=ftplocaldir+flocalSessionPrefix+ftpfname;
						File fidown=new File(ftpflname);
						ftpcn.download(ftpfname, fidown);
						fidown.renameTo(new File(ftplocaldir+ftpfname));
						ftpcn.rename(ftpfname, ftpprocessedfolder+ftpfname);
					}
				}
				
				ftpcn.disconnect(true);
			}
		}
		catch(Exception ftpe){
			
		}
	}*/

	private void log_debug(String message) {
		inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug(message);		
	}

	public void shutdownImportQueue() {
		_shutdownQueue=true;		
	}
}
