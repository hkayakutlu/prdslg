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
import cb.esi.esiclient.util.BagKeyNotFoundException;
import cb.esi.esiclient.util.ESIBag;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;


public class PassChangeScr extends JFrame implements ActionListener {

	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 900;

	private JTextField txtOldPass,txtNewPass;
	private JLabel lblOldPass,lblNewPass;
	public JButton butClear,butExceltoScreen,btnSave,btnExit,butNextSheet;
	private ActionHandler actionHandler;
	private JPanel paramPanelMain,pnlPassPar,paramPanelBtn,pnlErrorMsg;
	private String userName="Hakan KAYAKUTLU";
	
	public PassChangeScr(ESIBag inBag){
		// This builds the JFrame portion of the object
		super("Password Change");
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

		try {
			userName = inBag.get("LOGINNAME").toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		// add parameter panel
		paramPanelMain = new JPanel(new GridLayout(1, 3, 5, 5));		
		pnlPassPar = new JPanel(new GridLayout(4, 2, 5, 5));
		paramPanelBtn = new JPanel(new GridLayout(1, 5, 15, 5));
		
		//labels
		lblOldPass = new JLabel("Old Password");
		lblNewPass = new JLabel("New Password");
		
		
		//text fields
		txtOldPass = new JTextField(20);
		txtNewPass = new JTextField(20);	
		
		// buttons		
		butClear = new JButton("Clear");	
		butExceltoScreen = new JButton("setDataToScreen");		
		btnSave = new JButton("Save");
		btnExit = new JButton("Exit");
		butNextSheet = new JButton("Next Sheet");
		
		butClear.addActionListener(actionHandler);
		butExceltoScreen.addActionListener(actionHandler);
		btnSave.addActionListener(actionHandler);	
		btnExit.addActionListener(actionHandler);	
		butNextSheet.addActionListener(actionHandler);
		
		btnSave.setEnabled(true);
		butClear.setEnabled(false);
		butExceltoScreen.setEnabled(false);
		butNextSheet.setEnabled(false);
		
		txtOldPass.setEnabled(false);

		pnlPassPar.add(lblOldPass);
		pnlPassPar.add(txtOldPass);
		pnlPassPar.add(lblNewPass);
		pnlPassPar.add(txtNewPass);
	
		// buttons
		paramPanelBtn.add(butClear);
		paramPanelBtn.add(butExceltoScreen);
		paramPanelBtn.add(btnSave);
		paramPanelBtn.add(btnExit);	
		
		paramPanelMain.add(pnlPassPar);
		
		paramPanelMain.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanelMain, BorderLayout.NORTH);		
		
		paramPanelBtn.setBorder(new EmptyBorder(10, 15, 5, 15));
		getContentPane().add(paramPanelBtn, BorderLayout.SOUTH);	
		try{
			txtOldPass.setText(ConnectToDb.getPassWithUserName(userName));
		} catch (Exception e) {
			// TODO: handle exception
		}

		// Put the final touches to the JFrame object
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		validate();
		setVisible(true);
	}
	
	class ActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			try {
				if (ae.getActionCommand().equals("Open")) {					
				}
				if (ae.getActionCommand().equals("setDataToScreen")) {					
				}
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
					
				}
				
			} catch (Exception ex) {
				String message = ex.getMessage();
				JOptionPane.showMessageDialog(pnlErrorMsg, message +" Global Problem" , 
						"Error Message", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		public void save() {
			try {
				if(controlPassLength()){
					JOptionPane.showMessageDialog(pnlErrorMsg, "Passsword lenght shold be 8-11", "Error", JOptionPane.ERROR_MESSAGE);
				}else{			
					ConnectToDb.updatePassword(txtNewPass.getText(),userName);
					btnSave.setEnabled(true);
					JOptionPane.showMessageDialog(pnlErrorMsg, "Your password changed successfully", "Info", JOptionPane.INFORMATION_MESSAGE);
				}			
				
			} catch (Exception ex) {
				String message = ex.getMessage();
				ex.printStackTrace();
			}
		}	
	
		public void clearAll() throws Exception {
			try {				
				// clear text area
				txtNewPass.setText("");	
				txtOldPass.setText("");
				btnSave.setEnabled(true);
			} catch (Exception ex) {
				throw ex;
			}
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
			PassChangeScr passChange = new PassChangeScr(inBag);
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
	
	private boolean controlPassLength(){			
		String pass = "";
		pass = txtNewPass.getText();
	
		int passIntLength = pass.toString().length();
			  
		if(passIntLength >= 8 && passIntLength <= 11){
			return true;
		}else{
			JOptionPane.showMessageDialog(pnlErrorMsg, "File already load please check", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}				
			
			
	}
	
}