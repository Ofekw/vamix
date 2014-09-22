package controller;

import javax.swing.JOptionPane;

import gui.AudioTab;

public class ReplaceAudioProcess extends testAbPro {

	//avconv -i inputVideo.mov -i inputAudio.mp3 -c:v copy -c:a copy -map 0:1 -map 1:0 outputVideo.mov
	
	//5.7 in avconv for video options for creating film from images, can then concatenate two
	//video files into one
	
	private AudioTab _tab;
	
	public ReplaceAudioProcess(String inputVideo, String inputAudio, String outputVideo, AudioTab tab){
		super.setCommand(makeCommand(inputVideo, inputAudio, outputVideo));
		_tab = tab;
	}
	
	protected void doDone() {
		if (get() == 0) {
			_tab.enableExtractButtons();
			_tab.progressBarFinished();
		} else if (get() > 0) {
							JOptionPane
									.showMessageDialog(_tab,"Something went wrong with the replacement. Please check input media file",
											"Replace Error", JOptionPane.ERROR_MESSAGE);
		}else if (get() >0){
			JOptionPane
			.showMessageDialog(_tab,"Process cancelled",
					"Replace Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	protected void doProcess(String line){
		System.out.println(line);
	}
	
	private String makeCommand(String inputVideo, String inputAudio, String outputVideo){
		return "avconv -i "+ inputVideo+" -i "+inputAudio+" -c:v copy -c:a copy -map 0:1 -map 1:0 "+outputVideo+".avi";
	}
}
