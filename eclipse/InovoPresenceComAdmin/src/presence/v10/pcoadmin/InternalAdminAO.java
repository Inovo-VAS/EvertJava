package presence.v10.pcoadmin;

public class InternalAdminAO extends presence.pcoadmin.InternalAdminAO{

	private presence.v10.pcoadmin.IAdministratorAO IAdminAO=null;
	public InternalAdminAO(){
		this.IAdminAO=presence.v10.pcoadmin.ClassFactory.createAdministratorAO();
	}
	
	public presence.v10.pcoadmin.IAdministratorAO iadminAO(){
		return this.IAdminAO;
	}
	
	@Override
	public void connectToPresence(String serverlabel){
		try{
			if(serverlabel!=null&&this.IAdminAO.connect(serverlabel)){
				System.out.println("Connected to presence ["+serverlabel+"]");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static InternalAdminAO adminAO=null;
	public static InternalAdminAO adminAO(){
		return adminAO==null?(adminAO=new InternalAdminAO()):adminAO;
	}
	
	public static void disposeAdminAO() {
		if(adminAO!=null){
			adminAO.cleanupAdminAO();
		}
	}
	
	private void cleanupAdminAO() {
		this.IAdminAO=null;
	}

	@Override
	public void enableMailBox(String mailboxid) {
		this.IAdminAO.enableMailbox(Integer.parseInt(mailboxid));
	}

	@Override
	public void disableMailBox(String mailboxid) {
		this.IAdminAO.disableMailbox(Integer.parseInt(mailboxid));
	}
}
