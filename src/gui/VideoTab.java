package gui;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Component;
import javax.swing.Box;

public class VideoTab extends Tab {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7118585127413410576L;
	private JTextField _txtVideoLoc;
	private JButton _btnBrowse;
	private Box verticalBox;
	private Box horizontalBox;
	private Component verticalStrut_1;
	private Component horizontalStrut;

		public VideoTab(VideoPanel panel) {
			super(panel);
		}

	protected void initialise() {

		verticalBox = Box.createVerticalBox();
		add(verticalBox);

		verticalStrut_1 = Box.createVerticalStrut(20);
		verticalBox.add(verticalStrut_1);

		horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);

		JLabel lblVideoFile = new JLabel("Video File");
		horizontalBox.add(lblVideoFile);

		horizontalStrut = Box.createHorizontalStrut(20);
		horizontalBox.add(horizontalStrut);

		_txtVideoLoc = new JTextField();
		_txtVideoLoc.setEditable(false);
		horizontalBox.add(_txtVideoLoc);
		_txtVideoLoc.setColumns(30);

		_btnBrowse = new JButton("Browse");
		horizontalBox.add(_btnBrowse);
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
						System.out.print("getting here");
						_videoPanel.setMedia(_txtVideoLoc.getText());
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
