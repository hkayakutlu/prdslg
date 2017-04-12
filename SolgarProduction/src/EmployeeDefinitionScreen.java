package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import cb.esi.esiclient.util.ESIBag;

import com.toedter.calendar.JDateChooser;

import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.TabExpander;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;

import util.Util;
import main.ConnectToDb;
import main.Dispatcher;

public class EmployeeDefinitionScreen extends JFrame implements ActionListener,MouseListener{

	private String userName="Hakan KAYAKUTLU";
	
	private JPanel contentPane,pnlValues,pnlResult,pnlButton,pnlInfoMsg;
	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 750;
	private static final Calendar cal = Calendar.getInstance();
	public SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
	private JTextField txtTabelNomer,txtName,txtEmail,txtPassword,txtAdditionalStatus;
	private JComboBox cmbBoxUnitName,cmbBoxCompany,cmbBoxTitle,cmbBoxStatus;
	private JButton btnAdd,btnUpdate,btnDelete,btnSave,btnCleanUp,btnExit;
	private JDateChooser dateStart,dateEnd,dateBirthDate;
	private JTable table;
	private JLabel lblBirthDate;
	
	final DefaultTableModel modelResult = new DefaultTableModel();
	String headerResult[] = new String[] {"Id", "Company", "Tabel Nomer", "Unit Name", "Title", "Name", "Activeness", "Start Date", 
			"End Date", "Email", "Password", "Additional Status", "Birth Date", "Entry Date", "Entry User"};
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ESIBag inBag = new ESIBag();
					EmployeeDefinitionScreen frame = new EmployeeDefinitionScreen(inBag);
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
	public EmployeeDefinitionScreen(ESIBag inBag) throws SQLException {
		super("Employee Definition&Update");
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
			}
			
		} catch (Exception e) {
			// simdilik yoksa yok
		}
		
		pnlValues = new JPanel();
		pnlValues.setBorder(new TitledBorder(null, "Inputs", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		pnlValues.setBounds(10, 6, 1064, 173);
		getContentPane().add(pnlValues);
		pnlValues.setLayout(null);
		
		JLabel lblTabelNomer = new JLabel("Tabel Nomer");
		lblTabelNomer.setBounds(10, 59, 83, 20);
		pnlValues.add(lblTabelNomer);
		
		JLabel lblCompany = new JLabel("Company Name");
		lblCompany.setBounds(10, 34, 93, 20);
		pnlValues.add(lblCompany);
		
		JLabel lblUnit = new JLabel("Unit Name");
		lblUnit.setBounds(10, 84, 83, 20);
		pnlValues.add(lblUnit);
		
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setBounds(10, 120, 83, 17);
		pnlValues.add(lblTitle);
		
		JLabel lblName = new JLabel("Name(FIO)");
		lblName.setBounds(276, 34, 93, 14);
		pnlValues.add(lblName);
		
		JLabel lblStartDate = new JLabel("Start Date");
		lblStartDate.setBounds(276, 59, 83, 14);
		pnlValues.add(lblStartDate);
		
		JLabel lblEndDate = new JLabel("End Date");
		lblEndDate.setBounds(276, 84, 46, 14);
		pnlValues.add(lblEndDate);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(276, 109, 46, 14);
		pnlValues.add(lblEmail);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(550, 34, 71, 14);
		pnlValues.add(lblPassword);
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(550, 59, 46, 14);
		pnlValues.add(lblStatus);
		
		JLabel lblAdditionalStatus = new JLabel("Additional Status");
		lblAdditionalStatus.setBounds(550, 84, 93, 14);
		pnlValues.add(lblAdditionalStatus);
		
		cmbBoxCompany = new JComboBox();
		cmbBoxCompany.addItem("");
		cmbBoxCompany.addItem("SOLGAR");
		cmbBoxCompany.addItem("NATURES BOUNTY");
		cmbBoxCompany.setBounds(100, 31, 120, 20);
		pnlValues.add(cmbBoxCompany);
		
		txtTabelNomer = new JTextField();
		txtTabelNomer.setBounds(100, 56, 120, 20);
		pnlValues.add(txtTabelNomer);
		txtTabelNomer.setColumns(10);
		
		cmbBoxUnitName = new WideComboBox();
		Util.getPRMDataGroupBy("unit", "solgar_prm.prm_org_units",cmbBoxUnitName,"","");
		cmbBoxUnitName.addItem("");
		cmbBoxUnitName.setSelectedIndex(-1);
		cmbBoxUnitName.setBounds(100, 84, 120, 20);
		pnlValues.add(cmbBoxUnitName);
		
		cmbBoxTitle = new WideComboBox();
		Util.getPRMDataGroupBy("title", "solgar_prm.prm_org_titles",cmbBoxTitle,"","");
		cmbBoxTitle.addItem("");
		cmbBoxTitle.setSelectedIndex(-1);
		cmbBoxTitle.setBounds(100, 117, 120, 20);
		pnlValues.add(cmbBoxTitle);
		
		txtName = new JTextField();
		txtName.setBounds(347, 34, 193, 20);
		pnlValues.add(txtName);
		txtName.setColumns(10);
		
		txtEmail = new JTextField();
		txtEmail.setBounds(347, 106, 193, 20);
		pnlValues.add(txtEmail);
		txtEmail.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setText("123456");
		txtPassword.setBounds(641, 34, 108, 20);
		pnlValues.add(txtPassword);
		txtPassword.setColumns(10);
		
		cmbBoxStatus = new JComboBox();
		cmbBoxStatus.addItem("ACTIVE");//1
		cmbBoxStatus.addItem("LEAVED");//2
		cmbBoxStatus.addItem("дейпер");//3
		cmbBoxStatus.setBounds(641, 59, 108, 20);
		pnlValues.add(cmbBoxStatus);
		
		txtAdditionalStatus = new JTextField();
		txtAdditionalStatus.setBounds(641, 84, 108, 20);
		pnlValues.add(txtAdditionalStatus);
		txtAdditionalStatus.setColumns(10);
		
		dateStart = new JDateChooser();
		dateStart.setDateFormatString("yyyy-MM-dd");
		dateStart.setBounds(347, 59, 108, 20);
		pnlValues.add(dateStart);
		
		dateEnd = new JDateChooser();
		dateEnd.setDateFormatString("yyyy-MM-dd");
		dateEnd.setBounds(347, 84, 108, 20);
		pnlValues.add(dateEnd);
		
		lblBirthDate = new JLabel("Birth Date");
		lblBirthDate.setBounds(550, 109, 83, 14);
		pnlValues.add(lblBirthDate);
		
		dateBirthDate = new JDateChooser();
		dateBirthDate.setDateFormatString("yyyy-MM-dd");
		dateBirthDate.setBounds(641, 109, 108, 20);
		pnlValues.add(dateBirthDate);
		
		pnlResult = new JPanel();
		pnlResult.setBorder(new TitledBorder(null, "Result", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		pnlResult.setBounds(10, 190, 1064, 348);
		getContentPane().add(pnlResult);
		pnlResult.setLayout(null);
		
		JScrollPane scrollResult = new JScrollPane();
		scrollResult.setBounds(10, 22, 1044, 274);
		pnlResult.add(scrollResult);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
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
				"Id", "Company", "Tabel Nomer", "Unit Name", "Title", "Name", "Activeness", "Start Date", "End Date", "Email", "Password", "Additional Status", "Birth Date", "Entry Date", "Entry User"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class, String.class, String.class, Object.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, Object.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, true, true, true, true, true, true, true, true, true, true, true, true, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(35);
		scrollResult.setViewportView(table);
		
		pnlButton = new JPanel();
		pnlButton.setLayout(null);
		pnlButton.setBorder(new TitledBorder(null, "Button Group", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		pnlButton.setBounds(10, 560, 1064, 62);
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
		
		/*Listeners*/
		btnAdd.addActionListener(this);	
		btnDelete.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnSave.addActionListener(this);
		btnExit.addActionListener(this);
		btnCleanUp.addActionListener(this);
		
		table.addMouseListener(this);
		
		getStaffInfo();
		
		contentPane.setLayout(null);
		
		validate();
		setVisible(true);
	}
	
	private void getStaffInfo() {
	    try{
	    	ESIBag tempBag =null;
			tempBag = Dispatcher.getEmployee(tempBag);
			modelResult.setColumnIdentifiers(headerResult);
			table.setModel(modelResult);
	    	
			for (int j = 0; j < tempBag.getSize("TABEL"); j++){
				modelResult.addRow(new Object [] 
		        		{
		        			tempBag.get("TABEL",j,"ID"),
		        			tempBag.get("TABEL",j,"COMPANY"),
		        			tempBag.get("TABEL",j,"TABELNOMER"),
		        			tempBag.get("TABEL",j,"UNIT"),
		        			tempBag.get("TABEL",j,"TITLE"),
		        			tempBag.get("TABEL",j,"NAME"),
		        			tempBag.get("TABEL",j,"ACTIVENESS"),
		        			tempBag.get("TABEL",j,"STARTDATE"),
		        			tempBag.get("TABEL",j,"ENDDATE"),
		        			tempBag.get("TABEL",j,"EMAIL"),
		        			tempBag.get("TABEL",j,"PASSWORD"),
		        			tempBag.get("TABEL",j,"ADDITIONALSTATUS"),
		        			tempBag.get("TABEL",j,"BIRTHDATE"),
		        			tempBag.get("TABEL",j,"ENTRYDATE"),
		        			tempBag.get("TABEL",j,"ENTRYUSER")
		        		});		
			}
			
	    }catch (Exception e) {
			// simdilik yoksa yok
		}
}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals("ADD")) {
				
				modelResult.setColumnIdentifiers(headerResult);
				table.setModel(modelResult);						
				final Object[] row = new Object[13];
				row[0] = "0";
				row[1] = cmbBoxCompany.getSelectedItem().toString() != null ? cmbBoxCompany.getSelectedItem().toString().toString() : "";
				row[2] = txtTabelNomer.getText().toString() != null ? txtTabelNomer.getText().toString().toString() : "";
				row[3] = cmbBoxUnitName.getSelectedItem().toString() != null ? cmbBoxUnitName.getSelectedItem().toString().toString() : "";
				row[4] = cmbBoxTitle.getSelectedItem().toString() != null ? cmbBoxTitle.getSelectedItem().toString().toString() : "";
				row[5] = txtName.getText().toString() != null ? txtName.getText().toString().toString() : "";
				row[6] = cmbBoxStatus.getSelectedItem().toString() != null ? cmbBoxStatus.getSelectedItem().toString().toString() : "";
				row[7] = dateStart.getDate() != null ? dcn.format(dateStart.getDate()) : "";
				row[8] = dateEnd.getDate() != null ? dcn.format(dateEnd.getDate()) : "";
				row[9] = txtEmail.getText().toString() != null ? txtEmail.getText().toString().toString() : "";
				row[10] = txtPassword.getText().toString() != null ? txtPassword.getText().toString().toString() : "";
				row[11] = txtAdditionalStatus.getText().toString() != null ? txtAdditionalStatus.getText().toString().toString() : "";
				row[12] = dateBirthDate.getDate() != null ? dcn.format(dateBirthDate.getDate()) : "";
	
				modelResult.insertRow(0, row);
				btnSave.setEnabled(true);
				
			}else if (e.getActionCommand().equals("UPDATE")) {
				int i = table.getSelectedRow();
				if (i >= 0){
					modelResult.setValueAt("0", i, 0);
					modelResult.setValueAt(cmbBoxCompany.getSelectedItem().toString() != null ? cmbBoxCompany.getSelectedItem().toString().toString() : "", i, 1);
					modelResult.setValueAt(txtTabelNomer.getText().toString() != null ? txtTabelNomer.getText().toString().toString() : "", i, 2);
					modelResult.setValueAt(cmbBoxUnitName.getSelectedItem().toString() != null ? cmbBoxUnitName.getSelectedItem().toString().toString() : "", i, 3);
					modelResult.setValueAt(cmbBoxTitle.getSelectedItem().toString() != null ? cmbBoxTitle.getSelectedItem().toString().toString() : "", i, 4);
					modelResult.setValueAt(txtName.getText().toString() != null ? txtName.getText().toString().toString() : "", i, 5);
					modelResult.setValueAt(cmbBoxStatus.getSelectedItem().toString() != null ? cmbBoxStatus.getSelectedItem().toString().toString() : "", i, 6);		
					modelResult.setValueAt(dateStart.getDate() != null ? dcn.format(dateStart.getDate()) : "", i, 7);						
					modelResult.setValueAt(dateEnd.getDate() != null ? dcn.format(dateEnd.getDate()) : "", i, 8);
					modelResult.setValueAt(txtEmail.getText().toString() != null ? txtEmail.getText().toString().toString() : "", i, 9);
					modelResult.setValueAt(txtPassword.getText().toString() != null ? txtPassword.getText().toString().toString() : "", i, 10);
					modelResult.setValueAt(txtAdditionalStatus.getText().toString() != null ? txtAdditionalStatus.getText().toString().toString() : "", i, 11);
					modelResult.setValueAt(dateBirthDate.getDate() != null ? dcn.format(dateBirthDate.getDate()) : "", i, 12);	
				}
				
			}else if (e.getActionCommand().equals("DELETE")) {
				int i = table.getSelectedRow();
				if (i >= 0) {
					modelResult.removeRow(i);
				}
			}else if (e.getActionCommand().equals("SAVE")) {
				ESIBag outBag = new ESIBag();
				int row = table.getRowCount();
				for (int j = 0; j  < row; j++) {	
					long id =Long.parseLong(table.getValueAt(j, 0).toString());
					outBag.put("USERNAME", userName);
					if(id<=0){
						formatToBagValue(outBag, j, 0, "TABEL", "ID");
						formatToBagValue(outBag, j, 1, "TABEL", "COMPANY");
						formatToBagValue(outBag, j, 2, "TABEL", "TABELNOMER");
						formatToBagValue(outBag, j, 3, "TABEL", "UNIT");
						formatToBagValue(outBag, j, 4, "TABEL", "TITLE");
						formatToBagValue(outBag, j, 5, "TABEL", "NAME");
						formatToBagValue(outBag, j, 6, "TABEL", "ACTIVENESS");
						formatToBagValue(outBag, j, 7, "TABEL", "STARTDATE");
						formatToBagValue(outBag, j, 8, "TABEL", "ENDDATE");
						formatToBagValue(outBag, j, 9, "TABEL", "EMAIL");
						formatToBagValue(outBag, j, 10, "TABEL", "PASSWORD");
						formatToBagValue(outBag, j, 11, "TABEL", "ADDITIONALSTATUS");
						formatToBagValue(outBag, j, 12, "TABEL", "BIRTHDATE");
						outBag.put("TABEL",j,"USER",userName);	
					}
				}
				
				boolean result = Dispatcher.saveEmployee(outBag);
				if(result){
					JOptionPane.showMessageDialog(pnlInfoMsg, "Operation completed succesfully", "Information", JOptionPane.INFORMATION_MESSAGE);
					cleanUp();
					getStaffInfo();
				}
				
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

	private void cleanUp() {		
		cmbBoxCompany.setSelectedItem("");
		txtTabelNomer.setText("");
		cmbBoxUnitName.setSelectedItem("");
		cmbBoxTitle.setSelectedItem("");
		txtName.setText("");		
		cmbBoxStatus.setSelectedItem("");
		dateStart.setDateFormatString("yyyy-MM-dd");		
		dateStart.setDate(cal.getTime());
		dateEnd.setDateFormatString("yyyy-MM-dd");		
		dateEnd.setDate(cal.getTime());
		txtEmail.setText("");
		txtPassword.setText("123456");
		txtAdditionalStatus.setText("");
		dateBirthDate.setDateFormatString("yyyy-MM-dd");		
		dateBirthDate.setDate(cal.getTime());
		
    	for( int j = modelResult.getRowCount() - 1; j >= 0; j-- ) {
    		modelResult.removeRow(j);
		}
		
	}

	private void formatToBagValue(ESIBag outBag,int rowNum,int index,String tableBagkey,String valueBagKey ){			
		if(table.getValueAt(rowNum, index) != null && table.getValueAt(rowNum, index).toString().length()>0){
			outBag.put(tableBagkey,rowNum,valueBagKey, table.getValueAt(rowNum, index).toString());
		}else{
			outBag.put(tableBagkey,rowNum,valueBagKey, "");			
		}
	}
	
	private void dateFormatter(JDateChooser outputDate, String inputStr) throws Exception{			
		if(inputStr != null){outputDate.setDate(dcn.parse(inputStr));
		}else{outputDate.setDateFormatString("");}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		try{
			int i = table.getSelectedRow();				
			cmbBoxCompany.setSelectedItem(modelResult.getValueAt(i, 1).toString() != null ? modelResult.getValueAt(i, 1).toString() : "");
			txtTabelNomer.setText(modelResult.getValueAt(i, 2).toString() != null ? modelResult.getValueAt(i, 2).toString() : "");
			cmbBoxUnitName.setSelectedItem(modelResult.getValueAt(i, 3).toString() != null ? modelResult.getValueAt(i, 3).toString() : "");
			cmbBoxTitle.setSelectedItem(modelResult.getValueAt(i, 4).toString() != null ? modelResult.getValueAt(i, 4).toString() : "");
			txtName.setText(modelResult.getValueAt(i, 5).toString() != null ? modelResult.getValueAt(i, 5).toString() : "");
			cmbBoxStatus.setSelectedItem(modelResult.getValueAt(i, 6).toString() != null ? modelResult.getValueAt(i, 6).toString() : "");
			dateFormatter(dateStart,modelResult.getValueAt(i, 7).toString());
			dateFormatter(dateEnd,modelResult.getValueAt(i, 8).toString());
			txtEmail.setText(modelResult.getValueAt(i, 9).toString() != null ? modelResult.getValueAt(i, 9).toString() : "");
			txtPassword.setText(modelResult.getValueAt(i, 10).toString() != null ? modelResult.getValueAt(i, 10).toString() : "");
			txtAdditionalStatus.setText(modelResult.getValueAt(i, 11).toString() != null ? modelResult.getValueAt(i, 11).toString() : "");
			dateFormatter(dateBirthDate,modelResult.getValueAt(i, 12).toString());
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