/** A label displaying the number of parks with the selected features.
 * 
 */
package cs310.creativeteamname.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class NParksLabel {
	
	final static String PRECEDING_TEXT = "Parks: ";

	int nParks;
	Label nParksLabel;
	private static NParksLabel instance;
	
	public static NParksLabel getInstance(int nParks) {
		if(instance == null) {
			instance = new NParksLabel(nParks);
		}
		return instance;
	}
	
	/** Create a new label.
	 * 
	 * @param nParks the number of parks to start with.
	 */
	private NParksLabel(int nParks) {
		this.nParks = nParks;
		String labelText = PRECEDING_TEXT + nParks;
		nParksLabel = new Label(labelText);
		nParksLabel.getElement().setId("nParksLabel");
		display();
	}
	
	/** Update the number of parks displayed by this label.
	 * 
	 * @param nParks the new number of parks for the label to display.
	 */
	public void updateText(int nParks) {
		this.nParks = nParks;
		nParksLabel.setText(PRECEDING_TEXT + nParks);
		display();
	}
	
	/** Remove the label.
	 * 
	 */
	public void removeLabel() {
		RootPanel.get("filterandview").remove(nParksLabel);
	}
	
	/** Display the nParks label.
	 * 
	 */
	public void display() {
		RootPanel.get("filterandview").add(nParksLabel);
	}
}
