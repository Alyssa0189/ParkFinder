package cs310.creativeteamname.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("comment")
public interface CommentService extends RemoteService{
	public void addComment(String input) throws IllegalArgumentException;
	public String[] getComment();
	
	
	
};


