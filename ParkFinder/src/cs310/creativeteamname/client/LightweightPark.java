/** A lightweight park with only a name, id, location and address.
 * 
 */

package cs310.creativeteamname.client;

import com.google.gwt.maps.client.geom.LatLng;

import cs310.creativeteamname.shared.Park;

public class LightweightPark implements Comparable {
	
	int id;
	LatLng location;
	String name;
	String address;
	
	/** Create a new park with an id, location and name.
	 * 
	 * @param location the park's location.
	 * @param name the park's name.
	 */
	public LightweightPark(int id, LatLng location, String name, String address) {
		this.id = id;
		this.location = location;
		this.name = name;
		this.address = address;
	}
	
	/** Create a new lightweight park from an existing lightweight park.
	 * 
	 * @param other the other lightweight park to copy.
	 */
	public LightweightPark(LightweightPark other) {
		this.id = other.getId();
		
		LatLng location = other.getLocation();
		this.location = LatLng.newInstance(location.getLatitude(), location.getLongitude());
		
		this.name = other.getName();
		
		this.address = other.getAddress();
	}
	
	/** Create a new lightweight park from a normal park.
	 * 
	 * @param heavyPark the park to create this lightweight park from.
	 */
	public LightweightPark(Park heavyPark) {
		this.id = heavyPark.getParkId();
		
		double latitude = heavyPark.getLat().doubleValue();
		double longitude = heavyPark.getLon().doubleValue();
		this.location = LatLng.newInstance(latitude, longitude);
		
		this.name = heavyPark.getName();
		
		String streetNumber = heavyPark.getStreetNumber();
		String streetName = heavyPark.getStreetName();
		this.address = streetNumber + " " + streetName;
	}
	
	/** Get the id of the park.
	 * 
	 * @return the park's id.
	 */
	public int getId() {
		return id;
	}
	
	/** Get the location of the park.
	 * 
	 * @return the park's location.
	 */
	public LatLng getLocation() {
		return location;
	}
	
	/** Get the name of the park.
	 * 
	 * @return the park's name.
	 */
	public String getName() {
		return name;
	}
	
	/** Get the address of the park.
	 * 
	 * @return the park's address.
	 */
	public String getAddress() {
		return address;
	}
	
	/** Determine whether or not this park is equal to another park.
	 * 
	 * @param other the other park.
	 * @return true if the two parks have the same id.
	 */
	@Override
	public boolean equals(Object other) {
		LightweightPark otherPark = (LightweightPark)other;
		return (otherPark.getId() == id);
	}
	
	/** Get the hash code for this park.
	 * 
	 * @return the hash code.
	 */
	@Override
	public int hashCode() {
		return id;
	}
	
	/** Compare this park to another park.
	 * 
	 */
	@Override
	public int compareTo(Object other) {
		LightweightPark otherPark = (LightweightPark)other;
		return id - otherPark.getId();
	}
}