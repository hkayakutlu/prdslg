package main;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import jxl.*;

import javax.swing.filechooser.*;
import cb.esi.esiclient.util.ESIBag;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;


public class ExcelReader extends JFrame implements ActionListener {

	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 900;
    private static final String LINE_BREAK = "\r"; 
    private static final String CELL_BREAK = "\t";

	private JTextArea textArea;
	private JTextField fileName;
	private JScrollPane jScroll;
	private final JFileChooser fc = new JFileChooser();
	private File file;
	private JLabel lblFileName,lblSheetName,lblEmpty;
	private JPanel paramPanel, pnlInsertInst;
	public JButton butClear, butOpen,butExceltoScreen,btnSave,btnExit;
	private ExcelActionHandler excelActionHandler;
	private JTable resultTable;
	//private File paymentPlansDirectory;

	//
	public ExcelReader() {
		// This builds the JFrame portion of the object
		super("Waiting...");
		Toolkit toolkit;
		Dimension dim;
		int screenHeight, screenWidth;
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
		//textArea = new JTextArea(200, 150);
		resultTable = new JTable(20, 8);
		jScroll.setViewportView(resultTable);
		jScroll.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(jScroll, BorderLayout.CENTER);
		
		// add parameter panel
		paramPanel = new JPanel(new GridLayout(0, 3, 5, 5));
		
		//labels
		lblFileName = new JLabel("File name please fill up with only xls type Excel");
		lblSheetName = new JLabel("");
		lblEmpty = new JLabel("");
		
		//text fields
		fileName = new JTextField(20);		

		pnlInsertInst = new JPanel(new GridLayout(0, 6, 5, 5));
		
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
		
		btnSave.setEnabled(false);
		butOpen.setEnabled(true);	
		butClear.setEnabled(false);
		butExceltoScreen.setEnabled(false);
		
		
		// filename
		paramPanel.add(lblFileName);
		paramPanel.add(fileName);
		paramPanel.add(butOpen);		
		paramPanel.add(lblSheetName);
		
		// buttons
		paramPanel.add(butClear);
		paramPanel.add(butExceltoScreen);
		paramPanel.add(lblEmpty);
		paramPanel.add(btnSave);
		paramPanel.add(btnExit);
		
		//paramPanel.add(resultTable);

		paramPanel.add(new JLabel(""));
		paramPanel.add(pnlInsertInst);	

		//
		paramPanel.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanel, BorderLayout.NORTH);
		
		
		fc.addChoosableFileFilter(new ExcelFilter());

