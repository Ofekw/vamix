package controller;

import java.awt.Color;

import gui.AudioTab;
import gui.TextTab;

import javax.swing.JOptionPane;


public class VideoOutroProcess extends testAbPro {
	
	private TextTab _tab;

	public VideoOutroProcess(TextTab tab,int textSize, String font, String text, Color colour){
		String loc = System.getProperty("user.dir");
		ShellProcess.command("rm -f "+loc+System.getProperty("file.separator")+"tempMedia"+System.getProperty("file.separator")+"tempOutro.mp4");
		super.setCommand(makeCommand(textSize, font, text, colour, loc));
		_tab = tab;
}
	protected void doDone() {
		if (get() == 0) {
			
		} else if (get() > 0) {
							JOptionPane
									.showMessageDialog(_tab,"Something went wrong with creating an outro. Please check input media file",
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
	
	private String makeCommand(int textSize, String font, String text, Color colour, String loc){
		
		String binLoc = loc+System.getProperty("file.separator")+"tempMedia"
				+System.getProperty("file.separator");
		return "avconv -i " + binLoc + "background.mp4 -strict experimental -vf " + "\"" + "drawtext=fontfile='/usr/share/fonts/truetype/ttf-dejavu/DejaVuSans.ttf':text='" + text + "':x=(main_w-text_w)/2:y=50:fontsize=" + textSize + ":fontcolor=" + toHexString(colour)
				+ "\"" + " "+binLoc+"tempOutro.mp4";
	}
	
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

