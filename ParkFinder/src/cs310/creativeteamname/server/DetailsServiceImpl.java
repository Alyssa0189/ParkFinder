package cs310.creativeteamname.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310.creativeteamname.client.DetailsService;
import cs310.creativeteamname.shared.Park;

public class DetailsServiceImpl extends RemoteServiceServlet implements DetailsService {
	
	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");
	
	public Park getParkDetails(int parkId) {
//		PersistenceManager pm = getPersistenceManager();
//		Park park = new Park();
//		try {
//			Query q = pm.newQuery(Park.class);
//			q.setFilter("parkId == parkIdParam");
//			q.declareParameters("int parkIdParam");
//			park = (Park) q.execute(parkId);
//		} finally {
//			pm.close();
//		}
		
		// mock object
		Park park = new Park();
		park.setName(Integer.toString(parkId));
		park.setStreetNumber("123");
		park.setStreetName("Fake Street");
		park.setNeighbourhoodName("Point Grey");
		park.setNeighbourhoodURL("www.google.ca");
		park.addFacility("facility 1");
		park.addFacility("facility 2");
		park.addSpecialFeature("feature 1");
		return park;
	}
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

}
