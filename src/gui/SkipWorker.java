package gui;

import java.util.List;

import javax.swing.JToggleButton;
import javax.swing.JViewport;
import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;


/**
 * Swing worker class for handling constant fast forwarding/ rewinding of video
 * @author patrick
 *
 */
public class SkipWorker extends SwingWorker<Void, Integer> {

	private static final int SKIP_TIME_MS = 1000;
	private EmbeddedMediaPlayer _player;
	private boolean _fastForward;
	private VideoPanel _panel;

	/**
	 * 
	 * @param mediaPlayer: media player with content to skip
	 * @param fastFoward: true if fastForwarding, false if rewinding
	 */
	public SkipWorker(EmbeddedMediaPlayer mediaPlayer, boolean fastFoward, VideoPanel panel){
		_player = mediaPlayer;
		_fastForward = fastFoward;
		_panel = panel;
	}

	@Override
	protected Void doInBackground() throws Exception {
		while(!isCancelled()){
			if (_fastForward){
				publish(SKIP_TIME_MS);
			}else{
				publish(-SKIP_TIME_MS);
			}
			Thread.sleep(100);
		}
		return null;
	}

	@Override
	protected void process(List<Integer> chunks) {
		for (Integer i : chunks) {
			skip(i);
		}
	}

	private void skip(int skipTime) {
		// Only skip time if can handle time setting
		if(_player.getLength() > 0) {
			_player.skip(skipTime);
		}
	}

}
