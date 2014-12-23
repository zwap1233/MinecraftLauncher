package me.zwap1233.launcher.Authentication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import org.json.JSONObject;

public abstract class Yggdrasil {
	
	protected final String baseurl = "https://authserver.mojang.com";
	
	protected URL url;
	protected HttpURLConnection con;
	
	protected OutputStreamWriter out;
	protected InputStreamReader in;
	protected InputStreamReader error;
	
	protected JSONObject payload;
	
	protected JSONObject result;
	
	protected boolean succeed = false;
	
	protected final UUID uuid;
	
	/**
	 * {@code public Yggdrasil(UUID uuid, String url)}<br><br> 
	 * 
	 * @param uuid - The uuid to be associeted with this instance
	 * @param url - The endpoint to be connected with
	 */
	public Yggdrasil(UUID uuid, String url){
		this.uuid = uuid;
		
		try {
			this.url = new URL(baseurl + url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract JSONObject createPayload();
	
	protected void execute() throws IOException{
		setupConnection();
		
		writePayload();
		
		if(con.getResponseCode() == 200){
			result = readResponse();
			succeed = true;
		} else {
			result = readError();
			succeed = false;
		}
	}
	
	protected void setupConnection() throws IOException{
		con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Content-Length", String.valueOf(payload.toString().length()));
		
		con.setDoInput(true);
		con.setDoOutput(true);
	}
	
	public boolean getSucceed(){
		return succeed;
	}
	
	public JSONObject getResult(UUID uuid){
		if(this.uuid.equals(uuid)){
			return result;
		} else {
			return null;
		}
	}
	
	protected void writePayload() throws IOException{
		out = new OutputStreamWriter(con.getOutputStream());
		
		payload.write(out);
		
		out.flush();
		out.close();
	}
	
	protected JSONObject readResponse() throws IOException {
		in = new InputStreamReader(con.getInputStream());
		StringBuilder str = new StringBuilder();
		
		int c;
		while((c = in.read()) != -1){
			str.append((char) c);
		}
		
		in.close();
		return new JSONObject(str.toString());
	}
	
	protected JSONObject readError() throws IOException {
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
}
