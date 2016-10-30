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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import com.toedter.calendar.JDateChooser;

import cb.esi.esiclient.smg.general.utility.CBBag;
import cb.esi.esiclient.util.ESIBag;
import main.ConnectToDb;
import main.SendMail;

public class ExpsObsScreen extends JFrame implements ActionListener,ItemListener,MouseListener,FocusListener{
	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 900;

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
	lblSearchEventDate,lblSearchEntryDate,lblEmpty,lblEmpty1,lblEmpty2,lblEmpty3,lblEmpty4,lblEmpty5,lblStatus;
	private JPanel paramPanelMain,paramPanelAddress,paramPanelDates,paramPanelResult,paramPanelExpTypes,paramPanelExpParMer,
	paramPanelExpParRek,paramPanelExpParPos,paramPanelBtn,paramPanelPaymentReceiver,paramPanelAmounts,pnlInfoMsg,paramPanelSearchDates;
	private JScrollPane jScroll;
	private JTable resultTable;
	private JComboBox cmbBoxCountry,cmbBoxRegion,cmbBoxCity,cmbBoxArea,
	cmbBoxExpMain,cmbBoxExpLevel1,cmbBoxExpLevel2,cmbBoxCompanyCode,cmbBoxExpMerLecture,cmbBoxExpMerOrganizator,
	cmbBoxExpRekChain,cmbBoxExpRekProduct,cmbBoxExpPosProduct,cmbBoxClinics,cmbBoxContracter,cmbBoxContracter1,cmbBoxKeyLeader,
	cmbBoxsearchEventDateSmall,cmbBoxsearchEventDateBig,cmbBoxsearchEntryDateSmall,cmbBoxsearchEntryDateBig,cmbBoxTema,cmbBoxStatus,cmbBoxKeyLeader1;
	private JDateChooser startDate,endDate;
	private JTextField txtExpMerUcastnik,txtExpRekUsloviya,txtExpPosParyadk,txtExpPosStatus,txtComment,txtEmpty1,txtEmpty2,txtEmpty3,txtEmpty4,txtEmpty5;
	public JButton btnAdd,btnClear,btnUpdate,btnSearch,btnExit;
	public JTextArea textAreaComment;
	public JFormattedTextField txtCountFormat,txtAmountFormat,txtAmount1,txtAmount2,txtAmount3,txtAmount4,txtAmount5,txtAmount6,txtAmount7;
	DefaultTableModel dtm = new DefaultTableModel(0, 0);

	DefaultTableModel model = new DefaultTableModel(null, new String [] {"ID","Approval_Status",
			"Company","Country","Area","Region","City",
			"First_Stage","Second_Stage",
			"Start_Date","End_Date","Total_Count","Total_Sum",
			"Conf_Food_Sum", "Org_Exps_Sum","Org_Exps_Sum1","Travel_Agency_Sum","Key_Person_Sum","Key_Person_Sum1", "Add_Exp_Sum", 
			"Chain","Contracter","Contracter1","Lecturer","Organizer","Clinic_Name","Key_Leader","Key_Leader1",
			"Product_Name","Tema","Attenders_Count","Conditions","Statuses","Comment",
			"Third Stage","entry_date","entry_user","approve_date","approve_user"}) {
        public Class getColumnClass(int c) {
          switch (c) {
            default: return String.class;
          }   
        } };

