package cs310.creativeteamname.server;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
public class DataHandler extends DefaultHandler {

	@Override
	public void startDocument(){
		System.out.println("debug this");	
	}
	
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts){
		System.out.println("debug this");
	}
	@Override
	public void characters(char[] temp, int start, int length){
		
	}
	@Override
	public void endElement(String uri, String localName, String qName) {
		
	}
}
