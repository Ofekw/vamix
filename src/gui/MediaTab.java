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
import javax.swing.JSeparator;

import controller.CheckFile;
import net.miginfocom.swing.MigLayout;
import javax.swing.JPanel;

public class MediaTab extends Tab {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7118585127413410576L;
	private JTextField _txtVideoLoc;
	private JButton _btnBrowse;
	private MainGui _main ;
	private Download download;

	public MediaTab(VideoPanel panel, MainGui main) {
		super(panel);
		this._main = main;
	}

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
						
						download = new Download((JPanel) null);
						download.setToolTipText("Media download");
						download.setName("");
						add(download, "cell 0 3,grow");
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

	public String getVideoLoc() {
		return _txtVideoLoc.getText();
	}

	public JTextField getVideoLocField() {
		return _txtVideoLoc;
	}
	
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
