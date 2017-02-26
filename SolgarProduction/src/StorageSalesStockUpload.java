package src;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jxl.*;

import javax.swing.filechooser.*;

import com.toedter.calendar.JDateChooser;

import main.Companies;
import main.ConnectToDb;
import main.Dispatcher;
import main.ReportQueries;
import main.SendMail;
import main.Storages;
import main.StoragesInfo;
import util.Util;
import cb.esi.esiclient.util.ESIBag;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;


public class StorageSalesStockUpload extends JFrame implements ActionListener {

	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 900;
    private static final String LINE_BREAK = "\r"; 
    private static final String CELL_BREAK = "\t";

	private JTextArea textArea;
	private JTextField fileName,txtTotalAmountSolgar,txtTotalCountSolgar,txtTotalAmountBounty,txtTotalCountBounty,txtProductType;
	private JScrollPane jScroll;
	private final JFileChooser fc = new JFileChooser();
	private File file;
	private JLabel lblFileName,lblTotalAmountSolgar,lblTotalCountSolgar,lblTotalAmountBounty,lblTotalCountBounty,lblProductType,
	lblSheetName,lblFileDate,lblChainName,lblEmpty1,lblEmpty2,lblEmpty3,lblEmpty4,lblEmpty5,lblEmpty6,lblEmpty7,lblEmpty8;
	private JPanel pnlInsertInst,pnlErrorMsg,
	paramPanelMain,paramPanelExcel,paramPanelPar,paramPanelBtn,paramPanelObs,paramPanelResult;
	public JButton butClear, butOpen,butExceltoScreen,btnSave,btnExit,butNextSheet;
	private ExcelActionHandler excelActionHandler;
	private JTable resultTable;
	private JComboBox cmbBoxCompanies;
	private JDateChooser beginDate,endDate;
	
	DefaultTableModel dtm = new DefaultTableModel(0, 0);
	String header[] = new String[] { "Row Number","Distrubutor","Operation Type","City", "Product Name","Product Type", "Count","Amount","Begin Date","End Date"};
	SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
	private String userName="Hakan KAYAKUTLU";
	private String userBrand="ALL";
	private String userCountry="ALL";
	private String userArea="ALL";

