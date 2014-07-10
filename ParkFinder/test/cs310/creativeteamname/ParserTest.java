package cs310.creativeteamname;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import cs310.creativeteamname.server.XmlHandler;
import cs310.creativeteamname.server.XmlParser;
import cs310.creativeteamname.shared.Park;

public class ParserTest {
	
	private static final String dataSource = "https://gist.githubusercontent.com/mcracker/9da2bb2e0702b4011a65/raw/0902e816ccb9efa207e59421f3db7749baebbf68/parkXML";
	HashMap<Integer, Park> parks = new HashMap<Integer, Park>();
	private Park first;
	private Park middle;
	private Park last;
	private static final double DELTA = 1.0e-6;
	
	@Before
	public void setup() {
		XmlHandler handler = new XmlHandler(parks);
		InputStream stream;
		try {
			stream = getXmlStream();
			XmlParser parser = new XmlParser(handler, stream);
			parser.parseData();
			first = parks.get(1);
			middle = parks.get(112);
			last = parks.get(246);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private InputStream getXmlStream() throws IOException {
		String url = dataSource;
		URL obj;
		obj = new URL(url);
		URLConnection con = obj.openConnection();
		InputStream stream = con.getInputStream();
		return stream;
	}
	
	@Test
	public void testNumParks() {
		assertEquals(215, parks.size());
	}
	
	@Test
	public void testAttributeFirst() {
		assertEquals(1, first.getParkId());
	}
	
	@Test
	public void testElementFirst() {
		assertEquals("Arbutus Village Park", first.getName());
	}
	
	@Test
	public void testDelimitedElementFirst() {		
		assertEquals(0, first.getLat().compareTo(new BigDecimal("49.249783")));
		assertEquals(0, first.getLon().compareTo(new BigDecimal("-123.155250")));
	}
	
	@Test
	public void testFloatFirst() {
		assertEquals(1.41, first.getHectare(), DELTA);
	}
	
	@Test
	public void testNestedElementsFirst() {
		assertEquals("Arbutus Ridge", first.getNeighbourhoodName());
		assertEquals("http://vancouver.ca/community_profiles/arbutus_ridge/index.htm",
				first.getNeighbourhoodURL());
	}
	
	@Test
	public void testAttributeMiddle() {
		assertEquals(112, middle.getParkId());
	}
	
	@Test
	public void testElementMiddle() {
		assertEquals("Kitsilano Beach Park", middle.getName());
	}
	
	@Test
	public void testDelimitedElementMiddle() {
		assertEquals(0, middle.getLat().compareTo(new BigDecimal("49.273431")));
		assertEquals(0, middle.getLon().compareTo(new BigDecimal("-123.153901")));
	}
	
	@Test
	public void testFloatMiddle() {
		assertEquals(13.47, middle.getHectare(), DELTA);
	}
	
	@Test
	public void testNestedElementsMiddle() {
		assertEquals("Kitsilano", middle.getNeighbourhoodName());
		assertEquals("http://vancouver.ca/community_profiles/kitsilano/index.htm",
				middle.getNeighbourhoodURL());
	}
	
	@Test
	public void testAttributeLast() {
		assertEquals(246, last.getParkId());
	}
	
	@Test
	public void testElementLast() {
		assertEquals("Creekway Park", last.getName());
	}
	
	@Test
	public void testDelimitedElementLast() {
		assertEquals(0, last.getLat().compareTo(new BigDecimal("49.288336")));
		assertEquals(0, last.getLon().compareTo(new BigDecimal("-123.036982")));
	}
	
	@Test
	public void testFloatLast() {
		assertEquals(8, last.getHectare(), DELTA);
	}
	
	@Test
	public void testNestedElementsLast() {
		assertEquals("Hastings-Sunrise", last.getNeighbourhoodName());
		assertEquals("http://vancouver.ca/community_profiles/hastings-sunrise/index.htm",
				last.getNeighbourhoodURL());
	}	
	
	@Test
	public void testOfficialTrue() {
		assertTrue(first.isOfficial());
	}
	
	@Test
	public void testOfficialFalse() {
		assertFalse(parks.get(116).isOfficial());
	}
	
	@Test
	public void testZeroFacilities() {
		assertEquals(0, last.getFacilities().length);
	}
	
	@Test
	public void testOneFacility() {
		assertTrue(Arrays.asList(first.getFacilities()).contains("Playgrounds"));
		assertEquals(1, first.getFacilities().length);
	}
	
	@Test
	public void testMultipleFacilities() {
		assertTrue(Arrays.asList(middle.getFacilities()).contains("Water/Spray Parks"));
		assertTrue(Arrays.asList(middle.getFacilities()).contains("Playgrounds"));
		assertTrue(Arrays.asList(middle.getFacilities()).contains("Tennis Courts"));
		assertTrue(Arrays.asList(middle.getFacilities()).contains("Basketball Courts"));
		assertTrue(Arrays.asList(middle.getFacilities()).contains("Swimming Pools"));
		assertTrue(Arrays.asList(middle.getFacilities()).contains("Beaches"));
		assertTrue(Arrays.asList(middle.getFacilities()).contains("Restaurants"));
		assertTrue(Arrays.asList(middle.getFacilities()).contains("Food Concessions"));
		assertEquals(8, middle.getFacilities().length);
	}
	
	@Test
	public void testZeroSpecialFeatures() {
		assertEquals(0, first.getSpecialFeatures().length);
	}
	
	@Test
	public void testOneSpecialFeature() {
		assertTrue(Arrays.asList(middle.getSpecialFeatures()).contains("Seawall"));
		assertEquals(1, middle.getSpecialFeatures().length);
	}
	
	@Test
	public void testMultipleSpecialFeatures() {
		assertTrue(Arrays.asList(parks.get(180).getSpecialFeatures()).contains("Horseshoe Pitch"));
		assertTrue(Arrays.asList(parks.get(180).getSpecialFeatures()).contains("Sport Court"));
		assertEquals(2, parks.get(180).getSpecialFeatures().length);
	}
	
	@Test
	public void testWashroomNonEmptyElement() {
		assertTrue(middle.isWashroom());
	}
	
	@Test
	public void testWashroomEmptyElement() {
		assertFalse(first.isWashroom());
	}

}
