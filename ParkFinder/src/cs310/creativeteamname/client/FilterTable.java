/** A table consisting of a series of categorized lists.
 * 
 */

package cs310.creativeteamname.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FilterTable {
	
	final int MAX_ITEMS_PER_COLUMN = 16;
	
	int onRow;
	int onCol;
	int featuresInCurrentCol;
	
	FlexTable filterTable;
	
	/** Create a new filter table.
	 */
	public FilterTable() {
		filterTable = new FlexTable();
		filterTable.setCellPadding(6);
		onRow = 0;
		onCol = 0;
	}
	
	/** Add a feature list to a cell of this table.
	 * 
	 * @param featureList the feature list to add.
	 */
	public void addFeatureList(FeatureList featureList) {
		setNextRowAndCol(featureList);
		
		VerticalPanel featureListWidget = featureList.getWidget();
		filterTable.setWidget(onRow, onCol, featureListWidget);
	}
	
	/** Get the widget of this filter table.
	 * 
	 * @return the FlexTable to display for this filter table.
	 */
	public FlexTable getWidget() {
		return filterTable;
	}
	
	/** Set the next row and column to add a feature list to.
	 * 
	 * @param featureList the list that will be added next.
	 */
	private void setNextRowAndCol(FeatureList featureList) {
		if(shouldStartNewColumn(featureList)) {
			onRow = 0;
			onCol++;
			featuresInCurrentCol = featureList.getNumFeatures();
		}
		else {
			if(!onFirstCell())
				onRow++;
			featuresInCurrentCol += featureList.getNumFeatures();
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
	
	/** Determine whether or not the next cell to fill is the first one.
	 * 
	 * @return true if the next cell to fill is the first one.
	 */
	private boolean onFirstCell() {
		return (onRow == 0 && onCol == 0);
	}
}
