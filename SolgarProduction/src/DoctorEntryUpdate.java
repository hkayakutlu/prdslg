package src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

public class DoctorEntryUpdate extends JFrame implements ActionListener,ItemListener,MouseListener{

	private JPanel contentPane;
	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 750;
	private static final Calendar cal = Calendar.getInstance();
	
	private JPanel pnlInfoMsg;
	private String userName="Hakan KAYAKUTLU";
	private String userBrand="ALL";
	private String userCountry="All";
	private String userArea="All";	
	private String empid="-1";
	private JTextField txtDoctorName,txtDoctorTel;
	private JComboBox cmbBoxCountry,cmbBoxArea,cmbBoxRegion,cmbBoxCity,cmbBoxCompanyCode,
	cmbBoxSpeciality,cmbBoxMrktStaff,cmbBoxDoctorCtgry,cmbBoxUnifiedSpeciality,cmbBoxActiveness;
	private JDateChooser doctorActivationDate;
	private static final DecimalFormat doubleFormatter = new DecimalFormat("#,###");
	private JTable tableResult;
	public JButton btnAdd,btnDelete,btnUpdate,btnSave,btnExit,btnCleanUp,btnGetAddress,btnListAptek;
	public SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
	public JTextArea txtAreaAddress;
	
	final DefaultTableModel modelResult = new DefaultTableModel();
	String headerResult[] = new String[] {"Id", "BRAND", "Country", "Area", "Region", "City", "Activeness", "Medrep", "Activation Date", "Doctor Name", "��������������� �������������", 
			"�������������", "��������� / �������", "Category", "Clinic Name","Clinic Address", "Clinic Count", "Key Person", "Doctor Tel", "Doctor Email", "Full Address", "Building Type", 
			"Country Code", "Administrative Area Name", "Sub Administrative Area Name", "Street", "Home Number", "point_y", "point_x","Entry User","Entry Date"};
	private JTextField txtDoctorEmail,txtPointY,txtPointX,txtHomeNumber,txtStreet,txtBuildingType,
	txtSubArea,txtArea,txtCountryCode,txtOffAddress,txtClinicCount,txtclinicName,txtPositionRegalia,txtKeyPerson,txtid;
	private String Lang = "RU";
	private ResourceBundle resourceBundle;
	private JTextField txtCount;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ESIBag inBag = new ESIBag();
					DoctorEntryUpdate frame = new DoctorEntryUpdate(inBag);
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
	public DoctorEntryUpdate(ESIBag inBag) throws SQLException {
		super("Doctor Entry&Update");
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
		contentPane = new JPanel();
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
		pnlAddress.setBounds(10, 11, 196, 155);
		getContentPane().add(pnlAddress);
		pnlAddress.setLayout(null);
		
		JLabel lblCountry = new JLabel(resourceBundle.getString("Country"));
		lblCountry.setBounds(10, 56, 69, 14);
		pnlAddress.add(lblCountry);
		
		JLabel lblArea = new JLabel(resourceBundle.getString("Area"));
		lblArea.setBounds(10, 81, 46, 14);
		pnlAddress.add(lblArea);
		
		JLabel lblRegion = new JLabel(resourceBundle.getString("Region"));
		lblRegion.setBounds(10, 106, 46, 14);
		pnlAddress.add(lblRegion);
		
		JLabel lblCity = new JLabel(resourceBundle.getString("City"));
		lblCity.setBounds(10, 131, 46, 14);
		pnlAddress.add(lblCity);
		
		cmbBoxCountry = new JComboBox( new String[]{});
		cmbBoxCountry.setBounds(59, 55, 130, 17);
		Util.getPRMDataGroupBy("country", "solgar_prm.prm_sales_addresses",cmbBoxCountry,"","");
		cmbBoxCountry.setEnabled(false);
		pnlAddress.add(cmbBoxCountry);
		
		cmbBoxArea = new JComboBox( new String[]{});
		cmbBoxArea.setBounds(59, 81, 130, 17);
		cmbBoxArea.setEnabled(false);
		pnlAddress.add(cmbBoxArea);
		
		cmbBoxRegion = new JComboBox( new String[]{});
		cmbBoxRegion.setBounds(59, 106, 130, 17);
		pnlAddress.add(cmbBoxRegion);
		
		cmbBoxCity = new JComboBox( new String[]{});
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
			/*cmbBoxCountry.addItem("Russia");
			cmbBoxCountry.setSelectedIndex(0);				
			if(userArea.equalsIgnoreCase("ALL")){
				Util.getPRMDataGroupBy("area", "solgar_prm.prm_sales_addresses",cmbBoxArea,"country",cmbBoxCountry.getSelectedItem().toString());
				cmbBoxArea.setSelectedIndex(0);
				cmbBoxArea.setEnabled(true);				
			}else if(userArea.equalsIgnoreCase("Moscow")){
				cmbBoxArea.addItem("Moscow");
				cmbBoxArea.setSelectedIndex(0);
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
				cmbBoxArea.setSelectedIndex(0);
				cmbBoxArea.setEnabled(true);				
			}else if(userArea.equalsIgnoreCase("Kiev")){
				cmbBoxArea.addItem("Kiev");
				cmbBoxArea.setSelectedIndex(0);
				Util.getPRMDataGroupBy("region", "solgar_prm.prm_sales_addresses",cmbBoxRegion,"area",cmbBoxArea.getSelectedItem().toString());
			}
		}
		
		
		JLabel lblCompany = new JLabel(resourceBundle.getString("Company"));
		lblCompany.setBounds(10, 32, 69, 14);
		pnlAddress.add(lblCompany);

		cmbBoxCompanyCode = new JComboBox( new String[]{});
		cmbBoxCompanyCode.setEnabled(false);
		cmbBoxCompanyCode.setBounds(97, 29, 92, 17);
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
		
		JPanel pnlAgreement = new JPanel();
		pnlAgreement.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Doctor Info", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		pnlAgreement.setBounds(216, 11, 547, 207);
		getContentPane().add(pnlAgreement);
		pnlAgreement.setLayout(null);
		
		JLabel lblDoctorTel = new JLabel(resourceBundle.getString("DoctorTel"));
		lblDoctorTel.setBounds(292, 98, 120, 17);
		pnlAgreement.add(lblDoctorTel);
		
		JLabel lblDoctorName = new JLabel(resourceBundle.getString("DoctorName"));
		lblDoctorName.setBounds(10, 23, 120, 17);
		pnlAgreement.add(lblDoctorName);
		
		JLabel lblPharmActivationDate = new JLabel(resourceBundle.getString("DoctorActivationDate"));
		lblPharmActivationDate.setBounds(10, 178, 120, 17);
		pnlAgreement.add(lblPharmActivationDate);
		
		JLabel lblDoctorEmail = new JLabel(resourceBundle.getString("DoctorEmail"));
		lblDoctorEmail.setBounds(292, 123, 120, 17);
		pnlAgreement.add(lblDoctorEmail);
		
		txtDoctorTel = new JTextField(resourceBundle.getString("DoctorTel"));
		txtDoctorTel.setBounds(408, 95, 130, 17);
		txtDoctorTel.setText("0");
		pnlAgreement.add(txtDoctorTel);
		
		txtDoctorName = new JTextField();
		txtDoctorName.setBounds(140, 22, 397, 17);
		pnlAgreement.add(txtDoctorName);
		txtDoctorName.setColumns(20);
		
		doctorActivationDate = new JDateChooser();
		doctorActivationDate.setBounds(140, 178, 100, 17);
		doctorActivationDate.setDateFormatString("yyyy-MM-dd");		
		doctorActivationDate.setDate(cal.getTime());
		pnlAgreement.add(doctorActivationDate);
		
		JLabel lblSpeciality = new JLabel(resourceBundle.getString("Speciality"));
		lblSpeciality.setBounds(10, 123, 120, 17);
		pnlAgreement.add(lblSpeciality);
		
		cmbBoxSpeciality = new JComboBox();
		cmbBoxSpeciality.addItem("");
		Util.getPRMData("specialty", "solgar_prm.prm_sales_doctor_speaciality",cmbBoxSpeciality);		
		cmbBoxSpeciality.setSelectedIndex(-1);
		cmbBoxSpeciality.setBounds(140, 123, 140, 17);
		pnlAgreement.add(cmbBoxSpeciality);
		
		JLabel lblMarketingStaff = new JLabel(resourceBundle.getString("MarketingStaff"));
		lblMarketingStaff.setBounds(10, 48, 120, 17);
		pnlAgreement.add(lblMarketingStaff);		
		
		cmbBoxMrktStaff = new JComboBox();
		cmbBoxMrktStaff.addItem("");
		Util.getMedRep(empid, cmbBoxCompanyCode.getSelectedItem().toString(),cmbBoxMrktStaff);
		cmbBoxMrktStaff.setBounds(140, 50, 140, 17);
		pnlAgreement.add(cmbBoxMrktStaff);
		
		cmbBoxDoctorCtgry = new JComboBox();
		cmbBoxDoctorCtgry.addItem("");
		cmbBoxDoctorCtgry.addItem("A");
		cmbBoxDoctorCtgry.addItem("B");
		cmbBoxDoctorCtgry.setSelectedIndex(-1);
		cmbBoxDoctorCtgry.setBounds(140, 74, 140, 17);
		pnlAgreement.add(cmbBoxDoctorCtgry);
		
		JLabel lblPharmCtgry = new JLabel(resourceBundle.getString("DoctorCategory"));
		lblPharmCtgry.setBounds(10, 74, 120, 17);
		pnlAgreement.add(lblPharmCtgry);
		
		JLabel lblAssortiment = new JLabel(resourceBundle.getString("MainSpeciality"));
		lblAssortiment.setBounds(10, 98, 120, 17);
		pnlAgreement.add(lblAssortiment);
		
		cmbBoxUnifiedSpeciality = new JComboBox();
		cmbBoxUnifiedSpeciality.addItem("");
		Util.getPRMDataGroupBy("Unified_specialty", "solgar_prm.prm_sales_doctor_unspeaciality",cmbBoxUnifiedSpeciality,"","");
		cmbBoxUnifiedSpeciality.setSelectedIndex(-1);
		cmbBoxUnifiedSpeciality.setBounds(140, 98, 140, 17);
		pnlAgreement.add(cmbBoxUnifiedSpeciality);
		
		JLabel lblDoctorActiveness = new JLabel(resourceBundle.getString("DoctorActiveness"));
		lblDoctorActiveness.setBounds(292, 50, 109, 17);
		pnlAgreement.add(lblDoctorActiveness);
		
		cmbBoxActiveness = new JComboBox();
		cmbBoxActiveness.addItem("�����");
		cmbBoxActiveness.addItem("�� �����");
		cmbBoxActiveness.addItem("� ��������");
		cmbBoxActiveness.setBounds(408, 48, 100, 17);
		pnlAgreement.add(cmbBoxActiveness);
		
		txtDoctorEmail = new JTextField();
		txtDoctorEmail.setBounds(410, 120, 130, 17);
		pnlAgreement.add(txtDoctorEmail);
		txtDoctorEmail.setColumns(10);
		
		btnListAptek = new JButton(resourceBundle.getString("ListDoctor"));
		btnListAptek.setActionCommand("List Doctor");
		btnListAptek.setForeground(Color.BLUE);
		btnListAptek.setBounds(408, 173, 130, 23);
		pnlAgreement.add(btnListAptek);
		
		JLabel lblPosition = new JLabel(resourceBundle.getString("Position"));
		lblPosition.setBounds(10, 152, 120, 14);
		pnlAgreement.add(lblPosition);
		
		txtPositionRegalia = new JTextField();
		txtPositionRegalia.setBounds(140, 151, 231, 20);
		pnlAgreement.add(txtPositionRegalia);
		txtPositionRegalia.setColumns(10);
		
		txtid = new JTextField();
		txtid.setEnabled(false);
		txtid.setBounds(432, 142, 86, 20);
		pnlAgreement.add(txtid);
		txtid.setColumns(10);
		
		JLabel lblCount = new JLabel(resourceBundle.getString("Count"));
		lblCount.setBounds(259, 179, 46, 14);
		pnlAgreement.add(lblCount);
		
		txtCount = new JTextField();
		txtCount.setEnabled(false);
		txtCount.setBounds(315, 176, 62, 20);
		pnlAgreement.add(txtCount);
		txtCount.setColumns(10);
		
		JPanel pnlResult = new JPanel();
		pnlResult.setBorder(new TitledBorder(null, "Result", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		pnlResult.setBounds(10, 382, 1064, 279);
		getContentPane().add(pnlResult);
		pnlResult.setLayout(null);
		
		JScrollPane scrollResult = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollResult.setBounds(10, 33, 1044, 235);
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
		
		
		JPanel panelButon = new JPanel();
		panelButon.setBorder(new TitledBorder(null, "Button Group", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		panelButon.setBounds(10, 639, 1064, 62);
		getContentPane().add(panelButon);
		panelButon.setLayout(null);
		
		btnAdd = new JButton(resourceBundle.getString("ADD"));
		btnAdd.setActionCommand("ADD");
		btnAdd.setForeground(Color.RED);
		btnAdd.setBounds(50, 28, 139, 23);
		panelButon.add(btnAdd);
		
		btnUpdate = new JButton(resourceBundle.getString("UPDATE"));
		btnUpdate.setActionCommand("UPDATE");
		btnUpdate.setForeground(Color.RED);
		btnUpdate.setBounds(221, 28, 132, 23);
		panelButon.add(btnUpdate);
		
		btnDelete = new JButton(resourceBundle.getString("DELETE"));
		btnDelete.setActionCommand("DELETE");
		btnDelete.setForeground(Color.RED);
		btnDelete.setBounds(377, 28, 132, 23);
		panelButon.add(btnDelete);
		
		btnSave = new JButton(resourceBundle.getString("SAVE"));
		btnSave.setActionCommand("SAVE");
		btnSave.setForeground(Color.RED);
		btnSave.setBounds(537, 28, 139, 23);
		panelButon.add(btnSave);
		
		btnCleanUp = new JButton(resourceBundle.getString("CLEANUP"));
		btnCleanUp.setActionCommand("CLEANUP");
		btnCleanUp.setForeground(Color.RED);
		btnCleanUp.setBounds(701, 28, 139, 23);
		panelButon.add(btnCleanUp);
		
		btnExit = new JButton(resourceBundle.getString("EXIT"));
		btnExit.setActionCommand("EXIT");
		btnExit.setForeground(Color.RED);
		btnExit.setBounds(885, 28, 132, 23);
		panelButon.add(btnExit);
		
		/*Listeners*/
		cmbBoxCountry.setName("cmbCountry");
		cmbBoxCountry.addItemListener(this);
		cmbBoxArea.setName("cmbArea");
		cmbBoxArea.addItemListener(this);
		cmbBoxRegion.setName("cmbRegion");
		
		JPanel pnlAddr = new JPanel();
		pnlAddr.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Clinic Info", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnlAddr.setBounds(773, 12, 301, 236);
		getContentPane().add(pnlAddr);
		pnlAddr.setLayout(null);
		
		JLabel lblClinicAddress = new JLabel(resourceBundle.getString("ClinicAddress"));
		lblClinicAddress.setBounds(5, 147, 103, 14);
		pnlAddr.add(lblClinicAddress);
		
		JLabel lblClinicName = new JLabel(resourceBundle.getString("ClinicName"));
		lblClinicName.setBounds(5, 25, 110, 14);
		pnlAddr.add(lblClinicName);
		
		txtclinicName = new JTextField();
		txtclinicName.setBounds(125, 22, 165, 20);
		pnlAddr.add(txtclinicName);
		txtclinicName.setColumns(10);
		
		JLabel lblClinicCount = new JLabel(resourceBundle.getString("ClinicCount"));
		lblClinicCount.setBounds(5, 50, 113, 14);
		pnlAddr.add(lblClinicCount);
		
		txtClinicCount = new JTextField();
		txtClinicCount.setBounds(110, 47, 80, 20);
		pnlAddr.add(txtClinicCount);
		txtClinicCount.setColumns(10);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(5, 104, 284, 32);
		pnlAddr.add(scrollPane_1);
		
		JTextArea txtAreaAddExp = new JTextArea();
		scrollPane_1.setViewportView(txtAreaAddExp);
		txtAreaAddExp.setEditable(false);
		txtAreaAddExp.setEnabled(false);
		txtAreaAddExp.setFont(new Font("Monospaced", Font.PLAIN, 10));
		txtAreaAddExp.setText("\u0410\u0434\u0440\u0435\u0441 \u0434\u043E\u043B\u0436\u0435\u043D \u0432\u043A\u043B\u044E\u0447\u0435\u043D \u0432 \u0441\u043B\u0435\u0434\u0443\u044E\u0449\u0438\u0439 \u0444\u043E\u0440\u043C\u0430\u0442 (\u043E\u0431\u043B\u0430\u0441\u0442\u044C, \u0433\u043E\u0440\u043E\u0434, \u0434\u043E\u043C, \u043A\u0432\u0430\u0440\u0442\u0438\u0440\u0430, \u043A\u043E\u0440\u043F\u0443\u0441)");
		txtAreaAddExp.setLineWrap(true);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(100, 143, 189, 72);
		pnlAddr.add(scrollPane);
		
		txtAreaAddress = new JTextArea();
		scrollPane.setViewportView(txtAreaAddress);
		txtAreaAddress.setFont(new Font("Monospaced", Font.PLAIN, 12));
		txtAreaAddress.setLineWrap(true);
		txtAreaAddress.setRows(2);
		
		JLabel lblKeyPerson = new JLabel(resourceBundle.getString("KeyPerson"));
		lblKeyPerson.setBounds(5, 75, 110, 14);
		pnlAddr.add(lblKeyPerson);
		
		txtKeyPerson = new JTextField();
		txtKeyPerson.setBounds(125, 73, 166, 20);
		pnlAddr.add(txtKeyPerson);
		txtKeyPerson.setColumns(10);
		
		JPanel pnlAddOfficial = new JPanel();
		pnlAddOfficial.setBorder(new TitledBorder(null, "Official Address Info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlAddOfficial.setBounds(10, 249, 1064, 122);
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
		
		JLabel lblStreet = new JLabel(resourceBundle.getString("Street"));
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
		
		getDoctorInfo();
		
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
	  	  }
  	  	}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			if (e.getActionCommand().equals("ADD")) {
				if(!mustEnterControl()){					 		
			 		return;
			 	}
				modelResult.setColumnIdentifiers(headerResult);
				tableResult.setModel(modelResult);						
				final Object[] row = new Object[31];
				row[0] = "0";
				row[1] = cmbBoxCompanyCode.getSelectedItem();
				row[2] = cmbBoxCountry.getSelectedItem();
				row[3] = cmbBoxArea.getSelectedItem();
				row[4] = cmbBoxRegion.getSelectedItem();
				row[5] = cmbBoxCity.getSelectedItem();
				row[6] = cmbBoxActiveness.getSelectedItem();
				row[7] = cmbBoxMrktStaff.getSelectedItem();
				row[8] = doctorActivationDate.getDate() != null ? dcn.format(doctorActivationDate.getDate()) : "";
				row[9] = txtDoctorName.getText();
				row[10] = cmbBoxUnifiedSpeciality.getSelectedItem();		
				row[11] = cmbBoxSpeciality.getSelectedItem();
				row[12] = txtPositionRegalia.getText();
				row[13] = cmbBoxDoctorCtgry.getSelectedItem();
				row[14] = txtclinicName.getText();
				row[15] = txtAreaAddress.getText();
				row[16] = txtClinicCount.getText();
				row[17] = txtKeyPerson.getText();
				row[18] = txtDoctorTel.getText();
				row[19] = txtDoctorEmail.getText();
				row[20] = txtOffAddress.getText();
				row[21] = txtBuildingType.getText();
				row[22] = txtCountryCode.getText();
				row[23] = txtArea.getText();
				row[24] = txtSubArea.getText();
				row[25] = txtStreet.getText();
				row[26] = txtHomeNumber.getText();
				row[27] = txtPointY.getText();
				row[28] = txtPointX.getText();			
			
				modelResult.insertRow(0,row);
				btnSave.setEnabled(true);
				
			}else if (e.getActionCommand().equals("UPDATE")) {
				if(!mustEnterControl()){					 		
			 		return;
			 	}
				int i = tableResult.getSelectedRow();
				if (i >= 0){
					modelResult.setValueAt(txtid.getText(), i, 0);
					modelResult.setValueAt(cmbBoxCompanyCode.getSelectedItem(), i, 1);
					modelResult.setValueAt(cmbBoxCountry.getSelectedItem(), i, 2);
					modelResult.setValueAt(cmbBoxArea.getSelectedItem(), i, 3);
					modelResult.setValueAt(cmbBoxRegion.getSelectedItem(), i, 4);
					modelResult.setValueAt(cmbBoxCity.getSelectedItem(), i, 5);
					modelResult.setValueAt(cmbBoxActiveness.getSelectedItem(), i, 6);
					modelResult.setValueAt(cmbBoxMrktStaff.getSelectedItem(), i, 7);
					modelResult.setValueAt(doctorActivationDate.getDate() != null ? dcn.format(doctorActivationDate.getDate()) : "", i, 8);
					modelResult.setValueAt(txtDoctorName.getText(), i, 9);
					modelResult.setValueAt(cmbBoxUnifiedSpeciality.getSelectedItem(), i, 10);	
					modelResult.setValueAt(cmbBoxSpeciality.getSelectedItem(), i, 11);
					modelResult.setValueAt(txtPositionRegalia.getText(), i, 12);
					modelResult.setValueAt(cmbBoxDoctorCtgry.getSelectedItem(), i, 13);
					modelResult.setValueAt(txtclinicName.getText(), i, 14);
					modelResult.setValueAt(txtAreaAddress.getText(), i, 15);
					modelResult.setValueAt(txtClinicCount.getText(), i, 16);
					modelResult.setValueAt(txtKeyPerson.getText(), i, 17);
					modelResult.setValueAt(txtDoctorTel.getText(), i, 18);
					modelResult.setValueAt(txtDoctorEmail.getText(), i, 19);
					modelResult.setValueAt(txtOffAddress.getText(), i, 20);
					modelResult.setValueAt(txtBuildingType.getText(), i, 21);
					modelResult.setValueAt(txtCountryCode.getText(), i, 22);
					modelResult.setValueAt(txtArea.getText(), i, 23);
					modelResult.setValueAt(txtSubArea.getText(), i, 24);
					modelResult.setValueAt(txtStreet.getText(), i, 25);
					modelResult.setValueAt(txtHomeNumber.getText(), i, 26);
					modelResult.setValueAt(txtPointY.getText(), i, 27);
					modelResult.setValueAt(txtPointX.getText(), i, 28);
				}
				btnSave.setEnabled(true);
				
			}else if (e.getActionCommand().equals("DELETE")) {
				int i = tableResult.getSelectedRow();
				if (i >= 0) {
					modelResult.setValueAt("-"+ modelResult.getValueAt(i, 0).toString(),i,0);
					modelResult.setValueAt("Delete".toString(),i,29);
				}
				btnSave.setEnabled(true);
			}else if (e.getActionCommand().equals("SAVE")) {
				ESIBag outBag = new ESIBag();
				ESIBag tempBag = new ESIBag();
				int rowResult = tableResult.getRowCount();
				int index = 0;

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
						formatToBagValueResult(outBag, j, 6, "RESULTTABLE", "ACTIVENESS",false,index);
						formatToBagValueResult(outBag, j, 7, "RESULTTABLE", "MARKETING_STAFF",false,index);
						formatToBagValueResult(outBag, j, 8, "RESULTTABLE", "ACTIVATION_DATE",false,index);
						formatToBagValueResult(outBag, j, 9, "RESULTTABLE", "DOCTOR_NAME",false,index);
						formatToBagValueResult(outBag, j, 10, "RESULTTABLE", "UNIFIED_SPECIALITY",false,index);
						formatToBagValueResult(outBag, j, 11, "RESULTTABLE", "SPECIALITY",false,index);
						formatToBagValueResult(outBag, j, 12, "RESULTTABLE", "POSITION_REGALIA",false,index);
						formatToBagValueResult(outBag, j, 13, "RESULTTABLE", "CATEGORY",false,index);
						formatToBagValueResult(outBag, j, 14, "RESULTTABLE", "CLINIC_NAME",false,index);	
						
						formatToBagValueResult(outBag, j, 15, "RESULTTABLE", "ADDRESS",false,index);
						formatToBagValueResult(outBag, j, 16, "RESULTTABLE", "CLINIC_COUNT",false,index);
						formatToBagValueResult(outBag, j, 17, "RESULTTABLE", "KEY_PERSON",false,index);
						formatToBagValueResult(outBag, j, 18, "RESULTTABLE", "DOCTOR_TEL",false,index);
						formatToBagValueResult(outBag, j, 19, "RESULTTABLE", "DOCTOR_EMAIL",false,index);
						formatToBagValueResult(outBag, j, 20, "RESULTTABLE", "FULL_ADDRESS",false,index);
						formatToBagValueResult(outBag, j, 21, "RESULTTABLE", "BUILDING_TYPE",false,index);
						formatToBagValueResult(outBag, j, 22, "RESULTTABLE", "COUNTRY_CODE",false,index);
						formatToBagValueResult(outBag, j, 23, "RESULTTABLE", "ADMINISTRATIVE_AREA_NAME",false,index);
						formatToBagValueResult(outBag, j, 24, "RESULTTABLE", "SUB_ADMINISTRATIVE_AREA_NAME",false,index);
						formatToBagValueResult(outBag, j, 25, "RESULTTABLE", "STREET",false,index);
						formatToBagValueResult(outBag, j, 26, "RESULTTABLE", "HOMENUMBER",false,index);
						formatToBagValueResult(outBag, j, 27, "RESULTTABLE", "POINT_Y",false,index);
						formatToBagValueResult(outBag, j, 28, "RESULTTABLE", "POINT_X",false,index);
						formatToBagValueResult(outBag, j, 29, "RESULTTABLE", "ENTRY_USER",false,index);
						index++;
					}
				}
				
				
				tempBag = Dispatcher.saveDoctorInfo(outBag);
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
			}else if (e.getActionCommand().equals("List Doctor")) {
				getDoctorInfo();
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
	    	cmbBoxCountry.setSelectedItem("");		   
	    	cmbBoxArea.setSelectedItem("");
	    	cmbBoxRegion.setSelectedItem("");
	    	cmbBoxCity.setSelectedItem("");	    
	    	cmbBoxActiveness.setSelectedItem("");
	    	//cmbBoxMrktStaff.setSelectedItem("");
	    	doctorActivationDate.setDate(cal.getTime());
	    	txtDoctorName.setText("");
	    	cmbBoxUnifiedSpeciality.setSelectedItem("");
	    	cmbBoxSpeciality.setSelectedItem("");
	    	txtPositionRegalia.setText("");	  
	    	cmbBoxDoctorCtgry.setSelectedItem("");
	    	txtclinicName.setText("");
	    	txtAreaAddress.setText("");
	    	txtClinicCount.setText("");
	    	txtKeyPerson.setText("");		    		    	
	    	txtDoctorTel.setText("");
	    	txtDoctorEmail.setText("");	    	
	    	txtOffAddress.setText("");
	    	txtBuildingType.setText("");
	    	txtCountryCode.setText("");
	    	txtArea.setText("");
	    	txtSubArea.setText("");
	    	txtStreet.setText("");
	    	txtHomeNumber.setText("");
	    	txtPointY.setText("");
	    	txtPointX.setText("");
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
    	if(!mustEnterCheckOneBy(cmbBoxDoctorCtgry)){return false;}
    	if(!mustEnterCheckOneBy(cmbBoxUnifiedSpeciality)){return false;}
    	if(!mustEnterCheckOneBy(cmbBoxSpeciality)){return false;}
    	if(!mustEnterCheckOneBy(cmbBoxMrktStaff)){return false;}
    	if(!mustEnterCheckOneBy(cmbBoxActiveness)){return false;}

    	if(txtDoctorName.getText().length()==0 && txtDoctorName.getText().length()==0){
			JOptionPane.showMessageDialog(pnlInfoMsg, "Please fill doctor name", "Error", JOptionPane.ERROR_MESSAGE);
			txtDoctorName.setBackground(Color.red);
			return false;
		}
    	txtDoctorName.setBackground(Color.white);
    	
    	if(txtPointY.getText().length()==0 && txtPointX.getText().length()==0){
			JOptionPane.showMessageDialog(pnlInfoMsg, "Please set official address info by Find Address Button", "Error", JOptionPane.ERROR_MESSAGE);			
			return false;
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
	
	private void getDoctorInfo() {
	    try{
	    	 ESIBag tempBag =new ESIBag();
			 tempBag.put("BRAND", cmbBoxCompanyCode.getSelectedItem().toString()); 
			 if(cmbBoxCountry.getSelectedItem() !=null){tempBag.put("COUNTRY", cmbBoxCountry.getSelectedItem().toString());}
			 if(cmbBoxArea.getSelectedItem() !=null){tempBag.put("AREA", cmbBoxArea.getSelectedItem().toString());}				 
			 if(cmbBoxRegion.getSelectedItem() !=null){tempBag.put("REGION", cmbBoxRegion.getSelectedItem().toString());} 
			 if(cmbBoxCity.getSelectedItem() !=null){tempBag.put("CITY", cmbBoxCity.getSelectedItem().toString());} 
			 if(cmbBoxMrktStaff.getSelectedItem() !=null){tempBag.put("MARKETING_STAFF", cmbBoxMrktStaff.getSelectedItem().toString());}
			 
			 if(cmbBoxSpeciality.getSelectedItem() !=null){tempBag.put("MAIN_SPECIALITY", cmbBoxSpeciality.getSelectedItem().toString());}
			 if(cmbBoxUnifiedSpeciality.getSelectedItem() !=null){tempBag.put("SUB_SPECIALITY", cmbBoxUnifiedSpeciality.getSelectedItem().toString());}
			 if(cmbBoxDoctorCtgry.getSelectedItem() !=null){tempBag.put("CATEGORY", cmbBoxDoctorCtgry.getSelectedItem().toString());}
			 if(cmbBoxActiveness.getSelectedItem() !=null){tempBag.put("ACTIVENESS", cmbBoxActiveness.getSelectedItem().toString());}
			 if(txtDoctorName.getText() !=null && txtDoctorName.getText().trim().length()>0){tempBag.put("DOCTOR_NAME", txtDoctorName.getText().toString());}
			 
			 DefaultTableModel dtm2 = (DefaultTableModel) tableResult.getModel();
	    	 for( int j = dtm2.getRowCount() - 1; j >= 0; j-- ) {
	    		 dtm2.removeRow(j);
				}
	    	
	    	tempBag = Dispatcher.getDoctorInfo(tempBag);
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
		        			tempBag.get("TABEL",j,"ACTIVENESS"),
		        			tempBag.get("TABEL",j,"MARKETING_STAFF"),
		        			tempBag.get("TABEL",j,"ACTIVATION_DATE"),
		        			tempBag.get("TABEL",j,"DOCTOR_NAME"),
		        			tempBag.get("TABEL",j,"UNIFIED_SPECIALITY"),
		        			tempBag.get("TABEL",j,"SPECIALITY"),
		        			tempBag.get("TABEL",j,"POSITION_REGALIA"),
		        			tempBag.get("TABEL",j,"CATEGORY"),
		        			tempBag.get("TABEL",j,"CLINIC_NAME"),
		        			tempBag.get("TABEL",j,"ADDRESS"),
		        			tempBag.get("TABEL",j,"CLINIC_COUNT"),
		        			tempBag.get("TABEL",j,"KEY_PERSON"),
		        			tempBag.get("TABEL",j,"DOCTOR_TEL"),
		        			tempBag.get("TABEL",j,"DOCTOR_EMAIL"),
		        			tempBag.get("TABEL",j,"FULL_ADDRESS"),
		        			tempBag.get("TABEL",j,"BUILDING_TYPE"),
		        			tempBag.get("TABEL",j,"COUNTRY_CODE"),
		        			tempBag.get("TABEL",j,"ADMINISTRATIVE_AREA_NAME"),
		        			tempBag.get("TABEL",j,"SUB_ADMINISTRATIVE_AREA_NAME"),
		        			tempBag.get("TABEL",j,"STREET"),
		        			tempBag.get("TABEL",j,"HOMENUMBER"),
		        			tempBag.get("TABEL",j,"POINT_Y"),
		        			tempBag.get("TABEL",j,"POINT_X"),
		        			tempBag.get("TABEL",j,"ENTRY_USER"),
		        			tempBag.get("TABEL",j,"ENTRY_DATE")
		        		});		
			}
				txtCount.setText(tempBag.get("COUNT"));
			
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
			cmbBoxActiveness.setSelectedItem(modelResult.getValueAt(i, 6).toString() != null ? modelResult.getValueAt(i, 6).toString() : "");
			cmbBoxMrktStaff.setSelectedItem(modelResult.getValueAt(i, 7).toString() != null ? modelResult.getValueAt(i, 7).toString() : "");
			doctorActivationDate.setDate(dcn.parse(modelResult.getValueAt(i, 8).toString()));			
			txtDoctorName.setText(modelResult.getValueAt(i, 9).toString() != null ? modelResult.getValueAt(i, 9).toString() : "");
			cmbBoxUnifiedSpeciality.setSelectedItem(modelResult.getValueAt(i, 10).toString() != null ? modelResult.getValueAt(i, 10).toString() : "");
			cmbBoxSpeciality.setSelectedItem(modelResult.getValueAt(i, 11).toString() != null ? modelResult.getValueAt(i, 11).toString() : "");
			txtPositionRegalia.setText(modelResult.getValueAt(i, 12).toString() != null ? modelResult.getValueAt(i, 12).toString() : "");
			cmbBoxDoctorCtgry.setSelectedItem(modelResult.getValueAt(i, 13).toString() != null ? modelResult.getValueAt(i, 13).toString():"");
			txtclinicName.setText(modelResult.getValueAt(i, 14).toString() != null ? modelResult.getValueAt(i, 14).toString() : "");
			txtAreaAddress.setText(modelResult.getValueAt(i, 15).toString() != null ? modelResult.getValueAt(i, 15).toString() : "");
			txtClinicCount.setText(modelResult.getValueAt(i, 16).toString() != null ? modelResult.getValueAt(i, 16).toString() : "");
			txtKeyPerson.setText(modelResult.getValueAt(i, 17).toString() != null ? modelResult.getValueAt(i, 17).toString() : "");			
			txtDoctorTel.setText(modelResult.getValueAt(i, 18).toString() != null ? modelResult.getValueAt(i, 18).toString() : "");
			txtDoctorEmail.setText(modelResult.getValueAt(i, 19).toString() != null ? modelResult.getValueAt(i, 19).toString() : "");
			txtOffAddress.setText(modelResult.getValueAt(i, 20).toString() != null ? modelResult.getValueAt(i, 20).toString() : "");
			txtBuildingType.setText(modelResult.getValueAt(i, 21).toString() != null ? modelResult.getValueAt(i, 21).toString() : "");
			txtCountryCode.setText(modelResult.getValueAt(i, 22).toString() != null ? modelResult.getValueAt(i, 22).toString() : "");
			txtArea.setText(modelResult.getValueAt(i, 23).toString() != null ? modelResult.getValueAt(i, 23).toString() : "");
			txtSubArea.setText(modelResult.getValueAt(i, 24).toString() != null ? modelResult.getValueAt(i, 24).toString() : "");
			txtStreet.setText(modelResult.getValueAt(i, 25).toString() != null ? modelResult.getValueAt(i, 25).toString() : "");
			txtHomeNumber.setText(modelResult.getValueAt(i, 26).toString() != null ? modelResult.getValueAt(i, 26).toString() : "");
			txtPointY.setText(modelResult.getValueAt(i, 27).toString() != null ? modelResult.getValueAt(i, 27).toString() : "");
			txtPointX.setText(modelResult.getValueAt(i, 28).toString() != null ? modelResult.getValueAt(i, 28).toString() : "");
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