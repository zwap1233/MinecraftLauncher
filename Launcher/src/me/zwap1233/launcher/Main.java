package me.zwap1233.launcher;

import me.zwap1233.launcher.Filesystem.FileSystem;
import me.zwap1233.launcher.Users.UserMannager;

public class Main {
	
	private UserMannager um;
	
	/**
	 * {@code private Main()}</br></br>
	 * 
	 * Startup the launcher</br></br>
	 * 
	 * @author zwap1233
	 * @version Alpha 0.1
	 */
	private Main(){
		new FileSystem();
		um = new UserMannager();
		
		um.login("woutereldar@gmail.com");
		
		um.destroy();
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
