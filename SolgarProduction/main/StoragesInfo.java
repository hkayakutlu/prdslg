package main;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Properties;
import java.sql.Date;

import javax.swing.JTable;

import util.Util;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import jxl.write.DateTime;

public class StoragesInfo {

   private int ID;
   private String status;
   private String distributor;
   private String optype;
   private String city;
   private String product;
   private String product_type;
   private int count;
   private String amount;
   private Date rep_begin_date;
   private Date rep_end_date;
   private DateTime uploadDate;
   private String client;
   private String legalAddress;
   private String actualAddress;
   private String INN;
   private String segment;
   
   Properties prop = ConnectToDb.readConfFile();
   private static final DecimalFormat DF = new DecimalFormat();  

   public StoragesInfo() {
	// TODO Auto-generated constructor stub
   }


   public void save(JTable resultTable) throws Exception{

	   	  Connection conn = null;
		  PreparedStatement  stmt = null;
				  
		   try{

		      Class.forName("com.mysql.jdbc.Driver");
		      conn = (Connection) DriverManager.getConnection(prop.getProperty("DB_URL_UPDATE"), prop.getProperty("USER"), prop.getProperty("PASS"));		
		      conn.setAutoCommit(false);
		      
		      try{
		      		    	  
			      int row = resultTable.getRowCount();
					for (int j = 0; j  < row; j++) {
					   stmt = (PreparedStatement) conn.prepareStatement( "INSERT INTO solgar_stk.stock_sales_table(`status`,`distributor`,`optype`,`city`,`product`,"
						   		+ "`product_type`,`count`,`amount`,`rep_begin_date`,`rep_end_date`,`client`,`legalAddress`,`actualAddress`,`INN`,`segment`,`uploadDate`)"
						   		+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);				   
					   
					   setData(resultTable, j);					   
					   stmt.setInt(1,1);//Status
					   setDataToStatement(stmt);					   											   
					   stmt.setTimestamp(16,Timestamp.valueOf(Util.getCurrentDateTime()));//Startd date				   
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
   
	public boolean controlLoadSales(String distributor,String opType,String beginDate,String endDate) throws Exception {

		String url = prop.getProperty("DB_URL");
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
            conn = (Connection) DriverManager.getConnection(url, prop.getProperty("USER"), prop.getProperty("PASS"));                    	
            selectSQL = "select * from solgar_stk.stock_sales_table where status = 1 and distributor = ? and optype = ? and rep_begin_date = ? and rep_end_date = ?";
            preparedStatement = (PreparedStatement)conn.prepareStatement(selectSQL);
            preparedStatement.setString(1, distributor);
            preparedStatement.setString(2, opType);
            preparedStatement.setDate(3, Date.valueOf(beginDate));
            preparedStatement.setDate(4, Date.valueOf(endDate));
            
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){    	
                return true;
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
	
	private void setDataToStatement(PreparedStatement stmt) throws SQLException {
		stmt.setString(2,getDistributor());
		   stmt.setString(3,getOptype());
		   stmt.setString(4,getCity());
		   stmt.setString(5,getProduct());
		   stmt.setString(6,getProductType());
		   stmt.setInt(7,getCount());
		   stmt.setString(8,getAmount());
		   stmt.setDate(9,getBeginDate());
		   stmt.setDate(10,getEndDate());
		   stmt.setString(11,getClient());
		   stmt.setString(12,getLegalAddress());
		   stmt.setString(13,getActualAddress());
		   stmt.setString(14,getINN());
		   stmt.setString(15,getSegment());
	}


	private void setData(JTable resultTable, int j) {
	   if(resultTable.getValueAt(j, 1) != null && resultTable.getValueAt(j, 1).toString().length()>0){
		   setDistributor(resultTable.getValueAt(j, 1).toString());
	   }else{
		   setDistributor("");
	   }
	   if(resultTable.getValueAt(j, 2) != null && resultTable.getValueAt(j, 2).toString().length()>0){
		   setOptype(resultTable.getValueAt(j, 2).toString());
	   }else{
		   setOptype("");
	   }
	   if(resultTable.getValueAt(j, 3) != null && resultTable.getValueAt(j, 3).toString().length()>0){
		   setCity(resultTable.getValueAt(j, 3).toString());
	   }else{
		   setCity("");
	   }
	   if(resultTable.getValueAt(j, 4) != null && resultTable.getValueAt(j, 4).toString().length()>0){
		   setProduct(resultTable.getValueAt(j, 4).toString());
	   }else{
		   setProduct("");
	   }
	   if(resultTable.getValueAt(j, 5) != null && resultTable.getValueAt(j, 5).toString().length()>0){
		   setProductType(resultTable.getValueAt(j, 5).toString());
	   }else{
		   setProductType("");
	   }
	   if(resultTable.getValueAt(j, 6) != null && resultTable.getValueAt(j, 6).toString().length()>0){
		   setCount(Integer.parseInt(resultTable.getValueAt(j, 6).toString().replace(".", "").trim()));
	   }else{
		   setCount(0);
	   }
	   if(resultTable.getValueAt(j, 7) != null && resultTable.getValueAt(j, 7).toString().length()>0){
		   setAmount(resultTable.getValueAt(j, 7).toString());
	   }else{
		   setAmount("");
	   }	   
	   setBeginDate(Date.valueOf(resultTable.getValueAt(j, 8).toString()));
	   setEndDate(Date.valueOf(resultTable.getValueAt(j, 9).toString()));
	  	   
	   
	   if(resultTable.getValueAt(j, 10) != null && resultTable.getValueAt(j, 10).toString().length()>0){
		   setClient(resultTable.getValueAt(j, 10).toString());
	   }else{
		   setClient("");
	   }
	   if(resultTable.getValueAt(j, 11) != null && resultTable.getValueAt(j, 11).toString().length()>0){
		   setLegalAddress(resultTable.getValueAt(j, 11).toString());
	   }else{
		   setLegalAddress("");
	   }
	   if(resultTable.getValueAt(j, 12) != null && resultTable.getValueAt(j, 12).toString().length()>0){
		   setActualAddress(resultTable.getValueAt(j, 12).toString());
	   }else{
		   setActualAddress("");
	   }
	   if(resultTable.getValueAt(j, 13) != null && resultTable.getValueAt(j, 13).toString().length()>0){
		   setINN(resultTable.getValueAt(j, 13).toString());
	   }else{
		   setINN("");
	   }
	   if(resultTable.getValueAt(j, 14) != null && resultTable.getValueAt(j, 14).toString().length()>0){
		   setSegment(resultTable.getValueAt(j, 14).toString());
	   }else{
		   setSegment("");
	   }
	   
	}

   public int getID()  {
       return ID;
   }
   public void setId(int ID)  {
       this.ID = ID;
   }
   
   public String getStatus()  {
       return status;
   }
   public void setApproval_Status(String status)  {
	   this.status = status;
   }
   
   public String getDistributor()  {
       return distributor;
   }
   public void setDistributor(String distributor)  {
	   if(distributor != null & distributor.length() > 0){
		   this.distributor = distributor;
	   }else{
		   this.distributor ="";
	   }
   }
	
   public String getOptype()  {
       return optype;
   }
   public void setOptype(String optype)  {
	   if(optype != null & optype.length() > 0){
		   this.optype = optype;
	   }else{
		   this.optype = "";
	   }
       
   }
   
   public String getCity()  {
       return city;
   }
   public void setCity(String city)  {
       
       if(city != null & city.length() > 0){
    	   this.city = city;
	   }else{
		   this.city = "";
	   }
   }
   
   public String getProduct()  {
       return product;
   }
   public void setProduct(String product)  {
	   if(product != null & product.length() > 0){
		   this.product = product;
	   }else{
		   this.product = "";
	   }
       
   }
   
   public String getProductType()  {
       return product_type;
   }
   public void setProductType(String product_type)  {       
       if(product_type != null & product_type.length() > 0){
    	   this.product_type = product_type;
	   }else{
		   this.product_type = "";
	   }
   }
   
   public int getCount()  {
       return count;
   }
   public void setCount(int count)  {      
       if(count > 0 || count<0){
    	   this.count = count;
	   }else{
		   this.count = 0;
	   }
   }
   
   public String getAmount()  {
       return amount;
   }
   public void setAmount(String amount)  {      
       if(amount != null & amount.length() > 0){
    	   this.amount = amount;
	   }else{
		   this.amount = "";
	   }
   } 
   
   public Date getBeginDate()  {
       return rep_begin_date;
   }
   public void setBeginDate(Date rep_begin_date)  {
       if(rep_begin_date != null){
    	   this.rep_begin_date = rep_begin_date;
	   }
   }
   public Date getEndDate()  {
       return rep_end_date;
   }
   public void setEndDate(Date rep_end_date)  {      
       if(rep_end_date != null){
    	   this.rep_end_date = rep_end_date;
	   }
   }   
   
   public DateTime getUploadDate()  {
       return uploadDate;
   }
   public void setUploadDate(DateTime uploadDate)  {
       this.uploadDate = uploadDate;
   }
   public String getClient()  {
       return client;
   }
   public void setClient(String client)  {      
       if(client != null & client.length() > 0){
    	   this.client = client;
	   }else{
		   this.client = "";
	   }
   }
   public String getLegalAddress()  {
       return legalAddress;
   }
   public void setLegalAddress(String legalAddress)  {      
       if(legalAddress != null & legalAddress.length() > 0){
    	   this.legalAddress = legalAddress;
	   }else{
		   this.legalAddress = "";
	   }
   }
   public String getActualAddress()  {
       return actualAddress;
   }
   public void setActualAddress(String actualAddress)  {      
       if(actualAddress != null & actualAddress.length() > 0){
    	   this.actualAddress = actualAddress;
	   }else{
		   this.actualAddress = "";
	   }
   }
   public String getINN()  {
       return INN;
   }
   public void setINN(String INN)  {      
       if(INN != null & INN.length() > 0){
    	   this.INN = INN;
	   }else{
		   this.INN = "";
	   }
   }
   public String getSegment()  {
       return segment;
   }
   public void setSegment(String segment)  {      
       if(segment != null & segment.length() > 0){
    	   this.segment = segment;
	   }else{
		   this.segment = "";
	   }
   }
}