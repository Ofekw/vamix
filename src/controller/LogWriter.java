package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogWriter {
	private File _file;
	private int _lineCount;

	public LogWriter(File file){
		setLogFile(file);
		calcLineCount();

	}
	/**
	 * Calculates number of lines in log file in order to display correct entry number
	 */
	private void calcLineCount() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(_file));
			_lineCount = 0;
			while (reader.readLine() != null) _lineCount++;
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	/**
	 * Writes to the log with formating, separated by a "," for the table.
	 */
	public  void write(String operation){
		Date date = new Date( );
		SimpleDateFormat ft = 
				new SimpleDateFormat ("dd-MMM-yy hh:mm:ss");
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(_file, true)))) {
			_lineCount++;
			out.println(_lineCount+","+operation+","+ft.format(date));
		}catch (IOException e) {		
		}
	}
	public  void setLogFile(File file){
		_file = file;
	}
	
	public int getLineCount(){
		return _lineCount;
	}
	
}
