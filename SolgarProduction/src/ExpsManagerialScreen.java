package src;

import cb.esi.esiclient.util.ESIBag;

import com.toedter.calendar.JDateChooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import main.ConnectToDb;
import main.ReportQueries;
import util.PublicInterface;
import util.Util;

public class ExpsManagerialScreen extends JFrame
  implements ActionListener, ItemListener
{
  private static final int FRAME_WIDTH = 1100;
  private static final int FRAME_HEIGHT = 900;

  private JLabel lblCompanyName,lblChainName,lblOpType,lblEmpty,lblEmpty1,lblEmpty2,lblEmpty3,lblEmpty4,lblEmpty5,
  lblAdrCountry,lblAdrDistrict,lblAdrRegion,lblAdrCity,lblCompanyCode,lblSecondStage,lblKOL,lblClinic,
  lblMedRep,lblParameters,lblDateParameters;
  private JPanel paramPanel,pnlErrorMsg;
  private JScrollPane jScroll;
  private JTable resultTable;
  private JComboBox cmbBoxCompanies,cmbBoxCountry,cmbBoxDistrict,cmbBoxRegion,cmbBoxCity,
  cmbBoxCompanyCode,cmbBoxRepType,cmbBoxParType,cmbBoxSecondStage,cmbBoxKeyLeader,cmbBoxClinics,cmbBoxMedRep,cmbBoxDatePar;
  public JButton butExceltoScreen;
  public JButton btnExit;
  private JDateChooser startDate;
  private JDateChooser endDate;
  private String userName = "Hakan KAYAKUTLU";
  private String Lang = "RU";
  private ResourceBundle resourceBundle;

  private static final Calendar cal = Calendar.getInstance();
  private static final SimpleDateFormat dcn = new SimpleDateFormat("yyyyMMdd");

  DefaultTableModel dtm = new DefaultTableModel(0, 0);
  String[] header = { "Row Number", "Column1", "Column2" };
  

  public static void main(String[] args)
  {
    EventQueue.invokeLater(new Runnable()
    {
      public void run() {
        try {
          ESIBag inBag = new ESIBag();
          ExpsManagerialScreen expsManScreen = new ExpsManagerialScreen(inBag);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public ExpsManagerialScreen(ESIBag inBag) throws SQLException{
    super("Expense Managerial Screen");

    setBackground(Color.lightGray);
    getContentPane().setLayout(new BorderLayout(5, 5));

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension dim = toolkit.getScreenSize();
    int screenHeight = dim.height;
    int screenWidth = dim.width;
    setBounds((screenWidth - 1100) / 2, (screenHeight - 900) / 2, 1100, 900);
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
    
    this.jScroll = new JScrollPane();

    this.resultTable = new JTable(20, 8);
    resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    this.dtm.setColumnIdentifiers(this.header);
    this.resultTable.setModel(this.dtm);
    
	jScroll = new JScrollPane(resultTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);		
    this.jScroll.setViewportView(this.resultTable);
    this.jScroll.setBorder(new EmptyBorder(10, 10, 10, 10));
    getContentPane().add(this.jScroll, "Center");

    this.paramPanel = new JPanel(new GridLayout(0, 4, 5, 5));

    this.lblCompanyName = new JLabel(resourceBundle.getString("ObservationDates"));
    this.lblCompanyCode = new JLabel("Company Name");
    this.lblAdrCountry = new JLabel("Country");
    this.lblAdrDistrict = new JLabel("District");
    this.lblAdrRegion = new JLabel("Region");
    this.lblAdrCity = new JLabel("City");
    this.lblSecondStage = new JLabel("Second Stage (trainings)");
    this.lblKOL = new JLabel("KOL");
    this.lblChainName = new JLabel("Chain Name");
    this.lblClinic = new JLabel("Clinic");
    this.lblMedRep = new JLabel("Medical Representative");
    
    this.lblParameters = new JLabel("Parameters");
    this.lblDateParameters = new JLabel("Date Parameters");
   
    this.lblOpType = new JLabel("Operation Type");
    this.lblEmpty = new JLabel("");
    this.lblEmpty1 = new JLabel("");
    this.lblEmpty2 = new JLabel("");
    this.lblEmpty3 = new JLabel("");
    this.lblEmpty4 = new JLabel("");
    this.lblEmpty5 = new JLabel("");
    
    
    Calendar n = Calendar.getInstance();		
	n.add(Calendar.DATE, 21);
	Calendar n1 = Calendar.getInstance();		
	n1.add(Calendar.DATE, -21);
    

    this.startDate = new JDateChooser();
    this.startDate.setDateFormatString("yyyy-MM-dd");
    this.startDate.setDate(n1.getTime());

    this.endDate = new JDateChooser();
    this.endDate.setDateFormatString("yyyy-MM-dd");
    this.endDate.setDate(n.getTime());

    this.cmbBoxCountry = new JComboBox(new String[0]);
    Util.getPRMDataGroupBy("country", "solgar_prm.prm_exps_addresses",cmbBoxCountry,"","");	
    this.cmbBoxCountry.setMaximumRowCount(50);
    this.cmbBoxCountry.setSelectedIndex(0);
    
    this.cmbBoxCompanies = new JComboBox(new String[0]);
    Util.getPRMData("group_company", "solgar_prm.prm_russia_chains",cmbBoxCompanies);
    this.cmbBoxCompanies.insertItemAt("", 0);
    this.cmbBoxCompanies.setMaximumRowCount(50);
    this.cmbBoxCompanies.setSelectedIndex(-1);
    
    this.cmbBoxDistrict = new JComboBox(new String[0]);
    Util.getPRMDataGroupBy("area", "solgar_prm.prm_exps_addresses", this.cmbBoxDistrict, "country", this.cmbBoxCountry.getSelectedItem().toString());
    this.cmbBoxDistrict.insertItemAt("", 0);
    this.cmbBoxDistrict.setMaximumRowCount(50);
    this.cmbBoxDistrict.setSelectedIndex(-1);
   
    this.cmbBoxRegion = new JComboBox(new String[0]);
    this.cmbBoxCity = new JComboBox(new String[0]);

    this.cmbBoxCompanyCode = new JComboBox(new String[0]);
    this.cmbBoxCompanyCode.addItem("SOLGAR");
    this.cmbBoxCompanyCode.addItem("NATURES BOUNTY");
    this.cmbBoxCompanyCode.setSelectedIndex(0);

    this.cmbBoxRepType = new JComboBox(new String[0]);
    this.cmbBoxRepType.addItem("TRAINING (SECOND STAGE)");
    this.cmbBoxRepType.addItem("MAIN DISTRICT");
    this.cmbBoxRepType.addItem("REGIONS");
    this.cmbBoxRepType.addItem("CHAINS");
    this.cmbBoxRepType.addItem("KOL (GONORAR)");
    this.cmbBoxRepType.addItem("MED REPS");
    this.cmbBoxRepType.addItem("CLINICS");
    this.cmbBoxRepType.setSelectedIndex(0);
    
    this.cmbBoxParType = new JComboBox(new String[0]);
    this.cmbBoxParType.addItem("QUANTITY");
    this.cmbBoxParType.addItem("TOTAL COUNT");
    this.cmbBoxParType.addItem("TOTAL SUM");
    this.cmbBoxParType.setSelectedIndex(0);
    
    this.cmbBoxSecondStage = new JComboBox(new String[0]);
    Util.getPRMDataGroupBy("level1", "solgar_prm.prm_exps_types",cmbBoxSecondStage,"main_name","Мероприятия");	
    this.cmbBoxSecondStage.insertItemAt("", 0);
    this.cmbBoxSecondStage.setMaximumRowCount(50);
    this.cmbBoxSecondStage.setSelectedIndex(-1);
    
    cmbBoxKeyLeader = new JComboBox( new String[]{});		
	Util.getPRMData("leader_name", "solgar_prm.prm_exps_key_leader",cmbBoxKeyLeader);
	cmbBoxKeyLeader.insertItemAt("", 0);
	cmbBoxKeyLeader.setMaximumRowCount(50);
	cmbBoxKeyLeader.setSelectedIndex(-1);
	
	cmbBoxClinics = new JComboBox( new String[]{});		
	Util.getPRMData("clinic_name", "solgar_prm.prm_exps_clinics",cmbBoxClinics);	
	cmbBoxClinics.insertItemAt("", 0);
	cmbBoxClinics.setMaximumRowCount(50);
	cmbBoxClinics.setSelectedIndex(-1);
	
	cmbBoxMedRep = new JComboBox( new String[]{});		
	Util.getPRMDataGroupBy("medrep_name", "solgar_prm.prm_report_medrep",cmbBoxMedRep,"company","SL");	
	cmbBoxMedRep.setMaximumRowCount(50);
	cmbBoxMedRep.setEditable(true);
	cmbBoxMedRep.setSelectedIndex(-1);
	
	this.cmbBoxDatePar = new JComboBox(new String[0]);
    this.cmbBoxDatePar.addItem("WEEKLY");
    this.cmbBoxDatePar.addItem("MONTHLY");
    this.cmbBoxDatePar.addItem("QUARTERLY");
    this.cmbBoxDatePar.setSelectedIndex(0);

    if (this.userName.matches("Hakan KAYAKUTLU|Халит Гекмен|Эртюрк Мурат Хакан|Смолина Юлия Сергеевна")) {
      //this.cmbBoxOpType.setEnabled(true);
    }

    this.butExceltoScreen = new JButton("Generate Report");
    this.btnExit = new JButton("Exit");

    this.cmbBoxCountry.setName("Country");
    this.cmbBoxDistrict.setName("District");
    this.cmbBoxRegion.setName("Region");
    this.cmbBoxCity.setName("City");
    this.cmbBoxCompanyCode.setName("CompanyCode");
    this.cmbBoxRepType.setName("RepType");

    this.butExceltoScreen.addActionListener(this);
    this.btnExit.addActionListener(this);
    this.cmbBoxCompanies.addActionListener(this);
    this.butExceltoScreen.setEnabled(true);

    this.cmbBoxCountry.addItemListener(this);
    this.cmbBoxDistrict.addItemListener(this);
    this.cmbBoxRegion.addItemListener(this);
    this.cmbBoxCity.addItemListener(this);
    this.cmbBoxCompanyCode.addItemListener(this);
    this.cmbBoxRepType.addItemListener(this);

    this.paramPanel.add(this.lblCompanyName);
    this.paramPanel.add(this.startDate);
    this.paramPanel.add(this.endDate);
    this.paramPanel.add(this.btnExit);   
    this.paramPanel.add(this.lblCompanyCode);
    this.paramPanel.add(this.cmbBoxCompanyCode);    
    this.paramPanel.add(this.cmbBoxRepType);    
    this.paramPanel.add(this.butExceltoScreen);
    this.paramPanel.add(this.lblChainName);
    this.paramPanel.add(this.cmbBoxCompanies);   
    this.paramPanel.add(this.lblAdrCountry);
    this.paramPanel.add(this.cmbBoxCountry);
    this.paramPanel.add(this.lblSecondStage);
    this.paramPanel.add(this.cmbBoxSecondStage);    
    this.paramPanel.add(this.lblAdrDistrict);
    this.paramPanel.add(this.cmbBoxDistrict);   
    this.paramPanel.add(this.lblKOL);
    this.paramPanel.add(this.cmbBoxKeyLeader);   
    this.paramPanel.add(this.lblAdrRegion);
    this.paramPanel.add(this.cmbBoxRegion);    
    this.paramPanel.add(this.lblClinic);
    this.paramPanel.add(this.cmbBoxClinics);    
    this.paramPanel.add(this.lblAdrCity);
    this.paramPanel.add(this.cmbBoxCity);        
    this.paramPanel.add(this.lblMedRep);
    this.paramPanel.add(this.cmbBoxMedRep);
    this.paramPanel.add(this.lblParameters);
    this.paramPanel.add(this.cmbBoxParType);    
    this.paramPanel.add(lblEmpty2);
    this.paramPanel.add(lblEmpty3);    
    this.paramPanel.add(lblDateParameters);
    this.paramPanel.add(cmbBoxDatePar); 
    
    this.paramPanel.setBorder(new EmptyBorder(10, 15, 5, 15));
    getContentPane().add(this.paramPanel, "North");

    setDefaultCloseOperation(2);
    validate();
    setVisible(true);
    
   /* cmbBoxCompanies.setEnabled(true);
    
    cmbBoxCountry.setEnabled(true);
    cmbBoxDistrict.setEnabled(true);
    cmbBoxRegion.setEnabled(true);
    cmbBoxCity.setEnabled(true);
    
    cmbBoxPrdMain.setEnabled(false);
    cmbBoxPrdSub.setEnabled(false);
    cmbBoxProduct.setEnabled(false);*/

  }

  public void actionPerformed(ActionEvent e)
  {
    try {
      if (e.getActionCommand().equals("Generate Report"))
      {
        for (int i = this.dtm.getRowCount() - 1; i >= 0; i--) {
          this.dtm.removeRow(i);
        }

        String selectedCountry= "",selectedSecondStage="",selectedDistrict = "",selectedRegion = "",selectedCity = "",selectedChain = "",
        		selectedBrand = "",selectedMedRep = "",selectedOpType = "",selectedPrdMain = "",selectedPrdSub = "",repType = "",
        		selectedKOL = "",selectedClinic="",selectedFromPar="",selectedDatePar="";

        String selectedBeginDate = dcn.format(this.startDate.getDate());
        String selectedEndDate = dcn.format(this.endDate.getDate());
        
        if ((this.cmbBoxCountry.getSelectedItem() != null) && (this.cmbBoxCountry.getSelectedItem().toString().trim().length() > 0)) {
            selectedCountry = this.cmbBoxCountry.getSelectedItem().toString();
          }
        if ((this.cmbBoxCompanies.getSelectedItem() != null) && (this.cmbBoxCompanies.getSelectedItem().toString().trim().length() > 0)) {
        	selectedChain = this.cmbBoxCompanies.getSelectedItem().toString();
        }
        if ((this.cmbBoxDistrict.getSelectedItem() != null) && (this.cmbBoxDistrict.getSelectedItem().toString().trim().length() > 0)) {
          selectedDistrict = this.cmbBoxDistrict.getSelectedItem().toString();
        }
        if ((this.cmbBoxRegion.getSelectedItem() != null) && (this.cmbBoxRegion.getSelectedItem().toString().trim().length() > 0)) {
          selectedRegion = this.cmbBoxRegion.getSelectedItem().toString();
        }
        if ((this.cmbBoxCity.getSelectedItem() != null) && (this.cmbBoxCity.getSelectedItem().toString().trim().length() > 0)) {
          selectedCity = this.cmbBoxCity.getSelectedItem().toString();
        }
        if ((this.cmbBoxCompanyCode.getSelectedItem() != null) && (this.cmbBoxCompanyCode.getSelectedItem().toString().trim().length() > 0)) {
          if (this.cmbBoxCompanyCode.getSelectedItem().toString().trim().equalsIgnoreCase("SOLGAR"))
            selectedBrand = this.cmbBoxCompanyCode.getSelectedItem().toString().trim();
          else {
            selectedBrand = this.cmbBoxCompanyCode.getSelectedItem().toString().trim();
          }
        }
        
        if ((this.cmbBoxRepType.getSelectedItem() != null) && (this.cmbBoxRepType.getSelectedItem().toString().trim().length() > 0)) {
          repType = this.cmbBoxRepType.getSelectedItem().toString();
        }    
        if ((this.cmbBoxSecondStage.getSelectedItem() != null) && (this.cmbBoxSecondStage.getSelectedItem().toString().trim().length() > 0)) {
        	selectedSecondStage = this.cmbBoxSecondStage.getSelectedItem().toString();
        }

        if ((this.cmbBoxKeyLeader.getSelectedItem() != null) && (this.cmbBoxKeyLeader.getSelectedItem().toString().trim().length() > 0)) {
        	selectedKOL = this.cmbBoxKeyLeader.getSelectedItem().toString();
        }
        
        if ((this.cmbBoxClinics.getSelectedItem() != null) && (this.cmbBoxClinics.getSelectedItem().toString().trim().length() > 0)) {
        	selectedClinic = this.cmbBoxClinics.getSelectedItem().toString();
        }
        if ((this.cmbBoxMedRep.getSelectedItem() != null) && (this.cmbBoxMedRep.getSelectedItem().toString().trim().length() > 0)) {
        	selectedMedRep = this.cmbBoxMedRep.getSelectedItem().toString();
        }
        if ((this.cmbBoxParType.getSelectedItem() != null) && (this.cmbBoxParType.getSelectedItem().toString().trim().length() > 0)) {
        	selectedFromPar = this.cmbBoxParType.getSelectedItem().toString();
        }
        if ((this.cmbBoxDatePar.getSelectedItem() != null) && (this.cmbBoxDatePar.getSelectedItem().toString().trim().length() > 0)) {
        	selectedDatePar = this.cmbBoxDatePar.getSelectedItem().toString();
        }        
        
        if ((selectedBeginDate.length() == 0) || (selectedEndDate.length() == 0)) {
          JOptionPane.showMessageDialog(this.pnlErrorMsg, "Please fill date and company code", "Error", 0);
        }
        else
        {
    	  DefaultTableModel localDefaultTableModel = ReportQueries.repExpsManagerial(
          this.resultTable, selectedBeginDate,selectedEndDate,selectedCountry,selectedDistrict,selectedRegion,selectedCity,
          selectedChain,selectedBrand,repType,selectedSecondStage,selectedKOL,selectedClinic,selectedMedRep,
          selectedFromPar,selectedDatePar);        
        }
      }
      else if (e.getActionCommand().equals("Exit")) {
        setVisible(false);
      }
    }
    catch (Exception ex) {
      String message = ex.getMessage();
      ex.printStackTrace();
    }
  }

  public void itemStateChanged(ItemEvent itemEvent)
  {
    JComboBox cmbBox = (JComboBox)itemEvent.getSource();
    String name = cmbBox.getName();
    if ((cmbBox.getSelectedItem() != null) && (cmbBox.getSelectedItem().toString().length() > 0)) {
      String compType = "BN";
      if (this.cmbBoxCompanyCode.getSelectedItem().toString().equalsIgnoreCase("SOLGAR")) {
        compType = "SL";
      }
      if (name.equalsIgnoreCase("CompanyCode")) {
    	  cmbBoxMedRep.removeAllItems();
    	  try {
    		Util.getPRMDataGroupBy("medrep_name", "solgar_prm.prm_report_medrep",cmbBoxMedRep,"company",compType);
    	  } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    	  }
    	  cmbBoxMedRep.setSelectedIndex(-1);
    	  
      }
      if (name.equalsIgnoreCase("Country")) {
    	  this.cmbBoxCompanies.removeAllItems();

		  cmbBoxDistrict.removeAllItems();
		  cmbBoxRegion.removeAllItems();
		  cmbBoxCity.removeAllItems();	    		  
		  try {
			Util.getPRMDataGroupBy("area", "solgar_prm.prm_exps_addresses",cmbBoxDistrict,"country",cmbBoxCountry.getSelectedItem().toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		  cmbBoxDistrict.insertItemAt("", 0);
		  cmbBoxRegion.setSelectedIndex(-1);
		  cmbBoxCity.setSelectedIndex(-1);	    		  	    		  
		 
      } else if (name.equalsIgnoreCase("District")) {
    	  cmbBoxMedRep.removeAllItems();
		  cmbBoxRegion.removeAllItems();
		  cmbBoxCity.removeAllItems();	    		  
		  try {
			Util.getPRMDataGroupBy("region", "solgar_prm.prm_exps_addresses",cmbBoxRegion,"area",cmbBoxDistrict.getSelectedItem().toString());
			Util.getPRMDataTwoConditionsGroupBy("medrep_name", "solgar_prm.prm_report_medrep",cmbBoxMedRep,"company",compType,"country",cmbBoxDistrict.getSelectedItem().toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  cmbBoxRegion.insertItemAt("", 0);		 
		  cmbBoxRegion.setSelectedIndex(-1);
		  cmbBoxCity.setSelectedIndex(-1);	    		  
		  cmbBoxMedRep.setSelectedIndex(-1);
      } else if (name.equalsIgnoreCase("Region")) {  	  
    	  cmbBoxCity.removeAllItems();
    	  cmbBoxCity.insertItemAt("", 0);
		  try {
			Util.getPRMDataGroupBy("city", "solgar_prm.prm_exps_addresses",cmbBoxCity,"region",cmbBoxRegion.getSelectedItem().toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		  
		  cmbBoxCity.setSelectedIndex(-1);
		
      } else if (!name.equalsIgnoreCase("City"))
      {
    	  
      }
      if(name.equalsIgnoreCase("RepType")){
    	  
      }     
      
    }
  }

  public static void exit()
  {
    System.exit(0);
  }

  public class Utils
  {
    public Utils()
    {
    }
  }
}