package src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;
import javax.swing.text.TabExpander;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.DefaultCellEditor;
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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextPane;

public class ChainExpsUpdateScreen extends JFrame implements ActionListener,ItemListener,MouseListener{

	private JPanel contentPane;
	private static final int FRAME_WIDTH = 1200;
	private static final int FRAME_HEIGHT = 750;
	private static final Calendar cal = Calendar.getInstance();
	
	private JPanel pnlInfoMsg;
	private String userName="Hakan KAYAKUTLU";
	private String userBrand="ALL";
	private String userCountry="ALL";
	private String userArea="ALL";	
	private JTextField txtAgreementNo,txtPaymentOrderNo,txtFixBonus;
	private JComboBox cmbBoxStages,cmbBoxAdvertisementType,cmbBoxDocumentStatus,cmbBoxCountry,cmbBoxArea,
	cmbBoxRegion,cmbBoxCity,cmbBoxCompanyCode,cmbBoxChain,cmbMonth,cmbYear;
	private JComboBox cmbBoxCity_1;
	private JComboBox cmbBoxRegion_1;
	private JComboBox cmbBoxArea_1;
	private JDateChooser agreementBeginDate,agreementEndDate,paymentDate;
	public JFormattedTextField txtPharmacyCount,fmtxPurchasingSum,fmtxPurchasingAmount,fmtxSellOutSum
	,fmtxSellAmountAmount,fmtxOutgoingAmount,fmtxBalance,fmtxTotPaySum;
	private static final DecimalFormat doubleFormatter = new DecimalFormat("#,###");
	private JTable tblTarget,tblRelease,tblBonusRates,tableResult,tblSearch;
	public JButton btnAdd,btnDelete,btnUpdate,btnSave,btnExit,btnCleanUp;
	public SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
	
	
	final DefaultTableModel modelResult = new DefaultTableModel();
	final DefaultTableModel modelAgreementSearch = new DefaultTableModel();
	final DefaultTableModel modelTarget = new DefaultTableModel();
	final DefaultTableModel modelRelease = new DefaultTableModel();
	final DefaultTableModel modelBonusTypes = new DefaultTableModel();
	
