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
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import main.ConnectToDb;
import main.ReportQueries;
import util.Util;
import cb.esi.esiclient.util.ESIBag;

public class ReportObservation extends JFrame implements ActionListener,ItemListener{
	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 900;
	
	private JLabel lblCompanyName,lblChainName,lblReportTypes,lblEmpty,lblEmpty1,lblEmpty2,lblEmpty3,
	lblAdrCountry,lblAdrRegion,lblAdrCity,lblAdrCityRegion,lblProduct,lblMedRep,lblCompanyCode;
	private JPanel paramPanel,pnlErrorMsg;
	private JScrollPane jScroll;
	private JTable resultTable;
	private JComboBox cmbBoxCompanies,cmbBoxDatesBegin,cmbBoxDatesEnd,cmbBoxReportTypes,
	cmbBoxCountry,cmbBoxRegion,cmbBoxCity,cmbBoxProduct,cmbBoxMedRep,cmbBoxCompanyCode;
	public JButton butExceltoScreen,btnExit;
	
	DefaultTableModel dtm = new DefaultTableModel(0, 0);
	String header[] = new String[] { "Row Number","Column1","Column2"};
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ReportObservation window = new ReportObservation();
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
	public ReportObservation() throws SQLException {
		super("Report Observation");
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

		
		// scroll pane
		jScroll = new JScrollPane();
		
		resultTable = new JTable(20, 8);		
		dtm.setColumnIdentifiers(header);
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		resultTable.setModel(dtm);
		
		jScroll.setViewportView(resultTable);
		jScroll.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(jScroll, BorderLayout.CENTER);
		
		
		// add parameter panel
		paramPanel = new JPanel(new GridLayout(0, 4, 5, 5));
		
		//labels
		lblCompanyName = new JLabel("Sales Dates For Each Chain");		
		lblChainName = new JLabel("Chain Names");
		lblReportTypes = new JLabel("Select Report Which You Need");
		lblEmpty = new JLabel("");
		lblEmpty1 = new JLabel("");
		lblEmpty2= new JLabel("");
		lblEmpty3 = new JLabel("");		
		lblAdrCountry = new JLabel("Country");		
		lblAdrRegion = new JLabel("Region");
		lblAdrCity = new JLabel("City");
		lblAdrCityRegion = new JLabel("City Region");
		lblProduct = new JLabel("Product Name");
		lblMedRep = new JLabel("Med Rep Name");
		lblCompanyCode = new JLabel("Company Code");
		
		//lblEmpty = new JLabel("");
		cmbBoxCompanies = new JComboBox( new String[]{});		
		Util.getPRMData("group_company", "solgar_prm.prm_russia_chains",cmbBoxCompanies);				
		cmbBoxCompanies.setMaximumRowCount(50);
		cmbBoxCompanies.setEditable(true);	
		cmbBoxCompanies.setSelectedIndex(-1);
		
		cmbBoxDatesBegin = new JComboBox( new String[]{});		
		Util.getPRMData("report_date", "solgar_prm.prm_report_dates",cmbBoxDatesBegin);				
		cmbBoxDatesBegin.setMaximumRowCount(50);
		cmbBoxDatesBegin.setEditable(true);
		
		cmbBoxDatesEnd = new JComboBox( new String[]{});		
		Util.getPRMData("report_date", "solgar_prm.prm_report_dates",cmbBoxDatesEnd);				
		cmbBoxDatesEnd.setMaximumRowCount(50);
		cmbBoxDatesEnd.setEditable(true);
		
		cmbBoxReportTypes = new JComboBox( new String[]{});		
		Util.getPRMData("report_name", "solgar_prm.prm_report_types",cmbBoxReportTypes);
		cmbBoxDatesBegin.setMaximumRowCount(50);
		cmbBoxDatesBegin.setEditable(true);
		
		cmbBoxCountry = new JComboBox( new String[]{});		
		Util.getPRMDataGroupBy("country", "solgar_prm.prm_address_group",cmbBoxCountry,"","");	
		cmbBoxCountry.setMaximumRowCount(50);
		cmbBoxCountry.setEditable(true);
		cmbBoxCountry.setSelectedIndex(-1);
		
		cmbBoxRegion = new JComboBox( new String[]{});		
		cmbBoxRegion.setEditable(true);
		
		cmbBoxCity = new JComboBox( new String[]{});		
		cmbBoxCity.setEditable(true);
		
		cmbBoxProduct = new JComboBox( new String[]{});		
		Util.getPRMDataGroupBy("product_official_name", "solgar_tst.sales_product_group",cmbBoxProduct,"product_type","SL");	
		cmbBoxProduct.setMaximumRowCount(100);
		cmbBoxProduct.setEditable(true);
		cmbBoxProduct.setSelectedIndex(-1);
		
		cmbBoxMedRep = new JComboBox( new String[]{});		
		Util.getPRMDataGroupBy("medrep_name", "solgar_prm.prm_report_medrep",cmbBoxMedRep,"company","SL");	
		cmbBoxMedRep.setMaximumRowCount(50);
		cmbBoxMedRep.setEditable(true);
		cmbBoxMedRep.setSelectedIndex(-1);
		
		cmbBoxCompanyCode = new JComboBox( new String[]{});		
		cmbBoxCompanyCode.addItem("SOLGAR");
		cmbBoxCompanyCode.addItem("NATURES BOUNTY");
		cmbBoxCompanyCode.setEditable(true);
		cmbBoxCompanyCode.setSelectedIndex(0);
		
		// Actions		
		butExceltoScreen = new JButton("Generate Report");
		btnExit = new JButton("Exit");

		cmbBoxCountry.setName("Country");
		cmbBoxRegion.setName("Region");
		cmbBoxCity.setName("City");
		cmbBoxCompanyCode.setName("CompanyCode");
		
		//Listener
		butExceltoScreen.addActionListener(this);
		btnExit.addActionListener(this);	
		cmbBoxCompanies.addActionListener(this);
		butExceltoScreen.setEnabled(true);
		
		cmbBoxCountry.addItemListener(this);
		cmbBoxRegion.addItemListener(this);
		cmbBoxCity.addItemListener(this);

		cmbBoxCompanyCode.addItemListener(this);
		
		//Add parameters to Screen
		paramPanel.add(lblCompanyName);
		paramPanel.add(cmbBoxDatesBegin);
		paramPanel.add(cmbBoxDatesEnd);
		paramPanel.add(btnExit);
		
		paramPanel.add(lblChainName);
		paramPanel.add(cmbBoxCompanies);
		paramPanel.add(lblEmpty1);
		paramPanel.add(butExceltoScreen);
		
		paramPanel.add(lblAdrCountry);
		paramPanel.add(cmbBoxCountry);		
		paramPanel.add(lblReportTypes);
		paramPanel.add(cmbBoxReportTypes);
		
		paramPanel.add(lblAdrRegion);
		paramPanel.add(cmbBoxRegion);
		paramPanel.add(lblProduct);
		paramPanel.add(cmbBoxProduct);
		
		paramPanel.add(lblAdrCity);
		paramPanel.add(cmbBoxCity);
		paramPanel.add(lblMedRep);
		paramPanel.add(cmbBoxMedRep);
		
		paramPanel.add(lblCompanyCode);
		paramPanel.add(cmbBoxCompanyCode);
		
		
		//Last Changes
		paramPanel.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanel, BorderLayout.NORTH);
		
		

