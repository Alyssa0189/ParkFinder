package cs310.creativeteamname.server;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XmlParser {

	private XmlHandler handler;
	private InputStream stream;
	public XmlParser(XmlHandler handler, InputStream stream){
		this.handler = handler;
		this.stream = stream;
	}
	public void parseData(){
		XMLReader reader;
		try {
			reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(handler);
			InputSource source = new InputSource(stream);
			reader.parse(source);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}