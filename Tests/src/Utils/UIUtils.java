package Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import seleniumTest.Settings;

public class UIUtils {

	public static String pathParser = "src/files/";
	public static String pathUI = "C:/Users/samanta/Documents/GitHub/visualiZation_to_virtUal_laB_Interface/Tests/src/files/";
	public static void hoverAnElement(String id){
		WebElement node= Settings.driver.findElement(By.id(id));
		hoverAnElement(node);
	}
	
	public static void hoverAnElement(WebElement element){
		Actions builder = new Actions(Settings.driver); 
		Actions hoverOverRegistrar = builder.moveToElement(element);
		hoverOverRegistrar.perform();
	}

}
