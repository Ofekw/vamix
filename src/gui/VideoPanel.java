package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import net.miginfocom.swing.MigLayout;

import sun.applet.Main;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.Timer;
import javax.swing.JSlider;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class VideoPanel extends JPanel {

	private final EmbeddedMediaPlayer mediaPlayer;
	private JProgressBar progressBar;
	private JSlider _progressSlider;
	private MainGui _parent;


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
		this.setBackground(Color.black);
		this.setLayout(new MigLayout("", "[]", "[][][][][]"));
		createControls();

		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();

		// Setup canvas for player to go on


		Canvas mediaCanvas = new Canvas();
		mediaCanvas.setBackground(Color.black);
		mediaCanvas.setPreferredSize(new Dimension(600,350));

		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(mediaCanvas));
		//Play/Resume/Restart button
		JButton playButton = new JButton("Play");

		playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String mediaLoc = _parent.getVideo().getVideoLoc();
				if(!mediaLoc.isEmpty()){
					//add file checks here
				mediaPlayer.prepareMedia(mediaLoc);
				}else{
					
				}
				if (mediaPlayer.getTime() == -1){
					mediaPlayer.play();
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
				}else{
					mediaPlayer.pause();
				}
			}
		});

		progressBar = new JProgressBar();
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
		add(_progressSlider, "cell 0 2,growx");
		this.add(playButton, "cell 0 3,growx");
	}
	
	private void createControls() {
		// TODO Auto-generated method stub
		
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
	
	
}
