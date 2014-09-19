package controller;

import javax.swing.JOptionPane;

import gui.AudioTab;

public class ReplaceAudioProcess extends testAbPro {

	//avconv -i inputVideo.mov -i inputAudio.mp3 -c:v copy -c:a copy -map 0:1 -map 1:0 outputVideo.mov
	
	//5.7 in avconv for video options for creating film from images, can then concatenate two
	//video files into one
	
	private AudioTab _tab;
	
	public ReplaceAudioProcess(String cmd, AudioTab tab){
		super.setCommand(cmd);
		_tab = tab;
	}
	
	protected void doDone() {
		if (get() == 0) {
			_tab.enableButtons();
			_tab.progressBarFinished();
		} else if (get() != 0) {
							JOptionPane
									.showMessageDialog(_tab,"Something went wrong with the extract. Please check input media file",
											"Extract Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
}
