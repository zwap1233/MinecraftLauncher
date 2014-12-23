package me.zwap1233.launcher.Profile;

import java.util.HashMap;

import org.json.JSONObject;

import me.zwap1233.launcher.Filesystem.FileJson;

public class ProfileMannager {
	
	private final String username;
	
	private HashMap<String, Profile> profiles = new HashMap<String, Profile>();
	private String selectedprofile;
	
	private String userdirendpoint;
	
	private FileJson savefile;
	private JSONObject save;
	
	/**
	 * {@code public ProfileMannager(String username, JSONObject profile)}<br><br> 
	 * 
	 * The ProfileMannager manages all profiles linked to a user.<br><br>
	 * 
	 * @param username - The name of user where this ProfileMannager is linked to
	 * @param profile - The JSONObject with the profile id and name of the selected user
	 */
	public ProfileMannager(String username, JSONObject profile){
		this.username = username;
		
		userdirendpoint = "\\profiles\\" + username + "\\";
		
		selectedprofile = profile.getString("name");
		
		loadProfile(profile);
	}
	
	/**
	 * {@code private void loadProfile(JSONObject profileobject)}<br><br>
	 * 
	 * creates a new profile with the given data.<br><br>
	 * 
	 * @param profileobject - The JSONObject where to profile is to be loaded from.
	 */
	private void loadProfile(JSONObject profileobject){
		profiles.put(profileobject.getString("name"), new Profile(username, profileobject.getString("name"), profileobject.getString("id")));
	}
	
	/**
	 * {@code private boolean saveProfile(String profilename)}<br><br>
	 * 
	 * Save a profile to the profiles.json file.<br><br>
	 * 
	 * @param profilename - The name of the profile to be saved
	 * @return false if couldn't save the specified profile
	 */
	private boolean saveProfile(String profilename){
		save = new JSONObject();
		
		save.put("username", username);
		for(String name:profiles.keySet()){
			save.put(name, profiles.get(name).getSaveData());
		}
		
		savefile = new FileJson(userdirendpoint + "profiles.json", save, true);
		if(savefile.getSucceed()){
			return true;
		}
		return false;
	}
	
	/**
	 * {@code public void saveprofiles()}<br><br>
	 * 
	 * save all stored profiles to the save file.
	 */
	public void saveprofiles(){
		for(String name:profiles.keySet()){
			saveProfile(name);
		}
	}
	
	/**
	 * {@code public String getSelectedProfile()}<br><br>
	 * 
	 * get the selected profile
	 * 
	 * @return The selected profile
	 */
	public String getSelectedProfile(){
		return selectedprofile;
	}
}
