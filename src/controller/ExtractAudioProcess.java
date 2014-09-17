package controller;


import gui.AudioTab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class ExtractAudioProcess extends AbsProcess {
	
	private AudioTab _tab;

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
		try {
			while ((line = stdoutBuffered.readLine()) != null) {
//				 System.out.println(line);
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
		_tab.enableButtons();
		_tab.progressBarFinished();
	} else if (this.getExitStatus() > 0) {
						JOptionPane
								.showMessageDialog(_tab,"Something went wrong with the extract. Please check input media file",
										"Extract Error", JOptionPane.ERROR_MESSAGE);
	}

}
	
	public void setTab(AudioTab tab){
		this._tab = tab;
	}

}