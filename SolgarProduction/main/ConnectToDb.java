package main;

import java.io.FileInputStream;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JComboBox;
import javax.swing.JTable;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import cb.esi.esiclient.util.ESIBag;

public class ConnectToDb {

	// JDBC driver name and database URL
	   //static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   //static final String DB_URL = "jdbc:mysql://localhost:3306/?useSSL=true";//"jdbc:mysql://127.0.0.1:3306";
	  // static final String DB_URL_UPDATE = "jdbc:mysql://127.0.0.1:3306?useSSL=true";

	   //  Database credentials
	   //private static final String USER = "root";
	   //private static final String PASS = "admin";
	static final Properties prop = readConfFile();
	 
	private static Properties readConfFile() {  	   
		Properties prop = new Properties();		
			try {			
				prop.load(new FileInputStream("conf/config.properties"));
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
	
	public static ESIBag repGetTotalCounts(String selectedCompany,String selectedDate,String reportName) throws Exception {

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
            	selectSQL = selectSQL.replace("sales_date = ? and", "");     
            	preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
            	preparedStatement.setString(1, selectedCompany);
            }else if(selectedDate.length()>0){
            	selectSQL = selectSQL.replace("and main_group = ?", "");
            	preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
            	preparedStatement.setDate(1, Date.valueOf(selectedDate));
            }        	

            ResultSet rs = preparedStatement.executeQuery();
            
            int i =0;
            while (rs.next()){    	
                outBag.put("TABLE",i,"MAINGROUP", rs.getString("main_group"));
                outBag.put("TABLE",i,"SALESCOUNT", rs.getString("salescount"));
                outBag.put("TABLE",i,"SALESDATE", String.valueOf(rs.getDate("Sales_Date")));
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
	
	
}
