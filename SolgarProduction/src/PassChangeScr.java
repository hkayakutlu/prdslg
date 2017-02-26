package src;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import main.ConnectToDb;
import util.Util;

public class PassChangeScr extends JFrame {

    private PassWordDialogA passDialog;
    public JTextField jtfUsername = new JTextField(15);

    public PassChangeScr(String userName) {  	
        passDialog = new PassWordDialogA(this, true, userName);
        passDialog.setVisible(true);
        jtfUsername.setText(userName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	String userName="Hakan";
                JFrame frame = new PassChangeScr(userName);
                frame.getContentPane().setBackground(Color.BLACK);
                frame.setTitle("Logged In");
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                frame.setVisible(false);
                
            }
        });
    }
}

class PassWordDialogA extends JDialog {
	
	private final JLabel jlblUsername = new JLabel("Username");
    private final JLabel jlblOldPassword = new JLabel("Old Password");
    private final JLabel jlblNewPassword = new JLabel("New Password");
   
    private final JTextField jtfUsername = new JTextField(15);
    private final JPasswordField jpfOldPassword = new JPasswordField();
    private final JPasswordField jpfNewPassword = new JPasswordField(8);

    private final JButton jbtOk = new JButton("Change");
    private final JButton jbtCancel = new JButton("Cancel");

    private final JLabel jlblStatus = new JLabel(" ");
    
    private JPanel pnlInfoMsg;

    public PassWordDialogA(final JFrame parent, boolean modal,String userName) {
        super(parent, modal);
        
        jtfUsername.setText(userName);
        jtfUsername.enable(false);
        
        JPanel p3 = new JPanel(new GridLayout(3, 1));
        p3.add(jlblUsername);
        p3.add(jlblOldPassword);
        p3.add(jlblNewPassword);

        JPanel p4 = new JPanel(new GridLayout(3, 1));
        p4.add(jtfUsername);
        p4.add(jpfOldPassword);
        p4.add(jpfNewPassword);

        JPanel p1 = new JPanel();
        p1.add(p3);
        p1.add(p4);

        JPanel p2 = new JPanel();
        p2.add(jbtOk);
        p2.add(jbtCancel);   

        JPanel p5 = new JPanel(new BorderLayout());
        p5.add(p2, BorderLayout.CENTER);
        p5.add(jlblStatus, BorderLayout.NORTH);
        jlblStatus.setForeground(Color.RED);
        jlblStatus.setHorizontalAlignment(SwingConstants.CENTER);
     
        setLayout(new BorderLayout());
        add(p1, BorderLayout.NORTH);
        add(p5, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        addWindowListener(new WindowAdapter() {  
            @Override
            public void windowClosing(WindowEvent e) {  
                System.exit(0);  
            }  
        });


        jbtOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String result="";
            	
            	if (jpfNewPassword.getText().toString().length() != 6) {

                    //Set password input box background color to green
            		JOptionPane.showMessageDialog(pnlInfoMsg, "Password should be 6 digit", "Information", JOptionPane.INFORMATION_MESSAGE);
            		jpfNewPassword.setBackground(Color.red);
                    return;
                    
                }
            	
				try {
					result = Util.changePass(jtfUsername.getText().toString(), jpfOldPassword.getText().toString(),jpfNewPassword.getText().toString());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            	if (result.equalsIgnoreCase("1")) {            		
                    parent.setVisible(true); 
                    JOptionPane.showMessageDialog(pnlInfoMsg, "Password changed Correctly", "Information", JOptionPane.INFORMATION_MESSAGE);
                	setVisible(false);              	
                } else {
                    jlblStatus.setText("Invalid username or password");
                }
            }
        });
        jbtCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                parent.dispose();
                //System.exit(0);
            }
        });
    }
}