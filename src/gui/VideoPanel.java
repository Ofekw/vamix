package gui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.binding.LibVlcConst;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import controller.FullScreenPlayer;
import controller.SkipWorker;


@SuppressWarnings("serial")
public class VideoPanel extends JPanel {

	private EmbeddedMediaPlayer mediaPlayer = null;
	private JSlider _progressSlider;

	private JLabel _timeLabel;

	private JToggleButton _rewindButton;
	private JButton _stopButton;
	private JButton _playButton;
	private JToggleButton _fastForwardButton;
	private JSlider _volumeSlider;
	private Timer _timer;
	private boolean isPlaying = false;
	private String _videoLocation;

	//Constant value for progress bar
	//it is now scaled so it updates alot
	//smoother than before, check the updatePosition
	//method for the details
	private final int maxTime = 100000;
	private JButton _muteToggle;

	private SkipWorker skipper;
	private JButton _fullScreen;
	private EmbeddedMediaPlayer _mediaPlayerFull;
	private FullScreenPlayer _fullScreenPlayer;
	private  AudioTab _audioTab;
	
	private Icon play;
	private Icon stop;
	private Icon fastForward;
	private Icon rewind;
	private Icon pause;
	private Icon fullScreen;
	private Icon unmute;
	private Icon mute;

	public VideoPanel(MainGui parent){
<<<<<<< HEAD
		
=======
		this.setOpaque(false);
>>>>>>> ofekdev
		play = new ImageIcon(getClass().getResource("/icons/play.png"));
		unmute = new ImageIcon(getClass().getResource("/icons/unmute.png"));
		mute = new ImageIcon(getClass().getResource("/icons/mute.png"));
		fullScreen = new ImageIcon(getClass().getResource("/icons/fullscreen.png"));
		fastForward = new ImageIcon(getClass().getResource("/icons/fastforward.png"));
		pause = new ImageIcon(getClass().getResource("/icons/pause.png"));
		rewind = new ImageIcon(getClass().getResource("/icons/rewind.png"));
		stop = new ImageIcon(getClass().getResource("/icons/stop.png"));





		
		
		
		this.setMinimumSize(new Dimension(parent.getFrame().getWidth()-50, parent.getFrame().getHeight()-300));
		this.setLayout(new MigLayout("", "[][]25[]25[][grow,center][][]", "[][][][][][][]"));
		createControls();
		registerListeners();

		this.setBorder(BorderFactory.createTitledBorder(""));

		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();

		// Setup canvas for player to go on

		JPanel playerBackground = new JPanel();
		//playerBackground.setPreferredSize(new Dimension(parent.getFrame().getWidth()-50,300));
		playerBackground.setBackground(Color.BLACK);
		Canvas mediaCanvas = new Canvas();
<<<<<<< HEAD
		//	mediaCanvas.setBackground(Color.black);
=======
		//mediaCanvas.setBackground(Color.black);
>>>>>>> ofekdev
		mediaCanvas.setPreferredSize(new Dimension(parent.getFrame().getWidth()-50,300));

		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(mediaCanvas));


