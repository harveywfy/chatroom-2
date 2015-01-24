import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lightcouch.CouchDbClient;

import com.google.gson.JsonObject;
import java.util.Timer;
import java.util.TimerTask;

public class reminder extends HttpServlet {
private Timer timer=new Timer();
	/**
	 * Constructor of the object.
	 */
	public reminder() {
		super();
	}
	CouchDbClient dbClient = new CouchDbClient("couchdb.properties");
	CouchDbClient dbClient2 = new CouchDbClient("couchdb2.properties");
	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String reminderstate="success";
		 PrintWriter out = response.getWriter();
		final String oldemail=(String) request.getParameter("oldemail");
		final String username = "hazheng@student.unimelb.edu.au";
		final String password = "wy111111";
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "false");
		props.put("mail.smtp.host", "smtp.unimelb.edu.au");
		props.put("mail.smtp.port", "25");
      props.setProperty(username, "hazheng@student.unimelb.edu.au");
      props.setProperty(password,"wy111111");
      props.put("mail.smtp.SSL.trust", "smtp.unimelb.edu.au");

		Session session2 = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
		
		
		List<JsonObject> allDocs = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
	    JsonObject json= new JsonObject();
		JsonObject jsonobject = new JsonObject();
		List<JsonObject> allDocs2 = dbClient2.view("_all_docs").includeDocs(true).query(JsonObject.class);
		JsonObject json2 = new JsonObject();
			
		
		
     try{Message message = new MimeMessage(session2);

		message.setFrom(new InternetAddress("hazheng@student.unimelb.edu.au"));//from email
	
		message.setRecipients(Message.RecipientType.TO,
			InternetAddress.parse(oldemail));//to  email
		message.setSubject("reminder");
		
		
		
		long date = System.currentTimeMillis();
        try{
        	json2 = dbClient2.find(JsonObject.class, oldemail);
        	long oritime = Long.parseLong(json2.get("time").toString());
        	long time = date-oritime;
        	if(time<60000*5){
        		reminderstate="time limit,try again after 5 minutes waiting";
        	}
        	else{
        		dbClient2.remove(json2);
        		json2.addProperty("_id", oldemail);
                json2.addProperty("time2", date);
                dbClient2.save(json2);
        	}
        }catch(Exception e){
        	json2.addProperty("_id", oldemail);
            json2.addProperty("time2", date);
            dbClient2.save(json2);
        }

		
		
		
		
        if(oldemail.equals("")){reminderstate="empty input";}
    	 if((!(oldemail.equals("")))&&(reminderstate.equals("success"))){
    	 for(int i=0;i<allDocs.size();i++){
			if(allDocs.get(i).get("_id").toString().replace("\"","").equals(oldemail)){
				String oldpassword=allDocs.get(i).get("password").toString().replace("\"","");
					if(!(oldpassword.equals(""))){
						reminderstate="sucess reminder";
					message.setText("The old password for your proj2 account is:"+" email: "+oldemail+" password: "+oldpassword+"\n"+"Keep it secret, keep it safe.");
					Transport.send(message);}
					else{reminderstate="empty password,not confirm yet";}
					
			}
			else{reminderstate="email not register yet";}
			}
    	 }
			
			
	       
	        response.setContentType("application/json");
	        jsonobject.addProperty("reminderstate",reminderstate);
			out.print(jsonobject);
     }
			catch (MessagingException e) {
				reminderstate="email system error";
				 System.out.println(e.getMessage());
				 System.out.println("email system error,please try again");

				//throw new RuntimeException(e);
			}
	
		
		out.flush();
		out.close();
	}
}