package controller;

public class CheckAudioFile extends testAbPro {

	private boolean result = false;
	
	public CheckAudioFile(String filePath){
		super.setCommand(makeCommand(filePath));
	}
	
	protected void doProcess(String line){
		if(line.contains("Audio")){
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
