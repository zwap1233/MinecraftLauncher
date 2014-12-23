package me.zwap1233.launcher.Users;

import java.util.UUID;

import org.json.JSONObject;

import me.zwap1233.launcher.Authentication.Authenticate;
import me.zwap1233.launcher.Authentication.Refresh;
import me.zwap1233.launcher.Filesystem.Download.Version;
import me.zwap1233.launcher.Filesystem.Download.Game.Minecraft;
import me.zwap1233.launcher.Filesystem.Download.Libraries.Libraries;
import me.zwap1233.launcher.Profile.ProfileMannager;

public class User {
	
	private final UserMannager um;
	
	private final UUID uuid;
	
	private final String user;
	private String pass;
	
	private String accesstoken;
	private String clienttoken;
	
	private String versionid;
	
	private Version version;
	private Libraries libraries;
	
	private Minecraft minecraft;
	
	private ProfileMannager pm;
	
	private boolean alive = true;

	/**
	 * {@code public User(UserMannager um, UUID uuid, String user, String pass)}<br><br>
	 * 
	 * Create a new user with the given username and password,
	 * the usermannager is required for proper functioning.
	 * Only use if you don't know the clienttoken and accesstoken<br><br>
	 * 
	 * This class is equipped with a simple security mechanism, the uuid is later needed to aquire the tokens.<br><br>
	 * 
	 * @param um - The UserMannager
	 * @param uuid - The UUID
	 * @param user - The name of this user
	 * @param pass - The password of this user
	 */
	public User(UserMannager um, UUID uuid, String user, String pass){
		this.um = um;
		
		this.uuid = uuid;
		
		this.user = user;
		this.pass = pass;
		
		this.accesstoken = null;
		this.clienttoken = null;
		
		this.versionid = "1.7.10";
		
		init();
	}
	
	/**
	 * {@code public User(UserMannager um, UUID uuid, String user, String pass, String clienttoken)}<br><br>
	 * 
	 * Create a new user with the given username and password,
	 * the usermannager is required for proper functioning.
	 * Only use if you don't know the accesstoken<br><br>
	 * 
	 * This class is equipped with a simple security mechanism, the uuid is later needed to aquire the tokens.<br><br>
	 * 
	 * @param um - The UserMannager
	 * @param uuid - The UUID
	 * @param user - The name of this user
	 * @param pass - The password of this user
	 * @param clienttoken - The clientToken of this session
	 */
	public User(UserMannager um, UUID uuid, String user, String pass, String clienttoken){
		this.um = um;
		
		this.uuid = uuid;
		
		this.user = user;
		this.pass = pass;
		
		this.accesstoken = null;
		this.clienttoken = clienttoken;
		
		this.versionid = "1.7.10";
		
		init();
	}
	
	/**
	 * {@code public User(UserMannager um, UUID uuid, String user, String accesstoken, String clienttoken, boolean useaccesstoken)}<br><br>
	 * 
	 * Create a new user with the given username,
	 * the usermannager is required for proper functioning.<br><br>
	 * 
	 * the accesstoken is used to login in combination with the clienttoken.
	 * this constructor is preferred over the others but is the required
	 * clienttoken and accesstoken are unknown one of the other constructors
	 * can be used.<br><br>
	 * 
	 * if useaccesstoken is false the argument accesstoken will be used as a
	 * password and this constructor functions accactly the same as the constructor
	 * User(UserMannager um, String user, String pass, String clienttoken).<br><br>
	 * 
	 * This class is equipped with a simple security mechanism, the uuid is later needed to aquire the tokens.<br><br>
	 * 
	 * @param um - The UserMannager
	 * @param uuid - The UUID
	 * @param user - the name of the user
	 * @param accesstoken - The accessToken to be associated with this user
	 * @param clienttoken - The clientToken of this session
	 * @param useaccesstoken - if false the accessToken will be used as a password and a new accessToken will be generated
	 */
	public User(UserMannager um, UUID uuid, String user, String accesstoken, String clienttoken, boolean useaccesstoken){
		this.um = um;
		
		this.uuid = uuid;
		
		this.user = user;
		
		if(useaccesstoken){
			this.pass = null;
			this.accesstoken = accesstoken;
		} else {
			this.pass = accesstoken;
			this.accesstoken = null;
		}
		
		this.clienttoken = clienttoken;
		
		this.versionid = "1.7.10";
		
		init();
	}
	
