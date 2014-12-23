package me.zwap1233.launcher.Filesystem.Download.Libraries;

import java.io.File;
import java.util.HashSet;

import me.zwap1233.launcher.Filesystem.FileSystem;

import org.json.JSONArray;

public class Libraries {
	
	private HashSet<String> pathtolibs = new HashSet<String>();
	
	private HashSet<Library> natives = new HashSet<Library>();
	
	private JSONArray libs;
	private String id;
	
	private File nativesfolder;
	
	public Libraries(JSONArray libs, String id){
		FileSystem.getInstance().createLibrariesDir();
		
		this.libs = libs;
		this.id = id;
		
		Library lib;
		for(int i = 0; i < libs.length(); i++){
			lib = new Library(libs.getJSONObject(i));
			if(lib.isAllowed()){
				pathtolibs.add(lib.getPath()); 
				if(lib.isNative()){
					natives.add(lib);
				}
			}
		}
		
		nativesfolder = new File(FileSystem.getInstance().getVersions(), "\\" + id + "\\natives-" + System.nanoTime() + "\\");
		nativesfolder.mkdirs();
		
		for(Library library:natives){
			new NativeLibrary(nativesfolder, this, library);
		}
	}
	
	public HashSet<String> getPathToLibs(){
		return pathtolibs;
	}
	
	public File getNativesFolder(){
		return nativesfolder;
	}
}
