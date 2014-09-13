package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.Timer;
import javax.swing.JSlider;

import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class VideoPanel extends JPanel {

	private EmbeddedMediaPlayer mediaPlayer = null;
	private JProgressBar _progressSlider;
	private MainGui _parent;
	private static final int SKIP_TIME_MS = 10 * 1000;

	private JLabel _timeLabel;

	private JButton _rewindButton;
	private JButton _stopButton;
	private JButton _playButton;
	private JButton _fastForwardButton;

	private boolean mousePressedPlaying = false;

	private Timer timer;

	private String videoLocation;
	
	//Constant value for progress bar
	//it is now scaled so it updates alot
	//smoother than before, check the updatePosition
	//method for the details
	private final int maxTime = 100000;





	public VideoPanel(MainGui parent){
		this._parent = parent;
		this.setMinimumSize(new Dimension(700, 500));
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


		//just setting up the timer and stopping it so it doesnt run
		timer = new Timer(300, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//checks if the file is at the end and resets it
				//fixs the player crashing
				if (!mediaPlayer.isPlayable()){
					resetPlayer();
					_progressSlider.setValue(maxTime);
					updateTime(mediaPlayer.getLength());;
				}else{
					updateTime(mediaPlayer.getTime());
					updatePosition(mediaPlayer.getTime());
				}
			}
		});
		timer.stop();

		this.add(mediaCanvas, "cell 0 0,growx");

		//		_progressSlider = new JProgressBar();
		//		_progressSlider.addMouseMotionListener(new MouseMotionAdapter() {
		//			@Override
		//			public void mouseMoved(MouseEvent arg0) {
		//				
		//			}
		//		});
	}

	private void createControls() {
		_timeLabel = new JLabel("00:00:00");

		_progressSlider = new JProgressBar();
		_progressSlider.setMinimum(0);
		_progressSlider.setMaximum(maxTime);
		_progressSlider.setValue(0);
		_progressSlider.setToolTipText("Position");
		_progressSlider.setBackground(Color.BLACK);

		_rewindButton = new JButton();
		_rewindButton.setIcon(new ImageIcon(("icons/fastforward.png")));
		_rewindButton.setToolTipText("Skip back");

		_stopButton = new JButton();
		_stopButton.setIcon(new ImageIcon(("icons/stop.png")));
		_stopButton.setToolTipText("Stop");

		_playButton = new JButton();
		_playButton.setIcon(new ImageIcon(("icons/play.png")));
		_playButton.setToolTipText("Play");

		_fastForwardButton = new JButton();
		_fastForwardButton.setIcon(new ImageIcon(("icons/fastforward.png")));
		_fastForwardButton.setToolTipText("Skip forward");

		this.add(_timeLabel, "cell 0 2");
		this.add(_progressSlider, "cell 0 2, growx");
		this.add(_rewindButton, "cell 0 3");
		this.add(_stopButton, "cell 0 3");
		this.add(_playButton, "cell 0 3");
		this.add(_fastForwardButton, "cell 0 3");

	}

	private void filePathInvalid() {
		JOptionPane.showMessageDialog(this, "Please select a media file",
				"Location Error", JOptionPane.ERROR_MESSAGE);
	}

	

	private void registerListeners() {

		//		_positionSlider.addMouseListener(new MouseAdapter() {
		//			@Override
		//			public void mousePressed(MouseEvent e) {
		//				if(mediaPlayer.isPlaying()) {
		//					mousePressedPlaying = true;
		//					mediaPlayer.pause();
		//				}else {
		//					mousePressedPlaying = false;
		//				}
		//				setSliderBasedPosition();
		//			}
		//
		//			@Override
		//			public void mouseReleased(MouseEvent e) {
		//				setSliderBasedPosition();
		//				updateUIState();
		//			}
		//		});


		_rewindButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skip(-SKIP_TIME_MS);
			}
		});

		_stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.stop();
				_playButton.setIcon(new ImageIcon(("icons/play.png")));
			}
		});

		_playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//This bit keeps restarting video, need to find
				//better way to check file (maybe in MainGui
				//where JFileChooser is located

				//Fixed it :D working now, video tab takes a video panel
				//which then allows for setting the media file

				//check if video hasn't started at all
				if (mediaPlayer.getTime() == -1){
					//check if there has been an input file selected
					if (videoLocation == null){
						errorPlaybackFile();
					}else{
						//start media from beginning and set play button to pause logo
						_progressSlider.setValue(0);
						mediaPlayer.play();
						_playButton.setIcon(new ImageIcon(("icons/pause.png")));
						//have to sleep cause vlcj sucks and won't allow
						//getting length until video has played for a small amount of time
						try {
							Thread.sleep(400);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						timer.start();
					}
					//check if video is paused
				}else if (!mediaPlayer.isPlaying()){
					mediaPlayer.start();
					timer.start();
					_playButton.setIcon(new ImageIcon(("icons/pause.png")));
					//pause video otherwise
				}else{
					mediaPlayer.pause();
					timer.stop();
					_playButton.setIcon(new ImageIcon(("icons/play.png")));
				}
			}
		});


		_fastForwardButton.addActionListener(new ActionListener() {
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
			//updateUIState();
		}
	}

	//	private void updateUIState() {
	//		if(!mediaPlayer.isPlaying()) {
	//			// Resume play or play a few frames then pause to show current position in video
	//			mediaPlayer.start();
	//			if(!mousePressedPlaying) {
	//				try {
	//					// Half a second probably gets an iframe
	//					Thread.sleep(500);
	//				}
	//				catch(InterruptedException e) {
	//					// Don't care if unblocked early
	//				}
	//				mediaPlayer.pause();
	//			}
	//		}
	//		long time = mediaPlayer.getTime();
	//		int position = (int)(mediaPlayer.getPosition() * 1000.0f);
	//		int chapter = mediaPlayer.getChapter();
	//		int chapterCount = mediaPlayer.getChapterCount();
	//		updateTime(time);
	//		updatePosition(position);
	//	}
	
	/**
	 * Updates the time label
	 * @param millis: time in milliseconds
	 */
	private void updateTime(long millis) {
		String s = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		_timeLabel.setText(s);
		//		System.out.println(s);
	}

	/**
	 * updates the progress bar
	 * @param value: new value
	 */
	private void updatePosition(long value) {
		_progressSlider.setValue((int)(((float)value)/mediaPlayer.getLength()*maxTime));
	}
	
	/**
	 * Resets the media player to it's default starting point
	 */
	
	private void resetPlayer(){
		timer.restart();
		timer.stop();
		mediaPlayer.prepareMedia(videoLocation);
		_playButton.setIcon(new ImageIcon(("icons/play.png")));
		_progressSlider.setValue(0);
		_timeLabel.setText("00:00:00");
	}
	
	/**
	 * Set file to be played on media player
	 * @param mediaLocation: Absolute path to media file location
	 */
	public void setMedia(String mediaLocation){
		//changes videolocation, prepares the video to be played, resets timer and progressSlider
		videoLocation = mediaLocation;
		mediaPlayer.prepareMedia(mediaLocation);
		resetPlayer();
		mediaPlayer.start();
		mediaPlayer.stop();
	}

	//	private void setSliderBasedPosition() {
	//		if(!mediaPlayer.isSeekable()) {
	//			return;
	//		}
	//		float positionValue = _positionSlider.getValue() / 1000.0f;
	//		// Avoid end of file freeze-up
	//		if(positionValue > 0.99f) {
	//			positionValue = 0.99f;
	//		}
	//		mediaPlayer.setPosition(positionValue);
	//	}
}
