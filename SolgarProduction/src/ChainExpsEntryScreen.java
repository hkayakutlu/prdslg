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

import main.Dispatcher;
import util.Util;

import javax.swing.JTextField;

import cb.esi.esiclient.util.ESIBag;

import com.toedter.calendar.JDateChooser;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;

public class ChainExpsEntryScreen extends JFrame implements ActionListener,ItemListener{

	private JPanel contentPane;
	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 750;
	private static final Calendar cal = Calendar.getInstance();
	
	private JPanel pnlInfoMsg;
	private String userName="Hakan KAYAKUTLU";
	private String userBrand="ALL";
	private String userCountry="ALL";
	private String userArea="ALL";	
	private JTextField txtAgreementNo,txtPaymentOrderNo;
	private JComboBox cmbBoxStages,cmbBoxAdvertisementType,cmbBoxDocumentStatus,cmbBoxCountry,cmbBoxArea,
	cmbBoxRegion,cmbBoxCity,cmbBoxCompanyCode,cmbBoxChain,cmbMonth,cmbYear;
	private JDateChooser agreementBeginDate,agreementEndDate,paymentDate;
	public JFormattedTextField txtPharmacyCount,fmtxPurchasingSum,fmtxPurchasingAmount,fmtxSellOutSum
	,fmtxSellAmountAmount,fmtxOutgoingAmount,fmtxBalance,fmtxTotPaySum,txtFixBonus;
	private static final DecimalFormat doubleFormatter = new DecimalFormat("#,###");
	private JTable tblTarget,tblRelease,tblBonusRates,tableResult;
	public JButton btnAdd,btnDelete,btnUpdate,btnSave,btnExit,btnCleanUp;
	public SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
	
