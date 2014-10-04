package controller;

import java.awt.Color;

import gui.AudioTab;
import gui.FilterTab;
import gui.TextTab;
import gui.MediaTab;
import gui.VideoCropTab;

import javax.swing.JOptionPane;


public class VideoCropProcess extends AbstractProcess {

	private VideoCropTab _tab;

	public VideoCropProcess(VideoCropTab videoCropTab){

		_tab = videoCropTab;
		super.setCommand(makeCommand(_tab.getSaveloc()));
	}
	protected void doDone() {
		if (get() == 0) {
			_tab.progressDone();
			MediaTab videoTab = _tab.getMain().getVideo();
			videoTab.getVideoLocField().setText(_tab.getSaveloc());
			_tab.getMain().getPlayer().setMedia(_tab.getSaveloc());
			_tab.getMain().getPlayer().play();
			
		} else if (get() > 0) {
			_tab.progressReset();
			JOptionPane
			.showMessageDialog(_tab,"Something went wrong with applying the filter. Please check input media file",
					"Process Error", JOptionPane.ERROR_MESSAGE);
			_tab.progressReset();
		} else if (get() < 0){
			JOptionPane
			.showMessageDialog(_tab,"Process cancelled",
					"Process Error", JOptionPane.ERROR_MESSAGE);
			_tab.progressReset();
		}
		_tab.enableButtons();
	}

	protected void doProcess(String line){
		System.out.println(line);

	}

	private String makeCommand(String saveLoc){
			String source = _tab.getFilterSelection();
			if ( source == "blur") {
				return "avconv -i "+_tab.getMain().getVideo().getVideoLoc()+" -strict experimental -vf " + "\"" + "boxblur=10:1:0:0:0:0"+ "\" " + saveLoc;
			} else if (source == "border") {
				return "avconv -i "+_tab.getMain().getVideo().getVideoLoc()+" -strict experimental -vf " + "\"" + "drawbox=0:0:00:00:red"+ "\" " + saveLoc;
			} else if (source == "flipH") {
				return "avconv -i "+_tab.getMain().getVideo().getVideoLoc()+" -strict experimental -vf " + "\"" + "hflip"+ "\" " + saveLoc;
			} else if (source == "flipV") {
				return "avconv -i "+_tab.getMain().getVideo().getVideoLoc()+" -strict experimental -vf " + "\"" + "vflip"+ "\" " + saveLoc;
			} else if (source == "mono") {
				return "avconv -i "+_tab.getMain().getVideo().getVideoLoc()+" -strict experimental -vf " + "\"" + "format=monow, pixdesctest"+ "\" " + saveLoc;
		}
			return null;
	
	}


}

