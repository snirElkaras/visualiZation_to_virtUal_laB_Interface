package seleniumTest;

import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Settings  {
	public static WebDriver driver;
	
	public static void init(){
		try {
			driver = new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
			driver.get("http://localhost/VisualizationServer/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
