package gui;

public class DownloadTab extends Tab{

	public DownloadTab(VideoPanel panel) {
		super(panel);
		Download download = new Download(this);
		download.setToolTipText("Media download");
		download.setName("");
		add(download, "cell 0 3,grow");
	}

	@Override
	protected void initialise() {
		// TODO Auto-generated method stub
		
	}

}
