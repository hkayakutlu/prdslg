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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import main.ReportQueries;
import util.Util;

public class StorageStockObservation extends JFrame
  implements ActionListener, ItemListener
{
  private static final int FRAME_WIDTH = 1100;
  private static final int FRAME_HEIGHT = 900;
  private JLabel lblCompanyName;
  private JLabel lblChainName,lblOpType,lblEmpty,lblEmpty1,lblEmpty2,lblEmpty3,lblEmpty4,lblEmpty5,
  lblAdrCountry,lblAdrDistrict,lblAdrRegion,lblAdrCity,lblCompanyCode,lblPrdMain,lblPrdSub,lblProduct,lblPrdCategory1,lblPrdCategory2;
  private JPanel paramPanel;
  private JPanel pnlErrorMsg;
  private JScrollPane jScroll;
  private JTable resultTable;
  private JComboBox cmbBoxCompanies,cmbBoxCountry,cmbBoxDistrict,cmbBoxRegion,cmbBoxCity,cmbBoxOpType,
  cmbBoxCompanyCode,cmbBoxPrdMain,cmbBoxPrdSub,cmbBoxProduct,cmbBoxRepType,cmbBoxPrdCategory1,cmbBoxPrdCategory2;
  public JButton butExceltoScreen;
  public JButton btnExit;
  private JDateChooser startDate;
  private JDateChooser endDate;
  private String userName = "Hakan KAYAKUTLU";

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
          StorageStockObservation localStorageStockObservation = new StorageStockObservation(inBag);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public StorageStockObservation(ESIBag inBag)
    throws SQLException
  {
    super("Storage Report Observation");

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

    }
    catch (Exception localException)
    {
    }

    this.jScroll = new JScrollPane();

    this.resultTable = new JTable(20, 8);
    this.dtm.setColumnIdentifiers(this.header);
    this.resultTable.setModel(this.dtm);

    this.jScroll.setViewportView(this.resultTable);
    this.jScroll.setBorder(new EmptyBorder(10, 10, 10, 10));
    getContentPane().add(this.jScroll, "Center");

    this.paramPanel = new JPanel(new GridLayout(9, 4, 5, 5));

    this.lblCompanyName = new JLabel("Sales Dates For Each Chain");
    this.lblChainName = new JLabel("Distrubutor Name");
    this.lblOpType = new JLabel("Operation Type");
    this.lblEmpty = new JLabel("");
    this.lblEmpty1 = new JLabel("");
    this.lblEmpty2 = new JLabel("");
    this.lblEmpty3 = new JLabel("");
    this.lblEmpty4 = new JLabel("");
    this.lblEmpty5 = new JLabel("");
    this.lblAdrCountry = new JLabel("Country");
    this.lblAdrDistrict = new JLabel("District");
    this.lblAdrRegion = new JLabel("Region");
    this.lblAdrCity = new JLabel("City");
    this.lblCompanyCode = new JLabel("Company Name");
    this.lblPrdMain = new JLabel("Product Main Group");
    this.lblPrdSub = new JLabel("Product Sub Group");
    this.lblProduct = new JLabel("Product Name");
    this.lblPrdCategory1 = new JLabel("Product Category 1");
    this.lblPrdCategory2 = new JLabel("Product Category 2");

    this.startDate = new JDateChooser();
    this.startDate.setDateFormatString("yyyy-MM-dd");
    this.startDate.setDate(cal.getTime());

    this.endDate = new JDateChooser();
    this.endDate.setDateFormatString("yyyy-MM-dd");
    this.endDate.setDate(cal.getTime());

    this.cmbBoxCountry = new JComboBox(new String[0]);
    Util.getPRMDataGroupBy("country", "solgar_stk.stock_address_group", this.cmbBoxCountry, "", "");
    this.cmbBoxCountry.setMaximumRowCount(50);
    this.cmbBoxCountry.setSelectedIndex(0);
    
    this.cmbBoxCompanies = new JComboBox(new String[0]);
    Util.getPRMDataGroupBy("storage", "solgar_prm.prm_storages", this.cmbBoxCompanies, "country", this.cmbBoxCountry.getSelectedItem().toString());
    this.cmbBoxCompanies.insertItemAt("", 0);
    this.cmbBoxCompanies.setMaximumRowCount(50);
    this.cmbBoxCompanies.setSelectedIndex(-1);
    
    this.cmbBoxDistrict = new JComboBox(new String[0]);
    Util.getPRMDataGroupBy("district", "solgar_stk.stock_address_group", this.cmbBoxDistrict, "country", this.cmbBoxCountry.getSelectedItem().toString());
    this.cmbBoxDistrict.insertItemAt("", 0);
    this.cmbBoxDistrict.setMaximumRowCount(50);
    this.cmbBoxDistrict.setSelectedIndex(-1);
   
    this.cmbBoxRegion = new JComboBox(new String[0]);
    this.cmbBoxCity = new JComboBox(new String[0]);

    this.cmbBoxCompanyCode = new JComboBox(new String[0]);
    this.cmbBoxCompanyCode.addItem("SOLGAR");
    this.cmbBoxCompanyCode.addItem("NATURES BOUNTY");
    this.cmbBoxCompanyCode.setSelectedIndex(0);

    this.cmbBoxPrdMain = new JComboBox(new String[0]);
    this.cmbBoxPrdMain.addItem("");
    Util.getPRMDataGroupBy("product_main_group", "solgar_stk.stock_product_group", this.cmbBoxPrdMain, "", "");
    this.cmbBoxPrdMain.setSelectedIndex(-1);

    this.cmbBoxPrdSub = new JComboBox(new String[0]);
    
    this.cmbBoxProduct = new JComboBox(new String[0]);
    this.cmbBoxProduct.addItem("");
    Util.getPRMDataGroupBy("product_official_name", "solgar_stk.stock_product_group", this.cmbBoxProduct, "product_type", "SL");
    this.cmbBoxProduct.setSelectedIndex(-1);
    
    this.cmbBoxPrdCategory1 = new JComboBox(new String[0]);
    this.cmbBoxPrdCategory1.addItem("");
    Util.getPRMDataGroupBy("product_category", "solgar_stk.stock_product_group", this.cmbBoxPrdCategory1, "product_type", "SL");
    this.cmbBoxPrdCategory1.setSelectedIndex(-1);
    
    this.cmbBoxPrdCategory2 = new JComboBox(new String[0]);
    this.cmbBoxPrdCategory2.addItem("");
    Util.getPRMDataGroupBy("product_category1", "solgar_stk.stock_product_group", this.cmbBoxPrdCategory2, "product_type", "SL");
    this.cmbBoxPrdCategory2.setSelectedIndex(-1);

    this.cmbBoxOpType = new JComboBox(new String[0]);
    this.cmbBoxOpType.addItem("SALES");
    this.cmbBoxOpType.addItem("STOCK");
    this.cmbBoxOpType.setSelectedIndex(1);
    this.cmbBoxOpType.setEnabled(false);

    this.cmbBoxRepType = new JComboBox(new String[0]);
    this.cmbBoxRepType.addItem("DISTRUBUTOR_REPORT");
    this.cmbBoxRepType.addItem("REGIONAL_REPORT");
    this.cmbBoxRepType.addItem("PRODUCT_REPORT");
    this.cmbBoxRepType.addItem("PRODUCT_SUB_GROUP_REPORT");
    this.cmbBoxRepType.addItem("TOP_PRODUCTS_REPORT");
    this.cmbBoxRepType.addItem("PROMOTION_PRODUCTS_REPORT");
    this.cmbBoxRepType.setSelectedIndex(0);

    if (this.userName.matches("Hakan KAYAKUTLU|Халит Гекмен|Эртюрк Мурат Хакан|Смолина Юлия Сергеевна")) {
      this.cmbBoxOpType.setEnabled(true);
    }

    this.butExceltoScreen = new JButton("Generate Report");
    this.btnExit = new JButton("Exit");

    this.cmbBoxCountry.setName("Country");
    this.cmbBoxDistrict.setName("District");
    this.cmbBoxRegion.setName("Region");
    this.cmbBoxCity.setName("City");
    this.cmbBoxCompanyCode.setName("CompanyCode");
    this.cmbBoxPrdMain.setName("PrdMain");
    this.cmbBoxPrdSub.setName("PrdSub");
    this.cmbBoxProduct.setName("Product");
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

    this.cmbBoxPrdMain.addItemListener(this);
    this.cmbBoxPrdSub.addItemListener(this);
    this.cmbBoxProduct.addItemListener(this);
    this.cmbBoxRepType.addItemListener(this);

    this.paramPanel.add(this.lblCompanyName);
    this.paramPanel.add(this.startDate);
    this.paramPanel.add(this.endDate);
    this.paramPanel.add(this.btnExit);

    this.paramPanel.add(this.lblChainName);
    this.paramPanel.add(this.cmbBoxCompanies);
    this.paramPanel.add(this.cmbBoxRepType);
    this.paramPanel.add(this.butExceltoScreen);

    this.paramPanel.add(this.lblOpType);
    this.paramPanel.add(this.cmbBoxOpType);
    this.paramPanel.add(this.lblAdrCountry);
    this.paramPanel.add(this.cmbBoxCountry);
    this.paramPanel.add(this.lblCompanyCode);
    this.paramPanel.add(this.cmbBoxCompanyCode);
    this.paramPanel.add(this.lblAdrDistrict);
    this.paramPanel.add(this.cmbBoxDistrict);
    this.paramPanel.add(this.lblPrdMain);
    this.paramPanel.add(this.cmbBoxPrdMain);
    this.paramPanel.add(this.lblAdrRegion);
    this.paramPanel.add(this.cmbBoxRegion);
    this.paramPanel.add(this.lblPrdSub);
    this.paramPanel.add(this.cmbBoxPrdSub);
    this.paramPanel.add(this.lblAdrCity);
    this.paramPanel.add(this.cmbBoxCity);  
    this.paramPanel.add(lblPrdCategory1);
    this.paramPanel.add(cmbBoxPrdCategory1);
    this.paramPanel.add(lblEmpty);
    this.paramPanel.add(lblEmpty1);    
    this.paramPanel.add(lblPrdCategory2);
    this.paramPanel.add(cmbBoxPrdCategory2);
    this.paramPanel.add(lblEmpty2);
    this.paramPanel.add(lblEmpty3);    
    this.paramPanel.add(this.lblProduct);
    this.paramPanel.add(this.cmbBoxProduct);

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

        String selectedCountry= "",selectedDistrubutor="",selectedDistrict = "",selectedRegion = "",selectedCity = "",selectedCompany = "",
        		selectedBrand = "",selectedMedRep = "",selectedOpType = "",selectedPrdMain = "",selectedPrdSub = "",
        		selectedProduct = "",repType = "",selectedPrdCategory1="",selectedPrdCategory2="";

        String selectedBeginDate = dcn.format(this.startDate.getDate());
        String selectedEndDate = dcn.format(this.endDate.getDate());
        
        if ((this.cmbBoxCountry.getSelectedItem() != null) && (this.cmbBoxCountry.getSelectedItem().toString().trim().length() > 0)) {
            selectedCountry = this.cmbBoxCountry.getSelectedItem().toString();
          }
        if ((this.cmbBoxCompanies.getSelectedItem() != null) && (this.cmbBoxCompanies.getSelectedItem().toString().trim().length() > 0)) {
          selectedCompany = this.cmbBoxCompanies.getSelectedItem().toString();
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
            selectedBrand = "SL";
          else {
            selectedBrand = "BN";
          }
        }
        if ((this.cmbBoxOpType.getSelectedItem() != null) && (this.cmbBoxOpType.getSelectedItem().toString().trim().length() > 0)) {
          selectedOpType = this.cmbBoxOpType.getSelectedItem().toString();
        }
        if ((this.cmbBoxRepType.getSelectedItem() != null) && (this.cmbBoxRepType.getSelectedItem().toString().trim().length() > 0)) {
          repType = this.cmbBoxRepType.getSelectedItem().toString();
        }
        if ((this.cmbBoxPrdMain.getSelectedItem() != null) && (this.cmbBoxPrdMain.getSelectedItem().toString().trim().length() > 0)) {
          selectedPrdMain = this.cmbBoxPrdMain.getSelectedItem().toString();
        }
        if ((this.cmbBoxPrdSub.getSelectedItem() != null) && (this.cmbBoxPrdSub.getSelectedItem().toString().trim().length() > 0)) {
          selectedPrdSub = this.cmbBoxPrdSub.getSelectedItem().toString();
        }
        if ((this.cmbBoxProduct.getSelectedItem() != null) && (this.cmbBoxProduct.getSelectedItem().toString().trim().length() > 0)) {
          selectedProduct = this.cmbBoxProduct.getSelectedItem().toString();
        }
	    if ((this.cmbBoxPrdCategory1.getSelectedItem() != null) && (this.cmbBoxPrdCategory1.getSelectedItem().toString().trim().length() > 0)) {
	        selectedPrdCategory1 = this.cmbBoxPrdCategory1.getSelectedItem().toString();
	    }
	    if ((this.cmbBoxPrdCategory2.getSelectedItem() != null) && (this.cmbBoxPrdCategory2.getSelectedItem().toString().trim().length() > 0)) {
	        selectedPrdCategory2 = this.cmbBoxPrdCategory2.getSelectedItem().toString();
	    }

        if ((selectedBeginDate.length() == 0) || (selectedEndDate.length() == 0) || (selectedOpType.length() == 0)) {
          JOptionPane.showMessageDialog(this.pnlErrorMsg, "Please fill date and company code", "Error", 0);
        }
        else
        {
        	DefaultTableModel localDefaultTableModel;
          if (repType.equalsIgnoreCase("DISTRUBUTOR_REPORT"))
            localDefaultTableModel = ReportQueries.repStorageStockMonthly(
              this.resultTable, selectedBeginDate, selectedEndDate, selectedCompany, selectedDistrict, 
              selectedRegion, selectedCity, selectedBrand, selectedOpType, selectedPrdMain, selectedPrdSub, selectedProduct,repType,
              selectedCountry,selectedPrdCategory1,selectedPrdCategory2);
          else if (repType.equalsIgnoreCase("REGIONAL_REPORT"))
              localDefaultTableModel = ReportQueries.repStorageStockMonthly(
                this.resultTable, selectedBeginDate, selectedEndDate, selectedCompany, selectedDistrict, 
                selectedRegion, selectedCity, selectedBrand, selectedOpType, selectedPrdMain, selectedPrdSub, selectedProduct,repType,
                selectedCountry,selectedPrdCategory1,selectedPrdCategory2);
          else if (repType.equalsIgnoreCase("PRODUCT_REPORT"))
              localDefaultTableModel = ReportQueries.repStorageStockMonthly(
                this.resultTable, selectedBeginDate, selectedEndDate, selectedCompany, selectedDistrict, 
                selectedRegion, selectedCity, selectedBrand, selectedOpType, selectedPrdMain, selectedPrdSub, selectedProduct,repType,
                selectedCountry,selectedPrdCategory1,selectedPrdCategory2);
          else if (repType.equalsIgnoreCase("PRODUCT_SUB_GROUP_REPORT"))
              localDefaultTableModel = ReportQueries.repStorageStockMonthly(
                this.resultTable, selectedBeginDate, selectedEndDate, selectedCompany, selectedDistrict, 
                selectedRegion, selectedCity, selectedBrand, selectedOpType, selectedPrdMain, selectedPrdSub, selectedProduct,repType,
                selectedCountry,selectedPrdCategory1,selectedPrdCategory2);
          else if (repType.equalsIgnoreCase("TOP_PRODUCTS_REPORT"))
              localDefaultTableModel = ReportQueries.repStorageStockMonthly(
                this.resultTable, selectedBeginDate, selectedEndDate, selectedCompany, selectedDistrict, 
                selectedRegion, selectedCity, selectedBrand, selectedOpType, selectedPrdMain, selectedPrdSub, selectedProduct,repType,
                selectedCountry,selectedPrdCategory1,selectedPrdCategory2);
          else if (repType.equalsIgnoreCase("PROMOTION_PRODUCTS_REPORT"))
              localDefaultTableModel = ReportQueries.repStorageStockMonthly(
                this.resultTable, selectedBeginDate, selectedEndDate, selectedCompany, selectedDistrict, 
                selectedRegion, selectedCity, selectedBrand, selectedOpType, selectedPrdMain, selectedPrdSub, selectedProduct,repType,
                selectedCountry,selectedPrdCategory1,selectedPrdCategory2);
          else {
            localDefaultTableModel = ReportQueries.repStorageStockResult(
              this.resultTable, selectedBeginDate, selectedEndDate, selectedCompany, selectedDistrict, selectedRegion, 
              selectedCity, selectedBrand, selectedOpType);
          }
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
    	     this.cmbBoxProduct.removeAllItems();
    	     this.cmbBoxPrdCategory2.removeAllItems();
    	     this.cmbBoxPrdCategory1.removeAllItems();
    	     this.cmbBoxProduct.addItem("");
    	     this.cmbBoxPrdCategory2.addItem("");
    	     this.cmbBoxPrdCategory1.addItem("");
    	    try{
	    	    Util.getPRMDataGroupBy("product_official_name", "solgar_stk.stock_product_group", this.cmbBoxProduct, "product_type", compType);
	    	    Util.getPRMDataGroupBy("product_category1", "solgar_stk.stock_product_group", this.cmbBoxPrdCategory2, "product_type", compType);
	    	    Util.getPRMDataGroupBy("product_category", "solgar_stk.stock_product_group", this.cmbBoxPrdCategory1, "product_type", compType);
		      }
		      catch (SQLException e) {
		        e.printStackTrace();
		      }
	}
      if (name.equalsIgnoreCase("Country")) {
    	  this.cmbBoxCompanies.removeAllItems();
    	  try {  
    		  Util.getPRMDataGroupBy("storage", "solgar_prm.prm_storages", this.cmbBoxCompanies, "country", this.cmbBoxCountry.getSelectedItem().toString());
	      }
	      catch (SQLException e) {
	        e.printStackTrace();
	      }
    	  this.cmbBoxCompanies.setSelectedIndex(-1);
    	  
        this.cmbBoxDistrict.removeAllItems();
        this.cmbBoxRegion.removeAllItems();
        this.cmbBoxCity.removeAllItems();
        try {
          Util.getPRMDataGroupBy("district", "solgar_stk.stock_address_group", this.cmbBoxDistrict, "country", this.cmbBoxCountry.getSelectedItem().toString());
        }
        catch (SQLException e) {
          e.printStackTrace();
        }
        this.cmbBoxDistrict.insertItemAt("", 0);
        this.cmbBoxDistrict.setSelectedIndex(-1);
        this.cmbBoxRegion.setSelectedIndex(-1);
        this.cmbBoxCity.setSelectedIndex(-1);
      } else if (name.equalsIgnoreCase("District")) {
        this.cmbBoxRegion.removeAllItems();
        this.cmbBoxCity.removeAllItems();
        try {
          Util.getPRMDataGroupBy("region", "solgar_stk.stock_address_group", this.cmbBoxRegion, "district", this.cmbBoxDistrict.getSelectedItem().toString());
        }
        catch (SQLException e) {
          e.printStackTrace();
        }
        this.cmbBoxRegion.insertItemAt("", 0);
        this.cmbBoxRegion.setSelectedIndex(-1);
        this.cmbBoxCity.setSelectedIndex(-1);
      } else if (name.equalsIgnoreCase("Region")) {
        this.cmbBoxCity.removeAllItems();
        try {
          Util.getPRMDataGroupBy("city", "solgar_stk.stock_address_group", this.cmbBoxCity, "region", this.cmbBoxRegion.getSelectedItem().toString());
        }
        catch (SQLException e) {
          e.printStackTrace();
        }
        this.cmbBoxCity.insertItemAt("", 0);
        this.cmbBoxCity.setSelectedIndex(-1);
      } else if (!name.equalsIgnoreCase("City"))
      {
        if (!name.equalsIgnoreCase("CompanyCode"))
        {
          if (name.equalsIgnoreCase("PrdMain")) {
            this.cmbBoxPrdSub.removeAllItems();
            this.cmbBoxPrdSub.addItem("");
            this.cmbBoxProduct.addItem("");
            try
            {
              Util.getPRMDataTwoConditionsGroupBy("product_sub_group", "solgar_stk.stock_product_group", this.cmbBoxPrdSub, "product_main_group", this.cmbBoxPrdMain.getSelectedItem().toString(), "product_type", compType);
            }
            catch (SQLException e) {
              e.printStackTrace();
            }
            this.cmbBoxPrdSub.setSelectedIndex(-1);
          } else if (name.equalsIgnoreCase("PrdSub")) {
            this.cmbBoxProduct.removeAllItems();
            this.cmbBoxProduct.addItem("");
            try
            {
              Util.getPRMDataTwoConditionsGroupBy("product_official_name", "solgar_stk.stock_product_group", this.cmbBoxProduct, "product_sub_group", this.cmbBoxPrdSub.getSelectedItem().toString(), "product_type", compType);
            }
            catch (SQLException e) {
              e.printStackTrace();
            }
            this.cmbBoxProduct.setSelectedIndex(-1);
          }
        }
      }
      if(name.equalsIgnoreCase("RepType")){

    	  /*this.cmbBoxCompanies.setEnabled(false);
    	  this.cmbBoxCompanies.setSelectedIndex(-1);
    	  
    	  this.cmbBoxCountry.setEnabled(false);
    	  this.cmbBoxCountry.setSelectedIndex(-1);
    	  this.cmbBoxDistrict.setEnabled(false);
    	  this.cmbBoxDistrict.setSelectedIndex(-1);
    	  this.cmbBoxRegion.setEnabled(false);
    	  this.cmbBoxRegion.setSelectedIndex(-1);
    	  this.cmbBoxCity.setEnabled(false);
    	  this.cmbBoxCity.setSelectedIndex(-1);
          
    	  this.cmbBoxPrdMain.setEnabled(false);
    	  this.cmbBoxPrdMain.setSelectedIndex(-1);
    	  this.cmbBoxPrdSub.setEnabled(false);
    	  this.cmbBoxPrdSub.setSelectedIndex(-1);
    	  this.cmbBoxProduct.setEnabled(false);
    	  this.cmbBoxProduct.setSelectedIndex(-1);
    	  
          String repType = cmbBoxRepType.getSelectedItem().toString();
    	  
    	  if(repType.equalsIgnoreCase("DISTRUBUTOR_SALES")){
    		  this.cmbBoxCompanies.setEnabled(true);
    		  this.cmbBoxCountry.setEnabled(true);
    		  this.cmbBoxDistrict.setEnabled(true);
    		  this.cmbBoxRegion.setEnabled(true);
    		  this.cmbBoxCity.setEnabled(true);
    	  }else if(repType.equalsIgnoreCase("REGIONAL_SALES")){
    		  this.cmbBoxCompanies.setEnabled(true);
    		  this.cmbBoxCountry.setEnabled(true);
    		  this.cmbBoxDistrict.setEnabled(true);
    		  this.cmbBoxRegion.setEnabled(true);
    		  this.cmbBoxCity.setEnabled(true);
    	  }else if(repType.equalsIgnoreCase("PRODUCT_SALES")){
    		  this.cmbBoxPrdMain.setEnabled(true);
    		  this.cmbBoxPrdSub.setEnabled(true);
    		  this.cmbBoxProduct.setEnabled(true);
    		  this.cmbBoxCountry.setEnabled(true);
    		  this.cmbBoxDistrict.setEnabled(true);
    		  this.cmbBoxRegion.setEnabled(true);
    		  this.cmbBoxCity.setEnabled(true);
    	  }*/
    	  
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