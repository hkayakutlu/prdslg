package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import cb.esi.esiclient.util.ESIBag;

public class ConnectToDb {

	static final Properties prop = readConfFile();
	
	public static Properties readConfFile() {  	   
		Properties prop = new Properties();		
			try {			
				//prop.load(new FileInputStream("conf/config.properties"));
				prop.load(new FileInputStream("C:\\SolgarInternalSysFile\\config.properties"));
	       } catch (Exception e) {
	           e.printStackTrace();
	       }			
			return prop;
	  }	 
	
	public static ResourceBundle readBundleFile(String country) {  	   
		FileInputStream fis = null;
		ResourceBundle rb = null;
			try {	
				if(country.equalsIgnoreCase("EN")){			
					//fis = new FileInputStream("conf/MyResources.properties");			
					fis = new FileInputStream("C:\\SolgarInternalSysFile\\MyResources.properties");		
				}else if(country.equalsIgnoreCase("RU")){
					//fis = new FileInputStream("conf/MyResources_ru.properties");
					fis = new FileInputStream("C:\\SolgarInternalSysFile\\MyResources_ru.properties");
				}							
				rb = new PropertyResourceBundle(fis);
				
	       } catch (Exception e) {
	           e.printStackTrace();
	       }			
			return rb;
	  }
	
}