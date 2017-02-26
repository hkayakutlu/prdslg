package main;

import java.text.SimpleDateFormat;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import cb.esi.esiclient.util.ESIBag;
import cb.smg.general.utility.CBBag;

public class Dispatcher {
	
	public static void setSaleDataToDB(JTable table) throws Exception {  	   	
		try {			
				
				ChainSales chainSales = new ChainSales();
				chainSales.save(table);
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }

	public static void saveMarktExps(JTable table,String userName) throws Exception {  	   	
		try {			
				
				MarketingExpsInfo exps = new MarketingExpsInfo();
				exps.save(table,userName);
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static void deleteMarktExps(String id,String userName,String status)throws Exception {  	   	
		try {			
				
				MarketingExpsInfo exps = new MarketingExpsInfo();
				exps.statusUpdate(id,userName,status);
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static void statuChangeMarktExps(String id,String userName,String status)throws Exception {  	   	
		try {			
				
				MarketingExpsInfo exps = new MarketingExpsInfo();
				exps.statusUpdate(id,userName,status);
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static void updateMarktExps(JTable table,String userName,int index,int approvalStatus) throws Exception{  	   	
		try {			
				
				MarketingExpsInfo exps = new MarketingExpsInfo();
				exps.update(table,userName,index,approvalStatus);
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static ESIBag getMarktExpsWithParam(JComboBox country,JComboBox area,JComboBox companyName,
			JComboBox expMain,JComboBox expLevel1,JComboBox expLevel2,JComboBox eventDateStart,
			JComboBox eventDateEnd,JComboBox entryDateStart,JComboBox entryDateEnd,JComboBox approveStatus,
			JComboBox organizator,JDateChooser eventDateDayBegin,JDateChooser eventDateDayEnd) {  	   	
		
		ESIBag outBag = new ESIBag();
		String countryStr,areaStr,companyNameStr,expMainStr,expLevel1Str,expLevel2Str,
		eventDateStartStr,eventDateEndStr,entryDateStartStr,entryDateEndStr,organizatorStr;
		String statusStr="";
		SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
		
		try {			
			if(country.getSelectedItem() != null 
					&& country.getSelectedItem().toString().length()>0){
				countryStr = country.getSelectedItem().toString();
			}else{
				countryStr ="";
			}
			if(area.getSelectedItem() != null 
					&& area.getSelectedItem().toString().length()>0){
				areaStr = area.getSelectedItem().toString();
			}else{
				areaStr ="";
			}
			if(companyName.getSelectedItem() != null 
					&& companyName.getSelectedItem().toString().length()>0){
				companyNameStr = companyName.getSelectedItem().toString();
			}else{
				companyNameStr ="";
			}
			if(expMain.getSelectedItem() != null 
					&& expMain.getSelectedItem().toString().length()>0){
				expMainStr = expMain.getSelectedItem().toString();
			}else{
				expMainStr ="";
			}
			if(expLevel1.getSelectedItem() != null 
					&& expLevel1.getSelectedItem().toString().length()>0){
				expLevel1Str = expLevel1.getSelectedItem().toString();
			}else{
				expLevel1Str ="";
			}
			if(expLevel2.getSelectedItem() != null 
					&& expLevel2.getSelectedItem().toString().length()>0){
				expLevel2Str = expLevel2.getSelectedItem().toString();
			}else{
				expLevel2Str ="";
			}
			if(eventDateStart.getSelectedItem() != null 
					&& eventDateStart.getSelectedItem().toString().length()>0){
				eventDateStartStr = eventDateStart.getSelectedItem().toString();
			}else{
				eventDateStartStr ="";
			}
			if(eventDateEnd.getSelectedItem() != null 
					&& eventDateEnd.getSelectedItem().toString().length()>0){
				eventDateEndStr = eventDateEnd.getSelectedItem().toString();
			}else{
				eventDateEndStr ="";
			}
			if(eventDateDayBegin!= null && eventDateDayBegin.getDate() != null 
					&& eventDateDayBegin.getDate().toString().length()>0){
				eventDateStartStr = dcn.format(eventDateDayBegin.getDate());
			}else{
				if(eventDateStartStr.trim().length() ==0){
					eventDateStartStr ="";
				}
			}
			if(eventDateDayEnd!= null && eventDateDayEnd.getDate() != null 
					&& eventDateDayEnd.getDate().toString().length()>0){
				eventDateEndStr = dcn.format(eventDateDayEnd.getDate());
			}else{
				if(eventDateEndStr.trim().length() ==0){
					eventDateEndStr ="";
				}
			}
			if(entryDateStart.getSelectedItem() != null 
					&& entryDateStart.getSelectedItem().toString().length()>0){
				entryDateStartStr = entryDateStart.getSelectedItem().toString();
			}else{
				entryDateStartStr ="";
			}
			if(entryDateEnd.getSelectedItem() != null 
					&& entryDateEnd.getSelectedItem().toString().length()>0){
				entryDateEndStr = entryDateEnd.getSelectedItem().toString();
			}else{
				entryDateEndStr ="";
			}
			if(organizator != null && organizator.getSelectedItem() != null 
					&& organizator.getSelectedItem().toString().length()>0){
				organizatorStr = organizator.getSelectedItem().toString();
			}else{
				organizatorStr ="";
			}
			if(approveStatus.getSelectedItem() != null 
					&& approveStatus.getSelectedItem().toString().length()>0){
				if (approveStatus.getSelectedItem().toString().equalsIgnoreCase("Waiting On Approval")){
					statusStr = "(1)";
				}else if (approveStatus.getSelectedItem().toString().equalsIgnoreCase("Approved")){
					statusStr = "(2)";
				}else if (approveStatus.getSelectedItem().toString().equalsIgnoreCase("Rejected")){
					statusStr = "(3)";
				}				
			}else{
				statusStr ="(1,2,3)";
			}
			
			MarketingExpsInfo exps = new MarketingExpsInfo();
			outBag = exps.getExpsWithParameters(countryStr,areaStr,companyNameStr,expMainStr,expLevel1Str,expLevel2Str,
					eventDateStartStr,eventDateEndStr,entryDateStartStr,entryDateEndStr,statusStr,organizatorStr);
			
       } catch (Exception e) {
           e.printStackTrace();
       }	
		
		return outBag;
  }
	public static void saveStockInfo(JTable table) throws Exception {  	   	
		try {			
				
				StoragesInfo exps = new StoragesInfo();
				exps.save(table);
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static boolean controlLoadStocks(String distributor,String opType,String beginDate,String endDate) throws Exception {  	   	
		try {			
				
				StoragesInfo exps = new StoragesInfo();
				return exps.controlLoadSales(distributor, opType, beginDate, endDate);
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	
	public static boolean saveChainExps(ESIBag inBag) throws Exception {  	   	
		try {			
				
				ChainExpsInfo exps = new ChainExpsInfo();
				exps.save(inBag);
				return true;
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static boolean updateChainExps(ESIBag inBag) throws Exception{  	   	
		try {			
				
				ChainExpsInfo exps = new ChainExpsInfo();
				exps.update(inBag);
				return true;
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static ESIBag getChainExps(ESIBag inBag) throws Exception{  	   	
		try {			
				
			ChainExpsInfo exps = new ChainExpsInfo();
			inBag = exps.getAgreement(inBag);
			return inBag;	
	       
		} catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static ESIBag getChainExpsAll(ESIBag inBag) throws Exception{  	   	
		try {			
				
			ChainExpsInfo exps = new ChainExpsInfo();
			inBag = exps.getAgreementAll(inBag);
			return inBag;	
	       
		} catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static boolean saveEmployee(ESIBag inBag) throws Exception {  	   	
		try {			
				
				Employee emp = new Employee();
				emp.save(inBag);
				return true;
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static void updateEmployee(ESIBag inBag) throws Exception{  	   	
		try {			
				
			Employee emp = new Employee();
			emp.update(inBag);
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static ESIBag getEmployee(ESIBag inBag) throws Exception{  	   	
		try {			
				
			Employee emp = new Employee();
			inBag = emp.get(inBag);
			return inBag;	
	       
		} catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	
	public static boolean saveAssesments(ESIBag inBag) throws Exception {  	   	
		try {			
				
				Assesment ass = new Assesment();
				ass.save(inBag);
				return true;
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static boolean updateAssesments(ESIBag inBag) throws Exception{  	   	
		try {			
				
			Assesment ass = new Assesment();
			ass.update(inBag);
			return true;
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static ESIBag getAssesments(ESIBag inBag) throws Exception{  	   	
		try {			
				
			Assesment ass = new Assesment();
			inBag = ass.get(inBag);
			return inBag;	
	       
		} catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static DefaultTableModel getAssesmentsAllData(ESIBag inBag) throws Exception{  	   	
		try {			
				
			Assesment ass = new Assesment();
			DefaultTableModel dtm = ass.getAllData(inBag);
			return dtm;	
	       
		} catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static ESIBag getAssesmentsToScreen(ESIBag inBag) throws Exception{  	   	
		try {			
				
			Assesment ass = new Assesment();
			inBag = ass.getAssesmentsToScreen(inBag);
			return inBag;	
	       
		} catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static ESIBag savePharmInfo(ESIBag inBag) throws Exception {  	   	
		try {			
				ESIBag outBag = new ESIBag();
				PharmData pharm = new PharmData();
				outBag = pharm.save(inBag);
				return outBag;
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static ESIBag getPharmInfo(ESIBag inBag) throws Exception{  	   	
		try {			
				
			PharmData pharm = new PharmData();
			inBag = pharm.getAll(inBag);
			return inBag;	
	       
		} catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static ESIBag getPharmOfficialAddress(ESIBag inBag) throws Exception{  	   	
		try {			
				
			PharmData pharm = new PharmData();
			inBag = pharm.getOffAddressInfo(inBag);
			return inBag;	
	       
		} catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static ESIBag saveDoctorInfo(ESIBag inBag) throws Exception {  	   	
		try {			
				ESIBag outBag = new ESIBag();
				DoctorData doctor = new DoctorData();
				outBag = doctor.save(inBag);
				return outBag;
				
	       } catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static ESIBag getDoctorInfo(ESIBag inBag) throws Exception{  	   	
		try {			
				
			DoctorData doctor = new DoctorData();
			inBag = doctor.getAll(inBag);
			return inBag;	
	       
		} catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static ESIBag getDoctorOfficialAddress(ESIBag inBag) throws Exception{  	   	
		try {			
				
			DoctorData doctor = new DoctorData();
			inBag = doctor.getOffAddressInfo(inBag);
			return inBag;	
	       
		} catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }			
	  }
	public static DefaultTableModel repGetTotalCounts(JTable resultTable,String reportName,String beginDate,String endDate,String chain,
			String selectedCompany,String selectedCountry,String selectedRegion,String selectedCity,String selectedProduct,String selectedMedRep) throws Exception {
		try {			
			
			ReportQueries rep = new ReportQueries();
			DefaultTableModel dtm = rep.repGetTotalCounts(resultTable, reportName, beginDate, endDate, chain,
					 selectedCompany, selectedCountry, selectedRegion, selectedCity, selectedProduct, selectedMedRep);
			return dtm;	
	       
		} catch (Exception e) {
	           e.printStackTrace();
	           throw e;
	       }		
	}
	
}