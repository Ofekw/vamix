package gui;

import java.awt.EventQueue;
<<<<<<< HEAD

import javax.swing.ImageIcon;
=======
>>>>>>> d343aed92e9cc26461fa2acd5923994142fae8d9
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.FlowLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JPanel;

<<<<<<< HEAD
public class MainGui {
	
	private JFrame frame;
=======
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import com.sun.jna.Native;
>>>>>>> d343aed92e9cc26461fa2acd5923994142fae8d9

public class MainGui extends JFrame {
	
	public static void main(String[] args){
		
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if ("GTK+".equals(info.getName())) {
				try {
					UIManager.setLookAndFeel(info.getClassName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
			}
		}
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainGui window = new MainGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public MainGui(){
<<<<<<< HEAD
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		ImageIcon img = new ImageIcon("V.png");
		frame.setIconImage(img.getImage());
		frame.setBounds(100, 100, 1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		panel.add(verticalStrut);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		frame.getContentPane().add(horizontalStrut);
=======
		this.setSize(1000, 600);
		JPanel videoPanel = new VideoPanel();
		this.setVisible(true);
		
>>>>>>> d343aed92e9cc26461fa2acd5923994142fae8d9
	}
}
