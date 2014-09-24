package gui;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Contains helper generic methods
 * @author Patrick Poole
 *
 */
public class VamixProcess{

	private ProcessBuilder vBuilder;
	private Process process;
	private String file;
	private InputStream stdout;
	private BufferedReader stdoutBuffered;

	public VamixProcess(String inputFile){
		file = inputFile;
	}

	/**
	 * Check if file exists in the current directory
	 * @return True if file exists, false otherwise
	 */
	protected boolean findFile(){
		vBuilder = new ProcessBuilder("/bin/bash", "-c", "basename "+ file+";find $(basename "+file+")");
		try {
			process = vBuilder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered =
					new BufferedReader(new InputStreamReader(stdout));
			process.waitFor();
			if (stdoutBuffered.readLine().equals(stdoutBuffered.readLine())){
				return true;
			}else{
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			process.destroy();
			process = null;
		}
		return false;
	}

	/**
	 * Removes file from directory
	 * @return True if succesful, false otherwise
	 */
	protected boolean removeFile(){
		vBuilder = new ProcessBuilder("/bin/bash", "-c", "rm $(basename "+file+")");
		try {
			Process process = vBuilder.start();
			process.waitFor();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Checks the file type of the input file is an audio file
	 * @param filePath Input file to check
	 * @return True if audio file, false otherwise
	 */
	protected boolean checkFileType(String filePath){
		vBuilder = new ProcessBuilder("/bin/bash", "-c", "file "+filePath);
		vBuilder.redirectErrorStream(true);
		try {
			process = vBuilder.start();
			stdout = process.getInputStream();
			stdoutBuffered =
					new BufferedReader(new InputStreamReader(stdout));
			String line = new String();
			while((line = stdoutBuffered.readLine()) != null){
				if(line.contains("Audio")){
					return true;
				}else{
					return false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * returns the file's size
	 * @return file size
	 */
	protected int getFileSize(){
		return file.length();
	}

}
