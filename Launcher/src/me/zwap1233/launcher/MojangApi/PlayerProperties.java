package me.zwap1233.launcher.MojangApi;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class PlayerProperties {
	private URL url;
	private HttpURLConnection con;
	
	private InputStreamReader in;
	private InputStreamReader error;
	
	private JSONObject result;
	
	private boolean succeed = false;
	
	/**
	 * {@code public PlayerProperties(String userid)}<br><br> 
	 * 
	 * download all the properties of the specified user by the userid.<br><br>
	 * 
	 * @param userid - The unique identifier of the user
	 */
	public PlayerProperties(String userid){
		try {
			this.url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + userid);
			
			setupConnection();
			execute();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setupConnection() throws IOException{
		con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		
		con.setDoInput(true);
		con.setDoOutput(false);
	}
	
	private void execute() throws IOException{
		setupConnection();
		
		if(con.getResponseCode() == 200){
			result = readResponse();
			succeed = true;
		} else if(con.getResponseCode() >= 500){
			result = new JSONObject();
			result.put("error", "Server is in maintenance");
			succeed = false;
		} else {
			result = readError();
			succeed = false;
		}
	}
	
	private JSONObject readResponse() throws IOException {
		in = new InputStreamReader(con.getInputStream());
		StringBuilder str = new StringBuilder();
		
		int c;
		while((c = in.read()) != -1){
			str.append((char) c);
		}
		
		in.close();
		return new JSONObject(str.toString());
	}
	
	private JSONObject readError() throws IOException {
		error = new InputStreamReader(con.getErrorStream());
		
		StringBuilder str = new StringBuilder();
		int c;
		while((c = error.read()) != -1){
			str.append((char) c);
		}
		
		error.close();
		
		JSONObject obj = new JSONObject(str.toString());
		obj.put("statusCode", con.getResponseCode());
		
		return obj;
	}
	
	public boolean getSucceed(){
		return succeed;
	}
	
	public JSONObject getResult(){
		return result;
	}
}
