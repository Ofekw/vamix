package model;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class LogTableModel extends DefaultTableModel {
	/**
	 * A model for the table used in the log tab.
	 */
	boolean[] columnEditables = new boolean[] {
			false, false, false};
	
	public boolean isCellEditable(int row, int column) {
		return columnEditables[column];
	}
	
	public LogTableModel(){
		super(new Object[][] {

		},
		new String[] {
				"#", "Operation", "Date"
		}
				);
	}

	}
