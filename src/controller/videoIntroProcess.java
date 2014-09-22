package controller;

import java.awt.Color;

import gui.AudioTab;
import gui.TextTab;

import javax.swing.JOptionPane;

import com.sun.jndi.url.rmi.rmiURLContext;

public class videoIntroProcess extends testAbPro {
	
	private TextTab _tab;

	public videoIntroProcess(TextTab tab,int textSize, String font, String text, Color colour){
		String loc = System.getProperty("user.dir");
		ShellProcess.command("rm -f "+loc+System.getProperty("file.separator")+"tempIntro.mp4");
		super.setCommand(makeCommand(textSize, font, text, colour, loc));
		_tab = tab;
}
	protected void doDone() {
		if (get() == 0) {
		} else if (get() > 0) {
							JOptionPane
									.showMessageDialog(_tab,"Something went wrong with the extract. Please check input media file",
											"Extract Error", JOptionPane.ERROR_MESSAGE);
		} else if (get() < 0){
			JOptionPane
			.showMessageDialog(_tab,"Process cancelled",
					"Extract Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void doProcess(String line){
		System.out.println(line);
		
	}
	
	private String makeCommand(int textSize, String input, String output, Color colour, String loc){
		
		return "avconv -i "+loc+System.getProperty("file.separator")+"tempMedia"
		+System.getProperty("file.separator")+"background.mp4 -strict experimental -vf "+
				"\""+"drawtext=fontfile='/usr/share/fonts/truetype/ttf-dejavu/DejaVuSans.ttf':text='"+input+
				"':x=(main_w-text_w)/2:y=50:fontsize="+textSize+":fontcolor="+colour+"\""+" tempIntro.mp4";
	}
	

	

}

