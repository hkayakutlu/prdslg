package main;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import java.util.regex.*;

import cb.esi.esiclient.util.ESIBag;

public class ReportQueries {

	static final Properties prop = ConnectToDb.readConfFile();
	public static ArrayList textVariableIndex;
	public static ArrayList textVaLueIndex;
	
	public ReportQueries() {
		// TODO Auto-generated constructor stub
	   }
	
	public static DefaultTableModel repGetTotalCounts(JTable resultTable,String reportName,String beginDate,String endDate,String chain,
			String selectedCompany,String selectedCountry,String selectedRegion,String selectedCity,String selectedProduct,String selectedMedRep) throws Exception {

        Connection conn =null;
        Statement stmt =null;
        String selectSQL ="";
        DefaultTableModel tableModel =null;         

        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));            
            
        	String sqlFrom ="select x.product_type,x.sales_date";
            String groupby ="group by x.sales_date,x.product_type";
            String whereCondition =" where 1=1 ";
            
            if(chain.length()>0){	
            	sqlFrom = sqlFrom+",x.main_group" ;
            	groupby =groupby+",x.main_group";
            	whereCondition = whereCondition+" and x.main_group='"+chain+"'";
            }
            if(selectedCountry.length()>0){
            	sqlFrom = sqlFrom+",x.country" ;
            	groupby =groupby+",x.country";
            	whereCondition = whereCondition+" and x.country='"+selectedCountry+"'";
            }
            if(selectedRegion.length()>0){	
            	sqlFrom = sqlFrom+",x.region" ;
            	groupby =groupby+",x.region";
            	whereCondition = whereCondition+" and x.region='"+selectedRegion+"'";
            }
            if(selectedCity.length()>0){
            	sqlFrom = sqlFrom+",x.city" ;
            	groupby =groupby+",x.city";
            	whereCondition = whereCondition+" and x.city='"+selectedCity+"'";
            }
            if(selectedProduct.length()>0){	
            	sqlFrom = sqlFrom+",x.product_name" ;
            	groupby =groupby+",x.product_name";
            	whereCondition = whereCondition+" and x.product_name='"+selectedProduct+"'";
            }
            if(selectedMedRep.length()>0){
            	sqlFrom = sqlFrom+",x.marketing_staff" ;
            	groupby =groupby+",x.marketing_staff";  
            	whereCondition = whereCondition+" and x.marketing_staff='"+selectedMedRep+"'";
            }
            sqlFrom = sqlFrom+",sum(x.sales_count) as total_sales_count " ;
            selectSQL = sqlFrom;
         
            if(selectedCompany.equalsIgnoreCase("SOLGAR")){
            	
            	if(selectedMedRep.length()>0){
            		selectSQL  =selectSQL + "from( SELECT a.sales_date,a.product_type,a.main_group,a.sub_group, "+
                			"(select country from solgar_tst.solgar_address_group m where m.administrative_area_name = b.administrative_area_name "+
                			"and m.sub_administrative_area_name = b.sub_administrative_area_name) as country,"+
                			"(select region from solgar_tst.solgar_address_group m where m.administrative_area_name = b.administrative_area_name "+
                			"and m.sub_administrative_area_name = b.sub_administrative_area_name) as  region, "+
                			"(select city from solgar_tst.solgar_address_group m where m.administrative_area_name = b.administrative_area_name "+
                			"and m.sub_administrative_area_name = b.sub_administrative_area_name) as  city, "+
                			"(select product_official_name from solgar_tst.sales_product_group where product_type = 'SL' and product_sales_name = a.product_Name) as product_Name,a.sales_count,c.marketing_staff "+
                			"FROM solgar_tst.general_sales_table a , solgar_tst.sales_address_group b, solgar_tst.pharmacy_data_solgar c "+
                			"where  a.sales_reader = b.sales_reader and b.point_x = c.point_x and b.point_y = c.point_y "; 
            	}else{            	
            		selectSQL  =selectSQL + "from( SELECT a.sales_date,a.product_type,a.main_group,a.sub_group, "+
            			"(select country from solgar_tst.solgar_address_group m where m.administrative_area_name = b.administrative_area_name "+
            			"and m.sub_administrative_area_name = b.sub_administrative_area_name) as country,"+
            			"(select region from solgar_tst.solgar_address_group m where m.administrative_area_name = b.administrative_area_name "+
            			"and m.sub_administrative_area_name = b.sub_administrative_area_name) as  region, "+
            			"(select city from solgar_tst.solgar_address_group m where m.administrative_area_name = b.administrative_area_name "+
            			"and m.sub_administrative_area_name = b.sub_administrative_area_name) as  city, "+
            			"(select product_official_name from solgar_tst.sales_product_group where product_type = 'SL' and product_sales_name = a.product_Name) as product_Name,a.sales_count "+
            			"FROM solgar_tst.general_sales_table a , solgar_tst.sales_address_group b where  a.sales_reader = b.sales_reader ";  
            		
            	}           
            	selectSQL = selectSQL+"and a.sales_date >='" +beginDate+"' and a.sales_date <='"+endDate+"' and a.product_type = 'SL' and length (b.full_address)>0)x ";
            }else{
            	if(selectedMedRep.length()>0){
            		selectSQL  =selectSQL + "from( SELECT  a.sales_date,a.product_type,a.main_group,a.sub_group,"+
                			"(select country from solgar_tst.bounty_address_group m where m.administrative_area_name = b.administrative_area_name "+
                			"and m.sub_administrative_area_name = b.sub_administrative_area_name) as country,"+
                			"(select region from solgar_tst.bounty_address_group m where m.administrative_area_name = b.administrative_area_name "+
                			"and m.sub_administrative_area_name = b.sub_administrative_area_name) as  region,"+
                			"(select city from solgar_tst.bounty_address_group m where m.administrative_area_name = b.administrative_area_name "+
                			"and m.sub_administrative_area_name = b.sub_administrative_area_name) as  city,"+
                			"(select product_official_name from solgar_tst.sales_product_group where product_type = 'BN' and product_sales_name = a.product_Name) as product_Name,a.sales_count,c.marketing_staff "+
                			"FROM solgar_tst.general_sales_table a , solgar_tst.sales_address_group b, solgar_tst.pharmacy_data_bounty c "+
                			"where  a.sales_reader = b.sales_reader and b.point_x = c.point_x and b.point_y = c.point_y ";
            	}else{
            		selectSQL  =selectSQL + "from( SELECT  a.sales_date,a.product_type,a.main_group,a.sub_group,"+
            			"(select country from solgar_tst.bounty_address_group m where m.administrative_area_name = b.administrative_area_name "+
            			"and m.sub_administrative_area_name = b.sub_administrative_area_name) as country,"+
            			"(select region from solgar_tst.bounty_address_group m where m.administrative_area_name = b.administrative_area_name "+
            			"and m.sub_administrative_area_name = b.sub_administrative_area_name) as  region,"+
            			"(select city from solgar_tst.bounty_address_group m where m.administrative_area_name = b.administrative_area_name "+
            			"and m.sub_administrative_area_name = b.sub_administrative_area_name) as  city,"+
            			"(select product_official_name from solgar_tst.sales_product_group where product_type = 'BN' and product_sales_name = a.product_Name) as product_Name,a.sales_count "+
            			"FROM solgar_tst.general_sales_table a , solgar_tst.sales_address_group b where  a.sales_reader = b.sales_reader ";  
            	}            
            	selectSQL = selectSQL+"and a.sales_date >='" +beginDate+"' and a.sales_date <='"+endDate+"' and a.product_type = 'BN' and length (b.full_address)>0)x ";
            }
            selectSQL  =selectSQL + whereCondition + groupby;
            
            stmt = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectSQL);

            tableModel = new DefaultTableModel();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
                tableModel.addColumn(metaData.getColumnLabel(columnIndex));
            }
            Object[] row = new Object[columnCount];
            
            while (rs.next()){
                for (int i = 0; i < columnCount; i++){
                    row[i] = rs.getObject(i+1);
                }
                tableModel.addRow(row);
            }

            //Now add that table model to your table and you are done :D
            resultTable.setModel(tableModel); 

        }
        catch(SQLException sqle){       	
            System.out.println("SQL Exception: " + sqle.getMessage());
        }finally{
        	if (stmt != null) {
        		stmt.close();
			}

			if (conn != null) {
				conn.close();
			}
        }
        
        return tableModel;
        
    }
	
	public static DefaultTableModel repGetSaleAnnually(JTable resultTable,String reportName,String beginDate,String endDate,String chain,String selectedCompany) throws Exception {

        Connection conn =null;
        Statement stmt =null;
        String selectSQL ="";
        DefaultTableModel tableModel =null;      

        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));
            selectSQL = getQueryScript(reportName);
           
            String whereCondition = " where sales_date >= '20160101'";
            String groupBy = " group by main_group,product_type";
            
            if(chain.length()>0){
            	whereCondition = whereCondition+" and main_group='"+chain+"'";
            }
            if(selectedCompany.length()>0){
            	whereCondition = whereCondition+" and product_type='"+selectedCompany+"'";
            }
            
            selectSQL = selectSQL+whereCondition+groupBy;
  
            stmt = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectSQL);

            tableModel = new DefaultTableModel();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
                tableModel.addColumn(metaData.getColumnLabel(columnIndex));
            }
            Object[] row = new Object[columnCount];
            
            while (rs.next()){
                for (int i = 0; i < columnCount; i++){
                    row[i] = rs.getObject(i+1);
                }
                tableModel.addRow(row);
            }

            //Now add that table model to your table and you are done :D
            resultTable.setModel(tableModel); 

        }
        catch(SQLException sqle){       	
            System.out.println("SQL Exception: " + sqle.getMessage());
        }finally{
        	if (stmt != null) {
        		stmt.close();
			}

			if (conn != null) {
				conn.close();
			}
        }
        
        return tableModel;
        
    }
	
	public static ESIBag repGetDeliverationStatus(String selectedCompany,String selectedDate,String reportName) throws Exception {
        Connection conn =null;
        PreparedStatement preparedStatement=null;
        String selectSQL ="";
        ESIBag outBag = new ESIBag();     

        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));
            
            selectSQL = getQueryScript(reportName);
            
            if(selectedCompany.length()>0 && selectedDate.length()>0){           	            	
            	preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
            	preparedStatement.setDate(1, Date.valueOf(selectedDate));
            	preparedStatement.setString(2, selectedCompany);
            }else if(selectedCompany.length()>0){
            	selectSQL = selectSQL.replace("c.report_date = ? and", "");     
            	preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
            	preparedStatement.setString(1, selectedCompany);
            }else if(selectedDate.length()>0){
            	selectSQL = selectSQL.replace("and a.group_company = ?", "");
            	preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
            	preparedStatement.setDate(1, Date.valueOf(selectedDate));
            }        	

            ResultSet rs = preparedStatement.executeQuery();
            
            int i =0;
            while (rs.next()){    	
                outBag.put("TABLE",i,"MAINGROUP", rs.getString("group_company"));
                outBag.put("TABLE",i,"SALESCOUNT", rs.getString("sold_boxes"));
                outBag.put("TABLE",i,"SALESDATE", String.valueOf(rs.getDate("report_date")));
                i++;
            } 

        }
        catch(SQLException sqle){       	
            System.out.println("SQL Exception: " + sqle.getMessage());
        }finally{
        	if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (conn != null) {
				conn.close();
			}
        }

        return outBag;
        
    }
	
	public static boolean controlLoadSales(String companyType,String salesDate,String product,String salesReader) throws Exception {
        Connection conn =null;
        PreparedStatement preparedStatement=null;
        String selectSQL ="";
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));                  	
            selectSQL = "select *  from solgar_tst.general_sales_table where main_group = ? and sales_date = ? and product_name = ? and sales_reader = ?";
            preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
            preparedStatement.setString(1, companyType);
            preparedStatement.setDate(2, Date.valueOf(salesDate));
            preparedStatement.setString(3, product);
            preparedStatement.setString(4,salesReader);
            
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){    	
                return true;
            } 

        }
        catch(SQLException sqle){       	
            System.out.println("SQL Exception: " + sqle.getMessage());
        }finally{
        	if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (conn != null) {
				conn.close();
			}
        }

        return false;
        
    }
	
	public static DefaultTableModel repStorageStockResult(JTable resultTable,String beginDate,String endDate,
			String selectedCompany,String selectedDistrict,String selectedRegion,String selectedCity,String selectedProduct,String selectedOpType) throws Exception {
        Connection conn =null;
        Statement stmt =null;
        String selectSQL ="";
        DefaultTableModel tableModel =null;      

        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));        
            String sqlFrom ="select a.optype as operation_type,a.rep_begin_date,a.rep_end_date";
            String groupby ="group by a.optype,a.rep_begin_date,a.rep_end_date";
            String whereCondition =" where a.city = b.stock_city and a.rep_begin_date >="+"'"+beginDate+"'"+" and a.rep_end_date<='"+endDate+"'"+" and a.optype ='"+selectedOpType+"'" ;
            
            if(selectedCompany.length()>0){	
            	sqlFrom = sqlFrom+",a.distributor" ;
            	groupby =groupby+",a.distributor";
            	whereCondition = whereCondition+" and a.distributor='"+selectedCompany+"'";
            }
            if(selectedDistrict.length()>0){
            	sqlFrom = sqlFrom+",b.district" ;
            	groupby =groupby+",b.district";
            	whereCondition = whereCondition+" and b.district='"+selectedDistrict+"'";
            }
            if(selectedRegion.length()>0){	
            	sqlFrom = sqlFrom+",b.region" ;
            	groupby =groupby+",b.region";
            	whereCondition = whereCondition+" and b.region='"+selectedRegion+"'";
            }
            if(selectedCity.length()>0){
            	sqlFrom = sqlFrom+",b.city" ;
            	groupby =groupby+",b.city";
            	whereCondition = whereCondition+" and b.city='"+selectedCity+"'";
            }
            if(selectedProduct.length()>0){	
            	sqlFrom = sqlFrom+",a.product_type" ;
            	groupby =groupby+",a.product_type";
            	whereCondition = whereCondition+" and a.product_type='"+selectedProduct+"'";
            }
            
            sqlFrom = sqlFrom+",sum(a.count) as total_sales_count " ;
            selectSQL = sqlFrom;
         
    		selectSQL  =selectSQL + "from solgar_stk.stock_sales_table a , solgar_stk.stock_address_group b "; 
            	
            selectSQL  =selectSQL + whereCondition + groupby;
            
            stmt = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectSQL);

            tableModel = new DefaultTableModel();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
                tableModel.addColumn(metaData.getColumnLabel(columnIndex));
            }
            Object[] row = new Object[columnCount];
            
            while (rs.next()){
                for (int i = 0; i < columnCount; i++){
                    row[i] = rs.getObject(i+1);
                }
                tableModel.addRow(row);
            }

            //Now add that table model to your table and you are done :D
            resultTable.setModel(tableModel); 

        }
        catch(SQLException sqle){       	
            System.out.println("SQL Exception: " + sqle.getMessage());
        }finally{
        	if (stmt != null) {
        		stmt.close();
			}

			if (conn != null) {
				conn.close();
			}
        }
        
        return tableModel;
        
    }
	public static DefaultTableModel repStorageStockMonthly(JTable resultTable, String beginDate, String endDate, String selectedCompany, String selectedDistrict, 
			String selectedRegion, String selectedCity, String selectedBrand, String selectedOpType, String selectedPrdMain, 
			String selectedPrdSub, String selectedProduct,String repType,String country,String ctgry1,String ctgry2)
		    throws Exception
		  {
		    Connection conn = null;
		    Statement stmt = null;
		    String selectSQL = "";
		    DefaultTableModel tableModel = null;
		    boolean quarter = false;
		    String fromCondition = "";
		    String groupby = "";
		    try
		    {
	    	
		      conn = (Connection)DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));
		      if(selectedOpType.equalsIgnoreCase("SALES")){
		    	  selectSQL = getQueryScript("STOCK_MONTHLY_ALL");
		    	  fromCondition = distributorSalesFromMonthly(beginDate, endDate, fromCondition);
		    	  selectSQL=fromCondition+selectSQL;
		    	  fromCondition="";
		      }
 
		      if(repType.equalsIgnoreCase("DISTRUBUTOR_REPORT")){
		    	  fromCondition = "select a.optype as operation_type,a.distributor,";
		    	  groupby = " group by a.optype,a.distributor";
		      }else if(repType.equalsIgnoreCase("REGIONAL_REPORT")){
		    	  fromCondition = fromCondition + "select a.optype as operation_type,b.region,";
			      groupby = groupby + " group by a.optype,b.region";
		      }else if(repType.equalsIgnoreCase("PRODUCT_REPORT")){
		    	  fromCondition = fromCondition + "select a.optype as operation_type,c.product_main_group,";
			      groupby = groupby + " group by a.optype,c.product_main_group";
		      }else if(repType.equalsIgnoreCase("PRODUCT_SUB_GROUP_REPORT")){
		    	  fromCondition = fromCondition + "select a.optype as operation_type,c.product_sub_group,";
			      groupby = groupby + " group by a.optype,c.product_sub_group";
		      }else if(repType.equalsIgnoreCase("TOP_PRODUCTS_REPORT")){
		    	  fromCondition = fromCondition + "select a.optype as operation_type,c.product_category,c.product_official_name,";
			      groupby = groupby + " group by a.optype,c.product_category,c.product_official_name";
		      }else if(repType.equalsIgnoreCase("PROMOTION_PRODUCTS_REPORT")){
		    	  fromCondition = fromCondition + "select a.optype as operation_type,c.product_category1,c.product_official_name,";
			      groupby = groupby + " group by a.optype,c.product_category1,c.product_official_name";
		      }
		      String whereCondition ="";
		      if(selectedOpType.equalsIgnoreCase("STOCK")){
		    	  whereCondition = "from solgar_stk.stock_sales_table a , solgar_stk.stock_address_group b ,solgar_stk.stock_product_group c where a.city = b.stock_city and a.product = c.product_sales_name";
		      }		      
		      whereCondition = whereCondition+" and a.rep_begin_date >='" + beginDate + "'" + " and a.rep_end_date<='" + endDate + "'" + " and a.optype ='" + selectedOpType + "'" + " and b.country ='" + country + "'";

		      if (selectedCompany.length() > 0) {
		    	  fromCondition = fromCondition + "a.distributor,";
			        groupby = groupby + ",a.distributor";
		        whereCondition = whereCondition + " and a.distributor='" + selectedCompany + "'";
		      }
		      if (selectedDistrict.length() > 0) {
		        fromCondition = fromCondition + "b.district,";
		        groupby = groupby + ",b.district";
		        whereCondition = whereCondition + " and b.district='" + selectedDistrict + "'";
		      }
		      if (selectedRegion.length() > 0) {
		        fromCondition = fromCondition + "b.region,";
		        groupby = groupby + ",b.region";
		        whereCondition = whereCondition + " and b.region='" + selectedRegion + "'";
		      }
		      if (selectedCity.length() > 0) {
		        fromCondition = fromCondition + "b.city,";
		        groupby = groupby + ",b.city";
		        whereCondition = whereCondition + " and b.city='" + selectedCity + "'";
		      }
		      if (selectedBrand.length() > 0) {
		        fromCondition = fromCondition + "a.product_type,";
		        groupby = groupby + ",a.product_type";
		        whereCondition = whereCondition + " and a.product_type='" + selectedBrand + "'";
		      }
		      if (selectedPrdMain.length() > 0) {
		    	  fromCondition = fromCondition + "c.product_main_group,";
			        groupby = groupby + ",c.product_main_group";
		        whereCondition = whereCondition + " and c.product_main_group='" + selectedPrdMain + "'";
		      }
		      if (selectedPrdSub.length() > 0) {
		        fromCondition = fromCondition + "c.product_sub_group,";
		        groupby = groupby + ",c.product_sub_group";
		        whereCondition = whereCondition + " and c.product_sub_group='" + selectedPrdSub + "'";
		      }
		      if (selectedProduct.length() > 0) {
		        fromCondition = fromCondition + "c.product_official_name,";
		        groupby = groupby + ",c.product_official_name";
		        whereCondition = whereCondition + " and c.product_official_name='" + selectedProduct + "'";
		      }
		      if (ctgry1.length() > 0) {
			        fromCondition = fromCondition + "c.product_category,c.product_official_name,";
			        groupby = groupby + ",c.product_category,c.product_official_name";
			        whereCondition = whereCondition + " and c.product_category='" + ctgry1 + "'";
			  }else if(repType.equalsIgnoreCase("TOP_PRODUCTS_REPORT")){
				  whereCondition = whereCondition + " and c.product_category='" + "TOP" + "'";
			  }
		      if (ctgry2.length() > 0) {
		    	  fromCondition = fromCondition + "c.product_category1,c.product_official_name,";
			        groupby = groupby + ",c.product_category1,c.product_official_name";
			        whereCondition = whereCondition + " and c.product_category1='" + ctgry2 + "'";
			  }else if(repType.equalsIgnoreCase("PROMOTION_PRODUCTS_REPORT")){
				  if(selectedBrand.equalsIgnoreCase("BN")){
					  whereCondition = whereCondition + " and c.product_category1='" + "NEW" + "'";
				  }else{
					  whereCondition = whereCondition + " and c.product_category1='" + "JOINT" + "'";  
				  }
			  }
		      if(selectedOpType.equalsIgnoreCase("STOCK")){
		    	  fromCondition = storageWeeklyFromCnd(beginDate, endDate, fromCondition);
		    	  fromCondition = fromCondition + "sum(a.count) as TOTAL ";
		      }
		      
		      groupby = groupby + " order by total desc";
		      selectSQL = fromCondition + selectSQL + whereCondition + groupby;

		      stmt = (Statement)conn.createStatement();
		      ResultSet rs = stmt.executeQuery(selectSQL);

		      tableModel = new DefaultTableModel();
		      ResultSetMetaData metaData = rs.getMetaData();
		      int columnCount = metaData.getColumnCount();
		      for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
		        tableModel.addColumn(metaData.getColumnLabel(columnIndex));
		      }
		      Object[] row = new Object[columnCount];

		      while (rs.next()) {
		        for (int i = 0; i < columnCount; i++) {
		          row[i] = rs.getObject(i + 1);
		        }
		        tableModel.addRow(row);
		      }

		      resultTable.setModel(tableModel);
		    }
		    catch (SQLException sqle)
		    {
		      System.out.println("SQL Exception: " + sqle.getMessage());
		    } finally {
		      if (stmt != null) {
		        stmt.close();
		      }

		      if (conn != null) {
		        conn.close();
		      }
		    }

		    return tableModel;
		  }
	
	private static String distributorSalesFromMonthly(String beginDate, String endDate, String fromCondition) {
		SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		Date a = Date.valueOf(endDate.substring(0, 4)+"-"+endDate.substring(4, 6)+"-"+endDate.substring(6, 8));
		c1.setTime(a);
		 
		Calendar c2 = Calendar.getInstance();
		Date c = Date.valueOf(beginDate.substring(0, 4)+"-"+beginDate.substring(4, 6)+"-"+beginDate.substring(6, 8));
		c2.setTime(c);
		 
		while(c1.after(c2)) {    			    	
	    	String selectedDate = c2.getTime().toString();//2016-01-01 Mon Feb 27 00:00:00 MSK 2017
	    	String year = selectedDate.substring(selectedDate.length()-4, selectedDate.length());
	    	String month = selectedDate.substring(4, 7);
	    	String day = selectedDate.substring(8, 10);
	    	String monthStr = "01";
	    	if(month.equalsIgnoreCase("Jan")){monthStr="01";}
	    	else if(month.equalsIgnoreCase("Feb")){monthStr="02";}
	    	else if(month.equalsIgnoreCase("Mar")){monthStr="03";}
	    	else if(month.equalsIgnoreCase("Apr")){monthStr="04";}
	    	else if(month.equalsIgnoreCase("May")){monthStr="05";}
	    	else if(month.equalsIgnoreCase("Jun")){monthStr="06";}
	    	else if(month.equalsIgnoreCase("Jul")){monthStr="07";}
	    	else if(month.equalsIgnoreCase("Aug")){monthStr="08";}
	    	else if(month.equalsIgnoreCase("Sep")){monthStr="09";}
	    	else if(month.equalsIgnoreCase("Oct")){monthStr="10";}
	    	else if(month.equalsIgnoreCase("Nov")){monthStr="11";}
	    	else if(month.equalsIgnoreCase("Dec")){monthStr="12";}
	    	Date d = Date.valueOf(year+"-"+monthStr+"-"+day);	    		    	
	    	
	    	Calendar n = Calendar.getInstance();
			Date l = Date.valueOf(String.valueOf(d));
			n.setTime(l);			
			n.add(Calendar.MONTH,1);
			String selectedDate1 = n.getTime().toString();
			String year1 = selectedDate1.substring(selectedDate1.length()-4, selectedDate1.length());
	    	String month1 = selectedDate1.substring(4, 7);
	    	String day1 = selectedDate1.substring(8, 10);	 
	    	
	    	String monthStr1 = "01";
			if(month1.equalsIgnoreCase("Jan")){monthStr1="01";}
	    	else if(month1.equalsIgnoreCase("Feb")){monthStr1="02";}
	    	else if(month1.equalsIgnoreCase("Mar")){monthStr1="03";}
	    	else if(month1.equalsIgnoreCase("Apr")){monthStr1="04";}
	    	else if(month1.equalsIgnoreCase("May")){monthStr1="05";}
	    	else if(month1.equalsIgnoreCase("Jun")){monthStr1="06";}
	    	else if(month1.equalsIgnoreCase("Jul")){monthStr1="07";}
	    	else if(month1.equalsIgnoreCase("Aug")){monthStr1="08";}
	    	else if(month1.equalsIgnoreCase("Sep")){monthStr1="09";}
	    	else if(month1.equalsIgnoreCase("Oct")){monthStr1="10";}
	    	else if(month1.equalsIgnoreCase("Nov")){monthStr1="11";}
	    	else if(month1.equalsIgnoreCase("Dec")){monthStr1="12";}
			Date e = Date.valueOf(year1+"-"+monthStr1+"-"+day1);
		    
			String a1="sum(case when substring(rep_begin_date,1,4)='"+year+"' THEN (case when substring(rep_begin_date,6,2) ='"+monthStr+"' then a.count else 0 end ) else 0 END) as "+year+"_"+month+",";				
			
			fromCondition = fromCondition+a1;
		    
		    c2.add(Calendar.MONTH,1);
		}
			
		return fromCondition;
	}

	private static String storageWeeklyFromCnd(String beginDate, String endDate, String fromCondition) {
		SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		Date a = Date.valueOf(endDate.substring(0, 4)+"-"+endDate.substring(4, 6)+"-"+endDate.substring(6, 8));
		c1.setTime(a);
		 
		Calendar c2 = Calendar.getInstance();
		Date c = Date.valueOf(beginDate.substring(0, 4)+"-"+beginDate.substring(4, 6)+"-"+beginDate.substring(6, 8));
		c2.setTime(c);
		 
		while(c1.after(c2)) {
		    if(c2.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY){	    			    	
		    	String selectedDate = c2.getTime().toString();//2016-01-01 Mon Feb 27 00:00:00 MSK 2017
		    	String year = selectedDate.substring(selectedDate.length()-4, selectedDate.length());
		    	String month = selectedDate.substring(4, 7);
		    	String day = selectedDate.substring(8, 10);
		    	String monthStr = "01";
		    	if(month.equalsIgnoreCase("Jan")){monthStr="01";}
		    	else if(month.equalsIgnoreCase("Feb")){monthStr="02";}
		    	else if(month.equalsIgnoreCase("Mar")){monthStr="03";}
		    	else if(month.equalsIgnoreCase("Apr")){monthStr="04";}
		    	else if(month.equalsIgnoreCase("May")){monthStr="05";}
		    	else if(month.equalsIgnoreCase("Jun")){monthStr="06";}
		    	else if(month.equalsIgnoreCase("Jul")){monthStr="07";}
		    	else if(month.equalsIgnoreCase("Aug")){monthStr="08";}
		    	else if(month.equalsIgnoreCase("Sep")){monthStr="09";}
		    	else if(month.equalsIgnoreCase("Oct")){monthStr="10";}
		    	else if(month.equalsIgnoreCase("Nov")){monthStr="11";}
		    	else if(month.equalsIgnoreCase("Dec")){monthStr="12";}
		    	Date d = Date.valueOf(year+"-"+monthStr+"-"+day);	    		    	
		    	
		    	Calendar n = Calendar.getInstance();
				Date l = Date.valueOf(String.valueOf(d));
				n.setTime(l);			
				n.add(Calendar.DATE, 6);
				String selectedDate1 = n.getTime().toString();
				String year1 = selectedDate1.substring(selectedDate1.length()-4, selectedDate1.length());
		    	String month1 = selectedDate1.substring(4, 7);
		    	String day1 = selectedDate1.substring(8, 10);
				
				if(month1.equalsIgnoreCase("Jan")){month1="01";}
		    	else if(month1.equalsIgnoreCase("Feb")){month1="02";}
		    	else if(month1.equalsIgnoreCase("Mar")){month1="03";}
		    	else if(month1.equalsIgnoreCase("Apr")){month1="04";}
		    	else if(month1.equalsIgnoreCase("May")){month1="05";}
		    	else if(month1.equalsIgnoreCase("Jun")){month1="06";}
		    	else if(month1.equalsIgnoreCase("Jul")){month1="07";}
		    	else if(month1.equalsIgnoreCase("Aug")){month1="08";}
		    	else if(month1.equalsIgnoreCase("Sep")){month1="09";}
		    	else if(month1.equalsIgnoreCase("Oct")){month1="10";}
		    	else if(month1.equalsIgnoreCase("Nov")){month1="11";}
		    	else if(month1.equalsIgnoreCase("Dec")){month1="12";}
				Date e = Date.valueOf(year1+"-"+month1+"-"+day1);	    			    	
		    	String a1="";
				String b1="";
				
				if(monthStr.equalsIgnoreCase(month1)){						
					a1="sum(case when substring(rep_begin_date,6,2) = '"+monthStr+"' THEN (case when substring(rep_begin_date,9,2) between "+day+" and " +day1+" then count else 0 end ) else 0 END) as "+month+"_"+ day+",";
				}else{
					a1="sum(case when substring(rep_begin_date,6,2) = '"+monthStr+"' THEN (case when substring(rep_begin_date,9,2) between "+day+" and "+"31"+" then count else 0 end ) else 0 END) as "+month+"_"+ day+",";
					b1="sum(case when substring(rep_begin_date,6,2) = '"+monthStr+"' THEN (case when substring(rep_begin_date,9,2) between "+"1"+" and "+day1+" then count else 0 end ) else 0 END) as "+month+"_"+ "1,";
				}
				
				fromCondition = fromCondition+a1+b1;	    	
		    }
		    c2.add(Calendar.DATE,1);
		}
		return fromCondition;
	}
	public static DefaultTableModel repGetWeeklyTrainings(JTable resultTable,String selectedCompany,String reportName) throws Exception {
        Connection conn =null;
        PreparedStatement preparedStatement=null;
        String selectSQL ="";
        DefaultTableModel tableModel =null;     

        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));
            
            selectSQL = getQueryScript(reportName);           
            preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
            preparedStatement.setString(1, selectedCompany);
            ResultSet rs = preparedStatement.executeQuery();
            
            tableModel = new DefaultTableModel();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
                tableModel.addColumn(metaData.getColumnLabel(columnIndex));
            }
            Object[] row = new Object[columnCount];
            
            while (rs.next()){
                for (int i = 0; i < columnCount; i++){
                    row[i] = rs.getObject(i+1);
                }
                tableModel.addRow(row);
            }

            //Now add that table model to your table and you are done :D
            resultTable.setModel(tableModel); 

        }
        catch(SQLException sqle){       	
            System.out.println("SQL Exception: " + sqle.getMessage());
        }finally{
        	if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (conn != null) {
				conn.close();
			}
        }

        return tableModel;
        
    }

	
	public static String getQueryScript(String queryName) throws Exception {
		PreparedStatement preparedStatement=null;
        Connection conn =null;
        String queryScript = "";
        
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));           
            String sorgu  = "select * from solgar_gen.dad_queries where query_name = ?";
            preparedStatement = (PreparedStatement)conn.prepareStatement(sorgu);
        	preparedStatement.setString(1, queryName);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
            	queryScript = rs.getString("query_script");
            }
 
        }
        catch(SQLException sqle){       	
            System.out.println("SQL Exception: " + sqle.getMessage());
        }finally{
        	if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (conn != null) {
				conn.close();
			}
        }
        return queryScript;
    }
	public static DefaultTableModel repMarkChainSales(JTable resultTable,String repType,String compType,String beginDateYear,String beginDateMonth,String endDateYear,String endDateMonth,
			String selectedChain,String Country,String Area,String Region,String City,String medRep,String productMainGroup,String productSubGroup,String product) throws Exception {
        Connection conn =null;
        Statement stmt =null;
        String selectSQL ="";
        DefaultTableModel tableModel =null;  
        boolean quarter = false;

        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));     
        	
        	if(beginDateMonth.equalsIgnoreCase("QUARTERLY")){
        		selectSQL = getQueryScript("MARK_"+repType+"_QUARTERLY");
        		quarter = true;
        	}else{
        		selectSQL = getQueryScript("MARK_"+repType);
        	}       		

            String groupby =" group by main_group order by total desc";
            String whereCondition =" " ;
            
            if(selectedChain.length()>0){	
            	whereCondition = whereCondition+" and main_group="+"'"+selectedChain+"'";
            }
            
            if(!quarter){
	            if(beginDateYear.length()>0 && beginDateMonth.length()>0){	
	            	whereCondition = whereCondition+" and replace(sales_date,'-','') >="+beginDateYear+beginDateMonth+"01";
	            }
	            if(endDateYear.length()>0 && endDateMonth.length()>0){	
	            	whereCondition = whereCondition+" and replace(sales_date,'-','') <="+endDateYear+endDateMonth+"01";
	            }
            }else{
            	if(beginDateYear.equalsIgnoreCase("2016")){
            		whereCondition = whereCondition+" and replace(sales_date,'-','') >= 20160101 and replace(sales_date,'-','') <= 20161201";
            	}else if(beginDateYear.equalsIgnoreCase("2017")){
            		whereCondition = whereCondition+" and replace(sales_date,'-','') >= 20170101 and replace(sales_date,'-','') <= 20171201";
            	}
            }
         	
            selectSQL  =selectSQL + whereCondition + groupby;
            
            stmt = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectSQL);

            tableModel = new DefaultTableModel();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
                tableModel.addColumn(metaData.getColumnLabel(columnIndex));
            }
            Object[] row = new Object[columnCount];
            
            while (rs.next()){
                for (int i = 0; i < columnCount; i++){
                    row[i] = rs.getObject(i+1);
                }
                tableModel.addRow(row);
            }

            //Now add that table model to your table and you are done :D
            resultTable.setModel(tableModel); 

        }
        catch(SQLException sqle){       	
            System.out.println("SQL Exception: " + sqle.getMessage());
        }finally{
        	if (stmt != null) {
        		stmt.close();
			}

			if (conn != null) {
				conn.close();
			}
        }
        
        return tableModel;
        
    }
	public static DefaultTableModel repMarkRegionalSales(JTable resultTable,String repType,String compType,String beginDateYear,String beginDateMonth,String endDateYear,String endDateMonth,
			String selectedChain,String Country,String Area,String Region,String City,String medRep,String productMainGroup,String productSubGroup,String product) throws Exception {
		Connection conn =null;
        Statement stmt =null;
        String selectSQL ="";
        DefaultTableModel tableModel =null;  
        boolean quarter = false;

        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));     
        	
        	if(beginDateMonth.equalsIgnoreCase("QUARTERLY")){
        		selectSQL = getQueryScript("MARK_"+repType+"_QUARTERLY");
        		quarter = true;
        	}else{
        		selectSQL = getQueryScript("MARK_"+repType);
        	}       		
        	
        	String fromCondition = "select 'SOLGAR' as PRODUCT,country as AREA,";

            String groupby =" group by country";
            String whereCondition =" " ;
            
            if(Area.length()>0){	
            	whereCondition = whereCondition+" and country="+"'"+Area+"'";
            }
            if(Region.length()>0){	
            	whereCondition = whereCondition+" and region="+"'"+Region+"'";
            	 groupby = groupby+",region";
            	 fromCondition=fromCondition+" region as REGION,";
            }
            if(City.length()>0){	
            	whereCondition = whereCondition+" and city="+"'"+City+"'";
            	 groupby = groupby+",city";
            	 fromCondition=fromCondition+" city as CITY,";
            }
            groupby = groupby +" order by total desc";
            
            if(!quarter){
	            if(beginDateYear.length()>0 && beginDateMonth.length()>0){	
	            	whereCondition = whereCondition+" and replace(sales_date,'-','') >="+beginDateYear+beginDateMonth+"01";
	            }
	            if(endDateYear.length()>0 && endDateMonth.length()>0){	
	            	whereCondition = whereCondition+" and replace(sales_date,'-','') <="+endDateYear+endDateMonth+"01";
	            }
            }else{
            	if(beginDateYear.equalsIgnoreCase("2016")){
            		whereCondition = whereCondition+" and replace(sales_date,'-','') >= 20160101 and replace(sales_date,'-','') <= 20161201";
            	}else if(beginDateYear.equalsIgnoreCase("2017")){
            		whereCondition = whereCondition+" and replace(sales_date,'-','') >= 20170101 and replace(sales_date,'-','') <= 20171201";
            	}
            }
         	
            selectSQL  =fromCondition+selectSQL + whereCondition + groupby;
            
            stmt = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectSQL);

            tableModel = new DefaultTableModel();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
                tableModel.addColumn(metaData.getColumnLabel(columnIndex));
            }
            Object[] row = new Object[columnCount];
            
            while (rs.next()){
                for (int i = 0; i < columnCount; i++){
                    row[i] = rs.getObject(i+1);
                }
                tableModel.addRow(row);
            }

            //Now add that table model to your table and you are done :D
            resultTable.setModel(tableModel); 

        }
        catch(SQLException sqle){       	
            System.out.println("SQL Exception: " + sqle.getMessage());
        }finally{
        	if (stmt != null) {
        		stmt.close();
			}

			if (conn != null) {
				conn.close();
			}
        }
        
        return tableModel;
        
    }
	public static DefaultTableModel repMarkMedRepSales(JTable resultTable,String repType,String compType,String beginDateYear,String beginDateMonth,String endDateYear,String endDateMonth,
			String selectedChain,String Country,String Area,String Region,String City,String medRep,String productMainGroup,String productSubGroup,String product) throws Exception {
        Connection conn =null;
        Statement stmt =null;
        String selectSQL ="";
        DefaultTableModel tableModel =null;  
        boolean quarter = false;

        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));     
        	
        	if(beginDateMonth.equalsIgnoreCase("QUARTERLY")){
        		selectSQL = getQueryScript("MARK_"+repType+"_QUARTERLY");
        		quarter = true;
        	}else{
        		selectSQL = getQueryScript("MARK_"+repType);
        	}       		

            String groupby =" group by marketing_staff order by total desc";
            String whereCondition =" " ;
        
            if(medRep.length()>0){	
            	whereCondition = whereCondition+" and marketing_staff="+"'"+medRep+"'";
            }
           
            
            if(!quarter){
	            if(beginDateYear.length()>0 && beginDateMonth.length()>0){	
	            	whereCondition = whereCondition+" and replace(sales_date,'-','') >="+beginDateYear+beginDateMonth+"01";
	            }
	            if(endDateYear.length()>0 && endDateMonth.length()>0){	
	            	whereCondition = whereCondition+" and replace(sales_date,'-','') <="+endDateYear+endDateMonth+"01";
	            }
            }else{
            	if(beginDateYear.equalsIgnoreCase("2016")){
            		whereCondition = whereCondition+" and replace(sales_date,'-','') >= 20160101 and replace(sales_date,'-','') <= 20161201";
            	}else if(beginDateYear.equalsIgnoreCase("2017")){
            		whereCondition = whereCondition+" and replace(sales_date,'-','') >= 20170101 and replace(sales_date,'-','') <= 20171201";
            	}
            }
         	
            selectSQL  =selectSQL + whereCondition + groupby;
            
            stmt = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectSQL);

            tableModel = new DefaultTableModel();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
                tableModel.addColumn(metaData.getColumnLabel(columnIndex));
            }
            Object[] row = new Object[columnCount];
            
            while (rs.next()){
                for (int i = 0; i < columnCount; i++){
                    row[i] = rs.getObject(i+1);
                }
                tableModel.addRow(row);
            }

            //Now add that table model to your table and you are done :D
            resultTable.setModel(tableModel); 

        }
        catch(SQLException sqle){       	
            System.out.println("SQL Exception: " + sqle.getMessage());
        }finally{
        	if (stmt != null) {
        		stmt.close();
			}

			if (conn != null) {
				conn.close();
			}
        }
        
        return tableModel;
        
    }
	public static DefaultTableModel repMarkProductSales(JTable resultTable,String repType,String compType,String beginDateYear,String beginDateMonth,String endDateYear,String endDateMonth,
			String selectedChain,String Country,String Area,String Region,String City,String medRep,String productMainGroup,String productSubGroup,String product) throws Exception {
        Connection conn =null;
        Statement stmt =null;
        String selectSQL ="";
        DefaultTableModel tableModel =null;  
        boolean quarter = false;

        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));     
        	
        	if(beginDateMonth.equalsIgnoreCase("QUARTERLY")){
        		selectSQL = getQueryScript("MARK_"+repType+"_QUARTERLY");
        		quarter = true;
        	}else{
        		selectSQL = getQueryScript("MARK_"+repType);
        	}       		
        	
        	String fromCondition = "select 'SOLGAR' as PRODUCT,product_main_group as PRODUCT_MAIN_GROUP,";

            String groupby =" group by product_main_group";
            String whereCondition =" " ;
            
            if(productMainGroup.length()>0){	
            	whereCondition = whereCondition+" and product_main_group="+"'"+productMainGroup+"'";
            }
            if(productSubGroup.length()>0){	
            	whereCondition = whereCondition+" and product_sub_group="+"'"+productSubGroup+"'";
            	 groupby = groupby+",product_sub_group";
            	 fromCondition=fromCondition+" product_sub_group as PRODUCT_SUB_GROUP,";
            }
            groupby = groupby +" order by total desc";
            
            if(!quarter){
	            if(beginDateYear.length()>0 && beginDateMonth.length()>0){	
	            	whereCondition = whereCondition+" and replace(sales_date,'-','') >="+beginDateYear+beginDateMonth+"01";
	            }
	            if(endDateYear.length()>0 && endDateMonth.length()>0){	
	            	whereCondition = whereCondition+" and replace(sales_date,'-','') <="+endDateYear+endDateMonth+"01";
	            }
            }else{
            	if(beginDateYear.equalsIgnoreCase("2016")){
            		whereCondition = whereCondition+" and replace(sales_date,'-','') >= 20160101 and replace(sales_date,'-','') <= 20161201";
            	}else if(beginDateYear.equalsIgnoreCase("2017")){
            		whereCondition = whereCondition+" and replace(sales_date,'-','') >= 20170101 and replace(sales_date,'-','') <= 20171201";
            	}
            }
         	
            selectSQL  =fromCondition+selectSQL + whereCondition + groupby;
            
            stmt = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectSQL);

            tableModel = new DefaultTableModel();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
                tableModel.addColumn(metaData.getColumnLabel(columnIndex));
            }
            Object[] row = new Object[columnCount];
            
            while (rs.next()){
                for (int i = 0; i < columnCount; i++){
                    row[i] = rs.getObject(i+1);
                }
                tableModel.addRow(row);
            }

            //Now add that table model to your table and you are done :D
            resultTable.setModel(tableModel); 

        }
        catch(SQLException sqle){       	
            System.out.println("SQL Exception: " + sqle.getMessage());
        }finally{
        	if (stmt != null) {
        		stmt.close();
			}

			if (conn != null) {
				conn.close();
			}
        }
        
        return tableModel;
        
    }
	
	public static DefaultTableModel repExpsManagerial(JTable resultTable,String selectedBeginDate,String selectedEndDate,
			String selectedCountry,String selectedDistrict,String selectedRegion,String selectedCity,
			String selectedChain,String selectedBrand,String repType,String selectedSecondStage,String selectedKOL,String selectedClinic,
			String selectedMedRep,String selectedFromPar,String selectedDatePar) throws Exception {
		        Connection conn =null;
		        Statement stmt =null;
		        String selectSQL ="";
		        DefaultTableModel tableModel =null;  
		
		        try{
		        	 conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));   
		        	 String fromCondition = "";
		             String groupby ="";
		        	 
		        	 if(repType.equalsIgnoreCase("TRAINING (SECOND STAGE)")){
		        		 fromCondition = "select company,second_stage,";
			             groupby =" group by second_stage "; 
		         	}else if(repType.equalsIgnoreCase("MAIN DISTRICT")){
		         		 fromCondition = "select company,area,";
			             groupby =" group by area ";
		           	}else if(repType.equalsIgnoreCase("REGIONS")){
		           		 fromCondition = "select company,region,";
			             groupby =" group by region ";
		             }else if(repType.equalsIgnoreCase("CHAINS")){
		            	 fromCondition = "select company,chain,";
			             groupby =" group by chain ";
		             }else if(repType.equalsIgnoreCase("KOL (GONORAR)")){
		            	 fromCondition = "select company,key_leader,";
			             groupby =" group by key_leader ";
		             }else if(repType.equalsIgnoreCase("MED REPS")){
		            	 fromCondition = "select company,organizer,";
			             groupby =" group by organizer ";
		             }else if(repType.equalsIgnoreCase("CLINICS")){
		            	 fromCondition = "select company,clinic_name,";
			             groupby =" group by clinic_name ";
		             }
		             String whereCondition =" from solgar_mcs.markt_exp_info where approval_status in('1','2') and first_stage = 'Ìונמןנטÿעטÿ' " ;
		             whereCondition = whereCondition+ "and start_date >='"+selectedBeginDate+"' and start_date<='"+selectedEndDate+"'";	             
		        	 
				      if (selectedBrand.length() > 0) {
				    	  fromCondition = fromCondition + "company,";
					      groupby = groupby + ",company";
				          whereCondition = whereCondition + " and company='" + selectedBrand + "'";
				      }	
				      if (selectedChain.length() > 0) {
				    	  fromCondition = fromCondition + "chain,";
					      groupby = groupby + ",chain";
				          whereCondition = whereCondition + " and chain='" + selectedChain + "'";
				      }	
				      if (selectedSecondStage.length() > 0) {
				    	  fromCondition = fromCondition + "second_stage,";
					      groupby = groupby + ",second_stage";
				          whereCondition = whereCondition + " and second_stage='" + selectedSecondStage + "'";
				      }	
				      if (selectedKOL.length() > 0) {
				    	  fromCondition = fromCondition + "key_leader,";
					      groupby = groupby + ",key_leader";
				          whereCondition = whereCondition + " and key_leader='" + selectedKOL + "'";
				      }
				      if (selectedClinic.length() > 0) {
				    	  fromCondition = fromCondition + "clinic_name,";
					      groupby = groupby + ",clinic_name";
				          whereCondition = whereCondition + " and clinic_name='" + selectedClinic + "'";
				      }
				      if (selectedClinic.length() > 0) {
				    	  fromCondition = fromCondition + "clinic_name,";
					      groupby = groupby + ",clinic_name";
				          whereCondition = whereCondition + " and clinic_name='" + selectedClinic + "'";
				      }
				      if (selectedMedRep.length() > 0) {
				    	  fromCondition = fromCondition + "organizer,";
					      groupby = groupby + ",organizer";
				          whereCondition = whereCondition + " and organizer='" + selectedMedRep + "'";
				      }
				      if (selectedDistrict.length() > 0) {
				    	  fromCondition = fromCondition + "area,";
					      groupby = groupby + ",area";
				          whereCondition = whereCondition + " and area='" + selectedDistrict + "'";
				      }
				      if (selectedRegion.length() > 0) {
				    	  fromCondition = fromCondition + "region,";
					      groupby = groupby + ",region";
				          whereCondition = whereCondition + " and region='" + selectedRegion + "'";
				      }
				      if (selectedCity.length() > 0) {
				    	  fromCondition = fromCondition + "city,";
					      groupby = groupby + ",city";
				          whereCondition = whereCondition + " and city='" + selectedCity + "'";
				      }
				      
				      if(selectedDatePar.equalsIgnoreCase("WEEKLY")){
			             fromCondition = expsManParFromWeekly(selectedBeginDate, selectedEndDate, fromCondition, selectedFromPar);
			          }
				      if(selectedDatePar.equalsIgnoreCase("MONTHLY")){
				             fromCondition = expsManParFromMonthly(selectedBeginDate, selectedEndDate, fromCondition, selectedFromPar);
				          }
				      if(selectedDatePar.equalsIgnoreCase("QUARTERLY")){
				             fromCondition = expsManParFromQuarterly(selectedBeginDate, selectedEndDate, fromCondition, selectedFromPar);
				          }
				      groupby= groupby+" order by total desc";
	             
		             selectSQL = fromCondition+whereCondition+groupby;
		        	 
		        	  stmt = (Statement) conn.createStatement();
		              ResultSet rs = stmt.executeQuery(selectSQL);
		              
		              tableModel = new DefaultTableModel();
		              ResultSetMetaData metaData = rs.getMetaData();
		              int columnCount = metaData.getColumnCount();
		              for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
		                  tableModel.addColumn(metaData.getColumnLabel(columnIndex));
		              }
		              Object[] row = new Object[columnCount];
		              
		              while (rs.next()){
		                  for (int i = 0; i < columnCount; i++){
		                      row[i] = rs.getObject(i+1);
		                  }
		                  tableModel.addRow(row);
		              }
		              resultTable.setModel(tableModel);
		        	
		        }
		        catch(SQLException sqle){       	
		            System.out.println("SQL Exception: " + sqle.getMessage());
		        }finally{
		        	if (stmt != null) {
		        		stmt.close();
					}
		
					if (conn != null) {
						conn.close();
					}
		        
		        }
        return tableModel;
    
	}
	private static String expsManParFromWeekly(String beginDate, String endDate, String fromCondition,String selectedFromPar) {
		SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		Date a = Date.valueOf(endDate.substring(0, 4)+"-"+endDate.substring(4, 6)+"-"+endDate.substring(6, 8));
		c1.setTime(a);
		 
		Calendar c2 = Calendar.getInstance();
		Date c = Date.valueOf(beginDate.substring(0, 4)+"-"+beginDate.substring(4, 6)+"-"+beginDate.substring(6, 8));
		c2.setTime(c);
		 
		while(c1.after(c2)) {
		    if(c2.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY){	    			    	
		    	String selectedDate = c2.getTime().toString();//2016-01-01 Mon Feb 27 00:00:00 MSK 2017
		    	String year = selectedDate.substring(selectedDate.length()-4, selectedDate.length());
		    	String month = selectedDate.substring(4, 7);
		    	String day = selectedDate.substring(8, 10);
		    	String monthStr = "01";
		    	if(month.equalsIgnoreCase("Jan")){monthStr="01";}
		    	else if(month.equalsIgnoreCase("Feb")){monthStr="02";}
		    	else if(month.equalsIgnoreCase("Mar")){monthStr="03";}
		    	else if(month.equalsIgnoreCase("Apr")){monthStr="04";}
		    	else if(month.equalsIgnoreCase("May")){monthStr="05";}
		    	else if(month.equalsIgnoreCase("Jun")){monthStr="06";}
		    	else if(month.equalsIgnoreCase("Jul")){monthStr="07";}
		    	else if(month.equalsIgnoreCase("Aug")){monthStr="08";}
		    	else if(month.equalsIgnoreCase("Sep")){monthStr="09";}
		    	else if(month.equalsIgnoreCase("Oct")){monthStr="10";}
		    	else if(month.equalsIgnoreCase("Nov")){monthStr="11";}
		    	else if(month.equalsIgnoreCase("Dec")){monthStr="12";}
		    	Date d = Date.valueOf(year+"-"+monthStr+"-"+day);	    		    	
		    	
		    	Calendar n = Calendar.getInstance();
				Date l = Date.valueOf(String.valueOf(d));
				n.setTime(l);			
				n.add(Calendar.DATE, 6);
				String selectedDate1 = n.getTime().toString();
				String year1 = selectedDate1.substring(selectedDate1.length()-4, selectedDate1.length());
		    	String month1 = selectedDate1.substring(4, 7);
		    	String day1 = selectedDate1.substring(8, 10);	 
		    	
		    	String monthStr1 = "01";
				if(month1.equalsIgnoreCase("Jan")){monthStr1="01";}
		    	else if(month1.equalsIgnoreCase("Feb")){monthStr1="02";}
		    	else if(month1.equalsIgnoreCase("Mar")){monthStr1="03";}
		    	else if(month1.equalsIgnoreCase("Apr")){monthStr1="04";}
		    	else if(month1.equalsIgnoreCase("May")){monthStr1="05";}
		    	else if(month1.equalsIgnoreCase("Jun")){monthStr1="06";}
		    	else if(month1.equalsIgnoreCase("Jul")){monthStr1="07";}
		    	else if(month1.equalsIgnoreCase("Aug")){monthStr1="08";}
		    	else if(month1.equalsIgnoreCase("Sep")){monthStr1="09";}
		    	else if(month1.equalsIgnoreCase("Oct")){monthStr1="10";}
		    	else if(month1.equalsIgnoreCase("Nov")){monthStr1="11";}
		    	else if(month1.equalsIgnoreCase("Dec")){monthStr1="12";}
				Date e = Date.valueOf(year1+"-"+monthStr1+"-"+day1);

				String a1="";
				String b1="";
				if(selectedFromPar.equalsIgnoreCase("QUANTITY")){
					if(month.equalsIgnoreCase(month1)){						
						a1="sum(case when substring(start_date,6,2) = '"+monthStr+"' THEN (case when substring(start_date,9,2) between "+day+" and " +day1+" then 1 else 0 end ) else 0 END) as "+month+"_"+ day+",";//quantity
					}else{
						a1="sum(case when substring(start_date,6,2) = '"+monthStr+"' THEN (case when substring(start_date,9,2) between "+day+" and " +"31"+" then 1 else 0 end ) else 0 END) as "+month+"_"+ day+",";//quantity
						b1="sum(case when substring(start_date,6,2) = '"+monthStr1+"' THEN (case when substring(start_date,9,2) between "+"1"+" and " +day1+" then 1 else 0 end ) else 0 END) as "+month1+"_"+"1,";//quantity
					}
				}else if(selectedFromPar.equalsIgnoreCase("TOTAL COUNT")){
					if(month.equalsIgnoreCase(month1)){						
						a1="sum(case when substring(start_date,6,2) = '"+monthStr+"' THEN (case when substring(start_date,9,2) between "+day+" and " +day1+" then total_count else 0 end ) else 0 END) as "+month+"_"+ day+",";//quantity
					}else{
						a1="sum(case when substring(start_date,6,2) = '"+monthStr+"' THEN (case when substring(start_date,9,2) between "+day+" and " +"31"+" then total_count else 0 end ) else 0 END) as "+month+"_"+ day+",";//quantity
						b1="sum(case when substring(start_date,6,2) = '"+monthStr1+"' THEN (case when substring(start_date,9,2) between "+"1"+" and " +day1+" then total_count else 0 end ) else 0 END) as "+month1+"_"+"1,";//quantity
					}		
				}else if(selectedFromPar.equalsIgnoreCase("TOTAL SUM")){
					if(month.equalsIgnoreCase(month1)){						
						a1="FORMAT(sum(case when substring(start_date,6,2) = '"+monthStr+"' THEN (case when substring(start_date,9,2) between "+day+" and " +day1+" then replace(total_sum,' ','') else 0 end ) else 0 END), 'C', 'ru-ru') as "+month+"_"+ day+",";//quantity
					}else{
						a1="FORMAT(sum(case when substring(start_date,6,2) = '"+monthStr+"' THEN (case when substring(start_date,9,2) between "+day+" and " +"31"+" then replace(total_sum,' ','') else 0 end ) else 0 END), 'C', 'ru-ru') as "+month+"_"+ day+",";//quantity
						b1="FORMAT(sum(case when substring(start_date,6,2) = '"+monthStr1+"' THEN (case when substring(start_date,9,2) between "+"1"+" and " +day1+" then replace(total_sum,' ','') else 0 end ) else 0 END), 'C', 'ru-ru') as "+month1+"_"+"1,";//quantity
					}
				}
				fromCondition = fromCondition+a1+b1;
		    }
		    c2.add(Calendar.DATE,1);
		}
		if(selectedFromPar.equalsIgnoreCase("QUANTITY")){
			fromCondition = fromCondition+"count(*) as TOTAL";
		}else if(selectedFromPar.equalsIgnoreCase("TOTAL COUNT")){
			fromCondition = fromCondition+"sum(total_count) as TOTAL";		
		}else if(selectedFromPar.equalsIgnoreCase("TOTAL SUM")){
			fromCondition = fromCondition+"FORMAT(sum(replace(total_sum,' ','')), 'C', 'ru-ru') as TOTAL";
		}
		return fromCondition;
	}
	private static String expsManParFromMonthly(String beginDate, String endDate, String fromCondition,String selectedFromPar) {
		SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		Date a = Date.valueOf(endDate.substring(0, 4)+"-"+endDate.substring(4, 6)+"-"+endDate.substring(6, 8));
		c1.setTime(a);
		 
		Calendar c2 = Calendar.getInstance();
		Date c = Date.valueOf(beginDate.substring(0, 4)+"-"+beginDate.substring(4, 6)+"-"+beginDate.substring(6, 8));
		c2.setTime(c);
		 
		while(c1.after(c2)) {    			    	
	    	String selectedDate = c2.getTime().toString();//2016-01-01 Mon Feb 27 00:00:00 MSK 2017
	    	String year = selectedDate.substring(selectedDate.length()-4, selectedDate.length());
	    	String month = selectedDate.substring(4, 7);
	    	String day = selectedDate.substring(8, 10);
	    	String monthStr = "01";
	    	if(month.equalsIgnoreCase("Jan")){monthStr="01";}
	    	else if(month.equalsIgnoreCase("Feb")){monthStr="02";}
	    	else if(month.equalsIgnoreCase("Mar")){monthStr="03";}
	    	else if(month.equalsIgnoreCase("Apr")){monthStr="04";}
	    	else if(month.equalsIgnoreCase("May")){monthStr="05";}
	    	else if(month.equalsIgnoreCase("Jun")){monthStr="06";}
	    	else if(month.equalsIgnoreCase("Jul")){monthStr="07";}
	    	else if(month.equalsIgnoreCase("Aug")){monthStr="08";}
	    	else if(month.equalsIgnoreCase("Sep")){monthStr="09";}
	    	else if(month.equalsIgnoreCase("Oct")){monthStr="10";}
	    	else if(month.equalsIgnoreCase("Nov")){monthStr="11";}
	    	else if(month.equalsIgnoreCase("Dec")){monthStr="12";}
	    	Date d = Date.valueOf(year+"-"+monthStr+"-"+day);	    		    	
	    	
	    	Calendar n = Calendar.getInstance();
			Date l = Date.valueOf(String.valueOf(d));
			n.setTime(l);			
			n.add(Calendar.MONTH,1);
			String selectedDate1 = n.getTime().toString();
			String year1 = selectedDate1.substring(selectedDate1.length()-4, selectedDate1.length());
	    	String month1 = selectedDate1.substring(4, 7);
	    	String day1 = selectedDate1.substring(8, 10);	 
	    	
	    	String monthStr1 = "01";
			if(month1.equalsIgnoreCase("Jan")){monthStr1="01";}
	    	else if(month1.equalsIgnoreCase("Feb")){monthStr1="02";}
	    	else if(month1.equalsIgnoreCase("Mar")){monthStr1="03";}
	    	else if(month1.equalsIgnoreCase("Apr")){monthStr1="04";}
	    	else if(month1.equalsIgnoreCase("May")){monthStr1="05";}
	    	else if(month1.equalsIgnoreCase("Jun")){monthStr1="06";}
	    	else if(month1.equalsIgnoreCase("Jul")){monthStr1="07";}
	    	else if(month1.equalsIgnoreCase("Aug")){monthStr1="08";}
	    	else if(month1.equalsIgnoreCase("Sep")){monthStr1="09";}
	    	else if(month1.equalsIgnoreCase("Oct")){monthStr1="10";}
	    	else if(month1.equalsIgnoreCase("Nov")){monthStr1="11";}
	    	else if(month1.equalsIgnoreCase("Dec")){monthStr1="12";}
			Date e = Date.valueOf(year1+"-"+monthStr1+"-"+day1);

			String a1="";
			//String b1="";
			if(selectedFromPar.equalsIgnoreCase("QUANTITY")){
				a1="sum(case when substring(start_date,1,4)='"+year+"' THEN (case when substring(start_date,6,2) ='"+monthStr+"' then 1 else 0 end ) else 0 END) as "+year+"_"+month+",";				
			}else if(selectedFromPar.equalsIgnoreCase("TOTAL COUNT")){
				a1="sum(case when substring(start_date,1,4)='"+year+"' THEN (case when substring(start_date,6,2) ='"+monthStr+"' then total_count else 0 end ) else 0 END) as "+year+"_"+month+",";						
			}else if(selectedFromPar.equalsIgnoreCase("TOTAL SUM")){
				a1="FORMAT(sum(case when substring(start_date,1,4)='"+year+"' THEN (case when substring(start_date,6,2) ='"+monthStr+"' then replace(total_sum,' ','') else 0 end ) else 0 END), 'C', 'ru-ru') as "+year+"_"+month+",";				
			}
			fromCondition = fromCondition+a1;
		    
		    c2.add(Calendar.MONTH,1);
		}
		if(selectedFromPar.equalsIgnoreCase("QUANTITY")){
			fromCondition = fromCondition+"count(*) as TOTAL";
		}else if(selectedFromPar.equalsIgnoreCase("TOTAL COUNT")){
			fromCondition = fromCondition+"sum(total_count) as TOTAL";		
		}else if(selectedFromPar.equalsIgnoreCase("TOTAL SUM")){
			fromCondition = fromCondition+"FORMAT(sum(replace(total_sum,' ','')), 'C', 'ru-ru') as TOTAL";
		}
		return fromCondition;
	}
	
	private static String expsManParFromQuarterly(String beginDate, String endDate, String fromCondition,String selectedFromPar) {
		SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		Date a = Date.valueOf(endDate.substring(0, 4)+"-"+endDate.substring(4, 6)+"-"+endDate.substring(6, 8));
		c1.setTime(a);
		 
		Calendar c2 = Calendar.getInstance();
		Date c = Date.valueOf(beginDate.substring(0, 4)+"-"+beginDate.substring(4, 6)+"-"+beginDate.substring(6, 8));
		c2.setTime(c);
		int quarter = 1;
		 
		while(c1.after(c2)) {    			    	
	    	String selectedDate = c2.getTime().toString();//2016-01-01 Mon Feb 27 00:00:00 MSK 2017
	    	String year = selectedDate.substring(selectedDate.length()-4, selectedDate.length());
	    	String month = selectedDate.substring(4, 7);
	    	String day = selectedDate.substring(8, 10);
	    	String monthStr = "01";
	    	if(month.equalsIgnoreCase("Jan")){monthStr="01";}
	    	else if(month.equalsIgnoreCase("Feb")){monthStr="02";}
	    	else if(month.equalsIgnoreCase("Mar")){monthStr="03";}
	    	else if(month.equalsIgnoreCase("Apr")){monthStr="04";}
	    	else if(month.equalsIgnoreCase("May")){monthStr="05";}
	    	else if(month.equalsIgnoreCase("Jun")){monthStr="06";}
	    	else if(month.equalsIgnoreCase("Jul")){monthStr="07";}
	    	else if(month.equalsIgnoreCase("Aug")){monthStr="08";}
	    	else if(month.equalsIgnoreCase("Sep")){monthStr="09";}
	    	else if(month.equalsIgnoreCase("Oct")){monthStr="10";}
	    	else if(month.equalsIgnoreCase("Nov")){monthStr="11";}
	    	else if(month.equalsIgnoreCase("Dec")){monthStr="12";}
	    	Date d = Date.valueOf(year+"-"+monthStr+"-"+day);	 
	    	
	    	String monthGroup = "";
	    	String asGroup = "";
	    	if(monthStr.matches("01|02|03")){
	    		monthGroup = "('01','02','03')";
	    		asGroup= year+"_First_Quarter";
	    	}else if(monthStr.matches("04|05|06")){
	    		monthGroup = "('04','05','06')";
	    		asGroup= year+"_Second_Quarter";
	    	}else if(monthStr.matches("07|08|09")){
	    		monthGroup = "('07','08','09')";
	    		asGroup= year+"_Third_Quarter";
	    	}else if(monthStr.matches("10|11|12")){
	    		monthGroup = "('10','11','12')";
	    		asGroup= year+"_Fourth_Quarter";
	    	}

			String a1="";
			//String b1="";
			if(selectedFromPar.equalsIgnoreCase("QUANTITY")){
				a1="sum(case when substring(start_date,1,4)='"+year+"' THEN (case when substring(start_date,6,2) in "+monthGroup+" then 1 else 0 end ) else 0 END) as "+asGroup+",";				
			}else if(selectedFromPar.equalsIgnoreCase("TOTAL COUNT")){
				a1="sum(case when substring(start_date,1,4)='"+year+"' THEN (case when substring(start_date,6,2) in "+monthGroup+" then total_count else 0 end ) else 0 END) as "+asGroup+",";						
			}else if(selectedFromPar.equalsIgnoreCase("TOTAL SUM")){
				a1="FORMAT(sum(case when substring(start_date,1,4)='"+year+"' THEN (case when substring(start_date,6,2) in "+monthGroup+" then replace(total_sum,' ','') else 0 end ) else 0 END), 'C', 'ru-ru') as "+asGroup+",";				
			}
			fromCondition = fromCondition+a1;
		    
		    c2.add(Calendar.MONTH,3);
		    quarter=quarter++;
		}
		if(selectedFromPar.equalsIgnoreCase("QUANTITY")){
			fromCondition = fromCondition+"count(*) as TOTAL";
		}else if(selectedFromPar.equalsIgnoreCase("TOTAL COUNT")){
			fromCondition = fromCondition+"sum(total_count) as TOTAL";		
		}else if(selectedFromPar.equalsIgnoreCase("TOTAL SUM")){
			fromCondition = fromCondition+"FORMAT(sum(replace(total_sum,' ','')), 'C', 'ru-ru') as TOTAL";
		}
		return fromCondition;
	}
	
	public static DefaultTableModel repDoctorManagerial(JTable resultTable,String selectedBeginDate,String selectedEndDate,
			String selectedCountry,String selectedDistrict,String selectedRegion,String selectedCity,
			String selectedSpeciality,String selectedBrand,String repType,String selectedSubSpeciality,String selectedClinic,
			String selectedMedRep,String selectedFromPar,String selectedDatePar) throws Exception {
		
		        Connection conn =null;
		        Statement stmt =null;
		        String selectSQL ="";
		        DefaultTableModel tableModel =null;  
		        
		        String from = "";
		        String groupBy = "";
		        String union = "";
		        String paramWhere = "";
		        String paramWhereUnion = "";
		        String prmDate = "";
				
		        textVariableIndex = new ArrayList();//PARAMFROM
				textVaLueIndex = new ArrayList(); //region			
				
		        try{
		        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));   
		        	 
		        	 if(repType.equalsIgnoreCase("REGIONS")){		        		 
		        		 from = from+"region,";
		        		 groupBy = groupBy+"region";
		        		 union = union+"'region' as a,";    			             
		         	}else if(repType.equalsIgnoreCase("MAIN_DISTRICT")){
		         		from = from+"area,";
		        		 groupBy = groupBy+"area";
		        		 union = union+"'area' as w,";		         		
		           	}else if(repType.equalsIgnoreCase("CITY")){
		           		from = from+"city,";
		        		 groupBy = groupBy+"city";
		        		 union = union+"'city' as j,";	
		             }else if(repType.equalsIgnoreCase("MAIN_SPECIALITY")){
		            	 from = from+"unified_specialty,";
		        		 groupBy = groupBy+"unified_specialty";
		        		 union = union+"'unified_specialty' as k,";
		             }else if(repType.equalsIgnoreCase("SUB_SPECIALITY")){
		            	 from = from+"specialty,";
		        		 groupBy = groupBy+"specialty";
		        		 union = union+"'specialty' as k,";
		             }else if(repType.equalsIgnoreCase("MED_REPS")){
		            	 from = from+"medrep,";
		        		 groupBy = groupBy+"medrep";
		        		 union = union+"'medrep' as k,";
		             }else if(repType.equalsIgnoreCase("CLINIC_NAME")){
		            	 from = from+"clinic_name,";
		        		 groupBy = groupBy+"clinic_name";
		        		 union = union+"'clinic_name' as l,";
		             }else if(repType.equalsIgnoreCase("ACTIVATION_DATE")){
		            	 from = from+"region,";
		        		 groupBy = groupBy+"region";
		        		 union = union+"'region' as a,";
		        		 prmDate = " and  replace (doctor_date,'-','') >="+selectedBeginDate+" and replace (doctor_date,'-','') <="+selectedEndDate;		            	 
		             } 
		        	 
				      
		        	 if (!selectedBrand.equalsIgnoreCase("SOLGAR")) {
				    	  selectedBrand =" and brand = '	'";
				      }	
				      if (selectedSpeciality.length() > 0) {				    	  
				    	  	from = from+"unified_specialty,";
			        		groupBy = groupBy+",unified_specialty";
			        		union = union+"'unified_specialty' as b,"; 
			        		paramWhere = paramWhere+" and unified_specialty='"+selectedSpeciality+"' " ;
			        		paramWhereUnion = paramWhereUnion+" and unified_specialty='"+selectedSpeciality+"' " ;				    	  
				      }	
				      if (selectedSubSpeciality.length() > 0) {
				    	  from = from+"specialty,";
			        		groupBy = groupBy+",specialty";
			        		union = union+"'specialty' as c,"; 
			        		paramWhere = paramWhere+" and specialty='"+selectedSubSpeciality+"' " ;
			        		paramWhereUnion = paramWhereUnion+" and specialty='"+selectedSubSpeciality+"' " ;
				      }	
				      if (selectedClinic.length() > 0) {
				    	  from = from+"clinic_name,";
			        		groupBy = groupBy+",clinic_name";
			        		union = union+"'clinic_name' as d,";
			        		paramWhere = paramWhere+" and clinic_name='"+selectedClinic+"' " ;
			        		paramWhereUnion = paramWhereUnion+" and clinic_name='"+selectedClinic+"' " ;
				      }				  
				      if (selectedMedRep.length() > 0) {
				    	  from = from+"medrep,";
			        		groupBy = groupBy+",medrep";
			        		union = union+"'medrep' as f,";
			        		paramWhere = paramWhere+" and medrep='"+selectedMedRep+"' " ;
			        		paramWhereUnion = paramWhereUnion+" and medrep='"+selectedMedRep+"' " ;
				      }
				      if (selectedDistrict.length() > 0) {
				    	  from = from+"area,";
			        		groupBy = groupBy+",area";
			        		union = union+"'area' as g,";
			        		paramWhere = paramWhere+" and area='"+selectedDistrict+"' " ;
			        		paramWhereUnion = paramWhereUnion+" and area='"+selectedDistrict+"' " ;
				      }
				      if (selectedRegion.length() > 0) {
				    	  from = from+"region,";
			        		groupBy = groupBy+",region";
			        		union = union+"'region' as h,";
			        		paramWhere = paramWhere+" and region='"+selectedRegion+"' " ;
			        		paramWhereUnion = paramWhereUnion+" and region='"+selectedRegion+"' " ;
				      }
				      if (selectedCity.length() > 0) {
				    	  from = from+"city,";
			        		groupBy = groupBy+",city";
			        		union = union+"'city' as i,";
			        		paramWhere = paramWhere+" and city='"+selectedCity+"' " ;
			        		paramWhereUnion = paramWhereUnion+" and city='"+selectedCity+"' " ;
				      }
		        	 
		        	 textVariableIndex.add(0,"PARAMFROM");
		             textVaLueIndex.add(0,from);
		             
		             textVariableIndex.add(1,"PARAMGROUP");
		             textVaLueIndex.add(1,groupBy);
		             
		             textVariableIndex.add(2,"PARAMUNION");
		             textVaLueIndex.add(2,union);
		             
		             textVariableIndex.add(3,"PARAMWHERE");
		             textVaLueIndex.add(3,paramWhere);
		             
		             textVariableIndex.add(4,"PRMWHEREUNION");
		             textVaLueIndex.add(4,paramWhereUnion);
		             
		             textVariableIndex.add(5,"PRMDATE");
		             textVaLueIndex.add(5,prmDate);
	             
		        	 
		        	 if(selectedFromPar.equalsIgnoreCase("TOTAL_CATEGORY")){
		        		 selectSQL = getParamQuery("PAR_REP_DOCTORDATA_CATEGORY_GET");
		        	 }else if(selectedFromPar.equalsIgnoreCase("TOTAL_QUANTITY")){
		        		 selectSQL = getParamQuery("PAR_REP_DOCTORDATA_QUANTITY_GET");
		        	 }
		        	 
		        	  stmt = (Statement) conn.createStatement();
		              ResultSet rs = stmt.executeQuery(selectSQL);
		              
		              tableModel = new DefaultTableModel();
		              ResultSetMetaData metaData = rs.getMetaData();
		              int columnCount = metaData.getColumnCount();
		              for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
		                  tableModel.addColumn(metaData.getColumnLabel(columnIndex));
		              }
		              Object[] row = new Object[columnCount];
		              
		              while (rs.next()){
		                  for (int i = 0; i < columnCount; i++){
		                      row[i] = rs.getObject(i+1);
		                  }
		                  tableModel.addRow(row);
		              }
		              resultTable.setModel(tableModel);
		        	
		        }
		        catch(SQLException sqle){       	
		            System.out.println("SQL Exception: " + sqle.getMessage());
		        }finally{
		        	if (stmt != null) {
		        		stmt.close();
					}
		
					if (conn != null) {
						conn.close();
					}
		        
		        }
        return tableModel;
    
	}
	
	public static DefaultTableModel repPharmManagerial(JTable resultTable,String selectedBeginDate,String selectedEndDate,
			String selectedCountry,String selectedDistrict,String selectedRegion,String selectedCity,
			String selectedChain,String selectedBrand,String repType,String selectedSubChains,String selectedAssortiment,String selectedPharmacyType,
			String selectedPromo,String selectedActiveness,String selectedMedRep,String selectedFromPar,String selectedDatePar) throws Exception {
		
		        Connection conn =null;
		        Statement stmt =null;
		        String selectSQL ="";
		        DefaultTableModel tableModel =null;  
		        
		        String from = "";
		        String groupBy = "";
		        String union = "";
		        String paramWhere = "";
		        String paramWhereUnion = "";
		        String prmDate = "";
				
		        textVariableIndex = new ArrayList();//PARAMFROM
				textVaLueIndex = new ArrayList(); //region			
				
		        try{
		        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));   
		        	 
		        	 if(repType.equalsIgnoreCase("REGIONS")){
		        		 
		        		 from = from+"region,";
		        		 groupBy = groupBy+"region";
		        		 union = union+"'region' as a,";    
			             
		         	}else if(repType.equalsIgnoreCase("MAIN_DISTRICT")){
		         		from = from+"area,";
		        		 groupBy = groupBy+"area";
		        		 union = union+"'area' as w,";		         		
		           	}else if(repType.equalsIgnoreCase("CITY")){
		           		from = from+"city,";
		        		 groupBy = groupBy+"city";
		        		 union = union+"'city' as j,";	
		             }else if(repType.equalsIgnoreCase("MED_REPS")){
		            	 from = from+"marketing_staff,";
		        		 groupBy = groupBy+"marketing_staff";
		        		 union = union+"'marketing_staff' as k,";
		             }else if(repType.equalsIgnoreCase("CHAINS")){
		            	 from = from+"group_company,";
		        		 groupBy = groupBy+"group_company";
		        		 union = union+"'group_company' as l,";
		             }else if(repType.equalsIgnoreCase("ACTIVATION_DATE")){
		            	 from = from+"region,";
		        		 groupBy = groupBy+"region";
		        		 union = union+"'region' as a,";
		        		 prmDate = " and  replace (pharmacy_activation_date,'-','') >="+selectedBeginDate+" and replace (pharmacy_activation_date,'-','') <="+selectedEndDate;
		            	 
		             }
		        	 
				      
		        	 if (!selectedBrand.equalsIgnoreCase("SOLGAR")) {
				    	  selectedBrand ="bounty";
				      }	
				      if (selectedChain.length() > 0) {
				    	  
				    	  	from = from+"group_company,";
			        		groupBy = groupBy+",group_company";
			        		union = union+"'group_company' as b,"; 
			        		paramWhere = paramWhere+" and group_company='"+selectedChain+"' " ;
			        		paramWhereUnion = paramWhereUnion+" and group_company='"+selectedChain+"' " ;
				    	  
				      }	
				      if (selectedSubChains.length() > 0) {
				    	  from = from+"subgroup_company,";
			        		groupBy = groupBy+",subgroup_company";
			        		union = union+"'subgroup_company' as c,"; 
			        		paramWhere = paramWhere+" and subgroup_company='"+selectedSubChains+"' " ;
			        		paramWhereUnion = paramWhereUnion+" and subgroup_company='"+selectedSubChains+"' " ;
				      }	
				      if (selectedAssortiment.length() > 0) {
				    	  from = from+"assortiment,";
			        		groupBy = groupBy+",assortiment";
			        		union = union+"'assortiment' as d,";
			        		paramWhere = paramWhere+" and assortiment='"+selectedAssortiment+"' " ;
			        		paramWhereUnion = paramWhereUnion+" and assortiment='"+selectedAssortiment+"' " ;
				      }
				      if (selectedPharmacyType.length() > 0) {
				    	  from = from+"pharmacy_type,";
			        		groupBy = groupBy+",pharmacy_type";
			        		union = union+"'pharmacy_type' as d,"; 
			        		paramWhere = paramWhere+" and pharmacy_type='"+selectedPharmacyType+"' " ;
			        		paramWhereUnion = paramWhereUnion+" and pharmacy_type='"+selectedPharmacyType+"' " ;
				      }
				      if (selectedPromo.length() > 0) {
				    	  from = from+"promo,";
			        		groupBy = groupBy+",promo";
			        		union = union+"'promo' as e,";
			        		paramWhere = paramWhere+" and promo='"+selectedPromo+"' " ;
			        		paramWhereUnion = paramWhereUnion+" and promo='"+selectedPromo+"' " ;
				      }
				      if (selectedMedRep.length() > 0) {
				    	  from = from+"marketing_staff,";
			        		groupBy = groupBy+",marketing_staff";
			        		union = union+"'marketing_staff' as f,";
			        		paramWhere = paramWhere+" and marketing_staff='"+selectedMedRep+"' " ;
			        		paramWhereUnion = paramWhereUnion+" and marketing_staff='"+selectedMedRep+"' " ;
				      }
				      if (selectedDistrict.length() > 0) {
				    	  from = from+"area,";
			        		groupBy = groupBy+",area";
			        		union = union+"'area' as g,";
			        		paramWhere = paramWhere+" and area='"+selectedDistrict+"' " ;
			        		paramWhereUnion = paramWhereUnion+" and area='"+selectedDistrict+"' " ;
				      }
				      if (selectedRegion.length() > 0) {
				    	  from = from+"region,";
			        		groupBy = groupBy+",region";
			        		union = union+"'region' as h,";
			        		paramWhere = paramWhere+" and region='"+selectedRegion+"' " ;
			        		paramWhereUnion = paramWhereUnion+" and region='"+selectedRegion+"' " ;
				      }
				      if (selectedCity.length() > 0) {
				    	  from = from+"city,";
			        		groupBy = groupBy+",city";
			        		union = union+"'city' as i,";
			        		paramWhere = paramWhere+" and city='"+selectedCity+"' " ;
			        		paramWhereUnion = paramWhereUnion+" and city='"+selectedCity+"' " ;
				      }
		        	 
		        	 textVariableIndex.add(0,"PARAMFROM");
		             textVaLueIndex.add(0,from);
		             
		             textVariableIndex.add(1,"PARAMGROUP");
		             textVaLueIndex.add(1,groupBy);
		             
		             textVariableIndex.add(2,"PARAMUNION");
		             textVaLueIndex.add(2,union);
		             
		             textVariableIndex.add(3,"BRANDTYPE");
		             textVaLueIndex.add(3,selectedBrand);
		             
		             textVariableIndex.add(4,"PARAMWHERE");
		             textVaLueIndex.add(4,paramWhere);
		             
		             textVariableIndex.add(5,"PRMWHEREUNION");
		             textVaLueIndex.add(5,paramWhereUnion);
		             
		             textVariableIndex.add(6,"PRMDATE");
		             textVaLueIndex.add(6,prmDate);
	             
		        	 
		        	 if(selectedFromPar.equalsIgnoreCase("TOTAL_CATEGORY")){
		        		 selectSQL = getParamQuery("PAR_REP_PHARMDATA_CATEGORY_GET");
		        	 }else if(selectedFromPar.equalsIgnoreCase("TOTAL_QUANTITY")){
		        		 selectSQL = getParamQuery("PAR_REP_PHARMDATA_QUANTITY_GET");
		        	 }else if(selectedFromPar.equalsIgnoreCase("TOTAL_ACTIVENESS")){
		        		 selectSQL = getParamQuery("PAR_REP_PHARMDATA_ACTIVENESS_GET");
		        	 }
		        	 
		        	  stmt = (Statement) conn.createStatement();
		              ResultSet rs = stmt.executeQuery(selectSQL);
		              
		              tableModel = new DefaultTableModel();
		              ResultSetMetaData metaData = rs.getMetaData();
		              int columnCount = metaData.getColumnCount();
		              for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
		                  tableModel.addColumn(metaData.getColumnLabel(columnIndex));
		              }
		              Object[] row = new Object[columnCount];
		              
		              while (rs.next()){
		                  for (int i = 0; i < columnCount; i++){
		                      row[i] = rs.getObject(i+1);
		                  }
		                  tableModel.addRow(row);
		              }
		              resultTable.setModel(tableModel);
		        	
		        }
		        catch(SQLException sqle){       	
		            System.out.println("SQL Exception: " + sqle.getMessage());
		        }finally{
		        	if (stmt != null) {
		        		stmt.close();
					}
		
					if (conn != null) {
						conn.close();
					}
		        
		        }
        return tableModel;
    
	}
	
	private static String getParamQuery(String queryName) throws Exception{
		
		String out = "";

		try {
			String selectSQL = getQueryScript(queryName);
			out = selectSQL;
				
				for (int k = 0; k < textVariableIndex.size(); k++) {
					
					Pattern p = Pattern.compile(textVariableIndex.get(k).toString());
					Matcher m = p.matcher(selectSQL);
					while (m.find())
						out = out.replaceFirst(p.pattern(), textVaLueIndex.get(k).toString());
					
				}
	
			
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
		return out;	
		
	}//GetExplanation

}
