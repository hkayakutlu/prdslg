package src;

import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

import javax.swing.*;

import cb.esi.esiclient.util.BagKeyNotFoundException;
import cb.esi.esiclient.util.ESIBag;
import util.Util;
import util.PublicInterface;

public class LoginScreen extends JFrame {

    private PassWordDialog passDialog;
    private String Lang;   

    public LoginScreen(ESIBag inBag) throws BagKeyNotFoundException {  	
    	Lang = inBag.get("LANGUAGE").toString();
        passDialog = new PassWordDialog(this, true);
        passDialog.setVisible(true);        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	ESIBag inBag = new ESIBag();
                JFrame frame =null;
				try {
					frame = new LoginScreen(inBag);
				} catch (BagKeyNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                frame.getContentPane().setBackground(Color.BLACK);
                frame.setTitle("Logged In");
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                frame.setVisible(false);
                
            }
        });
    }
}

class PassWordDialog extends JDialog {

    private final JLabel jlblUsername = new JLabel(PublicInterface.resourceBundle.getString("Username"));
    private final JLabel jlblPassword = new JLabel(PublicInterface.resourceBundle.getString("Password"));

    private final JTextField jtfUsername = new JTextField(15);
    private final JPasswordField jpfPassword = new JPasswordField();

    private final JButton jbtOk = new JButton(PublicInterface.resourceBundle.getString("Login"));
    private final JButton jbtCancel = new JButton(PublicInterface.resourceBundle.getString("Cancel"));

    private final JLabel jlblStatus = new JLabel(" ");
    

    public PassWordDialog() {
        this(null, true);
    }

    public PassWordDialog(final JFrame parent, boolean modal) {
        super(parent, modal);
        
        JPanel p3 = new JPanel(new GridLayout(2, 1));
        p3.add(jlblUsername);
        p3.add(jlblPassword);

        JPanel p4 = new JPanel(new GridLayout(2, 1));
        p4.add(jtfUsername);
        p4.add(jpfPassword);

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
            	ESIBag outBag = new ESIBag();
            	String employeee_name="";
            	String emplopyeeId = "0";
				try {
					outBag = Util.getAuthorization(jtfUsername.getText().toString(), jpfPassword.getText().toString());
					employeee_name = outBag.get("EMPLOYEE_NAME");
					emplopyeeId = outBag.get("ID");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            	if (employeee_name.length()>0) {
                    parent.setVisible(true);
                    parent.setTitle(employeee_name);
                    parent.setName(emplopyeeId);;
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
                System.exit(0);
            }
        });
    }
}