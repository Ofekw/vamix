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

public class Subtitleworker extends SwingWorker<Void, Void>{
	private SubTitles _tab;
	public static final String tempSubPath = MainGui.VAMIX.getAbsolutePath()+MainGui.SEPERATOR+".tempMedia"+MainGui.SEPERATOR;
	private File _srtFile;
	private int importOrCreate; // >0 = create srt otherwise import
	public Subtitleworker(SubTitles tab, int i){ 
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
				for(int i = 0; i < lines.length; i++) {
					writer.println(lines[i]);
				}		
				return null;
			}finally{
				writer.close();
			}
		}else{
				BufferedReader br = new BufferedReader(new FileReader(_tab.getImportLoc()));
				int subLines = 0;
				try {
					StringBuilder sb = new StringBuilder();
					String line = br.readLine();

					while (line != null) {
						sb.append(line);
						sb.append("\n");
						line = br.readLine();
						if (!line.equals("\n")){ //ignore white space
						subLines++;
						}
					}
					_tab.setImport(sb.toString());
				} finally {
					br.close();
					_tab.setSubNum(subLines/3); //divide by 3 as each subtitle input takes up 3 lines
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
