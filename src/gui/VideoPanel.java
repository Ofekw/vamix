package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.binding.LibVlcConst;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import controller.gui.FullScreenPlayer;
import controller.processes.SkipWorker;


@SuppressWarnings("serial")
public class VideoPanel extends JPanel {

	private EmbeddedMediaPlayer _mediaPlayer = null;
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
	private final int maxTime = 100000000;
	private JButton _muteToggle;

	private SkipWorker skipper;
	private JButton _fullScreen;
	//these items are used, but considered suppressed because not initialized
	@SuppressWarnings("unused")
	private EmbeddedMediaPlayer _mediaPlayerFull;
	@SuppressWarnings("unused")
	private FullScreenPlayer _fullScreenPlayer;
	@SuppressWarnings("unused")
	private  AudioTab _audioTab;
	
	private Icon play;
	private Icon stop;
	private Icon fastForward;
	private Icon rewind;
	private Icon pause;
	private Icon fullScreen;
	private Icon unmute;
	private Icon mute;
	private Canvas _mediaCanvas;

	public VideoPanel(MainGui parent){
		this.setOpaque(false);
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
		_mediaCanvas = new Canvas();
		//mediaCanvas.setBackground(Color.black);
		_mediaCanvas.setPreferredSize(new Dimension(parent.getFrame().getWidth()-50,300));

		_mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		_mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(_mediaCanvas));


