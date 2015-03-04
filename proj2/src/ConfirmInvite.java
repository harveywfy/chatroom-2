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


public class ConfirmInvite extends HttpServlet {
	CouchDbClient dbClient3 = new CouchDbClient("groupowner-member.properties");
	CouchDbClient dbClient = new CouchDbClient("couchdb.properties");
	/**
	 * Constructor of the object.
	 */
	public ConfirmInvite() {
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

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session=request.getSession(false);
		String id=(String) request.getParameter("id");
		String inviteusername=(String) request.getParameter("inviteusername");
		String token=(String) request.getParameter("token");
		
		String seusername = (String)session.getAttribute("seusername");
		String sepassword = (String)session.getAttribute("sepassword");
		String seaccesstoken = (String)session.getAttribute("accesstoken");
		String ConfirmInvitestate="";
		List<JsonObject> allDocs = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
		List<JsonObject> allDocs3 = dbClient3.view("_all_docs").includeDocs(true).query(JsonObject.class);
	    JsonObject json= new JsonObject();
	    JsonObject json3= new JsonObject();
		JsonObject jsonobject = new JsonObject();
		for(int i=0;i<allDocs.size();i++){
			if((allDocs.get(i).get("_id").toString().replace("\"","").equals(inviteusername))&&
			(allDocs.get(i).get("token").toString().replace("\"","").equals(token))){
				String oldgroupnameusername="";
				if(!(allDocs.get(i).get("groupnameusername").toString().replace("\"","").equals(""))){
					oldgroupnameusername=allDocs.get(i).get("groupnameusername").toString().replace("\"","");}
				dbClient.remove(allDocs.get(i));
				json.addProperty("_id",inviteusername);
	            json.addProperty("password",sepassword);
	            json.addProperty("token","");
				json.addProperty("accesstoken",seaccesstoken);
				json.addProperty("groupnameusername",oldgroupnameusername+"/"+id);
				dbClient.save(json);
				ConfirmInvitestate="success 1";
			}
		}
		if(ConfirmInvitestate=="success 1"){
			for(int i=0;i<allDocs3.size();i++){
				if(allDocs3.get(i).get("_id").toString().replace("\"","").equals(id))
						{String oldmember="";
						if(!(allDocs3.get(i).get("members").toString().replace("\"","").equals(""))){
					oldmember=allDocs3.get(i).get("members").toString();}
					String[] msg=null;
					msg=allDocs3.get(i).get("_id").toString().split("-");
					String ownername=msg[1];
					dbClient3.remove(allDocs3.get(i));
					json3.addProperty("_id",id);
					json3.addProperty("ownername",ownername);
					json3.addProperty("members",oldmember+"-"+inviteusername);
					dbClient3.save(json3);
					ConfirmInvitestate="sucess confirm,you have been invited";
				}
			}
		}
		
		
		jsonobject.addProperty("ConfirmInvitestate", ConfirmInvitestate);
		response.setContentType("application/json");
		out.print(jsonobject);
		out.close();
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
