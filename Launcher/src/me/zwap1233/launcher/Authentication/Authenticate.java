package me.zwap1233.launcher.Authentication;

import java.io.IOException;
import java.util.UUID;

import org.json.JSONObject;

public class Authenticate extends Yggdrasil{
	
	private final String user;
	private final String pass;
	
	private String clienttoken;
	
	/**
	 * {@code public Authenticate(UUID uuid, String username, String password)}<br><br> 
	 * 
	 * log a user in with the specifeid username and password.
	 * the function {@code public Authenticate(UUID uuid, String username, String password, String clienttoken)}
	 * is prefered but this constructor can be used if the clientToken is unknown.<br><br>
	 * 
	 * @param uuid - The uuid to be associated with this instance
	 * @param username - The name of the user to be logged in
	 * @param password - The password of the user to be logged in
	 */
	public Authenticate(UUID uuid, String username, String password){
		super(uuid, "/authenticate");
		
		this.user = username;
		this.pass = password;
		
		payload = createPayload();
		
		try {
			execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Authenticate(UUID uuid, String username, String password, String clienttoken){
		super(uuid, "/authenticate");
		
		this.user = username;
		this.pass = password;
		
		this.clienttoken = clienttoken;
		
		payload = createPayload();
		
		try {
			execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.clienttoken = null;
	}
	
	protected JSONObject createPayload(){
		JSONObject obj = new JSONObject();
		
		JSONObject agent = new JSONObject();
		agent.put("name", "minecraft");
		agent.put("version", 1);
		
		obj.put("agent", agent);
		obj.put("username", user);
		obj.put("password", pass);
		
		if(clienttoken != null){
			obj.put("clientToken", clienttoken);
		}
		
		return obj;
	}
}
