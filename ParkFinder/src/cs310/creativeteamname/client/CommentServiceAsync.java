package cs310.creativeteamname.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CommentServiceAsync {
	public void addComment(String symbol, AsyncCallback<Void> callback);
	public void displayComment(AsyncCallback<String[]> callback);

}
