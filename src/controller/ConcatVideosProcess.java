package controller;

import java.awt.Color;

import gui.AudioTab;
import gui.MainGui;
import gui.TextTab;
import gui.VideoTab;

import javax.swing.JOptionPane;


public class ConcatVideosProcess extends AbstractProcess {

	private TextTab _tab;
	public static final String tempPath = MainGui.VAMIX.getAbsolutePath()+MainGui.SEPERATOR+".tempMedia"+MainGui.SEPERATOR;
	public ConcatVideosProcess(TextTab tab){

		_tab = tab;
		ShellProcess.command("rm -f "+tempPath+"1.mpeg");
		ShellProcess.command("rm -f "+tempPath+"2.mpeg");
		ShellProcess.command("rm -f "+tempPath+"3.mpeg");
		super.setCommand(makeCommand(_tab.getSaveloc()));
	}
	protected void doDone() {
		if (get() == 0) {
			_tab.progressDone();
			VideoTab videoTab = _tab.getMain().getVideo();
			videoTab.getVideoLocField().setText(_tab.getSaveloc());
			_tab.getMain().getPlayer().setMedia(_tab.getSaveloc());
			_tab.getMain().getPlayer().play();
		
		} else if (get() > 0) {
			_tab.progressReset();
			JOptionPane
			.showMessageDialog(_tab,"Something went wrong with concating. Please check input media file",
					"Process Error", JOptionPane.ERROR_MESSAGE);
		} else if (get() < 0){
			JOptionPane
			.showMessageDialog(_tab,"Process cancelled",
					"Process Error", JOptionPane.ERROR_MESSAGE);
		}
		_tab.enableButtons();
	}

	protected void doProcess(String line){
		//System.out.println(line);

	}

	private String makeCommand(String saveLoc){
		//we need to enable a slight pause, in order to avoid concurrency errors when concatonating the media files
		this.setPause(true);
		
		if (!_tab.userText()[0].isEmpty() && !_tab.userText()[1].isEmpty()){
			return "avconv -i "+tempPath+"tempIntro.mp4 -qscale 1 "+tempPath
					+"1.mpeg; avconv -i "+_tab.getMain().getVideo().getVideoLoc()+" -qscale 1 "+tempPath+"2.mpeg; avconv -i "+tempPath
					+"tempOutro.mp4 -qscale 1 "+tempPath+"3.mpeg; cat "+tempPath+"1.mpeg "+tempPath
					+"2.mpeg "+tempPath+"3.mpeg | avconv -f mpeg -i - -vcodec mpeg4 -strict experimental "+saveLoc;
			
		}else if (_tab.userText()[0].isEmpty() && !_tab.userText()[1].isEmpty()){
			return "avconv -i "+_tab.getMain().getVideo().getVideoLoc()+" -qscale 1 "+tempPath+"2.mpeg; avconv -i "+tempPath
					+"tempOutro.mp4 -qscale 1 "+tempPath+"3.mpeg; cat "+tempPath
					+"2.mpeg "+tempPath+"3.mpeg | avconv -f mpeg -i - -vcodec mpeg4 -strict experimental "+saveLoc;
		}else if (!_tab.userText()[0].isEmpty() && _tab.userText()[1].isEmpty()){
			return "avconv -i "+tempPath+"tempIntro.mp4 -qscale 1 "+tempPath
					+"1.mpeg; avconv -i "+_tab.getMain().getVideo().getVideoLoc()+" -qscale 1 "+tempPath+"2.mpeg; cat "+tempPath+"1.mpeg "+tempPath
					+"2.mpeg | avconv -f mpeg -i - -vcodec mpeg4 -strict experimental "+saveLoc;
		}
		return null;
	}


}

