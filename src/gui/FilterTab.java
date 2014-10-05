package gui;

import java.awt.Color;
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
import controller.SaveLoadState;
import controller.ShellProcess;

@SuppressWarnings("serial")
/**
 * Tab for video manipulation and filters
 * @author Ofek Wittenberg
 *
 */
public class FilterTab extends Tab {
	private MainGui _main;
	private JButton _apply;
	private String _saveLoc;
	private JProgressBar _progressBar;
	private ButtonGroup _filters;
	private JRadioButton _rdbtnNone;
	private JRadioButton _rdbtnBorder;
	private JRadioButton _rdbtnFlipHorizontally;
	private JRadioButton _rdbtnFlipVertically;
	private JRadioButton _rdbtnMono;
	private JRadioButton _rdbtnBlur;
	private String _selection = "blur";
	
	private SaveLoadState saveLoad;

	public FilterTab(VideoPanel panel, MainGui main){
		super(panel);
		this.setPreferredSize(new Dimension(1046, 150));
		setLayout(new MigLayout("", "[][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][73.00][90.00][83.00][][][][]", "[][][][][][][][][][][][][][][]"));


		_main = main;
		_filters = new ButtonGroup();

		JLabel lblChooseVideoFilter = new JLabel("Choose Video Filter:");
		add(lblChooseVideoFilter, "cell 1 1");

		_rdbtnNone = new JRadioButton("None");
//		add(_rdbtnNone, "cell 5 1");
		
		_rdbtnBlur = new JRadioButton("Blur");
		_rdbtnBlur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_selection="blur";
			}
		});
		add(_rdbtnBlur, "flowx,cell 7 1");
		_rdbtnBlur.setSelected(true);
		_filters.add(_rdbtnBlur);

		_rdbtnFlipVertically = new JRadioButton("Flip Vertically");
		_rdbtnFlipVertically.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_selection="flipV";
			}
		});
		add(_rdbtnFlipVertically, "cell 9 1");
		
				_rdbtnFlipHorizontally = new JRadioButton("Flip Horizontally");
				_rdbtnFlipHorizontally.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						_selection="flipH";
					}
				});
				add(_rdbtnFlipHorizontally, "flowx,cell 11 1");
				_filters.add(_rdbtnFlipHorizontally);
		
		_rdbtnMono = new JRadioButton("Mono");
		_rdbtnMono.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_selection="mono";
			}
		});
		add(_rdbtnMono, "cell 13 1");
		_filters.add(_rdbtnFlipVertically);
		_filters.add(_rdbtnMono);
		
				_rdbtnBorder = new JRadioButton("Border");
				_rdbtnBorder.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						_selection="border";
					}
				});
				add(_rdbtnBorder, "cell 15 1");
				_filters.add(_rdbtnBorder);
		


		_progressBar = new JProgressBar();
		add(_progressBar, "cell 1 13 46 1,growx");
		_apply = new JButton("Apply");
		add(_apply, "cell 47 13");

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
		_apply.setEnabled(false);

	}

	public void enableButtons() {
		_apply.setEnabled(true);

	}

	public void progressReset(){
		_progressBar.setValue(0);
		_progressBar.setIndeterminate(false);
		enableButtons();
	}

	public void progressDone(){
		_progressBar.setValue(100);
		_progressBar.setIndeterminate(false);
		enableButtons();
	}

	private void createProcess() {
		FilterProcess process = new FilterProcess(this);
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

/**
 * Saves the current text settings to a vamix save file
 * @param saveFileName: Path to save file
 */
public void save(String saveFileName){
	saveLoad = new SaveLoadState(getFilterSelection(), saveFileName);
	saveLoad.save();
}

/**
 * Loads the settings from the file specified
 * @param loadFileName: Path to Vamix save file to be loaded
 */
public void load(String loadFileName){
	saveLoad = new SaveLoadState(getFilterSelection(), loadFileName);
	int filter = saveLoad.load("filter");
	if (filter == 0){
		_selection = saveLoad.getFilter();
		System.out.println(saveLoad.getFilter());
	}
//	deselectAll();
	if ( _selection.equals("blur")) {
		_rdbtnBlur.doClick();
	} else if (_selection.equals("border")) {
		_rdbtnBorder.doClick();
	} else if (_selection.equals("flipH")) {
		_rdbtnFlipHorizontally.doClick();
	} else if (_selection.equals("flipV")) {
		_rdbtnFlipVertically.doClick();
	} else if (_selection.equals("mono")) {
		_rdbtnNone.doClick();
}
	repaint();
}

private void deselectAll() {
	_rdbtnBlur.setSelected(false);
	_rdbtnBorder.setSelected(false);
	_rdbtnFlipHorizontally.setSelected(false);
	_rdbtnFlipVertically.setSelected(false);
	_rdbtnMono.setSelected(false);
	_rdbtnNone.setSelected(false);
}

}
