package parsers.temporalParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import Utils.ParsersUtils;

public class SolutionMixTest {
	private static SolutionMix sm;


	@Before
	public void setUp() throws Exception {
		sm = new SolutionMix("event_id", "user", "event_session", "origin", "timestamp");
		sm.setSource(ParsersUtils.generateFlask("SRC"));
		sm.setRecipient(ParsersUtils.generateFlask("TRG"));
		sm.setResult(ParsersUtils.generateFlask("RES"));
		sm.setReadable("readable");
		sm.setVolume("volume");
	}


	@Test
	public void testParse() {
		JSONObject json = sm.parse();
		String actual = json.toString();
		String expected = "{\"readable\":\"readable\",\"recipient_flask\":{\"id\":\"TRG\",\"vesselType\":\"vesselTypeTRG\",\"species\":[{\"mass\":\"1namemass\",\"id\":\"1nameid\",\"name\":\"1namename\",\"state\":\"1namestate\",\"moles\":\"1namemoles\"},{\"mass\":\"2namemass\",\"id\":\"2nameid\",\"name\":\"2namename\",\"state\":\"2namestate\",\"moles\":\"2namemoles\"},{\"mass\":\"3namemass\",\"id\":\"3nameid\",\"name\":\"3namename\",\"state\":\"3namestate\",\"moles\":\"3namemoles\"}],\"name\":\"flaskTRG\",\"vesselVolume\":\"vesselVolumeTRG\",\"volume\":\"volumeTRG\",\"temp\":\"tempTRG\"},\"readableAmount\":\"volumeL\",\"origin\":\"origin\",\"event_id\":\"event_id\",\"event_session\":\"event_session\",\"readableRcp\":\"flaskTRG (1namemoles moles of 1namename, 2namemoles moles of 2namename, 3namemoles moles of 3namename)\",\"timestamp\":\"timestamp\",\"source_flask\":{\"id\":\"SRC\",\"vesselType\":\"vesselTypeSRC\",\"species\":[{\"mass\":\"1namemass\",\"id\":\"1nameid\",\"name\":\"1namename\",\"state\":\"1namestate\",\"moles\":\"1namemoles\"},{\"mass\":\"2namemass\",\"id\":\"2nameid\",\"name\":\"2namename\",\"state\":\"2namestate\",\"moles\":\"2namemoles\"},{\"mass\":\"3namemass\",\"id\":\"3nameid\",\"name\":\"3namename\",\"state\":\"3namestate\",\"moles\":\"3namemoles\"}],\"name\":\"flaskSRC\",\"vesselVolume\":\"vesselVolumeSRC\",\"volume\":\"volumeSRC\",\"temp\":\"tempSRC\"},\"readableSrc\":\"flaskSRC (1namemoles moles of 1namename, 2namemoles moles of 2namename, 3namemoles moles of 3namename)\",\"volume\":\"volume\",\"result_flask\":{\"id\":\"RES\",\"vesselType\":\"vesselTypeRES\",\"species\":[{\"mass\":\"1namemass\",\"id\":\"1nameid\",\"name\":\"1namename\",\"state\":\"1namestate\",\"moles\":\"1namemoles\"},{\"mass\":\"2namemass\",\"id\":\"2nameid\",\"name\":\"2namename\",\"state\":\"2namestate\",\"moles\":\"2namemoles\"},{\"mass\":\"3namemass\",\"id\":\"3nameid\",\"name\":\"3namename\",\"state\":\"3namestate\",\"moles\":\"3namemoles\"}],\"name\":\"flaskRES\",\"vesselVolume\":\"vesselVolumeRES\",\"volume\":\"volumeRES\",\"temp\":\"tempRES\"},\"readableRes\":\"flaskRES (1namemoles moles of 1namename, 2namemoles moles of 2namename, 3namemoles moles of 3namename)\",\"user\":\"user\"}";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testParseBadInput() {
		sm.setSource(null);
		JSONObject actual = sm.parse();
		assertNull(actual);
	}

}
