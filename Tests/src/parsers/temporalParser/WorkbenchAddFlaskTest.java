package parsers.temporalParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import Utils.ParsersUtils;

public class WorkbenchAddFlaskTest {
	private WorkbenchAddFlask addFlask;

	@Before
	public void setUp() throws Exception {
		addFlask = new WorkbenchAddFlask("event_id", "user", "event_session", "origin", "timestamp");
		addFlask.setFlask(ParsersUtils.generateFlask("name"));
		addFlask.setReadable("readable");
		addFlask.setWorkbench_id("workbench_id");
	}

	@Test
	public void testParse() {
		JSONObject json = addFlask.parse();
		String actual = json.toString();
		String expected = "{\"timestamp\":\"timestamp\",\"readable\":\"readable\",\"flask\":{\"id\":\"name\",\"vesselType\":\"vesselTypename\",\"species\":[{\"mass\":\"1namemass\",\"id\":\"1nameid\",\"name\":\"1namename\",\"state\":\"1namestate\",\"moles\":\"1namemoles\"},{\"mass\":\"2namemass\",\"id\":\"2nameid\",\"name\":\"2namename\",\"state\":\"2namestate\",\"moles\":\"2namemoles\"},{\"mass\":\"3namemass\",\"id\":\"3nameid\",\"name\":\"3namename\",\"state\":\"3namestate\",\"moles\":\"3namemoles\"}],\"name\":\"flaskname\",\"vesselVolume\":\"vesselVolumename\",\"volume\":\"volumename\",\"temp\":\"tempname\"},\"workbench_id\":\"workbench_id\",\"origin\":\"origin\",\"event_id\":\"event_id\",\"event_session\":\"event_session\",\"user\":\"user\"}";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testParseBadInput() {
		addFlask.setFlask(null);
		JSONObject actual = addFlask.parse();
		assertNull(actual);
	}

}
