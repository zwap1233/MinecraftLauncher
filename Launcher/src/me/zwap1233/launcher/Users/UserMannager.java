package me.zwap1233.launcher.Users;

import java.util.HashMap;
import java.util.UUID;

import me.zwap1233.launcher.Filesystem.FileJson;
import me.zwap1233.launcher.Filesystem.FileSystem;

import org.json.JSONObject;

public class UserMannager {
	
	private HashMap<String, JSONObject> users = new HashMap<String, JSONObject>();
	
	private String favoriteuser;
	
	private User activeuser;
	
	private String clienttoken;
	
	private FileJson usersfile;
	private JSONObject usersobj;
	
	/**
	 * {@code public UserMannager()}<br><br> 
	 * 
	 * The UserMannager handles creation of new users,
	 * loading know users, saving known users and launching
	 * the game. it also keeps track of the user that
	 * is logged in.
	 */
	public UserMannager(){
		FileSystem.getInstance().createProfilesDir();
		
		usersfile = new FileJson("\\profiles\\users.json");
		if(usersfile.getSucceed()){
			usersobj = usersfile.getJSONObject();
			
			loadUsers(usersobj);
			
			if(usersobj.has("favoriteUser")){
				if(users.containsKey(usersobj.getString("favoriteUser"))){
					favoriteuser = usersobj.getString("favoriteUser");
				}
			}
			
			if(usersobj.has("clientToken")){
				clienttoken = usersobj.getString("clientToken");
			}
		}
	}
	
	/**
	 * {@code public boolean login(String username)}</br></br>
	 * 
	 * log the user in with the specified username,
	 * this only works if the user is already known
	 * and the accessToken and clientToken are known.</br></br>
	 * 
	 * @param username - the name of the user you want to login.
	 * @return false if the user can't be logged in.
	 */
	public boolean login(String username){
		if(users.containsKey(username)){
			
			JSONObject user = users.get(username);
			if(clienttoken != null){
				if(user.has("accessToken")){
					logoutActiveUser();
					
					activeuser = new User(this, UUID.randomUUID(), username, user.getString("accessToken"), clienttoken, true);
					
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * {@code public boolean login(String username, String password)}</br></br>
	 * 
	 * log a user in with the specified username,
	 * if the clientToken and accessToken are known
	 * then these are used if not than the password
	 * is used.</br></br>
	 * 
	 * @param username - The name of the user you want to login.
	 * @param password - The password of the user you want to login.
	 * @return false if the user can't be logged in.
	 */
	public boolean login(String username, String password){
		if(users.containsKey(username)){
			
			JSONObject user = users.get(username);
			UUID uuid = UUID.randomUUID();
			if(clienttoken != null){
				if(user.has("accessToken")){
					logoutActiveUser();
					
					activeuser = new User(this, uuid, username, user.getString("accessToken"), clienttoken, true);
					users.put(username, user.put("accessToken", activeuser.getAccessToken(uuid)));
					uuid = null;
					
					return true;
				}
				logoutActiveUser();
				
				activeuser = new User(this, uuid, username, password, clienttoken);
				users.put(username, user.put("accessToken", activeuser.getAccessToken(uuid)));
				uuid = null;
				
				return true;
			} else {
				logoutActiveUser();
				
				activeuser = new User(this, uuid, username, password);
				clienttoken = activeuser.getClientToken(uuid);
				users.put(username, user.put("accessToken", activeuser.getAccessToken(uuid)));
				uuid = null;
				
				return true;
			}
		}
		return false;
	}
	
	/**
	 * {@code public boolean logoutActiveUser()}</br></br>
	 * 
	 * log the user out that is logged in.</br></br>
	 * 
	 * @return false if there is no active user
	 */
	public boolean logoutActiveUser(){
		if(activeuser != null){
			activeuser.destroy();
			activeuser = null;
			
			return true;
		}
		return false;
	}
	
	/**
	 * {@code public boolean addUser(String username)}</br></br>
	 * 
	 * add a new user that was not known before.
	 * only use this function if you don't know the
	 * accessToken.</br></br>
	 * 
	 * Note: this doesn't create a new instance of the
	 * class but merely adds a new option to the list
	 * of possible users.</br></br>
	 * 
	 * @param username - The name of the user to be created
	 * @return false if the user already exists
	 */
	public boolean addUser(String username){
		if(!users.containsKey(username)){
			JSONObject user = new JSONObject();
			user.put("username", username);
			
			users.put(username, user);
			
			return true;
		}
		return false;
	}
	
	/**
	 * {@code public boolean addUser(String username, String accessToken)}</br></br>
	 * 
	 * add a new user that was not known before with the specified accessToken</br></br>
	 * 
	 * Note: this doesn't create a new instance of the
	 * class but merely adds a new option to the list
	 * of possible users.</br></br>
	 * 
	 * @param username - The name of the user to be created
	 * @param accessToken - The accessToken of the specified user
	 * @return false if the user already exists
	 */
	public boolean addUser(String username, String accessToken){
		if(!users.containsKey(username)){
			JSONObject user = new JSONObject();
			user.put("username", username);
			user.put("accessToken", accessToken);
			
			users.put(username, user);
			
			return true;
		}
		return false;
	}
	
	/**
	 * {@code private void loadUsers(JSONObject usersobj)}</br></br>
	 * 
	 * load all data from the JSONObject</br></br>
	 * 
	 * @param usersobj - The JSONObject where the data should be loaded from
	 */
	private void loadUsers(JSONObject usersobj){
		for(String key:usersobj.keySet()){
			if(!key.equals("favoriteUser") && !key.equals("clientToken")){
				users.put(key, usersobj.getJSONObject(key));
			}
		}
	}
	
	/**
	 * {@code private boolean saveUsersToFile(String path)}</br></br>
	 * 
	 * saves the users, the clientToken and the favorite user to the file specified by path<br><br>
	 * 
	 * @param path - a path pointing to the location where the users should be stored
	 * @return false if there was an error or if there is no activeuser
	 */
	private boolean saveUsersToFile(String path){
		usersobj = new JSONObject();
		
		if(clienttoken != null){
			usersobj.put("clientToken", clienttoken);
		}
		
		if(favoriteuser != null){
			usersobj.put("favoriteUser", favoriteuser);
		}
		
		for(String user:users.keySet()){
			usersobj.put(user, users.get(user));
		}
		
		usersfile = new FileJson(path, usersobj, true);
		if(usersfile.getSucceed()){
			return true;
		}
		return false;
	}
	
	/**
	 * {@code public void destroy()}</br></br>
	 * 
	 * terminate this instance of UserMananger.<br><br>
	 */
	public void destroy(){
		saveUsersToFile("\\profiles\\users.json");
		activeuser.destroy();
	}
	
	/**
	 * {@code public void setFavoriteUser(String username)}<br><br>
	 * 
	 * set a new favorite user.<br><br>
	 * 
	 * @param username - The name of the user that should be set as the new favorite user
	 */
	public void setFavoriteUser(String username){
		this.favoriteuser = username;
	}
}
