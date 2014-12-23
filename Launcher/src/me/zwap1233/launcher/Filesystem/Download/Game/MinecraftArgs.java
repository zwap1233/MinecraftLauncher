package me.zwap1233.launcher.Filesystem.Download.Game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

import org.json.JSONArray;

import me.zwap1233.launcher.Filesystem.FileSystem;
import me.zwap1233.launcher.Filesystem.Download.Libraries.Libraries;

public class MinecraftArgs {
	
	private final String base;
	
	private final String classpathbase = "-cp";
	private HashSet<String> libs = new HashSet<String>();
	private String classpathline;
	
	private final String nativesbase = "-Djava.library.path=";
	private final String nativesfolder;
	private String nativesline;
	
	private String entrypoint;
	
	private final UUID uuid;
	
	private String[] baseargs;
	private ArrayList<String> baseargsline = new ArrayList<String>();
	
	private final String user;
	private final String version;
	private final String assetsindex;
	private final String profileid;
	private final String accesstoken;
	private final JSONArray properties;
	private final String usertype = "mojang";
	
	private ArrayList<String> cmdline = new ArrayList<String>();
	
	public MinecraftArgs(UUID uuid, Libraries libraries, String entrypoint, String baseargs, String user, String version, String assetsindex, String profileid, String accesstoken, JSONArray properties){
		this.uuid = uuid;
		
		base = System.getProperty("java.home") + "\\bin\\javaw.exe";
		
		this.entrypoint = entrypoint;
		
		this.user = user;
		this.version = version;
		this.assetsindex = assetsindex;
		this.profileid = profileid;
		this.accesstoken = accesstoken;
		this.properties = properties;
		
		this.baseargs = disectArgs(baseargs);
		
		libs = libraries.getPathToLibs();
		
		nativesfolder = libraries.getNativesFolder().getPath();
	}
	
	private String[] disectArgs(String args){
		return args.split(" ");
	}
	
	public void addLibrary(String path){
		libs.add(path);
	}
	
	public ArrayList<String> getCommandLine(UUID uuid){
		if(this.uuid.equals(uuid) && entrypoint != null){
			generateCommandLine();
			return cmdline;
		}
		return null;
	}
	
	private void generateCommandLine(){
		generateArgsLine();
		generateClasspathLine();
		generateNativesLine();
		
		cmdline.add(base);
		cmdline.add(classpathbase);
		cmdline.add(classpathline);
		cmdline.add(nativesline);
		cmdline.add(entrypoint);
		cmdline.addAll(baseargsline);
	}
	
	private void generateArgsLine(){
		for(int i = 0; i < baseargs.length; i++){
			if(baseargs[i].equals("--username")){
				i++;
				baseargs[i] = user;
			} else if(baseargs[i].equals("--version")){
				i++;
				baseargs[i] = version;
			} else if(baseargs[i].equals("--gameDir")){
				i++;
				baseargs[i] = FileSystem.basefile.getPath();
			} else if(baseargs[i].equals("--assetsDir")){
				i++;
				baseargs[i] = FileSystem.getInstance().getAssets().getPath();
			} else if(baseargs[i].equals("--assetIndex")){
				i++;
				baseargs[i] = assetsindex;
			} else if(baseargs[i].equals("--uuid")){
				i++;
				baseargs[i] = profileid;
			} else if(baseargs[i].equals("--accessToken")){
				i++;
				baseargs[i] = accesstoken;
			} else if(baseargs[i].equals("--userProperties")){
				i++;
				baseargs[i] = properties.toString();
			} else if(baseargs[i].equals("--userType")){
				i++;
				baseargs[i] = usertype;
			}
		}
		
		for(int i = 0; i < baseargs.length; i++){
			baseargsline.add(baseargs[i]);
		}
	}
	
	private void generateClasspathLine(){
		classpathline = "";
		StringBuilder str = new StringBuilder();
		
		Iterator<String> it = libs.iterator();
		while(it.hasNext()){
			str.append(it.next());
			if(it.hasNext()){
				str.append(";");
			}
		}
		
		classpathline = classpathline + str.toString();
	}
	
	private void generateNativesLine(){
		nativesline = nativesbase + nativesfolder;
	}
}
