package main;

import cb.esi.esiclient.util.ESIBag;
import jxl.Cell;
import jxl.Sheet;

public class Companies {

	
	public static ESIBag aveParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
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
				if (c1.getContents().indexOf("������������") >= 0 ||
						c1.getContents().indexOf("������") >= 0) {
					startOfSklad = columNo;
				}
				if (c1.getContents().indexOf("����������") >= 0
						|| c1.getContents().indexOf("�������") >= 0) {
					startOfCount = columNo;
				}
				if (c1.getContents().indexOf("�����") >= 0) {
					startOfAmount = columNo;
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
					
					Cell c2 = sheet.getCell(startOfTovar, i);
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());

					pharmacy = c3.getContents();
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacy);			
					
					if(pharmacy.indexOf("36,6") >= 0){
						outBag.put("TABLE",tableindex,"SUBGROUP", "36.6");
					}else{
						if(pharmacy.indexOf(",")>0){
							String subGroup = pharmacy.substring(0,pharmacy.indexOf(","));
							outBag.put("TABLE",tableindex,"SUBGROUP", subGroup);
							pharmacy = pharmacy.replace(subGroup, "");
						}else{
							outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
						}
					}
					if(pharmacy.indexOf(". ������") >= 0){
						String aptekNo =  pharmacy.substring(pharmacy.indexOf(". ������")+2,pharmacy.length());
						outBag.put("TABLE",tableindex,"APTEKNO",aptekNo);
						pharmacy = pharmacy.replace(aptekNo, "");
					}else{
						outBag.put("TABLE",tableindex,"APTEKNO", "");
					}
					
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacy);
					
		
					Cell c4 = sheet.getCell(startOfCount, i);
					if(c4.getContents() != null && c4.getContents().length() >0){
						String count  = c4.getContents();
						outBag.put("TABLE",tableindex,"COUNT", count);
					}
		
					Cell c5 = sheet.getCell(startOfAmount, i);
					if(c5.getContents() != null && c5.getContents().length() >0){
						outBag.put("TABLE",tableindex,"AMOUNT", c5.getContents());
					}else{
						outBag.put("TABLE",tableindex,"AMOUNT","0.00");
					}
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);	
					outBag.put("TABLE",tableindex,"CITY", "");			
				
					tableindex++;
			}
		}
		}
		
		return outBag;
	}

	
	public static ESIBag radugaKarisikParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0, startOfColumn = 0, productColumn = 0, pharmacyRow = 0,tableindex = 0;
		boolean breakFor = false;
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0) {
					startOfColumn = columNo + 5;
					startOfRow = rowNo + 3;
					productColumn = columNo;
					pharmacyRow = rowNo;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn;

		while (index < horizontalLimit) {

			for (int i = startOfRow; i < verticalLimit - 1; i++) {

				Cell c3 = sheet.getCell(index, pharmacyRow);// pharmacy name
				if (c3.getContents() != null && c3.getContents().length() > 0) {
					outBag.put("TABLE",tableindex,"PHARMACY", c3.getContents());

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

					Cell c5 = sheet.getCell(index + 1, i);// Amount
					if (c5.getContents() != null
							&& c5.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"AMOUNT", c5.getContents());
					} else {
						outBag.put("TABLE",tableindex,"AMOUNT", "0");
					}
					
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"APTEKNO", "");
					outBag.put("TABLE",tableindex,"CITY", "");
					
					tableindex++;
				}

			}

			index = index + 2;
		}

		return outBag;
		
	}
	
	public static ESIBag ediFarmParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0, startOfColumn = 0, productColumn = 0, pharmacyRow = 0,tableindex = 0;
		boolean breakFor = false;
		int index = 0;
		ESIBag outBag = new ESIBag();
		boolean spb =false;

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("����� � ��������� �������� �� ������������ � ������� ���� �����")>= 0){
					spb =true;
				}
				if(spb){
					if (c1.getContents().indexOf("������������ / ������ ��������") >= 0) {
						startOfColumn = columNo;
						startOfRow = rowNo+1;
						productColumn = columNo;
						//pharmacyRow = rowNo+2;
						pharmacyRow = rowNo-3;
						breakFor = true;
						break;
					}
				}else{
					if (c1.getContents().indexOf("������������") >= 0
							&& c1.getContents().length() <=12) {
						startOfColumn = columNo + 2;
						startOfRow = rowNo + 3;
						productColumn = columNo;
						//pharmacyRow = rowNo+2;
						pharmacyRow = rowNo;
						breakFor = true;
						break;
					}
				}

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn;

		while (index < horizontalLimit-1) {

			for (int i = startOfRow; i < verticalLimit - 1; i++) {

				Cell c3 = sheet.getCell(index, pharmacyRow);// pharmacy name
				String pharmacyName =  c3.getContents();
				if (c3.getContents() != null && c3.getContents().length() > 0) {				
					if(pharmacyName.indexOf("(")>=0){
						outBag.put("TABLE",tableindex,"APTEKNO", pharmacyName.substring(0, pharmacyName.indexOf("(")).trim());
						if(pharmacyName.indexOf(")")>=0){
							outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName.substring(pharmacyName.indexOf("(")+1,pharmacyName.indexOf(")")).trim());
						}else{
							outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName.substring(pharmacyName.indexOf("(")+1,pharmacyName.length()).trim());
						}
						outBag.put("TABLE",tableindex,"CITY", "");
					}else if(pharmacyName.indexOf("��� ")>=0){
						if(pharmacyName.indexOf(",")>=0){
							outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName.substring(pharmacyName.indexOf(",")+1,pharmacyName.length()).trim());
						}else{
							outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
						}
						Cell c5 = sheet.getCell(index, pharmacyRow+1);// aptekno
						if (c5.getContents() != null
								&& c5.getContents().length() > 0) {
							outBag.put("TABLE",tableindex,"APTEKNO", c5.getContents());
						} else {
							outBag.put("TABLE",tableindex,"APTEKNO","");
						}
						outBag.put("TABLE",tableindex,"CITY", "���");
											
					}else{
						outBag.put("TABLE",tableindex,"PHARMACY",pharmacyName);
						outBag.put("TABLE",tableindex,"APTEKNO",pharmacyName);	
						outBag.put("TABLE",tableindex,"CITY", "");
					}
					outBag.put("TABLE",tableindex,"SALESREADER",pharmacyName);
					

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

					/*Cell c5 = sheet.getCell(index + 1, i);// Amount
					if (c5.getContents() != null
							&& c5.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"AMOUNT", c5.getContents());
					} else {*/
						outBag.put("TABLE",tableindex,"AMOUNT", "0");
					//}
					
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					
					
					tableindex++;
				}

			}

			index = index + 1;
		}

		return outBag;
		
	}
	
	
	public static ESIBag aloeParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,tableindex = 0;
		boolean breakFor = false;
		String productName="";
		mainGroup = "EDIFARM";
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0) {
					startOfColumn = columNo;
					startOfRow = rowNo + 2;
					breakFor =true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {

			Cell c2 = sheet.getCell(startOfColumn, i);
			if(c2.getContents() != null && c2.getContents().length() >0){//Product Name
				if(c2.getContents().subSequence(0, 1).equals("0")||
						c2.getContents().subSequence(0, 1).equals("1")
						||c2.getContents().subSequence(0, 1).equals("2")
						||c2.getContents().subSequence(0, 1).equals("3")||
						(c2.getContents().subSequence(0, 5)).toString().toUpperCase().equals("�����")){		
					outBag.put("TABLE",tableindex,"PRODUCT",productName);
					outBag.put("TABLE",tableindex,"APTEKNO", "");
					String pharmacyName = c2.getContents();
					
					if(pharmacyName.indexOf("(")>=0){
						outBag.put("TABLE",tableindex,"APTEKNO", pharmacyName.substring(0, pharmacyName.indexOf("(")).trim());
						if(pharmacyName.indexOf(")")>=0){
							outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName.substring(pharmacyName.indexOf("(")+1,pharmacyName.indexOf(")")).trim());
						}else{
							outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName.substring(pharmacyName.indexOf("(")+1,pharmacyName.length()).trim());
						}
					}else{
						outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
					}
					
					
					
					Cell c4 = sheet.getCell(startOfColumn+2, i);
					if(c4.getContents() != null && c4.getContents().length() >0){ //Count
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());						
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}

					Cell c5 = sheet.getCell(startOfColumn+3, i);
					if(c5.getContents() != null && c5.getContents().length() >0){ //Kalan
						outBag.put("TABLE",tableindex,"REMAINING", c5.getContents());
					}else{
						outBag.put("TABLE",tableindex,"REMAINING", "0");
					}
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);					
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					tableindex++;					
				}else{
					productName = c2.getContents();
				}
				
				
			}

		}
		
		return outBag;
	}
	
	public static ESIBag urazmanovParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,productColumn=0,pharmacyRow=0,tableindex = 0;
		boolean breakFor = false;
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;
					productColumn = columNo;
					pharmacyRow = rowNo;
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
					
			for (int i = startOfRow; i < verticalLimit-1; i++) {
	
				Cell c3 = sheet.getCell(index, pharmacyRow);//pharmacy name
				String pharmacyName = c3.getContents();
				if(pharmacyName != null && pharmacyName.length() >0){	
					
					outBag.put("TABLE",tableindex,"PHARMACY", c3.getContents());
					outBag.put("TABLE",tableindex,"APTEKNO", c3.getContents());
					Cell c2 = sheet.getCell(productColumn, i); //product name
					if(c2.getContents() != null && c2.getContents().length() >0){
						outBag.put("TABLE",tableindex,"PRODUCT",c2.getContents());
					}	
		
					Cell c4 = sheet.getCell(index, i);//count
					if(c4.getContents() != null && c4.getContents().trim().length() >0){
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					
					tableindex++;
		
				}
	
			}
		
			index = index+1;
	}
		
		return outBag;
		
	}
	
	public static ESIBag a5Parser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfFirm = 0,startOfAmount = 0,tableindex = 0;
		boolean breakFor = false;
		verticalLimit = verticalLimit-1;
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
				}
				if (c1.getContents().indexOf("�����") >= 0) {
					startOfFirm = columNo;
				}
				if (c1.getContents().indexOf("�������� ���-�� ���� � ������ ��������") >= 0) {
					startOfAmount = columNo;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {

			Cell c2 = sheet.getCell(startOfTovar, i);
			if(c2.getContents() != null && c2.getContents().length() >0){
				outBag.put("TABLE",tableindex,"PRODUCT",c2.getContents());		

				Cell c3 = sheet.getCell(startOfSklad, i);
				String pharmacyName = c3.getContents();
				if(pharmacyName != null && pharmacyName.length() >0){
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
				}else{
					outBag.put("TABLE",tableindex,"PHARMACY", "");
				}
	
				Cell c4 = sheet.getCell(startOfFirm, i);
				if(c4.getContents() != null && c4.getContents().length() >0){
					outBag.put("TABLE",tableindex,"SUBGROUP", c4.getContents());
				}else{
					outBag.put("TABLE",tableindex,"SUBGROUP", "");
				}
	
				Cell c5 = sheet.getCell(startOfAmount, i);
				if(c5.getContents() != null && c5.getContents().length() >0){
					outBag.put("TABLE",tableindex,"COUNT", c5.getContents());
				}else{
					outBag.put("TABLE",tableindex,"COUNT", "");
				}
				outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
				outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"APTEKNO", "");
				outBag.put("TABLE",tableindex,"CITY", "");
				outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
				tableindex++;
			}

		}
		
		return outBag;
	}
	public static ESIBag riglaParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0,startOfAptekNo=0,startOfCity=0;
		boolean breakFor = false;
		verticalLimit = verticalLimit-1;
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
				if (c1.getContents().indexOf("������� � ��.") >= 0) {
					startOfCount = columNo;
				}
				
				if (c1.getContents().indexOf("�������� ����������") >= 0) {
					startOfAptekNo = columNo;
				}
				
				if (c1.getContents().indexOf("������������ ��������") >= 0) {
					startOfCity = columNo;
				}
				
				
				if (c1.getContents().indexOf("�������, � �����.����� ��� ���") >= 0) {
					startOfAmount = columNo;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i <= verticalLimit; i++) {

			Cell c2 = sheet.getCell(startOfTovar, i);
			if(c2.getContents() != null && c2.getContents().length() >0){
				outBag.put("TABLE",tableindex,"PRODUCT",c2.getContents());			

				Cell c3 = sheet.getCell(startOfSklad, i);
				String pharmacyName = c3.getContents();
				if(pharmacyName != null && pharmacyName.length() >0){
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
				}else{
					outBag.put("TABLE",tableindex,"PHARMACY", "");
				}
	
				Cell c4 = sheet.getCell(startOfCount, i);
				if(c4.getContents() != null && c4.getContents().length() >0){
					outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
				}else{
					outBag.put("TABLE",tableindex,"COUNT", "0");
				}
	
				Cell c6 = sheet.getCell(startOfAptekNo, i);
				if(c6.getContents() != null && c6.getContents().length() >0){
					outBag.put("TABLE",tableindex,"APTEKNO", c6.getContents());
				}else{
					outBag.put("TABLE",tableindex,"APTEKNO", "");
				}
				
				Cell c5 = sheet.getCell(startOfAmount, i);
				if(c5.getContents() != null && c5.getContents().length() >0){
					outBag.put("TABLE",tableindex,"AMOUNT", c5.getContents());
				}else{
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
				}
				
				Cell c7 = sheet.getCell(startOfCity, i);
				if(c7.getContents() != null && c7.getContents().length() >0){
					outBag.put("TABLE",tableindex,"CITY", c7.getContents());
				}else{
					outBag.put("TABLE",tableindex,"CITY", "");
				}
				
				outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
				tableindex++;
			}

		}
		
		return outBag;
		
	}
	
	public static ESIBag universtitetskieParserOld(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,tableindex = 0;
		boolean breakFor = false;
		String pharmacyName="";
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0) {
					startOfColumn = columNo;
					startOfRow = rowNo + 2;
					breakFor =true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {

			Cell c2 = sheet.getCell(startOfColumn, i);
			if(c2.getContents() != null && c2.getContents().length() >0){//Product Name
				if(c2.getContents().subSequence(0, 1).equals("0")){				
					
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
					outBag.put("TABLE",tableindex,"APTEKNO", pharmacyName);
					
					Cell c4 = sheet.getCell(startOfColumn+2, i);
					if(c4.getContents() != null && c4.getContents().length() >0){ //Count
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					}

					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"REMAINING", "");
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					
					tableindex++;
					
				}else{
					pharmacyName = c2.getContents();
				}
			}

		}
		
		return outBag;
	}
	public static ESIBag planetZdoroviya(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0,startOfAptekNo=0,startOfCity=0;
		boolean breakFor = false;
		String productName = "",city ="";
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������ ������") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("������") >= 0 && c1.getContents().length() <=8) {
					startOfSklad = columNo; 
					
				}
				if (c1.getContents().indexOf("������� (���-��)") >= 0) {//if (c1.getContents().indexOf("������� (���-��)") >= 0) {
					startOfCount = columNo;
				}
				if (c1.getContents().indexOf("������") >= 0) {
					startOfCity = columNo;
				}
				
				if (c1.getContents().indexOf("������� (����� ����)") >= 0) {
					startOfAmount = columNo;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {

			Cell c2 = sheet.getCell(startOfSklad, i);
			String pharmacyName = c2.getContents();
			if(pharmacyName != null && pharmacyName.length() >0){
				outBag.put("TABLE",tableindex,"PHARMACY",pharmacyName);			

				Cell c3 = sheet.getCell(startOfTovar, i);
				if(c3.getContents() != null && c3.getContents().length() >0){
					productName = c3.getContents();					
				}
				outBag.put("TABLE",tableindex,"PRODUCT", productName);
	
				Cell c4 = sheet.getCell(startOfCount, i);
				if(c4.getContents() != null && c4.getContents().length() >0){
					outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
				}else{
					outBag.put("TABLE",tableindex,"COUNT", "0");
				}
	
				outBag.put("TABLE",tableindex,"APTEKNO", "");
				
				
				/*Cell c5 = sheet.getCell(startOfAmount, i);
				if(c5.getContents() != null && c5.getContents().length() >0){
					outBag.put("TABLE",tableindex,"AMOUNT", c5.getContents());
				}else{*/
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
				//}
				Cell c6 = sheet.getCell(startOfCity, i);
				if(c6.getContents() != null && c6.getContents().length() >0){
					city = c6.getContents() ;
				}
				
				outBag.put("TABLE",tableindex,"CITY", city);
				
				
				outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
				tableindex++;
			}

		}
		
		return outBag;
		
	}
	
	public static ESIBag planetZdoroviyaNew(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,productColumn=0,pharmacyRow=0,tableindex = 0,aptekaRow=0,aptekaColumn=0;
		boolean breakFor = false;
		
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);				
				if (c1.getContents().indexOf("������������ ������") >= 0 ) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;	
					aptekaColumn = columNo+1;
					productColumn = columNo;
					pharmacyRow = rowNo;
					aptekaRow=rowNo-1;
					breakFor = true;
					break;					
				}
				
				

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn+1;
		
				
		while (index < horizontalLimit) {
					
			for (int i = startOfRow; i < verticalLimit-1; i++) {
	
				Cell c3 = sheet.getCell(index, pharmacyRow);//pharmacy name
				String pharmacyName = c3.getContents();
				if(pharmacyName != null && pharmacyName.length() >0){	
					
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);					
					outBag.put("TABLE",tableindex,"APTEKNO", "");					
					
					Cell c2 = sheet.getCell(productColumn, i); //product name
					if(c2.getContents() != null && c2.getContents().length() >0){
						outBag.put("TABLE",tableindex,"PRODUCT",c2.getContents());
					}	
		
					Cell c4 = sheet.getCell(index, i);//count
					if(c4.getContents() != null && c4.getContents().length() >0){
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");					
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					
					tableindex++;
		
				}
	
			}		
				index = index+1;
			
	}
		
		return outBag;
		
	}
	
	public static ESIBag planetZdoroviyaOLd(Sheet sheet,String mainGroup, int verticalLimit, int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,tableindex=0;
		boolean breakFor = false;
		String pharmacyAddress="",productName="",amount="0.00",aptekNo="",count="";
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������� (���-��)") >= 0) {
					startOfColumn = columNo;
					startOfRow = rowNo + 2;
					breakFor =true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {

			Cell c2 = sheet.getCell(startOfColumn, i);//Pharmacy Address
			Cell c3 = sheet.getCell(startOfColumn, i+2);//Product Name
			if(c2.getContents() != null && c2.getContents().length() >0){//Pharmacy Address
				pharmacyAddress = c2.getContents();
			}
				
			if(c3.getContents() != null && c3.getContents().length() >0){
				Cell c4 = sheet.getCell(startOfColumn, i+1);//Aptek No
				Cell c5 = sheet.getCell(startOfColumn, i+4);//Count
				
				productName = c3.getContents();
				aptekNo = c4.getContents();
				count = c5.getContents();

				
				outBag.put("TABLE",tableindex,"PRODUCT", productName);
				outBag.put("TABLE",tableindex,"PHARMACY", pharmacyAddress);
				outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"COUNT", count);
				outBag.put("TABLE",tableindex,"AMOUNT", amount);
				outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"APTEKNO", aptekNo);
				outBag.put("TABLE",tableindex,"CITY", "");
				outBag.put("TABLE",tableindex,"SALESREADER", pharmacyAddress);
				tableindex++;
				
			}

		}
		
		return outBag;
	}
	public static ESIBag edelvesParser(Sheet sheet,String mainGroup, int verticalLimit, int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,tableindex=0;
		boolean breakFor = false;
		String pharmacyAddress="",productName="",amount="0.00",aptekNo="",count="";
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("�����") >= 0) {
					startOfColumn = columNo;
					startOfRow = rowNo+1;
					breakFor =true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {

			if(i+1<verticalLimit){
				Cell c2 = sheet.getCell(startOfColumn, i);//Product Name
				Cell c3 = sheet.getCell(startOfColumn+1, i);//Pharmacy Address
				if(c3.getContents() != null && c3.getContents().length() >0 &&
						c3.getContents().indexOf("������")>=0){//Pharmacy Address
					pharmacyAddress = c3.getContents();
				}
					
				if(c2.getContents() != null && c2.getContents().length() >0){
					Cell c5 = sheet.getCell(startOfColumn+1, i);//Count
					if(c5.getContents() != null && c5.getContents().trim().length() >0){					
						count = c5.getContents();
					}else{
						count = "0";
					}
					
					productName = c2.getContents();
	
					
					outBag.put("TABLE",tableindex,"PRODUCT", productName);
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyAddress);
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"COUNT", count);
					outBag.put("TABLE",tableindex,"AMOUNT", amount);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"APTEKNO", pharmacyAddress);
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyAddress);
					outBag.put("TABLE",tableindex,"CITY", "");
					tableindex++;
					
				}
			}

		}
		return outBag;
	}
	public static ESIBag farmlendParser(Sheet sheet,String mainGroup, int verticalLimit, int horizontalLimit) {
		int startOfRow = 0, startOfColumn = 0, productColumn = 0, pharmacyRow = 0,tableindex = 0,cityRow=0;
		boolean breakFor = false;
		int index = 0;
		String city = "���";
		String address = "";
		boolean region = false;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("�������") >= 0) {
					startOfColumn = columNo + 2; //dikey
					startOfRow = rowNo + 1; //yatay
					productColumn = columNo;
					pharmacyRow = rowNo;
					cityRow = 0;
					region = true;
					breakFor = true;
					break;
				}
				if (c1.getContents().indexOf("���") >= 0) {
					startOfColumn = columNo + 2;
					startOfRow = rowNo +1 ;
					productColumn = columNo;
					pharmacyRow = rowNo;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn;

		while (index < horizontalLimit) {

			for (int i = startOfRow; i < verticalLimit - 1; i++) {

				if(region){
					Cell c6 = sheet.getCell(index, 0);// pharmacy city bir dikey 2 yatay
					if (c6.getContents() != null && c6.getContents().trim().length() > 0) {
						city = c6.getContents();
					}		
				}
				
				Cell c3 = sheet.getCell(index, pharmacyRow);// pharmacy name				
				
				if (c3.getContents() != null && c3.getContents().length() > 0) {
					address = c3.getContents();
					outBag.put("TABLE",tableindex,"PHARMACY", address);

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
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"APTEKNO",address);
					outBag.put("TABLE",tableindex,"CITY", city);
					outBag.put("TABLE",tableindex,"SALESREADER",address);
					
					tableindex = tableindex+1;
				}

			}

			index = index+1;
		}

		return outBag;
	}
	
	public static ESIBag fialkaParser(Sheet sheet,String mainGroup, int verticalLimit, int horizontalLimit) {
		int startOfRow = 0, startOfColumn = 0, productColumn = 0, pharmacyRow = 0,tableindex = 0;
		boolean breakFor = false;
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("�������� �����") >= 0) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;
					productColumn = columNo;
					pharmacyRow = rowNo-1;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn;

		while (index < horizontalLimit-2) {

			for (int i = startOfRow; i < verticalLimit-1; i++) {

				Cell c3 = sheet.getCell(index, pharmacyRow);// pharmacy name
				if (c3.getContents() != null && c3.getContents().length() > 0) {
					String address = c3.getContents();
					outBag.put("TABLE",tableindex,"PHARMACY", address);

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

					Cell c5 = sheet.getCell(index + 1, i);// Amount
					if (c5.getContents() != null
							&& c5.getContents().length() > 0) {
						outBag.put("TABLE",tableindex,"AMOUNT", c5.getContents());
					} else {
						outBag.put("TABLE",tableindex,"AMOUNT", "0");
					}
					
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"APTEKNO", address);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER",address);
					
					tableindex++;
				}

			}

			index = index + 2;
		}

		return outBag;
		
	}
	
	public static ESIBag kazansky_Apteki_Parser(Sheet sheet,String mainGroup, int verticalLimit, int horizontalLimit) {
		int startOfRow = 0, startOfColumn = 0, productColumn = 0, pharmacyRow = 0,tableindex = 0;
		boolean breakFor = false;
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez
																// bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("��. ����") >= 0 || c1.getContents().indexOf("��������") >= 0) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;
					productColumn = columNo;
					pharmacyRow = rowNo;
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

			for (int i = startOfRow; i < verticalLimit-1; i++) {

				Cell c3 = sheet.getCell(index, pharmacyRow);// pharmacy name
				if (c3.getContents() != null && c3.getContents().length() > 0) {
					String address = c3.getContents();
					String aptekaNo = address.substring(0,address.indexOf("(")).trim();
					String realAddress=address.substring(address.indexOf("(")+1,address.indexOf(")")).trim();
					
					outBag.put("TABLE",tableindex,"PHARMACY", realAddress);

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

					outBag.put("TABLE",tableindex,"AMOUNT", "0");		
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"APTEKNO", aptekaNo);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER",address);
					
					tableindex++;
				}

			}

			index = index + 1;
		}

		return outBag;
		
	}
	
	public static ESIBag melodiya_Zdorovie_krasnodar_Parser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,tableindex = 0;
		boolean breakFor = false;
		String pharmacyName="";
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0) {
					startOfColumn = columNo;
					startOfRow = rowNo + 1;
					breakFor =true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit-4; i++) {

			Cell c2 = sheet.getCell(startOfColumn, i);
			if(c2.getContents() != null && c2.getContents().length() >0){//Product Name
				if(c2.getContents().indexOf("���,")>=0){		
					pharmacyName = c2.getContents();
				}else{		
					String productName = c2.getContents();
					outBag.put("TABLE",tableindex,"PRODUCT",productName);					
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
					String aptekaNo = pharmacyName.substring(pharmacyName.indexOf(",")+1,pharmacyName.length()-1).trim();
					
					Cell c4 = sheet.getCell(startOfColumn+2, i);
					if(c4.getContents() != null && c4.getContents().length() >0){ //Count
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());						
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}

					outBag.put("TABLE",tableindex,"REMAINING", "0");
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"APTEKNO", aptekaNo);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					tableindex++;				
				}
				
				
			}

		}
		
		return outBag;
	}
	public static ESIBag nevisParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,productColumn=0,pharmacyRow=0,tableindex = 0;
		boolean breakFor = false;
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������, �����") >= 0) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;
					productColumn = columNo;
					pharmacyRow = rowNo;
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
					
			for (int i = startOfRow; i < verticalLimit-1; i++) {
	
				Cell c3 = sheet.getCell(index, pharmacyRow);//product Name
				String productName = c3.getContents();
				if(productName != null && productName.length() >0){	
					outBag.put("TABLE",tableindex,"PRODUCT",c3.getContents());
				
					Cell c2 = sheet.getCell(productColumn, i); //product name
					String pharmacyName = c2.getContents();
					if(c2.getContents() != null && c2.getContents().length() >0){
						outBag.put("TABLE",tableindex,"SALESREADER",c2.getContents());
						outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName.substring(pharmacyName.indexOf(",")+1,pharmacyName.length()).trim());
						outBag.put("TABLE",tableindex,"APTEKNO", pharmacyName.substring(0,pharmacyName.indexOf(",")).trim());
					}else{
						outBag.put("TABLE",tableindex,"SALESREADER","");
						outBag.put("TABLE",tableindex,"PHARMACY", "");
						outBag.put("TABLE",tableindex,"APTEKNO", "");
					}	
		
					Cell c4 = sheet.getCell(index, i);//count
					if(c4.getContents() != null && c4.getContents().length() >0){
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					
					tableindex++;
		
				}
	
			}
		
			index = index+1;
	}
		
		return outBag;
		
	}
	public static ESIBag novayaAptekaKaliningradParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,tableindex = 0;
		boolean breakFor = false;
		String productName="";
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0) {
					startOfColumn = columNo;
					startOfRow = rowNo + 3;
					breakFor =true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {

			Cell c2 = sheet.getCell(startOfColumn, i);
			if(c2.getContents() != null && c2.getContents().length() >=6){//Product Name
				if(c2.getContents().subSequence(0, 6).equals("������")){		
					productName = c2.getContents();					
				}else if(c2.getContents().subSequence(0, 6).equals("������")){
					
					outBag.put("TABLE",tableindex,"PRODUCT",productName);
					String pharmacyName = c2.getContents();
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
					
					Cell c4 = sheet.getCell(startOfColumn+1, i);
					String count = c4.getContents();
					if(c4.getContents() != null && c4.getContents().length() >0){ //Count
						if(count.indexOf(".") > 0){
							outBag.put("TABLE",tableindex,"COUNT", count.substring(0,count.indexOf(".")).trim());	
						}else if(count.indexOf(",") > 0){
							outBag.put("TABLE",tableindex,"COUNT", count.substring(0,count.indexOf(",")).trim());
						}else{
							outBag.put("TABLE",tableindex,"COUNT", count.trim());
						}
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}

					outBag.put("TABLE",tableindex,"REMAINING", "0");				
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"APTEKNO", pharmacyName);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					tableindex++;
				}				
				
			}

		}
		
		return outBag;
	}
	public static ESIBag pharmakorParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0,startOfApteka=0;
		boolean breakFor = false;
		String pharmacy="";
		boolean adresVar = false;
		
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������ �� ������������") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
					breakFor = true;
				}
				if (c1.getContents().indexOf("������") >= 0) {
					startOfApteka = columNo;
				}
				if (c1.getContents().indexOf("�����") >= 0) {
					startOfSklad = columNo;
					adresVar = true;
				}			

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {
			if (i>0){
				Cell c2 = sheet.getCell(startOfTovar, i);
				if(c2.getContents() != null && c2.getContents().length() >0){
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());

					if(adresVar){	
						Cell c3 = sheet.getCell(startOfSklad, i);
						if(c3.getContents() != null && c3.getContents().length() >0){
							pharmacy = c3.getContents();
							outBag.put("TABLE",tableindex,"SALESREADER", pharmacy);																
							
							Cell c6 = sheet.getCell(startOfApteka, i);
							if(c6.getContents() != null && c6.getContents().length() >0){
								outBag.put("TABLE",tableindex,"APTEKNO", c6.getContents());
							}else{
								outBag.put("TABLE",tableindex,"APTEKNO", "");
							}													
							outBag.put("TABLE",tableindex,"PHARMACY", pharmacy);
						}
					}else{
						Cell c3 = sheet.getCell(startOfApteka, i);
						if(c3.getContents() != null && c3.getContents().length() >0){
							pharmacy = c3.getContents();
							outBag.put("TABLE",tableindex,"SALESREADER", pharmacy);																
							outBag.put("TABLE",tableindex,"APTEKNO", pharmacy);													
							outBag.put("TABLE",tableindex,"PHARMACY", pharmacy);
						}
					}
		
				Cell c4 = sheet.getCell(startOfTovar+1, i);
				if(c4.getContents() != null && c4.getContents().length() >0){
					String count  = c4.getContents();
					outBag.put("TABLE",tableindex,"COUNT", count);
				}
	
				outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
				outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);	
				outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"CITY", "");
					
				tableindex++;
			}
		}
		}
		
		return outBag;
	}
	public static ESIBag radugaParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,productColumn=0,pharmacyRow=0,tableindex = 0;
		boolean breakFor = false;
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("�������� �����") >= 0) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;
					productColumn = columNo;
					pharmacyRow = rowNo;
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
					
			for (int i = startOfRow; i < verticalLimit-1; i++) {
	
				Cell c3 = sheet.getCell(index, pharmacyRow);//product Name
				String productName = c3.getContents();
				if(productName != null && productName.length() >0){	
					outBag.put("TABLE",tableindex,"PRODUCT",c3.getContents());
				
					Cell c2 = sheet.getCell(productColumn, i); //product name
					String pharmacyName = c2.getContents();
					if(c2.getContents() != null && c2.getContents().length() >0){
						outBag.put("TABLE",tableindex,"SALESREADER",pharmacyName);
						outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
						outBag.put("TABLE",tableindex,"APTEKNO", pharmacyName);
					}else{
						outBag.put("TABLE",tableindex,"SALESREADER","");
						outBag.put("TABLE",tableindex,"PHARMACY", "");
						outBag.put("TABLE",tableindex,"APTEKNO", "");
					}	
		
					Cell c4 = sheet.getCell(index, i);//count
					if(c4.getContents() != null && c4.getContents().length() >0){
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"CITY", "");
					
					tableindex++;
		
				}
	
			}
		
			index = index+1;
	}
		
		return outBag;
		
	}
	public static ESIBag rifarmParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,productColumn=0,pharmacyRow=0,tableindex = 0,aptekaRow=0;
		boolean breakFor = false;
		boolean withOstatok = false;
		
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0 || 
						c1.getContents().indexOf("������� ����� ���������")>=0) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;
					productColumn = columNo;
					pharmacyRow = rowNo-1;
					aptekaRow=rowNo-1;
				}
				if ( c1.getContents().indexOf("������� ����� ���������")>=0) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;
					productColumn = columNo;
					pharmacyRow = rowNo-1;
					aptekaRow=rowNo-2;
				}
				if (c1.getContents().trim().length() <=7 && c1.getContents().trim().indexOf("�����") >= 0) {
					breakFor = true;
					withOstatok = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn;
		
				
		while (index < horizontalLimit-1) {
					
			for (int i = startOfRow; i < verticalLimit-1; i++) {
	
				Cell c3 = sheet.getCell(index, pharmacyRow);//pharmacy name
				String pharmacyName = c3.getContents();
				if(pharmacyName != null && pharmacyName.length() >0){	
					
					outBag.put("TABLE",tableindex,"PHARMACY", c3.getContents());
					
					Cell c6 = sheet.getCell(index, aptekaRow);//pharmacy name
					String aptekNo = c6.getContents();
					if(aptekNo != null && aptekNo.length() >0){
						outBag.put("TABLE",tableindex,"APTEKNO", aptekNo);
					}else{
						outBag.put("TABLE",tableindex,"APTEKNO", "");
					}											
					Cell c2 = sheet.getCell(productColumn, i); //product name
					if(c2.getContents() != null && c2.getContents().length() >0){
						outBag.put("TABLE",tableindex,"PRODUCT",c2.getContents());
					}		
					Cell c4 = sheet.getCell(index, i);//count
					if(c4.getContents() != null && c4.getContents().length() >0){
						if(c4.getContents().equalsIgnoreCase("-* 0,�-")){
							outBag.put("TABLE",tableindex,"COUNT", "0");
						}else{
							outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
						}
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					
					tableindex++;
		
				}
	
			}
			if(withOstatok){
				index = index+2;
			}else{
				index = index+1;
			}
	}
		
		return outBag;
		
	}
	public static ESIBag sakuraParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,productColumn=0,pharmacyRow=0,tableindex = 0,aptekaRow=0;
		boolean breakFor = false;
		
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("�����") >= 0 ) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 2;
					productColumn = columNo;
					pharmacyRow = rowNo;
					aptekaRow=rowNo-1;
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
					
			for (int i = startOfRow; i < verticalLimit-1; i++) {
	
				Cell c3 = sheet.getCell(index, pharmacyRow);//pharmacy name
				String pharmacyName = c3.getContents();
				if(pharmacyName != null && pharmacyName.length() >0){	
					
					outBag.put("TABLE",tableindex,"PHARMACY", c3.getContents());
					outBag.put("TABLE",tableindex,"APTEKNO", "");
											
					
					Cell c2 = sheet.getCell(productColumn, i); //product name
					if(c2.getContents() != null && c2.getContents().length() >0){
						outBag.put("TABLE",tableindex,"PRODUCT",c2.getContents());
					}	
		
					Cell c4 = sheet.getCell(index, i);//count
					if(c4.getContents() != null && c4.getContents().length() >0){
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
					Cell c5 = sheet.getCell(index+1, i);//amount
					if(c5.getContents() != null && c5.getContents().length() >0){
						outBag.put("TABLE",tableindex,"AMOUNT", c5.getContents());
					}else{
						outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					}
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					
					tableindex++;
		
				}
	
			}		
				index = index+1;
			
	}
		
		return outBag;
		
	}
	public static ESIBag samsonPharmaParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,productColumn=0,pharmacyRow=0,tableindex = 0,aptekaRow=0;
		boolean breakFor = false;
		
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("�������� ������") >= 0 ) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;
					productColumn = columNo;
					pharmacyRow = rowNo;
					aptekaRow=rowNo-1;
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
	
				Cell c3 = sheet.getCell(index, pharmacyRow);//pharmacy name
				String pharmacyName = c3.getContents();
				if(pharmacyName != null && pharmacyName.length() >0){	
					
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
					outBag.put("TABLE",tableindex,"APTEKNO", pharmacyName);
											
					
					Cell c2 = sheet.getCell(productColumn, i); //product name
					if(c2.getContents() != null && c2.getContents().length() >0){
						outBag.put("TABLE",tableindex,"PRODUCT",c2.getContents());
					}	
		
					Cell c4 = sheet.getCell(index, i);//count
					if(c4.getContents() != null && c4.getContents().length() >0){
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");					
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					
					tableindex++;
		
				}
	
			}		
				index = index+1;
			
	}
		
		return outBag;
		
	}
	public static ESIBag universitestskyAptekiParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,productColumn=0,pharmacyRow=0,tableindex = 0,aptekaRow=0;
		boolean breakFor = false;
		
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������ ������") >= 0 ) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;
					productColumn = columNo;
					pharmacyRow = rowNo;
					aptekaRow=rowNo-1;
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
					
			for (int i = startOfRow; i < verticalLimit-1; i++) {
	
				Cell c3 = sheet.getCell(index, pharmacyRow);//pharmacy name
				String pharmacyName = c3.getContents();
				if(pharmacyName != null && pharmacyName.length() >0){	
					
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
					outBag.put("TABLE",tableindex,"APTEKNO", pharmacyName);
											
					
					Cell c2 = sheet.getCell(productColumn, i); //product name
					if(c2.getContents() != null && c2.getContents().length() >0){
						outBag.put("TABLE",tableindex,"PRODUCT",c2.getContents());
					}	
		
					Cell c4 = sheet.getCell(index, i);//count
					if(c4.getContents() != null && c4.getContents().length() >0){
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");					
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					
					tableindex++;
		
				}
	
			}		
				index = index+1;
			
	}
		
		return outBag;
		
	}
	public static ESIBag vitaSamaraParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,productColumn=0,pharmacyRow=0,tableindex = 0,aptekaRow=0;
		boolean breakFor = false;
		
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0 ) {
					startOfColumn = columNo+5;
					startOfRow = rowNo + 1;
					productColumn = columNo;
					pharmacyRow = rowNo;
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
					
			for (int i = startOfRow; i < verticalLimit-1; i++) {
	
				Cell c3 = sheet.getCell(index, pharmacyRow);//pharmacy name
				String pharmacyName = c3.getContents();
				Cell c2 = sheet.getCell(productColumn, i); //product name
				String productName = c2.getContents();
			
				if(pharmacyName != null && pharmacyName.length() >0
						&& productName != null && productName.length() >5){	
					
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
					outBag.put("TABLE",tableindex,"APTEKNO", pharmacyName);
					outBag.put("TABLE",tableindex,"PRODUCT",productName);	
		
					Cell c4 = sheet.getCell(index, i);//count
					if(c4.getContents() != null && c4.getContents().length() >0){
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");					
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					
					tableindex++;
		
				}
	
			}		
				index = index+1;
			
	}
		
		return outBag;
		
	}
	public static ESIBag vitaSamaraParserNew(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,productColumn=0,pharmacyRow=0,tableindex = 0,aptekaRow=0,pharmacyColumn=0,productRow=0;
		boolean breakFor = false;
		
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0 ) {//Colum dikey row yatay
					startOfColumn = columNo +1;
					productRow = rowNo;
				}
				if (c1.getContents().indexOf("�����") >= 0 ) {//Colum dikey row yatay
					startOfRow = rowNo + 1;		
					pharmacyRow = rowNo+1;
					pharmacyColumn = columNo;
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
					
			for (int i = startOfRow; i < verticalLimit-1; i++) {
	
				Cell c3 = sheet.getCell(pharmacyColumn, i);//pharmacy name
				String pharmacyName = c3.getContents();
				Cell c2 = sheet.getCell(index, productRow); //product name
				String productName = c2.getContents();
			
				if(pharmacyName != null && pharmacyName.length() >0
						&& productName != null && productName.length() >5){	
					
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
					outBag.put("TABLE",tableindex,"APTEKNO", pharmacyName);
					outBag.put("TABLE",tableindex,"PRODUCT",productName);	
		
					Cell c4 = sheet.getCell(index, i);//count
					if(c4.getContents() != null && c4.getContents().length() >0){
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");					
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					
					tableindex++;
		
				}
	
			}		
				index = index+1;
			
	}
		
		return outBag;
		
	}
	public static ESIBag vitaSamaraParserProductHorizontal(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,productColumn=0,pharmacyRow=0,tableindex = 0,aptekaRow=0,pharmacyColumn=0,productRow=0;
		boolean breakFor = false;
		
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0 ) {//Colum dikey row yatay
					startOfColumn = columNo +1;
					productRow = rowNo;
					startOfRow = rowNo + 5;		
					pharmacyRow = rowNo+1;
					pharmacyColumn = columNo;
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
					
			for (int i = startOfRow; i < verticalLimit-1; i++) {
	
				Cell c3 = sheet.getCell(pharmacyColumn, i);//pharmacy name
				String pharmacyName = c3.getContents();
				Cell c2 = sheet.getCell(index, productRow); //product name
				String productName = c2.getContents();
			
				if(pharmacyName != null && pharmacyName.length() >0
						&& productName != null && productName.length() >5){	
					
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
					outBag.put("TABLE",tableindex,"APTEKNO", pharmacyName);
					outBag.put("TABLE",tableindex,"PRODUCT",productName);	
		
					Cell c4 = sheet.getCell(index, i);//count
					if(c4.getContents() != null && c4.getContents().length() >0){
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");					
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					
					tableindex++;
		
				}
	
			}		
				index = index+1;
			
	}
		
		return outBag;
		
	}
	public static ESIBag vitaTomskParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0;
		boolean breakFor = false;
		String pharmacy="";
		
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("�����/������������") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("�����/��������") >= 0) {
					startOfSklad = columNo;
				}
				if (c1.getContents().indexOf("������� �� �����/���-��(��)") >= 0) {
					startOfCount = columNo;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {
			if (i>0){
				Cell c2 = sheet.getCell(startOfTovar, i);
				if(c2.getContents() != null && c2.getContents().length() >0){
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());

				Cell c3 = sheet.getCell(startOfSklad, i);
				if(c3.getContents() != null && c3.getContents().length() >0){
					pharmacy = c3.getContents();
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacy);								
					outBag.put("TABLE",tableindex,"APTEKNO", "");								
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacy);
				}
	
				Cell c4 = sheet.getCell(startOfCount, i);
				if(c4.getContents() != null && c4.getContents().length() >0){
					String count  = c4.getContents();
					outBag.put("TABLE",tableindex,"COUNT", count);
				}

				outBag.put("TABLE",tableindex,"AMOUNT", "0.00");				
				outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);	
				outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"CITY", "�����");
				
				tableindex++;
			}
		}
		}
		
		return outBag;
	}
	public static ESIBag zdorovRuParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0;
		boolean breakFor = false;
		String pharmacy="";
		
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("��������") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("�����") >= 0) {
					startOfSklad = columNo;
				}
				if (c1.getContents().indexOf("���-��") >= 0) {
					startOfCount = columNo;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {
			if (i>0){
				Cell c2 = sheet.getCell(startOfTovar, i);
				if(c2.getContents() != null && c2.getContents().length() >0){
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());

				Cell c3 = sheet.getCell(startOfSklad, i);
				if(c3.getContents() != null && c3.getContents().length() >0){
					pharmacy = c3.getContents();
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacy);								
					outBag.put("TABLE",tableindex,"APTEKNO", pharmacy);								
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacy);
				}
	
				Cell c4 = sheet.getCell(startOfCount, i);
				if(c4.getContents() != null && c4.getContents().length() >0){
					String count  = c4.getContents();
					outBag.put("TABLE",tableindex,"COUNT", count);
				}

				outBag.put("TABLE",tableindex,"AMOUNT", "0.00");				
				outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);	
				outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"CITY", "");
				
				tableindex++;
			}
		}
		}
		
		return outBag;
	}
	public static ESIBag zhivikaParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,productColumn=0,pharmacyRow=0,tableindex = 0,aptekaRow=0;
		boolean breakFor = false;
		
		int index = 0;
		ESIBag outBag = new ESIBag();
		boolean productVertical = false;

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().trim().indexOf("�����") >= 0 ) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;
					productColumn = columNo;
					pharmacyRow = rowNo;
					aptekaRow=rowNo-1;
					breakFor = true;
					break;
				}else if(c1.getContents().trim().indexOf("�������� �����") >= 0 ) {
					productVertical = false;
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;
					productColumn = columNo;
					pharmacyRow = rowNo;
					aptekaRow=rowNo-1;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn;
		
		if(productVertical)	{
			while (index < horizontalLimit-1) {
				
				for (int i = startOfRow; i < verticalLimit-1; i++) {
		
					Cell c3 = sheet.getCell(index, pharmacyRow);//pharmacy name
					String product = c3.getContents();
					if(product != null && product.length() >0){	
						
						outBag.put("TABLE",tableindex,"PRODUCT",product);						
						outBag.put("TABLE",tableindex,"APTEKNO", "");
												
						
						Cell c2 = sheet.getCell(productColumn, i); //address
						String pharmacyName = c2.getContents().trim();
						if(pharmacyName != null && pharmacyName.length() >0){
							outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
						}	
			
						Cell c4 = sheet.getCell(index, i);//count
						if(c4.getContents() != null && c4.getContents().length() >0){
							outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
						}else{
							outBag.put("TABLE",tableindex,"COUNT", "0");
						}
						
						outBag.put("TABLE",tableindex,"AMOUNT", "0.00");					
						outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
						outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
						outBag.put("TABLE",tableindex,"CITY", "");
						outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
						
						tableindex++;
			
					}
		
				}		
					index = index+1;
			
			}
		}else{
			while (index < horizontalLimit-1) {
						
				for (int i = startOfRow; i < verticalLimit-1; i++) {
		
					Cell c3 = sheet.getCell(index, pharmacyRow);//pharmacy name
					String pharmacyName = c3.getContents();
					if(pharmacyName != null && pharmacyName.length() >0){	
						
						outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
						outBag.put("TABLE",tableindex,"APTEKNO", "");
												
						
						Cell c2 = sheet.getCell(productColumn, i); //product name
						if(c2.getContents() != null && c2.getContents().length() >0){
							outBag.put("TABLE",tableindex,"PRODUCT",c2.getContents());
						}	
			
						Cell c4 = sheet.getCell(index, i);//count
						if(c4.getContents() != null && c4.getContents().length() >0){
							outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
						}else{
							outBag.put("TABLE",tableindex,"COUNT", "0");
						}
						
						outBag.put("TABLE",tableindex,"AMOUNT", "0.00");					
						outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
						outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
						outBag.put("TABLE",tableindex,"CITY", "");
						outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
						
						tableindex++;
			
					}
		
				}		
					index = index+1;
			
			}
		
	}
		
		return outBag;
		
	}
	public static ESIBag melodiyaZdorovyNovosibirskParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,tableindex = 0;
		boolean breakFor = false;
		String productName="",pharmacyName="";
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0) {
					startOfColumn = columNo;
					startOfRow = rowNo + 1;
					breakFor =true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {

			Cell c2 = sheet.getCell(startOfColumn, i);
			if(c2.getContents() != null && c2.getContents().length() >0){//Product Name
				if(c2.getContents().indexOf("������")>=0){		
					pharmacyName = c2.getContents();	
				}else{					
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
					productName = c2.getContents();
					outBag.put("TABLE",tableindex,"PRODUCT",productName);
										
					Cell c4 = sheet.getCell(startOfColumn+1, i);
					if(c4.getContents() != null && c4.getContents().length() >0){ //Count
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());						
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}

					outBag.put("TABLE",tableindex,"REMAINING", "0");				
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"APTEKNO", pharmacyName);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					tableindex++;			
				}
				
				
			}

		}
		
		return outBag;
	}
	public static ESIBag pervayaPomoshBarnaulParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0;
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
				if (c1.getContents().indexOf("�������� �����") >= 0) {
					startOfSklad = columNo;
				}
				if (c1.getContents().indexOf("����������") >= 0) {
					startOfCount = columNo;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {
			if (i>0){
				Cell c2 = sheet.getCell(startOfTovar, i);
				if(c2.getContents() != null && c2.getContents().length() >0){
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());

				Cell c3 = sheet.getCell(startOfSklad, i);
				if(c3.getContents() != null && c3.getContents().length() >0){
					pharmacy = c3.getContents();
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacy);								
					outBag.put("TABLE",tableindex,"APTEKNO", pharmacy);								
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacy);
				}
	
				Cell c4 = sheet.getCell(startOfCount, i);
				if(c4.getContents() != null && c4.getContents().length() >0){
					String count  = c4.getContents();
					outBag.put("TABLE",tableindex,"COUNT", count);
				}

				outBag.put("TABLE",tableindex,"AMOUNT", "0.00");				
				outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);	
				outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"CITY", "");
				
				tableindex++;
			}
		}
		}
		
		return outBag;
	}
	public static ESIBag doctorStoletovParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,productColumn=0,pharmacyRow=0,tableindex = 0,aptekaRow=0,aptekaColumn=0;
		boolean breakFor = false;
		
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������ ��") >= 0 ) {			
					startOfColumn = columNo + 6;
					startOfRow = rowNo + 1;	
					productColumn = columNo;
					aptekaColumn = columNo+1;
					pharmacyRow = rowNo;
					aptekaRow=rowNo-1;
				}
				if (c1.getContents().indexOf("������������") >= 0 ) {
					startOfColumn = columNo + 4;
					startOfRow = rowNo + 1;	
					aptekaColumn = columNo-1;
					productColumn = columNo-2;
					pharmacyRow = rowNo;
					aptekaRow=rowNo-1;				
				}
				
				if (c1.getContents().indexOf("MED_DESCR") >= 0 ) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;	
					aptekaColumn = columNo-1;
					productColumn = columNo;
					pharmacyRow = rowNo;
					aptekaRow=rowNo-1;				
				}
				
				if (c1.getContents().indexOf("AP_DESCR") >= 0 ) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;	
					aptekaColumn = columNo-1;
					productColumn = columNo;
					pharmacyRow = rowNo;
					aptekaRow=rowNo-1;				
				}
				
				

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn;
		
				
		while (index < horizontalLimit) {
					
			for (int i = startOfRow; i < verticalLimit; i++) {
	
				Cell c3 = sheet.getCell(index, pharmacyRow);//pharmacy name
				String pharmacyName = c3.getContents();
				if(pharmacyName != null && pharmacyName.length() >0){	
					
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
					if(pharmacyName.indexOf("��� �")>=0 && pharmacyName.trim().length()>10){
						outBag.put("TABLE",tableindex,"APTEKNO", pharmacyName.substring(0, 9));	
						outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName.substring(10, pharmacyName.length()));
					}else{
						outBag.put("TABLE",tableindex,"APTEKNO", "");
					}
					
											
					
					Cell c2 = sheet.getCell(productColumn, i); //product name
					if(c2.getContents() != null && c2.getContents().length() >0){
						outBag.put("TABLE",tableindex,"PRODUCT",c2.getContents());
					}	
		
					Cell c4 = sheet.getCell(index, i);//count
					if(c4.getContents() != null && c4.getContents().length() >0){
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");					
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					
					tableindex++;
		
				}
	
			}		
				index = index+1;
			
	}
		
		return outBag;
		
	}
	public static ESIBag mosAptekaParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0;
		boolean breakFor = false;
		String pharmacy="";
		
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("�������� ������") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("������") >= 0) {
					startOfSklad = columNo;
				}
				if (c1.getContents().indexOf("����� ������") >= 0) {
					startOfCount = columNo;
				}
				if (c1.getContents().indexOf("����� ������") >= 0) {
					startOfAmount = columNo;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {
			
			Cell c2 = sheet.getCell(startOfTovar, i);
			if(c2.getContents() != null && c2.getContents().length() >0 
					&& c2.getContents().indexOf("�����")<0){
				outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());	
			
				Cell c3 = sheet.getCell(startOfSklad, i);
				if(c3.getContents() != null && c3.getContents().length() >0){
					pharmacy = c3.getContents();					
				}
				outBag.put("TABLE",tableindex,"SALESREADER", pharmacy);								
				outBag.put("TABLE",tableindex,"APTEKNO", pharmacy);								
				outBag.put("TABLE",tableindex,"PHARMACY", pharmacy);

				Cell c4 = sheet.getCell(startOfCount, i);
				if(c4.getContents() != null && c4.getContents().length() >0){
					String count  = c4.getContents();
					outBag.put("TABLE",tableindex,"COUNT", count);
				}
				Cell c5 = sheet.getCell(startOfAmount, i);
				if(c5.getContents() != null && c5.getContents().length() >0){
					outBag.put("TABLE",tableindex,"AMOUNT", c5.getContents());
				}else{
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
				}
			
				outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);	
				outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"CITY", "");
				
				tableindex++;
			}
		}
		
		return outBag;
	}
	public static ESIBag panazeyaParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0;
		boolean breakFor = false;
		String pharmacy="";
		
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 3;
				}
				if (c1.getContents().indexOf("������ �� ������") >= 0) {
					startOfCount = columNo;
					startOfAmount = columNo+1;
				}
				

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {
			
			Cell c2 = sheet.getCell(startOfTovar, i);
			if(c2.getContents() != null && c2.getContents().length() >0){
				outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());	
				outBag.put("TABLE",tableindex,"SALESREADER", mainGroup);								
				outBag.put("TABLE",tableindex,"APTEKNO", mainGroup);								
				outBag.put("TABLE",tableindex,"PHARMACY", mainGroup);

				Cell c4 = sheet.getCell(startOfCount, i);
				if(c4.getContents() != null && c4.getContents().length() >0){
					String count  = c4.getContents();
					outBag.put("TABLE",tableindex,"COUNT", count);
				}
				Cell c5 = sheet.getCell(startOfAmount, i);
				if(c5.getContents() != null && c5.getContents().length() >0){
					outBag.put("TABLE",tableindex,"AMOUNT", c5.getContents());
				}else{
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
				}
			
				outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);	
				outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"CITY", "");
				
				tableindex++;
			}
		}
		
		return outBag;
	}
	public static ESIBag pervayaAptekaArhangelskParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,tableindex = 0;
		boolean breakFor = false;
		String productName="";
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("�������������") >= 0) {
					startOfColumn = columNo;
					startOfRow = rowNo + 1;
					breakFor =true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {

			Cell c2 = sheet.getCell(startOfColumn, i);
			if(c2.getContents() != null && c2.getContents().length() >0){//Product Name
				if(c2.getContents().trim().subSequence(0, 1).equals(".")){	//Address	
					outBag.put("TABLE",tableindex,"PRODUCT",productName);
					String pharmacyName = c2.getContents();
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
					
					Cell c4 = sheet.getCell(startOfColumn+1, i);
					if(c4.getContents() != null && c4.getContents().length() >0){ //Count
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());						
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}

					outBag.put("TABLE",tableindex,"REMAINING", "0");			
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"APTEKNO", "");
					outBag.put("TABLE",tableindex,"CITY", "�����������");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					tableindex++;					
				}else{
					productName = c2.getContents();
				}
				
				
			}

		}
		
		return outBag;
	}
	
	public static ESIBag sosialnayaAptekaParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0,startOfAptekNo=0,startOfCity=0;
		boolean breakFor = false;
		String productName = "",city ="",pharmacyName="";
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������� ���.") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("����� � ����") >= 0) {
					startOfSklad = columNo; 
					
				}
				if (c1.getContents().indexOf("������� ��.") >= 0) {//if (c1.getContents().indexOf("������� (���-��)") >= 0) {
					startOfCount = columNo;
				}
				if (c1.getContents().indexOf("�����") >= 0) {
					startOfCity = columNo;
				}
				

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {

			Cell c3 = sheet.getCell(startOfTovar, i);
			productName = c3.getContents();	
			if(productName != null && productName.length() >0){
				
				Cell c2 = sheet.getCell(startOfSklad, i);
				if(c2.getContents() != null && c2.getContents().length() >0){
					pharmacyName = c2.getContents();
				}
				
				outBag.put("TABLE",tableindex,"PHARMACY",pharmacyName);			
				outBag.put("TABLE",tableindex,"PRODUCT", productName);
	
				Cell c4 = sheet.getCell(startOfCount, i);
				if(c4.getContents() != null && c4.getContents().length() >0){
					outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
				}else{
					outBag.put("TABLE",tableindex,"COUNT", "0");
				}
	
				outBag.put("TABLE",tableindex,"APTEKNO", "");
				outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
				
				Cell c6 = sheet.getCell(startOfCity, i);
				if(c6.getContents() != null && c6.getContents().length() >0){
					city = c6.getContents() ;
				}				
				outBag.put("TABLE",tableindex,"CITY", city);
				
				
				outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
				tableindex++;
			}

		}
		
		return outBag;
		
	}
	public static ESIBag vestaParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0,startOfAptekNo=0,startOfCity=0;
		boolean breakFor = false;
		String productName = "",city ="",pharmacyName="";
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������� ���.") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("����� � ����") >= 0) {
					startOfSklad = columNo; 
					
				}
				if (c1.getContents().indexOf("������� ��.") >= 0) {//if (c1.getContents().indexOf("������� (���-��)") >= 0) {
					startOfCount = columNo;
				}
				if (c1.getContents().indexOf("�����") >= 0) {
					startOfCity = columNo;
				}
				

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {

			Cell c3 = sheet.getCell(startOfTovar, i);
			productName = c3.getContents();	
			if(productName != null && productName.length() >0){
				
				Cell c2 = sheet.getCell(startOfSklad, i);
				if(c2.getContents() != null && c2.getContents().length() >0){
					pharmacyName = c2.getContents();
				}
				
				outBag.put("TABLE",tableindex,"PHARMACY",pharmacyName);			
				outBag.put("TABLE",tableindex,"PRODUCT", productName);
	
				Cell c4 = sheet.getCell(startOfCount, i);
				if(c4.getContents() != null && c4.getContents().length() >0){
					outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
				}else{
					outBag.put("TABLE",tableindex,"COUNT", "0");
				}
	
				outBag.put("TABLE",tableindex,"APTEKNO", "");
				outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
				
				Cell c6 = sheet.getCell(startOfCity, i);
				if(c6.getContents() != null && c6.getContents().length() >0){
					city = c6.getContents() ;
				}				
				outBag.put("TABLE",tableindex,"CITY", city);
				
				
				outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
				outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
				tableindex++;
			}

		}
		
		return outBag;
		
	}
	public static ESIBag aptekiKubaniParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,tableindex = 0;
		boolean breakFor = false;
		String productName="",pharmacyName="";
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0) {
					startOfColumn = columNo;
					startOfRow = rowNo + 1;
					breakFor =true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {

			Cell c2 = sheet.getCell(startOfColumn, i);
			if(c2.getContents() != null && c2.getContents().length() >0){//Product Name
				if(c2.getContents().trim().indexOf("������")>=0){	//Product	
					productName = c2.getContents();
					outBag.put("TABLE",tableindex,"PRODUCT",productName);
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
					
					Cell c4 = sheet.getCell(startOfColumn+1, i);
					if(c4.getContents() != null && c4.getContents().length() >0){ //Count
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());						
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}

					outBag.put("TABLE",tableindex,"REMAINING", "0");			
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"APTEKNO", pharmacyName);
					outBag.put("TABLE",tableindex,"CITY", "�����������");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					tableindex++;					
				}else{
					pharmacyName = c2.getContents();
				}
				
				
			}

		}
		
		return outBag;
	}
	public static ESIBag apteka245Parser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,productColumn=0,pharmacyRow=0,tableindex = 0,aptekaRow=0;
		boolean breakFor = false;
		
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().trim().indexOf("��������") >= 0 ) {
					startOfColumn = columNo + 6;
					startOfRow = rowNo + 1;
					productColumn = columNo;
					pharmacyRow = rowNo;
					aptekaRow=rowNo-1;
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
					
			for (int i = startOfRow; i < verticalLimit-1; i++) {
	
				Cell c3 = sheet.getCell(index, pharmacyRow);//pharmacy name
				String pharmacyName = c3.getContents();
				if(pharmacyName != null && pharmacyName.length() >0){	
					
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
					outBag.put("TABLE",tableindex,"APTEKNO", pharmacyName);
											
					
					Cell c2 = sheet.getCell(productColumn, i); //product name
					if(c2.getContents() != null && c2.getContents().length() >0){
						outBag.put("TABLE",tableindex,"PRODUCT",c2.getContents());
					}	
		
					Cell c4 = sheet.getCell(index, i);//count
					if(c4.getContents() != null && c4.getContents().length() >0){
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");					
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					
					tableindex++;
		
				}
	
			}		
				index = index+1;
			
	}
		
		return outBag;
		
	}
	
	public static ESIBag piluliParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
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
				if (c1.getContents().indexOf("�����") >= 0) {
					startOfSklad = columNo;
				}
				if (c1.getContents().indexOf("���-��") >= 0) {
					startOfCount = columNo;
				}
				if (c1.getContents().indexOf("�������������") >= 0) {
					startOfAmount = columNo;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit-1; i++) {
			if (i>0){
				Cell c2 = sheet.getCell(startOfTovar, i);
				if(c2.getContents() != null && c2.getContents().length() >0){
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());

					Cell c3 = sheet.getCell(startOfSklad, i);
					if(c3.getContents() != null && c3.getContents().length() >0){
						pharmacy = c3.getContents();
						outBag.put("TABLE",tableindex,"SALESREADER", pharmacy);			
					}else{
						outBag.put("TABLE",tableindex,"SALESREADER", "");
					}			
					
					Cell c4 = sheet.getCell(startOfCount, i);
					if(c4.getContents() != null && c4.getContents().length() >0){
						String count  = c4.getContents();
						outBag.put("TABLE",tableindex,"COUNT", count);
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
		
					Cell c5 = sheet.getCell(startOfAmount, i);
					if(c5.getContents() != null && c5.getContents().length() >0){
						outBag.put("TABLE",tableindex,"AMOUNT", c5.getContents());
					}else{
						outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					}
				
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);	
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"PHARMACY", "");
					outBag.put("TABLE",tableindex,"APTEKNO", "");
					outBag.put("TABLE",tableindex,"CITY", "");
					
					tableindex++;
			 }
		 }
		}
		
		return outBag;
	}
	
	public static ESIBag othersParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,
				startOfCount = 0,startOfAmount = 0,tableindex = 0,startOfSubGroup=0,startOfPharmacyNo=0,startOfCity=0;
		boolean breakFor = false;
		String pharmacy="";
		
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("Product") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("SubGroup") >= 0) {
					startOfSubGroup = columNo;
				}
				if (c1.getContents().indexOf("PharmacyAddress") >= 0) {
					startOfSklad = columNo;
				}
				if (c1.getContents().indexOf("Count") >= 0) {
					startOfCount = columNo;
				}
				if (c1.getContents().indexOf("Amount") >= 0) {
					startOfAmount = columNo;
				}
				if (c1.getContents().indexOf("AptekaNo") >= 0) {
					startOfPharmacyNo = columNo;
				}
				if (c1.getContents().indexOf("City") >= 0) {
					startOfCity = columNo;
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
				Cell c2 = sheet.getCell(startOfTovar, i);
				if(c2.getContents() != null && c2.getContents().length() >0){
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());

					Cell c3 = sheet.getCell(startOfSklad, i);
					if(c3.getContents() != null && c3.getContents().length() >0){
						pharmacy = c3.getContents();
						outBag.put("TABLE",tableindex,"SALESREADER", pharmacy);			
					}else{
						outBag.put("TABLE",tableindex,"SALESREADER", "");
					}
					
					Cell c6 = sheet.getCell(startOfSubGroup, i);
					if(c6.getContents() != null && c6.getContents().length() >0){
						outBag.put("TABLE",tableindex,"SUBGROUP", c6.getContents());			
					}else{
						outBag.put("TABLE",tableindex,"SUBGROUP", "");
					}
					
					Cell c7 = sheet.getCell(startOfPharmacyNo, i);
					if(c7.getContents() != null && c7.getContents().length() >0){
						outBag.put("TABLE",tableindex,"APTEKNO", c7.getContents());			
					}else{
						outBag.put("TABLE",tableindex,"APTEKNO", "");
					}		
					
					Cell c4 = sheet.getCell(startOfCount, i);
					if(c4.getContents() != null && c4.getContents().length() >0){
						String count  = c4.getContents();
						outBag.put("TABLE",tableindex,"COUNT", count);
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
		
					Cell c5 = sheet.getCell(startOfAmount, i);
					if(c5.getContents() != null && c5.getContents().length() >0){
						outBag.put("TABLE",tableindex,"AMOUNT", c5.getContents());
					}else{
						outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					}
					Cell c8 = sheet.getCell(startOfCity, i);
					if(c8.getContents() != null && c8.getContents().length() >0){
						outBag.put("TABLE",tableindex,"CITY", c8.getContents());
					}else{
						outBag.put("TABLE",tableindex,"CITY", "");
					}
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);	
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacy);
					
					tableindex++;
			 }
		 }
		}
		
		return outBag;
	}
	
	public static ESIBag sheksnaParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,tableindex = 0;
		boolean breakFor = false;
		String pharmacyName="";
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0) {
					startOfColumn = columNo;
					startOfRow = rowNo + 2;
					breakFor =true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {

			Cell c2 = sheet.getCell(startOfColumn, i);
			if(c2.getContents() != null && c2.getContents().length() >0){//Product Name
				if(c2.getContents().subSequence(0, 1).equals("0")){				
					
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
					outBag.put("TABLE",tableindex,"APTEKNO", pharmacyName);
					
					Cell c4 = sheet.getCell(startOfColumn+2, i);
					if(c4.getContents() != null && c4.getContents().length() >0){ //Count
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
					}

					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"REMAINING", "");
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
					
					tableindex++;
					
				}else{
					pharmacyName = c2.getContents();
				}
			}

		}
		
		return outBag;
	}
	
	public static ESIBag klassikaParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,tableindex = 0;
		boolean breakFor = false;
		String productName="";
		String city = "";
		String address = "";
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0) {
					startOfColumn = columNo;//dikey
					startOfRow = rowNo + 2;//yatay
					breakFor =true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit-1; i++) {

			Cell c2 = sheet.getCell(startOfColumn, i);
			if(c2.getContents() != null && c2.getContents().length() >0){//City adres and product 
				if(c2.getContents().subSequence(0, 1).equals("=")){
					city = c2.getContents().trim().substring(1, c2.getContents().trim().length());
				}else if(c2.getContents().trim().subSequence(0, 1).toString().matches("[0-9]"))	{
					address = city +","+ c2.getContents().trim().substring(c2.getContents().trim().lastIndexOf(" "), c2.getContents().trim().length());
				}else{
					productName = c2.getContents().trim();
					outBag.put("TABLE",tableindex,"PRODUCT",productName);
					outBag.put("TABLE",tableindex,"APTEKNO", "");				
					outBag.put("TABLE",tableindex,"PHARMACY", address);

					Cell c4 = sheet.getCell(startOfColumn+1, i);
					if(c4.getContents() != null && c4.getContents().length() >0){ //Count
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents().trim());						
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}

					outBag.put("TABLE",tableindex,"REMAINING", "0");					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);					
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", address);
					tableindex++;
				}	
															
			}				
				
			}
		
		return outBag;
	}
	public static ESIBag oasParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0,startOfCity=0;
		boolean breakFor = false;
		String pharmacy="";
		
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������ ������������ ������") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("�����") >= 0) {
					startOfSklad = columNo;
					startOfCount = columNo+1;
					breakFor = true;
					break;
				}
				if (c1.getContents().indexOf("�����") >= 0) {
					startOfCity = columNo;
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
					Cell c2 = sheet.getCell(startOfTovar, i);
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());

					pharmacy = c3.getContents();
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacy);			
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					
					outBag.put("TABLE",tableindex,"APTEKNO", "");
					
					
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacy);
					
		
					Cell c4 = sheet.getCell(startOfCount, i);
					if(c4.getContents() != null && c4.getContents().length() >0){
						String count  = c4.getContents();
						outBag.put("TABLE",tableindex,"COUNT", count);
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
					Cell c6 = sheet.getCell(startOfCity, i);
					if(c6.getContents() != null && c6.getContents().length() >0){
						String city  = c6.getContents();
						outBag.put("TABLE",tableindex,"CITY", city);
					}
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);		
					outBag.put("TABLE",tableindex,"AMOUNT","0.00");
				
					tableindex++;
			}
		}
		}
		
		return outBag;
	}
	public static ESIBag megaFarmParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0;
		boolean breakFor = false;
		String pharmacy="";
		
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0 
						&& c1.getContents().length() <= 12) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("�������������")>=0) {
					startOfSklad = columNo;
				}
				if (c1.getContents().indexOf("����������") >= 0) {
					startOfCount = columNo;
				}
				if (c1.getContents().indexOf("�����") >= 0) {
					startOfAmount = columNo;
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
					
					Cell c2 = sheet.getCell(startOfTovar, i);
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());

					pharmacy = c3.getContents();
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacy);			
					
					
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
						
					outBag.put("TABLE",tableindex,"APTEKNO", "");		
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacy);
					
		
					Cell c4 = sheet.getCell(startOfCount, i);
					if(c4.getContents() != null && c4.getContents().length() >0){
						String count  = c4.getContents();
						outBag.put("TABLE",tableindex,"COUNT", count);
					}
		
					Cell c5 = sheet.getCell(startOfAmount, i);
					if(c5.getContents() != null && c5.getContents().length() >0){
						outBag.put("TABLE",tableindex,"AMOUNT", c5.getContents());
					}else{
						outBag.put("TABLE",tableindex,"AMOUNT","0.00");
					}
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);	
					outBag.put("TABLE",tableindex,"CITY", "");			
				
					tableindex++;
			}
		}
		}
		
		return outBag;
	}
	
	public static ESIBag dejurnayaParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0;
		boolean breakFor = false;
		String pharmacy="";
		
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("�������� ������") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("�����")>=0) {
					startOfSklad = columNo;
				}
				if (c1.getContents().indexOf("���-��") >= 0) {
					startOfCount = columNo;
				}
				if (c1.getContents().indexOf("����� ������� � ���") >= 0) {
					startOfAmount = columNo;
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
					
					Cell c2 = sheet.getCell(startOfTovar, i);
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());

					pharmacy = c3.getContents();
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacy);			
					
					
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
						
					outBag.put("TABLE",tableindex,"APTEKNO", "");		
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacy);
					
		
					Cell c4 = sheet.getCell(startOfCount, i);
					if(c4.getContents() != null && c4.getContents().length() >0){
						String count  = c4.getContents();
						outBag.put("TABLE",tableindex,"COUNT", count);
					}
		
					Cell c5 = sheet.getCell(startOfAmount, i);
					if(c5.getContents() != null && c5.getContents().length() >0){
						outBag.put("TABLE",tableindex,"AMOUNT", c5.getContents());
					}else{
						outBag.put("TABLE",tableindex,"AMOUNT","0.00");
					}
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);	
					outBag.put("TABLE",tableindex,"CITY", "");			
				
					tableindex++;
			}
		}
		}
		
		return outBag;
	}
	
	public static ESIBag nikaParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0;
		boolean breakFor = false;
		String pharmacy="";
		
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("�������� ������") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("�����")>=0) {
					startOfSklad = columNo;
				}
				if (c1.getContents().indexOf("�������") >= 0) {
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
					
					Cell c2 = sheet.getCell(startOfTovar, i);
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());

					pharmacy = c3.getContents();
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacy);			
					
					
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
						
					outBag.put("TABLE",tableindex,"APTEKNO", "");		
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacy);
					
		
					Cell c4 = sheet.getCell(startOfCount, i);
					if(c4.getContents() != null && c4.getContents().length() >0){
						String count  = c4.getContents();
						outBag.put("TABLE",tableindex,"COUNT", count);
					}
		
					
					outBag.put("TABLE",tableindex,"AMOUNT","0.00");
					
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);	
					outBag.put("TABLE",tableindex,"CITY", "");			
				
					tableindex++;
			}
		}
		}
		
		return outBag;
	}
	public static ESIBag avicennaParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {

		int startOfRow = 0, startOfColumn = 0, productColumn = 0, pharmacyRow = 0, tableindex = 0, aptekaRow = 0,
				aptekaColumn = 0;

		boolean breakFor = false;
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {// Tum exceli gez// bul
			for (int columNo = 0; columNo < horizontalLimit; columNo++) {
				// ����� ����� ����_���������� ����_����������

				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("������������") >= 0) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;
					aptekaColumn = columNo + 1;
					productColumn = columNo;
					pharmacyRow = rowNo;
					aptekaRow = rowNo - 1;
					breakFor = true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		index = startOfColumn + 1;

		while (index < horizontalLimit) {
			for (int i = startOfRow; i < verticalLimit; i++) {
				Cell c3 = sheet.getCell(index, pharmacyRow);// pharmacy name
				String pharmacyName = c3.getContents();

				if (pharmacyName != null && pharmacyName.length() > 0) {
					outBag.put("TABLE", tableindex, "PHARMACY", pharmacyName);
					outBag.put("TABLE", tableindex, "APTEKNO", "");
					Cell c2 = sheet.getCell(productColumn, i); // product name
					if (c2.getContents() != null && c2.getContents().length() > 0) {
						outBag.put("TABLE", tableindex, "PRODUCT", c2.getContents());
					}

					Cell c4 = sheet.getCell(index, i);// count
					if (c4.getContents() != null && c4.getContents().length() > 0) {
						outBag.put("TABLE", tableindex, "COUNT", c4.getContents());
					} else {
						outBag.put("TABLE", tableindex, "COUNT", "0");
					}

					outBag.put("TABLE", tableindex, "AMOUNT", "0.00");
					outBag.put("TABLE", tableindex, "SUBGROUP", mainGroup);
					outBag.put("TABLE", tableindex, "MAINGROUP", mainGroup);
					outBag.put("TABLE", tableindex, "CITY", "");
					outBag.put("TABLE", tableindex, "SALESREADER", pharmacyName);
					tableindex++;
				}
			}
			index = index + 1;
		}
		return outBag;
	}
	public static ESIBag dialogParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,startOfCount = 0,startOfAmount = 0,tableindex = 0;
		boolean breakFor = false;
		String pharmacy="";
		
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("��������") >= 0) {
					startOfTovar = columNo;
					startOfRow = rowNo + 1;
				}
				if (c1.getContents().indexOf("�����")>=0) {
					startOfSklad = columNo;
					breakFor = true;
					break;
				}
				if (c1.getContents().indexOf("���-�� ����") >= 0) {
					startOfCount = columNo;
				}
				if (c1.getContents().indexOf("����� ����") >= 0) {
					startOfAmount = columNo;					
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
					
					Cell c2 = sheet.getCell(startOfTovar, i);
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());

					pharmacy = c3.getContents();
					outBag.put("TABLE",tableindex,"SALESREADER", pharmacy);			
					
					
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
						
					outBag.put("TABLE",tableindex,"APTEKNO", "");		
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacy);
					
		
					Cell c4 = sheet.getCell(startOfCount, i);
					if(c4.getContents() != null && c4.getContents().length() >0){
						String count  = c4.getContents();
						outBag.put("TABLE",tableindex,"COUNT", count);
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
		
					Cell c5 = sheet.getCell(startOfAmount, i);
					if(c5.getContents() != null && c5.getContents().length() >0){
						outBag.put("TABLE",tableindex,"AMOUNT", c5.getContents());
					}else{
						outBag.put("TABLE",tableindex,"AMOUNT","0.00");
					}
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);	
					outBag.put("TABLE",tableindex,"CITY", "");			
				
					tableindex++;
			}
		}
		}
		
		return outBag;
	}
	public static ESIBag listikParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,productColumn=0,pharmacycolumn=0,tableindex = 0;
		boolean breakFor = false;
		
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().trim().indexOf("������") >= 0 ) {
					pharmacycolumn = columNo;
				}else if(c1.getContents().trim().indexOf("�������� ������") >= 0 ) {
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;
					productColumn = columNo;
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
				
				for (int i = startOfRow; i < verticalLimit-1; i++) {
		
					Cell c3 = sheet.getCell(productColumn, i);//pharmacy name ystsy dikey 
					String product = c3.getContents();
					if(product != null && product.length() >0){	
						
						outBag.put("TABLE",tableindex,"PRODUCT",product);						
						outBag.put("TABLE",tableindex,"APTEKNO", "");
												
						
						Cell c2 = sheet.getCell(index, pharmacycolumn); //address
						String pharmacyName = c2.getContents().trim();
						if(pharmacyName != null && pharmacyName.length() >0){
							outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);
						}else{
							outBag.put("TABLE",tableindex,"PHARMACY", "");
						}
			
						Cell c4 = sheet.getCell(index, i);//count
						if(c4.getContents() != null && c4.getContents().length() >0){
							outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
						}else{
							outBag.put("TABLE",tableindex,"COUNT", "0");
						}
						
						outBag.put("TABLE",tableindex,"AMOUNT", "0.00");					
						outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
						outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
						outBag.put("TABLE",tableindex,"CITY", "");
						outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
						
						tableindex++;
			
					}
		
				}		
					index = index+1;
			
			}
		return outBag;
		
	}
	public static ESIBag novaVitaParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,productColumn=0,pharmacyRow=0,tableindex = 0;
		boolean breakFor = false;
		
		int index = 0;
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if(c1.getContents().trim().indexOf("�������� �����") >= 0 ) {
					pharmacyRow = rowNo;
					startOfColumn = columNo + 1;
					startOfRow = rowNo + 1;
					productColumn = columNo;
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
				
				for (int i = startOfRow; i < verticalLimit-1; i++) {
		
					Cell c3 = sheet.getCell(index, pharmacyRow);//pharmacy name
					String pharmacyName = c3.getContents();
					if(pharmacyName != null && pharmacyName.length() >0){	
						outBag.put("TABLE",tableindex,"PHARMACY", pharmacyName);											
						outBag.put("TABLE",tableindex,"APTEKNO", "");		
						Cell c2 = sheet.getCell(productColumn, i); //address

						outBag.put("TABLE",tableindex,"PRODUCT",c2.getContents().trim());
			
						Cell c4 = sheet.getCell(index, i);//count
						if(c4.getContents() != null && c4.getContents().length() >0){
							outBag.put("TABLE",tableindex,"COUNT", c4.getContents());
						}else{
							outBag.put("TABLE",tableindex,"COUNT", "0");
						}
						
						outBag.put("TABLE",tableindex,"AMOUNT", "0.00");					
						outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
						outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);
						outBag.put("TABLE",tableindex,"CITY", "");
						outBag.put("TABLE",tableindex,"SALESREADER", pharmacyName);
						
						tableindex++;
			
					}
		
				}		
					index = index+1;
			
			}
		return outBag;
		
	}
	public static ESIBag evalarParser(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfTovar = 0,startOfSklad = 0,
				startOfCount = 0,startOfAmount = 0,tableindex = 0,startOfSubGroup=0,startOfPharmacyNo=0,startOfCity=0;
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
					startOfSklad = columNo;
				}
				if (c1.getContents().indexOf("���-��") >= 0) {
					startOfCount = columNo;
				}
				if (c1.getContents().indexOf("����� ��������") >= 0) {
					startOfAmount = columNo;
				}				

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit; i++) {
			if (i>0){
				Cell c2 = sheet.getCell(startOfTovar, i);
				if(c2.getContents() != null && c2.getContents().length() >0){
					outBag.put("TABLE",tableindex,"PRODUCT", c2.getContents());

					Cell c3 = sheet.getCell(startOfSklad, i);
					if(c3.getContents() != null && c3.getContents().length() >0){
						pharmacy = c3.getContents();
						outBag.put("TABLE",tableindex,"SALESREADER", pharmacy);			
					}else{
						outBag.put("TABLE",tableindex,"SALESREADER", "");
					}					
					
					Cell c4 = sheet.getCell(startOfCount, i);
					if(c4.getContents() != null && c4.getContents().length() >0){
						String count  = c4.getContents();
						outBag.put("TABLE",tableindex,"COUNT", count);
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}
		
					Cell c5 = sheet.getCell(startOfAmount, i);
					if(c5.getContents() != null && c5.getContents().length() >0){
						outBag.put("TABLE",tableindex,"AMOUNT", c5.getContents());
					}else{
						outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					}
					
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"APTEKNO", "");
					outBag.put("TABLE",tableindex,"CITY", "");					
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);	
					outBag.put("TABLE",tableindex,"PHARMACY", pharmacy);
					
					tableindex++;
			 }
		 }
		}
		
		return outBag;
	}
	
	public static ESIBag sheksnaParserNew(Sheet sheet,String mainGroup, int verticalLimit,int horizontalLimit) {
		int startOfRow = 0,startOfColumn = 0,tableindex = 0;
		boolean breakFor = false;
		String productName="";
		String city = "";
		String address = "";
		ESIBag outBag = new ESIBag();

		for (int rowNo = 0; rowNo < verticalLimit; rowNo++) {//Tum exceli gez bul

			for (int columNo = 0; columNo < horizontalLimit; columNo++) {

				// ����� ����� ����_���������� ����_����������
				Cell c1 = sheet.getCell(columNo, rowNo);
				if (c1.getContents().indexOf("�������� �����") >= 0) {
					startOfColumn = columNo;//dikey
					startOfRow = rowNo + 1;//yatay
					breakFor =true;
					break;
				}

			}

			if (breakFor) {
				break;
			}

		}

		for (int i = startOfRow; i < verticalLimit-1; i++) {

			Cell c2 = sheet.getCell(startOfColumn, i);
			if(c2.getContents() != null && c2.getContents().length() >0){//City adres and product 
				if(c2.getContents().trim().subSequence(0, 6).toString().matches("������"))	{
					address = c2.getContents().trim();
				}else{
					productName = c2.getContents().trim();
					outBag.put("TABLE",tableindex,"PRODUCT",productName);
					outBag.put("TABLE",tableindex,"APTEKNO", "");				
					outBag.put("TABLE",tableindex,"PHARMACY", address);

					Cell c4 = sheet.getCell(startOfColumn+1, i);
					if(c4.getContents() != null && c4.getContents().length() >0){ //Count
						outBag.put("TABLE",tableindex,"COUNT", c4.getContents().trim());						
					}else{
						outBag.put("TABLE",tableindex,"COUNT", "0");
					}

					outBag.put("TABLE",tableindex,"REMAINING", "0");					
					outBag.put("TABLE",tableindex,"AMOUNT", "0.00");
					outBag.put("TABLE",tableindex,"SUBGROUP", mainGroup);
					outBag.put("TABLE",tableindex,"MAINGROUP", mainGroup);					
					outBag.put("TABLE",tableindex,"CITY", "");
					outBag.put("TABLE",tableindex,"SALESREADER", address);
					tableindex++;
				}	
															
			}								
			}
		
		return outBag;
	}
}