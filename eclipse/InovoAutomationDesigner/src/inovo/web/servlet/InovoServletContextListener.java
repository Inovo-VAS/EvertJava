package inovo.web.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.web.WebLoggerContextUtils;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InovoServletContextListener  implements ServletContextListener{

	private static Logger _logger=null;
	
	private static InovoServletContextListener _inovoServletContextListener=null;
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		_inovoServletContextListener=this;
	}
	
	public static synchronized InovoServletContextListener inovoServletContextListener(){
		return _inovoServletContextListener;
	}

	public void executeServletRequest(HttpServletRequest request,
			HttpServletResponse response) {
		ServletRequestHandler servletRequestHandler=new ServletRequestHandler(){
			 @Override
			public void log(String message, Request request, Exception e) {
				debug(message,request,e);
			}

		};
		
		try {
			servletRequestHandler.doRequest(request, response);
		} catch (Exception e1) {
		}
		
		servletRequestHandler.cleanupRequest();
	}
	
	public void debug(String message,Object sender,Exception e){
		if(_logger!=null){
			if(e==null){
				_logger.debug(message,e);
			}
			else{
				_logger.debug(message);
			}
		}
		else{
			if(e==null){
				System.out.println(sender.toString()+" - "+message);
			}
			else{
				System.out.println("exception:"+sender.toString()+" - "+message);
				System.out.println("exception:"+e.getMessage());
			}
		}
	}
}
