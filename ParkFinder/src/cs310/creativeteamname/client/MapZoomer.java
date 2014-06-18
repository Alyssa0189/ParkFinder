/** Takes care of zooming and centering on parks on an associated park map.
 * 
 */

package cs310.creativeteamname.client;

import java.util.Iterator;
import java.util.Set;

import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;

class MapZoomer {
	private final double VANCOUVER_SOUTH_LATITUDE = 49.197859;
	private final double VANCOUVER_NORTH_LATITUDE = 49.316394;
	private final double VANCOUVER_WEST_LONGITUDE = -123.264599;
	private final double VANCOUVER_EAST_LONGITUDE = -123.021839;
	
	private LatLngBounds vancouverBounds;
	MapWidget map;
	
	/** Create a new map zoomer associated with the given map.
	 * 
	 * @param map the map associated with the zoomer.
	 */
	MapZoomer(MapWidget map) {
		this.map = map;
		
		
		LatLng southWestVancouver = LatLng.newInstance(VANCOUVER_SOUTH_LATITUDE, VANCOUVER_WEST_LONGITUDE);
		LatLng northEastVancouver = LatLng.newInstance(VANCOUVER_NORTH_LATITUDE, VANCOUVER_EAST_LONGITUDE);
		vancouverBounds = LatLngBounds.newInstance(southWestVancouver, northEastVancouver);
	}
	
	/** Zoom and center the map on the list of parks.
	 *  If there are no parks, zoom to all of Vancouver.
	 * 
	 * @param parks the parks to zoom and center on.
	 */
	public void zoomAndCenter(Set<LightweightPark> parks) {
		
		removeParksNotInBounds(parks);
		LatLngBounds bounds = getBoundingRectangle(parks);
		
		centerOnBounds(bounds);
		zoomToBounds(bounds);
		

	}
	
	
	/** Get the bounding rectangle enclosing all listed parks that are in Vancouver.
	 *  If there are no parks, zoom to all of Vancouver.
	 * 
	 * @param parks the parks that's locations will determine the bounding rectangle.
	 * @return the bounding rectangle containing the parks.
	 */
	private LatLngBounds getBoundingRectangle(Set<LightweightPark> parks) {
		// When there are no parks, default bounds should be all of Vancouver.
		if(parks.isEmpty())
			return vancouverBounds;
		
		Iterator<LightweightPark> parkIter = parks.iterator();
		LatLngBounds bounds = initializeBoundsToPark(parks, parkIter);
		
		while(parkIter.hasNext()) {
			LightweightPark aPark = (LightweightPark)parkIter.next();
			if(boundsRequireExtension(bounds, aPark))
				bounds.extend(aPark.getLocation());
		}
		
		return bounds;
	}
	
	/** Get bounds for a single park in a given list.
	 * 
	 * @param parks the list of parks
	 * @param parkIter an iterator intitialized for the list of parks
	 * @return the bounds enclosing a single park in the list of parks
	 */
	private LatLngBounds initializeBoundsToPark(Set<LightweightPark> parks, Iterator<LightweightPark> parkIter) {
		LightweightPark aPark = (LightweightPark)parkIter.next();
		LatLngBounds pointBound = getBoundsFromPoint(aPark.getLocation());
		
		return pointBound;
	}
	
	/** Remove all parks not in Vancouver's bounds from a set of parks.
	 * 
	 * @param parks the set of parks to check.
	 */
	private void removeParksNotInBounds(Set<LightweightPark> parks) {
		for(LightweightPark park : parks)
			if(!parkInBounds(park))
				parks.remove(park);
	}
	
	/** Get a small bound enclosing a point.
	 * 
	 * @param point the point to represent.
	 * @return a bounding box consisting of a small box around the point.
	 */
	private LatLngBounds getBoundsFromPoint(LatLng point) {
		double pointLat = point.getLatitude();
		double pointLon = point.getLongitude();
		
		LatLng southWestCorner = LatLng.newInstance(pointLat - 0.001, pointLon - 0.001);
		LatLng northEastCorner = LatLng.newInstance(pointLat + 0.001, pointLon + 0.001);
		
		return LatLngBounds.newInstance(northEastCorner, southWestCorner);
	}
	
	/** Center the map on the center of the given bounds.
	 * 
	 * @param bounds the bounds to center on.
	 */
	private void centerOnBounds(LatLngBounds bounds) {
		LatLng center = bounds.getCenter();
		map.setCenter(center, 20);
	}
	
	/** Zoom the map to encompass the given bounds.
	 * 
	 * @param bounds the bounds to be zoomed onto.
	 */
	private void zoomToBounds(LatLngBounds bounds) {
		int zoomLevel = map.getBoundsZoomLevel(bounds);
		System.out.println("North east: " + bounds.getNorthEast());
		System.out.println("South west: " + bounds.getSouthWest());
		System.out.println("Zoom level: " + zoomLevel);
		map.setZoomLevel(zoomLevel);
	}

	
	/** Determine whether or not considering a particular new park will require the bounds to be extended.
	 * 
	 * @param bounds the bounds so far that may or may not require extension.
	 * @param park the new park under consideration.
	 * @return true if the park is outside of the bounds and in Vancouver.
	 */
	private boolean boundsRequireExtension(LatLngBounds bounds, LightweightPark park) {
		LatLng parkLocation = park.getLocation();
		return !bounds.containsLatLng(parkLocation);
	}
	
	/** Determine whether or not the given park is in the bounds of Vancouver.
	 * 
	 * @param park the park that's location will be evaluated.
	 * @return true if the park is in the bounds of Vancouver.
	 */
	private boolean parkInBounds(LightweightPark park) {
		LatLng parkLocation = park.getLocation();
		return vancouverBounds.containsLatLng(parkLocation);
	}
}
