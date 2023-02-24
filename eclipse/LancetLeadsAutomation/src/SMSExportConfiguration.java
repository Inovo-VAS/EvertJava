

import inovo.servlet.InovoServletContextListener;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SMSExportConfiguration
 */
@WebServlet("/SMSExportConfiguration")
public class SMSExportConfiguration extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SMSExportConfiguration() {
        super();
    }
    
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void servlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InovoServletContextListener.inovoServletListener().doRequest("/SMSExportConfiguration", request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InovoServletContextListener.inovoServletListener().doRequest("/SMSExportConfiguration", request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InovoServletContextListener.inovoServletListener().doRequest("/SMSExportConfiguration", request, response);
	}
}
