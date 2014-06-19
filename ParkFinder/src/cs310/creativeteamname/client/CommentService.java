package cs310.creativeteamname.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cs310.creativeteamname.shared.Comment;

@RemoteServiceRelativePath("comment")
public interface CommentService extends RemoteService {

	public void addComment(Comment comment);

	public Comment[] getComment(int parkId);

}
