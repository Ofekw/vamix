package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import model.CharsOnlyLimitFilter;
import model.PreviewPane;
import controller.gui.CheckFile;
import controller.processes.SaveLoadState;
import controller.processes.ShellProcess;
import controller.processes.VideoIntroProcess;
import controller.processes.VideoOutroProcess;
/**
 * Tab for the creation of intro/credits screens
 * This tab is used to add opening and closing text scenes to the selected video file
 * @author Ofek Wittenberg
 *
 */
@SuppressWarnings("serial")
public class TextTab extends Tab {
	private JTextField _textFieldIntro;
	private JTextField _textFieldEnd;
	private PreviewPane _txtPreview;
	private JSpinner _fontSize;
	private JComboBox<String> _fontType;
	private Color _colourSelect = Color.RED;
	private JButton _btnColourSelect;
	private MainGui _main;
	private String _saveLoc;
	private JProgressBar _progressBar;
	private JButton _apply;
	private int _processNumber;
	private String _background = "bg0";

	private SaveLoadState saveLoad;
	private JComboBox<String> _backgroundSelect;

	public TextTab(VideoPanel panel, MainGui main) {

		super(panel);
		this._main=main;

	}
	/**
	 * initializes the main ui components
	 */
	@Override
	protected void initialise() {
	this.setPreferredSize(new Dimension(1000, 130));

		Box verticalBox = Box.createVerticalBox();
		verticalBox.setPreferredSize(new Dimension(980,170));

		add(verticalBox);

		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);

		_txtPreview = new PreviewPane();
		
		_txtPreview.setToolTipText("This pane shows a preview of the text with formatting");
		_txtPreview.setFont(new Font("Dialog", Font.PLAIN, 24));
		_txtPreview.setEditable(false);

		JLabel lblOpeningText = new JLabel("Opening Text:");
		horizontalBox.add(lblOpeningText);

		_textFieldIntro = new JTextField();
		CharsOnlyLimitFilter charFilter = new CharsOnlyLimitFilter(200); // characters limited to 200 and no special chars this is about 40 words. A limit of 40 words is set so everything fits in the frame even with a large font


		((AbstractDocument) _textFieldIntro.getDocument()).setDocumentFilter(charFilter);


