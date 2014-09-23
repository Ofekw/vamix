package controller;

import javax.swing.JOptionPane;

import org.omg.stub.java.rmi._Remote_Stub;

import gui.AudioTab;

public class ReplaceAudioProcess extends testAbPro {
	
//	avconv -i sample.avi -i testAudio.mp3 -c:v copy -map 0:0 -c:a copy -map 1:0 VIDEOwithNEWaudio.mp4

	
	private AudioTab _tab;
	
	public ReplaceAudioProcess(String inputVideo, String inputAudio, String outputVideo, AudioTab tab){
		super.setCommand(makeCommand(inputVideo, inputAudio, outputVideo));
		_tab = tab;
	}
	
	protected void doDone() {
		if (get() == 0) {
			_tab.replaceFinished();
			JOptionPane
			.showMessageDialog(_tab,"Replacement Complete!",
					"Replace Complete!", JOptionPane.INFORMATION_MESSAGE);
		} else if (get() > 0) {
							JOptionPane
									.showMessageDialog(_tab,"Something went wrong with the replacement. Please check input media files",
											"Replace Error", JOptionPane.ERROR_MESSAGE);
		}else if (get() > 0){
			JOptionPane
			.showMessageDialog(_tab,"Process cancelled",
					"Replace Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	protected void doProcess(String line){
	}
	
	private String makeCommand(String inputVideo, String inputAudio, String outputVideo){
		return "avconv -i "+ inputVideo+" -i "+inputAudio+" -c:v copy -map 0:0 -c:a copy -map 1:0 "+outputVideo;
	}
}
