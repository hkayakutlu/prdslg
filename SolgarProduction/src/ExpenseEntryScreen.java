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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

import cb.esi.esiclient.util.ESIBag;
import main.ConnectToDb;
import main.SendMail;

public class ExpenseEntryScreen extends JFrame implements ActionListener,ItemListener,MouseListener,FocusListener{
	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 900;

	private JFrame frame;
	private JTable tblReportResult;	
	private JLabel lblAdrCountry,lblAdrRegion,lblAdrCity,lblAdrCityRegion,lblStartDate,lblEndDate,
	lblExpFirstStage,lblExpSecondStage,lblExpThirdStage,
	lblExpMerLecture,lblExpMerOrganizator,lblExpMerTema,lblExpMerUcastnik,
    lblExpRekUsloviya,lblExpRekProduct,lblExpRekChain,
	lblExpPosProduct,lblExpPosParyadk,lblExpPosStatus,
	lblCount,lblAmount,lblComment,lblCompanyCode,
	lblAmount1,lblAmount2,lblAmount3,lblAmount4,lblAmount5;
	private JPanel paramPanelMain,paramPanelAddress,paramPanelDates,paramPanelResult,paramPanelExpTypes,paramPanelExpParMer,
	paramPanelExpParRek,paramPanelExpParPos,paramPanelBtn,paramPanelBtn1,paramPanelAmounts,pnlInfoMsg;
	private JScrollPane jScroll;
	private JTable resultTable;
	private JComboBox cmbBoxCountry,cmbBoxRegion,cmbBoxCity,cmbBoxCityRegion,
	cmbBoxExpMain,cmbBoxExpLevel1,cmbBoxExpLevel2,cmbBoxCompanyCode,cmbBoxExpMerLecture,cmbBoxExpMerOrganizator;
	private JFormattedTextField startDate,endDate;
	private JTextField txtExpMerTema,txtExpMerUcastnik,
	txtExpRekUsloviya,txtExpRekProduct,txtExpRekChain,
	txtExpPosProduct,txtExpPosParyadk,txtExpPosStatus;
	public JButton btnAdd,btnDelete,btnUpdate,btnSave,btnExit;
	public JTextArea textAreaComment;
	public JFormattedTextField txtCountFormat,txtAmountFormat,txtAmount1,txtAmount2,txtAmount3,txtAmount4,txtAmount5;
	final DefaultTableModel model = new DefaultTableModel();
	DefaultTableModel dtm = new DefaultTableModel(0, 0);
	String header[] = new String[] { "Row Number","Column1","Column2"};
	private String userName="Hakan KAYAKUTLU";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ESIBag inBag = new ESIBag();
					ExpenseEntryScreen window = new ExpenseEntryScreen(inBag);
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
	public ExpenseEntryScreen(ESIBag inBag) {
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
			}
		} catch (Exception e) {
			// simdilik yoksa yok
		}
		
		// add parameter panel
		paramPanelMain = new JPanel(new GridLayout(3, 3, 5, 5));
		paramPanelAddress = new JPanel(new GridLayout(4, 2, 5, 5));
		paramPanelDates = new JPanel(new GridLayout(4, 2, 5, 5));
		paramPanelExpTypes = new JPanel(new GridLayout(4, 4, 5, 5));
		paramPanelExpParMer = new JPanel(new GridLayout(4, 2, 5, 5));
		paramPanelExpParRek = new JPanel(new GridLayout(4, 2, 5, 5));
		paramPanelExpParPos = new JPanel(new GridLayout(4, 2, 5, 5));
		paramPanelBtn = new JPanel(new GridLayout(3, 2, 5, 5));
		paramPanelBtn1 = new JPanel(new GridLayout(2, 1, 5, 5));
		paramPanelAmounts = new JPanel(new GridLayout(5, 2, 5, 5));
		
		paramPanelResult = new JPanel(new GridLayout(0, 1, 5, 5));
		
		NumberFormat numberFormat = NumberFormat.getInstance();
	    NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
	    numberFormatter.setValueClass(Integer.class);
	    numberFormatter.setMinimum(0);
	    numberFormatter.setMaximum(Integer.MAX_VALUE);
	    numberFormatter.setAllowsInvalid(false);
	    // If you want the value to be committed on each keystroke instead of focus lost
	    numberFormatter.setCommitsOnValidEdit(true);
	    
	    DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
	    decimalFormat.setGroupingUsed(false);
	    decimalFormat.setMinimumIntegerDigits(0);
	    decimalFormat.setMaximumIntegerDigits(8);
		
		//Table
		resultTable = new JTable(20, 8);		
		dtm.setColumnIdentifiers(header);
		resultTable.setModel(dtm);
		
		// scroll pane
		jScroll = new JScrollPane(resultTable);		
		jScroll.setViewportView(resultTable);
		jScroll.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(jScroll, BorderLayout.CENTER);
		
		
		//labels
		lblAdrCountry = new JLabel("Country");		
		lblAdrRegion = new JLabel("Region");
		lblAdrCity = new JLabel("City");
		lblAdrCityRegion = new JLabel("City Region");
		
		lblStartDate = new JLabel("Start Date");
		lblEndDate = new JLabel("End Date");
		lblCount = new JLabel("Count");
		lblAmount = new JLabel("Amount");
		
		lblCompanyCode = new JLabel("Company Code");
		lblExpFirstStage = new JLabel("First Stage");
		lblExpSecondStage = new JLabel("Second Stage");
		lblExpThirdStage = new JLabel("Third Stage");
		
		lblExpMerLecture = new JLabel("Lecture");
		lblExpMerOrganizator = new JLabel("Organizator");
		lblExpMerTema = new JLabel("Tema");
		lblExpMerUcastnik = new JLabel("Attenders Count");
		
		lblExpRekUsloviya = new JLabel("Conditions");
		lblExpRekProduct = new JLabel("Product");
		lblExpRekChain = new JLabel("Chain");
		
		lblExpPosProduct = new JLabel("Product");
		lblExpPosParyadk = new JLabel("Contracter");
		lblExpPosStatus = new JLabel("Status");
		
		lblComment = new JLabel("Comment");
		
		lblAmount1 = new JLabel("���������-��� � �������");
		lblAmount2 = new JLabel("���.�����");
		lblAmount3 = new JLabel("������������ ������� � ����������");
		lblAmount4 = new JLabel("�������");
		lblAmount5 = new JLabel("������");
		
		cmbBoxCountry = new JComboBox( new String[]{});		
		/*ConnectToDb.getPRMDataGroupBy("country", "solgar_prm.prm_address_group",cmbBoxCountry,"","");	
		cmbBoxCountry.setMaximumRowCount(50);
		cmbBoxCountry.setEditable(true);
		cmbBoxCountry.setSelectedIndex(-1);*/
		
		cmbBoxRegion = new JComboBox( new String[]{});		
		cmbBoxRegion.setEditable(true);
		
		cmbBoxCity = new JComboBox( new String[]{});
		cmbBoxCity.setEditable(true);
		
		if(userName.matches("Hakan KAYAKUTLU|����� ������|������� ������ ���������|������ ����� �����")){					
			ConnectToDb.getPRMDataGroupBy("country", "solgar_prm.prm_address_group",cmbBoxCountry,"","");	
			cmbBoxCountry.setMaximumRowCount(50);
			cmbBoxCountry.setEditable(true);
			cmbBoxCountry.setSelectedIndex(-1);
		}else if(userName.matches("�������� ������� ����������")){
			cmbBoxCountry.addItem("Moscow");
			cmbBoxCountry.setEnabled(false);
			cmbBoxCountry.setEditable(false);
			ConnectToDb.getPRMDataGroupBy("region", "solgar_prm.prm_address_group",cmbBoxRegion,"country",cmbBoxCountry.getSelectedItem().toString());
			cmbBoxRegion.setSelectedIndex(0);
			ConnectToDb.getPRMDataGroupBy("city", "solgar_prm.prm_address_group",cmbBoxCity,"region",cmbBoxRegion.getSelectedItem().toString());
			cmbBoxCity.setSelectedIndex(-1);
		}else if(userName.matches("������� ������ ��������")){
			cmbBoxCountry.addItem("Saint Petersburg");
			cmbBoxCountry.setEnabled(false);
			cmbBoxCountry.setEditable(false);
			ConnectToDb.getPRMDataGroupBy("region", "solgar_prm.prm_address_group",cmbBoxRegion,"country",cmbBoxCountry.getSelectedItem().toString());
			cmbBoxRegion.setSelectedIndex(0);
			ConnectToDb.getPRMDataGroupBy("city", "solgar_prm.prm_address_group",cmbBoxCity,"region",cmbBoxRegion.getSelectedItem().toString());
			cmbBoxCity.setSelectedIndex(-1);
		}else if(userName.matches("������� ����� ���������")){
			cmbBoxCountry.addItem("Region");
			cmbBoxCountry.setEnabled(false);
			cmbBoxCountry.setEditable(false);
			ConnectToDb.getPRMDataGroupBy("region", "solgar_prm.prm_address_group",cmbBoxRegion,"country",cmbBoxCountry.getSelectedItem().toString());
			cmbBoxRegion.setSelectedIndex(0);
			ConnectToDb.getPRMDataGroupBy("city", "solgar_prm.prm_address_group",cmbBoxCity,"region",cmbBoxRegion.getSelectedItem().toString());
			cmbBoxCity.setSelectedIndex(-1);
		}else if(userName.matches("Ekateryna Shevtsova")){
			cmbBoxCountry.addItem("Ukraine");
			cmbBoxCountry.setEnabled(false);
			cmbBoxCountry.setEditable(false);
			ConnectToDb.getPRMDataGroupBy("region", "solgar_prm.prm_address_group",cmbBoxRegion,"country",cmbBoxCountry.getSelectedItem().toString());
			cmbBoxRegion.setSelectedIndex(0);
			ConnectToDb.getPRMDataGroupBy("city", "solgar_prm.prm_address_group",cmbBoxCity,"region",cmbBoxRegion.getSelectedItem().toString());
			cmbBoxCity.setSelectedIndex(-1);
		}else{
			cmbBoxCountry.addItem("No Authorization");
			cmbBoxCountry.setEnabled(false);
			cmbBoxCountry.setEditable(false);
		}
		
		cmbBoxCityRegion = new JComboBox( new String[]{});		
		cmbBoxCityRegion.setEditable(true);
		
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
		//ConnectToDb.getPRMDataGroupBy("level2", "solgar_prm.prm_exps_types",cmbBoxExpLevel2,"level1",cmbBoxExpLevel1.getSelectedItem().toString());		
		//cmbBoxExpLevel2.setMaximumRowCount(50);
		cmbBoxExpLevel2.setEditable(true);
		//cmbBoxExpLevel2.setSelectedIndex(-1);
		
		cmbBoxCompanyCode = new JComboBox( new String[]{});		
		cmbBoxCompanyCode.addItem("SOLGAR");
		cmbBoxCompanyCode.addItem("NATURES BOUNTY");
		cmbBoxCompanyCode.setEditable(true);
		cmbBoxCompanyCode.setSelectedIndex(0);
		
		
		cmbBoxExpMerLecture = new JComboBox( new String[]{});		
		cmbBoxExpMerLecture.setEditable(true);	
		
		cmbBoxExpMerOrganizator = new JComboBox( new String[]{});		
		cmbBoxExpMerOrganizator.setEditable(true);
		
		//Date Field
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		startDate = new JFormattedTextField(df);
		startDate.setValue(new java.util.Date());
		endDate = new JFormattedTextField(df);
		endDate.setValue(new java.util.Date());
		
		//Text Fields
		txtExpMerTema = new JTextField();
		txtExpMerUcastnik = new JTextField();
		
		txtExpRekUsloviya = new JTextField();
		txtExpRekProduct = new JTextField();
		txtExpRekChain = new JTextField();
		
		txtExpPosProduct = new JTextField();
		txtExpPosParyadk = new JTextField();
		txtExpPosStatus = new JTextField();
		
		txtCountFormat = new JFormattedTextField(numberFormatter);
		txtCountFormat.setText("0");
		txtAmountFormat = new JFormattedTextField(decimalFormat);
		txtAmountFormat.setText("0");
		
		txtAmount1 = new JFormattedTextField(decimalFormat);
		txtAmount2 = new JFormattedTextField(decimalFormat);
		txtAmount3 = new JFormattedTextField(decimalFormat);
		txtAmount4 = new JFormattedTextField(decimalFormat);
		txtAmount5 = new JFormattedTextField(decimalFormat);
		txtAmount1.setText("0");
		txtAmount2.setText("0");
		txtAmount3.setText("0");
		txtAmount4.setText("0");
		txtAmount5.setText("0");
		txtAmount1.setName("amount1");
		txtAmount2.setName("amount2");
		txtAmount3.setName("amount3");
		txtAmount4.setName("amount4");
		txtAmount5.setName("amount5");
		
		//Buttons
		btnAdd = new JButton("Add");
		btnDelete = new JButton("Delete");
		btnUpdate = new JButton("Update");		
		btnExit = new JButton("Exit");
		btnSave = new JButton("Save");
		btnSave.setEnabled(false);
		
		cmbBoxCountry.setName("Country");
		cmbBoxRegion.setName("Region");
		cmbBoxCity.setName("City");
		cmbBoxCityRegion.setName("CityRegion");
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
		cmbBoxRegion.addItemListener(this);
		cmbBoxCity.addItemListener(this);
		cmbBoxCityRegion.addItemListener(this);
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

		//address parameters
		paramPanelAddress.add(lblAdrCountry);
		paramPanelAddress.add(cmbBoxCountry);
		paramPanelAddress.add(lblAdrRegion);
		paramPanelAddress.add(cmbBoxRegion);
		paramPanelAddress.add(lblAdrCity);
		paramPanelAddress.add(cmbBoxCity);
		paramPanelAddress.add(lblAdrCityRegion);
		paramPanelAddress.add(cmbBoxCityRegion);

		paramPanelAddress.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanelAddress, BorderLayout.NORTH);
		
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
		paramPanelExpTypes.add(lblCompanyCode);
		paramPanelExpTypes.add(cmbBoxCompanyCode);
		paramPanelExpTypes.add(lblExpFirstStage);
		paramPanelExpTypes.add(cmbBoxExpMain);
		paramPanelExpTypes.add(lblExpSecondStage);
		paramPanelExpTypes.add(cmbBoxExpLevel1);	
		paramPanelExpTypes.add(lblExpThirdStage);
		paramPanelExpTypes.add(cmbBoxExpLevel2);	
		paramPanelExpTypes.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanelExpTypes, BorderLayout.NORTH);
		
		//expenses parametric parameters
		paramPanelExpParMer.add(lblExpMerLecture);
		paramPanelExpParMer.add(cmbBoxExpMerLecture);
		paramPanelExpParMer.add(lblExpMerOrganizator);
		paramPanelExpParMer.add(cmbBoxExpMerOrganizator);
		paramPanelExpParMer.add(lblExpMerTema);
		paramPanelExpParMer.add(txtExpMerTema);
		paramPanelExpParMer.add(lblExpMerUcastnik);
		paramPanelExpParMer.add(txtExpMerUcastnik);
		paramPanelExpParMer.setVisible(false);
		
		paramPanelExpParRek.add(lblExpRekUsloviya );
		paramPanelExpParRek.add(txtExpRekUsloviya);
		paramPanelExpParRek.add(lblExpRekProduct);
		paramPanelExpParRek.add(txtExpRekProduct);
		paramPanelExpParRek.add(lblExpRekChain);
		paramPanelExpParRek.add(txtExpRekChain);
		paramPanelExpParRek.setVisible(false);
		
		paramPanelExpParPos.add(lblExpPosProduct );
		paramPanelExpParPos.add(txtExpPosProduct);
		paramPanelExpParPos.add(lblExpPosParyadk);
		paramPanelExpParPos.add(txtExpPosParyadk);
		paramPanelExpParPos.add(lblExpPosStatus);
		paramPanelExpParPos.add(txtExpPosStatus);
		//paramPanelExpParPos.setVisible(false);
		
		//Button group
		paramPanelBtn1.add(lblComment);
		paramPanelBtn1.add(scrollPaneTxtArea);
		
		
		paramPanelAmounts.add(lblAmount1 );
		paramPanelAmounts.add(txtAmount1);
		paramPanelAmounts.add(lblAmount2);
		paramPanelAmounts.add(txtAmount2);
		paramPanelAmounts.add(lblAmount3);
		paramPanelAmounts.add(txtAmount3);
		paramPanelAmounts.add(lblAmount4);
		paramPanelAmounts.add(txtAmount4);
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
		paramPanelMain.add(paramPanelExpParRek);
		paramPanelMain.add(paramPanelExpParPos);
		paramPanelMain.add(paramPanelBtn1);
		paramPanelMain.add(paramPanelAmounts);
		paramPanelMain.add(paramPanelBtn);
	
		
		paramPanelMain.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanelMain, BorderLayout.NORTH);		
		
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
			if (e.getActionCommand().equals("Generate Report")) {				
			}else if (e.getActionCommand().equals("Add")) {
				
					boolean amountControl = amountControl(txtAmountFormat.getText(),txtAmount1.getText(),txtAmount2.getText(),
							txtAmount3.getText(),txtAmount4.getText(),txtAmount5.getText());
					if(amountControl){
					Object[] columns = { "Id", "approve_status","Company Code", "Country", "Region","City", "City_Region", "Start_Date", "End_Date","First_Stage", "Second_Stage", 
							"Third_Stage", "Exp1","Exp2", "Exp3", "Exp4", "Exp_Count", "Exp_Amount","comments","Amount1","Amount2","Amount3","Amount4","Amount5"};				
					model.setColumnIdentifiers(columns);
					resultTable.setModel(model);
					
					final Object[] row = new Object[24];
					row[0] = "0";
					row[1] = "Waiting Approve";
					row[2] = cmbBoxCompanyCode.getSelectedItem();
					row[3] = cmbBoxCountry.getSelectedItem();
					row[4] = cmbBoxRegion.getSelectedItem();
					row[5] = cmbBoxCity.getSelectedItem();
					row[6] = cmbBoxCityRegion.getSelectedItem();
					row[7] = startDate.getText();
					row[8] = endDate.getText();
					row[9] = cmbBoxExpMain.getSelectedItem();
					row[10] = cmbBoxExpLevel1.getSelectedItem();
					row[11] = cmbBoxExpLevel2.getSelectedItem();
					
					if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("�����������")){
						row[12] = cmbBoxExpMerLecture.getSelectedItem();
						row[13] = cmbBoxExpMerOrganizator.getSelectedItem();
						row[14] = txtExpMerTema.getText();
						row[15] = txtExpMerUcastnik.getText();
		    		  }else if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("�������")){
		    			row[12] = txtExpRekUsloviya.getText();
						row[13] = txtExpRekProduct.getText();
						row[14] = txtExpRekChain.getText();
						row[15] = "";	    		
		    		  }else if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("POS-���������")){
		    			row[12] = txtExpPosProduct.getText();
						row[13] = txtExpPosParyadk.getText();
						row[14] = txtExpPosStatus.getText();
						row[15] = "";	    			  
		    		  }
					
					
					row[16] = txtCountFormat.getText();
					row[17] = txtAmountFormat.getText();
					row[18] = textAreaComment.getText();	
					
					row[19] = txtAmount1.getText();
					row[20] = txtAmount2.getText();
					row[21] = txtAmount3.getText();
					row[22] = txtAmount4.getText();
					row[23] = txtAmount5.getText();
					
					// add row to the model
					model.addRow(row);
					btnSave.setEnabled(true);
				}else{
					JOptionPane.showMessageDialog(pnlInfoMsg, "Please check amounts expenses total amount should be equal to totatl amount", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				
			}else if (e.getActionCommand().equals("Update")) {
				int i = resultTable.getSelectedRow();
				if (i >= 0)
				{				
					boolean amountControl = amountControl(txtAmountFormat.getText(),txtAmount1.getText(),txtAmount2.getText(),
							txtAmount3.getText(),txtAmount4.getText(),txtAmount5.getText());
					if(amountControl){
						model.setValueAt(cmbBoxCompanyCode.getSelectedItem(), i, 2);
						model.setValueAt(cmbBoxCountry.getSelectedItem(), i, 3);
						model.setValueAt(cmbBoxRegion.getSelectedItem(), i, 4);
						model.setValueAt(cmbBoxCity.getSelectedItem(), i, 5);
						model.setValueAt(cmbBoxCityRegion.getSelectedItem(), i, 6);
						model.setValueAt(startDate.getText(), i, 7);
						model.setValueAt(endDate.getText(), i, 8);
						model.setValueAt(cmbBoxExpMain.getSelectedItem(), i, 9);
						model.setValueAt(cmbBoxExpLevel1.getSelectedItem(), i, 10);
						model.setValueAt(cmbBoxExpLevel2.getSelectedItem(), i, 11);
						
						if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("�����������")){
							model.setValueAt(cmbBoxExpMerLecture.getSelectedItem(), i, 12);
							model.setValueAt(cmbBoxExpMerOrganizator.getSelectedItem(), i, 13);
							model.setValueAt(txtExpMerTema.getText(), i, 14);
							model.setValueAt(txtExpMerUcastnik.getText(), i, 15);
			    		  }else if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("�������")){	 
							model.setValueAt(txtExpRekUsloviya.getText(), i, 12);
							model.setValueAt(txtExpRekProduct.getText(), i, 13);
							model.setValueAt(txtExpRekChain.getText(), i, 14);
							model.setValueAt("", i, 15);
			    		  }else if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("POS-���������")){   
							model.setValueAt(txtExpPosProduct.getText(), i, 12);
							model.setValueAt(txtExpPosParyadk.getText(), i, 13);
							model.setValueAt(txtExpPosStatus.getText(), i, 14);
							model.setValueAt("", i, 15);
			    		  }
						model.setValueAt(txtCountFormat.getText(), i, 16);
						model.setValueAt(txtAmountFormat.getText(), i, 17);
						model.setValueAt(textAreaComment.getText(), i, 18);	
						model.setValueAt(txtAmount1.getText(), i, 19);
						model.setValueAt(txtAmount2.getText(), i, 20);
						model.setValueAt(txtAmount3.getText(), i, 21);
						model.setValueAt(txtAmount4.getText(), i, 22);
						model.setValueAt(txtAmount5.getText(), i, 23);
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
				
				ESIBag outBag = ConnectToDb.saveMarketingExpenses(resultTable,userName);	
				
				String expenseId = "";
				for (int i = 0; i < outBag.getSize("TABLE"); i++){
					expenseId = expenseId +","+outBag.get("TABLE",i,"ID");
				}
				//String emailText = "Dear reciepents,\n\n"+"Please approve below id's expenses\n\n" + expenseId;						
				//SendMail.sendEmailToReceipents("hakan.kayakutlu@gmail.com","hgokmen@solgarvitamin.ru","mkamaeva@solgarvitamin.ru", "Auto mail approve expense", emailText);

				for( int i = model.getRowCount() - 1; i >= 0; i-- ) {
					model.removeRow(i);
				}	
				btnSave.setEnabled(false);
				JOptionPane.showMessageDialog(pnlInfoMsg, "Expenses sent to approve", "Information", JOptionPane.INFORMATION_MESSAGE);			
				
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
	    		  cmbBoxRegion.removeAllItems();
	    		  cmbBoxCity.removeAllItems();
	    		  cmbBoxCityRegion.removeAllItems();
	    		  ConnectToDb.getPRMDataGroupBy("region", "solgar_prm.prm_address_group",cmbBoxRegion,"country",cmbBoxCountry.getSelectedItem().toString());
	    		  cmbBoxExpMerLecture.removeAllItems();
	    		  cmbBoxExpMerOrganizator.removeAllItems();
	    		  if(cmbBoxCompanyCode.getSelectedItem() != null){
	    			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerLecture,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
    					  "country",cmbBoxCountry.getSelectedItem().toString());
	    			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerOrganizator,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
	    					  "country",cmbBoxCountry.getSelectedItem().toString());
	    		  }
	    		  cmbBoxCity.setSelectedIndex(-1);
	    		  cmbBoxCityRegion.setSelectedIndex(-1);	    		  
	    	  }else if(name.equalsIgnoreCase("Region")){
	    		  cmbBoxCity.removeAllItems();
	    		  cmbBoxCityRegion.removeAllItems();
	    		  ConnectToDb.getPRMDataGroupBy("city", "solgar_prm.prm_address_group",cmbBoxCity,"region",cmbBoxRegion.getSelectedItem().toString());
	    		  cmbBoxCity.setSelectedIndex(-1);
	    		  cmbBoxCityRegion.setSelectedIndex(-1);
	    	  }else if(name.equalsIgnoreCase("City")){
	    		  cmbBoxCityRegion.removeAllItems();
	    		  ConnectToDb.getPRMDataGroupBy("city_region", "solgar_prm.prm_address_group",cmbBoxCityRegion,"city",cmbBoxCity.getSelectedItem().toString());
	    		  cmbBoxCityRegion.setSelectedIndex(-1);
	    	  }else if(name.equalsIgnoreCase("CityRegion")){
	    		 //enson
	    	  }else if(name.equalsIgnoreCase("ExpMain")){
	    		  if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("�����������")){
	    			  paramPanelExpParMer.setVisible(true);
	    			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerLecture,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
	    					  "country",cmbBoxCountry.getSelectedItem().toString());
	    			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerOrganizator,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
	    					  "country",cmbBoxCountry.getSelectedItem().toString());
	    			  paramPanelExpParRek.setVisible(false);
	    			  paramPanelExpParPos.setVisible(false);
	    		  }else if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("�������")){
	    			  paramPanelExpParMer.setVisible(false);
	    			  paramPanelExpParRek.setVisible(true);
	    			  paramPanelExpParPos.setVisible(false);
	    		  }else if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("POS-���������")){
	    			  paramPanelExpParMer.setVisible(false);
	    			  paramPanelExpParRek.setVisible(false);
	    			  paramPanelExpParPos.setVisible(true);
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
	    		  cmbBoxExpMerLecture.removeAllItems();
	    		  cmbBoxExpMerOrganizator.removeAllItems();
	    		  if(cmbBoxCountry.getSelectedItem() != null){
	    			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerLecture,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
    					  "country",cmbBoxCountry.getSelectedItem().toString());
	    			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerOrganizator,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
	    					  "country",cmbBoxCountry.getSelectedItem().toString());
	    		  }	    		  	    		  
	    	  }
    	  }
           
      }   
      

	  public void mouseClicked(MouseEvent e) {
		  	int i = resultTable.getSelectedRow();
		  	cmbBoxCountry.setSelectedItem(model.getValueAt(i, 3).toString());
			cmbBoxRegion.setSelectedItem(model.getValueAt(i, 4).toString());
			
			if(model.getValueAt(i, 5) != null){
				cmbBoxCity.setSelectedItem(model.getValueAt(i, 5).toString());
			}else{
				cmbBoxCity.setSelectedItem("");
			}
			if(model.getValueAt(i, 6) != null){
				cmbBoxCityRegion.setSelectedItem(model.getValueAt(i, 6).toString());
			}else{
				cmbBoxCityRegion.setSelectedItem("");
			}
			
			if(model.getValueAt(i, 7) != null){
				startDate.setText(model.getValueAt(i, 7).toString());
			}else{
				startDate.setText("");
			}
			if(model.getValueAt(i, 8) != null){
				endDate.setText(model.getValueAt(i, 8).toString());
			}else{
				endDate.setText("");
			}
			
			cmbBoxExpMain.setSelectedItem(model.getValueAt(i, 9).toString());
			cmbBoxExpLevel1.setSelectedItem(model.getValueAt(i, 10).toString());
			if(model.getValueAt(i, 11) != null){
				cmbBoxExpLevel2.setSelectedItem(model.getValueAt(i, 11).toString());
			}else{
				cmbBoxExpLevel2.setSelectedItem("");
			}
			
			 if(model.getValueAt(i, 9).toString().equalsIgnoreCase("�����������")){
				if(model.getValueAt(i, 12) != null){
					cmbBoxExpMerLecture.setSelectedItem(model.getValueAt(i, 12).toString());
				}else{
					cmbBoxExpMerLecture.setSelectedItem("");
				}
				if(model.getValueAt(i, 13) != null){
					cmbBoxExpMerOrganizator.setSelectedItem(model.getValueAt(i, 13).toString());
				}else{
					cmbBoxExpMerOrganizator.setSelectedItem("");
				}
				if(model.getValueAt(i, 14) != null){
					txtExpMerTema.setText(model.getValueAt(i, 14).toString());
				}else{
					txtExpMerTema.setText("");
				}
				if(model.getValueAt(i, 15) != null){
					txtExpMerUcastnik.setText(model.getValueAt(i, 15).toString());
				}else{
					txtExpMerUcastnik.setText("");
				}				
    		  }else if(model.getValueAt(i, 9).toString().equalsIgnoreCase("�������")){  
				  if(model.getValueAt(i, 12) != null){
					  txtExpRekUsloviya.setText(model.getValueAt(i, 12).toString());
				  }else{
					txtExpRekUsloviya.setText("");
				  }
				  if(model.getValueAt(i, 13) != null){
					txtExpRekProduct.setText(model.getValueAt(i, 13).toString());
				  }else{
					txtExpRekProduct.setText("");
				  }
				  if(model.getValueAt(i, 14) != null){
					txtExpRekChain.setText(model.getValueAt(i, 14).toString());
				  }else{
					txtExpRekChain.setText("");
				  }	
    		  }else if(model.getValueAt(i, 9).toString().equalsIgnoreCase("POS-���������")){
    			  if(model.getValueAt(i, 12) != null){
    				  txtExpPosProduct.setText(model.getValueAt(i, 12).toString());
				  }else{
					  txtExpPosProduct.setText("");
				  }
				  if(model.getValueAt(i, 13) != null){
					  txtExpPosParyadk.setText(model.getValueAt(i, 13).toString());
				  }else{
					  txtExpPosParyadk.setText("");
				  }
				  if(model.getValueAt(i, 14) != null){
					  txtExpPosStatus.setText(model.getValueAt(i, 14).toString());
				  }else{
					  txtExpPosStatus.setText("");
				  }
				
    		  }
			 if(model.getValueAt(i, 16) != null){
				 txtCountFormat.setText(model.getValueAt(i, 16).toString());
			  }else{
				  txtCountFormat.setText("");
			  }
			 if(model.getValueAt(i, 17) != null){
				 txtAmountFormat.setText(model.getValueAt(i, 17).toString());
			  }else{
				  txtAmountFormat.setText("");
			  }
			 if(model.getValueAt(i, 18) != null){
				 textAreaComment.setText(model.getValueAt(i, 18).toString());
			  }else{
				  textAreaComment.setText("");
			  }	
			 
			 
			 if(model.getValueAt(i, 19) != null){
				 txtAmount1.setText(model.getValueAt(i, 19).toString());
			  }else{
				  txtAmount1.setText("");
			  }
			 if(model.getValueAt(i, 20) != null){
				 txtAmount2.setText(model.getValueAt(i, 20).toString());
			  }else{
				  txtAmount2.setText("");
			  }
			 if(model.getValueAt(i, 21) != null){
				 txtAmount3.setText(model.getValueAt(i, 21).toString());
			  }else{
				  txtAmount3.setText("");
			  }
			 if(model.getValueAt(i, 22) != null){
				 txtAmount4.setText(model.getValueAt(i, 22).toString());
			  }else{
				 txtAmount4.setText("");
			  }
			 if(model.getValueAt(i, 23) != null){
				 txtAmount5.setText(model.getValueAt(i, 23).toString());
			  }else{
				  txtAmount5.setText("");
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
					String amount3, String amount4, String amount5) {
		    	  double totalAmountExpenses = 0;
		    	  
		    	  if((amount1.length() > 0 && !amount1.equalsIgnoreCase("0"))||
		    			  amount2.length() > 0 && !amount2.equalsIgnoreCase("0")||
		    			  amount3.length() > 0 && !amount3.equalsIgnoreCase("0")||
		    			  amount4.length() > 0 && !amount4.equalsIgnoreCase("0")||
		    			  amount5.length() > 0 && !amount5.equalsIgnoreCase("0")){
		    		  totalAmountExpenses = Double.parseDouble(amount1)+Double.parseDouble(amount2)+Double.parseDouble(amount3)+
		    				  Double.parseDouble(amount4)+Double.parseDouble(amount5);
		    		  if(Double.parseDouble(totalAmount) != totalAmountExpenses){
		    			  return false;
		    		  }else{
		    			  return true;
		    		  }
		    	  }else{
		    		  return true;
		    	  }	  
		    	  
				
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
			}
			
		}
		
		private void sumOfExpensens() {
			double totalAmountExpenses = 0;
			if(txtAmount1.getText() != null && txtAmount1.getText().length()>0){
				totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount1.getText());
			}
			if(txtAmount2.getText() != null && txtAmount2.getText().length()>0){
				totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount2.getText());
			}
			if(txtAmount3.getText() != null && txtAmount3.getText().length()>0){
				totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount3.getText());
			}
			if(txtAmount4.getText() != null && txtAmount4.getText().length()>0){
				totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount4.getText());
			}
			if(txtAmount5.getText() != null && txtAmount5.getText().length()>0){
				totalAmountExpenses = totalAmountExpenses+Double.parseDouble(txtAmount5.getText());
			}		
			 txtAmountFormat.setText(String.valueOf(totalAmountExpenses));
		}
	
	
}