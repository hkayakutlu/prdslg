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

import cb.esi.esiclient.util.ESIBag;
import main.ConnectToDb;
import main.SendMail;

public class ExpsEntryScreen extends JFrame implements ActionListener,ItemListener,MouseListener,FocusListener{
	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 900;

	private JFrame frame;
	private JTable tblReportResult;	
	private JLabel lblAdrCountry,lblAdrArea,lblAdrRegion,lblAdrCity,lblStartDate,lblEndDate,
	lblExpFirstStage,lblExpSecondStage,lblExpThirdStage,
	lblExpMerLecture,lblExpMerOrganizator,lblExpMerTema,lblExpMerUcastnik,
    lblExpRekUsloviya,lblExpRekProduct,lblExpRekChain,
	lblExpPosProduct,lblExpPosParyadk,lblExpPosStatus,
	lblCount,lblAmount,lblComment,lblCompanyCode,
	lblAmount1,lblAmount2,lblAmount3,lblAmount4,lblAmount5,lblAmount6,
	lblClinicName,lblContracter,lblContracter1,lblKeyLeader,lblKeyLeader1,
	lblEmpty,lblEmpty1,lblAmount7;
	private JPanel paramPanelMain,paramPanelAddress,paramPanelDates,paramPanelResult,paramPanelExpTypes,paramPanelExpParMer,
	paramPanelExpParRek,paramPanelExpParPos,paramPanelBtn,paramPanelPaymentReceiver,paramPanelAmounts,pnlInfoMsg;
	private JScrollPane jScroll;
	private JTable resultTable;
	private JComboBox cmbBoxCountry,cmbBoxRegion,cmbBoxCity,cmbBoxArea,
	cmbBoxExpMain,cmbBoxExpLevel1,cmbBoxExpLevel2,cmbBoxCompanyCode,cmbBoxExpMerLecture,cmbBoxExpMerOrganizator,
	cmbBoxExpRekChain,cmbBoxExpRekProduct,cmbBoxExpPosProduct,cmbBoxClinics,cmbBoxContracter,cmbBoxContracter1,cmbBoxKeyLeader,cmbBoxTema,cmbBoxKeyLeader1;
	private JDateChooser startDate,endDate;
	private JTextField txtExpMerUcastnik,txtExpRekUsloviya,txtExpPosParyadk,txtExpPosStatus,txtComment,txtEmpty,txtEmpty1;
	public JButton btnAdd,btnDelete,btnUpdate,btnSave,btnExit;
	public JTextArea textAreaComment;
	public JFormattedTextField txtCountFormat,txtAmountFormat,txtAmount1,txtAmount2,txtAmount3,txtAmount4,txtAmount5,txtAmount6,txtAmount7;
	final DefaultTableModel model = new DefaultTableModel();
	DefaultTableModel dtm = new DefaultTableModel(0, 0);
	String header[] = new String[] { "ID","Approval_Status",
			"Company","Country","Area","Region","City",
			"First_Stage","Second_Stage",
			"Start_Date","End_Date","Total_Count","Total_Sum",
			"Conf_Food_Sum", "Org_Exps_Sum", "Travel_Agency_Sum","Key_Person_Sum", "Add_Exp_Sum", 
			"Chain","Contracter","Lecturer","Organizer","Clinic_Name","Key_Leader",
			"Product_Name","Tema","Attenders_Count","Conditions","Statuses","Comment",
			"Third Stage","Contracter1","Org_Exps_Sum1","Key_Leader1","Key_Person_Sum1","entry_date","entry_user","approve_date","approve_user"};

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
					ExpsEntryScreen window = new ExpsEntryScreen(inBag);
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
	public ExpsEntryScreen(ESIBag inBag) {
		super("Expense Entry");
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
		paramPanelMain = new JPanel(new GridLayout(2, 3, 15, 5));
		
		paramPanelAddress = new JPanel(new GridLayout(7, 2, 15, 5));
		paramPanelDates = new JPanel(new GridLayout(7, 2, 5, 15));
		paramPanelExpTypes = new JPanel(new GridLayout(7, 2, 15, 5));
		
		paramPanelExpParMer = new JPanel(new GridLayout(7, 2, 15, 5));
		paramPanelPaymentReceiver = new JPanel(new GridLayout(8, 2, 15, 5));
		paramPanelAmounts = new JPanel(new GridLayout(7, 2, 15, 5));
		
		paramPanelBtn = new JPanel(new GridLayout(1, 5, 15, 5));
		paramPanelResult = new JPanel(new GridLayout(0, 1, 15, 5));
		
		NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMAN);
	    NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
	    numberFormatter.setValueClass(Integer.class);
	    numberFormatter.setMinimum(0);
	    numberFormatter.setMaximum(Integer.MAX_VALUE);
	    numberFormatter.setAllowsInvalid(false);
	    // If you want the value to be committed on each keystroke instead of focus lost
	    numberFormatter.setCommitsOnValidEdit(true);
	    
