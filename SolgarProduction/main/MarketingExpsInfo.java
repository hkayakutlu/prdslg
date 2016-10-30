package main;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;
import java.math.BigDecimal;
import java.sql.Date;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import util.Util;
import cb.esi.esiclient.util.ESIBag;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import jxl.write.DateTime;

public class MarketingExpsInfo {

   private int ID;
   private int Approval_Status;
   private String Company;
   private String Country;
   private String Area;
   private String Region;
   private String City;
   private String First_Stage;
   private String Second_Stage;
   private Date Start_Date;
   private Date End_Date;
   private int Total_Count;
   private String Total_Sum;
   private String Conf_Food_Sum;
   private String Org_Exps_Sum;
   private String Travel_Agency_Sum;
   private String Key_Person_Sum;
   private String Add_Exp_Sum;  
   private String Chain;
   private String Contracter;
   private String Lecturer;
   private String Organizer;
   private String Clinic_Name;
   private String Key_Leader;
   private String Product_Name;
   private String Tema;
   private String Attenders_Count;
   private String Conditions;
   private String Statuses;
   private String Comment;
   private String Third_Stage;
   private String Contracter1;
   private String Org_Exps_Sum1;
   private String Key_Leader1;
   private String Key_Person_Sum1;
   private DateTime entry_date;
   private String entry_user;
   private DateTime approve_date;
   private String approve_user;
   
   Properties prop = ConnectToDb.readConfFile();
   private static final DecimalFormat DF = new DecimalFormat();
   

   public MarketingExpsInfo() {
	// TODO Auto-generated constructor stub
   }


