package cs310.creativeteamname.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rating")
public interface RatingService extends RemoteService {
	
	public void addRating(int parkId, String userRating);
	
	public String getUserRating(int parkId);
	
	public String getAverageRating(int parkId);

}