	//
	public StorageSalesStockUpload(ESIBag inBag) throws SQLException{
		// This builds the JFrame portion of the object
		super("Storage Sales And Stock Upload");
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

		excelActionHandler = new ExcelActionHandler(this);
		
		resultTable = new JTable(20, 8);		
		dtm.setColumnIdentifiers(header);
		resultTable.setModel(dtm);
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		// scroll pane
		jScroll = new JScrollPane(resultTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
		jScroll.setViewportView(resultTable);
		jScroll.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(jScroll, BorderLayout.CENTER);


		// add parameter panel
		paramPanelMain = new JPanel(new GridLayout(1, 3, 5, 5));
		
		paramPanelExcel = new JPanel(new GridLayout(4, 2, 5, 5));
		paramPanelPar = new JPanel(new GridLayout(4, 2, 5, 5));
		paramPanelObs = new JPanel(new GridLayout(5, 2, 5, 5));
		paramPanelBtn = new JPanel(new GridLayout(1, 5, 5, 5));
		paramPanelResult = new JPanel(new GridLayout(0, 1, 5, 5));
		
		//labels
		lblFileName = new JLabel("File name please fill up with only xls type Excel");
		lblTotalAmountSolgar = new JLabel("Total Amount Solgar");
		lblTotalCountSolgar = new JLabel("Total Count Solgar");
		lblTotalAmountBounty = new JLabel("Total Amount Bounty");
		lblTotalCountBounty = new JLabel("Total Count Bounty");
		lblProductType = new JLabel("Product Type");
		lblSheetName = new JLabel("");
		lblFileDate = new JLabel("Report Date Between");
		lblChainName = new JLabel("Distrubutor Name");
		lblEmpty1 = new JLabel("Select file");
		lblEmpty2 = new JLabel("");
		lblEmpty3 = new JLabel("");
		lblEmpty4 = new JLabel("");
		lblEmpty5 = new JLabel("");
		lblEmpty6 = new JLabel("");
		lblEmpty7 = new JLabel("");
		lblEmpty8 = new JLabel("");
		
		//lblEmpty = new JLabel("");
		cmbBoxCompanies = new JComboBox( new String[]{});		
		Util.getPRMData("storage", "solgar_prm.prm_storages",cmbBoxCompanies);				
		cmbBoxCompanies.setMaximumRowCount(50);
		cmbBoxCompanies.setEditable(true);
		
		
		//Date Field		
		Calendar cal = Calendar.getInstance();
		beginDate = new JDateChooser();
		beginDate.setDateFormatString("yyyy-MM-dd");		
		beginDate.setDate(cal.getTime());
		
		endDate = new JDateChooser();
		endDate.setDateFormatString("yyyy-MM-dd");		
		endDate.setDate(cal.getTime());	
		
		
		//text fields
		fileName = new JTextField(20);		
		txtTotalAmountSolgar = new JTextField(20);
		txtTotalCountSolgar = new JTextField(20);
		txtTotalAmountBounty = new JTextField(20);
		txtTotalCountBounty = new JTextField(20);
		txtProductType = new JTextField(20);
		
		
		// buttons		
		butClear = new JButton("Clear");
		butOpen = new JButton("Open");	
		butExceltoScreen = new JButton("setDataToScreen");
		
		btnSave = new JButton("Save");
		btnExit = new JButton("Exit");
		butNextSheet = new JButton("Next Sheet");
		
		butClear.addActionListener(excelActionHandler);
		butOpen.addActionListener(excelActionHandler);
		butExceltoScreen.addActionListener(excelActionHandler);
		btnSave.addActionListener(excelActionHandler);	
		btnExit.addActionListener(excelActionHandler);	
		cmbBoxCompanies.addActionListener(excelActionHandler);
		butNextSheet.addActionListener(excelActionHandler);
		
		btnSave.setEnabled(false);
		butOpen.setEnabled(true);	
		butClear.setEnabled(false);
		butExceltoScreen.setEnabled(false);
		butNextSheet.setEnabled(false);
		
		txtProductType.setEnabled(false);
		txtTotalAmountSolgar.setEnabled(false);
		txtTotalCountSolgar.setEnabled(false);
		txtTotalAmountBounty.setEnabled(false);
		txtTotalCountBounty.setEnabled(false);

		paramPanelExcel.add(lblFileName);
		paramPanelExcel.add(fileName);
		paramPanelExcel.add(lblEmpty1);
		paramPanelExcel.add(butOpen);
		paramPanelExcel.add(butNextSheet);
		paramPanelExcel.add(lblSheetName);		
		paramPanelExcel.add(lblEmpty3);	
		paramPanelExcel.add(lblEmpty4);	
		
		paramPanelPar.add(lblFileDate);
		paramPanelPar.add(beginDate);
		paramPanelPar.add(endDate);
		paramPanelPar.add(lblChainName);
		paramPanelPar.add(cmbBoxCompanies);
		paramPanelPar.add(lblEmpty5);	
		paramPanelPar.add(lblEmpty6);	
		paramPanelPar.add(lblEmpty7);	
		paramPanelPar.add(lblEmpty8);	
		
		paramPanelObs.add(lblTotalAmountSolgar);
		paramPanelObs.add(txtTotalAmountSolgar);
		paramPanelObs.add(lblTotalCountSolgar);		
		paramPanelObs.add(txtTotalCountSolgar);
		paramPanelObs.add(lblTotalAmountBounty);
		paramPanelObs.add(txtTotalAmountBounty);
		paramPanelObs.add(lblTotalCountBounty);		
		paramPanelObs.add(txtTotalCountBounty);
		paramPanelObs.add(lblProductType);		
		paramPanelObs.add(txtProductType);
		
		// buttons
		paramPanelBtn.add(butClear);
		paramPanelBtn.add(butExceltoScreen);
		paramPanelBtn.add(btnSave);
		paramPanelBtn.add(btnExit);	
		
		paramPanelMain.add(paramPanelExcel);
		paramPanelMain.add(paramPanelPar);
		paramPanelMain.add(paramPanelObs);
		
		paramPanelMain.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanelMain, BorderLayout.NORTH);		
		
		paramPanelBtn.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanelBtn, BorderLayout.SOUTH);	
		
