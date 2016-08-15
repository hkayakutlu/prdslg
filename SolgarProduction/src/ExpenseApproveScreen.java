package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;

import cb.esi.esiclient.util.ESIBag;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.ConnectToDb;
import main.SendMail;

public class ExpenseApproveScreen extends JFrame implements ActionListener,ItemListener,MouseListener{
	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 900;
	
	private String userName="Hakan KAYAKUTLU";

	private JFrame frame;
	private JTable tblReportResult;	
	private JLabel lblAdrCountry,lblAdrRegion,lblAdrCity,lblAdrCityRegion,lblStartDate,lblEndDate,
	lblExpFirstStage,lblExpSecondStage,lblExpThirdStage,
	lblExpMerLecture,lblExpMerOrganizator,lblExpMerTema,lblExpMerUcastnik,
    lblExpRekUsloviya,lblExpRekProduct,lblExpRekChain,
	lblExpPosProduct,lblExpPosParyadk,lblExpPosStatus,
	lblCount,lblAmount,lblComment,lblId,lblCompanyCode,
	lblAmount1,lblAmount2,lblAmount3,lblAmount4,lblAmount5;
	private JPanel paramPanelMain,paramPanelAddress,paramPanelDates,paramPanelResult,paramPanelExpTypes,paramPanelExpParMer,
	paramPanelExpParRek,paramPanelExpParPos,paramPanelBtn,paramPanelBtn1,paramPanelAmounts,pnlInfoMsg;
	private JScrollPane jScroll;
	private JTable resultTable;
	private JComboBox cmbBoxCountry,cmbBoxRegion,cmbBoxCity,cmbBoxCityRegion,
	cmbBoxExpMain,cmbBoxExpLevel1,cmbBoxExpLevel2,cmbBoxCompanyCode;
	private JFormattedTextField startDate,endDate;
	private JTextField txtExpMerLecture,txtExpMerOrganizator,txtExpMerTema,txtExpMerUcastnik,
	txtExpRekUsloviya,txtExpRekProduct,txtExpRekChain,
	txtExpPosProduct,txtExpPosParyadk,txtExpPosStatus,txtId;
	public JButton btnApprove,btnReject,btnUpdate,btnSave,btnExit;
	public JTextArea textAreaComment;
	public JFormattedTextField txtCountFormat,txtAmountFormat,txtAmount1,txtAmount2,txtAmount3,txtAmount4,txtAmount5;
	//final DefaultTableModel model = new DefaultTableModel();
	//DefaultTableModel dtm = new DefaultTableModel(0, 0);
	DefaultTableModel model = new DefaultTableModel(null, new String [] {"id","approve_status","company_code","entry_date","entry_user","approve_date",
			"approve_user","Country","Region","City","City_Region","Start_Date","End_Date","First_Stage","Second_Stage",
			"Third_Stage","Exp1","Exp2","Exp3","Exp4","Exp_Count","Exp_Amount","comments","amount1","amount2","amount3","amount4","amount5","selecTable"}) {
        public Class getColumnClass(int c) {
          switch (c) {
            case 28: return Boolean.class;
            default: return String.class;
          }   
        } };
	//String header[] = new String[] { "Row Number","Column1","Column2"};
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ESIBag inBag = new ESIBag();
					ExpenseApproveScreen window = new ExpenseApproveScreen(inBag);
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
	public ExpenseApproveScreen(ESIBag inBag) {
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
		
		//Table
		resultTable = new JTable(model);		
		//dtm.setColumnIdentifiers(header);
		//resultTable.setModel(dtm);
		
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
		lblId = new JLabel("Id Record");
		
		lblExpPosProduct = new JLabel("Product");
		lblExpPosParyadk = new JLabel("Contracter");
		lblExpPosStatus = new JLabel("Status");
		
		lblComment = new JLabel("Comment");
		
		lblAmount1 = new JLabel("Amount1");
		lblAmount2 = new JLabel("Amount2");
		lblAmount3 = new JLabel("Amount3");
		lblAmount4 = new JLabel("Amount4");
		lblAmount5 = new JLabel("Amount5");
		
		cmbBoxCountry = new JComboBox( new String[]{});		
		ConnectToDb.getPRMDataGroupBy("country", "solgar_prm.prm_address_group",cmbBoxCountry,"","");	
		cmbBoxCountry.setMaximumRowCount(50);
		cmbBoxCountry.setEnabled(false);
		
		cmbBoxRegion = new JComboBox( new String[]{});		
		cmbBoxRegion.setEnabled(false);
		
		
		cmbBoxCity = new JComboBox( new String[]{});		
		cmbBoxCity.setEnabled(false);
		
		cmbBoxCityRegion = new JComboBox( new String[]{});		
		cmbBoxCityRegion.setEnabled(false);
		
		cmbBoxExpMain = new JComboBox( new String[]{});		
		ConnectToDb.getPRMDataGroupBy("main_name", "solgar_prm.prm_exps_types",cmbBoxExpMain,"","");	
		cmbBoxExpMain.setMaximumRowCount(50);
		cmbBoxExpMain.setEnabled(false);
		
		cmbBoxExpLevel1 = new JComboBox( new String[]{});		
		cmbBoxExpLevel1.setEnabled(false);
		
		cmbBoxExpLevel2 = new JComboBox( new String[]{});		
		cmbBoxExpLevel2.setEnabled(false);
		
		cmbBoxCompanyCode = new JComboBox( new String[]{});	
		cmbBoxCompanyCode.addItem("SOLGAR");
		cmbBoxCompanyCode.addItem("NATURES BOUNTY");
		cmbBoxCompanyCode.setSelectedIndex(-1);
		cmbBoxCompanyCode.setEnabled(false);
		
		//Date Field
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		startDate = new JFormattedTextField(df);
		startDate.setValue(new java.util.Date());
		startDate.setEnabled(false);
		endDate = new JFormattedTextField(df);
		endDate.setValue(new java.util.Date());
		endDate.setEnabled(false);
		
		//Text Fields
		txtExpMerLecture = new JTextField();
		txtExpMerLecture.setEnabled(false);
		
		txtExpMerOrganizator = new JTextField();
		txtExpMerOrganizator.setEnabled(false);
		
		txtExpMerTema = new JTextField();
		txtExpMerTema.setEnabled(false);
		
		txtExpMerUcastnik = new JTextField();
		txtExpMerUcastnik.setEnabled(false);
		
		txtExpRekUsloviya = new JTextField();
		txtExpRekUsloviya.setEnabled(false);
		
		txtExpRekProduct = new JTextField();
		txtExpRekProduct.setEnabled(false);
		
		txtExpRekChain = new JTextField();
		txtExpRekChain.setEnabled(false);
		
		txtId = new JTextField();
		txtId.setEnabled(false);
		
		txtExpPosProduct = new JTextField();
		txtExpPosProduct.setEnabled(false);
		
		txtExpPosParyadk = new JTextField();
		txtExpPosParyadk.setEnabled(false);
		
		txtExpPosStatus = new JTextField();
		txtExpPosStatus.setEnabled(false);
		
		txtCountFormat = new JFormattedTextField(numberFormatter);
		txtCountFormat.setEnabled(false);
		
		txtAmountFormat = new JFormattedTextField(decimalFormat);
		txtAmountFormat.setEnabled(false);
		
		txtAmount1 = new JFormattedTextField(decimalFormat);
		txtAmount1.setEnabled(false);
		txtAmount2 = new JFormattedTextField(decimalFormat);
		txtAmount2.setEnabled(false);
		txtAmount3 = new JFormattedTextField(decimalFormat);
		txtAmount3.setEnabled(false);
		txtAmount4 = new JFormattedTextField(decimalFormat);
		txtAmount4.setEnabled(false);
		txtAmount5 = new JFormattedTextField(decimalFormat);
		txtAmount5.setEnabled(false);
		
		//Buttons
		btnApprove = new JButton("Approve");
		btnApprove.setEnabled(false);
		btnReject = new JButton("Reject");
		btnReject.setEnabled(false);
		
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
		
		//Tetx Area
		textAreaComment = new JTextArea(2, 5);
		textAreaComment.setLineWrap(true);
		JScrollPane scrollPaneTxtArea = new JScrollPane(textAreaComment,
	            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
		textAreaComment.setEditable(false);

		//Listener
		resultTable.addMouseListener(this);
		
		cmbBoxCountry.addItemListener(this);
		cmbBoxRegion.addItemListener(this);
		cmbBoxCity.addItemListener(this);
		cmbBoxCityRegion.addItemListener(this);
		cmbBoxExpMain.addItemListener(this);
		cmbBoxExpLevel1.addItemListener(this);
		cmbBoxExpLevel2.addItemListener(this);
		
		btnApprove.addActionListener(this);	
		btnReject.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnSave.addActionListener(this);
		btnExit.addActionListener(this);

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
		paramPanelExpParMer.add(txtExpMerLecture);
		paramPanelExpParMer.add(lblExpMerOrganizator);
		paramPanelExpParMer.add(txtExpMerOrganizator);
		paramPanelExpParMer.add(lblExpMerTema);
		paramPanelExpParMer.add(txtExpMerTema);
		paramPanelExpParMer.add(lblExpMerUcastnik);
		paramPanelExpParMer.add(txtExpMerUcastnik);
		//paramPanelExpParMer.setVisible(false);
		
		paramPanelExpParRek.add(lblExpRekUsloviya );
		paramPanelExpParRek.add(txtExpRekUsloviya);
		paramPanelExpParRek.add(lblExpRekProduct);
		paramPanelExpParRek.add(txtExpRekProduct);
		paramPanelExpParRek.add(lblExpRekChain);
		paramPanelExpParRek.add(txtExpRekChain);
		paramPanelExpParRek.add(lblId);
		paramPanelExpParRek.add(txtId);
		//paramPanelExpParRek.setVisible(false);
		
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
		
		paramPanelBtn.add(btnApprove);
		//paramPanelBtn.add(btnUpdate);
		paramPanelBtn.add(btnReject);
		//paramPanelBtn.add(btnSave);
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
		
		/*dtm = ConnectToDb.getExpensesForApprove();
		resultTable.setModel(dtm);*/

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

	private void createModel() {
		ESIBag tempBag = ConnectToDb.getExpensesForApprove();
		    try{
			for (int j = 0; j < tempBag.getSize("TABLE"); j++){
				model.addRow(new Object [] 
		        		{tempBag.get("TABLE",j,"id"),
		        			tempBag.get("TABLE",j,"approve_status"),
				        	tempBag.get("TABLE",j,"company_code"),
				        	tempBag.get("TABLE",j,"entry_date"),
				        	tempBag.get("TABLE",j,"entry_user"),
				        	tempBag.get("TABLE",j,"approve_date"),
				        	tempBag.get("TABLE",j,"approve_user"),
				        	tempBag.get("TABLE",j,"Country"),
				        	tempBag.get("TABLE",j,"Region"),
				        	tempBag.get("TABLE",j,"City"),
				        	tempBag.get("TABLE",j,"City_Region"),
				        	tempBag.get("TABLE",j,"Start_Date"),
				        	tempBag.get("TABLE",j,"End_Date"),
				        	tempBag.get("TABLE",j,"First_Stage"),
				        	
				        	tempBag.get("TABLE",j,"Second_Stage"),
				        	tempBag.get("TABLE",j,"Third_Stage"),
				        	tempBag.get("TABLE",j,"Exp1"),
				        	tempBag.get("TABLE",j,"Exp2"),
				        	tempBag.get("TABLE",j,"Exp3"),
				        	tempBag.get("TABLE",j,"Exp4"),
				        	tempBag.get("TABLE",j,"Exp_Count"),
				        	tempBag.get("TABLE",j,"Exp_Amount"),
				        	tempBag.get("TABLE",j,"comments"),
				        	tempBag.get("TABLE",j,"amount1"),
				        	tempBag.get("TABLE",j,"amount2"),
				        	tempBag.get("TABLE",j,"amount3"),
				        	tempBag.get("TABLE",j,"amount4"),
				        	tempBag.get("TABLE",j,"amount5"),
				        	false});		
			}
		    }catch (Exception e) {
				// simdilik yoksa yok
			}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals("Approve")) {

				for (int j = 0; j < model.getRowCount(); j++){
					if(resultTable.getValueAt(j, 28).toString().equalsIgnoreCase("true")){					
						ConnectToDb.updateExpenseStatus(resultTable.getValueAt(j, 0).toString(),"2",userName);	
					}
				}				
				
				for( int i = model.getRowCount() - 1; i >= 0; i-- ) {
					model.removeRow(i);
				}
				createModel();
				String emailText = "Dear reciepents,\n\n"+"Below id's expense approved by necessary user\n\n" + txtId.getText();						
				//SendMail.sendEmailToReceipents("hakan.kayakutlu@gmail.com","","", "Auto mail approve expense", emailText);
				JOptionPane.showMessageDialog(pnlInfoMsg, "Expense approved", "Information", JOptionPane.INFORMATION_MESSAGE);
				
			}else if (e.getActionCommand().equals("Reject")) {
				//ConnectToDb.updateExpenseStatus(txtId.getText(),"3",user);		
				for (int j = 0; j < model.getRowCount(); j++){
					if(resultTable.getValueAt(j, 27).toString().equalsIgnoreCase("true")){					
						ConnectToDb.updateExpenseStatus(resultTable.getValueAt(j, 0).toString(),"3",userName);	
					}
				}
				for( int i = model.getRowCount() - 1; i >= 0; i-- ) {
					model.removeRow(i);
				}
				createModel();				
				String emailText = "Dear reciepents,\n\n"+"Below id's expense rejected by necessary user\n\n" + txtId.getText();						
				//SendMail.sendEmailToReceipents("hakan.kayakutlu@gmail.com","hgokmen@solgarvitamin.ru","mkamaeva@solgarvitamin.ru", "Auto mail reject expense", emailText);
				JOptionPane.showMessageDialog(pnlInfoMsg, "Expense rejected", "Information", JOptionPane.INFORMATION_MESSAGE);
				
			}else if (e.getActionCommand().equals("Delete")) {
				
			}else if (e.getActionCommand().equals("Save")) {				
				
						
				
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
	    	  }else if(name.equalsIgnoreCase("Region")){
	    		  cmbBoxCity.removeAllItems();
	    		  cmbBoxCityRegion.removeAllItems();
	    		  ConnectToDb.getPRMDataGroupBy("city", "solgar_prm.prm_address_group",cmbBoxCity,"region",cmbBoxRegion.getSelectedItem().toString());
	    	  }else if(name.equalsIgnoreCase("City")){
	    		  cmbBoxCityRegion.removeAllItems();
	    		  ConnectToDb.getPRMDataGroupBy("city_region", "solgar_prm.prm_address_group",cmbBoxCityRegion,"city",cmbBoxCity.getSelectedItem().toString());
	    	  }else if(name.equalsIgnoreCase("CityRegion")){
	    		 //enson
	    	  }else if(name.equalsIgnoreCase("ExpMain")){
	    		  if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("Мероприятия")){
	    			 // paramPanelExpParMer.setVisible(true);
	    			 // paramPanelExpParRek.setVisible(false);
	    			 // paramPanelExpParPos.setVisible(false);
	    		  }else if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("Реклама")){
	    			  //  paramPanelExpParMer.setVisible(false);
	    			  // paramPanelExpParRek.setVisible(true);
	    			  //  paramPanelExpParPos.setVisible(false);
	    		  }else if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("POS-материалы")){
	    			  // paramPanelExpParMer.setVisible(false);
	    			  // paramPanelExpParRek.setVisible(false);
	    			  // paramPanelExpParPos.setVisible(true);
	    		  }
	    		  cmbBoxExpLevel1.removeAllItems();
	    		  cmbBoxExpLevel2.removeAllItems();
	    		  ConnectToDb.getPRMDataGroupBy("level1", "solgar_prm.prm_exps_types",cmbBoxExpLevel1,"main_name",cmbBoxExpMain.getSelectedItem().toString());
	    	  }else if(name.equalsIgnoreCase("Level1")){
	    		  cmbBoxExpLevel2.removeAllItems();
	    		  ConnectToDb.getPRMDataGroupBy("level2", "solgar_prm.prm_exps_types",cmbBoxExpLevel2,"level1",cmbBoxExpLevel1.getSelectedItem().toString());
	    	  }else if(name.equalsIgnoreCase("Level2")){
	    		  //enson
	    	  }
    	  }	  
    	  
                 
      }   
      

	  public void mouseClicked(MouseEvent e) {
		  	cleanAllScreen();
		  	int i = resultTable.getSelectedRow();
		  	txtId.setText(resultTable.getValueAt(i, 0).toString());
		  	cmbBoxCompanyCode.setSelectedItem(resultTable.getValueAt(i, 2).toString());
		  	cmbBoxCountry.setSelectedItem(resultTable.getValueAt(i, 7).toString());
			cmbBoxRegion.setSelectedItem(resultTable.getValueAt(i, 8).toString());
			cmbBoxCity.setSelectedItem(resultTable.getValueAt(i, 9).toString());
			if(resultTable.getValueAt(i, 10) != null){
				cmbBoxCityRegion.setSelectedItem(resultTable.getValueAt(i, 10).toString());
			}else{
				cmbBoxCityRegion.setSelectedItem("");
			}
			
			if(resultTable.getValueAt(i, 11) != null){
				startDate.setText(resultTable.getValueAt(i, 11).toString());
			}else{
				startDate.setText("");
			}
			if(resultTable.getValueAt(i, 12) != null){
				endDate.setText(resultTable.getValueAt(i, 12).toString());
			}else{
				endDate.setText("");
			}
			
			cmbBoxExpMain.setSelectedItem(resultTable.getValueAt(i, 13).toString());
			cmbBoxExpLevel1.setSelectedItem(resultTable.getValueAt(i, 14).toString());
			if(resultTable.getValueAt(i, 15) != null){
				cmbBoxExpLevel2.setSelectedItem(resultTable.getValueAt(i, 15).toString());
			}else{
				cmbBoxExpLevel2.setSelectedItem("");
			}
			
			 if(resultTable.getValueAt(i, 13).toString().equalsIgnoreCase("Мероприятия")){
				if(resultTable.getValueAt(i, 16) != null){
					txtExpMerLecture.setText(resultTable.getValueAt(i, 16).toString());
				}else{
					txtExpMerLecture.setText("");
				}
				if(resultTable.getValueAt(i, 17) != null){
					txtExpMerOrganizator.setText(resultTable.getValueAt(i, 17).toString());
				}else{
					txtExpMerOrganizator.setText("");
				}
				if(resultTable.getValueAt(i, 18) != null){
					txtExpMerTema.setText(resultTable.getValueAt(i, 18).toString());
				}else{
					txtExpMerTema.setText("");
				}
				if(resultTable.getValueAt(i, 19) != null){
					txtExpMerUcastnik.setText(resultTable.getValueAt(i, 19).toString());
				}else{
					txtExpMerUcastnik.setText("");
				}				
    		  }else if(resultTable.getValueAt(i, 13).toString().equalsIgnoreCase("Реклама")){  
				  if(resultTable.getValueAt(i, 16) != null){
					  txtExpRekUsloviya.setText(resultTable.getValueAt(i, 16).toString());
				  }else{
					txtExpRekUsloviya.setText("");
				  }
				  if(resultTable.getValueAt(i, 17) != null){
					txtExpRekProduct.setText(resultTable.getValueAt(i, 17).toString());
				  }else{
					txtExpRekProduct.setText("");
				  }
				  if(resultTable.getValueAt(i, 18) != null){
					txtExpRekChain.setText(resultTable.getValueAt(i, 18).toString());
				  }else{
					txtExpRekChain.setText("");
				  }	
    		  }else if(resultTable.getValueAt(i, 13).toString().equalsIgnoreCase("POS-материалы")){
    			  if(resultTable.getValueAt(i, 16) != null){
    				  txtExpPosProduct.setText(resultTable.getValueAt(i, 16).toString());
				  }else{
					  txtExpPosProduct.setText("");
				  }
				  if(resultTable.getValueAt(i, 17) != null){
					  txtExpPosParyadk.setText(resultTable.getValueAt(i, 17).toString());
				  }else{
					  txtExpPosParyadk.setText("");
				  }
				  if(resultTable.getValueAt(i, 18) != null){
					  txtExpPosStatus.setText(resultTable.getValueAt(i, 18).toString());
				  }else{
					  txtExpPosStatus.setText("");
				  }
				
    		  }
			 if(resultTable.getValueAt(i, 20) != null){
				 txtCountFormat.setText(resultTable.getValueAt(i, 20).toString());
			  }else{
				  txtCountFormat.setText("");
			  }
			 if(resultTable.getValueAt(i, 21) != null){
				 txtAmountFormat.setText(resultTable.getValueAt(i, 21).toString());
			  }else{
				  txtAmountFormat.setText("");
			  }
			 if(resultTable.getValueAt(i, 22) != null){
				 textAreaComment.setText(resultTable.getValueAt(i, 22).toString());
			  }else{
				  textAreaComment.setText("");
			  }	
			 
			 
			 if(resultTable.getValueAt(i, 23) != null){
				 txtAmount1.setText(resultTable.getValueAt(i, 23).toString());
			  }else{
				  txtAmount1.setText("");
			  }
			 if(resultTable.getValueAt(i, 24) != null){
				 txtAmount2.setText(resultTable.getValueAt(i, 24).toString());
			  }else{
				  txtAmount2.setText("");
			  }
			 if(resultTable.getValueAt(i, 25) != null){
				 txtAmount3.setText(resultTable.getValueAt(i, 25).toString());
			  }else{
				  txtAmount3.setText("");
			  }
			 if(resultTable.getValueAt(i, 26) != null){
				 txtAmount4.setText(resultTable.getValueAt(i, 26).toString());
			  }else{
				 txtAmount4.setText("");
			  }
			 if(resultTable.getValueAt(i, 27) != null){
				 txtAmount5.setText(resultTable.getValueAt(i, 27).toString());
			  }else{
				  txtAmount5.setText("");
			  }
			
			btnApprove.setEnabled(true);
			btnReject.setEnabled(true);
			
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
		private void cleanAllScreen() {
			
		  	cmbBoxCompanyCode.setSelectedItem("");
		  	cmbBoxCountry.setSelectedItem("");
			cmbBoxRegion.setSelectedItem("");
			cmbBoxCity.setSelectedItem("");					
			cmbBoxCityRegion.setSelectedItem("");
			startDate.setText("");					
			endDate.setText("");
			cmbBoxExpMain.setSelectedItem("");
			cmbBoxExpLevel1.setSelectedItem("");					
			cmbBoxExpLevel2.setSelectedItem("");					
			txtExpMerLecture.setText("");
			txtExpMerOrganizator.setText("");						
			txtExpMerTema.setText("");					
			txtExpMerUcastnik.setText("");					
			txtExpRekUsloviya.setText("");						  
			txtExpRekProduct.setText("");						  
			txtExpRekChain.setText("");						
			txtExpPosProduct.setText("");						  
			txtExpPosParyadk.setText("");						  
			txtExpPosStatus.setText("");						
			txtCountFormat.setText("");					 
			txtAmountFormat.setText("");					
			textAreaComment.setText("");					  
			txtAmount1.setText("");					  
			txtAmount2.setText("");					  
			txtAmount3.setText("");				 
			txtAmount4.setText("");					  
			txtAmount5.setText("");

		}
	
	
}