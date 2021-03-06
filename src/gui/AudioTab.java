package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import net.miginfocom.swing.MigLayout;
import controller.gui.CheckFile;
import controller.processes.AbstractProcess;
import controller.processes.ExtractAudioProcess;
import controller.processes.OverlayAudioProcess;
import controller.processes.ReplaceAudioProcess;
import controller.processes.SaveLoadState;
import controller.processes.ShellProcess;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

@SuppressWarnings("serial")
/**
 * Tab for audio manipulation
 * @author patrick
 *
 */
public class AudioTab extends Tab {
	/**
	 * This tab is used to extract the audio from the movie file, future implementation can include extracting specific time frame audio
	 */
	private JButton _extractAudio;
	protected String _newFileLoc;
	private String _mediaLoc;
	private JProgressBar _progressBar;
	private JButton _cancel;
	private AbstractProcess process;
	private JButton _replace;
	private JTextField _inputAudio;
	private JButton _inputAudioSelect;
	
	private VideoTab _tab;

	private SaveLoadState saveLoad;

	private JLabel _startLabel;
	private JSpinner _startHours;
	private JSpinner _startMinutes;
	private JSpinner _startSeconds;

	private JLabel _durationLabel;
	private JSpinner _durationHours;
	private JSpinner _durationMinutes;
	private JSpinner _durationSeconds;

	private JButton _removeAudio;
	private JButton _overLayAudio;
	
	private final String REMOVE = "REMOVE";
	private final String OVERLAY = "OVERLAY";
	private final String REPLACE = "REPLACE";
	private int[] mediaLength = new int[3];



	public AudioTab(VideoPanel panel, VideoTab tab){
		super(panel);
		_tab = tab;
	}

