package gui;

import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

/**
 * ExtractProcess for cutting audio files
 * @author Patrick Poole
 *
 */
public class ExtractProcess extends SwingWorker<Void, Void> {

	private ProcessBuilder _eBuilder;
	private Process _process;
	private File _inputFile;
	private String _outputName;
	private String _startTime;
	private String _duration;
	private JPanel _panel;
	private int _exitStatus;

	private static final String AVCONV = new String("avconv -i ");

	public ExtractProcess(File inputFile, String outputName, String startTime, String duration, JPanel panel){
		_eBuilder = new ProcessBuilder();
		_inputFile = inputFile;
		_outputName = outputName+".mp3";
		_startTime = startTime;
		_duration = duration;
		_panel = panel;
		_exitStatus = -1;
	}

	/**
	 * Extract method for extracting audio from mp3 file
	 */
	private void extract(){
			_eBuilder = new ProcessBuilder("/bin/bash", "-c", makeCommand());
			try {
				_eBuilder.redirectErrorStream(true);
				_process = _eBuilder.start();
				_exitStatus = _process.waitFor();
			} catch (IOException e){
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	//create avconv command from inputs
	private String makeCommand(){
		return AVCONV+_inputFile.getAbsolutePath()
				+" -ss " +_startTime+ " -t " + _duration + " "+_outputName;
	}

	@Override
	protected Void doInBackground() throws Exception {
		extract();
		return null;
	}
	
	protected void done(){
		if (_exitStatus != 0){
			JOptionPane.showMessageDialog(_panel, "An error occured!");
		}else{
			JOptionPane.showMessageDialog(_panel, "Extraction Complete!");
		}
	}

}
