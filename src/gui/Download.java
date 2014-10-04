package gui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import javax.swing.SwingConstants;

import controller.DownloadProcess;
import controller.ShellProcess;

@SuppressWarnings("serial")
public class Download extends JPanel implements PropertyChangeListener { // ActionListener,

	private JTextField _textField;
	private boolean _fileExists = false;
	private String _inputUrl;
	private int _openSource = 1; // 1 == no
	private String _fileName;
	private JProgressBar _progressBar;
	private DownloadProcess _shell = null;
	private int _userResponse = 2; // 2 == cancel
	private JLabel _statusLabel;
	private JButton _btnDownload;
	private JButton _btnCancel;
	private JButton _btnPause;
	private String _fileLoc;
	private String _filePath;
	private JPanel _parent;

	public Download(JPanel parent) {
		super(new FlowLayout(FlowLayout.LEFT));
		this._parent = parent;
		/**
		 * Initializes the download pane.
		 */
		this.setPreferredSize(new Dimension(1000, 180));

		Box verticalBox = Box.createVerticalBox();
		add(verticalBox);

		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);
		// _shell = new ShellProcess();
		JLabel lblMpUrl = new JLabel("Media URL:");
		horizontalBox.add(lblMpUrl);

		Component rigidArea = Box.createRigidArea(new Dimension(50, 20));
		rigidArea.setPreferredSize(new Dimension(105, 20));
		horizontalBox.add(rigidArea);

		_textField = new JTextField();
		horizontalBox.add(_textField);
		_textField.setColumns(45);

		_btnDownload = new JButton("Download");
		horizontalBox.add(_btnDownload);
		_btnDownload.setHorizontalAlignment(SwingConstants.LEFT);

		_btnPause = new JButton("Pause");
		horizontalBox.add(_btnPause);
		_btnPause.setEnabled(false);

