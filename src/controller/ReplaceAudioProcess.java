package controller;

import javax.swing.JOptionPane;

import gui.AudioTab;

public class ReplaceAudioProcess extends testAbPro {

	//	avconv -i sample.avi -i testAudio.mp3 -c:v copy -map 0:0 -c:a copy -map 1:0 VIDEOwithNEWaudio.mp4


	private AudioTab _tab;
	private float totalTime;
	private static final int maxValue = 100000;
	private boolean _removeAudio;

	/**
	 * 
	 * @param inputVideo
	 * @param inputAudio
	 * @param outputVideo
	 * @param tab
	 * @param doProcessing: True if running remove audio and want processing, false if replacing audio 
	 */
	public ReplaceAudioProcess(String inputVideo, String inputAudio, String outputVideo,
			AudioTab tab, boolean removeAudio){
		super.setCommand(makeCommand(inputVideo, inputAudio, outputVideo));
		_tab = tab;
		_removeAudio = removeAudio;
	}

	protected void doDone() {
		_tab.replaceFinished();
		if (get() == 0) {
			JOptionPane
			.showMessageDialog(_tab,"Replacement Complete!",
					"Replace Complete!", JOptionPane.INFORMATION_MESSAGE);
		} else if (get() > 0) {
			JOptionPane
			.showMessageDialog(_tab,"Something went wrong with the replacement. Please check input media files",
					"Replace Error", JOptionPane.ERROR_MESSAGE);
		}else if (get() < 0){
			JOptionPane
			.showMessageDialog(_tab,"Process cancelled",
					"Replace Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	protected void doProcess(String line){
		if (_removeAudio){
			if (line.contains("Duration:")){
				Float time;
				String duration = line.substring(line.indexOf(":")+1, line.indexOf(",")).trim();
				String[] times = duration.split(":");
				time = Float.parseFloat(times[0])*360;
				time += Float.parseFloat(times[1])*60;
				time += Float.parseFloat(times[2]);
				totalTime = time;
				_tab.setExtractMax(maxValue);
			}else if(line.contains("time=")){
				Float time = Float.parseFloat(line.substring(line.indexOf("time")+5, line.indexOf("b")));
				Float value = time/totalTime*maxValue;
				_tab.setExtractValue(value.intValue());
			}
		}
	}

	/**
	 * Makes command for replacing audio on video
	 * @param inputVideo: input video path
	 * @param inputAudio: input audio path or blank string if audio to be removed from video
	 * @param outputVideo: output video path
	 * @return
	 */
	private String makeCommand(String inputVideo, String inputAudio, String outputVideo){
		if (_removeAudio){
			//Replaces audio with nothing
			return "avconv -i " +inputVideo+ " -an -c:v copy " +outputVideo; 
		}else{
			return "avconv -i "+ inputVideo+" -i "+inputAudio+" -c:v copy -map 0:0 -c:a copy -map 1:0 "+outputVideo;
		}
	}
}
