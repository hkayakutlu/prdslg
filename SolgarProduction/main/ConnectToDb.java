package main;

import java.io.FileInputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import cb.esi.esiclient.util.ESIBag;

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
	
	


	
	
	
}