    	private String userName="Hakan KAYAKUTLU";
    	private String userBrand="ALL";
    	private String userCountry="ALL";
    	private String userArea="ALL";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ESIBag inBag = new ESIBag();
					ExpsObsScreen window = new ExpsObsScreen(inBag);
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
	 */
	public ExpsObsScreen(ESIBag inBag) {
		super("Expense Observation");
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
		
		
		cmbBoxStatus = new JComboBox( new String[]{});
		cmbBoxStatus.addItem("Waiting On Approval");
		cmbBoxStatus.addItem("Approved");
		cmbBoxStatus.addItem("Rejected");
		cmbBoxStatus.setEditable(true);	
		cmbBoxStatus.setEnabled(true);
		cmbBoxStatus.setSelectedIndex(-1);
		
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
			cmbBoxCompanyCode.setSelectedIndex(0);
			cmbBoxCompanyCode.setEnabled(true);
		}else if(userBrand.equalsIgnoreCase("SOLGAR")){
			cmbBoxCompanyCode.addItem("SOLGAR");
			cmbBoxCompanyCode.setSelectedIndex(0);
		}else if(userBrand.equalsIgnoreCase("BOUNTY")){
			cmbBoxCompanyCode.addItem("NATURES BOUNTY");
			cmbBoxCompanyCode.setSelectedIndex(0);
		}
		
		if(userCountry.equalsIgnoreCase("ALL")){
			ConnectToDb.getPRMDataGroupBy("country", "solgar_prm.prm_exps_addresses",cmbBoxCountry,"","");	
			cmbBoxCountry.setMaximumRowCount(50);
			cmbBoxCountry.setEditable(true);
			cmbBoxCountry.setSelectedIndex(-1);
			cmbBoxCountry.setEnabled(true);
			cmbBoxArea.setEnabled(true);
		}else if(userCountry.equalsIgnoreCase("Russia")){
			cmbBoxCountry.addItem("Russia");
			cmbBoxCountry.setSelectedIndex(0);
			if(userArea.equalsIgnoreCase("ALL")){
				ConnectToDb.getPRMDataGroupBy("area", "solgar_prm.prm_exps_addresses",cmbBoxArea,"country",cmbBoxCountry.getSelectedItem().toString());
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
				ConnectToDb.getPRMDataGroupBy("area", "solgar_prm.prm_exps_addresses",cmbBoxArea,"country",cmbBoxCountry.getSelectedItem().toString());
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
		ConnectToDb.getPRMDataGroupBy("main_name", "solgar_prm.prm_exps_types",cmbBoxExpMain,"","");	
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
		ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerOrganizator,"company_name",
				cmbBoxCompanyCode.getSelectedItem().toString(),"","");
		cmbBoxExpMerOrganizator.setSelectedIndex(-1);
		cmbBoxExpMerOrganizator.setEditable(true);
		//cmbBoxExpMerOrganizator.setEnabled(false);
		
		cmbBoxExpRekChain = new JComboBox( new String[]{});		
		cmbBoxExpRekChain.setEditable(true);
		cmbBoxExpRekChain.setEnabled(false);

		cmbBoxExpRekProduct = new JComboBox( new String[]{});	
		cmbBoxExpRekProduct.setEditable(true);
		cmbBoxExpRekProduct.setEnabled(false);
		
		cmbBoxExpPosProduct = new JComboBox( new String[]{});	
		cmbBoxExpPosProduct.setEditable(true);
		cmbBoxExpPosProduct.setEnabled(false);
		
		cmbBoxClinics = new JComboBox( new String[]{});
		cmbBoxClinics.setEditable(true);
		cmbBoxClinics.setEnabled(false);
		
		cmbBoxKeyLeader = new JComboBox( new String[]{});	
		cmbBoxKeyLeader.setEditable(true);
		cmbBoxKeyLeader.setEnabled(false);
		
		cmbBoxKeyLeader1 = new JComboBox( new String[]{});					
		cmbBoxKeyLeader1.setEditable(true);
		cmbBoxKeyLeader1.setEnabled(false);
		
		cmbBoxContracter = new JComboBox( new String[]{});		
		cmbBoxContracter.setEditable(true);	
		cmbBoxContracter.setEnabled(false);
		
		cmbBoxContracter1 = new JComboBox( new String[]{});		
		cmbBoxContracter1.setEditable(true);	
		cmbBoxContracter1.setEnabled(false);
		
		cmbBoxTema = new JComboBox( new String[]{});		
		cmbBoxTema.setEditable(true);	
		cmbBoxTema.setEnabled(false);
		
		cmbBoxsearchEventDateSmall = new JComboBox( new String[]{});		
		ConnectToDb.getPRMData("report_date", "solgar_prm.prm_report_dates",cmbBoxsearchEventDateSmall);				
		cmbBoxsearchEventDateSmall.setMaximumRowCount(50);
		cmbBoxsearchEventDateSmall.setEditable(true);
		cmbBoxsearchEventDateSmall.setSelectedIndex(-1);
		
		cmbBoxsearchEventDateBig = new JComboBox( new String[]{});		
		ConnectToDb.getPRMData("report_date", "solgar_prm.prm_report_dates",cmbBoxsearchEventDateBig);				
		cmbBoxsearchEventDateBig.setMaximumRowCount(50);
		cmbBoxsearchEventDateBig.setEditable(true);
		cmbBoxsearchEventDateBig.setSelectedIndex(-1);
		
		cmbBoxsearchEntryDateSmall = new JComboBox( new String[]{});		
		ConnectToDb.getPRMData("report_date", "solgar_prm.prm_report_dates",cmbBoxsearchEntryDateSmall);				
		cmbBoxsearchEntryDateSmall.setMaximumRowCount(50);
		cmbBoxsearchEntryDateSmall.setEditable(true);
		cmbBoxsearchEntryDateSmall.setSelectedIndex(-1);
		
		cmbBoxsearchEntryDateBig = new JComboBox( new String[]{});		
		ConnectToDb.getPRMData("report_date", "solgar_prm.prm_report_dates",cmbBoxsearchEntryDateBig);				
		cmbBoxsearchEntryDateBig.setMaximumRowCount(50);
		cmbBoxsearchEntryDateBig.setEditable(true);
		cmbBoxsearchEntryDateBig.setSelectedIndex(-1);
		
		//Date Field		
		
		startDate = new JDateChooser();
		startDate.setDateFormatString("yyyy-MM-dd");		
		//startDate.setDate(cal.getTime());
		//startDate.setEnabled(false);
		
		endDate = new JDateChooser();
		endDate.setDateFormatString("yyyy-MM-dd");		
		//endDate.setDate(cal.getTime());
		//endDate.setEnabled(false);
		
		//Text Fields	
		txtEmpty1 = new JTextField();
		txtEmpty2 = new JTextField();
		txtEmpty3 = new JTextField();
		txtEmpty4 = new JTextField();
		txtEmpty5 = new JTextField();
		txtEmpty1.setVisible(false);
		txtEmpty2.setVisible(false);
		txtEmpty3.setVisible(false);
		txtEmpty4.setVisible(false);
		txtEmpty5.setVisible(false);
		
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
		
		//Buttons
		btnAdd = new JButton("Add");
		btnAdd.setEnabled(false);
		
		btnClear = new JButton("Clear");
		
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
		btnClear.addActionListener(this);
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
		paramPanelAddress.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanelAddress, BorderLayout.NORTH);
		
