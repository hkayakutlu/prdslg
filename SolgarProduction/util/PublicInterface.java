package util;

import java.util.ResourceBundle;
import src.MainPage;
import main.ConnectToDb;


public interface PublicInterface {

	String Lang = MainPage.giveLangCode();
	ResourceBundle resourceBundle = ConnectToDb.readBundleFile(Lang);
	//ResourceBundle resourceBundleEn = ConnectToDb.readBundleFile("EN");
}
