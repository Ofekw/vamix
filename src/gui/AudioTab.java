package gui;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import controller.ExtractAudioProcess;
import controller.ShellProcess;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

public class AudioTab extends Tab {
	/*
	 * This tab is used to extract the audio from the movie file, future implementation can include extracting specific time frame audio
	 */
	private JButton _extractAudio;
	protected String _newFileLoc;
	private String _mediaLoc;
	
	public void setMediaLoc(String _mediaLoc) {
		this._mediaLoc = _mediaLoc;
	}

	public AudioTab(VideoPanel panel){
		super(panel);
	}

	@Override
	protected void initialise() {
		this.setPreferredSize(new Dimension(1000, 130));
		Box verticalBox = Box.createVerticalBox();
		add(verticalBox);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalBox.add(verticalStrut);
		
		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);
		
		 _extractAudio = new JButton("Extract Audio");
		_extractAudio.setEnabled(false);
		
		_extractAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * Audio extraction process
				 */
				/*
				 * Obtains a save directory from the user
				 */
				saveDialog();
			}
		});
		horizontalBox.add(_extractAudio);
		
	}
	private void extractAudio() {
		// TODO Auto-generated method stub
		ExtractAudioProcess process = new ExtractAudioProcess();
		process.command("avconv -i "+_mediaLoc+" "+_newFileLoc);
		
	}

	public void enableButtons() {
		_extractAudio.setEnabled(true);
		
	}

	private void saveDialog() {
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

		fileChooser.setDialogTitle("Specify where to save audio");

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
			_newFileLoc=fileToSave.getAbsolutePath();
			extractAudio();
}
	}

}
