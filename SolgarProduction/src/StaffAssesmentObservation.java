	package src;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import cb.esi.esiclient.util.ESIBag;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;

import main.ConnectToDb;
import main.Dispatcher;
import util.Util;

import javax.swing.ScrollPaneConstants;

	public class StaffAssesmentObservation extends JFrame implements ActionListener,ItemListener,MouseListener{

		private String userName="Hakan KAYAKUTLU";
		private String userBrand="ALL";
		private String userCountry="ALL";
		private String userArea="ALL";
		
		private JPanel contentPane,pnlValues,pnlButton,pnlInfoMsg;
		private static final int FRAME_WIDTH = 1100;
		private static final int FRAME_HEIGHT = 750;
		private static final Calendar cal = Calendar.getInstance();
		public SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
		private JButton btnAdd,btnUpdate,btnDelete,btnSave,btnCleanUp,btnExit;
		private JTable tblPharmacyNum,tblDocNum,tblRealization,tblCalculation;
		private JPanel pnlPharmNum,pnlDocNum,pnlRealization,panel,pnlStaff;
		private JScrollPane scrollDocNum,scrollRealization,scrollCalculation,scrollResult,scrollPharmacyNum;
		private JComboBox cmbBoxBrand,cmbBoxCountry,cmbBoxRegion,cmbBoxCity,cmbBoxStaffName,cmbMonth,cmbYear,cmbBoxBonusTypes,cmbPharmacyType,cmbDoctorType,
		cmbBoxYear,cmbBoxMonth,cmbBoxId;
		
		private DefaultTableModel modelResult = new DefaultTableModel();
		final DefaultTableModel modelPharmacy = new DefaultTableModel();
		final DefaultTableModel modelDoctor = new DefaultTableModel();
		final DefaultTableModel modelRelease = new DefaultTableModel();
		final DefaultTableModel modelCalculate = new DefaultTableModel();
		String headerResult[] = new String[] {""};
		String headerPharmacy[] = new String[] {"Id", "Pharmacy Type", "Sum"};
		String headerDoctor[] = new String[] {"Id", "Doctor Type", "Total"};
		String headerRelease[] = new String[] {"Id", "BonusType", "Plan", "Realization", "Total"};
		String headerCalculate[] = new String[] {"Id", "BonusType", "Sum"};
		private JTextField txtMonVisit;
		private JTextField txtWorkingDay;
		private JTable tblResult;

		/**
		 * Launch the application.
		 */
		public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						ESIBag inBag = new ESIBag();
						StaffAssesmentObservation frame = new StaffAssesmentObservation(inBag);
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		/**
		 * Create the frame.
		 * @throws SQLException 
		 * @throws Exception 
		 */
		public StaffAssesmentObservation(ESIBag inBag) throws SQLException {
			super("Assesment Observation");
			Toolkit toolkit;
			Dimension dim;
			int screenHeight, screenWidth;
			setBackground(Color.lightGray);
			toolkit = Toolkit.getDefaultToolkit();
			dim = toolkit.getScreenSize();
			screenHeight = dim.height;
			screenWidth = dim.width;
			setBounds((screenWidth - FRAME_WIDTH) / 2, (screenHeight - FRAME_HEIGHT) / 2, FRAME_WIDTH, FRAME_HEIGHT);
			
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			contentPane = new JPanel();
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			
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
			
			pnlValues = new JPanel();
			pnlValues.setBorder(new TitledBorder(null, "Inputs", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
			pnlValues.setBounds(10, 6, 1064, 485);
			getContentPane().add(pnlValues);
			pnlValues.setLayout(null);
			
			pnlPharmNum = new JPanel();
			pnlPharmNum.setBorder(new TitledBorder(null, "Actual Pharmacy Number", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlPharmNum.setBounds(10, 143, 368, 167);
			pnlValues.add(pnlPharmNum);
			pnlPharmNum.setLayout(null);
			
			scrollPharmacyNum = new JScrollPane();
			scrollPharmacyNum.setBounds(10, 22, 350, 134);
			pnlPharmNum.add(scrollPharmacyNum);
	
			cmbPharmacyType = new JComboBox();
			cmbPharmacyType.addItem("A");
			cmbPharmacyType.addItem("A+");
			cmbPharmacyType.addItem("B");
			cmbPharmacyType.addItem("B+");
			cmbPharmacyType.addItem("C");
			cmbPharmacyType.addItem("C+");
			
			cmbDoctorType = new JComboBox();
			cmbDoctorType.addItem("A");
			cmbDoctorType.addItem("B");
		
			cmbBoxBonusTypes = new JComboBox( new String[]{});
			Util.getPRMData("bonusType", "solgar_prm.prm_assesment_bonus_types",cmbBoxBonusTypes);		
			
			tblPharmacyNum = new JTable();
			tblPharmacyNum.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
				},
				new String[] {
					"Id", "Pharmacy Type", "Sum"
				}
			) {
				Class[] columnTypes = new Class[] {
					Integer.class, Object.class, Object.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
			tblPharmacyNum.getColumnModel().getColumn(0).setPreferredWidth(41);
			tblPharmacyNum.getColumnModel().getColumn(1).setPreferredWidth(90);
			tblPharmacyNum.getColumnModel().getColumn(2).setPreferredWidth(83);
			tblPharmacyNum.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(cmbPharmacyType));
			scrollPharmacyNum.setViewportView(tblPharmacyNum);
			
			pnlDocNum = new JPanel();
			pnlDocNum.setBorder(new TitledBorder(null, "Actual Doctor Number", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlDocNum.setBounds(10, 321, 368, 153);
			pnlValues.add(pnlDocNum);
			pnlDocNum.setLayout(null);
			
			scrollDocNum = new JScrollPane();
			scrollDocNum.setBounds(10, 24, 351, 118);
			pnlDocNum.add(scrollDocNum);
			
			tblDocNum = new JTable();
			tblDocNum.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
				},
				new String[] {
					"Id", "Doctor Type", "Total"
				}
			));
			tblDocNum.getColumnModel().getColumn(0).setPreferredWidth(28);
			tblDocNum.getColumnModel().getColumn(1).setPreferredWidth(76);
			tblDocNum.getColumnModel().getColumn(2).setPreferredWidth(63);
			tblDocNum.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(cmbDoctorType));
			scrollDocNum.setViewportView(tblDocNum);
			
			pnlRealization = new JPanel();
			pnlRealization.setBorder(new TitledBorder(null, "Release", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlRealization.setBounds(388, 143, 443, 331);
			pnlValues.add(pnlRealization);
			pnlRealization.setLayout(null);
			
			scrollRealization = new JScrollPane();
			scrollRealization.setBounds(10, 29, 423, 291);
			pnlRealization.add(scrollRealization);
			
			tblRealization = new JTable();
			tblRealization.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
					{null, null, null, null, null},
				},
				new String[] {
					"Id", "BonusType", "Plan", "Realization", "Total"
				}
			));
			tblRealization.getColumnModel().getColumn(0).setPreferredWidth(32);
			tblRealization.getColumnModel().getColumn(1).setPreferredWidth(102);
			tblRealization.getColumnModel().getColumn(2).setPreferredWidth(59);
			tblRealization.getColumnModel().getColumn(3).setPreferredWidth(73);
			tblRealization.getColumnModel().getColumn(4).setPreferredWidth(57);
			tblRealization.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(cmbBoxBonusTypes));
			scrollRealization.setViewportView(tblRealization);
			
			panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Calculation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel.setBounds(840, 143, 214, 331);
			pnlValues.add(panel);
			panel.setLayout(null);
			
			scrollCalculation = new JScrollPane();
			scrollCalculation.setBounds(10, 29, 194, 291);
			panel.add(scrollCalculation);
			
			tblCalculation = new JTable();
			tblCalculation.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
				},
				new String[] {
					"Id", "BonusType", "Sum"
				}
			));
			tblCalculation.getColumnModel().getColumn(0).setPreferredWidth(23);
			tblCalculation.getColumnModel().getColumn(1).setPreferredWidth(83);
			tblCalculation.getColumnModel().getColumn(2).setPreferredWidth(59);
			tblCalculation.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(cmbBoxBonusTypes));
			scrollCalculation.setViewportView(tblCalculation);
			
			pnlStaff = new JPanel();
			pnlStaff.setBorder(new TitledBorder(null, "Staff Info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlStaff.setBounds(10, 28, 1007, 104);
			pnlValues.add(pnlStaff);
			pnlStaff.setLayout(null);
			
			JLabel lblBrand = new JLabel("Brand");
			lblBrand.setBounds(22, 21, 76, 14);
			pnlStaff.add(lblBrand);
			
			JLabel lblCountry = new JLabel("Country");
			lblCountry.setBounds(22, 46, 46, 14);
			pnlStaff.add(lblCountry);
			
			JLabel lblRegion = new JLabel("Region");
			lblRegion.setBounds(314, 21, 88, 14);
			pnlStaff.add(lblRegion);
			
			JLabel lblCity = new JLabel("City");
			lblCity.setBounds(314, 46, 65, 14);
			pnlStaff.add(lblCity);
			
			JLabel lblName = new JLabel("Staff Name");
			lblName.setBounds(595, 21, 105, 14);
			pnlStaff.add(lblName);
			
			cmbBoxBrand = new JComboBox();
			cmbBoxBrand.setBounds(90, 18, 158, 20);
			pnlStaff.add(cmbBoxBrand);
			
			if(userBrand.equalsIgnoreCase("ALL")){
				cmbBoxBrand.addItem("SOLGAR");
				cmbBoxBrand.addItem("NATURES BOUNTY");
				cmbBoxBrand.setSelectedIndex(0);
				cmbBoxBrand.setEnabled(true);
			}else if(userBrand.equalsIgnoreCase("SOLGAR")){
				cmbBoxBrand.addItem("SOLGAR");
				cmbBoxBrand.setSelectedIndex(0);
			}else if(userBrand.equalsIgnoreCase("BOUNTY")){
				cmbBoxBrand.addItem("NATURES BOUNTY");
				cmbBoxBrand.setSelectedIndex(0);
			}
			
			
			cmbBoxCountry = new JComboBox( new String[]{});
			cmbBoxCountry.setBounds(90, 43, 158, 20);
			pnlStaff.add(cmbBoxCountry);
			
			cmbBoxRegion = new JComboBox( new String[]{});
			cmbBoxRegion.setBounds(374, 18, 158, 20);
			pnlStaff.add(cmbBoxRegion);
			
			cmbBoxCity = new JComboBox( new String[]{});
			cmbBoxCity.setBounds(374, 43, 158, 20);
			pnlStaff.add(cmbBoxCity);
			
			cmbBoxStaffName = new JComboBox( new String[]{});
			cmbBoxStaffName.setBounds(696, 18, 224, 20);
			pnlStaff.add(cmbBoxStaffName);
			
			
			if(userCountry.equalsIgnoreCase("ALL")){
				Util.getPRMDataGroupBy("country", "solgar_prm.prm_assesment_staff_info",cmbBoxCountry,"","");	
				cmbBoxCountry.setMaximumRowCount(50);
				cmbBoxCountry.setSelectedIndex(-1);
				cmbBoxCountry.setEnabled(true);
				cmbBoxRegion.setEnabled(true);
			}else if(userCountry.equalsIgnoreCase("Russia")){
				cmbBoxCountry.addItem("Russia");
				cmbBoxCountry.setSelectedIndex(0);
				if(userArea.equalsIgnoreCase("ALL")){
					Util.getPRMDataGroupBy("region", "solgar_prm.prm_assesment_staff_info",cmbBoxRegion,"country",cmbBoxCountry.getSelectedItem().toString());
					cmbBoxRegion.setSelectedIndex(0);
					cmbBoxRegion.setEnabled(true);				
				}else{
					if(userArea.equalsIgnoreCase("Moscow")){
						cmbBoxRegion.addItem("Moscow");									
					}else if(userArea.equalsIgnoreCase("Region")){
						cmbBoxRegion.addItem("Region");
					}else if(userArea.equalsIgnoreCase("Saint Petersburg")){
						cmbBoxRegion.addItem("Saint Petersburg");
					}
					cmbBoxRegion.setSelectedIndex(0);
					Util.getPRMDataGroupBy("city", "solgar_prm.prm_assesment_staff_info",cmbBoxCity,"region",cmbBoxRegion.getSelectedItem().toString());
				}
				
			}else if(userCountry.equalsIgnoreCase("Ukraine")){
				cmbBoxCountry.addItem("Ukraine");
				cmbBoxCountry.setSelectedIndex(0);
				if(userArea.equalsIgnoreCase("ALL")){
					Util.getPRMDataGroupBy("region", "solgar_prm.prm_assesment_staff_info",cmbBoxRegion,"country",cmbBoxCountry.getSelectedItem().toString());
					cmbBoxRegion.setSelectedIndex(0);
					cmbBoxRegion.setEnabled(true);				
				}else if(userArea.equalsIgnoreCase("Kiev")){
					cmbBoxRegion.addItem("Kiev");
					cmbBoxRegion.setSelectedIndex(0);
					Util.getPRMDataGroupBy("city", "solgar_prm.prm_assesment_staff_info",cmbBoxCity,"region",cmbBoxRegion.getSelectedItem().toString());
				}
			}
			
			pnlButton = new JPanel();
			pnlButton.setLayout(null);
			pnlButton.setBorder(new TitledBorder(null, "Button Group", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
			pnlButton.setBounds(10, 639, 1064, 62);
			getContentPane().add(pnlButton);
			
			btnAdd = new JButton("ADD");
			btnAdd.setBounds(50, 28, 139, 23);
			pnlButton.add(btnAdd);
			
			btnUpdate = new JButton("UPDATE");
			btnUpdate.setBounds(221, 28, 132, 23);
			pnlButton.add(btnUpdate);
			
			btnDelete = new JButton("DELETE");
			btnDelete.setBounds(377, 28, 132, 23);
			pnlButton.add(btnDelete);
			
			btnSave = new JButton("SAVE");
			btnSave.setBounds(537, 28, 139, 23);
			pnlButton.add(btnSave);
			
			btnCleanUp = new JButton("CLEANUP");
			btnCleanUp.setBounds(701, 28, 139, 23);
			pnlButton.add(btnCleanUp);
			
			btnExit = new JButton("EXIT");
			btnExit.setBounds(885, 28, 132, 23);
			pnlButton.add(btnExit);
			
			cmbBoxCountry.setName("Country");
			cmbBoxRegion.setName("Region");
			cmbBoxCity.setName("City");
			cmbBoxStaffName.setName("Staff");
			
			JLabel lblYear = new JLabel("Year");
			lblYear.setBounds(22, 79, 46, 14);
			pnlStaff.add(lblYear);
			
			JLabel lblMonth = new JLabel("Month");
			lblMonth.setBounds(312, 79, 46, 14);
			pnlStaff.add(lblMonth);
			
			cmbBoxYear = new JComboBox();
			cmbBoxYear.addItem("2017");
			cmbBoxYear.addItem("2018");
			cmbBoxYear.setSelectedIndex(-1);
			cmbBoxYear.setBounds(90, 74, 158, 20);
			pnlStaff.add(cmbBoxYear);
			
			cmbBoxMonth = new JComboBox();
			cmbBoxMonth.addItem("JAN");
			cmbBoxMonth.addItem("FEB");
			cmbBoxMonth.addItem("MAR");
			cmbBoxMonth.addItem("APR");
			cmbBoxMonth.addItem("MAY");
			cmbBoxMonth.addItem("JUN");
			cmbBoxMonth.addItem("JUL");
			cmbBoxMonth.addItem("AUG");
			cmbBoxMonth.addItem("SEP");
			cmbBoxMonth.addItem("OCT");
			cmbBoxMonth.addItem("NOW");
			cmbBoxMonth.addItem("DEC");
			cmbBoxMonth.setBounds(374, 76, 158, 20);
			cmbBoxMonth.setSelectedIndex(-1);
			pnlStaff.add(cmbBoxMonth);
	
			
			JLabel lblMonVisit = new JLabel("Monthly Visit");
			lblMonVisit.setBounds(595, 46, 105, 14);
			pnlStaff.add(lblMonVisit);
			
			JLabel lblWorkingDay = new JLabel("Working Day");
			lblWorkingDay.setBounds(595, 77, 105, 14);
			pnlStaff.add(lblWorkingDay);
			
			txtMonVisit = new JTextField();
			txtMonVisit.setBounds(696, 43, 86, 20);
			pnlStaff.add(txtMonVisit);
			txtMonVisit.setColumns(10);
			
			txtWorkingDay = new JTextField();
			txtWorkingDay.setText("");
			txtWorkingDay.setBounds(696, 76, 86, 20);
			pnlStaff.add(txtWorkingDay);
			txtWorkingDay.setColumns(10);
			
			cmbBoxId = new JComboBox();
			cmbBoxId.setEnabled(false);
			cmbBoxId.setBounds(950, 78, 47, 17);
			pnlStaff.add(cmbBoxId);
			
			JPanel pnlResult = new JPanel();
			pnlResult.setBounds(10, 507, 1064, 121);
			getContentPane().add(pnlResult);
			pnlResult.setLayout(null);
			
			scrollResult = new JScrollPane();
			scrollResult.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollResult.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollResult.setBounds(10, 11, 1044, 99);
			pnlResult.add(scrollResult);
			
			tblResult = new JTable(modelResult);
			tblResult.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			scrollResult.setViewportView(tblResult);
			
			tblResult.setName("ResultTabel");
			
			btnAdd.setEnabled(false);
			btnDelete.setEnabled(false);
			btnUpdate.setEnabled(false);
			btnUpdate.setEnabled(false);
			btnSave.setEnabled(false);
			
			/*Listeners*/
			cmbBoxCountry.addItemListener(this);			
			cmbBoxRegion.addItemListener(this);
			cmbBoxCity.addItemListener(this);
			cmbBoxStaffName.addItemListener(this);
			
			btnAdd.addActionListener(this);	
			btnDelete.addActionListener(this);
			btnUpdate.addActionListener(this);
			btnSave.addActionListener(this);
			btnExit.addActionListener(this);
			btnCleanUp.addActionListener(this);
			
			tblResult.addMouseListener(this);
			
  		    
  		    try{
  		    	inBag.put("EMPLOYEEID", "-1");
  		    	modelResult = Dispatcher.getAssesmentsAllData(inBag);
  		    }catch (Exception e) {
				// TODO: handle exception
			}
			tblResult.setModel(modelResult);
			
			contentPane.setLayout(null);
			
			validate();
			setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				if (e.getActionCommand().equals("ADD")) {
					
					
				}else if (e.getActionCommand().equals("UPDATE")) {
									
				}else if (e.getActionCommand().equals("DELETE")) {
					
				}else if (e.getActionCommand().equals("SAVE")) {
					
					
				}else if (e.getActionCommand().equals("CLEANUP")) {
					cleanUp();
				}else if (e.getActionCommand().equals("EXIT")) {
					setVisible(false);
				}
			} catch (Exception e2) {
				try {
					throw e2;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		public void itemStateChanged(ItemEvent itemEvent) {
	    	  JComboBox cmbBox = (JComboBox)itemEvent.getSource();
	    	  String name = cmbBox.getName();
	    	  if(cmbBox.getSelectedItem() != null &&cmbBox.getSelectedItem().toString().length()>0){
		    	  if(name.equalsIgnoreCase("Country")){
		    		  cmbBoxRegion.removeAllItems();
		    		  cmbBoxCity.removeAllItems();	
		    		  cmbBoxStaffName.removeAllItems();	
		    		  try {
						Util.getPRMDataGroupBy("region", "solgar_prm.prm_assesment_staff_info",cmbBoxRegion,"country",cmbBoxCountry.getSelectedItem().toString());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	    		  
		    		  cmbBoxRegion.setSelectedIndex(-1);
		    		  cmbBoxCity.setSelectedIndex(-1);
		    		  cmbBoxStaffName.setSelectedIndex(-1);
		    	  }else if(name.equalsIgnoreCase("Region")){
		    		  cmbBoxCity.removeAllItems();	
		    		  cmbBoxStaffName.removeAllItems();
		    		  try {
						Util.getPRMDataGroupBy("city", "solgar_prm.prm_assesment_staff_info",cmbBoxCity,"region",cmbBoxRegion.getSelectedItem().toString());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		  cmbBoxCity.setSelectedIndex(-1);	  
		    		  cmbBoxStaffName.setSelectedIndex(-1);
		    	  }else if(name.equalsIgnoreCase("City")){
		    		  cmbBoxStaffName.removeAllItems();
		    		  if(cmbBoxCity.getSelectedItem()!= null && cmbBoxCity.getSelectedItem().toString().length()>0){
		    			  try {
							Util.getPRMDataGroupBy("employeeName", "solgar_prm.prm_assesment_staff_info",cmbBoxStaffName,"city",cmbBoxCity.getSelectedItem().toString());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    		  }
		    		  cmbBoxStaffName.setSelectedIndex(-1);
		    	  }else if(name.equalsIgnoreCase("Staff")){
		    		  cmbBoxId.removeAllItems();
		    		  try {
						Util.getPRMDataGroupBy("id", "solgar_prm.prm_assesment_staff_info",cmbBoxId,"employeeName",cmbBoxStaffName.getSelectedItem().toString());
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
		    		  cmbBoxId.setSelectedIndex(0);		    		 
		    		  try{
		    			  if(cmbBoxStaffName.getSelectedItem()!= null && cmbBoxStaffName.getSelectedItem().toString().length()>0){
			    			  ESIBag inBag = new ESIBag();
				    		  inBag.put("EMPLOYEEID", cmbBoxId.getSelectedItem().toString());
				    		  //modelResult = Dispatcher.getAssesmentsAllData(inBag);
			    			  //tblResult.setModel(modelResult);
		    			  }
		    		  }catch (Exception e){
		    			  try {
							throw e;
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		    		  }
		    		  
		    	  }
	    	  }
	           
	      }
		private void cleanUp() {		
			
			cmbBoxBrand.setSelectedItem("");	    	
	    	cmbBoxCountry.setSelectedItem("");	    	
	    	cmbBoxRegion.setSelectedItem("");
	    	cmbBoxCity.setSelectedItem("");
	    	cmbBoxStaffName.setSelectedItem("");
	    	cmbBoxYear.setSelectedItem("");
	    	cmbBoxMonth.setSelectedItem("");
	    	cmbBoxId.setSelectedItem("");	    	
	    	txtMonVisit.setText("");
	    	txtWorkingDay.setText("");
			
			for( int j = modelPharmacy.getRowCount() - 1; j >= 0; j-- ) {
				modelPharmacy.removeRow(j);
			}	    	
	    	for( int j = modelDoctor.getRowCount() - 1; j >= 0; j-- ) {
	    		modelDoctor.removeRow(j);
			}
	    	for( int j = modelRelease.getRowCount() - 1; j >= 0; j-- ) {
	    		modelRelease.removeRow(j);
			}
	    	for( int j = modelCalculate.getRowCount() - 1; j >= 0; j-- ) {
	    		modelCalculate.removeRow(j);
			}
			
		}
		
		
		private void setAssesmentBaseInfo(ESIBag tempBag) {
		    try{	    	
		    	cmbBoxBrand.setSelectedItem(tempBag.get("BRAND")!= null ? tempBag.get("BRAND").toString() : "");
		    	cmbBoxCountry.setSelectedItem(tempBag.get("COUNTRY")!= null ? tempBag.get("COUNTRY").toString() : "");
		    	cmbBoxRegion.setSelectedItem(tempBag.get("REGION")!= null ? tempBag.get("REGION").toString() : "");
		    	cmbBoxCity.setSelectedItem(tempBag.get("CITY")!= null ? tempBag.get("CITY").toString() : "");
		     	cmbBoxStaffName.setSelectedItem(tempBag.get("STAFFNAME")!= null ? tempBag.get("STAFFNAME").toString() : "");
		     	cmbBoxYear.setSelectedItem(tempBag.get("YEAR")!= null ? tempBag.get("YEAR").toString() : "");
		     	cmbBoxMonth.setSelectedItem(tempBag.get("MONTH")!= null ? tempBag.get("MONTH").toString() : "");
		    	txtMonVisit.setText(tempBag.get("MONTHLYVISIT")!= null ? tempBag.get("MONTHLYVISIT").toString() : "");
		    	txtWorkingDay.setText(tempBag.get("WORKINGDAY")!= null ? tempBag.get("WORKINGDAY").toString() : "");
		    		
	    	}catch (Exception e) {
			// simdilik yoksa yok
	    	}
		}
		private void createTabelPharmacyModel(ESIBag tempBag) {
		    try{
				modelPharmacy.setColumnIdentifiers(headerPharmacy);
				tblPharmacyNum.setModel(modelPharmacy);
		    	
				for (int j = 0; j < tempBag.getSize("PHARMTABLE"); j++){
					modelPharmacy.addRow(new Object [] 
			        		{	tempBag.get("PHARMTABLE",j,"ID"),
			        			tempBag.get("PHARMTABLE",j,"PHARMACYTYPE"),
			        			tempBag.get("PHARMTABLE",j,"SUMTOTAL")			        			
			        		});		
				}
		    }catch (Exception e) {
				// simdilik yoksa yok
			}
		}
		private void createTabelDoctorModel(ESIBag tempBag) {
		    try{
				modelDoctor.setColumnIdentifiers(headerDoctor);
				tblDocNum.setModel(modelDoctor);
		    	
				for (int j = 0; j < tempBag.getSize("DOCTORTABLE"); j++){
					modelDoctor.addRow(new Object [] 
			        		{	tempBag.get("DOCTORTABLE",j,"ID"),
			        			tempBag.get("DOCTORTABLE",j,"DOCTORTYPE"),
			        			tempBag.get("DOCTORTABLE",j,"SUMTOTAL")			        			
			        		});		
				}
		    }catch (Exception e) {
				// simdilik yoksa yok
			}
		}
		private void createTabelReleaseModel(ESIBag tempBag) {
		    try{
				modelRelease.setColumnIdentifiers(headerRelease);
				tblRealization.setModel(modelRelease);
		    	
				for (int j = 0; j < tempBag.getSize("RELEASETABLE"); j++){
					modelRelease.addRow(new Object [] 
			        		{	tempBag.get("RELEASETABLE",j,"ID"),
			        			tempBag.get("RELEASETABLE",j,"BONUSTYPE"),
			        			tempBag.get("RELEASETABLE",j,"PLANS"),
			        			tempBag.get("RELEASETABLE",j,"REALIZATION"),
			        			tempBag.get("RELEASETABLE",j,"SUMTOTAL"),
			        		});		
				}
		    }catch (Exception e) {
				// simdilik yoksa yok
			}
		}

		private void createTabelCalculation(ESIBag tempBag) {
		    try{
				modelCalculate.setColumnIdentifiers(headerCalculate);
				tblCalculation.setModel(modelCalculate);
		    	
				for (int j = 0; j < tempBag.getSize("CALCTABLE"); j++){
					modelCalculate.addRow(new Object [] 
			        		{	tempBag.get("CALCTABLE",j,"ID"),
			        			tempBag.get("CALCTABLE",j,"BONUSTYPE"),
			        			tempBag.get("CALCTABLE",j,"SUMTOTAL")			        			
			        		});		
				}
		    }catch (Exception e) {
				// simdilik yoksa yok
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
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
			
			try {	
				if (e.getComponent().getName().equals("ResultTabel")) {	
					cleanUp();
					ESIBag tempBag = new ESIBag();			
					int i = tblResult.getSelectedRow();		
					tempBag.put("ID", tblResult.getValueAt(i, 0).toString());
					tempBag = Dispatcher.getAssesmentsToScreen(tempBag);
					setAssesmentBaseInfo(tempBag);
					createTabelDoctorModel(tempBag);
					createTabelPharmacyModel(tempBag);
					createTabelReleaseModel(tempBag);
					createTabelCalculation(tempBag);
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}