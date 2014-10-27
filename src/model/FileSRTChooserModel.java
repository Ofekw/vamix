package model;

import java.io.File;

/**
 * File chooser model, to limit file selection to .srt files only
 * @author ofek
 *
 */

public class FileSRTChooserModel extends javax.swing.filechooser.FileFilter  
{  
	public boolean accept(File file)  
	{  
		//Convert to lower case before checking extension  
		return (file.getName().toLowerCase().endsWith(".srt")  ||  
				file.isDirectory()); 
	}  

	public String getDescription()  
	{  
		return "Text File (*.srt)";  
	}  
} 

