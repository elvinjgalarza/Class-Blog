package guestbook;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.googlecode.objectify.ObjectifyService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

@SuppressWarnings("serial")

public class GAEJCronServlet extends HttpServlet {
	
	private static final Logger _logger = Logger.getLogger(GAEJCronServlet.class.getName());
	// list of recipient emails
	private static List<String> recipient = new ArrayList<String>();
	
	public static void add(String x) {
		recipient.add(x);
	}
	
	public static boolean find(String x) {
		return ((recipient.indexOf(x) == -1) ? false:true);
	}
	
	public static void remove(String x) {
		recipient.remove(x);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			
			String strCallResult = "";
			// let receiver treat as plain text
			resp.setContentType("text/plain");
			try {
				_logger.info("Cron Job has been executed");
				
				
				//Register Blog Post Entities
				ObjectifyService.register(Greeting.class);
				
				//obtain the blogpost lists from the datastore
				List<Greeting> blogList = ObjectifyService.ofy().load().type(Greeting.class).list();
				//sort the posts by most recent
				Collections.sort(blogList, Collections.reverseOrder());
				
				//determine the date for previous 24 hours from now
				Date date = new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DATE, -1);
				date = calendar.getTime();
			
				//if post_24_hrs flag is 1 then that means a post was made in the recent 24 hrs, 0 otherwise
				int post_24_hrs = 1;
				if(blogList.size() > 0) {
					if(blogList.get(0).getDate().before(date)) {
						post_24_hrs = 0;
					}
				}
				
				// start doing GAEJ email shtuff
				Properties props = new Properties();
				Session session = Session.getDefaultInstance(props, null);
				Message msg = new MimeMessage(session);
				
				//Replace sender with this
				
				//	msg.setFrom(new InternetAddress("theboringblog.appspotmail.com", "The Boring Blog Update"));
				
				//Or this
				msg.setFrom(new InternetAddress("sduong1125@gmail.com")); // lol sorry mate
				  
				if(post_24_hrs == 1) {
					  // look through recipient list
					  for(String x: recipient) {
						  msg.addRecipient(Message.RecipientType.TO, new InternetAddress(x));
						  String email_Subject = "The Boring Blog 24HR Alert";
						  String email_Content = "";
						//look through list of post made recently to construct content of email to be sent
						  for(int j = 0; j < blogList.size(); j++) {
							  email_Content = email_Content + blogList.get(j).getDate() + " " + blogList.get(j).getUser() + " " + blogList.get(j).getTitle() + " " + blogList.get(j).getContent();
						  }
						  
						  // now continue to set up GAEJ email shtuff where we left off
						  msg.setSubject(email_Subject);
						  msg.setText(email_Content);
						  //now send message
						  Transport.send(msg);
						  strCallResult = "Success, an email was sent to you!";
						  resp.getWriter().println(strCallResult);
						  }
					  }
				  }
				catch (Exception ex) { //sad
				//Log any exceptions in your Cron Job
					strCallResult = "Uh oh: " + ex.getMessage();
					resp.getWriter().println(strCallResult);
				}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}