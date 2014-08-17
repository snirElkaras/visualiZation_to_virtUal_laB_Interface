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
		chooseFile.sendKeys(UIUtils.pathUI + "temporal.log");
		chooseFile.submit();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Settings.driver.quit();
	}
	
	@Before
	public void setUp() throws Exception {
		UIUtils.hoverAnElement("flask");
	}

	@Test
	public void displayedDetailsWhenHoverANodeTest() {
		UIUtils.hoverAnElement("10 250mL Beaker");
		WebElement details= Settings.driver.findElement(By.id("details"));
		String expected = "Amount :\n0.001L\nSource Flask : (ID:6 Disposable Pipet)\nDisposable Pipet (0.0 moles of H2O, 1.0048138305734868E-10 moles of H+, 1.0048138305734868E-10 moles of OH-, 0.001 moles of A)\n250mL\nRecipient Flask : (ID:10 250mL Beaker)\n250mL Beaker\nResult : (ID:10 250mL Beaker)\n250mL Beaker (0.0 moles of H2O, 1.0048138305734868E-10 moles of H+, 1.0048138305734868E-10 moles of OH-, 0.001 moles of A)";
		assertEquals(expected, details.getText());
	}
	@Test
	public void displayedDetailsWhenNotHoveringAnymoreOnTheNodeAndNotHoveringOnOtherNodesTest() {
		UIUtils.hoverAnElement("10 250mL Beaker");
		UIUtils.hoverAnElement("flask");
		WebElement details= Settings.driver.findElement(By.id("details"));
		String expected = "Amount :\n0.001L\nSource Flask : (ID:6 Disposable Pipet)\nDisposable Pipet (0.0 moles of H2O, 1.0048138305734868E-10 moles of H+, 1.0048138305734868E-10 moles of OH-, 0.001 moles of A)\n250mL\nRecipient Flask : (ID:10 250mL Beaker)\n250mL Beaker\nResult : (ID:10 250mL Beaker)\n250mL Beaker (0.0 moles of H2O, 1.0048138305734868E-10 moles of H+, 1.0048138305734868E-10 moles of OH-, 0.001 moles of A)";
		assertEquals(expected, details.getText());
	}
	@Test
	public void detailsAreChangedAfterHoverOtherNodeTest() {
		UIUtils.hoverAnElement("10 250mL Beaker");
		UIUtils.hoverAnElement("13 Disposable Pipet");
		WebElement details= Settings.driver.findElement(By.id("details"));
		String expected = "Amount :\n0.001L\n250mL\nSource Flask : (ID:2 Solution B)\nSolution B (0.0 moles of H2O, 1.0048138305734867E-8 moles of H+, 1.0048138305734867E-8 moles of OH-, 0.1 moles of B)\nRecipient Flask : (ID:13 Disposable Pipet)\nDisposable Pipet\nResult : (ID:13 Disposable Pipet)\nDisposable Pipet (0.0 moles of H2O, 1.0048138305734868E-10 moles of H+, 1.0048138305734868E-10 moles of OH-, 0.001 moles of B)";
		assertEquals(expected, details.getText());
	}
	
}
