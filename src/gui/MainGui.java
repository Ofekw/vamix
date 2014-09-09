package gui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class MainGui {
	
	private JFrame frame;

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
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public MainGui(){
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		ImageIcon img = new ImageIcon("V.png");
		frame.setIconImage(img.getImage());
		frame.setBounds(100, 100, 1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		VideoPanel videoPanel = new VideoPanel();
		frame.add(videoPanel);
	}
}
