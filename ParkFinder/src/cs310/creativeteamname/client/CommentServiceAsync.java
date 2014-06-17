package cs310.creativeteamname.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CommentServiceAsync {
	public void addComment(String input, AsyncCallback<Void> callback) throws IllegalArgumentException;
	public void getComment(AsyncCallback<String[]> callback);

}
