package cs310.creativeteamname.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RatingServiceAsync {
	
	public void addRating(int parkId, String userRating, AsyncCallback<Void> callback);
	
	public void getUserRating(int parkId, AsyncCallback<String> callback);
	
	public void getAverageRating(int parkId, AsyncCallback<String> callback);

}
