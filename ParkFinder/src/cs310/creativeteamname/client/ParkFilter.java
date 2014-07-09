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
		
		
		System.out.println("Filtering by some features: " + features[0] + " ... ");
		printFilteredParks();
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
		System.out.println("Filtering by " + feature);
		printFilteredParks();
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
		
		System.out.println("Filtering by " + feature);
		printFilteredParks();
		return filteredParks.size();
	}
	
	/** Remove all filters.
	 * 
	 */
	public void removeAllFilters() {
		filteredParks = new TreeSet<Park>(allParks);
	}
	
	/** Determine whether or not a particular filter is being applied.
	 * 
	 * @param potentialFilter the filter to check for.
	 * @return true if the given filter is being applied currently.
	 */
	public boolean beingFiltered(String potentialFilter) {
		return filteringBy.contains(potentialFilter);
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
		
		Set<String> parkFeatures = getFeaturesOfPark(park);
		return parkFeatures.contains(feature);
	}
	
	/** Get a list of all features for a given park.
	 * 
	 * @param park the park to find the features of.
	 * @return all features of the park.
	 */
	private Set<String> getFeaturesOfPark(Park park) {
		Set<String> parkFeatures = new TreeSet<String>();
		
		if(park.isWashroom())
			parkFeatures.add("Washrooms");
		
		for(String facility : park.getFacilities())
			parkFeatures.add(facility);
		
		for(String specialFeature : park.getSpecialFeatures())
			parkFeatures.add(specialFeature);
		
		return parkFeatures;
	}
	
	// Method for testing.
	private void printFilteredParks() {
		System.out.println("Filtered parks (" + filteredParks.size() + " parks):");
		for(Park park : filteredParks) {
			System.out.print(" " + park.getName());
		}
		System.out.println("\n\nFiltering by (" + filteringBy.size() + " filters):");
		for(String fb : filteringBy) {
			System.out.print(" " + fb);
		}
		System.out.println();
	}
}
