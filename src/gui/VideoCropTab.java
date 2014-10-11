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
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import controller.CheckFile;
import controller.FilterProcess;
import controller.SaveLoadState;
import controller.ShellProcess;
import controller.VideoCropProcess;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

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
	private ButtonGroup _filters;
	private JRadioButton _rdbtnNone;
	private String _selection = "blur";
	private JLabel lblEnterEndTime;
	private JSpinner _startMin;
	private JSpinner _startSec;
	private JSpinner _startHour;
	private JSpinner _endSec;
	private JSpinner _endMin;
	private JSpinner _endHr;
	private SaveLoadState saveLoad;

	public VideoCropTab(VideoPanel panel, MainGui main){
		super(panel);
		this.setPreferredSize(new Dimension(1046, 150));
		setLayout(new MigLayout("", "[][][][100px,grow][][][100px][][100][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][73.00][90.00][83.00][][][][]", "[][][][][][][][][][][][][][][][]"));


		_main = main;

		JLabel lblChooseVideoFilter = new JLabel("Enter start time (HH:MM:SS):");
		add(lblChooseVideoFilter, "cell 1 1");

		_rdbtnNone = new JRadioButton("None");
		
		_startHour = new JSpinner();
		_startHour.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		_startHour.setToolTipText("hours");
		add(_startHour, "cell 3 1,growx");
		
		_startMin = new JSpinner();
		_startMin.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		_startMin.setToolTipText("minutes");
		add(_startMin, "cell 6 1,growx");
		
		_startSec = new JSpinner();
		_startSec.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		_startSec.setToolTipText("seconds");
		add(_startSec, "cell 8 1,growx");
		
		lblEnterEndTime = new JLabel("Duration (HH:MM:SS):");
		add(lblEnterEndTime, "cell 1 4");
		
		_endHr = new JSpinner();
		_endHr.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		_endHr.setToolTipText("hours");
		add(_endHr, "cell 3 4,growx");
		
		_endMin = new JSpinner();
		_endMin.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		_endMin.setToolTipText("minutes");
		add(_endMin, "cell 6 4,growx");
		
		_endSec = new JSpinner();
		_endSec.setModel(new SpinnerNumberModel(0, 0, 99, 1));
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
		_apply.setEnabled(false);
		_apply.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(_apply.isEnabled()){
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
			disableButtons();
		}

	}

	private void disableButtons() {
		// TODO Auto-generated method stub

	}

	public void enableButtons() {
		_apply.setEnabled(true);

	}

	public void progressReset(){
		_progressBar.setValue(0);
		_progressBar.setIndeterminate(false);
	}

	public void progressDone(){
		_progressBar.setValue(100);
		_progressBar.setIndeterminate(false);
	}

	private void createProcess() {
		VideoCropProcess process = new VideoCropProcess(this, _startHour.getValue().toString(), _startMin.getValue().toString(),_startSec.getValue().toString(),_endHr.getValue().toString(),_endMin.getValue().toString(),_endSec.getValue().toString());
		process.execute();
	}

	public String getSaveloc(){
		return _saveLoc;
	}

	public MainGui getMain(){
		return _main;
	}

	public String getFilterSelection(){
	
		return _selection;
}

private void noMediaSelected() {
	JOptionPane.showMessageDialog(this, "Invalid media selected in the Media tab",
			"Media Error", JOptionPane.ERROR_MESSAGE);
}

public void save(String saveFile){
	JTextField empty = new JTextField("");
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
	JTextField empty = new JTextField("");
	saveLoad = new SaveLoadState(_startHour,
			_startMin, _startSec, _endHr,
			_endMin, _endSec, 
			saveFile);
	saveLoad.load("video");
}
}
