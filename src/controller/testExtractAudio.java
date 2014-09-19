package controller;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import gui.AudioTab;

public class testExtractAudio extends testAbPro {
	
	private AudioTab _tab;
	
	private static final int maxValue = 100000;
	
	private float totalTime;
	
	public testExtractAudio(String cmd, AudioTab tab){
		super.setCommand(cmd);
		_tab = tab;
	}
	
	protected void doDone() {
		if (get() == 0) {
			_tab.enableButtons();
			_tab.progressBarFinished();
		} else if (get() != 0) {
							JOptionPane
									.showMessageDialog(_tab,"Something went wrong with the extract. Please check input media file",
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
			_tab.setProgressBarMax(maxValue);
		}else if(line.contains("time")){
			Float time = Float.parseFloat(line.substring(line.indexOf("time")+5, line.indexOf("b")));
			Float value = time/totalTime*maxValue;
			_tab.setProgressValue(value.intValue());
		}
		//System.out.println(line);
	}
	

}