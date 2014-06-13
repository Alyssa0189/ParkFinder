/* Singleton which creates a map of specified parks. 
 * 
 */

package cs310.creativeteamname.client;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class MapMaker {
	private final double VANCOUVER_CENTER_LATITUDE = 49.24181;
	private final double VANCOUVER_CENTER_LONGITUDE = -123.11368;
	
	private LatLng vancouverCenter;				// The center of vancouer.
	private static MapMaker instance = null;	// The instance of this map maker.
	private ArrayList<LightweightPark> parks;	// The list of parks to be displayed.
	private MapWidget parkMap;					// The actual map containing the parks.
	
	/** Construct the basic map properties.
	 * 
	 */
	protected MapMaker() {
		vancouverCenter = LatLng.newInstance(VANCOUVER_CENTER_LATITUDE, VANCOUVER_CENTER_LONGITUDE);
		parkMap = new MapWidget(vancouverCenter, 12);
		parkMap.setSize("100%", "100%");
		parkMap.addControl(new LargeMapControl());
		parkMap.addOverlay(new Marker(vancouverCenter));
		parkMap.getInfoWindow().open(parkMap.getCenter(), new InfoWindowContent("hi"));
	}
	
	/** Get the map maker's instance.
	 * 
	 * @return the instance of the map maker.
	 */
	public static MapMaker getInstance() {
		if(instance == null) {
			instance = new MapMaker();
		}
		return instance;
	}
	
	/** Declare the list of parks that will be displayed on the map.
	 * 
	 * @param parks the parks that will be displayed on the map.
	 */
	public void setParks(ArrayList<LightweightPark> parks) {
		this.parks = new ArrayList<LightweightPark>();
	}
	
	/** Get the actual map maintained by the map maker, displaying park names and locations, zoomed and centered on the parks.
	 * 
	 * @return the map of parks, complete with names and locations of the parks that have been assigned.
	 */
	public MapWidget getParkMap() {
		zoomAndCenter();
		return parkMap;
	}
	
	/** Add a park to the map.
	 * 
	 * @param park the park to be added to the map.
	 */
	public void addPark(LightweightPark park) {
		parks.add(park);
	}
	
	/** Remove a park from the map.
	 * 
	 * @param park the park to be removed from the map.
	 */
	public void removePark(LightweightPark park) {
		parks.remove(park);
	}
	
	
	/** Zooms and centers the map on the list of parks.
	 * 
	 */
	private void zoomAndCenter() {
		
	}
	
	/** Finds the smallest zoom level at which all parks in Vancouver appear.
	 * 
	 * @return the smallest zoom level at which all parks in Vancover appear.
	 */
	private int findZoomLevel() {
		LatLngBounds bounds = LatLngBounds.newInstance(vancouverCenter, vancouverCenter);
		
		for(LightweightPark park : parks) {
			LatLng parkLocation = park.getLocation();
			
			// If this point is in Vancouver but lies outside of the bounds so far, extend those bounds.
			if(parkInBounds(park) && !bounds.containsLatLng(parkLocation)) {
				bounds.extend(parkLocation);
			}
		}
		
		return parkMap.getBoundsZoomLevel(bounds);
	}
	
	/** Determine whether or not the given park is in the bounds of Vancouver.
	 * 
	 * @param park the park that's location will be evaluated.
	 * @return true if the park is in the bounds of Vancouver.
	 */
	private boolean parkInBounds(LightweightPark park) {
		return true;	// Need to implement.
	}
	
}
