package util;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.JComboBox;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import cb.esi.esiclient.util.ESIBag;
import main.ConnectToDb;
import main.YandexAddress;


public class Util {
	
	static final Properties prop = ConnectToDb.readConfFile();

	public static String getCurrentDateTime() {  	   
		String currDate = "";		
			try {			

				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				currDate = dateFormat.format(cal.getTime()); 
				
			} catch (Exception e) {
	           e.printStackTrace();
	       }			
			return currDate;
	  }
	public static String getCurrentDate() {  	   
		String currDate = "";		
			try {			

				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				currDate = dateFormat.format(cal.getTime()); 
				
			} catch (Exception e) {
	           e.printStackTrace();
	       }			
			return currDate;
	  }
	
	public static void getPRMData(String columnName,String tableName,JComboBox cmbBox) throws SQLException {
        Connection conn = null;
        Statement stmt = null;       

        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));             
            String sorgu  = "select "+columnName+" from " +tableName +" order by "+columnName;
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
            if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				conn.rollback();
				throw se;
			} 
		}

    }
	public static void getPRMDataGrpBy(String columnName,String tableName,JComboBox cmbBox) throws SQLException {
        Connection conn = null;
        Statement stmt = null;       

        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));             
            String sorgu  = "select "+columnName+" from " +tableName + " group by "+columnName +" order by "+columnName;
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
            if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				conn.rollback();
				throw se;
			} 
		}

    }
	public static void getPRMDataGroupBy(String columnName,String tableName,JComboBox cmbBox,String conditionColumn,String conditionValue) throws SQLException {
		 Connection conn = null;
	        Statement stmt = null;
        String sorgu="";        
        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));              
            if(conditionValue.length()>0){
            	sorgu  = "select "+columnName+" from " +tableName + " where " + conditionColumn+ "='"+conditionValue+ "' group by "+columnName+" order by "+columnName ;
            }else{
            	sorgu  = "select "+columnName+" from " +tableName + " group by "+columnName +" order by "+columnName;
            }            
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
            if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				conn.rollback();
				throw se;
			} 
		}
	}
	
	public static void getPRMDataTwoConditionsGroupBy(String columnName,String tableName,JComboBox cmbBox,String conditionColumn,String conditionValue,String conditionColumn1,String conditionValue1) throws SQLException {
		Connection conn = null;
	    Statement stmt = null;
        String sorgu="";
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));            
        	if(conditionValue.length()>0 && conditionValue1.length()>0){
            	sorgu  = "select "+columnName+" from " +tableName + " where " + conditionColumn+ "='"+conditionValue+ "' and " + conditionColumn1+ "='"+conditionValue1+ "' group by "+columnName +" order by "+columnName;
            }else if(conditionValue.length()>0){
            	sorgu  = "select "+columnName+" from " +tableName + " where " + conditionColumn+ "='"+conditionValue+ "' group by "+columnName +" order by "+columnName;
            }else{
            	sorgu  = "select "+columnName+" from " +tableName + " group by "+columnName +" order by "+columnName;
            }
            
            stmt = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(sorgu);
            
            cmbBox.removeAllItems();

            while (rs.next()){
            	cmbBox.addItem(rs.getString(columnName));
            }
            
            stmt.close();
            conn.close();
        }
        catch(SQLException sqle){
            System.out.println("SQL Exception: " + sqle.getMessage());
            if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				conn.rollback();
				throw se;
			} 
		}
	}
	public static void getPRMDataThreeConditionsGroupBy(String columnName,String tableName,JComboBox cmbBox,
			String conditionColumn,String conditionValue,String conditionColumn1,String conditionValue1,String conditionColumn2,String conditionValue2) throws SQLException {
		Connection conn = null;
	    Statement stmt = null;
        String sorgu="";
        try{
            Class.forName(prop.getProperty("JDBC_DRIVER"));
        }
        catch(java.lang.ClassNotFoundException cnfe){
            System.out.println("Class Not Found - " + cnfe.getMessage());
        }       

        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));            
            sorgu  = "select "+columnName+" from " +tableName + " where " + 
        	conditionColumn+ "='"+conditionValue+ "' and " + conditionColumn1+ "='"+conditionValue1+"' and " + conditionColumn2+ "='"+conditionValue2+
        	"' group by "+columnName +" order by "+columnName;          
            stmt = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(sorgu);
            
            cmbBox.removeAllItems();

            while (rs.next()){
            	cmbBox.addItem(rs.getString(columnName));
            }
            
            stmt.close();
            conn.close();
        }
        catch(SQLException sqle){
            System.out.println("SQL Exception: " + sqle.getMessage());
            if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				conn.rollback();
				throw se;
			} 
		}
	}
	
	public static ESIBag getAuthorization(String user,String pass) throws Exception {

		PreparedStatement preparedStatement=null;
        Connection conn =null;
        String employee_name = ""; 
        int employeeId = 0;
        ESIBag outBag = new ESIBag();

        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));         
            String sorgu  = "select * from solgar_org.employees where EMPLOYEE_EMAIL = ? and EMPLOYEE_PASSWORD=?;";
            preparedStatement = (PreparedStatement)conn.prepareStatement(sorgu);
        	preparedStatement.setString(1, user);
        	preparedStatement.setString(2, pass);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
            	 outBag.put("EMPLOYEE_NAME", rs.getString("EMPLOYEE_NAME"));
                 outBag.put("ID", rs.getString("ID"));
            }
 
        }
        catch(SQLException sqle){       	
            System.out.println("SQL Exception: " + sqle.getMessage());
            if (conn != null) {
				conn.close();
			}
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
	
	public static ESIBag getMenuUserRelation(String userName) throws Exception {
        Connection conn =null;
        PreparedStatement preparedStatement=null;
        String selectSQL ="";
        ESIBag outBag = new ESIBag();
          
        try{
        	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));
            
            selectSQL = "select * from solgar_org.menu_user_relation where employee_name = ?";//getQueryScript(reportName);            
            preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
            preparedStatement.setString(1, userName);            
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){    	
                outBag.put("BRAND", rs.getString("brand"));
                outBag.put("COUNTRY", rs.getString("country"));
                outBag.put("AREA", rs.getString("area"));
                outBag.put("SALESENTRY", rs.getString("sales_entry"));
                outBag.put("SALESOBSERVATION", rs.getString("sales_observation"));
                outBag.put("EXPSENTRY", rs.getString("exps_entry"));
                outBag.put("EXPSUPDATE", rs.getString("exps_update"));
                outBag.put("EXPSOBSERVATION", rs.getString("exps_observation"));
                outBag.put("EXPSAPPROVE", rs.getString("exps_approve"));
                outBag.put("STORAGEENTRY", rs.getString("distrubutor_sale_stock"));
                outBag.put("STORAGEOBSERVATION", rs.getString("distrubutor_sale_observation"));
                outBag.put("CHAINEXPSENTRY", rs.getString("chain_exps_entry"));
                outBag.put("CHAINEXPSUPDATE", rs.getString("chain_exps_update"));
                outBag.put("CHAINEXPSOBSERVATION", rs.getString("chain_exps_observation"));              
                outBag.put("EXPSUPDATEAPPROVED", rs.getString("exps_update_apprvd_ops"));
                outBag.put("CHAINEXPSCAMPAIGNENTRY", rs.getString("chain_exps_campaign_entry"));
                outBag.put("STAFFDEFINITION", rs.getString("staff_definition"));
                outBag.put("STAFFASSDEFINITION", rs.getString("staff_assesment_definition"));
                outBag.put("STAFFASSOBSERVATION", rs.getString("staff_assesment_observation"));
                outBag.put("PHARMDATADEFINITION", rs.getString("pharm_data_definition"));
                outBag.put("DOCTORDATADEFINITION", rs.getString("doctor_data_definition"));
                outBag.put("EXECUTIVEMRKTEXPENSE", rs.getString("executive_mrkt_expense"));
                outBag.put("EXECUTIVEPHARMACY", rs.getString("executive_pharmacy"));
                outBag.put("EXECUTIVEDOCTOR", rs.getString("executive_doctor"));
                outBag.put("EXECUTIVECHAINEXPENSE", rs.getString("executive_chain_expense"));
            } 

        }
        catch(SQLException sqle){       	
            System.out.println("SQL Exception: " + sqle.getMessage());
            if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (conn != null) {
				conn.close();
			}
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
	
	public static String changePass(String userName,String oldPass,String newPass) throws Exception {

		  Connection conn = null;
		  PreparedStatement  stmt = null;
		  PreparedStatement preparedStatement=null;
	      String selectSQL ="";
		  String changepass = "0";
		  		  
		  try{
		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");
		      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
                  	
	            selectSQL = "select * from solgar_org.employees where employee_password = ?  and employee_name = ?";
	            preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
	            preparedStatement.setString(1, oldPass);
	            preparedStatement.setString(2, userName);
	            
	            ResultSet rs = preparedStatement.executeQuery();

	            while (rs.next()){    	
	              stmt = (PreparedStatement) conn.prepareStatement( "update solgar_org.employees set employee_password = ?  WHERE employee_name = ? and employee_password = ?");	  		      
	  		      stmt.setString(1,newPass);
	  		      stmt.setString(2,userName);
	  		      stmt.setString(3,oldPass);	  		      
	  		      stmt.executeUpdate();
	  		      stmt.close();
	  		     changepass="1";
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
		         if(preparedStatement!=null)
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
		   
		  return changepass;
		}//end main	

	
	public static boolean controlVersion(String versionNumber) throws Exception {
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
    	  conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));                    	
          selectSQL = "select *  from solgar_gen.dad_versions where versionNumber = ?";
          preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
          preparedStatement.setString(1, versionNumber);          
          ResultSet rs = preparedStatement.executeQuery();
          while (rs.next()){    	
              if(rs.getDate("endDate")!= null){
              	return true;
              }
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
	public static void updatePassword(String pass,String empName) throws Exception {

		  Connection conn = null;
		  PreparedStatement  stmt = null;
		   try{

		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");
		      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));
		      stmt = (PreparedStatement) conn.prepareStatement( "update solgar_org.employees set EMPLOYEE_PASSWORD =? where EMPLOYEE_NAME =?");
		      
		      stmt.setString(1,pass);
		      stmt.setString(2,empName);
		      
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
		}//end main	
	
	public static ESIBag getOfficialAddressInfo(ESIBag outBag) throws Exception {
		   Statement stmt =null;
	       Connection conn = null;
	       String sorgu  ="";
	       
	       String full_address="";
		   String requested="";
		   String building_type="";
		   String country_code="";
		   String administrative_area_name="";
		   String sub_administrative_area_name="";
		   String street="";
		   String homenumber="";
		   String point_y="";
		   String point_x="";
	       
		   try{
			   if (outBag.existsBagKey("REQUEST")) {
					requested = outBag.get("REQUEST").toString();
				}
			   
			   	ESIBag tempBag = new ESIBag();
				tempBag.put("ADDRESSROW1", requested);
				tempBag = YandexAddress.getResult(tempBag);
			   
				if (tempBag.existsBagKey("FULLADDRESS")) {
					full_address = tempBag.get("FULLADDRESS").toString();
				}				
				if (tempBag.existsBagKey("BUILDINGTYPE")) {
					building_type = tempBag.get("BUILDINGTYPE").toString();
				}
				if (tempBag.existsBagKey("COUNTRYCODE")) {
					country_code = tempBag.get("COUNTRYCODE").toString();
				}
				if (tempBag.existsBagKey("ADMINISTRATIVEAREANAME")) {
					administrative_area_name = tempBag.get("ADMINISTRATIVEAREANAME").toString();
				}
				if (tempBag.existsBagKey("SUBADMINISTRATIVEAREANAME")) {
					sub_administrative_area_name = tempBag.get("SUBADMINISTRATIVEAREANAME").toString();
				}
				if (tempBag.existsBagKey("STREET")) {
					street = tempBag.get("STREET").toString();
				}
				if (tempBag.existsBagKey("HOMENUMBER")) {
					homenumber = tempBag.get("HOMENUMBER").toString();
				}
				if (tempBag.existsBagKey("POINTY")) {
					point_y = tempBag.get("POINTY").toString();
				}
				if (tempBag.existsBagKey("POINTX")) {
					point_x = tempBag.get("POINTX").toString();
				}
				
				outBag.put("FULL_ADDRESS", full_address);
				outBag.put("BUILDING_TYPE", building_type);
				outBag.put("COUNTRY_CODE", country_code);
				outBag.put("ADMINISTRATIVE_AREA_NAME", administrative_area_name);
				outBag.put("SUB_ADMINISTRATIVE_AREA_NAME", sub_administrative_area_name);
				outBag.put("STREET", street);
				outBag.put("HOMENUMBER", homenumber);
				outBag.put("POINT_Y", point_y);
				outBag.put("POINT_X", point_x);		   
			   
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
	public static void getMedRep(String empId,String company,JComboBox cmbBox) throws SQLException {
		 Connection conn = null;
		 PreparedStatement preparedStatement = null;
         String selectSQL="";  
         String medrep = "";
       try{
       	conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"));              
                	
        selectSQL = "select * from solgar_prm.prm_sales_medrep_names where status = 1 and reg_id=?";
        preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
        preparedStatement.setInt(1, Integer.valueOf(empId));          
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){    	
        	medrep = rs.getString("sales_name");
          	 cmbBox.addItem(medrep);
          	cmbBox.setSelectedIndex(1);
          	 cmbBox.setEnabled(false);
          	 return;
        }            
        preparedStatement.close();
        
        selectSQL = "select * from solgar_prm.prm_sales_medrep_names where status = 1 and company=?";
        preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
        preparedStatement.setString(1, company);          
        ResultSet rs1 = preparedStatement.executeQuery();
        
	       while (rs1.next()){
	       	 cmbBox.addItem(rs1.getString("sales_name"));
	       }
	       cmbBox.addItem("");
	       cmbBox.setSelectedIndex(-1);
   
	       preparedStatement.close();
	       conn.close();
       }
       catch(SQLException sqle){
           System.out.println("SQL Exception: " + sqle.getMessage());
           if (preparedStatement != null)
        	   preparedStatement.close();
			if (conn != null)
				conn.close();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				conn.rollback();
				throw se;
			} 
		}
	}
}
