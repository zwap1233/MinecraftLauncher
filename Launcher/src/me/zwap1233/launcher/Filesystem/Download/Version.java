package me.zwap1233.launcher.Filesystem.Download;

import java.io.File;
import java.io.IOException;

import org.json.JSONObject;

import me.zwap1233.launcher.Filesystem.FileSystem;
import me.zwap1233.launcher.Filesystem.NetworkJson;
import me.zwap1233.launcher.Filesystem.Download.Assets.Assets;
import me.zwap1233.launcher.Filesystem.Download.Game.Minecraft;
import me.zwap1233.launcher.Filesystem.Download.Libraries.Libraries;

public class Version {
	
	private String id;
	
	private JSONObject version;
	
	private Minecraft minecraft;
	private Libraries libraries;
	private Assets assets;
	
	private File versionfile;
	
	public Version(String id){
		this.id = id;
		
		FileSystem.getInstance().createVersionsDir();
		
		try {
			loadVersionJson();
			
			minecraft = new Minecraft(id);
			libraries = new Libraries(version.getJSONArray("libraries"), id);
			assets = new Assets(version.getString("assets"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadVersionJson() throws IOException{
		versionfile = new File(FileSystem.basefile, "\\versions\\" + id + "\\" + id + ".json");
		versionfile.getParentFile().mkdirs();
		
		NetworkJson netfile = new NetworkJson("http://s3.amazonaws.com/Minecraft.Download/versions/" + id + "/" + id + ".json");
		if(netfile.getSucceed()){
			version = netfile.getJSONObject();
			netfile.writeToFile(versionfile, false);
		}
	}
	
	public Minecraft getMinecraft(){
		return minecraft;
	}
	
	public Libraries getLibraries(){
		return libraries;
	}
	
	public Assets getAssets(){
		return assets;
	}
	
	public String getMinecraftArgs(){
		return version.getString("minecraftArguments");
	}
	
	public String getAssetsIndex(){
		return version.getString("assets");
	}
	
	public String getEntryPoint(){
		return version.getString("mainClass");
	}
}
