package src;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.ConnectToDb;
import cb.esi.esiclient.util.ESIBag;

public class ChainExpsManagerialScreen extends JFrame {

	private JPanel contentPane;
	private ResourceBundle resourceBundle;
	
	  private String userName = "Hakan KAYAKUTLU";
	  private String Lang = "RU";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ESIBag inBag= new ESIBag();
					ChainExpsManagerialScreen frame = new ChainExpsManagerialScreen(inBag);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChainExpsManagerialScreen(ESIBag inBag) throws SQLException{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		
		try
	    {
	      if (inBag.existsBagKey("LOGINNAME")) {
	        this.userName = inBag.get("LOGINNAME").toString();
	      }
	      if (inBag.existsBagKey("LANGUAGE")) {
	    	  Lang = inBag.get("LANGUAGE").toString();
	      }
	      resourceBundle = ConnectToDb.readBundleFile(Lang);

	    }
	    catch (Exception localException)
	    {
	    }
		
	}

}
