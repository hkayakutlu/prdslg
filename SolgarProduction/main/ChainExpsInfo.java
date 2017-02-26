package main;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Properties;

import util.Util;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import cb.esi.esiclient.util.ESIBag;

public class ChainExpsInfo {

	 Properties prop = ConnectToDb.readConfFile();
	 private static final DecimalFormat doubleFormatter = new DecimalFormat("#,###");
	
  public void save(ESIBag inBag) throws Exception{
	  Connection conn = null;
	  try{
		  Class.forName("com.mysql.jdbc.Driver");
	      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
	      conn.setAutoCommit(false);
		  if(!inBag.get("EXPENSETYPE").toString().equalsIgnoreCase("CAMPAIGN")){
			  saveAgreementBaseInfo(conn,inBag);
		  }
		  saveTargets(conn,inBag);
		  saveReleases(conn,inBag);
		  saveBonusTypes(conn,inBag);
		  savePayments(conn,inBag);		
		 
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
	
  private void savePayments(Connection conn,ESIBag inBag) throws Exception, SQLException {
		PreparedStatement  stmt = null;
				  
		   try{
		      String relationId = inBag.get("RELATIONID").toString();
		      String paymentType  = inBag.get("EXPENSETYPE").toString();
			      int row = inBag.getSize("RESULTTABLE");
					for (int j = 0; j  < row; j++) {
						if(inBag.get("RESULTTABLE",j,"BRAND") != null && inBag.get("RESULTTABLE",j,"BRAND").toString().length()>0){
							   stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_mcs.chain_exps_payment_rows(`status`,`relationid`,`brand`,`chain`,`paymentType`,"
								   		+ "`totalPaymentAmount`,`purchasingSum`,`purchasingAmount`,`sellOutSum`,`sellOutAmount`,`paymentDate`,`paymentOrderNo`,`advertisementType`,`documentStatus`,"
								   		+ "`outgoingAmount`,`balance`,`expenseType`)"
								   		+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
							   
							   String totPaySum = inBag.get("RESULTTABLE",j,"TOTALPAYMENTSUM").toString().replaceAll(" ", "");
							   if(totPaySum.indexOf(",")>0){
								   totPaySum = totPaySum.replace(",", "");
							   }
							   String purcHasingAmount = inBag.get("RESULTTABLE",j,"PURCHASINGAMOUNT").toString().replaceAll(" ", "");
							   if(purcHasingAmount.indexOf(",")>0){
								   purcHasingAmount = purcHasingAmount.replace(",", "");
							   }
							   String selloutAmount = inBag.get("RESULTTABLE",j,"SELLOUTAMOUNT").toString().replaceAll(" ", "");
							   if(selloutAmount.indexOf(",")>0){
								   selloutAmount = selloutAmount.replace(",", "");
							   }
							
							   stmt.setInt(1,1);//Status
							   stmt.setString(2,relationId); 
							   stmt.setString(3,inBag.get("RESULTTABLE",j,"BRAND"));
							   stmt.setString(4,inBag.get("RESULTTABLE",j,"CHAIN"));
							   stmt.setString(5,inBag.get("RESULTTABLE",j,"PAYMENTTYPE"));
							   stmt.setString(6,totPaySum);
							   stmt.setString(7,inBag.get("RESULTTABLE",j,"PURCHASINGSUM"));
							   stmt.setString(8,purcHasingAmount);
							   stmt.setString(9,inBag.get("RESULTTABLE",j,"SELLOUTSUM"));
							   stmt.setString(10,selloutAmount);
							   stmt.setDate(11,Date.valueOf(inBag.get("RESULTTABLE",j,"PAYMENTDATE")));
							   stmt.setString(12,inBag.get("RESULTTABLE",j,"PAYMENTORDERNO"));
							   stmt.setString(13,inBag.get("RESULTTABLE",j,"ADVTYPE"));
							   stmt.setString(14,inBag.get("RESULTTABLE",j,"DOCUMENTSTATUS"));
							   stmt.setString(15,inBag.get("RESULTTABLE",j,"OUTGOINGAMOUNT"));
							   stmt.setString(16,inBag.get("RESULTTABLE",j,"BALANCE"));
							   stmt.setString(17,paymentType);
							   		
							   
							   stmt.executeUpdate();
							   stmt.close(); 
						}
					    
					}   
				
					//conn.commit();					
				
		      }catch(SQLException se){
			      throw se;
			   }catch(Exception e){
			      e.printStackTrace();
			      throw e;
			   }finally{}//end try
		}//end main
	
	private void deletePayments(Connection conn,int relationId) throws Exception, SQLException {
		PreparedStatement  stmt = null;				  
		   try{
			   stmt = (PreparedStatement) conn.prepareStatement( "update solgar_mcs.chain_exps_payment_rows set status = 0  WHERE status = 1 and relationId = ?");	  		         
			   stmt.setInt(1,relationId);
			   stmt.executeUpdate();
			   stmt.close(); 					
				
		      }catch(SQLException se){
			      throw se;
			   }catch(Exception e){
			      e.printStackTrace();
			      throw e;
			   }finally{}//end try	
	}

	private void saveBonusTypes(Connection conn,ESIBag inBag) throws Exception, SQLException{
		PreparedStatement  stmt = null;				  
		   try{
		      String relationId = inBag.get("RELATIONID").toString();
    	  
			      int row = inBag.getSize("BONUSTABLE");
					for (int j = 0; j  < row; j++) {
					   stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_mcs.chain_exps_bonus_types(`status`,`relationid`,`bonusType`,`rate1`,`rate2`,"
						   		+ "`rate3`,`rate4`)"
						   		+ " VALUES(?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);				   					   
					
					   stmt.setInt(1,1);//Status
					   stmt.setString(2,relationId); 
					   stmt.setString(3,inBag.get("BONUSTABLE",j,"BONUSTYPE"));
					   stmt.setString(4,inBag.get("BONUSTABLE",j,"RATE1"));
					   stmt.setString(5,inBag.get("BONUSTABLE",j,"RATE2"));
					   stmt.setString(6,inBag.get("BONUSTABLE",j,"RATE3"));
					   stmt.setString(7,inBag.get("BONUSTABLE",j,"RATE4"));
							   
					   stmt.executeUpdate();
					   stmt.close(); 			   
					    
					}   				
					//conn.commit();					
				
		      }catch(SQLException se){
			      throw se;
			   }catch(Exception e){
			      e.printStackTrace();
			      throw e;
			   }finally{}//end try
		
	
	}
	private void deleteBonusTypes(Connection conn,int relationId) throws Exception, SQLException {
		PreparedStatement  stmt = null;				  
		   try{
			   stmt = (PreparedStatement) conn.prepareStatement( "update solgar_mcs.chain_exps_bonus_types set status = 0  WHERE status = 1 and relationId = ?");	  		         
			   stmt.setInt(1,relationId);
			   stmt.executeUpdate();
			   stmt.close(); 					
				
		      }catch(SQLException se){
			      throw se;
			   }catch(Exception e){
			      e.printStackTrace();
			      throw e;
			   }finally{}//end try	
	}

	private void saveReleases(Connection conn,ESIBag inBag) throws Exception, SQLException {
		PreparedStatement  stmt = null;				  
		   try{
		      String relationId = inBag.get("RELATIONID").toString();
		      String paymentType  = inBag.get("EXPENSETYPE").toString();
			      int row = inBag.getSize("RELEASETABLE");
					for (int j = 0; j  < row; j++) {
						if(inBag.get("RELEASETABLE",j,"YEAR") != null && inBag.get("RELEASETABLE",j,"YEAR").toString().length()>0){
						   stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_mcs.chain_exps_release(`status`,`relationid`,`ordering`,`year`,`month`,"
							   		+ "`boxCountPurchase`,`boxCountSellOut`,`amount`,`expenseType`)"
							   		+ " VALUES(?,?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);				   					   
						
						   stmt.setInt(1,1);//Status
						   stmt.setString(2,relationId);
						   stmt.setString(3,inBag.get("RELEASETABLE",j,"ORDER"));
						   stmt.setString(4,inBag.get("RELEASETABLE",j,"YEAR"));
						   stmt.setString(5,inBag.get("RELEASETABLE",j,"MONTH"));
						   stmt.setString(6,inBag.get("RELEASETABLE",j,"PURCHASEBOXCOUNT"));
						   stmt.setString(7,inBag.get("RELEASETABLE",j,"SELLOUTBOXCOUNT"));
						   stmt.setString(8,inBag.get("RELEASETABLE",j,"AMOUNT"));
						   stmt.setString(9,paymentType);
						   
						   stmt.executeUpdate();
						   stmt.close(); 	
						}
					    
					}   

					//conn.commit();					
				
		      }catch(SQLException se){
			      throw se;
			   }catch(Exception e){
			      e.printStackTrace();
			      throw e;
			   }finally{}//end try
	
	}
	
	private void deleteReleases(Connection conn,int relationId) throws Exception, SQLException {
		PreparedStatement  stmt = null;				  
		   try{
			   stmt = (PreparedStatement) conn.prepareStatement( "update solgar_mcs.chain_exps_release set status = 0  WHERE status = 1 and relationId = ?");	  		         
			   stmt.setInt(1,relationId);
			   stmt.executeUpdate();
			   stmt.close(); 					
				
		      }catch(SQLException se){
			      throw se;
			   }catch(Exception e){
			      e.printStackTrace();
			      throw e;
			   }finally{}//end try	
	}


	private void saveTargets(Connection conn , ESIBag inBag) throws Exception, SQLException{
		PreparedStatement  stmt = null;				  
		   try{
		      String relationId = inBag.get("RELATIONID").toString();
		      String paymentType  = inBag.get("EXPENSETYPE").toString();
		      		    	  
			      int row = inBag.getSize("TARGETTABLE");
					for (int j = 0; j  < row; j++) {
						if(inBag.get("TARGETTABLE",j,"YEAR") != null && inBag.get("TARGETTABLE",j,"YEAR").toString().length()>0){
						   stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_mcs.chain_exps_targets(`status`,`relationid`,`ordering`,`year`,`month`,"
							   		+ "`target1`,`target2`,`target3`,`target4`,`target5`,`expenseType`)"
							   		+ " VALUES(?,?,?,?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);				   					   
						
						   stmt.setInt(1,1);//Status
						   stmt.setString(2,relationId);
						   stmt.setString(3,inBag.get("TARGETTABLE",j,"ORDER"));
						   stmt.setString(4,inBag.get("TARGETTABLE",j,"YEAR"));
						   stmt.setString(5,inBag.get("TARGETTABLE",j,"MONTH"));
						   stmt.setString(6,inBag.get("TARGETTABLE",j,"TARGET1"));
						   stmt.setString(7,inBag.get("TARGETTABLE",j,"TARGET2"));
						   stmt.setString(8,inBag.get("TARGETTABLE",j,"TARGET3"));
						   stmt.setString(9,inBag.get("TARGETTABLE",j,"TARGET4"));
						   //stmt.setString(10,inBag.get("TARGETTABLE",j,"TARGET5"));	
						   stmt.setString(10,"0");
						   stmt.setString(11,paymentType);	
						   stmt.executeUpdate();
						   stmt.close(); 		
						}   
					    
					}   

					//conn.commit();					
				
		      }catch(SQLException se){
			      throw se;
			   }catch(Exception e){
			      e.printStackTrace();
			      throw e;
			   }finally{}//end try
	
	}

	private void saveAgreementBaseInfo(Connection conn,ESIBag inBag) throws Exception, SQLException {
		PreparedStatement  stmt = null;				  
		   try{
	    	  stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_mcs.chain_exps_agreement_base_info(`status`,`company`,`chain`,`country`,`area`,"
				   		+ "`region`,`city`,`pharmacyCount`,`agreementNo`,`agreementBeginDate`,`agreementEndDate`,`bonusRate`,`entryDate`,`entryUser`)"
				   		+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);				   					   
			
			   stmt.setInt(1,1);//Status
			   stmt.setString(2,inBag.get("COMPANY"));
			   stmt.setString(3,inBag.get("CHAIN"));
			   stmt.setString(4,inBag.get("COUNTRY"));
			   stmt.setString(5,inBag.get("AREA"));
			   stmt.setString(6,inBag.get("REGION"));
			   stmt.setString(7,inBag.get("CITY"));
			   stmt.setString(8,inBag.get("PHARMACYCOUNT"));
			   stmt.setString(9,inBag.get("AGREEMENTNO"));
			   stmt.setDate(10,Date.valueOf(inBag.get("AGREEMENTBEGINDATE")));			
			   stmt.setDate(11,Date.valueOf(inBag.get("AGREEMENTENDDATE")));	
			   stmt.setString(12,inBag.get("BONUSRATE").replaceAll(",", "."));
			   stmt.setTimestamp(13,Timestamp.valueOf(Util.getCurrentDateTime()));
			   stmt.setString(14,inBag.get("USERNAME"));
			   stmt.executeUpdate();
			   
			   ResultSet keyResultSet = stmt.getGeneratedKeys();
	            if (keyResultSet.next()) {
	            	inBag.put("RELATIONID", String.valueOf(keyResultSet.getInt(1)));
	            }
			   
			   stmt.close(); 			   				
				  // conn.commit();		

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
			  int id = Integer.parseInt(inBag.get("RELATIONID").toString());
			  
			  Class.forName("com.mysql.jdbc.Driver");
		      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
		      conn.setAutoCommit(false);
			  
		      deleteReleases(conn,id);
			  saveReleases(conn,inBag);
			  deleteBonusTypes(conn,id);
			  saveBonusTypes(conn,inBag);
			  deletePayments(conn,id);
			  savePayments(conn,inBag);		
			 
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
	public void delete(ESIBag inBag) throws Exception{
		  
	  try{
	  
	  } catch (Exception e) {
		System.out.println(e.getMessage());
		throw e;
	  }			  
			  
	}
	
	public ESIBag getAgreement(ESIBag inBag) throws Exception {
			
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
	           String sorgu  = "select * from solgar_mcs.chain_exps_agreement_base_info where status = 1 order by id desc"; 	                     
	           
	           stmt = (Statement) conn.createStatement();
	           ResultSet rs = stmt.executeQuery(sorgu);

	           int j =0;
	           while (rs.next()){	  
	        	   outBag.put("TABLE",j,"ID", String.valueOf(rs.getInt("id")));
	        	   outBag.put("TABLE",j,"BRAND", rs.getString("company"));
	        	   outBag.put("TABLE",j,"CHAIN", rs.getString("chain"));
	        	   outBag.put("TABLE",j,"AGREEMENTNO", rs.getString("agreementNo"));
	        	   outBag.put("TABLE",j,"BEGINDATE", rs.getString("agreementBeginDate"));
	        	   outBag.put("TABLE",j,"ENDDATE", rs.getString("agreementEndDate"));
	        	   outBag.put("TABLE",j,"BONUSRATE", rs.getString("bonusRate"));
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
	
	public ESIBag getAgreementAll(ESIBag inBag) throws Exception {
		
		   String url = prop.getProperty("DB_URL");
	       Connection conn = null;
	       PreparedStatement stmt =null;
	       ResultSet rs = null;
	       ESIBag outBag = new ESIBag();
	       int j =0;
	       
	       try{
	           Class.forName(prop.getProperty("JDBC_DRIVER"));
	       }
	       catch(java.lang.ClassNotFoundException cnfe){
	           System.out.println("Class Not Found - " + cnfe.getMessage());
	       }       

	       try{
	    	   int id = Integer.parseInt(inBag.get("ID").toString());
	    	   String expenseType = inBag.get("EXPENSETYPE").toString();
	    	   
	           conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"),prop.getProperty("PASS")); 
	           
	         //AgreementBase Info
	           stmt = (PreparedStatement) conn.prepareStatement("select * from solgar_mcs.chain_exps_agreement_base_info where status = 1 and id = ?");
	           stmt.setInt(1, id);
	           rs = stmt.executeQuery();	           
	           while (rs.next()){	  
	        	   outBag.put("ID", String.valueOf(rs.getInt("id")));
	        	   outBag.put("COMPANY", rs.getString("company"));
	        	   outBag.put("CHAIN", rs.getString("chain"));
	        	   outBag.put("COUNTRY", rs.getString("country"));
	        	   outBag.put("AREA", rs.getString("area"));
	        	   outBag.put("REGION", rs.getString("region"));
	        	   outBag.put("CITY", rs.getString("city"));
	        	   outBag.put("PHARMACYCOUNT", String.valueOf(rs.getInt("pharmacyCount")));
	        	   outBag.put("AGREEMENTNO", rs.getString("agreementNo"));
	        	   outBag.put("BEGINDATE", rs.getString("agreementBeginDate"));
	        	   outBag.put("ENDDATE", rs.getString("agreementEndDate"));
	        	   outBag.put("BONUSRATE", rs.getString("bonusRate"));
	           } 	           	           
	           stmt.close();
	           
	         //Target Info
	           stmt = (PreparedStatement) conn.prepareStatement("select * from solgar_mcs.chain_exps_targets where status = 1 and relationid = ? and expenseType = ? order by ordering desc");
	           stmt.setInt(1, id);
	           stmt.setString(2, expenseType);
	           rs = stmt.executeQuery();

	           j =0;
	           while (rs.next()){	  
	        	   outBag.put("TABLETARGET",j,"ID", String.valueOf(rs.getInt("id")));
	        	   outBag.put("TABLETARGET",j,"YEAR", rs.getString("year"));
	        	   outBag.put("TABLETARGET",j,"MONTH", rs.getString("month"));
	        	   outBag.put("TABLETARGET",j,"TRGT1", rs.getString("target1"));
	        	   outBag.put("TABLETARGET",j,"TRGT2", rs.getString("target2"));
	        	   outBag.put("TABLETARGET",j,"TRGT3", rs.getString("target3"));
	        	   outBag.put("TABLETARGET",j,"TRGT4", rs.getString("target4"));
	        	   outBag.put("TABLETARGET",j,"TRGT5", rs.getString("target5"));
	        	   j++;
	           } 	           
	           stmt.close();
	           
	           for (int i = j; i < 24; i++) {
	        	   outBag.put("TABLETARGET",i,"ID", "");
	        	   outBag.put("TABLETARGET",i,"YEAR", "");
	        	   outBag.put("TABLETARGET",i,"MONTH", "");
	        	   outBag.put("TABLETARGET",i,"TRGT1", "");
	        	   outBag.put("TABLETARGET",i,"TRGT2", "");
	        	   outBag.put("TABLETARGET",i,"TRGT3", "");
	        	   outBag.put("TABLETARGET",i,"TRGT4", "");
	        	   outBag.put("TABLETARGET",i,"TRGT5", "");
	           }
	           
	         //Release Info
	           stmt = (PreparedStatement) conn.prepareStatement("select * from solgar_mcs.chain_exps_release where status = 1 and relationid = ? and expenseType = ? order by ordering desc");
	           stmt.setInt(1, id);
	           stmt.setString(2, expenseType);
	           rs = stmt.executeQuery();

	           j =0;
	           while (rs.next()){	  
	        	   outBag.put("TABLERELEASE",j,"ID", String.valueOf(rs.getInt("id")));
	        	   outBag.put("TABLERELEASE",j,"YEAR", rs.getString("year"));
	        	   outBag.put("TABLERELEASE",j,"MONTH", rs.getString("month"));
	        	   outBag.put("TABLERELEASE",j,"PURCHASEBOXCOUNT", String.valueOf(rs.getInt("boxCountPurchase")));
	        	   outBag.put("TABLERELEASE",j,"SELLOUTBOXCOUNT", String.valueOf(rs.getInt("boxCountSellOut")));
	        	   outBag.put("TABLERELEASE",j,"AMOUNT", rs.getString("amount"));
	        	   j++;
	           } 	           
	           stmt.close();
	           
	           for (int i = j; i < 24; i++) {
	        	   outBag.put("TABLERELEASE",i,"ID", "");
	        	   outBag.put("TABLERELEASE",i,"YEAR", "");
	        	   outBag.put("TABLERELEASE",i,"MONTH", "");
	        	   outBag.put("TABLERELEASE",i,"BOX", "");
	        	   outBag.put("TABLERELEASE",i,"AMOUNT", "");
	           }
	           
	         //BonusType Info
	           stmt = (PreparedStatement) conn.prepareStatement("select * from solgar_mcs.chain_exps_bonus_types where status = 1 and relationid = ?");
	           stmt.setInt(1, id);
	           rs = stmt.executeQuery();

	           j =0;
	           while (rs.next()){	  
	        	   outBag.put("TABLEBONUSTYPES",j,"ID", String.valueOf(rs.getInt("id")));
	        	   outBag.put("TABLEBONUSTYPES",j,"BONUSTYPE", rs.getString("bonusType"));
	        	   outBag.put("TABLEBONUSTYPES",j,"RATE1", rs.getString("rate1"));
	        	   outBag.put("TABLEBONUSTYPES",j,"RATE2", rs.getString("rate2"));
	        	   outBag.put("TABLEBONUSTYPES",j,"RATE3", rs.getString("rate3"));
	        	   outBag.put("TABLEBONUSTYPES",j,"RATE4", rs.getString("rate4"));	        	
	        	   j++;
	           } 	           
	           stmt.close();
	           
	         //Payment Info
	           stmt = (PreparedStatement) conn.prepareStatement("select * from solgar_mcs.chain_exps_payment_rows where status = 1 and relationid = ? and expenseType = ?");
	           stmt.setInt(1, id);
	           stmt.setString(2, expenseType);
	           rs = stmt.executeQuery();

	           j =0;
	           while (rs.next()){	  
	        	   outBag.put("TABLERESULT",j,"ID", String.valueOf(rs.getInt("id")));
	        	   outBag.put("TABLERESULT",j,"BRAND", rs.getString("brand"));
	        	   outBag.put("TABLERESULT",j,"CHAIN", rs.getString("chain"));
	        	   outBag.put("TABLERESULT",j,"PAYMENTTYPE", rs.getString("paymenttype"));
	        	   outBag.put("TABLERESULT",j,"TOTALPAYMENTSUM", rs.getString("totalPaymentAmount"));
	        	   outBag.put("TABLERESULT",j,"PURCHASINGSUM", rs.getString("purchasingSum"));
	        	   outBag.put("TABLERESULT",j,"PURCHASINGAMOUNT", String.valueOf(rs.getInt("purchasingAmount")));
	        	   outBag.put("TABLERESULT",j,"SELLOUTSUM", rs.getString("sellOutSum"));
	        	   outBag.put("TABLERESULT",j,"SELLOUTAMOUNT", String.valueOf(rs.getInt("sellOutAmount")));
	        	   outBag.put("TABLERESULT",j,"DATE", String.valueOf(rs.getDate("paymentDate")));
	        	   outBag.put("TABLERESULT",j,"PAYMENTORDERNO", rs.getString("paymentOrderNo"));
	        	   outBag.put("TABLERESULT",j,"ADVTYPE", rs.getString("advertisementType"));
	        	   outBag.put("TABLERESULT",j,"DOCUMENTSTATUS", rs.getString("documentStatus"));
	        	   outBag.put("TABLERESULT",j,"OUTGOINGAMOUNT", rs.getString("outgoingAmount"));
	        	   outBag.put("TABLERESULT",j,"BALANCE", rs.getString("balance"));      	
	        	   j++;
	           } 	           
	           stmt.close();    
	           
	           for (int i = j; i < 36; i++) {
	        	   outBag.put("TABLERESULT",i,"ID", "");
	        	   outBag.put("TABLERESULT",i,"BRAND", "");
	        	   outBag.put("TABLERESULT",i,"CHAIN", "");
	        	   outBag.put("TABLERESULT",i,"PAYMENTTYPE", "");
	        	   outBag.put("TABLERESULT",i,"TOTALPAYMENTSUM", "");
	        	   outBag.put("TABLERESULT",i,"PURCHASINGSUM", "");
	        	   outBag.put("TABLERESULT",i,"PURCHASINGAMOUNT", "");
	        	   outBag.put("TABLERESULT",i,"SELLOUTSUM", "");
	        	   outBag.put("TABLERESULT",i,"SELLOUTAMOUNT", "");
	        	   outBag.put("TABLERESULT",i,"DATE", "");
	        	   outBag.put("TABLERESULT",i,"PAYMENTORDERNO", "");
	        	   outBag.put("TABLERESULT",i,"ADVTYPE", "");
	        	   outBag.put("TABLERESULT",i,"DOCUMENTSTATUS", "");
	        	   outBag.put("TABLERESULT",i,"OUTGOINGAMOUNT","");
	        	   outBag.put("TABLERESULT",i,"BALANCE", "");
	           }
	           
	           //Discount Info
	           stmt = (PreparedStatement) conn.prepareStatement("select * from solgar_mcs.chain_exps_discount_sales where status = 1 and relationid = ? and expenseType = ? order by ordering desc");
	           stmt.setInt(1, id);
	           stmt.setString(2, expenseType);
	           rs = stmt.executeQuery();

	           j =0;
	           while (rs.next()){	  
	        	   outBag.put("TABLEDISCOUNT",j,"ID", String.valueOf(rs.getInt("id")));
	        	   outBag.put("TABLEDISCOUNT",j,"YEAR", rs.getString("year"));
	        	   outBag.put("TABLEDISCOUNT",j,"MONTH", rs.getString("month"));
	        	   outBag.put("TABLEDISCOUNT",j,"DISCOUNTRATE", String.valueOf(rs.getInt("discountRate")));
	        	   outBag.put("TABLEDISCOUNT",j,"REALIZATION", rs.getString("realization"));
	        	   outBag.put("TABLEDISCOUNT",j,"PACK", rs.getString("pack"));
	        	   outBag.put("TABLEDISCOUNT",j,"SUM", rs.getString("sum"));
	        	   j++;
	           } 	           
	           stmt.close();
	           for (int i = j; i < 24; i++) {
	        	   outBag.put("TABLEDISCOUNT",i,"ID","");
	        	   outBag.put("TABLEDISCOUNT",i,"YEAR", "");
	        	   outBag.put("TABLEDISCOUNT",i,"MONTH", "");
	        	   outBag.put("TABLEDISCOUNT",i,"DISCOUNTRATE", "");
	        	   outBag.put("TABLEDISCOUNT",i,"REALIZATION", "");
	        	   outBag.put("TABLEDISCOUNT",i,"PACK", "");
	        	   outBag.put("TABLEDISCOUNT",i,"SUM", "");
	           }
	           
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
	private String formatAmount(String amount){			
		if(amount == null || amount.length()==0){
			amount = "0";
		}
		if(amount.indexOf(" ")>=0){amount = amount.replace(" ", "");}
		//String tempAmount = doubleFormatter.format(Double.valueOf(amount));
		return amount;
	}
	private String formatCount(String count){			
		if(count == null || count.length()==0){
			count = "0";
		}
		if(count.indexOf(".")>=0){count = count.replace(".", "");}
		String tempAmount = doubleFormatter.format(Double.valueOf(count));
		return tempAmount;
	}
}