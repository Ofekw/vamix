package gui;

import javax.swing.JPanel;


public abstract class Tab extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4544519857638271081L;
	protected VideoPanel _videoPanel;
	
	public Tab (VideoPanel panel){
		_videoPanel = panel;
		initialise();
	}

	protected abstract void initialise();
}
