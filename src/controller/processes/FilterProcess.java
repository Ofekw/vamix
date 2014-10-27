package controller.processes;

import gui.FilterTab;
import gui.MediaTab;

import javax.swing.JOptionPane;

/**
 * Uses the file linux process to call specific audio manipulation calls on the media
 * @param filter tab
 */
public class FilterProcess extends AbstractProcess {

	private FilterTab _tab;

	public FilterProcess(FilterTab tab){

		_tab = tab;
		super.setCommand(makeCommand(_tab.getSaveloc()));
	}
	
	//returns appropriate error/success for the completion of the process
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
//calls specific effect on the mediafile
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

