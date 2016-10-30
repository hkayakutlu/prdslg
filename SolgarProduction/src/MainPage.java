package src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import cb.esi.esiclient.util.ESIBag;
import main.ConnectToDb;
 
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
	  private JMenu fileMenu,salesReportsMenu,marketingExpensesMenu;
	  private JMenu editMenu;
	  private JMenuItem openMenuItem,excelLoadMenuItem,reportObservation,exitMenuItem,cutMenuItem,copyMenuItem,pasteMenuItem,passChangeMenuItem,
	  marketingExpenseEntry,marketingExpenseApprove,marketingExpenseObservation,marketingExpenseUpdate;
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
	//lblImage =  new JLabel(new ImageIcon("images/SolgarPic.png"));
	lblImage =  new JLabel(new ImageIcon("C:\\SolgarPic.png"));
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

    salesReportsMenu = new JMenu("Sales Reports");
    fileMenu.add(salesReportsMenu);  
    salesReportsMenu.setVisible(false);    
    
    excelLoadMenuItem = new JMenuItem("Excel Load");
    excelLoadMenuItem.addActionListener(this);
    salesReportsMenu.add(excelLoadMenuItem);
    //excelLoadMenuItem.setVisible(false);
    
    reportObservation = new JMenuItem("Report Observation");
    reportObservation.addActionListener(this);
    salesReportsMenu.add(reportObservation);
    //reportObservation.setVisible(false);
    
    marketingExpensesMenu = new JMenu("Marketing Expenses");
    fileMenu.add(marketingExpensesMenu);  
    marketingExpensesMenu.setVisible(false); 
    
    marketingExpenseEntry = new JMenuItem("Marketing Expense Entry");
    marketingExpenseEntry.addActionListener(this);
    marketingExpensesMenu.add(marketingExpenseEntry);
    
    marketingExpenseUpdate = new JMenuItem("Marketing Expense Update");
    marketingExpenseUpdate.addActionListener(this);
    marketingExpensesMenu.add(marketingExpenseUpdate);
    
    marketingExpenseApprove = new JMenuItem("Marketing Expense Approve");
    marketingExpenseApprove.addActionListener(this);
    marketingExpensesMenu.add(marketingExpenseApprove);
    
    marketingExpenseObservation = new JMenuItem("Marketing Expense Observation");
    marketingExpenseObservation.addActionListener(this);
    marketingExpensesMenu.add(marketingExpenseObservation);
  
    
    exitMenuItem = new JMenuItem("Exit");
    exitMenuItem.addActionListener(this);
    fileMenu.add(exitMenuItem);
 
    // build the Edit menu
    editMenu = new JMenu("Edit");
    cutMenuItem = new JMenuItem("Cut");
    copyMenuItem = new JMenuItem("Copy");
    pasteMenuItem = new JMenuItem("Paste");
    passChangeMenuItem = new JMenuItem("Password Change");
    editMenu.add(passChangeMenuItem);
    editMenu.add(cutMenuItem);
    editMenu.add(copyMenuItem);
    editMenu.add(pasteMenuItem);
    
    passChangeMenuItem.addActionListener(this);
 
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
	 ESIBag inBag = new ESIBag();
	  
	  if(ev.getActionCommand().equals("LogIn")){	  
	    /*SampleDialog dialog = new SampleDialog();
	    dialog.setModal(true);
	    dialog.setVisible(true);*/
		 LoginScreen login = new LoginScreen();
		 login.setVisible(false);
		 String loginName = login.getTitle();
		 lblUserName.setText(loginName);
		 salesReportsMenu.setVisible(true);
		 marketingExpensesMenu.setVisible(true);
		 setMenuUserRelation(inBag,loginName);
		 openMenuItem.setVisible(false);
	 }else if(ev.getActionCommand().equals("Password Change")){
		 inBag.put("LOGINNAME",lblUserName.getText());
		 PassChangeScr excelUpload = new PassChangeScr(inBag);
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Excel Load")){
		 ExcelUpload excelUpload = new ExcelUpload();
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Report Observation")){
		 ReportObservation reportObservation = new ReportObservation();
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Marketing Expense Entry")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 ExpsEntryScreen marketingExpenseEntry = new ExpsEntryScreen(tempBag);
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Marketing Expense Update")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 ExpsUpdateScreen marketingExpenseUpdate = new ExpsUpdateScreen(tempBag);
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Marketing Expense Approve")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 ExpsAppScreen marketingExpenseApprove = new ExpsAppScreen(tempBag);
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Marketing Expense Observation")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 ExpsObsScreen marketingExpenseObservation = new ExpsObsScreen(tempBag);
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Exit")){
		 System.exit(0);
	 }
  }
 
  private ESIBag setMenuUserRelation(ESIBag userBag,String loginName) {

	  try {
		
		  excelLoadMenuItem.setVisible(false);
		  reportObservation.setVisible(false);
		  marketingExpenseEntry.setVisible(false);
		  marketingExpenseUpdate.setVisible(false);
		  marketingExpenseObservation.setVisible(false);
		  marketingExpenseApprove.setVisible(false);
		  
		  userBag = ConnectToDb.getMenuUserRelation(loginName);
		  
		  if (userBag.existsBagKey("BRAND")){
			  if(userBag.get("SALESENTRY").equalsIgnoreCase("1")){
				  excelLoadMenuItem.setVisible(true);
			  }
			  if(userBag.get("SALESOBSERVATION").equalsIgnoreCase("1")){
				  reportObservation.setVisible(true);
			  }
			  if(userBag.get("EXPSENTRY").equalsIgnoreCase("1")){
				  marketingExpenseEntry.setVisible(true);
			  }
			  if(userBag.get("EXPSUPDATE").equalsIgnoreCase("1")){
				  marketingExpenseUpdate.setVisible(true);
			  }
			  if(userBag.get("EXPSOBSERVATION").equalsIgnoreCase("1")){
				  marketingExpenseObservation.setVisible(true);
			  }
			  if(userBag.get("EXPSAPPROVE").equalsIgnoreCase("1")){
				  marketingExpenseApprove.setVisible(true);
			  }
		  }		
  
		  
	} catch (Exception e) {
		//throw e;
	}
	  
	  return userBag;
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