package src;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.DecimalFormat;

import jxl.*;

import javax.swing.filechooser.*;

import main.Companies;
import main.ConnectToDb;
import main.SendMail;
import cb.esi.esiclient.util.ESIBag;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;


public class ExcelUpload extends JFrame implements ActionListener {

	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 900;
    private static final String LINE_BREAK = "\r"; 
    private static final String CELL_BREAK = "\t";

	private JTextArea textArea;
	private JTextField fileName,txtTotalAmount,txtTotalCount,txtProductType;
	private JScrollPane jScroll;
	private final JFileChooser fc = new JFileChooser();
	private File file;
	private JLabel lblFileName,lblTotalAmount,lblTotalCount,lblProductType;
	private JPanel paramPanel,paramPanel1,paramPanel2, pnlInsertInst,pnlErrorMsg;
	public JButton butClear, butOpen,butExceltoScreen,btnSave,btnExit;
	private ExcelActionHandler excelActionHandler;
	private JTable resultTable;
	private JComboBox cmbBoxCompanies,cmbBoxDates;
	
	DefaultTableModel dtm = new DefaultTableModel(0, 0);
	String header[] = new String[] { "Row Number","Main Group","Sub Group","Pharmacy Address", "Product Name","Aptek No", "Count","Amount","SalesDate","Index","City"};

	//
	public ExcelUpload(){
		// This builds the JFrame portion of the object
		super("Excel Upload");
		Toolkit toolkit;
		Dimension dim;
		int screenHeight, screenWidth;
		String prmData="";
		/**
		 * Set skin
		 */
		try{
			//UIManager.setLookAndFeel(new SubstanceBusinessBlueSteelLookAndFeel());
		}catch(Exception e){
				
		}
		// Initialize basic layout properties
		setBackground(Color.lightGray);
		getContentPane().setLayout(new BorderLayout(5, 5));
		// Set the frame's display to be WIDTH x HEIGHT in the middle of the screen
		toolkit = Toolkit.getDefaultToolkit();
		dim = toolkit.getScreenSize();
		screenHeight = dim.height;
		screenWidth = dim.width;
		setBounds((screenWidth - FRAME_WIDTH) / 2, (screenHeight - FRAME_HEIGHT) / 2, FRAME_WIDTH, FRAME_HEIGHT);

		excelActionHandler = new ExcelActionHandler(this);
		// scroll pane
		jScroll = new JScrollPane();
		
		resultTable = new JTable(20, 8);		
		dtm.setColumnIdentifiers(header);
		resultTable.setModel(dtm);
		
		jScroll.setViewportView(resultTable);
		jScroll.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(jScroll, BorderLayout.CENTER);
		
		
		// add parameter panel
		paramPanel = new JPanel(new GridLayout(0, 3, 5, 5));
		
		
		//labels
		lblFileName = new JLabel("File name please fill up with only xls type Excel");
		lblTotalAmount = new JLabel("Total Amount");
		lblTotalCount = new JLabel("Total Count");
		lblProductType = new JLabel("Product Type");
		//lblEmpty = new JLabel("");
		cmbBoxCompanies = new JComboBox( new String[]{});		
		ConnectToDb.getPRMData("group_company", "solgar_prm.prm_russia_chains",cmbBoxCompanies);				
		cmbBoxCompanies.setMaximumRowCount(50);
		cmbBoxCompanies.setEditable(true);
		
		cmbBoxDates = new JComboBox( new String[]{});		
		ConnectToDb.getPRMData("report_date", "solgar_prm.prm_report_dates",cmbBoxDates);				
		cmbBoxDates.setMaximumRowCount(50);
		cmbBoxDates.setEditable(true);
		
		
		//text fields
		fileName = new JTextField(20);		
		txtTotalAmount = new JTextField(20);
		txtTotalCount = new JTextField(20);
		txtProductType = new JTextField(20);
		
		//Panels
		//pnlInsertInst = new JPanel(new GridLayout(0, 6, 5, 5));
		paramPanel1 = new JPanel(new GridLayout(0, 4, 5, 5));
		paramPanel2 = new JPanel(new GridLayout(0, 2, 5, 5));
		
		// buttons		
		butClear = new JButton("Clear");
		butOpen = new JButton("Open");	
		butExceltoScreen = new JButton("setDataToScreen");
		
		btnSave = new JButton("Save");
		btnExit = new JButton("Exit");
		
		butClear.addActionListener(excelActionHandler);
		butOpen.addActionListener(excelActionHandler);
		butExceltoScreen.addActionListener(excelActionHandler);
		btnSave.addActionListener(excelActionHandler);	
		btnExit.addActionListener(excelActionHandler);	
		cmbBoxCompanies.addActionListener(excelActionHandler);
		
		btnSave.setEnabled(false);
		butOpen.setEnabled(true);	
		butClear.setEnabled(false);
		butExceltoScreen.setEnabled(false);
		
		
		// filename
		paramPanel.add(lblFileName);
		paramPanel.add(fileName);
		paramPanel.add(butOpen);		
		paramPanel.add(cmbBoxDates);
		
		// buttons
		paramPanel.add(butClear);
		paramPanel.add(butExceltoScreen);
		paramPanel.add(cmbBoxCompanies);
		paramPanel.add(btnSave);
		paramPanel.add(btnExit);
		
		//paramPanel.add(resultTable);

		//paramPanel.add(new JLabel(""));
		//paramPanel.add(pnlInsertInst);	
		
		
		paramPanel1.add(lblTotalAmount);
		paramPanel1.add(txtTotalAmount);
		paramPanel1.add(lblTotalCount);		
		paramPanel1.add(txtTotalCount);		
		paramPanel.add(paramPanel1);
		
		txtTotalAmount.setEnabled(false);
		txtTotalCount.setEnabled(false);
		
		
		paramPanel2.add(lblProductType);		
		paramPanel2.add(txtProductType);		
		paramPanel.add(paramPanel2);
		
		txtProductType.setEnabled(false);
		
		//
		paramPanel.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanel, BorderLayout.NORTH);
		
