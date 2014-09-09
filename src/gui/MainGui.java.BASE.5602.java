package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class MainGui extends JFrame {

	public static void main(String[] args){
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
	}
}
