package me.zwap1233.launcher.Filesystem;

import java.io.File;

public class FileSystem {
	
	private static FileSystem instance;
	
	public static final File basefile = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\.launcher");
	
	private File assets;
	private File versions;
	private File libraries;
	private File profiles;
	
	public FileSystem(){
		instance = this;
		
		basefile.mkdir();
	}
	
	public static FileSystem getInstance(){
		return instance;
	}
	
	public void createAssetsDir(){
		assets = new File(basefile, "\\assets");
		assets.mkdir();
	}
	
	public void createVersionsDir(){
		versions = new File(basefile, "\\versions");
		versions.mkdir();
	}
	
	public void createLibrariesDir(){
		libraries = new File(basefile, "\\libraries");
		libraries.mkdir();
	}
	
	public void createProfilesDir(){
		profiles = new File(basefile, "\\profiles");
		profiles.mkdir();
	}

	public File getAssets() {
		return assets;
	}

	public File getVersions() {
		return versions;
	}

	public File getLibraries() {
		return libraries;
	}

	public File getProfiles() {
		return profiles;
	}
}
