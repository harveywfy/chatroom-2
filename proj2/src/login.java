import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

import org.json.simple.JSONObject;
import org.lightcouch.CouchDbClient;

import com.google.gson.JsonObject;


public class login extends HttpServlet {

	public login() {
		super();
	}
	CouchDbClient dbClient = new CouchDbClient("couchdb.properties");
	
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
		HttpSession session=request.getSession(true);
		JsonObject jsonobject = new JsonObject();
		String username=null;
		username = (String) request.getParameter("username");
		String password=null;
		String state="email not registered";
		password = (String) request.getParameter("password");
		 PrintWriter out = response.getWriter();
		 String accesstoken=Register.randomString(6);;
		System.out.println(password);
		System.out.println(username);
			
			List<JsonObject> allDocs = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
			JsonObject json = new JsonObject();
			//response.sendRedirect("http://10.9.218.37:8080/proj1/login.html");
			for(int i=0;i<allDocs.size();i++){
				
			if(allDocs.get(i).get("_id").toString().replace("\"","").equals(username)){
			if (allDocs.get(i).get("password").toString().replace("\"","").equals(password)){
				state="login";session.setAttribute("accesstoken", accesstoken); 
				String groupnameusername=allDocs.get(i).get("groupnameusername").toString().replace("\"","");
				dbClient.remove(allDocs.get(i));
				json.addProperty("_id",username);
	            json.addProperty("password",password);
	            json.addProperty("token","");
				json.addProperty("accesstoken",accesstoken);
				json.addProperty("groupnameusername",groupnameusername);}
			else {state="wrong password";}
			
			}}
			
			session.setAttribute("sestate", state);
			session.setAttribute("seusername", username);
			session.setAttribute("sepassword", password);
			session.setAttribute("accesstoken", accesstoken);
			System.out.print(session.getAttribute("sestate"));
			
            jsonobject.addProperty("state", state);
			
	        dbClient.save(json); 
	        response.setContentType("application/json");
		
			out.print(jsonobject);
			out.close();
		out.flush();
		out.close();
	}

	

}
