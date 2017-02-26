package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.jgoodies.common.base.Strings;
import com.toedter.calendar.JDateChooser;

import cb.esi.esiclient.util.BagKeyNotFoundException;
import cb.esi.esiclient.util.ESIBag;
import main.ConnectToDb;
import main.Dispatcher;
import main.SendMail;
import util.Util;

public class ExpsAppScreen extends JFrame implements ActionListener,ItemListener,MouseListener,FocusListener{
	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 900;
	private static final Calendar cal = Calendar.getInstance();

	private JFrame frame;
	private JTable tblReportResult;	
	private JLabel lblAdrCountry,lblAdrRegion,lblAdrCity,lblAdrArea,lblStartDate,lblEndDate,
	lblExpFirstStage,lblExpSecondStage,lblExpThirdStage,
	lblExpMerLecture,lblExpMerOrganizator,lblExpMerTema,lblExpMerUcastnik,
    lblExpRekUsloviya,lblExpRekProduct,lblExpRekChain,
	lblExpPosProduct,lblExpPosParyadk,lblExpPosStatus,
	lblCount,lblAmount,lblComment,lblCompanyCode,
	lblAmount1,lblAmount2,lblAmount3,lblAmount4,lblAmount5,lblAmount6,
	lblClinicName,lblContracter,lblContracter1,lblKeyLeader,lblKeyLeader1,lblAmount7,
	lblSearchEventDate,lblSearchEntryDate,lblEmpty,lblEmpty1,lblStatus,lblEmpty2,lblEmpty3,lblEmpty4,
	lblObsTotCount,lblObsTotAmount,lblEmpty5,lblEmpty6,lblEmpty7,lblEmpty8,lblEmpty9,lblEmpty10;
	private JPanel paramPanelMain,paramPanelAddress,paramPanelDates,paramPanelResult,paramPanelExpTypes,paramPanelExpParMer,
	paramPanelExpParRek,paramPanelExpParPos,paramPanelBtn,paramPanelPaymentReceiver,paramPanelAmounts,
	pnlInfoMsg,paramPanelSearchDates,paramPanelObs;
	private JScrollPane jScroll;
	private JTable resultTable;
	private JComboBox cmbBoxCountry,cmbBoxRegion,cmbBoxCity,cmbBoxArea,
	cmbBoxExpMain,cmbBoxExpLevel1,cmbBoxExpLevel2,cmbBoxCompanyCode,cmbBoxExpMerLecture,cmbBoxExpMerOrganizator,
	cmbBoxExpRekChain,cmbBoxExpRekProduct,cmbBoxExpPosProduct,cmbBoxClinics,cmbBoxContracter,cmbBoxContracter1,cmbBoxKeyLeader,
	cmbBoxsearchEventDateSmall,cmbBoxsearchEventDateBig,cmbBoxsearchEntryDateSmall,cmbBoxsearchEntryDateBig,cmbBoxTema,cmbBoxStatus,cmbBoxKeyLeader1;
	private JDateChooser startDate,endDate;
	private JTextField txtExpMerUcastnik,txtExpRekUsloviya,txtExpPosParyadk,txtExpPosStatus,txtComment,
	txtEmpty,txtEmpty1,txtEmpty2,txtEmpty3,txtObsTotCount,txtObsTotAmount;
	public JButton btnAdd,btnDelete,btnUpdate,btnSearch,btnExit;
	public JTextArea textAreaComment;
	public JFormattedTextField txtCountFormat,txtAmountFormat,txtAmount1,txtAmount2,txtAmount3,txtAmount4,txtAmount5,txtAmount6,txtAmount7;
	DefaultTableModel dtm = new DefaultTableModel(0, 0);

	DefaultTableModel model = new DefaultTableModel(null, new String [] {"selecTable","ID","Approval_Status",
			"Company","Country","Area","Region","City",
			"First_Stage","Second_Stage",
			"Start_Date","End_Date","Total_Count","Total_Sum",
			"Conf_Food_Sum", "Org_Exps_Sum", "Travel_Agency_Sum","Key_Person_Sum", "Add_Exp_Sum", 
			"Chain","Contracter","Lecturer","Organizer","Clinic_Name","Key_Leader",
			"Product_Name","Tema","Attenders_Count","Conditions","Statuses","Comment",
			"Third Stage","Contracter1","Org_Exps_Sum1","Key_Leader1","Key_Person_Sum1",
			"entry_date","entry_user","approve_date","approve_user"}) {
        public Class getColumnClass(int c) {
          switch (c) {
            case 0: return Boolean.class;
            default: return String.class;
          }   
        } };

