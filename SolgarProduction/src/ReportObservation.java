package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import main.ConnectToDb;
import cb.esi.esiclient.util.ESIBag;

public class ReportObservation extends JFrame implements ActionListener{
	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 900;
	
	private JLabel lblCompanyName,lblChainName,lblReportTypes,lblEmpty,lblEmpty1,lblEmpty2,lblEmpty3;
	private JPanel paramPanel;
	private JScrollPane jScroll;
	private JTable resultTable;
	private JComboBox cmbBoxCompanies,cmbBoxDates,cmbBoxReportTypes;
	public JButton butExceltoScreen,btnExit;
	
	DefaultTableModel dtm = new DefaultTableModel(0, 0);
	String header[] = new String[] { "Row Number","Column1","Column2"};
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ReportObservation window = new ReportObservation();
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
	public ReportObservation() {
		super("Report Observation");
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
		lblCompanyName = new JLabel("Sales Dates For Each Chain");		
		lblChainName = new JLabel("Chain Names");
		lblReportTypes = new JLabel("Select Report Which You Need");
		lblEmpty = new JLabel("");
		lblEmpty1 = new JLabel("");
		lblEmpty2= new JLabel("");
		lblEmpty3 = new JLabel("");
		
		
		//lblEmpty = new JLabel("");
		cmbBoxCompanies = new JComboBox( new String[]{});		
		ConnectToDb.getPRMData("group_company", "solgar_prm.prm_russia_chains",cmbBoxCompanies);				
		cmbBoxCompanies.setMaximumRowCount(50);
		cmbBoxCompanies.setEditable(true);
		
		cmbBoxDates = new JComboBox( new String[]{});		
		ConnectToDb.getPRMData("report_date", "solgar_prm.prm_report_dates",cmbBoxDates);				
		cmbBoxDates.setMaximumRowCount(50);
		cmbBoxDates.setEditable(true);
		
		cmbBoxReportTypes = new JComboBox( new String[]{});		
		ConnectToDb.getPRMData("report_name", "solgar_prm.prm_report_types",cmbBoxReportTypes);
		cmbBoxDates.setMaximumRowCount(50);
		cmbBoxDates.setEditable(true);
		
		// Actions		
		butExceltoScreen = new JButton("Generate Report");
		btnExit = new JButton("Exit");

		//Listener
		butExceltoScreen.addActionListener(this);
		btnExit.addActionListener(this);	
		cmbBoxCompanies.addActionListener(this);
		butExceltoScreen.setEnabled(true);

	
		
		//Add parameters to Screen
		paramPanel.add(lblCompanyName);
		paramPanel.add(cmbBoxDates);
		//paramPanel.add(lblEmpty);
		paramPanel.add(btnExit);
		paramPanel.add(lblChainName);
		paramPanel.add(cmbBoxCompanies);
		//paramPanel.add(lblEmpty1);
		paramPanel.add(butExceltoScreen);
		paramPanel.add(lblReportTypes);
		paramPanel.add(cmbBoxReportTypes);
		
		//Last Changes
		paramPanel.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanel, BorderLayout.NORTH);
		
		

		// Put the final touches to the JFrame object
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
				String selectedReport = cmbBoxReportTypes.getSelectedItem().toString();
				
				if(selectedReport.equalsIgnoreCase("GENERAL SALES REPORT")){					
					String header[] = new String[] { "Chain Name","Sales Count","Sales Date"};
					dtm.setColumnIdentifiers(header);
					resultTable.setModel(dtm);
					
					String selectedCompany = cmbBoxCompanies.getSelectedItem().toString();
					String selectedDate = cmbBoxDates.getSelectedItem().toString();

					ESIBag outBag =ConnectToDb.repGetTotalCounts(selectedCompany,selectedDate,selectedReport);
									
					for (int i = 0; i < outBag.getSize("TABLE"); i++){
						dtm.addRow(new Object[] {
							outBag.get("TABLE",i,"MAINGROUP"),
				        	outBag.get("TABLE",i,"SALESCOUNT"),
				        	outBag.get("TABLE",i,"SALESDATE"), 
							});
					}
					
				}else if(selectedReport.equalsIgnoreCase("CONTROL REPORT DELIVERATION")){					
					String header[] = new String[] { "Chain Name","Sales Count","Sales Date"};
					dtm.setColumnIdentifiers(header);
					resultTable.setModel(dtm);
					
					String selectedCompany = cmbBoxCompanies.getSelectedItem().toString();
					String selectedDate = cmbBoxDates.getSelectedItem().toString();

					ESIBag outBag =ConnectToDb.repGetDeliverationStatus(selectedCompany,selectedDate,selectedReport);
									
					for (int i = 0; i < outBag.getSize("TABLE"); i++){
						dtm.addRow(new Object[] {
							outBag.get("TABLE",i,"MAINGROUP"),
				        	outBag.get("TABLE",i,"SALESCOUNT"),
				        	outBag.get("TABLE",i,"SALESDATE"), 
							});
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