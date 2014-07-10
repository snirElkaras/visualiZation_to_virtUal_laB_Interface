package seleniumTest;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Utils.UIUtils;

public class TemporalViewTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Settings.init();
		WebElement chooseFile = Settings.driver.findElement(By.id("chooseFile"));
		chooseFile.sendKeys(UIUtils.path + "temporal.log");
		chooseFile.submit();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Settings.driver.quit();
	}
	
	@Before
	public void setUp() throws Exception {
		UIUtils.hoverAnElement("title");
	}

	@Test
	public void displayedDetailsWhenHoverANodeTest() {
		UIUtils.hoverAnElement("10");
		WebElement details= Settings.driver.findElement(By.id("details"));
		String expected = "Amount :\n0.001L\nSource Flask : (ID:6)\nDisposable Pipet (0.0 moles of H2O, 1.0048138305734868E-10 moles of H+, 1.0048138305734868E-10 moles of OH-, 0.001 moles of A)\n250mL\nRecipient Flask : (ID:10)\n250mL Beaker\nResult : (ID:10)\n250mL Beaker (0.0 moles of H2O, 1.0048138305734868E-10 moles of H+, 1.0048138305734868E-10 moles of OH-, 0.001 moles of A)";
		assertEquals(expected, details.getText());
	}
	@Test
	public void displayedDetailsWhenNotHoveringAnymoreOnTheNodeAndNotHoveringOnOtherNodesTest() {
		UIUtils.hoverAnElement("10");
		UIUtils.hoverAnElement("title");
		WebElement details= Settings.driver.findElement(By.id("details"));
		String expected = "Amount :\n0.001L\nSource Flask : (ID:6)\nDisposable Pipet (0.0 moles of H2O, 1.0048138305734868E-10 moles of H+, 1.0048138305734868E-10 moles of OH-, 0.001 moles of A)\n250mL\nRecipient Flask : (ID:10)\n250mL Beaker\nResult : (ID:10)\n250mL Beaker (0.0 moles of H2O, 1.0048138305734868E-10 moles of H+, 1.0048138305734868E-10 moles of OH-, 0.001 moles of A)";
		assertEquals(expected, details.getText());
	}
	@Test
	public void detailsAreChangedAfterHoverOtherNodeTest() {
		UIUtils.hoverAnElement("10");
		UIUtils.hoverAnElement("13");
		WebElement details= Settings.driver.findElement(By.id("details"));
		String expected = "Amount :\n0.001L\n250mL\nSource Flask : (ID:2)\nSolution B (0.0 moles of H2O, 1.0048138305734867E-8 moles of H+, 1.0048138305734867E-8 moles of OH-, 0.1 moles of B)\nRecipient Flask : (ID:13)\nDisposable Pipet\nResult : (ID:13)\nDisposable Pipet (0.0 moles of H2O, 1.0048138305734868E-10 moles of H+, 1.0048138305734868E-10 moles of OH-, 0.001 moles of B)";
		assertEquals(expected, details.getText());
	}
	
}
