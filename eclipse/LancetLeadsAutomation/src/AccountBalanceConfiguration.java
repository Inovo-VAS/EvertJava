

import inovo.servlet.InovoCoreEnvironmentManager;
import inovo.servlet.InovoServletContextListener;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AccountBalanceConfiguration
 */
@WebServlet("/AccountBalanceConfiguration")
public class AccountBalanceConfiguration extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccountBalanceConfiguration() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    	InovoServletContextListener.inovoServletListener().doRequest("/AccountBalanceConfiguration", request, response);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InovoServletContextListener.inovoServletListener().doRequest("/AccountBalanceConfiguration", request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InovoServletContextListener.inovoServletListener().doRequest("/AccountBalanceConfiguration", request, response);
	}

}