	    doubleFormatter.setMinimumIntegerDigits(0);
	    doubleFormatter.setMaximumIntegerDigits(8);
	    
		//Table
		resultTable = new JTable(20, 8);		
		dtm.setColumnIdentifiers(header);
		resultTable.setModel(dtm);
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		// scroll pane
		jScroll = new JScrollPane(resultTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
		jScroll.setViewportView(resultTable);
		jScroll.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(jScroll, BorderLayout.CENTER);
		
		
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
		
		lblEmpty = new JLabel("");
		lblEmpty1 = new JLabel("");
		lblEmpty.setVisible(false);
		lblEmpty1.setVisible(false);
		
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
		
		cmbBoxExpMerLecture = new JComboBox( new String[]{});		
		cmbBoxExpMerLecture.setEditable(true);	
		
		cmbBoxExpMerOrganizator = new JComboBox( new String[]{});		
		cmbBoxExpMerOrganizator.setEditable(true);
		
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
		
		cmbBoxExpPosProduct = new JComboBox( new String[]{});	
		cmbBoxExpPosProduct.setEditable(true);	
		cmbBoxExpRekProduct = new JComboBox( new String[]{});	
		cmbBoxExpRekProduct.setEditable(true);
		
		if(cmbBoxCompanyCode.getSelectedItem() != null){
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
				ConnectToDb.getPRMDataGroupBy("region", "solgar_prm.prm_exps_addresses",cmbBoxRegion,"area",cmbBoxArea.getSelectedItem().toString());				
			}else if(userArea.equalsIgnoreCase("Region")){
				cmbBoxArea.addItem("Region");
				cmbBoxArea.setSelectedIndex(0);
				ConnectToDb.getPRMDataGroupBy("region", "solgar_prm.prm_exps_addresses",cmbBoxRegion,"area",cmbBoxArea.getSelectedItem().toString());
			}else if(userArea.equalsIgnoreCase("Saint Petersburg")){
				cmbBoxArea.addItem("Saint Petersburg");
				cmbBoxArea.setSelectedIndex(0);
				ConnectToDb.getPRMDataGroupBy("region", "solgar_prm.prm_exps_addresses",cmbBoxRegion,"area",cmbBoxArea.getSelectedItem().toString());
			}
			if(cmbBoxCompanyCode.getSelectedItem() != null && cmbBoxArea.getSelectedItem() != null){
  			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerLecture,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
				  "country",cmbBoxArea.getSelectedItem().toString());
  			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerOrganizator,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
  					  "country",cmbBoxArea.getSelectedItem().toString());
  			  cmbBoxExpMerLecture.setSelectedIndex(-1);
  			  cmbBoxExpMerOrganizator.setSelectedIndex(-1);
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
				ConnectToDb.getPRMDataGroupBy("region", "solgar_prm.prm_exps_addresses",cmbBoxRegion,"area",cmbBoxArea.getSelectedItem().toString());
			}
			if(cmbBoxCompanyCode.getSelectedItem() != null && cmbBoxArea.getSelectedItem() != null){
  			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerLecture,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
				  "country",cmbBoxArea.getSelectedItem().toString());
  			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerOrganizator,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
  					  "country",cmbBoxArea.getSelectedItem().toString());
  			  cmbBoxExpMerLecture.setSelectedIndex(-1);
  			  cmbBoxExpMerOrganizator.setSelectedIndex(-1);
  		  }
		}
		
		cmbBoxRegion.setEnabled(true);
		cmbBoxCity.setEnabled(true);
				
		cmbBoxExpMain = new JComboBox( new String[]{});		
		ConnectToDb.getPRMDataGroupBy("main_name", "solgar_prm.prm_exps_types",cmbBoxExpMain,"","");	
		cmbBoxExpMain.setMaximumRowCount(50);
		cmbBoxExpMain.setEditable(true);
		cmbBoxExpMain.setSelectedIndex(0);
		
		cmbBoxExpLevel1 = new JComboBox( new String[]{});		
		ConnectToDb.getPRMDataGroupBy("level1", "solgar_prm.prm_exps_types",cmbBoxExpLevel1,"main_name",cmbBoxExpMain.getSelectedItem().toString());
		cmbBoxExpLevel1.setMaximumRowCount(50);
		cmbBoxExpLevel1.setEditable(true);
		cmbBoxExpLevel1.setSelectedIndex(-1);
		
		cmbBoxExpLevel2 = new JComboBox( new String[]{});	
		cmbBoxExpLevel2.setEditable(true);	
		
		cmbBoxExpRekChain = new JComboBox( new String[]{});		
		ConnectToDb.getPRMData("group_company", "solgar_prm.prm_russia_chains",cmbBoxExpRekChain);				
		cmbBoxExpRekChain.setMaximumRowCount(50);
		cmbBoxExpRekChain.setEditable(true);
		cmbBoxExpRekChain.setSelectedIndex(-1);
		
		cmbBoxClinics = new JComboBox( new String[]{});		
		ConnectToDb.getPRMData("clinic_name", "solgar_prm.prm_exps_clinics",cmbBoxClinics);				
		cmbBoxClinics.setMaximumRowCount(50);
		cmbBoxClinics.setEditable(true);
		cmbBoxClinics.setSelectedIndex(-1);
		
		cmbBoxKeyLeader = new JComboBox( new String[]{});		
		ConnectToDb.getPRMData("leader_name", "solgar_prm.prm_exps_key_leader",cmbBoxKeyLeader);				
		cmbBoxKeyLeader.setMaximumRowCount(50);
		cmbBoxKeyLeader.setEditable(true);
		cmbBoxKeyLeader.setSelectedIndex(-1);	
		
		cmbBoxKeyLeader1 = new JComboBox( new String[]{});		
		ConnectToDb.getPRMData("leader_name", "solgar_prm.prm_exps_key_leader",cmbBoxKeyLeader1);				
		cmbBoxKeyLeader1.setMaximumRowCount(50);
		cmbBoxKeyLeader1.setEditable(true);
		cmbBoxKeyLeader1.setSelectedIndex(-1);
		
		cmbBoxContracter = new JComboBox( new String[]{});		
		ConnectToDb.getPRMData("Contracter", "solgar_prm.prm_exps_contracter",cmbBoxContracter);				
		cmbBoxContracter.setMaximumRowCount(50);
		cmbBoxContracter.setEditable(true);
		cmbBoxContracter.setSelectedIndex(-1);
		
		cmbBoxContracter1 = new JComboBox( new String[]{});		
		ConnectToDb.getPRMData("Contracter", "solgar_prm.prm_exps_contracter1",cmbBoxContracter1);				
		cmbBoxContracter1.setMaximumRowCount(50);
		cmbBoxContracter1.setEditable(true);
		cmbBoxContracter1.setSelectedIndex(-1);
		
		cmbBoxTema = new JComboBox( new String[]{});		
		ConnectToDb.getPRMData("tema", "solgar_prm.prm_exps_temas",cmbBoxTema);				
		cmbBoxTema.setMaximumRowCount(50);
		cmbBoxTema.setEditable(true);
		cmbBoxTema.setSelectedIndex(-1);
		
		//Date Field		
		Calendar cal = Calendar.getInstance();
		startDate = new JDateChooser();
		startDate.setDateFormatString("yyyy-MM-dd");		
		startDate.setDate(cal.getTime());
		
		endDate = new JDateChooser();
		endDate.setDateFormatString("yyyy-MM-dd");		
		endDate.setDate(cal.getTime());
		
		
		//Text Fields
		txtExpMerUcastnik = new JTextField();
		
		txtExpRekUsloviya = new JTextField();
		
		txtExpPosParyadk = new JTextField();
		txtExpPosStatus = new JTextField();
		txtComment = new JTextField();
		
		txtEmpty = new JTextField();
		txtEmpty1 = new JTextField();
		txtEmpty.setVisible(false);
		txtEmpty1.setVisible(false);
		
		txtCountFormat = new JFormattedTextField(numberFormatter);
		txtCountFormat.setText("");
		txtAmountFormat = new JFormattedTextField(doubleFormatter);
		txtAmountFormat.setText("0");
		
		txtAmount1 = new JFormattedTextField(doubleFormatter);
		txtAmount2 = new JFormattedTextField(doubleFormatter);
		txtAmount3 = new JFormattedTextField(doubleFormatter);
		txtAmount4 = new JFormattedTextField(doubleFormatter);
		txtAmount5 = new JFormattedTextField(doubleFormatter);
		txtAmount6 = new JFormattedTextField(doubleFormatter);
		txtAmount7 = new JFormattedTextField(doubleFormatter);
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
	
		//Buttons
		btnAdd = new JButton("Add");
		btnDelete = new JButton("Delete");
		btnUpdate = new JButton("Update");		
		btnExit = new JButton("Exit");
		btnSave = new JButton("Save");
		btnSave.setEnabled(false);
		
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
		btnSave.addActionListener(this);
		btnExit.addActionListener(this);
		
		txtAmount1.addFocusListener(this);
		txtAmount2.addFocusListener(this);
		txtAmount3.addFocusListener(this);
		txtAmount4.addFocusListener(this);
		txtAmount5.addFocusListener(this);
		txtAmount6.addFocusListener(this);
		txtAmount7.addFocusListener(this);
		
		/*pageLoad*/
		amountShownless(false);
		
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
		
		paramPanelExpTypes.add(lblEmpty);
		paramPanelExpTypes.add(txtEmpty);
		paramPanelExpTypes.add(lblEmpty1);
		paramPanelExpTypes.add(txtEmpty1);
		
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
		paramPanelBtn.add(btnSave);
		paramPanelBtn.add(btnExit);
		
		paramPanelMain.add(paramPanelAddress);		
		paramPanelMain.add(paramPanelExpTypes);
		paramPanelMain.add(paramPanelDates);
		
		paramPanelMain.add(paramPanelExpParMer);
		paramPanelMain.add(paramPanelPaymentReceiver);				
		paramPanelMain.add(paramPanelAmounts);	
		
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
				 	SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
				 	boolean amountControl = true;
				 	if(cmbBoxExpMain.getSelectedItem() != null && cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("Мероприятия")){
				 		amountControl = amountControl(txtAmountFormat.getText(),txtAmount1.getText(),txtAmount2.getText(),
							txtAmount3.getText(),txtAmount4.getText(),txtAmount5.getText(),txtAmount6.getText(),txtAmount7.getText());
				 	}
					if(amountControl){			
						model.setColumnIdentifiers(header);
						resultTable.setModel(model);						
						final Object[] row = new Object[35];
						row[0] = "0";
						row[1] = "Waiting Approve";
						row[2] = cmbBoxCompanyCode.getSelectedItem();
						row[3] = cmbBoxCountry.getSelectedItem();
						row[4] = cmbBoxArea.getSelectedItem();
						row[5] = cmbBoxRegion.getSelectedItem();
						row[6] = cmbBoxCity.getSelectedItem();
						row[7] = cmbBoxExpMain.getSelectedItem();
						row[8] = cmbBoxExpLevel1.getSelectedItem();
						row[9] = dcn.format(startDate.getDate());
						row[10] = dcn.format(endDate.getDate());
						row[11] = txtCountFormat.getText();
						row[12] = formatAmount(txtAmountFormat.getText());
						row[13] = formatAmount(txtAmount1.getText());
						row[14] = formatAmount(txtAmount2.getText());
						row[15] = formatAmount(txtAmount3.getText());					
						row[16] = formatAmount(txtAmount4.getText());
						row[17] = formatAmount(txtAmount5.getText());						
						row[18] = cmbBoxExpRekChain.getSelectedItem();
						row[19] = cmbBoxContracter.getSelectedItem();
						row[20] = cmbBoxExpMerLecture.getSelectedItem();
						row[21] = cmbBoxExpMerOrganizator.getSelectedItem();
						row[22] = cmbBoxClinics.getSelectedItem();
						row[23] = cmbBoxKeyLeader.getSelectedItem();
						row[24] = cmbBoxExpRekProduct.getSelectedItem();
						row[25] = cmbBoxTema.getSelectedItem();
						row[26] = txtExpMerUcastnik.getText();
						row[27] = txtExpRekUsloviya.getText();
						row[28] = txtExpPosStatus.getText();
						row[29] = txtComment.getText();
						row[30] = cmbBoxExpLevel2.getSelectedItem();	
						row[31] = cmbBoxContracter1.getSelectedItem();
						row[32] = formatAmount(txtAmount6.getText());
						row[33] = cmbBoxKeyLeader1.getSelectedItem();
						row[34] = formatAmount(txtAmount7.getText());
						// add row to the model
						model.addRow(row);
						btnSave.setEnabled(true);
				}else{
					JOptionPane.showMessageDialog(pnlInfoMsg, "Please check amounts expenses total amount should be equal to totatl amount", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				
			}else if (e.getActionCommand().equals("Update")) {
				SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
				int i = resultTable.getSelectedRow();
				if (i >= 0)
				{	
					boolean amountControl = true;
					if(cmbBoxExpMain.getSelectedItem() != null && cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("Мероприятия")){
						amountControl = amountControl(txtAmountFormat.getText(),txtAmount1.getText(),txtAmount2.getText(),
							txtAmount3.getText(),txtAmount4.getText(),txtAmount5.getText(),txtAmount6.getText(),txtAmount7.getText());
					}
					if(amountControl){	
						model.setValueAt(cmbBoxCompanyCode.getSelectedItem(), i, 2);
						model.setValueAt(cmbBoxCountry.getSelectedItem(), i, 3);
						model.setValueAt(cmbBoxArea.getSelectedItem(), i, 4);
						model.setValueAt(cmbBoxRegion.getSelectedItem(), i, 5);
						model.setValueAt(cmbBoxCity.getSelectedItem(), i, 6);		
						model.setValueAt(cmbBoxExpMain.getSelectedItem(), i, 7);						
						model.setValueAt(cmbBoxExpLevel1.getSelectedItem(), i, 8);
						model.setValueAt(dcn.format(startDate.getDate()), i, 9);
						model.setValueAt(dcn.format(endDate.getDate()), i, 10);
						model.setValueAt(txtCountFormat.getText(), i, 11);
						model.setValueAt(txtAmountFormat.getText(), i, 12);	
						model.setValueAt(formatAmount(txtAmount1.getText()), i, 13);
						model.setValueAt(formatAmount(txtAmount2.getText()), i, 14);
						model.setValueAt(formatAmount(txtAmount3.getText()), i, 15);
						model.setValueAt(formatAmount(txtAmount4.getText()), i, 16);
						model.setValueAt(formatAmount(txtAmount5.getText()), i, 17);
						model.setValueAt(cmbBoxExpRekChain.getSelectedItem(), i, 18);						
						model.setValueAt(cmbBoxContracter.getSelectedItem(), i, 19);
						model.setValueAt(cmbBoxExpMerLecture.getSelectedItem(), i, 20);
						model.setValueAt(cmbBoxExpMerOrganizator.getSelectedItem(), i, 21);
						model.setValueAt(cmbBoxClinics.getSelectedItem(), i, 22);
						model.setValueAt(cmbBoxKeyLeader.getSelectedItem(), i, 23);
						model.setValueAt(cmbBoxExpRekProduct.getSelectedItem(), i, 24);						
						model.setValueAt(cmbBoxTema.getSelectedItem(), i, 25);
						model.setValueAt(txtExpMerUcastnik.getText(), i, 26);
						model.setValueAt(txtExpRekUsloviya.getText(), i, 27);
						model.setValueAt(txtExpPosStatus.getText(), i, 28);
						model.setValueAt(txtComment.getText(), i, 29);
						model.setValueAt(cmbBoxExpLevel2.getSelectedItem(), i, 30);
						model.setValueAt(cmbBoxContracter1.getSelectedItem(), i, 31);
						model.setValueAt(formatAmount(txtAmount6.getText()), i, 32);	
						model.setValueAt(cmbBoxKeyLeader1.getSelectedItem(), i, 33);
						model.setValueAt(formatAmount(txtAmount7.getText()), i, 34);
					}else{
						JOptionPane.showMessageDialog(pnlInfoMsg, "Please check amounts expenses total amount should be equal to totatl amount", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}

				else {
					System.out.println("Update Error");
				}
			}else if (e.getActionCommand().equals("Delete")) {
				int i = resultTable.getSelectedRow();
				if (i >= 0) {
					model.removeRow(i);
				}
				else {
					System.out.println("Delete Error");
				}
			}else if (e.getActionCommand().equals("Save")) {				
				boolean withoutError = true;
				try{					
					ConnectToDb.saveMarktExps(resultTable,userName);				
				}catch (Exception ef) {
					JOptionPane.showMessageDialog(pnlInfoMsg, ef.getMessage(), "Error in Save", JOptionPane.ERROR_MESSAGE);
					withoutError = false;
				}
				if(withoutError){
					for( int i = model.getRowCount() - 1; i >= 0; i-- ) {
						model.removeRow(i);
					}					
					String emailText = "Dear reciepents,\n\n"+"Expense entry user\n\n" + userName;						
					SendMail.sendEmailToReceipents("hakan.kayakutlu@gmail.com","","","", "Auto mail exps entry", emailText);					
					cleanAllScreen();
					btnSave.setEnabled(false);
					JOptionPane.showMessageDialog(pnlInfoMsg, "Expenses sent to approve", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
				
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
	    		  }
	    		  
	    		  if(cmbBoxArea.getSelectedItem() != null && cmbBoxCompanyCode.getSelectedItem() != null){
		    		  cmbBoxExpMerLecture.removeAllItems();
		    		  cmbBoxExpMerOrganizator.removeAllItems();	
	    			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerLecture,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
    					  "country",cmbBoxArea.getSelectedItem().toString());
	    			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerOrganizator,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
	    					  "country",cmbBoxArea.getSelectedItem().toString());	    			  
	    		  }	    		  	    		  
	    	  }
    	  }
           
      }       

	  public void mouseClicked(MouseEvent e) {
			SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dcn1 = new SimpleDateFormat("yyyy-MM-dd");
		  	int i = resultTable.getSelectedRow();
		  	
		  	if(model.getValueAt(i, 2) != null){
		  		cmbBoxCompanyCode.setSelectedItem(model.getValueAt(i, 2).toString());
		  	}else{
		  		cmbBoxCompanyCode.setSelectedItem("");
		  	}
		  	if(model.getValueAt(i, 3) != null){
		  		cmbBoxCountry.setSelectedItem(model.getValueAt(i, 3).toString());
		  	}else{
		  		cmbBoxCountry.setSelectedItem("");
		  	}
			if(model.getValueAt(i, 4) != null){
		  		cmbBoxArea.setSelectedItem(model.getValueAt(i, 4).toString());
		  	}else{
		  		cmbBoxArea.setSelectedItem("");
		  	}
		  	if(model.getValueAt(i, 5) != null){
		  		cmbBoxRegion.setSelectedItem(model.getValueAt(i, 5).toString());
		  	}else{
		  		cmbBoxRegion.setSelectedItem("");
		  	}
		  	if(model.getValueAt(i, 6) != null){
		  		cmbBoxCity.setSelectedItem(model.getValueAt(i, 6).toString());
		  	}else{
		  		cmbBoxCity.setSelectedItem("");
		  	}
		  	if(model.getValueAt(i, 7) != null){
		  		cmbBoxExpMain.setSelectedItem(model.getValueAt(i,7).toString());
		  	}else{
		  		cmbBoxExpMain.setSelectedItem("");
		  	}
		  	if(model.getValueAt(i, 8) != null){
		  		cmbBoxExpLevel1.setSelectedItem(model.getValueAt(i, 8).toString());
		  	}else{
		  		cmbBoxExpLevel1.setSelectedItem("");
		  	}
		  	if(model.getValueAt(i, 9) != null){			
		  		try {
		  			startDate.setDate(dcn.parse(model.getValueAt(i, 9).toString()));
		  		} catch (Exception e2) {
		  			// TODO: handle exception
		  		}
		  	}else{
		  		startDate.setDateFormatString("");
		  	}
		  	if(model.getValueAt(i, 10) != null){	
		  		try{
		  			endDate.setDate(dcn1.parse(model.getValueAt(i, 10).toString()));
		  		} catch (Exception e2) {
		  			// TODO: handle exception
		  		}
		  	}else{
		  		endDate.setDateFormatString("");
		  	}
		  	if(model.getValueAt(i, 11) != null){
		  		txtCountFormat.setText(model.getValueAt(i, 11).toString());
		  	}else{
		  		txtCountFormat.setText("");
		  	}
		  	if(model.getValueAt(i, 12) != null){
		  	 txtAmountFormat.setText(model.getValueAt(i, 12).toString());
		  	}else{
		  	  txtAmountFormat.setText("");
		  	}
		  	if(model.getValueAt(i, 13) != null){
		  	 txtAmount1.setText(model.getValueAt(i, 13).toString());
		  	}else{
		  	  txtAmount1.setText("");
		  	}
		  	if(model.getValueAt(i,14) != null){
		  	 txtAmount2.setText(model.getValueAt(i, 14).toString());
		  	}else{
		  	  txtAmount2.setText("");
		  	}
		  	if(model.getValueAt(i, 15) != null){
		  	 txtAmount3.setText(model.getValueAt(i, 15).toString());
		  	}else{
		  	  txtAmount3.setText("");
		  	}
		  	if(model.getValueAt(i, 16) != null){
		  	 txtAmount4.setText(model.getValueAt(i, 16).toString());
		  	}else{
		  	 txtAmount4.setText("");
		  	}
		  	if(model.getValueAt(i, 17) != null){
		  	 txtAmount5.setText(model.getValueAt(i, 17).toString());
		  	}else{
		  	  txtAmount5.setText("");
		  	}
		  	if(model.getValueAt(i, 18) != null){
		  	  cmbBoxExpRekChain.setSelectedItem(model.getValueAt(i, 18).toString());
		  	}else{
		  	  cmbBoxExpRekChain.setSelectedItem("");
		  	}
		  	if(model.getValueAt(i, 19) != null){
		  	  cmbBoxContracter.setSelectedItem(model.getValueAt(i, 19).toString());
		  	}else{
		  	  cmbBoxContracter.setSelectedItem("");
		  	}
		  	if(model.getValueAt(i, 20) != null){
		  		cmbBoxExpMerLecture.setSelectedItem(model.getValueAt(i, 20).toString());
		  	}else{
		  		cmbBoxExpMerLecture.setSelectedItem("");
		  	}
		  	if(model.getValueAt(i, 21) != null){
		  		cmbBoxExpMerOrganizator.setSelectedItem(model.getValueAt(i, 21).toString());
		  	}else{
		  		cmbBoxExpMerOrganizator.setSelectedItem("");
		  	}
		  	if(model.getValueAt(i, 22) != null){
		  		cmbBoxClinics.setSelectedItem(model.getValueAt(i, 22).toString());
		  	}else{
		  		cmbBoxClinics.setSelectedItem("");
		  	}
		  	if(model.getValueAt(i, 23) != null){
		  		cmbBoxKeyLeader.setSelectedItem(model.getValueAt(i, 23).toString());
		  	}else{
		  		cmbBoxKeyLeader.setSelectedItem("");
		  	}
		  	 if(model.getValueAt(i, 24) != null){
		  	  cmbBoxExpRekProduct.setSelectedItem(model.getValueAt(i, 24).toString());
		  	}else{
		  	  cmbBoxExpRekProduct.setSelectedItem("");
		  	}
		  	if(model.getValueAt(i, 25) != null){
		  		cmbBoxTema.setSelectedItem(model.getValueAt(i, 25).toString());
		  	}else{
		  		cmbBoxTema.setSelectedItem("");
		  	}
		  	if(model.getValueAt(i, 26) != null){
		  		txtExpMerUcastnik.setText(model.getValueAt(i, 26).toString());
		  	}else{
		  		txtExpMerUcastnik.setText("");
		  	}
		  	if(model.getValueAt(i, 27) != null){
		  	  txtExpRekUsloviya.setText(model.getValueAt(i, 27).toString());
		  	}else{
		  	txtExpRekUsloviya.setText("");
		  	}
		  	if(model.getValueAt(i, 28) != null){
		  	  txtExpPosStatus.setText(model.getValueAt(i, 28).toString());
		  	}else{
		  	  txtExpPosStatus.setText("");
		  	}
		  	if(model.getValueAt(i, 29) != null){
		  	 txtComment.setText(model.getValueAt(i, 29).toString());
		  	}else{
		  	  txtComment.setText("");
		  	}
		  	if(model.getValueAt(i, 30) != null){
		  		cmbBoxExpLevel2.setSelectedItem(model.getValueAt(i, 30).toString());
		  	}else{
		  		cmbBoxExpLevel2.setSelectedItem("");
		  	}
			if(model.getValueAt(i, 31) != null){
		  		cmbBoxContracter1.setSelectedItem(model.getValueAt(i, 31).toString());
		  	}else{
		  		cmbBoxContracter1.setSelectedItem("");
		  	}
			if(model.getValueAt(i, 32) != null){
			  	txtAmount6.setText(model.getValueAt(i, 32).toString());
			}else{
			  	txtAmount6.setText("");
			}
			if(model.getValueAt(i, 33) != null){
		  		cmbBoxKeyLeader1.setSelectedItem(model.getValueAt(i, 33).toString());
		  	}else{
		  		cmbBoxKeyLeader1.setSelectedItem("");
		  	}
			if(model.getValueAt(i, 34) != null){
			  	txtAmount7.setText(model.getValueAt(i, 34).toString());
			}else{
			  	txtAmount7.setText("");
			}
			
			
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
		
		 private boolean amountControl(String totalAmount, String amount1, String amount2,
					String amount3, String amount4, String amount5, String amount6, String amount7) {
		    	  double totalAmountExpenses = 0;
		    	  if(amount1.indexOf(" ")>=0){amount1 = amount1.replace(" ", "");}
		    	  if(amount2.indexOf(" ")>=0){amount2 = amount2.replace(" ", "");}
		    	  if(amount3.indexOf(" ")>=0){amount3 = amount3.replace(" ", "");}
		    	  if(amount4.indexOf(" ")>=0){amount4 = amount4.replace(" ", "");}
		    	  if(amount5.indexOf(" ")>=0){amount5 = amount5.replace(" ", "");}
		    	  if(amount6.indexOf(" ")>=0){amount6 = amount6.replace(" ", "");}
		    	  if(amount7.indexOf(" ")>=0){amount7 = amount7.replace(" ", "");}
		    	  if(totalAmount.indexOf(" ")>=0){totalAmount = totalAmount.replace(" ", "");}
		    	  
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
				totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount1.getText().replace(" ",""));
			}
			if(txtAmount2.getText() != null && txtAmount2.getText().length()>0){
				totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount2.getText().replace(" ",""));
			}
			if(txtAmount3.getText() != null && txtAmount3.getText().length()>0){
				totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount3.getText().replace(" ",""));
			}
			if(txtAmount4.getText() != null && txtAmount4.getText().length()>0){
				totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount4.getText().replace(" ",""));
			}
			if(txtAmount5.getText() != null && txtAmount5.getText().length()>0){
				totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount5.getText().replace(" ",""));
			}	
			if(txtAmount6.getText() != null && txtAmount6.getText().length()>0){
				totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount6.getText().replace(" ",""));
			}
			if(txtAmount7.getText() != null && txtAmount7.getText().length()>0){
				totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount7.getText().replace(" ",""));
			}
			String txtTotAmount = String.valueOf(totalAmountExpenses);
			txtTotAmount = txtTotAmount.substring(0, txtTotAmount.indexOf("."));
			txtAmountFormat.setText(formatAmount(txtTotAmount));
		}
		private void cleanAllScreen() {
			cmbBoxCompanyCode.setSelectedItem("");
			cmbBoxCountry.setSelectedItem("");
			cmbBoxArea.setSelectedItem("");
			cmbBoxRegion.setSelectedItem("");
			cmbBoxCity.setSelectedItem("");
			cmbBoxExpMain.setSelectedItem("");
			cmbBoxExpLevel1.setSelectedItem("");
			startDate.setDateFormatString("");
			endDate.setDateFormatString("");
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
		private String formatAmount(String amount){			
			if(amount == null && amount.length()==0){
				amount = "0";
			}
			if(amount.indexOf(" ")>=0){amount = amount.replace(" ", "");}
			String tempAmount = doubleFormatter.format(Double.valueOf(amount));
			return tempAmount;
		}
}