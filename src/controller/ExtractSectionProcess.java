package controller;

import gui.AudioTab;

import javax.swing.JOptionPane;

public class ExtractSectionProcess extends testAbPro{
	
	private AudioTab _tab;
	
	public ExtractSectionProcess(AudioTab tab, String inputFile, String startTime, String duration, String outputName){
		super.setCommand(makeCommand(inputFile, startTime, duration, outputName));
		_tab = tab;
	}
	
	protected void doDone() {
		_tab.extractFinished();
		if (get() == 0) {
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
	
	//create avconv command from inputs
	private String makeCommand(String inputFile, String startTime, String duration, String outputName){
		return "avconv "+inputFile
				+" -ss " +startTime+ " -t " + duration + " "+outputName + ".mp3";
	}

}
