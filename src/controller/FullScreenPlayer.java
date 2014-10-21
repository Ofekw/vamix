package controller;
import gui.VideoPanel;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class FullScreenPlayer {
	/**
	 * Full screen player copied and modified from the VLCJ library
	 * @param  video media player panel in the main gui
	 */

	public static void main(final String args, final VideoPanel panel) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new FullScreenPlayer(args,panel);
			}
		});
	}

	protected long _time;
	protected VideoPanel _panel;

	public FullScreenPlayer(String args, VideoPanel panel) {
		this._panel = panel;
		Canvas c = new Canvas();

		c.setBackground(Color.black);

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(c, BorderLayout.CENTER);

		final JFrame f = new JFrame();

		//creates the full screen panel
		f.setContentPane(p);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 600);

		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		final EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new DefaultFullScreenStrategy(f));
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(c));

		f.setVisible(true);

		mediaPlayer.setFullScreen(true);

		// Put a Thread.sleep(500) here if you get a fatal JVM crash
		mediaPlayer.playMedia(args);
		if (_panel.getTime()>0){
			mediaPlayer.setTime(_panel.getTime());
		}
		
		
		f.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();
				if(code == KeyEvent.VK_ESCAPE){
					_time = mediaPlayer.getTime();
					//Key pressed is the Escape key. Exit fullscreen
					if (!mediaPlayer.isPlaying()){
						_panel.StopPlay(_time);
					}else{//if playing
						_panel.ContinuePlay(_time);
						
					}
					mediaPlayer.stop();
					f.dispose();
				}else if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE){
					@SuppressWarnings("unused")
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

					//Key press is Enter, play/pause video
					if (!mediaPlayer.isPlaying()){
						mediaPlayer.play();					
						//pause video otherwise
					}else{
						mediaPlayer.pause();
					}
				}
			}
		});
	}


}