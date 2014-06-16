package cs310.creativeteamname.server;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import cs310.creativeteamname.shared.Facility;
import cs310.creativeteamname.shared.Park;
import cs310.creativeteamname.shared.Washroom;
import cs310.creativeteamname.shared.XmlConstants;
import model.*;
/**
 * This code is styled after the lab solution for CS210 SAX parsing lab.
 * @author Dan
 *
 */
public class DataHandler extends DefaultHandler{
	StringBuffer accumulator;
	List<Park> parkLocations;
	Park currentPark;
	Washroom currentWashroom = new Washroom();
	Facility currentFacility = new Facility();
	public List<Park> getParkLocations(){
		return parkLocations;
	}
	public DataHandler(){
		parkLocations = new LinkedList<Park>();
	}
	@Override
	public void startDocument(){
		accumulator = new StringBuffer();
		
	}
	
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts){
		accumulator.setLength(0);
		if(XmlConstants.PARK.equals(qName)){
			currentPark = new Park();
			try{
				int id = Integer.parseInt(atts.getValue(XmlConstants.PARK_ID));
				currentPark.setParkId(id);
			}catch(NumberFormatException nfe){
				// do nothing
			}
		}
	}
	@Override
	public void characters(char[] temp, int start, int length){
		accumulator.append(temp, start, length);
	}
	@Override
	public void endElement(String uri, String localName, String qName) {
		switch(qName){
		case XmlConstants.PARK:
			parkLocations.add(currentPark);
			break;
		case XmlConstants.NAME:
			currentPark.setName(accumulator.toString());
			break;
		case XmlConstants.OFFICIAL:
			if("1".equals(accumulator.toString())){
				currentPark.setOfficial(true);
			}else{
				currentPark.setOfficial(false);
			}
			break;
		case XmlConstants.STREET_NUMBER:
			currentPark.setStreetNumber(accumulator.toString());
			break;
		case XmlConstants.STREET_NAME:
			currentPark.setStreetName(accumulator.toString());
			break;
		case XmlConstants.EAST_WEST_STREET:
			currentPark.setEastWestStreet(accumulator.toString());
			break;
		case XmlConstants.NORTH_SOUTH_STREET:
			currentPark.setNorthSouthStreet(accumulator.toString());
			break;
		case XmlConstants.GOOGLE_MAP_DEST:
			int comma = accumulator.indexOf(",");
			if(comma > 0){
				int length = accumulator.length();
				String latitudeString = accumulator.substring(0, comma);
				String longitudeString = accumulator.substring(comma + 1, length);
				try{
					BigDecimal lat = new BigDecimal(latitudeString);
					BigDecimal lon = new BigDecimal(longitudeString);
					currentPark.setLat(lat);
					currentPark.setLon(lon);
				}catch(NumberFormatException nfe){
					System.out.println("Unable to parse lat lon");
				}
			}
			break;
		case XmlConstants.HECTARE:
			try{
				float h = Float.parseFloat(accumulator.toString());
				currentPark.setHectare(h);
			}catch(NumberFormatException nfe){
				System.out.println("cannot parse hectare");
			}
			break;
		case XmlConstants.NEIGHBOURHOOD_NAME:
			currentPark.setNeighbourhoodName(accumulator.toString());
			break;
		case XmlConstants.NEIGHBOURHOOD_URL:
			currentPark.setNeighbourhoodURL(accumulator.toString());
			break;
		case XmlConstants.ADVISORIES:
			break;
		case XmlConstants.ADVISORY:
			break;
		case XmlConstants.ADVISORY_TEXT:
			break;
		case XmlConstants.ADVISORY_URL:
			break;
		case XmlConstants.FACILITIES:
			break;
		case XmlConstants.FACILITY:
			currentPark.addFacility(currentFacility);
			currentFacility = new Facility();
			break;
		case XmlConstants.FACILITY_COUNT:
			try{
				int c = Integer.parseInt(accumulator.toString());
				currentFacility.setFacilityCount(c);
			}catch(NumberFormatException nfe){
				System.out.println("cannot parse facility count");
			}
			
			break;
		case XmlConstants.FACILITY_TYPE:
			currentFacility.setFacilityType(accumulator.toString());
			break;
		case XmlConstants.FACILITY_URL:
			currentFacility.setFacilityURL(accumulator.toString());
			break;
		case XmlConstants.WASHROOM:
			if(currentWashroom.getLocation() != null){
				currentPark.addWashroom(currentWashroom);
				currentWashroom = new Washroom();
			}
			break;
		case XmlConstants.WASHROOM_LOCATION:
			currentWashroom.setLocation(accumulator.toString());
			break;
		case XmlConstants.WASHROOM_NOTES:
			currentWashroom.setNotes(accumulator.toString());
			break;
		case XmlConstants.WASHROOM_WINTER_HOURS:
			currentWashroom.setWinterHours(accumulator.toString());
			break;
		case XmlConstants.WASHROOM_SUMMER_HOURS:
			currentWashroom.setSummerHours(accumulator.toString());
			break;
		
		}
	}
}

