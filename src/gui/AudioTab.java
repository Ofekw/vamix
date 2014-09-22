package gui;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import controller.CheckFile;
import controller.ExtractAudioProcess;
import controller.ShellProcess;
import controller.testAbPro;
import controller.testExtractAudio;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class AudioTab extends Tab {
	/*
	 * This tab is used to extract the audio from the movie file, future implementation can include extracting specific time frame audio
	 */
	private JButton _extractAudio;
	protected String _newFileLoc;
	private String _mediaLoc;
	private JProgressBar _extractProgressBar;
	private JButton _cancel;
	private testAbPro process;
	private JButton _replace;
	private JProgressBar _replaceProgressBar;
	private JTextField _inputVideo;
	private JTextField _inputAudio;
	private JTextField _outputVideo;
	private JButton _inputVideoSelect;
	private JButton _inputAudioSelect;
	private JButton _outputSelect;

	public void setMediaLoc(String _mediaLoc) {
		this._mediaLoc = _mediaLoc;
	}

	public AudioTab(VideoPanel panel){
		super(panel);
	}

	@Override
	protected void initialise() {
		this.setPreferredSize(new Dimension(1000, 130));
		Box verticalBox = Box.createVerticalBox();
		add(verticalBox);

		Component verticalStrut = Box.createVerticalStrut(20);
		verticalBox.add(verticalStrut);

		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);

		_extractAudio = new JButton("Extract Audio");
		_extractAudio.setEnabled(false);

		_cancel = new JButton("Cancel");
		_cancel.setEnabled(false);
		_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				process.cancel();
				enableButtons();
				_extractProgressBar.setValue(_extractProgressBar.getMaximum());
			}
		});

		_extractAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * Audio extraction process
				 */
				/*
				 * Obtains a save directory from the user
				 */
				saveDialog();
			}
		});

		_replaceProgressBar = new JProgressBar();
		_replace = new JButton("Replace Audio");
		_inputVideo = new JTextField();
		_inputVideoSelect = new JButton("Select Video");
		_inputVideoSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser
				.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					if ((selectedFile == null || selectedFile.getAbsolutePath().isEmpty()) || !fileExists(selectedFile)) {
						filePathInvalid();
					}else{
						CheckFile check = new CheckFile(selectedFile.getAbsolutePath(), true);
						if (!check.checkFileType()){
							JOptionPane
							.showMessageDialog(_inputVideo,"Invalid Video File",
									"Extract Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					_inputVideo.setText(selectedFile.getAbsolutePath());
				}
			}
		});
		
		_inputAudio = new JTextField();
		_inputAudioSelect = new JButton("Select Audio");
		_outputVideo = new JTextField();
		_outputSelect = new JButton();

		horizontalBox.add(_extractAudio);

		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox.add(rigidArea);

		_extractProgressBar = new JProgressBar();
		_extractProgressBar.setPreferredSize(new Dimension(300, 10));
		horizontalBox.add(_extractProgressBar);
		_extractProgressBar.setValue(0);

		this.add(_cancel);
		this.add(_replace);
		this.add(_replaceProgressBar);
		this.add(_inputVideo);
		this.add(_inputVideoSelect);
		this.add(_inputAudio);
		this.add(_outputVideo);

	}
	private void extractAudio() {
		// TODO Auto-generated method stub
		_extractProgressBar.setValue(0);
		process = new testExtractAudio(_mediaLoc,_newFileLoc, this);
		process.execute();
		_cancel.setEnabled(true);

		//		ExtractAudioProcess process = new ExtractAudioProcess();
		//		process.setCommand("avconv -i "+_mediaLoc+" "+_newFileLoc);
		//		process.setTab(this);
		//		process.execute();
		disableButtons();

	}

	public void enableButtons() {
		_extractAudio.setEnabled(true);
		//		_progressBar.setIndeterminate(false);
	}

	private void disableButtons(){
		_extractAudio.setEnabled(false);
		//		_progressBar.setIndeterminate(true);
	}

	public void progressBarFinished() {
		_extractProgressBar.setValue(_extractProgressBar.getMaximum());
	}

	private void saveDialog() {
		JFileChooser fileChooser = new JFileChooser() {
			@Override
			public void approveSelection() {

				File f = getSelectedFile();
				/*
				 * Makes sure the filename ends with extension .mp3 when checking for overwrite
				 */

				if (!f.getAbsolutePath()
						.endsWith(".mp3")) {
					f = new File(getSelectedFile()
							+ ".mp3");
				}
				if (f.exists() && getDialogType() == SAVE_DIALOG) {
					int result = JOptionPane
							.showConfirmDialog(
									this,
									"The file exists, overwrite?",
									"Existing file",
									JOptionPane.YES_NO_OPTION);
					switch (result) {
					case JOptionPane.YES_OPTION:
						ShellProcess
						.command("rm -f " + f.getAbsolutePath());
						super.approveSelection();
						return;
					case JOptionPane.NO_OPTION:
						cancelSelection();
						return;
					case JOptionPane.CLOSED_OPTION:
						return;
					case JOptionPane.CANCEL_OPTION:
						cancelSelection();
						return;
					}
				}
				super.approveSelection();
			}
		};

		fileChooser.setDialogTitle("Specify where to save audio");

		int userSelection = fileChooser.showSaveDialog(this);
		File fileToSave = null;
		String startTime = null;
		String duration = null;
		if (userSelection == JFileChooser.OPEN_DIALOG) {
			fileToSave = fileChooser.getSelectedFile();
			/*
			 * Makes sure the filename ends with extension .mp3
			 */

			if (!fileChooser.getSelectedFile().getAbsolutePath()
					.endsWith(".mp3")) {
				fileToSave = new File(fileChooser.getSelectedFile()
						+ ".mp3");
			}
			_newFileLoc=fileToSave.getAbsolutePath();
			extractAudio();
		}
	}

	public void setProgressBarMax(int max){
		_extractProgressBar.setMaximum(max);
	}

	public void setProgressValue(int value){
		_extractProgressBar.setValue(value);
	}

	private boolean fileExists(File f) {
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}

	private void filePathInvalid() {
		JOptionPane.showMessageDialog(this, "File path is invalid",
				"Location Error", JOptionPane.ERROR_MESSAGE);
	}
}
