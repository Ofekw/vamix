package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Checks if the type of the file is audio or video
 * @author patrick
 *
 */
public class CheckFile {

	private boolean checkVideo = false;

	/**
	 * 
	 * @param filePath: path to file
	 * @param checkVid: true to check if video, false to check audio
	 */
	public CheckFile(boolean checkVid){
		checkVideo = checkVid;
	}

	public boolean checkFileType(String file) {
		ProcessBuilder vBuilder = new ProcessBuilder("/bin/bash", "-c", "file "+file);
		vBuilder.redirectErrorStream(true);
		try {
			Process process = vBuilder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			String line = new String();
			while((line = stdoutBuffered.readLine()) != null){
				if (checkVideo){
					if(line.contains("Media") || line.contains("video") || line.contains("Matroska")){
						return true;
					}else{
						return false;
					}
				}else{
					if(line.contains("Audio")){
						return true;
					}else{
						return false;
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean checkVideoHasAudio(String file){
		ProcessBuilder vBuilder = new ProcessBuilder("/bin/bash", "-c", "avconv -i "+file);
		vBuilder.redirectErrorStream(true);
		try {
			Process process = vBuilder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			String line = new String();
			while((line = stdoutBuffered.readLine()) != null){
				if(line.contains("Audio")){
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}