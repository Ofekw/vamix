package controller;

import gui.MainGui;
import gui.TextTab;

import java.awt.Color;

import javax.swing.JOptionPane;



public class VideoIntroProcess extends AbstractProcess {
	/**
	 * Main process for creating the intro screen 
	 * @author ofek wittenberg
	 */

	private TextTab _tab;
	public static final String tempIntroPath = MainGui.VAMIX.getAbsolutePath()+MainGui.SEPERATOR+".tempMedia"+MainGui.SEPERATOR;

	public VideoIntroProcess(TextTab tab,int textSize, String font, String text, Color colour, String background){
		String loc = System.getProperty("user.dir");
		ShellProcess.command("rm -f "+tempIntroPath+"tempIntro.mp4");
		super.setCommand(makeCommand(textSize, font, text, colour, loc, background));
		_tab = tab;
	}
	/**
	 * returns the appropriate response to the intro process
	 */
	protected void doDone() {
		if (get() == 0) {
			if(_tab.getProcessNumber() == 1){
				ConcatVideosProcess process = new ConcatVideosProcess(_tab);
				process.execute();
			}
		} else if (get() > 0) {
			JOptionPane
			.showMessageDialog(_tab,"Something went wrong with creating an intro. Please check input media file",
					"Process Error", JOptionPane.ERROR_MESSAGE);
			_tab.enableButtons();
		} else if (get() < 0){
			JOptionPane
			.showMessageDialog(_tab,"Process cancelled",
					"Process Error", JOptionPane.ERROR_MESSAGE);
			_tab.enableButtons();
		}
	}

	protected void doProcess(String line){
		//System.out.println(line);
	}

	private String makeCommand(int textSize, String font, String text, Color colour, String loc, String background){

		return "avconv -i " + tempIntroPath +background+".mp4 -strict experimental -vf " + "\"" + "drawtext=fontfile='"+font+"':text='" + text + "':x=(main_w-text_w)/2:y=50:fontsize=" + textSize + ":fontcolor=" + toHexString(colour)
				+ "\"" + " "+tempIntroPath+"tempIntro.mp4";
	}
//copied from http://stackoverflow.com/questions/3607858/how-to-convert-a-rgb-color-value-to-an-hexadecimal-value-in-java
	public static String toHexString(Color c) {
		StringBuilder sb = new StringBuilder("#");

		if (c.getRed() < 16) sb.append('0');
		sb.append(Integer.toHexString(c.getRed()));

		if (c.getGreen() < 16) sb.append('0');
		sb.append(Integer.toHexString(c.getGreen()));

		if (c.getBlue() < 16) sb.append('0');
		sb.append(Integer.toHexString(c.getBlue()));

		return sb.toString();
	}



}

