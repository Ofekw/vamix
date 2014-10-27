package model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
/**
 * This is a model of the textpane in the text tab. This displays the background image on the preview to give an idea of the final intro/outro product.
 * @author ofek
 *
 */

public class PreviewPane extends JTextPane   {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private String _name = "bg1";
	BufferedImage img = null;

	public static final String DIRECTORY = "directory";
	public static final String FILE = "file";
	public static final String COMPUTER = "computer";
	public static final String HARD_DRIVE = "harddrive";
	public static final String FLOPPY = "floppy";

	Map<String, Image> _images = new HashMap<>();

	private Image currentImage;

	public PreviewPane() {
		super();
		setOpaque(false);
		StyledDocument document = this.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		document.setParagraphAttributes(0, document.getLength(), center, false);
		initImageMap();
	}
	/**
	 * gets the resource files and adds them to a map
	 */
	private void initImageMap() {
		ImageIcon bg0 = new ImageIcon(getClass().getResource("/icons/"+"bg0"+".png"));
		ImageIcon bg1 = new ImageIcon(getClass().getResource("/icons/"+"bg1"+".png"));
		ImageIcon bg2 = new ImageIcon(getClass().getResource("/icons/"+"bg2"+".png"));
		ImageIcon bg3 = new ImageIcon(getClass().getResource("/icons/"+"bg3"+".png"));
		ImageIcon bg4 = new ImageIcon(getClass().getResource("/icons/"+"bg4"+".png"));
		_images.put("bg0", bg0.getImage());
		_images.put("bg1", bg1.getImage());
		_images.put("bg2", bg2.getImage());
		_images.put("bg3", bg3.getImage());
		_images.put("bg4", bg4.getImage());
	}

	public void repaintBackground(String key) {
		currentImage = _images.get(key);
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {

		g.drawImage(currentImage, 0, 0, getWidth(), getHeight(), this);
		super.paintComponent(g);
	}


}
