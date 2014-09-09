package gui;

import java.awt.Dimension;

import javax.swing.JPanel;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

public class VideoPanel extends JPanel {

	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

	public VideoPanel(){
		this.setPreferredSize(new Dimension(800, 400));

		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
	}
	
	public void play(String[] args){
		 mediaPlayerComponent.getMediaPlayer().playMedia("Video",args);
	}
}
