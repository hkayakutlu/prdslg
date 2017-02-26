package main;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import javax.swing.JTable;

import util.Util;
import cb.esi.esiclient.util.ESIBag;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class Employee {

	   private int ID;
	   private String TabelNomer;
	   private String Unit;
	   private String Title;
	   private String NameSurname;
	   private String Activeness;
	   private Date Start_Date;
	   private Date End_Date;
	   private String EMail;
	   private String Password;
	   private String AdditionalStatus;
	   private Date BirthDate;
	   private String EntryUser;
	   private Date EntryDate;
	   Properties prop = ConnectToDb.readConfFile();
	   
	   public Employee() {
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
						if(inBag.get("TABEL",j,"ID") != null && inBag.get("TABEL",j,"ID").toString().trim().equalsIgnoreCase("0")){
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
		   Connection conn = null;
			  try{
				  Class.forName("com.mysql.jdbc.Driver");
			      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
			      conn.setAutoCommit(false);
				  
			      delete(conn,inBag);
			      insert(conn,inBag);
				 
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
	   
	   public void insert(Connection conn,ESIBag inBag) throws Exception{
		   PreparedStatement  stmt = null;
		   try{
	                   
			   stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_org.employees(`status`,`EMPLOYEE_TABEL_NOMER`,`EMPLOYEE_UNIT`,`EMPLOYEE_TITLE`,`EMPLOYEE_NAME`,"
				   		+ "`EMPLOYEE_ACTIVE`,`EMPLOYEE_START_DATE`,`EMPLOYEE_END_DATE`,`EMPLOYEE_EMAIL`,`EMPLOYEE_PASSWORD`,`EMPLOYEE_ADD_STATUS`,`EMPLOYEE_BIRTH_DATE`,`entryUser`,`entryDate`,`company`)"
				   		+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);				   					   
			
			   stmt.setInt(1,1);//Status
			   stmt.setString(2,inBag.get("TABELNOMER"));
			   stmt.setString(3,inBag.get("UNIT"));
			   stmt.setString(4,inBag.get("TITLE"));
			   stmt.setString(5,inBag.get("NAME"));
			   stmt.setString(6,inBag.get("ACTIVENESS"));
			   stmt.setDate(7,Date.valueOf(inBag.get("STARTDATE")));
			   stmt.setDate(8,Date.valueOf(inBag.get("ENDDATE")));
			   stmt.setString(9,inBag.get("EMAIL"));
			   stmt.setString(10,inBag.get("PASSWORD"));	
			   stmt.setString(11,inBag.get("ADDITIONALSTATUS"));
			   stmt.setDate(12,Date.valueOf(inBag.get("BIRTHDATE")));
			   stmt.setString(13,inBag.get("USER"));
			   stmt.setTimestamp(14,Timestamp.valueOf(Util.getCurrentDateTime()));
			   stmt.setString(15,inBag.get("COMPANY"));
			   
			   stmt.executeUpdate();
			   stmt.close();
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
	   
	   private void delete(Connection conn,ESIBag inBag) throws Exception, SQLException{
			PreparedStatement  stmt = null;				  
			   try{
			      int Id = Integer.parseInt(inBag.get("ID").toString());
		
			      stmt = (PreparedStatement) conn.prepareStatement( "update solgar_org.employees set status = 0 where id =?");			      
			      stmt.setInt(1,Id);
			      
			      stmt.executeUpdate();
			      stmt.close(); 							
					
			      }catch(SQLException se){
				      throw se;
				   }catch(Exception e){
				      e.printStackTrace();
				      throw e;
				   }finally{}//end try			
		
		}
	   
	   public ESIBag get(ESIBag inBag) throws Exception{
		   
			  try{				 						  
			      inBag = setDataToTableBag(inBag);			  
			  } catch (SQLException e) {
					System.out.println(e.getMessage());					
					throw e;
				}
			  return inBag;
	   
	   }
	   public ESIBag setDataToTableBag(ESIBag inBag) throws Exception{
		   Statement stmt =null;
	       ESIBag outBag = new ESIBag();
	       Connection conn = null;
		   try{
	           
			   Class.forName("com.mysql.jdbc.Driver");
			   conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));
	           String sorgu  = "select * from solgar_org.employees where status = 1 order by id desc";          
	           stmt = (Statement) conn.createStatement();
	           ResultSet rs = stmt.executeQuery(sorgu);
	
	           int j =0;
	           while (rs.next()){	  
	        	   outBag.put("TABEL",j,"ID", String.valueOf(rs.getInt("id")));
	        	   outBag.put("TABEL",j,"COMPANY", rs.getString("company"));
	        	   outBag.put("TABEL",j,"TABELNOMER", rs.getString("EMPLOYEE_TABEL_NOMER"));
	        	   outBag.put("TABEL",j,"UNIT", rs.getString("EMPLOYEE_UNIT"));
	        	   outBag.put("TABEL",j,"TITLE", rs.getString("EMPLOYEE_TITLE"));
	        	   outBag.put("TABEL",j,"NAME", rs.getString("EMPLOYEE_NAME"));
	        	   outBag.put("TABEL",j,"ACTIVENESS", rs.getString("EMPLOYEE_ACTIVE"));
	        	   outBag.put("TABEL",j,"STARTDATE", rs.getString("EMPLOYEE_START_DATE"));
	        	   outBag.put("TABEL",j,"ENDDATE", rs.getString("EMPLOYEE_END_DATE"));
	        	   outBag.put("TABEL",j,"EMAIL", rs.getString("EMPLOYEE_EMAIL"));
	        	   outBag.put("TABEL",j,"PASSWORD", rs.getString("EMPLOYEE_PASSWORD"));
	        	   outBag.put("TABEL",j,"ADDITIONALSTATUS", rs.getString("EMPLOYEE_ADD_STATUS"));
	        	   outBag.put("TABEL",j,"BIRTHDATE", rs.getString("EMPLOYEE_BIRTH_DATE"));	 
	        	   outBag.put("TABEL",j,"ENTRYUSER", rs.getString("entryUser"));
	        	   outBag.put("TABEL",j,"ENTRYDATE", rs.getString("entryDate"));	        	   
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
	   
	   public int getID()  {
	       return ID;
	   }
	   public void setID(int ID)  {
	       this.ID = ID;
	   }
	   public String getTabelNomer()  {
	       return TabelNomer;
	   }
	   public void setTabelNomer(String TabelNomer)  {
	       this.TabelNomer = TabelNomer;
	   }
	   public String getUnit()  {
	       return Unit;
	   }
	   public void setUnit(String Unit)  {
	       this.Unit = Unit;
	   }
	   public String getTitle()  {
	       return Title;
	   }
	   public void setTitle(String Title)  {
	       this.Title = Title;
	   }
	   public String getNameSurname()  {
	       return NameSurname;
	   }
	   public void setNameSurname(String NameSurname)  {
	       this.NameSurname = NameSurname;
	   }
	   public String getActiveness()  {
	       return Activeness;
	   }
	   public void setActiveness(String Activeness)  {
	       this.Activeness = Activeness;
	   }
	   public Date getStart_Date()  {
	       return Start_Date;
	   }
	   public void setStart_Date(Date Start_Date)  {
	       this.Start_Date = Start_Date;
	   }
	   public Date getEnd_Date()  {
	       return End_Date;
	   }
	   public void setEnd_Date(Date End_Date)  {
	       this.End_Date = End_Date;
	   }
	   public String getEMail()  {
	       return EMail;
	   }
	   public void setEMail(String EMail)  {
	       this.EMail = EMail;
	   }
	   public String getPassword()  {
	       return Password;
	   }
	   public void setPassword(String Password)  {
	       this.Password = Password;
	   }
	   public String getAdditionalStatus()  {
	       return AdditionalStatus;
	   }
	   public void setAdditionalStatus(String AdditionalStatus)  {
	       this.AdditionalStatus = AdditionalStatus;
	   }
	   public Date getBirthDate()  {
	       return BirthDate;
	   }
	   public void setAdditionalStatus(Date BirthDate)  {
	       this.BirthDate = BirthDate;
	   }
	   public String getEntryUser()  {
	       return EntryUser;
	   }
	   public void setEntryUser(String EntryUser)  {
	       this.EntryUser = EntryUser;
	   }
	   public Date getEntryDate()  {
	       return EntryDate;
	   }
	   public void setEntryDate(Date EntryDate)  {
	       this.EntryDate = EntryDate;
	   }
}