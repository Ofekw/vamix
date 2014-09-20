package gui;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.JLabel;
import java.awt.Component;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JRadioButton;
import javax.swing.SpinnerNumberModel;

public class TextTab extends Tab {
	private JTextField textField;
	private JTextField textField_1;

	public TextTab(VideoPanel panel) {
		
		super(panel);
		
		
	}

	@Override
	protected void initialise() {
this.setPreferredSize(new Dimension(1000, 130));
		
		Box verticalBox = Box.createVerticalBox();
		//verticalBox.setPreferredSize(new Dimension(980,80));
		
		add(verticalBox);
		
		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);
		
		JLabel lblOpeningText = new JLabel("Opening Text");
		horizontalBox.add(lblOpeningText);
		
		textField = new JTextField();
		horizontalBox.add(textField);
		textField.setColumns(10);
		
		Component verticalStrut = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_1);
		
		JLabel lblClosingText = new JLabel("Closing Text");
		horizontalBox_1.add(lblClosingText);
		
		textField_1 = new JTextField();
		horizontalBox_1.add(textField_1);
		textField_1.setColumns(10);
		
		Component verticalStrut_1 = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut_1);
		
		Box horizontalBox_2 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_2);
		
		JLabel lblFontSize = new JLabel("Font Size:");
		horizontalBox_2.add(lblFontSize);
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(8, 8, 48, 1));
		horizontalBox_2.add(spinner);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Font 1");
		horizontalBox_2.add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnFont = new JRadioButton("Font 2");
		horizontalBox_2.add(rdbtnFont);
		
		JRadioButton rdbtnFont_1 = new JRadioButton("Font 3");
		horizontalBox_2.add(rdbtnFont_1);
		
		JRadioButton rdbtnFont_2 = new JRadioButton("Font 4");
		horizontalBox_2.add(rdbtnFont_2);
		
	}

}
