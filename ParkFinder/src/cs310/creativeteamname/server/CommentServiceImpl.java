package cs310.creativeteamname.server;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;



import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310.creativeteamname.client.CommentService;

public class CommentServiceImpl extends RemoteServiceServlet implements CommentService{

	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");


	public void addComment(String input) {
		PersistenceManager pm =getPersistenceManager();
		pm.makePersistent(new Comment(input));
		pm.close();
			
	}
	
	
		public String[] getComment() {
		PersistenceManager pm = getPersistenceManager();
		List<String> inputs = new ArrayList<String>();
			List<Comment> comments = new ArrayList<Comment>();
			for (Comment comment : comments) {
				inputs.add(comment.getInput());
				pm.close();
			}
			return (String[]) inputs.toArray(new String[0]);
	}
	
			
			private PersistenceManager getPersistenceManager() {
				return PMF.getPersistenceManager();
				
			}

			
}
