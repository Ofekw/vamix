package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import controller.ReplaceAudioProcess;
import controller.ShellProcess;
import controller.videoIntroProcess;

public class TextTab extends Tab {
	private JTextField textFieldIntro;
	private JTextField textFieldEnd;
	private JTextPane txtPreview;
	private JSpinner fontSize;
	private JComboBox fontColour;
	private JComboBox fontType;
	private Color _colourSelect = Color.BLACK;
	private JButton _btnColourSelect;
	private TextTab _tab;
	private String _saveLoc;
	private JProgressBar _progressBar;
	private JButton apply;

	public TextTab(VideoPanel panel) {

		super(panel);
		this._tab=this;

	}

	@Override
	protected void initialise() {
		this.setPreferredSize(new Dimension(1000, 130));

		Box verticalBox = Box.createVerticalBox();
		verticalBox.setPreferredSize(new Dimension(980,170));

		add(verticalBox);

		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);
		
		txtPreview = new JTextPane();
		txtPreview.setEditable(false);
		txtPreview.setText("Text preview");

		JLabel lblOpeningText = new JLabel("Opening Text:");
		horizontalBox.add(lblOpeningText);

		textFieldIntro = new JTextField();
		textFieldIntro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String text = textFieldIntro.getText();
				SetPreview(txtPreview, text, getUserColour(), (int)fontSize.getValue(), getUserFont());    
				txtPreview.selectAll();
			}

		});     
		horizontalBox.add(textFieldIntro);
		textFieldIntro.setColumns(10);

		Component verticalStrut = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut);

		Box horizontalBox_1 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_1);

		JLabel lblClosingText = new JLabel("Closing Text:   ");
		horizontalBox_1.add(lblClosingText);

		textFieldEnd = new JTextField();
		textFieldEnd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String text = textFieldEnd.getText();
				SetPreview(txtPreview, text, getUserColour(), (int)fontSize.getValue(), getUserFont());    
				txtPreview.selectAll();
			}
		});
		horizontalBox_1.add(textFieldEnd);
		textFieldEnd.setColumns(10);

		Component verticalStrut_1 = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut_1);

		Box horizontalBox_2 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_2);

		JLabel lblFontSize = new JLabel("Font Size:");
		horizontalBox_2.add(lblFontSize);

		fontSize = new JSpinner();
		fontSize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				String text = txtPreview.getText();
				SetPreview(txtPreview, text, getUserColour(), (int)fontSize.getValue(), getUserFont());    
				txtPreview.selectAll();
			}
		});
		fontSize.setMaximumSize(new Dimension(20, 30));
		fontSize.setModel(new SpinnerNumberModel(14, 8, 64, 1));
		horizontalBox_2.add(fontSize);

		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox_2.add(rigidArea);

		JLabel lblNewLabel = new JLabel("Font Colour: ");
		//horizontalBox_2.add(lblNewLabel);

		//		fontColour = new JComboBox();
		//		fontColour.setToolTipText("Font Colour");
		//		fontColour.setMaximumSize(new Dimension(25, 32767));
		//		Color[] colours = {
		//                Color.RED,
		//                Color.BLUE,
		//                Color.DARK_GRAY,
		//                Color.PINK,
		//                Color.BLACK,
		//                Color.MAGENTA,
		//                Color.YELLOW,
		//                Color.ORANGE
		//              };
		//		fontColour.setModel(new DefaultComboBoxModel(colours));

		//horizontalBox_2.add(colorChooser);

		_btnColourSelect = new JButton("Colour Select");
		_btnColourSelect.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				createColourChooser();
				_btnColourSelect.setForeground(getUserColour());
			}
		});
		horizontalBox_2.add(_btnColourSelect);
		//horizontalBox_2.add(fontColour);

		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox_2.add(rigidArea_1);

		JLabel lblFontType = new JLabel("Font Type: ");
		horizontalBox_2.add(lblFontType);

		fontType = new JComboBox();
		
		
		fontType.setMaximumSize(new Dimension(40, 32767));
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String []fontFamilies = ge.getAvailableFontFamilyNames();

		fontType.setModel(new DefaultComboBoxModel(fontFamilies));
		fontType.setToolTipText("Font Type");
		
