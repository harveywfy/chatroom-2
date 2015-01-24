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


public class invitesendemail extends HttpServlet {
	CouchDbClient dbClient3 = new CouchDbClient("groupowner-member.properties");
	CouchDbClient dbClient = new CouchDbClient("couchdb.properties");
	/**
	 * Constructor of the object.
	 */
	public invitesendemail() {
		super();
	}

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
		HttpSession session=request.getSession(false);
	String invitestate="error, try again";
	String b=Register.randomString(6);
		String inviteusername = (String) request.getParameter("inviteusername");
		String invitegroupname= (String) request.getParameter("invitegroupname");
		String usernameinvitemember = (String)session.getAttribute("seusername");
		
		String seusername = (String)session.getAttribute("seusername");
		String sepassword = (String)session.getAttribute("sepassword");
		String seaccesstoken = (String)session.getAttribute("accesstoken");
		
		
		 PrintWriter out = response.getWriter();
		 List<JsonObject> allDocs3 = dbClient3.view("_all_docs").includeDocs(true).query(JsonObject.class);
		 List<JsonObject> allDocs = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
		 JsonObject json3 = new JsonObject();
		 JsonObject json = new JsonObject();
			JsonObject jsonobject = new JsonObject();
			System.out.println(inviteusername);
			for(int i=0;i<allDocs.size();i++){
				if
				(allDocs.get(i).get("_id").toString().replace("\"","").equals(inviteusername)){
					invitestate="register before,but group error or not owner,invite failure";
				}
				else{}
			}
			
			for(int i=0;i<allDocs3.size();i++){
				if((invitestate=="register before,but group error or not owner,invite failure")&&
((allDocs3.get(i).get("_id").toString().replace("\"","").equals(invitegroupname+"-"+usernameinvitemember))&&(!usernameinvitemember.equals(inviteusername))&&
						!(allDocs3.get(i).get("members").toString().replace("\"","").equals(inviteusername)))){
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

				Session session1 = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				  });
				 
		   
				try {
					
					Message message = new MimeMessage(session1);
					message.setFrom(new InternetAddress("hazheng@student.unimelb.edu.au"));//from email
				
					message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(inviteusername));//to  email
					message.setSubject("Invitation");
						 
					message.setText("Somebody has invited your to join group. "+"the groupname is: " +invitegroupname+" \n and the owner is " +
							usernameinvitemember+" Click the following link to confirm Invitation:\n"+"http://swen90002-11.cis.unimelb.edu.au:8080/proj2/ConfirmInvite.html?"+invitegroupname+"-"+usernameinvitemember+"&"+inviteusername+"&"+b);
			Transport.send(message);
			invitestate="success invite";
			}
		 catch (MessagingException e) {
			//throw new RuntimeException(e);
			 invitestate="email system error";
			 System.out.println(e.getMessage());
			 System.out.println("email system error,please try again"); 
		}
	}else{}
				
			}
	if(invitestate=="success invite"){
		for(int i=0;i<allDocs.size();i++){
			if(allDocs.get(i).get("_id").toString().replace("\"","").equals(inviteusername)){
				
				String oldgroupnameusername="";
				if(!(allDocs.get(i).get("groupnameusername").toString().replace("\"","").equals(""))){
					oldgroupnameusername=allDocs.get(i).get("groupnameusername").toString().replace("\"","");}
				dbClient.remove(allDocs.get(i));
				json.addProperty("_id",inviteusername);
	            json.addProperty("password",sepassword);
	            json.addProperty("token",b);
				json.addProperty("accesstoken",seaccesstoken);
				json.addProperty("groupnameusername",oldgroupnameusername);
				dbClient.save(json);
			}
		}
	}
			jsonobject.addProperty("invitestate", invitestate);
		
			
	         
	        response.setContentType("application/json");
		
			out.print(jsonobject);
			out.close();
			out.flush();     	
	

}
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
