

import inovo.servlet.InovoServletContextListener;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MailboxScheduledStates
 */
@WebServlet("/MailboxScheduledStates")
public class MailboxScheduledStates extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MailboxScheduledStates() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#servlet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void servlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InovoServletContextListener.inovoServletListener().doRequest("/MailboxScheduledStates", request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InovoServletContextListener.inovoServletListener().doRequest("/MailboxScheduledStates", request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InovoServletContextListener.inovoServletListener().doRequest("/MailboxScheduledStates", request, response);
	}

}
