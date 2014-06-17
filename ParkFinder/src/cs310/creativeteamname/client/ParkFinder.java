package cs310.creativeteamname.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ParkFinder implements EntryPoint {

	private VerticalPanel detailsPanel = new VerticalPanel();
	private VerticalPanel commentPanel = new VerticalPanel();
	private TextArea commentInputArea = new TextArea();
	private Label commentHereLabel = new Label(
			"Please add your comment in the textbox below (maximum character allowed: 250)");
	private Button addCommentButton = new Button("Add your comment");
	private Button submitButton = new Button("Submit");
	private Button cancelButton = new Button("Cancel");
	private ArrayList<String> comments = new ArrayList<String>(); 
	private final CommentServiceAsync commentService = GWT
			.create(CommentService.class);
	private FlexTable commentFlexTable = new FlexTable();

	private static final int CHARACTER_LIMIT=250;

	/**
	 * This is the entry point method.
	 */

	public void onModuleLoad() {

		

		// Assemble Main panel
		detailsPanel.add(addCommentButton);
		detailsPanel.add(commentFlexTable);
		RootPanel.get("detailPage").add(detailsPanel);

		// ToDoList
		// the comments from field should be displayed
		
		// when addComment button is pressed leads to comment page

		loadCommentPanel();
	// Map testing
		MapMaker maker = MapMaker.getInstance();
		MapWidget map = maker.getParkMap();	
		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
		dock.addNorth(map, 500);
		RootPanel.get("maptest").add(dock);

	private void loadComment() {
		commentService.getComment(new AsyncCallback<String[]>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(String[] inputs) {
				displayComments(inputs);
				
			}
		});
	}

	private void displayComments(String[] inputs) {
		// display comments on main page
		for (String input : inputs) {
			displayComment(input);
		}

	}

	private void loadCommentPanel() {
		// Move cursor focus to the input box
		commentInputArea.setFocus(true);
		// Limited the number of characters in text
		// Reference used:
		// http://stackoverflow.com/questions/6980908/maxlength-for-gwt-textarea
		commentInputArea.getElement().setAttribute("maxlength", "250");

		loadComment();
		
		//Assemble comment panel
		commentPanel.setSpacing(10);
		commentPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		commentPanel.setSize("500", "500");
		commentPanel.add(commentHereLabel);
		commentPanel.add(commentInputArea);
		commentPanel.add(submitButton);
		commentPanel.add(cancelButton);
		RootPanel.get("commentBox").add(commentPanel);

		// Listen for mouse events on the submit button
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addComment();
			}
		});

		// Listen for mouse events on the cancel button
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				goToDetailsPage();
			}
		});

		// Listen for keyboard events in the input box.
		commentInputArea.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					addComment();
				}
			}
		});

	}

	/*when submitComment button is pressed it stores comment in our db and
	displays it in details page*/
	private void addComment() {
		final String input = commentInputArea.getText().trim();
		commentInputArea.setFocus(true);

		try {
			if (input.isEmpty()) {
			}
		} catch (IllegalArgumentException e) {
			Window.alert("Please add a comment");
		}
		
		
		try {
			if (input.length() > CHARACTER_LIMIT) {

			}
		} catch (IllegalArgumentException e) {
			Window.alert ("You have exceeded 250 character limit");

		}
		if (comments.contains(input))
			return;
		addCommentToDb(input);

	}

	
	// adds comment to database
	private void addCommentToDb(final String input) {
		commentService.addComment(input, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(Void ignore) {
				// store comment in db and go to detail page where comment shows
				displayComment(input);
				// updateCommentTable(input);

			}
		});

	}

	private void displayComment(final String input) {
		// Add comments to table which is displayed on details page
		goToDetailsPage();
		
		int row = commentFlexTable.getRowCount();
		comments.add(input);
		commentFlexTable.setText(row, 0, input);
		// commentFlexTable.setWidget(row, 2, new Label());
		// CommentDetail[] input=new CommentDetail[comments.size()];

	}

	/*
	 * Update a single row in comment table
	 * 
	 * @param comment
	 */
	// NOTE: This method hasn't been called yet
	private void updateCommentTable(CommentDetail[] input) {
		CommentDetail[] inputs = new CommentDetail[comments.size()];
		for (int i = 0; i < comments.size(); i++) {
			updateCommentTable(input[i]);
		}

	}

	private void updateCommentTable(CommentDetail input) {
		int row = comments.indexOf(input.getInput()) + 1;

		// Populate the input field with new data
		commentFlexTable.setText(row, 1, input.getInput());
	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
	}

	private void goToDetailsPage() {
		// TODO Auto-generated method stub

	}

}
