

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class WallboardServiceAvailableAgents
 */
@WebServlet("/WallboardServiceAvailableAgents")
public class WallboardServiceAvailableAgents extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WallboardServiceAvailableAgents() {
        super();
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		inovo.servlet.InovoServletContextListener.inovoServletListener().doRequest("/WallboardServiceAvailableAgents", request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		inovo.servlet.InovoServletContextListener.inovoServletListener().doRequest("/WallboardServiceAvailableAgents", request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		inovo.servlet.InovoServletContextListener.inovoServletListener().doRequest("/WallboardServiceAvailableAgents", request, response);
	}

}
