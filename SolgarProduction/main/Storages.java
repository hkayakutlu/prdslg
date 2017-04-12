package main;

import jxl.Cell;
import jxl.Sheet;
import cb.esi.esiclient.util.ESIBag;

public class Storages {

	public static ESIBag katrenSalesParser(Sheet sheet,String mainGroup, int verticalLimit, int horizontalLimit) {
		int startOfRow = 0, startOfColumn = 0, productColumn = 0, tableindex = 0,cityRow=0;
		boolean breakFor = false;
		int index = 0;
		String city = "���";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������� �� �������") >= 0) {
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
		String city = "���";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("���������") >= 0) {
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

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("������") >= 0) {
					startOfClient = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("��. �����") >= 0) {
					startOfCorpAdress = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("����. �����") >= 0) {
					startOfRetailAdress = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("���") >= 0) {
					startOfINN = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("���������") >= 0) {
					startOfCount = columNo;
				}
				if (c1.getContents().indexOf("������") >= 0) {
					startOfSklad = columNo;
					
				}				
				if (c1.getContents().indexOf("�������") >= 0) {
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

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("�����") >= 0) {
					startOfSklad = columNo;					
				}
				if (c1.getContents().indexOf("�����") >= 0) {
					startOfCount = columNo;
				}
				if (c1.getContents().indexOf("�������� �������") >= 0) {
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
			
			if(fulladdress.indexOf("������")>=0){//������-10
				city = "������";
			}else if(fulladdress.indexOf("�������")>=0){//������-11
				city = "�������";
			}else if(fulladdress.indexOf("������")>=0){//������-12
				city = "������";
			}else if(fulladdress.indexOf("������������")>=0){//������-14
				city = "������������";
			}else if(fulladdress.indexOf("�����������")>=0){//������-16
				city = "�����������";
			}else if(fulladdress.indexOf("���������")>=0){//������-2
				city = "���������";
			}else if(fulladdress.indexOf("������ �� ����")>=0){//������-20
				city = "������ �� ����";
			}else if(fulladdress.indexOf("����")>=0){//������-22
				city = "����";
			}else if(fulladdress.indexOf("��������")>=0){//������-23
				city = "��������";
			}else if(fulladdress.indexOf("���������")>=0){//������-24
				city = "���������";
			}else if(fulladdress.indexOf("�����������")>=0){//������-25
				city = "�����������";
			}else if(fulladdress.indexOf("�������")>=0){//������-27
				city = "�������";
			}else if(fulladdress.indexOf("�.- ���������")>=0){//������-3
				city = "�.- ���������";
			}else if(fulladdress.indexOf("���������")>=0){//������-30
				city = "���������";
			}else if(fulladdress.indexOf("�����")>=0){//������-33
				city = "�����";
			}else if(fulladdress.indexOf("����������")>=0){//������-39
				city = "����������";
			}else if(fulladdress.indexOf("���������")>=0){//������-4
				city = "���������";
			}else if(fulladdress.indexOf("������")>=0){//������-40
				city = "������";
			}else if(fulladdress.indexOf("�����")>=0){//������-42
				city = "�����";
			}else if(fulladdress.indexOf("���������")>=0){//������-44
				city = "���������";
			}else {
				city = "������";
			}	
		
			return city;
	
	}
	
	public static ESIBag pulseSalesParser(Sheet sheet,String mainGroup, int verticalLimit, int horizontalLimit) {
		int startOfRow = 0, startOfColumn = 0, productColumn = 0, tableindex = 0,cityRow=0;
		boolean breakFor = false;
		int index = 0;
		String city = "���";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0) {
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
		String city = "���";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0) {
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
		String city = "���";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("��� ������") >= 0) {
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
		String city = "���";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������ ���������") >= 0) {
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
		String city = "���";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("�����") >= 0) {
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
		String city = "���";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("�����") >= 0) {
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

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("�����") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("�������") >= 0) {
					startOfSklad = columNo;					
				}
				if (c1.getContents().indexOf("����������") == 0) {
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

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("�����") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("������") >= 0) {
					startOfSklad = columNo;
					breakFor = true;
					break;
				}
				if (c1.getContents().indexOf("���������� ���������") >= 0) {
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