	/**
	 * {@code private void init()}<br><br>
	 * 
	 * initialize this class, this code is put in its own method so it wouldn't have to
	 * be copied in each constructor.
	 */
	private void init(){
		if(accesstoken == null || clienttoken == null){
			if(Authenticate()){
				version = new Version(versionid);
				minecraft = version.getMinecraft();
				libraries = version.getLibraries();
				
			} else {
				destroy();
			}
		} else {
			if(Refresh()){
				version = new Version(versionid);
				minecraft = version.getMinecraft();
				libraries = version.getLibraries();
			} else {
				destroy();
			}
		}
	}
	
	/**
	 * {@code private boolean Authenticate()}<br><br>
	 * 
	 * logs a user in using the authenticate method this will generate a new 
	 * accesstoken and can generate a new clienttoken if it is not provided
	 * before hand.<br><br>
	 * 
	 * @return if failed to login this will return false
	 */
	private boolean Authenticate(){
		UUID uuid = UUID.randomUUID();
		Authenticate auth;
		
		if(clienttoken != null){
			auth = new Authenticate(uuid, user, pass, clienttoken);
		} else {
			auth = new Authenticate(uuid, user, pass);
		}
		
		if(auth.getSucceed()){
			JSONObject result = auth.getResult(uuid);
			
			accesstoken = result.getString("accessToken");
			clienttoken = result.getString("clientToken");
			
			JSONObject profile = result.getJSONObject("selectedProfile");
			
			pm = new ProfileMannager(this.user, profile);
			
			profile = null;
			result = null;
			uuid = null;
			auth = null;
			
			return true;
		} else {
			System.out.println("Failed to login, " + auth.getResult(uuid).getString("errorMessage"));
			
			uuid = null;
			auth = null;
			
			return false;
		}
	}
	
	/**
	 * {@code private boolean Refresh()}<br><br>
	 * 
	 * logs a user in using the refresh method this needs the clientToken and accessToken
	 * to be provided before hand. This method is prevered over the authenticate method since
	 * this doesn't generate a new accessToken and clientToken<br><br>
	 * 
	 * @return if failed to login this will return false
	 */
	private boolean Refresh(){
		UUID uuid = UUID.randomUUID();
		Refresh refresh;
		
		if(clienttoken == null || accesstoken == null){
			return false;
		}
		
		refresh = new Refresh(uuid, accesstoken, clienttoken);
		if(refresh.getSucceed()){
			accesstoken = refresh.getResult(uuid).getString("accessToken");
			clienttoken = refresh.getResult(uuid).getString("clientToken");
			
			JSONObject profile = refresh.getResult(uuid).getJSONObject("selectedProfile");
			pm = new ProfileMannager(this.user, profile);
			
			profile = null;
			uuid = null;
			refresh = null;
			
			return true;
		} else {
			System.out.println("Failed to login, " + refresh.getResult(uuid).getString("errorMessage"));
		}
		return false;
	}
	
	/**
	 * {@code public void destroy()}<br><br>
	 * 
	 * terminate this user, can be compared to logging out.
	 */
	public void destroy(){
		if(alive){
			alive = false;
			
			pass = null;
			accesstoken = null;
			clienttoken = null;
			
			if(pm != null){
				pm.saveprofiles();
			}
		}
	}
	
	/**
	 * {@code public String getClientToken(UUID uuid)}<br><br>
	 * 
	 * get the clientToken, the given uuid has to be the same as the one given in the constructor.
	 * 
	 * @param uuid - The UUID to verify that the caller is allowed to use this.
	 * @return if the uuid is invailid this will return false
	 */
	public String getClientToken(UUID uuid){
		if(uuid.equals(this.uuid) && alive){
			return clienttoken;
		}
		return null;
	}
	
	/**
	 * {@code public String getAccessToken(UUID uuid)}<br><br>
	 * 
	 * get the accessToken, the given uuid has to be the same as the one given in the constructor.
	 * 
	 * @param uuid - The UUID to verify that the caller is allowed to use this.
	 * @return if the uuid is invailid this will return false
	 */
	public String getAccessToken(UUID uuid){
		if(uuid.equals(this.uuid) && alive){
			return accesstoken;
		}
		return null;
	}
}
