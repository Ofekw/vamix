package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import controller.videoIntroProcess;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

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

	public TextTab(VideoPanel panel) {

		super(panel);
		this._tab=this;

	}

	@Override
	protected void initialise() {
		this.setPreferredSize(new Dimension(1000, 130));

		Box verticalBox = Box.createVerticalBox();
		verticalBox.setPreferredSize(new Dimension(980,150));

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

		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox_2.add(horizontalGlue);

		JButton apply = new JButton("Apply Changes");
		apply.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				createProcess();	
			}
		});
		horizontalBox_2.add(apply);

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
}
