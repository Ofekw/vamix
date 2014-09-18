package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
/**
 * 
 * @author patrick
 *
 * @param <R>Return Value
 * @param <M>Mid term Result
 */
public abstract class testAbPro{

	protected ProcessBuilder _pb;
	protected Process _process = null;
	protected InputStream _stdout;
	protected BufferedReader _stdoutBuffered;
	protected int _status;

	private ProcessWorker processWorker;

	private class ProcessWorker extends  SwingWorker<Integer, String>{

		private String _cmd;
		
		private ProcessWorker(String cmd){
			_cmd = cmd;
		}

		@Override
		protected Integer doInBackground() throws Exception {
			return runProcess(_cmd);
		}

		protected void done(){
			doDone();
		}

		@Override
		protected void process(List<String> chunks) {
			for (String line : chunks) {
				doProcess(line);
			}
		}

		protected void getPublish(String line){
			publish(line);
		}
	}

	public void destroy() {
		/**
		 * The abstract framework on which processes are built off.
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

	/**
	 * override to implement done method in subclass
	 */
	protected void doDone(){}

	
	/**
	 * Override to process command output in subclass if wanted
	 * @param line
	 */
	protected void doProcess(String line){}

	/**
	 * Runs the given bash command
	 * @param cmd: bash command to be run
	 * @return: commands exit status or -1 if error occurred
	 */
	private Integer runProcess(String cmd){
		_pb = new ProcessBuilder("/bin/bash", "-c", cmd);
		_process = null;
		_pb.redirectErrorStream(true);
		try {
			_process = _pb.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		InputStream stdout = _process.getInputStream();
		BufferedReader stdoutBuffered = new BufferedReader(
				new InputStreamReader(stdout));
		String line = null;
		try {
			while ((line = stdoutBuffered.readLine()) != null) {
				processWorker.getPublish(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			_status = _process.waitFor();
			destroy();
			return _status;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		destroy();
		return -1;
	}

	/**
	 * Call to start process
	 */
	public void execute(){
		processWorker.execute();
	}
	
	/**
	 * Call to cancel process
	 */
	public void cancel(){
		processWorker.cancel(true);
	}
	
	public int get(){
		try {
			return processWorker.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return -1;
	}

	protected void setCommand(String cmd){
		processWorker = new ProcessWorker(cmd);
	}
}
