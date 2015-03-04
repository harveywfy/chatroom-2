

import java.io.IOException;
import java.io.PrintWriter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.sf.json.JSONArray;

import org.json.simple.JSONObject;
import org.lightcouch.CouchDbClient;
import com.google.gson.JsonObject;

public class chat extends HttpServlet {
CouchDbClient dbClient4 = new CouchDbClient("couchdb-chat.properties");
    //ArrayList<JSONObject> arraylist = new ArrayList<JSONObject>();


	private static final long serialVersionUID = 1L;
	
    public chat() {
        super(); }
    

    protected void save(){
        
    }
    protected void find() throws IOException{

    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<JsonObject> arraylist2 = new ArrayList<JsonObject>();
		response.setContentType("application/json");
		
		String groupname = request.getParameter("groupname");
		String ownername = request.getParameter("ownername");
		PrintWriter out = response.getWriter();
		List<JsonObject> allDocs4 = dbClient4.view("_all_docs").includeDocs(true).query(JsonObject.class);
		

			for(int i=0;i<allDocs4.size();i++){
				JsonObject a=new JsonObject();
				a=allDocs4.get(i);
				String agroupname=a.get("groupname").toString().replace("\"","");
				String aownername=a.get("ownername").toString().replace("\"","");
				
				if(agroupname.equals(groupname)&&(aownername.equals(ownername))){
					arraylist2.add(a);
				}
			}
			out.print(arraylist2);
			out.close();
		
	}
		
		

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.save();ArrayList<JsonObject> arraylist = new ArrayList<JsonObject>();
		String username = request.getParameter("username");
		String groupname = request.getParameter("groupname");
		String ownername = request.getParameter("ownername");
		String content = request.getParameter("content");
		
		String time = new Date().toString();
		
		JsonObject objclient = new JsonObject();
		
		List<JsonObject> allDocs4 = dbClient4.view("_all_docs").includeDocs(true).query(JsonObject.class);
		
		if(request.getParameter("content")==""){
			System.out.print("null");
		}
		else{
			objclient.addProperty("_id",time);
			objclient.addProperty("username", username);
			objclient.addProperty("groupname", groupname);
			objclient.addProperty("ownername", ownername);
			objclient.addProperty("content", content);

			allDocs4.add(objclient);
			dbClient4.save(objclient);
			for(int i=0;i<allDocs4.size();i++){
				JsonObject a=new JsonObject();
				a=allDocs4.get(i);
				String agroupname=a.get("groupname").toString().replace("\"","");
				String aownername=a.get("ownername").toString().replace("\"","");
				
				if(agroupname.equals(groupname)&&(aownername.equals(ownername))){
					arraylist.add(a);
				}
			}
			PrintWriter out = response.getWriter();
			
			
			out.print(arraylist);
			out.close();
		}
		
		
	}

}