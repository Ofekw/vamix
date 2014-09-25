package controller;

import javax.swing.JOptionPane;

import gui.AudioTab;

public class OverlayAudioProcess extends testAbPro {

	private final String loc = System.getProperty("user.dir");
	private final String binLoc = loc+System.getProperty("file.separator")+
			".tempMedia"+System.getProperty("file.seperator");
	private final String temp1 = binLoc+"sound.mp3";
	private final String temp2 = binLoc+"mergedSound.mp3";
	private AudioTab _tab;

	//avconv -i sample.avi sound.mp3 ;
	//avconv -i sound.mp3 -i audio.mp3 -filter_complex amix=inputs=2 OUTPUT.mp3 ;
	//avconv -i sample.avi -i OUTPUT.mp3 -c:v copy -map 0:0 -c:a copy -map 1:0 VIDEOwithMergedaudio.mp4

	public OverlayAudioProcess(String inputVideo, String inputAudio, String outputFile, AudioTab tab){
		super.setCommand(makeCommand(inputVideo, inputAudio, outputFile));
		_tab = tab;
	}


	protected void doProcess(String line) {
	}

	protected void doDone() {
		ShellProcess
		.command("rm -f " + temp1);
		ShellProcess
		.command("rm -f " + temp2);
		_tab.replaceFinished();
		if (get() == 0) {
			JOptionPane
			.showMessageDialog(_tab,"Overlay Complete!",
					"Overlay Complete!", JOptionPane.INFORMATION_MESSAGE);
		} else if (get() > 0) {
			JOptionPane
			.showMessageDialog(_tab,"Something went wrong with the overlay. Please check input media files",
					"Overlay Error", JOptionPane.ERROR_MESSAGE);
		}else if (get() < 0){
			JOptionPane
			.showMessageDialog(_tab,"Overlay cancelled",
					"Overlay Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private String makeCommand(String inputVideo, String inputAudio, String outputFile){
		return "avconv -i "+inputVideo+" "+temp1+" ; avconv -i "+temp1+" -i "+inputAudio+
				" -filter_complex amix=inputs=2 "+temp2+" ; avconv -i "+inputVideo+
				" -i "+temp2+" -c:v copy -map 0:0 -c:a copy -map 1:0 "+outputFile;
	}

}
