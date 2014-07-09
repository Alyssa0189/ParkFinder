package cs310.creativeteamname.server;

import java.math.BigDecimal;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310.creativeteamname.client.RatingService;
import cs310.creativeteamname.shared.Rating;

public class RatingServiceImpl extends RemoteServiceServlet implements RatingService {
	
	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	public void addRating(int parkId, String userRating) {
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(Rating.class);
			q.setFilter("user == u && parkId == p");
			q.declareParameters("com.google.appengine.api.users.User u, int p");
			List<Rating> ratings = (List<Rating>) q.execute(getUser(), parkId);
			if(!ratings.isEmpty()) {
				ratings.get(0).setUserRating(new BigDecimal(userRating));  // update rating
			} else pm.makePersistent(new Rating(getUser(), parkId, new BigDecimal(userRating)));
		} finally {
			pm.close();
		}
	}
	
	public String getUserRating(int parkId) {
		PersistenceManager pm = getPersistenceManager();
		String userRating = "N/A";
		try {
			Query q = pm.newQuery(Rating.class);
			q.setFilter("user == u && parkId == p");
			q.declareParameters("com.google.appengine.api.users.User u, int p");
			List<Rating> ratings = (List<Rating>) q.execute(getUser(), parkId);
			if(!ratings.isEmpty()) {
				userRating = ratings.get(0).getUserRating().setScale(0).toString();
			}
		} finally {
			pm.close();
		}
		return userRating;
	}
	
	public String getAverageRating(int parkId) {
		PersistenceManager pm = getPersistenceManager();
		String averageRating = "N/A";
		try {
			Query q = pm.newQuery(Rating.class);
			q.setFilter("parkId == p");
			q.declareParameters("int p");
			List<Rating> ratings = (List<Rating>) q.execute(parkId);
			BigDecimal sum = BigDecimal.ZERO;
			if(!ratings.isEmpty()) {
				for(Rating rating : ratings) {
					sum = sum.add(rating.getUserRating());
				}
				averageRating = sum.divide(new BigDecimal(ratings.size()), 2, BigDecimal.ROUND_CEILING)
						.toString();
			}
		} finally {
			pm.close();
		}
		return averageRating;
	}
	
	private User getUser() {
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

}
