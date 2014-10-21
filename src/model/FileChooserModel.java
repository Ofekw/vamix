package model;

import java.io.File;

public class FileChooserModel extends javax.swing.filechooser.FileFilter  
{  
	public boolean accept(File file)  
	{  
		//Convert to lower case before checking extension  
		return (file.getName().toLowerCase().endsWith(".vamix")  ||  
				file.isDirectory()); 
	}  

	public String getDescription()  
	{  
		return "Vamix save File (*.vamix)";  
	}  
} 

