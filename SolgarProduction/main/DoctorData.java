package main;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import cb.esi.esiclient.util.ESIBag;
import cb.smg.general.utility.CBBag;
import util.Util;

public class DoctorData {
	 Properties prop = ConnectToDb.readConfFile();
	 public DoctorData() {
			// TODO Auto-generated constructor stub
		   }
	
	 public ESIBag save(ESIBag inBag) throws Exception{
		  Connection conn = null;
		  PreparedStatement  stmt = null;	
		  ESIBag outBag = new ESIBag();
		  int doctorId=0;
		  int generatedId=0;
		  try{		  
			  Class.forName("com.mysql.jdbc.Driver");
		      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
		      conn.setAutoCommit(false);
		      
		      String username = inBag.get("USERNAME").toString();
		      int row = inBag.getSize("RESULTTABLE");
			  for (int j = 0; j  < row; j++) {
			      String doctor_name = inBag.get("RESULTTABLE",j,"DOCTOR_NAME").toString();
			      String point_x = inBag.get("RESULTTABLE",j,"POINT_X");
			      String point_y = inBag.get("RESULTTABLE",j,"POINT_Y");
			      String entryUser = inBag.get("RESULTTABLE",j,"ENTRY_USER");
			      
			      int id = Integer.parseInt(inBag.get("RESULTTABLE",j,"ID").toString());
			      if(id==0){
				      String controlfalse = control_uniquless(doctor_name, point_x, point_y);
				      if(controlfalse.equalsIgnoreCase("1")){
				    	  outBag.put("RC","false");
				    	  outBag.put("ERROR_MESSAGE", "Please check next doctor already recorded:"+doctor_name);
				    	  return outBag;
				      }
			      }
			     
			      if(id<0){//if update first delete
			    	  doctorId = getDoctorId(conn, id*-1);
			    	  deleteDoctor(conn,id,username);
			      }
			      if(!entryUser.equalsIgnoreCase("Delete"))  {
			    	  stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_tst.doctor_data(`status`,`brand`,`country`,`area`,`region`,`city`,"
			    			  +"`activeness`,`medrep`,`doctor_date`,`doctor_name`,`Unified_specialty`,`Specialty`,`Position_regalia`,`category`,`clinic_name`,"
			    			  +"`clinic_address`,`clinic_count`,`key_person`,`doctor_tel`,`doctor_email`,`full_address`,`requested`,`building_type`,`country_code`,"
			    			  +"`administrative_area_name`,`sub_administrative_area_name`,`street`,`homenumber`,`point_y`,`point_x`,`doctor_id`,`entry_date`,`entry_user`)"
						   		+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);//32
				      
				    	  stmt.setInt(1,1);//Status
				    	  stmt.setString(2,inBag.get("RESULTTABLE",j,"BRAND"));
				    	  stmt.setString(3,inBag.get("RESULTTABLE",j,"COUNTRY"));
				    	  stmt.setString(4,inBag.get("RESULTTABLE",j,"AREA"));
				    	  stmt.setString(5,inBag.get("RESULTTABLE",j,"REGION"));
				    	  stmt.setString(6,inBag.get("RESULTTABLE",j,"CITY"));
				    	  
				    	  stmt.setString(7,inBag.get("RESULTTABLE",j,"ACTIVENESS"));
				    	  stmt.setString(8,inBag.get("RESULTTABLE",j,"MARKETING_STAFF"));
				    	  stmt.setString(9,inBag.get("RESULTTABLE",j,"ACTIVATION_DATE"));
				    	  stmt.setString(10,inBag.get("RESULTTABLE",j,"DOCTOR_NAME"));
				    	  stmt.setString(11,inBag.get("RESULTTABLE",j,"UNIFIED_SPECIALITY"));
				    	  stmt.setString(12,inBag.get("RESULTTABLE",j,"SPECIALITY"));
				    	  stmt.setString(13,inBag.get("RESULTTABLE",j,"POSITION_REGALIA"));
				    	  stmt.setString(14,inBag.get("RESULTTABLE",j,"CATEGORY"));
				    	  stmt.setString(15,inBag.get("RESULTTABLE",j,"CLINIC_NAME"));
				    	  
				    	  stmt.setString(16,inBag.get("RESULTTABLE",j,"ADDRESS"));
				    	  if(inBag.get("RESULTTABLE",j,"CLINIC_COUNT")!= null && inBag.get("RESULTTABLE",j,"CLINIC_COUNT").toString().length()>0){
				    		  stmt.setInt(17,Integer.parseInt(inBag.get("RESULTTABLE",j,"CLINIC_COUNT")));
				    	  }else{
				    		  stmt.setInt(17,0);  
				    	  }
				    	  stmt.setString(18,inBag.get("RESULTTABLE",j,"KEY_PERSON"));
				    	  stmt.setString(19,inBag.get("RESULTTABLE",j,"DOCTOR_TEL"));
				    	  stmt.setString(20,inBag.get("RESULTTABLE",j,"DOCTOR_EMAIL"));
				    	  stmt.setString(21,inBag.get("RESULTTABLE",j,"FULL_ADDRESS"));
				    	  stmt.setString(22,inBag.get("RESULTTABLE",j,"ADDRESS"));
				    	  stmt.setString(23,inBag.get("RESULTTABLE",j,"BUILDING_TYPE"));
				    	  stmt.setString(24,inBag.get("RESULTTABLE",j,"COUNTRY_CODE"));
				    	  
				    	  stmt.setString(25,inBag.get("RESULTTABLE",j,"ADMINISTRATIVE_AREA_NAME"));
				    	  stmt.setString(26,inBag.get("RESULTTABLE",j,"SUB_ADMINISTRATIVE_AREA_NAME"));
				    	  stmt.setString(27,inBag.get("RESULTTABLE",j,"STREET"));
				    	  stmt.setString(28,inBag.get("RESULTTABLE",j,"HOMENUMBER"));
				    	  stmt.setString(29,inBag.get("RESULTTABLE",j,"POINT_Y"));
				    	  stmt.setString(30,inBag.get("RESULTTABLE",j,"POINT_X"));
				    	  stmt.setInt(31,doctorId);//Status
				    	  stmt.setTimestamp(32,Timestamp.valueOf(Util.getCurrentDateTime()));
				    	  stmt.setString(33,username);
				    	  stmt.executeUpdate();
						   
				    	  ResultSet keyResultSet = stmt.getGeneratedKeys();
				          if (keyResultSet.next()) {
				        	generatedId = keyResultSet.getInt(1);
				            inBag.put("RELATIONID", String.valueOf(generatedId));
				          }				   
						  stmt.close();
						  
						  if(id==0){//Yeni ise update et doctor id yi
							  updateDoctorId(conn,generatedId); 
						  }						  
						  
			      }
		      }
			 
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
		  return outBag;
	  }
	 
	 private void deleteDoctor(Connection conn,int id,String userName) throws Exception {
		 PreparedStatement  stmt = null;				  
		   try{
			   id = id*-1;
			   
			   stmt = (PreparedStatement) conn.prepareStatement( "update solgar_tst.doctor_data set status = 0  WHERE status = 1 and id = ?");			   
			   //stmt.setString(1,userName);
			   //stmt.setTimestamp(2,Timestamp.valueOf(Util.getCurrentDateTime()));
			   stmt.setInt(1,id);
			   stmt.executeUpdate();
			   stmt.close(); 					
				
		      }catch(SQLException se){
			      throw se;
			   }catch(Exception e){
			      e.printStackTrace();
			      throw e;
			   }finally{}//end try	
		
	}
	 
	 private void updateDoctorId(Connection conn,int id) throws Exception {
		 PreparedStatement  stmt = null;				  
		   try{
			   
			   stmt = (PreparedStatement) conn.prepareStatement( "update solgar_tst.doctor_data set doctor_id = ? WHERE status = 1 and id = ?");			   
			   stmt.setInt(1,id);
			   stmt.setInt(2,id);
			   stmt.executeUpdate();
			   stmt.close(); 					
				
		      }catch(SQLException se){
			      throw se;
			   }catch(Exception e){
			      e.printStackTrace();
			      throw e;
			   }finally{}//end try	
		
	}
	 private int getDoctorId(Connection conn,int id) throws Exception {
		 Statement stmt =null;
		 String sorgu  ="";
		 int doctorId=0;
		   try{
			   sorgu  = "select * from solgar_tst.doctor_data where status = 1 ";
			   sorgu = sorgu +" and id ="+id;
			   
			   stmt = (Statement) conn.createStatement();
	           ResultSet rs = stmt.executeQuery(sorgu);
	           
	           while (rs.next()){
	        	   doctorId = rs.getInt("doctor_id");
	           }
			   					
	           stmt.close();
	           
		      }catch(SQLException se){
			      throw se;
			   }catch(Exception e){
			      e.printStackTrace();
			      throw e;
			   }finally{}//end try	
		   return doctorId;
		
	}
	 
	 public void update(ESIBag inBag) throws Exception{
		  Connection conn = null;
		  try{ 
			  Class.forName("com.mysql.jdbc.Driver");
		      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
		      conn.setAutoCommit(false);
		      
		      
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
	 
	 public ESIBag getAll(ESIBag inBag) throws Exception {
		   Statement stmt =null;
	       ESIBag outBag = new ESIBag();
	       Connection conn = null;
	       String sorgu  ="";
		   try{
	           
			   Class.forName("com.mysql.jdbc.Driver");
			   conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));
			   
			   String brand = inBag.get("BRAND").toString();
			   
			   sorgu  = "select * from solgar_tst.doctor_data where status = 1 ";
			   
			   
			   if(inBag.existsBagKey("COUNTRY") && inBag.get("COUNTRY").toString().length()>0){
				   sorgu = sorgu +" and country ='"+inBag.get("COUNTRY").toString()+"'";
			   }
			   if(inBag.existsBagKey("AREA") && inBag.get("AREA").toString().length()>0){
				   sorgu = sorgu +" and area ='"+inBag.get("AREA").toString()+"'";
			   }
			   if(inBag.existsBagKey("REGION") && inBag.get("REGION").toString().length()>0){
				   sorgu = sorgu +" and region ='"+inBag.get("REGION").toString()+"'";
			   }
			   if(inBag.existsBagKey("CITY") && inBag.get("CITY").toString().length()>0){
				   sorgu = sorgu +" and city ='"+inBag.get("CITY").toString()+"'";
			   }
			   /*if(inBag.existsBagKey("CITY_REGION") && inBag.get("CITY_REGION").toString().length()>0){
				   sorgu = sorgu +" and city_region ='"+inBag.get("CITY_REGION").toString()+"'";
			   }*/
			   if(inBag.existsBagKey("MARKETING_STAFF") && inBag.get("MARKETING_STAFF").toString().length()>0){
				   sorgu = sorgu +" and medrep ='"+inBag.get("MARKETING_STAFF").toString()+"'";
			   }
			   
			   if(inBag.existsBagKey("SUB_SPECIALITY") && inBag.get("SUB_SPECIALITY").toString().length()>0){
				   sorgu = sorgu +" and specialty ='"+inBag.get("SUB_SPECIALITY").toString()+"'";
			   }
			   if(inBag.existsBagKey("MAIN_SPECIALITY") && inBag.get("MAIN_SPECIALITY").toString().length()>0){
				   sorgu = sorgu +" and unified_specialty ='"+inBag.get("MAIN_SPECIALITY").toString()+"'";
			   }
			   if(inBag.existsBagKey("CATEGORY") && inBag.get("CATEGORY").toString().length()>0){
				   sorgu = sorgu +" and category ='"+inBag.get("CATEGORY").toString()+"'";
			   }
			   if(inBag.existsBagKey("ACTIVENESS") && inBag.get("ACTIVENESS").toString().length()>0){
				   sorgu = sorgu +" and activeness ='"+inBag.get("ACTIVENESS").toString()+"'";
			   }
			   if(inBag.existsBagKey("DOCTOR_NAME") && inBag.get("DOCTOR_NAME").toString().length()>0){
				   sorgu = sorgu +" and doctor_name like '%"+inBag.get("DOCTOR_NAME").toString()+"%'";
			   }
				          
	           stmt = (Statement) conn.createStatement();
	           ResultSet rs = stmt.executeQuery(sorgu);
	
	           int j =0;
	           while (rs.next()){	
	        	   outBag.put("TABEL",j,"ID", String.valueOf(rs.getInt("id")));
	        	   outBag.put("TABEL",j,"BRAND", "SOLGAR");
	         	   outBag.put("TABEL",j,"COUNTRY", "Russia");
	         	   outBag.put("TABEL",j,"AREA", rs.getString("area"));
	         	   outBag.put("TABEL",j,"REGION", rs.getString("region"));
	         	   outBag.put("TABEL",j,"CITY", rs.getString("city"));
	         	   outBag.put("TABEL",j,"ACTIVENESS", rs.getString("activeness"));
	         	   outBag.put("TABEL",j,"MARKETING_STAFF", rs.getString("medrep"));
	         	   outBag.put("TABEL",j,"ACTIVATION_DATE", rs.getString("doctor_date"));
		           outBag.put("TABEL",j,"DOCTOR_NAME", rs.getString("doctor_name"));
		           outBag.put("TABEL",j,"UNIFIED_SPECIALITY", rs.getString("Unified_specialty"));
		           outBag.put("TABEL",j,"SPECIALITY", rs.getString("Specialty"));
		           outBag.put("TABEL",j,"POSITION_REGALIA", rs.getString("Position_regalia"));
		           outBag.put("TABEL",j,"CATEGORY", rs.getString("category"));
		           outBag.put("TABEL",j,"CLINIC_NAME", rs.getString("clinic_name"));
		           outBag.put("TABEL",j,"ADDRESS", rs.getString("clinic_address"));
		           outBag.put("TABEL",j,"CLINIC_COUNT", rs.getString("clinic_count"));   
		           outBag.put("TABEL",j,"KEY_PERSON", rs.getString("key_person")); 
		           outBag.put("TABEL",j,"DOCTOR_TEL", rs.getString("doctor_tel")); 
		           outBag.put("TABEL",j,"DOCTOR_EMAIL", rs.getString("doctor_email"));        	   
	        	   outBag.put("TABEL",j,"FULL_ADDRESS", rs.getString("full_address"));
	         	   outBag.put("TABEL",j,"BUILDING_TYPE", rs.getString("building_type"));
	         	   outBag.put("TABEL",j,"COUNTRY_CODE", rs.getString("country_code"));
	         	   outBag.put("TABEL",j,"ADMINISTRATIVE_AREA_NAME", rs.getString("administrative_area_name"));
	         	   outBag.put("TABEL",j,"SUB_ADMINISTRATIVE_AREA_NAME", rs.getString("sub_administrative_area_name"));
	         	   outBag.put("TABEL",j,"STREET", rs.getString("street"));
	         	   outBag.put("TABEL",j,"HOMENUMBER", rs.getString("homenumber"));
	         	   outBag.put("TABEL",j,"POINT_Y", rs.getString("point_y"));
	         	   outBag.put("TABEL",j,"POINT_X", rs.getString("point_x"));   
	         	   outBag.put("TABEL",j,"ENTRY_DATE", rs.getString("entry_date"));
	         	   outBag.put("TABEL",j,"ENTRY_USER", rs.getString("entry_user"));	        	   
	        	   j++;
	           } 
	           outBag.put("COUNT",String.valueOf(j));
	           
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
	 
	 public ESIBag getOffAddressInfo(ESIBag inBag) throws Exception {
		 ESIBag outBag = new ESIBag();
		   try{
			  outBag = Util.getOfficialAddressInfo(inBag);
	       }
	       catch(SQLException sqle){
	           System.out.println("SQL Exception: " + sqle.getMessage());
	       }
		   return outBag;
	   }
	 public String control_uniquless(String doctorName,String point_x,String point_y) throws Exception {
		   Statement stmt =null;
	       Connection conn = null;
	       String sorgu  ="";
		   try{
	           
			   Class.forName("com.mysql.jdbc.Driver");
			   conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));			   
			   sorgu  = "select * from solgar_tst.doctor_data where status = 1 ";
			   sorgu = sorgu +" and doctor_name='"+doctorName+"'"+" and point_y='"+point_y+"'"+" and point_x='"+point_x+"'";
				          
	           stmt = (Statement) conn.createStatement();
	           ResultSet rs = stmt.executeQuery(sorgu);

	           while (rs.next()){	
	        	   return  "1";
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
		   return  "0";
	   }
	 
}