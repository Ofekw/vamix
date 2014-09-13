package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import net.miginfocom.swing.MigLayout;

import sun.applet.Main;
import uk.co.caprica.vlcj.binding.LibVlcConst;
import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.Timer;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class VideoPanel extends JPanel {

	private final EmbeddedMediaPlayer mediaPlayer;
	private JProgressBar progressBar;
	private JSlider _progressSlider;
	private MainGui _parent;
	private static final int SKIP_TIME_MS = 10 * 1000;

	private JLabel timeLabel;
	private JSlider positionSlider;
	private JLabel chapterLabel;

	private JButton previousChapterButton;
	private JButton rewindButton;
	private JButton stopButton;
	private JButton pauseButton;
	private JButton playButton;
	private JButton fastForwardButton;
	private JButton nextChapterButton;

	private JButton toggleMuteButton;

	private JButton captureButton;

	private JButton ejectButton;
	private JButton connectButton;

	private JButton fullScreenButton;

	private JButton subTitlesButton;

	private JFileChooser fileChooser;

	private boolean mousePressedPlaying = false;


	private SwingWorker<Void, Integer> videoWorker = new SwingWorker<Void, Integer>(){

		Timer timer = new Timer(5, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//System.out.println(mediaPlayer.getTime());
				publish((int)mediaPlayer.getTime());
			}
		});

		@Override
		protected Void doInBackground() throws Exception {
			if (isCancelled()){
				timer.stop();
			}else{
				timer.start();
			}
			return null;
		}

		@Override
		protected void process(List<Integer> chunks) {
			for(Integer i: chunks){
				progressBar.setValue(i);
				_progressSlider.setValue(i);
			}
		}
	};

	public VideoPanel(MainGui parent){
		this._parent = parent;
		this.setMinimumSize(new Dimension(700, 500));
//		this.setBackground(Color.WHITE);
		this.setLayout(new MigLayout("", "[]", "[][][][][]"));
		createControls();
		registerListeners();

		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();

		// Setup canvas for player to go on


		Canvas mediaCanvas = new Canvas();
	//	mediaCanvas.setBackground(Color.black);
		mediaCanvas.setPreferredSize(new Dimension(600,300));

		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(mediaCanvas));
		//Play/Resume/Restart button
//		JButton playButton = new JButton("Play");
//
//		playButton.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				String mediaLoc = _parent.getVideo().getVideoLoc();
//				if(!mediaLoc.isEmpty()){
//					//add file checks here
//				mediaPlayer.prepareMedia(mediaLoc);
//				}else{
//					
//				}
//				if (mediaPlayer.getTime() == -1){
//					mediaPlayer.play();
//					videoWorker.execute();
//					try {
//						Thread.sleep(400);
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					progressBar.setMaximum((int)(mediaPlayer.getLength()));
//					_progressSlider.setMaximum((int)(mediaPlayer.getLength()));
//				}else if (!mediaPlayer.isPlaying()){
//					mediaPlayer.start();
//				}else{
//					mediaPlayer.pause();
//				}
//			}
//		});

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setValue(0);
		progressBar.setVisible(true);

		this.add(mediaCanvas, "cell 0 0,growx");
		this.add(progressBar, "cell 0 1,growx");
		
		_progressSlider = new JSlider();
		_progressSlider.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
			}
		});
