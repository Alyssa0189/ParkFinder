package cs310.creativeteamname.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310.creativeteamname.client.CommentService;
import cs310.creativeteamname.shared.Comment;

public class CommentServiceImpl extends RemoteServiceServlet implements CommentService {

	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	public void addComment(Comment comment) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(comment);
		} finally {
			pm.close();
		}
	}

	public Comment[] getComment(int parkId) {
		PersistenceManager pm = getPersistenceManager();
		List<Comment> result = new ArrayList<Comment>();
		try {
			Query q = pm.newQuery(Comment.class);
			q.setFilter("parkId == parkIdParam");
			q.declareParameters("int parkIdParam");
			q.setOrdering("createTime desc");
			result = (List<Comment>) q.execute(parkId);
		} finally {
			pm.close();
		}
		Comment[] comments = result.toArray(new Comment[result.size()]);
		return comments;
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

}
