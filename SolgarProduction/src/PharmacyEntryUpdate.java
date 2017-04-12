	package src;

	import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

	import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;
import javax.swing.text.TableView;

	import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.JComboBox;

import main.ConnectToDb;
import main.Dispatcher;
import util.Util;

import javax.swing.JTextField;

import cb.esi.esiclient.util.ESIBag;

import com.toedter.calendar.JDateChooser;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.Font;

	public class PharmacyEntryUpdate extends JFrame implements ActionListener,ItemListener,MouseListener{

		private static final int FRAME_WIDTH = 1100;
		private static final int FRAME_HEIGHT = 750;
		private static final Calendar cal = Calendar.getInstance();
		
		private JPanel pnlInfoMsg;
		private String userName="Hakan KAYAKUTLU";
		private String userBrand="ALL";
		private String userCountry="ALL";
		private String userArea="ALL";	
		private String empid="-1";
		private JTextField txtPharmHeadName,txtPharmacyTel;
		private JComboBox cmbBoxCountry,cmbBoxArea,
		cmbBoxRegion,cmbBoxCity,cmbBoxCompanyCode,cmbBoxChain,cmbBoxDistrict,
		cmbBoxSubChain,cmbBoxMrktStaff,cmbBoxPharmCtgry,cmbBoxAssortiment,cmbBoxActiveness,cmbBoxPharmacyType,
		cmbBoxPromo,cmbBoxMetro;
		private JDateChooser agreementBeginDate;
		private static final DecimalFormat doubleFormatter = new DecimalFormat("#,###");
		private JTable tableResult;
		public JButton btnAdd,btnDelete,btnUpdate,btnSave,btnExit,btnCleanUp,btnGetAddress,btnListAptek;
		public SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
		public JTextArea txtAreaAddress;
		
		final DefaultTableModel modelResult = new DefaultTableModel();
		String headerResult[] = new String[] {"Id", "BRAND", "Country", "Area", "Region", "City", "District", "METRO", "GROUP COMPANY", "SUBGROUP COMPANY", "Marketing Staff", 
				"PHARMACY ADDRESS", "ASSORTMENT","PHARMACY CATEGORY","PHARMACY_TYPE", "PROMO", "Pharmacy Activeness", "Pharmacy Activation Date", "PHARM HEAD NAME", "Pharmacy Tel", "Pharmacy Email", 
				"Comments", "Full Address", "Building Type", "Country Code", "Administrative Area Name", "Sub Administrative Area Name", "Street", "Home Number", "point_y", "point_x","Pharmacy No","Entry User","Entry Date"};
		private JTextField txtPharmEmail,txtPointY,txtPointX,txtHomeNumber,txtStreet,txtBuildingType,
		txtSubArea,txtArea,txtCountryCode,txtOffAddress,txtAdditionalInfo,txtAptekaNo,txtAptekCount;
		private JTextField txtid;
		private String Lang = "RU";
		private ResourceBundle resourceBundle;
		/**
		 * Launch the application.
		 */
		public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						ESIBag inBag = new ESIBag();
						PharmacyEntryUpdate frame = new PharmacyEntryUpdate(inBag);
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
		 */
		public PharmacyEntryUpdate(ESIBag inBag) throws SQLException {
			super("Pharmacy Entry&Update");
			Toolkit toolkit;
			Dimension dim;
			int screenHeight, screenWidth;
			setBackground(Color.lightGray);
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
					empid = inBag.get("EMPLOYEEID").toString();
				}
				if (inBag.existsBagKey("LANGUAGE")) {
			    	  Lang = inBag.get("LANGUAGE").toString();
			      }
			    resourceBundle = ConnectToDb.readBundleFile(Lang);
				
			} catch (Exception e) {
				// simdilik yoksa yok
			}
			
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			
			/*Formatters*/
			NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMAN);
		    NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
		    numberFormatter.setValueClass(Integer.class);
		    numberFormatter.setMinimum(0);
		    numberFormatter.setMaximum(Integer.MAX_VALUE);
		    numberFormatter.setAllowsInvalid(false);
		    numberFormatter.setCommitsOnValidEdit(true);
		    
		    doubleFormatter.setMinimumIntegerDigits(0);
		    doubleFormatter.setMaximumIntegerDigits(8);
			
			JPanel pnlAddress = new JPanel();
			pnlAddress.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Global Address", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
			pnlAddress.setBounds(10, 11, 196, 207);
			getContentPane().add(pnlAddress);
			pnlAddress.setLayout(null);
			
			JLabel lblCountry = new JLabel(resourceBundle.getString("Country"));
			//lblCountry.setOpaque(true);
			lblCountry.setBounds(10, 56, 69, 14);
			pnlAddress.add(lblCountry);
			
			JLabel lblArea = new JLabel(resourceBundle.getString("Area"));
			//lblArea.setOpaque(true);
			lblArea.setBounds(10, 81, 56, 14);
			pnlAddress.add(lblArea);
			
			JLabel lblRegion = new JLabel(resourceBundle.getString("Region"));
			lblRegion.setBounds(10, 106, 46, 14);
			pnlAddress.add(lblRegion);
			
			JLabel lblCity = new JLabel(resourceBundle.getString("City"));
			lblCity.setBounds(10, 131, 46, 14);
			pnlAddress.add(lblCity);
			
			cmbBoxCountry = new JComboBox( new String[]{});
			cmbBoxCountry.setBounds(59, 55, 130, 17);
			cmbBoxCountry.setEnabled(false);
			pnlAddress.add(cmbBoxCountry);
			
			cmbBoxArea = new JComboBox( new String[]{});
			cmbBoxArea.setBounds(69, 81, 120, 17);
			cmbBoxArea.setEnabled(false);
			pnlAddress.add(cmbBoxArea);
			
			cmbBoxRegion = new JComboBox( new String[]{});
			cmbBoxRegion.setBounds(59, 106, 130, 17);
			pnlAddress.add(cmbBoxRegion);
			
			cmbBoxCity = new JComboBox( new String[]{});
			cmbBoxCity.setBounds(59, 128, 130, 17);
			pnlAddress.add(cmbBoxCity);
			
			if(userCountry.equalsIgnoreCase("ALL")){
				Util.getPRMDataGroupBy("country", "solgar_prm.prm_sales_addresses",cmbBoxCountry,"","");	
				cmbBoxCountry.setMaximumRowCount(50);
				cmbBoxCountry.setSelectedIndex(-1);
				cmbBoxCountry.setEnabled(true);
				cmbBoxArea.setEnabled(true);
			}else if(userCountry.equalsIgnoreCase("Russia")){
				cmbBoxCountry.addItem("Russia");
				cmbBoxCountry.setSelectedIndex(0);		
				Util.getPRMDataGroupBy("area", "solgar_prm.prm_sales_addresses",cmbBoxArea,"country",cmbBoxCountry.getSelectedItem().toString());
				cmbBoxArea.setSelectedIndex(-1);
				cmbBoxArea.setEnabled(true);
				/*if(userArea.equalsIgnoreCase("ALL")){
					Util.getPRMDataGroupBy("area", "solgar_prm.prm_sales_addresses",cmbBoxArea,"country",cmbBoxCountry.getSelectedItem().toString());
					cmbBoxArea.setSelectedIndex(0);
					cmbBoxArea.setEnabled(true);				
				}else if(userArea.equalsIgnoreCase("Moscow")){
					cmbBoxArea.addItem("Moscow");
					cmbBoxArea.setSelectedIndex(0);
					cmbBoxArea.setEnabled(true);
					Util.getPRMDataGroupBy("region", "solgar_prm.prm_sales_addresses",cmbBoxRegion,"area",cmbBoxArea.getSelectedItem().toString());				
				}else if(userArea.equalsIgnoreCase("Region")){
					cmbBoxArea.addItem("Region");
					cmbBoxArea.setSelectedIndex(0);
					Util.getPRMDataGroupBy("region", "solgar_prm.prm_sales_addresses",cmbBoxRegion,"area",cmbBoxArea.getSelectedItem().toString());
				}else if(userArea.equalsIgnoreCase("Saint Petersburg")){
					cmbBoxArea.addItem("Saint Petersburg");
					cmbBoxArea.setSelectedIndex(0);
					Util.getPRMDataGroupBy("region", "solgar_prm.prm_sales_addresses",cmbBoxRegion,"area",cmbBoxArea.getSelectedItem().toString());
				}*/
			}else if(userCountry.equalsIgnoreCase("Ukraine")){
				cmbBoxCountry.addItem("Ukraine");
				cmbBoxCountry.setSelectedIndex(0);
				if(userArea.equalsIgnoreCase("ALL")){
					Util.getPRMDataGroupBy("area", "solgar_prm.prm_sales_addresses",cmbBoxArea,"country",cmbBoxCountry.getSelectedItem().toString());
					cmbBoxArea.setSelectedIndex(-1);
					cmbBoxArea.setEnabled(true);				
				}else if(userArea.equalsIgnoreCase("Kiev")){
					cmbBoxArea.addItem("Kiev");
					cmbBoxArea.setSelectedIndex(0);
					Util.getPRMDataGroupBy("region", "solgar_prm.prm_sales_addresses",cmbBoxRegion,"area",cmbBoxArea.getSelectedItem().toString());
				}
			}	
			
			JLabel lblCompany = new JLabel(resourceBundle.getString("Company"));
			lblCompany.setBounds(10, 32, 66, 14);
			pnlAddress.add(lblCompany);

			cmbBoxCompanyCode = new JComboBox( new String[]{});			
			cmbBoxCompanyCode.setBounds(79, 29, 110, 17);
			cmbBoxCompanyCode.setEnabled(false);
			pnlAddress.add(cmbBoxCompanyCode);
			
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
			cmbBoxCompanyCode.addItemListener(this);
			cmbBoxCompanyCode.setName("cmbCompany");
			
			JPanel pnlAgreement = new JPanel();
			pnlAgreement.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Pharm Info", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
			pnlAgreement.setBounds(216, 11, 547, 207);
			getContentPane().add(pnlAgreement);
			pnlAgreement.setLayout(null);
			
			JLabel lblChainName = new JLabel(resourceBundle.getString("GroupCompany"));
			lblChainName.setBounds(10, 26, 120, 17);
			pnlAgreement.add(lblChainName);
			
			JLabel lblPharmTel = new JLabel(resourceBundle.getString("PharmacyTel"));
			lblPharmTel.setBounds(292, 98, 120, 17);
			pnlAgreement.add(lblPharmTel);
			
			JLabel lblPharmHeadName = new JLabel(resourceBundle.getString("PharmHeadName"));
			lblPharmHeadName.setBounds(292, 76, 120, 17);
			pnlAgreement.add(lblPharmHeadName);
			
			JLabel lblPharmActivationDate = new JLabel(resourceBundle.getString("PharmacyActivationDate"));
			lblPharmActivationDate.setBounds(10, 179, 158, 17);
			pnlAgreement.add(lblPharmActivationDate);
			
			JLabel lblPharmEmail = new JLabel(resourceBundle.getString("PharmEmail"));
			lblPharmEmail.setBounds(292, 123, 120, 17);
			pnlAgreement.add(lblPharmEmail);

			cmbBoxChain = new JComboBox( new String[]{});
			cmbBoxChain.addItem("");
			Util.getPRMData("group_company", "solgar_prm.prm_sales_chains",cmbBoxChain);		
			cmbBoxChain.setSelectedIndex(-1);
			cmbBoxChain.setBounds(138, 23, 140, 17);
			pnlAgreement.add(cmbBoxChain);
			
			txtPharmacyTel = new JTextField();
			txtPharmacyTel.setBounds(408, 95, 130, 17);
			txtPharmacyTel.setText("");
			pnlAgreement.add(txtPharmacyTel);
			
			txtPharmHeadName = new JTextField();
			txtPharmHeadName.setBounds(408, 73, 130, 17);
			pnlAgreement.add(txtPharmHeadName);
			txtPharmHeadName.setColumns(20);
			
			agreementBeginDate = new JDateChooser();
			agreementBeginDate.setBounds(178, 179, 100, 17);
			agreementBeginDate.setDateFormatString("yyyy-MM-dd");		
			agreementBeginDate.setDate(cal.getTime());
			pnlAgreement.add(agreementBeginDate);
			
			JLabel lblChainSub = new JLabel(resourceBundle.getString("SubGroupCompany"));
			lblChainSub.setBounds(10, 51, 120, 17);
			pnlAgreement.add(lblChainSub);
			
			cmbBoxSubChain = new JComboBox();
			cmbBoxSubChain.addItem("");
			Util.getPRMData("group_company", "solgar_prm.prm_sales_sub_chains",cmbBoxSubChain);		
			cmbBoxSubChain.setSelectedIndex(-1);
			cmbBoxSubChain.setBounds(138, 48, 140, 17);
			pnlAgreement.add(cmbBoxSubChain);
			
			JLabel lblMarketingStaff = new JLabel(resourceBundle.getString("MarketingStaff"));
			lblMarketingStaff.setBounds(10, 76, 120, 17);
			pnlAgreement.add(lblMarketingStaff);
			
			cmbBoxMrktStaff = new JComboBox();
			cmbBoxMrktStaff.addItem("");
			Util.getMedRep(empid, cmbBoxCompanyCode.getSelectedItem().toString(),cmbBoxMrktStaff);
			cmbBoxMrktStaff.setBounds(138, 73, 140, 17);
			pnlAgreement.add(cmbBoxMrktStaff);
			
			cmbBoxPharmCtgry = new JComboBox();
			cmbBoxPharmCtgry.addItem("");
			Util.getPRMDataGroupBy("pharmacycategory", "solgar_prm.prm_sales_pharmacycategory",cmbBoxPharmCtgry,"","");
			cmbBoxPharmCtgry.setSelectedIndex(-1);			
			cmbBoxPharmCtgry.setBounds(138, 95, 140, 17);
			pnlAgreement.add(cmbBoxPharmCtgry);
			
			JLabel lblPharmCtgry = new JLabel(resourceBundle.getString("PharmacyCategory"));
			lblPharmCtgry.setBounds(10, 101, 120, 17);
			pnlAgreement.add(lblPharmCtgry);
			
			JLabel lblAssortiment = new JLabel(resourceBundle.getString("Assortiment"));
			lblAssortiment.setBounds(10, 123, 120, 17);
			pnlAgreement.add(lblAssortiment);
			
			cmbBoxAssortiment = new JComboBox();
			cmbBoxAssortiment.addItem("");
			Util.getPRMDataGroupBy("assortiment", "solgar_prm.prm_sales_assortiment",cmbBoxAssortiment,"","");
			cmbBoxAssortiment.addItem("под заказ");
			cmbBoxAssortiment.addItem("assortiment");			
			cmbBoxAssortiment.setSelectedIndex(-1);
			cmbBoxAssortiment.setBounds(138, 120, 140, 17);
			pnlAgreement.add(cmbBoxAssortiment);
			
			JLabel lblPharmActiveness = new JLabel(resourceBundle.getString("PharmacyActiveness"));
			lblPharmActiveness.setBounds(10, 154, 120, 17);
			pnlAgreement.add(lblPharmActiveness);
			
			cmbBoxActiveness = new JComboBox();
			cmbBoxActiveness.addItem("");
			cmbBoxActiveness.addItem("Актив");
			cmbBoxActiveness.addItem("в процессе");
			cmbBoxActiveness.addItem("Не Актив");
			cmbBoxActiveness.setBounds(138, 151, 140, 17);
			pnlAgreement.add(cmbBoxActiveness);
			
			JLabel lblPharmacType = new JLabel(resourceBundle.getString("PharmacyType"));
			lblPharmacType.setBounds(292, 26, 120, 17);
			pnlAgreement.add(lblPharmacType);
			
			cmbBoxPharmacyType = new JComboBox();
			cmbBoxPharmacyType.addItem("");
			Util.getPRMDataGroupBy("pharmacytype", "solgar_prm.prm_sales_pharmacytype",cmbBoxPharmacyType,"","");
			cmbBoxPharmacyType.setSelectedIndex(-1);
			cmbBoxPharmacyType.setBounds(408, 23, 130, 17);
			pnlAgreement.add(cmbBoxPharmacyType);
			
			JLabel lblPromo = new JLabel(resourceBundle.getString("Promo"));
			lblPromo.setBounds(292, 51, 120, 17);
			pnlAgreement.add(lblPromo);
			
			cmbBoxPromo = new JComboBox();
			cmbBoxPromo.addItem("");
			Util.getPRMDataGroupBy("promo", "solgar_prm.prm_sales_promo",cmbBoxPromo,"","");
			cmbBoxPromo.setSelectedIndex(-1);
			cmbBoxPromo.setBounds(408, 48, 130, 17);
			pnlAgreement.add(cmbBoxPromo);
			
			txtPharmEmail = new JTextField();
			txtPharmEmail.setBounds(410, 120, 130, 17);
			pnlAgreement.add(txtPharmEmail);
			txtPharmEmail.setColumns(10);
			
			btnListAptek = new JButton(resourceBundle.getString("ListApteka"));
			btnListAptek.setActionCommand("List Apteka");
			btnListAptek.setForeground(Color.BLUE);
			btnListAptek.setBounds(408, 151, 130, 23);
			pnlAgreement.add(btnListAptek);
			
			txtid = new JTextField();
			txtid.setEnabled(false);
			txtid.setBounds(498, 177, 39, 20);
			pnlAgreement.add(txtid);
			txtid.setColumns(10);
			
			JLabel lblCount = new JLabel(resourceBundle.getString("Count"));
			lblCount.setBounds(291, 180, 65, 14);
			pnlAgreement.add(lblCount);
			
			txtAptekCount = new JTextField();
			txtAptekCount.setEnabled(false);
			txtAptekCount.setBounds(366, 177, 46, 20);
			pnlAgreement.add(txtAptekCount);
			txtAptekCount.setColumns(10);
			
			JPanel pnlResult = new JPanel();
			pnlResult.setBorder(new TitledBorder(null, "Result", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
			pnlResult.setBounds(10, 362, 1054, 279);
			getContentPane().add(pnlResult);
			pnlResult.setLayout(null);
			
			JScrollPane scrollResult = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollResult.setBounds(10, 33, 1034, 235);
			pnlResult.add(scrollResult);
			
			tableResult = new JTable(){
				public boolean isCellEditable(int row, int column){  
			    return false;  
			  }};	
			tableResult.setRowSelectionAllowed(true);
			tableResult.putClientProperty("terminateEditOnFocusLost", true);
			
			modelResult.setColumnIdentifiers(headerResult);
			tableResult.setModel(modelResult);
			tableResult.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);			
			scrollResult.setViewportView(tableResult);
			tableResult.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
			if (tableResult.isEditing())
				tableResult.getCellEditor().stopCellEditing();
			
			JPanel panelButon = new JPanel();
			panelButon.setBorder(new TitledBorder(null, "Button Group", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
			panelButon.setBounds(10, 639, 1064, 62);
			getContentPane().add(panelButon);
			panelButon.setLayout(null);
			
			btnAdd = new JButton(resourceBundle.getString("ADDAPTEKA"));
			btnAdd.setForeground(Color.RED);
			btnAdd.setActionCommand("ADD APTEKA");
			btnAdd.setBounds(50, 28, 150, 23);
			panelButon.add(btnAdd);
			
			btnUpdate = new JButton(resourceBundle.getString("UPDATE"));
			btnUpdate.setForeground(Color.RED);
			btnUpdate.setActionCommand("UPDATE");
			btnUpdate.setBounds(221, 28, 132, 23);
			panelButon.add(btnUpdate);
			
			btnDelete = new JButton(resourceBundle.getString("DELETE"));
			btnDelete.setForeground(Color.RED);
			btnDelete.setActionCommand("DELETE");
			btnDelete.setBounds(377, 28, 132, 23);
			panelButon.add(btnDelete);
			
			btnSave = new JButton(resourceBundle.getString("SAVE"));
			btnSave.setForeground(Color.RED);
			btnSave.setActionCommand("SAVE");
			btnSave.setBounds(537, 28, 139, 23);
			panelButon.add(btnSave);
			
			btnCleanUp = new JButton(resourceBundle.getString("CLEANUP"));
			btnCleanUp.setForeground(Color.RED);
			btnCleanUp.setActionCommand("CLEANUP");
			btnCleanUp.setBounds(701, 28, 139, 23);
			panelButon.add(btnCleanUp);
			
			btnExit = new JButton(resourceBundle.getString("EXIT"));
			btnExit.setForeground(Color.RED);
			btnExit.setActionCommand("EXIT");
			btnExit.setBounds(885, 28, 132, 23);
			panelButon.add(btnExit);
			
			/*Listeners*/
			cmbBoxCountry.setName("cmbCountry");
			cmbBoxCountry.addItemListener(this);
			cmbBoxArea.setName("cmbArea");
			cmbBoxArea.addItemListener(this);
			cmbBoxRegion.setName("cmbRegion");
			
			cmbBoxCity.addItemListener(this);
			cmbBoxCity.setName("cmbCity");
			
			JLabel lblDistrict = new JLabel(resourceBundle.getString("District"));
			lblDistrict.setBounds(10, 160, 46, 14);
			pnlAddress.add(lblDistrict);
			
			JLabel lblMetro = new JLabel(resourceBundle.getString("Metro"));
			lblMetro.setBounds(10, 185, 46, 14);
			pnlAddress.add(lblMetro);
			
			cmbBoxDistrict = new JComboBox();
			cmbBoxDistrict.addItem("");
			Util.getPRMDataGroupBy("district", "solgar_prm.prm_sales_district",cmbBoxDistrict,"","");
			cmbBoxDistrict.setSelectedIndex(-1);
			cmbBoxDistrict.setBounds(59, 156, 130, 20);
			pnlAddress.add(cmbBoxDistrict);
			
			cmbBoxMetro = new JComboBox();
			cmbBoxMetro.addItem("");
			Util.getPRMDataGroupBy("metro", "solgar_prm.prm_sales_metro",cmbBoxMetro,"","");
			cmbBoxMetro.setSelectedIndex(-1);
			cmbBoxMetro.setBounds(59, 182, 130, 17);
			pnlAddress.add(cmbBoxMetro);
			
			JPanel pnlAddr = new JPanel();
			pnlAddr.setBorder(new TitledBorder(null, resourceBundle.getString("PharmacyAddressInfo"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlAddr.setBounds(773, 12, 301, 207);
			getContentPane().add(pnlAddr);
			pnlAddr.setLayout(null);
			
			JLabel lblPharmAddress = new JLabel(resourceBundle.getString("PharmacyAddress"));
			lblPharmAddress.setBounds(5, 147, 103, 14);
			pnlAddr.add(lblPharmAddress);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(100, 120, 191, 74);
			pnlAddr.add(scrollPane);
			
			txtAreaAddress = new JTextArea();
			txtAreaAddress.setFont(new Font("Monospaced", Font.PLAIN, 12));
			scrollPane.setViewportView(txtAreaAddress);
			txtAreaAddress.setLineWrap(true);
			txtAreaAddress.setRows(2);
			
			JLabel lblAptekaNo = new JLabel(resourceBundle.getString("AptekaNo"));
			lblAptekaNo.setBounds(5, 25, 103, 14);
			pnlAddr.add(lblAptekaNo);
			
			txtAptekaNo = new JTextField();
			txtAptekaNo.setBounds(118, 22, 173, 20);
			pnlAddr.add(txtAptekaNo);
			txtAptekaNo.setColumns(10);
			
			JLabel lblAdditionalInfo = new JLabel(resourceBundle.getString("AdditionalInfo"));
			lblAdditionalInfo.setBounds(5, 50, 103, 14);
			pnlAddr.add(lblAdditionalInfo);
			
			txtAdditionalInfo = new JTextField();
			txtAdditionalInfo.setBounds(118, 47, 173, 20);
			pnlAddr.add(txtAdditionalInfo);
			txtAdditionalInfo.setColumns(10);
			
			JScrollPane scrollPane_1 = new JScrollPane();
			scrollPane_1.setBounds(5, 82, 286, 34);
			pnlAddr.add(scrollPane_1);
			
			JTextArea txtAreaAddExp = new JTextArea();
			txtAreaAddExp.setEditable(false);
			txtAreaAddExp.setEnabled(false);
			txtAreaAddExp.setFont(new Font("Monospaced", Font.PLAIN, 10));
			txtAreaAddExp.setText("\u0410\u0434\u0440\u0435\u0441 \u0434\u043E\u043B\u0436\u0435\u043D \u0432\u043A\u043B\u044E\u0447\u0435\u043D \u0432 \u0441\u043B\u0435\u0434\u0443\u044E\u0449\u0438\u0439 \u0444\u043E\u0440\u043C\u0430\u0442 (\u043E\u0431\u043B\u0430\u0441\u0442\u044C, \u0433\u043E\u0440\u043E\u0434, \u0434\u043E\u043C, \u043A\u0432\u0430\u0440\u0442\u0438\u0440\u0430, \u043A\u043E\u0440\u043F\u0443\u0441)");
			txtAreaAddExp.setLineWrap(true);
			scrollPane_1.setViewportView(txtAreaAddExp);
			
			JPanel pnlAddOfficial = new JPanel();
			pnlAddOfficial.setBorder(new TitledBorder(null, "Official Address Info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlAddOfficial.setBounds(10, 229, 1064, 122);
			getContentPane().add(pnlAddOfficial);
			pnlAddOfficial.setLayout(null);
			
			JLabel lblOffAddress = new JLabel(resourceBundle.getString("OfficialAddress"));
			lblOffAddress.setBounds(10, 24, 103, 14);
			pnlAddOfficial.add(lblOffAddress);
			
			txtOffAddress = new JTextField();
			txtOffAddress.setEditable(false);
			txtOffAddress.setEnabled(false);
			txtOffAddress.setBounds(123, 21, 653, 17);
			pnlAddOfficial.add(txtOffAddress);
			txtOffAddress.setColumns(10);
			
			JLabel lblCountryCode = new JLabel(resourceBundle.getString("CountryCode"));
			lblCountryCode.setBounds(10, 49, 103, 14);
			pnlAddOfficial.add(lblCountryCode);
			
			txtCountryCode = new JTextField();
			txtCountryCode.setEnabled(false);
			txtCountryCode.setEditable(false);
			txtCountryCode.setBounds(123, 46, 86, 17);
			pnlAddOfficial.add(txtCountryCode);
			txtCountryCode.setColumns(10);
			
			JLabel lblAdmArea = new JLabel(resourceBundle.getString("Area"));
			lblAdmArea.setBounds(10, 74, 86, 14);
			pnlAddOfficial.add(lblAdmArea);
			
			txtArea = new JTextField();
			txtArea.setEnabled(false);
			txtArea.setEditable(false);
			txtArea.setBounds(123, 74, 236, 17);
			pnlAddOfficial.add(txtArea);
			txtArea.setColumns(10);
			
			JLabel lblSubArea = new JLabel(resourceBundle.getString("SubArea"));
			lblSubArea.setBounds(10, 99, 103, 14);
			pnlAddOfficial.add(lblSubArea);
			
			txtSubArea = new JTextField();
			txtSubArea.setEnabled(false);
			txtSubArea.setEditable(false);
			txtSubArea.setBounds(123, 96, 236, 17);
			pnlAddOfficial.add(txtSubArea);
			txtSubArea.setColumns(10);
			
			JLabel lblBuildingType = new JLabel(resourceBundle.getString("BuildingType"));
			lblBuildingType.setBounds(419, 49, 103, 14);
			pnlAddOfficial.add(lblBuildingType);
			
			JLabel lblStreet = new JLabel("Street");
			lblStreet.setBounds(419, 74, 78, 14);
			pnlAddOfficial.add(lblStreet);
			
			JLabel lblHomeNo = new JLabel(resourceBundle.getString("HomeNumber"));
			lblHomeNo.setBounds(419, 99, 103, 14);
			pnlAddOfficial.add(lblHomeNo);
			
			txtBuildingType = new JTextField();
			txtBuildingType.setEnabled(false);
			txtBuildingType.setEditable(false);
			txtBuildingType.setBounds(532, 46, 119, 20);
			pnlAddOfficial.add(txtBuildingType);
			txtBuildingType.setColumns(10);
			
			txtStreet = new JTextField();
			txtStreet.setEnabled(false);
			txtStreet.setEditable(false);
			txtStreet.setBounds(532, 71, 244, 20);
			pnlAddOfficial.add(txtStreet);
			txtStreet.setColumns(10);
			
			txtHomeNumber = new JTextField();
			txtHomeNumber.setEnabled(false);
			txtHomeNumber.setEditable(false);
			txtHomeNumber.setBounds(532, 96, 119, 20);
			pnlAddOfficial.add(txtHomeNumber);
			txtHomeNumber.setColumns(10);
			
			JLabel lblPointX = new JLabel(resourceBundle.getString("Point_X"));
			lblPointX.setBounds(839, 49, 56, 14);
			pnlAddOfficial.add(lblPointX);
			
			JLabel lblPointY = new JLabel(resourceBundle.getString("Point_Y"));
			lblPointY.setBounds(839, 74, 56, 14);
			pnlAddOfficial.add(lblPointY);
			
			txtPointX = new JTextField();
			txtPointX.setEnabled(false);
			txtPointX.setEditable(false);
			txtPointX.setBounds(905, 46, 86, 20);
			pnlAddOfficial.add(txtPointX);
			txtPointX.setColumns(10);
			
			txtPointY = new JTextField();
			txtPointY.setEnabled(false);
			txtPointY.setEditable(false);
			txtPointY.setBounds(905, 71, 86, 20);
			pnlAddOfficial.add(txtPointY);
			txtPointY.setColumns(10);
			
			btnGetAddress = new JButton(resourceBundle.getString("FindAddress"));
			btnGetAddress.setActionCommand("Find Address");
			btnGetAddress.setForeground(Color.BLUE);
			btnGetAddress.setBounds(839, 20, 215, 23);
			pnlAddOfficial.add(btnGetAddress);
			cmbBoxRegion.addItemListener(this);
			
			btnAdd.addActionListener(this);	
			btnDelete.addActionListener(this);
			btnUpdate.addActionListener(this);
			btnSave.addActionListener(this);
			btnExit.addActionListener(this);
			btnCleanUp.addActionListener(this);
			btnListAptek.addActionListener(this);
			btnGetAddress.addActionListener(this);
			
			tableResult.addMouseListener(this);
			
			btnUpdate.setEnabled(false);
			btnSave.setEnabled(false);
			btnDelete.setEnabled(false);
			
			getPharmInfo();
			
			validate();
			setVisible(true);
		}
		
		public void itemStateChanged(ItemEvent itemEvent) {
	  	  JComboBox cmbBox = (JComboBox)itemEvent.getSource();
	  	  String name = cmbBox.getName();
	  	  	if(cmbBox.getSelectedItem() != null &&cmbBox.getSelectedItem().toString().length()>0){
		  	  	if(name.equalsIgnoreCase("cmbCountry")){
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
		  	    }else if(name.equalsIgnoreCase("cmbArea")){
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
		  	    }else if(name.equalsIgnoreCase("cmbRegion")){
		  		  cmbBoxCity.removeAllItems();
		  		  cmbBoxCity.addItem("");
		  		  try {
					Util.getPRMDataGroupBy("city", "solgar_prm.prm_sales_addresses",cmbBoxCity,"region",cmbBoxRegion.getSelectedItem().toString());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  		  cmbBoxCity.setSelectedIndex(-1);
		  	  }else if(name.equalsIgnoreCase("cmbCity")){
		  		  cmbBoxMetro.removeAllItems();
		  		  cmbBoxMetro.addItem("");
		  		  try {
					Util.getPRMDataGroupBy("metro", "solgar_prm.prm_sales_metro",cmbBoxMetro,"city",cmbBoxCity.getSelectedItem().toString());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  		  cmbBoxMetro.setSelectedIndex(-1);
		  	  }else if(name.equalsIgnoreCase("cmbCompany")){
		  		cmbBoxMrktStaff.removeAllItems();
		  		  try {
		  			 Util.getMedRep(empid, cmbBoxCompanyCode.getSelectedItem().toString(),cmbBoxMrktStaff);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  		cmbBoxMrktStaff.setSelectedIndex(-1);
		  	  }		  	 
	  	  	}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				if (e.getActionCommand().equals("ADD APTEKA")) {
					
					if(!mustEnterControl()){					 		
				 		return;
				 	}
										
						modelResult.setColumnIdentifiers(headerResult);
						tableResult.setModel(modelResult);						
						final Object[] row = new Object[32];
						row[0] = "0";
						row[1] = cmbBoxCompanyCode.getSelectedItem();
						row[2] = cmbBoxCountry.getSelectedItem();
						row[3] = cmbBoxArea.getSelectedItem();
						row[4] = cmbBoxRegion.getSelectedItem();
						row[5] = cmbBoxCity.getSelectedItem();
						row[6] = cmbBoxDistrict.getSelectedItem();
						row[7] = cmbBoxMetro.getSelectedItem();
						row[8] = cmbBoxChain.getSelectedItem();
						row[9] = cmbBoxSubChain.getSelectedItem();
						row[10] = cmbBoxMrktStaff.getSelectedItem();
						row[11] = txtAreaAddress.getText();
						row[12] = cmbBoxAssortiment.getSelectedItem();					
						row[13] = cmbBoxPharmCtgry.getSelectedItem();
						row[14] = cmbBoxPharmacyType.getSelectedItem();
						row[15] = cmbBoxPromo.getSelectedItem();					
						row[16] = cmbBoxActiveness.getSelectedItem();
						row[17] = agreementBeginDate.getDate() != null ? dcn.format(agreementBeginDate.getDate()) : "";
						row[18] = txtPharmHeadName.getText();
						row[19] = txtPharmacyTel.getText();
						row[20] = txtPharmEmail.getText();
						row[21] = txtAdditionalInfo.getText();
						row[22] = txtOffAddress.getText();
						row[23] = txtBuildingType.getText();
						row[24] = txtCountryCode.getText();
						row[25] = txtArea.getText();
						row[26] = txtSubArea.getText();
						row[27] = txtStreet.getText();
						row[28] = txtHomeNumber.getText();
						row[29] = txtPointY.getText();
						row[30] = txtPointX.getText();
						row[31] = txtAptekaNo.getText();
					
						modelResult.insertRow(0,row);
						btnSave.setEnabled(true);
					
					
				}else if (e.getActionCommand().equals("UPDATE")) {
					int i = tableResult.getSelectedRow();
					if(!mustEnterControl()){					 		
				 		return;
				 	}
					if (i >= 0){
						modelResult.setValueAt(txtid.getText(), i, 0);
						modelResult.setValueAt(cmbBoxCompanyCode.getSelectedItem(), i, 1);
						modelResult.setValueAt(cmbBoxCountry.getSelectedItem(), i, 2);
						modelResult.setValueAt(cmbBoxArea.getSelectedItem(), i, 3);
						modelResult.setValueAt(cmbBoxRegion.getSelectedItem(), i, 4);
						modelResult.setValueAt(cmbBoxCity.getSelectedItem(), i, 5);
						modelResult.setValueAt(cmbBoxDistrict.getSelectedItem(), i, 6);
						modelResult.setValueAt(cmbBoxMetro.getSelectedItem(), i, 7);
						modelResult.setValueAt(cmbBoxChain.getSelectedItem(), i, 8);
						modelResult.setValueAt(cmbBoxSubChain.getSelectedItem(), i, 9);
						modelResult.setValueAt(cmbBoxMrktStaff.getSelectedItem(), i, 10);
						modelResult.setValueAt(txtAreaAddress.getText(), i, 11);
						modelResult.setValueAt(cmbBoxAssortiment.getSelectedItem(), i, 12);						
						modelResult.setValueAt(cmbBoxPharmCtgry.getSelectedItem(), i, 13);
						modelResult.setValueAt(cmbBoxPharmacyType.getSelectedItem(), i, 14);
						modelResult.setValueAt(cmbBoxPromo.getSelectedItem(), i, 15);
						modelResult.setValueAt(cmbBoxActiveness.getSelectedItem(), i, 16);
						modelResult.setValueAt(agreementBeginDate.getDate() != null ? dcn.format(agreementBeginDate.getDate()) : "", i, 17);
						modelResult.setValueAt(txtPharmHeadName.getText(), i, 18);
						modelResult.setValueAt(txtPharmacyTel.getText(), i, 19);
						modelResult.setValueAt(txtPharmEmail.getText(), i, 20);
						modelResult.setValueAt(txtAdditionalInfo.getText(), i, 21);
						modelResult.setValueAt(txtOffAddress.getText(), i, 22);
						modelResult.setValueAt(txtBuildingType.getText(), i, 23);
						modelResult.setValueAt(txtCountryCode.getText(), i, 24);
						modelResult.setValueAt(txtArea.getText(), i, 25);
						modelResult.setValueAt(txtSubArea.getText(), i, 26);
						modelResult.setValueAt(txtStreet.getText(), i, 27);
						modelResult.setValueAt(txtHomeNumber.getText(), i, 28);
						modelResult.setValueAt(txtPointY.getText(), i, 29);
						modelResult.setValueAt(txtPointX.getText(), i, 30);												
						modelResult.setValueAt(txtAptekaNo.getText(), i, 31);						
					}
					btnSave.setEnabled(true);
					
				}else if (e.getActionCommand().equals("DELETE")) {
					int i = tableResult.getSelectedRow();
					if (i >= 0) {
						modelResult.setValueAt("-"+ modelResult.getValueAt(i, 0).toString(),i,0);
						modelResult.setValueAt("Delete".toString(),i,32);
						//modelResult.removeRow(i);
					}
					btnSave.setEnabled(true);
				}else if (e.getActionCommand().equals("SAVE")) {
					ESIBag outBag = new ESIBag();
					ESIBag tempBag = new ESIBag();
					int rowResult = tableResult.getRowCount();	
					int index = 0;
													
					// rows
					for (int j = 0; j  < rowResult; j++) {
						long id =Long.parseLong(tableResult.getValueAt(j, 0).toString());
						outBag.put("USERNAME", userName);
						if(id<=0){
							outBag.put("RESULTTABLE",index,"ID", tableResult.getValueAt(j, 0).toString());
							formatToBagValueResult(outBag, j, 1, "RESULTTABLE", "BRAND",false,index);
							formatToBagValueResult(outBag, j, 2, "RESULTTABLE", "COUNTRY",false,index);
							formatToBagValueResult(outBag, j, 3, "RESULTTABLE", "AREA",false,index);
							formatToBagValueResult(outBag, j, 4, "RESULTTABLE", "REGION",false,index);
							formatToBagValueResult(outBag, j, 5, "RESULTTABLE", "CITY",false,index);
							//formatToBagValueResult(outBag, j, 5, "RESULTTABLE", "CITY_REGION",false,index);
							formatToBagValueResult(outBag, j, 6, "RESULTTABLE", "DISTRICT",false,index);
							formatToBagValueResult(outBag, j, 7, "RESULTTABLE", "METRO",false,index);
							formatToBagValueResult(outBag, j, 8, "RESULTTABLE", "GROUP_COMPANY",false,index);
							formatToBagValueResult(outBag, j, 9, "RESULTTABLE", "SUBGROUP_COMPANY",false,index);
							formatToBagValueResult(outBag, j, 10, "RESULTTABLE", "MARKETING_STAFF",false,index);
							formatToBagValueResult(outBag, j, 11, "RESULTTABLE", "PHARMACY_ADDRESS",false,index);
							formatToBagValueResult(outBag, j, 12, "RESULTTABLE", "ASSORTIMENT",false,index);
							formatToBagValueResult(outBag, j, 13, "RESULTTABLE", "PHARMACY_CATEGORY",false,index);
							formatToBagValueResult(outBag, j, 14, "RESULTTABLE", "PHARMACY_TYPE",false,index);
							formatToBagValueResult(outBag, j, 15, "RESULTTABLE", "PROMO",false,index);
							formatToBagValueResult(outBag, j, 16, "RESULTTABLE", "PHARMACY_ACTIVENESS",false,index);
							formatToBagValueResult(outBag, j, 17, "RESULTTABLE", "PHARMACY_ACTIVATION_DATE",false,index);	
							formatToBagValueResult(outBag, j, 18, "RESULTTABLE", "PHARMACY_RESPONSE_PERSON",false,index);
							formatToBagValueResult(outBag, j, 19, "RESULTTABLE", "PHARMACY_TEL",false,index);
							formatToBagValueResult(outBag, j, 20, "RESULTTABLE", "PHARMACY_EMAIL",false,index);
							formatToBagValueResult(outBag, j, 21, "RESULTTABLE", "COMMENTS",false,index);
							formatToBagValueResult(outBag, j, 22, "RESULTTABLE", "FULL_ADDRESS",false,index);
							formatToBagValueResult(outBag, j, 23, "RESULTTABLE", "BUILDING_TYPE",false,index);
							formatToBagValueResult(outBag, j, 24, "RESULTTABLE", "COUNTRY_CODE",false,index);
							formatToBagValueResult(outBag, j, 25, "RESULTTABLE", "ADMINISTRATIVE_AREA_NAME",false,index);
							formatToBagValueResult(outBag, j, 26, "RESULTTABLE", "SUB_ADMINISTRATIVE_AREA_NAME",false,index);
							formatToBagValueResult(outBag, j, 27, "RESULTTABLE", "STREET",false,index);
							formatToBagValueResult(outBag, j, 28, "RESULTTABLE", "HOMENUMBER",false,index);
							formatToBagValueResult(outBag, j, 29, "RESULTTABLE", "POINT_Y",false,index);
							formatToBagValueResult(outBag, j, 30, "RESULTTABLE", "POINT_X",false,index);
							formatToBagValueResult(outBag, j, 31, "RESULTTABLE", "PHARMACY_NO",false,index);
							formatToBagValueResult(outBag, j, 32, "RESULTTABLE", "ENTRY_USER",false,index);
							index++;
						}
					}
					
					tempBag = Dispatcher.savePharmInfo(outBag);
					if(tempBag.existsBagKey("ERROR_MESSAGE")){
						JOptionPane.showMessageDialog(pnlInfoMsg, tempBag.get("ERROR_MESSAGE").toString(), "Error", JOptionPane.ERROR_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(pnlInfoMsg, "Operation completed succesfully", "Information", JOptionPane.INFORMATION_MESSAGE);
						cleanUp();
						btnUpdate.setEnabled(false);
						btnSave.setEnabled(false);
						btnDelete.setEnabled(false);
					}
		
				}else if (e.getActionCommand().equals("CLEANUP")) {
					cleanUp();
				}else if (e.getActionCommand().equals("EXIT")) {
					setVisible(false);
				}else if (e.getActionCommand().equals("List Apteka")) {
					getPharmInfo();
				}else if (e.getActionCommand().equals("Find Address")) {
					ESIBag tempBag =new ESIBag();
					tempBag.put("REQUEST", txtAreaAddress.getText()); 
					tempBag = Dispatcher.getPharmOfficialAddress(tempBag);
					
					txtOffAddress.setText(tempBag.get("FULL_ADDRESS"));
					txtBuildingType.setText(tempBag.get("BUILDING_TYPE"));
					txtCountryCode.setText(tempBag.get("COUNTRY_CODE"));
					txtArea.setText(tempBag.get("ADMINISTRATIVE_AREA_NAME"));
					txtSubArea.setText(tempBag.get("SUB_ADMINISTRATIVE_AREA_NAME"));
					txtStreet.setText(tempBag.get("STREET"));
					txtHomeNumber.setText(tempBag.get("HOMENUMBER"));
					txtPointX.setText(tempBag.get("POINT_X"));
					txtPointY.setText(tempBag.get("POINT_Y"));
					
				}
		
			} catch (Exception e2) {
				// TODO: handle exception
			}
			
		}

		private void formatToBagValueResult(ESIBag outBag,int rowNum,int index,String tableBagkey,String valueBagKey,boolean number,int bagkeyIndex){			
			if(tableResult.getValueAt(rowNum, index) != null && tableResult.getValueAt(rowNum, index).toString().length()>0){
				outBag.put(tableBagkey,bagkeyIndex,valueBagKey, tableResult.getValueAt(rowNum, index).toString());
			}else{
				if(number){
					outBag.put(tableBagkey,bagkeyIndex,valueBagKey, "0");
				}else{
					outBag.put(tableBagkey,bagkeyIndex,valueBagKey, "");
				}
			}
		}
		private void cleanUp() {
		    try{	    	
		    	 cleanTexts();
		    	 DefaultTableModel dtm2 = (DefaultTableModel) tableResult.getModel();
		    	 for( int j = dtm2.getRowCount() - 1; j >= 0; j-- ) {
		    		 dtm2.removeRow(j);
					}
		    	 
		    		
	    	}catch (Exception e) {
			// simdilik yoksa yok
	    	}
		}
		
		private void cleanTexts() {
		    try{	    	
		    	//cmbBoxCompanyCode.setSelectedItem("");
		    	//cmbBoxCountry.setSelectedItem("");	
		    	//cmbBoxArea.setSelectedItem("");
		    	cmbBoxRegion.setSelectedItem("");
		    	cmbBoxCity.setSelectedItem("");
		    	cmbBoxDistrict.setSelectedItem("");
		    	cmbBoxArea.setSelectedItem("");		    	
		    	cmbBoxMetro.setSelectedItem("");
		    	cmbBoxChain.setSelectedItem("");
		    	cmbBoxSubChain.setSelectedItem("");	    	
		    	//cmbBoxMrktStaff.setSelectedItem("");
		    	cmbBoxPharmacyType.setSelectedItem("");
		    	txtAreaAddress.setText("");
		    	cmbBoxAssortiment.setSelectedItem("");	    	
		    	cmbBoxActiveness.setSelectedItem("");
		    	agreementBeginDate.setDate(cal.getTime());	
		    	cmbBoxPharmCtgry.setSelectedItem("");
		    	cmbBoxPromo.setSelectedItem("");		    	
		    	txtPharmHeadName.setText("");
		    	txtPharmacyTel.setText("");
		    	txtPharmEmail.setText("");
		    	txtAdditionalInfo.setText("");
		    	txtOffAddress.setText("");
		    	txtBuildingType.setText("");
		    	txtCountryCode.setText("");
		    	txtArea.setText("");
		    	txtSubArea.setText("");
		    	txtStreet.setText("");
		    	txtHomeNumber.setText("");
		    	txtPointY.setText("");
		    	txtPointX.setText("");
		    	txtAptekaNo.setText("");
		    	txtid.setText("");
		    		
	    	}catch (Exception e) {
			// simdilik yoksa yok
	    	}
		}
		
		private boolean mustEnterControl() { 	
	    	if(!mustEnterCheckOneBy(cmbBoxCompanyCode)){return false;}
	    	if(!mustEnterCheckOneBy(cmbBoxCountry)){return false;}
	    	if(!mustEnterCheckOneBy(cmbBoxArea)){return false;}
	    	if(!mustEnterCheckOneBy(cmbBoxRegion)){return false;}
	    	if(!mustEnterCheckOneBy(cmbBoxCity)){return false;}
	    	if(!mustEnterCheckOneBy(cmbBoxChain)){return false;}
	    	if(!mustEnterCheckOneBy(cmbBoxSubChain)){return false;}
	    	if(!mustEnterCheckOneBy(cmbBoxMrktStaff)){return false;}
	    	if(!mustEnterCheckOneBy(cmbBoxPharmCtgry)){return false;}
	    	if(!mustEnterCheckOneBy(cmbBoxAssortiment)){return false;}
	    	if(!mustEnterCheckOneBy(cmbBoxActiveness)){return false;}
	    	if(!mustEnterCheckOneBy(cmbBoxPharmacyType)){return false;}
	    	if(txtPointY.getText().length()==0 && txtPointX.getText().length()==0){
				//JOptionPane.showMessageDialog(pnlInfoMsg, "Please set official address info by Find Address Button", "Error", JOptionPane.ERROR_MESSAGE);
				//return false;
			}
			return true;
		}
		
		private boolean mustEnterCheckOneBy(JComboBox cmbBox) {
			if(cmbBox.getSelectedItem() == null || cmbBox.getSelectedItem().toString().length()==0){
				JOptionPane.showMessageDialog(pnlInfoMsg, "Please fill red marked field ", "Error", JOptionPane.ERROR_MESSAGE);
				cmbBox.setBackground(Color.red);
				return false;
			}
			cmbBox.setBackground(Color.white);
			return true;
		}
		
		private void getPharmInfo() {
		    try{
		    	 ESIBag tempBag =new ESIBag();
				 tempBag.put("BRAND", cmbBoxCompanyCode.getSelectedItem().toString());  
				 if(cmbBoxCountry.getSelectedItem() !=null){tempBag.put("COUNTRY", cmbBoxCountry.getSelectedItem().toString());}	
				 if(cmbBoxArea.getSelectedItem() !=null){tempBag.put("AREA", cmbBoxArea.getSelectedItem().toString());}				 
				 if(cmbBoxRegion.getSelectedItem() !=null){tempBag.put("REGION", cmbBoxRegion.getSelectedItem().toString());} 
				 if(cmbBoxCity.getSelectedItem() !=null){tempBag.put("CITY", cmbBoxCity.getSelectedItem().toString());} 
				 if(cmbBoxDistrict.getSelectedItem() !=null){tempBag.put("CITY_REGION", cmbBoxDistrict.getSelectedItem().toString());} 
				 if(cmbBoxMrktStaff.getSelectedItem() !=null){tempBag.put("MARKETING_STAFF", cmbBoxMrktStaff.getSelectedItem().toString());}
				 if(cmbBoxChain.getSelectedItem() !=null){tempBag.put("MAIN_GROUP", cmbBoxChain.getSelectedItem().toString());}
				 if(cmbBoxPharmCtgry.getSelectedItem() !=null){tempBag.put("CATEGORY", cmbBoxPharmCtgry.getSelectedItem().toString());}
				 
				 if(cmbBoxSubChain.getSelectedItem() !=null){tempBag.put("SUB_GROUP", cmbBoxSubChain.getSelectedItem().toString());}
				 if(cmbBoxAssortiment.getSelectedItem() !=null){tempBag.put("ASSORTIMENT", cmbBoxAssortiment.getSelectedItem().toString());}
				 if(cmbBoxActiveness.getSelectedItem() !=null){tempBag.put("ACTIVENESS", cmbBoxActiveness.getSelectedItem().toString());}
				 if(cmbBoxPharmacyType.getSelectedItem() !=null){tempBag.put("PHARMACYTYPE", cmbBoxPharmacyType.getSelectedItem().toString());}
				 if(cmbBoxPromo.getSelectedItem() !=null){tempBag.put("PROMO", cmbBoxPromo.getSelectedItem().toString());}
				 if(txtAreaAddress.getText() !=null && txtAreaAddress.getText().trim().length()>0){tempBag.put("ADDRESS", txtAreaAddress.getText().trim().toString());}
				 
				 DefaultTableModel dtm2 = (DefaultTableModel) tableResult.getModel();
		    	 for( int j = dtm2.getRowCount() - 1; j >= 0; j-- ) {
		    		 dtm2.removeRow(j);
					}
		    	
		    	tempBag = Dispatcher.getPharmInfo(tempBag);
				modelResult.setColumnIdentifiers(headerResult);
				tableResult.setModel(modelResult);
	    	
				for (int j = 0; j < tempBag.getSize("TABEL"); j++){
					modelResult.addRow(new Object [] 
			        		{
			        			tempBag.get("TABEL",j,"ID"),
			        			tempBag.get("TABEL",j,"BRAND"),			        			
			        			tempBag.get("TABEL",j,"COUNTRY"),
			        			tempBag.get("TABEL",j,"AREA"),
			        			tempBag.get("TABEL",j,"REGION"),
			        			tempBag.get("TABEL",j,"CITY"),
			        			//tempBag.get("TABEL",j,"CITY_REGION"),
			        			tempBag.get("TABEL",j,"DISTRICT"),
			        			tempBag.get("TABEL",j,"METRO"),
			        			tempBag.get("TABEL",j,"GROUP_COMPANY"),
			        			tempBag.get("TABEL",j,"SUBGROUP_COMPANY"),
			        			tempBag.get("TABEL",j,"MARKETING_STAFF"),
			        			tempBag.get("TABEL",j,"PHARMACY_ADDRESS"),
			        			tempBag.get("TABEL",j,"ASSORTIMENT"),
			        			tempBag.get("TABEL",j,"PHARMACY_CATEGORY"),
			        			tempBag.get("TABEL",j,"PHARMACY_TYPE"),
			        			tempBag.get("TABEL",j,"PROMO"),
			        			tempBag.get("TABEL",j,"PHARMACY_ACTIVENESS"),
			        			tempBag.get("TABEL",j,"PHARMACY_ACTIVATION_DATE"),			        			
			        			tempBag.get("TABEL",j,"PHARMACY_RESPONSE_PERSON"),
			        			tempBag.get("TABEL",j,"PHARMACY_TEL"),
			        			tempBag.get("TABEL",j,"PHARMACY_EMAIL"),
			        			tempBag.get("TABEL",j,"COMMENTS"),
			        			tempBag.get("TABEL",j,"FULL_ADDRESS"),
			        			tempBag.get("TABEL",j,"BUILDING_TYPE"),
			        			tempBag.get("TABEL",j,"COUNTRY_CODE"),
			        			tempBag.get("TABEL",j,"ADMINISTRATIVE_AREA_NAME"),
			        			tempBag.get("TABEL",j,"SUB_ADMINISTRATIVE_AREA_NAME"),
			        			tempBag.get("TABEL",j,"STREET"),
			        			tempBag.get("TABEL",j,"HOMENUMBER"),
			        			tempBag.get("TABEL",j,"POINT_Y"),
			        			tempBag.get("TABEL",j,"POINT_X"),
			        			tempBag.get("TABEL",j,"PHARMACY_NO"),
			        			tempBag.get("TABEL",j,"ENTRY_USER"),
			        			tempBag.get("TABEL",j,"ENTRY_DATE"),
			        		});		
				}
				txtAptekCount.setText(tempBag.get("COUNT"));
				
		    }catch (Exception e) {
				// simdilik yoksa yok
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			try{				
				int i = tableResult.getSelectedRow();	
				txtid.setText("-"+ modelResult.getValueAt(i, 0).toString());
				cmbBoxCompanyCode.setSelectedItem(modelResult.getValueAt(i, 1).toString() != null ? modelResult.getValueAt(i, 1).toString() : "");
				cmbBoxCountry.setSelectedItem(modelResult.getValueAt(i, 2).toString() != null ? modelResult.getValueAt(i, 2).toString() : "");
				cmbBoxArea.setSelectedItem(modelResult.getValueAt(i, 3).toString() != null ? modelResult.getValueAt(i, 3).toString() : "");
				cmbBoxRegion.setSelectedItem(modelResult.getValueAt(i, 4).toString() != null ? modelResult.getValueAt(i, 4).toString() : "");
				cmbBoxCity.setSelectedItem(modelResult.getValueAt(i, 5).toString() != null ? modelResult.getValueAt(i, 5).toString() : "");
				//cmbBoxCityRegion.setSelectedItem(modelResult.getValueAt(i, 5).toString() != null ? modelResult.getValueAt(i, 5).toString() : "");
				cmbBoxDistrict.setSelectedItem(modelResult.getValueAt(i, 6).toString() != null ? modelResult.getValueAt(i, 6).toString() : "");
				cmbBoxMetro.setSelectedItem(modelResult.getValueAt(i, 7).toString() != null ? modelResult.getValueAt(i, 7).toString() : "");
				cmbBoxChain.setSelectedItem(modelResult.getValueAt(i, 8).toString() != null ? modelResult.getValueAt(i, 8).toString() : "");
				cmbBoxSubChain.setSelectedItem(modelResult.getValueAt(i, 9).toString() != null ? modelResult.getValueAt(i, 9).toString() : "");
				cmbBoxMrktStaff.setSelectedItem(modelResult.getValueAt(i, 10).toString() != null ? modelResult.getValueAt(i, 10).toString() : "");
				txtAreaAddress.setText(modelResult.getValueAt(i, 11).toString() != null ? modelResult.getValueAt(i, 11).toString() : "");
				cmbBoxAssortiment.setSelectedItem(modelResult.getValueAt(i, 12).toString() != null ? modelResult.getValueAt(i, 12).toString() : "");				
				cmbBoxPharmCtgry.setSelectedItem(modelResult.getValueAt(i, 13).toString() != null ? modelResult.getValueAt(i, 13).toString() : "");
				cmbBoxPharmacyType.setSelectedItem(modelResult.getValueAt(i, 14).toString() != null ? modelResult.getValueAt(i, 14).toString() : "");
				cmbBoxPromo.setSelectedItem(modelResult.getValueAt(i, 15).toString() != null ? modelResult.getValueAt(i, 15).toString() : "");				
				cmbBoxActiveness.setSelectedItem(modelResult.getValueAt(i, 16).toString() != null ? modelResult.getValueAt(i, 16).toString() : "");	
				if(modelResult.getValueAt(i, 17) != null){			
			  		try {
			  			agreementBeginDate.setDate(dcn.parse(modelResult.getValueAt(i, 17).toString()));
			  		} catch (Exception e2) {
			  			// TODO: handle exception
			  		}
			  	}else{
			  		agreementBeginDate.setDateFormatString("");
			  	}
				txtPharmHeadName.setText(modelResult.getValueAt(i, 18).toString() != null ? modelResult.getValueAt(i, 18).toString() : "");
				txtPharmacyTel.setText(modelResult.getValueAt(i, 19).toString() != null ? modelResult.getValueAt(i, 19).toString() : "");
				txtPharmEmail.setText(modelResult.getValueAt(i, 20).toString() != null ? modelResult.getValueAt(i, 20).toString() : "");					
				txtAdditionalInfo.setText(modelResult.getValueAt(i, 21).toString() != null ? modelResult.getValueAt(i, 21).toString() : "");
				txtOffAddress.setText(modelResult.getValueAt(i, 22).toString() != null ? modelResult.getValueAt(i, 22).toString() : "");
				txtBuildingType.setText(modelResult.getValueAt(i, 23).toString() != null ? modelResult.getValueAt(i, 23).toString() : "");
				txtCountryCode.setText(modelResult.getValueAt(i, 24).toString() != null ? modelResult.getValueAt(i, 24).toString() : "");
				txtArea.setText(modelResult.getValueAt(i, 25).toString() != null ? modelResult.getValueAt(i, 25).toString() : "");
				txtSubArea.setText(modelResult.getValueAt(i, 26).toString() != null ? modelResult.getValueAt(i, 26).toString() : "");
				txtStreet.setText(modelResult.getValueAt(i, 27).toString() != null ? modelResult.getValueAt(i, 27).toString() : "");
				txtHomeNumber.setText(modelResult.getValueAt(i, 28).toString() != null ? modelResult.getValueAt(i, 28).toString() : "");
				txtPointY.setText(modelResult.getValueAt(i, 29).toString() != null ? modelResult.getValueAt(i, 29).toString() : "");
				txtPointX.setText(modelResult.getValueAt(i, 30).toString() != null ? modelResult.getValueAt(i, 30).toString() : "");
				txtAptekaNo.setText(modelResult.getValueAt(i, 31).toString() != null ? modelResult.getValueAt(i, 31).toString() : "");
				
				btnUpdate.setEnabled(true);
				btnDelete.setEnabled(true);
			} catch (Exception e2) {
				// TODO: handle exception
			}
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseReleased(MouseEvent e) {
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
		
	}