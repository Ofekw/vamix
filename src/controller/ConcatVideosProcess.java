package controller;

import java.awt.Color;

import gui.AudioTab;
import gui.TextTab;

import javax.swing.JOptionPane;


public class ConcatVideosProcess extends testAbPro {
	
	private TextTab _tab;

	public ConcatVideosProcess(TextTab tab){
		
		super.setCommand(makeCommand(_tab.getSaveloc()));
		_tab = tab;
}
	protected void doDone() {
		if (get() == 0) {
			
		} else if (get() > 0) {
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
		return "avconv -i "+binLoc+"tempIntro.mp4 -qscale 1 "+binLoc
				+"1.mpeg; avconv -i "+saveLoc+" -qscale 1 "+binLoc+"2.mpeg avconv -i "+binLoc
				+"tempOutro.mp4 -qscale 1 "+binLoc+"3.mpeg; cat "+binLoc+"1.mpeg "+binLoc
				+"2.mpeg "+binLoc+"3.mpeg | avconv -f mpeg -i - -vcodec mpeg4 -strict experimental output.mp4";
	}

	

}

