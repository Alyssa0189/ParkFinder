/** A singleton filter for the parks.
 * 
 */

package cs310.creativeteamname.client;

import cs310.creativeteamname.shared.Park;

import java.util.Set;
import java.util.TreeSet;

public class ParkFilter {
	
	Set<Park> allParks;
	Set<Park> filteredParks;
	Set<String> filteringBy;
	Set<String> filteringByNeighborhoods;
	Set<String> allNeighborhoods;
	NParksLabel nParksLabel;
	private static ParkFilter instance;
	
	/** Create a new park filter with a given set of parks.
	 * 
	 * @param parks all parks considered for filtering
	 * @param neighborhoods all neighborhoods being considered.
	 */
	private ParkFilter(Set<Park> parks, Set<String> neighborhoods) {
		this.allParks = new TreeSet<Park>(parks);
		this.filteredParks = new TreeSet<Park>(parks);
		this.filteringBy = new TreeSet<String>();
		this.filteringByNeighborhoods = new TreeSet<String>(neighborhoods);
		this.allNeighborhoods = new TreeSet<String>(neighborhoods);
		this.nParksLabel = NParksLabel.getInstance(filteredParks.size());
	}
	
	public static ParkFilter getInstance(Set<Park> parks, Set<String> neighborhoods) {
		if(instance == null)
			instance = new ParkFilter(parks, neighborhoods);
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
		filteringByNeighborhoods = new TreeSet<String>(allNeighborhoods);
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
	
	/** Filter the existing set of filtered parks by a particular feature.
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
		updateNParksLabel();
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
			if(passesBothFilters(park))
				parksTemp.add(park);

		filteredParks = new TreeSet<Park>(parksTemp);
		
		updateNParksLabel();
		return filteredParks.size();
	}
	
	/** Filter out parks from a particular neighborhood.
	 * 
	 * @param neighborhood the neighborhood to filter out by
	 */
	public void filterOutNeighborhood(String neighborhood) {
		filteringByNeighborhoods.remove(neighborhood);

		Set<Park> parksTemp = new TreeSet<Park>();
		
		for(Park park : filteredParks)
			if(!isInNeighborhood(park, neighborhood))
				parksTemp.add(park);
		
		filteredParks = new TreeSet<Park>(parksTemp);
		updateNParksLabel();
	}
	
	/** Filter in parks from a particular neighborhood.
	 * 
	 * @param neighborhood the neighborhood to filter in by
	 */
	public void filterInNeighborhood(String neighborhood) {
		filteringByNeighborhoods.add(neighborhood);
		
		Set<Park> parksTemp = new TreeSet<Park>();
		
		for(Park park : allParks) 
			if(passesBothFilters(park))
				parksTemp.add(park);
		
		filteredParks = new TreeSet<Park>(parksTemp);
		updateNParksLabel();
	}
	
	/** Determine whether or not a particular filter is being applied.
	 * 
	 * @param potentialFilter the filter to check for.
	 * @return true if the given filter is being applied currently.
	 */
	public boolean beingFiltered(String potentialFilter) {
		return filteringBy.contains(potentialFilter);
	}
	
	/** Determine whether or not a particular neighborhood filter is being applied.
	 * 
	 * @param potentialFilter the neighborhood
	 * @return true if the neighborhood is being filtered.
	 */
	public boolean neighborhoodBeingFiltered(String potentialFilter) {
		return filteringByNeighborhoods.contains(potentialFilter);
	}
	
	/** Update the number of parks label.
	 * 
	 */
	private void updateNParksLabel() {
		nParksLabel.updateText(filteredParks.size());
	}
	
	/** Determine whether or not a park passes through both feature and neighborhood filters.
	 * 
	 * @param park the park to test.
	 * @return true if the park has all the features specified in the filter and is in a filtered-in neighborhood.
	 */
	private boolean passesBothFilters(Park park) {
		return hasFeatures(park, filteringBy) && isInNeighborhoodFilter(park);
	}
	
	/** Determine whether or not a given park is in a particular neighborhood.
	 * 
	 * @param park the park to consider
	 * @param neighborhood the neighborhood to consider
	 * @return true if the given park is in the given neighborhood.
	 */
	private boolean isInNeighborhood(Park park, String neighborhood) {
		String parkNeighborhood = park.getNeighbourhoodName();
		return (parkNeighborhood.equals(neighborhood));
	}
	
	/** Determine whether or not a given park has all the given features.
	 * 
	 * @param park the park to check.
	 * @param feature the features to determine if the park has.
	 * @return true if the park has the given features.
	 */
	private boolean hasFeatures(Park park, Set<String> features) {
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

	/** Determine whether or not a particular park is in the neighborhood filter.
	 * 
	 * @param park the park to test.
	 * @return true if this park's neighborhood is in the list of neighborhood filters being applied.
	 */
	private boolean isInNeighborhoodFilter(Park park) {
		String parkNeighborhood = park.getNeighbourhoodName();
		return filteringByNeighborhoods.contains(parkNeighborhood);
	}
}
