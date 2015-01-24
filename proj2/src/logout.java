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

public class logout extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public logout() {
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
		List<JsonObject> allDocs = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(allDocs);
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
		List<JsonObject> allDocs = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		JsonObject jsonobject = new JsonObject();
		HttpSession session=request.getSession(false);
		String state2="";
		
		
		JsonObject json = new JsonObject();
		if(session.getAttribute("seusername").equals("")){
			state2="token is invalid";jsonobject.addProperty("state2", state2);dbClient.save(json); 
        response.setContentType("application/json");
	
		out.print(jsonobject);
		out.close();
		out.flush();
		}
		
		for(int i=0;i<allDocs.size();i++){
		if(allDocs.get(i).get("_id").toString().replace("\"","").equals((session.getAttribute("seusername")))){
		if(allDocs.get(i).get("accesstoken").toString().replace("\"","").equals((session.getAttribute("accesstoken")))){
		session.removeAttribute("sestate");
		session.removeAttribute("seusername");
		session.removeAttribute("sepassword");
		
		String username=allDocs.get(i).get("_id").toString().replace("\"","");
		String password=allDocs.get(i).get("password").toString().replace("\"","");
		
		dbClient.remove(allDocs.get(i));
	
		json.addProperty("_id",username);
        json.addProperty("password",password);
        json.addProperty("token","");
        json.addProperty("accesstoken","");
        json.addProperty("groupnameusername","");
		state2="sucess logout";}}
		else {state2="token is invalid";}
		}
		jsonobject.addProperty("state2", state2);
		if (json.equals("")){}
		else{
         dbClient.save(json); }
        response.setContentType("application/json");
	
		out.print(jsonobject);
		out.close();
		out.flush();
	}
		catch(Exception e){System.out.println(e);}

}}
