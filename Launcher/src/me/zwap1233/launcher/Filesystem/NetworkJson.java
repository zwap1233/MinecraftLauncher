package me.zwap1233.launcher.Filesystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class NetworkJson {
	
	private URL url;
	private HttpURLConnection con;
	
	private BufferedReader in;
	private StringBuilder str;
	
	private File file;
	private BufferedWriter out;
	
	private JSONObject obj;
	
	private boolean succeed = false;
	
	public NetworkJson(String url){
		try {
			this.url = new URL(url);
			
			con = (HttpURLConnection) this.url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			
			download();
			
			con.disconnect();
			
			succeed = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void download() throws IOException{
		in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		str = new StringBuilder();
		
		int c;
		while((c = in.read()) != -1){
			str.append((char) c);
		}
		
		obj = new JSONObject(str.toString());
		
		in.close();
	}
	
	public boolean writeToFile(String path, boolean override) throws IOException{
		return writeToFile(new File(FileSystem.basefile, path), override);
	}
	
	public boolean writeToFile(File file, boolean override) throws IOException{
		this.file = file;
		if((this.file.exists() && override) || !this.file.exists()){
			this.file.getParentFile().mkdirs();
			this.file.createNewFile();
			
			out = new BufferedWriter(new FileWriter(this.file));
			
			out.write(obj.toString());
			
			out.flush();
			out.close();
			
			return true;
		} else {
			return false;
		}
	}
	
	public boolean getSucceed(){
		return succeed;
	}
	
	public JSONObject getJSONObject(){
		return obj;
	}
}