		/**
		 * adds relevant listeners to the text field to update the live preview pane whilst typing
		 */
		_textFieldIntro.setToolTipText("Text to be placed at the start of the video (999 character limit)");
		_textFieldIntro.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String text = _textFieldIntro.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());    
				_txtPreview.selectAll();
				if(!_textFieldIntro.getText().isEmpty()){
					_apply.setEnabled(true);
				}else if(_textFieldEnd.getText().isEmpty()){
					_apply.setEnabled(false);
				}
			}
			@Override
			public void focusGained(FocusEvent arg0) {
				String text = _textFieldIntro.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());    
				_txtPreview.selectAll();
				if(!_textFieldIntro.getText().isEmpty()){
					_apply.setEnabled(true);
				}else if(_textFieldEnd.getText().isEmpty()){
					_apply.setEnabled(false);
				}
			}
		});
		
		/**
		 * key listeners added to update textpane as users type
		 */
		
		_textFieldIntro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				String text = _textFieldIntro.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());    
				_txtPreview.selectAll();
				if(!_textFieldIntro.getText().isEmpty()){
					_apply.setEnabled(true);
				}else if(_textFieldEnd.getText().isEmpty()){
					_apply.setEnabled(false);
				}
			}
		});

		_textFieldIntro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String text = _textFieldIntro.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());    
				_txtPreview.selectAll();
				if(!_textFieldIntro.getText().isEmpty()){
					_apply.setEnabled(true);
				}else if(_textFieldEnd.getText().isEmpty()){
					_apply.setEnabled(false);
				}
			}

		});     
		horizontalBox.add(_textFieldIntro);
		_textFieldIntro.setColumns(10);

		Component verticalStrut = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut);

		Box horizontalBox_1 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_1);

		JLabel lblClosingText = new JLabel("Closing Text:  ");
		horizontalBox_1.add(lblClosingText);

		_textFieldEnd = new JTextField();
		_textFieldEnd.setToolTipText("Text to be placed at the end of the video (250 character limit)");


		((AbstractDocument) _textFieldEnd.getDocument()).setDocumentFilter(charFilter);
		/**
		 * similiar to the intro text, key listeners are added to update the preview pane as users type
		 */
		_textFieldEnd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String text = _textFieldEnd.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());    
				_txtPreview.selectAll();
				if(!_textFieldEnd.getText().isEmpty()){
					_apply.setEnabled(true);
				}else if(_textFieldIntro.getText().isEmpty()){
					_apply.setEnabled(false);
				}
			}
		});

		_textFieldEnd.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String text = _textFieldEnd.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());    
				_txtPreview.selectAll();
				if(!_textFieldEnd.getText().isEmpty()){
					_apply.setEnabled(true);
				}else if(_textFieldIntro.getText().isEmpty()){
					_apply.setEnabled(false);
				}
			}
			
			/**
			 * makes sure to set the preview immediately when text focus is gained
			 */
			@Override
			public void focusGained(FocusEvent e) {
				String text = _textFieldEnd.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());    
				_txtPreview.selectAll();
				if(!_textFieldEnd.getText().isEmpty()){
					_apply.setEnabled(true);
				}else if(_textFieldIntro.getText().isEmpty()){
					_apply.setEnabled(false);
				}
			}
		});
		
		
		_textFieldEnd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String text = _textFieldEnd.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());    
				_txtPreview.selectAll();
				if(!_textFieldEnd.getText().isEmpty()){
					_apply.setEnabled(true);
				}else if(_textFieldIntro.getText().isEmpty()){
					_apply.setEnabled(false);
				}
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
		
		/**
		 * spinners for setting the font
		 */

		_fontSize = new JSpinner();
		_fontSize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				String text = _txtPreview.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());    
				_txtPreview.selectAll();
			}
		});
		_fontSize.setMaximumSize(new Dimension(20, 30));
		_fontSize.setModel(new SpinnerNumberModel(24, 8, 64, 1));
		horizontalBox_2.add(_fontSize);

		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox_2.add(rigidArea);
		/**
		 * button to change the colour of the text (button also changes colour to reflect this)
		 */
		_btnColourSelect = new JButton("Colour Select");
		_btnColourSelect.setForeground(Color.RED);
		_btnColourSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		_btnColourSelect.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				createColourChooser();
				_btnColourSelect.setForeground(getUserColour());
			}
		});
		horizontalBox_2.add(_btnColourSelect);
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox_2.add(rigidArea_1);

		JLabel lblFontType = new JLabel("Font Type: ");
		horizontalBox_2.add(lblFontType);

		_fontType = new JComboBox<String>();


		_fontType.setMaximumSize(new Dimension(40, 32767));
		//Can't use enviroment varialbes as it's impossible to get font location which is needed for drawtext
		//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		//		String []fontFamilies = ge.getAvailableFontFamilyNames();

		_fontType.setModel(new DefaultComboBoxModel<String>(new String[] {"DejaVu Sans", "Ubuntu", "FreeSans", "Liberation Serif", "NanumGothic"}));
		_fontType.setToolTipText("Font Type");


		_fontType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				String text = _txtPreview.getText();
				SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());
			}
		});

		horizontalBox_2.add(_fontType);
		/**
		 * following the existing design scheme of having one apply button for a user focused experience
		 */
		_apply = new JButton("Apply");
		_apply.setEnabled(false);
		_apply.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(_apply.isEnabled()){
					if (!_main.getVideo().getVideoLoc().isEmpty() &&
							new CheckFile(true).checkFileType(_main.getVideo().getVideoLoc())){
						SaveLocAndTextProcess();	
					}else {
						noMediaSelected();
					}
				}
			}
		});
		
