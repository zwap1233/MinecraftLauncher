package me.zwap1233.launcher.Profile;

import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;

import me.zwap1233.launcher.MojangApi.PlayerProperties;

import org.json.JSONArray;
import org.json.JSONObject;

public class Profile {
	
	private final String username;
	
	private final String name;
	private final String id;
	
	private ProfileImage image;
	
	private JSONArray propertiesraw;
	
	private byte[] base64;
	private String base64string;
	
	private String skinurl;
	
	private JSONObject savedata;
	
	/**
	 * {@code public Profile(String username, String name, String id)}<br><br> 
	 * 
	 * this resembles a minecraft profile. Not to be mistaken for a user.<br><br>
	 * 
	 * @param username - The name of the associated user
	 * @param name - The name of this profile
	 * @param id - The id of this profile
	 */
	public Profile(String username, String name, String id){
		this.username = username;
		
		this.name = name;
		this.id = id;
		
		loadProperties();
		
		image = new ProfileImage(this);
	}
	
	/**
	 * {@code private void loadProperties()}<br><br>
	 * 
	 * load all properties of this profile from the mojang servers
	 */
	private void loadProperties(){
		PlayerProperties playerinfo = new PlayerProperties(id);
		
		if(playerinfo.getSucceed()){
			
			propertiesraw = playerinfo.getResult().getJSONArray("properties");
			
			try {
				decodeProperties();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(playerinfo.getResult().toString());
		}
	}
	
	/**
	 * {@code private void decodeProperties() throws UnsupportedEncodingException}<br><br>
	 * 
	 * decode the base64 string and load the data from it.<br><br>
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private void decodeProperties() throws UnsupportedEncodingException{
		JSONObject obj;
		
		for(int i = 0; i < propertiesraw.length(); i++){
			obj = propertiesraw.getJSONObject(i);
			
			base64 = DatatypeConverter.parseBase64Binary(obj.getString("value"));
			base64string = new String(base64, "US-ASCII");
			
			JSONObject props = new JSONObject(base64string);
			
			skinurl = props.getJSONObject("textures").getJSONObject("SKIN").getString("url");
		}
	}
	
	/**
	 * {@code public JSONObject getSaveData()}<br><br>
	 * 
	 * collects all data that should be saved and puts it in a JSONObject.<br><br>
	 * 
	 * @return A JSONObject with all the data that should be saved
	 */
	public JSONObject getSaveData(){
		savedata = new JSONObject();
		savedata.put("name", name);
		savedata.put("id", id);
		
		return savedata;
	}
	
	/**
	 * {@code public String getSkinUrl()}<br><br>
	 * 
	 * Get the skin url.<br><br>
	 * 
	 * @return The url to the location where the skin is stored on the mojang servers
	 */
	public String getSkinUrl(){
		return skinurl;
	}
	
	/**
	 * {@code public String getUsername()}<br><br>
	 * 
	 * Gets the name of the associated user.<br><br>
	 * 
	 * @return The name of the user that this profile is associated with
	 */
	public String getUsername(){
		return username;
	}
	
	/**
	 * {@code public String getName()}<br><br>
	 * 
	 * Gets the name of this profile
	 * 
	 * @return The name of this profile
	 */
	public String getName(){
		return name;
	}
}
