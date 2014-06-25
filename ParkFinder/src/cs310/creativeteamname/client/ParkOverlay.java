/** An overlay for a park on the map.
 * 
 */
package cs310.creativeteamname.client;

import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.ui.RootPanel;

public class ParkOverlay {
	final String ICON_URL = "http://imgur.com/V8dOflq.png";
	
	ParkFinder parkFinder;
	Overlay parkMarker;
	
	/** Create a new overlay for the given park.
	 * 
	 * @param park the park that this overlay represents.
	 */
	public ParkOverlay(LightweightPark park, ParkFinder parkFinder) {
		this.parkFinder = parkFinder;
		MarkerOptions options = getMarkerOptions(park);
		parkMarker = new Marker(park.getLocation(), options);
		addClickHandler(park.getId());
	}
	
	/** Return the actual overlay this class represents.
	 * 
	 * @return the overlay
	 */
	public Overlay getOverlay() {
		return parkMarker;
	}
	
	/** Get the marker options for a park's icon.
	 * 
	 * @return the marker options for the park's icon.
	 */
	private MarkerOptions getMarkerOptions(LightweightPark park) {
		MarkerOptions options = MarkerOptions.newInstance();
		
		Icon icon = Icon.newInstance(ICON_URL);
		Size iconSize = Size.newInstance(15,16);
		icon.setIconSize(iconSize);
		options.setIcon(icon);
		options.setTitle(park.getName());
		
		return options;
	}
	
	/** Add a click handler to display details for this park.
	 * 
	 * @param id the park's id.
	 */
	private void addClickHandler(final int id) {
		((Marker) parkMarker).addMarkerClickHandler(new MarkerClickHandler() {
			public void onClick(MarkerClickEvent event) {
				RootPanel.get("parkfinder").clear();
				displayParkDetails(id);
			}
		});
	}
	
	
	/** Display the details for a particular park.
	 * 
	 * @param id the park's id
	 */
	private void displayParkDetails(int id) {
		this.parkFinder.loadDetailsPanel(id);
	}
}
