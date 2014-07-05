/** Holds information about a feature options to display for the filter.
 * 
 */

package cs310.creativeteamname.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class FeatureOption implements Comparable {
	
	String featureName;
	CheckBox featureCheckbox;
	HorizontalPanel featurePanel;
	boolean isSelected;
	
	/** Create a new feature option with a given feature.
	 * 
	 * @param feature the feature's name.
	 */
	public FeatureOption(String featureName) {
		this.featureName = featureName;
		featureCheckbox = new CheckBox(featureName);
		featureCheckbox.setValue(false);
		featurePanel.add(featureCheckbox);
		addClickHandler();
	}

	/** Get the name of the feature.
	 * 
	 * @return the feature's name.
	 */
	public String getFeatureName() {
		return featureName;
	}
	
	/** Get the feature to display in a list.
	 * 
	 * @return the feature to display.
	 */
	public HorizontalPanel getFeatureToDisplay() {
		return featurePanel;
	}
	
	/** Add a click handler to the checkbox.
	 * (Followed example from http://www.gwtproject.org/javadoc/latest/com/google/gwt/user/client/ui/CheckBox.html)
	 * 
	 */
	private void addClickHandler() {
		featureCheckbox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean checked = ((CheckBox) event.getSource()).getValue();
				setChecked(checked);
			}
		});
	}
	
	/** Set the checked status for this feature.
	 * 
	 * @param checked whether or not the box is checked.
	 */
	private void setChecked(boolean checked) {
		this.isSelected = checked;
	}
	
	@Override
	public int compareTo(Object other) {
		FeatureOption otherFeature = (FeatureOption)other;
		return featureName.compareTo(otherFeature.getFeatureName());
	}
	
	@Override
	public boolean equals(Object other) {
		FeatureOption otherFeature = (FeatureOption)other;
		return featureName.equals(otherFeature.getFeatureName());
	}
	
	@Override
	public int hashCode() {
		return featureName.hashCode();
	}
}

