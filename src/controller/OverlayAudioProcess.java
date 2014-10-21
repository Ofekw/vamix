package controller;

import javax.swing.JOptionPane;

import gui.AudioTab;

public class OverlayAudioProcess extends AbstractProcess {
/**
 * Process to overlay the video with selected audio
 * @author patrick
 */

private final String loc = System.getProperty("user.dir");
private final String binLoc = loc+System.getProperty("file.separator")+
".tempMedia"+System.getProperty("file.seperator");
private final String temp1 = binLoc+"sound.mp3";
private final String temp2 = binLoc+"mergedSound.mp3";
private AudioTab _tab;

public OverlayAudioProcess(String inputVideo, String inputAudio, String outputFile, AudioTab tab){
super.setCommand(makeCommand(inputVideo, inputAudio, outputFile));
_tab = tab;
}


protected void doProcess(String line) {
}
//removes temp files once the process is done and shows appropriate success/error message
protected void doDone() {
ShellProcess
.command("rm -f " + temp1);
ShellProcess
.command("rm -f " + temp2);
_tab.replaceFinished();
if (get() == 0) {
JOptionPane
.showMessageDialog(_tab,"Overlay Complete!",
"Overlay Complete!", JOptionPane.INFORMATION_MESSAGE);
} else if (get() > 0) {
JOptionPane
.showMessageDialog(_tab,"Something went wrong with the overlay. Please check input media files",
"Overlay Error", JOptionPane.ERROR_MESSAGE);
}else if (get() < 0){
JOptionPane
.showMessageDialog(_tab,"Overlay cancelled",
"Overlay Error", JOptionPane.ERROR_MESSAGE);
}
}

private String makeCommand(String inputVideo, String inputAudio, String outputFile){
return "avconv -i "+inputVideo+" -i "+inputAudio+" -filter_complex amix=duration=longest" +
" -strict experimental -vcodec copy "+outputFile;

}

}