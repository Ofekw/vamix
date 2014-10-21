package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileView;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;

import model.FileChooserModel;
import model.FileSRTChooserModel;
import model.IntegerField;
import net.miginfocom.swing.MigLayout;
import controller.AddSubtitleProcess;
import controller.RemoveSubtitleProcess;
import controller.SaveLoadState;
import controller.ShellProcess;
import controller.Subtitleworker;

import javax.swing.JProgressBar;

import sun.awt.image.OffScreenImage;

@SuppressWarnings("serial")
/**
 * Tab for adding and removing subtitles
 * @author Ofek Wittenberg
 *
 */
public class SubTitles extends Tab {
	private MainGui _main;
	private JButton _embed;
	private String _saveLoc;
	private SaveLoadState saveLoad;
	private JTextField _subtitleInput;
	private JButton _remove;
	private int _subTitleNo = 1; //number of subtitles
	private JButton _setEndTime;
	private StyledDocument _doc;
	private JButton _set;
	private JButton _setStartTime;
	private JTextField _starTime;
	private JTextField _endTime;
	private JTextPane _subtitleOutputFormat;
	private JButton _undo;
	private int alterations =0;
	private String[] alt = {"",""};


	public SubTitles(VideoPanel panel, MainGui main){
		super(panel);
		this.setPreferredSize(new Dimension(1046, 172));
		setLayout(new MigLayout("", "[][][82.00][][grow][][grow][][][][grow][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][]", "[][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][grow][][][][][][][][][][][][][][][][][][][][][][]"));


		_main = main;
		_subtitleOutputFormat = new JTextPane();
		_doc = _subtitleOutputFormat.getStyledDocument();
		_subtitleOutputFormat.setEditable(true);

		JScrollPane scrollBar = new JScrollPane(_subtitleOutputFormat);
		scrollBar.setViewportView(_subtitleOutputFormat);

		_subtitleOutputFormat.setToolTipText("Output .srt file preview");
		add(scrollBar, "flowx,cell 27 1 40 6,grow");

		JLabel lblSubtitleText = new JLabel("Subtitle Text");
		add(lblSubtitleText, "cell 1 2");

		_subtitleInput = new JTextField();
		add(_subtitleInput, "cell 2 2 23 1,growx");
		_subtitleInput.setColumns(10);

		_setStartTime = new JButton("Get Start Time");
		_setStartTime.setToolTipText("Set the start time of the subtitle from currently playing video");
		_setStartTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_starTime.setText(_videoPanel.getCurrentTime());
			}
		});
		add(_setStartTime, "cell 1 3");

		_starTime = new IntegerField();
		_starTime.setText("00:00:00");
		add(_starTime, "cell 2 3 14 1,growx");
		_starTime.setColumns(10);

		_setEndTime = new JButton("Get End Time");
		_setEndTime.setToolTipText("Set the end time of the subtitle from currently playing video");
		_setEndTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_endTime.setText(_videoPanel.getCurrentTime());
			}
		});

		_set = new JButton("Commit");
		_set.setEnabled(false);
		_set.setToolTipText("Add subtitle, you can add as many as you want.\nOnce you are done, press \"generate\".");
		_set.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!getText().isEmpty()){
					try{
						_doc.insertString(_doc.getLength(), Integer.toString(_subTitleNo)+"\n", null);
						_doc.insertString(_doc.getLength(), _starTime.getText()+",600 -->"+_endTime.getText() +",600 X1:0 !!!\n", null);
						_doc.insertString(_doc.getLength(), "<font color="+ "\"" + "#ffff00"+ "\"" + ">"+getText()+"</font>\n", null);
						//_doc.insertString(_doc.getLength(), "\n", null);
						_subTitleNo++; //add to subtitle count
						_subtitleInput.setText("");
						//saves previous input for the undo file
						alterations++;
						alt[alterations%2] = _subtitleOutputFormat.getText();
					}
					catch (Exception err){
						System.out.println(err);
					}
				}
			}
		});
		add(_set, "cell 18 3 1 3,grow");
		add(_setEndTime, "cell 1 5,growx");

		_endTime = new IntegerField();
		_endTime.setText("00:00:00");
		_endTime.setColumns(10);
		add(_endTime, "cell 2 5 14 1,growx");

		_undo = new JButton("Undo\n");
		_undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//gets the previous input to undo action
				_subTitleNo=subtitleCount();
				_subTitleNo++;
				_subtitleOutputFormat.setText(alt[(alterations-1)%2]);
				if (_subTitleNo>0){
			}
			}
		});
		add(_undo, "cell 18 7,growx");
		
				JButton _importSrt = new JButton("Import Subtitle File");
				_importSrt.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Subtitleworker writer = new Subtitleworker(SubTitles.this, 0);
						writer.execute();
					}
				});
				_importSrt.setToolTipText("Import .srt file");
				add(_importSrt, "cell 27 7");
		
				_remove = new JButton("Remove Subtitles");
				_remove.setEnabled(false);
				_remove.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String vidLoc = getMain().getVideo().getVideoLoc();
						String srtLoc = (vidLoc.substring(0,vidLoc.lastIndexOf(".")) + ".srt");
						ShellProcess.command("rm -f "+srtLoc);
						_subtitleOutputFormat.setText("");
						_subTitleNo=subtitleCount();
						_subTitleNo++;
						getMain().getPlayer().resetPlayer();
						getMain().getPlayer().play();
					}
				});
				add(_remove, "cell 44 7");
		_embed = new JButton("Apply Subtitles");
		_embed.setEnabled(false);
		_embed.setToolTipText("Create .srt file");
		add(_embed, "cell 65 7,growx");

		_embed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SaveLocAndTextProcess();
			}
		});
	}

	public String getText() {
		return _subtitleInput.getText();
	}

	private void disableButtons() {
		// TODO Auto-generated method stub

	}
	
	private int subtitleCount(){
		//copied and modified from http://stackoverflow.com/questions/13807575/how-to-get-the-number-of-lines-from-a-jtextpane
		int totalCharacters = _subtitleOutputFormat.getText().length(); 
		int lineCount = (totalCharacters == 0) ? 1 : 0;

		try {
		   int offset = totalCharacters; 
		   while (offset > 0) {
		      offset = Utilities.getRowStart(_subtitleOutputFormat, offset) - 1;
		      lineCount++;
		   }
		} catch (BadLocationException e) {
		    e.printStackTrace();
		}
		return lineCount/3;
	}
	//sets the subtitle number when importing srt
	public void setSubNum(int i){
		_subTitleNo = i;
	}

	public String getSRT(){
		return _subtitleOutputFormat.getText();
	}

	public void enableButtons() {
		_embed.setEnabled(true);
		_remove.setEnabled(true);
		_set.setEnabled(true);

	}


	private void createProcess() {
		Subtitleworker writer = new Subtitleworker(this, 1);
		writer.execute();
	}

	public String getSaveloc(){
		return _saveLoc;
	}

	public MainGui getMain(){
		return _main;
	}

	private void SaveLocAndTextProcess(){
		createProcess();
	}


	public void save(String saveFile){

	}

	/**
	 * Load video settings
	 * @param saveFile: Path to file to load
	 */
	public void load(String saveFile){

	}

	@Override
	protected void initialise() {
		// TODO Auto-generated method stub

	}

	public String getImportLoc() {
		JFileChooser chooser = new JFileChooser(new File(SaveLoadState.HOME.toString()));
		chooser.setFileFilter(new FileSRTChooserModel());
		chooser.setFileHidingEnabled(false);
		chooser.setFileView(new FileView() {
			@Override
			public Boolean isTraversable(File f) {
				return (f.isDirectory() && f.getName().equals(SaveLoadState.VAMIX.toString())); 
			}


		});
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = new File(chooser.getSelectedFile().toString());
			if (file.isFile()){
				return file.getAbsolutePath();
			}
		}else{
			JOptionPane.showMessageDialog(this, "File is not a valid save file!");
			return null;
		}
		return null;
	}


	public void setImport(String string) {
		_subtitleOutputFormat.setText(string);

	}

}