package me.zwap1233.launcher.Profile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import me.zwap1233.launcher.Filesystem.FileSystem;

public class ProfileImage {
	
	private Profile profile;
	
	private File imagefile;
	
	private BufferedImage skinbuf;
	
	private BufferedImage previewbuf;
	private Graphics2D g;
	
	/**
	 * {@code public ProfileImage(Profile profile)}<br><br> 
	 * 
	 * This class downloads and stores the skin of the specified profile.<br><br>
	 * 
	 * @param profile - The profile where the skin should be downloaded from
	 */
	public ProfileImage(Profile profile){
		this.profile = profile;
		
		imagefile = new File(FileSystem.basefile, "\\profiles\\" + profile.getUsername() + "\\" + profile.getName() + ".png");
		imagefile.getParentFile().mkdirs();
		
		try {
			loadSkinImage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * {@code private void loadSkinImage() throws IOException}<br><br>
	 * 
	 * load the skin image and store it.<br><br>
	 * 
	 * @throws IOException
	 */
	private void loadSkinImage() throws IOException{
		skinbuf = ImageIO.read(new URL(profile.getSkinUrl()));
		
		previewbuf = new BufferedImage(16, 32, BufferedImage.TYPE_4BYTE_ABGR);
		g = previewbuf.createGraphics();
		
		g.drawImage(skinbuf.getSubimage(8, 8, 8, 8), null, 4, 0);
		g.drawImage(skinbuf.getSubimage(20, 20, 8, 12), null, 4, 8);
		g.drawImage(skinbuf.getSubimage(44, 20, 4, 12), null, 0, 8);
		g.drawImage(skinbuf.getSubimage(44, 20, 4, 12), null, 12, 8);
		g.drawImage(skinbuf.getSubimage(4, 20, 4, 12), null, 4, 20);
		g.drawImage(skinbuf.getSubimage(4, 20, 4, 12), null, 8, 20);
		
		ImageIO.write(previewbuf, "png", imagefile);
	}
	
	/**
	 * {@code public BufferedImage getModifiedSkin()}<br><br>
	 * 
	 * Get the modified skin image.<br><br>
	 * 
	 * @return a BufferedImage of the modified skin.
	 */
	public BufferedImage getModifiedSkin(){
		return previewbuf;
	}
}
