package gui;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class MainGui {

	private JFrame frame;
	private JFrame editFrame;
	private VideoTab _video;
	private AudioTab _audio;
	private TextTab _text;

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
		_text = new TextTab(videoPanel);
		_video = new VideoTab(videoPanel,_audio);
		tabbedPane.addTab("Media", null, _video, null);
		tabbedPane.addTab("Audio", null, _audio, null);
		tabbedPane.addTab("Text", null, _text, null);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mainMenu = new JMenu("Project");
		menuBar.add(mainMenu);
		JMenuItem save = new JMenuItem("Save Project",KeyEvent.VK_T);
		save.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_1, ActionEvent.ALT_MASK));
		save.getAccessibleContext().setAccessibleDescription(
				"Save current project settings");
		mainMenu.add(save);
		
		JMenuItem load = new JMenuItem("Load Project",KeyEvent.VK_T);
		load.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_2, ActionEvent.ALT_MASK));
		load.getAccessibleContext().setAccessibleDescription(
				"Load project settings");
		mainMenu.add(load);

	}

	public VideoTab getVideo() {
		return _video;
	}
}
