/** A singleton filter for the parks.
 * 
 */

package cs310.creativeteamname.client;

import cs310.creativeteamname.shared.Park;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ParkFilter {
	
	Set<Park> allParks;
	Set<Park> filteredParks;
	List<String> filteringBy;
	private static ParkFilter instance;
	
	/** Create a new park filter with a given set of parks.
	 * 
	 */
	private ParkFilter(Set<Park> parks) {
		this.allParks = new TreeSet<Park>(parks);
		this.filteredParks = new TreeSet<Park>(parks);
		this.filteringBy = new ArrayList<String>();
	}
	
	public static ParkFilter getInstance(Set<Park> parks) {
		if(instance == null)
			instance = new ParkFilter(parks);
		return instance;
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
		filteringBy.clear();
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
		filteringBy.add(feature);
		Set<Park> parksTemp = new TreeSet<Park>();
		
		for(Park park : filteredParks)
			if(hasFeature(park, feature))
				parksTemp.add(park);
		
		filteredParks = parksTemp;
		return filteredParks.size();
	}
	
	/** Undo the result of filtering out a particular feature.
	 * 
	 * @param feature the feature to undo filtering by.
	 * @return the number of resulting parks.
	 */
	public int undoFilterBy(String feature) {
		filteringBy.remove(feature);
		Set<Park> parksTemp = new TreeSet<Park>(filteredParks);
		
		for(Park park : allParks)
			if(hasFeatures(park, filteringBy))
				parksTemp.add(park);

		filteredParks = new TreeSet<Park>(parksTemp);
		return filteredParks.size();
	}
	
	/** Determine whether or not a given park has all the given features.
	 * 
	 * @param park the park to check.
	 * @param feature the features to determine if the park has.
	 * @return true if the park has the given features.
	 */
	private boolean hasFeatures(Park park, List<String> features) {
		for(String feature : features)
			if(!hasFeature(park, feature))
				return false;
		
		return true;
	}
	
	/** Determine whether or not a given park has a given feature.
	 * 
	 * @param park the park to check.
	 * @param feature the feature to determine if the park has.
	 * @return true if the park has the given feature.
	 */
	private boolean hasFeature(Park park, String feature) {
		if(feature.equals("Washrooms"))
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