		//just setting up the timer and stopping it so it doesnt run
		_timer = new Timer(300, new ActionListener() {
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
		_timer.stop();
		this.add(playerBackground,"cell 0 0 7 1,growx");
		playerBackground.add(mediaCanvas);

	}

	private void createControls() {

		_stopButton = new JButton();
		_stopButton.setIcon(stop);
		_stopButton.setToolTipText("Stop");

		_fastForwardButton = new JToggleButton();
		_fastForwardButton.setIcon(fastForward);
		_fastForwardButton.setToolTipText("Skip forward");

		//Creating audio manipulation controls
		_volumeSlider = new JSlider();
		_volumeSlider.setOrientation(JSlider.HORIZONTAL);
		_volumeSlider.setMinimum(LibVlcConst.MIN_VOLUME);
		_volumeSlider.setMaximum(LibVlcConst.MAX_VOLUME);
		_volumeSlider.setPreferredSize(new Dimension(100, 40));
		_volumeSlider.setToolTipText("Change volume");
<<<<<<< HEAD

		this.add(_timeLabel, "cell 0 2");
		this.add(_progressSlider, "cell 0 2, growx");
		this.add(_rewindButton, "flowx,cell 0 3");
		this.add(_stopButton, "cell 0 3");
		this.add(_playButton, "cell 0 3");
		this.add(_fastForwardButton, "cell 0 3");

		_muteToggle = new JButton();
		_muteToggle.setToolTipText("Mute/Unmute");
		add(_muteToggle, "cell 0 3");

		_fullScreen = new JButton();
		_fullScreen.setToolTipText("Toggles fullscreen");
		_fullScreen.setIcon(fullScreen);
		_fullScreen.addActionListener(new ActionListener() {
			private EmbeddedMediaPlayerComponent mediaPlayerComponentFullScreen;
			@Override
			public void actionPerformed(ActionEvent e) {
				//			FullScreenMultiMediaTest full = new FullScreenMultiMediaTest(_parent);
				//			full.setMedia(videoLocation);
				//			full.play();
				if (mediaPlayer.getTime() == -1 && _progressSlider.getValue() == 0){
					//check if there has been an input file selected
					if (_videoLocation == null){
						errorPlaybackFile();
					}else{
						//start media from beginning and set play button to pause logo
						_progressSlider.setValue(0);
						mediaPlayer.play();
						//mediaPlayer.stop();
						pause();
						//have to sleep cause vlcj sucks and won't allow
						//getting length until video has played for a small amount of time
						try {
							Thread.sleep(400);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						_timer.start();
						fullScreenToggle();
					}
					//check if video is paused
				}else if (!mediaPlayer.isPlaying()){
					pause();
					fullScreenToggle();
					//pause video otherwise
				}else{
					pause();
					if(_fastForwardButton.isSelected()){
						skipper.cancel(true);
						enableSkips();
					}else if (_rewindButton.isSelected()){
						skipper.cancel(true);
						enableSkips();
					}
					fullScreenToggle();
				}

			}
		});
		add(_fullScreen, "cell 0 3");
		this.add(_volumeSlider, "cell 0 3");
=======
		_timeLabel = new JLabel("00:00:00");
		
				this.add(_timeLabel, "cell 0 2");
		_progressSlider = new JSlider(JSlider.HORIZONTAL);
		_progressSlider.setMinimum(0);
		_progressSlider.setMaximum(maxTime);
		_progressSlider.setValue(0);
		_progressSlider.setEnabled(false);
		this.add(_progressSlider, "cell 1 2 6 1,growx");
				
						_playButton = new JButton();
						_playButton.setIcon(play);
						_playButton.setToolTipText("Play");
						this.add(_playButton, "cell 1 3");
		
				_rewindButton = new JToggleButton();
				_rewindButton.setIcon(rewind);
				_rewindButton.setToolTipText("Skip back");
				this.add(_rewindButton, "flowx,cell 2 3");
		this.add(_stopButton, "cell 2 3");
		this.add(_fastForwardButton, "cell 2 3");
		
				_fullScreen = new JButton();
				_fullScreen.setToolTipText("Toggles fullscreen");
				_fullScreen.setIcon(fullScreen);
				_fullScreen.addActionListener(new ActionListener() {
					private EmbeddedMediaPlayerComponent mediaPlayerComponentFullScreen;
					@Override
					public void actionPerformed(ActionEvent e) {
						//			FullScreenMultiMediaTest full = new FullScreenMultiMediaTest(_parent);
						//			full.setMedia(videoLocation);
						//			full.play();
						if (mediaPlayer.getTime() == -1 && _progressSlider.getValue() == 0){
							//check if there has been an input file selected
							if (_videoLocation == null){
								errorPlaybackFile();
							}else{
								//start media from beginning and set play button to pause logo
								_progressSlider.setValue(0);
								mediaPlayer.play();
								//mediaPlayer.stop();
								pause();
								//have to sleep cause vlcj sucks and won't allow
								//getting length until video has played for a small amount of time
								try {
									Thread.sleep(400);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								_timer.start();
								fullScreenToggle();
							}
							//check if video is paused
						}else if (!mediaPlayer.isPlaying()){
							pause();
							fullScreenToggle();
							//pause video otherwise
						}else{
							pause();
							if(_fastForwardButton.isSelected()){
								skipper.cancel(true);
								enableSkips();
							}else if (_rewindButton.isSelected()){
								skipper.cancel(true);
								enableSkips();
							}
							fullScreenToggle();
						}

					}
				});
				add(_fullScreen, "cell 3 3");

		_muteToggle = new JButton();
		_muteToggle.setToolTipText("Mute/Unmute");
		add(_muteToggle, "cell 6 3");
		this.add(_volumeSlider, "cell 6 3");
>>>>>>> ofekdev

		skipper = new SkipWorker(mediaPlayer, true, VideoPanel.this);
	}

	protected void fullScreenToggle() {
		//init();

		String mrlString = mediaPlayer.mrl();
		_fullScreenPlayer = new FullScreenPlayer(mrlString, this);
	}

	/**
	 * Cancels fast forward/rewind
	 */
	public void stopSkipping(){
		skipper.cancel(true);
		_fastForwardButton.setSelected(false);
		_rewindButton.setSelected(false);
		pause();
	}

	/**
	 * Registers listeners to all buttons
	 */
	private void registerListeners() {

		_progressSlider.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseReleased(MouseEvent e) {
				setSliderBasedPosition();
				if (isPlaying){
					play();
					isPlaying = false;
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (mediaPlayer.isPlaying()){
					pause();
					isPlaying = true;
				}
				setSliderBasedPosition();
			}
		});


		_stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Stop media and update progressbar and time labels to 0
				mediaPlayer.stop();
				_playButton.setIcon(play);
				updateTime(0);
				updatePosition(0);

				//Cancel skip buttons if selected
				if(_fastForwardButton.isSelected()){
					skipper.cancel(true);
					_fastForwardButton.setSelected(false);
					_rewindButton.setSelected(false);
				}else if (_rewindButton.isSelected()){
					skipper.cancel(true);
					_rewindButton.setSelected(false);
					_fastForwardButton.setSelected(false);
				}
			}
		});