	String headerResult[] = new String[] {"Id", "BRAND", "CHAIN", "PAYMENT TYPE", "TOTAL PAYMENT SUM", "PURCHASING SUM", "PURCHASING AMOUNT", 
			"SELL OUT SUM", "SELL OUT AMOUNT", "DATE", "PAYMENT ORDER NO", "ADV TYPE", "DOCUMENT STATUS", "OUTGOING AMOUNT", "BALANCE"};
	String headerAgreement[] = new String[] {"Id", "BRAND", "CHAIN", "AGREEMENTNO", "BEGIN DATE", "END DATE"};
	String headerTarget[] = new String[] {"Id", "Year", "Month", "Trgt1", "Trgt2", "Trgt3", "Trgt4", "Trgt5"};
	String headerRelease[] = new String[] {"Id", "Year", "Month", "Purchase","Sell Out", "Amount"};
	String headerBonusTypes[] = new String[] {"Id", "Bonus Type", "1", "2", "3", "4"};
	private JTextField txtAgrNo1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ESIBag inBag = new ESIBag();
					ChainExpsUpdateScreen frame = new ChainExpsUpdateScreen(inBag);
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
	public ChainExpsUpdateScreen(ESIBag inBag) throws SQLException {
		super("Chain Update Entry");
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
    
		JPanel pnlStage = new JPanel();
		pnlStage.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Create Payment Row With Below Data", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		pnlStage.setBounds(356, 318, 797, 139);
		getContentPane().add(pnlStage);
		pnlStage.setLayout(null);
		
		JLabel lblStages = new JLabel("Stages");
		lblStages.setBounds(10, 28, 61, 22);
		pnlStage.add(lblStages);
		
		cmbBoxStages = new JComboBox( new String[]{});
		cmbBoxStages.setEditable(false);
		cmbBoxStages.setBounds(137, 25, 108, 22);
		cmbBoxStages.addItem("");
		cmbBoxStages.addItem("MARKETING");
		cmbBoxStages.addItem("FIX PURCHASING BONUS");
		cmbBoxStages.addItem("FIX SELL OUT BONUS");
		cmbBoxStages.addItem("CHAIN BONUS");
		cmbBoxStages.addItem("PHARMACANT BONUS");
		cmbBoxStages.addItem("PHARM MANAGER BONUS");
		cmbBoxStages.addItem("ADVERTISEMENT");
		cmbBoxStages.addItem("TRAINING");
		pnlStage.add(cmbBoxStages);
		
		JLabel lbltotPaySum = new JLabel("Total Payment Sum");
		lbltotPaySum.setBounds(10, 61, 117, 14);
		pnlStage.add(lbltotPaySum);
		
		JLabel lblPurchaseSum = new JLabel("Operation Sum");
		lblPurchaseSum.setBounds(10, 86, 108, 14);
		pnlStage.add(lblPurchaseSum);
		
		JLabel lblPurchaseAmnt = new JLabel("Operation Amount");
		lblPurchaseAmnt.setBounds(10, 114, 117, 14);
		pnlStage.add(lblPurchaseAmnt);
		
		/*JLabel lblSellOutSum = new JLabel("Sell Out Sum");
		lblSellOutSum.setBounds(299, 28, 81, 14);
		lblSellOutSum.setVisible(false);
		pnlStage.add(lblSellOutSum);
		
		JLabel lblSellOutAmont = new JLabel("Sell Out Amount");
		lblSellOutAmont.setBounds(299, 57, 93, 14);
		lblSellOutAmont.setVisible(false);
		pnlStage.add(lblSellOutAmont);*/
		
		JLabel lblPayDate = new JLabel("Payment Date");
		lblPayDate.setBounds(299, 32, 93, 14);
		pnlStage.add(lblPayDate);
		
		JLabel lblPayOrderNo = new JLabel("Payment Order No ");
		lblPayOrderNo.setBounds(299, 61, 93, 14);
		pnlStage.add(lblPayOrderNo);
		
		JLabel lblAdvertisementType = new JLabel("Advertisment Type");
		lblAdvertisementType.setBounds(549, 29, 117, 14);
		pnlStage.add(lblAdvertisementType);
		
		JLabel lblDocStatus = new JLabel("Document Status");
		lblDocStatus.setBounds(549, 57, 105, 18);
		pnlStage.add(lblDocStatus);
		
		JLabel lblOutAmount = new JLabel("Outgoing Amount");
		lblOutAmount.setBounds(549, 86, 105, 14);
		pnlStage.add(lblOutAmount);
		
		JLabel lblBalance = new JLabel("Balance");
		lblBalance.setBounds(549, 114, 61, 14);
		pnlStage.add(lblBalance);
		
		fmtxTotPaySum = new JFormattedTextField(doubleFormatter);
		fmtxTotPaySum.setText("0.00");
		fmtxTotPaySum.setBounds(137, 58, 108, 20);
		pnlStage.add(fmtxTotPaySum);
		
		fmtxPurchasingSum = new JFormattedTextField(numberFormatter);
		fmtxPurchasingSum.setText("0");
		fmtxPurchasingSum.setBounds(137, 80, 108, 20);
		pnlStage.add(fmtxPurchasingSum);
		
		fmtxPurchasingAmount = new JFormattedTextField(doubleFormatter);
		fmtxPurchasingAmount.setText("0.00");
		fmtxPurchasingAmount.setBounds(137, 108, 108, 20);
		pnlStage.add(fmtxPurchasingAmount);
		
		/*fmtxSellOutSum = new JFormattedTextField(numberFormatter);
		fmtxSellOutSum.setText("0");
		fmtxSellOutSum.setBounds(403, 29, 72, 20);
		fmtxSellOutSum.setVisible(false);
		pnlStage.add(fmtxSellOutSum);
		
		fmtxSellAmountAmount = new JFormattedTextField(doubleFormatter);
		fmtxSellAmountAmount.setText("0.00");
		fmtxSellAmountAmount.setBounds(403, 58, 81, 20);
		fmtxSellAmountAmount.setVisible(false);
		pnlStage.add(fmtxSellAmountAmount);*/
		
		paymentDate = new JDateChooser();
		paymentDate.setDateFormatString("yyyy-MM-dd");
		paymentDate.setBounds(402, 28, 101, 20);
		paymentDate.setDate(cal.getTime());
		pnlStage.add(paymentDate);
		
		txtPaymentOrderNo = new JTextField();
		txtPaymentOrderNo.setBounds(402, 58, 101, 20);
		pnlStage.add(txtPaymentOrderNo);
		txtPaymentOrderNo.setColumns(10);
		
		cmbBoxAdvertisementType = new JComboBox( new String[]{});
		cmbBoxAdvertisementType.addItem("");
		Util.getPRMDataGroupBy("level2", "solgar_prm.prm_chain_exps_types",cmbBoxAdvertisementType,"level1","Advertisement");
		cmbBoxAdvertisementType.setEditable(false);
		cmbBoxAdvertisementType.setSelectedIndex(-1);
		cmbBoxAdvertisementType.setBounds(664, 28, 123, 22);
		pnlStage.add(cmbBoxAdvertisementType);
		
		cmbBoxDocumentStatus = new JComboBox( new String[]{});
		cmbBoxDocumentStatus.addItem("");
		cmbBoxDocumentStatus.addItem("NO");
		cmbBoxDocumentStatus.addItem("WAITING");
		cmbBoxDocumentStatus.addItem("DELIVERED");
		cmbBoxDocumentStatus.setEditable(false);
		cmbBoxDocumentStatus.setBounds(664, 57, 123, 18);
		pnlStage.add(cmbBoxDocumentStatus);
		
		fmtxOutgoingAmount = new JFormattedTextField(doubleFormatter);
		fmtxOutgoingAmount.setText("0.00");
		fmtxOutgoingAmount.setBounds(664, 83, 93, 20);
		pnlStage.add(fmtxOutgoingAmount);

		fmtxBalance = new JFormattedTextField(doubleFormatter);
		fmtxBalance.setText("0.00");
		fmtxBalance.setBounds(664, 111, 93, 20);
		pnlStage.add(fmtxBalance);
		
		JPanel pnlAddress = new JPanel();
		pnlAddress.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Address", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		pnlAddress.setBounds(10, 110, 223, 156);
		getContentPane().add(pnlAddress);
		pnlAddress.setLayout(null);
		
		JLabel lblCountry = new JLabel("Country");
		lblCountry.setBounds(10, 56, 69, 14);
		pnlAddress.add(lblCountry);
		
		JLabel lblArea = new JLabel("Area");
		lblArea.setBounds(10, 81, 46, 14);
		pnlAddress.add(lblArea);
		
		JLabel lblRegion = new JLabel("Region");
		lblRegion.setBounds(10, 106, 46, 14);
		pnlAddress.add(lblRegion);
		
		JLabel lblCity = new JLabel("City");
		lblCity.setBounds(10, 131, 46, 14);
		pnlAddress.add(lblCity);
		
		cmbBoxCountry = new JComboBox( new String[]{});
		cmbBoxCountry.setEnabled(false);
		cmbBoxCountry.setBounds(59, 55, 153, 17);
		Util.getPRMDataGroupBy("country", "solgar_prm.prm_exps_addresses",cmbBoxCountry,"","");
		cmbBoxCountry.setSelectedIndex(-1);
		pnlAddress.add(cmbBoxCountry);
		
		cmbBoxArea = new JComboBox( new String[]{});
		cmbBoxArea_1 = new JComboBox( new String[]{});
		cmbBoxArea_1.setEnabled(false);
		cmbBoxArea_1.setBounds(59, 81, 153, 17);
		pnlAddress.add(cmbBoxArea_1);
		
		cmbBoxRegion = new JComboBox( new String[]{});
		cmbBoxRegion_1 = new JComboBox( new String[]{});
		cmbBoxRegion_1.setEnabled(false);
		cmbBoxRegion_1.setBounds(59, 106, 153, 17);
		pnlAddress.add(cmbBoxRegion_1);
		
		cmbBoxCity = new JComboBox( new String[]{});
		cmbBoxCity_1 = new JComboBox( new String[]{});
		cmbBoxCity_1.setEnabled(false);
		cmbBoxCity_1.setBounds(59, 128, 153, 17);
		pnlAddress.add(cmbBoxCity_1);
		
		JLabel lblCompany = new JLabel("Company");
		lblCompany.setBounds(10, 32, 46, 14);
		pnlAddress.add(lblCompany);

		cmbBoxCompanyCode = new JComboBox( new String[]{});
		cmbBoxCompanyCode.setEnabled(false);
		cmbBoxCompanyCode.setBounds(59, 29, 153, 17);
		cmbBoxCompanyCode.addItem("SOLGAR");
		cmbBoxCompanyCode.addItem("NATURES BOUNTY");
		cmbBoxCompanyCode.setSelectedIndex(0);
		pnlAddress.add(cmbBoxCompanyCode);
		
		JPanel pnlAgreement = new JPanel();
		pnlAgreement.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Agreement Info", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		pnlAgreement.setBounds(243, 110, 264, 197);
		getContentPane().add(pnlAgreement);
		pnlAgreement.setLayout(null);
		
		JLabel lblChainName = new JLabel("Chain");
		lblChainName.setBounds(10, 26, 79, 14);
		pnlAgreement.add(lblChainName);
		
		JLabel lblPharmacyCount = new JLabel("Pharmacy Count");
		lblPharmacyCount.setBounds(10, 69, 118, 14);
		pnlAgreement.add(lblPharmacyCount);
		
		JLabel lblAgreementNo = new JLabel("Agreement No");
		lblAgreementNo.setBounds(10, 92, 106, 14);
		pnlAgreement.add(lblAgreementNo);
		
		JLabel lblAgreementBeginDate = new JLabel("Agreement Begin Date");
		lblAgreementBeginDate.setBounds(10, 141, 124, 14);
		pnlAgreement.add(lblAgreementBeginDate);
		
		JLabel lblAgreementEndDate = new JLabel("Agreement End Date");
		lblAgreementEndDate.setBounds(10, 166, 124, 14);
		pnlAgreement.add(lblAgreementEndDate);

		cmbBoxChain = new JComboBox( new String[]{});
		cmbBoxChain.setEnabled(false);
		Util.getPRMData("group_company", "solgar_prm.prm_russia_chains",cmbBoxChain);		
		cmbBoxChain.setSelectedIndex(-1);
		cmbBoxChain.setBounds(138, 23, 118, 20);
		pnlAgreement.add(cmbBoxChain);
		
		txtPharmacyCount = new JFormattedTextField(numberFormatter);
		txtPharmacyCount.setEnabled(false);
		txtPharmacyCount.setBounds(138, 68, 118, 15);
		txtPharmacyCount.setText("0");
		pnlAgreement.add(txtPharmacyCount);
		
		txtAgreementNo = new JTextField();
		txtAgreementNo.setEnabled(false);
		txtAgreementNo.setBounds(138, 89, 118, 20);
		pnlAgreement.add(txtAgreementNo);
		txtAgreementNo.setColumns(20);
		
		agreementBeginDate = new JDateChooser();
		agreementBeginDate.setBounds(148, 141, 108, 20);
		agreementBeginDate.setDateFormatString("yyyy-MM-dd");		
		agreementBeginDate.setDate(cal.getTime());
		agreementBeginDate.setEnabled(false);
		pnlAgreement.add(agreementBeginDate);
		
		agreementEndDate = new JDateChooser();
		agreementEndDate.setBounds(148, 166, 108, 20);
		agreementEndDate.setDateFormatString("yyyy-MM-dd");		
		agreementEndDate.setDate(cal.getTime());
		agreementEndDate.setEnabled(false);
		pnlAgreement.add(agreementEndDate);
		
		JLabel lblFixBonus = new JLabel("Fix Bonus");
		lblFixBonus.setBounds(10, 51, 118, 14);
		pnlAgreement.add(lblFixBonus);
		
		txtFixBonus = new JTextField();
		txtFixBonus.setBounds(138, 51, 118, 14);
		txtFixBonus.setEnabled(false);
		pnlAgreement.add(txtFixBonus);
		
		JLabel lblNewLabel = new JLabel("AgreementNo 1");
		lblNewLabel.setBounds(10, 117, 118, 14);
		pnlAgreement.add(lblNewLabel);
		
		txtAgrNo1 = new JTextField();
		txtAgrNo1.setEnabled(false);
		txtAgrNo1.setBounds(138, 114, 118, 20);
		pnlAgreement.add(txtAgrNo1);
		txtAgrNo1.setColumns(10);
		
		JPanel pnlTargets = new JPanel();
		pnlTargets.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Targets", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		pnlTargets.setBounds(517, 11, 552, 161);
		getContentPane().add(pnlTargets);
		pnlTargets.setLayout(null);
		
		JScrollPane scrollTarget = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollTarget.setEnabled(false);
		scrollTarget.setBounds(10, 29, 520, 121);
		pnlTargets.add(scrollTarget);
		
		tblTarget = new JTable();
		scrollTarget.setViewportView(tblTarget);
		tblTarget.setModel(new DefaultTableModel(
			new Object[][] {
				{null, "", "", null, null, null, null, null},
				{null, "", "", null, null, null, null, null},
				{null, "", "", null, null, null, null, null},
				{null, "", "", null, null, null, null, null},
				{null, "", "", null, null, null, null, null},
				{null, "", "", null, null, null, null, null},
			},
			new String[] {
				"Id", "Year", "Month", "Trgt1", "Trgt2", "Trgt3", "Trgt4", "Trgt5"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblTarget.getColumnModel().getColumn(0).setPreferredWidth(22);
		tblTarget.getColumnModel().getColumn(1).setPreferredWidth(45);
		tblTarget.getColumnModel().getColumn(2).setPreferredWidth(43);
		tblTarget.getColumnModel().getColumn(3).setPreferredWidth(55);
		tblTarget.getColumnModel().getColumn(4).setPreferredWidth(57);
		tblTarget.getColumnModel().getColumn(5).setPreferredWidth(52);
		tblTarget.getColumnModel().getColumn(6).setPreferredWidth(52);
		tblTarget.getColumnModel().getColumn(7).setPreferredWidth(55);

		cmbMonth = new JComboBox();
		cmbMonth.addItem("");
		cmbMonth.addItem("JAN");
		cmbMonth.addItem("FEB");
		cmbMonth.addItem("MAR");
		cmbMonth.addItem("APR");
		cmbMonth.addItem("MAY");
		cmbMonth.addItem("JUN");
		cmbMonth.addItem("JUL");
		cmbMonth.addItem("AUG");
		cmbMonth.addItem("SEP");
		cmbMonth.addItem("OCT");
		cmbMonth.addItem("NOW");
		cmbMonth.addItem("DEC");
		tblTarget.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(cmbMonth));
		
		cmbYear = new JComboBox();
		cmbYear.addItem("");
		cmbYear.addItem("2016");
		cmbYear.addItem("2017");
		cmbYear.addItem("2018");
		tblTarget.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(cmbYear));
		
		JPanel pnlRelease = new JPanel();
		pnlRelease.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Release", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		pnlRelease.setBounds(10, 301, 336, 156);
		getContentPane().add(pnlRelease);
		pnlRelease.setLayout(null);
		
		JScrollPane scrollRelease = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollRelease.setBounds(10, 28, 316, 117);
		pnlRelease.add(scrollRelease);
		
		tblRelease = new JTable();
		tblRelease.setModel(new DefaultTableModel(
			new Object[][] {
				{null, "", "", null, null, null},
				{null, "", "", null, null, null},
				{null, "", "", null, null, null},
				{null, "", "", null, null, null},
				{null, "", "", null, null, null},
				{null, "", "", null, null, null},
			},
			new String[] {
				"Id", "Year", "Month", "Purchase", "Sell Out", "Amount"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, Object.class, Integer.class, Integer.class, Double.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblRelease.getColumnModel().getColumn(0).setPreferredWidth(15);
		tblRelease.getColumnModel().getColumn(1).setPreferredWidth(40);
		tblRelease.getColumnModel().getColumn(2).setPreferredWidth(44);
		tblRelease.getColumnModel().getColumn(3).setPreferredWidth(63);
		tblRelease.getColumnModel().getColumn(5).setPreferredWidth(59);
		scrollRelease.setViewportView(tblRelease);
		
		JPanel pnlBonusRate = new JPanel();
		pnlBonusRate.setBorder(new TitledBorder(null, "Bonus Rates", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		pnlBonusRate.setBounds(527, 167, 333, 113);
		getContentPane().add(pnlBonusRate);
		pnlBonusRate.setLayout(null);

		JScrollPane scrollBonusRates = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollBonusRates.setBounds(10, 23, 316, 77);
		pnlBonusRate.add(scrollBonusRates);
		
		tblBonusRates = new JTable();
		scrollBonusRates.setViewportView(tblBonusRates);
		tblBonusRates.setModel(new DefaultTableModel(
			new Object[][] {
				{null, "CHAIN BONUS", new Double(0.0), new Double(0.0), new Double(0.0), new Double(0.0)},
				{null, "PHARMACIST BONUS", new Double(0.0), new Double(0.0), new Double(0.0), new Double(0.0)},
				{null, "PHARMACY MANAGER BONUS", new Double(0.0), new Double(0.0), new Double(0.0), new Double(0.0)},
			},
			new String[] {
				"Id", "Bonus Type", "1", "2", "3", "4"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, Object.class, Double.class, Double.class, Double.class, Double.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblBonusRates.getColumnModel().getColumn(0).setPreferredWidth(23);
		tblBonusRates.getColumnModel().getColumn(1).setPreferredWidth(156);
		tblBonusRates.getColumnModel().getColumn(2).setPreferredWidth(30);
		tblBonusRates.getColumnModel().getColumn(3).setPreferredWidth(30);
		tblBonusRates.getColumnModel().getColumn(4).setPreferredWidth(30);
		tblBonusRates.getColumnModel().getColumn(5).setPreferredWidth(30);
		
		JPanel pnlResult = new JPanel();
		pnlResult.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		pnlResult.setBounds(10, 468, 1143, 173);
		getContentPane().add(pnlResult);
		pnlResult.setLayout(null);
		
		JScrollPane scrollResult = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollResult.setBounds(10, 11, 1123, 155);
		pnlResult.add(scrollResult);
		
		tableResult = new JTable();
		tableResult.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
			},
			new String[] {
				"Id", "BRAND", "CHAIN", "PAYMENT TYPE", "TOTAL PAYMENT SUM", "PURCHASING SUM", "PURCHASING AMOUNT", "SELL OUT SUM", "SELL OUT AMOUNT", "DATE", "PAYMENT ORDER NO", "ADV TYPE", "DOCUMENT STATUS", "OUTGOING AMOUNT", "BALANCE"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class, String.class, Integer.class, Integer.class, Double.class, Integer.class, Double.class, String.class, String.class, String.class, String.class, Double.class, Double.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tableResult.getColumnModel().getColumn(0).setPreferredWidth(29);
		tableResult.getColumnModel().getColumn(1).setPreferredWidth(53);
		tableResult.getColumnModel().getColumn(2).setPreferredWidth(55);
		tableResult.getColumnModel().getColumn(3).setPreferredWidth(86);
		tableResult.getColumnModel().getColumn(4).setPreferredWidth(116);
		tableResult.getColumnModel().getColumn(5).setPreferredWidth(107);
		tableResult.getColumnModel().getColumn(6).setPreferredWidth(127);
		tableResult.getColumnModel().getColumn(7).setPreferredWidth(83);
		tableResult.getColumnModel().getColumn(8).setPreferredWidth(103);
		tableResult.getColumnModel().getColumn(10).setPreferredWidth(115);
		tableResult.getColumnModel().getColumn(12).setPreferredWidth(107);
		tableResult.getColumnModel().getColumn(13).setPreferredWidth(111);
		scrollResult.setViewportView(tableResult);
		
		JPanel panelButon = new JPanel();
		panelButon.setBorder(new TitledBorder(null, "Button Group", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		panelButon.setBounds(67, 636, 1064, 62);
		getContentPane().add(panelButon);
		panelButon.setLayout(null);
		
		btnAdd = new JButton("ADD");
		btnAdd.setBounds(50, 28, 139, 23);
		panelButon.add(btnAdd);
		
		btnUpdate = new JButton("UPDATE");
		btnUpdate.setBounds(221, 28, 132, 23);
		panelButon.add(btnUpdate);
		
		btnDelete = new JButton("DELETE");
		btnDelete.setBounds(377, 28, 132, 23);
		panelButon.add(btnDelete);
		
		btnSave = new JButton("SAVE");
		btnSave.setBounds(537, 28, 139, 23);
		panelButon.add(btnSave);
		
		btnCleanUp = new JButton("CLEANUP");
		btnCleanUp.setBounds(701, 28, 139, 23);
		panelButon.add(btnCleanUp);
		
		btnExit = new JButton("EXIT");
		btnExit.setBounds(885, 28, 132, 23);
		panelButon.add(btnExit);
		
		/*Listeners*/
		cmbBoxCountry.setName("cmbCountry");
		cmbBoxCountry.addItemListener(this);
		cmbBoxArea_1.setName("cmbArea");
		cmbBoxArea_1.addItemListener(this);
		cmbBoxRegion_1.setName("cmbRegion");
		
		JPanel pnlSearch = new JPanel();
		pnlSearch.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		pnlSearch.setBounds(10, 11, 493, 99);
		getContentPane().add(pnlSearch);
		pnlSearch.setLayout(null);
		
		JScrollPane scrollSearch = new JScrollPane();
		scrollSearch.setEnabled(false);
		scrollSearch.addMouseListener(new MouseAdapter() {
			
		});
		scrollSearch.setBounds(10, 25, 473, 62);
		pnlSearch.add(scrollSearch);
		
		tblSearch = new JTable();
		tblSearch.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
			},
			new String[] {
				"Id", "BRAND", "CHAIN", "AGREEMENTNO", "BEGIN DATE", "END DATE"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tblSearch.getColumnModel().getColumn(0).setPreferredWidth(28);
		tblSearch.getColumnModel().getColumn(3).setPreferredWidth(91);
		scrollSearch.setViewportView(tblSearch);
		
		/*Listeners*/
		cmbBoxCountry.setName("cmbCountry");
		cmbBoxCountry.addItemListener(this);
		cmbBoxArea_1.setName("cmbArea");
		cmbBoxArea_1.addItemListener(this);
		cmbBoxRegion_1.setName("cmbRegion");
		cmbBoxRegion_1.addItemListener(this);
		
		btnAdd.addActionListener(this);	
		btnDelete.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnSave.addActionListener(this);
		btnExit.addActionListener(this);
		btnCleanUp.addActionListener(this);
		
		tblSearch.setName("SearchTabel");
		tableResult.setName("ResultTabel");
		
		tblSearch.addMouseListener(this);
		tableResult.addMouseListener(this);
				
		createAgreementSearchModel();
		
		validate();
		setVisible(true);
	}
	
	public void itemStateChanged(ItemEvent itemEvent) {
  	  JComboBox cmbBox = (JComboBox)itemEvent.getSource();
  	  String name = cmbBox.getName();
  	  	if(cmbBox.getSelectedItem() != null &&cmbBox.getSelectedItem().toString().length()>0){
	  	  	if(name.equalsIgnoreCase("cmbCountry")){
	  		  cmbBoxArea_1.removeAllItems();
	  		  cmbBoxRegion_1.removeAllItems();
	  		  cmbBoxCity_1.removeAllItems();	   
	  		  cmbBoxArea_1.addItem("");
	  		  try {
				Util.getPRMDataGroupBy("area", "solgar_prm.prm_exps_addresses",cmbBoxArea_1,"country",cmbBoxCountry.getSelectedItem().toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	    		  
	  		  cmbBoxRegion_1.setSelectedIndex(-1);
	  		  cmbBoxCity_1.setSelectedIndex(-1);	    		  	    		  
	  	    }else if(name.equalsIgnoreCase("cmbArea")){
	  		  cmbBoxRegion_1.removeAllItems();
	  		  cmbBoxCity_1.removeAllItems();	    
	  		  cmbBoxRegion_1.addItem("");
	  		  try {
				Util.getPRMDataGroupBy("region", "solgar_prm.prm_exps_addresses",cmbBoxRegion_1,"area",cmbBoxArea_1.getSelectedItem().toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  		  cmbBoxRegion_1.setSelectedIndex(-1);
	  		  cmbBoxCity_1.setSelectedIndex(-1);	    		  
	  	    }else if(name.equalsIgnoreCase("cmbRegion")){
	  		  cmbBoxCity_1.removeAllItems();
	  		  cmbBoxCity_1.addItem("");
	  		  try {
				Util.getPRMDataGroupBy("city", "solgar_prm.prm_exps_addresses",cmbBoxCity_1,"region",cmbBoxRegion_1.getSelectedItem().toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  		  cmbBoxCity_1.setSelectedIndex(-1);
	  	  }
  	  	}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			if (e.getActionCommand().equals("ADD")) {
				
				modelResult.setColumnIdentifiers(headerResult);
				tableResult.setModel(modelResult);						
				final Object[] row = new Object[15];
				row[0] = "0";
				row[1] = cmbBoxCompanyCode.getSelectedItem();
				row[2] = cmbBoxChain.getSelectedItem();
				row[3] = cmbBoxStages.getSelectedItem();
				row[4] = formatAmount(fmtxTotPaySum.getText());
				row[5] = fmtxPurchasingSum.getText();
				row[6] = formatAmount(fmtxPurchasingAmount.getText());
				row[7] = fmtxSellOutSum.getText();
				row[8] = formatAmount(fmtxSellAmountAmount.getText());
				row[9] = dcn.format(paymentDate.getDate());
				row[10] = txtPaymentOrderNo.getText();
				row[11] = cmbBoxAdvertisementType.getSelectedItem();
				row[12] = cmbBoxDocumentStatus.getSelectedItem();
				row[13] = formatAmount(fmtxOutgoingAmount.getText());
				row[14] = formatAmount(fmtxBalance.getText());
				
				modelResult.addRow(row);
				btnSave.setEnabled(true);
				
			}else if (e.getActionCommand().equals("UPDATE")) {
				int i = tableResult.getSelectedRow();
				if (i >= 0){
					modelResult.setValueAt(cmbBoxCompanyCode.getSelectedItem(), i, 1);
					modelResult.setValueAt(cmbBoxChain.getSelectedItem(), i, 2);
					modelResult.setValueAt(cmbBoxStages.getSelectedItem(), i, 3);
					modelResult.setValueAt(formatAmount(fmtxTotPaySum.getText()), i, 4);
					modelResult.setValueAt(fmtxPurchasingSum.getText(), i, 5);
					modelResult.setValueAt(formatAmount(fmtxPurchasingAmount.getText()), i, 6);		
					//modelResult.setValueAt(fmtxSellOutSum.getText(), i, 7);						
					//modelResult.setValueAt(formatAmount(fmtxSellAmountAmount.getText()), i, 8);
					modelResult.setValueAt("0", i, 7);						
					modelResult.setValueAt("0", i, 8);
					modelResult.setValueAt(dcn.format(paymentDate.getDate()), i, 9);
					modelResult.setValueAt(txtPaymentOrderNo.getText(), i, 10);
					modelResult.setValueAt(cmbBoxAdvertisementType.getSelectedItem(), i, 11);
					modelResult.setValueAt(cmbBoxDocumentStatus.getSelectedItem(), i, 12);	
					modelResult.setValueAt(formatAmount(fmtxOutgoingAmount.getText()), i, 13);
					modelResult.setValueAt(formatAmount(fmtxBalance.getText()), i, 14);
				}
				
			}else if (e.getActionCommand().equals("DELETE")) {
				int i = tableResult.getSelectedRow();
				if (i >= 0) {
					modelResult.removeRow(i);
				}
			}else if (e.getActionCommand().equals("SAVE")) {
				ESIBag outBag = new ESIBag();
				int rowResult = tableResult.getRowCount();
				int rowRelease = tblRelease.getRowCount();
				int rowBonus = tblBonusRates.getRowCount();
				
				outBag.put("USERNAME", userName);
				outBag.put("EXPENSETYPE", "CHAIN");
				
				//Release
				for (int j = 0; j  < rowRelease; j++) {
					outBag.put("RELEASETABLE",j,"ORDER", String.valueOf(j));						
					formatToBagValueRelease(outBag, j, 1, "RELEASETABLE", "YEAR",false);
					formatToBagValueRelease(outBag, j, 2, "RELEASETABLE", "MONTH",false);
					formatToBagValueRelease(outBag, j, 3, "RELEASETABLE", "PURCHASEBOXCOUNT",true);
					formatToBagValueRelease(outBag, j, 4, "RELEASETABLE", "SELLOUTBOXCOUNT",true);
					formatToBagValueRelease(outBag, j, 5, "RELEASETABLE", "AMOUNT",true);					
				}
				//Bonus
				for (int j = 0; j  < rowBonus; j++) {
					formatToBagValueBonus(outBag, j, 1, "BONUSTABLE", "BONUSTYPE",false);
					formatToBagValueBonus(outBag, j, 2, "BONUSTABLE", "RATE1",true);
					formatToBagValueBonus(outBag, j, 3, "BONUSTABLE", "RATE2",true);
					formatToBagValueBonus(outBag, j, 4, "BONUSTABLE", "RATE3",true);
					formatToBagValueBonus(outBag, j, 5, "BONUSTABLE", "RATE4",true);
				}								
				//Payment rows
				for (int j = 0; j  < rowResult; j++) {
					formatToBagValueResult(outBag, j, 1, "RESULTTABLE", "BRAND",false);
					formatToBagValueResult(outBag, j, 2, "RESULTTABLE", "CHAIN",false);
					formatToBagValueResult(outBag, j, 3, "RESULTTABLE", "PAYMENTTYPE",false);
					formatToBagValueResult(outBag, j, 4, "RESULTTABLE", "TOTALPAYMENTSUM",true);
					formatToBagValueResult(outBag, j, 5, "RESULTTABLE", "PURCHASINGSUM",true);
					formatToBagValueResult(outBag, j, 6, "RESULTTABLE", "PURCHASINGAMOUNT",true);
					formatToBagValueResult(outBag, j, 7, "RESULTTABLE", "SELLOUTSUM",true);
					formatToBagValueResult(outBag, j, 8, "RESULTTABLE", "SELLOUTAMOUNT",true);
					formatToBagValueResult(outBag, j, 9, "RESULTTABLE", "PAYMENTDATE",false);
					formatToBagValueResult(outBag, j, 10, "RESULTTABLE", "PAYMENTORDERNO",false);
					formatToBagValueResult(outBag, j, 11, "RESULTTABLE", "ADVTYPE",false);
					formatToBagValueResult(outBag, j, 12, "RESULTTABLE", "DOCUMENTSTATUS",false);
					formatToBagValueResult(outBag, j, 13, "RESULTTABLE", "OUTGOINGAMOUNT",true);
					formatToBagValueResult(outBag, j, 14, "RESULTTABLE", "BALANCE",true);					
				}
				int i = tblSearch.getSelectedRow();		
				outBag.put("RELATIONID", modelAgreementSearch.getValueAt(i, 0).toString());
				
				boolean result = Dispatcher.updateChainExps(outBag);
				if(result){
					JOptionPane.showMessageDialog(pnlInfoMsg, "Operation completed succesfully", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			
				
			}else if (e.getActionCommand().equals("CLEANUP")) {
				cleanUp();
			}else if (e.getActionCommand().equals("EXIT")) {
				setVisible(false);
			}
			
			
		} catch (Exception e2) {
			// TODO: handle exception
		}
		
	}
	private String formatAmount(String amount){			
		if(amount == null || amount.length()==0 || amount.trim().equalsIgnoreCase("0")){
			amount = "0";
		}
		if(amount.indexOf(" ")>=0){amount = amount.replaceAll(" ", "");}
		if(amount.indexOf(",")>=0){amount = amount.replaceAll(",", "");}
		String tempAmount = doubleFormatter.format(Double.valueOf(amount));
		return tempAmount;
	}
	
	private void formatToBagValueTarget(ESIBag outBag,int rowNum,int index,String tableBagkey,String valueBagKey,boolean number ){			
		if(tblTarget.getValueAt(rowNum, index) != null && tblTarget.getValueAt(rowNum, index).toString().length()>0){
			outBag.put(tableBagkey,rowNum,valueBagKey, tblTarget.getValueAt(rowNum, index).toString());
		}else{
			if(number){
				outBag.put(tableBagkey,rowNum,valueBagKey, "0");
			}else{
				outBag.put(tableBagkey,rowNum,valueBagKey, "");
			}
		}
	}	
	private void formatToBagValueRelease(ESIBag outBag,int rowNum,int index,String tableBagkey,String valueBagKey,boolean number){			
		if(tblRelease.getValueAt(rowNum, index) != null && tblRelease.getValueAt(rowNum, index).toString().length()>0){
			outBag.put(tableBagkey,rowNum,valueBagKey, tblRelease.getValueAt(rowNum, index).toString());
		}else{
			if(number){
				outBag.put(tableBagkey,rowNum,valueBagKey, "0");
			}else{
				outBag.put(tableBagkey,rowNum,valueBagKey, "");
			}
		}
	}
	private void formatToBagValueBonus(ESIBag outBag,int rowNum,int index,String tableBagkey,String valueBagKey,boolean number){			
		if(tblBonusRates.getValueAt(rowNum, index) != null && tblBonusRates.getValueAt(rowNum, index).toString().length()>0){
			outBag.put(tableBagkey,rowNum,valueBagKey, tblBonusRates.getValueAt(rowNum, index).toString());
		}else{
			if(number){
				outBag.put(tableBagkey,rowNum,valueBagKey, "0");
			}else{
				outBag.put(tableBagkey,rowNum,valueBagKey, "");
			}
		}
	}
	private void formatToBagValueResult(ESIBag outBag,int rowNum,int index,String tableBagkey,String valueBagKey,boolean number){			
		if(tableResult.getValueAt(rowNum, index) != null && tableResult.getValueAt(rowNum, index).toString().length()>0){
			outBag.put(tableBagkey,rowNum,valueBagKey, tableResult.getValueAt(rowNum, index).toString());
		}else{
			if(number){
				outBag.put(tableBagkey,rowNum,valueBagKey, "0");
			}else{
				outBag.put(tableBagkey,rowNum,valueBagKey, "");
			}
		}
	}
	
	private void createAgreementSearchModel() {
		    try{
		    	ESIBag tempBag =null;
				tempBag = Dispatcher.getChainExps(tempBag);
				modelAgreementSearch.setColumnIdentifiers(headerAgreement);
				tblSearch.setModel(modelAgreementSearch);
		    	
				for (int j = 0; j < tempBag.getSize("TABLE"); j++){
					modelAgreementSearch.addRow(new Object [] 
			        		{
			        			tempBag.get("TABLE",j,"ID"),
			        			tempBag.get("TABLE",j,"BRAND"),
			        			tempBag.get("TABLE",j,"CHAIN"),
			        			tempBag.get("TABLE",j,"AGREEMENTNO"),
			        			tempBag.get("TABLE",j,"BEGINDATE"),
			        			tempBag.get("TABLE",j,"ENDDATE")
			        		});		
				}
		    }catch (Exception e) {
				// simdilik yoksa yok
			}
	}
	
	private void createTabelTargetModel(ESIBag tempBag) {
	    try{
	    	DefaultTableModel dtm3 = (DefaultTableModel) tblTarget.getModel();
		   	 for( int j = dtm3.getRowCount() - 1; j >= 0; j-- ) {
		   		 dtm3.removeRow(j);
			 }
	    	
	    	modelTarget.setColumnIdentifiers(headerTarget);
			tblTarget.setModel(modelTarget);
	    	
			for (int j = 0; j < tempBag.getSize("TABLETARGET"); j++){
				modelTarget.addRow(new Object [] 
		        		{
		        			tempBag.get("TABLETARGET",j,"ID"),
		        			tempBag.get("TABLETARGET",j,"YEAR"),
		        			tempBag.get("TABLETARGET",j,"MONTH"),
		        			tempBag.get("TABLETARGET",j,"TRGT1"),
		        			tempBag.get("TABLETARGET",j,"TRGT2"),
		        			tempBag.get("TABLETARGET",j,"TRGT3"),
		        			tempBag.get("TABLETARGET",j,"TRGT4"),
		        			tempBag.get("TABLETARGET",j,"TRGT5")
		        		});		
			}
	    }catch (Exception e) {
			// simdilik yoksa yok
		}
	}

	private void createTabelReleaseModel(ESIBag tempBag) {
	    try{
	    	DefaultTableModel dtm1 = (DefaultTableModel) tblRelease.getModel();
		   	 for( int j = dtm1.getRowCount() - 1; j >= 0; j-- ) {
		   		 dtm1.removeRow(j);
			 }
	    	
	    	modelRelease.setColumnIdentifiers(headerRelease);
			tblRelease.setModel(modelRelease);
	    	
			for (int j = 0; j < tempBag.getSize("TABLERELEASE"); j++){
				modelRelease.addRow(new Object [] 
		        		{
		        			tempBag.get("TABLERELEASE",j,"ID"),
		        			tempBag.get("TABLERELEASE",j,"YEAR"),
		        			tempBag.get("TABLERELEASE",j,"MONTH"),
		        			tempBag.get("TABLERELEASE",j,"PURCHASEBOXCOUNT"),
		        			tempBag.get("TABLERELEASE",j,"SELLOUTBOXCOUNT"),
		        			tempBag.get("TABLERELEASE",j,"AMOUNT")
		        		});		
			}
	    }catch (Exception e) {
			// simdilik yoksa yok
		}
	}
	private void createTabelPaymentModel(ESIBag tempBag) {
	    try{
			
	    	DefaultTableModel dtm2 = (DefaultTableModel) tableResult.getModel();
		   	 for( int j = dtm2.getRowCount() - 1; j >= 0; j-- ) {
		   		 dtm2.removeRow(j);
			 }
	    	
	    	modelResult.setColumnIdentifiers(headerResult);
			tableResult.setModel(modelResult);
	    	
			for (int j = 0; j < tempBag.getSize("TABLERESULT"); j++){
				modelResult.addRow(new Object [] 
		        		{
		        			tempBag.get("TABLERESULT",j,"ID"),
		        			tempBag.get("TABLERESULT",j,"BRAND"),
		        			tempBag.get("TABLERESULT",j,"CHAIN"),
		        			tempBag.get("TABLERESULT",j,"PAYMENTTYPE"),
		        			tempBag.get("TABLERESULT",j,"TOTALPAYMENTSUM"),
		        			tempBag.get("TABLERESULT",j,"PURCHASINGSUM"),
		        			tempBag.get("TABLERESULT",j,"PURCHASINGAMOUNT"),
		        			tempBag.get("TABLERESULT",j,"SELLOUTSUM"),
		        			tempBag.get("TABLERESULT",j,"SELLOUTAMOUNT"),
		        			tempBag.get("TABLERESULT",j,"DATE"),
		        			tempBag.get("TABLERESULT",j,"PAYMENTORDERNO"),
		        			tempBag.get("TABLERESULT",j,"ADVTYPE"),
		        			tempBag.get("TABLERESULT",j,"DOCUMENTSTATUS"),
		        			tempBag.get("TABLERESULT",j,"OUTGOINGAMOUNT"),
		        			tempBag.get("TABLERESULT",j,"BALANCE"),
		        		});		
			}
	    }catch (Exception e) {
			// simdilik yoksa yok
		}
	}
	private void createTabelBonusTypesModel(ESIBag tempBag) {
	    try{
	    	 DefaultTableModel dtm = (DefaultTableModel) tblBonusRates.getModel();
		   	 for( int j = dtm.getRowCount() - 1; j >= 0; j-- ) {
		   		 dtm.removeRow(j);
		   	 }
	    	
	    	modelBonusTypes.setColumnIdentifiers(headerBonusTypes);
			tblBonusRates.setModel(modelBonusTypes);
	    	
			for (int j = 0; j < tempBag.getSize("TABLEBONUSTYPES"); j++){
				modelBonusTypes.addRow(new Object [] 
		        		{
							tempBag.get("TABLEBONUSTYPES",j,"ID"),
							tempBag.get("TABLEBONUSTYPES",j,"BONUSTYPE"),
		        			tempBag.get("TABLEBONUSTYPES",j,"RATE1"),
		        			tempBag.get("TABLEBONUSTYPES",j,"RATE2"),
		        			tempBag.get("TABLEBONUSTYPES",j,"RATE3"),
		        			tempBag.get("TABLEBONUSTYPES",j,"RATE4"),
		        		});		
			}
	    }catch (Exception e) {
			// simdilik yoksa yok
		}
	}
	private void setAgreementBaseInfo(ESIBag tempBag) {
	    try{	    	
	    	cmbBoxCompanyCode.setSelectedItem(tempBag.get("COMPANY")!= null ? tempBag.get("COMPANY").toString() : "");
	    	cmbBoxChain.setSelectedItem(tempBag.get("CHAIN")!= null ? tempBag.get("CHAIN").toString() : "");
	    	cmbBoxCountry.setSelectedItem(tempBag.get("COUNTRY")!= null ? tempBag.get("COUNTRY").toString() : "");
	    	cmbBoxArea_1.setSelectedItem(tempBag.get("AREA")!= null ? tempBag.get("AREA").toString() : "");
	    	cmbBoxRegion_1.setSelectedItem(tempBag.get("REGION")!= null ? tempBag.get("REGION").toString() : "");
	    	cmbBoxCity_1.setSelectedItem(tempBag.get("CITY")!= null ? tempBag.get("CITY").toString() : "");
	    	txtPharmacyCount.setText(tempBag.get("PHARMACYCOUNT")!= null ? tempBag.get("PHARMACYCOUNT").toString() : "");
	    	txtAgreementNo.setText(tempBag.get("AGREEMENTNO")!= null ? tempBag.get("AGREEMENTNO").toString() : "");
	    	agreementBeginDate.setDate(dcn.parse(tempBag.get("BEGINDATE")!= null ? tempBag.get("BEGINDATE").toString() : ""));
	    	agreementEndDate.setDate(dcn.parse(tempBag.get("ENDDATE")!= null ? tempBag.get("ENDDATE").toString() : ""));
	    	txtFixBonus.setText(tempBag.get("BONUSRATE")!= null ? tempBag.get("BONUSRATE").toString() : "");
	    	txtAgrNo1.setText(tempBag.get("AGREEMENTNO1")!= null ? tempBag.get("AGREEMENTNO1").toString() : "");
	    		
    	}catch (Exception e) {
		// simdilik yoksa yok
    	}
	}
	
	private void cleanUp() {
	    try{	    	
	    	cmbBoxCompanyCode.setSelectedItem("");
	    	cmbBoxChain.setSelectedItem("");
	    	cmbBoxCountry.setSelectedItem("");
	    	cmbBoxArea_1.setSelectedItem("");
	    	cmbBoxRegion_1.setSelectedItem("");
	    	cmbBoxCity_1.setSelectedItem("");
	    	txtPharmacyCount.setText("");
	    	txtAgreementNo.setText("");
	    	txtAgrNo1.setText("");
	    	agreementBeginDate.setDate(cal.getTime());
	    	agreementEndDate.setDate(cal.getTime());
	    	
	    	cmbBoxStages.setSelectedItem("");
	    	fmtxTotPaySum.setText("");
	    	fmtxPurchasingSum.setText("");
	    	fmtxPurchasingAmount.setText("");
	    	fmtxSellOutSum.setText("");
	    	fmtxSellAmountAmount.setText("");	    	
	    	paymentDate.setDate(cal.getTime());
	    	txtPaymentOrderNo.setText("");
	    	cmbBoxAdvertisementType.setSelectedItem("");
	    	cmbBoxDocumentStatus.setSelectedItem("");
	    	fmtxOutgoingAmount.setText("");
	    	fmtxBalance.setText("");
			
	    	for( int j = modelBonusTypes.getRowCount() - 1; j >= 0; j-- ) {
	    		modelBonusTypes.removeRow(j);
			}
	    	for( int j = modelRelease.getRowCount() - 1; j >= 0; j-- ) {
	    		modelRelease.removeRow(j);
			}
	    	for( int j = modelResult.getRowCount() - 1; j >= 0; j-- ) {
	    		modelResult.removeRow(j);
			}
	    	for( int j = modelTarget.getRowCount() - 1; j >= 0; j-- ) {
	    		modelTarget.removeRow(j);
			}
	    		
    	}catch (Exception e) {
		// simdilik yoksa yok
    	}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		try {	
			if (e.getComponent().getName().equals("SearchTabel")) {	
				cleanUp();
				ESIBag tempBag = new ESIBag();			
				int i = tblSearch.getSelectedRow();		
				tempBag.put("ID", modelAgreementSearch.getValueAt(i, 0).toString());
				tempBag.put("EXPENSETYPE", "CHAIN");
				tempBag = Dispatcher.getChainExpsAll(tempBag);
				setAgreementBaseInfo(tempBag);
				createTabelPaymentModel(tempBag);
				createTabelTargetModel(tempBag);
				createTabelReleaseModel(tempBag);
				createTabelBonusTypesModel(tempBag);
			}else if (e.getComponent().getName().equals("ResultTabel")) {	
				int i = tableResult.getSelectedRow();				
				cmbBoxStages.setSelectedItem(modelResult.getValueAt(i, 3).toString() != null ? modelResult.getValueAt(i, 3).toString() : "");
				fmtxTotPaySum.setText(modelResult.getValueAt(i, 4).toString() != null ? modelResult.getValueAt(i, 4).toString() : "");
				fmtxPurchasingSum.setText(modelResult.getValueAt(i, 5).toString() != null ? modelResult.getValueAt(i, 5).toString() : "");
				fmtxPurchasingAmount.setText(modelResult.getValueAt(i, 6).toString() != null ? modelResult.getValueAt(i, 6).toString() : "");
				//fmtxSellOutSum.setText(modelResult.getValueAt(i, 7).toString() != null ? modelResult.getValueAt(i, 7).toString() : "");
				
				//fmtxSellAmountAmount.setText(modelResult.getValueAt(i, 8).toString() != null ? modelResult.getValueAt(i, 8).toString() : "");
				if(modelResult.getValueAt(i, 9).toString() != null){
					paymentDate.setDate(dcn.parse(modelResult.getValueAt(i, 9).toString()));
				}else{paymentDate.setDateFormatString("");}
				txtPaymentOrderNo.setText(modelResult.getValueAt(i, 10).toString() != null ? modelResult.getValueAt(i, 10).toString() : "");
				cmbBoxAdvertisementType.setSelectedItem(modelResult.getValueAt(i, 11).toString() != null ? modelResult.getValueAt(i, 11).toString() : "");
				cmbBoxDocumentStatus.setSelectedItem(modelResult.getValueAt(i, 12).toString() != null ? modelResult.getValueAt(i, 12).toString() : "");
				
				fmtxOutgoingAmount.setText(modelResult.getValueAt(i, 13).toString() != null ? modelResult.getValueAt(i, 13).toString() : "");
				fmtxBalance.setText(modelResult.getValueAt(i, 14).toString() != null ? modelResult.getValueAt(i, 14).toString() : "");

		    	
			}
			
		} catch (Exception e2) {
			// TODO: handle exception
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {		
	}
}