		// Expense type parameters		
		paramPanelExpTypes.add(lblExpFirstStage);
		paramPanelExpTypes.add(cmbBoxExpMain);
		paramPanelExpTypes.add(lblExpSecondStage);
		paramPanelExpTypes.add(cmbBoxExpLevel1);	
		paramPanelExpTypes.add(lblExpThirdStage);
		paramPanelExpTypes.add(cmbBoxExpLevel2);	
		paramPanelExpTypes.add(lblEmpty1);
		paramPanelExpTypes.add(txtEmpty1);
		paramPanelExpTypes.add(lblEmpty1);
		paramPanelExpTypes.add(txtEmpty2);
		paramPanelExpTypes.add(lblEmpty2);
		paramPanelExpTypes.add(txtEmpty3);
		
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
		paramPanelBtn.add(btnClear);
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
		paramPanelSearchDates.add(lblEmpty5);
		paramPanelSearchDates.add(txtEmpty5);
		
		paramPanelMain.add(paramPanelAddress);		
		paramPanelMain.add(paramPanelExpTypes);
		paramPanelMain.add(paramPanelDates);		
		paramPanelMain.add(paramPanelPaymentReceiver);
		paramPanelMain.add(paramPanelExpParMer);	
		paramPanelMain.add(paramPanelAmounts);
		paramPanelMain.add(paramPanelSearchDates);
		
