package cs310.creativeteamname.server;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310.creativeteamname.client.DataVancouverService;
import cs310.creativeteamname.shared.Park;
public class DataVancouverServiceImpl extends RemoteServiceServlet implements DataVancouverService{
	private static final String dataSource = "ftp://webftp.vancouver.ca/opendata/xml/parks_facilities.xml";
	private static final String zipSource = "ftp://webftp.vancouver.ca/opendata/csv/csv_parks_facilities.zip";
	private static final String PARK_IMAGES_FILE_NAME = "park_images.csv";
	public void refreshData(){
		
		LinkedList<Park> parks = new LinkedList<Park>();
		XmlHandler handler = new XmlHandler(parks);
		InputStream stream;
		try {
			stream = getXmlStream();
			XmlParser parser = new XmlParser(handler, stream);
			parser.parseData();
		} catch (IOException e) {
			return;
		}
	}
	private InputStream getXmlStream() throws IOException{
		String url = dataSource;
		URL obj;
			obj = new URL(url);
			URLConnection con = obj.openConnection();
			InputStream stream = con.getInputStream();
			return stream;
	}
}
