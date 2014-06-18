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
	
	private VancouverBounds vancouverBounds = new VancouverBounds();
	MapWidget map;
	
	/** Create a new map zoomer associated with the given map.
	 * 
	 * @param map the map associated with the zoomer.
	 */
	MapZoomer(MapWidget map) {
		this.map = map;
	}
	
	/** Zoom and center the map on the list of parks.
	 *  If there are no parks, zoom to all of Vancouver.
	 * 
	 * @param parks the parks to zoom and center on.
	 */
	public void zoomAndCenter(Set<LightweightPark> parks) {
		
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
			return vancouverBounds.getBounds();
		
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
		
		return LatLngBounds.newInstance(southWestCorner, northEastCorner);
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
		map.setZoomLevel(zoomLevel);
	}

	
	/** Determine whether or not considering a particular new park will require the bounds to be extended.
	 * 
	 * @param bounds the bounds so far that may or may not require extension.
	 * @param park the new park under consideration.
	 * @return true if the park is outside of the given bounds.
	 */
	private boolean boundsRequireExtension(LatLngBounds bounds, LightweightPark park) {
		LatLng parkLocation = park.getLocation();
		return !bounds.containsLatLng(parkLocation);
	}
}