		// Put the final touches to the JFrame object
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		validate();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals("Generate Report")) {
			
				for( int i = dtm.getRowCount() - 1; i >= 0; i-- ) {//Clean Table
					dtm.removeRow(i);
				}
				String selectedChain = "";
				String selectedCountry ="";
				String selectedRegion ="";
				String selectedCity ="";
				String selectedCompany ="";
				String selectedProduct ="";
				String selectedMedRep ="";
				String selectedReport = cmbBoxReportTypes.getSelectedItem().toString();
				String selectedBeginDate = cmbBoxDatesBegin.getSelectedItem().toString();
				String selectedEndDate = cmbBoxDatesEnd.getSelectedItem().toString();
				
				if(cmbBoxCompanies.getSelectedItem() != null){
					selectedChain = cmbBoxCompanies.getSelectedItem().toString();;
				}				
				if(cmbBoxCountry.getSelectedItem() != null){
					 selectedCountry = cmbBoxCountry.getSelectedItem().toString();
				}
				if(cmbBoxRegion.getSelectedItem() != null){
					 selectedRegion = cmbBoxRegion.getSelectedItem().toString();
				}
				if(cmbBoxCity.getSelectedItem() != null){
					 selectedCity = cmbBoxCity.getSelectedItem().toString();
				}
				if(cmbBoxCompanyCode.getSelectedItem() != null){
					 selectedCompany = cmbBoxCompanyCode.getSelectedItem().toString();
				}
				if(cmbBoxProduct.getSelectedItem() != null){
					 selectedProduct = cmbBoxProduct.getSelectedItem().toString();
				}
				if(cmbBoxMedRep.getSelectedItem() != null){
					 selectedMedRep = cmbBoxMedRep.getSelectedItem().toString();
				}
				
				if(selectedReport.equalsIgnoreCase("GENERAL SALES REPORT")){							
					if(selectedBeginDate.length()==0 || selectedEndDate.length()==0 || selectedCompany.length()==0){//Show error message						
						JOptionPane.showMessageDialog(pnlErrorMsg, "Please fill date and company code", "Error", JOptionPane.ERROR_MESSAGE);
					}else{
						DefaultTableModel dtm = ReportQueries.repGetTotalCounts(resultTable,selectedReport,selectedBeginDate,selectedEndDate,selectedChain,selectedCompany,selectedCountry,selectedRegion,selectedCity,selectedProduct,selectedMedRep);
					}
					
				}else if(selectedReport.equalsIgnoreCase("CONTROL REPORT DELIVERATION")){					
					String header[] = new String[] { "Chain Name","Sales Count","Sales Date"};
					dtm.setColumnIdentifiers(header);
					resultTable.setModel(dtm);
					ESIBag outBag =ReportQueries.repGetDeliverationStatus(selectedChain,selectedBeginDate,selectedReport);
									
					for (int i = 0; i < outBag.getSize("TABLE"); i++){
						dtm.addRow(new Object[] {
							outBag.get("TABLE",i,"MAINGROUP"),
				        	outBag.get("TABLE",i,"SALESCOUNT"),
				        	outBag.get("TABLE",i,"SALESDATE"), 
							});
					}
					
				}else if(selectedReport.equalsIgnoreCase("ANNUALLY_CHAINS_SALE_REPORT")){
					if(selectedCompany.equalsIgnoreCase("SOLGAR")){selectedCompany="SL";}else{selectedCompany="BN";}
					DefaultTableModel dtm = ReportQueries.repGetSaleAnnually(resultTable,selectedReport,selectedBeginDate,selectedEndDate,selectedChain,selectedCompany);					
				}
				
				else if(selectedReport.equalsIgnoreCase("WEEKLY_TRAINING_PLAN")){
					DefaultTableModel dtm = ReportQueries.repGetWeeklyTrainings(resultTable,selectedCompany,selectedReport);
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
	    		  cmbBoxRegion.removeAllItems();
	    		  cmbBoxCity.removeAllItems();
	    		  try {
					Util.getPRMDataGroupBy("region", "solgar_prm.prm_address_group",cmbBoxRegion,"country",cmbBoxCountry.getSelectedItem().toString());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		  cmbBoxRegion.setSelectedIndex(-1);
	    		  cmbBoxCity.setSelectedIndex(-1);
	    	  }else if(name.equalsIgnoreCase("Region")){
	    		  cmbBoxCity.removeAllItems();
	    		  try {
					Util.getPRMDataGroupBy("city", "solgar_prm.prm_address_group",cmbBoxCity,"region",cmbBoxRegion.getSelectedItem().toString());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		  cmbBoxCity.setSelectedIndex(-1);
	    	  }else if(name.equalsIgnoreCase("City")){
	    		  
	    	  }else if(name.equalsIgnoreCase("CompanyCode")){
	    		  cmbBoxProduct.removeAllItems();
	    		  cmbBoxMedRep.removeAllItems();
	    		  String selectedGroup = cmbBoxCompanyCode.getSelectedItem().toString();
	    		  if(selectedGroup.equalsIgnoreCase("SOLGAR")){
	    			  try {
						Util.getPRMDataGroupBy("product_official_name", "solgar_tst.sales_product_group",cmbBoxProduct,"product_type","SL");
						Util.getPRMDataGroupBy("medrep_name", "solgar_prm.prm_report_medrep",cmbBoxMedRep,"company","SL");	
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			  
	    		  }else{
	    			  try {
						Util.getPRMDataGroupBy("product_official_name", "solgar_tst.sales_product_group",cmbBoxProduct,"product_type","BN");
						Util.getPRMDataGroupBy("medrep_name", "solgar_prm.prm_report_medrep",cmbBoxMedRep,"company","BN");	
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			  
	    		  }
	    		  cmbBoxProduct.setSelectedIndex(-1);
	    		  cmbBoxMedRep.setSelectedIndex(-1);	    			    		  
	    	  }//enson
	    	
  	  }
         
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
	
}