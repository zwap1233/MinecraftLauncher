package me.zwap1233.launcher.MojangApi;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProfileInformation {
	private String profilename;
	
	private URL url;
	private HttpURLConnection con;
	
	private OutputStreamWriter out;
	private InputStreamReader in;
	private InputStreamReader error;
	
	private JSONArray payload;
	
	private JSONArray result;
	private JSONObject errorobj;
	
	private boolean succeed = false;
	
	/**
	 * {@code public ProfileInformation(String profilename)}<br><br> 
	 * 
	 * download the information of a profile<br><br>
	 * 
	 * @param profilename - The name of the profile
	 */
	public ProfileInformation(String profilename){
		this.profilename = profilename;
		
		try {
			this.url = new URL("https://api.mojang.com/profiles/minecraft");
			
			payload = createPayload();
			
			setupConnection();
			execute();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private JSONArray createPayload(){
		JSONArray array = new JSONArray();
		
		array.put(profilename);
		
		return array;
	}
	
	private void setupConnection() throws IOException{
		con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		
		con.setDoInput(true);
		con.setDoOutput(true);
	}
	
	private void execute() throws IOException{
		setupConnection();
		
		writePayload();
		
		if(con.getResponseCode() == 200){
			result = readResponse();
			succeed = true;
		} else {
			errorobj = readError();
			succeed = false;
		}
	}
	
	protected void writePayload() throws IOException{
		out = new OutputStreamWriter(con.getOutputStream());
		
		payload.write(out);
		
		out.flush();
		out.close();
	}
	
	private JSONArray readResponse() throws IOException {
		in = new InputStreamReader(con.getInputStream());
		StringBuilder str = new StringBuilder();
		
		int c;
		while((c = in.read()) != -1){
			str.append((char) c);
		}
		
		in.close();
		return new JSONArray(str.toString());
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
	
	public JSONArray getResult(){
		return result;
	}
	
	public JSONObject getError(){
		return errorobj;
	}
}
