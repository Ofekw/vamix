package controller.gui;
import gui.VideoPanel;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
	private EmbeddedMediaPlayer _mediaPlayer;

	public FullScreenPlayer(String args, VideoPanel panel) {
		this._panel = panel;
		final Canvas c = new Canvas();

		c.setBackground(Color.black);

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(c, BorderLayout.CENTER);

		final JFrame f = new JFrame();

		//creates the full screen panel
		f.setContentPane(p);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setSize(800, 600);

		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		_mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new DefaultFullScreenStrategy(f));
		_mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(c));

		f.setVisible(true);

		_mediaPlayer.setFullScreen(true);
		
		overlay("TEXT!!!!");

		// Put a Thread.sleep(500) here if you get a fatal JVM crash
		_mediaPlayer.playMedia(args);
		if (_panel.getTime()>0){
			_mediaPlayer.setTime(_panel.getTime());
		}
		
		
		c.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();
				if(code == KeyEvent.VK_ESCAPE){
					_time = _mediaPlayer.getTime();
					//Key pressed is the Escape key. Exit fullscreen
					if (!_mediaPlayer.isPlaying()){
						_panel.StopPlay(_time);
					}else{//if playing
						_panel.ContinuePlay(_time);
						
					}
					_mediaPlayer.stop();
					f.dispose();
				}else if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE){
					@SuppressWarnings("unused")
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

					//Key press is Enter, play/pause video
					if (!_mediaPlayer.isPlaying()){
						_mediaPlayer.play();					
						//pause video otherwise
					}else{
						_mediaPlayer.pause();
					}
				}
			}
		});
		
		f.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();
				if(code == KeyEvent.VK_ESCAPE){
					_time = _mediaPlayer.getTime();
					//Key pressed is the Escape key. Exit fullscreen
					if (!_mediaPlayer.isPlaying()){
						_panel.StopPlay(_time);
					}else{//if playing
						_panel.ContinuePlay(_time);
						
					}
					_mediaPlayer.stop();
					f.dispose();
				}else if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE){
					@SuppressWarnings("unused")
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

					//Key press is Enter, play/pause video
					if (!_mediaPlayer.isPlaying()){
						_mediaPlayer.play();
						overlay("Play");
						//pause video otherwise
					}else{
						_mediaPlayer.pause();
						overlay("Pause");
					}
				}
			}
		});
		
		f.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				c.requestFocus();
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				_mediaPlayer.release();
				f.dispose();
				
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				
				
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				c.requestFocus();
				
			}
		});
		
		
	}
	
	/**
	 * Creates a short overlay on the media panel to show specific media action (such as play,pause etc)
	 * @param String text for overlay
	 */
	private void overlay(String text){
		_mediaPlayer.setMarqueeText(text);
		_mediaPlayer.setMarqueeSize(60);
		_mediaPlayer.setMarqueeOpacity(70);
		_mediaPlayer.setMarqueeColour(Color.GREEN);
		_mediaPlayer.setMarqueeTimeout(3000);
		_mediaPlayer.setMarqueeLocation(50, 50);
		_mediaPlayer.enableMarquee(true);
	}


}