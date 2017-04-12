package main;

import jxl.Cell;
import jxl.Sheet;
import cb.esi.esiclient.util.ESIBag;

public class Storages {

	public static ESIBag katrenSalesParser(Sheet sheet,String mainGroup, int verticalLimit, int horizontalLimit) {
		int startOfRow = 0, startOfColumn = 0, productColumn = 0, tableindex = 0,cityRow=0;
		boolean breakFor = false;
		int index = 0;
		String city = "Уфа";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("Продажи по системе") >= 0) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo +3 ;
					productColumn = columNo;
					cityRow = rowNo+2;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn;

		while (index < horizontalLimit-1) {

			for (int i = startOfRow; i < verticalLimit - 1; i++) {
				
				Cell c3 = sheet.getCell(index, cityRow);// pharmacy name				
				
				if (c3.getContents() != null && c3.getContents().length() > 0) {
					city = c3.getContents();
					outBag.put("TABLE",tableindex,"CITY", city);

					Cell c2 = sheet.getCell(productColumn, i); // product name
					if (c2.getContents() != null
							&& c2.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());
					}

					Cell c4 = sheet.getCell(index, i);// count
					if (c4.getContents() != null
							&& c4.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					} else {
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}

					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					
					tableindex = tableindex+1;
				}

			}