		_btnCancel = new JButton("Cancel");
		horizontalBox.add(_btnCancel);
		_btnCancel.setEnabled(false);
		_btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (_btnCancel.isEnabled()) {
					_progressBar.setValue(0);
					if (_shell != null) {
						getStatusLabel().setText("Status: Cancelled");
						_shell.destroy();
						_shell.cancel(true);
						ShellProcess.command("rm " + _filePath);
						toggleButtonsCancelled();

						_shell = null;
					}
				}
			}
		});
		_btnPause.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (_btnPause.isEnabled()) {
					if (_shell != null) {
						getStatusLabel().setText("Status: Paused");
						_shell.destroy();
						_shell.cancel(true);
						toggleButtonsCancelled();
						_shell = null;
					}
				}
			}
		});
		_btnDownload.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (_btnDownload.isEnabled()) {
					toggleButtonsDownload();
					/**
					 * Checks that file is open-source from the user.
					 */
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					try {
						_inputUrl = _textField.getText();
						if (!_inputUrl.isEmpty()) {
							Object[] options = { "Yes", "No" };
							_openSource = JOptionPane.showOptionDialog(null,
									"Is this media file open source?", "Warning",

									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE,

									null, options, options[0]);

							if (_openSource == 0) {
								_openSource = 1;
								_fileLoc = null;
								/**
								 * Obtains a save directory from the user
								 */
								JFileChooser dirChooser = new JFileChooser();
								dirChooser
								.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
								dirChooser
								.setDialogTitle("Select File Location");
								int returnValue = dirChooser
										.showOpenDialog(null);
								if (returnValue == JFileChooser.APPROVE_OPTION) {
									File selectedFile = dirChooser
											.getSelectedFile();
									_fileLoc = (selectedFile.getAbsolutePath());
								} else if (returnValue == JFileChooser.CANCEL_OPTION) {
									toggleButtonsCancelled();
									return;
								}
								fileName(_inputUrl);
							} else {
								_statusLabel
								.setText("Status: Song must be open-source");
								toggleButtonsCancelled();
							}
						} else {
							toggleButtonsCancelled();
							_statusLabel.setText("Status: Invalid URL");
							;
						}
					} catch (Exception m) {
						m.printStackTrace();
					}
				}
			}
		});

		Box horizontalBox_1 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_1);

		JSeparator separator = new JSeparator();
		verticalBox.add(separator);

		Box horizontalBox_2 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_2);
		_progressBar = new JProgressBar();
		horizontalBox_2.add(_progressBar);

		Box horizontalBox_4 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_4);

		Box horizontalBox_3 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_3);

		setStatusLabel(new JLabel("Status: waiting for download"));
		horizontalBox_3.add(getStatusLabel());
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox_3.add(horizontalGlue);

	}

	private void toggleButtonsDownload() {
		_btnDownload.setEnabled(false);
		_btnCancel.setEnabled(true);
		_btnPause.setEnabled(true);
		_textField.setEnabled(false);
	}

	private void toggleButtonsCancelled() {
		this.setCursor(Cursor.getDefaultCursor());
		_btnDownload.setEnabled(true);
		_btnCancel.setEnabled(false);
		_btnPause.setEnabled(false);
		_textField.setEnabled(true);
	}

	private void fileName(String url) {
		_fileName = ShellProcess.command("basename " + url);
		_fileName = _fileName.trim();
		fileCheck();
		if (_fileExists) {
			_userResponse = 1;
			/**
			 * Check if to overwrite or continue download
			 */
			String serverFileSize = ShellProcess
					.command("wget "
							+ _inputUrl
							+ " --spider --server-response -O - 2>&1 | sed -ne '/Content-Length/{s/.*: //;p}'");
			serverFileSize = serverFileSize.trim();
			File file = new File(_filePath
					+ System.getProperty("file.separator"));
			long size = file.length();
			String txtSize = String.valueOf(size);
			/**
			 * If file exists, overwrite pane will appear
			 */
			if (txtSize.equals(serverFileSize)) {
				Object[] options = { "Overwrite", "Cancel" };
				_userResponse = JOptionPane
						.showOptionDialog(
								null,
								"The file: "
										+ _fileName
										+ " already exists in the directory, would you like to overwrite it ",
										"Overwrite?",

										JOptionPane.YES_NO_OPTION,
										JOptionPane.INFORMATION_MESSAGE,

										null, options, options[0]);

				if (_userResponse == 0) {
					_progressBar.setValue(0);
					ShellProcess.command("rm " + _filePath);
					download();
				} else if (_userResponse == 1) {
					toggleButtonsCancelled();
				}
				/**
				 * Checks if file has been partially downloaded and prompts to
				 * continue download if it has
				 */
			} else if (!txtSize.equals(serverFileSize)) {
				Object[] options = { "Restart", "Continue", "Cancel" };
				_userResponse = JOptionPane
						.showOptionDialog(
								null,
								"This file: "
										+ _fileName
										+ " has been partially downloaded. Would you like to restart or continue the download?",
										"Restart/Continue",

										JOptionPane.YES_NO_OPTION,
										JOptionPane.INFORMATION_MESSAGE,

										null, options, options[0]);

				if (_userResponse == 0) {
					_progressBar.setValue(0);
					ShellProcess.command("rm " + _filePath);
					download();
				} else if (_userResponse == 1) {
					download();
				} else if (_userResponse == 2) {
					toggleButtonsCancelled();
				}
			}
		} else {
			download();
		}

	}

	private void download() {
		/**
		 * Initializes the download process
		 */
		getStatusLabel().setText("Status: Downloading...");
		_shell = null;
		_shell = new DownloadProcess();
		_shell.setDownload(this);
		_shell.setCommand("wget -P " + _fileLoc + " -c --progress=dot "
				+ _inputUrl);
		_shell.addPropertyChangeListener(this);
		_shell.execute();
	}

	private void fileCheck() {
		_filePath = _fileLoc + System.getProperty("file.separator") + _fileName;
		File f = new File(_filePath);
		if (f.exists()) {
			_fileExists = true;
		} else {
			_fileExists = false;
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		/**
		 * Updates the progress bar
		 */
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			_progressBar.setValue(progress);
			if (progress == 100 || _shell.getExitStatus() == 0) {
				getStatusLabel().setText("Status: Complete");
				this.setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	public void reset() {
		this.setCursor(Cursor.getDefaultCursor());
		_shell.destroy();
		_shell.cancel(true);
		toggleButtonsCancelled();

	}

	public JLabel getStatusLabel() {
		return _statusLabel;
	}

	public void setStatusLabel(JLabel _statusLabel) {
		this._statusLabel = _statusLabel;
	}
}
