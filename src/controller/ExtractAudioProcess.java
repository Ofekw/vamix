package controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class ExtractAudioProcess extends AbsProcess {

	public void command(String cmd) {
		_pb = new ProcessBuilder("/bin/bash", "-c", cmd);
		_process = null;
		_pb.redirectErrorStream(true);
		try {
			_process = _pb.start();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		InputStream stdout = _process.getInputStream();
		BufferedReader stdoutBuffered = new BufferedReader(
				new InputStreamReader(stdout));
		String line = null;
		_progress = 0;
		try {
			while ((line = stdoutBuffered.readLine()) != null) {
				// System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				super._status = _process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			destroy();
		}

	}

	protected void doDone() {
	if (this.getExitStatus() == 0) {

	} else if (this.getExitStatus() != 1) {
		//				JOptionPane
		//						.showMessageDialog(
		//								_extract,
		//								"Something went wrong with the Extract. Please check files and input fields.",
		//								"Extract Error", JOptionPane.ERROR_MESSAGE);
	}

}

}