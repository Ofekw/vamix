package gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import model.IntegerField;
import controller.ExtractProcess;
import controller.ShellProcess;

@SuppressWarnings("serial")
public class Extract extends JPanel {

	private JTextField _txtFileLoc;
	private JTextField _txtSec;
	private JTextField _txtMin;
	private JTextField _txtHours;
	private JTextField _txtHoursExt;
	private JTextField _txtMinExt;
	private JTextField _txtSecExt;
	private boolean _fileExists;
	private String _fileName;
	private ExtractProcess _shell;
	private String _newFileName;
	private JProgressBar _progressBar;
	private JButton _btnExtract;
	private JButton _btnCancel;
	private JButton _btnBrowse;

	public Extract() {
		/**
		 * Initializes the Extract pane.
		 */
		Box verticalBox = Box.createVerticalBox();
		this.add(verticalBox);

		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);

		JLabel lblFilename = new JLabel("Filename  :");
		horizontalBox.add(lblFilename);

		_txtFileLoc = new JTextField();
		horizontalBox.add(_txtFileLoc);
		_txtFileLoc.setColumns(10);

		_btnBrowse = new JButton("Browse");
		_btnBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (_btnBrowse.isEnabled()) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser
							.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					int returnValue = fileChooser.showOpenDialog(null);
					if (returnValue == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();
						_txtFileLoc.setText(selectedFile.getAbsolutePath());
					}
				}
			}
		});

		horizontalBox.add(_btnBrowse);

		JSeparator separator_3 = new JSeparator();
		verticalBox.add(separator_3);

		Box horizontalBox_1 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_1);

		JLabel lblStartTime = new JLabel("Start Time:");
		horizontalBox_1.add(lblStartTime);
		/**
		 * Use the integer model to limit fields to just 2 digit numbers
		 */
		_txtHours = new IntegerField();
		_txtHours.setText("00");
		_txtHours.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				_txtHours.setText("");
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if (_txtHours.getText().length() == 1) {
					_txtHours.setText("0" + _txtHours.getText());
				} else if (_txtHours.getText().isEmpty()) {
					_txtHours.setText("00");
				}
			}
		});
		horizontalBox_1.add(_txtHours);
		_txtHours.setColumns(10);

		_txtMin = new IntegerField();
		_txtMin.setText("00");
		_txtMin.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				_txtMin.setText("");
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if (_txtMin.getText().length() == 1) {
					_txtMin.setText("0" + _txtMin.getText());
				} else if (_txtMin.getText().isEmpty()) {
					_txtMin.setText("00");
				}

			}
		});

		horizontalBox_1.add(_txtMin);
		_txtMin.setColumns(10);

		_txtSec = new IntegerField();
		_txtSec.setText("00");
		_txtSec.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				_txtSec.setText("");
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if (_txtSec.getText().length() == 1) {
					_txtSec.setText("0" + _txtSec.getText());
				} else if (_txtSec.getText().isEmpty()) {
					_txtSec.setText("00");
				}

			}
		});
		horizontalBox_1.add(_txtSec);
		_txtSec.setColumns(10);

		JSeparator separator_2 = new JSeparator();
		verticalBox.add(separator_2);

		Box horizontalBox_2 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_2);

		JLabel lblDuration = new JLabel("Duration   :");
		horizontalBox_2.add(lblDuration);

		_txtHoursExt = new IntegerField();
		_txtHoursExt.setText("00");
		_txtHoursExt.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				_txtHoursExt.setText("");
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if (_txtHoursExt.getText().length() == 1) {
					_txtHoursExt.setText("0" + _txtHoursExt.getText());
				} else if (_txtHoursExt.getText().isEmpty()) {
					_txtHoursExt.setText("00");
				}

			}
		});
		_txtHoursExt.setColumns(10);
		horizontalBox_2.add(_txtHoursExt);

		_txtMinExt = new IntegerField();
		_txtMinExt.setText("00");
		_txtMinExt.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				_txtMinExt.setText("");
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if (_txtMinExt.getText().length() == 1) {
					_txtMinExt.setText("0" + _txtMinExt.getText());
				} else if (_txtMinExt.getText().isEmpty()) {
					_txtMinExt.setText("00");
				}

			}
		});
		_txtMinExt.setColumns(10);
		horizontalBox_2.add(_txtMinExt);

		_txtSecExt = new IntegerField();
		_txtSecExt.setText("00");
		_txtSecExt.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				_txtSecExt.setText("");
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if (_txtSecExt.getText().length() == 1) {
					_txtSecExt.setText("0" + _txtSecExt.getText());
				} else if (_txtSecExt.getText().isEmpty()) {
					_txtSecExt.setText("00");
				}

			}
		});
		_txtSecExt.setColumns(10);
		horizontalBox_2.add(_txtSecExt);

		JSeparator separator = new JSeparator();
		verticalBox.add(separator);

		Box horizontalBox_3 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_3);

		_btnExtract = new JButton("Extract");
		_btnExtract.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (_btnExtract.isEnabled()) {
					startExtract();
				}
			}
		});
		horizontalBox_3.add(_btnExtract);

		_btnCancel = new JButton("Cancel");
		_btnCancel.setEnabled(false);
		_btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (_btnCancel.isEnabled()) {
					if (_shell != null) {
						_shell.destroy();
						_shell.cancel(true);
						ShellProcess.command("rm " + _txtFileLoc);
						resetState();
						_progressBar.setValue(0);
						_shell = null;
					} else {
						resetState();
					}
				}
			}
		});
		horizontalBox_3.add(_btnCancel);

		_progressBar = new JProgressBar();
		horizontalBox_3.add(_progressBar);
	}

	private void startExtract() {
		/**
		 * Simple checks for correct filepath and disables buttons
		 */
		_btnCancel.setEnabled(true);
		_btnExtract.setEnabled(false);
		_progressBar.setIndeterminate(true);
		_fileName = null;
		_fileExists = false;
		fileName(_txtFileLoc.getText());
		if (_txtFileLoc != null && !_txtFileLoc.getText().isEmpty()) {
			fileCheck();
			if (_fileExists) {
				extract();
			} else {
				JOptionPane.showMessageDialog(this, "File path is invalid",
						"Location Error", JOptionPane.ERROR_MESSAGE);
				resetState();
			}
		} else {
			JOptionPane.showMessageDialog(this, "File path is invalid",
					"Location Error", JOptionPane.ERROR_MESSAGE);
			resetState();
		}

	}

	private void fileCheck() {
		File f = new File(_txtFileLoc.getText());
		if (f.exists()) {
			_fileExists = true;
		} else {
			_fileExists = false;
		}
	}

	private void fileName(String fileLoc) {
		_fileName = ShellProcess.command("basename " + fileLoc);
	}

	private void extract() {
		_progressBar.setValue(0);
		mediaTest();

	}

	public void extractSong() {
		/**
		 * Initializes the extracting process.
		 */
		if (_shell.getExitStatus() == 0) {
			// if legit mp3 file
			ShellProcess.command("rm -f " + "\"" + "temp.mp3" + "\"");
			@SuppressWarnings("serial")
			/**
			 * Asks for save path and location (checks for overwrite).
			 */
			JFileChooser fileChooser = new JFileChooser() {
				@Override
				public void approveSelection() {
					File f = getSelectedFile();
					if (f.exists() && getDialogType() == SAVE_DIALOG) {
						int result = JOptionPane
								.showConfirmDialog(
										this,
										"The file exists, overwrite? (Don't overide source file)",
										"Existing file",
										JOptionPane.YES_NO_OPTION);
						switch (result) {
						case JOptionPane.YES_OPTION:
							ShellProcess
									.command("rm -f " + f.getAbsolutePath());
							super.approveSelection();
							return;
						case JOptionPane.NO_OPTION:
							resetState();
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

			fileChooser.setDialogTitle("Specify where to save song");

			int userSelection = fileChooser.showSaveDialog(this);
			File fileToSave = null;
			String startTime = null;
			String duration = null;
			if (userSelection == JFileChooser.OPEN_DIALOG) {
				fileToSave = fileChooser.getSelectedFile();
				if (!fileChooser.getSelectedFile().getAbsolutePath()
						.endsWith(".mp3")) {
					fileToSave = new File(fileChooser.getSelectedFile()
							+ ".mp3");
				}
				_newFileName = fileChooser.getName(fileToSave);
				startTime = _txtHours.getText() + ":" + _txtMin.getText() + ":"
						+ _txtSec.getText();
				duration = _txtHoursExt.getText() + ":" + _txtMinExt.getText()
						+ ":" + _txtSecExt.getText();
				_shell = new ExtractProcess(this, 1);
				_shell.setCommand("avconv -i " + _txtFileLoc.getText()
						+ " -ss " + startTime + " -t " + duration + " "
						+ fileToSave.getAbsolutePath());
				_shell.execute();

			}

		} else {
			JOptionPane.showMessageDialog(this, "Selected file is not a song",
					"File Error", JOptionPane.ERROR_MESSAGE);
			resetState();
		}
	}

	public void doneState() {
		if (_shell != null) {
			_shell.cancel(true);
		}
		_progressBar.setValue(100);
		_progressBar.setIndeterminate(false);
		_btnExtract.setEnabled(true);
		_btnCancel.setEnabled(false);
	}

	public void resetState() {
		if (_shell != null) {
			_shell.cancel(true);
		}
		_progressBar.setValue(0);
		_progressBar.setIndeterminate(false);
		_btnExtract.setEnabled(true);
		_btnCancel.setEnabled(false);
	}

	private void mediaTest() {
		/**
		 * Uses a simple avconv command rather than a "File" command to check if
		 * mp3 is legit because this is a much more comprehensive test.
		 */
		_shell = null;
		ShellProcess.command("rm -f " + "\"" + "temp.mp3" + "\"");
		_shell = new ExtractProcess(this, 0);
		// check if file is valid
		_shell.setCommand("avconv -i " + _txtFileLoc.getText()
				+ " -ss 00:00:00 -t 00:00:01 " + "\"" + "temp.mp3" + "\"");
		_shell.execute();
	}
}