//		fontType.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent arg0) {
//				String text = txtPreview.getText();
//				SetPreview(txtPreview, text, getUserColour(), (int)fontSize.getValue(), getUserFont());
//			}
//		});
		
		fontType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				String text = txtPreview.getText();
				SetPreview(txtPreview, text, getUserColour(), (int)fontSize.getValue(), getUserFont());
			}
		});

		horizontalBox_2.add(fontType);
		EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));

		apply = new JButton("Apply Changes");
		apply.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				createProcess();	
			}
		});
		
		_progressBar = new JProgressBar();
		horizontalBox_2.add(_progressBar);
		horizontalBox_2.add(apply);
		
		Box horizontalBox_4 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_4);
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		verticalStrut_2.setMaximumSize(new Dimension(32767, 10));
		verticalStrut_2.setPreferredSize(new Dimension(0, 10));
		horizontalBox_4.add(verticalStrut_2);

		Box horizontalBox_3 = Box.createHorizontalBox();
		horizontalBox_3.setPreferredSize(new Dimension(980, 600));
		verticalBox.add(horizontalBox_3);

		
		horizontalBox_3.add(txtPreview);

		JScrollPane scrollBar = new JScrollPane(txtPreview);
		//scrollBar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR);
		//scrollBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollBar.setViewportView(txtPreview);
		horizontalBox_3.add(scrollBar);

	}

	private void createColourChooser() {

		_colourSelect = JColorChooser.showDialog(this, "Choose a color", Color.WHITE);
		System.out.println("The selected color was:" + _colourSelect);
		
		String text = txtPreview.getText();
		SetPreview(txtPreview, text, getUserColour(), (int)fontSize.getValue(), getUserFont());
		

	}


	private void SetPreview(JTextPane tp, String msg, Color c, int fontSize, String font) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
		aset=sc.addAttribute(aset, StyleConstants.FontSize, fontSize);

		aset = sc.addAttribute(aset, StyleConstants.FontFamily, font);
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

		int len = tp.getDocument().getLength();
		tp.setCaretPosition(len);
		tp.setCharacterAttributes(aset, false);
		tp.setText(msg);

	}

	private String getUserFont() {
		String fontString = fontType.getSelectedItem().toString();
		return fontString;
	}

	private Color getUserColour() {
		return _colourSelect;
	}
	
	private void createProcess() {
		System.out.println(_colourSelect.toString());
		videoIntroProcess process = new videoIntroProcess(this, (int)fontSize.getValue(), getUserFont(), textFieldIntro.getText(), _colourSelect);
		process.execute();
	}
	
	private void SaveLocAndTextProcess(){
		JFileChooser fileChooser = new JFileChooser() {
			@Override
			public void approveSelection() {

				File f = getSelectedFile();
				/*
				 * Makes sure the filename ends with extension .mp3 when checking for overwrite
				 */

				if (!f.getAbsolutePath()
						.endsWith(".mp4")) {
					f = new File(getSelectedFile()
							+ ".mp4");
				}
				if (f.exists() && getDialogType() == SAVE_DIALOG) {
					int result = JOptionPane
							.showConfirmDialog(
									this,
									"The file exists, overwrite?",
									"Existing file",
									JOptionPane.YES_NO_OPTION);
					switch (result) {
					case JOptionPane.YES_OPTION:
						ShellProcess
						.command("rm -f " + f.getAbsolutePath());
						super.approveSelection();
						return;
					case JOptionPane.NO_OPTION:
						cancelSelection();
						return;
					case JOptionPane.CLOSED_OPTION:
						return;
					case JOptionPane.CANCEL_OPTION:
						cancelSelection();
						return;
					}
				}
				super.approveSelection();
			}
		};

		fileChooser.setDialogTitle("Specify where to save media file with text");

		int userSelection = fileChooser.showSaveDialog(this);
		File fileToSave = null;
		if (userSelection == JFileChooser.OPEN_DIALOG) {
			fileToSave = fileChooser.getSelectedFile();
			/*
			 * Makes sure the filename ends with extension .mp3
			 */

			if (!fileChooser.getSelectedFile().getAbsolutePath()
					.endsWith(".mp4")) {
				fileToSave = new File(fileChooser.getSelectedFile()
						+ ".mp4");
			}
			_saveLoc=fileToSave.getAbsolutePath();
			createProcess();
			apply.setEnabled(false);
			_progressBar.setIndeterminate(true);
		}
		
	}
}
