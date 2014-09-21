package gui;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.JLabel;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JRadioButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

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
		verticalBox.setPreferredSize(new Dimension(980,110));
		
		add(verticalBox);
		
		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);
		
		JLabel lblOpeningText = new JLabel("Opening Text:");
		horizontalBox.add(lblOpeningText);
		
		textField = new JTextField();
		horizontalBox.add(textField);
		textField.setColumns(10);
		
		Component verticalStrut = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_1);
		
		JLabel lblClosingText = new JLabel("Closing Text:   ");
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
		spinner.setMaximumSize(new Dimension(20, 30));
		spinner.setModel(new SpinnerNumberModel(8, 8, 48, 1));
		//spinner.setPreferredSize(new Dimension(2,1));
		horizontalBox_2.add(spinner);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox_2.add(rigidArea);
		
		JLabel lblNewLabel = new JLabel("Font Colour: ");
		horizontalBox_2.add(lblNewLabel);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setToolTipText("Font Colour");
		comboBox.setMaximumSize(new Dimension(25, 32767));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Black", "White"}));
		horizontalBox_2.add(comboBox);
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox_2.add(rigidArea_1);
		
		JLabel lblFontType = new JLabel("Font Type: ");
		horizontalBox_2.add(lblFontType);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setMaximumSize(new Dimension(40, 32767));
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"Font 1", "Font 2", "Font 3", "Font 4"}));
		comboBox_1.setToolTipText("Font Type");
		horizontalBox_2.add(comboBox_1);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox_2.add(horizontalGlue);
		
		JButton apply = new JButton("Apply Changes");
		horizontalBox_2.add(apply);
		
	}
}
