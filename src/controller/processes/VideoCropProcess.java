package controller.processes;

import gui.MediaTab;
import gui.VideoCropTab;

import javax.swing.JOptionPane;


public class VideoCropProcess extends AbstractProcess {
	/**
	 * Main process for cropping video
	 * @author ofek
	 */

	private VideoCropTab _tab;

	public VideoCropProcess(VideoCropTab videoCropTab, String startHr, String startMin, String startSec, String endHr, String endMin, String endSec){

		_tab = videoCropTab;
		super.setCommand(makeCommand(_tab.getSaveloc(), startHr, startMin, startSec, endHr, endMin, endSec));
	}
	/**
	 * returns appropriate response to process outcome
	 */
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
			.showMessageDialog(_tab,"Something went wrong with croping. Please check input media file",
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

	private String makeCommand(String saveLoc, String startHr, String startMin, String startSec, String endHr, String endMin, String endSec){
		return "avconv -i "+_tab.getMain().getVideo().getVideoLoc()+" -ss "+startHr+":"+startMin+":"+startSec+" -t "+endHr+":"+endMin+":"+endSec+" -codec copy "+saveLoc;
	
	}


}

