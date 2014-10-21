package gui;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import controller.CheckFile;
import net.miginfocom.swing.MigLayout;

public class MediaTab extends Tab {
	/**
	 * The main media tab for file selection. Used in editing and media play-back
	 * @author Ofek Wittenberg
	 */
	private static final long serialVersionUID = -7118585127413410576L;
	private JTextField _txtVideoLoc;
	private JButton _btnBrowse;
	private MainGui _main ;

	public MediaTab(VideoPanel panel, MainGui main) {
		super(panel);
		this._main = main;
	}
	/**
	 * Creates the UI for the tab
	 */
	protected void initialise() {
		this.setPreferredSize(new Dimension(1000, 180));
		setLayout(new MigLayout("", "[980px,grow]", "[][][][180px,grow]"));
		
				JLabel lblVideoFile = new JLabel("Target/Playback Media:");
				add(lblVideoFile, "flowx,cell 0 1");
		
				_txtVideoLoc = new JTextField();
				add(_txtVideoLoc, "cell 0 1,growx");
				_txtVideoLoc.setToolTipText("Location of playback/edit media");
				_txtVideoLoc.setPreferredSize(new Dimension(5,10));
				
						_btnBrowse = new JButton("Browse");
						add(_btnBrowse, "cell 0 1");
						
						
		_btnBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (_btnBrowse.isEnabled()) {
					fileBrowser();
				}
			}
			/**
			 * calls the file browser to obtain media player
			 */
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
						if (new CheckFile(true).checkFileType(_txtVideoLoc.getText()) ||
								new CheckFile(false).checkFileType(_txtVideoLoc.getText())){
							_videoPanel.setMedia(_txtVideoLoc.getText());
							_videoPanel.enableSlider();
							CheckFile check = new CheckFile(false);
							if(!check.checkFileType(selectedFile.getAbsolutePath())){
								//Enables all the buttons after the media check
								_main.getAudio().enableExtractButtons();
								_main.getAudio().setMediaLoc(_txtVideoLoc.getText());
								_main.getFilters().enableButtons();
								_main.getCrop().enableButtons();
								_main.getSubtitles().enableButtons();
								
							}
						}else{
							_txtVideoLoc.setText("");
							JOptionPane.showMessageDialog(MediaTab.this, "Invalid Media File!",
									"File Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});

	}

	/**
	 * simple file check method
	 */
	private boolean fileExists() {
		File f = new File(_txtVideoLoc.getText());
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * checks that the specified path is legitimate
	 */
	private void filePathInvalid() {
		JOptionPane.showMessageDialog(this, "File path is invalid",
				"Location Error", JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * returns the location of the valid media file
	 * @return
	 */
	public String getVideoLoc() {
		return _txtVideoLoc.getText();
	}
	/**
	 * returns the full media location text tab object
	 * @return
	 */
	public JTextField getVideoLocField() {
		return _txtVideoLoc;
	}
	/**
	 * Checks that the media file is playable
	 * @param selectedFile
	 * @return boolean (and error message pop up)
	 */
	public boolean checkMediaFile(File selectedFile){
		if (new CheckFile(true).checkFileType(_txtVideoLoc.getText()) ||
				new CheckFile(false).checkFileType(_txtVideoLoc.getText())){
			_videoPanel.setMedia(_txtVideoLoc.getText());
			_videoPanel.enableSlider();
			CheckFile check = new CheckFile(false);
			if(!check.checkFileType(selectedFile.getAbsolutePath())){
				_main.getAudio().enableExtractButtons();
				_main.getAudio().setMediaLoc(_txtVideoLoc.getText());
				return true;
			}
			return true;
		}else{
			_txtVideoLoc.setText("");
			JOptionPane.showMessageDialog(MediaTab.this, "Invalid Media File!",
					"File Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
}
