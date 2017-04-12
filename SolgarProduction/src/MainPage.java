package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.*;

import org.apache.log4j.Logger;

import cb.esi.esiclient.util.BagKeyNotFoundException;
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
	private static String  Lang = "RU";
	final static Logger logger = Logger.getLogger(MainPage.class);
	
	  private JFrame frame;
	  private JMenuBar menuBar,menuBarUser;
	  private JMenu fileMenu,editMenu,salesReportsMenu,marketingExpensesMenu,chainExpensesMenu,organizationMenu,dataBaseMenu,executiveScreensMenu;
	  private JMenuItem openMenuItem,excelLoadMenuItem,reportObservation,exitMenuItem,
	  marketingExpenseEntry,marketingExpenseApprove,marketingExpenseObservation,marketingExpenseUpdate,marketingExpenseUpdateApproved,stockLoadMenuItem,
	  stockObservationMenuItem,passChangeScr,chainExpenseEntry,chainExpenseObservation,chainExpenseUpdate,chainExpenseCampaignEntry,
	  staffDefinition,staffAssesmentDefinition,staffAssesmentObservation,pharmDataDefinition,doctorDataDefinition,
	  engMenuItem,rusMenuItem,marketingExpsExecutiveScr,pharmacyExecutiveScr,doctorExecutiveScr,chainExpenseExecutiveScr,stockObservationMenuItemExc;
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
	lblImage =  new JLabel(new ImageIcon("C:\\SolgarInternalSysFile\\SolgarPic.png"));
    frame.setContentPane(lblImage);
    //add(lblImage,BorderLayout.CENTER);
    
    //Load Bundle
    ResourceBundle rb = ConnectToDb.readBundleFile(Lang);
    
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    
    // build the File menu
    //Yeni ekran menulerini buraya ekle
    fileMenu = new JMenu(rb.getString("MainMenu"));
    fileMenu.setName("File");
    
    openMenuItem = new JMenuItem(rb.getString("LogIn"));
    openMenuItem.setActionCommand("LogIn");
    openMenuItem.addActionListener(this);
    fileMenu.add(openMenuItem);

    salesReportsMenu = new JMenu(rb.getString("SalesReports"));
    salesReportsMenu.setActionCommand("Sales Reports");
    fileMenu.add(salesReportsMenu);  
    salesReportsMenu.setVisible(false);    
    
    excelLoadMenuItem = new JMenuItem(rb.getString("ExcelLoad"));
    excelLoadMenuItem.setActionCommand("Excel Load");
    excelLoadMenuItem.addActionListener(this);
    salesReportsMenu.add(excelLoadMenuItem);
    //excelLoadMenuItem.setVisible(false);
    
    stockLoadMenuItem = new JMenuItem(rb.getString("DistrubutorStock&SaleLoad"));
    stockLoadMenuItem.setActionCommand("Distrubutor Stock&Sale Load");
    stockLoadMenuItem.addActionListener(this);
    salesReportsMenu.add(stockLoadMenuItem);
    
    stockObservationMenuItem = new JMenuItem(rb.getString("DistrubutorStock&SaleObservation"));
    stockObservationMenuItem.setActionCommand("Distrubutor Stock&Sale Observation");
    stockObservationMenuItem.addActionListener(this);
    salesReportsMenu.add(stockObservationMenuItem);
    
    reportObservation = new JMenuItem(rb.getString("ReportObservation"));
    reportObservation.setActionCommand("Report Observation");
    reportObservation.addActionListener(this);
    salesReportsMenu.add(reportObservation);
    //reportObservation.setVisible(false);
    
    marketingExpensesMenu = new JMenu(rb.getString("MarketingExpenses"));
    marketingExpensesMenu.setActionCommand("MarketingExpenses");
    fileMenu.add(marketingExpensesMenu);  
    marketingExpensesMenu.setVisible(false); 
    
    marketingExpenseEntry = new JMenuItem(rb.getString("MarketingExpenseEntry"));
    marketingExpenseEntry.setActionCommand("Marketing Expense Entry");
    marketingExpenseEntry.addActionListener(this);
    marketingExpensesMenu.add(marketingExpenseEntry);
    
    marketingExpenseUpdate = new JMenuItem(rb.getString("MarketingExpenseUpdate"));
    marketingExpenseUpdate.setActionCommand("Marketing Expense Update");
    marketingExpenseUpdate.addActionListener(this);
    marketingExpensesMenu.add(marketingExpenseUpdate);
    
    marketingExpenseUpdateApproved = new JMenuItem(rb.getString("MarketingExpenseApprovedOperationsUpdate"));
    marketingExpenseUpdateApproved.setActionCommand("Marketing Expense Approved Operations Update");
    marketingExpenseUpdateApproved.addActionListener(this);
    marketingExpensesMenu.add(marketingExpenseUpdateApproved);
    
    marketingExpenseApprove = new JMenuItem(rb.getString("MarketingExpenseApprove"));
    marketingExpenseApprove.setActionCommand("Marketing Expense Approve");
    marketingExpenseApprove.addActionListener(this);
    marketingExpensesMenu.add(marketingExpenseApprove);
    
    marketingExpenseObservation = new JMenuItem(rb.getString("MarketingExpenseObservation"));
    marketingExpenseObservation.setActionCommand("Marketing Expense Observation");
    marketingExpenseObservation.addActionListener(this);
    marketingExpensesMenu.add(marketingExpenseObservation);
 
    chainExpensesMenu = new JMenu(rb.getString("ChainExpenses"));
    chainExpensesMenu.setActionCommand("Chain Expenses");
    fileMenu.add(chainExpensesMenu);  
    chainExpensesMenu.setVisible(false); 
    
    chainExpenseEntry = new JMenuItem(rb.getString("ChainExpenseEntry"));
    chainExpenseEntry.setActionCommand("Chain Expense Entry");
    chainExpenseEntry.addActionListener(this);
    chainExpensesMenu.add(chainExpenseEntry);
    
    chainExpenseUpdate = new JMenuItem(rb.getString("ChainExpenseUpdate"));
    chainExpenseUpdate.setActionCommand("Chain Expense Update");
    chainExpenseUpdate.addActionListener(this);
    chainExpensesMenu.add(chainExpenseUpdate);
    
    chainExpenseCampaignEntry = new JMenuItem(rb.getString("ChainExpenseCampaignEntry"));
    chainExpenseCampaignEntry.setActionCommand("Chain Expense Campaign Entry");
    chainExpenseCampaignEntry.addActionListener(this);
    chainExpensesMenu.add(chainExpenseCampaignEntry);
    
    chainExpenseObservation = new JMenuItem(rb.getString("ChainExpenseObservation"));
    chainExpenseObservation.setActionCommand("Chain Expense Observation");
    chainExpenseObservation.addActionListener(this);
    chainExpensesMenu.add(chainExpenseObservation);
    
    organizationMenu = new JMenu(rb.getString("Organization"));
    organizationMenu.setActionCommand("Organization");
    fileMenu.add(organizationMenu);  
    organizationMenu.setVisible(false); 
    
    staffDefinition = new JMenuItem(rb.getString("StaffDefinition&Update"));
    staffDefinition.setActionCommand("Staff Definition&Update");
    staffDefinition.addActionListener(this);
    organizationMenu.add(staffDefinition);
    
    staffAssesmentDefinition = new JMenuItem(rb.getString("StaffAssesmentDefinition&Update"));
    staffAssesmentDefinition.setActionCommand("Staff Assesment Definition&Update");
    staffAssesmentDefinition.addActionListener(this);
    organizationMenu.add(staffAssesmentDefinition);
    
    staffAssesmentObservation = new JMenuItem(rb.getString("StaffAssesmentObservation"));
    staffAssesmentObservation.setActionCommand("Staff Assesment Observation");
    staffAssesmentObservation.addActionListener(this);
    organizationMenu.add(staffAssesmentObservation);
    
    dataBaseMenu = new JMenu(rb.getString("Database"));
    dataBaseMenu.setActionCommand("Database");
    fileMenu.add(dataBaseMenu);  
    dataBaseMenu.setVisible(false);
    
    pharmDataDefinition = new JMenuItem(rb.getString("AptekaEntry&Update"));
    pharmDataDefinition.setActionCommand("Apteka Entry&Update");
    pharmDataDefinition.addActionListener(this);
    dataBaseMenu.add(pharmDataDefinition);
    
    doctorDataDefinition = new JMenuItem(rb.getString("DoctorEntry&Update"));
    doctorDataDefinition.setActionCommand("Doctor Entry&Update");
    doctorDataDefinition.addActionListener(this);
    dataBaseMenu.add(doctorDataDefinition);
    
    executiveScreensMenu = new JMenu(rb.getString("ExecutiveScreens"));
    executiveScreensMenu.setActionCommand("ExecutiveScreens");
    fileMenu.add(executiveScreensMenu);  
    executiveScreensMenu.setVisible(false);
    
    stockObservationMenuItemExc = new JMenuItem(rb.getString("DistrubutorStock&SaleObservation"));
    stockObservationMenuItemExc.setActionCommand("Distrubutor Stock&Sale Observation");
    stockObservationMenuItemExc.addActionListener(this);
    executiveScreensMenu.add(stockObservationMenuItemExc);
    
    marketingExpsExecutiveScr = new JMenuItem(rb.getString("MarketingExecutiveScreen"));
    marketingExpsExecutiveScr.setActionCommand("MarketingExpsExecutiveScr");
    marketingExpsExecutiveScr.addActionListener(this);
    executiveScreensMenu.add(marketingExpsExecutiveScr);  
    
    pharmacyExecutiveScr = new JMenuItem(rb.getString("PharmacyExecutiveScr"));
    pharmacyExecutiveScr.setActionCommand("PharmacyExecutiveScr");
    pharmacyExecutiveScr.addActionListener(this);
    executiveScreensMenu.add(pharmacyExecutiveScr);
    
    doctorExecutiveScr = new JMenuItem(rb.getString("DoctorExecutiveScr"));
    doctorExecutiveScr.setActionCommand("DoctorExecutiveScr");
    doctorExecutiveScr.addActionListener(this);
    executiveScreensMenu.add(doctorExecutiveScr);
    
    chainExpenseExecutiveScr = new JMenuItem(rb.getString("ChainExpenseExecutiveScr"));
    chainExpenseExecutiveScr.setActionCommand("ChainExpenseExecutiveScr");
    chainExpenseExecutiveScr.addActionListener(this);
    executiveScreensMenu.add(chainExpenseExecutiveScr);
    
    exitMenuItem = new JMenuItem(rb.getString("Exit"));
    exitMenuItem.setActionCommand("Exit");
    exitMenuItem.addActionListener(this);
    fileMenu.add(exitMenuItem);
 
    // build the Edit menu
    editMenu = new JMenu(rb.getString("Edit"));
    //cutMenuItem = new JMenuItem("Cut");
    //copyMenuItem = new JMenuItem("Copy");
    //pasteMenuItem = new JMenuItem("Paste");
    engMenuItem = new JMenuItem("English");
    rusMenuItem = new JMenuItem("Russian");
    
    engMenuItem.addActionListener(this);
    engMenuItem.setVisible(false);
    rusMenuItem.addActionListener(this);
    rusMenuItem.setVisible(false);
    
    passChangeScr = new JMenuItem(rb.getString("PasswordChange"));
    passChangeScr.setActionCommand("Password Change");
    passChangeScr.addActionListener(this);
    passChangeScr.setVisible(false);
    
   // editMenu.add(cutMenuItem);
   // editMenu.add(copyMenuItem);
   // editMenu.add(pasteMenuItem);
    editMenu.add(engMenuItem);
    editMenu.add(rusMenuItem);
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
	  marketingExpsExecutiveScr.setVisible(false);
	  stockObservationMenuItemExc.setVisible(false);
	  pharmacyExecutiveScr.setVisible(false);
	  doctorExecutiveScr.setVisible(false);
	  chainExpenseExecutiveScr.setVisible(false);
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
	     inBag.put("LANGUAGE", Lang);
		 LoginScreen login =null;
		try {
			login = new LoginScreen(inBag);
		} catch (BagKeyNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		 executiveScreensMenu.setVisible(true);
		 setMenuUserRelation(inBag,loginName);
		 openMenuItem.setVisible(false);
		 passChangeScr.setVisible(true);
		 engMenuItem.setVisible(true);
		 rusMenuItem.setVisible(true);
		 logger.info(loginName + ": Enter the system.");
	 }else if(ev.getActionCommand().matches("English|Russian")){		 
		 if(ev.getActionCommand().equalsIgnoreCase("English")){
			 Lang = "EN";
		 }else{Lang = "RU";}
		 ResourceBundle rb = ConnectToDb.readBundleFile(Lang);
		 fileMenu.setText(rb.getString("MainMenu"));
		 editMenu.setText(rb.getString("Edit"));
		 openMenuItem.setText(rb.getString("LogIn"));
		 salesReportsMenu.setText(rb.getString("SalesReports"));
		 excelLoadMenuItem.setText(rb.getString("ExcelLoad"));
		 stockLoadMenuItem.setText(rb.getString("DistrubutorStock&SaleLoad"));
		 stockObservationMenuItem.setText(rb.getString("DistrubutorStock&SaleObservation"));
		 reportObservation.setText(rb.getString("ReportObservation"));
		 marketingExpensesMenu.setText(rb.getString("MarketingExpenses"));
		 marketingExpenseEntry.setText(rb.getString("MarketingExpenseEntry"));
		 marketingExpenseUpdate.setText(rb.getString("MarketingExpenseUpdate"));
		 marketingExpenseUpdateApproved.setText(rb.getString("MarketingExpenseApprovedOperationsUpdate"));
		 marketingExpenseApprove.setText(rb.getString("MarketingExpenseApprove"));
		 marketingExpenseObservation.setText(rb.getString("MarketingExpenseObservation"));
		 chainExpensesMenu.setText(rb.getString("ChainExpenses"));
		 chainExpenseEntry.setText(rb.getString("ChainExpenseEntry"));
		 chainExpenseUpdate.setText(rb.getString("ChainExpenseUpdate"));
		 chainExpenseCampaignEntry.setText(rb.getString("ChainExpenseCampaignEntry"));
		 chainExpenseObservation.setText(rb.getString("ChainExpenseObservation"));
		 organizationMenu.setText(rb.getString("Organization"));
		 staffDefinition.setText(rb.getString("StaffDefinition&Update"));
		 staffAssesmentDefinition.setText(rb.getString("StaffAssesmentDefinition&Update"));
		 staffAssesmentObservation.setText(rb.getString("StaffAssesmentObservation"));
		 dataBaseMenu.setText(rb.getString("Database"));
		 pharmDataDefinition.setText(rb.getString("AptekaEntry&Update"));
		 doctorDataDefinition.setText("Doctor Entry&Update");
		 exitMenuItem.setText(rb.getString("Exit"));
		 passChangeScr.setText(rb.getString("PasswordChange"));
		 exitMenuItem.setText(rb.getString("Exit"));
		 executiveScreensMenu.setText(rb.getString("ExecutiveScreens"));
		 marketingExpsExecutiveScr.setText(rb.getString("MarketingExecutiveScreen"));
		 pharmacyExecutiveScr.setText(rb.getString("PharmacyExecutiveScr"));
		 doctorExecutiveScr.setText(rb.getString("DoctorExecutiveScr"));
		 chainExpenseExecutiveScr.setText(rb.getString("ChainExpenseExecutiveScr"));
		
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
		 tempBag.put("LANGUAGE",Lang);
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
		 tempBag.put("LANGUAGE",Lang);
		 try {
			DoctorEntryUpdate pharmEntry = new DoctorEntryUpdate(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("MarketingExpsExecutiveScr")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 tempBag.put("EMPLOYEEID",lblEmpId.getText());
		 try {
			ExpsManagerialScreen expManagerial = new ExpsManagerialScreen(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("PharmacyExecutiveScr")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 tempBag.put("EMPLOYEEID",lblEmpId.getText());
		 tempBag.put("LANGUAGE",Lang);
		 try {
			PharmacyManagerialScreen pharmManagerial = new PharmacyManagerialScreen(tempBag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("DoctorExecutiveScr")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 tempBag.put("EMPLOYEEID",lblEmpId.getText());
		 tempBag.put("LANGUAGE",Lang);
		 try {
			DoctorManagerialScreen doctorManagerial = new DoctorManagerialScreen(tempBag);
		
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 frame.validate();
	 }else if(ev.getActionCommand().equals("ChainExpenseExecutiveScr")){
		 ESIBag tempBag = setMenuUserRelation(inBag,lblUserName.getText());
		 tempBag.put("LOGINNAME",lblUserName.getText());
		 tempBag.put("EMPLOYEEID",lblEmpId.getText());
		 try {
			ChainExpsManagerialScreen chainExpsManagerial = new ChainExpsManagerialScreen(tempBag);
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
				  stockObservationMenuItemExc.setVisible(true);
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
			  if(userBag.get("EXECUTIVEMRKTEXPENSE").equalsIgnoreCase("1")){
				  marketingExpsExecutiveScr.setVisible(true);
			  }
			  if(userBag.get("EXECUTIVEPHARMACY").equalsIgnoreCase("1")){
				  pharmacyExecutiveScr.setVisible(true);
			  }
			  if(userBag.get("EXECUTIVEDOCTOR").equalsIgnoreCase("1")){
				  doctorExecutiveScr.setVisible(true);
			  }
			  if(userBag.get("EXECUTIVECHAINEXPENSE").equalsIgnoreCase("1")){
				  chainExpenseExecutiveScr.setVisible(true);
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

public static String giveLangCode(){
	return Lang;
}

}