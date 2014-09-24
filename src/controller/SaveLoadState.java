package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	
	private String saveFormat;
	
	public SaveLoadState(JTextField intro, JTextField end, JTextPane preview, 
			JSpinner fontSize, JComboBox<String> fontColour, JComboBox<String> fontType, 
			String saveLoc, String saveFileName){
		saveFormat = format(intro.getText(), end.getText(), preview.getText(), 
				fontSize.getValue().toString(), fontColour.getSelectedItem().toString(),
				fontType.getSelectedItem().toString(), saveLoc);
		SaveFile = new File(SAVE+saveFileName);
		checkVamix();
	}
	
	public void save(String saveFile){
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(SaveFile)));
			out.write(saveFormat);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String format(String intro, String end, String preview, 
			String fontSize, String fontColour, String fontType, 
			String saveLoc){
		return intro+":"+intro+":"+end+":"+preview+":"+fontSize+":"+fontColour+":"+fontType+
				":"+saveLoc;		
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
