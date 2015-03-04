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


public class deletemember extends HttpServlet {
	CouchDbClient dbClient3 = new CouchDbClient("groupowner-member.properties");
	CouchDbClient dbClient = new CouchDbClient("couchdb.properties");
	/**
	 * Constructor of the object.
	 */
	public deletemember() {
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
		
			 JsonObject json= new JsonObject();
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			HttpSession session=request.getSession(false);
			String deletemembername=(String) request.getParameter("deletemembername");
			String deletegroupname=(String) request.getParameter("deletegroupname");
			String deleteownername=(String) request.getParameter("deleteownername");
			String username = (String)session.getAttribute("seusername");
			String deletememberstate="error";
			
			if (username.equals("")){deletememberstate="you have not login";}
			List<JsonObject> allDocs3 = dbClient3.view("_all_docs").includeDocs(true).query(JsonObject.class);
			List<JsonObject> allDocs = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
		    JsonObject json3= new JsonObject();
			JsonObject jsonobject = new JsonObject();
			if(((deleteownername).equals(username))&&(!(deletemembername).equals(deleteownername))){deletememberstate="you are the owner";}
			if((deleteownername).equals(deletemembername)){deletememberstate="the membername is also the owner, cant delete a owner;";}
			System.out.println(deletememberstate);
			
			if(deletememberstate=="you are the owner"){
			for (int i = 0; i < allDocs3.size(); i++) {System.out.println("1");System.out.println(deletegroupname + "-" + deleteownername);
				if (allDocs3.get(i).get("_id").toString().replace("\"", "")
						.equals(deletegroupname + "-" + deleteownername))
				{System.out.println("2");
					String delete = "-" + deletemembername;
					String oldmembers = allDocs3.get(i).get("members")
							.toString().replace("\"", "");
					System.out.println("delete "+delete);
					System.out.println("oldmembers "+oldmembers);
					String[] members = oldmembers.split("-");
					for (int t = 0; t < members.length; t++) {
						if ((deletemembername).equals(members[t])) {
							deletememberstate = "success";
						
						oldmembers = oldmembers.replaceAll(delete, "");
						dbClient3.remove(allDocs3.get(i));

						json3.addProperty("_id", deletegroupname + "-"
								+ deleteownername);
						json3.addProperty("ownername", deleteownername);
						json3.addProperty("members", oldmembers);
						dbClient3.save(json3);
					}				
					
				}
				}}
			for(int i=0;i<allDocs.size();i++){
				if(allDocs.get(i).get("_id").toString().replace("\"","").equals(deletemembername)){
				String oldgroupnameusername=allDocs.get(i).get("groupnameusername").toString().replace("\"","");
				String[] groupnameusername=oldgroupnameusername.split("/");
				for(int q=0;q<groupnameusername.length;q++){
					if((deletegroupname+"-"+deleteownername).equals(groupnameusername[q])){
						
					
					String deletegroupnameusername="/"+deletegroupname+"-"+deleteownername;
					oldgroupnameusername=oldgroupnameusername.replaceAll(deletegroupnameusername,"");
					String password=allDocs.get(i).get("password").toString().replace("\"","");
					String token=allDocs.get(i).get("token").toString().replace("\"","");
					String accesstoken=allDocs.get(i).get("accesstoken").toString().replace("\"","");
					dbClient.remove(allDocs.get(i));
					json.addProperty("_id",deletemembername);
		            json.addProperty("password",password);
		            json.addProperty("token",token);
					json.addProperty("accesstoken",accesstoken);
					json.addProperty("groupnameusername",oldgroupnameusername);
					dbClient.save(json);
					deletememberstate="success";
					System.out.println("oldgroupnameusername "+oldgroupnameusername);
					System.out.println("deletegroupnameusername: "+deletegroupnameusername);
					System.out.println("oldgroupnameusername "+oldgroupnameusername);
					}
				}
				}
				
				}
			
			
			}
			
			for(int i=0;i<allDocs3.size();i++){
				if(allDocs3.get(i).get("_id").toString().replace("\"","").equals(deletegroupname+"-"+deleteownername)){
			
				String oldmembers=allDocs3.get(i).get("members").toString().replace("\"","");
				String[] members=null;
				members=oldmembers.split("-");
				for(int j=0;j<members.length;j++){
					if(((deletemembername).equals(members[j]))&&(deletemembername).equals(username)
							&&(!(deleteownername).equals(deletemembername))){
					deletememberstate="you are a member";}
				}}
				else{}
				
				}
			if(deletememberstate=="you are a member"){
				for(int i=0;i<allDocs3.size();i++){
					if(allDocs3.get(i).get("_id").toString().replace("\"","").equals(deletegroupname+"-"+deleteownername))
							//&&(allDocs3.get(i).get("members").toString().replace("-","").equals(deletemembername))
							{
						String delete="-"+deletemembername;	
						String oldmembers=allDocs3.get(i).get("members").toString().replace("\"","");
						oldmembers=oldmembers.replaceAll(delete,"");
						dbClient3.remove(allDocs3.get(i));
						deletememberstate="success delete yourself becuase youre a member"; 
						json3.addProperty("_id",deletegroupname+"-"+deleteownername);
						json3.addProperty("ownername",deleteownername);
						json3.addProperty("members",oldmembers);
						dbClient3.save(json3);
					}}
				for(int i=0;i<allDocs.size();i++){
					if(allDocs.get(i).get("_id").toString().replace("\"","").equals(username)){
					String olgroupnamemembername=allDocs.get(i).get("groupnameusername").toString().replace("\"","");
					String[] groupnamemmebername=olgroupnamemembername.split("/");
					for(int q=0;q<groupnamemmebername.length;q++){
						if((deletegroupname+"-"+deleteownername).equals(groupnamemmebername[q])){
							String oldgroupnameusername=allDocs.get(i).get("groupnameusername").toString().replace("\"","");
						
						String deletegroupnameusername="/"+deletegroupname+"-"+deleteownername;
						oldgroupnameusername=oldgroupnameusername.replaceAll(deletegroupnameusername,"");
						String password=allDocs.get(i).get("password").toString().replace("\"","");
						String token=allDocs.get(i).get("token").toString().replace("\"","");
						String accesstoken=allDocs.get(i).get("accesstoken").toString().replace("\"","");
						dbClient.remove(allDocs.get(i));
						json.addProperty("_id",username);
			            json.addProperty("password",password);
			            json.addProperty("token",token);
						json.addProperty("accesstoken",accesstoken);
						json.addProperty("groupnameusername",oldgroupnameusername);
						dbClient.save(json);
						
						}
					}
					}
					}
				
				}
			jsonobject.addProperty("deletememberstate", deletememberstate);
			response.setContentType("application/json");
			out.print(jsonobject);
			out.close();
	}}
					
				
		