package src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
 
/**
 * Written/modified by Hakan Kayakutlu
 * 
 * This class was nspired by a thread in the Mac Java-dev mailing list
 * (<a href="http://lists.apple.com/archives/java-dev/" title="http://lists.apple.com/archives/java-dev/">http://lists.apple.com/archives/java-dev/</a>).
 */
public class MainPage extends JFrame implements Runnable,ActionListener{
	public MainPage() {
	}
	

	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 900;	

	  private JFrame frame;
	  private JMenuBar menuBar,menuBarUser;
	  private JMenu fileMenu;
	  private JMenu editMenu;
	  private JMenuItem openMenuItem;
	  private JMenuItem excelLoadMenuItem;
	  private JMenuItem reportObservation;
	  private JMenuItem exitMenuItem;
	  private JMenuItem cutMenuItem;
	  private JMenuItem copyMenuItem;
	  private JMenuItem pasteMenuItem;
	  private JLabel lblImage,lblYouAreWelcome,lblUserName;
	  
	  
 
  public static void main(String[] args)
  {
    // needed on mac os x
    System.setProperty("apple.laf.useScreenMenuBar", "true");
 
    // the proper way to show a jframe (invokeLater)
    SwingUtilities.invokeLater(new MainPage());
  }
 
  public void MainPage()
  {
    frame = new JFrame("Main Page");
    menuBar = new JMenuBar();
    menuBarUser = new JMenuBar();
    
    Toolkit toolkit;
	Dimension dim;
	int screenHeight, screenWidth;
    
    setBackground(Color.lightGray);
	getContentPane().setLayout(new BorderLayout(5, 5));
	// Set the frame's display to be WIDTH x HEIGHT in the middle of the screen
	toolkit = Toolkit.getDefaultToolkit();
	dim = toolkit.getScreenSize();
	screenHeight = dim.height;
	screenWidth = dim.width;
	
    
	
	frame.setBounds((screenWidth - FRAME_WIDTH) / 2, (screenHeight - FRAME_HEIGHT) / 2, FRAME_WIDTH, FRAME_HEIGHT);	
	frame.getContentPane().setLayout(new BorderLayout());

    //frame.setContentPane(new JLabel(new ImageIcon("C:\\SolgarPic.png")));
	lblImage =  new JLabel(new ImageIcon("images/SolgarPic.png"));
    frame.setContentPane(lblImage);
    //add(lblImage,BorderLayout.CENTER);
    
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    
    // build the File menu
    //Yeni ekran menulerini buraya ekle
    fileMenu = new JMenu("File");
    
    openMenuItem = new JMenuItem("LogIn");
    openMenuItem.addActionListener(this);
    fileMenu.add(openMenuItem);
    
    excelLoadMenuItem = new JMenuItem("Excel Load");
    excelLoadMenuItem.addActionListener(this);
    fileMenu.add(excelLoadMenuItem);
    excelLoadMenuItem.setVisible(false);
    
    reportObservation = new JMenuItem("Report Observation");
    reportObservation.addActionListener(this);
    fileMenu.add(reportObservation);
    reportObservation.setVisible(false);
    
    exitMenuItem = new JMenuItem("Exit");
    exitMenuItem.addActionListener(this);
    fileMenu.add(exitMenuItem);
 
    // build the Edit menu
    editMenu = new JMenu("Edit");
    cutMenuItem = new JMenuItem("Cut");
    copyMenuItem = new JMenuItem("Copy");
    pasteMenuItem = new JMenuItem("Paste");
    editMenu.add(cutMenuItem);
    editMenu.add(copyMenuItem);
    editMenu.add(pasteMenuItem);
 
    // add menus to menubar
    menuBar.add(fileMenu);
    menuBar.add(editMenu);
    
    lblYouAreWelcome = new JLabel("You Are Welcome User: ");
    lblUserName = new JLabel(" Guest ");
    
    menuBar.add(Box.createHorizontalGlue());
    menuBar.add(lblYouAreWelcome).setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);    
    menuBar.add(lblUserName);
 
    // put the menubar on the frame
    frame.setJMenuBar(menuBar);
   
  }
 
  /**
   * This handles the action for the File/Open event, and demonstrates
   * the implementation of an ActionListener.
   * In a real-world program you'd handle this differently.
   */
  @Override
public void actionPerformed(ActionEvent ev) 
  {
	 if(ev.getActionCommand().equals("LogIn")){	  
	    /*SampleDialog dialog = new SampleDialog();
	    dialog.setModal(true);
	    dialog.setVisible(true);*/
		 LoginScreen login = new LoginScreen();
		 login.setVisible(false);
		 excelLoadMenuItem.setVisible(true);
		 reportObservation.setVisible(true);
		 openMenuItem.setVisible(false);
	 }else if(ev.getActionCommand().equals("Excel Load")){
		 ExcelUpload excelUpload = new ExcelUpload();
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Report Observation")){
		 ReportObservation reportObservation = new ReportObservation();
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Exit")){
		 System.exit(0);
	 }
  }
 
  /**
   * This dialog is displayed when the user selects the File/Open menu item.
   * Burda icerdeki ekran
   */
  private class SampleDialog extends JDialog implements ActionListener
  {
    private JButton okButton = new JButton("OK");
 
    private SampleDialog()
    {
      super(frame, "Sample Dialog", true);
      JPanel panel = new JPanel(new FlowLayout());
      panel.add(okButton);
      getContentPane().add(panel);
      okButton.addActionListener(this);
      setPreferredSize(new Dimension(300, 200));
      pack();
      setLocationRelativeTo(frame);
    }
 
    @Override
	public void actionPerformed(ActionEvent ev)
    {
    	setVisible(false);
    }
  }

@Override
public void run() {
	MainPage();
}

}