import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

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

import org.json.simple.JSONObject;
import org.lightcouch.CouchDbClient;

import com.google.gson.JsonObject;


public class Confirm extends HttpServlet {

	public Confirm() {
		super();
	}
	CouchDbClient dbClient = new CouchDbClient("couchdb.properties");
	CouchDbClient dbClient3 = new CouchDbClient("groupowner-member.properties");
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
protected void save(){
        
    }
    protected void find() throws IOException{

    }
   
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
	List<JsonObject> allDocs = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
	
	response.setContentType("application/json");
	PrintWriter out = response.getWriter();
	out.print(allDocs);
	out.close();
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String state="email is not correct,maybe not registered, confirm fail";
		String emailConfirm = null;String tokenConfirm = null;
		
			
			List<JsonObject> allDocs = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
			JsonObject json = new JsonObject();
			JsonObject jsonobject = new JsonObject();
			//if (emailConfirm.equals("")){state="email not regist";}else{
			emailConfirm = (String) request.getParameter("email");//}
			//if (tokenConfirm.equals("")){state="token error";}else{
			tokenConfirm = (String) request.getParameter("token");//}
			 PrintWriter out = response.getWriter();
			for(int i=0;i<allDocs.size();i++){
				if((tokenConfirm.equals(""))){state="token is empty";
				}
				
				else if(((allDocs.get(i).get("_id").toString().replace("\"","")).equals(emailConfirm))
					&&(allDocs.get(i).get("token").toString().replace("\"","").equals(tokenConfirm))){

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

				Session session = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				  }); String password1=Register.randomString(6);

				try {

					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress("hazheng@student.unimelb.edu.au"));//from email
				
					message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(emailConfirm));//to  email
					message.setSubject("Confirm");
					message.setText("The Email is: "+ emailConfirm+"\n"+" The password for your proj2 account is:"+password1+"\n"+" Keep it secret, keep it safe.");	 
					
					Transport.send(message);
				dbClient.remove(allDocs.get(i));
				json.addProperty("_id",emailConfirm);
	            json.addProperty("password",password1);
	            json.addProperty("token","");
	            json.addProperty("accesstoken","");
	            json.addProperty("groupnameusername","");
	            state="sucess confirm";jsonobject.addProperty("cstate",state);
				}catch (MessagingException e) {
					state="email system error";
					 System.out.println(e.getMessage());
 System.out.println("email system error,please try again");
					//throw new RuntimeException(e);
				}
				
				
		
		
		}

		}
			
			dbClient.save(json); 
	        response.setContentType("application/json");
	        jsonobject.addProperty("cstate",state);
			out.print(jsonobject);
			out.close();		
}}
