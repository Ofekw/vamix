package controller;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 * Class for saving and loading settings
 * @author Patrick Poole
 *
 */
public class SaveLoadState {

	public static final String HOME = System.getProperty("user.home");
	public static final String SEPERATOR = File.separator;
	public static final File VAMIX = new File(HOME+SEPERATOR+".vamix");
	public static final String SAVE = new String(HOME+SEPERATOR+".vamix"+SEPERATOR);
	
	private final String SAVEMESSAGE = "Vamix Save File";
	private final File SaveFile;
	private JTextField _intro;
	private JTextField _end;
	private JTextPane _preview;
	private JSpinner _fontSize;
	private Color _fontColour;
	private JComboBox<String> _fontType; 

	private JSpinner _startHours;
	private JSpinner _startMinutes;
	private JSpinner _startSeconds;

	private JSpinner _durationHours;
	private JSpinner _durationMinutes;
	private JSpinner _durationSeconds;

	private JTextField _inputAudio;
	private JTextField _video;


	private String saveFormat;

	/**
	 * Constructor for creating object for saving/loading audio settings
	 * @param video
	 * @param startHours
	 * @param startMinutes
	 * @param startSeconds
	 * @param durationHours
	 * @param durationMinutes
	 * @param durationSeconds
	 * @param inputAudio
	 * @param saveFileName
	 */
	public SaveLoadState(JTextField video, JSpinner startHours, JSpinner startMinutes, JSpinner startSeconds,
			JSpinner durationHours, JSpinner durationMinutes, JSpinner durationSeconds, JTextField inputAudio
			, String saveFileName){

		_video = video;
		_inputAudio = inputAudio;
		_startHours = startHours;
		_startMinutes = startMinutes;
		_startSeconds = startSeconds;

		_durationHours = durationHours;
		_durationMinutes = durationMinutes;
		_durationSeconds = durationSeconds;

		saveFormat = format(_video.getText(), _inputAudio.getText(), _startHours.getValue().toString(),
				_startMinutes.getValue().toString(), _startSeconds.getValue().toString(), _durationHours.getValue().toString(),
				_durationMinutes.getValue().toString(), _durationSeconds.getValue().toString());

		SaveFile = new File(SAVE+saveFileName);
	}

	/**
	 * Constructor for saving/loading text settings
	 * @param intro
	 * @param end
	 * @param preview
	 * @param fontSize
	 * @param fontColour
	 * @param fontType
	 * @param saveFileName
	 */
	public SaveLoadState(JTextField intro, JTextField end, JTextPane preview, 
			JSpinner fontSize, Color fontColour, JComboBox<String> fontType, 
			String saveFileName){

		_intro = intro;
		_end = end;
		_preview = preview;
		_fontSize = fontSize;
		_fontColour = fontColour;
		_fontType = fontType;

		saveFormat = format(intro.getText(), end.getText(), preview.getText(), 
				fontSize.getValue().toString(), _fontColour.getRGB()+"",
				fontType.getSelectedItem().toString());
		SaveFile = new File(SAVE+saveFileName);
	}

	/**
	 * Saves settings to file
	 */
	public void save(){
		checkVamix();
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(SaveFile, true)));
			out.write(saveFormat+"\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads settings from file
	 * @param loadingText: True if loading text settings, false if loading audio settings
	 * @return: RGB Color or 0 if not applicable
	 */
	public int load(boolean loadingText){
		String [] textSettings = null;
		String [] audioSettings = null;
		try {
			List<String> lines = Files.readAllLines(SaveFile.toPath(), Charset.defaultCharset());
			if (!lines.get(0).equals(SAVEMESSAGE)){
				return 0;
			}else{
			audioSettings = lines.get(1).split(":");
			textSettings = lines.get(2).split(":");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (loadingText){
			_intro.setText(textSettings[0]);
			_end.setText(textSettings[1]);
			_preview.setText(textSettings[2]);
			_fontSize.setValue(Integer.parseInt(textSettings[3]));
			_fontType.setSelectedItem(textSettings[5]);
			return Integer.parseInt(textSettings[4]);
		}else{
			_video.setText(audioSettings[0]);
			_inputAudio.setText(audioSettings[1]);
			_startHours.setValue(Integer.parseInt(audioSettings[2]));
			_startMinutes.setValue(Integer.parseInt(audioSettings[3]));
			_startSeconds.setValue(Integer.parseInt(audioSettings[4]));

			_durationHours.setValue(Integer.parseInt(audioSettings[5]));
			_durationMinutes.setValue(Integer.parseInt(audioSettings[6]));
			_durationSeconds.setValue(Integer.parseInt(audioSettings[7]));
			return 0;
		}
	}

	/**
	 * Formats Audio settings into string for saving
	 * @param video
	 * @param inputAudio
	 * @param startHours
	 * @param startMinutes
	 * @param startSeconds
	 * @param durationHours
	 * @param durationMinutes
	 * @param durationSeconds
	 * @return
	 */
	private String format(String video, String inputAudio, String startHours, String startMinutes, String startSeconds,
			String durationHours, String durationMinutes, String durationSeconds){

		return video+":"+inputAudio+":"+startHours+":"+startMinutes+":"+startSeconds+":"+durationHours+":"+durationMinutes
				+":"+durationSeconds;
	}

	/**
	 * Formats text settings into string for saving
	 * @param intro
	 * @param end
	 * @param preview
	 * @param fontSize
	 * @param fontColour
	 * @param fontType
	 * @return
	 */
	private String format(String intro, String end, String preview, 
			String fontSize, String fontColour, String fontType){
		return intro+":"+end+":"+preview+":"+fontSize+":"+fontColour+":"+fontType;		
	}

	/**
	 * Checks that the .vamix file exists, create's it if it does not
	 */
	private void checkVamix(){
		if (!VAMIX.exists()){
			VAMIX.mkdir();
		}
		if (!SaveFile.exists()){
			try {
				SaveFile.createNewFile();
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(SaveFile, true)));
				out.write(SAVEMESSAGE+"\n");
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
