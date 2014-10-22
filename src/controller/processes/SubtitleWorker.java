package controller.processes;

import gui.MainGui;
import gui.SubTitles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import javax.swing.SwingWorker;
/**
 * Swing worker process which genearates a simple SRT file and restarts the player
 * @author ofek
 *
 */

public class SubtitleWorker extends SwingWorker<Void, Void>{
	private SubTitles _tab;
	public static final String tempSubPath = MainGui.VAMIX.getAbsolutePath()+MainGui.SEPERATOR+".tempMedia"+MainGui.SEPERATOR;
	private File _srtFile;
	private int importOrCreate; // >0 = create srt otherwise import
	public SubtitleWorker(SubTitles tab, int i){ 
		_tab = tab;
		importOrCreate = i;

	}

	@Override
	protected Void doInBackground() throws Exception {
		// writes  srt file if importOrCreate > 1
		if(importOrCreate>0){
			String vidLoc = _tab.getMain().getVideo().getVideoLoc();
			String srtLoc = (vidLoc.substring(0,vidLoc.lastIndexOf(".")) + ".srt");
			ShellProcess.command("rm -f "+srtLoc);
			_srtFile = new File(srtLoc);
			_srtFile.createNewFile();
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(_srtFile, "UTF-8");
				String[] lines = _tab.getSRT().split("\\n"); 
				int count = 1;
				for(int i = 0; i < lines.length; i++) {
					if ((i)%3 == 0){
						writer.println(count);
						count++;
					}
					writer.println(lines[i]);
					i++;
					writer.println("<font color="+ "\"" + "#ffff00"+ "\"" + ">"+lines[i]+"</font>");
					i++;
					writer.println(lines[i]);
					//writer.println("\n");
				}		
				return null;
			}finally{
				writer.close();
			}
		}else{
				BufferedReader br = new BufferedReader(new FileReader(_tab.getImportLoc()));
				try {
					StringBuilder sb = new StringBuilder();
					String line = br.readLine();
					while (line != null ) {
						if (line.length() > 1){
							sb.append(line+"\n");
						}
						if (line.contains("font")){
							sb.append("\n");
						}
						line = br.readLine();
					}
					_tab.setImport(sb.toString());
				} finally {
					br.close();
				}
			}
		return null;
		}
	@Override
	protected void done() {
		_tab.getMain().getPlayer().resetPlayer();
		_tab.getMain().getPlayer().play();
	}
}
