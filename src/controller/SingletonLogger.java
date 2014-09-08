package controller;

import java.io.File;
import java.io.IOException;

public class SingletonLogger {

	private static LogWriter _logger = null;
	/**
	 * Initializes the singleton log object for simplicity
	 */

	public LogWriter getInstance() {
		if (_logger == null){
			String loc = ShellProcess.command("echo ~/.vamix");
			loc = loc.trim();
			File file = new File(loc,"log");
			if (!file.exists()) {
				try {
					if (!file.isFile() && !file.createNewFile()){
					    throw new IOException("Error creating new log file: " + file.getAbsolutePath());
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			_logger = new LogWriter(file);
		}
		return _logger;
	}

}