		_playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playListener();
			}
		});

		_fastForwardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (_rewindButton.isSelected()){
					_rewindButton.setSelected(false);
				}
				if(_fastForwardButton.isSelected()){
					skipper.cancel(true);
					skipper = new SkipWorker(mediaPlayer, true, VideoPanel.this);
					skipper.execute();
				}else{
					skipper.cancel(true);
				}
			}
		});

		_rewindButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (_fastForwardButton.isSelected()){
					_fastForwardButton.setSelected(false);
				}
				if(_rewindButton.isSelected()){
					skipper.cancel(true);
					skipper = new SkipWorker(mediaPlayer, false, VideoPanel.this);
					skipper.execute();
				}else{
					skipper.cancel(true);
				}
			}
		});

		_volumeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				// if(!source.getValueIsAdjusting()) {
				mediaPlayer.setVolume(source.getValue());
				// }
			}
		});

		_muteToggle.setIcon(unmute);
		_muteToggle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.mute();
				if (mediaPlayer.isMute()){
					_muteToggle.setIcon(unmute);
				}else{
					_muteToggle.setIcon(mute);
				}
			}
		});
	}

	/**
	 * Pause the currently playing media file
	 */
	private void pause(){
		mediaPlayer.pause();
		_timer.stop();
		_playButton.setIcon(play);
	}
	/**
	 * resume play of media file
	 */
	public void play(){
		mediaPlayer.start();
		_timer.start();
		_playButton.setIcon(pause);
	}

	/**
	 * Error message for invalid media files
	 */
	private void errorPlaybackFile() {
		JOptionPane.showMessageDialog(this, "No valid media file selected",
				"Location Error", JOptionPane.ERROR_MESSAGE);
	}



	/**
	 * Updates the time label
	 * @param millis: time in milliseconds
	 */
	private void updateTime(long millis) {
		String s = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		_timeLabel.setText(s);
		//		System.out.println(s);
	}
	
	public String getCurrentTime() {
		long millis = mediaPlayer.getTime();
		String s = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		System.out.println(s);
		return s;
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

	public void resetPlayer(){
		_timer.restart();
		_timer.stop();
		mediaPlayer.prepareMedia(_videoLocation);
		_playButton.setIcon(play);
		_progressSlider.setValue(0);
		_timeLabel.setText("00:00:00");
	}

	/**
	 * Set file to be played on media player
	 * @param mediaLocation: Absolute path to media file location
	 */
	public void setMedia(String mediaLocation){
		//changes videolocation, prepares the video to be played, resets timer and progressSlider
		_videoLocation = mediaLocation;
		mediaPlayer.prepareMedia(mediaLocation);
		resetPlayer();
		mediaPlayer.start();
		mediaPlayer.stop();
	}

	private void setSliderBasedPosition() {
		if(!mediaPlayer.isSeekable()) {
			return;
		}
		float positionValue = _progressSlider.getValue();
		if (positionValue>=maxTime){
			_progressSlider.setValue(maxTime);
		}
		mediaPlayer.setPosition(positionValue/maxTime);
		updateTime(mediaPlayer.getTime());
	}

	public void enableSlider(){
		_progressSlider.setEnabled(true);
	}

	public void enableSkips(){
		_fastForwardButton.setSelected(false);
		_rewindButton.setSelected(false);
	}



	public void ContinuePlay(long time) {
		play();
		mediaPlayer.setTime(time);
		updatePosition(time);
		updateTime(time);
	}

	public void StopPlay(long time) {
		play();
		mediaPlayer.setTime(time);
		updatePosition(time);
		updateTime(time);
		pause();
	}

	public long getTime(){
		return mediaPlayer.getTime();
	}

	private void playListener(){
		//check if video hasn't started at all
		if (mediaPlayer.getTime() == -1 && _progressSlider.getValue() == 0){
			//check if there has been an input file selected
			if (_videoLocation == null){
				errorPlaybackFile();
			}else{
				//start media from beginning and set play button to pause logo
				_progressSlider.setValue(0);
				mediaPlayer.play();
				_playButton.setIcon(pause);
				//have to sleep cause vlcj sucks and won't allow
				//getting length until video has played for a small amount of time
				try {
					Thread.sleep(400);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				_timer.start();
			}
			//check if video is paused
		}else if (!mediaPlayer.isPlaying()){
			play();
			//pause video otherwise
		}else{
			pause();
			if(_fastForwardButton.isSelected()){
				skipper.cancel(true);
				enableSkips();
			}else if (_rewindButton.isSelected()){
				skipper.cancel(true);
				enableSkips();
			}
		}
	}
}

