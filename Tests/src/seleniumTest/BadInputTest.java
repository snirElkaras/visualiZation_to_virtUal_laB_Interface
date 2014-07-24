package seleniumTest;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Utils.UIUtils;

public class BadInputTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Settings.init();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Settings.driver.quit();
	}

	@Test
	public void corruptedFileNotificationTest() {
		WebElement chooseFile = Settings.driver.findElement(By.id("chooseFile"));
		chooseFile.sendKeys(UIUtils.pathUI + "corruptedFile.xml");
		chooseFile.submit();
		WebElement errorNotification = Settings.driver.findElement(By.id("errorNotification"));
		String actual = errorNotification.getText();
		assertEquals("Corrupted file", actual);
	}
	@Test
	public void InvalidFileTypeTest() {
		WebElement chooseFile = Settings.driver.findElement(By.id("chooseFile"));
		chooseFile.sendKeys(UIUtils.pathUI + "badSuffix.bad");
		chooseFile.submit();
		WebElement errorNotification = Settings.driver.findElement(By.id("errorNotification"));
		String actual = errorNotification.getText();
		assertEquals("Invalid file type", actual);
	}
	
	@Test
	public void errorNotDisplayedAfterValidFileUploadedTest() {
		WebElement chooseFile = Settings.driver.findElement(By.id("chooseFile"));
		chooseFile.sendKeys(UIUtils.pathUI + "badSuffix.bad");
		chooseFile.submit();
		chooseFile.sendKeys(UIUtils.pathUI + "plan.xml");
		chooseFile.submit();
		WebElement errorNotification = Settings.driver.findElement(By.id("errorNotification"));
		String actual = errorNotification.getText();
		assertEquals("", actual);
	}

}
