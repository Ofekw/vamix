package controller;

import gui.Extract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class ExtractProcess extends AbsProcess {
	protected Extract _extract;
	private int _option; // 0 = test file, 1 =extract file,

	public ExtractProcess(Extract extract, int option) {
		this._extract = extract;
		this._option = option;
	}

	protected void command(String cmd) {
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
		if (_option == 0) {
			_extract.extractSong();
		} else {
			if (this.getExitStatus() == 0) {
				SingletonLogger logger = new SingletonLogger();
				LogWriter log = logger.getInstance();
				log.write("EXTRACT");
				_extract.doneState();
			} else if (this.getExitStatus() != 1) {
				JOptionPane
						.showMessageDialog(
								_extract,
								"Something went wrong with the Extract. Please check files and input fields.",
								"Extract Error", JOptionPane.ERROR_MESSAGE);
				_extract.resetState();
			} else {
				_extract.resetState();
			}
		}
	}

}