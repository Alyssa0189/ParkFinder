package cs310.creativeteamname.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cs310.creativeteamname.shared.Comment;

public interface CommentServiceAsync {

	public void addComment(Comment comment, AsyncCallback<Void> callback);

	public void getComment(int parkId, AsyncCallback<Comment[]> callback);

}
