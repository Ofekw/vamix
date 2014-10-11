package controller;

import gui.MainGui;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Creates .vamix folder and moves all neccesarry files to .vamix folder from within jar file
 * Author: Patrick Poole
 */
public class FileMover extends AbstractProcess{
	
	private static final String[] moveableFiles = {".tempMedia", "bat", "css", "images", "js", "readme.html"} ;

	public FileMover(){
		if (!MainGui.VAMIX.exists()){
			MainGui.VAMIX.mkdir();
		}
		File location = null;
		String command = "";
		try {
			location = new File(MainGui.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		String fileDirectory = location.getAbsolutePath().toString();
		String baseDirectory = fileDirectory.substring(0,fileDirectory.lastIndexOf("/")+1);
		if (location.getAbsoluteFile().toString().contains(".jar")){
			for(String inputFile : moveableFiles ){
				command+="jar xf "+location.getAbsolutePath()+" "+inputFile+" && "
			+"mv "+baseDirectory+inputFile+" "+MainGui.VAMIX+" && ";
			}
		}else{
			for (String inputFile : moveableFiles){
				if (inputFile.equals("readme.html")){
					command+="cp "+baseDirectory+"src/vamix.BIN/"+inputFile+" "+MainGui.VAMIX+" && ";
				}else{
					command+="cp -r "+baseDirectory+"src/vamix.BIN/"+inputFile+" "+MainGui.VAMIX+" && ";
				}
			}
		}
		super.setCommand(command.substring(0, command.length()-3));
	}
}
