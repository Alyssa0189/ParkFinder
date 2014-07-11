package cs310.creativeteamname.server;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Logger;

import java.net.HttpURLConnection;
//import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.apphosting.utils.security.urlfetch.URLFetchServiceStreamHandler;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310.creativeteamname.client.FacebookService;
public class FacebookServiceImpl extends RemoteServiceServlet  implements FacebookService {
	Logger logger = Logger.getLogger("Logger");
	String clientId = "1416292605327006";
	String appSecret = "8ee64e750db5af43566cb3afecb64778";
	String loginURL = "https://www.facebook.com/dialog/oauth?";
	String confirmIdentityURL = "https://graph.facebook.com/oauth/access_token?";
	String debugURL = "https://graph.facebook.com/debug_token?";
    
	String redirectURL = "http%3A%2F%2F1-dot-yvrparks.appspot.com%2Fparkfinder%2Ffacebook";  // TODO consider having comment appended
	String token;
	String postURL = "https://graph.facebook.com";
	String appTokenUrl = "https://graph.facebook.com/oauth/access_token?";
	
	String access_token = null;
	String comment = null;
	String parkName = null;
	public String setStoryData(String comment, String parkName){
		this.comment = comment;
		this.parkName = parkName;
		return "success";
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //Do something in here.
		
		logger.severe("received get request: ");
		Enumeration e = req.getParameterNames();
		
		while(e.hasMoreElements()){
			String s = (String)e.nextElement();
			logger.severe(s);
		}
		resp.setStatus(HttpServletResponse.SC_FOUND);
		String code = req.getParameter("code");
		//String comment = req.getParameter("comment");
		logger.severe(req.getRequestURL().toString());
		logger.severe("comment = " + comment);
		token = exchangeCode(code);
		
		String userId = debugToken(token, access_token);
		this.postOnWall(userId, access_token, comment);
		logger.severe("token = " + token);
		try {
			//resp.sendRedirect("http://www.google.com");
			//resp.sendRedirect("http://127.0.0.1:8888/FacebookLoginRedirect.html");
			resp.sendRedirect("/FacebookLoginRedirect.html");
		} catch (IOException e1) {
			logger.severe("cannot redirect");
		}
    }

	/**
	 * Given a code from a fb redirect, performs a get request to gain an access token
	 * @param code
	 * @param comment
	 * @return
	 */
	private String exchangeCode(String code){
		String res = null;
		logger.severe("investigating code");
		String url = "https://graph.facebook.com/oauth/access_token?";
		url += "client_id=" + clientId + "&";
		comment = comment.replace(" ", "++");
		url += "redirect_uri=" + redirectURL + "&"; // TODO consider having the comment appended to redirect URL?
		url += "client_secret=" + appSecret + "&";
		url += "code=" + code;
		logger.severe("get request follows:");
		logger.severe(url);
		URL obj;
		try {
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			logger.severe("receiving response from fb: " + response);
			
			res = parseToken(response.toString());
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.severe("malformed");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.severe("protocol");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.severe("io exception");
		}
		return res;
	}
	private static String parseToken(String message){
		String token = null;
		int start = message.indexOf("token=");
		
		if(start != -1){
			String s = message.substring(start + "token=".length());
			int end = s.indexOf("&");
			if(end != -1){
				token = s.substring(0, end);
			}else{
				token = s;
			}
		}
		return token;
	}
	/**
	 * Returns the id of the user
	 * @param token
	 * @param accessToken
	 * @return
	 */
	private String debugToken(String token, String accessToken){
		//"GET graph.facebook.com/debug_token?
	     //input_token={token-to-inspect}
	    // &access_token={app-token-or-admin-token}"";
		String userId = null;
		logger.severe("debugging token");
		String url = debugURL;
		url += "input_token=" + token + "&";
		url += "access_token=" + accessToken;
		logger.severe(url);
		URL obj;
		try{
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			 
			// optional default is GET
			con.setRequestMethod("GET");
	 
			//add request header
			//con.setRequestProperty("User-Agent", USER_AGENT);
	 
			int responseCode = con.getResponseCode();
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			logger.severe("response from facebook:");
			logger.severe(response.toString());
			userId = getUserId(response.toString());
		}catch(Exception e){
			logger.severe("encountered exception in debugToken");
			logger.severe(e.getMessage());
		}
		return userId;
	}
	private String getUserId(String message){
		String userId = null;
		String idKey = "user_id\":\"";
		int start = message.indexOf(idKey) + idKey.length();
		if(start != -1){
			String substring = message.substring(start);
			int end = substring.indexOf("\"");
			if(end != -1){
				substring = substring.substring(0, end);
				userId = substring.trim();
				logger.severe("user id follows:");
				logger.severe(userId);
			}
		}
		
		return userId;
		
	}
	
	private void postOnWall(String userId, String accessToken, String comment) {
		String url = postURL + "/";
		url += userId + "/feed";

		try {
			logger.severe("url = " + url);
			URL obj = new URL(url);
			HttpURLConnection con;
			con = (HttpURLConnection) obj.openConnection();
			//con = (HttpsURLConnection) obj.openConnection();

			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String urlParameters = "message=" + "I commented on a park (" + parkName + ") at Vancouver Park Finder! \n" + comment + " \nhttp://1-dot-yvrparks.appspot.com"
					+ "&";
			urlParameters += "access_token=" + accessToken;

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			logger.severe("response code = " + responseCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			logger.severe("facebook responding to POST ON WALL request:");
			logger.severe(response.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.severe("encountered " + e.getClass() + " in postOnWall");
//			StackTraceElement trace[] = e.getStackTrace();
//			StackTraceElement t;
//			for(int i = 0; i < trace.length; i++){
//				t = trace[i];
//				logger.severe(t.getClassName());
//				logger.severe(t.getMethodName());
//				logger.severe("line number = " + t.getLineNumber());
//			}
			
		}
	}
	public String logAppOnFacebook(){
		getAppToken();
		return "success";
	}
	
	public String getAppToken(){
		String appToken = null;
		//GET /oauth/access_token?
	     //client_id={app-id}
	    // &client_secret={app-secret}
	    // &grant_type=client_credentials
		String url = appTokenUrl;
		url += "client_id=" + clientId + "&";
		url += "client_secret=" + appSecret + "&";
		url += "grant_type=client_credentials";
		URL obj;
		try{
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			 
			// optional default is GET
			con.setRequestMethod("GET");
	 
			//add request header
			//con.setRequestProperty("User-Agent", USER_AGENT);
	 
			int responseCode = con.getResponseCode();
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			appToken = parseToken(response.toString());
			logger.severe("response from facebook:");
			logger.severe(response.toString());
			logger.severe("found appToken");
			logger.severe(appToken);
			access_token = appToken;
		}catch(Exception e){
			logger.severe("encountered following error:");
			logger.severe(e.getMessage());
		}
		return appToken;
	}
}
