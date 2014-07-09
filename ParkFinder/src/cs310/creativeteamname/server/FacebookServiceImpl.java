package cs310.creativeteamname.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310.creativeteamname.client.FacebookService;
public class FacebookServiceImpl extends RemoteServiceServlet  implements FacebookService {
	Logger logger = Logger.getLogger("Logger");
	String clientId = "1416292605327006";
	String appSecret = "8ee64e750db5af43566cb3afecb64778";
	String loginURL = "https://www.facebook.com/dialog/oauth?";
	String investigateURL = "https://graph.facebook.com/oauth/access_token?";
	String redirectURL = "http%3A%2F%2F1-dot-yvrparks.appspot.com%2Fparkfinder%2Ffacebook%3Fcomment%3D";
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //Do something in here.
		
		logger.severe("received get request: " + req.getPathInfo());
		Enumeration e = req.getParameterNames();
		
		while(e.hasMoreElements()){
			String s = (String)e.nextElement();
			logger.severe(s);
		}
		resp.setStatus(HttpServletResponse.SC_FOUND);
		String code = req.getParameter("code");
		String comment = req.getParameter("comment");
		investigateCode(code, comment);
		try {
			//resp.sendRedirect("http://www.google.com");
			//resp.sendRedirect("http://127.0.0.1:8888/FacebookLoginRedirect.html");
			resp.sendRedirect("/FacebookLoginRedirect.html");
		} catch (IOException e1) {
			logger.severe("cannot redirect");
		}
    }
	private void investigateCode(String code, String comment){
		logger.severe("investigating code");
		String url = "https://graph.facebook.com/oauth/access_token?";
		url += "client_id=" + clientId + "&";
		url += "redirecturi=" + redirectURL + comment + "&";
		url += "client_secret=" + appSecret + "&";
		url += "code=" + code;
		URL obj;
		try {
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			 
			// optional default is GET
			con.setRequestMethod("GET");
	 
			//add request header
			//con.setRequestProperty("User-Agent", USER_AGENT);
	 
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			logger.severe("receiving response from fb: " + response);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
