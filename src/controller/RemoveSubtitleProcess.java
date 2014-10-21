package controller;

import java.awt.Color;

import gui.AudioTab;
import gui.MainGui;
import gui.SubTitles;
import gui.TextTab;
import gui.MediaTab;

import javax.swing.JOptionPane;


public class RemoveSubtitleProcess extends AbstractProcess {

	private SubTitles _tab;
	public static final String tempPath = MainGui.VAMIX.getAbsolutePath()+MainGui.SEPERATOR+".tempMedia"+MainGui.SEPERATOR;
	public RemoveSubtitleProcess(SubTitles tab){

		_tab = tab;
		super.setCommand(makeCommand());
	}
	protected void doDone() {
		if (get() == 0) {
			AddSubtitleProcess add = new AddSubtitleProcess(_tab);
			add.execute();
		
		} else if (get() > 0) {
//			_tab.progressReset();
//			JOptionPane
//			.showMessageDialog(_tab,"Something went wrong with removing embedded SRT file. Please check input media file contains subtitles",
//					"Process Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("process is > 0");
			if (get() == 0) {
				AddSubtitleProcess add = new AddSubtitleProcess(_tab);
				add.execute();
			}
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
		return ("avconv -i " + _tab.getMain().getVideo().getVideoLoc() + " -map 0:0 -map 0:1 -vcodec copy -acodec libmp3lame " + tempPath + "removedSRT.mkv");
	}


}

