/** A lightweight park with only a name and location, used for the map.
 * 
 */

package cs310.creativeteamname.client;

import com.google.gwt.maps.client.geom.LatLng;

public class LightweightPark {
	
	LatLng location;	// The latitude and longitude of this park.
	String name;		// The name of this park.
	
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
}
