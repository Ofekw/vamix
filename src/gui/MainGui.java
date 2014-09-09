package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.Color;

import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

public class MainGui {
	
	private JFrame frame;
	private JFrame editFrame;

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
		editFrame = new JFrame();
		ImageIcon img = new ImageIcon("V.png");
		frame.setIconImage(img.getImage());
		frame.setBounds(100, 100, 1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		Box verticalBox = Box.createVerticalBox();
		frame.getContentPane().add(verticalBox);
		
		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);
		
		VideoPanel videoPanel = new VideoPanel();
		horizontalBox.add(videoPanel);
		videoPanel.setBackground(Color.BLACK);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		frame.getContentPane().add(horizontalBox_1);
		
		JPanel EditPanel = new JPanel();
		EditPanel.setPreferredSize(new Dimension(1000, 150));
		EditPanel.setBackground(Color.LIGHT_GRAY);
		horizontalBox_1.add(EditPanel);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		EditPanel.add(tabbedPane);
		
		JPanel Video = new JPanel();
		Video.setPreferredSize(new Dimension(1000, 130));
		tabbedPane.addTab("Video", null, Video, null);
		
		JPanel Audio = new JPanel();
		Audio.setPreferredSize(new Dimension(1000, 130));
		tabbedPane.addTab("Audio", null, Audio, null);
		
		JPanel Text = new JPanel();
		Text.setPreferredSize(new Dimension(1000, 130));
		tabbedPane.addTab("Text", null, Text, null);
	}
}
