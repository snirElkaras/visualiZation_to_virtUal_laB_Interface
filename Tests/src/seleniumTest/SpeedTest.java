package seleniumTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Utils.UIUtils;

public class SpeedTest {
	private static final int timeoutRequirements = 5000;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Settings.init();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
				Settings.driver.quit();
	}

	@Test(timeout=timeoutRequirements)
	public void planViewUploadSpeedTest() {
		WebElement chooseFile = Settings.driver.findElement(By.id("chooseFile"));
		chooseFile.sendKeys(UIUtils.pathUI + "plan.xml");
		chooseFile.submit();
		WebElement someCircle = Settings.driver.findElement(By.className("nodeCircle"));
		assertTrue(someCircle.isDisplayed());
	}
	
	@Test(timeout=timeoutRequirements)
	public void temporalViewUploadSpeedTest() {
		WebElement chooseFile = Settings.driver.findElement(By.id("chooseFile"));
		chooseFile.sendKeys(UIUtils.pathUI + "temporal.log");
		chooseFile.submit();
		WebElement someCircle = Settings.driver.findElement(By.className("circles"));
		assertTrue(someCircle.isDisplayed());
	}
}
