package cs310.creativeteamname.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FacebookServiceAsync {
	public void logAppOnFacebook(AsyncCallback<String> callback );
	public void setStoryData(String comment, String parkName, AsyncCallback<String> callback);
}
