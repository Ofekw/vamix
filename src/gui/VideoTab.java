package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class VideoTab extends Tab {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7118585127413410576L;
	private JTextField _txtVideoLoc;
	private JButton _btnBrowse;
	private Box verticalBox;
	private Box horizontalBox;
	private Component horizontalStrut;
	private AudioTab _audio ;

		public VideoTab(VideoPanel panel, AudioTab audio) {
			super(panel);
			this._audio = audio;
		}

	protected void initialise() {
		
		Download download = new Download(this);
		this.setPreferredSize(new Dimension(1000, 180));

		verticalBox = Box.createVerticalBox();
		verticalBox.setPreferredSize(new Dimension(980,180));
		add(verticalBox);

		horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);

		JLabel lblVideoFile = new JLabel("Playback Media:");
		horizontalBox.add(lblVideoFile);

		horizontalStrut = Box.createHorizontalStrut(20);
		horizontalBox.add(horizontalStrut);

		_txtVideoLoc = new JTextField();
		horizontalBox.add(_txtVideoLoc);
		_txtVideoLoc.setPreferredSize(new Dimension(5,10));

		_btnBrowse = new JButton("Browse");
		horizontalBox.add(_btnBrowse);
		verticalBox.add(download);
		_btnBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (_btnBrowse.isEnabled()) {
					fileBrowser();
				}
			}

			private void fileBrowser() {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser
				.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					_txtVideoLoc.setText(selectedFile.getAbsolutePath());
					if ((_txtVideoLoc == null || _txtVideoLoc.getText().isEmpty()) || !fileExists() ) {
						filePathInvalid();
					}else{
						/*
						 * need to add valid files checks here
						 */
						_videoPanel.setMedia(_txtVideoLoc.getText());
						_audio.enableExtractButtons();
						_audio.setMediaLoc(_txtVideoLoc.getText());
					}
				}
			}
		});

	}

	private boolean fileExists() {
		File f = new File(_txtVideoLoc.getText());
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

	//	public JTextField getVideoLoc() {
	//		return _txtVideoLoc;
	//	}
}
