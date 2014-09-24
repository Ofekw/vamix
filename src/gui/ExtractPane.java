package gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

/**
 * Extraction Gui
 * @author Patrick Poole
 *
 */
@SuppressWarnings("serial")
public class ExtractPane extends JPanel{

	private JButton _inputButton;
	private JLabel _inputLabel;
	private JTextField _inputLocation;

	private JLabel _outputLabel;
	private JTextField _outputName;

	private JPanel _timePane;
	private JLabel _startLabel;
	private JSpinner _startHours;
	private JSpinner _startMinutes;
	private JSpinner _startSeconds;

	private JLabel _durationLabel;
	private JSpinner _durationHours;
	private JSpinner _durationMinutes;
	private JSpinner _durationSeconds;

	private JButton _extractButton;
	
	private VamixProcess _basicProcess;

	private File _inputFile;

	public ExtractPane(){
		
		//Component decelerations etc
		this.setLayout(new MigLayout());
		_timePane = new JPanel(new MigLayout());

		_inputLabel = new JLabel("Select mp3 file:");
		_inputButton = new JButton("Select");
		_inputLocation = new JTextField();
		_inputLocation.setEditable(false);

		_outputLabel = new  JLabel("Output file name:");
		_outputName = new JTextField();

		_startLabel = new JLabel("Enter start time (HH:MM:SS)");
		_startHours = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
		_startMinutes = new JSpinner(new SpinnerNumberModel(0, 0, 60, 1));
		_startSeconds = new JSpinner(new SpinnerNumberModel(0, 0, 60, 1));

		_durationLabel = new JLabel("Enter duration (HH:MM:SS)");
		_durationHours = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
		_durationMinutes = new JSpinner(new SpinnerNumberModel(0, 0, 60, 1));
		_durationSeconds = new JSpinner(new SpinnerNumberModel(0, 0, 60, 1));

		_extractButton = new JButton("Extract");
		
		//action listener to open file chooser to get input file
		_inputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileFilter filter = new FileNameExtensionFilter("MP3 file", new String[] {"mp3"});
				JFileChooser inputFile = new JFileChooser();
				inputFile.setFileFilter(filter);
				inputFile.showOpenDialog(_inputButton);
				if (inputFile.getSelectedFile()!=null){
					_inputFile = inputFile.getSelectedFile();
					_inputLocation.setText(_inputFile.getName());
				}
			}
		});

		//extract button to start extraction process/check all requirements have been met
		_extractButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String duration = createTime(_durationHours.getValue().toString(), 
						_durationMinutes.getValue().toString(), _durationSeconds.getValue().toString());

				String start = createTime(_startHours.getValue().toString(), 
						_startMinutes.getValue().toString(), _startSeconds.getValue().toString());

				ExtractProcess process = new ExtractProcess(_inputFile, _outputName.getText(), 
						start, duration, _timePane);
				
				_basicProcess = new VamixProcess(_outputName.getText()+".mp3");
				
				if (_inputFile == null){
					JOptionPane.showMessageDialog(_extractButton, "No input file selected!");
				}else if (_outputName.getText().equals("")){
					JOptionPane.showMessageDialog(_extractButton, "Invalid output file name!");
				}else if (duration.equals("00:00:00")){
					JOptionPane.showMessageDialog(_extractButton, "Invalid duration!");
				}else if (!_basicProcess.checkFileType(_inputFile.getAbsolutePath())){
					JOptionPane.showMessageDialog(_extractButton, "Invalid file type!");
				}else if (_basicProcess.findFile()){
					int choice = -1;
					Object[] options = { "Overwrite", "Cancel"};
					choice = JOptionPane.showOptionDialog(_extractButton, "Output file already exists, would you like to overwrite it?"
							, "Warning",
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
							null, options, options[0]);
					if (choice == 0){
						_basicProcess.removeFile();
						process.execute();
					}
				}else{
					process.execute();
				}
			}
		});

		_timePane.add(_startLabel);
		_timePane.add(_startHours, "split 3, wmax 40");
		_timePane.add(_startMinutes, "wmax 40");
		_timePane.add(_startSeconds, "wmax 40, wrap 5");
		_timePane.add(_durationLabel);
		_timePane.add(_durationHours, "split 3, wmax 40");
		_timePane.add(_durationMinutes, "wmax 40");
		_timePane.add(_durationSeconds, "wmax 40");

		this.add(_inputLabel);
		this.add(_inputLocation, "wmin 170");
		this.add(_inputButton, "growx, wrap 5, gapright 5");
		this.add(_outputLabel);
		this.add(_outputName, "growx, wrap 5");
		this.add(_timePane, "span, wrap");
		this.add(_extractButton, "span,growx, gapright 5, wrap");
	}

	//helper method for creating time from input
	private String createTime(String hours, String minutes, String seconds){
		if (hours.length() == 1){
			hours="0"+hours;
		}
		if (minutes.length() == 1){
			minutes="0"+minutes;
		}
		if (seconds.length() == 1){
			seconds="0"+seconds;
		}
		return hours+":"+minutes+":"+seconds;
	}
}
