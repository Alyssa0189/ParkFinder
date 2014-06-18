/** A lightweight park with only a name and location, used for the map.
 * 
 */

package cs310.creativeteamname.client;

import com.google.gwt.maps.client.geom.LatLng;

public class LightweightPark implements Comparable {
	
	LatLng location;
	String name;
	
	/** Create a new park with a location and name.
	 * 
	 * @param location the park's location.
	 * @param name the park's name.
	 */
	public LightweightPark(LatLng location, String name) {
		this.location = location;
		this.name = name;
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
	
	/** Determine whether or not this park is equal to another park.
	 * 
	 * @param other the other park.
	 * @return true if the two parks have the same name and location.
	 */
	public boolean equals(LightweightPark other) {
		return (name.equals(other.getName()) && location.equals(other.getLocation()));
	}
	
	/** Get the hash code for this park.
	 * 
	 * @return the hash code.
	 */
	public int hashCode() {
		int locationHash = location.hashCode();
		int nameHash = name.hashCode();
		return locationHash + (7 * nameHash);
	}

	@Override
	public int compareTo(Object other) {
		LightweightPark otherPark = (LightweightPark)other;
		
		if(this.equals(other))
			return 0;
		
		int nameCompare = this.name.compareTo(otherPark.getName());
		
		if(nameCompare != 0)
			return nameCompare;
		
		double thisLat = this.getLocation().getLatitude();
		double thisLon = this.getLocation().getLongitude();
		double otherLat = otherPark.getLocation().getLatitude();
		double otherLon = otherPark.getLocation().getLongitude();
			
		if(thisLat < otherLat)
			return -1;
		if(thisLat > otherLat)
			return 1;
		if(thisLon < otherLon)
			return -1;
		return 1;
		
	}
}
