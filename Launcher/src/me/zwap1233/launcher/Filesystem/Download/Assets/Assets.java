package me.zwap1233.launcher.Filesystem.Download.Assets;

import java.io.IOException;

import org.json.JSONObject;

import me.zwap1233.launcher.Filesystem.FileSystem;
import me.zwap1233.launcher.Filesystem.NetworkJson;

public class Assets {
	
	private JSONObject index;
	private NetworkJson networkindex;
	
	public Assets(String id){
		FileSystem.getInstance().createAssetsDir();
		
		try{
			networkindex = new NetworkJson("https://s3.amazonaws.com/Minecraft.Download/indexes/" + id + ".json");
			if(networkindex.getSucceed()){
				index = networkindex.getJSONObject().getJSONObject("objects");
				networkindex.writeToFile("\\assets\\indexes\\" + id + ".json", true);
				
				iterateIndex();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void iterateIndex(){
		for(String key : index.keySet()){
			new Asset(index.getJSONObject(key));
		}
	}
}
