package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;


public abstract class Tab extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4544519857638271081L;
	protected VideoPanel _videoPanel;
	
	public Tab (VideoPanel panel){
		//super(new FlowLayout(FlowLayout.LEFT));
		this.setPreferredSize(new Dimension(1000, 130));
		_videoPanel = panel;
		initialise();
	}

	protected abstract void initialise();
}
