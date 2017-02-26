package main;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import util.Util;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import cb.esi.esiclient.util.ESIBag;

public class Parameter {
	
	Properties prop = ConnectToDb.readConfFile();

	public Parameter() {
		// TODO Auto-generated constructor stub
	   }
	public void save(ESIBag inBag) throws Exception{
		PreparedStatement  stmt = null;	
		   Connection conn = null;
			  try{
				  Class.forName("com.mysql.jdbc.Driver");
			      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
			      conn.setAutoCommit(false);
				  
			      int row = inBag.getSize("TABEL");
					for (int j = 0; j  < row; j++) {
						if(inBag.get("TABEL",j,"ID") != null && inBag.get("TABEL",j,"ID").toString().trim().equalsIgnoreCase("2")){
					
							stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_org.employees(`status`,`EMPLOYEE_TABEL_NOMER`,`EMPLOYEE_UNIT`,`EMPLOYEE_TITLE`,`EMPLOYEE_NAME`,"
							   		+ "`EMPLOYEE_ACTIVE`,`EMPLOYEE_START_DATE`,`EMPLOYEE_END_DATE`,`EMPLOYEE_EMAIL`,`EMPLOYEE_PASSWORD`,`EMPLOYEE_ADD_STATUS`,`EMPLOYEE_BIRTH_DATE`,`entryUser`,`entryDate`,`company`)"
							   		+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);				   					   
						
						   stmt.setInt(1,1);//Status
						   stmt.setString(2,inBag.get("TABEL",j,"TABELNOMER"));
						   stmt.setString(3,inBag.get("TABEL",j,"UNIT"));
						   stmt.setString(4,inBag.get("TABEL",j,"TITLE"));
						   stmt.setString(5,inBag.get("TABEL",j,"NAME"));
						   stmt.setString(6,inBag.get("TABEL",j,"ACTIVENESS"));
						   stmt.setDate(7,Date.valueOf(inBag.get("TABEL",j,"STARTDATE")));
						   stmt.setDate(8,Date.valueOf( inBag.get("TABEL",j,"ENDDATE").toString().length() >0 ? inBag.get("TABEL",j,"ENDDATE") : "2099-01-01"));//
						   stmt.setString(9,inBag.get("TABEL",j,"EMAIL"));
						   stmt.setString(10,inBag.get("TABEL",j,"PASSWORD"));	
						   stmt.setString(11,inBag.get("TABEL",j,"ADDITIONALSTATUS"));
						   stmt.setDate(12,Date.valueOf(inBag.get("TABEL",j,"BIRTHDATE")));
						   stmt.setString(13,inBag.get("TABEL",j,"USER"));
						   stmt.setTimestamp(14,Timestamp.valueOf(Util.getCurrentDateTime()));
						   stmt.setString(15,inBag.get("TABEL",j,"COMPANY"));
						   
						   stmt.executeUpdate();
						   stmt.close(); 		
						}   
					    
					}   
					conn.commit();
				 
				  try{
					  conn.commit();
				  }catch (SQLException e) {
					  conn.rollback();
					  throw e;
				  }catch (Exception e) {
					  conn.rollback();
					  throw e;
				}
				  
			  } catch (SQLException e) {
					System.out.println(e.getMessage());
					conn.rollback();
					throw e;
				}finally{
			      try{
			         if(conn!=null)
			            conn.close();
			      }catch(SQLException se){
			    	  conn.rollback();
			    	  throw se;
			      }//end finally try
				   }
	}
	
	public void update(ESIBag inBag) throws Exception{
		
	}
	
}