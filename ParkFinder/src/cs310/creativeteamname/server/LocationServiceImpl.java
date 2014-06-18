package cs310.creativeteamname.server;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310.creativeteamname.client.LocationService;
import cs310.creativeteamname.shared.Park;
public class LocationServiceImpl  extends RemoteServiceServlet implements LocationService{
	private static final PersistenceManagerFactory PMF =
		      JDOHelper.getPersistenceManagerFactory("transactions-optional");
	private static HashMap<Integer, Park> parks;
	Logger logger = Logger.getLogger("");
	@Override
	public HashMap<Integer, Park> getAllParks() {
		// TODO Auto-generated method stub
		logger.severe("returning " + parks.size() + " parks");
		return parks;
		//return retrieveParks();
	}
	
	private HashMap<Integer, Park> retrieveParks(){
		logger.severe("in retrieve parks method");
		HashMap<Integer, Park> parks = new HashMap<Integer, Park>();
		PersistenceManager pm = PMF.getPersistenceManager();
		Transaction transaction = pm.currentTransaction();
		transaction.begin();
		Extent<Park> e = pm.getExtent(Park.class);
		Iterator iter=e.iterator();
	    while (iter.hasNext())
	    {
	        Park p = (Park)iter.next();
	        parks.put(p.getParkId(), p);
	        logger.severe("retrieving park with id = " + p.getParkId());
	    }
	    transaction.commit();
	    
	    
		return parks;
	}
	/**
	 * This is a temporary method to unblock the UI team.
	 * This will be replaced with persistence functionality.
	 * @param parks
	 */
	public static void setParks(HashMap<Integer, Park> parks){
		LocationServiceImpl.parks = parks;
	}
}
