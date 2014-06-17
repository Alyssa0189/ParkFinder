package cs310.creativeteamname.server;
import java.util.HashMap;
import java.util.Iterator;

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

	@Override
	public HashMap<Integer, Park> getAllParks() {
		// TODO Auto-generated method stub
		return retrieveParks();
	}
	
	private HashMap<Integer, Park> retrieveParks(){
		HashMap<Integer, Park> parks = new HashMap<Integer, Park>();
		PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory();
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction transaction = pm.currentTransaction();
		transaction.begin();
		Extent<Park> e = pm.getExtent(Park.class);
		Iterator iter=e.iterator();
	    while (iter.hasNext())
	    {
	        Park p = (Park)iter.next();
	        parks.put(p.getParkId(), p);
	    }
	    transaction.commit();
		return parks;
	}
}
