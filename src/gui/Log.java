package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import model.LogTableModel;
import controller.LogWriter;
import controller.ShellProcess;
import controller.SingletonLogger;

public class Log extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3856695100376881365L;
	JTextArea _taskLog;
	private String _loc;
	private LogTableModel _tableModel;
	private JTable _table;
	private JScrollPane _scrollPane;
	private JButton _refreshBtn;

	public Log() {

		_refreshBtn = new JButton("Refresh Log");
		_refreshBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/**
				 * Simply swaps the current table object with a new one when
				 * updated
				 */
				reset();
				setTable();
				setup(_refreshBtn);
				display();
			}
		});

		setTable();
		setup(_refreshBtn);
		display();
	}

	private void setup(JButton btnNewButton) {
		add(_table);
		_scrollPane = new JScrollPane(_table);
		this.add(_scrollPane, BorderLayout.CENTER);
		add(btnNewButton);
		_loc = ShellProcess.command("echo ~/.vamix");
		_loc = _loc.trim();
	}

	private void reset() {
		remove(_table);
		remove(_scrollPane);
	}

	private void setTable() {
		_tableModel = new LogTableModel();
		_table = new JTable();

		_table.setModel(_tableModel);
		_table.setPreferredScrollableViewportSize(new Dimension(400, 110));
	}

	private void display() {
		/**
		 * obtains environment variables, obtains the logger instance and gets
		 * it's data to display
		 */
		String loc = ShellProcess.command("echo ~/.vamix");
		loc = loc.trim();
		File propFile = new File(loc, "log");
		SingletonLogger logger = new SingletonLogger();
		LogWriter log = logger.getInstance();

		if (log.getLineCount() != 0) {
			getLogFiles(propFile);
		} else {
			JOptionPane.showMessageDialog(null,
					"No operations have been performed for this user so far");
		}
	}

	private void getLogFiles(File propFile) {
		String line;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(propFile));
			while ((line = reader.readLine()) != null) {
				_tableModel.addRow(line.split(","));
			}
			reader.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error");
			e.printStackTrace();

		}
	}
}
