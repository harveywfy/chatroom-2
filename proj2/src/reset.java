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


public class reset extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public reset() {
		super();
	}
	CouchDbClient dbClient = new CouchDbClient("couchdb.properties");
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
		try{
			HttpSession session=request.getSession(false);
		
		String email = (String)session.getAttribute("seusername");
		String state=(String)session.getAttribute("sestate");
		String newpassword=Register.randomString(6);
		 PrintWriter out = response.getWriter();
		
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
		for(int i=0;i<allDocs.size();i++){
			if(allDocs.get(i).get("_id").toString().replace("\"","").equals((session.getAttribute("seusername")))){
			if(allDocs.get(i).get("accesstoken").toString().replace("\"","").equals((session.getAttribute("accesstoken")))){
				dbClient.remove(allDocs.get(i));
				
				json.addProperty("_id",email);
		        json.addProperty("password",newpassword);
		        json.addProperty("token","");
		        json.addProperty("accesstoken","");
		        json.addProperty("groupnameusername","");
				state="sucess reset";
			}
		}
			}
		jsonobject.addProperty("state",state);
		
     try{

			Message message = new MimeMessage(session2);
			message.setFrom(new InternetAddress("hazheng@student.unimelb.edu.au"));//from email
		
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(email));//to  email
			message.setSubject("Reset");
			message.setText("The new password for your proj2 account is:"+"email: "+email+"password: "+newpassword+"\n"+"Keep it secret, keep it safe.");	 
			if(state.equals("sucess reset"))
			{Transport.send(message);}
			else{state="no send ,error";}
			
	       
	        dbClient.save(json); 
	        response.setContentType("application/json");
		
			out.print(jsonobject);
     }
			catch (MessagingException e) {
				 System.out.println(e.getMessage());
				 System.out.println("email system error,please try again");

				//throw new RuntimeException(e);
			}
	
		
		out.flush();
		out.close();
	}
		catch(Exception e){System.out.println(e);}
}}
