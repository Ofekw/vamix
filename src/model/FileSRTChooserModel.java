package model;

import java.io.File;

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

