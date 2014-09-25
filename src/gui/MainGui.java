package gui;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileView;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import controller.SaveLoadState;
import controller.ShellProcess;

public class MainGui {

	private JFrame frame;
	private VideoTab _video;
	private AudioTab _audio;
	private TextTab _text;
	private VideoPanel _videoPanel;

	public static void main(String[] args){
		NativeLibrary.addSearchPath(
				RuntimeUtil.getLibVlcLibraryName(), "/home/linux/vlc/install/lib"
				);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if ("GTK+".equals(info.getName())) {
				try {
					UIManager.setLookAndFeel(info.getClassName());
					UIManager.put("Slider.paintValue", false);
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
		frame.setResizable(false);
		ImageIcon img = new ImageIcon("V.png");
		frame.setIconImage(img.getImage());
		frame.setBounds(0, 0, 1040, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		Box verticalBox = Box.createVerticalBox();
		frame.getContentPane().add(verticalBox);

		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);

		_videoPanel = new VideoPanel(this);
		horizontalBox.add(_videoPanel);
		//videoPanel.setBackground(Color.BLACK);

		Box horizontalBox_1 = Box.createHorizontalBox();
		frame.getContentPane().add(horizontalBox_1);

		JPanel EditPanel = new JPanel();
		//EditPanel.setPreferredSize(new Dimension(1000, 150));
		horizontalBox_1.add(EditPanel);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		EditPanel.add(tabbedPane);


		_text = new TextTab(_videoPanel, this);

		//java is being weird and not intialising the fields right
		//had to double up the constructors
		_video = new VideoTab(_videoPanel,_audio);
		_audio = new AudioTab(_videoPanel, _video);
		_video = new VideoTab(_videoPanel, _audio);
		_audio = new AudioTab(_videoPanel, _video);

		tabbedPane.addTab("Media", null, _video, null);
		tabbedPane.addTab("Audio", null, _audio, null);
		tabbedPane.addTab("Text", null, _text, null);

		final JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mainMenu = new JMenu("Project");
		JMenu help = new JMenu("Help");
		help.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				File htmlFile = new File("site/index.html");
				try {
					Desktop.getDesktop().browse(htmlFile.toURI());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		menuBar.add(mainMenu);
		menuBar.add(help);
		JMenuItem save = new JMenuItem("Save Project",KeyEvent.VK_1);
		save.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		save.getAccessibleContext().setAccessibleDescription(
				"Save current project settings");
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				while (true){
					String result = JOptionPane.showInputDialog(menuBar, "Enter save file name");
					if ((result == null)){
						return;
					}else{
						if (!result.endsWith(".txt")){
							result+=".txt";
						}
						File saveFile = new File(SaveLoadState.VAMIX.toString()+SaveLoadState.SEPERATOR+result);
						if (saveFile.exists()){
							int option = JOptionPane.showConfirmDialog(menuBar, "File already Exists, " +
									"would you like to overwrite?", "Save file already exists!", JOptionPane.OK_CANCEL_OPTION);
							if (option == JOptionPane.YES_OPTION){
								ShellProcess.command("rm -f " + saveFile.getAbsolutePath());
							}else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION){
								continue;
							}
						}
						_audio.save(result);
						_text.save(result);
						return;
					}
				}
			}
		});

		mainMenu.add(save);

		JMenuItem load = new JMenuItem("Load Project",KeyEvent.VK_2);
		load.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		load.getAccessibleContext().setAccessibleDescription(
				"Load project settings");
		load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//				FileDialog fd = new FileDialog(frame, "Load Save file", FileDialog.LOAD);
				//				fd.setDirectory(SaveLoadState.VAMIX.toString());
				//				fd.setVisible(true);
				JFileChooser chooser = new JFileChooser(new File(SaveLoadState.VAMIX.toString())); 
				chooser.setFileView(new FileView() {
					@Override
					public Boolean isTraversable(File f) {
						return (f.isDirectory() && f.getName().equals(SaveLoadState.VAMIX.toString())); 
					}
				});
				int returnVal = chooser.showOpenDialog(menuBar);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = new File(chooser.getSelectedFile().toString());
					if (file.isFile()){
						_text.load(chooser.getSelectedFile().getName());
						_audio.load(chooser.getSelectedFile().getName());
					}else{
						JOptionPane.showMessageDialog(menuBar, "File is not a valid save file!");
					}
				}
			}
		});
		mainMenu.add(load);

		JMenuItem quit = new JMenuItem("Quit Project",KeyEvent.VK_3);
		quit.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		quit.getAccessibleContext().setAccessibleDescription(
				"Quit Vamix");
		quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainGui.this.getFrame().dispose();
			}
		});

		mainMenu.add(quit);

	}

	public VideoTab getVideo() {
		return _video;
	}
	public VideoPanel getPlayer() {
		return _videoPanel;
	}

	public JFrame getFrame(){
		return frame;
	}
}
