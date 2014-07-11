/** A table consisting of a series of categorized lists.
 * 
 */

package cs310.creativeteamname.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FilterTable {
	
	final int MAX_ITEMS_PER_COLUMN = 25;
	final int FILTER_LIST_ROW = 0;
	
	int onCol;
	int featuresInCurrentCol;
	
	FlexTable filterTable;
	VerticalPanel colContents;
	
	/** Create a new filter table.
	 */
	public FilterTable() {
		filterTable = new FlexTable();
		filterTable.addStyleName("filtertable");
		filterTable.setCellPadding(20);

		colContents = new VerticalPanel();
		onCol = 0;
	}
	
	/** Add a feature list to a cell of this table.
	 * 
	 * @param featureList the feature list to add.
	 */
	public void addFeatureList(FeatureList featureList) {
		if(setNextCol(featureList)) {
			filterTable.setWidget(FILTER_LIST_ROW, onCol - 1, colContents);
			colContents = new VerticalPanel();
			colContents.addStyleName("colcontents");
		}
		
		VerticalPanel featureListWidget = featureList.getWidget();
		colContents.add(featureListWidget);
	}
	
	/** Get the widget of this filter table.
	 * 
	 * @return the FlexTable to display for this filter table.
	 */
	public FlexTable getWidget() {
		filterTable.setWidget(FILTER_LIST_ROW, onCol, colContents);
		return filterTable;
	}
	
	/** Set the next column to add a feature list to.
	 * 
	 * @param featureList the list that will be added next.
	 * @return true if starting a new column.
	 */
	private boolean setNextCol(FeatureList featureList) {
		if(shouldStartNewColumn(featureList)) {
			featuresInCurrentCol = featureList.getNumFeatures();
			onCol++;
			return true;
		}
		else {
			featuresInCurrentCol += featureList.getNumFeatures();
			return false;
		}
	}
	
	/** Determine whether the introduction of a new feature list will require starting a new column.
	 * 
	 * @param nextFeatureList the feature list to be added next.
	 * @return true if adding the given feature list to the current column would overflow the column limit.
	 */
	private boolean shouldStartNewColumn(FeatureList nextFeatureList) {
		int numFeatures = nextFeatureList.getNumFeatures();
		return (featuresInCurrentCol + numFeatures > MAX_ITEMS_PER_COLUMN);
	}
}
