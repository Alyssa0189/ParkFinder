/** A filter for the parks.
 * 
 */

package cs310.creativeteamname.client;

import java.util.Set;
import java.util.TreeSet;

public class ParkFilter {
	
	Set<LightweightPark> parks;
	
	/** Create a new park filter with a given set of parks.
	 * 
	 */
	public ParkFilter(Set<LightweightPark> parks) {
		this.parks = new TreeSet<LightweightPark>(parks);
	}
	
	/** Retrieve the filtered set of parks.
	 * 
	 * @return the filtered set of parks.
	 */
	public Set<LightweightPark> getFilteredParks() {
		return parks;
	}
}
