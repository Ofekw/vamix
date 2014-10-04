package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;
import controller.CheckFile;
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

	public FilterTab(VideoPanel panel, MainGui main){
		super(panel);
		setLayout(new MigLayout("", "[][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][]", "[][][][]"));
		
		
		_main = main;
		_apply = new JButton("Apply");
		add(_apply, "cell 29 3");
	}

	@Override
	protected void initialise() {
		this.setPreferredSize(new Dimension(1000, 130));
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

	private void progressReset() {
		// TODO Auto-generated method stub
		
	}

	private void createProcess() {
		// TODO Auto-generated method stub
		
	}
	
	private void noMediaSelected() {
		JOptionPane.showMessageDialog(this, "Invalid media selected in the Media tab",
				"Media Error", JOptionPane.ERROR_MESSAGE);
	}
}
