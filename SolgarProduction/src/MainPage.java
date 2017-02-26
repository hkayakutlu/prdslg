package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;

import cb.esi.esiclient.util.ESIBag;
import main.ConnectToDb;
import util.Util;
 
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
	private static final String Version = "9-2-8";
	  private JFrame frame;
	  private JMenuBar menuBar,menuBarUser;
	  private JMenu fileMenu,editMenu,salesReportsMenu,marketingExpensesMenu,chainExpensesMenu,organizationMenu,dataBaseMenu;
	  private JMenuItem openMenuItem,excelLoadMenuItem,reportObservation,exitMenuItem,cutMenuItem,copyMenuItem,pasteMenuItem,
	  marketingExpenseEntry,marketingExpenseApprove,marketingExpenseObservation,marketingExpenseUpdate,marketingExpenseUpdateApproved,stockLoadMenuItem,
	  stockObservationMenuItem,passChangeScr,chainExpenseEntry,chainExpenseObservation,chainExpenseUpdate,chainExpenseCampaignEntry,
	  staffDefinition,staffAssesmentDefinition,staffAssesmentObservation,pharmDataDefinition,doctorDataDefinition;
	  private JLabel lblImage,lblYouAreWelcome,lblUserName,lblEmpId;
	  private JPanel pnlInfoMsg; 
 
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
    
    stockLoadMenuItem = new JMenuItem("Distrubutor Stock&Sale Load");
    stockLoadMenuItem.addActionListener(this);
    salesReportsMenu.add(stockLoadMenuItem);
    
    stockObservationMenuItem = new JMenuItem("Distrubutor Stock&Sale Observation");
    stockObservationMenuItem.addActionListener(this);
    salesReportsMenu.add(stockObservationMenuItem);
    
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
    
    marketingExpenseUpdateApproved = new JMenuItem("Marketing Expense Approved Operations Update");
    marketingExpenseUpdateApproved.addActionListener(this);
    marketingExpensesMenu.add(marketingExpenseUpdateApproved);
    
    marketingExpenseApprove = new JMenuItem("Marketing Expense Approve");
    marketingExpenseApprove.addActionListener(this);
    marketingExpensesMenu.add(marketingExpenseApprove);
    
    marketingExpenseObservation = new JMenuItem("Marketing Expense Observation");
    marketingExpenseObservation.addActionListener(this);
    marketingExpensesMenu.add(marketingExpenseObservation);
 
    chainExpensesMenu = new JMenu("Chain Expenses");
    fileMenu.add(chainExpensesMenu);  
    chainExpensesMenu.setVisible(false); 
    
    chainExpenseEntry = new JMenuItem("Chain Expense Entry");
    chainExpenseEntry.addActionListener(this);
    chainExpensesMenu.add(chainExpenseEntry);
    
    chainExpenseUpdate = new JMenuItem("Chain Expense Update");
    chainExpenseUpdate.addActionListener(this);
    chainExpensesMenu.add(chainExpenseUpdate);
    
    chainExpenseCampaignEntry = new JMenuItem("Chain Expense Campaign Entry");
    chainExpenseCampaignEntry.addActionListener(this);
    chainExpensesMenu.add(chainExpenseCampaignEntry);
    
    chainExpenseObservation = new JMenuItem("Chain Expense Observation");
    chainExpenseObservation.addActionListener(this);
    chainExpensesMenu.add(chainExpenseObservation);
    
    organizationMenu = new JMenu("Organization");
    fileMenu.add(organizationMenu);  
    organizationMenu.setVisible(false); 
    
    staffDefinition = new JMenuItem("Staff Definition&Update");
    staffDefinition.addActionListener(this);
    organizationMenu.add(staffDefinition);
    
    staffAssesmentDefinition = new JMenuItem("Staff Assesment Definition&Update");
    staffAssesmentDefinition.addActionListener(this);
    organizationMenu.add(staffAssesmentDefinition);
    
    staffAssesmentObservation = new JMenuItem("Staff Assesment Observation");
    staffAssesmentObservation.addActionListener(this);
    organizationMenu.add(staffAssesmentObservation);
    
    dataBaseMenu = new JMenu("Database");
    fileMenu.add(dataBaseMenu);  
    dataBaseMenu.setVisible(false);
    
    pharmDataDefinition = new JMenuItem("Apteka Entry&Update");
    pharmDataDefinition.addActionListener(this);
    dataBaseMenu.add(pharmDataDefinition);
    
    doctorDataDefinition = new JMenuItem("Doctor Entry&Update");
    doctorDataDefinition.addActionListener(this);
    dataBaseMenu.add(doctorDataDefinition);
    
    
    exitMenuItem = new JMenuItem("Exit");
    exitMenuItem.addActionListener(this);
    fileMenu.add(exitMenuItem);
 
    // build the Edit menu
    editMenu = new JMenu("Edit");
    cutMenuItem = new JMenuItem("Cut");
    copyMenuItem = new JMenuItem("Copy");
    pasteMenuItem = new JMenuItem("Paste");
    
    passChangeScr = new JMenuItem("Password Change");
    passChangeScr.addActionListener(this);
    passChangeScr.setVisible(false);
    
    editMenu.add(cutMenuItem);
    editMenu.add(copyMenuItem);
    editMenu.add(pasteMenuItem);
    editMenu.add(passChangeScr);
 
    // add menus to menubar
    menuBar.add(fileMenu);
    menuBar.add(editMenu);
    
    lblYouAreWelcome = new JLabel("You Are Welcome User: ");
    lblUserName = new JLabel(" Guest ");
    lblEmpId = new JLabel("-1");
    
    menuBar.add(Box.createHorizontalGlue());
    menuBar.add(lblYouAreWelcome).setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);    
    menuBar.add(lblUserName);
 
    // put the menubar on the frame
    frame.setJMenuBar(menuBar);
    
      excelLoadMenuItem.setVisible(false);
	  reportObservation.setVisible(false);
	  marketingExpenseEntry.setVisible(false);
	  marketingExpenseUpdate.setVisible(false);
	  marketingExpenseUpdateApproved.setVisible(false);
	  marketingExpenseObservation.setVisible(false);
	  marketingExpenseApprove.setVisible(false);
	  stockLoadMenuItem.setVisible(false);
	  stockObservationMenuItem.setVisible(false);
	  chainExpenseEntry.setVisible(false);
	  chainExpenseCampaignEntry.setVisible(false);
	  chainExpenseUpdate.setVisible(false);
	  chainExpenseObservation.setVisible(false);
	  staffDefinition.setVisible(false);
	  staffAssesmentDefinition.setVisible(false);
	  staffAssesmentObservation.setVisible(false);
	  pharmDataDefinition.setVisible(false);
	  doctorDataDefinition.setVisible(false);
   
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
	     try{
		  if(Util.controlVersion(Version)){
			  JOptionPane.showMessageDialog(pnlInfoMsg, "Program version is old please contact With Atilla", "Error", JOptionPane.ERROR_MESSAGE);
			  return;
		  }
	     }catch (Exception e) {
			// TODO: handle exception
		}
		 LoginScreen login = new LoginScreen();
		 login.setVisible(false);
		 String loginName = login.getTitle();
		 String loginId  = login.getName();
		 lblUserName.setText(loginName);
		 lblEmpId.setText(loginId);
		 salesReportsMenu.setVisible(true);
		 marketingExpensesMenu.setVisible(true);
		 chainExpensesMenu.setVisible(true);
		 organizationMenu.setVisible(true);
		 dataBaseMenu.setVisible(true);
		 setMenuUserRelation(inBag,loginName);
		 openMenuItem.setVisible(false);
		 passChangeScr.setVisible(true);
	 }else if(ev.getActionCommand().equals("Excel Load")){
		 try {
			ExcelUpload excelUpload = new ExcelUpload();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Report Observation")){
		 try {
			//ReportObservation reportObservation = new ReportObservation();
			 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
			 tempBag.put("LOGINNAME",lblUserName.getText());
			 
			 SalesRepObservation reportObservation = new SalesRepObservation(tempBag);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Marketing Expense Entry")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 try {
			ExpsEntryScreen marketingExpenseEntry = new ExpsEntryScreen(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Marketing Expense Update")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 try {
			ExpsUpdateScreen marketingExpenseUpdate = new ExpsUpdateScreen(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Marketing Expense Approved Operations Update")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 try {
			ExpsUpdateApprovedOpsScreen marketingExpenseUpdApprove = new ExpsUpdateApprovedOpsScreen(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Marketing Expense Approve")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 try {
			ExpsAppScreen marketingExpenseApprove = new ExpsAppScreen(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Marketing Expense Observation")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 try {
			ExpsObsScreen marketingExpenseObservation = new ExpsObsScreen(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Distrubutor Stock&Sale Load")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 try {
			StorageSalesStockUpload storageLoad = new StorageSalesStockUpload(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Distrubutor Stock&Sale Observation")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 try {
			StorageStockObservation storageObservation = new StorageStockObservation(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Password Change")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 PassChangeScr passChange = new PassChangeScr(lblUserName.getText());
		 passChange.setVisible(false);
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Chain Expense Entry")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 try {
			ChainExpsEntryScreen chainExpEntry = new ChainExpsEntryScreen(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Chain Expense Campaign Entry")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 try {
			ChainExpsCampaignEnrtyScreen chainExpCampEntry = new ChainExpsCampaignEnrtyScreen(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Chain Expense Update")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 try {
			ChainExpsUpdateScreen chainExpUpdate = new ChainExpsUpdateScreen(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Chain Expense Observation")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 try {
			ChainExpsObservationScreen marketingExpenseObservation = new ChainExpsObservationScreen(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Staff Definition&Update")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 try {
			EmployeeDefinitionScreen empDefinition = new EmployeeDefinitionScreen(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Staff Assesment Definition&Update")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 try {
			StaffAssesmentEntryUpdate staffAssEntry = new StaffAssesmentEntryUpdate(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Staff Assesment Observation")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 try {
			StaffAssesmentObservation staffAssObs = new StaffAssesmentObservation(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Apteka Entry&Update")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 tempBag.put("EMPLOYEEID",lblEmpId.getText());
		 try {
			PharmacyEntryUpdate pharmEntry = new PharmacyEntryUpdate(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("Doctor Entry&Update")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 tempBag.put("EMPLOYEEID",lblEmpId.getText());
		 try {
			DoctorEntryUpdate pharmEntry = new DoctorEntryUpdate(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }	  
	 else if(ev.getActionCommand().equals("Exit")){
		 System.exit(0);
	 }
  }
 
  private ESIBag setMenuUserRelation(ESIBag userBag,String loginName) {

	  try {	  
		  userBag = Util.getMenuUserRelation(loginName);
		  
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
			  if(userBag.get("EXPSUPDATEAPPROVED").equalsIgnoreCase("1")){
				  marketingExpenseUpdateApproved.setVisible(true);
			  }
			  if(userBag.get("EXPSOBSERVATION").equalsIgnoreCase("1")){
				  marketingExpenseObservation.setVisible(true);
			  }
			  if(userBag.get("EXPSAPPROVE").equalsIgnoreCase("1")){
				  marketingExpenseApprove.setVisible(true);
			  }
			  if(userBag.get("STORAGEENTRY").equalsIgnoreCase("1")){
				  stockLoadMenuItem.setVisible(true);
			  }
			  if(userBag.get("STORAGEOBSERVATION").equalsIgnoreCase("1")){
				  stockObservationMenuItem.setVisible(true);
			  }	
			  if(userBag.get("CHAINEXPSENTRY").equalsIgnoreCase("1")){
				  chainExpenseEntry.setVisible(true);
			  }
			  if(userBag.get("CHAINEXPSCAMPAIGNENTRY").equalsIgnoreCase("1")){
				  chainExpenseCampaignEntry.setVisible(true);
			  }
			  if(userBag.get("CHAINEXPSUPDATE").equalsIgnoreCase("1")){
				  chainExpenseUpdate.setVisible(true);
			  }
			  if(userBag.get("CHAINEXPSOBSERVATION").equalsIgnoreCase("1")){
				  chainExpenseObservation.setVisible(true);
			  }
			  if(userBag.get("STAFFDEFINITION").equalsIgnoreCase("1")){
				  staffDefinition.setVisible(true);
			  }
			  if(userBag.get("STAFFASSDEFINITION").equalsIgnoreCase("1")){
				  staffAssesmentDefinition.setVisible(true);
			  }
			  if(userBag.get("STAFFASSOBSERVATION").equalsIgnoreCase("1")){
				  staffAssesmentObservation.setVisible(true);
			  }
			  if(userBag.get("PHARMDATADEFINITION").equalsIgnoreCase("1")){
				  pharmDataDefinition.setVisible(true);
			  }
			  if(userBag.get("DOCTORDATADEFINITION").equalsIgnoreCase("1")){
				  doctorDataDefinition.setVisible(true);
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