	@Override
	protected void initialise() {
		//Sets up the main gui components for the AudioTab
		this.setLayout(new MigLayout("", "[400px, grow][400px, grow]", "[300px, grow][200px,grow]"));

		JPanel progressPanel = new JPanel(new MigLayout());
		JPanel rightSide = new JPanel(new MigLayout());
		this.setPreferredSize(new Dimension(1000, 130));
		JPanel leftSide = new JPanel(new MigLayout());


		leftSide.setPreferredSize(new Dimension(500, 200));
		rightSide.setPreferredSize(new Dimension(500, 200));


		leftSide.setBorder(BorderFactory.createTitledBorder(""));
		rightSide.setBorder(BorderFactory.createTitledBorder(""));
		//sets the labels and buttons
		_startLabel = new JLabel("Enter start time (HH:MM:SS)");
		_startHours = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
		_startHours.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int value = (int)_durationHours.getValue();
				int range = mediaLength[0]-(int)_startHours.getValue();
				if(value>range){
				_durationHours.setModel(new SpinnerNumberModel(value-1, 0, (range), 1));
				}else{
					_durationHours.setModel(new SpinnerNumberModel(value, 0, (range), 1));
				}
			}
		});
		_startMinutes = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
		_startMinutes.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int value = (int)_durationMinutes.getValue();
				int range = mediaLength[1]-(int)_startMinutes.getValue();
				if(value>range){
				_durationMinutes.setModel(new SpinnerNumberModel(value-1, 0, (range), 1));
				}else{
					_durationMinutes.setModel(new SpinnerNumberModel(value, 0, (range), 1));
				}
			}
		});
		_startSeconds = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
		_startSeconds.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int value = (int)_durationSeconds.getValue();
				int range = mediaLength[2]-(int)_startSeconds.getValue();
				if(value>range){
				_durationSeconds.setModel(new SpinnerNumberModel(value-1, 0, (range), 1));
				}else{
					_durationSeconds.setModel(new SpinnerNumberModel(value, 0, (range), 1));
				}
			}
		});

		_durationLabel = new JLabel("Enter duration (HH:MM:SS)");
		_durationHours = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
		_durationMinutes = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
		_durationSeconds = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

		_extractAudio = new JButton("Extract Audio");
		_extractAudio.setEnabled(false);

		_cancel = new JButton("Cancel");
		_cancel.setEnabled(false);

		_progressBar = new JProgressBar();
		_progressBar.setPreferredSize(new Dimension(300, 10));
		_progressBar.setValue(0);

		_removeAudio = new JButton("Remove Audio");
		_removeAudio.setEnabled(false);

		_replace = new JButton("Replace Audio");
		_replace.setEnabled(false);

		_overLayAudio = new JButton("Overlay Audio");
		_overLayAudio.setEnabled(false);

		_inputAudioSelect = new JButton("Select Audio");

		_inputAudio = new JTextField();
		_inputAudio.setEditable(false);
		_inputAudio.setVisible(true);

		leftSide.add(_startLabel);
		leftSide.add(_startHours, "split 3, wmax 40, w 100%");
		leftSide.add(_startMinutes, "wmax 40, w 100%");
		leftSide.add(_startSeconds, "wmax 40, wrap 5, w 100%");
		leftSide.add(_durationLabel);
		leftSide.add(_durationHours, "split 3, wmax 40, w 100%");
		leftSide.add(_durationMinutes, "wmax 40, w 100%");
		leftSide.add(_durationSeconds, "wmax 40, w 100%, wrap");

		leftSide.add(_inputAudio, "split 5, span 4, w 100%");
		leftSide.add(_inputAudioSelect, "span 1");

		rightSide.add(_extractAudio,"w 100%, h 100%");
		rightSide.add(_removeAudio, "w 100%, h 100%,wrap");
		rightSide.add(_replace, "w 100%, h 100%");
		rightSide.add(_overLayAudio, "w 100%, h 100%,wrap");
		rightSide.add(_cancel, "w 100%, h 100%,span");


		progressPanel.add(_progressBar, "w 100%");

		add(leftSide, "growx");
		add(rightSide, "growx, wrap");
		add(progressPanel, "w 100%, span 2");


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

		//implements all the listeners 
		_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				process.cancel();
			}
		});

		_inputAudioSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				fileChooser(_inputAudio, false);
			}
		});


		_replace.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				replaceAudio(REPLACE);
			}
		});
		
		_removeAudio.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				replaceAudio(REMOVE);
			}
		});
		
		_overLayAudio.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				replaceAudio(OVERLAY);
			}
		});
	}

	/**
	 * Asks user for output file name and checks if it exists, asks to overwrite if file does exist
	 * Runs replace audio command once all inputs valid
	 */
	private void replaceAudio(String command){
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

		fileChooser.setDialogTitle("Specify new video file name");

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
			if (command.equals(REMOVE)){
				_progressBar.setValue(0);
				process = new ReplaceAudioProcess(_mediaLoc, _inputAudio.getText(), _newFileLoc, this, true);
			}else if (command.equals(OVERLAY)){
				process = new OverlayAudioProcess(_mediaLoc, _inputAudio.getText(), _newFileLoc, this);
				_progressBar.setIndeterminate(true);
			}else{
				process = new 
						ReplaceAudioProcess(_mediaLoc, _inputAudio.getText(), 
								_newFileLoc, this, false);
				_progressBar.setIndeterminate(true);
			}
			process.execute();
			_cancel.setEnabled(true);
			disableAllButtons();
		}

	}
	
	/**
	 * Asks the user for output file name then runs the extract audio process
	 */

	private void extractAudio() {
		CheckFile check = new CheckFile(false);
		//check if video file has no audio
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

		fileChooser.setDialogTitle("Specify new audio file name");

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
			_progressBar.setValue(0);
			//Get values from spinners for start and duration times
			String start = createTime(_startHours.getValue()+"", 
					_startMinutes.getValue()+"", _startSeconds.getValue()+"");
			String duration = createTime(_durationHours.getValue()+"", 
					_durationMinutes.getValue()+"", _durationSeconds.getValue()+"");
			process = new ExtractAudioProcess(_mediaLoc,fileToSave.getAbsolutePath(), this, start, duration);

			process.execute();
			_cancel.setEnabled(true);
			disableAllButtons();
		}
	}
	
	/**
	 * Method calls to enable/disable buttons
	 */

	public void enableExtractButtons() {
		_extractAudio.setEnabled(true);
		_removeAudio.setEnabled(true);
	}

	private void enableReplaceButton(){
		_replace.setEnabled(true);
		_overLayAudio.setEnabled(true);
	}

	private void disableExtractButtons(){
		_extractAudio.setEnabled(false);
		_removeAudio.setEnabled(false);
	}

	private void disableReplaceButton(){
		_replace.setEnabled(false);
		_overLayAudio.setEnabled(false);
	}
	
	private void disableAllButtons(){
		disableExtractButtons();
		disableReplaceButton();
	}

	/**
	 * Set the extract progress bar to finished
	 */
	public void extractFinished() {
		_progressBar.setValue(_progressBar.getMaximum());
		enableExtractButtons();
		_cancel.setEnabled(false);
	}

	/**
	 * Sets progressBar to finished and re-enables buttons
	 */
	public void replaceFinished(){
		if (!_inputAudio.getText().isEmpty()){
			enableReplaceButton();
			_progressBar.setIndeterminate(false);
		}
		_progressBar.setValue(_progressBar.getMaximum());
		enableExtractButtons();
		_cancel.setEnabled(false);
	}
	

	/**
	 * Set max value of extract progress bar
	 * @param max: max value
	 */
	public void setExtractMax(int max){
		_progressBar.setMaximum(max);
	}

	/**
	 * Set the progress bar value
	 * @param value: Value to be set
	 */
	public void setExtractValue(int value){
		_progressBar.setValue(value);
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
					.showMessageDialog(field,"Invalid Audio File",
							"Extract Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			field.setText(selectedFile.getAbsolutePath());
			if (!(_mediaLoc == null)){
				enableReplaceButton();
			}
		}
	}

	/**
	 * Set the location of media loaded
	 * @param _mediaLoc: Location of media
	 */
	public void setMediaLoc(String _mediaLoc) {
		this._mediaLoc = _mediaLoc;
		if(!_inputAudio.getText().isEmpty()){
			enableReplaceButton();
		}
	}

	private String createTime(String hours, String minutes, String seconds){
		if (hours.length() == 1){
			hours="0"+hours;
		}
		if (minutes.length() == 1){
			minutes="0"+minutes;
		}
		if (seconds.length() == 1){
			seconds="0"+seconds;
		}
		return hours+":"+minutes+":"+seconds;
	}

	/**
	 * Save audio settings
	 * @param saveFile: Path to save file
	 */
	public void save(String saveFile){
		saveLoad = new SaveLoadState(_tab.getVideoLocField(), _startHours,
				_startMinutes, _startSeconds, _durationHours,
				_durationMinutes, _durationSeconds, _inputAudio, 
				saveFile);
		saveLoad.save();
	}
	
	/**
	 * Load audio settings
	 * @param saveFile: Path to file to load
	 */
	public void load(String saveFile){
		saveLoad = new SaveLoadState(_tab.getVideoLocField(), _startHours,
				_startMinutes, _startSeconds, _durationHours,
				_durationMinutes, _durationSeconds, _inputAudio, 
				saveFile);
		saveLoad.load("audio");
	}
	
	public void setLimits() {
		String length = _videoPanel.getLength();
		String[] format = length.split(":");
		for (int i = 0; i<3;i++){
			mediaLength[i] = Integer.parseInt(format[i]);
		}
			_startHours.setModel(new SpinnerNumberModel(0, 0, mediaLength[0], 1));
			_startMinutes.setModel(new SpinnerNumberModel(0, 0, mediaLength[1], 1));
			_startSeconds.setModel(new SpinnerNumberModel(0, 0, mediaLength[2], 1));
			_durationHours.setModel(new SpinnerNumberModel(0, 0, mediaLength[0], 1));
			_durationMinutes.setModel(new SpinnerNumberModel(0, 0, mediaLength[1], 1));
			_durationSeconds.setModel(new SpinnerNumberModel(0, 0, mediaLength[2], 1));
			_durationHours.setValue((new Integer(mediaLength[0])));
			_durationMinutes.setValue((new Integer(mediaLength[1])));
			_durationSeconds.setValue((new Integer(mediaLength[2])));
			resetLimits();
	}

	
