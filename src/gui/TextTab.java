package gui;

import java.applet.Applet;
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

import sun.security.util.DisabledAlgorithmConstraints;

import controller.ReplaceAudioProcess;
import controller.ShellProcess;
import controller.VideoIntroProcess;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TextTab extends Tab {
	private JTextField _textFieldIntro;
	private JTextField _textFieldEnd;
	private JTextPane _txtPreview;
	private JSpinner _fontSize;
	private JComboBox fontColour;
	private JComboBox _fontType;
	private Color _colourSelect = Color.BLACK;
	private JButton _btnColourSelect;
	private TextTab _tab;
	private String _saveLoc;
	private JProgressBar _progressBar;
	private JButton _apply;

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

		_txtPreview = new JTextPane();
		_txtPreview.setEditable(false);
		_txtPreview.setText("Text preview");

		JLabel lblOpeningText = new JLabel("Opening Text:");
		horizontalBox.add(lblOpeningText);

		_textFieldIntro = new JTextField();
		_textFieldIntro.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String text = _textFieldIntro.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());    
				_txtPreview.selectAll();
			}
		});
		_textFieldIntro.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				String text = _textFieldIntro.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());    
				_txtPreview.selectAll();
			}
		});
		_textFieldIntro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String text = _textFieldIntro.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());    
				_txtPreview.selectAll();
			}

		});     
		horizontalBox.add(_textFieldIntro);
		_textFieldIntro.setColumns(10);

		Component verticalStrut = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut);

		Box horizontalBox_1 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_1);

		JLabel lblClosingText = new JLabel("Closing Text:   ");
		horizontalBox_1.add(lblClosingText);

		_textFieldEnd = new JTextField();
		_textFieldEnd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String text = _textFieldEnd.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());    
				_txtPreview.selectAll();
			}
		});

		_textFieldEnd.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String text = _textFieldEnd.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());    
				_txtPreview.selectAll();
			}
		});
		horizontalBox_1.add(_textFieldEnd);
		_textFieldEnd.setColumns(10);

		Component verticalStrut_1 = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut_1);

		Box horizontalBox_2 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_2);

		JLabel lblFontSize = new JLabel("Font Size:");
		horizontalBox_2.add(lblFontSize);

		_fontSize = new JSpinner();
		_fontSize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				String text = _txtPreview.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());    
				_txtPreview.selectAll();
			}
		});
		_fontSize.setMaximumSize(new Dimension(20, 30));
		_fontSize.setModel(new SpinnerNumberModel(24, 8, 54, 1));
		horizontalBox_2.add(_fontSize);

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

		_fontType = new JComboBox();


		_fontType.setMaximumSize(new Dimension(40, 32767));
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String []fontFamilies = ge.getAvailableFontFamilyNames();

		_fontType.setModel(new DefaultComboBoxModel(fontFamilies));
		_fontType.setToolTipText("Font Type");

		//		fontType.addMouseListener(new MouseAdapter() {
		//			@Override
		//			public void mousePressed(MouseEvent arg0) {
		//				String text = txtPreview.getText();
		//				SetPreview(txtPreview, text, getUserColour(), (int)fontSize.getValue(), getUserFont());
		//			}
		//		});

		_fontType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				String text = _txtPreview.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());
			}
		});

		horizontalBox_2.add(_fontType);
		EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));

		_apply = new JButton("Apply Changes");
		_apply.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				SaveLocAndTextProcess();	
			}
		});

		_progressBar = new JProgressBar();
		horizontalBox_2.add(_progressBar);
		horizontalBox_2.add(_apply);

		Box horizontalBox_4 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_4);

		Component verticalStrut_2 = Box.createVerticalStrut(20);
		verticalStrut_2.setMaximumSize(new Dimension(32767, 10));
		verticalStrut_2.setPreferredSize(new Dimension(0, 10));
		horizontalBox_4.add(verticalStrut_2);

		Box horizontalBox_3 = Box.createHorizontalBox();
		horizontalBox_3.setPreferredSize(new Dimension(980, 600));
		verticalBox.add(horizontalBox_3);


		horizontalBox_3.add(_txtPreview);

		JScrollPane scrollBar = new JScrollPane(_txtPreview);
		//scrollBar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR);
		//scrollBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollBar.setViewportView(_txtPreview);
		horizontalBox_3.add(scrollBar);

	}

	private void createColourChooser() {

		_colourSelect = JColorChooser.showDialog(this, "Choose a color", Color.WHITE);
		System.out.println("The selected color was:" + _colourSelect);

		String text = _txtPreview.getText();
		SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());


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
		String fontString = _fontType.getSelectedItem().toString();
		return fontString;
	}

	private Color getUserColour() {
		return _colourSelect;
	}

	private void createProcess() {
		if(!_textFieldIntro.getText().isEmpty()){
			VideoIntroProcess process = new VideoIntroProcess(this, (int)_fontSize.getValue(), getUserFont(), _textFieldIntro.getText(), _colourSelect);
			process.execute();
		}
		
		if(!_textFieldEnd.getText().isEmpty()){
			VideoIntroProcess process = new VideoIntroProcess(this, (int)_fontSize.getValue(), getUserFont(), _textFieldIntro.getText(), _colourSelect);
			process.execute();
		}
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

		fileChooser.setDialogTitle("Specify where to save the video with text");

		int userSelection = fileChooser.showSaveDialog(this);
		File fileToSave = null;
		if (userSelection == JFileChooser.OPEN_DIALOG) {
			fileToSave = fileChooser.getSelectedFile();
			/*
			 * Makes sure the filename ends with extension .mp4
			 */

			if (!fileChooser.getSelectedFile().getAbsolutePath()
					.endsWith(".mp4")) {
				fileToSave = new File(fileChooser.getSelectedFile()
						+ ".mp4");
			}
			_saveLoc=fileToSave.getAbsolutePath();
			createProcess();
			_progressBar.setValue(0);
			_apply.setEnabled(false);
			_progressBar.setIndeterminate(true);
			_progressBar.setValue(100);
			disableButtons();
		}

	}

	public String getSaveloc(){
		return _saveLoc;
	}

	public void disableButtons(){
		_textFieldEnd.setEnabled(false);
		_textFieldIntro.setEnabled(false);
		_apply.setEnabled(false);
		_btnColourSelect.setEnabled(false);
		_fontSize.setEnabled(false);
		_fontType.setEnabled(false);
	}

	public void enableButtons(){
		_textFieldEnd.setEnabled(true);
		_textFieldIntro.setEnabled(true);
		_apply.setEnabled(true);
		_btnColourSelect.setEnabled(true);
		_fontSize.setEnabled(true);
		_fontType.setEnabled(true);
		_progressBar.setIndeterminate(false);
	}
}
