import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.lightcouch.CouchDbClient;

import com.google.gson.JsonObject;


public class page3print extends HttpServlet {
	CouchDbClient dbClient3 = new CouchDbClient("groupowner-member.properties");
	CouchDbClient dbClient = new CouchDbClient("couchdb.properties");
	/**
	 * Constructor of the object.
	 */
	public page3print() {
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

	PrintWriter out = response.getWriter();
		
	String groupname=(String) request.getParameter("groupname");
	String ownername=(String) request.getParameter("ownername");
	String username=(String) request.getParameter("username");
	JsonObject jsonobject = new JsonObject();
		List<JsonObject> allDocs3 = dbClient3.view("_all_docs").includeDocs(true).query(JsonObject.class);
		ArrayList<JSONObject> list = new ArrayList<JSONObject> ();
		JSONObject obj = new JSONObject();
		for(int i=0;i<allDocs3.size();i++){
			if(allDocs3.get(i).get("_id").toString().replace("\"","").equals(groupname+"-"+ownername)){
				String oldmembers=allDocs3.get(i).get("members").toString().replace("\"","");
				String oldowner=allDocs3.get(i).get("ownername").toString().replace("\"","");
				obj.put("groupname", groupname);
				obj.put("ownername", oldowner);
				obj.put("username", username);
				obj.put("members", oldmembers);
				list.add(obj);
			} else {
				
			}
		}
		
		response.setContentType("application/json");
		
		out.print(list);
		out.close();}
		
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
