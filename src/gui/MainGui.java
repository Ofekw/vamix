package gui;

import java.awt.Desktop;
import java.awt.EventQueue;
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
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.filechooser.FileView;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import controller.gui.FileMover;
import controller.processes.SaveLoadState;
import controller.processes.ShellProcess;

import model.FileChooserModel;

public class MainGui {
	/**
	 * Main Frame for vamix
	 *  worked on collaboratively with Patrick Poole as part of 206 assignment 3.
	 * @author Patrick Poole and Ofek Wittenburg
	 *
	 */
	private JFrame frame;
	private MediaTab _media;
	private AudioTab _audio;
	private TextTab _text;
	private VideoPanel _videoPanel;
	private FilterTab _filterTab;
	private VideoCropTab _videoCrop;
	public static final String HOME = System.getProperty("user.home");
	public static final String SEPERATOR = File.separator;
	public static final File VAMIX = new File(HOME+SEPERATOR+"vamix");
	public static final String ZIP = new String("vamix.BIN.zip");
	@SuppressWarnings("unused")
	private String[] _moveableFiles = {".tempMedia", "bat", "com", "css", "images", "js", "readme.html"} ;
	private SubTitles _subtitle;
	private DownloadTab _download;
	/**
	 * The main running starting point of the application, checks that vlc is installed, and starts setting the gui in a new ED thread
	 * @param args
	 * @author Ofek
	 */
	public static void main(String[] args){
		NativeLibrary.addSearchPath(
				RuntimeUtil.getLibVlcLibraryName(), "/home/linux/vlc/install/lib"
				);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

		try {
			UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
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
	/**
	 * calls the setup for the file mover (unpacks required jar files) and starts creating the gui
	 */
	public MainGui(){
		setup();
		initialize();
	}
	/**
	 * File mover for (unpacking required jar / bin files)
	 */
	private void setup() {
		FileMover mover = new FileMover();
		mover.execute();

	}
	/**
	 * Creates the main frame with all the tabs and the media player
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setContentPane(new JLabel(new ImageIcon(getClass().getResource("/icons/"+"background.png"))));

		frame.setResizable(false);
		frame.setTitle("Video Audio Mixer");
		ImageIcon img = new ImageIcon(getClass().getResource("/icons/"+"V.png"));
		frame.setIconImage(img.getImage());
		frame.setBounds(10, 10, 1200, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		Box verticalBox = Box.createVerticalBox();
		frame.getContentPane().add(verticalBox);

		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);

		_videoPanel = new VideoPanel(this);
		horizontalBox.add(_videoPanel);

		Box horizontalBox_1 = Box.createHorizontalBox();
		frame.getContentPane().add(horizontalBox_1);

		JPanel EditPanel = new JPanel();

		horizontalBox_1.add(EditPanel);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		EditPanel.setOpaque(false);
		EditPanel.add(tabbedPane);

		//creating all the tabs
		_text = new TextTab(_videoPanel, this);
		_text.setToolTipText("Tab for adding an Intro/Credits screen to your video");
		_audio = new AudioTab(_videoPanel, _media);
		_audio.setToolTipText("Tab for extracting,overlaying and removing audio from media");
		_media = new MediaTab(_videoPanel,this);
		_media.setToolTipText("This is where you select the media to be used for playback and editing");
		_audio.setVideoTab(_media);
		_filterTab = new FilterTab(_videoPanel, this);
		_filterTab.setToolTipText("Tab for applying visual filters to video");
		_videoCrop = new VideoCropTab(_videoPanel, this);
		_subtitle = new SubTitles(_videoPanel, this);
		_subtitle.setToolTipText("Tab for adding/removing subtitles to videos");
		_download = new DownloadTab(_videoPanel);
		_download.setToolTipText("Tab for downloading opensource media from the internet");


		tabbedPane.addTab("Media", null, _media, null);
		tabbedPane.addTab("Audio", null, _audio, null);
		tabbedPane.addTab("Intro/Credits", null, _text, null);
		tabbedPane.addTab("Filters", null, _filterTab, null);
		tabbedPane.addTab("Video Crop", null, _videoCrop, null);
		tabbedPane.addTab("Subtitles", null, _subtitle, null);
		tabbedPane.addTab("Download", null, _download, null);

		final JMenuBar menuBar = new JMenuBar();
		frame.getRootPane().setJMenuBar(menuBar);
		//creates the menu bar
		JMenu mainMenu = new JMenu("Project");
		JMenu help = new JMenu("Help");
		help.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				File htmlFile = new File(VAMIX+SEPERATOR+"readme.html");
				try {
					Desktop.getDesktop().browse(htmlFile.toURI());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		menuBar.add(mainMenu);
		menuBar.add(help);

		//Add save shortcut
		JMenuItem save = new JMenuItem("Save Project",KeyEvent.VK_1);
		save.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		save.getAccessibleContext().setAccessibleDescription(
				"Save current project settings");
		save.addActionListener(new ActionListener() {

			// This calls all the save functions in all the tabs and places data in a text file
			@Override
			public void actionPerformed(ActionEvent arg0) {
				while (true){
					String result = JOptionPane.showInputDialog(menuBar, "Enter save file name");
					if ((result == null)){
						return;
					}else{
						if (!result.endsWith(".vamix")){
							result+=".vamix";
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
						_filterTab.save(result);
						_videoCrop.save(result);
						_subtitle.save(result);
						return;
					}
				}
			}
		});

		mainMenu.add(save);

		//add load shortcut
		JMenuItem load = new JMenuItem("Load Project",KeyEvent.VK_2);
		load.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		load.getAccessibleContext().setAccessibleDescription(
				"Load project settings");
		load.addActionListener(new ActionListener() {
			//this calls all the load functions in the tabs and sets data from the text save file
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser(new File(SaveLoadState.VAMIX.toString()));
				chooser.setFileFilter(new FileChooserModel());
				chooser.setFileHidingEnabled(false);
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
						_filterTab.load(chooser.getSelectedFile().getName());
						_videoCrop.load(chooser.getSelectedFile().getName());
						_subtitle.load(chooser.getSelectedFile().getName());
						if (!_media.getVideoLoc().equals("")){
							_media.checkMediaFile(new File(_media.getVideoLoc()));
						}
					}else{
						JOptionPane.showMessageDialog(menuBar, "File is not a valid save file!");
					}
				}
			}
		});
		mainMenu.add(load);


		//add quit shortcut
		JMenuItem quit = new JMenuItem("Quit Project",KeyEvent.VK_3);
		quit.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		quit.getAccessibleContext().setAccessibleDescription(
				"Quit Vamix");
		quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainGui.this.getFrame().dispose();
				System.exit(0);
			}
		});

		mainMenu.add(quit);


	}
	
	/**
	 * all the getters for the tab objects
	 * @return
	 */

	public MediaTab getVideo() {
		return _media;
	}
	
	public TextTab getTextTab() {
		return _text;
	}
	public VideoPanel getPlayer() {
		return _videoPanel;
	}

	public FilterTab getFilters() {
		return _filterTab;
	}

	public AudioTab getAudio() {
		return _audio;
	}

	public VideoCropTab getCrop(){
		return _videoCrop;
	}

	public JFrame getFrame(){
		return frame;
	}
	
	public SubTitles getSubtitles(){
		return _subtitle;
	}

}
