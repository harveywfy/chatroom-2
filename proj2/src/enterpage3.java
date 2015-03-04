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



public class enterpage3 extends HttpServlet {
	CouchDbClient dbClient3 = new CouchDbClient("groupowner-member.properties");
	CouchDbClient dbClient = new CouchDbClient("couchdb.properties");
	/**
	 * Constructor of the object.
	 */
	public enterpage3() {
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

		;
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
		HttpSession session=request.getSession(false);
		
		String chatgroupname=(String) request.getParameter("chatgroupname");
		String chatownername=(String) request.getParameter("chatownername");
		String Username2=(String) session.getAttribute("seusername");
		String chatstate="error input try again";String memberstate="member";
		JsonObject jsonobject = new JsonObject();
		JsonObject json3= new JsonObject();
		List<JsonObject> allDocs = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
		List<JsonObject> allDocs3 = dbClient3.view("_all_docs").includeDocs(true).query(JsonObject.class);
		String oldmember="";
		PrintWriter out = response.getWriter();	
		System.out.println(chatgroupname+"-"+chatownername);
		for(int i=0;i<allDocs3.size();i++){
			if(allDocs3.get(i).get("_id").toString().replace("\"","").equals(chatgroupname+"-"+chatownername)){
				System.out.println(allDocs3.get(i).get("_id").toString().replace("\"",""));
				chatstate="success";
			}
			else{
				
				
			}
			}
		
//		if(chatstate=="find the group"){
//			for(int i=0;i<allDocs3.size();i++){
//				if(allDocs3.get(i).get("_id").toString().replace("\"","").equals(chatgroupname+"-"+chatownername))
//				{
//					if(allDocs3.get(i).get("ownername").toString().replace("\"","").equals(Username2)){
//						memberstate="owner";chatstate="success";
//						
//						oldmember=allDocs.get(i).get("members").toString().replace("\"","");
//						dbClient3.remove(allDocs.get(i));
//						json3.addProperty("_id",chatgroupname+"-"+chatownername);
//						json3.addProperty("ownername2",chatownername);
//						json3.addProperty("members",oldmember);
//						json3.addProperty("chatstate",chatstate);
//						json3.addProperty("identifystate",memberstate);
//						
//						dbClient3.save(json3);
//						out.print(allDocs3);
//					}else{
//						System.out.println("2");
//					}	
//				}
//			}
//		}
//		System.out.println(chatstate+"2");
		response.setContentType("application/json");
		jsonobject.addProperty("chatstate", chatstate);
		jsonobject.addProperty("username", Username2);
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
