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
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

	import cb.esi.esiclient.util.ESIBag;
import main.ConnectToDb;

	public class ExpenseUpdateScreen extends JFrame implements ActionListener,ItemListener,MouseListener{
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
		lblCount,lblAmount,lblComment,lblCompanyCode,
		lblAmount1,lblAmount2,lblAmount3,lblAmount4,lblAmount5,lblId;
		private JPanel paramPanelMain,paramPanelAddress,paramPanelDates,paramPanelResult,paramPanelExpTypes,paramPanelExpParMer,
		paramPanelExpParRek,paramPanelExpParPos,paramPanelBtn,paramPanelBtn1,paramPanelAmounts,pnlInfoMsg;
		private JScrollPane jScroll;
		private JTable resultTable;
		private JComboBox cmbBoxCountry,cmbBoxRegion,cmbBoxCity,cmbBoxCityRegion,
		cmbBoxExpMain,cmbBoxExpLevel1,cmbBoxExpLevel2,cmbBoxCompanyCode,cmbBoxExpMerLecture,cmbBoxExpMerOrganizator;
		private JFormattedTextField startDate,endDate;
		private JTextField txtExpMerTema,txtExpMerUcastnik,
		txtExpRekUsloviya,txtExpRekProduct,txtExpRekChain,
		txtExpPosProduct,txtExpPosParyadk,txtExpPosStatus,txtId;
		public JButton btnAdd,btnDelete,btnUpdate,btnSave,btnExit;
		public JTextArea textAreaComment;
		public JFormattedTextField txtCountFormat,txtAmountFormat,txtAmount1,txtAmount2,txtAmount3,txtAmount4,txtAmount5;
		final DefaultTableModel model = new DefaultTableModel();
		DefaultTableModel dtm = new DefaultTableModel(0, 0);
		String header[] = new String[] { "Row Number","Column1","Column2"};
		
		/**
		 * Launch the application.
		 */
		public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						ESIBag inBag = new ESIBag();
						ExpenseUpdateScreen window = new ExpenseUpdateScreen(inBag);
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
		public ExpenseUpdateScreen(ESIBag inBag) {
			super("Expense Update");
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
			cmbBoxRegion = new JComboBox( new String[]{});		
			cmbBoxRegion.setEditable(true);
			
			if(userName.matches("Hakan KAYAKUTLU|Halit Gokmen|Камаева Марина Сергеевна|Эртюрк Мурат Хакан")){					
				ConnectToDb.getPRMDataGroupBy("country", "solgar_prm.prm_address_group",cmbBoxCountry,"","");	
				cmbBoxCountry.setMaximumRowCount(50);
				cmbBoxCountry.setEditable(true);
				cmbBoxCountry.setSelectedIndex(-1);
				ConnectToDb.getAllExpenses(resultTable,1,"");
			}else if(userName.matches("Шарыпова Сюзанна Николаевна")){
				cmbBoxCountry.addItem("Moscow");
				cmbBoxCountry.setEnabled(false);
				cmbBoxCountry.setEditable(false);
				ConnectToDb.getPRMDataGroupBy("region", "solgar_prm.prm_address_group",cmbBoxRegion,"country",cmbBoxCountry.getSelectedItem().toString());
				ConnectToDb.getAllExpenses(resultTable,1,"Moscow");
			}else if(userName.matches("Копрова Ксения Олеговна")){
				cmbBoxCountry.addItem("Saint Petersburg");
				cmbBoxCountry.setEnabled(false);
				cmbBoxCountry.setEditable(false);
				ConnectToDb.getPRMDataGroupBy("region", "solgar_prm.prm_address_group",cmbBoxRegion,"country",cmbBoxCountry.getSelectedItem().toString());
				ConnectToDb.getAllExpenses(resultTable,1,"Saint Petersburg");
			}else if(userName.matches("Зайцева Дарья Андреевна")){
				cmbBoxCountry.addItem("Region");
				cmbBoxCountry.setEnabled(false);
				cmbBoxCountry.setEditable(false);
				ConnectToDb.getPRMDataGroupBy("region", "solgar_prm.prm_address_group",cmbBoxRegion,"country",cmbBoxCountry.getSelectedItem().toString());
				ConnectToDb.getAllExpenses(resultTable,1,"Region");
			}else{
				cmbBoxCountry.addItem("No Authorization");
				cmbBoxCountry.setEnabled(false);
				cmbBoxCountry.setEditable(false);
			}
			
			cmbBoxCity = new JComboBox( new String[]{});		
			cmbBoxCity.setEditable(true);
			
			cmbBoxCityRegion = new JComboBox( new String[]{});		
			cmbBoxCityRegion.setEditable(true);
			
			cmbBoxExpMain = new JComboBox( new String[]{});		
			ConnectToDb.getPRMDataGroupBy("main_name", "solgar_prm.prm_exps_types",cmbBoxExpMain,"","");	
			cmbBoxExpMain.setMaximumRowCount(50);
			cmbBoxExpMain.setEditable(true);
			cmbBoxExpMain.setSelectedIndex(-1);
			
			cmbBoxExpLevel1 = new JComboBox( new String[]{});		
			cmbBoxExpLevel1.setEditable(true);
			
			cmbBoxExpLevel2 = new JComboBox( new String[]{});		
			cmbBoxExpLevel2.setEditable(true);
			
			cmbBoxCompanyCode = new JComboBox( new String[]{});		
			cmbBoxCompanyCode.addItem("SOLGAR");
			cmbBoxCompanyCode.addItem("NATURES BOUNTY");
			cmbBoxCompanyCode.setEditable(true);
			cmbBoxCompanyCode.setEnabled(false);
			
			
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
			txtId = new JTextField();
			txtId.setEnabled(false);
			
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
			
			
			//Buttons
			btnAdd = new JButton("Add");
			btnDelete = new JButton("Delete");
			btnUpdate = new JButton("Update");		
			btnExit = new JButton("Exit");
			btnSave = new JButton("Save");
			btnSave.setEnabled(false);
			btnAdd.setEnabled(false);
			btnUpdate.setEnabled(false);
			btnDelete.setEnabled(false);
			
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
			paramPanelExpParRek.add(lblId);
			paramPanelExpParRek.add(txtId);
			paramPanelExpParRek.setVisible(false);
			
			paramPanelExpParPos.add(lblExpPosProduct );
			paramPanelExpParPos.add(txtExpPosProduct);
			paramPanelExpParPos.add(lblExpPosParyadk);
			paramPanelExpParPos.add(txtExpPosParyadk);
			paramPanelExpParPos.add(lblExpPosStatus);
			paramPanelExpParPos.add(txtExpPosStatus);
			paramPanelExpParPos.setVisible(false);
			
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
					
				}else if (e.getActionCommand().equals("Update")) {
					int i = resultTable.getSelectedRow();
					if (i >= 0)
					{	
						boolean amountControl = amountControl(txtAmountFormat.getText(),txtAmount1.getText(),txtAmount2.getText(),
								txtAmount3.getText(),txtAmount4.getText(),txtAmount5.getText());
						if(amountControl){
							resultTable.setValueAt(cmbBoxCompanyCode.getSelectedItem(), i, 2);
							resultTable.setValueAt(cmbBoxCountry.getSelectedItem(), i, 7);
							resultTable.setValueAt(cmbBoxRegion.getSelectedItem(), i, 8);
							resultTable.setValueAt(cmbBoxCity.getSelectedItem(), i, 9);
							resultTable.setValueAt(cmbBoxCityRegion.getSelectedItem(), i, 10);
							resultTable.setValueAt(startDate.getText(), i, 11);
							resultTable.setValueAt(endDate.getText(), i, 12);
							resultTable.setValueAt(cmbBoxExpMain.getSelectedItem(), i, 13);
							resultTable.setValueAt(cmbBoxExpLevel1.getSelectedItem(), i, 14);
							resultTable.setValueAt(cmbBoxExpLevel2.getSelectedItem(), i, 15);
							
							if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("Мероприятия")){
								resultTable.setValueAt(cmbBoxExpMerLecture.getSelectedItem(), i, 16);
								resultTable.setValueAt(cmbBoxExpMerOrganizator.getSelectedItem(), i, 17);
								resultTable.setValueAt(txtExpMerTema.getText(), i, 18);
								resultTable.setValueAt(txtExpMerUcastnik.getText(), i, 19);
				    		  }else if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("Реклама")){	 
				    			  resultTable.setValueAt(txtExpRekUsloviya.getText(), i, 16);
				    			  resultTable.setValueAt(txtExpRekProduct.getText(), i, 17);
				    			  resultTable.setValueAt(txtExpRekChain.getText(), i, 18);
				    			  resultTable.setValueAt("", i, 19);
				    		  }else if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("POS-материалы")){   
				    			  resultTable.setValueAt(txtExpPosProduct.getText(), i, 16);
				    			  resultTable.setValueAt(txtExpPosParyadk.getText(), i, 17);
				    			  resultTable.setValueAt(txtExpPosStatus.getText(), i, 18);
				    			  resultTable.setValueAt("", i, 19);
				    		  }
							resultTable.setValueAt(txtCountFormat.getText(), i, 20);
							resultTable.setValueAt(txtAmountFormat.getText(), i, 21);
							resultTable.setValueAt(textAreaComment.getText(), i, 22);	
							resultTable.setValueAt(txtAmount1.getText(), i, 23);
							resultTable.setValueAt(txtAmount2.getText(), i, 24);
							resultTable.setValueAt(txtAmount3.getText(), i, 25);
							resultTable.setValueAt(txtAmount4.getText(), i, 26);
							resultTable.setValueAt(txtAmount5.getText(), i, 27);
							btnSave.setEnabled(true);
						}else{
							JOptionPane.showMessageDialog(pnlInfoMsg, "Please check amounts expenses total amount should be equal to totatl amount", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}

					else {
						System.out.println("Update Error");
					}
				}else if (e.getActionCommand().equals("Delete")) {
					int i = resultTable.getSelectedRow();
					/*if (i >= 0) {
						model.removeRow(i);
					}
					else {
						System.out.println("Delete Error");
					}*/
				}else if (e.getActionCommand().equals("Save")) {				
					int i = resultTable.getSelectedRow();
					ConnectToDb.updateMarketingExpenses(resultTable,userName,i,txtId.getText().toString());	
					
					dtm.setColumnIdentifiers(header);
					resultTable.setModel(dtm);	
					
					btnSave.setEnabled(false);
					JOptionPane.showMessageDialog(pnlInfoMsg, "Expenses sent to approve", "Information", JOptionPane.INFORMATION_MESSAGE);			
					
					if(userName.matches("Hakan KAYAKUTLU|Halit Gokmen|Камаева Марина Сергеевна|Эртюрк Мурат Хакан")){					
						ConnectToDb.getAllExpenses(resultTable,1,"");
					}else if(userName.matches("Шарыпова Сюзанна Николаевна")){
						ConnectToDb.getAllExpenses(resultTable,1,"Moscow");
					}else if(userName.matches("Копрова Ксения Олеговна")){
						ConnectToDb.getAllExpenses(resultTable,1,"Saint Petersburg");
					}else if(userName.matches("Зайцева Дарья Андреевна")){
						ConnectToDb.getAllExpenses(resultTable,1,"Region");
					}else{
						cmbBoxCountry.addItem("No Authorization");
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
		    		  if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("Мероприятия")){
		    			  paramPanelExpParMer.setVisible(true);
		    			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerLecture,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
		    					  "country",cmbBoxCountry.getSelectedItem().toString());
		    			  ConnectToDb.getPRMDataTwoConditionsGroupBy("lecture", "solgar_prm.prm_exps_lectures",cmbBoxExpMerOrganizator,"company_name",cmbBoxCompanyCode.getSelectedItem().toString(),
		    					  "country",cmbBoxCountry.getSelectedItem().toString());
		    			  paramPanelExpParRek.setVisible(false);
		    			  paramPanelExpParPos.setVisible(false);
		    		  }else if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("Реклама")){
		    			  paramPanelExpParMer.setVisible(false);
		    			  paramPanelExpParRek.setVisible(true);
		    			  paramPanelExpParPos.setVisible(false);
		    		  }else if(cmbBoxExpMain.getSelectedItem().toString().equalsIgnoreCase("POS-материалы")){
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
						cmbBoxExpMerLecture.setSelectedItem(resultTable.getValueAt(i, 16).toString());
					}else{
						cmbBoxExpMerLecture.setSelectedItem("");
					}
					if(resultTable.getValueAt(i, 17) != null){
						cmbBoxExpMerOrganizator.setSelectedItem(resultTable.getValueAt(i, 17).toString());
					}else{
						cmbBoxExpMerOrganizator.setSelectedItem("");
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
				 btnUpdate.setEnabled(true);
					//btnDelete.setEnabled(false);
				
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
		
		
	}
