package parsers.temporalParser;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import Utils.ParsersUtils;

public class SpeciesTest {
	private static Species sp;
	
	@Before
	public void setUp() throws Exception {
		sp = ParsersUtils.generateSingleSpecies("name");
	}

	@Test
	public void testCreateReadable() {
		String actual = sp.createReadable();
		String expected = "namemoles moles of namename";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testToJson() {
		JSONObject json = sp.toJson();
		String actual = json.toString();
		String expected = "{\"mass\":\"namemass\",\"id\":\"nameid\",\"name\":\"namename\",\"state\":\"namestate\",\"moles\":\"namemoles\"}";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCreateReadableBadInput() {
		sp.setName(null);
		String actual = sp.createReadable();
		String expected = "namemoles moles of null";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testToJsonBadInput() {
		sp.setId(null);
		JSONObject json = sp.toJson();
		String actual = json.toString();
		String expected = "{\"mass\":\"namemass\",\"name\":\"namename\",\"state\":\"namestate\",\"moles\":\"namemoles\"}";
		assertEquals(expected, actual);
	}

}