		// setting up the timer and stopping it so it does not run
		_timer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//checks if the file is at the end and resets it
				//fixs the player crashing
				if (!_mediaPlayer.isPlayable()){
					resetPlayer();
					_progressSlider.setValue(maxTime);
					updateTime(_mediaPlayer.getLength());;
				}else{
					updateTime(_mediaPlayer.getTime());
					updatePosition(_mediaPlayer.getTime());
				}
			}
		});
		_timer.stop();
		this.add(playerBackground,"cell 0 0 7 1,growx");
		playerBackground.add(_mediaCanvas);

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
					@SuppressWarnings("unused")
					private EmbeddedMediaPlayerComponent mediaPlayerComponentFullScreen;
					@Override
					public void actionPerformed(ActionEvent e) {
						//check if video hasn't started at all
						if (_mediaPlayer.getTime() == -1 && _progressSlider.getValue() == 0){
							//check if there has been an input file selected
							if (_videoLocation == null){
								errorPlaybackFile();
							}else{
								//start media from beginning and set play button to pause logo
								_progressSlider.setValue(0);
								_mediaPlayer.play();
								_playButton.setIcon(pause);
								//Sleep thread so video plays, allows for getting video duration
								//Must do this due to vlcj incapabilities
								try {
									Thread.sleep(400);
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
								_timer.start();
							}
							//check if video is paused
						}else if (!_mediaPlayer.isPlaying()){
							//cancel skipping
							skipper.cancel(true);
							enableSkips();
							play();
							//pause video otherwise
						}else{
							//cancel skipping
							skipper.cancel(true);
							enableSkips();
							pause();
							fullScreenToggle();
						}
					}
				});
				add(_fullScreen, "cell 3 3");

		_muteToggle = new JButton();
		_muteToggle.setToolTipText("Mute/Unmute");
		add(_muteToggle, "cell 6 3");
		this.add(_volumeSlider, "cell 6 3");

		skipper = new SkipWorker(_mediaPlayer, true, VideoPanel.this);
	}

	/**
	 * Toggle full screen mode
	 */
	protected void fullScreenToggle() {
		//get video mrl and create new fullscreen player
		String mrlString = _mediaPlayer.mrl();
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

			//play media if media was playing before selecting the slider
			@Override
			public void mouseReleased(MouseEvent e) {
				setSliderBasedPosition();
				if (isPlaying){
					play();
					isPlaying = false;
				}
			}

			//pause media if media was playing when slider selected
			@Override
			public void mousePressed(MouseEvent e) {
				if (_mediaPlayer.isPlaying()){
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
				_mediaPlayer.stop();
				_playButton.setIcon(play);
				updateTime(0);
				updatePosition(0);
				cancelSkipButtons();
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
					//pause media and start fastforwarding
					play();
					overlay("Fast forward");
					skipper.cancel(true);
					skipper = new SkipWorker(_mediaPlayer, true, VideoPanel.this);
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
					//pause media and start rewinding
					play();
					overlay("Rewind");
					skipper.cancel(true);
					skipper = new SkipWorker(_mediaPlayer, false, VideoPanel.this);
					skipper.execute();
				}else{
					skipper.cancel(true);
				}
			}
		});

		//add listener for adjusting sound level
		_volumeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				_mediaPlayer.setVolume(source.getValue());
			}
		});

		_muteToggle.setIcon(unmute);
		_muteToggle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_mediaPlayer.mute();
				//mute media
				if (_mediaPlayer.isMute()){
					overlay("Unmute");
					_muteToggle.setIcon(unmute);
					//unmute media
				}else{
					overlay("Mute");
					_muteToggle.setIcon(mute);
				}
			}
		});
	}
	
	private void cancelSkipButtons(){
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

	/**
	 * Pause the currently playing media file
	 */
	private void pause(){
		_mediaPlayer.pause();
		_timer.stop();
		_playButton.setIcon(play);
	}
	/**
	 * resume play of media file
	 */
	public void play(){
		_mediaPlayer.start();
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
		long millis = _mediaPlayer.getTime();
		String s = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		//System.out.println(s);
		return s;
	}

	/**
	 * updates the progress bar
	 * @param value: new value
	 */
	private void updatePosition(long value) {
		_progressSlider.setValue((int)(((float)value)/_mediaPlayer.getLength()*maxTime));
	}

	/**
	 * Resets the media player to it's default starting point
	 */

	public void resetPlayer(){
		_timer.restart();
		_timer.stop();
		_mediaPlayer.prepareMedia(_videoLocation);
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
		_mediaPlayer.prepareMedia(mediaLocation);
		resetPlayer();
		_mediaPlayer.start();
		_mediaPlayer.stop();
	}

	private void setSliderBasedPosition() {
		if(!_mediaPlayer.isSeekable()) {
			return;
		}
		float positionValue = _progressSlider.getValue();
		if (positionValue>=maxTime){
			_progressSlider.setValue(maxTime);
			
//			Point p = VideoPanel.this._progressSlider.getMousePosition();
//			int width = VideoPanel.this._progressSlider.getWidth();
//			VideoPanel.this._progressSlider.setValue(maxTime *p.x/width);
		}
		_mediaPlayer.setPosition(positionValue/maxTime);
		updateTime(_mediaPlayer.getTime());
	}

	public void enableSlider(){
		_progressSlider.setEnabled(true);
	}

	public void enableSkips(){
		_fastForwardButton.setSelected(false);
		_rewindButton.setSelected(false);
	}


	/**
	 * Used for fullscreen mode, resume play on main video panel to position
	 * of media playing in fullscreen mode
	 * @param time
	 */
	public void ContinuePlay(long time) {
		play();
		_mediaPlayer.setTime(time);
		updatePosition(time);
		updateTime(time);
	}

	/**
	 * Used for full screen mode, stop media when fullscreen is started
	 * @param time: Time of media
	 */
	public void StopPlay(long time) {
		play();
		_mediaPlayer.setTime(time);
		updatePosition(time);
		updateTime(time);
		pause();
	}

	/**
	 * return the current time of the media player
	 * @return: Time of media player
	 */
	public long getTime(){
		return _mediaPlayer.getTime();
	}
	
	private void playListener(){
		//check if video hasn't started at all
		if (_mediaPlayer.getTime() == -1 && _progressSlider.getValue() == 0){
			//check if there has been an input file selected
			if (_videoLocation == null){
				errorPlaybackFile();
			}else{
				//start media from beginning and set play button to pause logo
				_progressSlider.setValue(0);
				_mediaPlayer.play();
				_playButton.setIcon(pause);
				//have to sleep cause vlcj
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
		}else if (!_mediaPlayer.isPlaying()){
			//cancel skipping
			skipper.cancel(true);
			enableSkips();
			overlay("Play");
			play();
			//pause video otherwise
		}else{
			//cancel skipping
			skipper.cancel(true);
			enableSkips();
			overlay("Pause");
			pause();
			}
		}
	/**
	 * Get the media player
	 * @return mediaPlayer object
	 */
	public EmbeddedMediaPlayer getMediaPlayer(){
		return _mediaPlayer;
	}
	
	/**
	 * Creates a short overlay on the media panel to show specific media action (such as play,pause etc)
	 * @param String text for overlay
	 */
	protected void overlay(String text){
		_mediaPlayer.setMarqueeText(text);
		_mediaPlayer.setMarqueeSize(60);
		_mediaPlayer.setMarqueeOpacity(70);
		_mediaPlayer.setMarqueeColour(Color.GREEN);
		_mediaPlayer.setMarqueeTimeout(3000);
		_mediaPlayer.setMarqueeLocation(50, 50);
		_mediaPlayer.enableMarquee(true);
	}
/**
 * Returns the length of the input media
 * @return String formatted time
 */
	public String getLength() {
		long length = _mediaPlayer.getLength();
		play();
		String s = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(length), TimeUnit.MILLISECONDS.toMinutes(length) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(length)), TimeUnit.MILLISECONDS.toSeconds(length) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(length)));
		pause();
		System.out.println(s);
		return s;
		
		
	}
}

