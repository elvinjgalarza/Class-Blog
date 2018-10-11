package guestbook;

import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.users.User;

import com.google.appengine.api.users.UserService;

import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;


public class GuestbookServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		
		throws IOException{
	  
	  UserService userService = UserServiceFactory.getUserService();
	  
	  User user = userService.getCurrentUser();
	  
	  
	  
	  if(user != null) {
		  
		  resp.setContentType("text/plain");
		  
		  resp.getWriter().println("Hello, " + user.getNickname());
	  }else {
		  
		  resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		  
	  }
	  
	}
	
	// Before Objectify is used to load or save data, entity classes
	// must be registered. Objectify will introspect these classes
	// and their annotations to build a metamodel, which is used to
	// efficiently manipulate entities at runtime.
	
	// Registration must be done at application startup, before Objectify is used,
	// so that's why it's in this Servlet. Must be single-threaded.
	static {

        ObjectifyService.register(Greeting.class);

    }
	
}