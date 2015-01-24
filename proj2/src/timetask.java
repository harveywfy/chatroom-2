import java.util.List;
import java.util.TimerTask;

import org.lightcouch.CouchDbClient;

import com.google.gson.JsonObject;

public class timetask extends TimerTask{
	CouchDbClient dbClient = new CouchDbClient("couchdb.properties");
		public String username = null;
		
		public timetask(String user) { 
		    this.username=user;
		  } 
		public void run(){
			List<JsonObject> list = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
			for(int j=0; j<list.size(); j++){
				if(list.get(j).get("_id").toString().replace("\"", "").replace("\\", "").equals(username)){			
					if(list.get(j).get("password").toString().replace("\"", "").equals("")){		
						dbClient.remove(list.get(j));
					}
				}
			}
		}
	}