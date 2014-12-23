package me.zwap1233.launcher.Filesystem.Download.Game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import me.zwap1233.launcher.Filesystem.FileSystem;
import me.zwap1233.launcher.Filesystem.NetworkFile;

public class Minecraft {
	
	private String id;
	
	private File jar;
	private NetworkFile netjar;
	
	private boolean downloaded = false;
	
	private ProcessBuilder processbuilder;
	
	public Minecraft(String id){
		this.id = id;
		
		if(downloadMinecraftJar()){
			downloaded = true;
		} else {
			downloaded = false;
		}
	}
	
	private boolean downloadMinecraftJar(){
		jar = new File(FileSystem.basefile, "\\versions\\" + id + "\\" + id + ".jar");
		
		netjar = new NetworkFile("http://s3.amazonaws.com/Minecraft.Download/versions/" + id + "/" + id + ".jar", jar);
		if(netjar.getSucceed()){
			jar = netjar.getFile();
			return true;
		}
		return false;
	}
	
	public boolean isDownloaded(){
		return downloaded;
	}
	
	public String getPath(){
		return jar.getPath();
	}
	
	public void launchMinecraft(ArrayList<String> cmdline) throws IOException{
		System.out.println(cmdline);
		
		processbuilder = new ProcessBuilder();
		processbuilder.command(cmdline);
		processbuilder.inheritIO();
		
		processbuilder.start();
		
		System.out.println("launched");
	}
}
