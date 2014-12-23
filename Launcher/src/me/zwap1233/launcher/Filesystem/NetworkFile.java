package me.zwap1233.launcher.Filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkFile {
	
	private URL url;
	private File file;
	
	private HttpURLConnection con;
	
	private InputStream in;
	private OutputStream out;
	
	private boolean succeed = false;
	
	public NetworkFile(String url, String path){
		try {
			this.url = new URL(url);
			this.file = new File(FileSystem.basefile, path);
			
			if(!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
				
				con = (HttpURLConnection) this.url.openConnection();
				con.setDoInput(true);
				
				download();
				
				con.disconnect();
				
				succeed = true;
			} else {
				succeed = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public NetworkFile(String url, File file){
		try {
			this.url = new URL(url);
			this.file = file;
			
			if(!this.file.exists()){
				this.file.getParentFile().mkdirs();
				this.file.createNewFile();
				
				con = (HttpURLConnection) this.url.openConnection();
				con.setDoInput(true);
				
				download();
				
				con.disconnect();
				
				succeed = true;
			} else {
				succeed = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public NetworkFile(String url, String path, boolean override){
		try {
			this.url = new URL(url);
			this.file = new File(FileSystem.basefile, path);
			
			if((file.exists() && override) || !file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
				
				con = (HttpURLConnection) this.url.openConnection();
				con.setDoInput(true);
				
				download();
				
				succeed = true;
			} else {
				succeed = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		con.disconnect();
	}
	
	public NetworkFile(String url, File file, boolean override){
		try {
			this.url = new URL(url);
			this.file = file;
			
			if((this.file.exists() && override) || !this.file.exists()){
				this.file.getParentFile().mkdirs();
				this.file.createNewFile();
				
				con = (HttpURLConnection) this.url.openConnection();
				con.setDoInput(true);
				
				download();
				
				succeed = true;
			} else {
				succeed = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		con.disconnect();
	}
	
	private void download() throws IOException{
		in = con.getInputStream();
		out = new FileOutputStream(file);
		
		byte[] buffer = new byte[1024];
		int bytesread;
		while((bytesread = in.read(buffer)) != -1){
			out.write(buffer, 0, bytesread);
		}
		
		out.flush();
		out.close();
		in.close();
	}
	
	public boolean getSucceed(){
		return succeed;
	}
	
	public File getFile(){
		return file;
	}
}
