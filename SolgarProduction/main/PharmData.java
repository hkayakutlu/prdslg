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
import jxl.write.DateTime;
import util.Util;

public class PharmData {
	 Properties prop = ConnectToDb.readConfFile();
	 public PharmData() {
			// TODO Auto-generated constructor stub
		   }
	
	 public ESIBag save(ESIBag inBag) throws Exception{
		  Connection conn = null;
		  PreparedStatement  stmt = null;	
		  ESIBag outBag = new ESIBag();
		  try{		  
			  Class.forName("com.mysql.jdbc.Driver");
		      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
		      conn.setAutoCommit(false);
		      
		      String username = inBag.get("USERNAME").toString();
		      int row = inBag.getSize("RESULTTABLE");
			  for (int j = 0; j  < row; j++) {
			      String brand = inBag.get("RESULTTABLE",j,"BRAND").toString();
			      String chain = inBag.get("RESULTTABLE",j,"GROUP_COMPANY").toString();
			      String point_x = inBag.get("RESULTTABLE",j,"POINT_X");
			      String point_y = inBag.get("RESULTTABLE",j,"POINT_Y");
			      String address = inBag.get("RESULTTABLE",j,"PHARMACY_ADDRESS");
			      String entryUser = inBag.get("RESULTTABLE",j,"ENTRY_USER");
			      
			      int id = Integer.parseInt(inBag.get("RESULTTABLE",j,"ID").toString());
			      if(id==0){
				      String controlfalse = control_uniquless(brand, chain, point_x, point_y);
				      if(controlfalse.equalsIgnoreCase("1")){
				    	  outBag.put("RC","false");
				    	  outBag.put("ERROR_MESSAGE", "Please check next pharmacy already recorded:"+chain+" "+address);
				    	  return outBag;
				      }
			      }
			     
			      if(id<0){//if update first delete
			    	  deletePharmacy(conn,id,brand,username);
			      }
			      
			    	if(!entryUser.equalsIgnoreCase("Delete"))  {
				      if(brand.equalsIgnoreCase("SOLGAR")){
				    	  stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_tst.pharmacy_data_solgar(`country`,`area`,`region`,`city`,`district`,`metro`,`group_company`,`subgroup_company`,`pharmacy_no`,"
				    			  +"`pharmacy_address`,`pharmacy_category`,`assortiment`,`pharmacy_type`,`promo`,`marketing_staff`,`pharmacy_response_person`,`pharmacy_tel`,"
				    			  +"`pharmacy_email`,`pharmacy_activeness`,`pharmacy_activation_date`,`Comments`,`marketing_staff_no`,`pharmacy_number_sale`,`found_no`,"
				    			  +"`full_address`,`requested`,`building_type`,`country_code`,`administrative_area_name`,`sub_administrative_area_name`,`street`,"
				    			  +"`homenumber`,`point_y`,`point_x`,`processed`,`status`,`entry_user`,`entry_date`)"
							   		+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);	
				      }else{
				    	  stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_tst.pharmacy_data_bounty(`country`,`area`,`region`,`city`,`district`,`metro`,`group_company`,`subgroup_company`,`pharmacy_no`,"
				    			  +"`pharmacy_address`,`pharmacy_category`,`assortiment`,`pharmacy_type`,`promo`,`marketing_staff`,`pharmacy_response_person`,`pharmacy_tel`,"
				    			  +"`pharmacy_email`,`pharmacy_activeness`,`pharmacy_activation_date`,`Comments`,`marketing_staff_no`,`pharmacy_number_sale`,`found_no`,"
				    			  +"`full_address`,`requested`,`building_type`,`country_code`,`administrative_area_name`,`sub_administrative_area_name`,`street`,"
				    			  +"`homenumber`,`point_y`,`point_x`,`processed`,`status`,`entry_user`,`entry_date`)"
								   	+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);  
				      }
						
						   stmt.setString(1,inBag.get("RESULTTABLE",j,"COUNTRY"));
						   stmt.setString(2,inBag.get("RESULTTABLE",j,"AREA"));
						   stmt.setString(3,inBag.get("RESULTTABLE",j,"REGION"));
						   stmt.setString(4,inBag.get("RESULTTABLE",j,"CITY"));
						   //stmt.setString(6,inBag.get("RESULTTABLE",j,"CITY_REGION"));
						   stmt.setString(5,inBag.get("RESULTTABLE",j,"DISTRICT"));
						   stmt.setString(6,inBag.get("RESULTTABLE",j,"METRO"));
						   stmt.setString(7,chain);
						   stmt.setString(8,inBag.get("RESULTTABLE",j,"SUBGROUP_COMPANY"));
						   stmt.setString(9,inBag.get("RESULTTABLE",j,"PHARMACY_NO"));		
						   
						   stmt.setString(10,address);	
						   stmt.setString(11,inBag.get("RESULTTABLE",j,"PHARMACY_CATEGORY"));
						   stmt.setString(12,inBag.get("RESULTTABLE",j,"ASSORTIMENT"));
						   stmt.setString(13,inBag.get("RESULTTABLE",j,"PHARMACY_TYPE"));
						   stmt.setString(14,inBag.get("RESULTTABLE",j,"PROMO"));
						   stmt.setString(15,inBag.get("RESULTTABLE",j,"MARKETING_STAFF"));
						   stmt.setString(16,inBag.get("RESULTTABLE",j,"PHARMACY_RESPONSE_PERSON"));
						   stmt.setString(17,inBag.get("RESULTTABLE",j,"PHARMACY_TEL"));
						   
						   stmt.setString(18,inBag.get("RESULTTABLE",j,"PHARMACY_EMAIL"));
						   stmt.setString(19,inBag.get("RESULTTABLE",j,"PHARMACY_ACTIVENESS"));
						   stmt.setDate(20,Date.valueOf(inBag.get("RESULTTABLE",j,"PHARMACY_ACTIVATION_DATE")));
						   stmt.setString(21,inBag.get("RESULTTABLE",j,"COMMENTS"));
						   stmt.setInt(22,999);//inBag.get("RESULTTABLE",j,"MARKETING_STAFF_NO"));
						   stmt.setString(23,"0");//inBag.get("RESULTTABLE",j,"PHARMACY_NUMBER_SALE"));
						   stmt.setInt(24,1);//found no
						   
						   stmt.setString(25,inBag.get("RESULTTABLE",j,"FULL_ADDRESS"));
						   stmt.setString(26,address);
						   stmt.setString(27,inBag.get("RESULTTABLE",j,"BUILDING_TYPE"));
						   stmt.setString(28,inBag.get("RESULTTABLE",j,"COUNTRY_CODE"));
						   stmt.setString(29,inBag.get("RESULTTABLE",j,"ADMINISTRATIVE_AREA_NAME"));
						   stmt.setString(30,inBag.get("RESULTTABLE",j,"SUB_ADMINISTRATIVE_AREA_NAME"));
						   stmt.setString(31,inBag.get("RESULTTABLE",j,"STREET"));
						   
						   stmt.setString(32,inBag.get("RESULTTABLE",j,"HOMENUMBER"));
						   stmt.setString(33,point_y);
						   stmt.setString(34,point_x);
						   stmt.setInt(35,1);
						   stmt.setInt(36,1);//STATUS
						   stmt.setString(37,username);
						   stmt.setTimestamp(38,Timestamp.valueOf(Util.getCurrentDateTime()));					  
						   stmt.executeUpdate();
						   
						   ResultSet keyResultSet = stmt.getGeneratedKeys();
				            if (keyResultSet.next()) {
				            	inBag.put("RELATIONID", String.valueOf(keyResultSet.getInt(1)));
				            }				   
						   stmt.close();
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
	 
	 private void deletePharmacy(Connection conn,int id,String brand,String userName) throws Exception {
		 PreparedStatement  stmt = null;				  
		   try{
			   id = id*-1;
			   if(brand.equalsIgnoreCase("SOLGAR")){
				   stmt = (PreparedStatement) conn.prepareStatement( "update solgar_tst.pharmacy_data_solgar set status = 0,entry_user=?,entry_date=?  WHERE status = 1 and id = ?");	  	
			   }else{
				   stmt = (PreparedStatement) conn.prepareStatement( "update solgar_tst.pharmacy_data_bounty set status = 0,entry_user=?,entry_date=?  WHERE status = 1 and id = ?");
			   }
			   stmt.setString(1,userName);
			   stmt.setTimestamp(2,Timestamp.valueOf(Util.getCurrentDateTime()));
			   stmt.setInt(3,id);
			   stmt.executeUpdate();
			   stmt.close(); 					
				
		      }catch(SQLException se){
			      throw se;
			   }catch(Exception e){
			      e.printStackTrace();
			      throw e;
			   }finally{}//end try	
		
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
			   
			   if(brand.equalsIgnoreCase("SOLGAR")){
				   sorgu  = "select * from solgar_tst.pharmacy_data_solgar where status = 1 ";
			   }else{
				   sorgu  = "select * from solgar_tst.pharmacy_data_bounty where status = 1 ";
			   }
			   
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
			   if(inBag.existsBagKey("CITY_REGION") && inBag.get("CITY_REGION").toString().length()>0){
				   sorgu = sorgu +" and city_region ='"+inBag.get("CITY_REGION").toString()+"'";
			   }
			   if(inBag.existsBagKey("MARKETING_STAFF") && inBag.get("MARKETING_STAFF").toString().length()>0){
				   sorgu = sorgu +" and marketing_staff ='"+inBag.get("MARKETING_STAFF").toString()+"'";
			   }
			   
				          
	           stmt = (Statement) conn.createStatement();
	           ResultSet rs = stmt.executeQuery(sorgu);
	
	           int j =0;
	           while (rs.next()){	
	        	   outBag.put("TABEL",j,"ID", String.valueOf(rs.getInt("id")));
	        	   outBag.put("TABEL",j,"BRAND", brand);
	         	   outBag.put("TABEL",j,"COUNTRY", rs.getString("country"));
	         	   outBag.put("TABEL",j,"AREA", rs.getString("area"));
	         	   outBag.put("TABEL",j,"REGION", rs.getString("region"));
	         	   outBag.put("TABEL",j,"CITY", rs.getString("city"));
	         	   outBag.put("TABEL",j,"CITY_REGION", rs.getString("city_region"));
	         	   outBag.put("TABEL",j,"DISTRICT", rs.getString("district"));
	         	   outBag.put("TABEL",j,"METRO", rs.getString("metro"));
	         	   outBag.put("TABEL",j,"GROUP_COMPANY", rs.getString("group_company"));
	         	   outBag.put("TABEL",j,"SUBGROUP_COMPANY", rs.getString("subgroup_company"));
	         	   outBag.put("TABEL",j,"PHARMACY_NO", rs.getString("pharmacy_no"));    	   
	         	   outBag.put("TABEL",j,"PHARMACY_ADDRESS", rs.getString("pharmacy_address"));
	         	   outBag.put("TABEL",j,"PHARMACY_CATEGORY", rs.getString("pharmacy_category"));
	         	   outBag.put("TABEL",j,"ASSORTIMENT", rs.getString("assortiment"));
	         	   outBag.put("TABEL",j,"PHARMACY_TYPE", rs.getString("pharmacy_type"));
	         	   outBag.put("TABEL",j,"PROMO", rs.getString("promo")); 
	         	   outBag.put("TABEL",j,"MARKETING_STAFF", rs.getString("marketing_staff"));
	         	   outBag.put("TABEL",j,"PHARMACY_RESPONSE_PERSON", rs.getString("pharmacy_response_person"));
	         	   outBag.put("TABEL",j,"PHARMACY_TEL", rs.getString("pharmacy_tel"));
	         	   outBag.put("TABEL",j,"PHARMACY_EMAIL", rs.getString("pharmacy_email"));
	         	   outBag.put("TABEL",j,"PHARMACY_ACTIVENESS", rs.getString("pharmacy_activeness"));
	         	   outBag.put("TABEL",j,"PHARMACY_ACTIVATION_DATE", rs.getString("pharmacy_activation_date"));
	         	   outBag.put("TABEL",j,"COMMENTS", rs.getString("comments"));
	         	   outBag.put("TABEL",j,"STAFF_NO", rs.getString("marketing_staff_no"));
	         	   outBag.put("TABEL",j,"PHARMACY_NUMBER_SALE", rs.getString("pharmacy_number_sale"));
	         	   outBag.put("TABEL",j,"FOUND_NO", rs.getString("found_no"));
	        	   outBag.put("TABEL",j,"FULL_ADDRESS", rs.getString("full_address"));
	        	   outBag.put("TABEL",j,"REQUESTED", rs.getString("requested"));
	         	   outBag.put("TABEL",j,"BUILDING_TYPE", rs.getString("building_type"));
	         	   outBag.put("TABEL",j,"COUNTRY_CODE", rs.getString("country_code"));
	         	   outBag.put("TABEL",j,"ADMINISTRATIVE_AREA_NAME", rs.getString("administrative_area_name"));
	         	   outBag.put("TABEL",j,"SUB_ADMINISTRATIVE_AREA_NAME", rs.getString("sub_administrative_area_name"));
	         	   outBag.put("TABEL",j,"STREET", rs.getString("street"));
	         	   outBag.put("TABEL",j,"HOMENUMBER", rs.getString("homenumber"));
	         	   outBag.put("TABEL",j,"POINT_Y", rs.getString("point_y"));
	         	   outBag.put("TABEL",j,"POINT_X", rs.getString("point_x"));
	        	   outBag.put("TABEL",j,"PROCESSED", rs.getString("processed"));
	        	   outBag.put("TABEL",j,"PHARMACY_NUMBER_SALE", rs.getString("status"));	          
	        	   outBag.put("TABEL",j,"ENTRY_USER", rs.getString("entry_user"));	
	        	   outBag.put("TABEL",j,"ENTRY_DATE", rs.getString("entry_date"));	
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
	 
	 public String control_uniquless(String brand,String chain,String point_x,String point_y) throws Exception {
		   Statement stmt =null;
	       Connection conn = null;
	       String sorgu  ="";
		   try{
	           
			   Class.forName("com.mysql.jdbc.Driver");
			   conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));
			   
			   if(brand.equalsIgnoreCase("SOLGAR")){
				   sorgu  = "select * from solgar_tst.pharmacy_data_solgar where status = 1 ";
			   }else{
				   sorgu  = "select * from solgar_tst.pharmacy_data_bounty where status = 1 ";
			   }
	   
			   sorgu = sorgu +" and group_company='"+chain+"'"+" and point_y='"+point_y+"'"+" and point_x='"+point_x+"'";
				          
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