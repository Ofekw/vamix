package controller;

import java.io.File;

public class FileExistsTest {
	public FileExistsTest(){

	}
	public 	static boolean Test(String dir, String name){

		File propFile = new File(dir, name);
		if (propFile.exists()) {
			System.out.println("FOUND");
			return true;
		} else {
			System.out.println("Not Found");
			return false;
		}
	}

}