    	private String userName="Hakan KAYAKUTLU";
    	private String userBrand="ALL";
    	private String userCountry="ALL";
    	private String userArea="ALL";
    	private static final DecimalFormat doubleFormatter = new DecimalFormat("#,###");
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ESIBag inBag = new ESIBag();
					ExpsAppScreen window = new ExpsAppScreen(inBag);
					//window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	/*public ReportObservation() {		
		initialize();
	}*/

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 */
	public ExpsAppScreen(ESIBag inBag) throws SQLException {
		super("Expense Approve");
		Toolkit toolkit;
		Dimension dim;
		int screenHeight, screenWidth;
		// Initialize basic layout properties
		setBackground(Color.lightGray);
		getContentPane().setLayout(new BorderLayout(5, 5));
		// Set the frame's display to be WIDTH x HEIGHT in the middle of the screen
		toolkit = Toolkit.getDefaultToolkit();
		dim = toolkit.getScreenSize();
		screenHeight = dim.height;
		screenWidth = dim.width;
		setBounds((screenWidth - FRAME_WIDTH) / 2, (screenHeight - FRAME_HEIGHT) / 2, FRAME_WIDTH, FRAME_HEIGHT);
		
		//Set Parameters from Main Page
		try {		
			if(inBag.existsBagKey("LOGINNAME")){
				userName = inBag.get("LOGINNAME").toString();
				userBrand = inBag.get("BRAND").toString();
				userCountry = inBag.get("COUNTRY").toString();
				userArea = inBag.get("AREA").toString();
			}
		} catch (Exception e) {
			// simdilik yoksa yok
		}
		
		// add parameter panel
		paramPanelMain = new JPanel(new GridLayout(2, 3, 5, 5));
		
		paramPanelAddress = new JPanel(new GridLayout(7, 2, 5, 5));
		paramPanelDates = new JPanel(new GridLayout(7, 2, 5, 5));
		paramPanelExpTypes = new JPanel(new GridLayout(7, 4, 5, 5));		
		paramPanelExpParMer = new JPanel(new GridLayout(7, 2, 5, 5));	
		paramPanelPaymentReceiver = new JPanel(new GridLayout(8, 2, 5, 5));
		paramPanelAmounts = new JPanel(new GridLayout(7, 2, 5, 5));
		
		paramPanelBtn = new JPanel(new GridLayout(1, 5, 5, 5));
		paramPanelResult = new JPanel(new GridLayout(0, 1, 5, 5));
		
		paramPanelSearchDates = new JPanel(new GridLayout(7, 2, 5, 5));
		paramPanelObs = new JPanel(new GridLayout(5, 2, 5, 5));
		
		NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMAN);
	    NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
	    numberFormatter.setValueClass(Integer.class);
	    numberFormatter.setMinimum(0);
	    numberFormatter.setMaximum(Integer.MAX_VALUE);
	    numberFormatter.setAllowsInvalid(false);
	    // If you want the value to be committed on each keystroke instead of focus lost
	    numberFormatter.setCommitsOnValidEdit(true);
	    
	    DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
	    decimalFormat.setGroupingUsed(true);
	    decimalFormat.setMinimumIntegerDigits(0);
	    decimalFormat.setMaximumIntegerDigits(8);
	    
		//Table
	    resultTable = new JTable(model);
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		// scroll pane
		jScroll = new JScrollPane(resultTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
		jScroll.setViewportView(resultTable);
		jScroll.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(jScroll, BorderLayout.CENTER);
		
		
		//labels
		lblEmpty= new JLabel("");		
		lblEmpty1= new JLabel("");
		lblEmpty2= new JLabel("");		
		lblEmpty3= new JLabel("");
		lblEmpty4= new JLabel("");
		lblEmpty5= new JLabel("");		
		lblEmpty6= new JLabel("");
		lblEmpty7= new JLabel("");		
		lblEmpty8= new JLabel("");
		lblEmpty9= new JLabel("");
		lblEmpty10= new JLabel("");
		
		//labels
		lblAdrCountry = new JLabel("Country",JLabel.RIGHT);		
		lblAdrArea = new JLabel("Area",JLabel.RIGHT);
		lblAdrRegion = new JLabel("Region",JLabel.RIGHT);
		lblAdrCity = new JLabel("City",JLabel.RIGHT);
		
		lblStartDate = new JLabel("Start Date",JLabel.RIGHT);
		lblEndDate = new JLabel("End Date",JLabel.RIGHT);
		lblCount = new JLabel("Count",JLabel.RIGHT);
		lblAmount = new JLabel("Amount",JLabel.RIGHT);
		
		lblCompanyCode = new JLabel("Company Code",JLabel.RIGHT);
		lblExpFirstStage = new JLabel("First Stage",JLabel.RIGHT);
		lblExpSecondStage = new JLabel("Second Stage",JLabel.RIGHT);
		lblExpThirdStage = new JLabel("Third Stage",JLabel.RIGHT);
		lblClinicName = new JLabel("Clinic Name",JLabel.RIGHT);
		
		lblExpMerLecture = new JLabel("Lecture",JLabel.RIGHT);
		lblExpMerOrganizator = new JLabel("Organizator",JLabel.RIGHT);
		lblExpMerTema = new JLabel("Tema",JLabel.RIGHT);
		lblExpMerUcastnik = new JLabel("Attenders Count",JLabel.RIGHT);
		
		lblExpRekUsloviya = new JLabel("Conditions",JLabel.RIGHT);
		lblExpRekProduct = new JLabel("Product",JLabel.RIGHT);
		lblExpRekChain = new JLabel("Chain",JLabel.RIGHT);
		
		lblExpPosProduct = new JLabel("Product",JLabel.RIGHT);
		lblExpPosStatus = new JLabel("Status",JLabel.RIGHT);
		
		lblComment = new JLabel("Comment",JLabel.RIGHT);
		lblContracter = new JLabel("Contracter",JLabel.RIGHT);
		lblContracter1 = new JLabel("Contracter1",JLabel.RIGHT);
		lblKeyLeader = new JLabel("Key Leader",JLabel.RIGHT);
		lblKeyLeader1 = new JLabel("Key Leader1",JLabel.RIGHT);
		
		lblAmount1 = new JLabel("Конференц-зал и питание",JLabel.RIGHT);
		lblAmount2 = new JLabel("Орг.взнос",JLabel.RIGHT);
		lblAmount3 = new JLabel("Турагентство",JLabel.RIGHT);
		lblAmount4 = new JLabel("Гонорар",JLabel.RIGHT);
		lblAmount7 = new JLabel("Гонорар1",JLabel.RIGHT);
		lblAmount5 = new JLabel("Доп.расходы(бензин, мойка и т.д.)",JLabel.RIGHT);
		lblAmount6 = new JLabel("Орг.взнос 1",JLabel.RIGHT);
		
		lblSearchEventDate = new JLabel("Event Date Between",JLabel.RIGHT);
		lblSearchEntryDate = new JLabel("Entry Date Between",JLabel.RIGHT);
		
		lblStatus = new JLabel("Status",JLabel.RIGHT);
		
		lblObsTotCount = new JLabel("Total Count",JLabel.RIGHT);
		lblObsTotAmount = new JLabel("Total Sum",JLabel.RIGHT);
		
		
		cmbBoxStatus = new JComboBox( new String[]{});
		cmbBoxStatus.addItem("Waiting On Approval");
		cmbBoxStatus.setEditable(true);	
		cmbBoxStatus.setEnabled(false);
		cmbBoxStatus.setSelectedIndex(0);
		
		cmbBoxCompanyCode = new JComboBox( new String[]{});				
		cmbBoxCompanyCode.setEditable(true);
		cmbBoxCompanyCode.setEnabled(false);
		
		cmbBoxCountry = new JComboBox( new String[]{});		
		cmbBoxCountry.setEditable(true);
		cmbBoxCountry.setEnabled(false);
		
		cmbBoxArea = new JComboBox( new String[]{});		
		cmbBoxArea.setEditable(true);
		cmbBoxArea.setEnabled(false);
		
		cmbBoxRegion = new JComboBox( new String[]{});		
		cmbBoxRegion.setEditable(true);
		cmbBoxRegion.setEnabled(false);
		
		cmbBoxCity = new JComboBox( new String[]{});
		cmbBoxCity.setEditable(true);	
		cmbBoxRegion.setEnabled(false);
		
		if(userBrand.equalsIgnoreCase("ALL")){
			cmbBoxCompanyCode.addItem("SOLGAR");
			cmbBoxCompanyCode.addItem("NATURES BOUNTY");
			cmbBoxCompanyCode.setSelectedIndex(-1);
			cmbBoxCompanyCode.setEnabled(true);
		}else if(userBrand.equalsIgnoreCase("SOLGAR")){
			cmbBoxCompanyCode.addItem("SOLGAR");
			cmbBoxCompanyCode.setSelectedIndex(0);
		}else if(userBrand.equalsIgnoreCase("BOUNTY")){
			cmbBoxCompanyCode.addItem("NATURES BOUNTY");
			cmbBoxCompanyCode.setSelectedIndex(0);
		}
		
		if(userCountry.equalsIgnoreCase("ALL")){
			Util.getPRMDataGroupBy("country", "solgar_prm.prm_exps_addresses",cmbBoxCountry,"","");	
			cmbBoxCountry.setMaximumRowCount(50);
			cmbBoxCountry.setEditable(true);
			cmbBoxCountry.setSelectedIndex(-1);
			cmbBoxCountry.setEnabled(true);
			cmbBoxArea.setEnabled(true);
		}else if(userCountry.equalsIgnoreCase("Russia")){
			cmbBoxCountry.addItem("Russia");
			cmbBoxCountry.setSelectedIndex(0);
			if(userArea.equalsIgnoreCase("ALL")){
				Util.getPRMDataGroupBy("area", "solgar_prm.prm_exps_addresses",cmbBoxArea,"country",cmbBoxCountry.getSelectedItem().toString());
				cmbBoxArea.setSelectedIndex(0);
				cmbBoxArea.setEnabled(true);				
			}else if(userArea.equalsIgnoreCase("Moscow")){
				cmbBoxArea.addItem("Moscow");
				cmbBoxArea.setSelectedIndex(0);
			}else if(userArea.equalsIgnoreCase("Region")){
				cmbBoxArea.addItem("Region");
				cmbBoxArea.setSelectedIndex(0);
			}else if(userArea.equalsIgnoreCase("Saint Petersburg")){
				cmbBoxArea.addItem("Saint Petersburg");
				cmbBoxArea.setSelectedIndex(0);
			}
		}else if(userCountry.equalsIgnoreCase("Ukraine")){
			cmbBoxCountry.addItem("Ukraine");
			cmbBoxCountry.setSelectedIndex(0);
			if(userArea.equalsIgnoreCase("ALL")){
				Util.getPRMDataGroupBy("area", "solgar_prm.prm_exps_addresses",cmbBoxArea,"country",cmbBoxCountry.getSelectedItem().toString());
				cmbBoxArea.setSelectedIndex(0);
				cmbBoxArea.setEnabled(true);				
			}else if(userArea.equalsIgnoreCase("Kiev")){
				cmbBoxArea.addItem("Kiev");
				cmbBoxArea.setSelectedIndex(0);
			}
		}
		
		cmbBoxRegion.setEnabled(false);
		cmbBoxCity.setEnabled(false);
		
		cmbBoxExpMain = new JComboBox( new String[]{});		
		Util.getPRMDataGroupBy("main_name", "solgar_prm.prm_exps_types",cmbBoxExpMain,"","");	
		cmbBoxExpMain.setMaximumRowCount(50);
		cmbBoxExpMain.setEditable(true);
		cmbBoxExpMain.setSelectedIndex(-1);
		
		cmbBoxExpLevel1 = new JComboBox( new String[]{});	
		cmbBoxExpLevel1.setEditable(true);
		
		cmbBoxExpLevel2 = new JComboBox( new String[]{});	
		cmbBoxExpLevel2.setEditable(true);
		
		cmbBoxExpMerLecture = new JComboBox( new String[]{});		
		cmbBoxExpMerLecture.setEditable(true);
		cmbBoxExpMerLecture.setEnabled(false);
		
		cmbBoxExpMerOrganizator = new JComboBox( new String[]{});		
		cmbBoxExpMerOrganizator.setEditable(true);
		cmbBoxExpMerOrganizator.setEnabled(false);
		
		cmbBoxExpRekChain = new JComboBox( new String[]{});		
		Util.getPRMData("group_company", "solgar_prm.prm_russia_chains",cmbBoxExpRekChain);				
		cmbBoxExpRekChain.setMaximumRowCount(50);
		cmbBoxExpRekChain.setEditable(true);
		cmbBoxExpRekChain.setSelectedIndex(-1);
		cmbBoxExpRekChain.setEnabled(false);

		cmbBoxExpRekProduct = new JComboBox( new String[]{});	
		Util.getPRMDataTwoConditionsGroupBy("product", "solgar_prm.prm_exps_top_products",cmbBoxExpRekProduct,"company","SL", "","");
		cmbBoxExpRekProduct.setMaximumRowCount(50);
		cmbBoxExpRekProduct.setEditable(true);
		cmbBoxExpRekProduct.setSelectedIndex(-1);
		cmbBoxExpRekProduct.setEnabled(false);
		
		cmbBoxExpPosProduct = new JComboBox( new String[]{});	
		Util.getPRMDataTwoConditionsGroupBy("product", "solgar_prm.prm_exps_top_products",cmbBoxExpPosProduct,"company","SL", "","");
		cmbBoxExpPosProduct.setMaximumRowCount(50);
		cmbBoxExpPosProduct.setEditable(true);
		cmbBoxExpPosProduct.setSelectedIndex(-1);
		cmbBoxExpPosProduct.setEnabled(false);
		
		cmbBoxClinics = new JComboBox( new String[]{});		
		Util.getPRMData("clinic_name", "solgar_prm.prm_exps_clinics",cmbBoxClinics);				
		cmbBoxClinics.setMaximumRowCount(50);
		cmbBoxClinics.setEditable(true);
		cmbBoxClinics.setSelectedIndex(-1);
		cmbBoxClinics.setEnabled(false);
		
		cmbBoxKeyLeader = new JComboBox( new String[]{});		
		Util.getPRMData("leader_name", "solgar_prm.prm_exps_key_leader",cmbBoxKeyLeader);				
		cmbBoxKeyLeader.setMaximumRowCount(50);
		cmbBoxKeyLeader.setEditable(true);
		cmbBoxKeyLeader.setSelectedIndex(-1);
		cmbBoxKeyLeader.setEnabled(false);
		
		cmbBoxKeyLeader1 = new JComboBox( new String[]{});		
		Util.getPRMData("leader_name", "solgar_prm.prm_exps_key_leader",cmbBoxKeyLeader1);				
		cmbBoxKeyLeader1.setMaximumRowCount(50);
		cmbBoxKeyLeader1.setEditable(true);
		cmbBoxKeyLeader1.setSelectedIndex(-1);
		cmbBoxKeyLeader1.setEnabled(false);
		
		cmbBoxContracter = new JComboBox( new String[]{});		
		cmbBoxContracter.setEditable(true);	
		cmbBoxContracter.setEnabled(false);
		
		cmbBoxContracter1 = new JComboBox( new String[]{});		
		cmbBoxContracter1.setEditable(true);	
		cmbBoxContracter1.setEnabled(false);
		
		cmbBoxsearchEventDateSmall = new JComboBox( new String[]{});		
		Util.getPRMData("report_date", "solgar_prm.prm_report_dates",cmbBoxsearchEventDateSmall);				
		cmbBoxsearchEventDateSmall.setMaximumRowCount(50);
		cmbBoxsearchEventDateSmall.setEditable(true);
		cmbBoxsearchEventDateSmall.setSelectedIndex(-1);
		
		cmbBoxsearchEventDateBig = new JComboBox( new String[]{});		
		Util.getPRMData("report_date", "solgar_prm.prm_report_dates",cmbBoxsearchEventDateBig);				
		cmbBoxsearchEventDateBig.setMaximumRowCount(50);
		cmbBoxsearchEventDateBig.setEditable(true);
		cmbBoxsearchEventDateBig.setSelectedIndex(-1);
		
		cmbBoxsearchEntryDateSmall = new JComboBox( new String[]{});		
		Util.getPRMData("report_date", "solgar_prm.prm_report_dates",cmbBoxsearchEntryDateSmall);				
		cmbBoxsearchEntryDateSmall.setMaximumRowCount(50);
		cmbBoxsearchEntryDateSmall.setEditable(true);
		cmbBoxsearchEntryDateSmall.setSelectedIndex(-1);
		
		cmbBoxsearchEntryDateBig = new JComboBox( new String[]{});		
		Util.getPRMData("report_date", "solgar_prm.prm_report_dates",cmbBoxsearchEntryDateBig);				
		cmbBoxsearchEntryDateBig.setMaximumRowCount(50);
		cmbBoxsearchEntryDateBig.setEditable(true);
		cmbBoxsearchEntryDateBig.setSelectedIndex(-1);
		
		cmbBoxTema = new JComboBox( new String[]{});		
		Util.getPRMData("tema", "solgar_prm.prm_exps_temas",cmbBoxTema);				
		cmbBoxTema.setMaximumRowCount(50);
		cmbBoxTema.setEditable(true);
		cmbBoxTema.setSelectedIndex(-1);
		cmbBoxTema.setEnabled(false);
		
		//Date Field		
		Calendar cal = Calendar.getInstance();
		startDate = new JDateChooser();
		startDate.setDateFormatString("yyyy-MM-dd");		
		startDate.setDate(cal.getTime());
		startDate.setEnabled(false);
		
		endDate = new JDateChooser();
		endDate.setDateFormatString("yyyy-MM-dd");		
		endDate.setDate(cal.getTime());
		endDate.setEnabled(false);
		
		//Text Fields		
		txtEmpty = new JTextField();
		txtEmpty.setVisible(false);
		txtEmpty1 = new JTextField();
		txtEmpty1.setVisible(false);
		txtEmpty2 = new JTextField();
		txtEmpty2.setVisible(false);
		txtEmpty3 = new JTextField();
		txtEmpty3.setVisible(false);
		
		txtExpMerUcastnik = new JTextField();
		txtExpMerUcastnik.setEnabled(false);
		
		txtExpRekUsloviya = new JTextField();
		txtExpRekUsloviya.setEnabled(false);
		
		txtExpPosParyadk = new JTextField();
		txtExpPosParyadk.setEnabled(false);
		
		txtExpPosStatus = new JTextField();
		txtExpPosStatus.setEnabled(false);
		
		txtComment = new JTextField();
		txtComment.setEnabled(false);
		
		txtCountFormat = new JFormattedTextField(numberFormatter);
		txtCountFormat.setText("0");
		txtCountFormat.setEnabled(false);
		
		txtAmountFormat = new JFormattedTextField(decimalFormat);
		txtAmountFormat.setText("0");
		txtAmountFormat.setEnabled(false);
		
		txtAmount1 = new JFormattedTextField(decimalFormat);
		txtAmount2 = new JFormattedTextField(decimalFormat);
		txtAmount3 = new JFormattedTextField(decimalFormat);
		txtAmount4 = new JFormattedTextField(decimalFormat);
		txtAmount5 = new JFormattedTextField(decimalFormat);
		txtAmount6 = new JFormattedTextField(decimalFormat);
		txtAmount7 = new JFormattedTextField(decimalFormat);
		txtAmount1.setText("0");
		txtAmount2.setText("0");
		txtAmount3.setText("0");
		txtAmount4.setText("0");
		txtAmount5.setText("0");
		txtAmount6.setText("0");
		txtAmount7.setText("0");
		txtAmount1.setName("amount1");
		txtAmount2.setName("amount2");
		txtAmount3.setName("amount3");
		txtAmount4.setName("amount4");
		txtAmount5.setName("amount5");
		txtAmount6.setName("amount6");
		txtAmount7.setName("amount7");
		
		txtAmount1.setEnabled(false);
		txtAmount2.setEnabled(false);
		txtAmount3.setEnabled(false);
		txtAmount4.setEnabled(false);
		txtAmount5.setEnabled(false);
		txtAmount6.setEnabled(false);
		txtAmount7.setEnabled(false);
		
		
		txtObsTotAmount = new JTextField();
		txtObsTotAmount.setEnabled(false);
		
		txtObsTotCount = new JTextField();
		txtObsTotCount.setEnabled(false);
		
		//Buttons
		btnAdd = new JButton("Approve");
		btnAdd.setEnabled(false);
		
		btnDelete = new JButton("Reject");
		btnDelete.setEnabled(false);
		
		btnUpdate = new JButton("Update");
		btnUpdate.setEnabled(false);
		
		btnExit = new JButton("Exit");
		btnSearch = new JButton("Search");
		
		cmbBoxCountry.setName("Country");
		cmbBoxArea.setName("Area");
		cmbBoxRegion.setName("Region");
		cmbBoxCity.setName("City");
		cmbBoxExpMain.setName("ExpMain");
		cmbBoxExpLevel1.setName("Level1");
		cmbBoxExpLevel2.setName("Level2");
		cmbBoxCompanyCode.setName("CompanyCode");
		
		//Tetx Area
		textAreaComment = new JTextArea(2, 5);
		textAreaComment.setLineWrap(true);
		JScrollPane scrollPaneTxtArea = new JScrollPane(textAreaComment,
	            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
		textAreaComment.setEditable(true);

		//Listener
		resultTable.addMouseListener(this);
		
		cmbBoxCountry.addItemListener(this);
		cmbBoxArea.addItemListener(this);
		cmbBoxRegion.addItemListener(this);
		cmbBoxCity.addItemListener(this);		
		cmbBoxExpMain.addItemListener(this);
		cmbBoxExpLevel1.addItemListener(this);
		cmbBoxExpLevel2.addItemListener(this);
		cmbBoxCompanyCode.addItemListener(this);
		
		btnAdd.addActionListener(this);	
		btnDelete.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnSearch.addActionListener(this);
		btnExit.addActionListener(this);
		
		txtAmount1.addFocusListener(this);
		txtAmount2.addFocusListener(this);
		txtAmount3.addFocusListener(this);
		txtAmount4.addFocusListener(this);
		txtAmount5.addFocusListener(this);
		txtAmount6.addFocusListener(this);
		txtAmount7.addFocusListener(this);
		
		
		 ListSelectionModel cellSelectionModel = resultTable.getSelectionModel();
		 cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		 //cellSelectionModel.addListSelectionListener(this);
		
		/*pageLoad*/
		//amountShownless(false);
		
		//address parameters
		paramPanelAddress.add(lblCompanyCode);
		paramPanelAddress.add(cmbBoxCompanyCode);
		paramPanelAddress.add(lblAdrCountry);
		paramPanelAddress.add(cmbBoxCountry);
		paramPanelAddress.add(lblAdrArea);
		paramPanelAddress.add(cmbBoxArea);
		paramPanelAddress.add(lblAdrRegion);
		paramPanelAddress.add(cmbBoxRegion);
		paramPanelAddress.add(lblAdrCity);
		paramPanelAddress.add(cmbBoxCity);
		
		// Date paarameters
		paramPanelDates.add(lblStartDate);
		paramPanelDates.add(startDate);
		paramPanelDates.add(lblEndDate);
		paramPanelDates.add(endDate);		
		paramPanelDates.add(lblCount);
		paramPanelDates.add(txtCountFormat);
		paramPanelDates.add(lblAmount);
		paramPanelDates.add(txtAmountFormat);
		
		// Expense type parameters		
		paramPanelExpTypes.add(lblExpFirstStage);
		paramPanelExpTypes.add(cmbBoxExpMain);
		paramPanelExpTypes.add(lblExpSecondStage);
		paramPanelExpTypes.add(cmbBoxExpLevel1);	
		paramPanelExpTypes.add(lblExpThirdStage);
		paramPanelExpTypes.add(cmbBoxExpLevel2);
		paramPanelExpTypes.add(lblEmpty3);
		paramPanelExpTypes.add(txtEmpty3);
		paramPanelExpTypes.add(lblEmpty4);
		paramPanelExpTypes.add(txtEmpty2);
		
		paramPanelExpTypes.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanelExpTypes, BorderLayout.NORTH);
		
		//expenses parametric parameters
		paramPanelExpParMer.add(lblExpRekProduct);
		paramPanelExpParMer.add(cmbBoxExpRekProduct);
		paramPanelExpParMer.add(lblExpMerTema);
		paramPanelExpParMer.add(cmbBoxTema);
		paramPanelExpParMer.add(lblExpMerUcastnik);
		paramPanelExpParMer.add(txtExpMerUcastnik);
		paramPanelExpParMer.add(lblExpRekUsloviya );
		paramPanelExpParMer.add(txtExpRekUsloviya);
		paramPanelExpParMer.add(lblExpPosStatus);
		paramPanelExpParMer.add(txtExpPosStatus);
		paramPanelExpParMer.add(lblComment);
		paramPanelExpParMer.add(txtComment);
		
		
		//Payment Receiver
		paramPanelPaymentReceiver.add(lblExpRekChain);
		paramPanelPaymentReceiver.add(cmbBoxExpRekChain);		
		paramPanelPaymentReceiver.add(lblContracter);
		paramPanelPaymentReceiver.add(cmbBoxContracter);
		paramPanelPaymentReceiver.add(lblContracter1);
		paramPanelPaymentReceiver.add(cmbBoxContracter1);
		paramPanelPaymentReceiver.add(lblExpMerLecture);
		paramPanelPaymentReceiver.add(cmbBoxExpMerLecture);
		paramPanelPaymentReceiver.add(lblExpMerOrganizator);
		paramPanelPaymentReceiver.add(cmbBoxExpMerOrganizator);		
		paramPanelPaymentReceiver.add(lblClinicName);
		paramPanelPaymentReceiver.add(cmbBoxClinics);		
		paramPanelPaymentReceiver.add(lblKeyLeader);
		paramPanelPaymentReceiver.add(cmbBoxKeyLeader);		
		paramPanelPaymentReceiver.add(lblKeyLeader1);
		paramPanelPaymentReceiver.add(cmbBoxKeyLeader1);
		
		paramPanelAmounts.add(lblAmount1 );
		paramPanelAmounts.add(txtAmount1);
		paramPanelAmounts.add(lblAmount2);
		paramPanelAmounts.add(txtAmount2);
		paramPanelAmounts.add(lblAmount6);
		paramPanelAmounts.add(txtAmount6);
		paramPanelAmounts.add(lblAmount3);
		paramPanelAmounts.add(txtAmount3);
		paramPanelAmounts.add(lblAmount4);
		paramPanelAmounts.add(txtAmount4);
		paramPanelAmounts.add(lblAmount7);
		paramPanelAmounts.add(txtAmount7);
		paramPanelAmounts.add(lblAmount5);
		paramPanelAmounts.add(txtAmount5);
		
		paramPanelBtn.add(btnAdd);
		paramPanelBtn.add(btnUpdate);
		paramPanelBtn.add(btnDelete);
		paramPanelBtn.add(btnSearch);
		paramPanelBtn.add(btnExit);
		
		
		paramPanelSearchDates.add(lblSearchEventDate);
		paramPanelSearchDates.add(cmbBoxsearchEventDateSmall);
		paramPanelSearchDates.add(lblEmpty);
		paramPanelSearchDates.add(cmbBoxsearchEventDateBig);		
		paramPanelSearchDates.add(lblSearchEntryDate);
		paramPanelSearchDates.add(cmbBoxsearchEntryDateSmall);
		paramPanelSearchDates.add(lblEmpty1);
		paramPanelSearchDates.add(cmbBoxsearchEntryDateBig);
		paramPanelSearchDates.add(lblStatus);
		paramPanelSearchDates.add(cmbBoxStatus);
		paramPanelSearchDates.add(lblEmpty2);
		paramPanelSearchDates.add(txtEmpty2);
		
		paramPanelObs.add(lblObsTotCount);
		paramPanelObs.add(txtObsTotCount);
		paramPanelObs.add(lblObsTotAmount);
		paramPanelObs.add(txtObsTotAmount);
		paramPanelObs.add(lblEmpty5);
		paramPanelObs.add(lblEmpty6);
		paramPanelObs.add(lblEmpty7);
		paramPanelObs.add(lblEmpty8);
		paramPanelObs.add(lblEmpty9);
		paramPanelObs.add(lblEmpty10);
		
		paramPanelMain.add(paramPanelAddress);		
		paramPanelMain.add(paramPanelExpTypes);
		paramPanelMain.add(paramPanelDates);		
		paramPanelMain.add(paramPanelPaymentReceiver);
		paramPanelMain.add(paramPanelExpParMer);	
		paramPanelMain.add(paramPanelAmounts);
		paramPanelMain.add(paramPanelSearchDates);
		paramPanelMain.add(paramPanelObs);
		
		
		paramPanelMain.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanelMain, BorderLayout.NORTH);		
		
		paramPanelBtn.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanelBtn, BorderLayout.SOUTH);	
		
		paramPanelResult.add(jScroll);		
		
		createModel();
		
		//Last Changes
		paramPanelResult.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanelResult, BorderLayout.CENTER);
		
		// Put the final touches to the JFrame object
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		validate();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals("Approve")) {
				for (int j = 0; j < model.getRowCount(); j++){
					if(resultTable.getValueAt(j, 0).toString().equalsIgnoreCase("true")){					
						Dispatcher.statuChangeMarktExps(resultTable.getValueAt(j, 1).toString(), userName,"2");
					}
				}				
				String emailText = setEmailText();//"Dear reciepents,\n\n"+"Below id's expense approved by necessary user\n\n";						
				//SendMail.sendEmailToReceipents("kkoprova@solgarvitamin.ru","hgokmen@solgarvitamin.ru","mkamaeva@solgarvitamin.ru","herturk@solgarvitamin.ru", "Auto mail approve expense", emailText);
				SendMail.sendEmailToReceipents("hakan.kayakutlu@gmail.com","hgokmen@solgarvitamin.ru","mkamaeva@solgarvitamin.ru","", "Auto mail approve expense", emailText);
				for( int i = model.getRowCount() - 1; i >= 0; i-- ) {
					model.removeRow(i);
				}
				createModel();	
				cleanAllScreen();
				JOptionPane.showMessageDialog(pnlInfoMsg, "Expense approved", "Information", JOptionPane.INFORMATION_MESSAGE);
			}else if (e.getActionCommand().equals("Reject")) {
				//ConnectToDb.updateExpenseStatus(txtId.getText(),"3",user);		
				for (int j = 0; j < model.getRowCount(); j++){
					if(resultTable.getValueAt(j, 0).toString().equalsIgnoreCase("true")){					
						Dispatcher.statuChangeMarktExps(resultTable.getValueAt(j, 1).toString(), userName,"3");
					}
				}
				for( int i = model.getRowCount() - 1; i >= 0; i-- ) {
					model.removeRow(i);
				}
				createModel();		
				cleanAllScreen();
				String emailText = "Dear reciepents,\n\n"+"Below id's expense rejected by necessary user\n\n";						
				//SendMail.sendEmailToReceipents("hakan.kayakutlu@gmail.com","hgokmen@solgarvitamin.ru","mkamaeva@solgarvitamin.ru", "Auto mail reject expense", emailText);
				JOptionPane.showMessageDialog(pnlInfoMsg, "Expense rejected", "Information", JOptionPane.INFORMATION_MESSAGE);
				
			}else if (e.getActionCommand().equals("Delete")) {
				
			}else if (e.getActionCommand().equals("Search")) {				
				for( int i = model.getRowCount() - 1; i >= 0; i-- ) {
					model.removeRow(i);
				}
				createModel();
				
			}else if (e.getActionCommand().equals("Exit")) {
				setVisible(false);
			}
			
			
		} catch (Exception ex) {
			String message = ex.getMessage();
			ex.printStackTrace();
		}
		
	}  
	
	private void createModel() {
		ESIBag tempBag = Dispatcher.getMarktExpsWithParam(cmbBoxCountry,cmbBoxArea,cmbBoxCompanyCode,cmbBoxExpMain,cmbBoxExpLevel1,cmbBoxExpLevel2,
				cmbBoxsearchEventDateSmall,cmbBoxsearchEventDateBig,cmbBoxsearchEntryDateSmall,cmbBoxsearchEntryDateBig,cmbBoxStatus,null,null,null);
		    try{
			for (int j = 0; j < tempBag.getSize("TABLE"); j++){
				model.addRow(new Object [] 
		        		{
		        			false,
		        			tempBag.get("TABLE",j,"id"),
		        			tempBag.get("TABLE",j,"approval_status"),
				        	tempBag.get("TABLE",j,"Company"),
				        	tempBag.get("TABLE",j,"Country"),
				        	tempBag.get("TABLE",j,"Area"),
				        	tempBag.get("TABLE",j,"Region"),
				        	tempBag.get("TABLE",j,"City"),
				        	tempBag.get("TABLE",j,"First_Stage"),				        	
				        	tempBag.get("TABLE",j,"Second_Stage"),
				        	tempBag.get("TABLE",j,"Start_Date"),
				        	tempBag.get("TABLE",j,"End_Date"),
				        	tempBag.get("TABLE",j,"Total_Count"),
				        	tempBag.get("TABLE",j,"Total_Sum"),
				        	tempBag.get("TABLE",j,"Conf_Food_Sum"),
				        	tempBag.get("TABLE",j,"Org_Exps_Sum"),
				        	tempBag.get("TABLE",j,"Travel_Agency_Sum"),
				        	tempBag.get("TABLE",j,"Key_Person_Sum"),
				        	tempBag.get("TABLE",j,"Add_Exp_Sum"),
				        	tempBag.get("TABLE",j,"Chain"),
				        	tempBag.get("TABLE",j,"Contracter"),
				        	tempBag.get("TABLE",j,"Lecturer"),
				        	tempBag.get("TABLE",j,"Organizer"),
				        	tempBag.get("TABLE",j,"Clinic_Name"),
				        	tempBag.get("TABLE",j,"Key_Leader"),
				        	tempBag.get("TABLE",j,"Product_Name"),
				        	tempBag.get("TABLE",j,"Tema"),
				        	tempBag.get("TABLE",j,"Attenders_Count"),
				        	tempBag.get("TABLE",j,"Conditions"),
				        	tempBag.get("TABLE",j,"Statuses"),
				        	tempBag.get("TABLE",j,"Comments"),
				        	tempBag.get("TABLE",j,"Third_Stage"),
				        	tempBag.get("TABLE",j,"Contracter1"),
				        	tempBag.get("TABLE",j,"Org_Exps_Sum1"),
				        	tempBag.get("TABLE",j,"Key_Leader1"),
				        	tempBag.get("TABLE",j,"Key_Person_Sum1"),
				        	tempBag.get("TABLE",j,"entry_date"),
				        	tempBag.get("TABLE",j,"entry_user"),
				        	tempBag.get("TABLE",j,"approve_date"),
				        	tempBag.get("TABLE",j,"approve_user"),
				        	});		
			}
		    }catch (Exception e) {
				// simdilik yoksa yok
			}
	}

	public void itemStateChanged(ItemEvent itemEvent){
	  	  JComboBox cmbBox = (JComboBox)itemEvent.getSource();
	  	  String name = cmbBox.getName();
	  	  if(cmbBox.getSelectedItem() != null &&cmbBox.getSelectedItem().toString().length()>0){
		    	  if(name.equalsIgnoreCase("Country")){
		    		  cmbBoxArea.removeAllItems();
		    		  cmbBoxRegion.removeAllItems();
		    		  cmbBoxCity.removeAllItems();	    		  
		    		  try {
						Util.getPRMDataGroupBy("area", "solgar_prm.prm_exps_addresses",cmbBoxArea,"country",cmbBoxCountry.getSelectedItem().toString());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	    		  
		    		  cmbBoxRegion.setSelectedIndex(-1);
		    		  cmbBoxCity.setSelectedIndex(-1);	    		  	    		  
		    	  }else if(name.equalsIgnoreCase("Area")){
		    		  cmbBoxRegion.removeAllItems();
		    		  cmbBoxCity.removeAllItems();	    		  
		    		  try {
						Util.getPRMDataGroupBy("region", "solgar_prm.prm_exps_addresses",cmbBoxRegion,"area",cmbBoxArea.getSelectedItem().toString());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		  cmbBoxExpMerLecture.removeAllItems();
		    		  cmbBoxExpMerOrganizator.removeAllItems();
		    		  if(cmbBoxCompanyCode.getSelectedItem() != null && cmbBoxArea.getSelectedItem() != null){
		    			  try {
							Util.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerLecture,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
							  "country",cmbBoxArea.getSelectedItem().toString());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    			  try {
							Util.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerOrganizator,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
									  "country",cmbBoxArea.getSelectedItem().toString());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    			  cmbBoxExpMerLecture.setSelectedIndex(-1);
		    			  cmbBoxExpMerOrganizator.setSelectedIndex(-1);
		    		  }
		    		  cmbBoxRegion.setSelectedIndex(-1);
		    		  cmbBoxCity.setSelectedIndex(-1);	    		  
		    	  }else if(name.equalsIgnoreCase("Region")){
		    		  cmbBoxCity.removeAllItems();
		    		  try {
						Util.getPRMDataGroupBy("city", "solgar_prm.prm_exps_addresses",cmbBoxCity,"region",cmbBoxRegion.getSelectedItem().toString());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		  cmbBoxCity.setSelectedIndex(-1);
		    		  if(cmbBoxCompanyCode.getSelectedItem() != null && cmbBoxArea.getSelectedItem() != null){
		    			  cmbBoxExpMerLecture.removeAllItems();
			    		  cmbBoxExpMerOrganizator.removeAllItems();
			    		  try {
							Util.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerLecture,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
							  "country",cmbBoxArea.getSelectedItem().toString());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		  try {
							Util.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerOrganizator,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
									  "country",cmbBoxArea.getSelectedItem().toString());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    			  cmbBoxExpMerLecture.setSelectedIndex(-1);
		    			  cmbBoxExpMerOrganizator.setSelectedIndex(-1);
		    		  }
		    	  }else if(name.equalsIgnoreCase("CityRegion")){
		    		 //enson
		    	  }else if(name.equalsIgnoreCase("ExpMain")){
		    		  if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("Мероприятия")){
		    			  amountShownless(true);
		    		  }else if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("Реклама")){
		    			  amountShownless(false);
		    		  }else if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("POS-материалы")){
		    			  amountShownless(false);
		    		  }
		    		  cmbBoxExpLevel1.removeAllItems();
		    		  cmbBoxExpLevel2.removeAllItems();
		    		  try {
						Util.getPRMDataGroupBy("level1", "solgar_prm.prm_exps_types",cmbBoxExpLevel1,"main_name",cmbBoxExpMain.getSelectedItem().toString());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		  cmbBoxExpLevel1.setSelectedIndex(-1);
		    		  cmbBoxExpLevel2.setSelectedIndex(-1);
		    	  }else if(name.equalsIgnoreCase("Level1")){
		    		  cmbBoxExpLevel2.removeAllItems();
		    		  try {
						Util.getPRMDataGroupBy("level2", "solgar_prm.prm_exps_types",cmbBoxExpLevel2,"level1",cmbBoxExpLevel1.getSelectedItem().toString());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		  cmbBoxExpLevel2.setSelectedIndex(-1);
		    	  }else if(name.equalsIgnoreCase("Level2")){
		    		  //enson
		    	  }else if(name.equalsIgnoreCase("CompanyCode")){    		  
		    		  if(cmbBoxCompanyCode.getSelectedItem() != null){
		    			  cmbBoxExpPosProduct.removeAllItems();	
		    			  cmbBoxExpRekProduct.removeAllItems();	
			    		  if(cmbBoxCompanyCode.getSelectedItem().toString().equalsIgnoreCase("SOLGAR")){
			    			  try {
								Util.getPRMDataTwoConditionsGroupBy("product", "solgar_prm.prm_exps_top_products",cmbBoxExpRekProduct,"company","SL", "","");
								Util.getPRMDataTwoConditionsGroupBy("product", "solgar_prm.prm_exps_top_products",cmbBoxExpPosProduct,"company","SL", "","");
			    			  } catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}			    			  
		    				  cmbBoxExpPosProduct.setSelectedIndex(-1);
		    				  cmbBoxExpRekProduct.setSelectedIndex(-1);
		    			  }else{
		    				  try {
								Util.getPRMDataTwoConditionsGroupBy("product", "solgar_prm.prm_exps_top_products",cmbBoxExpRekProduct,"company","BN", "","");
								Util.getPRMDataTwoConditionsGroupBy("product", "solgar_prm.prm_exps_top_products",cmbBoxExpPosProduct,"company","BN", "","");
		    				  } catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				  
		    				  cmbBoxExpRekProduct.setSelectedIndex(-1);
		    				  cmbBoxExpPosProduct.setSelectedIndex(-1);
		    			  }
		    		  }
		    		  
		    		  if(cmbBoxArea.getSelectedItem() != null && cmbBoxCompanyCode.getSelectedItem() != null){
			    		  cmbBoxExpMerLecture.removeAllItems();
			    		  cmbBoxExpMerOrganizator.removeAllItems();	
			    		  try {
							Util.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerLecture,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
								  "country",cmbBoxArea.getSelectedItem().toString());
							  Util.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerOrganizator,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
			    					  "country",cmbBoxArea.getSelectedItem().toString());	 
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		   			  
		    		  }	    		  	    		  
		    	  }
	  	  }
	         
	    }   
      

	 public void mouseClicked(MouseEvent e) {
		  SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dcn1 = new SimpleDateFormat("yyyy-MM-dd");
		  	int i = resultTable.getSelectedRow();
		  	
		  	if(resultTable.getValueAt(i, 3) != null){
		  		cmbBoxCompanyCode.setSelectedItem(resultTable.getValueAt(i, 3).toString());
		  	}else{
		  		cmbBoxCompanyCode.setSelectedItem("");
		  	}
		  	if(resultTable.getValueAt(i, 4) != null){
		  		cmbBoxCountry.setSelectedItem(resultTable.getValueAt(i, 4).toString());
		  	}else{
		  		cmbBoxCountry.setSelectedItem("");
		  	}
		  	if(resultTable.getValueAt(i, 5) != null){
		  		cmbBoxArea.setSelectedItem(resultTable.getValueAt(i, 5).toString());
		  	}else{
		  		cmbBoxArea.setSelectedItem("");
		  	}
		  	if(resultTable.getValueAt(i, 6) != null){
		  		cmbBoxRegion.setSelectedItem(resultTable.getValueAt(i,6).toString());
		  	}else{
		  		cmbBoxRegion.setSelectedItem("");
		  	}
		  	if(resultTable.getValueAt(i, 7) != null){
		  		cmbBoxCity.setSelectedItem(resultTable.getValueAt(i, 7).toString());
		  	}else{
		  		cmbBoxCity.setSelectedItem("");
		  	}
		  	if(resultTable.getValueAt(i, 8) != null){
		  		cmbBoxExpMain.setSelectedItem(resultTable.getValueAt(i, 8).toString());
		  	}else{
		  		cmbBoxExpMain.setSelectedItem("");
		  	}
		  	if(resultTable.getValueAt(i, 9) != null){
		  		cmbBoxExpLevel1.setSelectedItem(resultTable.getValueAt(i, 9).toString());
		  	}else{
		  		cmbBoxExpLevel1.setSelectedItem("");
		  	}
		  	if(resultTable.getValueAt(i, 10) != null){			
		  		try {
		  			startDate.setDate(dcn.parse(resultTable.getValueAt(i, 10).toString()));
		  		} catch (Exception e2) {
		  			// TODO: handle exception
		  		}
		  	}else{
		  		startDate.setDateFormatString("");
		  	}
		  	if(resultTable.getValueAt(i, 11) != null){	
		  		try{
		  			endDate.setDate(dcn1.parse(resultTable.getValueAt(i, 11).toString()));
		  		} catch (Exception e2) {
		  			// TODO: handle exception
		  		}
		  	}else{
		  		endDate.setDateFormatString("");
		  	}
		  	if(resultTable.getValueAt(i, 12) != null){
		  		txtCountFormat.setText(resultTable.getValueAt(i, 12).toString());
		  	}else{
		  		txtCountFormat.setText("");
		  	}
		  	if(resultTable.getValueAt(i, 13) != null){
		  	 txtAmountFormat.setText(resultTable.getValueAt(i, 13).toString());
		  	}else{
		  	  txtAmountFormat.setText("");
		  	}
		  	if(resultTable.getValueAt(i, 14) != null){
		  	 txtAmount1.setText(resultTable.getValueAt(i, 14).toString());
		  	}else{
		  	  txtAmount1.setText("");
		  	}
		  	if(resultTable.getValueAt(i, 15) != null){
		  	 txtAmount2.setText(resultTable.getValueAt(i, 15).toString());
		  	}else{
		  	  txtAmount2.setText("");
		  	}
		  	if(resultTable.getValueAt(i, 16) != null){
		  	 txtAmount3.setText(resultTable.getValueAt(i, 16).toString());
		  	}else{
		  	  txtAmount3.setText("");
		  	}
		  	if(resultTable.getValueAt(i, 17) != null){
		  	 txtAmount4.setText(resultTable.getValueAt(i, 17).toString());
		  	}else{
		  	 txtAmount4.setText("");
		  	}
		  	if(resultTable.getValueAt(i, 18) != null){
		  	 txtAmount5.setText(resultTable.getValueAt(i, 18).toString());
		  	}else{
		  	  txtAmount5.setText("");
		  	}
		  	if(resultTable.getValueAt(i, 19) != null){
		  	  cmbBoxExpRekChain.setSelectedItem(resultTable.getValueAt(i, 19).toString());
		  	}else{
		  	  cmbBoxExpRekChain.setSelectedItem("");
		  	}
		  	if(resultTable.getValueAt(i, 20) != null){
		  	  cmbBoxContracter.setSelectedItem(resultTable.getValueAt(i, 20).toString());
		  	}else{
		  	  cmbBoxContracter.setSelectedItem("");
		  	}
		  	if(resultTable.getValueAt(i, 21) != null){
		  		cmbBoxExpMerLecture.setSelectedItem(resultTable.getValueAt(i, 21).toString());
		  	}else{
		  		cmbBoxExpMerLecture.setSelectedItem("");
		  	}
		  	if(resultTable.getValueAt(i, 22) != null){
		  		cmbBoxExpMerOrganizator.setSelectedItem(resultTable.getValueAt(i, 22).toString());
		  	}else{
		  		cmbBoxExpMerOrganizator.setSelectedItem("");
		  	}
		  	if(resultTable.getValueAt(i, 23) != null){
		  		cmbBoxClinics.setSelectedItem(resultTable.getValueAt(i, 23).toString());
		  	}else{
		  		cmbBoxClinics.setSelectedItem("");
		  	}
		  	if(resultTable.getValueAt(i, 24) != null){
		  		cmbBoxKeyLeader.setSelectedItem(resultTable.getValueAt(i, 24).toString());
		  	}else{
		  		cmbBoxKeyLeader.setSelectedItem("");
		  	}
		  	 if(resultTable.getValueAt(i, 25) != null){
		  	  cmbBoxExpRekProduct.setSelectedItem(resultTable.getValueAt(i, 25).toString());
		  	}else{
		  	  cmbBoxExpRekProduct.setSelectedItem("");
		  	}
		  	if(resultTable.getValueAt(i, 26) != null){
		  		cmbBoxTema.setSelectedItem(resultTable.getValueAt(i, 26).toString());
		  	}else{
		  		cmbBoxTema.setSelectedItem("");
		  	}
		  	if(resultTable.getValueAt(i, 27) != null){
		  		txtExpMerUcastnik.setText(resultTable.getValueAt(i, 27).toString());
		  	}else{
		  		txtExpMerUcastnik.setText("");
		  	}
		  	if(resultTable.getValueAt(i, 28) != null){
		  	  txtExpRekUsloviya.setText(resultTable.getValueAt(i, 28).toString());
		  	}else{
		  	txtExpRekUsloviya.setText("");
		  	}
		  	if(resultTable.getValueAt(i, 29) != null){
		  	  txtExpPosStatus.setText(resultTable.getValueAt(i, 29).toString());
		  	}else{
		  	  txtExpPosStatus.setText("");
		  	}
		  	if(resultTable.getValueAt(i, 30) != null){
		  	 txtComment.setText(resultTable.getValueAt(i, 30).toString());
		  	}else{
		  	  txtComment.setText("");
		  	}
		  	if(resultTable.getValueAt(i, 31) != null){
		  		cmbBoxExpLevel2.setSelectedItem(resultTable.getValueAt(i, 31).toString());
		  	}else{
		  		cmbBoxExpLevel2.setSelectedItem("");
		  	}
		  	if(model.getValueAt(i, 32) != null){
		  		cmbBoxContracter1.setSelectedItem(model.getValueAt(i, 32).toString());
		  	}else{
		  		cmbBoxContracter1.setSelectedItem("");
		  	}
			if(model.getValueAt(i, 33) != null){
			  	txtAmount6.setText(model.getValueAt(i, 33).toString());
			}else{
			  	txtAmount6.setText("");
			}
			if(model.getValueAt(i, 34) != null){
		  		cmbBoxKeyLeader1.setSelectedItem(model.getValueAt(i, 34).toString());
		  	}else{
		  		cmbBoxKeyLeader1.setSelectedItem("");
		  	}
			if(model.getValueAt(i, 35) != null){
			  	txtAmount7.setText(model.getValueAt(i, 35).toString());
			}else{
			  	txtAmount7.setText("");
			}
			btnAdd.setEnabled(true);
			btnDelete.setEnabled(true);
			getTotalCountSum();
			
		}			
		
	  @Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	
	// A common point of exit
		public static void exit() {
			
			System.out.println("\nThank you for using ExcelUploads");
			System.exit(0);
		}

		public class Utils {
			/*
			 * Get the extension of a file.
			 */
		}
		
		private void getTotalCountSum(){
			double totTempSum=0;
			int totCount = 0;
			String tempAmount = "";
			for (int j = 0; j < model.getRowCount(); j++) {
				if (resultTable.getValueAt(j, 0).toString().equalsIgnoreCase("true")) {	
					tempAmount = resultTable.getValueAt(j, 13).toString();
					if(tempAmount.indexOf(" ")>=0){tempAmount = tempAmount.replace(" ", "");}
					totTempSum = totTempSum + Double.parseDouble(tempAmount);
					totCount = totCount+1;
				}
			}
			txtObsTotAmount.setText(formatAmount(String.valueOf(totTempSum)));
			txtObsTotCount.setText(String.valueOf(totCount));
		}
		
		 private boolean amountControl(String totalAmount, String amount1, String amount2,
					String amount3, String amount4, String amount5, String amount6, String amount7) {
	    	  double totalAmountExpenses = 0;
	    	  if(amount1.indexOf(".")>=0){amount1 = amount1.replace(".", "");}
	    	  if(amount2.indexOf(".")>=0){amount2 = amount2.replace(".", "");}
	    	  if(amount3.indexOf(".")>=0){amount3 = amount3.replace(".", "");}
	    	  if(amount4.indexOf(".")>=0){amount4 = amount4.replace(".", "");}
	    	  if(amount5.indexOf(".")>=0){amount5 = amount5.replace(".", "");}
	    	  if(amount6.indexOf(".")>=0){amount6 = amount6.replace(".", "");}
	    	  if(amount7.indexOf(".")>=0){amount7 = amount7.replace(".", "");}
	    	  if(totalAmount.indexOf(".")>=0){totalAmount = totalAmount.replace(".", "");}
	    	  
	    	  if((amount1.length() > 0 && !amount1.equalsIgnoreCase("0"))||
	    			  amount2.length() > 0 && !amount2.equalsIgnoreCase("0")||
	    			  amount3.length() > 0 && !amount3.equalsIgnoreCase("0")||
	    			  amount4.length() > 0 && !amount4.equalsIgnoreCase("0")||
	    			  amount5.length() > 0 && !amount5.equalsIgnoreCase("0")||
	    			  amount6.length() > 0 && !amount6.equalsIgnoreCase("0")||
	    	  		  amount7.length() > 0 && !amount7.equalsIgnoreCase("0")){
	    		  totalAmountExpenses = Double.parseDouble(amount1)+Double.parseDouble(amount2)+Double.parseDouble(amount3)+
	    				  Double.parseDouble(amount4)+Double.parseDouble(amount5)+Double.parseDouble(amount6)+Double.parseDouble(amount7);
	    		  if(Double.parseDouble(totalAmount) != totalAmountExpenses){
	    			  return false;
	    		  }else{
	    			  return true;
	    		  }
	    	  }else{
	    		  return true;
	    	  }	  		
		}
	 
		 private void amountShownless(Boolean show) {	
			  lblAmount1.setVisible(show);
			  lblAmount2.setVisible(show);
			  lblAmount3.setVisible(show);
			  lblAmount4.setVisible(show);
			  lblAmount5.setVisible(show);
			  lblAmount6.setVisible(show);
			  lblAmount7.setVisible(show);
	
			  txtAmount1.setVisible(show);
			  txtAmount2.setVisible(show);
			  txtAmount3.setVisible(show);
			  txtAmount4.setVisible(show);
			  txtAmount5.setVisible(show);
			  txtAmount6.setVisible(show);
			  txtAmount7.setVisible(show);
			  
			  txtAmount1.setText("0");
			  txtAmount2.setText("0");
			  txtAmount3.setText("0");
			  txtAmount4.setText("0");
			  txtAmount5.setText("0");
			  txtAmount6.setText("0");
			  txtAmount7.setText("0");
			  
			  txtAmountFormat.setText("");
		 }

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		
		if (e.getComponent().getName().equals("amount1")) {
			sumOfExpensens(); 
		}else if (e.getComponent().getName().equals("amount2")) {
			sumOfExpensens();
		}else if (e.getComponent().getName().equals("amount3")) {
			sumOfExpensens();
		}else if (e.getComponent().getName().equals("amount4")) {
			sumOfExpensens();
		}else if (e.getComponent().getName().equals("amount5")) {
			sumOfExpensens();
		}else if (e.getComponent().getName().equals("amount6")) {
			sumOfExpensens();
		}else if (e.getComponent().getName().equals("amount7")) {
			sumOfExpensens();
		}		
	}
	
	private void sumOfExpensens() {
		double totalAmountExpenses = 0;
		if(txtAmount1.getText() != null && txtAmount1.getText().length()>0){
			totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount1.getText().replace(".", ""));
		}
		if(txtAmount2.getText() != null && txtAmount2.getText().length()>0){
			totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount2.getText().replace(".", ""));
		}
		if(txtAmount3.getText() != null && txtAmount3.getText().length()>0){
			totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount3.getText().replace(".", ""));
		}
		if(txtAmount4.getText() != null && txtAmount4.getText().length()>0){
			totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount4.getText().replace(".", ""));
		}
		if(txtAmount5.getText() != null && txtAmount5.getText().length()>0){
			totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount5.getText().replace(".", ""));
		}	
		if(txtAmount6.getText() != null && txtAmount6.getText().length()>0){
			totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount6.getText().replace(".", ""));
		}
		if(txtAmount7.getText() != null && txtAmount7.getText().length()>0){
			totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount7.getText().replace(".", ""));
		}
		String txtTotAmount = String.valueOf(totalAmountExpenses);
		txtTotAmount = txtTotAmount.substring(0, txtTotAmount.indexOf("."));
		txtAmountFormat.setText(txtTotAmount);
	}
	
	private void cleanAllScreen() {
		cmbBoxCompanyCode.setSelectedItem("");
		cmbBoxCountry.setSelectedItem("");
		cmbBoxArea.setSelectedItem("");
		cmbBoxRegion.setSelectedItem("");
		cmbBoxCity.setSelectedItem("");
		cmbBoxExpMain.setSelectedItem("");
		cmbBoxExpLevel1.setSelectedItem("");
		startDate.setDate(cal.getTime());
		endDate.setDate(cal.getTime());
		txtCountFormat.setText("0");
		txtAmountFormat.setText("");
		txtAmount1.setText("");
		txtAmount2.setText("");
		txtAmount3.setText("");
		txtAmount4.setText("");
		txtAmount5.setText("");
		txtAmount6.setText("");
		txtAmount7.setText("");
		cmbBoxExpRekChain.setSelectedItem("");
		cmbBoxContracter.setSelectedItem("");
		cmbBoxContracter1.setSelectedItem("");
		cmbBoxExpMerLecture.setSelectedItem("");
		cmbBoxExpMerOrganizator.setSelectedItem("");
		cmbBoxClinics.setSelectedItem("");
		cmbBoxKeyLeader.setSelectedItem("");
		cmbBoxKeyLeader1.setSelectedItem("");
		cmbBoxExpRekProduct.setSelectedItem("");
		cmbBoxTema.setSelectedItem("");
		txtExpMerUcastnik.setText("");
		txtExpRekUsloviya.setText("");
		txtExpPosStatus.setText("");
		txtComment.setText("");
		cmbBoxExpLevel2.setSelectedItem("");
	}

	private String setEmailText() throws Exception {

		String emailText = "";
		double totSumExps = 0;
		int totalCountExps = 0;
		ESIBag tempBag = new ESIBag();
		int k = 0;
		int l = 0;
		String company ="";
		String country ="";
		String area ="";
		String firstStage = "";

		for (int j = 0; j < model.getRowCount(); j++) {

			if (resultTable.getValueAt(j, 0).toString().equalsIgnoreCase("true")) {

				company = resultTable.getValueAt(j, 3).toString().trim(); // company
				country = resultTable.getValueAt(j, 4).toString().trim();
				area = resultTable.getValueAt(j, 5).toString().trim(); // area
				firstStage = resultTable.getValueAt(j, 8).toString().trim(); // first
				String secondStage = resultTable.getValueAt(j, 9).toString().trim(); // secondStage

				// PaymentType
				String totalCount = resultTable.getValueAt(j, 12).toString().trim();
				String totalSum = resultTable.getValueAt(j, 13).toString().trim();
				String CofFoods = resultTable.getValueAt(j, 14).toString().trim();
				String OrgVznos = resultTable.getValueAt(j, 15).toString().trim();
				String travelAgency = resultTable.getValueAt(j, 16).toString().trim();
				String gonorar = resultTable.getValueAt(j, 17).toString().trim();
				String additionalExps = resultTable.getValueAt(j, 18).toString().trim();
				String OrgVznos1 = resultTable.getValueAt(j, 33).toString().trim();
				String gonorar1 = resultTable.getValueAt(j, 35).toString().trim();

				// PaymentReceiver

				String chain = resultTable.getValueAt(j, 19).toString().trim();
				String Contracter = resultTable.getValueAt(j, 20).toString().trim();
				String Lecturer = resultTable.getValueAt(j, 21).toString().trim();
				String Organizer = resultTable.getValueAt(j, 22).toString().trim();
				String ClinicName = resultTable.getValueAt(j, 23).toString().trim();
				String KOL = resultTable.getValueAt(j, 24).toString().trim();
				String Contracter1 = resultTable.getValueAt(j, 32).toString().trim();
				String KOL1 = resultTable.getValueAt(j, 34).toString().trim();

				if (firstStage.equalsIgnoreCase("Мероприятия")) {

					if (secondStage.equalsIgnoreCase("MK") || secondStage.equalsIgnoreCase("PAS & Master-Set")||
							secondStage.equalsIgnoreCase("Конгресс")||secondStage.equalsIgnoreCase("Конференция")) {
						tempBag.put("TABLE", k, "PAYMENTTYPE", secondStage);
						tempBag.put("TABLE", k, "TOTALCOUNT", totalCount);
						tempBag.put("TABLE", k, "TOTALSUM", totalSum);
						k++;

						if (OrgVznos.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Contracter");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Contracter);
							tempBag.put("TABLE1", l, "PAYMENTSUM", OrgVznos);
							l++;
						}

						if (gonorar.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Gonorar");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", KOL);
							tempBag.put("TABLE1", l, "PAYMENTSUM", gonorar);
							l++;
						}

						if (travelAgency.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Travel Agency");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", "Travel Agency");
							tempBag.put("TABLE1", l, "PAYMENTSUM", travelAgency);
							l++;

						}
						if (additionalExps.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Organizator");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Organizer);
							tempBag.put("TABLE1", l, "PAYMENTSUM", additionalExps);
							l++;

						}

					} else if (secondStage.equalsIgnoreCase("Фарм.кружки")||
							secondStage.equalsIgnoreCase("МП")) {
						tempBag.put("TABLE", k, "PAYMENTTYPE", secondStage);
						tempBag.put("TABLE", k, "TOTALCOUNT", totalCount);
						tempBag.put("TABLE", k, "TOTALSUM", totalSum);
						k++;

						if (CofFoods.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Organizator");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Organizer);
							tempBag.put("TABLE1", l, "PAYMENTSUM", CofFoods);
							l++;
						}
						if (additionalExps.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Organizator");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Organizer);
							tempBag.put("TABLE1", l, "PAYMENTSUM", additionalExps);
							l++;
						}
						if (gonorar1.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Gonorar1");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", KOL1);
							tempBag.put("TABLE1", l, "PAYMENTSUM", gonorar1);
							l++;
						}

					}else if (secondStage.equalsIgnoreCase("ВО")) {
						tempBag.put("TABLE", k, "PAYMENTTYPE", secondStage);
						tempBag.put("TABLE", k, "TOTALCOUNT", totalCount);
						tempBag.put("TABLE", k, "TOTALSUM", totalSum);
						k++;

						if (OrgVznos.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Chain");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", chain);
							tempBag.put("TABLE1", l, "PAYMENTSUM", OrgVznos);
							l++;
						}
						if (CofFoods.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Organizator");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Organizer);
							tempBag.put("TABLE1", l, "PAYMENTSUM", CofFoods);
							l++;
						}
						if (additionalExps.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Organizator");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Organizer);
							tempBag.put("TABLE1", l, "PAYMENTSUM", additionalExps);
							l++;
						}
						

					} else if (secondStage.equalsIgnoreCase("ДС")) {
						tempBag.put("TABLE", k, "PAYMENTTYPE", secondStage);
						tempBag.put("TABLE", k, "TOTALCOUNT", totalCount);
						tempBag.put("TABLE", k, "TOTALSUM", totalSum);
						k++;

						if (CofFoods.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Organizator");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Organizer);
							tempBag.put("TABLE1", l, "PAYMENTSUM", CofFoods);
							l++;

						}

						if (additionalExps.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Organizator");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Organizer);
							tempBag.put("TABLE1", l, "PAYMENTSUM", additionalExps);
							l++;
						}

					}  else if (secondStage.equalsIgnoreCase("КС")) {
						tempBag.put("TABLE", k, "PAYMENTTYPE", secondStage);
						tempBag.put("TABLE", k, "TOTALCOUNT", totalCount);
						tempBag.put("TABLE", k, "TOTALSUM", totalSum);
						k++;

						if (CofFoods.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Organizator");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Organizer);
							tempBag.put("TABLE1", l, "PAYMENTSUM", CofFoods);
							l++;
						}
						if (gonorar.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Gonorar");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", KOL);
							tempBag.put("TABLE1", l, "PAYMENTSUM", gonorar);
							l++;
						}
						if (additionalExps.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Organizator");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Organizer);
							tempBag.put("TABLE1", l, "PAYMENTSUM", additionalExps);
							l++;
						}

					}  else if (secondStage.equalsIgnoreCase("мед.предст")||
							secondStage.equalsIgnoreCase("партнер")) {
						tempBag.put("TABLE", k, "PAYMENTTYPE", secondStage);
						tempBag.put("TABLE", k, "TOTALCOUNT", totalCount);
						tempBag.put("TABLE", k, "TOTALSUM", totalSum);
						k++;
						if (additionalExps.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Organizator");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Organizer);
							tempBag.put("TABLE1", l, "PAYMENTSUM", additionalExps);
							l++;
						}
						
					}   else if (secondStage.equalsIgnoreCase("ЦО")) {
						tempBag.put("TABLE", k, "PAYMENTTYPE", secondStage);
						tempBag.put("TABLE", k, "TOTALCOUNT", totalCount);
						tempBag.put("TABLE", k, "TOTALSUM", totalSum);
						k++;
						if (CofFoods.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Contracter1");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Contracter1);
							tempBag.put("TABLE1", l, "PAYMENTSUM", CofFoods);
							l++;
						}
						if (additionalExps.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Organizator");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Organizer);
							tempBag.put("TABLE1", l, "PAYMENTSUM", additionalExps);
							l++;
						}
						if (travelAgency.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Travel Agency");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", "Travel Agency");
							tempBag.put("TABLE1", l, "PAYMENTSUM", travelAgency);
							l++;

						}
						if (gonorar.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Gonorar");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", KOL);
							tempBag.put("TABLE1", l, "PAYMENTSUM", gonorar);
							l++;
						}

					} else if (secondStage.equalsIgnoreCase("Школа")) {
						tempBag.put("TABLE", k, "PAYMENTTYPE", secondStage);
						tempBag.put("TABLE", k, "TOTALCOUNT", totalCount);
						tempBag.put("TABLE", k, "TOTALSUM", totalSum);
						k++;
						if (OrgVznos.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Clinic");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", ClinicName);
							tempBag.put("TABLE1", l, "PAYMENTSUM", OrgVznos);
							l++;
						}
						if (additionalExps.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Organizator");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Organizer);
							tempBag.put("TABLE1", l, "PAYMENTSUM", additionalExps);
							l++;
						}
						if (travelAgency.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Travel Agency");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", "Travel Agency");
							tempBag.put("TABLE1", l, "PAYMENTSUM", travelAgency);
							l++;

						}
						if (gonorar.length() > 0) {
							tempBag.put("TABLE1", l, "PAYMENTGROUP", "Gonorar");
							tempBag.put("TABLE1", l, "PAYMENTRECEIVER", KOL);
							tempBag.put("TABLE1", l, "PAYMENTSUM", gonorar);
							l++;
						}
					}

				} else if (firstStage.equalsIgnoreCase("POS-материалы")) {
					tempBag.put("TABLE", k, "PAYMENTTYPE", secondStage);
					tempBag.put("TABLE", k, "TOTALCOUNT", totalCount);
					tempBag.put("TABLE", k, "TOTALSUM", totalSum);
					k++;
					tempBag.put("TABLE1", l, "PAYMENTGROUP", "Contracter");
					tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Contracter);
					tempBag.put("TABLE1", l, "PAYMENTSUM", totalSum);
					l++;				
				}else if (firstStage.equalsIgnoreCase("Реклама")) {			
					 if (secondStage.equalsIgnoreCase("SMM")) {
						tempBag.put("TABLE", k, "PAYMENTTYPE", secondStage);
						tempBag.put("TABLE", k, "TOTALCOUNT", totalCount);
						tempBag.put("TABLE", k, "TOTALSUM", totalSum);
						k++;

						tempBag.put("TABLE1", l, "PAYMENTGROUP", "Contracter");
						tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Contracter);
						tempBag.put("TABLE1", l, "PAYMENTSUM", totalSum);
						l++;				
					} else if (secondStage.equalsIgnoreCase("Digital")) {
						tempBag.put("TABLE", k, "PAYMENTTYPE", secondStage);
						tempBag.put("TABLE", k, "TOTALCOUNT", totalCount);
						tempBag.put("TABLE", k, "TOTALSUM", totalSum);
						k++;

						tempBag.put("TABLE1", l, "PAYMENTGROUP", "Contracter");
						tempBag.put("TABLE1", l, "PAYMENTRECEIVER", Contracter);
						tempBag.put("TABLE1", l, "PAYMENTSUM", totalSum);
						l++;						
					} else if (secondStage.equalsIgnoreCase("Клиники")) {
						tempBag.put("TABLE", k, "PAYMENTTYPE", secondStage);
						tempBag.put("TABLE", k, "TOTALCOUNT", totalCount);
						tempBag.put("TABLE", k, "TOTALSUM", totalSum);
						k++;

						tempBag.put("TABLE1", l, "PAYMENTGROUP", "Clinic");
						tempBag.put("TABLE1", l, "PAYMENTRECEIVER", ClinicName);
						tempBag.put("TABLE1", l, "PAYMENTSUM", totalSum);
						l++;						
					} else if (secondStage.equalsIgnoreCase("Аптеки")) {
						tempBag.put("TABLE", k, "PAYMENTTYPE", secondStage);
						tempBag.put("TABLE", k, "TOTALCOUNT", totalCount);
						tempBag.put("TABLE", k, "TOTALSUM", totalSum);
						k++;

						tempBag.put("TABLE1", l, "PAYMENTGROUP", "Aptek");
						tempBag.put("TABLE1", l, "PAYMENTRECEIVER", chain);
						tempBag.put("TABLE1", l, "PAYMENTSUM", totalSum);
						l++;						
					}
					

				}

			}		

		}
		emailText = "Ниже расходов, утвержденных "+ "\n\n";
		emailText = emailText + "Company : "+ company+ "\n\n";
		emailText = emailText + "Country : "+ country+ "\n\n";
		emailText = emailText + "Area : "+ area+ "\n\n";
		emailText = emailText + "Main Expense Group : "+ firstStage+ "\n\n";
		emailText = emailText + RPad("Расходы",50,' ') + RPad("Общее количество",20,' ')+RPad("сумма",15,' ');
		
		int manTableSize = tempBag.getSize("TABLE");
		int accTableSize = tempBag.getSize("TABLE1");
		double totalExps = 0;
		int totCount = 0;
		int count = 0;
		if(firstStage.equalsIgnoreCase("Мероприятия")){
			count = 1;
		}
		for (int i = 0; i < manTableSize; i ++) { 
			if(!tempBag.get("TABLE", i, "TOTALSUM").toString().equalsIgnoreCase("0.000")){
				String payType = tempBag.get("TABLE", i, "PAYMENTTYPE").toString();
				Double tempExps = Double.parseDouble(tempBag.get("TABLE", i, "TOTALSUM").toString().replace(" ", ""));
				if(!firstStage.equalsIgnoreCase("Мероприятия")){
					count = Integer.valueOf(tempBag.get("TABLE", i, "TOTALCOUNT").toString());
				}else{
					count = 1;
				}				
				for (int m = 0; m < manTableSize; m ++) { 
					if(m>i && payType.equalsIgnoreCase(tempBag.get("TABLE", m, "PAYMENTTYPE").toString())){
						tempExps = tempExps + Double.parseDouble(tempBag.get("TABLE", m, "TOTALSUM").toString().replace(" ", ""));
						if(firstStage.equalsIgnoreCase("Мероприятия")){
							count=count+1;
						}else{
							count = count + Integer.valueOf(tempBag.get("TABLE", m, "TOTALCOUNT").toString());
						}
						tempBag.put("TABLE", m, "PAYMENTTYPE","");
						tempBag.put("TABLE", m, "TOTALSUM","0.000");
					}
				}
				//String strSumExps = String.format("%,d", tempExps);
				String strSumExps = String.valueOf(tempExps);
				emailText = emailText + "\n\n" + RPad(payType,50,' ')+ RPad(String.valueOf(count),20,' ')+RPad(strSumExps,15,' ');
				totalExps =totalExps+ tempExps;
				totCount = totCount + count;
			}
		}
		emailText = emailText + "\n\n" +RPad("TOTAL",50,' ')+ RPad(String.valueOf(totCount),20,' ')+RPad(String.valueOf(totalExps),15,' ');
		emailText = emailText + "\n\n"+ "\n\n"+RPad("#",30,' ')+ RPad(String.valueOf("детали платежа"),50,' ')+"сумма";
		String payType="";
		String payReceiver="";
		Double tempExps =0.00;
		totalExps =0;
		count = 1;
		for (int i = 0; i < accTableSize; i++) { 
			if(!tempBag.get("TABLE1", i, "PAYMENTSUM").toString().trim().equalsIgnoreCase("0.000")&&
					!tempBag.get("TABLE1", i, "PAYMENTSUM").toString().trim().equalsIgnoreCase("0")){
				payType = tempBag.get("TABLE1", i, "PAYMENTGROUP").toString();
				payReceiver = tempBag.get("TABLE1", i, "PAYMENTRECEIVER").toString();
				tempExps = Double.parseDouble(tempBag.get("TABLE1", i, "PAYMENTSUM").toString().replace(" ", ""));
				for (int m = 0; m < accTableSize; m ++) {
					if(m>i && payType.equalsIgnoreCase(tempBag.get("TABLE1", m, "PAYMENTGROUP").toString())
							&&payReceiver.equalsIgnoreCase(tempBag.get("TABLE1", m, "PAYMENTRECEIVER").toString())){
						tempExps = tempExps + Double.parseDouble(tempBag.get("TABLE1", m, "PAYMENTSUM").toString().replace(" ", ""));
						tempBag.put("TABLE1", m, "PAYMENTGROUP","");
						tempBag.put("TABLE1", m, "PAYMENTRECEIVER","");
						tempBag.put("TABLE1", m, "PAYMENTSUM","0.000");
					}
				}
				//String strSumExps = String.format("%,d", tempExps);
				totalExps = totalExps+tempExps;
				String strSumExps = String.valueOf(tempExps);
				emailText = emailText + "\n\n" + RPad(payType,30,' ')+ RPad(payReceiver,50,' ')+strSumExps+" руб";
				count = count+1;
			}
			
		}
		emailText = emailText + "\n\n" + RPad("Total",80,' ')+String.valueOf(totalExps)+" руб";

		return emailText;

	}
	public static String RPad(String str, Integer length, char car) {
		  return str+String.format("%" + (length - str.length()) + "s", "")
		               .replace(" ", String.valueOf(car)) ;
		}
	private String formatAmount(String amount){			
		if(amount == null && amount.length()==0){
			amount = "0";
		}
		if(amount.indexOf(" ")>=0){amount = amount.replace(" ", "");}
		String tempAmount = doubleFormatter.format(Double.valueOf(amount));
		return tempAmount;
	}

}