	final DefaultTableModel modelResult = new DefaultTableModel();
	final DefaultTableModel modelTarget = new DefaultTableModel();
	final DefaultTableModel modelRelease = new DefaultTableModel();
	final DefaultTableModel modelBonusTypes = new DefaultTableModel();
	String headerResult[] = new String[] {"Id", "BRAND", "CHAIN", "PAYMENT TYPE", "TOTAL PAYMENT SUM", "PURCHASING SUM", "PURCHASING AMOUNT", 
			"SELL OUT SUM", "SELL OUT AMOUNT", "DATE", "PAYMENT ORDER NO", "ADV TYPE", "DOCUMENT STATUS", "OUTGOING AMOUNT", "BALANCE"};
	String headerTarget[] = new String[] {"Id", "Year", "Month", "Trgt1", "Trgt2", "Trgt3", "Trgt4", "Trgt5"};
	String headerRelease[] = new String[] {"Id", "Year", "Month", "Box", "Amount"};
	String headerBonusTypes[] = new String[] {"Id", "Bonus Type", "1", "2", "3", "4"};
	/**
	* Launch the application.
	*/
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ESIBag inBag = new ESIBag();
					ChainExpsEntryScreen frame = new ChainExpsEntryScreen(inBag);
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
	public ChainExpsEntryScreen(ESIBag inBag) throws SQLException {
		super("Chain Expense Entry");
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
	   
	   NumberFormat percentFormat = NumberFormat.getNumberInstance();
	   percentFormat.setMinimumFractionDigits(2);
    
		JPanel pnlStage = new JPanel();
		pnlStage.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Create Payment Row With Below Data", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		pnlStage.setBounds(450, 193, 614, 194);
		getContentPane().add(pnlStage);
		pnlStage.setLayout(null);
		
		JLabel lblStages = new JLabel("Stages");
		lblStages.setBounds(10, 28, 61, 22);
		pnlStage.add(lblStages);
		
		cmbBoxStages = new JComboBox( new String[]{});
		cmbBoxStages.setEditable(false);
		cmbBoxStages.setBounds(137, 25, 167, 22);
		cmbBoxStages.addItem("");
		cmbBoxStages.addItem("MARKETING");
		cmbBoxStages.addItem("CHAIN BONUS");
		cmbBoxStages.addItem("DISCOUNT");		
		cmbBoxStages.addItem("BONUS PAYMENT");		
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
		
		JLabel lblSellOutSum = new JLabel("Sell Out Sum");
		lblSellOutSum.setBounds(10, 139, 117, 14);
		lblSellOutSum.setVisible(false);
		pnlStage.add(lblSellOutSum);
		
		JLabel lblSellOutAmont = new JLabel("Sell Out Amount");
		lblSellOutAmont.setBounds(10, 164, 117, 14);
		lblSellOutAmont.setVisible(false);
		pnlStage.add(lblSellOutAmont);
		
		JLabel lblPayDate = new JLabel("Payment Date");
		lblPayDate.setBounds(333, 32, 93, 14);
		pnlStage.add(lblPayDate);
		
		JLabel lblPayOrderNo = new JLabel("Payment Order No ");
		lblPayOrderNo.setBounds(333, 61, 93, 14);
		pnlStage.add(lblPayOrderNo);
		
		JLabel lblAdvertisementType = new JLabel("Advertisment Type");
		lblAdvertisementType.setBounds(333, 86, 101, 14);
		pnlStage.add(lblAdvertisementType);
		
		JLabel lblDocStatus = new JLabel("Document Status");
		lblDocStatus.setBounds(333, 112, 93, 18);
		pnlStage.add(lblDocStatus);
		
		JLabel lblOutAmount = new JLabel("Outgoing Amount");
		lblOutAmount.setBounds(333, 141, 93, 14);
		pnlStage.add(lblOutAmount);
		
		JLabel lblBalance = new JLabel("Balance");
		lblBalance.setBounds(333, 164, 93, 14);
		pnlStage.add(lblBalance);
		
		fmtxTotPaySum = new JFormattedTextField(doubleFormatter);
		fmtxTotPaySum.setText("0.00");
		fmtxTotPaySum.setBounds(137, 58, 167, 18);
		pnlStage.add(fmtxTotPaySum);
		
		fmtxPurchasingSum = new JFormattedTextField(numberFormatter);
		fmtxPurchasingSum.setText("0");
		fmtxPurchasingSum.setBounds(137, 80, 167, 20);
		pnlStage.add(fmtxPurchasingSum);
		
		fmtxPurchasingAmount = new JFormattedTextField(doubleFormatter);
		fmtxPurchasingAmount.setText("0.00");
		fmtxPurchasingAmount.setBounds(137, 108, 167, 20);
		pnlStage.add(fmtxPurchasingAmount);
		
		fmtxSellOutSum = new JFormattedTextField(numberFormatter);
		fmtxSellOutSum.setText("0");
		fmtxSellOutSum.setBounds(137, 136, 167, 20);
		fmtxSellOutSum.setVisible(false);
		pnlStage.add(fmtxSellOutSum);
		
		fmtxSellAmountAmount = new JFormattedTextField(doubleFormatter);
		fmtxSellAmountAmount.setText("0.00");
		fmtxSellAmountAmount.setBounds(137, 161, 167, 20);
		fmtxSellAmountAmount.setVisible(false);
		pnlStage.add(fmtxSellAmountAmount);
		
		paymentDate = new JDateChooser();
		paymentDate.setDateFormatString("yyyy-MM-dd");
		paymentDate.setBounds(436, 28, 101, 20);
		paymentDate.setDate(cal.getTime());
		pnlStage.add(paymentDate);
		
		txtPaymentOrderNo = new JTextField();
		txtPaymentOrderNo.setBounds(437, 58, 167, 20);
		pnlStage.add(txtPaymentOrderNo);
		txtPaymentOrderNo.setColumns(10);
		
		cmbBoxAdvertisementType = new JComboBox( new String[]{});
		cmbBoxAdvertisementType.addItem("");
		Util.getPRMDataGroupBy("level2", "solgar_prm.prm_chain_exps_types",cmbBoxAdvertisementType,"level1","Advertisement");
		cmbBoxAdvertisementType.setEditable(false);
		cmbBoxAdvertisementType.setSelectedIndex(-1);
		cmbBoxAdvertisementType.setBounds(436, 82, 168, 18);
		pnlStage.add(cmbBoxAdvertisementType);
		
		cmbBoxDocumentStatus = new JComboBox( new String[]{});
		cmbBoxDocumentStatus.addItem("");
		cmbBoxDocumentStatus.addItem("NO");
		cmbBoxDocumentStatus.addItem("WAITING");
		cmbBoxDocumentStatus.addItem("DELIVERED");
		cmbBoxDocumentStatus.setEditable(false);
		cmbBoxDocumentStatus.setBounds(437, 112, 167, 18);
		pnlStage.add(cmbBoxDocumentStatus);
		
		fmtxOutgoingAmount = new JFormattedTextField(doubleFormatter);
		fmtxOutgoingAmount.setText("0.00");
		fmtxOutgoingAmount.setBounds(436, 136, 168, 20);
		pnlStage.add(fmtxOutgoingAmount);

		fmtxBalance = new JFormattedTextField(doubleFormatter);
		fmtxBalance.setText("0.00");
		fmtxBalance.setBounds(437, 161, 167, 20);
		pnlStage.add(fmtxBalance);
		
		JPanel pnlAddress = new JPanel();
		pnlAddress.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Address", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		pnlAddress.setBounds(10, 11, 172, 156);
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
		cmbBoxCountry.setBounds(59, 55, 100, 17);
		Util.getPRMDataGroupBy("country", "solgar_prm.prm_exps_addresses",cmbBoxCountry,"","");
		cmbBoxCountry.setSelectedIndex(-1);
		pnlAddress.add(cmbBoxCountry);
		
		cmbBoxArea = new JComboBox( new String[]{});
		cmbBoxArea = new JComboBox( new String[]{});
		cmbBoxArea.setBounds(59, 81, 100, 17);
		pnlAddress.add(cmbBoxArea);
		
		cmbBoxRegion = new JComboBox( new String[]{});
		cmbBoxRegion = new JComboBox( new String[]{});
		cmbBoxRegion.setBounds(59, 106, 100, 17);
		pnlAddress.add(cmbBoxRegion);
		
		cmbBoxCity = new JComboBox( new String[]{});
		cmbBoxCity = new JComboBox( new String[]{});
		cmbBoxCity.setBounds(59, 128, 100, 17);
		pnlAddress.add(cmbBoxCity);
		
		JLabel lblCompany = new JLabel("Company");
		lblCompany.setBounds(10, 32, 46, 14);
		pnlAddress.add(lblCompany);

		cmbBoxCompanyCode = new JComboBox( new String[]{});
		cmbBoxCompanyCode.setBounds(59, 29, 100, 17);
		cmbBoxCompanyCode.addItem("SOLGAR");
		cmbBoxCompanyCode.addItem("NATURES BOUNTY");
		cmbBoxCompanyCode.setSelectedIndex(0);
		pnlAddress.add(cmbBoxCompanyCode);
		
		JPanel pnlAgreement = new JPanel();
		pnlAgreement.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Agreement Info", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		pnlAgreement.setBounds(192, 11, 258, 171);
		getContentPane().add(pnlAgreement);
		pnlAgreement.setLayout(null);
		
		JLabel lblChainName = new JLabel("Chain");
		lblChainName.setBounds(10, 26, 79, 14);
		pnlAgreement.add(lblChainName);
		
		JLabel lblPharmacyCount = new JLabel("Pharmacy Count");
		lblPharmacyCount.setBounds(10, 72, 118, 14);
		pnlAgreement.add(lblPharmacyCount);
		
		JLabel lblAgreementNo = new JLabel("Agreement No");
		lblAgreementNo.setBounds(10, 97, 106, 14);
		pnlAgreement.add(lblAgreementNo);
		
		JLabel lblAgreementBeginDate = new JLabel("Agreement Begin Date");
		lblAgreementBeginDate.setBounds(10, 115, 124, 14);
		pnlAgreement.add(lblAgreementBeginDate);
		
		JLabel lblAgreementEndDate = new JLabel("Agreement End Date");
		lblAgreementEndDate.setBounds(10, 140, 124, 14);
		pnlAgreement.add(lblAgreementEndDate);

		cmbBoxChain = new JComboBox( new String[]{});
		Util.getPRMData("group_company", "solgar_prm.prm_russia_chains",cmbBoxChain);		
		cmbBoxChain.setSelectedIndex(-1);
		cmbBoxChain.setBounds(138, 23, 118, 20);
		pnlAgreement.add(cmbBoxChain);
		
		txtPharmacyCount = new JFormattedTextField(numberFormatter);
		txtPharmacyCount.setBounds(138, 69, 118, 18);
		txtPharmacyCount.setText("0");
		pnlAgreement.add(txtPharmacyCount);
		
		txtAgreementNo = new JTextField();
		txtAgreementNo.setBounds(138, 94, 118, 18);
		pnlAgreement.add(txtAgreementNo);
		txtAgreementNo.setColumns(20);
		
		agreementBeginDate = new JDateChooser();
		agreementBeginDate.setBounds(148, 115, 108, 20);
		agreementBeginDate.setDateFormatString("yyyy-MM-dd");		
		agreementBeginDate.setDate(cal.getTime());
		pnlAgreement.add(agreementBeginDate);
		
		agreementEndDate = new JDateChooser();
		agreementEndDate.setBounds(148, 140, 108, 20);
		agreementEndDate.setDateFormatString("yyyy-MM-dd");		
		agreementEndDate.setDate(cal.getTime());
		pnlAgreement.add(agreementEndDate);
		
		JLabel lblFixBonus = new JLabel("Fix Bonus");
		lblFixBonus.setBounds(10, 51, 118, 14);
		pnlAgreement.add(lblFixBonus);
		
		txtFixBonus = new JFormattedTextField(percentFormat);
		txtFixBonus.setText("0.00");
		txtFixBonus.setBounds(138, 48, 118, 18);
		pnlAgreement.add(txtFixBonus);
		
		JPanel pnlTargets = new JPanel();
		pnlTargets.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Targets", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		pnlTargets.setBounds(450, 11, 306, 156);
		getContentPane().add(pnlTargets);
		pnlTargets.setLayout(null);
		
		JScrollPane scrollTarget = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollTarget.setBounds(10, 29, 288, 116);
		pnlTargets.add(scrollTarget);
		
		tblTarget = new JTable();
		scrollTarget.setViewportView(tblTarget);
		tblTarget.setModel(new DefaultTableModel(
			new Object[][] {
				{null, "", "", null, null, null, null},
				{null, "", "", null, null, null, null},
				{null, "", "", null, null, null, null},
				{null, "", "", null, null, null, null},
				{null, "", "", null, null, null, null},
				{null, "", "", null, null, null, null},
				{null, "", "", null, null, null, null},
				{null, "", "", null, null, null, null},
				{null, "", "", null, null, null, null},
				{null, "", "", null, null, null, null},
				{null, "", "", null, null, null, null},
				{null, null, null, null, null, null, null},
			},
			new String[] {
				"Id", "Year", "Month", "Trgt1", "Trgt2", "Trgt3", "Trgt4"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class
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
		this.tblTarget.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(this.cmbMonth));
		
		cmbYear = new JComboBox();
		cmbYear.addItem("");
		cmbYear.addItem("2016");
		cmbYear.addItem("2017");
		cmbYear.addItem("2018");
		this.tblTarget.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(this.cmbYear));
		
		JPanel pnlBonusRate = new JPanel();
		pnlBonusRate.setBorder(new TitledBorder(null, "Bonus Rates", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		pnlBonusRate.setBounds(766, 11, 308, 136);
		getContentPane().add(pnlBonusRate);
		pnlBonusRate.setLayout(null);

		JScrollPane scrollBonusRates = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollBonusRates.setBounds(10, 33, 288, 81);
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
		pnlResult.setBorder(new TitledBorder(null, "Result", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		pnlResult.setBounds(10, 398, 1064, 230);
		getContentPane().add(pnlResult);
		pnlResult.setLayout(null);
		
		JScrollPane scrollResult = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollResult.setBounds(10, 33, 1034, 188);
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
		panelButon.setBounds(10, 639, 1064, 62);
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
		cmbBoxArea.setName("cmbArea");
		cmbBoxArea.addItemListener(this);
		cmbBoxRegion.setName("cmbRegion");
		
		JPanel pnlRelease = new JPanel();
		pnlRelease.setBounds(10, 193, 440, 194);
		getContentPane().add(pnlRelease);
		pnlRelease.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Realization", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		pnlRelease.setLayout(null);
		
		JScrollPane scrollRelease = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollRelease.setBounds(10, 28, 420, 154);
		pnlRelease.add(scrollRelease);
		
		tblRelease = new JTable();
		tblRelease.setModel(new DefaultTableModel(
			new Object[][] {
				{"", "", null, null, null},
				{"", "", null, null, null},
				{"", "", null, null, null},
				{"", "", null, null, null},
				{"", "", null, null, null},
				{"", "", null, null, null},
				{"", "", null, null, null},
				{"", "", null, null, null},
				{"", "", null, null, null},
				{"", "", null, null, null},
				{"", "", null, null, null},
				{"", "", null, null, null},
			},
			new String[] {
				"Year", "Month", "Purchasing", "SellOut", "Amount"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Object.class, Integer.class, Integer.class, Double.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblRelease.getColumnModel().getColumn(0).setPreferredWidth(40);
		tblRelease.getColumnModel().getColumn(1).setPreferredWidth(44);
		tblRelease.getColumnModel().getColumn(2).setPreferredWidth(64);
		tblRelease.getColumnModel().getColumn(3).setPreferredWidth(50);
		tblRelease.getColumnModel().getColumn(4).setPreferredWidth(59);
	    this.tblRelease.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(this.cmbYear));
	    this.tblRelease.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(this.cmbMonth));
		scrollRelease.setViewportView(tblRelease);
		cmbBoxRegion.addItemListener(this);
		
		btnAdd.addActionListener(this);	
		btnDelete.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnSave.addActionListener(this);
		btnExit.addActionListener(this);
		btnCleanUp.addActionListener(this);
		
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
				Util.getPRMDataGroupBy("area", "solgar_prm.prm_exps_addresses",cmbBoxArea,"country",cmbBoxCountry.getSelectedItem().toString());
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
				Util.getPRMDataGroupBy("region", "solgar_prm.prm_exps_addresses",cmbBoxRegion,"area",cmbBoxArea.getSelectedItem().toString());
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
	 			  Util.getPRMDataGroupBy("city", "solgar_prm.prm_exps_addresses",cmbBoxCity,"region",cmbBoxRegion.getSelectedItem().toString());
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

				if(cmbBoxStages.getSelectedItem().toString().equalsIgnoreCase("CHAIN BONUS")){
					createChainBonusRows();
				}else if(cmbBoxStages.getSelectedItem().toString().equalsIgnoreCase("MARKETING")){
					createMarketingBonusRows();
				}else{
					/*modelResult.setColumnIdentifiers(headerResult);
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
					
					modelResult.addRow(row);*/
					
					modelResult.setColumnIdentifiers(headerResult);
					tableResult.setModel(modelResult);
					final Object[] row = new Object[15];
					row[0] = "0";
					row[1] = cmbBoxCompanyCode.getSelectedItem();
					row[2] = cmbBoxChain.getSelectedItem();
					row[3] = cmbBoxStages.getSelectedItem();
					row[4] = fmtxTotPaySum.getText();//formatAmount(String.valueOf(fmtxTotPaySum.getText()));
					row[5] = fmtxPurchasingSum.getText();
					row[6] = fmtxPurchasingAmount.getText();//formatAmount(fmtxPurchasingAmount.getText());
					row[7] = "0";
					row[8] = "0";
					row[9] = dcn.format(paymentDate.getDate());
					row[10] = txtPaymentOrderNo.getText();
					row[11] = "";
					row[12] = cmbBoxDocumentStatus.getSelectedItem();
					row[13] = "0";
					row[14] = "0";					
					modelResult.addRow(row);
					
				}
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
					modelResult.setValueAt(fmtxSellOutSum.getText(), i, 7);						
					modelResult.setValueAt(formatAmount(fmtxSellAmountAmount.getText()), i, 8);
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
				int rowTarget = tblTarget.getRowCount();
				int rowRelease = tblRelease.getRowCount();
				int rowBonus = tblBonusRates.getRowCount();
				
				//AgreementBase Info
				outBag.put("EXPENSETYPE", "CHAIN");
				outBag.put("COMPANY", cmbBoxCompanyCode.getSelectedItem().toString());
				outBag.put("CHAIN", cmbBoxChain.getSelectedItem().toString());
				outBag.put("COUNTRY", cmbBoxCountry.getSelectedItem().toString());
				outBag.put("AREA", cmbBoxArea.getSelectedItem().toString());
				outBag.put("REGION", cmbBoxRegion.getSelectedItem().toString());
				outBag.put("CITY", cmbBoxCity.getSelectedItem().toString());
				outBag.put("PHARMACYCOUNT", txtPharmacyCount.getText());
				outBag.put("AGREEMENTNO", txtAgreementNo.getText());
				outBag.put("AGREEMENTBEGINDATE", dcn.format(agreementBeginDate.getDate()));
				outBag.put("AGREEMENTENDDATE", dcn.format(agreementEndDate.getDate()));
				outBag.put("BONUSRATE",  txtFixBonus.getText());
				
				outBag.put("USERNAME", userName);
				
				//Targets
				for (int j = 0; j  < rowTarget; j++) {					
					outBag.put("TARGETTABLE",j,"ORDER", String.valueOf(j));		
					formatToBagValueTarget(outBag, j, 1, "TARGETTABLE", "YEAR",false);
					formatToBagValueTarget(outBag, j, 2, "TARGETTABLE", "MONTH",false);
					formatToBagValueTarget(outBag, j, 3, "TARGETTABLE", "TARGET1",true);
					formatToBagValueTarget(outBag, j, 4, "TARGETTABLE", "TARGET2",true);
					formatToBagValueTarget(outBag, j, 5, "TARGETTABLE", "TARGET3",true);
					formatToBagValueTarget(outBag, j, 6, "TARGETTABLE", "TARGET4",true);
					//formatToBagValueTarget(outBag, j, 7, "TARGETTABLE", "TARGET5",true);
				}
				//Release
				for (int j = 0; j  < rowRelease; j++) {
					outBag.put("RELEASETABLE",j,"ORDER", String.valueOf(j));						
					formatToBagValueRelease(outBag, j, 0, "RELEASETABLE", "YEAR",false);
					formatToBagValueRelease(outBag, j, 1, "RELEASETABLE", "MONTH",false);
					formatToBagValueRelease(outBag, j, 2, "RELEASETABLE", "PURCHASEBOXCOUNT",true);
					formatToBagValueRelease(outBag, j, 3, "RELEASETABLE", "SELLOUTBOXCOUNT",true);
					formatToBagValueRelease(outBag, j, 4, "RELEASETABLE", "AMOUNT",true);					
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
				
				boolean result = Dispatcher.saveChainExps(outBag);
				if(result){
					JOptionPane.showMessageDialog(pnlInfoMsg, "Operation completed succesfully", "Information", JOptionPane.INFORMATION_MESSAGE);
					cleanUp();
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
	private void createChainBonusRows() {
		
		int rowTarget = tblTarget.getRowCount();
		int rowRelease = tblRelease.getRowCount();
		int rowBonus = tblBonusRates.getRowCount();
			
		//Release
		for (int j = 0; j  < rowRelease; j++) {
			if(tblRelease.getValueAt(j, 2)!=null){
				String year = tblRelease.getValueAt(j, 0).toString();
				String month = tblRelease.getValueAt(j, 1).toString();
				int boxCount = Integer.parseInt(tblRelease.getValueAt(j, 2).toString());//BOXCOUNT
				//int boxCountSellOut = Integer.parseInt(tblRelease.getValueAt(j, 3).toString());//BOXCOUNT Sell out
				String  amount = tblRelease.getValueAt(j, 4).toString();//AMOUNT	
				double amountDbl = Double.parseDouble(amount);
				
				for (int k = 0; k  < rowTarget; k++) {			
					if(tblTarget.getValueAt(k, 1) != null){
						if(year.equalsIgnoreCase(tblTarget.getValueAt(k, 1).toString())
								&& month.equalsIgnoreCase(tblTarget.getValueAt(k, 2).toString())){
						double bonusRate = 0;
						int target1 = Integer.parseInt(tblTarget.getValueAt(k, 3).toString());
						int target2 = Integer.parseInt(tblTarget.getValueAt(k, 4).toString());
						int target3 = Integer.parseInt(tblTarget.getValueAt(k, 5).toString());
						int target4 = Integer.parseInt(tblTarget.getValueAt(k, 6).toString());
						//int target5 = Integer.parseInt(tblTarget.getValueAt(j, 7).toString());
						String strbonusRate = "";
						
						if(boxCount<target1){/*0 cek*/}else{//Chain Bonus
							if(boxCount<target2){
								strbonusRate =tblBonusRates.getValueAt(0, 2).toString().replace(",", ".");
								bonusRate = Double.parseDouble(strbonusRate);}
							else{
								if(boxCount<target3){strbonusRate =tblBonusRates.getValueAt(0, 3).toString().replace(",", ".");bonusRate = Double.parseDouble(strbonusRate);}
								else{	
									if(boxCount<target4){strbonusRate =tblBonusRates.getValueAt(0, 4).toString().replace(",", ".");bonusRate = Double.parseDouble(strbonusRate);}
									else{
										strbonusRate =tblBonusRates.getValueAt(0, 5).toString().replace(",", ".");
										bonusRate = Double.parseDouble(strbonusRate);
										/*if(boxCount<target5){bonusRate = Integer.parseInt(tblBonusRates.getValueAt(0, 5).toString());}
										else{
											bonusRate = Integer.parseInt(tblBonusRates.getValueAt(0, 5).toString());
										}*/
									}
								}
							}
						}
						String montNum = getMonthToNumber(month);
						double paymentSumChainBonus = (amountDbl*bonusRate)/100;
						System.out.println(paymentSumChainBonus);						
						createPaymentRow(j, year, boxCount, amount, paymentSumChainBonus,montNum,"CHAIN BONUS",String.valueOf(j+1));
						
						String strbonusRate1 = tblBonusRates.getValueAt(1, 2).toString();
						String strbonusRate2 = tblBonusRates.getValueAt(1, 3).toString();
						String strbonusRate3 = tblBonusRates.getValueAt(1, 4).toString();
						String strbonusRate4 = tblBonusRates.getValueAt(1, 5).toString();
						double bonusRate1 =Double.parseDouble(strbonusRate1.replace(",", "."));
						double bonusRate2 =Double.parseDouble(strbonusRate2.replace(",", "."));
						double bonusRate3 =Double.parseDouble(strbonusRate3.replace(",", "."));
						double bonusRate4 =Double.parseDouble(strbonusRate4.replace(",", "."));
						
						if(bonusRate1>0||bonusRate2>0||bonusRate3>0||bonusRate4>0){//Pharmacist Bonus					
							if(boxCount<target1){/*0 cek*/}else{//Chain Bonus
								if(boxCount<target2){
									bonusRate = bonusRate1;}
								else{
									if(boxCount<target3){bonusRate = bonusRate2;}
									else{	
										if(boxCount<target4){bonusRate =bonusRate3;}
										else{
											bonusRate = bonusRate4;
											/*if(boxCount<target5){bonusRate = Integer.parseInt(tblBonusRates.getValueAt(0, 5).toString());}
											else{
												bonusRate = Integer.parseInt(tblBonusRates.getValueAt(0, 5).toString());
											}*/
										}
									}
								}
							}
							
							double paymentSumPharmacistBonus = (amountDbl*bonusRate)/100;
							System.out.println(paymentSumPharmacistBonus);						
							createPaymentRow(j, year, boxCount, amount, paymentSumPharmacistBonus,montNum,"PHARMACANT BONUS",String.valueOf(j+11));
						}
						 strbonusRate1 = tblBonusRates.getValueAt(2, 2).toString();
						 strbonusRate2 = tblBonusRates.getValueAt(2, 3).toString();
						 strbonusRate3 = tblBonusRates.getValueAt(2, 4).toString();
						 strbonusRate4 = tblBonusRates.getValueAt(2, 5).toString();
						 bonusRate1 =Double.parseDouble(strbonusRate1.replace(",", "."));
						 bonusRate2 =Double.parseDouble(strbonusRate2.replace(",", "."));
						 bonusRate3 =Double.parseDouble(strbonusRate3.replace(",", "."));
						 bonusRate4 =Double.parseDouble(strbonusRate4.replace(",", "."));
						
						if(bonusRate1>0||bonusRate2>0||bonusRate3>0||bonusRate4>0){//Pharmacy Manager Bonus					
							if(bonusRate1>0||bonusRate2>0||bonusRate3>0){//Pharmacist Bonus					
								if(boxCount<target1){/*0 cek*/}else{//Chain Bonus
									if(boxCount<target2){
										bonusRate = bonusRate1;}
									else{
										if(boxCount<target3){bonusRate = bonusRate2;}
										else{	
											if(boxCount<target4){bonusRate =bonusRate3;}
											else{
												bonusRate = bonusRate4;
												/*if(boxCount<target5){bonusRate = Integer.parseInt(tblBonusRates.getValueAt(0, 5).toString());}
												else{
													bonusRate = Integer.parseInt(tblBonusRates.getValueAt(0, 5).toString());
												}*/
											}
										}
									}
								}
							
							double paymentSumPharmacistBonus = (amountDbl*bonusRate)/100;
							System.out.println(paymentSumPharmacistBonus);						
							createPaymentRow(j, year, boxCount, amount, paymentSumPharmacistBonus,montNum,"PHARM MANAGER BONUS",String.valueOf(j+21));
						}
						
							break;
					}else{//Exception 
						
					}	
					}
					}
			}
			
			}
			}
	
		
	}
private void createMarketingBonusRows() {
		int rowRelease = tblRelease.getRowCount();
			
		//Release
		for (int j = 0; j  < rowRelease; j++) {
			if(tblRelease.getValueAt(j, 3)!=null){
				String year = tblRelease.getValueAt(j, 0).toString();
				String month = tblRelease.getValueAt(j, 1).toString();
				int boxCountSellOut = Integer.parseInt(tblRelease.getValueAt(j, 3).toString());//BOXCOUNT Sell out
				String  amount = tblRelease.getValueAt(j, 4).toString();//AMOUNT	
				double amountDbl = Double.parseDouble(amount);
				String strbonusRate = txtFixBonus.getText().toString();
				strbonusRate = strbonusRate.replace(",", ".");
				
				double bonusRate = Double.parseDouble(strbonusRate);
				
				String montNum = getMonthToNumber(month);
				double paymentSumChainBonus = (amountDbl*bonusRate)/100;
				System.out.println(paymentSumChainBonus);						
				createPaymentRow(j, year, boxCountSellOut, amount, paymentSumChainBonus,montNum,"MARKETING",String.valueOf(j+1));		
				
				}else{//Exception 
					
				}	
				
			}
		
	}

	private void createPaymentRow(int j, String year, int boxCount,
			String amount, double paymentSum, String montNum,String bonusType,String paymentIndex) {
		modelResult.setColumnIdentifiers(headerResult);
		tableResult.setModel(modelResult);
		final Object[] row = new Object[15];
		row[0] = "0";
		row[1] = cmbBoxCompanyCode.getSelectedItem();
		row[2] = cmbBoxChain.getSelectedItem();
		row[3] = bonusType;
		row[4] = formatAmount(String.valueOf(paymentSum));
		row[5] = String.valueOf(boxCount);
		row[6] = formatAmount(amount);
		row[7] = "0";
		row[8] = "0";
		row[9] = year+"-"+montNum+"-01";
		row[10] = txtPaymentOrderNo.getText()+"-"+paymentIndex;
		row[11] = "";
		row[12] = cmbBoxDocumentStatus.getSelectedItem();
		row[13] = "0";
		row[14] = "0";					
		modelResult.addRow(row);
	}
	
	private String getMonthToNumber(String monthStr) {
		String beginMonth="";
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
		}
		return beginMonth;
	}

	private String formatAmount(String amount){			
		if(amount == null || amount.length()==0){
			amount = "0";
		}
		if(amount.indexOf(" ")>=0){amount = amount.replaceAll(" ", "");}
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
	private void cleanUp() {
	   try{	    	
	   	cmbBoxCompanyCode.setSelectedItem("");
	   	cmbBoxChain.setSelectedItem("");
	   	cmbBoxCountry.setSelectedItem("");
	   	cmbBoxArea.setSelectedItem("");
	   	cmbBoxRegion.setSelectedItem("");
	   	cmbBoxCity.setSelectedItem("");
	   	txtPharmacyCount.setText("");
	   	txtAgreementNo.setText("");
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
	   	
	   	 DefaultTableModel dtm = (DefaultTableModel) tblBonusRates.getModel();
	   	 for( int j = dtm.getRowCount() - 1; j >= 0; j-- ) {
	   		 dtm.removeRow(j);
				}
	   	 DefaultTableModel dtm1 = (DefaultTableModel) tblRelease.getModel();
	   	 for( int j = dtm1.getRowCount() - 1; j >= 0; j-- ) {
	   		 dtm1.removeRow(j);
				}
	   	 DefaultTableModel dtm2 = (DefaultTableModel) tableResult.getModel();
	   	 for( int j = dtm2.getRowCount() - 1; j >= 0; j-- ) {
	   		 dtm2.removeRow(j);
				}
	   	 DefaultTableModel dtm3 = (DefaultTableModel) tblTarget.getModel();
	   	 for( int j = dtm3.getRowCount() - 1; j >= 0; j-- ) {
	   		 dtm3.removeRow(j);
				}
	   		
    	}catch (Exception e) {
		// simdilik yoksa yok
    	}
	}
}