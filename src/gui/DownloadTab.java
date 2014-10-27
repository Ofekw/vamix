package gui;

public class DownloadTab extends Tab{

	/**
	 * Download tab that contains the download object to download media
	 * @author ofek
	 */
	private static final long serialVersionUID = 7930042257291577125L;

	public DownloadTab(VideoPanel panel) {
		super(panel);
		Download download = new Download(this);
		download.setToolTipText("Media download");
		download.setName("");
		add(download, "cell 0 3,grow");
	}

	@Override
	protected void initialise() {	
	}

}
