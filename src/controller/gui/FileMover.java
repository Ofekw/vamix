package controller.gui;

import gui.MainGui;

import java.io.File;

import controller.processes.AbstractProcess;

/**
 * Creates .vamix folder and moves all neccesarry files to .vamix folder from within jar file
 * Author: Patrick Poole
 * Modified by Ofek
 */
public class FileMover extends AbstractProcess{
	
	private static final String[] moveableFiles = {".tempMedia", "bat", "css", "images", "js", "readme.html"} ;
	private File location;

	/**
	 * Unpacks the neccesary files to the vamix file
	 */
	public FileMover(){
		if (!MainGui.VAMIX.exists()){
			MainGui.VAMIX.mkdir();
		}
		String command = "";
		location = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());;;
		String fileDirectory = location.getAbsolutePath().toString();
		String baseDirectory = fileDirectory.substring(0,fileDirectory.lastIndexOf("/")+1);
		if (!location.getAbsoluteFile().toString().contains("bin")){
			for(String inputFile : moveableFiles ){
				//jar file must be name owit454.jar!!! for file moving to take place
				command+="jar xf "+location.getAbsolutePath().toString()+File.separator+"owit454.jar"+" "+inputFile+" && "
			+"mv "+location.getAbsolutePath().toString()+File.separator+inputFile+" "+MainGui.VAMIX+" && ";
			}
		}else{
			for (String inputFile : moveableFiles){
				if (inputFile.equals("readme.html")){
					command+="cp "+baseDirectory+"src/"+inputFile+" "+MainGui.VAMIX+" && ";
				}else{
					command+="cp -r "+baseDirectory+"src/"+inputFile+" "+MainGui.VAMIX+" && ";
				}
			}
		}
		//System.out.println(command.substring(0, command.length()-3));
		super.setCommand(command.substring(0, command.length()-3));
	}
}
