package me.zwap1233.launcher.Filesystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

public class FileJson {
	
	private File file;
	
	private JSONObject obj;
	
	private BufferedReader in;
	private StringBuilder str;
	
	private BufferedWriter out;
	
	private boolean succeed = false;
	
	public FileJson(String path){
		file = new File(FileSystem.basefile, path);
		
		if(file.exists()){
			succeed = true;
			try {
				obj = readFile();
			} catch (IOException e) {
				e.printStackTrace();
				succeed = false;
			}
		} else {
			succeed = false;
		}
	}
	
	public FileJson(String path, JSONObject obj){
		file = new File(FileSystem.basefile, path);
		
		if(file.exists()){
			succeed = true;
			try {
				obj = readFile();
			} catch (IOException e) {
				e.printStackTrace();
				succeed = false;
			}
		} else {
			succeed = true;
			this.obj = obj;
			
			file.getParentFile().mkdirs();
			
			try {
				writeFile();
			} catch (IOException e) {
				e.printStackTrace();
				succeed = false;
			}
		}
	}
	
	public FileJson(String path, JSONObject obj, boolean override){
		file = new File(FileSystem.basefile, path);
		
		if(file.exists() && !override){
			succeed = true;
			try {
				obj = readFile();
			} catch (IOException e) {
				e.printStackTrace();
				succeed = false;
			}
		} else {
			succeed = true;
			this.obj = obj;
			
			file.getParentFile().mkdirs();
			
			try {
				writeFile();
			} catch (IOException e) {
				e.printStackTrace();
				succeed = false;
			}
		}
	}
	
	private void writeFile() throws IOException{
		out = new BufferedWriter(new FileWriter(file));
		
		out.write(obj.toString(4));
		
		out.flush();
		out.close();
	}
	
	private JSONObject readFile() throws IOException{
		in = new BufferedReader(new FileReader(file));
		str = new StringBuilder();
		
		while(in.ready()){
			str.append((char)in.read());
		}
		
		JSONObject obj = new JSONObject(str.toString());
		
		return obj;
	}
	
	public boolean getSucceed(){
		return succeed;
	}
	
	public JSONObject getJSONObject(){
		return obj;
	}
}
