package gui;

import java.awt.Dimension;

public class AudioTab extends Tab {
	
	public AudioTab(VideoPanel panel){
		super(panel);
	}

	@Override
	protected void initialise() {
		this.setPreferredSize(new Dimension(1000, 130));
		
	}

}
