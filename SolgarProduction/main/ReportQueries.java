package main;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import cb.esi.esiclient.util.ESIBag;

public class ReportQueries {

	static final Properties prop = ConnectToDb.readConfFile();
	
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
			String selectedPrdSub, String selectedProduct,String repType,String country)
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
		      selectSQL = getQueryScript("STOCK_MONTHLY_ALL");
 
		      if(repType.equalsIgnoreCase("DISTRUBUTOR_SALES")){
		    	  fromCondition = "select a.optype as operation_type,a.distributor,";
		    	  groupby = " group by a.optype,a.distributor";
		      }else if(repType.equalsIgnoreCase("REGIONAL_SALES")){
		    	  fromCondition = fromCondition + "select a.optype as operation_type,b.district,";
			      groupby = groupby + " group by a.optype,b.district";
		      }else if(repType.equalsIgnoreCase("PRODUCT_SALES")){
		    	  fromCondition = fromCondition + "select a.optype as operation_type,c.product_main_group,";
			      groupby = groupby + " group by a.optype,c.product_main_group";
		      }
		      
		      
		      String whereCondition = " and a.rep_begin_date >='" + beginDate + "'" + " and a.rep_end_date<='" + endDate + "'" + " and a.optype ='" + selectedOpType + "'" + " and b.country ='" + country + "'";

		      if (selectedCompany.length() > 0) {
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
}
