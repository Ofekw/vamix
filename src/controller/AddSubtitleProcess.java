package controller;

import java.awt.Color;

import gui.AudioTab;
import gui.MainGui;
import gui.SubTitles;
import gui.TextTab;
import gui.MediaTab;

import javax.swing.JOptionPane;


public class AddSubtitleProcess extends AbstractProcess {

	private SubTitles _tab;
	public static final String tempPath = MainGui.VAMIX.getAbsolutePath()+MainGui.SEPERATOR+".tempMedia"+MainGui.SEPERATOR;
	public AddSubtitleProcess(SubTitles tab){

		_tab = tab;
		super.setCommand(makeCommand());

	}
	protected void doDone() {
		if (get() == 0) {
			
		
		} else if (get() > 0) {
			JOptionPane
			.showMessageDialog(_tab,"Something went wrong with embedding SRT file. Please check input media file",
					"Process Error", JOptionPane.ERROR_MESSAGE);
		} else if (get() < 0){
			JOptionPane
			.showMessageDialog(_tab,"Process cancelled",
					"Process Error", JOptionPane.ERROR_MESSAGE);
		}
		_tab.enableButtons();
	}

	protected void doProcess(String line){
		System.out.println(line);

	}

	private String makeCommand(){
		
		
		return "avconv -i " + tempPath + "removedSRT.mkv" + " -i " + tempPath+"tempSRT.srt" + 
				" -vcodec h264 -acodec ac3 -scodec ass -metadata:s:s:0 language=eng " + _tab.getSaveloc();
	}


}

