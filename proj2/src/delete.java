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


public class delete extends HttpServlet {
	CouchDbClient dbClient = new CouchDbClient("couchdb.properties");
	CouchDbClient dbClient3 = new CouchDbClient("groupowner-member.properties");
	/**
	 * Constructor of the object.
	 */
	public delete() {
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
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session=request.getSession(false);
		String password = (String)session.getAttribute("sepassword");
		String username = (String)session.getAttribute("seusername");
		String state=(String)session.getAttribute("sestate");
		
		List<JsonObject> allDocs = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
	    JsonObject json= new JsonObject();
		JsonObject jsonobject = new JsonObject();
		for(int i=0;i<allDocs.size();i++){
			if(allDocs.get(i).get("_id").toString().replace("\"","").equals((session.getAttribute("seusername")))){
			if(allDocs.get(i).get("accesstoken").toString().replace("\"","").equals((session.getAttribute("accesstoken")))){
				dbClient.remove(allDocs.get(i));
				state="delete";
			}
		}
			}
		jsonobject.addProperty("state",state);
		
        dbClient.save(json); 
        response.setContentType("application/json");
	
		out.print(jsonobject);
		out.close();
	}
catch(Exception e){System.out.println(e);}

}
}