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
	import java.text.SimpleDateFormat;
	import java.util.Calendar;

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

	import com.toedter.calendar.JDateChooser;

	import main.ConnectToDb;
	import main.ReportQueries;
	import util.Util;
	import cb.esi.esiclient.util.ESIBag;

	public class SalesRepObservation extends JFrame implements ActionListener,ItemListener{
		private static final int FRAME_WIDTH = 1100;
		private static final int FRAME_HEIGHT = 900;
		
		private JLabel lblEmpty,lblEmpty2,lblEmpty3;
		private JPanel pnlErrorMsg;
		private String userName="Hakan KAYAKUTLU";

		private JComboBox cmbBoxStartYear,cmbBoxStartMonth,
		cmbBoxEndYear,cmbBoxEndMonth,cmbBoxChain,cmbBoxCountry,cmbBoxRegion,cmbBoxCity,cmbBoxMedRep,
		cmbBoxPrdMain,cmbBoxPrdSub,cmbBoxProduct,cmbBoxReportType,cmbBoxCompanyType,cmbBoxArea;
		
		DefaultTableModel dtm = new DefaultTableModel(0, 0);
		String header[] = new String[] { "Row Number","Column1","Column2"};
		private JTable resultTable;
		/**
		 * Launch the application.
		 */
		public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						ESIBag inBag = new ESIBag();
						SalesRepObservation window = new SalesRepObservation(inBag);
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
		public SalesRepObservation(ESIBag inBag) throws SQLException {
			super("Sales Report Observation");
			Toolkit toolkit;
			Dimension dim;
			int screenHeight, screenWidth;
			// Initialize basic layout properties
			setBackground(Color.lightGray);
			// Set the frame's display to be WIDTH x HEIGHT in the middle of the screen
			toolkit = Toolkit.getDefaultToolkit();
			dim = toolkit.getScreenSize();
			screenHeight = dim.height;
			screenWidth = dim.width;
			setBounds((screenWidth - FRAME_WIDTH) / 2, (screenHeight - FRAME_HEIGHT) / 2, FRAME_WIDTH, FRAME_HEIGHT);

			try {		
				if(inBag.existsBagKey("LOGINNAME")){
					userName = inBag.get("LOGINNAME").toString();				
				}
				
			} catch (Exception e) {
				// simdilik yoksa yok
			}
			dtm.setColumnIdentifiers(header);
			lblEmpty = new JLabel("");
			lblEmpty2= new JLabel("");
			lblEmpty3 = new JLabel("");
		
			// Put the final touches to the JFrame object
	        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	        getContentPane().setLayout(null);
	        
	        JPanel pnlParam = new JPanel();
	        pnlParam.setBounds(10, 11, 1064, 196);
	        getContentPane().add(pnlParam);
	        pnlParam.setLayout(null);
	        
	        JLabel lblsalesDate = new JLabel("Sales Date Between");
	        lblsalesDate.setBounds(10, 42, 102, 14);
	        pnlParam.add(lblsalesDate);
	        
	        JLabel lblChain = new JLabel("Chain");
	        lblChain.setBounds(10, 67, 46, 14);
	        pnlParam.add(lblChain);
	        
	        JLabel lblCountry = new JLabel("Country");
	        lblCountry.setBounds(10, 92, 46, 14);
	        pnlParam.add(lblCountry);
	        
	        JLabel lblRegion = new JLabel("Region");
	        lblRegion.setBounds(10, 143, 46, 14);
	        pnlParam.add(lblRegion);
	        
	        JLabel lblCity = new JLabel("City");
	        lblCity.setBounds(10, 168, 46, 14);
	        pnlParam.add(lblCity);
	        
	        JLabel lblMedrep = new JLabel("MedRep");
	        lblMedrep.setBounds(516, 14, 46, 14);
	        pnlParam.add(lblMedrep);
	        
	        JLabel lblPrdMainGroup = new JLabel("Product Main Group");
	        lblPrdMainGroup.setBounds(516, 39, 114, 14);
	        pnlParam.add(lblPrdMainGroup);
	        
	        JLabel lblProductSubGroup = new JLabel("Product Sub Group");
	        lblProductSubGroup.setBounds(516, 64, 102, 14);
	        pnlParam.add(lblProductSubGroup);
	        
	        JLabel lblProductName = new JLabel("Product Name");
	        lblProductName.setBounds(516, 89, 102, 14);
	        pnlParam.add(lblProductName);
	        
	        cmbBoxStartYear = new JComboBox();
	        cmbBoxStartYear.setBounds(122, 36, 72, 20);
	        cmbBoxStartYear.addItem("2016");
	        cmbBoxStartYear.addItem("2017");
	        cmbBoxStartYear.addItem("2018");
	        pnlParam.add(cmbBoxStartYear);
	        
	        cmbBoxStartMonth = new JComboBox();
	        cmbBoxStartMonth.setBounds(204, 36, 71, 20);
	        cmbBoxStartMonth.addItem("JAN");
	        cmbBoxStartMonth.addItem("FEB");
	        cmbBoxStartMonth.addItem("MAR");
	        cmbBoxStartMonth.addItem("APR");
	        cmbBoxStartMonth.addItem("MAY");
	        cmbBoxStartMonth.addItem("JUN");
	        cmbBoxStartMonth.addItem("JUL");
	        cmbBoxStartMonth.addItem("AUG");
	        cmbBoxStartMonth.addItem("SEP");
	        cmbBoxStartMonth.addItem("OCT");
	        cmbBoxStartMonth.addItem("NOW");
	        cmbBoxStartMonth.addItem("DEC");
	        cmbBoxStartMonth.addItem("QUARTERLY");
	        pnlParam.add(cmbBoxStartMonth);
	        
	        JLabel lblAnd = new JLabel("And");
	        lblAnd.setBounds(285, 39, 34, 14);
	        pnlParam.add(lblAnd);
	        
	        cmbBoxEndYear = new JComboBox();
	        cmbBoxEndYear.setBounds(323, 36, 77, 20);
	        cmbBoxEndYear.addItem("2016");
	        cmbBoxEndYear.addItem("2017");
	        cmbBoxEndYear.addItem("2018");
	        cmbBoxEndYear.setSelectedIndex(1);
	        pnlParam.add(cmbBoxEndYear);
	        
	        cmbBoxEndMonth = new JComboBox();
	        cmbBoxEndMonth.setBounds(410, 36, 82, 20);
	        cmbBoxEndMonth.addItem("JAN");
	        cmbBoxEndMonth.addItem("FEB");
	        cmbBoxEndMonth.addItem("MAR");
	        cmbBoxEndMonth.addItem("APR");
	        cmbBoxEndMonth.addItem("MAY");
	        cmbBoxEndMonth.addItem("JUN");
	        cmbBoxEndMonth.addItem("JUL");
	        cmbBoxEndMonth.addItem("AUG");
	        cmbBoxEndMonth.addItem("SEP");
	        cmbBoxEndMonth.addItem("OCT");
	        cmbBoxEndMonth.addItem("NOW");
	        cmbBoxEndMonth.addItem("DEC");
	        cmbBoxEndMonth.setSelectedIndex(11);
	        pnlParam.add(cmbBoxEndMonth);
     
	        cmbBoxChain = new JComboBox();
	        Util.getPRMData("group_company", "solgar_prm.prm_russia_chains",cmbBoxChain);			
	        cmbBoxChain.setBounds(122, 64, 247, 20);
	        cmbBoxChain.setMaximumRowCount(50);
			cmbBoxChain.setEditable(true);	
			cmbBoxChain.setSelectedIndex(-1);
			pnlParam.add(cmbBoxChain);
	        
	        cmbBoxCountry = new JComboBox();
	        cmbBoxCountry.setBounds(122, 89, 247, 20);
	        cmbBoxCountry.addItem("");
	        Util.getPRMDataGroupBy("country", "solgar_prm.prm_sales_addresses",cmbBoxCountry,"","");	
			cmbBoxCountry.setMaximumRowCount(50);
			cmbBoxCountry.setSelectedIndex(-1);
	        pnlParam.add(cmbBoxCountry);
        
	        cmbBoxRegion = new JComboBox();
	        cmbBoxRegion.setBounds(122, 140, 247, 20);
	        pnlParam.add(cmbBoxRegion);
	        
	        cmbBoxCity = new JComboBox();
	        cmbBoxCity.setBounds(122, 165, 247, 20);
	        pnlParam.add(cmbBoxCity);
	        
	        cmbBoxMedRep = new JComboBox();
	        cmbBoxMedRep.setBounds(626, 11, 163, 20);
			cmbBoxMedRep.setMaximumRowCount(100);
			cmbBoxMedRep.setEditable(true);
			cmbBoxMedRep.setSelectedIndex(-1);
	        pnlParam.add(cmbBoxMedRep);
	        
	        cmbBoxPrdMain = new JComboBox();
	        cmbBoxPrdMain.setBounds(626, 36, 163, 20);
	        cmbBoxPrdMain.addItem("");
	        Util.getPRMDataGroupBy("product_main_group", "solgar_tst.sales_product_group",cmbBoxPrdMain,"","");
	        cmbBoxPrdMain.setSelectedIndex(-1);	        
	        pnlParam.add(cmbBoxPrdMain);
	        
	        cmbBoxPrdSub = new JComboBox();
	        cmbBoxPrdSub.setBounds(628, 61, 161, 20);
	        pnlParam.add(cmbBoxPrdSub);
	        
	        cmbBoxProduct = new JComboBox();
	        cmbBoxProduct.setBounds(626, 86, 163, 20);
	        pnlParam.add(cmbBoxProduct);
	        
	        JLabel lblReportType = new JLabel("Report Type");
	        lblReportType.setBounds(830, 22, 82, 14);
	        pnlParam.add(lblReportType);
	        
	        cmbBoxReportType = new JComboBox();
	        cmbBoxReportType.setBounds(922, 22, 132, 20);
	        cmbBoxReportType.addItem("CHAIN_SALES");
	        cmbBoxReportType.addItem("REGIONAL_SALES");
	        cmbBoxReportType.addItem("MEDREP_SALES");
	        cmbBoxReportType.addItem("PRODUCT_SALES");
	        cmbBoxReportType.setSelectedIndex(0);
	        pnlParam.add(cmbBoxReportType);
	        
	        JButton btnGenerate = new JButton("Generate Report");
	        btnGenerate.setBounds(830, 52, 223, 23);
	        pnlParam.add(btnGenerate);
	        
	        JLabel lblCompanyType = new JLabel("Company Type");
	        lblCompanyType.setBounds(10, 11, 102, 14);
	        pnlParam.add(lblCompanyType);
	        
	        cmbBoxCompanyType = new JComboBox();
	        cmbBoxCompanyType.setBounds(122, 8, 247, 20);
	        cmbBoxCompanyType.addItem("SOLGAR");
	        cmbBoxCompanyType.addItem("NATURES BOUNTY");
	        cmbBoxCompanyType.setSelectedIndex(0);
	        pnlParam.add(cmbBoxCompanyType);
	        
	        cmbBoxArea = new JComboBox();
			cmbBoxArea.setBounds(122, 114, 247, 20);
			pnlParam.add(cmbBoxArea);
			cmbBoxCompanyType.addItemListener(this);
			
			if(cmbBoxCompanyType.getSelectedItem() != null){
				String compType = "BN";
				if(cmbBoxCompanyType.getSelectedItem().toString().equalsIgnoreCase("SOLGAR")){
					compType = "SL";
				}
				cmbBoxMedRep.addItem("");
				Util.getPRMDataGroupBy("medrep_name", "solgar_prm.prm_report_medrep",cmbBoxMedRep,"company",compType);				
				cmbBoxMedRep.setSelectedIndex(0);
			}
	        
	        JPanel pnlResult = new JPanel();
	        pnlResult.setBounds(10, 218, 1064, 616);
	        getContentPane().add(pnlResult);
	        pnlResult.setLayout(null);
	        
	        JScrollPane scrollPane = new JScrollPane();
	        scrollPane.setBounds(10, 11, 1044, 591);
	        pnlResult.add(scrollPane);
	        
	        
	        resultTable = new JTable(20, 8);		
			dtm.setColumnIdentifiers(header);
			resultTable.setModel(dtm);
			resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			
			scrollPane.setViewportView(resultTable);
			scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));       
			
	  		cmbBoxCountry.setEnabled(false);
	  		cmbBoxArea.setEnabled(false);
	  		cmbBoxRegion.setEnabled(false);
	  		cmbBoxCity.setEnabled(false);
	  		
	  		cmbBoxMedRep.setEnabled(false);
	  		cmbBoxPrdMain.setEnabled(false);
	  		cmbBoxPrdSub.setEnabled(false);
	  		cmbBoxProduct.setEnabled(false);
			
			cmbBoxChain.setName("Chain");
			cmbBoxChain.addItemListener(this);
			cmbBoxCountry.setName("Country");
			cmbBoxCountry.addItemListener(this);
			cmbBoxArea.setName("Area");
			cmbBoxArea.addItemListener(this);
			cmbBoxRegion.setName("Region");
			cmbBoxRegion.addItemListener(this);
			cmbBoxCity.setName("City");
			cmbBoxCity.addItemListener(this);
			cmbBoxMedRep.setName("MedRep");
			cmbBoxMedRep.addItemListener(this);
			cmbBoxPrdMain.setName("PrdMain");
			cmbBoxPrdMain.addItemListener(this);
			cmbBoxPrdSub.setName("PrdSub");
			cmbBoxPrdSub.addItemListener(this);
			cmbBoxProduct.setName("Product");
			cmbBoxProduct.addItemListener(this);
			cmbBoxReportType.setName("ReportType");
			cmbBoxReportType.addItemListener(this);
			cmbBoxCompanyType.setName("CompanyType");
			
			btnGenerate.addActionListener(this);
			
			JLabel lblArea = new JLabel("Area");
			lblArea.setBounds(10, 117, 46, 14);
			pnlParam.add(lblArea);

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
						
					String compType = "",beginYear = "",beginMonth="",endYear="",endMonth="",chain="",country="",
							area="",region="",city="",medRep="",productMainGroup="",productSubGroup="",product="";
					String repType = cmbBoxReportType.getSelectedItem().toString();
			  		
			  		if(cmbBoxCompanyType.getSelectedItem() != null && cmbBoxCompanyType.getSelectedItem().toString().trim().length()>0){						
			  			if(cmbBoxCompanyType.getSelectedItem().toString().trim().equalsIgnoreCase("SOLGAR")){
			  				compType = "SL";
						}else{
							compType = "BN";
						}						
					}
			  		if(cmbBoxStartYear.getSelectedItem() != null && cmbBoxStartYear.getSelectedItem().toString().trim().length()>0){						
			  			beginYear = cmbBoxStartYear.getSelectedItem().toString();						
					}
			  		if(cmbBoxStartMonth.getSelectedItem() != null && cmbBoxStartMonth.getSelectedItem().toString().trim().length()>0){						
			  			beginMonth = getMonthToNumber(cmbBoxStartMonth.getSelectedItem().toString());			  									
					}
			  		if(cmbBoxEndYear.getSelectedItem() != null && cmbBoxEndYear.getSelectedItem().toString().trim().length()>0){						
			  			endYear = cmbBoxEndYear.getSelectedItem().toString();						
					}
			  		if(cmbBoxEndMonth.getSelectedItem() != null && cmbBoxEndMonth.getSelectedItem().toString().trim().length()>0){						
			  			endMonth = getMonthToNumber(cmbBoxEndMonth.getSelectedItem().toString());
					}
			  		
			  		if(cmbBoxChain.getSelectedItem() != null && cmbBoxChain.getSelectedItem().toString().trim().length()>0){						
			  			chain = cmbBoxChain.getSelectedItem().toString();						
					}
			  		if(cmbBoxCountry.getSelectedItem() != null && cmbBoxCountry.getSelectedItem().toString().trim().length()>0){						
			  			country = cmbBoxCountry.getSelectedItem().toString();						
					}
			  		if(cmbBoxArea.getSelectedItem() != null && cmbBoxArea.getSelectedItem().toString().trim().length()>0){						
			  			area = cmbBoxArea.getSelectedItem().toString();						
					}
			  		if(cmbBoxRegion.getSelectedItem() != null && cmbBoxRegion.getSelectedItem().toString().trim().length()>0){						
			  			region = cmbBoxRegion.getSelectedItem().toString();						
					}
			  		if(cmbBoxCity.getSelectedItem() != null && cmbBoxCity.getSelectedItem().toString().trim().length()>0){						
			  			city = cmbBoxCity.getSelectedItem().toString();						
					}
			  		
			  		if(cmbBoxMedRep.getSelectedItem() != null && cmbBoxMedRep.getSelectedItem().toString().trim().length()>0){						
			  			medRep = cmbBoxMedRep.getSelectedItem().toString();						
					}
			  		if(cmbBoxPrdMain.getSelectedItem() != null && cmbBoxPrdMain.getSelectedItem().toString().trim().length()>0){						
			  			productMainGroup = cmbBoxPrdMain.getSelectedItem().toString();						
					}
			  		if(cmbBoxPrdSub.getSelectedItem() != null && cmbBoxPrdSub.getSelectedItem().toString().trim().length()>0){						
			  			productSubGroup = cmbBoxPrdSub.getSelectedItem().toString();						
					}
			  		if(cmbBoxProduct.getSelectedItem() != null && cmbBoxProduct.getSelectedItem().toString().trim().length()>0){						
			  			product = cmbBoxProduct.getSelectedItem().toString();						
					}
					
					if(compType.length()==0){//Show error message						
						JOptionPane.showMessageDialog(pnlErrorMsg, "Please fill company code", "Error", JOptionPane.ERROR_MESSAGE);
					}else{
						
						if(repType.equalsIgnoreCase("CHAIN_SALES")){
							DefaultTableModel dtm = ReportQueries.repMarkChainSales
									(resultTable,repType,compType ,beginYear,beginMonth,endYear,endMonth,chain,country,
									area,region,city,medRep,productMainGroup,productSubGroup,product);
				  		  }else if(repType.equalsIgnoreCase("REGIONAL_SALES")){
				  			DefaultTableModel dtm = ReportQueries.repMarkRegionalSales
									(resultTable,repType,compType ,beginYear,beginMonth,endYear,endMonth,chain,country,
									area,region,city,medRep,productMainGroup,productSubGroup,product);
				  		  }else if(repType.equalsIgnoreCase("MEDREP_SALES")){
				  			DefaultTableModel dtm = ReportQueries.repMarkMedRepSales
									(resultTable,repType,compType ,beginYear,beginMonth,endYear,endMonth,chain,country,
									area,region,city,medRep,productMainGroup,productSubGroup,product);
				  		  }else if(repType.equalsIgnoreCase("PRODUCT_SALES")){
				  			DefaultTableModel dtm = ReportQueries.repMarkProductSales
									(resultTable,repType,compType ,beginYear,beginMonth,endYear,endMonth,chain,country,
									area,region,city,medRep,productMainGroup,productSubGroup,product);
				  		  }
						
						
					}
					
					
				}else if (e.getActionCommand().equals("Exit")) {
					setVisible(false);
				}
				
			} catch (Exception ex) {
				String message = ex.getMessage();
				ex.printStackTrace();
			}
			
		}

		private String getMonthToNumber(String monthStr) {
			String beginMonth;
			if(monthStr.equalsIgnoreCase("JAN")){
				beginMonth = "01";
			}else if(monthStr.equalsIgnoreCase("FEB")){
				beginMonth = "02";
			}else if(monthStr.equalsIgnoreCase("MAR")){
				beginMonth = "03";
			}else if(monthStr.equalsIgnoreCase("APR")){
				beginMonth = "04";
			}else if(monthStr.equalsIgnoreCase("MAY")){
				beginMonth = "05";
			}else if(monthStr.equalsIgnoreCase("JUN")){
				beginMonth = "06";
			}else if(monthStr.equalsIgnoreCase("JUL")){
				beginMonth = "07";
			}else if(monthStr.equalsIgnoreCase("AUG")){
				beginMonth = "08";
			}else if(monthStr.equalsIgnoreCase("SEP")){
				beginMonth = "09";
			}else if(monthStr.equalsIgnoreCase("OCT")){
				beginMonth = "10";
			}else if(monthStr.equalsIgnoreCase("NOW")){
				beginMonth = "11";
			}else if(monthStr.equalsIgnoreCase("DEC")){
				beginMonth = "12";
			}else{
				beginMonth = cmbBoxStartMonth.getSelectedItem().toString();
			}
			return beginMonth;
		}
		
		public void itemStateChanged(ItemEvent itemEvent) {
	  	  JComboBox cmbBox = (JComboBox)itemEvent.getSource();
	  	  String name = cmbBox.getName();
	  	  if(cmbBox.getSelectedItem() != null &&cmbBox.getSelectedItem().toString().length()>0){
	  		if(name.equalsIgnoreCase("Country")){
		  		  cmbBoxArea.removeAllItems();
		  		  cmbBoxRegion.removeAllItems();
		  		  cmbBoxCity.removeAllItems();	   
		  		  cmbBoxArea.addItem("");
		  		  try {
					Util.getPRMDataGroupBy("area", "solgar_prm.prm_sales_addresses",cmbBoxArea,"country",cmbBoxCountry.getSelectedItem().toString());
		  		  } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		  		  }	    		  
		  		  cmbBoxRegion.setSelectedIndex(-1);
		  		  cmbBoxCity.setSelectedIndex(-1);	    		  	    		  
		  	    }else if(name.equalsIgnoreCase("Area")){
		  		  cmbBoxRegion.removeAllItems();
		  		  cmbBoxCity.removeAllItems();	    
		  		  cmbBoxRegion.addItem("");
		  		  try {
					Util.getPRMDataGroupBy("region", "solgar_prm.prm_sales_addresses",cmbBoxRegion,"area",cmbBoxArea.getSelectedItem().toString());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  		  cmbBoxRegion.setSelectedIndex(-1);
		  		  cmbBoxCity.setSelectedIndex(-1);	    		  
		  	    }else if(name.equalsIgnoreCase("Region")){
		  		  cmbBoxCity.removeAllItems();
		  		  cmbBoxCity.addItem("");
		  		  try {
					Util.getPRMDataGroupBy("city", "solgar_prm.prm_sales_addresses",cmbBoxCity,"region",cmbBoxRegion.getSelectedItem().toString());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  		  cmbBoxCity.setSelectedIndex(-1);
		  	  }else if(name.equalsIgnoreCase("PrdMain")){
		  		  cmbBoxPrdSub.removeAllItems();
		  		  cmbBoxProduct.removeAllItems();	    
		  		  cmbBoxPrdSub.addItem("");
		  		  try {
					Util.getPRMDataGroupBy("product_sub_group", "solgar_tst.sales_product_group",cmbBoxPrdSub,"product_main_group",cmbBoxPrdMain.getSelectedItem().toString());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  		cmbBoxPrdSub.setSelectedIndex(-1);    		  
		  	    }
		  	  else if(name.equalsIgnoreCase("CompanyType")){
		  		cmbBoxMedRep.removeAllItems();
		  		cmbBoxMedRep.addItem("");
		  		  try {
		  			String compType = "BN";
					if(cmbBoxCompanyType.getSelectedItem().toString().equalsIgnoreCase("SOLGAR")){
						compType = "SL";
					}
					Util.getPRMDataGroupBy("medrep_name", "solgar_prm.prm_report_medrep",cmbBoxMedRep,"company",compType);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  	  }else if(name.equalsIgnoreCase("ReportType")){
		  		String repType = cmbBoxReportType.getSelectedItem().toString();
		  		
		  		cmbBoxChain.setEnabled(false);
		  		cmbBoxChain.setSelectedIndex(-1);
		  		cmbBoxCountry.setEnabled(false);
		  		cmbBoxCountry.setSelectedIndex(-1);
		  		cmbBoxArea.setEnabled(false);
		  		cmbBoxArea.setSelectedIndex(-1);
		  		cmbBoxRegion.setEnabled(false);
		  		cmbBoxRegion.setSelectedIndex(-1);
		  		cmbBoxCity.setEnabled(false);
		  		cmbBoxCity.setSelectedIndex(-1);
		  		
		  		cmbBoxMedRep.setEnabled(false);
		  		cmbBoxMedRep.setSelectedIndex(-1);
		  		cmbBoxPrdMain.setEnabled(false);
		  		cmbBoxPrdMain.setSelectedIndex(-1);
		  		cmbBoxPrdSub.setEnabled(false);
		  		cmbBoxPrdSub.setSelectedIndex(-1);
		  		cmbBoxProduct.setEnabled(false);
		  		cmbBoxProduct.setSelectedIndex(-1);
		  		
		  		  if(repType.equalsIgnoreCase("CHAIN_SALES")){
		  			cmbBoxChain.setEnabled(true);
		  		  }else if(repType.equalsIgnoreCase("REGIONAL_SALES")){
		  			cmbBoxCountry.setEnabled(true);
		  			cmbBoxArea.setEnabled(true);
		  			cmbBoxRegion.setEnabled(true);
		  			cmbBoxCity.setEnabled(true);
		  		  }else if(repType.equalsIgnoreCase("MEDREP_SALES")){
		  			cmbBoxMedRep.setEnabled(true);
		  		  }else if(repType.equalsIgnoreCase("PRODUCT_SALES")){
		  			cmbBoxPrdMain.setEnabled(true);
			  		cmbBoxPrdSub.setEnabled(true);
			  		cmbBoxProduct.setEnabled(true);
		  		  }
		  	  }
		  		
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