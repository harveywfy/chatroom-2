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


public class group extends HttpServlet {
	CouchDbClient dbClient3 = new CouchDbClient("groupowner-member.properties");
	CouchDbClient dbClient = new CouchDbClient("couchdb.properties");
	/**
	 * Constructor of the object.
	 */
	public group() {
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
			String groupname=(String) request.getParameter("groupname");
			String ownername = (String)session.getAttribute("seusername");
			String password = (String)session.getAttribute("sepassword");
			String accesstoken = (String)session.getAttribute("accesstoken");
			String groupstate="success create group with owner";
			List<JsonObject> allDocs = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
			List<JsonObject> allDocs3 = dbClient3.view("_all_docs").includeDocs(true).query(JsonObject.class);
		    JsonObject json= new JsonObject();
		    JsonObject json3= new JsonObject();
			JsonObject jsonobject = new JsonObject();
			
			for(int i=0;i<allDocs3.size();i++){
				if(allDocs3.get(i).get("_id").toString().replace("\"","").equals(groupname+"-"+ownername)){
					
					groupstate="group and onwer already exists"; 
					
				} else {
					
				}
			}
			System.out.println(groupstate);
			for(int i=0;i<allDocs.size();i++){
				if ((groupstate.equals("success create group with owner"))&&
				((allDocs.get(i).get("_id").toString().replace("\"","").equals(ownername))))
				{	
					String oldgroupnameusername="";
					if(!(allDocs.get(i).get("groupnameusername").toString().replace("\"","").equals(""))){
						oldgroupnameusername=allDocs.get(i).get("groupnameusername").toString().replace("\"","");}
					System.out.println(oldgroupnameusername);
				
					
					
					
					dbClient.remove(allDocs.get(i));
				json.addProperty("_id",ownername);
	            json.addProperty("password",password);
	            json.addProperty("token","");
				json.addProperty("accesstoken",accesstoken);
				json.addProperty("groupnameusername",oldgroupnameusername+"/"+groupname+"-"+ownername);
				dbClient.save(json);
				}
			}
			if (groupstate.equals("success create group with owner")){
			
			json3.addProperty("_id",groupname+"-"+ownername);
			json3.addProperty("ownername",ownername);
			json3.addProperty("members","");
			dbClient3.save(json3);
			
			
			jsonobject.addProperty("groupstate", groupstate);
			response.setContentType("application/json");
			
			out.print(jsonobject);
			out.close();

		}else{
			jsonobject.addProperty("groupstate", groupstate);
		response.setContentType("application/json");
		
		out.print(jsonobject);
		out.close();}
			}
			catch (Exception e) {
			System.out.println(e);
			System.out.println("error because you have not login");
		}
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
