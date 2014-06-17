package cs310.creativeteamname.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310.creativeteamname.client.DetailsService;
import cs310.creativeteamname.shared.Park;

public class DetailsServiceImpl extends RemoteServiceServlet implements DetailsService {
	
	public Park getParkDetails(String parkId) {
		// mock object
		Park park = new Park();
		park.setName("Fake Park");
		park.setStreetNumber("123");
		park.setStreetName("Fake Street");
		park.setNeighbourhoodName("Point Grey");
		park.setNeighbourhoodURL("www.google.ca");
		park.addFacility("facility 1");
		park.addFacility("facility 2");
		park.addSpecialFeature("feature 1");
		return park;
	}

}
