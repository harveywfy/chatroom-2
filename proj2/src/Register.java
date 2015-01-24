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


public class Register extends HttpServlet {

	public Register() {
		super();
	}
	CouchDbClient dbClient = new CouchDbClient("couchdb.properties");
	CouchDbClient dbClient3 = new CouchDbClient("groupowner-member.properties");
protected void save(){
        
    }
    protected void find() throws IOException{

    }
   private static Random randGen = null;
	private static char[] numbersAndLetters = null;

	public static final String randomString(int length) {
	         if (length < 1) {
	             return null;
	         }
	         if (randGen == null) {
	                randGen = new Random();
	                numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
	                   "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
	                  //numbersAndLetters = ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
	                 }
	         char [] randBuffer = new char[length];
	         for (int i=0; i<randBuffer.length; i++) {
	             randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
	          //randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
	         }
	         return new String(randBuffer);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{

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

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JsonObject json = new JsonObject();
		JsonObject jsonobject = new JsonObject();
		String email;String a;  String state="success";
		 email = (String) request.getParameter("email");
		 a=Register.randomString(6);
		 PrintWriter out = response.getWriter();
		
		 List<JsonObject> allDocs = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
			
			
			
			for(int i=0;i<allDocs.size();i++){
			
			if(allDocs.get(i).get("_id").toString().replace("\"","").equals(email)){
				state="have already registered.forget password?click the link below!";
			}
			else{

			}}
			if (state.equals("success")){
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
				  });
				 
		   
				try {
					//System.out.println(state);
					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress("hazheng@student.unimelb.edu.au"));//from email
				
					message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(email));//to  email
					message.setSubject("Register");
					//message.setText("Somebody, probably you, has registered your email address. Click the following link to confirm registration:\n");	 
					message.setText("Somebody, probably you, has registered your email address. Click the following link to confirm registration:\n"+"http://swen90002-11.cis.unimelb.edu.au:8080/proj2/Confirm.html?"+a+"&"+email);
					//System.out.println("mabia00");
					Transport.send(message);
			
			
			json.addProperty("_id",email);
			json.addProperty("groupuser","");
			json.addProperty("password","");
			json.addProperty("token",a);
			json.addProperty("accesstoken","");
			json.addProperty("groupnameusername","");
			
			 dbClient.save(json);
			 jsonobject.addProperty("rstate",state);
			 Time t=new Time(email);
			 t.reminde();
			 
	         
		        response.setContentType("application/json");
			   
				out.print(jsonobject);
				//out.close();
			}
			
			
		
		
		 catch (MessagingException e) {
			//throw new RuntimeException(e);
			 state="email system error";
			 System.out.println(e.getMessage());
			 System.out.println("email system error,please try again");
           
		}
	
		out.flush();
		out.close();
	}else{jsonobject.addProperty("rstate",state);
	 
	//System.out.println("mabiazuihou");
    response.setContentType("application/json");
   
	out.print(jsonobject);
	out.close();}

	

}}