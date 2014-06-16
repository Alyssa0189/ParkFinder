package cs310.creativeteamname.shared;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import javax.jdo.annotations.PersistenceCapable;
@PersistenceCapable
public class Park {
	public Park(){
		washrooms = new LinkedList<Washroom>();
		facilities = new LinkedList<Facility>();
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
		return NeighbourhoodName;
	}
	public void setNeighbourhoodName(String neighbourhoodName) {
		NeighbourhoodName = neighbourhoodName;
	}
	public String getNeighbourhoodURL() {
		return NeighbourhoodURL;
	}
	public void setNeighbourhoodURL(String neighbourhoodURL) {
		NeighbourhoodURL = neighbourhoodURL;
	}
	public List<Facility> getFacilities() {
		return facilities;
	}
	public void addFacility(Facility facility){
		facilities.add(facility);
	}
	public List<Washroom> getWashrooms(){
		return washrooms;
	}
	public void addWashroom(Washroom washroom){
		washrooms.add(washroom);
	}
	private List<Washroom> washrooms;
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
    private String NeighbourhoodName;
    private String NeighbourhoodURL;
    private List<Facility> facilities;
}
