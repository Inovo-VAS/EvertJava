

import inovo.servlet.InovoServletContextListener;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FlatFileLeadsImportQueue
 */
@WebServlet({ "/FlatFileLeadsImportQueue", "/" })
public class FlatFileLeadsImportQueue extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FlatFileLeadsImportQueue() {
        super();
    }

    protected void service(HttpServletRequest request, HttpServletResponse response)
    	    throws ServletException, IOException
	{
	    InovoServletContextListener.inovoServletListener().doRequest("/FlatFileLeadsImportQueue",request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    InovoServletContextListener.inovoServletListener().doRequest("/FlatFileLeadsImportQueue",request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	{
	    InovoServletContextListener.inovoServletListener().doRequest("/FlatFileLeadsImportQueue",request, response);
	}
}