   public void save(JTable resultTable,String userName) throws Exception{

	   	  Connection conn = null;
		  PreparedStatement  stmt = null;
				  
		   try{

		      Class.forName("com.mysql.jdbc.Driver");
		      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
		      conn.setAutoCommit(false);
		      
		      try{
		      		    	  
			      int row = resultTable.getRowCount();
					for (int j = 0; j  < row; j++) {
					   stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_mcs.markt_exp_info(`Approval_Status`,`Company`,`Country`,`Area`,`Region`,"
						   		+ "`City`,`First_Stage`,`Second_Stage`,`Start_Date`,`End_Date`,`Total_Count`,`Total_Sum`,`Conf_Food_Sum`,`Org_Exps_Sum`,"
						   		+ "`Travel_Agency_Sum`,`Key_Person_Sum`,`Add_Exp_Sum`,`Chain`,`Contracter`,`Lecturer`,`Organizer`,`Clinic_Name`,`Key_Leader`,"
						   		+ "`Product_Name`,`Tema`,`Attenders_Count`,`Conditions`,`Statuses`,`Comments`,`Third_Stage`,`Contracter1`,`Org_Exps_Sum1`,`Key_Leader1`,`Key_Person_Sum1`,"
						   		+ "`entry_date`,`entry_user`)"
						   		+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);				   
					   
					   setData(resultTable, j);					   
					   stmt.setInt(1,1);//Approval_Status
					   setDataToStatement(stmt);					   											   
					   stmt.setTimestamp(35,Timestamp.valueOf(Util.getCurrentDateTime()));//Startd date
					   stmt.setString(36,userName);					   
					   stmt.executeUpdate();
					   stmt.close(); 
					    
					}   
				
					conn.commit();					
				
		      } catch (SQLException e) {

					System.out.println(e.getMessage());
					conn.rollback();
					throw e;

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
		      throw se;
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
		}//end main	
   
   public ESIBag getExpsWithParameters(String country,String area,String companyName,String expMain,
		   String expLevel1,String expLevel2,String eventDateStart,String eventDateEnd,String entryDateStart,
		   String entryDateEnd,String approvalStatus,String organizator) throws Exception {
		
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
           String sorgu  = "select * from solgar_mcs.markt_exp_info where 1=1 "; 
           if(approvalStatus.toString().length()>0){sorgu = sorgu + "and approval_status in" +approvalStatus.toString()+" ";}
           if(country.toString().length()>0){sorgu = sorgu + "and Country=" +"'"+country.toString()+"' ";}
           if(area.toString().length()>0){sorgu = sorgu + "and Area=" +"'"+area.toString()+"' ";}
           if(companyName.toString().length()>0){sorgu = sorgu + "and Company=" +"'"+companyName.toString()+"' ";}
           if(expMain.toString().length()>0){sorgu = sorgu + "and First_Stage=" +"'"+expMain.toString()+"' ";}
           if(expLevel1.toString().length()>0){sorgu = sorgu + "and Second_Stage=" +"'"+expLevel1.toString()+"' ";}
           if(expLevel2.toString().length()>0){sorgu = sorgu + "and Third_Stage=" +"'"+expLevel2.toString()+"' ";}
           if(eventDateStart.toString().length()>0){sorgu = sorgu + "and Start_Date >= " +"'"+eventDateStart.toString()+"' ";}
           if(eventDateEnd.toString().length()>0){sorgu = sorgu + "and End_Date <= " +"'"+eventDateEnd.toString()+"' ";}
           if(entryDateStart.toString().length()>0){sorgu = sorgu + "and entry_date >= " +"'"+entryDateStart.toString()+"' ";}
           if(entryDateEnd.toString().length()>0){sorgu = sorgu + "and entry_date <= " +"'"+entryDateEnd.toString()+"' ";}    
           if(organizator.toString().length()>0){sorgu = sorgu + "and Organizer = " +"'"+organizator.toString()+"' ";}  
           sorgu = sorgu + "order by id desc";           
           
           stmt = (Statement) conn.createStatement();
           ResultSet rs = stmt.executeQuery(sorgu);

           int j =0;
           while (rs.next()){	   
	           	setToBag(outBag, rs, j);
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
   
	public void statusUpdate(String id,String userName,String status) throws Exception {

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
		      stmt = (PreparedStatement) conn.prepareStatement( "update solgar_mcs.markt_exp_info set approval_status = ?, approve_date=?, approve_user=?  WHERE id = ? order by id desc");
		      
		      stmt.setInt(1,statusInt);
		      stmt.setTimestamp(2,Timestamp.valueOf(Util.getCurrentDateTime()));
		      stmt.setString(3,userName);
		      stmt.setInt(4,idInt);
		      
		      stmt.executeUpdate();
		      stmt.close();
		      

		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		      throw se;
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		      throw e;
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
	
	public void update(JTable resultTable,String userName,int j) throws Exception {

		   Connection conn = null;
			  PreparedStatement  stmt = null;
					  
			   try{

			      Class.forName("com.mysql.jdbc.Driver");
			      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
			      conn.setAutoCommit(false);
			      
			      try{
			      		    	  

					   stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_mcs.markt_exp_info(`Approval_Status`,`Company`,`Country`,`Area`,`Region`,"
						   		+ "`City`,`First_Stage`,`Second_Stage`,`Start_Date`,`End_Date`,`Total_Count`,`Total_Sum`,`Conf_Food_Sum`,`Org_Exps_Sum`,"
						   		+ "`Travel_Agency_Sum`,`Key_Person_Sum`,`Add_Exp_Sum`,`Chain`,`Contracter`,`Lecturer`,`Organizer`,`Clinic_Name`,`Key_Leader`,"
						   		+ "`Product_Name`,`Tema`,`Attenders_Count`,`Conditions`,`Statuses`,`Comments`,`Third_Stage`,`Contracter1`,`Org_Exps_Sum1`,`Key_Leader1`,`Key_Person_Sum1`,"
						   		+ "`approve_date`,`approve_user`)"
						   		+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);				   
					   
					   setData(resultTable, j);					   
					   stmt.setInt(1,1);//Approval_Status
					   setDataToStatement(stmt);					   						
					   
					   stmt.setTimestamp(35,Timestamp.valueOf(Util.getCurrentDateTime()));//Startd date
					   stmt.setString(36,userName);					   
					   stmt.executeUpdate();
					   
					   /*ResultSet keyResultSet = stmt.getGeneratedKeys();
			            if (keyResultSet.next()) {
			            	outBag.put("TABLE",j,"ID", String.valueOf(keyResultSet.getInt(1)));
			            }*/
					   stmt.close(); 
					   conn.commit();					
					
			      } catch (SQLException e) {
						conn.rollback();
						throw e;

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
			      throw se;
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
			}//end main	


private void setToBag(ESIBag outBag, ResultSet rs, int j) throws SQLException {
	outBag.put("TABLE",j,"id", rs.getString("id"));	           	
	if(rs.getInt("approval_status")==2){
		outBag.put("TABLE",j,"approval_status", "Approved");
	}else if(rs.getInt("approval_status")==3){
		outBag.put("TABLE",j,"approval_status", "Rejected");
	}else{
		outBag.put("TABLE",j,"approval_status", "Waiting On Approval");
	}
	
	outBag.put("TABLE",j,"Company", rs.getString("Company"));
	outBag.put("TABLE",j,"Country", rs.getString("Country"));
	outBag.put("TABLE",j,"Area", rs.getString("Area"));
	outBag.put("TABLE",j,"Region", rs.getString("Region"));
	outBag.put("TABLE",j,"City", rs.getString("City"));
	outBag.put("TABLE",j,"First_Stage", rs.getString("First_Stage"));
	outBag.put("TABLE",j,"Second_Stage", rs.getString("Second_Stage"));
	outBag.put("TABLE",j,"Start_Date", rs.getString("Start_Date"));
	outBag.put("TABLE",j,"End_Date", rs.getString("End_Date"));	           	
	outBag.put("TABLE",j,"Total_Count", rs.getString("Total_Count"));
	outBag.put("TABLE",j,"Total_Sum", rs.getString("Total_Sum"));	           	
	outBag.put("TABLE",j,"Conf_Food_Sum", rs.getString("Conf_Food_Sum"));
	outBag.put("TABLE",j,"Org_Exps_Sum", rs.getString("Org_Exps_Sum"));
	outBag.put("TABLE",j,"Travel_Agency_Sum", rs.getString("Travel_Agency_Sum"));
	outBag.put("TABLE",j,"Key_Person_Sum", rs.getString("Key_Person_Sum"));	
	outBag.put("TABLE",j,"Add_Exp_Sum", rs.getString("Add_Exp_Sum"));
	outBag.put("TABLE",j,"Chain", rs.getString("Chain"));
	outBag.put("TABLE",j,"Contracter", rs.getString("Contracter"));
	outBag.put("TABLE",j,"Lecturer", rs.getString("Lecturer"));
	
	outBag.put("TABLE",j,"Organizer", rs.getString("Organizer"));
	outBag.put("TABLE",j,"Clinic_Name", rs.getString("Clinic_Name"));
	outBag.put("TABLE",j,"Key_Leader", rs.getString("Key_Leader"));
	outBag.put("TABLE",j,"Product_Name", rs.getString("Product_Name"));
	outBag.put("TABLE",j,"Tema", rs.getString("Tema"));
	outBag.put("TABLE",j,"Attenders_Count", rs.getString("Attenders_Count"));
	outBag.put("TABLE",j,"Conditions", rs.getString("Conditions"));
	outBag.put("TABLE",j,"Statuses", rs.getString("Statuses"));
	outBag.put("TABLE",j,"Comments", rs.getString("Comments"));
	outBag.put("TABLE",j,"Third_Stage", rs.getString("Third_Stage"));
	outBag.put("TABLE",j,"Contracter1", rs.getString("Contracter1"));
	outBag.put("TABLE",j,"Org_Exps_Sum1", rs.getString("Org_Exps_Sum1"));
	outBag.put("TABLE",j,"Key_Leader1", rs.getString("Key_Leader1"));
	outBag.put("TABLE",j,"Key_Person_Sum1", rs.getString("Key_Person_Sum1"));
	
	outBag.put("TABLE",j,"entry_date", rs.getString("entry_date"));
	outBag.put("TABLE",j,"entry_user", rs.getString("entry_user"));	           	
	outBag.put("TABLE",j,"approve_date", rs.getString("approve_date"));
	outBag.put("TABLE",j,"approve_user", rs.getString("approve_user"));
	outBag.put("TABLE",j,"SELECT", "");
}

private void setDataToStatement(PreparedStatement stmt) throws SQLException {
	stmt.setString(2,getCompany());
	   stmt.setString(3,getCountry());
	   stmt.setString(4,getArea());
	   stmt.setString(5,getRegion());
	   stmt.setString(6,getCity());
	   stmt.setString(7,getFirst_Stage());
	   stmt.setString(8,getSecond_Stage());
	   stmt.setDate(9,getStart_Date());
	   stmt.setDate(10,getEnd_Date());
	   stmt.setInt(11,getTotal_Count());					   
	   stmt.setString(12,getTotal_Sum());
	   stmt.setString(13,getConf_Food_Sum());
	   stmt.setString(14,getOrg_Exps_Sum());
	   stmt.setString(15,getTravel_Agency_Sum());					   
	   stmt.setString(16,getKey_Person_Sum());
	   stmt.setString(17,getAdd_Exp_Sum());
	   stmt.setString(18,getChain());
	   stmt.setString(19,getContracter());
	   stmt.setString(20,getLecturer());
	   stmt.setString(21,getOrganizer());					   
	   stmt.setString(22,getClinic_Name());
	   stmt.setString(23,getKey_Leader());
	   stmt.setString(24,getProduct_Name());
	   stmt.setString(25,getTema());
	   stmt.setString(26,getAttenders_Count());
	   stmt.setString(27,getConditions());
	   stmt.setString(28,getStatuses());
	   stmt.setString(29,getComment());
	   stmt.setString(30,getThird_Stage());
	   stmt.setString(31,getContracter1());
	   stmt.setString(32,getOrg_Exps_Sum1());
	   stmt.setString(33,getKey_Leader1());
	   stmt.setString(34,getKey_Person_Sum1());
}


	private void setData(JTable resultTable, int j) {
	   if(resultTable.getValueAt(j, 2) != null && resultTable.getValueAt(j, 2).toString().length()>0){
		   setCompany(resultTable.getValueAt(j, 2).toString());
	   }else{
		   setCompany("");
	   }
	   if(resultTable.getValueAt(j, 3) != null && resultTable.getValueAt(j, 3).toString().length()>0){
		   setCountry(resultTable.getValueAt(j, 3).toString());
	   }else{
		   setCountry("");
	   }
	   if(resultTable.getValueAt(j, 4) != null && resultTable.getValueAt(j, 4).toString().length()>0){
		   setArea(resultTable.getValueAt(j, 4).toString());
	   }else{
		   setArea("");
	   }
	   if(resultTable.getValueAt(j, 5) != null && resultTable.getValueAt(j, 5).toString().length()>0){
		   setRegion(resultTable.getValueAt(j, 5).toString());
	   }else{
		   setRegion("");
	   }
	   if(resultTable.getValueAt(j, 6) != null && resultTable.getValueAt(j, 6).toString().length()>0){
		   setCity(resultTable.getValueAt(j, 6).toString());
	   }else{
		   setCity("");
	   }
	   if(resultTable.getValueAt(j, 7) != null && resultTable.getValueAt(j, 7).toString().length()>0){
		   setFirst_Stage(resultTable.getValueAt(j, 7).toString());
	   }else{
		   setFirst_Stage("");
	   }
	   if(resultTable.getValueAt(j, 8) != null && resultTable.getValueAt(j, 8).toString().length()>0){
		   setSecond_Stage(resultTable.getValueAt(j, 8).toString());
	   }else{
		   setSecond_Stage("");
	   }
	   setStart_Date(Date.valueOf(resultTable.getValueAt(j, 9).toString()));
	   setEnd_Date(Date.valueOf(resultTable.getValueAt(j, 10).toString()));
	   
	   if(resultTable.getValueAt(j, 11) != null && resultTable.getValueAt(j, 11).toString().length()>0){
		   setTotal_Count(Integer.parseInt(resultTable.getValueAt(j, 11).toString().replace(".", "").trim()));
	   }else{
		   setTotal_Count(0);
	   }
	   if(resultTable.getValueAt(j, 12) != null && resultTable.getValueAt(j, 12).toString().length()>0){
		   setTotal_Sum(resultTable.getValueAt(j, 12).toString());
	   }else{
		   setTotal_Sum("");
	   }
	   if(resultTable.getValueAt(j, 13) != null && resultTable.getValueAt(j, 13).toString().length()>0){
		   setConf_Food_Sum(resultTable.getValueAt(j, 13).toString());
	   }else{
		   setConf_Food_Sum("");
	   }
	   if(resultTable.getValueAt(j, 14) != null && resultTable.getValueAt(j, 14).toString().length()>0){
		   setOrg_Exps_Sum(resultTable.getValueAt(j, 14).toString());
	   }else{
		   setOrg_Exps_Sum("");
	   }
	   if(resultTable.getValueAt(j, 15) != null && resultTable.getValueAt(j, 15).toString().length()>0){
		   setTravel_Agency_Sum(resultTable.getValueAt(j, 15).toString());		
	   }else{
		   setTravel_Agency_Sum("");
	   }
	   if(resultTable.getValueAt(j, 16) != null && resultTable.getValueAt(j, 16).toString().length()>0){
		   setKey_Person_Sum(resultTable.getValueAt(j, 16).toString());	
	   }else{
		   setKey_Person_Sum("");
	   }
	   if(resultTable.getValueAt(j, 17) != null && resultTable.getValueAt(j, 17).toString().length()>0){
		   setAdd_Exp_Sum(resultTable.getValueAt(j, 17).toString());
	   }else{
		   setAdd_Exp_Sum("");
	   }
	   if(resultTable.getValueAt(j, 18) != null && resultTable.getValueAt(j, 18).toString().length()>0){
		   setChain(resultTable.getValueAt(j, 18).toString());
	   }else{
		   setChain("");
	   }
	   if(resultTable.getValueAt(j, 19) != null && resultTable.getValueAt(j, 19).toString().length()>0){
		   setContracter(resultTable.getValueAt(j, 19).toString());
	   }else{
		   setContracter("");
	   }
	   if(resultTable.getValueAt(j, 20) != null && resultTable.getValueAt(j, 20).toString().length()>0){
		   setLecturer(resultTable.getValueAt(j, 20).toString());
	   }else{
		   setLecturer("");
	   }
	   if(resultTable.getValueAt(j, 21) != null && resultTable.getValueAt(j, 21).toString().length()>0){
		   setOrganizer(resultTable.getValueAt(j, 21).toString());
	   }else{
		   setOrganizer("");
	   }
	   if(resultTable.getValueAt(j, 22) != null && resultTable.getValueAt(j, 22).toString().length()>0){
		   setClinic_Name(resultTable.getValueAt(j, 22).toString());
	   }else{
		   setClinic_Name("");
	   }
	   if(resultTable.getValueAt(j, 23) != null && resultTable.getValueAt(j, 23).toString().length()>0){
		   setKey_Leader(resultTable.getValueAt(j, 23).toString());
	   }else{
		   setKey_Leader("");
	   }				   
	   if(resultTable.getValueAt(j, 24) != null && resultTable.getValueAt(j, 24).toString().length()>0){
		   setProduct_Name(resultTable.getValueAt(j, 24).toString());
	   }else{
		   setProduct_Name("");
	   }
	   if(resultTable.getValueAt(j, 25) != null && resultTable.getValueAt(j, 25).toString().length()>0){
		   setTema(resultTable.getValueAt(j, 25).toString());	
	   }else{
		   setTema("");
	   }
	   if(resultTable.getValueAt(j, 26) != null && resultTable.getValueAt(j, 26).toString().length()>0){
		   setAttenders_Count(resultTable.getValueAt(j, 26).toString());
	   }else{
		   setAttenders_Count("");
	   }
	   if(resultTable.getValueAt(j, 27) != null && resultTable.getValueAt(j, 27).toString().length()>0){
		   setConditions(resultTable.getValueAt(j, 27).toString());
	   }else{
		   setConditions("");
	   }
	   if(resultTable.getValueAt(j, 28) != null && resultTable.getValueAt(j, 28).toString().length()>0){
		   setStatuses(resultTable.getValueAt(j, 28).toString());
	   }else{
		   setStatuses("");
	   }			   
	   if(resultTable.getValueAt(j, 29) != null && resultTable.getValueAt(j, 29).toString().length()>0){
		   setComment(resultTable.getValueAt(j, 29).toString());
	   }else{
		   setComment("");
	   }
	   if(resultTable.getValueAt(j, 30) != null && resultTable.getValueAt(j, 30).toString().length()>0){
		   setThird_Stage(resultTable.getValueAt(j, 30).toString());
	   }else{
		   setThird_Stage("");
	   }   
	   if(resultTable.getValueAt(j, 31) != null && resultTable.getValueAt(j, 31).toString().length()>0){
		   setContracter1(resultTable.getValueAt(j, 31).toString());
	   }else{
		   setContracter1("");
	   }
	   if(resultTable.getValueAt(j, 32) != null && resultTable.getValueAt(j, 32).toString().length()>0){
		   setOrg_Exps_Sum1(resultTable.getValueAt(j, 32).toString());
	   }else{
		   setOrg_Exps_Sum1("");
	   }
	   if(resultTable.getValueAt(j, 33) != null && resultTable.getValueAt(j, 33).toString().length()>0){
		   setKey_Leader1(resultTable.getValueAt(j, 33).toString());
	   }else{
		   setKey_Leader1("");
	   }
	   if(resultTable.getValueAt(j, 34) != null && resultTable.getValueAt(j, 34).toString().length()>0){
		   setKey_Person_Sum1(resultTable.getValueAt(j, 34).toString());
	   }else{
		   setKey_Person_Sum1("");
	   }
	}
	   


public int getID()  {
       return ID;
   }
   public void setName(int ID)  {
       this.ID = ID;
   }
   
   public int getApproval_Status()  {
       return Approval_Status;
   }
   public void setApproval_Status(int Approval_Status)  {
	   this.Approval_Status = Approval_Status;
   }
   
   public String getCompany()  {
       return Company;
   }
   public void setCompany(String Company)  {
	   if(Company != null & Company.length() > 0){
		   this.Company = Company;
	   }else{
		   this.Company ="";
	   }
   }
	
   public String getCountry()  {
       return Country;
   }
   public void setCountry(String Country)  {
	   if(Country != null & Country.length() > 0){
		   this.Country = Country;
	   }else{
		   this.Country = "";
	   }
       
   }
   public String getArea()  {
       return Area;
   }
   public void setArea(String Area)  {
	   if(Area != null & Area.length() > 0){
		   this.Area = Area;
	   }else{
		   this.Area = "";
	   }
       
   }
   public String getRegion()  {
       return Region;
   }
   public void setRegion(String Region)  {       
       if(Region != null & Region.length() > 0){
    	   this.Region = Region;
	   }else{
		   this.Region = "";
	   }
   }
	
   public String getCity()  {
       return City;
   }
   public void setCity(String City)  {
       
       if(City != null & City.length() > 0){
    	   this.City = City;
	   }else{
		   this.City = "";
	   }
   }
   
   public String getFirst_Stage()  {
       return First_Stage;
   }
   public void setFirst_Stage(String First_Stage)  {
       if(First_Stage != null & First_Stage.length() > 0){
    	   this.First_Stage = First_Stage;
	   }else{
		   this.First_Stage = "";
	   }
   }
   public String getSecond_Stage()  {
       return Second_Stage;
   }
   public void setSecond_Stage(String Second_Stage)  {
       if(Second_Stage != null & Second_Stage.length() > 0){
    	   this.Second_Stage = Second_Stage;
	   }else{
		   this.Second_Stage = "";
	   }
   }
   public Date getStart_Date()  {
       return Start_Date;
   }
   public void setStart_Date(Date Start_Date)  {
       if(Start_Date != null){
    	   this.Start_Date = Start_Date;
	   }
   }
   public Date getEnd_Date()  {
       return End_Date;
   }
   public void setEnd_Date(Date End_Date)  {      
       if(End_Date != null){
    	   this.End_Date = End_Date;
	   }
   }
   public int getTotal_Count()  {
       return Total_Count;
   }
   public void setTotal_Count(int Total_Count)  {      
       if(Total_Count > 0){
    	   this.Total_Count = Total_Count;
	   }else{
		   this.Total_Count = 0;
	   }
   }
   public String getTotal_Sum()  {
       return Total_Sum;
   }
   public void setTotal_Sum(String Total_Sum)  {      
       if(Total_Sum != null & Total_Sum.length() > 0){
    	   this.Total_Sum = Total_Sum;
	   }else{
		   this.Total_Sum = "";
	   }
   }  
   public String getConf_Food_Sum()  {
       return Conf_Food_Sum;
   }
   public void setConf_Food_Sum(String Conf_Food_Sum)  {      
       if(Conf_Food_Sum != null & Conf_Food_Sum.length() > 0){
    	   this.Conf_Food_Sum = Conf_Food_Sum;
	   }else{
		   this.Conf_Food_Sum = "";
	   }
   }
   public String getOrg_Exps_Sum()  {
       return Org_Exps_Sum;
   }
   public void setOrg_Exps_Sum(String Org_Exps_Sum)  {
      
       if(Org_Exps_Sum != null & Org_Exps_Sum.length() > 0){
    	   this.Org_Exps_Sum = Org_Exps_Sum;
	   }else{
		   this.Org_Exps_Sum = "";
	   }
   }
   
   public String getTravel_Agency_Sum()  {
       return Travel_Agency_Sum;
   }
   public void setTravel_Agency_Sum(String Travel_Agency_Sum)  {
       
       if(Travel_Agency_Sum != null & Travel_Agency_Sum.length() > 0){
    	   this.Travel_Agency_Sum = Travel_Agency_Sum;
	   }else{
		   this.Travel_Agency_Sum = "";
	   }
   }  
   public String getKey_Person_Sum()  {
       return Key_Person_Sum;
   }
   public void setKey_Person_Sum(String Key_Person_Sum)  {
       
       if(Key_Person_Sum != null & Key_Person_Sum.length() > 0){
    	   this.Key_Person_Sum = Key_Person_Sum;
	   }else{
		   this.Key_Person_Sum = "";
	   }
   }
   public String getAdd_Exp_Sum()  {
       return Add_Exp_Sum;
   }
   public void setAdd_Exp_Sum(String Add_Exp_Sum)  {
       
       if(Add_Exp_Sum != null & Add_Exp_Sum.length() > 0){
    	   this.Add_Exp_Sum = Add_Exp_Sum;
	   }else{
		   this.Add_Exp_Sum = "";
	   }
   }
   
   public String getChain()  {
       return Chain;
   }
   public void setChain(String Chain)  {
       
       if(Chain != null & Chain.length() > 0){
    	   this.Chain = Chain;
	   }else{
		   this.Chain = "";
	   }
   }  
   public String getContracter()  {
       return Contracter;
   }
   public void setContracter(String Contracter)  {
       
       if(Contracter != null & Contracter.length() > 0){
    	   this.Contracter = Contracter;
	   }else{
		   this.Contracter = "";
	   }
   }
   public String getLecturer()  {
       return Lecturer;
   }
   public void setLecturer(String Lecturer)  {
       
       if(Lecturer != null & Lecturer.length() > 0){
    	   this.Lecturer = Lecturer;
	   }else{
		   this.Lecturer = "";
	   }
   }
   
   public String getOrganizer()  {
       return Organizer;
   }
   public void setOrganizer(String Organizer)  {
       if(Organizer != null & Organizer.length() > 0){
    	   this.Organizer = Organizer;
	   }else{
		   this.Organizer = "";
	   }
   }  
   public String getClinic_Name()  {
       return Clinic_Name;
   }
   public void setClinic_Name(String Clinic_Name)  {
       
       if(Clinic_Name != null & Clinic_Name.length() > 0){
    	   this.Clinic_Name = Clinic_Name;
	   }else{
		   this.Clinic_Name = "";
	   }
   }
   public String getKey_Leader()  {
       return Key_Leader;
   }
   public void setKey_Leader(String Key_Leader)  {
       if(Key_Leader != null & Key_Leader.length() > 0){
    	   this.Key_Leader = Key_Leader;
	   }else{
		   this.Key_Leader = "";
	   }
   }
   
   public String getProduct_Name()  {
       return Product_Name;
   }
   public void setProduct_Name(String Product_Name)  {
      
       if(Product_Name != null & Product_Name.length() > 0){
    	   this.Product_Name = Product_Name;
	   }else{
		   this.Product_Name = "";
	   }
   }  
   public String getTema()  {
       return Tema;
   }
   public void setTema(String Tema)  {
       
       if(Tema != null & Tema.length() > 0){
    	   this.Tema = Tema;
	   }else{
		   this.Tema = "";
	   }
   }
   public String getAttenders_Count()  {
       return Attenders_Count;
   }
   public void setAttenders_Count(String Attenders_Count)  {
      
       if(Attenders_Count != null & Attenders_Count.length() > 0){
    	   this.Attenders_Count = Attenders_Count;
	   }else{
		   this.Attenders_Count = "";
	   }
   }
   
   public String getConditions()  {
       return Conditions;
   }
   public void setConditions(String Conditions)  {
      
       if(Conditions != null & Conditions.length() > 0){
    	   this.Conditions = Conditions;
	   }else{
		   this.Conditions = "";
	   }
   }  
   public String getStatuses()  {
       return Statuses;
   }
   public void setStatuses(String Statuses)  {
       if(Statuses != null & Statuses.length() > 0){
    	   this.Statuses = Statuses;
	   }else{
		   this.Statuses = "";
	   }
   }
   public String getComment()  {
       return Comment;
   }
   public void setComment(String Comment)  {
       
       if(Comment != null & Comment.length() > 0){
    	   this.Comment = Comment;
	   }else{
		   this.Comment = "";
	   }
   }
   
   public String getThird_Stage()  {
       return Third_Stage;
   }
   public void setThird_Stage(String Third_Stage)  {
       
       if(Third_Stage != null & Third_Stage.length() > 0){
    	   this.Third_Stage = Third_Stage;
	   }else{
		   this.Third_Stage = "";
	   }
   }
   
   public DateTime getEntry_date()  {
       return entry_date;
   }
   public void setEntry_date(DateTime entry_date)  {
       this.entry_date = entry_date;
   }
   
   public String getentry_user()  {
       return entry_user;
   }
   public void setentry_user(String entry_user)  {
       
       if(entry_user != null & entry_user.length() > 0){
    	   this.entry_user = entry_user;
	   }else{
		   this.entry_user = "";
	   }
   }
   
   public DateTime getapprove_date()  {
       return approve_date;
   }
   public void setapprove_date(DateTime approve_date)  {
       this.approve_date = approve_date;
   }
   
   public String getapprove_user()  {
       return approve_user;
   }
   public void setapprove_user(String approve_user)  {
       
       if(approve_user != null & approve_user.length() > 0){
    	   this.approve_user = approve_user;
	   }else{
		   this.approve_user = "";
	   }
   }
   
   public String getContracter1()  {
       return Contracter1;
   }
   public void setContracter1(String Contracter1)  {
       
       if(Contracter1 != null & Contracter1.length() > 0){
    	   this.Contracter1 = Contracter1;
	   }else{
		   this.Contracter1 = "";
	   }
   }
   
   public String getOrg_Exps_Sum1()  {
       return Org_Exps_Sum1;
   }
   public void setOrg_Exps_Sum1(String Org_Exps_Sum1)  {
      
       if(Org_Exps_Sum1 != null & Org_Exps_Sum1.length() > 0){
    	   this.Org_Exps_Sum1 = Org_Exps_Sum1;
	   }else{
		   this.Org_Exps_Sum1 = "";
	   }
   }
   
   public String getKey_Leader1()  {
       return Key_Leader1;
   }
   public void setKey_Leader1(String Key_Leader1)  {
       
       if(Key_Leader1 != null & Key_Leader1.length() > 0){
    	   this.Key_Leader1 = Key_Leader1;
	   }else{
		   this.Key_Leader1 = "";
	   }
   }
   
   public String getKey_Person_Sum1()  {
       return Key_Person_Sum1;
   }
   public void setKey_Person_Sum1(String Key_Person_Sum1)  {
      
       if(Key_Person_Sum1 != null & Key_Person_Sum1.length() > 0){
    	   this.Key_Person_Sum1 = Key_Person_Sum1;
	   }else{
		   this.Key_Person_Sum1 = "";
	   }
   }
}
