package model;

import java.io.File;

public class FileChooserModel extends javax.swing.filechooser.FileFilter  
{  
	public boolean accept(File file)  
	{  
		//Convert to lower case before checking extension  
		return (file.getName().toLowerCase().endsWith(".txt")  ||  
				file.isDirectory()); 
	}  

	public String getDescription()  
	{  
		return "Text File (*.txt)";  
	}  
} 