		fc.addChoosableFileFilter(new ExcelFilter());

		// Put the final touches to the JFrame object
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		validate();
		setVisible(true);
	}
	
	class ExcelActionHandler implements ActionListener, ClipboardOwner {

		Workbook workbook;
		Sheet sheet;
		ExcelUpload parent;
		int sheetCount = 0;
		int currentSheet = 0;
		
		public ExcelActionHandler(ExcelUpload parent){
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			try {
				if (ae.getActionCommand().equals("Open")) {
					openExcelFile();
				}
				if (ae.getActionCommand().equals("setDataToScreen")) {
					runExcelSetDataToScreen();
				}
				// clear
				if (ae.getActionCommand().equals("Clear")) {
					clearAll();
				}
				if (ae.getActionCommand().equals("Save")) {
					save();
				}
				if (ae.getActionCommand().equals("Exit")) {
					setVisible(false);
				}
				
			} catch (Exception ex) {
				String message = ex.getMessage();
				JOptionPane.showMessageDialog(pnlErrorMsg, message +" Global Problem" , 
						"Error Message", JOptionPane.ERROR_MESSAGE);
			}
		}
		public void openExcelFile() {
			try {
				//
				int returnVal = fc.showOpenDialog(jScroll);
				//
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// open excel sheet
					file = fc.getSelectedFile();
					fileName.setText(file.getName());
					workbook = Workbook.getWorkbook(file);
					
					if(file.getName().indexOf("A5_")>=0){ 
						currentSheet = 1;
					}else{
						currentSheet = 0;
					}				
					
					sheetCount = workbook.getSheets().length;					
					sheet = workbook.getSheet(currentSheet);
					parent.butExceltoScreen.setEnabled(true);
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
		
		public void runExcelSetDataToScreen() {
			
			try {
				ESIBag outBag = new ESIBag();
				String fileName = file.getName();
				fileName = fileName.toUpperCase();
				resultTable.setAutoResizeMode(1);
				int verticalLimit = sheet.getRows();
				int horizontalLimit =sheet.getColumns() ;
				String selectedItem = cmbBoxCompanies.getSelectedItem().toString();
				//2016-01-01
				String selectedDate = cmbBoxDates.getSelectedItem().toString();
				selectedDate = selectedDate.substring(5, 7) +"_"+selectedDate.substring(0, 4);
				int totalCount = 0;
				double totalAmount = 0;
				String product ="";
				DecimalFormat df = new DecimalFormat("#.00");
				
				if(fileName.indexOf(selectedItem)<0||fileName.indexOf(selectedDate)<0){//Show error message
					parent.butClear.setEnabled(true);
					parent.butExceltoScreen.setEnabled(false);
					parent.btnSave.setEnabled(false);
					JOptionPane.showMessageDialog(pnlErrorMsg, "Please select correct file", "Error", JOptionPane.ERROR_MESSAGE);
				}else{						
					if(fileName.indexOf("AVE")>=0){ //dikey
						outBag = Companies.aveParser(sheet,selectedItem, verticalLimit, horizontalLimit);					
					}else if (fileName.indexOf("RADUGA")>=0){//Yatay
						outBag = Companies.radugaParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("ALOE")>=0){//dikey icice
						outBag = Companies.aloeParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("URAZMANOV")>=0){//Yatay
						outBag = Companies.urazmanovParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("A5_")>=0){//Dikey
						outBag = Companies.a5Parser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("RIGLA")>=0){//Dikey
						outBag =Companies.riglaParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("UNIVERSITETSKIE")>=0){//Dikey
						outBag =Companies.universtitetskieParserOld(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("PLANET_ZDOROVYIA")>=0){////dikey icice eczane
						outBag =Companies.planetZdoroviya(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("EDIFARM")>=0){////dikey icice eczane
						outBag =Companies.ediFarmParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("EDELVES")>=0){////dikey icice eczane
						outBag = Companies.edelvesParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("FARMLEND")>=0){////dikey icice eczane
						outBag = Companies.farmlendParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("FIALKA")>=0){////dikey icice eczane
						outBag = Companies.fialkaParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("KAZANSKY_APTEKI")>=0){////dikey icice eczane
						outBag = Companies.kazansky_Apteki_Parser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("MELODIYA_ZDOROVIE_KRASNODAR")>=0){////dikey icice eczane
						outBag = Companies.melodiya_Zdorovie_krasnodar_Parser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("NEVIS")>=0){////dikey icice eczane
						outBag = Companies.nevisParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("NOVAYAAPTEKA_KALININGRAD")>=0){////dikey icice eczane
						outBag = Companies.novayaAptekaKaliningradParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("PHARMAKOR")>=0){////dikey icice eczane
						outBag = Companies.pharmakorParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("RIFARM")>=0){////dikey icice eczane
						outBag = Companies.rifarmParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("SAKURA")>=0){
						outBag = Companies.sakuraParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("SAMSON_PHARMA")>=0){
						outBag = Companies.samsonPharmaParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("UNIVERSITETSKY_APTEKI")>=0){
						outBag = Companies.universitestskyAptekiParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("VITA_SAMARA")>=0){
						outBag = Companies.vitaSamaraParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("VITA_TOMSK")>=0){
						outBag = Companies.vitaTomskParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("ZDOROV.RU")>=0){
						outBag = Companies.zdorovRuParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("ZHIVIKA")>=0){
						outBag = Companies.zhivikaParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("MELODIYA_ZDOROVIE_NOVOSIBIRSK")>=0){
						outBag = Companies.melodiyaZdorovyNovosibirskParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("PERVAYA_POMOSH_BARNAUL")>=0){
						outBag = Companies.pervayaPomoshBarnaulParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("DOCTOR_STOLETOV")>=0){
						outBag = Companies.doctorStoletovParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("MOSAPTEKA")>=0){
						outBag = Companies.mosAptekaParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("PANAZEYA")>=0){
						outBag = Companies.panazeyaParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("PERVAYA_APTEKA_ARKHANGELSK")>=0){
						outBag = Companies.pervayaAptekaArhangelskParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("SOSIALNAYA_APTEKA")>=0){
						outBag = Companies.sosialnayaAptekaParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("VESTA")>=0){
						outBag = Companies.vestaParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("APTEKI_KUBANI")>=0){
						outBag = Companies.aptekiKubaniParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("APTEKA245")>=0){
						outBag = Companies.apteka245Parser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("PILULI")>=0){
						outBag = Companies.piluliParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}else if (fileName.indexOf("OTHERS")>=0){
						outBag = Companies.othersParser(sheet,selectedItem, verticalLimit, horizontalLimit);		
					}
					
					
					// add row dynamically into the table      
					for (int i = 0; i < outBag.getSize("TABLE"); i++){
					    String countStr = outBag.get("TABLE",i,"COUNT");   
					    String amountStr = outBag.get("TABLE",i,"AMOUNT");
					    product = outBag.get("TABLE",i,"PRODUCT");
						dtm.addRow(new Object[] 
					        		{ i+1,
					        		outBag.get("TABLE",i,"MAINGROUP"),
					        		outBag.get("TABLE",i,"SUBGROUP"),
					        		outBag.get("TABLE",i,"PHARMACY"), 
					        		product, 
					        		outBag.get("TABLE",i,"APTEKNO"),
					        		countStr,
					        		amountStr,
					        		cmbBoxDates.getSelectedItem().toString(),
					        		outBag.get("TABLE",i,"SALESREADER"),
					        		outBag.get("TABLE",i,"CITY")
					        		});

					        if(countStr.indexOf(".")>0){
					        	countStr = countStr.substring(0, countStr.indexOf("."));
							}
						    if(countStr.indexOf(",")>0){
						    	countStr = countStr.substring(0, countStr.indexOf(","));
							}
						    int intCount =  Integer.parseInt(countStr);
						    totalCount = totalCount + intCount;
						    
						    if(amountStr != null && amountStr.trim().length()>0){
							    amountStr = amountStr.replaceAll("\\s+", "");	
							    amountStr = amountStr.replaceAll("�", "");
							    amountStr = amountStr.trim();
							    if(amountStr.equalsIgnoreCase(",00")){
							    	amountStr = "0.00";
								}
							    if(amountStr.indexOf(",")>=0){
							    	amountStr = amountStr.replaceAll(",", ".");
								}
							    totalAmount = totalAmount + Double.parseDouble(amountStr);
						    }else{
						    	totalAmount = totalAmount + Double.parseDouble("0.00");
						    }
							        
					 }
					txtTotalAmount.setText(String.valueOf(df.format(totalAmount)));
					txtTotalCount.setText(String.valueOf(totalCount));
					
					if(product.toUpperCase().indexOf("������")>=0 || product.toUpperCase().indexOf("BOUNTY")>=0){
						txtProductType.setText("NATURES BOUNTY");
					}else{
						txtProductType.setText("SOLGAR");
					}
					
				}
	
				//tableCopyToClipboard(true);
				parent.butClear.setEnabled(true);
				parent.butExceltoScreen.setEnabled(false);
				parent.btnSave.setEnabled(true);
				
			} catch (Exception ex) {
				for( int i = dtm.getRowCount() - 1; i >= 0; i-- ) {
					dtm.removeRow(i);
				}
				String message = ex.getMessage();
				ex.printStackTrace();
				JOptionPane.showMessageDialog(pnlErrorMsg, message +" Excel format incorrect please check file and chain" , "Error Message", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		public void save() {
			try {
				
				String productType = "BN";
				if(txtProductType.getText().equalsIgnoreCase("SOLGAR")){
					productType = "SL";
				}
				
				String mainGroup = resultTable.getValueAt(1, 1).toString();
				String productName = resultTable.getValueAt(1, 4).toString();
				String salesReader = resultTable.getValueAt(1, 9).toString();
				String salesDate = resultTable.getValueAt(1, 8).toString();
				
				if(ConnectToDb.controlLoadSales(mainGroup, salesDate, productName, salesReader)){
					JOptionPane.showMessageDialog(pnlErrorMsg, "File already load please check", "Error", JOptionPane.ERROR_MESSAGE);
				}else{			
					ConnectToDb.setSaleDataToDB(resultTable);
					
					String emailText = "Dear reciepents,\n\n"+
					cmbBoxCompanies.getSelectedItem().toString() +" "+txtProductType.getText()
					+" upload to system for "+
					cmbBoxDates.getSelectedItem().toString()+".\n\nTotal Count:"+
					txtTotalCount.getText()+".\n\nTotal Amount:"+ txtTotalAmount.getText();
					
					SendMail.sendEmailToReceipents("hakan.kayakutlu@gmail.com","hgokmen@solgarvitamin.ru","", "Auto mail sale report", emailText);
					
					for( int i = dtm.getRowCount() - 1; i >= 0; i-- ) {
						dtm.removeRow(i);
					}
					txtTotalAmount.setText("");
					txtTotalCount.setText("");
				
				}
				
				
			} catch (Exception ex) {
				String message = ex.getMessage();
				ex.printStackTrace();
			}
		}

		
	
		public void clearAll() throws Exception {
			try {
				parent.setTitle("Excel Upload");
				
				workbook.close();
				
				// clear text area
				fileName.setText("");
				cmbBoxCompanies.setSelectedItem("");
				cmbBoxDates.setSelectedItem("");
				//resultTable.setModel(new DefaultTableModel());
				for( int i = dtm.getRowCount() - 1; i >= 0; i-- ) {
					dtm.removeRow(i);
				}
				
				parent.butOpen.setEnabled(true);
				parent.btnSave.setEnabled(false);
				parent.butExceltoScreen.setEnabled(false);
				parent.butClear.setEnabled(false);
				
				txtTotalAmount.setText("");
				txtTotalCount.setText("");			
				
			} catch (Exception ex) {
				throw ex;
			}
		}
		
		public void setClipboardContents(String aString) {
			StringSelection stringSelection = new StringSelection(aString);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, this);
		}

	   /**
		 * Empty implementation of the ClipboardOwner interface.
		 */
		@Override
		public void lostOwnership(Clipboard aClipboard, Transferable aContents) {
			// do nothing
		}

		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("Refresh")) {
			try {
			} catch (Exception ex) {
				String message = ex.getMessage();
				ex.printStackTrace();
			}
		} else if (ae.getActionCommand().equals("OK")) {
		} else if (ae.getActionCommand().equals("Cancel")) {
		}
	}
	public static void main(String[] args) {
		try {
			ExcelUpload xlReader = new ExcelUpload();
		} catch (Exception ex) {
			ex.printStackTrace();
			exit();
		}
	}
	// A common point of exit
	public static void exit() {
		
		System.out.println("\nThank you for using ExcelUploads");
		System.exit(0);
	}
	public class ExcelFilter extends FileFilter {

		public final static String excel = "xls";
		// Accept all directories and all gif, jpg, tiff, or png files.
		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}
			String extension = getExtension(f);
			if (extension != null) {
				if (extension.equals(excel)) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}
		// The description of this filter
		@Override
		public String getDescription() {
			return "Just Images";
		}
		public String getExtension(File f) {
			String ext = null;
			String s = f.getName();
			int i = s.lastIndexOf('.');
			if (i > 0 && i < s.length() - 1) {
				ext = s.substring(i + 1).toLowerCase();
			}
			return ext;
		}
	}
	public class Utils {
		/*
		 * Get the extension of a file.
		 */
	}
	
}