<<<<<<< HEAD
=======
		_txtPreview.repaintBackground("bg0");
		_backgroundSelect = new JComboBox<String>();
		/**
		 * combo box which has the selection of different backgrounds the user can choose from
		 */
		_backgroundSelect.setModel(new DefaultComboBoxModel<String>(new String[] { "Background 1", "Background 2", "Background 3", "Background 4", "Background 5"}));
		_backgroundSelect.setToolTipText("Background type");
		horizontalBox_2.add(_backgroundSelect);
		
		_backgroundSelect.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				switch (_backgroundSelect.getSelectedIndex()) {
				case 0: _txtPreview.repaintBackground("bg0");
				_background="bg0";
				/**
				 * switch case to appropriately repaint the textpreview pane to selected background	
				 */
					break;
				case 1: _txtPreview.repaintBackground("bg1");
				_background="bg1";
					break;
				case 2: _txtPreview.repaintBackground("bg2");
				_background="bg2";
					break;
				case 3: _txtPreview.repaintBackground("bg3");
				_background="bg3";
					break;
				case 4: _txtPreview.repaintBackground("bg4");
				_background="bg4";
					break;
				default: _txtPreview.repaintBackground("bg0");
				_background="bg0";
					break;
				}
			}
		});
		

<<<<<<< HEAD
>>>>>>> ofekdev

=======
		/**
		 * Indeterminate progress bar to show that the application is working 
		 */
