package controller;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import gui.AudioTab;

public class testExtractAudio extends testAbPro {
	
	private AudioTab _tab;
	
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
		}
		System.out.println(line);
	}
	

}
