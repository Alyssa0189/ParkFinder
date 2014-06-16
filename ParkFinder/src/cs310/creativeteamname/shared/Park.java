package cs310.creativeteamname.shared;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
public class Park implements Serializable {
	
	private int parkId;
    private String name;
    private boolean official;
    private String streetNumber;
    private String streetName;
    private String eastWestStreet;
    private String northSouthStreet;
    private BigDecimal lat;
    private BigDecimal lon;
    private float hectare;
    private String neighbourhoodName;
    private String neighbourhoodURL;
    private List<String> facilities;
    private List<String> specialFeatures;
    private boolean isWashroom;
    private String imageUrl;

	public Park() {
		facilities = new LinkedList<String>();
		specialFeatures = new LinkedList<String>();
		isWashroom = false;
	}
    
	public int getParkId() {
		return parkId;
	}
	
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isOfficial() {
		return official;
	}
	
	public void setOfficial(boolean official) {
		this.official = official;
	}
	
	public String getStreetNumber() {
		return streetNumber;
	}
	
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
	
	public String getStreetName() {
		return streetName;
	}
	
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	
	public String getEastWestStreet() {
		return eastWestStreet;
	}
	
	public void setEastWestStreet(String eastWestStreet) {
		this.eastWestStreet = eastWestStreet;
	}
	
	public String getNorthSouthStreet() {
		return northSouthStreet;
	}
	
	public void setNorthSouthStreet(String northSouthStreet) {
		this.northSouthStreet = northSouthStreet;
	}
	
	public BigDecimal getLat() {
		return lat;
	}
	
	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}
	
	public BigDecimal getLon() {
		return lon;
	}
	
	public void setLon(BigDecimal lon) {
		this.lon = lon;
	}
	
	public float getHectare() {
		return hectare;
	}
	
	public void setHectare(float hectare) {
		this.hectare = hectare;
	}
	
	public String getNeighbourhoodName() {
		return neighbourhoodName;
	}
	
	public void setNeighbourhoodName(String neighbourhoodName) {
		this.neighbourhoodName = neighbourhoodName;
	}
	
	public String getNeighbourhoodURL() {
		return neighbourhoodURL;
	}
	
	public void setNeighbourhoodURL(String neighbourhoodURL) {
		this.neighbourhoodURL = neighbourhoodURL;
	}
	
	public List<String> getFacilities() {
		return facilities;
	}

	public void setFacilities(List<String> facilities) {
		this.facilities = facilities;
	}

	public void addFacility(String facility){
		facilities.add(facility);
	}
	public List<String> getSpecialFeatures() {
		return specialFeatures;
	}

	public void setSpecialFeatures(List<String> specialFeatures) {
		this.specialFeatures = specialFeatures;
	}
	
	public void addSpecialFeature(String specialFeature){
		specialFeatures.add(specialFeature);
	}

	public boolean isWashroom() {
		return isWashroom;
	}

	public void setWashroom(boolean isWashroom) {
		this.isWashroom = isWashroom;
	}

	public String getImageUrl(){
		return imageUrl;
	}
	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}
}
