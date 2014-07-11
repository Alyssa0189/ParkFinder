/** Represents the bounds of Vancouver.
 * 
 * Basic singleton layout taken from www.javaworld.com/article/2073352/core-java/simply-singleton.html
 */
package cs310.creativeteamname.client;

import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;

public class VancouverBounds {
	
	private final double VANCOUVER_SOUTH_LATITUDE = 49.197859;
	private final double VANCOUVER_NORTH_LATITUDE = 49.309384;
	private final double VANCOUVER_WEST_LONGITUDE = -123.226901;
	private final double VANCOUVER_EAST_LONGITUDE = -123.021839;
	
	public static VancouverBounds instance = null;
	
	private LatLngBounds bounds;
	
	/** Initialize the bounds of Vancouver.
	 * 
	 */
	protected VancouverBounds() {
		LatLng southWestCorner = LatLng.newInstance(VANCOUVER_SOUTH_LATITUDE, VANCOUVER_WEST_LONGITUDE);
		LatLng northEastCorner = LatLng.newInstance(VANCOUVER_NORTH_LATITUDE, VANCOUVER_EAST_LONGITUDE);
		bounds = LatLngBounds.newInstance(southWestCorner, northEastCorner);
	}
	
	/** Get the instance of this singleton.
	 * 
	 * @return the instance of VancouverBounds.
	 */
	public static VancouverBounds getInstance() {
		if(instance == null) {
			instance = new VancouverBounds();
		}
		return instance;
	}
	
	/** Get the bounds of Vancouver.
	 * 
	 * @return Vancouver's bounds.
	 */
	public LatLngBounds getBounds() {
		return bounds;
	}
	
	/** Determine whether or not a particular park lies within the bounds of Vancouver.
	 * 
	 * @param park the park to test.
	 * @return true if the park is in the bounds of Vancouver.
	 */
	public boolean containsPark(LightweightPark park) {
		LatLng parkLocation = park.getLocation();
		return bounds.containsLatLng(parkLocation);
	}
}
