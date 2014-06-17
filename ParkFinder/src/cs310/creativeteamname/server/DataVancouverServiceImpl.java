package cs310.creativeteamname.server;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedList;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.datanucleus.Transaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310.creativeteamname.client.DataVancouverService;
import cs310.creativeteamname.shared.Park;
public class DataVancouverServiceImpl extends RemoteServiceServlet implements DataVancouverService{
	private static final String dataSource = "ftp://webftp.vancouver.ca/opendata/xml/parks_facilities.xml";
	private static final String zipSource = "ftp://webftp.vancouver.ca/opendata/csv/csv_parks_facilities.zip";
	private static final String PARK_IMAGES_FILE_NAME = "park_images.csv";
	public void refreshData(){
		
		HashMap<Integer, Park> parks = new HashMap<Integer, Park>();
		XmlHandler handler = new XmlHandler(parks);
		InputStream stream;
		try {
			stream = getXmlStream();
			XmlParser parser = new XmlParser(handler, stream);
			parser.parseData();
		} catch (IOException e) {
			return;
		}
		save(parks);
		
	}

	private InputStream getXmlStream() throws IOException {
		String url = dataSource;
		URL obj;
		obj = new URL(url);
		URLConnection con = obj.openConnection();
		InputStream stream = con.getInputStream();
		return stream;
	}
	
	@SuppressWarnings("unchecked")
	private void save(HashMap<Integer, Park> parks){
		PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory();
		PersistenceManager pm = pmf.getPersistenceManager();
		javax.jdo.Transaction transaction = pm.currentTransaction();
		transaction.begin();
		pm.makePersistentAll(parks);
		transaction.commit();
	}
}
