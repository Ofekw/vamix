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

import net.miginfocom.swing.MigLayout;
import controller.CheckFile;
import controller.FilterProcess;
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
	private JSpinner spinner_1;
	private JSpinner spinner_2;
	private JSpinner spinner;
	private JSpinner spinner_3;
	private JSpinner spinner_4;
	private JSpinner spinner_5;

	public VideoCropTab(VideoPanel panel, MainGui main){
		super(panel);
		this.setPreferredSize(new Dimension(1046, 150));
		setLayout(new MigLayout("", "[][][][100px,grow][][][100px][][100][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][73.00][90.00][83.00][][][][]", "[][][][][][][][][][][][][][][][]"));


		_main = main;
		_filters = new ButtonGroup();

		JLabel lblChooseVideoFilter = new JLabel("Enter start time (HH:MM:SS):");
		add(lblChooseVideoFilter, "cell 1 1");

		_rdbtnNone = new JRadioButton("None");
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		spinner.setToolTipText("hours");
		add(spinner, "cell 3 1,growx");
		
		spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		spinner_1.setToolTipText("minutes");
		add(spinner_1, "cell 6 1,growx");
		
		spinner_2 = new JSpinner();
		spinner_2.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		spinner_2.setToolTipText("seconds");
		add(spinner_2, "cell 8 1,growx");
		
		lblEnterEndTime = new JLabel("Enter end time (HH:MM:SS):");
		add(lblEnterEndTime, "cell 1 4");
		
		spinner_5 = new JSpinner();
		spinner_5.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		spinner_5.setToolTipText("hours");
		add(spinner_5, "cell 3 4,growx");
		
		spinner_4 = new JSpinner();
		spinner_4.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		spinner_4.setToolTipText("minutes");
		add(spinner_4, "cell 6 4,growx");
		
		spinner_3 = new JSpinner();
		spinner_3.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		spinner_3.setToolTipText("seconds");
		add(spinner_3, "cell 8 4,growx");
		


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
		VideoCropProcess process = new VideoCropProcess(this);
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
}
