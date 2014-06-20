package cs310.creativeteamname.server;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.datanucleus.Transaction;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310.creativeteamname.client.DataVancouverService;
import cs310.creativeteamname.shared.Park;
public class DataVancouverServiceImpl extends RemoteServiceServlet implements DataVancouverService{
	//private static final String dataSource = "ftp://webftp.vancouver.ca/opendata/xml/parks_facilities.xml";
	
	private static final String dataSource = "https://gist.githubusercontent.com/mcracker/9da2bb2e0702b4011a65/raw/0902e816ccb9efa207e59421f3db7749baebbf68/parkXML";
	private static final String csvSource = "https://gist.githubusercontent.com/mcracker/fa82ca097e6820228ea3/raw/d68a98a78b1ce2bc24851e57012ce3e61115f546/parkImages";
	private static final String zipSource = "ftp://webftp.vancouver.ca/opendata/csv/csv_parks_facilities.zip";
	private static final String PARK_IMAGES_FILE_NAME = "park_images.csv";
	private static final Logger logger = Logger.getLogger("logger"); // how many times can you type logger before it looks weird?
	private static final PersistenceManagerFactory PMF =
		      JDOHelper.getPersistenceManagerFactory("transactions-optional");
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static final int entitiesPerTransaction = 4;
	TransactionOptions options = TransactionOptions.Builder.withXG(true);
	public void refreshData(){
		deleteParks();
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
		parseImageDataRaw(parks);
		//parseImageData(parks);
		save(parks);
		//LocationServiceImpl.setParks(parks);
	}
	private void parseImageDataRaw(HashMap<Integer, Park> parks){
		String url = csvSource;
		URL obj;
		try {
			obj = new URL(url);
			URLConnection con = obj.openConnection();
			InputStream stream = con.getInputStream();
	        CsvStreamReader.addImagesWithLibrary(stream, parks);
	        		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void parseImageData(HashMap<Integer, Park> parks){
		String url = zipSource;
		URL obj;
		try {
			obj = new URL(url);
			URLConnection con = obj.openConnection();
			InputStream stream = con.getInputStream();
			ZipInputStream zipIn = new ZipInputStream(stream);
	        ZipEntry entry = zipIn.getNextEntry();
	        // iterates over entries in the zip file
	        while (entry != null) {
	        	if(PARK_IMAGES_FILE_NAME.equals(entry.getName())){
	        		CsvStreamReader.addImagesWithLibrary(zipIn, parks);
	        		break;
	        	}
	        	entry = zipIn.getNextEntry();
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private InputStream getXmlStream() throws IOException {
		String url = dataSource;
		URL obj;
		obj = new URL(url);
		URLConnection con = obj.openConnection();
		InputStream stream = con.getInputStream();
		return stream;
	}
	
	private void save(HashMap<Integer, Park> parks){
		PersistenceManager pm = PMF.getPersistenceManager();
		com.google.appengine.api.datastore.Transaction transaction = datastore.beginTransaction(options);
		int t = 0;
		int count = 0;
		for(Park p : parks.values()){
			count++;
			t++;
	    	if(t > entitiesPerTransaction){
	    		transaction.commit();
	    		transaction = datastore.beginTransaction(options);
	    		t = 0;
	    	}
			
			pm.makePersistent(p);
		}
		transaction.commit();
		pm.flush();
		pm.close();
		logger.severe("\radded " +  count + " parks");
	}
	
	private void saveWithQuery(HashMap<Integer, Park> parks){
		
	}
	
	private void deleteParks(){
		PersistenceManager pm = PMF.getPersistenceManager();
		com.google.appengine.api.datastore.Transaction transaction = datastore.beginTransaction(options);
		Extent<Park> e = pm.getExtent(Park.class, true);
		Iterator iter=e.iterator();
		int t = 0;
		int count = 0;
	    while (iter.hasNext())
	    {
	    	count++;
	    	t++;
	    	if(t > entitiesPerTransaction){
	    		transaction.commit();
	    		transaction = datastore.beginTransaction(options);
	    		t = 0;
	    	}
	    	Park p = (Park)iter.next();
	        pm.deletePersistent(p);
	    }
	    logger.severe("\rdeleted " + count + " parks");
	    transaction.commit();
	    pm.flush();
	    pm.close();
	}
}
