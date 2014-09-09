package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class VideoPanel extends JPanel {

	public VideoPanel(){
		this.setPreferredSize(new Dimension(800, 400));
		this.setLayout(new MigLayout());

		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		Canvas c = new Canvas();
		c.setBackground(Color.black);
		c.setPreferredSize(new Dimension(800,400));
		EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(c));
		this.add(c);
	}
	//	
	//	public void play(){
	//		 mediaPlayerComponent.getMediaPlayer().playMedia("Video", "");
	//	}
}
