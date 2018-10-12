//https://guestbook-project-217223.appspot.com/ofyguestbook.jsp

package guestbook;

import com.googlecode.objectify.ObjectifyService;
import static com.googlecode.objectify.ObjectifyService.ofy;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class OfySignGuestbookServlet extends HttpServlet {
	// Registration must be done at application startup, before Objectify is used,
	// so that's why it's in this Servlet. Must be single-threaded.
	
	List<Greeting> greetingList = new ArrayList<Greeting>();
	
	static {
        ObjectifyService.register(Greeting.class);
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp)

                throws IOException {
    	UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String guestbookName = req.getParameter("guestbookName");
        
    	if(req.getParameter("clear") != null) {
	        ofy().delete().entities(greetingList).now();
    	}else if(req.getParameter("sub") != null) {
    		if(!GAEJCronServlet.find(user.getEmail())) {
    			GAEJCronServlet.add(user.getEmail());
    		}else {
    			GAEJCronServlet.remove(user.getEmail());
    		}
    	}
    	else {
    		
	        // We have one entity group per Guestbook with all Greetings residing
	        // in the same entity group as the Guestbook to which they belong.
	        // This lets us run a transactional ancestor query to retrieve all
	        // Greetings for a given Guestbook.  However, the write rate to each
	        // Guestbook should be limited to ~1/second.
	
	        Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);
	        String content = req.getParameter("content");
	        String title = req.getParameter("title");
	        Greeting greeting = new Greeting(user, content, guestbookKey, title);
	        greetingList.add(greeting);
	        
	        ofy().save().entity(greeting).now();
	        
	        
    	}
    	resp.sendRedirect("/ofyguestbook.jsp?guestbookName=" + guestbookName);
    }
}