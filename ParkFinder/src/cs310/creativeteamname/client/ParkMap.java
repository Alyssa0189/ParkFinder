/* A map of parks in Vancouver.
 * 
 */

package cs310.creativeteamname.client;

import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class ParkMap {

	private MapWidget map;
	private Set<LightweightPark> parks;
	private MapZoomer zoomer;
	private VancouverBounds vancouverBounds;
	
	/** Create a park map without any parks.
	 * 
	 */
	protected ParkMap() {
		map = new MapWidget();
		map.setSize("100%", "100%");
		map.addControl(new LargeMapControl());
		
		parks = new TreeSet<LightweightPark>();
		zoomer = new MapZoomer(map);
		vancouverBounds = VancouverBounds.getInstance();
	}
	
	/** Declare the set of parks on this map.
	 * 
	 * @param parks the parks that will be displayed on the map.
	 */
	public void setParks(Set<LightweightPark> parks) {
		for(LightweightPark park : parks)
			addPark(park);
	}
	
	/** Get the widget representation of this park map.
	 * 
	 * @return the map of parks, complete with names and locations of the parks that have been assigned.
	 */
	public MapWidget getWidget() {
		addParkOverlays();
		return map;
	}
	
	/** Zoom and center to the parks on the map.
	 * (must be called after widget has been added to element).
	 * 
	 * @param width the width of the element containing the map
	 * @param height the height of the element containing the map
	 * 
	 */
	public void zoomAndCenter(int width, int height) {
		// Must be done, or zoom level is computed incorrectly.
		String widthStr = width + "px";
		String heightStr = height + "px";
		map.setSize(widthStr, heightStr);
		
		zoomer.zoomAndCenter(parks);
	}
	
	/** Add a park to the map.
	 * 
	 * @param park the park to be added to the map.
	 */
	public void addPark(LightweightPark park) {
		if(vancouverBounds.containsPark(park)) {
			this.parks.add(park);
		}
	}
	
	/** Remove a park from the map.
	 * 
	 * @param park the park to be removed from the map.
	 */
	public void removePark(LightweightPark park) {
		parks.remove(park);
	}
	
	/** Add the overlays for the parks.
	 * 
	 */
	private void addParkOverlays() {
		MarkerOptions options = MarkerOptions.newInstance();
		
		for(LightweightPark park : parks) {
			options.setTitle(park.getName());
			map.addOverlay(new Marker(park.getLocation(), options));
		}
	}
}