<<<<<<< HEAD
	public void setVideoTab(VideoTab video) {
=======
	/**
	 * resets the limits if there are several min/ hours to allow seconds
	 */
		private void resetLimits() {
			if (mediaLength[1]>0){
				mediaLength[2] = 59;
				mediaLength[1] = (mediaLength[1]-1 == 0) ? 0 : mediaLength[1]-1;
				_startSeconds.setModel(new SpinnerNumberModel(0, 0, 59, 1));
				_durationSeconds.setValue(new Integer(59));
				_startMinutes.setModel(new SpinnerNumberModel(0, 0, mediaLength[1], 1));
				_durationMinutes.setModel(new SpinnerNumberModel(0, 0, mediaLength[1], 1));
				_durationMinutes.setValue((new Integer(mediaLength[1])));
			}
			//if there is more than one hour then min spinner can get to 59 and lower the hour limit
			if (mediaLength[0]>0){
				mediaLength[1] = 59;
				mediaLength[0] = (mediaLength[0]-1 == 0) ? 0 : mediaLength[0]-1;
				_startMinutes.setModel(new SpinnerNumberModel(0, 0, 59, 1));
				_durationMinutes.setValue(new Integer(59));
				_startHours.setModel(new SpinnerNumberModel(0, 0, mediaLength[0], 1));
				_durationHours.setModel(new SpinnerNumberModel(0, 0, mediaLength[0], 1));
				_durationHours.setValue((new Integer(mediaLength[0])));
			}
		}
	public void setVideoTab(MediaTab video) {
>>>>>>> ofekdev
		_tab = video;
	}
}
