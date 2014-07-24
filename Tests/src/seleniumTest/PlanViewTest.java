package seleniumTest;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Utils.UIUtils;

public class PlanViewTest {
	private static WebElement root;
	private static WebElement leaf;
	private static WebElement nonLeafNode;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Settings.init();
		WebElement chooseFile = Settings.driver.findElement(By.id("chooseFile"));
		chooseFile.sendKeys(UIUtils.pathUI + "plan.xml");
		chooseFile.submit();
	}
	
	@Before
	public void setUp() throws Exception {
		initAllNodesType();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
				Settings.driver.quit();
	}

	@Test
	public void collapseAllBtnTest() {
		collapseAll();
		WebElement circle = root.findElement(By.className("nodeCircle"));
		String actual = circle.getAttribute("style");
		String expected = "fill: rgb(255, 0, 0);";//red
		assertEquals(expected, actual);
	}
	
	@Test
	public void expandAllBtnTest() {
		collapseAll();
		expandAll();
		int numOfNodesDisplayed = initAllNodesType();
		assertEquals(33, numOfNodesDisplayed);
		WebElement circle = root.findElement(By.className("nodeCircle"));
		String actual = circle.getAttribute("style");
		String expected = "fill: rgb(255, 255, 255);";//white
		assertEquals(expected, actual);
		
	}


	@Test
	public void collapseANonLeafNodeTest() {
		nonLeafNode.click();
		WebElement circle = nonLeafNode.findElement(By.className("nodeCircle"));
		String actual = circle.getAttribute("style");
		String expected = "fill: rgb(255, 0, 0);";//red
		assertEquals(expected, actual);
	}

	@Test
	public void collapseALeafNodeTest() {
		WebElement circle = leaf.findElement(By.className("nodeCircle"));
		circle.click();
		String actual = circle.getAttribute("style");
		String expected = "fill: rgb(255, 255, 255);";//white
		assertEquals(expected, actual);
	}
	
	@Test
	public void displayedDetailsWhenHoverANodeTest() {
		UIUtils.hoverAnElement(nonLeafNode);
		WebElement details= Settings.driver.findElement(By.id("details"));
		String expected = "A+C+D\npos: 39\nIDs: from ID4 to ID5\nscd: 0E0g of H2O 1E-7M of H+ 1E-7M of OH- 1E0M of D\ndcd: 0E0g of H2O 1E-7M of H+ 1E-7M of OH- 4.84E-1M of A 1.6E-25M of B 1.61E-1M of C 1.94E-1M of D\nrcd: 0E0g of H2O 1E-7M of H+ 1E-7M of OH- 3.75E-1M of A 1.29E-25M of B 1.25E-1M of C 3.75E-1M of D\nvol: 400.0\naction: mix";
		assertEquals(expected, details.getText());
	}
	
	@Test
	public void displayedDetailsWhenNotHoveringAnymoreOnTheNodeAndNotHoveringOnOtherNodesTest() {
		UIUtils.hoverAnElement(nonLeafNode);
		UIUtils.hoverAnElement("flask");
		WebElement details= Settings.driver.findElement(By.id("details"));
		String expected = "A+C+D\npos: 39\nIDs: from ID4 to ID5\nscd: 0E0g of H2O 1E-7M of H+ 1E-7M of OH- 1E0M of D\ndcd: 0E0g of H2O 1E-7M of H+ 1E-7M of OH- 4.84E-1M of A 1.6E-25M of B 1.61E-1M of C 1.94E-1M of D\nrcd: 0E0g of H2O 1E-7M of H+ 1E-7M of OH- 3.75E-1M of A 1.29E-25M of B 1.25E-1M of C 3.75E-1M of D\nvol: 400.0\naction: mix";
		assertEquals(expected, details.getText());
	}

	private static void collapseAll(){
		WebElement collapseAllBtn = Settings.driver.findElement(By.id("collapseButton"));
		collapseAllBtn.click();
	}

	private static void expandAll(){
		WebElement expandAllBtn = Settings.driver.findElement(By.id("expandButton"));
		expandAllBtn.click();
	}
	
	private static int initAllNodesType() {
		expandAll();
		WebElement svg = Settings.driver.findElement(By.id("mainSVG"));
		List<WebElement> nodes = svg.findElements(By.className("node"));
		int numberOfNodesDisplayed = 0;
		for (WebElement node : nodes) {
			numberOfNodesDisplayed++;
			switch(node.getText()){
			case "Solve Problem":
				root = node;
				break;
			case "C":
				leaf = node;
				break;
			case "A+C+D":
				nonLeafNode = node;
				break;
			}
		}
		return numberOfNodesDisplayed;
	}

}
