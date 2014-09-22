package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class TextTab extends Tab {
	private JTextField textFieldIntro;
	private JTextField textFieldEnd;
	private JTextPane tPane;
	private JTextPane txtPreview;
	private JSpinner fontSize;
	private JComboBox fontColour;
	private JComboBox fontType;

	public TextTab(VideoPanel panel) {
		
		super(panel);
		
		
	}

	@Override
	protected void initialise() {
this.setPreferredSize(new Dimension(1000, 130));
		
		Box verticalBox = Box.createVerticalBox();
		verticalBox.setPreferredSize(new Dimension(980,130));
		
		add(verticalBox);
		
		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);
		
		JLabel lblOpeningText = new JLabel("Opening Text:");
		horizontalBox.add(lblOpeningText);
		
		textFieldIntro = new JTextField();
		textFieldIntro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String text = textFieldIntro.getText();
                SetPreview(txtPreview, text, Color.BLACK, (int)fontSize.getValue());    
                txtPreview.selectAll();
            }

			private void SetPreview(JTextPane tp, String msg, Color c, int fontSize) {
				 StyleContext sc = StyleContext.getDefaultStyleContext();
			        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
			        aset=sc.addAttribute(aset, StyleConstants.FontSize, fontSize);

			        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
			        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

			        int len = tp.getDocument().getLength();
			        tp.setCaretPosition(len);
			        tp.setCharacterAttributes(aset, false);
			        tp.replaceSelection(msg);
				
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
		horizontalBox_1.add(textFieldEnd);
		textFieldEnd.setColumns(10);
		
		Component verticalStrut_1 = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut_1);
		
		Box horizontalBox_2 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_2);
		
		JLabel lblFontSize = new JLabel("Font Size:");
		horizontalBox_2.add(lblFontSize);
		
		fontSize = new JSpinner();
		fontSize.setMaximumSize(new Dimension(20, 30));
		fontSize.setModel(new SpinnerNumberModel(8, 8, 48, 1));
		horizontalBox_2.add(fontSize);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox_2.add(rigidArea);
		
		JLabel lblNewLabel = new JLabel("Font Colour: ");
		horizontalBox_2.add(lblNewLabel);
		
		fontColour = new JComboBox();
		fontColour.setToolTipText("Font Colour");
		fontColour.setMaximumSize(new Dimension(25, 32767));
		fontColour.setModel(new DefaultComboBoxModel(new String[] {"Black", "White"}));
		horizontalBox_2.add(fontColour);
		
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
		horizontalBox_2.add(fontType);
		 EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox_2.add(horizontalGlue);
		
		JButton apply = new JButton("Apply Changes");
		horizontalBox_2.add(apply);
		
		Box horizontalBox_3 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_3);
		
		txtPreview = new JTextPane();
		horizontalBox_3.add(txtPreview);
		txtPreview.setText("Text preview");
		
	}
}
