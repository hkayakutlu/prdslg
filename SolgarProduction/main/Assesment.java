package main;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import cb.esi.esiclient.util.ESIBag;
import util.Util;

public class Assesment {
	 Properties prop = ConnectToDb.readConfFile();
	 public Assesment() {
			// TODO Auto-generated constructor stub
		   }
	
	 public void save(ESIBag inBag) throws Exception{
		  Connection conn = null;
		  try{
			  Class.forName("com.mysql.jdbc.Driver");
		      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
		      conn.setAutoCommit(false);
			  
			  saveAssesmentBaseInfo(conn,inBag);
			  savePharmInfo(conn,inBag);	
			  saveDoctorInfo(conn,inBag);
			  saveReleaseInfo(conn,inBag);
			  saveCalculationInfo(conn,inBag);
			 
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
			  int id = Integer.parseInt(inBag.get("RELATIONID").toString());
			  Class.forName("com.mysql.jdbc.Driver");
		      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
		      conn.setAutoCommit(false);
			  
			  saveAssesmentBaseInfo(conn,inBag);
			  deleteAssesmentBaseInfo(conn, id);
			  
			  savePharmInfo(conn,inBag);	
			  deletePharmInfo(conn, id);
			  
			  saveDoctorInfo(conn,inBag);
			  deleteDoctorInfo(conn, id);
			  
			  saveReleaseInfo(conn,inBag);
			  deleteReleaseInfo(conn, id);
			  
			  saveCalculationInfo(conn,inBag);
			  deleteCalculationInfo(conn, id);
			  
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
			   }	}
	 
	 public ESIBag get(ESIBag inBag) throws Exception {
		 
		 return inBag;
		 
	 }
	 
	 
		private void saveAssesmentBaseInfo(Connection conn,ESIBag inBag) throws Exception, SQLException {
			PreparedStatement  stmt = null;				  
			   try{
		    	  stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_org.assesment_main_info(`status`,`employeeId`,`brand`,`country`,"
		    	  		+ "`region`,`city`,`staffName`,`years`,`months`,`monthlyVisit`,`workingDay`,`entry_Date`,`entry_User`)"
					   		+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);				   					   
				
				   stmt.setInt(1,1);//Status
				   stmt.setString(2,inBag.get("EMPLOYEEID"));
				   stmt.setString(3,inBag.get("BRAND"));
				   stmt.setString(4,inBag.get("COUNTRY"));
				   stmt.setString(5,inBag.get("REGION"));
				   stmt.setString(6,inBag.get("CITY"));
				   stmt.setString(7,inBag.get("STAFFNAME"));
				   stmt.setString(8,inBag.get("YEAR"));
				   stmt.setString(9,inBag.get("MONTH"));
				   stmt.setString(10,inBag.get("MONTHLYVISIT"));			
				   stmt.setString(11,inBag.get("WORKINGDAY"));	
				   stmt.setTimestamp(12,Timestamp.valueOf(Util.getCurrentDateTime()));
				   stmt.setString(13,inBag.get("USERNAME"));
				   stmt.executeUpdate();
				   
				   ResultSet keyResultSet = stmt.getGeneratedKeys();
		            if (keyResultSet.next()) {
		            	inBag.put("RELATIONID", String.valueOf(keyResultSet.getInt(1)));
		            }				   
				   stmt.close(); 			   				

			   }catch(SQLException se){
			      throw se;
			   }catch(Exception e){
			      e.printStackTrace();
			      throw e;
			   }finally{}//end try
		
		}
		
