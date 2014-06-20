/* A map of parks in Vancouver.
 * 
 */

package cs310.creativeteamname.client;

import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler.MarkerClickEvent;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class ParkMap {

	private MapWidget map;
	private Set<LightweightPark> parks;
	private MapZoomer zoomer;
	private VancouverBounds vancouverBounds;
	private LightweightPark currentPark;
	ParkFinder parkFinder;
	
	/** Create a park map without any parks.
	 * 
	 */
	protected ParkMap() {
		map = new MapWidget();
		map.setSize("800px", "800px");
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
	 * @param parkFinder the park finder object.
	 * @return the map of parks, complete with names and locations of the parks that have been assigned.
	 */
	public MapWidget getWidget(ParkFinder parkFinder) {
		this.parkFinder = parkFinder;
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
		MarkerOptions options;
				
		for(LightweightPark park : parks) {
			options = getMarkerOptions(park);
			options.setTitle(park.getName());
			Marker parkMarker = new Marker(park.getLocation(), options);

			map.addOverlay(parkMarker);

			LightweightPark p1 = new LightweightPark(park);
			this.currentPark = p1;

			parkMarker.addMarkerClickHandler(new MarkerClickHandler() {
				LightweightPark p = currentPark;
				
				public void onClick(MarkerClickEvent event) {
					RootPanel.get("parkfinder").clear();
					displayParkDetails(p);
					map.savePosition();
				}
			});
		}
	}
	
	/** Display the details for a particular park.
	 * 
	 * @param id the id of the park to display.
	 */
	private void displayParkDetails(LightweightPark park) {
		this.parkFinder.loadDetailsPanel(park.getId());
	}
	
	/** Get the marker options for a park's icon.
	 * 
	 * @return the marker options for the park's icon.
	 */
	private MarkerOptions getMarkerOptions(LightweightPark park) {
		MarkerOptions options = MarkerOptions.newInstance();
		
		Icon icon = Icon.newInstance("http://imgur.com/V8dOflq.png");
		Size iconSize = Size.newInstance(15,16);
		icon.setIconSize(iconSize);
		options.setIcon(icon);
		options.setTitle(park.getName());
		
		return options;
	}
	
}
