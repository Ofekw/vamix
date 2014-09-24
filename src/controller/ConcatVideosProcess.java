package controller;

import java.awt.Color;

import gui.AudioTab;
import gui.TextTab;
import gui.VideoTab;

import javax.swing.JOptionPane;


public class ConcatVideosProcess extends testAbPro {

	private TextTab _tab;

	public ConcatVideosProcess(TextTab tab){

		_tab = tab;
		String loc = System.getProperty("user.dir");
		String binLoc = loc+System.getProperty("file.separator")+"tempMedia"
				+System.getProperty("file.separator");
		ShellProcess.command("rm -f "+binLoc+"1.mpeg");
		ShellProcess.command("rm -f "+binLoc+"2.mpeg");
		ShellProcess.command("rm -f "+binLoc+"3.mpeg");
		System.out.println("GETTING HERE");
		super.setCommand(makeCommand(_tab.getSaveloc()));
	}
	protected void doDone() {
		if (get() == 0) {
			_tab.progressDone();
			VideoTab videoTab = _tab.getMain().getVideo();
			videoTab.getVideoLocField().setText(_tab.getSaveloc());
			_tab.getMain().getPlayer().resetPlayer();
			_tab.getMain().getPlayer().play();
			
		} else if (get() > 0) {
			_tab.progressReset();
			JOptionPane
			.showMessageDialog(_tab,"Something went wrong with creating an intro. Please check input media file",
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

	private String makeCommand(String saveLoc){
		String loc = System.getProperty("user.dir");
		String binLoc = loc+System.getProperty("file.separator")+"tempMedia"
				+System.getProperty("file.separator");
		
		if (!_tab.userText()[0].isEmpty() && !_tab.userText()[1].isEmpty()){
			return "avconv -i "+binLoc+"tempIntro.mp4 -qscale 1 "+binLoc
					+"1.mpeg; avconv -i "+_tab.getMain().getVideo().getVideoLoc()+" -qscale 1 "+binLoc+"2.mpeg; avconv -i "+binLoc
					+"tempOutro.mp4 -qscale 1 "+binLoc+"3.mpeg; cat "+binLoc+"1.mpeg "+binLoc
					+"2.mpeg "+binLoc+"3.mpeg | avconv -f mpeg -i - -vcodec mpeg4 -strict experimental "+saveLoc;
			
		}else if (_tab.userText()[0].isEmpty() && !_tab.userText()[1].isEmpty()){
			return "avconv -i "+_tab.getMain().getVideo().getVideoLoc()+" -qscale 1 "+binLoc+"2.mpeg; avconv -i "+binLoc
					+"tempOutro.mp4 -qscale 1 "+binLoc+"3.mpeg; cat "+binLoc
					+"2.mpeg "+binLoc+"3.mpeg | avconv -f mpeg -i - -vcodec mpeg4 -strict experimental "+saveLoc;
		}else if (!_tab.userText()[0].isEmpty() && _tab.userText()[1].isEmpty()){
			return "avconv -i "+binLoc+"tempIntro.mp4 -qscale 1 "+binLoc
					+"1.mpeg; avconv -i "+_tab.getMain().getVideo().getVideoLoc()+" -qscale 1 "+binLoc+"2.mpeg; cat "+binLoc+"1.mpeg "+binLoc
					+"2.mpeg | avconv -f mpeg -i - -vcodec mpeg4 -strict experimental "+saveLoc;
		}
		return null;
	}


}