		private void deleteAssesmentBaseInfo(Connection conn,int relationId) throws Exception, SQLException {
			PreparedStatement  stmt = null;				  
			   try{
				   stmt = (PreparedStatement) conn.prepareStatement( "update solgar_org.assesment_main_info set status = 0  WHERE status = 1 and Id = ?");	  		         
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
		
		private void savePharmInfo(Connection conn , ESIBag inBag) throws Exception, SQLException{
			PreparedStatement  stmt = null;				  
			   try{
			      String relationId = inBag.get("RELATIONID").toString();			      		    	  
				      int row = inBag.getSize("PHARMTABLE");
						for (int j = 0; j  < row; j++) {
							if(inBag.get("PHARMTABLE",j,"PHARMACYTYPE") != null && inBag.get("PHARMTABLE",j,"PHARMACYTYPE").toString().length()>0){
							   stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_org.assesment_pharmacy_counts(`status`,`relationid`,`pharmacyType`,`sumTotal`)"
								   		+ " VALUES(?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);				   					   
							
							   stmt.setInt(1,1);//Status
							   stmt.setString(2,relationId);
							   stmt.setString(3,inBag.get("PHARMTABLE",j,"PHARMACYTYPE"));
							   stmt.setString(4,inBag.get("PHARMTABLE",j,"SUMTOTAL"));							 
							   stmt.executeUpdate();
							   stmt.close(); 		
							}   
						    
						}   				
					
			      }catch(SQLException se){
				      throw se;
				   }catch(Exception e){
				      e.printStackTrace();
				      throw e;
				   }finally{}//end try
		
		}
		private void deletePharmInfo(Connection conn,int relationId) throws Exception, SQLException {
			PreparedStatement  stmt = null;				  
			   try{
				   stmt = (PreparedStatement) conn.prepareStatement( "update solgar_org.assesment_pharmacy_counts set status = 0  WHERE status = 1 and relationid = ?");	  		         
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
		
		private void saveDoctorInfo(Connection conn , ESIBag inBag) throws Exception, SQLException{
			PreparedStatement  stmt = null;				  
			   try{
			      String relationId = inBag.get("RELATIONID").toString();			      		    	  
				      int row = inBag.getSize("DOCTORTABLE");
						for (int j = 0; j  < row; j++) {
							if(inBag.get("DOCTORTABLE",j,"DOCTORTYPE") != null && inBag.get("DOCTORTABLE",j,"DOCTORTYPE").toString().length()>0){
							   stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_org.assesment_doctor_counts(`status`,`relationid`,`doctorType`,`sumTotal`)"
								   		+ " VALUES(?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);				   					   							
							   stmt.setInt(1,1);//Status
							   stmt.setString(2,relationId);
							   stmt.setString(3,inBag.get("DOCTORTABLE",j,"DOCTORTYPE"));
							   stmt.setString(4,inBag.get("DOCTORTABLE",j,"SUMTOTAL"));							 
							   stmt.executeUpdate();
							   stmt.close(); 		
							}   
						    
						}   				
					
			      }catch(SQLException se){
				      throw se;
				   }catch(Exception e){
				      e.printStackTrace();
				      throw e;
				   }finally{}//end try
		
		}
		private void deleteDoctorInfo(Connection conn,int relationId) throws Exception, SQLException {
			PreparedStatement  stmt = null;				  
			   try{
				   stmt = (PreparedStatement) conn.prepareStatement( "update solgar_org.assesment_doctor_counts set status = 0  WHERE status = 1 and relationid = ?");	  		         
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
		private void saveReleaseInfo(Connection conn , ESIBag inBag) throws Exception, SQLException{
			PreparedStatement  stmt = null;				  
			   try{
			      String relationId = inBag.get("RELATIONID").toString();			      		    	  
				      int row = inBag.getSize("RELEASETABLE");
						for (int j = 0; j  < row; j++) {
							if(inBag.get("RELEASETABLE",j,"BONUSTYPE") != null && inBag.get("RELEASETABLE",j,"BONUSTYPE").toString().length()>0){
							   stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_org.assesment_release(`status`,`relationid`,`bonusType`,`plans`,`realization`,`sumTotal`)"
								   		+ " VALUES(?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);				   					   							
							   stmt.setInt(1,1);//Status
							   stmt.setString(2,relationId);
							   stmt.setString(3,inBag.get("RELEASETABLE",j,"BONUSTYPE"));
							   stmt.setString(4,inBag.get("RELEASETABLE",j,"PLANS"));	
							   stmt.setString(5,inBag.get("RELEASETABLE",j,"REALIZATION"));
							   stmt.setString(6,inBag.get("RELEASETABLE",j,"SUMTOTAL"));
							   stmt.executeUpdate();
							   stmt.close(); 		
							}   
						    
						}   		
					
			      }catch(SQLException se){
				      throw se;
				   }catch(Exception e){
				      e.printStackTrace();
				      throw e;
				   }finally{}//end try
		
		}
		private void deleteReleaseInfo(Connection conn,int relationId) throws Exception, SQLException {
			PreparedStatement  stmt = null;				  
			   try{
				   stmt = (PreparedStatement) conn.prepareStatement( "update solgar_org.assesment_release set status = 0  WHERE status = 1 and relationid = ?");	  		         
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
		private void saveCalculationInfo(Connection conn , ESIBag inBag) throws Exception, SQLException{
			PreparedStatement  stmt = null;				  
			   try{
			      String relationId = inBag.get("RELATIONID").toString();			      		    	  
				      int row = inBag.getSize("CALCTABLE");
						for (int j = 0; j  < row; j++) {
							if(inBag.get("CALCTABLE",j,"BONUSTYPE") != null && inBag.get("CALCTABLE",j,"BONUSTYPE").toString().length()>0){
							   stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_org.assesment_calculation(`status`,`relationid`,`bonusType`,`sumTotal`)"
								   		+ " VALUES(?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);				   					   							
							   stmt.setInt(1,1);//Status
							   stmt.setString(2,relationId);
							   stmt.setString(3,inBag.get("CALCTABLE",j,"BONUSTYPE"));
							   stmt.setString(4,inBag.get("CALCTABLE",j,"SUMTOTAL"));	
							   stmt.executeUpdate();
							   stmt.close(); 		
							}   
						    
						}   		
					
			      }catch(SQLException se){
				      throw se;
				   }catch(Exception e){
				      e.printStackTrace();
				      throw e;
				   }finally{}//end try
		
		}
		private void deleteCalculationInfo(Connection conn,int relationId) throws Exception, SQLException {
			PreparedStatement  stmt = null;				  
			   try{
				   stmt = (PreparedStatement) conn.prepareStatement( "update solgar_org.assesment_calculation set status = 0  WHERE status = 1 and relationid = ?");	  		         
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
		public DefaultTableModel getAllData(ESIBag inBag) throws Exception {
	        Connection conn =null;
	        Statement stmt =null;
	        String selectSQL ="";
	        DefaultTableModel tableModel =null;       
	        try{
	        	int employeeId = Integer.parseInt(inBag.get("EMPLOYEEID").toString());
	        	Class.forName("com.mysql.jdbc.Driver");
			    conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
	            selectSQL = ReportQueries.getQueryScript("ASSESMENT_GET_ALL_DATA");	            
	            
	            if(employeeId == -1){
	            	selectSQL = selectSQL+"  group by b.relationId,c.relationId,d.relationId,e.relationId order by a.id desc";
	            }else{
	            	selectSQL = selectSQL+"and  a.employeeid ="+employeeId+"  group by b.relationId,c.relationId,d.relationId,e.relationId order by a.id desc";
	            }

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
		
		public ESIBag getAssesmentsToScreen(ESIBag inBag) throws Exception {
			
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
		    	   
		           conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"),prop.getProperty("PASS")); 
		           
		         //AgreementBase Info
		           stmt = (PreparedStatement) conn.prepareStatement("select * from solgar_org.assesment_main_info where status = 1 and id = ?");
		           stmt.setInt(1, id);
		           rs = stmt.executeQuery();	           
		           while (rs.next()){	  
		        	   outBag.put("ID", String.valueOf(rs.getInt("id")));
		        	   outBag.put("BRAND", rs.getString("brand"));
		        	   outBag.put("COUNTRY", rs.getString("country"));
		        	   outBag.put("REGION", rs.getString("region"));
		        	   outBag.put("CITY", rs.getString("city"));
		        	   outBag.put("STAFFNAME", rs.getString("staffName"));
		        	   outBag.put("YEAR", rs.getString("years"));
		        	   outBag.put("MONTH", rs.getString("months"));
		        	   outBag.put("MONTHLYVISIT", rs.getString("monthlyVisit"));
		        	   outBag.put("WORKINGDAY", rs.getString("workingDay"));
		           } 	           	           
		           stmt.close();
		           
		         //Pharmacy Info
		           stmt = (PreparedStatement) conn.prepareStatement("select * from solgar_org.assesment_pharmacy_counts where status = 1 and relationid = ? order by id asc");
		           stmt.setInt(1, id);
		           rs = stmt.executeQuery();

		           j =0;
		           while (rs.next()){	  
		        	   outBag.put("PHARMTABLE",j,"ID", String.valueOf(rs.getInt("id")));
		        	   outBag.put("PHARMTABLE",j,"PHARMACYTYPE", rs.getString("pharmacyType"));
		        	   outBag.put("PHARMTABLE",j,"SUMTOTAL", rs.getString("sumTotal"));
		        	   j++;
		           } 	           
		           stmt.close();
		           
		           for (int i = j; i < 12; i++) {
		        	   outBag.put("PHARMTABLE",i,"ID", "");
		        	   outBag.put("PHARMTABLE",i,"PHARMACYTYPE", "");
		        	   outBag.put("PHARMTABLE",i,"SUMTOTAL", "");
		           }
		           
		         //Doctor Info
		           stmt = (PreparedStatement) conn.prepareStatement("select * from solgar_org.assesment_doctor_counts where status = 1 and relationid = ? order by id asc");
		           stmt.setInt(1, id);
		           rs = stmt.executeQuery();

		           j =0;
		           while (rs.next()){	  
		        	   outBag.put("DOCTORTABLE",j,"ID", String.valueOf(rs.getInt("id")));
		        	   outBag.put("DOCTORTABLE",j,"DOCTORTYPE", rs.getString("doctorType"));
		        	   outBag.put("DOCTORTABLE",j,"SUMTOTAL", rs.getString("sumTotal"));
		        	   j++;
		           } 	           
		           stmt.close();
		           
		           for (int i = j; i < 12; i++) {
		        	   outBag.put("DOCTORTABLE",i,"ID", "");
		        	   outBag.put("DOCTORTABLE",i,"DOCTORTYPE", "");
		        	   outBag.put("DOCTORTABLE",i,"SUMTOTAL", "");
		           }
		           
		         //Release Info
		           stmt = (PreparedStatement) conn.prepareStatement("select * from solgar_org.assesment_release where status = 1 and relationid = ? order by id asc");
		           stmt.setInt(1, id);
		           rs = stmt.executeQuery();

		           j =0;
		           while (rs.next()){	  
		        	   outBag.put("RELEASETABLE",j,"ID", String.valueOf(rs.getInt("id")));
		        	   outBag.put("RELEASETABLE",j,"BONUSTYPE", rs.getString("bonusType"));
		        	   outBag.put("RELEASETABLE",j,"PLANS", String.valueOf(rs.getInt("plans")));
		        	   outBag.put("RELEASETABLE",j,"REALIZATION", String.valueOf(rs.getInt("realization")));
		        	   outBag.put("RELEASETABLE",j,"SUMTOTAL", String.valueOf(rs.getInt("sumTotal")));	        	
		        	   j++;
		           } 	           
		           stmt.close();
		           
		           for (int i = j; i < 16; i++) {
		        	   outBag.put("RELEASETABLE",i,"ID", "");
		        	   outBag.put("RELEASETABLE",i,"BONUSTYPE", "");
		        	   outBag.put("RELEASETABLE",i,"PLANS", "");
		        	   outBag.put("RELEASETABLE",i,"REALIZATION", "");
		        	   outBag.put("RELEASETABLE",i,"SUMTOTAL", "");
		           }
		           
		         //Calculation Info
		           stmt = (PreparedStatement) conn.prepareStatement("select * from solgar_org.assesment_calculation where status = 1 and relationid = ? order by id asc");
		           stmt.setInt(1, id);
		           rs = stmt.executeQuery();

		           j =0;
		           while (rs.next()){	  
		        	   outBag.put("CALCTABLE",j,"ID", String.valueOf(rs.getInt("id")));
		        	   outBag.put("CALCTABLE",j,"BONUSTYPE", rs.getString("bonusType"));
		        	   outBag.put("CALCTABLE",j,"SUMTOTAL", rs.getString("sumTotal"));      	
		        	   j++;
		           } 	           
		           stmt.close();    
		           
		           for (int i = j; i < 16; i++) {
		        	   outBag.put("CALCTABLE",i,"ID", "");
		        	   outBag.put("CALCTABLE",i,"BONUSTYPE", "");
		        	   outBag.put("CALCTABLE",i,"SUMTOTAL", "");		      
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
	 
}
