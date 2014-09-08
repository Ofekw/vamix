package controller;

import gui.Download;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DownloadProcess extends AbsProcess{

	protected Download _download;

	public DownloadProcess(){

	}

	public  void initialize() {


	}


	protected  void command(String cmd) {
		/**
		 * Initialize the bash processes
		 */
		_pb = new ProcessBuilder("/bin/bash", "-c", cmd);
		_process = null;
		_pb.redirectErrorStream(true);
		try {
			_process  = _pb.start();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		InputStream stdout = _process.getInputStream();
		BufferedReader stdoutBuffered =new BufferedReader(new InputStreamReader(stdout));

		String line = null;
		_progress = 0;
		try {
			while ((line = stdoutBuffered.readLine()) != null ) {
				for (int i = 0; i<line.length();i++){
					if (line.charAt(i) == '%'){
						_output=line.substring(i-2, i).trim();
						_progress = Integer.parseInt(_output);
						if (_progress == 0){
							_progress = 100;
						}
						setProgress(_progress);
						//System.out.println(_progress);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				_status = _process.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			destroy();
		}

	}

	public void destroy() {
		/**
		 * Tries to kill the process as gently as possbile.
		 */		
		try {
			_process.getInputStream().close();
			_process.getOutputStream().close();
			_process.getErrorStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		_process.destroy();
	}


	public static boolean isInteger(String c) {
		/**
		 * Parsing string inputs to get the progress.
		 */
		try {
			Integer.parseInt(c);
		} catch(NumberFormatException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}


	public  String getOutput(){
		return _output;
	}

	public void setCommand(String cmd){
		_cmd =cmd;
	}

	@Override
	protected Integer doInBackground() throws Exception {
		// Start
		_status = 1;
		command(_cmd);
		//System.out.println("Exit Status: "+_status);
		return _status;
	}

	protected void doDone() {
		/**
		 * Gives informative information in accordance to exit values
		 */
		try{
		if(_status == 0){
			SingletonLogger logger =new SingletonLogger();
			 LogWriter log = logger.getInstance();
			 log.write("DOWNLOAD");
		}else if (_status == 8){
			this._download.getStatusLabel().setText("Status: URL address invalid");
		}else if ( _status == 4){
			this._download.getStatusLabel().setText("Status: Network failure");
		}else if (_status == 3){
			this._download.getStatusLabel().setText("Status: File I/O error");
		}
		}finally{
		_download.reset();
		}
	}

	public void setDownload(Download download){
		this._download=download;
		 
	}
	
	public int getExitStatus(){
		return _status;
	}





}
