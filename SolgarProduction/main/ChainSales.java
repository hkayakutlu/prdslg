package main;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JTable;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class ChainSales {
	static final Properties prop = ConnectToDb.readConfFile();
	
	public ChainSales() {
		// TODO Auto-generated constructor stub
	   }
	
	public static void save(JTable resultTable) throws Exception {

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
						
						if(productName.toUpperCase().indexOf("аюсмрх")>=0 || productName.toUpperCase().indexOf("BOUNTY")>=0
								|| productName.toUpperCase().indexOf("ма ")>=0){
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
	
}
