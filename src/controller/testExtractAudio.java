package controller;


import javax.swing.JOptionPane;
import gui.AudioTab;

//avconv -i infile.avi -an -c:v copy outfile.avi 
//Strips audio off video file

public class testExtractAudio extends testAbPro {
	
	private AudioTab _tab;
	private static final int maxValue = 100000;
	private float totalTime;
	
	public testExtractAudio(String mediaLocation, String outputLocation, AudioTab tab){
		super.setCommand(makeCommand(mediaLocation, outputLocation));
		_tab = tab;
	}
	
	protected void doDone() {
		if (get() == 0) {
			_tab.extractFinished();
			JOptionPane
			.showMessageDialog(_tab,"Extraction Complete!",
					"Extract Complete!", JOptionPane.INFORMATION_MESSAGE);
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
		if (line.contains("Duration:")){
			Float time;
			String duration = line.substring(line.indexOf(":")+1, line.indexOf(",")).trim();
			String[] times = duration.split(":");
			time = Float.parseFloat(times[0])*360;
			time += Float.parseFloat(times[1])*60;
			time += Float.parseFloat(times[2]);
			totalTime = time;
			_tab.setExtractMax(maxValue);
		}else if(line.contains("time")){
			Float time = Float.parseFloat(line.substring(line.indexOf("time")+5, line.indexOf("b")));
			Float value = time/totalTime*maxValue;
			_tab.setExtractValue(value.intValue());
		}
	}
	
	private String makeCommand(String input, String output){
		return "avconv -i "+input+" "+output;
	}
	

	

}
