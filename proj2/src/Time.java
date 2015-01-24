import java.util.Timer;

import javax.servlet.http.HttpServlet;

	public class Time extends HttpServlet {
		public String username = null;
		public Time(String username){
			this.username=username;
		}
		public void reminde(){
			Timer timer = new Timer(true); 
			timetask mytask = new timetask(username);
			timer.schedule(mytask,60000*5);
			//timer.schedule(mytask,10000);
		}
	
		
	}