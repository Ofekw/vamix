package gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import com.sun.jna.Native;

public class MainGui extends JFrame {
	
	public static void main(String[] args){
		
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if ("GTK+".equals(info.getName())) {
				try {
					UIManager.setLookAndFeel(info.getClassName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
			}
		}
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainGui window = new MainGui();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public MainGui(){
		this.setSize(1000, 600);
		JPanel videoPanel = new VideoPanel();
		this.setVisible(true);
		
	}
}
