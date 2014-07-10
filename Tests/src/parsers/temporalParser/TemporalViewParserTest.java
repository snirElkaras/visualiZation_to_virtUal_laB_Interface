package parsers.temporalParser;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import Utils.ParsersUtils;

public class TemporalViewParserTest {
	private static String fileContent;

	@Before
	public void setUp() throws Exception {
		fileContent = ParsersUtils.fileToString(ParsersUtils.path + "TemporalViewExample.log");
	}

	@Test
	public void testParse() {
		TemporalViewParser parser = new TemporalViewParser();
		JSONObject json = parser.parse(fileContent);
		String actual = json.toString();
		String expected = "{\"solution_mix_list\":[{\"readable\":\"0.01 L from 250mL Erlenmeyer Flask (ID1) that contains Solution A (0.0g of HO, 1.0048138305734867E-7M of H, 1.0048138305734867E-7M of OH, 1.0M of A; V=0.1 mL,T=298.15K) is poured into 250mL Beaker (ID3) that contains 250mL Beaker (V=0.0 mL,T=298.15K). The resulting solution is 250mL Beaker (ID3) that contains 250mL Beaker (0.0g of HO, 1.0048138305734868E-7M of H, 1.0048138305734868E-7M of OH, 1.0M of A; V=0.01 mL,T=298.15K).\",\"recipient_flask\":{\"id\":\"3\",\"vesselType\":\"250mL Beaker\",\"species\":[],\"name\":\"250mL Beaker\",\"vesselVolume\":\"0.25893\",\"volume\":\"0.0\",\"temp\":\"298.15\"},\"readableAmount\":\"0.01L\",\"origin\":\"IRYDIUM_VLAB\",\"event_id\":\"18\",\"event_session\":\"null\",\"readableRcp\":\"250mL Beaker\",\"timestamp\":\"1399927495536\",\"source_flask\":{\"id\":\"1\",\"vesselType\":\"250mL Erlenmeyer Flask\",\"species\":[{\"mass\":\"0.0\",\"id\":\"0\",\"name\":\"H<sub>2<\\/sub>O\",\"state\":\"l\",\"moles\":\"0.0\"},{\"mass\":\"1.0128523412180747E-8\",\"id\":\"1\",\"name\":\"H<sup>+<\\/sup>\",\"state\":\"aq\",\"moles\":\"1.0048138305734867E-8\"},{\"mass\":\"1.708987363039386E-7\",\"id\":\"2\",\"name\":\"OH<sup>-<\\/sup>\",\"state\":\"aq\",\"moles\":\"1.0048138305734867E-8\"},{\"mass\":\"5.1000000000000005\",\"id\":\"3\",\"name\":\"A\",\"state\":\"aq\",\"moles\":\"0.1\"}],\"name\":\"Solution A\",\"vesselVolume\":\"0.2548\",\"volume\":\"0.1\",\"temp\":\"298.15\"},\"readableSrc\":\"Solution A (0.0 moles of H<sub>2<\\/sub>O, 1.0048138305734867E-8 moles of H<sup>+<\\/sup>, 1.0048138305734867E-8 moles of OH<sup>-<\\/sup>, 0.1 moles of A)\",\"volume\":\"0.01\",\"result_flask\":{\"id\":\"3\",\"vesselType\":\"250mL Beaker\",\"species\":[{\"mass\":\"0.0\",\"id\":\"0\",\"name\":\"H<sub>2<\\/sub>O\",\"state\":\"l\",\"moles\":\"0.0\"},{\"mass\":\"1.0128523412180747E-9\",\"id\":\"1\",\"name\":\"H<sup>+<\\/sup>\",\"state\":\"aq\",\"moles\":\"1.0048138305734869E-9\"},{\"mass\":\"1.7089873630393864E-8\",\"id\":\"2\",\"name\":\"OH<sup>-<\\/sup>\",\"state\":\"aq\",\"moles\":\"1.0048138305734869E-9\"},{\"mass\":\"0.51\",\"id\":\"3\",\"name\":\"A\",\"state\":\"aq\",\"moles\":\"0.01\"}],\"name\":\"250mL Beaker\",\"vesselVolume\":\"0.25893\",\"volume\":\"0.01\",\"temp\":\"298.15\"},\"readableRes\":\"250mL Beaker (0.0 moles of H<sub>2<\\/sub>O, 1.0048138305734869E-9 moles of H<sup>+<\\/sup>, 1.0048138305734869E-9 moles of OH<sup>-<\\/sup>, 0.01 moles of A)\",\"user\":\"null\"}],\"add_flask_list\":[{\"timestamp\":\"1399927507413\",\"readable\":\"A 250mL Erlenmeyer Flask (ID13) is added to the workbench.\",\"flask\":{\"id\":\"13\",\"vesselType\":\"250mL Erlenmeyer Flask\",\"species\":[{\"mass\":\"0.0\",\"id\":\"0\",\"name\":\"H<sub>2<\\/sub>O\",\"state\":\"l\",\"moles\":\"0.0\"},{\"mass\":\"1.0128523412180747E-8\",\"id\":\"1\",\"name\":\"H<sup>+<\\/sup>\",\"state\":\"aq\",\"moles\":\"1.0048138305734867E-8\"},{\"mass\":\"1.708987363039386E-7\",\"id\":\"2\",\"name\":\"OH<sup>-<\\/sup>\",\"state\":\"aq\",\"moles\":\"1.0048138305734867E-8\"},{\"mass\":\"5.1000000000000005\",\"id\":\"3\",\"name\":\"A\",\"state\":\"aq\",\"moles\":\"0.1\"}],\"name\":\"Solution A\",\"vesselVolume\":\"0.2548\",\"volume\":\"0.1\",\"temp\":\"298.15\"},\"workbench_id\":\"1\",\"origin\":\"IRYDIUM_VLAB\",\"event_id\":\"39\",\"event_session\":\"null\",\"user\":\"null\"}]}";
		assertEquals(expected , actual);
	}
	
	@Test
	public void testParseBadInput() {
		TemporalViewParser parser = new TemporalViewParser();
		JSONObject json = parser.parse("bad");
		String actual = json.toString();
		String expected = "{\"solution_mix_list\":[],\"add_flask_list\":[]}";
		assertEquals(expected , actual);
	}

}
