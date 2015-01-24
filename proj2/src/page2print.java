import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lightcouch.CouchDbClient;

import com.google.gson.JsonObject;


public class page2print extends HttpServlet {
	CouchDbClient dbClient3 = new CouchDbClient("groupowner-member.properties");
	CouchDbClient dbClient = new CouchDbClient("couchdb.properties");
	/**
	 * Constructor of the object.
	 */
	public page2print() {
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
		
		try{
			
			List<JsonObject> allDocs = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			JsonObject jsonobject = new JsonObject();
			
			String username2=(String) request.getParameter("username");
			String state2="success";
			
			
			
			if(username2.equals("")){
				state2="you need to login";
				jsonobject.addProperty("state2", state2);
	        response.setContentType("application/json");
		
			out.print(jsonobject);
			out.close();
			out.flush();
			}System.out.println(state2);
			if(state2=="success"){
			for(int i=0;i<allDocs.size();i++){
			if(allDocs.get(i).get("_id").toString().replace("\"","").equals(username2)){
			
				String username=allDocs.get(i).get("_id").toString().replace("\"","");
				String groupnameusername=allDocs.get(i).get("groupnameusername").toString().replace("\"","");
				
				jsonobject.addProperty("_id", username);
				jsonobject.addProperty("groupnameusername", groupnameusername);
				jsonobject.addProperty("state2", state2);
	        response.setContentType("application/json");
		
			out.print(jsonobject);
			out.close();
			out.flush();
		}}}
			}
			catch(Exception e){System.out.println(e);System.out.println("you have not login,please try again");}

	}}