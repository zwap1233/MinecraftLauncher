package me.zwap1233.launcher.Filesystem.Download.Libraries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import me.zwap1233.launcher.Filesystem.NetworkFile;

import org.json.JSONArray;
import org.json.JSONObject;

public class Library {
	
	private final JSONObject lib;
	
	private String os;
	private String arch;
	
	private String pack;
	private String name;
	private String version;
	
	private String natives;
	
	private JSONArray exclude;
	
	private JSONArray rules;
	
	private boolean downloaded = false;
	private boolean allowed = false;
	
	private boolean isnative = false;
	
	private File file;
	private File tempfile;
	
	private JarFile jar;
	
	private JarOutputStream out;
	private InputStream in;
	
	private Enumeration<JarEntry> entries;
	
	public Library(JSONObject lib){
		this.lib = lib;
		
		gatherData();
		
		if(analyzeRules()){
			download();
			
			try {
				extractFromJar();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			allowed = true;
		} else {
			downloaded = false;
			allowed = false;
		}
	}
	
	private boolean extractFromJar() throws IOException{
		if(file != null && exclude != null){
			jar = new JarFile(file);
			
			tempfile = new File(file.getPath() + ".tmp");
			
			out = new JarOutputStream(new FileOutputStream(tempfile));
			
			entries = jar.entries();
			
			JarEntry oldentry;
			JarEntry newentry;
			while(entries.hasMoreElements()){
				oldentry = entries.nextElement();
				
				if(!mustBeExcluded(oldentry.getName())){
					in = jar.getInputStream(oldentry);
					
					newentry = new JarEntry(oldentry.getName());
					out.putNextEntry(newentry);
					
					byte[] buffer = new byte[1024];
					int bytesread;
					while((bytesread = in.read(buffer)) != -1){
						out.write(buffer, 0, bytesread);
					}
					
					out.flush();
					out.closeEntry();
					in.close();
				}
			}
			out.close();
			jar.close();
			
			file.delete();
			tempfile.renameTo(file);
			tempfile = null;
			jar = null;
			
			return true;
		}
		return false;
	}
	
	private boolean mustBeExcluded(String name){
		if(exclude != null){
			for(int i = 0; i < exclude.length(); i++){
				if(name.startsWith(exclude.getString(i))){
					return true;
				}
			}
		}
		return false;
	}
	
	private void download(){
		String url = "https://libraries.minecraft.net/";
		url = url + pack.replace('.', '/') + "/";
		url = url + name + "/";
		url = url + version + "/";
		
		if(natives != null){
			url = url + name + "-" + version + "-" + natives + ".jar";
		} else {
			url = url + name + "-" + version + ".jar";
		}
		
		String path = "\\libraries\\" + pack.replace('.', '\\') + "\\";
		path = path + name + "\\";
		path = path + version + "\\";
		
		if(natives != null){
			path = path + name + "-" + version + "-" + natives + ".jar";
		} else {
			path = path + name + "-" + version + ".jar";
		}
		
		NetworkFile file = new NetworkFile(url, path);
		if(file.getSucceed()){
			downloaded = true;
			this.file = file.getFile();
		} else {
			downloaded = false;
			this.file = file.getFile();
		}
	}
	
	private void gatherData(){
		String ostmp = System.getProperty("os.name");
		if(ostmp.startsWith("Windows")){
			this.os = "windows";
		} else if(ostmp.startsWith("Linux")){
			this.os = "linux";
		} else if(ostmp.startsWith("Mac")){
			this.os = "osx";
		}
		
		String archtmp = System.getProperty("os.arch");
		if(archtmp.equals("x86")){
			this.arch = "32";
		} else if(archtmp.equals("x86_64")){
			this.arch = "64";
		} else if(archtmp.equals("amd64")){
			this.arch = "64";
		}
		
		String tmp = lib.getString("name");
		pack = tmp.split(":")[0];
		name = tmp.split(":")[1];
		version = tmp.split(":")[2];
		
		if(lib.has("natives")){
			isnative = true;
			if(lib.getJSONObject("natives").has(os)){
				natives = lib.getJSONObject("natives").getString(os);
				if(natives.endsWith("${arch}")){
					natives = natives.replace("${arch}", arch);
				}
			}
		} else {
			isnative = false;
		}
		
		if(lib.has("extract")){
			exclude = lib.getJSONObject("extract").getJSONArray("exclude");
		}
		
		if(lib.has("rules")){
			rules = lib.getJSONArray("rules");
		}
	}
	
	private boolean analyzeRules(){
		if(rules == null){
			return true;
		} else {
			JSONObject obj;
			boolean tmp = true;
			
			for(int i = 0; i < rules.length(); i++){
				obj = rules.getJSONObject(i);
				if(obj.has("os")){
					if(obj.getJSONObject("os").getString("name") == os){
						if(obj.getString("action") == "allow"){
							tmp = true;
							break;
						} else if(obj.getString("action") == "disallow"){
							tmp = false;
							break;
						}
					}
				} else {
					if(obj.getString("action") == "allow"){
						tmp = true;
					} else if(obj.getString("action") == "disallow"){
						tmp = false;
					}
				}
			}
			
			if(tmp){
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean isAllowed(){
		return allowed;
	}
	
	public boolean isDownloaded(){
		return downloaded;
	}
	
	public boolean isNative(){
		return isnative;
	}
	
	public String getPath(){
		return file.getPath();
	}
}
