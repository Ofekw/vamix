package controller;

public class CheckVideoFile extends testAbPro {

	private boolean result = false;
	
	public CheckVideoFile(String filePath){
		super.setCommand(makeCommand(filePath));
	}
	
	protected void doProcess(String line){
		if(line.contains("video")){
			result = true;
		}else{
			result = false;
		}
	}
	
	private String makeCommand(String filePath){
		return "file "+filePath;
	}
	
	public boolean getResult(){
		return result;
	}
}