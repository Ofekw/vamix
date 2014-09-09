package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class VideoPanel extends JPanel {

	public VideoPanel(){
		this.setPreferredSize(new Dimension(800, 400));
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		Canvas c = new Canvas();
		c.setBackground(Color.black);
		c.setPreferredSize(new Dimension(800,400));
		EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(c));
		this.add(c);
		mediaPlayer.playMedia("Video", "/home/patrick/206Assignments/big_buck_bunny_720p_stereo.avi");
	}
}
