package parsers.temporalParser;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import Utils.ParsersUtils;

public class FlaskTest {
	private static Flask flask;
	
	@Before
	public void setUp() throws Exception {
		flask = ParsersUtils.generateFlask("MyFlask");
	}

	@Test
	public void testCreateReadable() {
		String actual = flask.createReadable();
		String expected = "flaskMyFlask (1namemoles moles of 1namename, 2namemoles moles of 2namename, 3namemoles moles of 3namename)";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testToJson() {
		JSONObject json = flask.toJson();
		String actual = json.toString();
		String expected = "{\"id\":\"MyFlask\",\"vesselType\":\"vesselTypeMyFlask\",\"species\":[{\"mass\":\"1namemass\",\"id\":\"1nameid\",\"name\":\"1namename\",\"state\":\"1namestate\",\"moles\":\"1namemoles\"},{\"mass\":\"2namemass\",\"id\":\"2nameid\",\"name\":\"2namename\",\"state\":\"2namestate\",\"moles\":\"2namemoles\"},{\"mass\":\"3namemass\",\"id\":\"3nameid\",\"name\":\"3namename\",\"state\":\"3namestate\",\"moles\":\"3namemoles\"}],\"name\":\"flaskMyFlask\",\"vesselVolume\":\"vesselVolumeMyFlask\",\"volume\":\"volumeMyFlask\",\"temp\":\"tempMyFlask\"}";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCreateReadableBadInput() {
		flask.setName(null);
		String actual = flask.createReadable();
		String expected = "null (1namemoles moles of 1namename, 2namemoles moles of 2namename, 3namemoles moles of 3namename)";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testToJsonBadInput() {
		flask.setId(null);
		JSONObject json = flask.toJson();
		String actual = json.toString();
		String expected = "{\"vesselType\":\"vesselTypeMyFlask\",\"species\":[{\"mass\":\"1namemass\",\"id\":\"1nameid\",\"name\":\"1namename\",\"state\":\"1namestate\",\"moles\":\"1namemoles\"},{\"mass\":\"2namemass\",\"id\":\"2nameid\",\"name\":\"2namename\",\"state\":\"2namestate\",\"moles\":\"2namemoles\"},{\"mass\":\"3namemass\",\"id\":\"3nameid\",\"name\":\"3namename\",\"state\":\"3namestate\",\"moles\":\"3namemoles\"}],\"name\":\"flaskMyFlask\",\"vesselVolume\":\"vesselVolumeMyFlask\",\"volume\":\"volumeMyFlask\",\"temp\":\"tempMyFlask\"}";
		assertEquals(expected, actual);
	}

}
