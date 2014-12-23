package me.zwap1233.launcher.Filesystem.Download.Libraries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class NativeLibrary {
	
	private File nativesfolder;
	private Library lib;
	
	private Libraries libraries;
	
	private JarFile jar;
	
	private Enumeration<JarEntry> entries;
	
	private JarEntry entry;
	private File file;
	
	private InputStream in;
	private FileOutputStream out;
	
	public NativeLibrary(File nativesfolder, Libraries libraries, Library lib){
		this.nativesfolder = nativesfolder;
		this.lib = lib;
		
		this.libraries = libraries;
		
		try {
			extract();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void extract() throws IOException{
		jar = new JarFile(lib.getPath());
		
		entries = jar.entries();
		
		while(entries.hasMoreElements()){
			entry = entries.nextElement();
			in = jar.getInputStream(entry);
			
			file = new File(nativesfolder, entry.getName());
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
	}
}
