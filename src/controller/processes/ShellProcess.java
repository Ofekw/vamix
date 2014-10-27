package controller.processes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class ShellProcess {

	public String cmd;
	public Map<String, String> env;
	public ProcessBuilder _pb;
	public Process _process = null;
	public InputStream _stdout;
	public BufferedReader _stdoutBuffered;
	private static int _progress = 0;
	private static String _output = "";

	public ShellProcess() {
		/**
		 * Initializes a static process for quick, non time consuming actions
		 */
	}

	public static String command(String cmd) {

		ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", cmd);
		Process process = null;
		pb.redirectErrorStream(true);
		try {
			process = pb.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		InputStream stdout = process.getInputStream();
		BufferedReader stdoutBuffered = new BufferedReader(
				new InputStreamReader(stdout));
		String line = null;
		String _output = "";
		try {
			while ((line = stdoutBuffered.readLine()) != null) {
				_output += (line + " ");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return _output;

	}

	public static int getProgress() {
		return _progress;
	}

	public static String getOutput() {
		return _output;
	}

}
