package guestbook;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;

@SuppressWarnings("serial")

public class GAEJCronServlet extends HttpServlet {
	
	private static final Logger _logger = Logger.getLogger(GAEJCronServlet.class.getName());
	private List<String> recipient = new ArrayList<String>();
	
	public void add(String x) {
		recipient.add(x);
	}
	
	public void remove(String x) {
		recipient.remove(x);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
			try {
				_logger.info("Cron Job has been executed");
				
				Properties props = new Properties();
				Session session = Session.getDefaultInstance(props, null);

				try {
				  Message msg = new MimeMessage(session);
				  //Email from
				  msg.setFrom(new InternetAddress("admin@example.com", "Example.com Admin"));
				  //Email to who
				  //msg.addRecipient(Message.RecipientType.TO, new InternetAddress("user@example.com", "Mr. User"));
				  for(String x: recipient) {
					  msg.addRecipient(Message.RecipientType.TO, new InternetAddress(x));
				  }
				  //Email subject
				  msg.setSubject("Your Example.com account has been activated");
				  //Email set the body
				  //msg.setText("This is a test");
				  //
				  Transport.send(msg);
				} catch (AddressException e) {
				  // ...
				} catch (MessagingException e) {
				  // ...
				} catch (UnsupportedEncodingException e) {
				  // ...
				}
				
			}
			catch (Exception ex) {
				//Log any exceptions in your Cron Job
			}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
