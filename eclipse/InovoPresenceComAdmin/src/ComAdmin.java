

import inovo.servlet.InovoServletContextListener;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ComAdmin
 */
@WebServlet("/ComAdmin")
public class ComAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ComAdmin() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
	 * @see HttpServlet#servlet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void servlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InovoServletContextListener.inovoServletListener().doRequest("/inovo/presence/ComAdmin", request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InovoServletContextListener.inovoServletListener().doRequest("/inovo/presence/ComAdmin", request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InovoServletContextListener.inovoServletListener().doRequest("/inovo/presence/ComAdmin", request, response);
	}

}