		paramPanelResult.add(jScroll);			
		//Last Changes
		paramPanelResult.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanelResult, BorderLayout.CENTER);
		
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
		StorageSalesStockUpload parent;
		int sheetCount = 0;
		int currentSheet = 0;
		
		public ExcelActionHandler(StorageSalesStockUpload parent){
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
				if (ae.getActionCommand().equals("Next Sheet")) {
					nextSheet(false);
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
					
					/*if(file.getName().indexOf("A5_")>=0){ 
						currentSheet = 1;
					}else{
						currentSheet = 0;
					}		*/
					currentSheet = 0;
					sheetCount = workbook.getSheets().length;					
					sheet = workbook.getSheet(currentSheet);
					
					setSheetName(sheet);
					
					parent.butOpen.setEnabled(false);
					parent.butNextSheet.setEnabled(true);
					
					
					sheet = workbook.getSheet(currentSheet);
					parent.butExceltoScreen.setEnabled(true);
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
		
		private void setSheetName(Sheet sheet){
			
			String sheetName = (currentSheet + 1) + " / " + sheetCount + " : " + sheet.getName();
			//parent.setTitle(sheetName);
			parent.lblSheetName.setText(sheetName);
			
		}
		public void nextSheet(boolean b) {
			try {
				currentSheet++;
				if(currentSheet == sheetCount) currentSheet = 0;				
				sheet = workbook.getSheet(currentSheet);
				setSheetName(sheet);				
				parent.butOpen.setEnabled(false);
				parent.butNextSheet.setEnabled(true);

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
				String selectedItem = cmbBoxCompanies.getSelectedItem().toString().toUpperCase();
				//2016-01-01
				int totalCount = 0;
				double totalAmount = 0;
				int totalCountSolgar = 0;
				double totalAmountSolgar = 0;
				int totalCountBounty = 0;
				double totalAmountBounty = 0;
				String product ="";
				DecimalFormat df = new DecimalFormat("#.00");
				boolean solgar = false;
				boolean bounty = false;
				String stockSalesType = "STOCK";
				String productType ="";
				String beginDateStr = String.valueOf((dcn.format(beginDate.getDate())));
				String endDateStr = String.valueOf(dcn.format(endDate.getDate()));
				
				if(selectedItem == null || selectedItem.trim().length() == 0){
					JOptionPane.showMessageDialog(pnlErrorMsg, "Please select distrubutor", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(fileName.indexOf(selectedItem)<0){//Show error message
					parent.butClear.setEnabled(true);
					parent.butExceltoScreen.setEnabled(false);
					parent.btnSave.setEnabled(false);
					JOptionPane.showMessageDialog(pnlErrorMsg, "Please select correct file", "Error", JOptionPane.ERROR_MESSAGE);
				}else{						
					if(fileName.indexOf("КАТРЕН")>=0){ 
						if(fileName.indexOf("ПРОДАЖИ")>=0){
							outBag = Storages.katrenSalesParser(sheet,selectedItem, verticalLimit, horizontalLimit);
							stockSalesType ="SALES";
						}else{
							outBag = Storages.katrenStockParser(sheet,selectedItem, verticalLimit, horizontalLimit);
							stockSalesType ="STOCK";
						}
					}else if (fileName.indexOf("ПРОТЕК")>=0){
						if(fileName.indexOf("ПРОДАЖИ")>=0){
							outBag = Storages.protekSalesParser(sheet,selectedItem, verticalLimit, horizontalLimit);
							stockSalesType ="SALES";
						}else{
							outBag = Storages.protekStockParser(sheet,selectedItem, verticalLimit, horizontalLimit);
							stockSalesType ="STOCK";
						}		
					}else if (fileName.indexOf("ПУЛЬС")>=0){//dikey icice
						if(fileName.indexOf("ПРОДАЖИ")>=0){
							outBag = Storages.pulseSalesParser(sheet,selectedItem, verticalLimit, horizontalLimit);
							stockSalesType ="SALES";
						}else{
							outBag = Storages.pulseStockParser(sheet,selectedItem, verticalLimit, horizontalLimit);
							stockSalesType ="STOCK";
						}	
					}else if (fileName.indexOf("ВЕНТА")>=0){//dikey icice
						if(fileName.indexOf("ПРОДАЖИ")>=0){
							outBag = Storages.ventaSalesParser(sheet,selectedItem, verticalLimit, horizontalLimit);
							stockSalesType ="SALES";
						}else{
							outBag = Storages.ventaStockParser(sheet,selectedItem, verticalLimit, horizontalLimit);
							stockSalesType ="STOCK";
						}	
					}else if (fileName.indexOf("ОПТИМА")>=0){//dikey icice
						if(fileName.indexOf("ПРОДАЖИ")>=0){
							outBag = Storages.optimaSalesParser(sheet,selectedItem, verticalLimit, horizontalLimit);
							stockSalesType ="SALES";
						}else{
							outBag = Storages.optimaStockParser(sheet,selectedItem, verticalLimit, horizontalLimit);
							stockSalesType ="STOCK";
						}	
					}else if (fileName.indexOf("БАДМ")>=0){//dikey icice
						if(fileName.indexOf("ПРОДАЖИ")>=0){
							outBag = Storages.badmSalesParser(sheet,selectedItem, verticalLimit, horizontalLimit);
							stockSalesType ="SALES";
						}else{
							outBag = Storages.badmStockParser(sheet,selectedItem, verticalLimit, horizontalLimit);
							stockSalesType ="STOCK";
						}	
					}
					
					int i = 0;
					// add row dynamically into the table     
					try{
					for (i = 0; i < outBag.getSize("TABLE"); i++){
					    String countStr = outBag.get("TABLE",i,"COUNT");   
					    String amountStr = outBag.get("TABLE",i,"AMOUNT");
					    product = outBag.get("TABLE",i,"PRODUCT");
	

					        if(countStr.indexOf(".")>0){
					        	countStr = countStr.substring(0, countStr.indexOf("."));
							}
						    if(countStr.indexOf(",")>0){
						    	countStr = countStr.substring(0, countStr.indexOf(","));
							}
						    countStr =  countStr.replaceAll("^\\s+","");
						    countStr = countStr.replaceAll("\\s+$","");
						    int intCount =  Integer.parseInt(countStr);					    
						    
						    if(amountStr != null && amountStr.trim().length()>0){
							    amountStr = amountStr.replaceAll("\\s+", "");	
							    amountStr = amountStr.replaceAll(" ", "");
							    amountStr = amountStr.trim();
							    if(amountStr.equalsIgnoreCase(",00")){
							    	amountStr = "0.00";
								}
							    if(amountStr.indexOf(",")>=0){
							    	amountStr = amountStr.replaceAll(",", ".");
								}
							    totalAmount = Double.parseDouble(amountStr);
						    }else{
						    	totalAmount = Double.parseDouble("0.00");
						    }
						    
						    if(product.toUpperCase().indexOf("БАУНТИ")>=0 || product.toUpperCase().indexOf("BOUNTY")>=0){
						    	productType = "BN";
						    	totalAmountBounty =totalAmountBounty + totalAmount;
						    	totalCountBounty = totalCountBounty + intCount;
								bounty = true;
							}else{
								productType = "SL";
								totalAmountSolgar =totalAmountSolgar + totalAmount;		
								totalCountSolgar = totalCountSolgar + intCount;
								solgar = true;
							}
						    
						    dtm.addRow(new Object[] 
					        		{ i+1,
					        		outBag.get("TABLE",i,"MAINGROUP"),
					        		stockSalesType,
					        		outBag.get("TABLE",i,"CITY"), 
					        		product, 
					        		productType,
					        		countStr.trim(),
					        		amountStr.trim(),
					        		beginDateStr,
					        		endDateStr
					        		});
						    
							        
					 }		
					}catch(Exception ex) {
						String message = ex.getMessage();
						//ex.printStackTrace();
						JOptionPane.showMessageDialog(pnlErrorMsg, message +" Excel format incorrect please check row: " +String.valueOf(i) , "Error Message", JOptionPane.ERROR_MESSAGE);
						throw ex;
					}
				
					if(bounty){
						txtTotalAmountBounty.setText(String.valueOf(df.format(totalAmountBounty)));
						txtTotalCountBounty.setText(String.valueOf(totalCountBounty));
						txtProductType.setText("NATURES BOUNTY");
					}
					if(solgar){
						txtTotalAmountSolgar.setText(String.valueOf(df.format(totalAmountSolgar)));
						txtTotalCountSolgar.setText(String.valueOf(totalCountSolgar));
						txtProductType.setText("SOLGAR");
					}	
					if(bounty&&solgar){
						txtProductType.setText("SOLGAR & NATURES BOUNTY");
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
				String beginDateStr = String.valueOf((dcn.format(beginDate.getDate())));
				String endDateStr = String.valueOf(dcn.format(endDate.getDate()));					
				String distributor = resultTable.getValueAt(1, 1).toString();
				String opType = resultTable.getValueAt(1, 2).toString();				
				
				if(Dispatcher.controlLoadStocks(distributor, opType,beginDateStr,endDateStr)){
					JOptionPane.showMessageDialog(pnlErrorMsg, "File already load please check", "Error", JOptionPane.ERROR_MESSAGE);
				}else{			
					//ConnectToDb.setSaleDataToDB(resultTable);
					Dispatcher.saveStockInfo(resultTable);
					
					if(opType.equalsIgnoreCase("STOCK")){
						String emailText = "Коллеги, добрый день!,\n\n"+
						"Solgar Стоки "+distributor +" на "+txtTotalCountSolgar.getText()+" "+beginDateStr+"\n\n"
						+"Natures Bounty Стоки "+distributor+" на "+txtTotalCountBounty.getText()+" "+beginDateStr+
						"\n\nПросьба ознакомиться.\n\nПо дополнительным вопросам, просьба , писать на ysmolina@solgarvitamin.ru";
						SendMail.sendEmailToReceipents("herturk@solgarvitamin.ru","hgokmen@solgarvitamin.ru","vsolodkova@solgarvitamin.ru","vmatushkina@solgarvitamin.ru", "Информация по стокам обновлена", emailText);
						Thread.sleep(100);
						ESIBag mailBag = new ESIBag();
						mailBag.put("TABLE",0,"EMAIL", "dzaytseva@solgarvitamin.ru");mailBag.put("TABLE",1,"EMAIL", "ssharipova@solgarvitamin.ru");mailBag.put("TABLE",2,"EMAIL", "achernyavskaya@solgarvitamin.ru");
						mailBag.put("TABLE",3,"EMAIL", "ezhukova@solgarvitamin.ru");mailBag.put("TABLE",4,"EMAIL", "mglushko@naturesbounty.ru");mailBag.put("TABLE",5,"EMAIL", "egololobova@naturesbounty.ru");
						mailBag.put("TABLE",6,"EMAIL", "apotapova@naturesbounty.ru");mailBag.put("TABLE",7,"EMAIL", "hakan.kayakutlu@gmail.com");
						SendMail.sendEmailToReceipentsMultiply("Информация по стокам обновлена", emailText,mailBag);
				
					}else{
						String emailText = "Коллеги, добрый день!,\n\n"+
						"Solgar Продажи "+distributor +" на "+txtTotalCountSolgar.getText()+" период "+beginDateStr+" & "+endDateStr+"\n\n"
						+"Natures Bounty Продажи "+distributor+" на "+txtTotalCountBounty.getText()+" период "+beginDateStr+" & "+endDateStr+
						"\n\nПросьба ознакомиться.\n\nПо дополнительным вопросам, просьба , писать на ysmolina@solgarvitamin.ru";
						SendMail.sendEmailToReceipents("herturk@solgarvitamin.ru","hgokmen@solgarvitamin.ru","hakan.kayakutlu@gmail.com","", "Информация по Продажи обновлена", emailText);
					}

					for( int i = dtm.getRowCount() - 1; i >= 0; i-- ) {
						dtm.removeRow(i);
					}
					txtTotalAmountSolgar.setText("");
					txtTotalCountSolgar.setText("");
					txtTotalAmountBounty.setText("");
					txtTotalCountBounty.setText("");
					
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
				//resultTable.setModel(new DefaultTableModel());
				for( int i = dtm.getRowCount() - 1; i >= 0; i-- ) {
					dtm.removeRow(i);
				}
				
				parent.butOpen.setEnabled(true);
				parent.btnSave.setEnabled(false);
				parent.butExceltoScreen.setEnabled(false);
				parent.butClear.setEnabled(false);
				parent.butNextSheet.setEnabled(false);
				
				txtTotalAmountSolgar.setText("");
				txtTotalCountSolgar.setText("");	
				txtTotalAmountBounty.setText("");
				txtTotalCountBounty.setText("");		
				
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
			ESIBag inBag = new ESIBag();
			StorageSalesStockUpload xlReader = new StorageSalesStockUpload(inBag);
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