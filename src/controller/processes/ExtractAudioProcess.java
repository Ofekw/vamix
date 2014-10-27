package controller.processes;


import javax.swing.JOptionPane;
import gui.AudioTab;

/**
 * Uses the file linux process to call extract the audio
 */

public class ExtractAudioProcess extends AbstractProcess {

	private AudioTab _tab;
	private static final int maxValue = 100000;
	private float totalTime;
	private String _duration;

	public ExtractAudioProcess(String mediaLocation, String outputLocation, AudioTab tab, String start, String duration){
		super.setCommand(makeCommand(mediaLocation, outputLocation, start, duration));
		_tab = tab;
		_duration = duration;
	}
//returns appropriate error messages or success for failed process
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

	protected void doProcess(String line){

		if (line.contains("Duration:")){
			Float time;
			String duration;
			if (_duration.equals("00:00:00")){
				duration = line.substring(line.indexOf(":")+1, line.indexOf(",")).trim();
			}else{
				duration = _duration;
			}
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

	//creates the avconv command for the audio process
	private String makeCommand(String input, String output, String start, String duration){
		if(duration.equals("00:00:00")){
			return "avconv -i "+input+ " " +"-acodec copy " +output;
		}else{
			return "avconv -i "+input+ " -ss " +start + " -t " +duration + " "+output;
		}
	}




}
