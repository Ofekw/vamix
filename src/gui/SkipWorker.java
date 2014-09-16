package gui;

import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;


/**
 * Swing worker class for handling constant fast forwarding/ rewinding of video
 * @author patrick
 *
 */
public class SkipWorker extends SwingWorker<Void, Void> {

	private static final int SKIP_TIME_MS = 1000;
	private EmbeddedMediaPlayer _player;
	private boolean _fastForward;
	
	/**
	 * 
	 * @param mediaPlayer: media player with content to skip
	 * @param fastFoward: true if fastForwarding, false if rewinding
	 */
	public SkipWorker(EmbeddedMediaPlayer mediaPlayer, boolean fastFoward){
		_player = mediaPlayer;
		_fastForward = fastFoward;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		while(!isCancelled()){
			if (_fastForward){
			skip(SKIP_TIME_MS);
			}else{
				skip(-SKIP_TIME_MS);
			}
			Thread.sleep(100);
		}
		return null;
	}
	
	private void skip(int skipTime) {
		// Only skip time if can handle time setting
		if(_player.getLength() > 0) {
			_player.skip(skipTime);
			//updateUIState();
		}
	}

}