//		add(_progressSlider, "cell 0 2,growx");
		//this.add(playButton, "cell 0 3,growx");
	}

	private void createControls() {
		timeLabel = new JLabel("hh:mm:ss");
		positionSlider = new JSlider();
		positionSlider.setMinimum(0);
		positionSlider.setMaximum(1000);
		positionSlider.setValue(0);
		positionSlider.setToolTipText("Position");

		chapterLabel = new JLabel("00/00");


		rewindButton = new JButton();
		rewindButton.setIcon(new ImageIcon(("icons/fastforward.png")));
		rewindButton.setToolTipText("Skip back");

		stopButton = new JButton();
//		stopButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/stop.png")));
		stopButton.setIcon(new ImageIcon(("icons/stop.png")));
		stopButton.setToolTipText("Stop");

		pauseButton = new JButton();
		pauseButton.setIcon(new ImageIcon(("icons/pause.png")));
		pauseButton.setToolTipText("Play/pause");

		playButton = new JButton();
		playButton.setIcon(new ImageIcon(("icons/play.png")));
		playButton.setToolTipText("Play");

		fastForwardButton = new JButton();
		fastForwardButton.setIcon(new ImageIcon(("icons/fastforward.png")));
		fastForwardButton.setToolTipText("Skip forward");



		this.add(rewindButton, "cell 0 3");
		this.add(stopButton, "cell 0 3");
		//this.add(pauseButton, "cell 0 3");
		this.add(playButton, "cell 0 3");
		this.add(fastForwardButton, "cell 0 3");
		
	}

	private void filePathInvalid() {
		JOptionPane.showMessageDialog(this, "Please select a media file",
				"Location Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Set file to be played on media player
	 * @param mediaLocation: Absolute path to media file location
	 */
	public void setMedia(String mediaLocation){
		mediaPlayer.prepareMedia(mediaLocation);
	}
	
	private void registerListeners() {

		positionSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(mediaPlayer.isPlaying()) {
					mousePressedPlaying = true;
					mediaPlayer.pause();
				}
				else {
					mousePressedPlaying = false;
				}
				setSliderBasedPosition();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				setSliderBasedPosition();
				updateUIState();
			}
		});


		rewindButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skip(-SKIP_TIME_MS);
			}
		});

		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.stop();
			}
		});

		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.pause();
			}
		});

		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String mediaLoc = _parent.getVideo().getVideoLoc();
				if(!mediaLoc.isEmpty()){
					//add file checks here
				mediaPlayer.prepareMedia(mediaLoc);
				}else{
					errorPlaybackFile();
				}
				if (mediaPlayer.getTime() == -1){
					System.out.println("getting here INITIAL play press");
					mediaPlayer.play();
					playButton.setIcon(new ImageIcon(("icons/pause.png")));
					videoWorker.execute();
					try {
						Thread.sleep(400);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					progressBar.setMaximum((int)(mediaPlayer.getLength()));
					_progressSlider.setMaximum((int)(mediaPlayer.getLength()));
				}else if (!mediaPlayer.isPlaying()){
					mediaPlayer.start();
					System.out.println("getting here to play after pause");
				}else{
					mediaPlayer.pause();
					System.out.println("getting here to pause");
					playButton.setIcon(new ImageIcon(("icons/play.png")));
				}
			}

		});


		fastForwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skip(SKIP_TIME_MS);
			}
		});




	}
	
	private void errorPlaybackFile() {
		JOptionPane.showMessageDialog(this, "No valid media file selected",
				"Location Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void skip(int skipTime) {
		// Only skip time if can handle time setting
		if(mediaPlayer.getLength() > 0) {
			mediaPlayer.skip(skipTime);
			updateUIState();
		}
	}
	
	private void updateUIState() {
		if(!mediaPlayer.isPlaying()) {
			// Resume play or play a few frames then pause to show current position in video
			mediaPlayer.play();
			if(!mousePressedPlaying) {
				try {
					// Half a second probably gets an iframe
					Thread.sleep(500);
				}
				catch(InterruptedException e) {
					// Don't care if unblocked early
				}
				mediaPlayer.pause();
			}
		}
		long time = mediaPlayer.getTime();
		int position = (int)(mediaPlayer.getPosition() * 1000.0f);
		int chapter = mediaPlayer.getChapter();
		int chapterCount = mediaPlayer.getChapterCount();
		updateTime(time);
		updatePosition(position);
	}
	private void updateTime(long millis) {
		String s = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		timeLabel.setText(s);
	}

	private void updatePosition(int value) {
		// positionProgressBar.setValue(value);
		positionSlider.setValue(value);
	}
	
	private void setSliderBasedPosition() {
		if(!mediaPlayer.isSeekable()) {
			return;
		}
		float positionValue = positionSlider.getValue() / 1000.0f;
		// Avoid end of file freeze-up
		if(positionValue > 0.99f) {
			positionValue = 0.99f;
		}
		mediaPlayer.setPosition(positionValue);
	}
}
