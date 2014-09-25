package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class SaveLoadState {
	
	public static final String HOME = System.getProperty("user.home");
	public static final String SEPERATOR = File.separator;
	public static final File VAMIX = new File(HOME+SEPERATOR+".vamix");
	public static final String SAVE = new String(HOME+SEPERATOR+".vamix"+SEPERATOR);
	private final File SaveFile;
	private JTextField _intro;
	private JTextField _end;
	private JTextPane _preview;
	private JSpinner _fontSize;
	private JComboBox<String> _fontColour;
	private JComboBox<String> _fontType; 
	private String saveFileName;
	
	
	private String saveFormat;
	
	public SaveLoadState(JTextField intro, JTextField end, JTextPane preview, 
			JSpinner fontSize, JComboBox<String> fontColour, JComboBox<String> fontType, 
			 String saveFileName){
		
		String colour;
		if(fontColour == null){
			colour = null;
		}else{
			colour = fontColour.getSelectedItem().toString();
		}
		_intro = intro;
		_end = end;
		_preview = preview;
		_fontSize = fontSize;
		_fontColour = fontColour;
		_fontType = fontType;
		
		saveFormat = format(intro.getText(), end.getText(), preview.getText(), 
				fontSize.getValue().toString(), colour,
				fontType.getSelectedItem().toString());
		SaveFile = new File(SAVE+saveFileName);
	}
	
	public void save(){
		checkVamix();
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(SaveFile)));
			out.write(saveFormat);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load(){
		String [] settings = null;
		try {
			List<String> lines = Files.readAllLines(SaveFile.toPath(), Charset.defaultCharset());
			for(String s:lines){
				settings = s.split(":");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		_intro.setText(settings[0]);
		_end.setText(settings[1]);
		_preview.setText(settings[2]);
		_fontSize.setValue(Integer.parseInt(settings[3]));
		if (settings[4].equals("null")){
			_fontColour = null;
		}else{
			_fontColour.setSelectedItem(settings[4]);
		}
		_fontType.setSelectedItem(settings[5]);
	}
	
	private String format(String intro, String end, String preview, 
			String fontSize, String fontColour, String fontType){
		return intro+":"+end+":"+preview+":"+fontSize+":"+fontColour+":"+fontType;		
	}
	
	private void checkVamix(){
		if (!VAMIX.exists()){
			VAMIX.mkdir();
		}
		if (!SaveFile.exists()){
			try {
				SaveFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
