package gui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import controller.CheckFile;
import controller.ReplaceAudioProcess;
import controller.ShellProcess;
import controller.testAbPro;
import controller.testExtractAudio;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import org.omg.CORBA._PolicyStub;

import net.miginfocom.swing.MigLayout;

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
	private JButton _inputVideoSelect;
	private JButton _inputAudioSelect;
	private Box horizontalBox_1;
	private Box horizontalBox_2;
	private Component rigidArea;
	private Component rigidArea_1;
	private Component rigidArea_2;
	private Component rigidArea_3;
	private Component rigidArea_4;
	private Component rigidArea_5;
	private Component rigidArea_6;
	
	private JLabel _startLabel;
	private JSpinner _startHours;
	private JSpinner _startMinutes;
	private JSpinner _startSeconds;

	private JLabel _durationLabel;
	private JSpinner _durationHours;
	private JSpinner _durationMinutes;
	private JSpinner _durationSeconds;



	public AudioTab(VideoPanel panel){
		super(panel);
	}

	@Override
	protected void initialise() {
		this.setLayout(new MigLayout("", "[500px, grow][500px, grow]", "[300px, grow][200px,grow]"));
		
		JPanel progressPanel = new JPanel(new MigLayout());
		JPanel rightSide = new JPanel(new MigLayout());
		this.setPreferredSize(new Dimension(1000, 130));
		JPanel leftSide = new JPanel(new MigLayout());
		
		
		leftSide.setPreferredSize(new Dimension(500, 200));
		rightSide.setPreferredSize(new Dimension(500, 200));
		
		
		leftSide.setBorder(BorderFactory.createBevelBorder(1));
		rightSide.setBorder(BorderFactory.createBevelBorder(1));
		progressPanel.setBorder(BorderFactory.createBevelBorder(1));
		
		_startLabel = new JLabel("Enter start time (HH:MM:SS)");
		_startHours = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
		_startMinutes = new JSpinner(new SpinnerNumberModel(0, 0, 60, 1));
		_startSeconds = new JSpinner(new SpinnerNumberModel(0, 0, 60, 1));

		_durationLabel = new JLabel("Enter duration (HH:MM:SS)");
		_durationHours = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
		_durationMinutes = new JSpinner(new SpinnerNumberModel(0, 0, 60, 1));
		_durationSeconds = new JSpinner(new SpinnerNumberModel(0, 0, 60, 1));
		
		_extractAudio = new JButton("Extract Audio");
		_extractAudio.setEnabled(false);
		
		_cancel = new JButton("Cancel");
//		horizontalBox.add(_cancel);
		_cancel.setEnabled(false);
		
		_extractProgressBar = new JProgressBar();
		_extractProgressBar.setPreferredSize(new Dimension(300, 10));
//		horizontalBox.add(_extractProgressBar);
		_extractProgressBar.setValue(0);
		
		leftSide.add(_startLabel);
		leftSide.add(_startHours, "split 3, wmax 40, w 100%");
		leftSide.add(_startMinutes, "wmax 40, w 100%");
		leftSide.add(_startSeconds, "wmax 40, wrap 5, w 100%");
		leftSide.add(_durationLabel);
		leftSide.add(_durationHours, "split 3, wmax 40, w 100%");
		leftSide.add(_durationMinutes, "wmax 40, w 100%");
		leftSide.add(_durationSeconds, "wmax 40, w 100%");
		
		rightSide.add(_extractAudio,"w 100%, h 100%,span,wrap");
		rightSide.add(_cancel, "w 100%, h 100%,span,wrap");
		
		progressPanel.add(_extractProgressBar, "w 100%");
		
		add(leftSide, "growx");
		add(rightSide, "growx, wrap");
		add(progressPanel, "w 100%, span 2");
		
//		this.add(progressPanel);
//		Box verticalBox = Box.createVerticalBox();
//		verticalBox.setPreferredSize(new Dimension(980,160));
//		add(verticalBox);
//
//		Component verticalStrut = Box.createVerticalStrut(20);
//		verticalBox.add(verticalStrut);
//
//		Box horizontalBox = Box.createHorizontalBox();
//		verticalBox.add(horizontalBox);




		_inputAudioSelect = new JButton("Select Audio");

//		horizontalBox.add(_extractAudio);
		
		rigidArea = Box.createRigidArea(new Dimension(20, 20));
		rigidArea.setPreferredSize(new Dimension(12, 20));
//		horizontalBox.add(rigidArea);


		
		rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
//		horizontalBox.add(rigidArea_2);




		horizontalBox_1 = Box.createHorizontalBox();
//		verticalBox.add(horizontalBox_1);

		_replace = new JButton("Replace Audio");
		_replace.setEnabled(false);
		horizontalBox_1.add(_replace);
		
		rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_1.setPreferredSize(new Dimension(12, 21));
		horizontalBox_1.add(rigidArea_1);


		_replaceProgressBar = new JProgressBar();
		horizontalBox_1.add(_replaceProgressBar);
		
		rigidArea_3 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_3.setPreferredSize(new Dimension(80, 20));
		horizontalBox_1.add(rigidArea_3);

		horizontalBox_2 = Box.createHorizontalBox();
//		verticalBox.add(horizontalBox_2);
		_inputVideo = new JTextField();
		_inputVideo.setMaximumSize(new Dimension(2147483647, 28));
		_inputVideo.setEditable(false);
		horizontalBox_2.add(_inputVideo);
		
		rigidArea_5 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_5.setPreferredSize(new Dimension(6, 20));
		horizontalBox_2.add(rigidArea_5);
		_inputVideoSelect = new JButton("Select Video");
		horizontalBox_2.add(_inputVideoSelect);
		
		_inputVideoSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				fileChooser(_inputVideo, true);
				if(!_inputAudio.getText().isEmpty()){
					enableReplaceButton();
				}
			}
		});
		
		rigidArea_4 = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox_2.add(rigidArea_4);

		_inputAudio = new JTextField();
		_inputAudio.setMaximumSize(new Dimension(2147483647, 28));
		_inputAudio.setEditable(false);
		horizontalBox_2.add(_inputAudio);
		
		rigidArea_6 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_6.setPreferredSize(new Dimension(6, 20));
		horizontalBox_2.add(rigidArea_6);
		_inputAudioSelect = new JButton("Select Audio");
		horizontalBox_2.add(_inputAudioSelect);
		
		//this.add(new ExtractPane());
		
		_extractAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * Audio extraction process
				 */
				/*
				 * Obtains a save directory from the user
				 */
				extractAudio();
			}
		});
		
		_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				process.cancel();
				if (process instanceof testExtractAudio){
				enableExtractButtons();
				_extractProgressBar.setValue(_extractProgressBar.getMaximum());
				JOptionPane.showMessageDialog(_cancel, "Extract cancelled!",
						"Cancelled!", JOptionPane.ERROR_MESSAGE);
				}else if (process instanceof ReplaceAudioProcess){
					_replaceProgressBar.setIndeterminate(false);
					JOptionPane.showMessageDialog(_cancel, "Replace cancelled!",
							"Cancelled!", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		_inputAudioSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				fileChooser(_inputAudio, false);
				if(!_inputVideo.getText().isEmpty()){
					enableReplaceButton();
				}
			}
		});
		

		_replace.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				replaceAudio();
			}
		});
	}
	
	/**
	 * Asks user for output file name and checks if it exists, asks to overwrite if file does exist
	 * Runs replace audio command once all inputs valid
	 */
	private void replaceAudio(){
		JFileChooser fileChooser = new JFileChooser() {
			@Override
			public void approveSelection() {

				File f = getSelectedFile();
				/*
				 * Makes sure the filename ends with extension .mp3 when checking for overwrite
				 */

				if (!f.getAbsolutePath()
						.endsWith(".mp4")) {
					f = new File(getSelectedFile()
							+ ".mp4");
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
		if (userSelection == JFileChooser.OPEN_DIALOG) {
			fileToSave = fileChooser.getSelectedFile();
			/*
			 * Makes sure the filename ends with extension .mp3
			 */

			if (!fileChooser.getSelectedFile().getAbsolutePath()
					.endsWith(".mp4")) {
				fileToSave = new File(fileChooser.getSelectedFile()
						+ ".mp4");
			}
			_newFileLoc=fileToSave.getAbsolutePath();
			
			process = new 
					ReplaceAudioProcess(_inputVideo.getText(), _inputAudio.getText(), 
							_newFileLoc, this);
			process.execute();
			_cancel.setEnabled(true);
			_replaceProgressBar.setIndeterminate(true);
			disableReplaceButton();
		}
		
	}

	public void enableExtractButtons() {
		_extractAudio.setEnabled(true);
	}
	
	private void enableReplaceButton(){
		_replace.setEnabled(true);
	}

	private void disableExtractButtons(){
		_extractAudio.setEnabled(false);
	}
	
	private void disableReplaceButton(){
		_replace.setEnabled(false);
	}

	/**
	 * Set the extract progress bar to finished
	 */
	public void extractFinished() {
		_extractProgressBar.setValue(_extractProgressBar.getMaximum());
		enableExtractButtons();
	}

	public void replaceFinished(){
		_replaceProgressBar.setIndeterminate(false);
		_replace.setEnabled(false);
		_inputAudio.setText("");
		_inputVideo.setText("");
	}
	/**
	 * Asks the user for output file name then runs the extract audio process
	 */
	
	private void extractAudio() {
		CheckFile check = new CheckFile(false);
		//check if checking for video file and video file has no audio
		if (!check.checkVideoHasAudio(_mediaLoc)){
			JOptionPane
			.showMessageDialog(this,"Video file contains no audio!",
					"Extract Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
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
			_extractProgressBar.setValue(0);
			process = new testExtractAudio(_mediaLoc,fileToSave.getAbsolutePath(), this);
			process.execute();
			_cancel.setEnabled(true);
			disableExtractButtons();
		}
	}

	/**
	 * Set max value of extract progress bar
	 * @param max: max value
	 */
	public void setExtractMax(int max){
		_extractProgressBar.setMaximum(max);
	}

	/**
	 * Set the progress bar value
	 * @param value: Value to be set
	 */
	public void setExtractValue(int value){
		_extractProgressBar.setValue(value);
	}

	/**
	 * Checks that the file exists
	 * @param f: file to be checked
	 * @return: True if it exists, false otherwise
	 */
	private boolean fileExists(File f) {
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Displays an invalid file path message
	 */
	private void filePathInvalid() {
		JOptionPane.showMessageDialog(this, "File path is invalid",
				"Location Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Allows the user to select an input file and verifies it is a valid file
	 * @param field: Text field to be set if file is valid
	 * @param checkVid: True if checking file is video, false if checking file is audio
	 */
	private void fileChooser(JTextField field, boolean checkVid){

		JFileChooser fileChooser = new JFileChooser();
		fileChooser
		.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			if ((selectedFile == null || selectedFile.getAbsolutePath().isEmpty()) || !fileExists(selectedFile)) {
				filePathInvalid();
			}else{
				CheckFile check = new CheckFile(checkVid);
				if (!check.checkFileType(selectedFile.getAbsolutePath())){
					JOptionPane
					.showMessageDialog(field,"Invalid Video File",
							"Extract Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			field.setText(selectedFile.getAbsolutePath());
		}
	}

	public void setMediaLoc(String _mediaLoc) {
		this._mediaLoc = _mediaLoc;
	}
}