>>>>>>> ofekdev
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
		scrollBar.setViewportView(_txtPreview);
		horizontalBox_3.add(scrollBar);

	}
	/**
	 * An interactive file chooser to set colour of text with fancy colour wheel
	 */
	private void createColourChooser() {

		_colourSelect = JColorChooser.showDialog(this, "Choose a color", Color.RED);

		String text = _txtPreview.getText();
		SetPreview(_txtPreview, text, getUserColour(), (int)_fontSize.getValue(), getUserFont());


	}

	/**
	 * sets the preview pane (called by the action/change listners)
	 * @param tp
	 * @param msg
	 * @param c
	 * @param fontSize
	 * @param font
	 */
	private void SetPreview(JTextPane tp, String msg, Color c, int fontSize, String font) {
		/*
		 * Method used to update the preview field
		 */
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
	/**
	 * obtains the selected user font for the linux process
	 * @return string colour
	 */
	private String getUserFont() {

		String fontString = _fontType.getSelectedItem().toString();
		return fontString;
	}
	/**
	 * returns the default font location on ubuntu machines
	 * @return fontloc
	 */
	public String getUserFontLoc(){
		String fontSelected = _fontType.getSelectedItem().toString();
		String fontLoc;
		switch (fontSelected) {
		case "DejaVu Sans":  fontLoc = "/usr/share/fonts/truetype/ttf-dejavu/DejaVuSans.ttf";
		break;
		case "Ubuntu":  fontLoc = "/usr/share/fonts/truetype/ubuntu-font-family/Ubuntu-RI.ttf";
		break;
		case "FreeSans":  fontLoc = "/usr/share/fonts/truetype/freefont/FreeSans.ttf";
		break;
		case "Liberation Serif":  fontLoc = "/usr/share/fonts/truetype/liberation/LiberationSans-Regular.ttf";
		break;
		case "NanumGothic":  fontLoc = "/usr/share/fonts/truetype/nanum/NanumBarunGothic.ttf";
		break;
		default: fontLoc = "/usr/share/fonts/truetype/ttf-dejavu/DejaVuSans.ttf";
		break;
		}
		return fontLoc;
	}
	/**
	 * returns the selected user colour
	 * @return Color
	 */
	private Color getUserColour() {
		return _colourSelect;
	}
	/**
	 * creates the intro/outro process according to which text fields are filled in
	 */
	private void createProcess() {
		_processNumber = 0;
		if(!_textFieldIntro.getText().isEmpty()){
			VideoIntroProcess process1 = new VideoIntroProcess(this, (int)_fontSize.getValue(), getUserFontLoc(), _textFieldIntro.getText(), _colourSelect, _background);
			process1.execute();
			_processNumber = 1;

		}

		if(!_textFieldEnd.getText().isEmpty()){
			VideoOutroProcess process2 = new VideoOutroProcess(this, (int)_fontSize.getValue(), getUserFontLoc(), _textFieldEnd.getText(), _colourSelect, _background);
			process2.execute();
			_processNumber = 2;
		}
	}
	/**
	 * error message if no media playback is selected in the media tab
	 */
	private void noMediaSelected() {
		JOptionPane.showMessageDialog(this, "Invalid media selected in the Media tab",
				"Media Error", JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * Obtains the save location from the user and calls the {@link}createProcess() method
	 */
	private void SaveLocAndTextProcess(){
		JFileChooser fileChooser = new JFileChooser() {
			@Override
			public void approveSelection() {

				File f = getSelectedFile();
				/*
				 * Makes sure the filename ends with extension .mp4 when checking for overwrite
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

		fileChooser.setDialogTitle("Specify where to save the modified video");

		int userSelection = fileChooser.showSaveDialog(this);
		File fileToSave = null;
		if (userSelection == JFileChooser.OPEN_DIALOG) {
			fileToSave = fileChooser.getSelectedFile();
			/**
			 * Makes sure the filename ends with extension .mp4
			 */

			if (!fileChooser.getSelectedFile().getAbsolutePath()
					.endsWith(".mp4")) {
				fileToSave = new File(fileChooser.getSelectedFile()
						+ ".mp4");
			}
			_saveLoc=fileToSave.getAbsolutePath();
			createProcess();
			progressReset();
			_apply.setEnabled(false);
			_progressBar.setIndeterminate(true);
			disableButtons();
		}

	}
	/**
	 * getter for the save location from the user
	 * @return
	 */
	public String getSaveloc(){
		return _saveLoc;
	}
	/**
	 * disables all the buttons when the text process is running
	 */
	public void disableButtons(){
		_textFieldEnd.setEnabled(false);
		_textFieldIntro.setEnabled(false);
		_apply.setEnabled(false);
		_btnColourSelect.setEnabled(false);
		_fontSize.setEnabled(false);
		_fontType.setEnabled(false);
	}
	/**
	 * enables all the buttons when the text process is finished
	 */
	public void enableButtons(){
		_textFieldEnd.setEnabled(true);
		_textFieldIntro.setEnabled(true);
		_apply.setEnabled(true);
		_btnColourSelect.setEnabled(true);
		_fontSize.setEnabled(true);
		_fontType.setEnabled(true);
		_progressBar.setIndeterminate(false);
	}
	/**
	 * Getters for the parent pane
	 * @return
	 */
	public MainGui getMain(){
		return _main;
	}

	public void progressDone(){
		_progressBar.setValue(100);
	}

	public void progressReset(){
		_progressBar.setValue(0);
		_progressBar.setIndeterminate(false);
	}

	public String[] userText(){
		String[] inputs ={_textFieldIntro.getText(), _textFieldEnd.getText()};
		return inputs;
	}

	public int getProcessNumber(){
		return _processNumber;
	}

	/**
	 * Saves the current text settings to a vamix save file
	 * @param saveFileName: Path to save file
	 */
	public void save(String saveFileName){
		saveLoad = new SaveLoadState(_textFieldIntro, _textFieldEnd, _txtPreview, _fontSize,
				_colourSelect, _fontType,_backgroundSelect, saveFileName);
		saveLoad.save();
	}

	/**
	 * Loads the settings from the file specified
	 * @param loadFileName: Path to Vamix save file to be loaded
	 */
	public void load(String loadFileName){
		saveLoad = new SaveLoadState(_textFieldIntro, _textFieldEnd, _txtPreview, _fontSize,
				_colourSelect, _fontType, _backgroundSelect,loadFileName);
		int colour = saveLoad.load("text");
		if (colour != 0){
			_colourSelect = new Color(colour);
		}
		SetPreview(_txtPreview, _textFieldIntro.getText(), getUserColour(), (int)_fontSize.getValue(), getUserFont());
		_btnColourSelect.setForeground(getUserColour());
	}

}
