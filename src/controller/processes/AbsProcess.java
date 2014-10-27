package controller.processes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.SwingWorker;

public abstract class AbsProcess extends SwingWorker<Integer, String> {

	protected ProcessBuilder _pb;
	protected Process _process = null;
	protected InputStream _stdout;
	protected BufferedReader _stdoutBuffered;
	protected int _progress = 0;
	protected String _output = "";
	protected String _cmd;
	protected int _status;

	abstract public void command(String cmd);

	public void destroy() {
		/**
		 * The abstract framework on which processes are built off. 
		 * @deprecated this is replaced by AbstractProcess
		 */

		try {
			_process.getInputStream().close();
			_process.getOutputStream().close();
			_process.getErrorStream().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_process.destroy();
	}

	public String getOutput() {
		return _output;
	}

	public void setCommand(String cmd) {
		_cmd = cmd;
	}

	@Override
	protected Integer doInBackground() throws Exception {
		// Start
		_status = 1; // By default set the exit status to an error (this will be
						// changed by the process)
		command(_cmd);
		// System.out.println("Exit Status: "+_status);
		return _status;
	}

	@Override
	protected void done() {
		doDone();
	}

	abstract protected void doDone();

	public int getExitStatus() {
		return _status;
	}
}
