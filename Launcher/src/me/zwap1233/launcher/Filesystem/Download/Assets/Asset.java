package me.zwap1233.launcher.Filesystem.Download.Assets;

import me.zwap1233.launcher.Filesystem.NetworkFile;

import org.json.JSONObject;

public class Asset {
	
	private final JSONObject asset;
	
	private String hash;
	private String index;
	private char[] indexchar;
	
	private int size;
	
	private NetworkFile file;
	
	public Asset(JSONObject asset){
		this.asset = asset;
		
		gatherData();
		download();
	}
	
	private void gatherData(){
		hash = asset.getString("hash");
		
		indexchar = new char[2];
		indexchar[0] = hash.charAt(0);
		indexchar[1] = hash.charAt(1);
		
		index = String.copyValueOf(indexchar);
		
		size = asset.getInt("size");
	}
	
	private boolean download(){
		file = new NetworkFile("http://resources.download.minecraft.net/" + index + "/" + hash, "\\assets\\objects\\" + index + "\\" + hash);
		if(file.getSucceed()){
			if(file.getFile().length() == size){
				return true;
			}
		}
		return false;
	}
}
