package controller;
/*package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JProgressBar;

public class DownloadWithCancel extends DownloadProcess {
	public DownloadWithCancel(JProgressBar bar, String cmd) {

		// TODO Auto-generated constructor stub
	}

	public  Process _process = null;


	public String command(String cmd) {


		ProcessBuilder	pb = new ProcessBuilder("/bin/bash", "-c", cmd);
		pb.redirectErrorStream(true);
		try {
			_process  = pb.start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		InputStream stdout = _process.getInputStream();
		BufferedReader stdoutBuffered =new BufferedReader(new InputStreamReader(stdout));
		//		env = _pb.environment();

		String line = null;
		String _output ="";
		try {
			while ((line = stdoutBuffered.readLine()) != null ) {
				//				System.out.println(line);
				_output+=(line+" ");
				//				System.out.println(line +" SHELL");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return _output;

	}

	public void stop(){
		_process.destroy();
	}

}
 */