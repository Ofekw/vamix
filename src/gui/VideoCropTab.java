package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;
import controller.gui.CheckFile;
import controller.processes.SaveLoadState;
import controller.processes.ShellProcess;
import controller.processes.VideoCropProcess;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

@SuppressWarnings("serial")
/**
 * Tab for video manipulation and filters
 * @author Ofek Wittenberg
 *
 */
public class VideoCropTab extends Tab {
	private MainGui _main;
	private JButton _apply;
	private String _saveLoc;
	private JProgressBar _progressBar;
	@SuppressWarnings("unused")
	private ButtonGroup _filters;
	@SuppressWarnings("unused")
	private String _selection = "blur";
	private JLabel lblEnterEndTime;
	private JSpinner _startMin;
	private JSpinner _startSec;
	private JSpinner _startHour;
	private JSpinner _endSec;
	private JSpinner _endMin;
	private JSpinner _endHr;
	private SaveLoadState saveLoad;
	private int[] mediaLength = new int[3];

	public VideoCropTab(VideoPanel panel, MainGui main){
		/**
		 * Creates and lays out the gui
		 */
		super(panel);
		this.setPreferredSize(new Dimension(1046, 150));
		setLayout(new MigLayout("", "[][][][100px,grow][][][100px][][100][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][73.00][90.00][83.00][][][][]", "[][][][][][][][][][][][][][][][]"));

		_main = main;

		JLabel lblChooseVideoFilter = new JLabel("Enter start time (HH:MM:SS):");
		add(lblChooseVideoFilter, "cell 1 1");
		/**
		 * Inserts all the spinners with listeners to limit the duration spinners
		 */
		_startHour = new JSpinner();
		_startHour.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int value = (int)_endHr.getValue();
				int range = mediaLength[0]-(int)_startHour.getValue();
				if(value>range){
					_endHr.setModel(new SpinnerNumberModel(value-1, 0, (range), 1));
				}else{
					_endHr.setModel(new SpinnerNumberModel(value, 0, (range), 1));
				}
			}
		});
		_startHour.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		_startHour.setToolTipText("hours");
		add(_startHour, "cell 3 1,growx");

		_startMin = new JSpinner();
		_startMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int value = (int)_endSec.getValue();
				int range = mediaLength[1]-(int)_startMin.getValue();
				if(value>range){
					_endMin.setModel(new SpinnerNumberModel(value-1, 0, (range), 1));
				}else{
					_endMin.setModel(new SpinnerNumberModel(value, 0, (range), 1));
				}
			}
		});
		_startMin.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		_startMin.setToolTipText("minutes");
		add(_startMin, "cell 6 1,growx");

		_startSec = new JSpinner();
		_startSec.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int value = (int)_endSec.getValue();
				int range = mediaLength[2]-(int)_startSec.getValue();
				if(value>range){
					_endSec.setModel(new SpinnerNumberModel(value-1, 0, (range), 1));
				}else{
					_endSec.setModel(new SpinnerNumberModel(value, 0, (range), 1));
				}
			}
		});
		_startSec.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		_startSec.setToolTipText("seconds");
		add(_startSec, "cell 8 1,growx");

		lblEnterEndTime = new JLabel("Duration (HH:MM:SS):");
		add(lblEnterEndTime, "cell 1 4");

		_endHr = new JSpinner();
		_endHr.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		_endHr.setToolTipText("hours");
		add(_endHr, "cell 3 4,growx");

		_endMin = new JSpinner();
		_endMin.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		_endMin.setToolTipText("minutes");
		add(_endMin, "cell 6 4,growx");

		_endSec = new JSpinner();
		_endSec.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		_endSec.setToolTipText("seconds");
		add(_endSec, "cell 8 4,growx");



		_progressBar = new JProgressBar();
		add(_progressBar, "cell 1 10 52 1,growx");
		_apply = new JButton("Apply");
		add(_apply, "cell 53 10");

		_apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		/**
		 * Apply button gets the save location and begins the process to crop the video
		 */
		_apply.setEnabled(false);
		_apply.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(_apply.isEnabled()){
					setLimits();
					if (!_main.getVideo().getVideoLoc().isEmpty() &&
							new CheckFile(true).checkFileType(_main.getVideo().getVideoLoc())){
						SaveLocAndTextProcess();	
					}else {
						noMediaSelected();
					}
				}
			}
		});
		initialise();
	}

	@Override
	protected void initialise() {

	}
	/**
	 * Obtains the save location when the apply button is pressed
	 */
	private void SaveLocAndTextProcess(){
		JFileChooser fileChooser = new JFileChooser() {
			@Override
			public void approveSelection() {

				File f = getSelectedFile();
				/*
				 * Makes sure the filename ends with extension .mp4 when checking for overwrite
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

		fileChooser.setDialogTitle("Specify where to save the modified video");

		int userSelection = fileChooser.showSaveDialog(this);
		File fileToSave = null;
		if (userSelection == JFileChooser.OPEN_DIALOG) {
			fileToSave = fileChooser.getSelectedFile();
			/*
			 * Makes sure the filename ends with extension .mp4
			 */

			if (!fileChooser.getSelectedFile().getAbsolutePath()
					.endsWith(".mp4")) {
				fileToSave = new File(fileChooser.getSelectedFile()
						+ ".mp4");
			}
			_saveLoc=fileToSave.getAbsolutePath();
			createProcess();
			progressReset();
			_apply.setEnabled(false);
			_progressBar.setIndeterminate(true);
		}

	}


	/**
	 * Enables depended buttons suchs as apply
	 */
	public void enableButtons() {
		_apply.setEnabled(true);
		setLimits();
		//method must be called twice to get proper end time
		setLimits();

	}
	/**
	 * Sets the limits through a spinner model to the max time of the video length
	 */
	private void setLimits() {
		String length = _videoPanel.getLength();
		System.out.println(length);
		String[] format = length.split(":");
		for (int i = 0; i<3;i++){
			mediaLength[i] = Integer.parseInt(format[i]);
			_startHour.setModel(new SpinnerNumberModel(0, 0, mediaLength[0], 1));
			_startMin.setModel(new SpinnerNumberModel(0, 0, mediaLength[1], 1));
			_startSec.setModel(new SpinnerNumberModel(0, 0, mediaLength[2], 1));
			_endHr.setModel(new SpinnerNumberModel(0, 0, mediaLength[0], 1));
			_endMin.setModel(new SpinnerNumberModel(0, 0, mediaLength[1], 1));
			_endSec.setModel(new SpinnerNumberModel(0, 0, mediaLength[2], 1));
			_endHr.setValue((new Integer(mediaLength[0])));
			_endMin.setValue((new Integer(mediaLength[1])));
			_endSec.setValue((new Integer(mediaLength[2])));
		}

	}
	/**
	 * Resets the progress bar
	 */
	public void progressReset(){
		_progressBar.setValue(0);
		_progressBar.setIndeterminate(false);
	}
	/**
	 * Sets progress bar to done when crop process is completed
	 */
	public void progressDone(){
		_progressBar.setValue(100);
		_progressBar.setIndeterminate(false);
	}
	/**
	 * creates the cropping process
	 */
	private void createProcess() {
		VideoCropProcess process = new VideoCropProcess(this, _startHour.getValue().toString(), _startMin.getValue().toString(),_startSec.getValue().toString(),_endHr.getValue().toString(),_endMin.getValue().toString(),_endSec.getValue().toString());
		process.execute();
	}
	/**
	 * getter for the save media location
	 * @return savelocation (string)
	 */
	public String getSaveloc(){
		return _saveLoc;
	}
	/**
	 * getter for the parent pane
	 * @return MainGui
	 */
	public MainGui getMain(){
		return _main;
	}
	/**
	 * Returns error popup if there is a problem with the media file selected
	 */
	private void noMediaSelected() {
		JOptionPane.showMessageDialog(this, "Invalid media selected in the Media tab",
				"Media Error", JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * Save video settings
	 * @param saveFile
	 */
	public void save(String saveFile){
		saveLoad = new SaveLoadState(_startHour,
				_startMin, _startSec, _endHr,
				_endMin, _endSec, saveFile);
		saveLoad.save();
	}

	/**
	 * Load video settings
	 * @param saveFile: Path to file to load
	 */
	public void load(String saveFile){
		saveLoad = new SaveLoadState(_startHour,
				_startMin, _startSec, _endHr,
				_endMin, _endSec, 
				saveFile);
		saveLoad.load("video");
	}
}