			index = index+1;
		}

		return outBag;
	}
	
	public static ESIBag katrenStockParser(Sheet sheet,String mainGroup, int verticalLimit, int horizontalLimit) {
		int startOfRow = 0, startOfColumn = 0, productColumn = 0, tableindex = 0,cityRow=0;
		boolean breakFor = false;
		int index = 0;
		String city = "Уфа";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("Поставщик") >= 0) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo +4 ;
					productColumn = columNo;
					cityRow = rowNo+3;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn;

		while (index < horizontalLimit-1) {

			for (int i = startOfRow; i < verticalLimit - 1; i++) {
				
				Cell c3 = sheet.getCell(index, cityRow);// pharmacy name				
				
				if (c3.getContents() != null && c3.getContents().length() > 0) {
					city = c3.getContents();
					outBag.put("TABLE",tableindex,"CITY", city);

					Cell c2 = sheet.getCell(productColumn, i); // product name
					if (c2.getContents() != null
							&& c2.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());
					}

					Cell c4 = sheet.getCell(index, i);// count
					if (c4.getContents() != null
							&& c4.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					} else {
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}

					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					
					tableindex = tableindex+1;
				}

			}

			index = index+1;
		}

		return outBag;
	}
	
	public static ESIBag protekSalesParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0,
				startOfClient = 0,startOfCorpAdress = 0,startOfRetailAdress = 0,startOfINN = 0,startOfSegment = 0;
		boolean breakFor = false;
		String pharmacy="";
		
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// Товар Склад Расх_Количество Расх_СуммаЗакуп
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("наименование") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("клиент") >= 0) {
					startOfClient = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("юр. адрес") >= 0) {
					startOfCorpAdress = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("факт. адрес") >= 0) {
					startOfRetailAdress = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("ИНН") >= 0) {
					startOfINN = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("отгружено") >= 0) {
					startOfCount = columNo;
				}
				if (c1.getContents().indexOf("регион") >= 0) {
					startOfSklad = columNo;
					
				}				
				if (c1.getContents().indexOf("сегмент") >= 0) {
					startOfSegment = columNo;
					breakFor = true;
					break;
				}
				

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {
			if (i>0){
				Cell c3 = sheet.getCell(startOfSklad, i);
				if(c3.getContents() != null && c3.getContents().length() >0){
					outBag.put("TABLE",tableindex,"CITY", c3.getContents());	
					
					Cell c2 = sheet.getCell(startOfTovar, i);
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());							
		
					Cell c4 = sheet.getCell(startOfCount, i);
					if(c4.getContents() != null && c4.getContents().length() >0){
						String count  = c4.getContents();
						outBag.put("TABLE",tableindex,"COUNT", count);
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
					Cell c5 = sheet.getCell(startOfClient, i);
					if(c5.getContents() != null && c5.getContents().length() >0){
						String client  = c5.getContents();
						outBag.put("TABLE",tableindex,"CLIENT", client);
					}else{
						outBag.put("TABLE",tableindex,"CLIENT", "");
					}
					
					Cell c6 = sheet.getCell(startOfCorpAdress, i);
					if(c6.getContents() != null && c6.getContents().length() >0){
						String legalAddress  = c6.getContents();
						outBag.put("TABLE",tableindex,"LEGALADDRESS", legalAddress);
					}else{
						outBag.put("TABLE",tableindex,"LEGALADDRESS", "");
					}
					
					Cell c7 = sheet.getCell(startOfRetailAdress, i);
					if(c7.getContents() != null && c7.getContents().length() >0){
						String actualAddress  = c7.getContents();
						outBag.put("TABLE",tableindex,"ACTUALADDRESS", actualAddress);
					}else{
						outBag.put("TABLE",tableindex,"ACTUALADDRESS", "");
					}
					
					Cell c8 = sheet.getCell(startOfINN, i);
					if(c8.getContents() != null && c8.getContents().length() >0){
						String INN  = c8.getContents();
						outBag.put("TABLE",tableindex,"INN", INN);
					}else{
						outBag.put("TABLE",tableindex,"INN", "");
					}
					
					Cell c9 = sheet.getCell(startOfSegment, i);
					if(c9.getContents() != null && c9.getContents().length() >0){
						String segment  = c9.getContents();
						outBag.put("TABLE",tableindex,"SEGMENT", segment);
					}else{
						outBag.put("TABLE",tableindex,"SEGMENT", "");
					}
		
					outBag.put("TABLE",tableindex,"AMOUNT","0.00");					
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);			
				
					tableindex++;
			}
		}
		}
		
		return outBag;
	}
	
	public static ESIBag protekStockParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfCount1 = 0,tableindex = 0;
		boolean breakFor = false;
		
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// Товар Склад Расх_Количество Расх_СуммаЗакуп
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("наименование") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("склад") >= 0) {
					startOfSklad = columNo;					
				}
				if (c1.getContents().indexOf("всего") >= 0) {
					startOfCount = columNo;
				}
				if (c1.getContents().indexOf("входящий транзит") >= 0) {
					startOfCount1 = columNo;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {
			if (i>0){
				Cell c3 = sheet.getCell(startOfSklad, i);
				if(c3.getContents() != null && c3.getContents().length() >0){
					outBag.put("TABLE",tableindex,"CITY", findProtekStockCities(c3.getContents()));	
					
					Cell c2 = sheet.getCell(startOfTovar, i);
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());							
		
					Cell c4 = sheet.getCell(startOfCount, i);
					String count = "0";
					if(c4.getContents() != null && c4.getContents().length() >0){
						count  = c4.getContents();
					}
					Cell c5 = sheet.getCell(startOfCount1, i);
					String count1 = "0";
					if(c5.getContents() != null && c5.getContents().length() >0){
						count1  = c5.getContents();
					}
					int totCount = Integer.parseInt(count)+Integer.parseInt(count1);
					
					outBag.put("TABLE",tableindex,"COUNT", String.valueOf(totCount));
					outBag.put("TABLE",tableindex,"AMOUNT","0.00");					
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);			
				
					tableindex++;
			}
		}
		}
		
		return outBag;
	}
	
	private static String findProtekStockCities(String fulladdress) {
		String city = "";
			
			if(fulladdress.indexOf("Казань")>=0){//Протек-10
				city = "Казань";
			}else if(fulladdress.indexOf("Барнаул")>=0){//Протек-11
				city = "Барнаул";
			}else if(fulladdress.indexOf("Самара")>=0){//Протек-12
				city = "Самара";
			}else if(fulladdress.indexOf("Екатеринбург")>=0){//Протек-14
				city = "Екатеринбург";
			}else if(fulladdress.indexOf("Новосибирск")>=0){//Протек-16
				city = "Новосибирск";
			}else if(fulladdress.indexOf("Волгоград")>=0){//Протек-2
				city = "Волгоград";
			}else if(fulladdress.indexOf("Ростов на Дону")>=0){//Протек-20
				city = "Ростов на Дону";
			}else if(fulladdress.indexOf("Омск")>=0){//Протек-22
				city = "Омск";
			}else if(fulladdress.indexOf("Мурманск")>=0){//Протек-23
				city = "Мурманск";
			}else if(fulladdress.indexOf("Хабаровск")>=0){//Протек-24
				city = "Хабаровск";
			}else if(fulladdress.indexOf("Владивосток")>=0){//Протек-25
				city = "Владивосток";
			}else if(fulladdress.indexOf("Иркутск")>=0){//Протек-27
				city = "Иркутск";
			}else if(fulladdress.indexOf("С.- Петербург")>=0){//Протек-3
				city = "С.- Петербург";
			}else if(fulladdress.indexOf("Краснодар")>=0){//Протек-30
				city = "Краснодар";
			}else if(fulladdress.indexOf("Пенза")>=0){//Протек-33
				city = "Пенза";
			}else if(fulladdress.indexOf("Ставрополь")>=0){//Протек-39
				city = "Ставрополь";
			}else if(fulladdress.indexOf("Ярославль")>=0){//Протек-4
				city = "Ярославль";
			}else if(fulladdress.indexOf("Сургут")>=0){//Протек-40
				city = "Сургут";
			}else if(fulladdress.indexOf("Курск")>=0){//Протек-42
				city = "Курск";
			}else if(fulladdress.indexOf("Астрахань")>=0){//Протек-44
				city = "Астрахань";
			}else {
				city = "Москва";
			}	
		
			return city;
	
	}
	
	public static ESIBag pulseSalesParser(Sheet sheet,String mainGroup, int verticalLimit, int horizontalLimit) {
		int startOfRow = 0, startOfColumn = 0, productColumn = 0, tableindex = 0,cityRow=0;
		boolean breakFor = false;
		int index = 0;
		String city = "Уфа";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("Номенклатура") >= 0) {
					startOfColumn = columNo + 3;
					startOfRow = rowNo +1 ;
					productColumn = columNo;
					cityRow = rowNo;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn;

		while (index < horizontalLimit-1) {

			for (int i = startOfRow; i < verticalLimit; i++) {
				
				Cell c3 = sheet.getCell(index, cityRow);// pharmacy name				
				
				if (c3.getContents() != null && c3.getContents().length() > 0) {
					city = c3.getContents();
					outBag.put("TABLE",tableindex,"CITY", city);

					Cell c2 = sheet.getCell(productColumn, i); // product name
					if (c2.getContents() != null
							&& c2.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());
					}

					Cell c4 = sheet.getCell(index, i);// count
					if (c4.getContents() != null
							&& c4.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					} else {
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}

					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					
					tableindex = tableindex+1;
				}

			}

			index = index+1;
		}

		return outBag;
	}
	
	public static ESIBag pulseStockParser(Sheet sheet,String mainGroup, int verticalLimit, int horizontalLimit) {
		int startOfRow = 0, startOfColumn = 0, productColumn = 0, tableindex = 0,cityRow=0;
		boolean breakFor = false;
		int index = 0;
		String city = "Уфа";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("Номенклатура") >= 0) {
					startOfColumn = columNo + 3;
					startOfRow = rowNo +1 ;
					productColumn = columNo;
					cityRow = rowNo;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn;

		while (index < horizontalLimit-1) {

			for (int i = startOfRow; i < verticalLimit; i++) {
				
				Cell c3 = sheet.getCell(index, cityRow);// pharmacy name				
				
				if (c3.getContents() != null && c3.getContents().length() > 0) {
					city = c3.getContents();
					outBag.put("TABLE",tableindex,"CITY", city);

					Cell c2 = sheet.getCell(productColumn, i); // product name
					if (c2.getContents() != null
							&& c2.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());
					}

					Cell c4 = sheet.getCell(index, i);// count
					if (c4.getContents() != null
							&& c4.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					} else {
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}

					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					
					tableindex = tableindex+1;
				}

			}

			index = index+1;
		}

		return outBag;
	}
	
	public static ESIBag ventaSalesParser(Sheet sheet,String mainGroup, int verticalLimit, int horizontalLimit) {
		int startOfRow = 0, startOfColumn = 0, productColumn = 0, tableindex = 0,cityRow=0;
		boolean breakFor = false;
		int index = 0;
		String city = "Уфа";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("Код товара") >= 0) {
					startOfColumn = columNo+2;
					startOfRow = rowNo +1 ;
					productColumn = columNo+1;
					cityRow = rowNo;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn;

		while (index < horizontalLimit-1) {

			for (int i = startOfRow; i < verticalLimit - 1; i++) {
				
				Cell c3 = sheet.getCell(index, cityRow);// pharmacy name				
				
				if (c3.getContents() != null && c3.getContents().length() > 0) {
					city = c3.getContents();
					outBag.put("TABLE",tableindex,"CITY", city);

					Cell c2 = sheet.getCell(productColumn, i); // product name
					if (c2.getContents() != null
							&& c2.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());
					}

					Cell c4 = sheet.getCell(index, i);// count
					if (c4.getContents() != null
							&& c4.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					} else {
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}

					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					
					tableindex = tableindex+1;
				}

			}

			index = index+1;
		}

		return outBag;
	}
	public static ESIBag ventaStockParser(Sheet sheet,String mainGroup, int verticalLimit, int horizontalLimit) {
		int startOfRow = 0, startOfColumn = 0, productColumn = 0, tableindex = 0,cityRow=0;
		boolean breakFor = false;
		int index = 0;
		String city = "Уфа";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("Наименование Препарата") >= 0) {
					startOfColumn = columNo+1;
					startOfRow = rowNo +1 ;
					productColumn = columNo;
					cityRow = rowNo;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn;

		while (index < horizontalLimit-1) {

			for (int i = startOfRow; i < verticalLimit - 1; i++) {
				
				Cell c3 = sheet.getCell(index, cityRow);// pharmacy name				
				
				if (c3.getContents() != null && c3.getContents().length() > 0) {
					city = c3.getContents();
					outBag.put("TABLE",tableindex,"CITY", city);

					Cell c2 = sheet.getCell(productColumn, i); // product name
					if (c2.getContents() != null
							&& c2.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());
					}

					Cell c4 = sheet.getCell(index, i);// count
					if (c4.getContents() != null
							&& c4.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					} else {
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}

					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					
					tableindex = tableindex+1;
				}

			}

			index = index+1;
		}

		return outBag;
	}
	public static ESIBag optimaSalesParser(Sheet sheet,String mainGroup, int verticalLimit, int horizontalLimit) {
		int startOfRow = 0, startOfColumn = 0, productColumn = 0, tableindex = 0,cityRow=0;
		boolean breakFor = false;
		int index = 0;
		String city = "Уфа";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("Товар") >= 0) {
					startOfColumn = columNo+1;
					startOfRow = rowNo +1 ;
					productColumn = columNo;
					cityRow = rowNo;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn;

		while (index < horizontalLimit-1) {

			for (int i = startOfRow; i < verticalLimit - 1; i++) {
				
				Cell c3 = sheet.getCell(index, cityRow);// pharmacy name				
				
				if (c3.getContents() != null && c3.getContents().length() > 0) {
					city = c3.getContents();
					outBag.put("TABLE",tableindex,"CITY", city);

					Cell c2 = sheet.getCell(productColumn, i); // product name
					if (c2.getContents() != null
							&& c2.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());
					}

					Cell c4 = sheet.getCell(index, i);// count
					if (c4.getContents() != null
							&& c4.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					} else {
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}

					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					
					tableindex = tableindex+1;
				}

			}

			index = index+1;
		}

		return outBag;
	}
	
	public static ESIBag optimaStockParser(Sheet sheet,String mainGroup, int verticalLimit, int horizontalLimit) {
		int startOfRow = 0, startOfColumn = 0, productColumn = 0, tableindex = 0,cityRow=0;
		boolean breakFor = false;
		int index = 0;
		String city = "Уфа";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("Товар") >= 0) {
					startOfColumn = columNo+1;
					startOfRow = rowNo +1 ;
					productColumn = columNo;
					cityRow = rowNo;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn;

		while (index < horizontalLimit-1) {

			for (int i = startOfRow; i < verticalLimit - 1; i++) {
				
				Cell c3 = sheet.getCell(index, cityRow);// pharmacy name				
				
				if (c3.getContents() != null && c3.getContents().length() > 0) {
					city = c3.getContents();
					outBag.put("TABLE",tableindex,"CITY", city);

					Cell c2 = sheet.getCell(productColumn, i); // product name
					if (c2.getContents() != null
							&& c2.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());
					}

					Cell c4 = sheet.getCell(index, i);// count
					if (c4.getContents() != null
							&& c4.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					} else {
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}

					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					
					tableindex = tableindex+1;
				}

			}

			index = index+1;
		}

		return outBag;
	}
	
	public static ESIBag badmSalesParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0;
		boolean breakFor = false;
		String pharmacy="";
		
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// Товар Склад Расх_Количество Расх_СуммаЗакуп
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("Товар") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("Область") >= 0) {
					startOfSklad = columNo;					
				}
				if (c1.getContents().indexOf("Количество") == 0) {
					startOfCount = columNo;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {
			if (i>0){
				Cell c3 = sheet.getCell(startOfSklad, i);
				if(c3.getContents() != null && c3.getContents().length() >0){
					outBag.put("TABLE",tableindex,"CITY", c3.getContents());	
					
					Cell c2 = sheet.getCell(startOfTovar, i);
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());							
		
					Cell c4 = sheet.getCell(startOfCount, i);
					if(c4.getContents() != null && c4.getContents().length() >0){
						String count  = c4.getContents();
						outBag.put("TABLE",tableindex,"COUNT", count);
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
		
					outBag.put("TABLE",tableindex,"AMOUNT","0.00");					
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);			
				
					tableindex++;
			}
		}
		}
		
		return outBag;
	}
	
	public static ESIBag badmStockParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0;
		boolean breakFor = false;
		String pharmacy="";
		
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// Товар Склад Расх_Количество Расх_СуммаЗакуп
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("Товар") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("Филиал") >= 0) {
					startOfSklad = columNo;
					breakFor = true;
					break;
				}
				if (c1.getContents().indexOf("Количество доступное") >= 0) {
					startOfCount = columNo;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {
			if (i>0){
				Cell c3 = sheet.getCell(startOfSklad, i);
				if(c3.getContents() != null && c3.getContents().length() >0){
					outBag.put("TABLE",tableindex,"CITY", c3.getContents());	
					
					Cell c2 = sheet.getCell(startOfTovar, i);
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());							
		
					Cell c4 = sheet.getCell(startOfCount, i);
					if(c4.getContents() != null && c4.getContents().length() >0){
						String count  = c4.getContents();
						outBag.put("TABLE",tableindex,"COUNT", count);
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
		
					outBag.put("TABLE",tableindex,"AMOUNT","0.00");					
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);			
				
					tableindex++;
			}
		}
		}
		
		return outBag;
	}
}