		// Put the final touches to the JFrame object
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		validate();
		setVisible(true);
	}
	
	class ExcelActionHandler implements ActionListener, ClipboardOwner {

		Workbook workbook;
		Sheet sheet;
		ExcelReader parent;
		int sheetCount = 0;
		int currentSheet = 0;
		
		public ExcelActionHandler(ExcelReader parent){
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
				ex.printStackTrace();
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

					//setSheetName(sheet);
					
					//parent.butOpen.setEnabled(false);
					parent.butExceltoScreen.setEnabled(true);
					//parent.butNextSheet.setEnabled(true);
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
		
		public void runExcelSetDataToScreen() {
			
			try {
				ESIBag outBag = new ESIBag();
				StringBuffer sb = new StringBuffer("");
				String fileName = file.getName();
				fileName = fileName.toUpperCase();
				//textArea.setText("");
				resultTable.setAutoResizeMode(1);
				int verticalLimit = sheet.getRows();
				int horizontalLimit =sheet.getColumns() ;
				
				/*if(fileName.indexOf("AVE")>=0){ //dikey
					outBag = Companies.aveParser(sheet,sb, verticalLimit, horizontalLimit);					
				}else if (fileName.indexOf("RADUGA")>=0){//Yatay
					Companies.radugaParser(sheet,sb, verticalLimit, horizontalLimit);		
				}else if (fileName.indexOf("ALOE")>=0){//dikey icice
					Companies.aloeParser(sheet,sb, verticalLimit, horizontalLimit);		
				}else if (fileName.indexOf("URAZMANOV")>=0){//Yatay
					Companies.urazmanovParser(sheet,sb, verticalLimit, horizontalLimit);		
				}else if (fileName.indexOf("A5_")>=0){//Dikey
					Companies.a5Parser(sheet,sb, verticalLimit, horizontalLimit);		
				}else if (fileName.indexOf("RIGLA")>=0){//Dikey
					Companies.riglaParser(sheet,sb, verticalLimit, horizontalLimit);		
				}else if (fileName.indexOf("UNIVERSITETSKIE")>=0){//Dikey
					Companies.universtitetskieParser(sheet,sb, verticalLimit, horizontalLimit);		
				}else if (fileName.indexOf("PLANET_ZDOROVYIA")>=0){////dikey icice eczane
					Companies.planetZdoroviya(sheet,sb, verticalLimit, horizontalLimit);		
				}*/
				
				// add header of the table
				DefaultTableModel dtm = new DefaultTableModel(0, 0);
				String header[] = new String[] { "Row Number","Main Group","Sub Group","Pharmacy Address", "Product Name","Aptek No", "Count","Amount"};
				// add header in table model     
				dtm.setColumnIdentifiers(header);
				  //set model into the table object
				resultTable.setModel(dtm);
				
				// add row dynamically into the table      
				for (int i = 0; i < outBag.getSize("TABLE"); i++){
				        dtm.addRow(new Object[] 
				        		{ i+1,
				        		outBag.get("TABLE",i,"MAINGROUP"),
				        		outBag.get("TABLE",i,"SUBGROUP"),
				        		outBag.get("TABLE",i,"PHARMACY"), 
				        		outBag.get("TABLE",i,"PRODUCT"), 
				        		outBag.get("TABLE",i,"APTEKNO"),
				        		outBag.get("TABLE",i,"COUNT"),
				        		outBag.get("TABLE",i,"AMOUNT")
				        		});
				 }
				
				
				tableCopyToClipboard(true);
				parent.butClear.setEnabled(true);
				parent.butExceltoScreen.setEnabled(false);
				parent.btnSave.setEnabled(true);
				
			} catch (Exception ex) {
				
				String message = ex.getMessage();
				ex.printStackTrace();
				
				textArea.setText(message + "\n\n" + ex.getStackTrace());
			}
		}

	private void tableCopyToClipboard (boolean isCut) { 
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
         int numCols=resultTable.getSelectedColumnCount(); 
         int numRows=resultTable.getSelectedRowCount(); 
         int[] rowsSelected=resultTable.getSelectedRows(); 
         int[] colsSelected=resultTable.getSelectedColumns(); 

         StringBuffer excelStr=new StringBuffer(); 
         for (int i=0; i<numRows; i++) { 
                 for (int j=0; j<numCols; j++) { 
                         excelStr.append(escape(resultTable.getValueAt(rowsSelected[i], colsSelected[j]))); 
                         if (isCut) { 
                        	 resultTable.setValueAt(null, rowsSelected[i], colsSelected[j]); 
                         } 
                         if (j<numCols-1) { 
                                 excelStr.append(CELL_BREAK); 
                         } 
                 } 
                 excelStr.append(LINE_BREAK); 
         } 

         StringSelection sel  = new StringSelection(excelStr.toString()); 
         clipboard.setContents(sel, sel); 
     }

	private String escape(Object cell) { 
        return cell.toString().replace(LINE_BREAK, " ").replace(CELL_BREAK, " "); 
	} 
		
		public void runExcelConversion() {}

		
		public void save() {
			try {

				int row = resultTable.getRowCount();
				int column = resultTable.getColumnCount();
				for (int j = 0; j  < row; j++) {
				    for (int i = 0; i  < column; i++) {
				        System.out.println(resultTable.getValueAt(j, i));
				    }
				}				
				
				
			} catch (Exception ex) {
				String message = ex.getMessage();
				ex.printStackTrace();
			}
		}
		
	
		public void clearAll() throws Exception {
			try {
				parent.setTitle("Waiting...");
				
				workbook.close();
				
				// clear text area
				textArea.setText("");
				fileName.setText("");
				lblSheetName.setText("");
				
				parent.butOpen.setEnabled(true);
				//parent.butRun.setEnabled(false);
				parent.butExceltoScreen.setEnabled(false);
				parent.butClear.setEnabled(false);
				//parent.butNextSheet.setEnabled(false);
				
				
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
			ExcelReader xlReader = new ExcelReader();
		} catch (Exception ex) {
			ex.printStackTrace();
			exit();
		}
	}
	// A common point of exit
	public static void exit() {
		
		System.out.println("\nThank you for using ExcelReaders");
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