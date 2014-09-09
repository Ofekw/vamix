//package gui;
//
//import java.awt.EventQueue;
//import java.awt.GridLayout;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//
//import javax.swing.ImageIcon;
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.JTabbedPane;
//import javax.swing.UIManager;
//import javax.swing.UIManager.LookAndFeelInfo;
//
//import controller.LogWriter;
//import controller.SingletonLogger;
//
//public class mainGUI {
//
//	private JFrame frame;
//	public LogWriter _logger;
//
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//
//		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//			if ("GTK+".equals(info.getName())) {
//				try {
//					UIManager.setLookAndFeel(info.getClassName());
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					break;
//				}
//			}
//		}
//		EventQueue.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					mainGUI window = new mainGUI();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//
//	/**
//	 * Create the application.
//	 */
//	public mainGUI() {
//		initialize();
//	}
//
//	/**
//	 * Initialize the contents of the frame.
//	 */
//	private void initialize() {
//		frame = new JFrame();
//		ImageIcon img = new ImageIcon("V.png");
//		frame.setIconImage(img.getImage());
//		frame.setBounds(100, 100, 550, 220);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
//
//		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
//		frame.getContentPane().add(tabbedPane);
//
//		JPanel panel = new JPanel();
//		tabbedPane.addTab("Main", null, panel, null);
//		panel.setLayout(new GridLayout(1, 0, 0, 0));
//
//		final JButton btnDownload = new JButton("Download");
//		btnDownload.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				if (btnDownload.isEnabled()) {
//					JPanel downloadP = new Download();
//					tabbedPane.addTab("Download", null, downloadP, null);
//					btnDownload.setEnabled(false);
//					for (int i = 0; i < tabbedPane.getTabCount(); i++) {
//						if (tabbedPane.getTitleAt(i) == "Download") {
//							tabbedPane.setSelectedIndex(i);
//						}
//					}
//				}
//			}
//		});
//
//		panel.add(btnDownload);
//
//		final JButton btnExtract = new JButton("Extract");
//		panel.add(btnExtract);
//		btnExtract.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				if (btnExtract.isEnabled()) {
//					Extract ePanel = new Extract();
//					tabbedPane.addTab("Extract", null, ePanel, null);
//					btnExtract.setEnabled(false);
//					for (int i = 0; i < tabbedPane.getTabCount(); i++) {
//						if (tabbedPane.getTitleAt(i) == "Extract") {
//							tabbedPane.setSelectedIndex(i);
//						}
//					}
//				}
//			}
//		});
//
//		final JButton btnLog = new JButton("Log");
//		panel.add(btnLog);
//		btnLog.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				if (btnLog.isEnabled()) {
//					Log lPanel = new Log();
//					tabbedPane.addTab("Log", null, lPanel, null);
//					btnLog.setEnabled(false);
//					for (int i = 0; i < tabbedPane.getTabCount(); i++) {
//						if (tabbedPane.getTitleAt(i) == "Log") {
//							tabbedPane.setSelectedIndex(i);
//						}
//					}
//				}
//			}
//		});
//
//		/**
//		 * Initializes the logging object
//		 */
//		SingletonLogger logger = new SingletonLogger();
//		logger.getInstance();
//
//	}
//
//}
