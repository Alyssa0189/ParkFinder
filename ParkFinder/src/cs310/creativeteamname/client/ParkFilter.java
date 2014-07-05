/** A filter for the parks.
 * 
 */

package cs310.creativeteamname.client;

import cs310.creativeteamname.shared.Park;

import java.util.Set;
import java.util.TreeSet;

public class ParkFilter {
	
	Set<Park> allParks;
	Set<Park> filteredParks;
	
	/** Create a new park filter with a given set of parks.
	 * 
	 */
	public ParkFilter(Set<Park> parks) {
		this.allParks = new TreeSet<Park>(parks);
		this.filteredParks = new TreeSet<Park>(parks);
	}
	
	/** Retrieve the filtered set of parks.
	 * 
	 * @return the filtered set of parks.
	 */
	public Set<Park> getFilteredParks() {
		return filteredParks;
	}
	
	/** Reset the filter to contain all of the parks.
	 * 
	 */
	public void resetFilter() {
		if(filteredParks.size() < allParks.size())
			filteredParks = new TreeSet<Park>(allParks);
	}
	
	/** Filter the existing set of parks by features.
	 * 
	 * @param features the features to filter the set by.
	 * @return the number of resulting parks.
	 */
	public int filterBy(String[] features) {
		for(String feature : features)
			filterBy(feature);
		
		return filteredParks.size();
	}
	
	/** Filter the existing set of parks by a particular feature.
	 * 
	 * @param feature the feature to filter the set by.
	 * @return the number of resulting parks.
	 */
	public int filterBy(String feature) {
		for(Park park : filteredParks)
			if(!hasFeature(park, feature))
				filteredParks.remove(park);
		
		return filteredParks.size();
	}
	
	/** Determine whether or not a given park has a given feature.
	 * 
	 * @param park the park to check.
	 * @param feature the feature to determine if the park has.
	 * @return true if the park with the given id has the given feature.
	 */
	private boolean hasFeature(Park park, String feature) {
		if(feature.equals("washroom"))
			return true;
		
		String[] facilities = park.getFacilities();
		String[] specialFeatures = park.getSpecialFeatures();
		
		return contains(facilities, feature) || contains(specialFeatures, feature);
	}
	
	/** Determine whether or not a given feature is in a list of features.
	 * 
	 * @param features the list of features.
	 * @return true if the feature is in the list of features.
	 */
	private boolean contains(String[] features, String feature) {
		for(int i = 0; i < features.length; i++)
			if(features[i].equals(feature))
				return true;
		
		return false;
	}
}
