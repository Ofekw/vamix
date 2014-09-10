package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import net.miginfocom.swing.MigLayout;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.Timer;

@SuppressWarnings("serial")
public class VideoPanel extends JPanel {

	private final EmbeddedMediaPlayer mediaPlayer;
	private JProgressBar progressBar;


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
			}
		}
	};

	public VideoPanel(){

		this.setMinimumSize(new Dimension(700, 500));
		this.setBackground(Color.black);
		this.setLayout(new MigLayout());

		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();

		// Setup canvas for player to go on


		Canvas mediaCanvas = new Canvas();
		mediaCanvas.setBackground(Color.black);
		mediaCanvas.setPreferredSize(new Dimension(600,350));

		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(mediaCanvas));


		mediaPlayer.prepareMedia("/home/patrick/Downloads/sample.avi");

		//Play/Resume/Restart button
		JButton playButton = new JButton("Play");

		playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
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
