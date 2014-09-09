package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.Timer;

@SuppressWarnings("serial")
public class VideoPanel extends JPanel {

	private final EmbeddedMediaPlayer mediaPlayer;
	private JProgressBar progressBar;
	private Timer timer;

	public VideoPanel(){

		this.setMinimumSize(new Dimension(1000, 600));
		this.setBackground(Color.black);
		this.setLayout(new MigLayout());

		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();

		// Setup canvas for player to go on
		timer = new Timer(200, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//System.out.println(mediaPlayer.getTime());
				progressBar.setValue((int)mediaPlayer.getTime());
			}
		});

		Canvas mediaCanvas = new Canvas();
		mediaCanvas.setBackground(Color.black);
		mediaCanvas.setPreferredSize(new Dimension(900,350));

		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(mediaCanvas));


		mediaPlayer.prepareMedia("/home/patrick/Downloads/How.to.Train.Your.Dragon.2.2014.HDRip.XViD-juggs[ETRG].avi");

		//Play/Resume/Restart button
		JButton playButton = new JButton("Play");

		playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (mediaPlayer.getTime() == -1){
					mediaPlayer.play();
					timer.start();
					try {
						Thread.sleep(400);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					progressBar.setMaximum((int)(mediaPlayer.getLength()));
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

		this.add(mediaCanvas, "growx, wrap");
		this.add(progressBar, "growx, wrap");
		this.add(playButton, "growx, wrap");
	}

	/**
	 * Set file to be played on media player
	 * @param mediaLocation: Absolute path to media file location
	 */
	public void setMedia(String mediaLocation){
		mediaPlayer.prepareMedia(mediaLocation);
	}
}