		paramPanelMain.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanelMain, BorderLayout.NORTH);		
		
		paramPanelBtn.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanelBtn, BorderLayout.SOUTH);	
		
		paramPanelResult.add(jScroll);			
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
			if (e.getActionCommand().equals("Add")) {
				
			}else if (e.getActionCommand().equals("Update")) {
				
			}else if (e.getActionCommand().equals("Clear")) {
				cleanAllScreen();
			}else if (e.getActionCommand().equals("Search")) {				
				
				//Clean all table
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

	public void itemStateChanged(ItemEvent itemEvent) {
  	  JComboBox cmbBox = (JComboBox)itemEvent.getSource();
  	  String name = cmbBox.getName();
  	  if(cmbBox.getSelectedItem() != null &&cmbBox.getSelectedItem().toString().length()>0){
	    	  if(name.equalsIgnoreCase("Country")){
	    		  cmbBoxArea.removeAllItems();
	    		  cmbBoxRegion.removeAllItems();
	    		  cmbBoxCity.removeAllItems();	    		  
	    		  ConnectToDb.getPRMDataGroupBy("area", "solgar_prm.prm_exps_addresses",cmbBoxArea,"country",cmbBoxCountry.getSelectedItem().toString());	    		  
	    		  cmbBoxRegion.setSelectedIndex(-1);
	    		  cmbBoxCity.setSelectedIndex(-1);	    		  	    		  
	    	  }else if(name.equalsIgnoreCase("Area")){
	    		  cmbBoxRegion.removeAllItems();
	    		  cmbBoxCity.removeAllItems();	    		  
	    		  ConnectToDb.getPRMDataGroupBy("region", "solgar_prm.prm_exps_addresses",cmbBoxRegion,"area",cmbBoxArea.getSelectedItem().toString());
	    		  cmbBoxExpMerLecture.removeAllItems();
	    		  cmbBoxExpMerOrganizator.removeAllItems();
	    		  if(cmbBoxCompanyCode.getSelectedItem() != null && cmbBoxArea.getSelectedItem() != null){
	    			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerLecture,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
  					  "country",cmbBoxArea.getSelectedItem().toString());
	    			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerOrganizator,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
	    					  "country",cmbBoxArea.getSelectedItem().toString());
	    			  cmbBoxExpMerLecture.setSelectedIndex(-1);
	    			  cmbBoxExpMerOrganizator.setSelectedIndex(-1);
	    		  }
	    		  cmbBoxRegion.setSelectedIndex(-1);
	    		  cmbBoxCity.setSelectedIndex(-1);	    		  
	    	  }else if(name.equalsIgnoreCase("Region")){
	    		  cmbBoxCity.removeAllItems();
	    		  ConnectToDb.getPRMDataGroupBy("city", "solgar_prm.prm_exps_addresses",cmbBoxCity,"region",cmbBoxRegion.getSelectedItem().toString());
	    		  cmbBoxCity.setSelectedIndex(-1);
	    		  if(cmbBoxCompanyCode.getSelectedItem() != null && cmbBoxArea.getSelectedItem() != null){
	    			  cmbBoxExpMerLecture.removeAllItems();
		    		  cmbBoxExpMerOrganizator.removeAllItems();
	    			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerLecture,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
  					  "country",cmbBoxArea.getSelectedItem().toString());
	    			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerOrganizator,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
	    					  "country",cmbBoxArea.getSelectedItem().toString());
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
	    		  ConnectToDb.getPRMDataGroupBy("level1", "solgar_prm.prm_exps_types",cmbBoxExpLevel1,"main_name",cmbBoxExpMain.getSelectedItem().toString());
	    		  cmbBoxExpLevel1.setSelectedIndex(-1);
	    		  cmbBoxExpLevel2.setSelectedIndex(-1);
	    	  }else if(name.equalsIgnoreCase("Level1")){
	    		  cmbBoxExpLevel2.removeAllItems();
	    		  ConnectToDb.getPRMDataGroupBy("level2", "solgar_prm.prm_exps_types",cmbBoxExpLevel2,"level1",cmbBoxExpLevel1.getSelectedItem().toString());
	    		  cmbBoxExpLevel2.setSelectedIndex(-1);
	    	  }else if(name.equalsIgnoreCase("Level2")){
	    		  //enson
	    	  }else if(name.equalsIgnoreCase("CompanyCode")){    		  
	    		  if(cmbBoxCompanyCode.getSelectedItem() != null){
	    			  cmbBoxExpPosProduct.removeAllItems();	
	    			  cmbBoxExpRekProduct.removeAllItems();	
		    		  if(cmbBoxCompanyCode.getSelectedItem().toString().equalsIgnoreCase("SOLGAR")){
	    				  ConnectToDb.getPRMDataTwoConditionsGroupBy("product", "solgar_prm.prm_exps_top_products",cmbBoxExpRekProduct,"company","SL", "","");
	    				  ConnectToDb.getPRMDataTwoConditionsGroupBy("product", "solgar_prm.prm_exps_top_products",cmbBoxExpPosProduct,"company","SL", "","");
	    				  cmbBoxExpPosProduct.setSelectedIndex(-1);
	    				  cmbBoxExpRekProduct.setSelectedIndex(-1);
	    			  }else{
	    				  ConnectToDb.getPRMDataTwoConditionsGroupBy("product", "solgar_prm.prm_exps_top_products",cmbBoxExpRekProduct,"company","BN", "","");
	    				  ConnectToDb.getPRMDataTwoConditionsGroupBy("product", "solgar_prm.prm_exps_top_products",cmbBoxExpPosProduct,"company","BN", "","");
	    				  cmbBoxExpRekProduct.setSelectedIndex(-1);
	    				  cmbBoxExpPosProduct.setSelectedIndex(-1);
	    			  }
		    		  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerOrganizator,"company_name",
		    					cmbBoxCompanyCode.getSelectedItem().toString(),"","");
		    		  cmbBoxExpMerOrganizator.setSelectedIndex(-1);
	    		  }
	    		  
	    		  if(cmbBoxArea.getSelectedItem() != null && cmbBoxCompanyCode.getSelectedItem() != null){
		    		  cmbBoxExpMerLecture.removeAllItems();
		    		  cmbBoxExpMerOrganizator.removeAllItems();	
	    			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerLecture,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
    					  "country",cmbBoxArea.getSelectedItem().toString());
	    			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerOrganizator,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
	    					  "country",cmbBoxArea.getSelectedItem().toString());	
	    			  cmbBoxExpMerLecture.setSelectedIndex(-1);
	    			  cmbBoxExpMerOrganizator.setSelectedIndex(-1);
	    		  }	    		  	    		  
	    	  }
  	  }
         
    }   
      

	  public void mouseClicked(MouseEvent e) {
		  //cleanAllScreen();
		    SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
		    int index  = 2;
		  	int i = resultTable.getSelectedRow();
		  	
		  	if(model.getValueAt(i, index) != null){
		  		cmbBoxCompanyCode.setSelectedItem(model.getValueAt(i, index).toString());
		  	}else{
		  		cmbBoxCompanyCode.setSelectedItem("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  		cmbBoxCountry.setSelectedItem(model.getValueAt(i, index).toString());
		  	}else{
		  		cmbBoxCountry.setSelectedItem("");
		  	}
		  	index = index+1;
			if(model.getValueAt(i, index) != null){
		  		cmbBoxArea.setSelectedItem(model.getValueAt(i, index).toString());
		  	}else{
		  		cmbBoxArea.setSelectedItem("");
		  	}
			index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  		cmbBoxRegion.setSelectedItem(model.getValueAt(i,index).toString());
		  	}else{
		  		cmbBoxRegion.setSelectedItem("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  		cmbBoxCity.setSelectedItem(model.getValueAt(i, index).toString());
		  	}else{
		  		cmbBoxCity.setSelectedItem("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  		cmbBoxExpMain.setSelectedItem(model.getValueAt(i,index).toString());
		  	}else{
		  		cmbBoxExpMain.setSelectedItem("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  		cmbBoxExpLevel1.setSelectedItem(model.getValueAt(i, index).toString());
		  	}else{
		  		cmbBoxExpLevel1.setSelectedItem("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){			
		  		try {
		  			startDate.setDate(dcn.parse(model.getValueAt(i, index).toString()));
		  		} catch (Exception e2) {
		  			// TODO: handle exception
		  		}
		  	}else{
		  		startDate.setDateFormatString("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){	
		  		try{
		  			endDate.setDate(dcn.parse(model.getValueAt(i, index).toString()));
		  		} catch (Exception e2) {
		  			// TODO: handle exception
		  		}
		  	}else{
		  		endDate.setDateFormatString("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  		txtCountFormat.setText(model.getValueAt(i, index).toString());
		  	}else{
		  		txtCountFormat.setText("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  	 txtAmountFormat.setText(model.getValueAt(i, index).toString());
		  	}else{
		  	  txtAmountFormat.setText("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  	 txtAmount1.setText(model.getValueAt(i, index).toString());
		  	}else{
		  	  txtAmount1.setText("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i,index) != null){
		  	 txtAmount2.setText(model.getValueAt(i, index).toString());
		  	}else{
		  	  txtAmount2.setText("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
			  	txtAmount6.setText(model.getValueAt(i, index).toString());
			}else{
			  	txtAmount6.setText("");
			}
			index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  	 txtAmount3.setText(model.getValueAt(i, index).toString());
		  	}else{
		  	  txtAmount3.setText("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  	 txtAmount4.setText(model.getValueAt(i, index).toString());
		  	}else{
		  	 txtAmount4.setText("");
		  	}
		  	index = index+1;		  	
		  	if(model.getValueAt(i, index) != null){
			  	txtAmount7.setText(model.getValueAt(i, index).toString());
			}else{
			  	txtAmount7.setText("");
			}
		  	index = index+1;	  	
		  	if(model.getValueAt(i, index) != null){
		  	 txtAmount5.setText(model.getValueAt(i, index).toString());
		  	}else{
		  	  txtAmount5.setText("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  	  cmbBoxExpRekChain.setSelectedItem(model.getValueAt(i, index).toString());
		  	}else{
		  	  cmbBoxExpRekChain.setSelectedItem("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  	  cmbBoxContracter.setSelectedItem(model.getValueAt(i, index).toString());
		  	}else{
		  	  cmbBoxContracter.setSelectedItem("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  		cmbBoxContracter1.setSelectedItem(model.getValueAt(i, index).toString());
		  	}else{
		  		cmbBoxContracter1.setSelectedItem("");
		  	}
			index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  		cmbBoxExpMerLecture.setSelectedItem(model.getValueAt(i, index).toString());
		  	}else{
		  		cmbBoxExpMerLecture.setSelectedItem("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  		cmbBoxExpMerOrganizator.setSelectedItem(model.getValueAt(i, index).toString());
		  	}else{
		  		cmbBoxExpMerOrganizator.setSelectedItem("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  		cmbBoxClinics.setSelectedItem(model.getValueAt(i, index).toString());
		  	}else{
		  		cmbBoxClinics.setSelectedItem("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  		cmbBoxKeyLeader.setSelectedItem(model.getValueAt(i, index).toString());
		  	}else{
		  		cmbBoxKeyLeader.setSelectedItem("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  		cmbBoxKeyLeader1.setSelectedItem(model.getValueAt(i, index).toString());
		  	}else{
		  		cmbBoxKeyLeader1.setSelectedItem("");
		  	}
			index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  	  cmbBoxExpRekProduct.setSelectedItem(model.getValueAt(i, index).toString());
		  	}else{
		  	  cmbBoxExpRekProduct.setSelectedItem("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  		cmbBoxTema.setSelectedItem(model.getValueAt(i, index).toString());
		  	}else{
		  		cmbBoxTema.setSelectedItem("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  		txtExpMerUcastnik.setText(model.getValueAt(i, index).toString());
		  	}else{
		  		txtExpMerUcastnik.setText("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  	  txtExpRekUsloviya.setText(model.getValueAt(i, index).toString());
		  	}else{
		  	txtExpRekUsloviya.setText("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  	  txtExpPosStatus.setText(model.getValueAt(i, index).toString());
		  	}else{
		  	  txtExpPosStatus.setText("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  	 txtComment.setText(model.getValueAt(i, index).toString());
		  	}else{
		  	  txtComment.setText("");
		  	}
		  	index = index+1;
		  	if(model.getValueAt(i, index) != null){
		  		cmbBoxExpLevel2.setSelectedItem(model.getValueAt(i, index).toString());
		  	}else{
		  		cmbBoxExpLevel2.setSelectedItem("");
		  	}
		  	index = index+1;				
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
		
		private void createModel() {
			ESIBag tempBag = ConnectToDb.getMarktExpsWithParam(cmbBoxCountry,cmbBoxArea,cmbBoxCompanyCode,cmbBoxExpMain,cmbBoxExpLevel1,cmbBoxExpLevel2,
					cmbBoxsearchEventDateSmall,cmbBoxsearchEventDateBig,cmbBoxsearchEntryDateSmall,cmbBoxsearchEntryDateBig,cmbBoxStatus,cmbBoxExpMerOrganizator,startDate,endDate);
			    try{
				for (int j = 0; j < tempBag.getSize("TABLE"); j++){
					model.addRow(new Object [] 
			        		{
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
					        	tempBag.get("TABLE",j,"Org_Exps_Sum1"),
					        	tempBag.get("TABLE",j,"Travel_Agency_Sum"),
					        	tempBag.get("TABLE",j,"Key_Person_Sum"),
					        	tempBag.get("TABLE",j,"Key_Person_Sum1"),
					        	tempBag.get("TABLE",j,"Add_Exp_Sum"),
					        	tempBag.get("TABLE",j,"Chain"),
					        	tempBag.get("TABLE",j,"Contracter"),
					        	tempBag.get("TABLE",j,"Contracter1"),
					        	tempBag.get("TABLE",j,"Lecturer"),
					        	tempBag.get("TABLE",j,"Organizer"),
					        	tempBag.get("TABLE",j,"Clinic_Name"),
					        	tempBag.get("TABLE",j,"Key_Leader"),
					        	tempBag.get("TABLE",j,"Key_Leader1"),
					        	tempBag.get("TABLE",j,"Product_Name"),
					        	tempBag.get("TABLE",j,"Tema"),
					        	tempBag.get("TABLE",j,"Attenders_Count"),
					        	tempBag.get("TABLE",j,"Conditions"),
					        	tempBag.get("TABLE",j,"Statuses"),
					        	tempBag.get("TABLE",j,"Comments"),
					        	tempBag.get("TABLE",j,"Third_Stage"),					        						        						        	
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

		private void cleanAllScreen() {
			Calendar cal = Calendar.getInstance();
			//cmbBoxCompanyCode.setSelectedItem("");
			//cmbBoxCountry.setSelectedItem("");
			//cmbBoxArea.setSelectedItem("");
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

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	
}