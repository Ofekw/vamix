package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import com.sun.jna.Native;

public class MainGui {
	
	private JFrame frame;
	private JFrame editFrame;
	private VideoTab _Video;
	private AudioTab _audio;

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
		frame.setBounds(100, 100, 1040, 690);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		Box verticalBox = Box.createVerticalBox();
		frame.getContentPane().add(verticalBox);
		
		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);
		
		VideoPanel videoPanel = new VideoPanel(this);
		horizontalBox.add(videoPanel);
		//videoPanel.setBackground(Color.BLACK);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		frame.getContentPane().add(horizontalBox_1);
		
		JPanel EditPanel = new JPanel();
		//EditPanel.setPreferredSize(new Dimension(1000, 150));
		horizontalBox_1.add(EditPanel);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		EditPanel.add(tabbedPane);
		
		_audio = new AudioTab(videoPanel);
		JPanel Text = new JPanel();
		_Video = new VideoTab(videoPanel,_audio);
		tabbedPane.addTab("Media", null, _Video, null);
	
		tabbedPane.addTab("Audio", null, _audio, null);
		
		//Text.setPreferredSize(new Dimension(1000, 150));
		tabbedPane.addTab("Text", null, Text, null);
	}

	public VideoTab getVideo() {
		return _Video;
	}
}
