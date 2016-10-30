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
import com.toedter.calendar.JDateChooser;

import cb.esi.esiclient.util.ESIBag;
import jxl.write.DateTime;
import util.Util;

public class ConnectToDb {

	static final Properties prop = readConfFile();
	
	public static Properties readConfFile() {  	   
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
	
	public static void updatePassword(String pass,String empName) throws Exception {

		 Connection conn = null;
		 PreparedStatement  stmt = null;
		  try{

		     //STEP 2: Register JDBC driver
		     Class.forName("com.mysql.jdbc.Driver");
		     conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));
		     stmt = (PreparedStatement) conn.prepareStatement( "update solgar_org.employees set EMPLOYEE_PASSWORD =? where EMPLOYEE_NAME =?");
		     
		     stmt.setString(1,pass);
		     stmt.setString(2,empName);
		     
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
            String sorgu  = "select "+columnName+" from " +tableName +" order by "+columnName;
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
            	sorgu  = "select "+columnName+" from " +tableName + " where " + conditionColumn+ "='"+conditionValue+ "' group by "+columnName+" order by "+columnName ;
            }else{
            	sorgu  = "select "+columnName+" from " +tableName + " group by "+columnName +" order by "+columnName;
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
            	sorgu  = "select "+columnName+" from " +tableName + " where " + conditionColumn+ "='"+conditionValue+ "' and " + conditionColumn1+ "='"+conditionValue1+ "' group by "+columnName +" order by "+columnName;
            }else if(conditionValue.length()>0){
            	sorgu  = "select "+columnName+" from " +tableName + " where " + conditionColumn+ "='"+conditionValue+ "' group by "+columnName +" order by "+columnName;
            }else{
            	sorgu  = "select "+columnName+" from " +tableName + " group by "+columnName +" order by "+columnName;
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
	
	public static String getPassWithUserName(String user) throws Exception {

		String url = prop.getProperty("DB_URL");
		PreparedStatement preparedStatement=null;
        Connection conn =null;
        String employee_pass = "";
        
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{
            conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"), prop.getProperty("PASS"));            
            String sorgu  = "select * from solgar_org.employees where EMPLOYEE_NAME=?;";
            preparedStatement = (PreparedStatement)conn.prepareStatement(sorgu);
        	preparedStatement.setString(1, user);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
            	employee_pass = rs.getString("EMPLOYEE_PASSWORD");
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
        return employee_pass;
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
	
	public static ESIBag getMenuUserRelation(String userName) throws Exception {

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
            
            selectSQL = "select * from solgar_org.menu_user_relation where employee_name = ?";//getQueryScript(reportName);            
            preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
            preparedStatement.setString(1, userName);            
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){    	
                outBag.put("BRAND", rs.getString("brand"));
                outBag.put("COUNTRY", rs.getString("country"));
                outBag.put("AREA", rs.getString("area"));
                outBag.put("SALESENTRY", rs.getString("sales_entry"));
                outBag.put("SALESOBSERVATION", rs.getString("sales_observation"));
                outBag.put("EXPSENTRY", rs.getString("exps_entry"));
                outBag.put("EXPSUPDATE", rs.getString("exps_update"));
                outBag.put("EXPSOBSERVATION", rs.getString("exps_observation"));
                outBag.put("EXPSAPPROVE", rs.getString("exps_approve"));
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
	
	
	public static void saveMarktExps(JTable table,String userName) throws Exception {  	   	
		try {			
				
				MarketingExpsInfo exps = new MarketingExpsInfo();
				exps.save(table,userName);
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static void deleteMarktExps(String id,String userName,String status)throws Exception {  	   	
		try {			
				
				MarketingExpsInfo exps = new MarketingExpsInfo();
				exps.statusUpdate(id,userName,status);
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static void statuChangeMarktExps(String id,String userName,String status)throws Exception {  	   	
		try {			
				
				MarketingExpsInfo exps = new MarketingExpsInfo();
				exps.statusUpdate(id,userName,status);
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static void updateMarktExps(JTable table,String userName,int index) throws Exception{  	   	
		try {			
				
				MarketingExpsInfo exps = new MarketingExpsInfo();
				exps.update(table,userName,index);
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static ESIBag getMarktExpsWithParam(JComboBox country,JComboBox area,JComboBox companyName,
			JComboBox expMain,JComboBox expLevel1,JComboBox expLevel2,JComboBox eventDateStart,
			JComboBox eventDateEnd,JComboBox entryDateStart,JComboBox entryDateEnd,JComboBox approveStatus,
			JComboBox organizator,JDateChooser eventDateDayBegin,JDateChooser eventDateDayEnd) {  	   	
		
		ESIBag outBag = new ESIBag();
		String countryStr,areaStr,companyNameStr,expMainStr,expLevel1Str,expLevel2Str,
		eventDateStartStr,eventDateEndStr,entryDateStartStr,entryDateEndStr,organizatorStr;
		String statusStr="";
		SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
		
		try {			
			if(country.getSelectedItem() != null 
					&& country.getSelectedItem().toString().length()>0){
				countryStr = country.getSelectedItem().toString();
			}else{
				countryStr ="";
			}
			if(area.getSelectedItem() != null 
					&& area.getSelectedItem().toString().length()>0){
				areaStr = area.getSelectedItem().toString();
			}else{
				areaStr ="";
			}
			if(companyName.getSelectedItem() != null 
					&& companyName.getSelectedItem().toString().length()>0){
				companyNameStr = companyName.getSelectedItem().toString();
			}else{
				companyNameStr ="";
			}
			if(expMain.getSelectedItem() != null 
					&& expMain.getSelectedItem().toString().length()>0){
				expMainStr = expMain.getSelectedItem().toString();
			}else{
				expMainStr ="";
			}
			if(expLevel1.getSelectedItem() != null 
					&& expLevel1.getSelectedItem().toString().length()>0){
				expLevel1Str = expLevel1.getSelectedItem().toString();
			}else{
				expLevel1Str ="";
			}
			if(expLevel2.getSelectedItem() != null 
					&& expLevel2.getSelectedItem().toString().length()>0){
				expLevel2Str = expLevel2.getSelectedItem().toString();
			}else{
				expLevel2Str ="";
			}
			if(eventDateStart.getSelectedItem() != null 
					&& eventDateStart.getSelectedItem().toString().length()>0){
				eventDateStartStr = eventDateStart.getSelectedItem().toString();
			}else{
				eventDateStartStr ="";
			}
			if(eventDateEnd.getSelectedItem() != null 
					&& eventDateEnd.getSelectedItem().toString().length()>0){
				eventDateEndStr = eventDateEnd.getSelectedItem().toString();
			}else{
				eventDateEndStr ="";
			}
			if(eventDateDayBegin!= null && eventDateDayBegin.getDate() != null 
					&& eventDateDayBegin.getDate().toString().length()>0){
				eventDateStartStr = dcn.format(eventDateDayBegin.getDate());
			}else{
				if(eventDateStartStr.trim().length() ==0){
					eventDateStartStr ="";
				}
			}
			if(eventDateDayEnd!= null && eventDateDayEnd.getDate() != null 
					&& eventDateDayEnd.getDate().toString().length()>0){
				eventDateEndStr = dcn.format(eventDateDayEnd.getDate());
			}else{
				if(eventDateEndStr.trim().length() ==0){
					eventDateEndStr ="";
				}
			}
			if(entryDateStart.getSelectedItem() != null 
					&& entryDateStart.getSelectedItem().toString().length()>0){
				entryDateStartStr = entryDateStart.getSelectedItem().toString();
			}else{
				entryDateStartStr ="";
			}
			if(entryDateEnd.getSelectedItem() != null 
					&& entryDateEnd.getSelectedItem().toString().length()>0){
				entryDateEndStr = entryDateEnd.getSelectedItem().toString();
			}else{
				entryDateEndStr ="";
			}
			if(organizator != null && organizator.getSelectedItem() != null 
					&& organizator.getSelectedItem().toString().length()>0){
				organizatorStr = organizator.getSelectedItem().toString();
			}else{
				organizatorStr ="";
			}
			if(approveStatus.getSelectedItem() != null 
					&& approveStatus.getSelectedItem().toString().length()>0){
				if (approveStatus.getSelectedItem().toString().equalsIgnoreCase("Waiting On Approval")){
					statusStr = "(1)";
				}else if (approveStatus.getSelectedItem().toString().equalsIgnoreCase("Approved")){
					statusStr = "(2)";
				}else if (approveStatus.getSelectedItem().toString().equalsIgnoreCase("Rejected")){
					statusStr = "(3)";
				}				
			}else{
				statusStr ="(1,2,3)";
			}
			
			MarketingExpsInfo exps = new MarketingExpsInfo();
			outBag = exps.getExpsWithParameters(countryStr,areaStr,companyNameStr,expMainStr,expLevel1Str,expLevel2Str,
					eventDateStartStr,eventDateEndStr,entryDateStartStr,entryDateEndStr,statusStr,organizatorStr);
			
       } catch (Exception e) {
           e.printStackTrace();
       }	
		
		return outBag;
  }
}
