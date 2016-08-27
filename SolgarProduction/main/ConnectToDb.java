package main;

import java.io.FileInputStream;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import cb.esi.esiclient.util.ESIBag;
import jxl.write.DateTime;
import util.Util;

public class ConnectToDb {

	static final Properties prop = readConfFile();
	
	private static Properties readConfFile() {  	   
		Properties prop = new Properties();		
			try {			
				//prop.load(new FileInputStream("conf/config.properties"));
				prop.load(new FileInputStream("C:\\config.properties"));
	       } catch (Exception e) {
	           e.printStackTrace();
	       }			
			return prop;
	  }	   
	
	public static ESIBag get(ESIBag inBag) throws Exception {

		String url = prop.getProperty("DB_URL");
		String address ="";
        //declare variables 
        Connection conn;
        Statement stmt;
        //instructions
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{
            conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"), prop.getProperty("PASS"));
            
            String sorgu 
            = "select id,city,pharmacy_address,requested from marketing.pharmacy_data_bounty where country_code <> 'RU' and length(country_code)>0";

            stmt = (Statement) conn.createStatement();

            ResultSet rs = stmt.executeQuery(sorgu);
            
            int i =0;
            while (rs.next())
            {
            	address = rs.getString("city");
            	address = address +","+rs.getString("pharmacy_address");      	
                inBag.put("TABLE",i,"ADDRESSROW1", address);
                i++;
            }
            stmt.close();
            conn.close();
        }
        catch(SQLException sqle){
            System.out.println("SQL Exception: " + sqle.getMessage());
        }
		return inBag;

    }
	
	
	public static void update(ESIBag inBag) throws Exception {

		  Connection conn = null;
		  PreparedStatement  stmt = null;
		   try{
			   int found_no = 0;
			   int id =0;

		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to a selected database...");
		      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		      
		      System.out.println("Connected database successfully...");
		      
				if (inBag.existsBagKey("FOUND")) {
					found_no = Integer.parseInt(inBag.get("FOUND").toString());
				}		
				
		      id = Integer.parseInt(inBag.get("ID").toString());
		      
		      
		      //STEP 4: Execute a query
		      System.out.println("Creating first statement...");
		      stmt = (PreparedStatement) conn.prepareStatement( "update marketing.pharmacy_data_bounty set found_no = ?  WHERE id = ?");
		      
		      stmt.setInt(1,found_no);
		      stmt.setInt(2,id);
		      
		      stmt.executeUpdate();
		      stmt.close();
		      

		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   System.out.println("Goodbye!");
		}//end main	
	
	public static void getPRMData(String columnName,String tableName,JComboBox cmbBox) {

		String url = prop.getProperty("DB_URL");
        Connection conn;
        Statement stmt;
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{
            conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"), prop.getProperty("PASS"));            
            String sorgu  = "select "+columnName+" from " +tableName;
            stmt = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(sorgu);

            while (rs.next()){
            	cmbBox.addItem(rs.getString(columnName));
            }
            
            stmt.close();
            conn.close();
        }
        catch(SQLException sqle){
            System.out.println("SQL Exception: " + sqle.getMessage());
        }

    }
	
	public static void getPRMDataGroupBy(String columnName,String tableName,JComboBox cmbBox,String conditionColumn,String conditionValue) {

		String url = prop.getProperty("DB_URL");
        Connection conn;
        Statement stmt;
        String sorgu="";
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{
            conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"),prop.getProperty("PASS"));  
            
            if(conditionValue.length()>0){
            	sorgu  = "select "+columnName+" from " +tableName + " where " + conditionColumn+ "='"+conditionValue+ "' group by "+columnName ;
            }else{
            	sorgu  = "select "+columnName+" from " +tableName + " group by "+columnName;
            }
            
            stmt = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(sorgu);

            while (rs.next()){
            	cmbBox.addItem(rs.getString(columnName));
            }
            
            stmt.close();
            conn.close();
        }
        catch(SQLException sqle){
            System.out.println("SQL Exception: " + sqle.getMessage());
        }
	}
	
	public static void getPRMDataTwoConditionsGroupBy(String columnName,String tableName,JComboBox cmbBox,String conditionColumn,String conditionValue,String conditionColumn1,String conditionValue1) {

		String url = prop.getProperty("DB_URL");
        Connection conn;
        Statement stmt;
        String sorgu="";
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{
            conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"),prop.getProperty("PASS"));  
            
            if(conditionValue.length()>0 && conditionValue1.length()>0){
            	sorgu  = "select "+columnName+" from " +tableName + " where " + conditionColumn+ "='"+conditionValue+ "' and " + conditionColumn1+ "='"+conditionValue1+ "' group by "+columnName ;
            }else if(conditionValue.length()>0){
            	sorgu  = "select "+columnName+" from " +tableName + " where " + conditionColumn+ "='"+conditionValue+ "' group by "+columnName ;
            }else{
            	sorgu  = "select "+columnName+" from " +tableName + " group by "+columnName;
            }
            
            stmt = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(sorgu);
            
            cmbBox.removeAllItems();

            while (rs.next()){
            	cmbBox.addItem(rs.getString(columnName));
            }
            
            stmt.close();
            conn.close();
        }
        catch(SQLException sqle){
            System.out.println("SQL Exception: " + sqle.getMessage());
        }
	}
	
	public static String getQueryScript(String queryName) throws Exception {

		String url = prop.getProperty("DB_URL");
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
            conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"), prop.getProperty("PASS"));            
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
	
	public static String getAuthorization(String user,String pass) throws Exception {

		String url = prop.getProperty("DB_URL");
		PreparedStatement preparedStatement=null;
        Connection conn =null;
        String employee_name = "";
        
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{
            conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"), prop.getProperty("PASS"));            
            String sorgu  = "select * from solgar_org.employees where EMPLOYEE_EMAIL = ? and EMPLOYEE_PASSWORD=?;";
            preparedStatement = (PreparedStatement)conn.prepareStatement(sorgu);
        	preparedStatement.setString(1, user);
        	preparedStatement.setString(2, pass);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
            	employee_name = rs.getString("EMPLOYEE_NAME");
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
        return employee_name;
    }
	
	public static void setSaleDataToDB(JTable resultTable) throws Exception {

		  Connection conn = null;
		  PreparedStatement  stmt = null;
		  String mainGroup="", productName ="",salesReader ="",salesDate="",count="",productType="";
				  
		   try{

		      Class.forName("com.mysql.jdbc.Driver");
		      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
		      conn.setAutoCommit(false);
		      
		      try{
		      
			      int row = resultTable.getRowCount();
					for (int j = 0; j  < row; j++) {
						mainGroup = resultTable.getValueAt(j, 1).toString();
						productName = resultTable.getValueAt(j, 4).toString();
						salesReader = resultTable.getValueAt(j, 9).toString();
						salesDate = resultTable.getValueAt(j, 8).toString();
						count = resultTable.getValueAt(j, 6).toString().trim();
						
						if(productName.toUpperCase().indexOf("аюсмрх")>=0 || productName.toUpperCase().indexOf("BOUNTY")>=0){
							productType= "BN";
						}else{
							productType = "SL";
						}
						
					    if(count.indexOf(".")>0){
							count = count.substring(0, count.indexOf("."));
						}
					    if(count.indexOf(",")>0){
							count = count.substring(0, count.indexOf(","));
						}
					    int intCount =  Integer.parseInt(count);
					    
					    if(intCount > 0){					  
						   stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_tst.general_sales_table(`main_group`,`sub_group`,`pharmacy_address`,`product_Name`,`pharmacy_no`,`sales_count`,`sales_amount`,`sales_date`,`sales_reader`,`city`,`product_type`,`address_id`)VALUES( ?,?,?,?,?,?,?,?,?,?,?,?)");
						   stmt.setString(1,mainGroup);
						   stmt.setString(2,resultTable.getValueAt(j, 2).toString());
						   stmt.setString(3,resultTable.getValueAt(j, 3).toString());
						   stmt.setString(4,productName);
						   stmt.setString(5,resultTable.getValueAt(j, 5).toString());				   
						   stmt.setInt(6,intCount);
						   stmt.setString(7,resultTable.getValueAt(j, 7).toString());
						   stmt.setDate(8,Date.valueOf(salesDate));	
						   stmt.setString(9,salesReader);
						   stmt.setString(10,resultTable.getValueAt(j, 10).toString());
						   stmt.setString(11,productType);
						   stmt.setInt(12,0);
						   stmt.executeUpdate();
						   stmt.close();
					    }
					}   
				
					conn.commit();					
				
		      } catch (SQLException e) {

					System.out.println(e.getMessage());
					conn.rollback();

				}finally{
				      //finally block used to close resources
				      try{
				         if(stmt!=null)
				            conn.close();
				      }catch(SQLException se){
				      }// do nothing
				      try{
				         if(conn!=null)
				            conn.close();
				      }catch(SQLException se){
				         se.printStackTrace();
				      }//end finally try
				   }

		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   System.out.println("Goodbye!");
		}//end main	
	
	public static DefaultTableModel repGetTotalCounts(JTable resultTable,String reportName,String beginDate,String endDate,String chain,
			String selectedCompany,String selectedCountry,String selectedRegion,String selectedCity,String selectedProduct,String selectedMedRep) throws Exception {

		String url = prop.getProperty("DB_URL");
        Connection conn =null;
        Statement stmt =null;
        String selectSQL ="";
        DefaultTableModel tableModel =null;
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{
            conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"), prop.getProperty("PASS"));
            
            /*selectSQL = getQueryScript(reportName);
            
            if(selectedCompany.length()>0 && selectedDate.length()>0){           	            	
            	preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
            	preparedStatement.setDate(1, Date.valueOf(selectedDate));
            	preparedStatement.setString(2, selectedCompany);
            }else if(selectedCompany.length()>0){
            	selectSQL = selectSQL.replace("sales_date = ? and", "");     
            	preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
            	preparedStatement.setString(1, selectedCompany);
            }else if(selectedDate.length()>0){
            	selectSQL = selectSQL.replace("and main_group = ?", "");
            	preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
            	preparedStatement.setDate(1, Date.valueOf(selectedDate));
            } */
            
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
	
	public static ESIBag repGetDeliverationStatus(String selectedCompany,String selectedDate,String reportName) throws Exception {

		String url = prop.getProperty("DB_URL");
        Connection conn =null;
        PreparedStatement preparedStatement=null;
        String selectSQL ="";
        ESIBag outBag = new ESIBag();
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{
            conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"), prop.getProperty("PASS"));
            
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

		String url = prop.getProperty("DB_URL");
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
            conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"), prop.getProperty("PASS"));                    	
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
	
	public static ESIBag saveMarketingExpenses(JTable resultTable,String userName) throws Exception {

		  Connection conn = null;
		  PreparedStatement  stmt = null;
		  ESIBag outBag = new ESIBag();
				  
		   try{

		      Class.forName("com.mysql.jdbc.Driver");
		      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
		      conn.setAutoCommit(false);
		      
		      try{
		      		    	  
			      int row = resultTable.getRowCount();
					for (int j = 0; j  < row; j++) {
											  
					   stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_mcs.marketing_exp_main_info(`approve_status`"
					   		+",`company_code`,`entry_date`,`entry_user`," //`approve_date`,`approve_user`,
							+"`Country`,`Region`,`City`,`City_Region`,`Start_Date`,`End_Date`,`First_Stage`,`Second_Stage`,`Third_Stage`,`Exp1`,`Exp2`,`Exp3`,`Exp4`,"
					   		+ "`Exp_Count`,`Exp_Amount`,`comments`,`amount1`,`amount2`,`amount3`,`amount4`,`amount5`"
					   		+ ")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
					   stmt.setInt(1,1);
					   stmt.setString(2,resultTable.getValueAt(j, 2).toString());//company code
					   stmt.setTimestamp(3,Timestamp.valueOf(Util.getCurrentDateTime()));//Startd date
					   stmt.setString(4,userName);
					   
					   stmt.setString(5,resultTable.getValueAt(j, 3).toString());//countyr
					   stmt.setString(6,resultTable.getValueAt(j, 4).toString());
					   if(resultTable.getValueAt(j, 5) != null){
						   stmt.setString(7,resultTable.getValueAt(j, 5).toString());
					   }else{
						   stmt.setString(7,"");
					   }
					   if(resultTable.getValueAt(j, 6) != null){
						   stmt.setString(8,resultTable.getValueAt(j, 6).toString());
					   }else{
						   stmt.setString(8,"");
					   }
					   stmt.setDate(9,Date.valueOf(resultTable.getValueAt(j, 7).toString()));//Startd date
					   stmt.setDate(10,Date.valueOf(resultTable.getValueAt(j, 8).toString()));//End date
					   stmt.setString(11,resultTable.getValueAt(j, 9).toString());					   
					   if(resultTable.getValueAt(j, 10).toString() != null){
						   stmt.setString(12,resultTable.getValueAt(j, 10).toString());
					   }else{
						   stmt.setString(12,"");
					   }
					   if(resultTable.getValueAt(j, 11) != null){
						   stmt.setString(13,resultTable.getValueAt(j, 11).toString());
					   }else{
						   stmt.setString(13,"");
					   }					   
					   stmt.setString(14,resultTable.getValueAt(j, 12).toString());//Exp1
					   stmt.setString(15,resultTable.getValueAt(j, 13).toString());
					   stmt.setString(16,resultTable.getValueAt(j, 14).toString());
					   stmt.setString(17,resultTable.getValueAt(j, 15).toString());
					   stmt.setInt(18,Integer.parseInt(resultTable.getValueAt(j, 16).toString().trim()));//Count
					   stmt.setString(19,resultTable.getValueAt(j, 17).toString());
					   stmt.setString(20,resultTable.getValueAt(j, 18).toString());
					   
					   stmt.setString(21,resultTable.getValueAt(j, 19).toString());//Amount1
					   stmt.setString(22,resultTable.getValueAt(j, 20).toString());
					   stmt.setString(23,resultTable.getValueAt(j, 21).toString());
					   stmt.setString(24,resultTable.getValueAt(j, 22).toString());
					   stmt.setString(25,resultTable.getValueAt(j, 23).toString());
					   
					   stmt.executeUpdate();
					   
					   ResultSet keyResultSet = stmt.getGeneratedKeys();
			            if (keyResultSet.next()) {
			            	outBag.put("TABLE",j,"ID", String.valueOf(keyResultSet.getInt(1)));
			            }
					   stmt.close(); 
					    
					}   
				
					conn.commit();					
				
		      } catch (SQLException e) {

					System.out.println(e.getMessage());
					conn.rollback();

				}finally{
				      //finally block used to close resources
				      try{
				         if(stmt!=null)
				            conn.close();
				      }catch(SQLException se){
				      }// do nothing
				      try{
				         if(conn!=null)
				            conn.close();
				      }catch(SQLException se){
				         se.printStackTrace();
				      }//end finally try
				   }

		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   return outBag;
		}//end main	
	
	public static ESIBag updateMarketingExpenses(JTable resultTable,String userName,int j,String id) throws Exception {

		  Connection conn = null;
		  PreparedStatement  stmt = null;
		  ESIBag outBag = new ESIBag();
				  
		   try{

		      Class.forName("com.mysql.jdbc.Driver");
		      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
		      conn.setAutoCommit(false);
		      
		      try{
		      		    	  
		    	  updateExpenseStatus(id, "-1", userName);
									  
				   stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_mcs.marketing_exp_main_info(`approve_status`"
				   		+",`company_code`,`entry_date`,`entry_user`,"
				   		/*+ "`approve_date`,`approve_user`," */
						+"`Country`,`Region`,`City`,`City_Region`,`Start_Date`,`End_Date`,`First_Stage`,`Second_Stage`,`Third_Stage`,`Exp1`,`Exp2`,`Exp3`,`Exp4`,"
				   		+ "`Exp_Count`,`Exp_Amount`,`comments`,`amount1`,`amount2`,`amount3`,`amount4`,`amount5`"
				   		+ ")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
				   stmt.setInt(1,1);
				   stmt.setString(2,resultTable.getValueAt(j, 2).toString());//company code
				   stmt.setTimestamp(3,Timestamp.valueOf(Util.getCurrentDateTime()));//Startd date
				   stmt.setString(4,userName);
				   
				   stmt.setString(5,resultTable.getValueAt(j, 7).toString());//countyr
				   stmt.setString(6,resultTable.getValueAt(j, 8).toString());
				   if(resultTable.getValueAt(j, 9) != null){
					   stmt.setString(7,resultTable.getValueAt(j, 9).toString());
				   }else{
					   stmt.setString(7,"");
				   }
				   if(resultTable.getValueAt(j, 10) != null){
					   stmt.setString(8,resultTable.getValueAt(j, 10).toString());
				   }else{
					   stmt.setString(8,"");
				   }
				   stmt.setDate(9,Date.valueOf(resultTable.getValueAt(j, 11).toString()));//Startd date
				   stmt.setDate(10,Date.valueOf(resultTable.getValueAt(j, 12).toString()));//End date
				   stmt.setString(11,resultTable.getValueAt(j, 13).toString());
				   stmt.setString(12,resultTable.getValueAt(j, 14).toString());
				   if(resultTable.getValueAt(j, 15) != null){
					   stmt.setString(13,resultTable.getValueAt(j, 15).toString());
				   }else{
					   stmt.setString(13,"");
				   }					   
				   stmt.setString(14,resultTable.getValueAt(j, 16).toString());//Exp1
				   stmt.setString(15,resultTable.getValueAt(j, 17).toString());
				   stmt.setString(16,resultTable.getValueAt(j, 18).toString());
				   stmt.setString(17,resultTable.getValueAt(j, 19).toString());
				   stmt.setInt(18,Integer.parseInt(resultTable.getValueAt(j, 20).toString().trim()));//Count
				   stmt.setString(19,resultTable.getValueAt(j, 21).toString());
				   stmt.setString(20,resultTable.getValueAt(j, 22).toString());
				   
				   stmt.setString(21,resultTable.getValueAt(j, 23).toString());//Amount1
				   stmt.setString(22,resultTable.getValueAt(j, 24).toString());
				   stmt.setString(23,resultTable.getValueAt(j, 25).toString());
				   stmt.setString(24,resultTable.getValueAt(j, 26).toString());
				   stmt.setString(25,resultTable.getValueAt(j, 27).toString());
				   
				   stmt.executeUpdate();
				   
				   ResultSet keyResultSet = stmt.getGeneratedKeys();
		            if (keyResultSet.next()) {
		            	outBag.put("TABLE",j,"ID", String.valueOf(keyResultSet.getInt(1)));
		            }
				   stmt.close(); 
				
				   conn.commit();					
				
		      } catch (SQLException e) {

					System.out.println(e.getMessage());
					conn.rollback();

				}finally{
				      //finally block used to close resources
				      try{
				         if(stmt!=null)
				            conn.close();
				      }catch(SQLException se){
				      }// do nothing
				      try{
				         if(conn!=null)
				            conn.close();
				      }catch(SQLException se){
				         se.printStackTrace();
				      }//end finally try
				   }

		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   return outBag;
		}//end main	
	
	public static void getExpenses(JTable resultTable) {

		String url = prop.getProperty("DB_URL");
        Connection conn = null;
        Statement stmt =null;
        //Vector columnNames = new Vector();
        //Vector data = new Vector();
        
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{
            conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"),prop.getProperty("PASS"));            
            String sorgu  = "select a.*,'false' as selecTable from solgar_mcs.marketing_exp_main_info a where approve_status = 1 order by id desc";
            stmt = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(sorgu);
            
          //Create new table model
            DefaultTableModel tableModel = new DefaultTableModel();

            //Retrieve meta data from ResultSet
            ResultSetMetaData metaData = rs.getMetaData();

            //Get number of columns from meta data
            int columnCount = metaData.getColumnCount();

            //Get all column names from meta data and add columns to table model
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
                tableModel.addColumn(metaData.getColumnLabel(columnIndex));
            }

            //Create array of Objects with size of column count from meta data
            Object[] row = new Object[columnCount];

            //Scroll through result set
            while (rs.next()){
                //Get object from column with specific index of result set to array of objects
                for (int i = 0; i < columnCount; i++){
                    row[i] = rs.getObject(i+1);
                }
                //Now add row to table model with that array of objects as an argument
                tableModel.addRow(row);
            }

            
            //Now add that table model to your table and you are done :D
            resultTable.setModel(tableModel);   
            
            stmt.close();
            conn.close();
        }
        catch(SQLException sqle){
            System.out.println("SQL Exception: " + sqle.getMessage());
        }finally{
  	      //finally block used to close resources
  	      try{
  	         if(stmt!=null)
  	            conn.close();
  	      }catch(SQLException se){
  	      }// do nothing
  	      try{
  	         if(conn!=null)
  	            conn.close();
  	      }catch(SQLException se){
  	         se.printStackTrace();
  	      }//end finally try
  	   }
	}
	
	public static ESIBag getExpensesForApprove() {

		String url = prop.getProperty("DB_URL");
        Connection conn = null;
        Statement stmt =null;
        DefaultTableModel tableModel = new DefaultTableModel();
        ESIBag outBag = new ESIBag();
        
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{
            conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"),prop.getProperty("PASS"));            
            String sorgu  = "select a.* from solgar_mcs.marketing_exp_main_info a where approve_status = 1 order by id desc";
            stmt = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(sorgu);

            int j =0;
            while (rs.next()){
            	outBag.put("TABLE",j,"id", rs.getString("id"));
            	outBag.put("TABLE",j,"approve_status", rs.getString("approve_status"));
            	outBag.put("TABLE",j,"company_code", rs.getString("company_code"));
            	outBag.put("TABLE",j,"entry_date", rs.getString("entry_date"));
            	outBag.put("TABLE",j,"entry_user", rs.getString("entry_user"));
            	outBag.put("TABLE",j,"approve_date", rs.getString("approve_date"));
            	outBag.put("TABLE",j,"approve_user", rs.getString("approve_user"));
            	outBag.put("TABLE",j,"Country", rs.getString("Country"));
            	outBag.put("TABLE",j,"Region", rs.getString("Region"));
            	outBag.put("TABLE",j,"City", rs.getString("City"));
            	outBag.put("TABLE",j,"City_Region", rs.getString("City_Region"));
            	outBag.put("TABLE",j,"Start_Date", rs.getString("Start_Date"));
            	outBag.put("TABLE",j,"End_Date", rs.getString("End_Date"));
            	outBag.put("TABLE",j,"First_Stage", rs.getString("First_Stage"));
            	
            	outBag.put("TABLE",j,"Second_Stage", rs.getString("Second_Stage"));
            	outBag.put("TABLE",j,"Third_Stage", rs.getString("Third_Stage"));
            	outBag.put("TABLE",j,"Exp1", rs.getString("Exp1"));
            	outBag.put("TABLE",j,"Exp2", rs.getString("Exp2"));
            	outBag.put("TABLE",j,"Exp3", rs.getString("Exp3"));
            	outBag.put("TABLE",j,"Exp4", rs.getString("Exp4"));
            	outBag.put("TABLE",j,"Exp_Count", rs.getString("Exp_Count"));
            	outBag.put("TABLE",j,"Exp_Amount", rs.getString("Exp_Amount"));
            	outBag.put("TABLE",j,"comments", rs.getString("comments"));
            	outBag.put("TABLE",j,"amount1", rs.getString("amount1"));
            	outBag.put("TABLE",j,"amount2", rs.getString("amount2"));
            	outBag.put("TABLE",j,"amount3", rs.getString("amount3"));
            	outBag.put("TABLE",j,"amount4", rs.getString("amount4"));
            	outBag.put("TABLE",j,"amount5", rs.getString("amount5"));
            	outBag.put("TABLE",j,"SELECT", "");
            	j++;
            }
            
            //Now add that table model to your table and you are done :D
            //resultTable.setModel(tableModel);   
            
            stmt.close();
            conn.close();
        }
        catch(SQLException sqle){
            System.out.println("SQL Exception: " + sqle.getMessage());
        }finally{
  	      //finally block used to close resources
  	      try{
  	         if(stmt!=null)
  	            conn.close();
  	      }catch(SQLException se){
  	      }// do nothing
  	      try{
  	         if(conn!=null)
  	            conn.close();
  	      }catch(SQLException se){
  	         se.printStackTrace();
  	      }//end finally try
  	   }
        
        return outBag;
	}
	
	public static ESIBag getExpensesForApproveWithPar(JTable resultTable,JComboBox country,JComboBox region,JComboBox companyName,JComboBox expMain,
			JComboBox expLevel1,JComboBox expLevel2,JComboBox eventDateStart,JComboBox eventDateEnd,JComboBox entryDateStart,JComboBox entryDateEnd) {

		String url = prop.getProperty("DB_URL");
        Connection conn = null;
        Statement stmt =null;
        ESIBag outBag = new ESIBag();
        
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{        
            conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"),prop.getProperty("PASS"));            
            String sorgu  = "select * from solgar_mcs.marketing_exp_main_info where approve_status = 1 ";   
            if(country.getSelectedItem() != null && country.getSelectedItem().toString().length()>0){sorgu = sorgu + "and country=" +"'"+country.getSelectedItem().toString()+"' ";}
            if(region.getSelectedItem()  != null && region.getSelectedItem().toString().length()>0){sorgu = sorgu + "and region=" +"'"+region.getSelectedItem().toString()+"' ";}
            if(companyName.getSelectedItem() != null && companyName.getSelectedItem().toString().length()>0){sorgu = sorgu + "and company_code=" +"'"+companyName.getSelectedItem().toString()+"' ";}
            if(expMain.getSelectedItem() != null && expMain.getSelectedItem().toString().length()>0){sorgu = sorgu + "and first_stage=" +"'"+expMain.getSelectedItem().toString()+"' ";}
            if(expLevel1.getSelectedItem() != null && expLevel1.getSelectedItem().toString().length()>0){sorgu = sorgu + "and second_stage=" +"'"+expLevel1.getSelectedItem().toString()+"' ";}
            if(expLevel2.getSelectedItem() != null && expLevel2.getSelectedItem().toString().length()>0){sorgu = sorgu + "and third_stage=" +"'"+expLevel2.getSelectedItem().toString()+"' ";}
            if(eventDateStart.getSelectedItem() != null && eventDateStart.getSelectedItem().toString().length()>0){sorgu = sorgu + "and start_date >= " +"'"+eventDateStart.getSelectedItem().toString()+"' ";}
            if(eventDateEnd.getSelectedItem() != null && eventDateEnd.getSelectedItem().toString().length()>0){sorgu = sorgu + "and start_date <= " +"'"+eventDateEnd.getSelectedItem().toString()+"' ";}
            if(entryDateStart.getSelectedItem() != null && entryDateStart.getSelectedItem().toString().length()>0){sorgu = sorgu + "and entry_date >= " +"'"+entryDateStart.getSelectedItem().toString()+"' ";}
            if(entryDateEnd.getSelectedItem() != null && entryDateEnd.getSelectedItem().toString().length()>0){sorgu = sorgu + "and entry_date <= " +"'"+entryDateEnd.getSelectedItem().toString()+"' ";}           
            sorgu = sorgu + "order by id desc";
            stmt = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(sorgu);

            int j =0;
            while (rs.next()){
            	outBag.put("TABLE",j,"id", rs.getString("id"));
            	outBag.put("TABLE",j,"approve_status", rs.getString("approve_status"));
            	outBag.put("TABLE",j,"company_code", rs.getString("company_code"));
            	outBag.put("TABLE",j,"entry_date", rs.getString("entry_date"));
            	outBag.put("TABLE",j,"entry_user", rs.getString("entry_user"));
            	outBag.put("TABLE",j,"approve_date", rs.getString("approve_date"));
            	outBag.put("TABLE",j,"approve_user", rs.getString("approve_user"));
            	outBag.put("TABLE",j,"Country", rs.getString("Country"));
            	outBag.put("TABLE",j,"Region", rs.getString("Region"));
            	outBag.put("TABLE",j,"City", rs.getString("City"));
            	outBag.put("TABLE",j,"City_Region", rs.getString("City_Region"));
            	outBag.put("TABLE",j,"Start_Date", rs.getString("Start_Date"));
            	outBag.put("TABLE",j,"End_Date", rs.getString("End_Date"));
            	outBag.put("TABLE",j,"First_Stage", rs.getString("First_Stage"));
            	
            	outBag.put("TABLE",j,"Second_Stage", rs.getString("Second_Stage"));
            	outBag.put("TABLE",j,"Third_Stage", rs.getString("Third_Stage"));
            	outBag.put("TABLE",j,"Exp1", rs.getString("Exp1"));
            	outBag.put("TABLE",j,"Exp2", rs.getString("Exp2"));
            	outBag.put("TABLE",j,"Exp3", rs.getString("Exp3"));
            	outBag.put("TABLE",j,"Exp4", rs.getString("Exp4"));
            	outBag.put("TABLE",j,"Exp_Count", rs.getString("Exp_Count"));
            	outBag.put("TABLE",j,"Exp_Amount", rs.getString("Exp_Amount"));
            	outBag.put("TABLE",j,"comments", rs.getString("comments"));
            	outBag.put("TABLE",j,"amount1", rs.getString("amount1"));
            	outBag.put("TABLE",j,"amount2", rs.getString("amount2"));
            	outBag.put("TABLE",j,"amount3", rs.getString("amount3"));
            	outBag.put("TABLE",j,"amount4", rs.getString("amount4"));
            	outBag.put("TABLE",j,"amount5", rs.getString("amount5"));
            	outBag.put("TABLE",j,"SELECT", "");
            	j++;
            }
            
            stmt.close();
            conn.close();
        }
        catch(SQLException sqle){
            System.out.println("SQL Exception: " + sqle.getMessage());
        }finally{
  	      //finally block used to close resources
  	      try{
  	         if(stmt!=null)
  	            conn.close();
  	      }catch(SQLException se){
  	      }// do nothing
  	      try{
  	         if(conn!=null)
  	            conn.close();
  	      }catch(SQLException se){
  	         se.printStackTrace();
  	      }//end finally try
  	   }
        
        return outBag;
	}
	
	public static void updateExpenseStatus(String id,String status,String userName) throws Exception {

		  Connection conn = null;
		  PreparedStatement  stmt = null;
		   try{
			   int statusInt =  Integer.parseInt(status);
			   int idInt =Integer.parseInt(id);

		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to a selected database...");
		      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		      
		      System.out.println("Connected database successfully...");

		      
		      //STEP 4: Execute a query
		      System.out.println("Creating first statement...");
		      stmt = (PreparedStatement) conn.prepareStatement( "update solgar_mcs.marketing_exp_main_info set approve_status = ?, approve_date=?, approve_user=?  WHERE id = ? order by id desc");
		      
		      stmt.setInt(1,statusInt);
		      stmt.setTimestamp(2,Timestamp.valueOf(Util.getCurrentDateTime()));
		      stmt.setString(3,userName);
		      stmt.setInt(4,idInt);
		      
		      stmt.executeUpdate();
		      stmt.close();
		      

		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   System.out.println("Goodbye!");
		}//end main	
	
	public static void getAllExpenses(JTable resultTable,int status,String country) {

		String url = prop.getProperty("DB_URL");
        Connection conn = null;
        PreparedStatement preparedStatement=null;
        String sorgu = "";
        ResultSet rs = null;
        //Vector columnNames = new Vector();
        //Vector data = new Vector();
        
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{
            conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"),prop.getProperty("PASS"));  
            if(country.length() > 0){
            	sorgu = "select * from solgar_mcs.marketing_exp_main_info where approve_status = ? and country = ? order by id desc";
            	 preparedStatement = (PreparedStatement) conn.prepareStatement(sorgu);
                 preparedStatement.setInt(1, status);
                 preparedStatement.setString(2, country);
                 rs = preparedStatement.executeQuery(); 
            }else{
            	sorgu = "select * from solgar_mcs.marketing_exp_main_info where approve_status = ? order by id desc";
            	preparedStatement = (PreparedStatement) conn.prepareStatement(sorgu);
                preparedStatement.setInt(1, status);
                rs = preparedStatement.executeQuery(); 
            }
                      
            
          //Create new table model
            DefaultTableModel tableModel = new DefaultTableModel();

            //Retrieve meta data from ResultSet
            ResultSetMetaData metaData = rs.getMetaData();

            //Get number of columns from meta data
            int columnCount = metaData.getColumnCount();

            //Get all column names from meta data and add columns to table model
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
               	tableModel.addColumn(metaData.getColumnLabel(columnIndex));            	
            }
            //tableModel.addColumn("Check Box");     
            //Create array of Objects with size of column count from meta data
            Object[] row = new Object[columnCount+1];

            //Scroll through result set
            while (rs.next()){
                //Get object from column with specific index of result set to array of objects
                for (int i = 0; i < columnCount; i++){
                    if(i==1){
                    	if(rs.getObject(i).equals(2)){
                    		row[i] = "Approved";
                    	}else if(rs.getObject(i).equals(3)){
                    		row[i] = "Rejected";
                    	}else{
                    		row[i] = "Waiting On Approval";
                    	}
                    }else{
                    	row[i] = rs.getObject(i+1);
                    }
                }
                //row[columnCount] = true;
                //Now add row to table model with that array of objects as an argument
                tableModel.addRow(row);
            }

            //Now add that table model to your table and you are done :D
            resultTable.setModel(tableModel);   
            
            preparedStatement.close();
            conn.close();
        }
        catch(SQLException sqle){
            System.out.println("SQL Exception: " + sqle.getMessage());
        }finally{
  	      //finally block used to close resources
  	      try{
  	         if(preparedStatement!=null)
  	            conn.close();
  	      }catch(SQLException se){
  	      }// do nothing
  	      try{
  	         if(conn!=null)
  	            conn.close();
  	      }catch(SQLException se){
  	         se.printStackTrace();
  	      }//end finally try
  	   }
	}
	
	public static void getExpsWithParameters(JTable resultTable,JComboBox country,JComboBox region,JComboBox companyName,JComboBox expMain,
			JComboBox expLevel1,JComboBox expLevel2,JComboBox eventDateStart,JComboBox eventDateEnd,JComboBox entryDateStart,JComboBox entryDateEnd) throws Exception {
		String url = prop.getProperty("DB_URL");
        Connection conn = null;
        Statement stmt =null;
        
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{
            conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"),prop.getProperty("PASS"));            
            String sorgu  = "select * from solgar_mcs.marketing_exp_main_info where approve_status in (1,2,3)";   
            if(country.getSelectedItem() != null && country.getSelectedItem().toString().length()>0){sorgu = sorgu + "and country=" +"'"+country.getSelectedItem().toString()+"' ";}
            if(region.getSelectedItem()  != null && region.getSelectedItem().toString().length()>0){sorgu = sorgu + "and region=" +"'"+region.getSelectedItem().toString()+"' ";}
            if(companyName.getSelectedItem() != null && companyName.getSelectedItem().toString().length()>0){sorgu = sorgu + "and company_code=" +"'"+companyName.getSelectedItem().toString()+"' ";}
            if(expMain.getSelectedItem() != null && expMain.getSelectedItem().toString().length()>0){sorgu = sorgu + "and first_stage=" +"'"+expMain.getSelectedItem().toString()+"' ";}
            if(expLevel1.getSelectedItem() != null && expLevel1.getSelectedItem().toString().length()>0){sorgu = sorgu + "and second_stage=" +"'"+expLevel1.getSelectedItem().toString()+"' ";}
            if(expLevel2.getSelectedItem() != null && expLevel2.getSelectedItem().toString().length()>0){sorgu = sorgu + "and third_stage=" +"'"+expLevel2.getSelectedItem().toString()+"' ";}
            if(eventDateStart.getSelectedItem() != null && eventDateStart.getSelectedItem().toString().length()>0){sorgu = sorgu + "and start_date >= " +"'"+eventDateStart.getSelectedItem().toString()+"' ";}
            if(eventDateEnd.getSelectedItem() != null && eventDateEnd.getSelectedItem().toString().length()>0){sorgu = sorgu + "and start_date <= " +"'"+eventDateEnd.getSelectedItem().toString()+"' ";}
            if(entryDateStart.getSelectedItem() != null && entryDateStart.getSelectedItem().toString().length()>0){sorgu = sorgu + "and entry_date >= " +"'"+entryDateStart.getSelectedItem().toString()+"' ";}
            if(entryDateEnd.getSelectedItem() != null && entryDateEnd.getSelectedItem().toString().length()>0){sorgu = sorgu + "and entry_date <= " +"'"+entryDateEnd.getSelectedItem().toString()+"' ";}           
            sorgu = sorgu + "order by id desc";
            
            
            stmt = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(sorgu);
            
          //Create new table model
            DefaultTableModel tableModel = new DefaultTableModel();

            //Retrieve meta data from ResultSet
            ResultSetMetaData metaData = rs.getMetaData();

            //Get number of columns from meta data
            int columnCount = metaData.getColumnCount();

            //Get all column names from meta data and add columns to table model
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
                tableModel.addColumn(metaData.getColumnLabel(columnIndex));
            }

            //Create array of Objects with size of column count from meta data
            Object[] row = new Object[columnCount];

            //Scroll through result set
            while (rs.next()){
                //Get object from column with specific index of result set to array of objects
                for (int i = 0; i < columnCount; i++){
                    row[i] = rs.getObject(i+1);
                    if(i==1){
                    	if(rs.getObject(i+1).equals(2)){
                    		row[i] = "Approved";
                    	}else if(rs.getObject(i+1).equals(3)){
                    		row[i] = "Rejected";
                    	}else{
                    		row[i] = "Waiting On Approval";
                    	}
                    }else{
                    	row[i] = rs.getObject(i+1);
                    }
                }
                //Now add row to table model with that array of objects as an argument
                tableModel.addRow(row);
            }

            //Now add that table model to your table and you are done :D
            resultTable.setModel(tableModel);   
            
            stmt.close();
            conn.close();
        }
        catch(SQLException sqle){
            System.out.println("SQL Exception: " + sqle.getMessage());
        }finally{
  	      //finally block used to close resources
  	      try{
  	         if(stmt!=null)
  	            conn.close();
  	      }catch(SQLException se){
  	      }// do nothing
  	      try{
  	         if(conn!=null)
  	            conn.close();
  	      }catch(SQLException se){
  	         se.printStackTrace();
  	      }//end finally try
  	   }
	}
}
