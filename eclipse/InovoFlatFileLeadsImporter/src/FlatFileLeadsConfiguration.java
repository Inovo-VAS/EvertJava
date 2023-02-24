import inovo.servlet.IInovoServletListener;
import inovo.servlet.InovoServletContextListener;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/FlatFileLeadsConfiguration"})
public class FlatFileLeadsConfiguration extends HttpServlet
{
  private static final long serialVersionUID = 1L;

	  protected void service(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	    {
	    InovoServletContextListener.inovoServletListener().doRequest("/FlatFileLeadsConfiguration",request, response);
	  }
	
	  protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	  {
	    InovoServletContextListener.inovoServletListener().doRequest("/FlatFileLeadsConfiguration",request, response);
	  }
	
	  protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	  {
	    InovoServletContextListener.inovoServletListener().doRequest("/FlatFileLeadsConfiguration",request, response);
	  }
}
