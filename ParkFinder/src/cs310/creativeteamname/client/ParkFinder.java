package cs310.creativeteamname.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

import cs310.creativeteamname.shared.Comment;
import cs310.creativeteamname.shared.Park;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ParkFinder implements EntryPoint {
	//Dan's button
	private Button refreshDataButton = new Button("Refresh data set");
	private Button retrieveDataButton = new Button("Retrieve data set");
	
	private HashMap<Integer, Park> allParks = new HashMap<Integer, Park>(); 
	
	private VerticalPanel detailsPanel = new VerticalPanel();
	private Label detailsLabel = new Label("Park Details");
	private Image detailsImage = new Image();
	private FlexTable detailsFlexTable = new FlexTable();
	private Button backToMapButton = new Button("Back to map");
	private Label commentsLabel = new Label("Comments");
	private Label noCommentsLabel = new Label("No comments for this park.");
	private FlexTable commentFlexTable = new FlexTable();
	private Button addNewCommentButton = new Button("Add your comment");
	private DetailsServiceAsync detailsService = GWT.create(DetailsService.class);	

	private VerticalPanel commentPanel = new VerticalPanel();
	private TextArea commentInputArea = new TextArea();
	private Label commentHereLabel = new Label(
			"Please add your comment in the textbox below (maximum characters allowed: 250)");
	private Button submitButton = new Button("Submit");
	private Button cancelButton = new Button("Cancel");
	private final CommentServiceAsync commentService = GWT
			.create(CommentService.class);
	private final DataVancouverServiceAsync dataVancouverService = GWT.create(DataVancouverService.class);
	private final LocationServiceAsync locationService = GWT.create(LocationService.class);
	private static final int CHARACTER_LIMIT = 250;
	private static final String VANCOUVER_URL = "http://vancouver.ca";

	/**
	 * This is the entry point method.
	 */	
	public void onModuleLoad() {
		addRefreshButton();
		addRetrieveButton();
	}
	
	/** 
	 * Load the park details panel.
	 * 
	 * @param parkId the park's id.
	 */
	public void loadDetailsPanel(final int parkId) {
		// Create table for park details.
		detailsFlexTable.setText(0, 0, "Park name:");
		detailsFlexTable.setText(1, 0, "Street address:");
		detailsFlexTable.setText(2, 0, "Neighbourhood:");
		detailsFlexTable.setText(3, 0, "Neighbourhood URL:");
		detailsFlexTable.setText(4, 0, "Facilities:");
		detailsFlexTable.setText(5, 0, "Special features:");
		detailsFlexTable.setText(6, 0, "Washrooms:");
		
		Park park = allParks.get(parkId);
		displayDetails(park);
		getComments(parkId);

		// Assemble details panel.
		detailsPanel.add(detailsLabel);
		detailsPanel.add(detailsImage);
		detailsPanel.add(detailsFlexTable);
		detailsPanel.add(backToMapButton);
		detailsPanel.add(commentsLabel);
		detailsPanel.add(noCommentsLabel);
		detailsPanel.add(commentFlexTable);
		detailsPanel.add(addNewCommentButton);

		// Associate details panel with HTML page.
		RootPanel.get("parkfinder").add(detailsPanel);

		// Add styles to elements in the panel.
		detailsPanel.setStyleName("detailsPanel");
		detailsLabel.addStyleName("detailsLabel");
		detailsFlexTable.getColumnFormatter().addStyleName(0, "detailsColumns");
		detailsFlexTable.addStyleName("detailsTable");
		commentsLabel.addStyleName("detailsLabel");
		
		// Listen for mouse events on the Back button.
		backToMapButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("parkfinder").clear();
				// TODO load map panel
			}
		});

		// Listen for mouse events on the Add button.
		addNewCommentButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("parkfinder").clear();
				loadCommentPanel(parkId);
			}
		});
	}

	private void addRefreshButton() {
		refreshDataButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dataVancouverService.refreshData(new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						Logger logger = Logger.getLogger("");
						logger.severe("Could not load data set");
						Window.alert("Could not load data set :(");
					}

					@Override
					public void onSuccess(Void result) {
						Logger logger = Logger.getLogger("");
						logger.severe("Successfully loaded data set - probably");
						Window.alert("Successfully loaded data set - yay!");;
					}
				});
			}
		});
		RootPanel.get("admin").add(refreshDataButton);
	}
	/**
	 * For test purposes only!  Author: DJMCCOOL (Dan)
	 */
	private void addRetrieveButton() {
		retrieveDataButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				locationService.getAllParks(new AsyncCallback<HashMap<Integer, Park>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(HashMap<Integer, Park> result) {
						allParks = result;
						Set<LightweightPark> parks = getLightParksFromHeavy(result);
						loadMap(parks);
					}
				});
			}
		});
		RootPanel.get("admin").add(retrieveDataButton);
	}
	
	/** 
	 * Get park details from the server.
	 * 
	 * @param parkId the park's id.
	 */
	private void getParkDetails(int parkId) {
		class DetailCallBack implements AsyncCallback<Park> {
			@Override
			public void onFailure(Throwable error) {
				handleError(error);
			}

			@Override
			public void onSuccess(Park result) {
				displayDetails(result);
			}
		}

		detailsService.getParkDetails(parkId, new DetailCallBack());
	}
	
	/**
	 * Display a park's details in the details panel.
	 * 
	 * @param park the park.
	 */
	private void displayDetails(Park park) {
		if(park.getImageUrl() == null) {
			detailsImage.setUrl("images/no_image_available.png");
		} else detailsImage.setUrl(VANCOUVER_URL + park.getImageUrl());
		
		detailsFlexTable.setText(0, 1, park.getName());
		
		String streetAddress = park.getStreetNumber() + " " + park.getStreetName();		
		detailsFlexTable.setText(1, 1, streetAddress);
		
		detailsFlexTable.setText(2, 1, park.getNeighbourhoodName());		
		detailsFlexTable.setText(3, 1, park.getNeighbourhoodURL());
		
		if(park.getFacilities().length==0) {
			detailsFlexTable.setText(4, 1, "N/A");
		} else detailsFlexTable.setText(4, 1, convertArrayToString(park.getFacilities()));
		
		if(park.getSpecialFeatures().length==0) {
			detailsFlexTable.setText(5, 1, "N/A");
		} else detailsFlexTable.setText(5, 1, convertArrayToString(park.getSpecialFeatures()));
		
		detailsFlexTable.setText(6, 1, convertBooleanToStr(park.isWashroom()));
	}
	
	/** 
	 * Convert an array of strings to a delimited string.
	 * 
	 * @param strings an array of strings.
	 * @return a delimited string.
	 * 
	 * @see http://stackoverflow.com/questions/6244823/convert-liststring-to-delimited-string
	 */
	private String convertArrayToString(String[] strings) {
		StringBuilder sb = new StringBuilder();
		for(String s : strings) {
			sb.append(s).append(", ");
		}
		sb.deleteCharAt(sb.length()-2); // delete last space and comma
		return sb.toString();
	}
	
	/**
	 * Convert a boolean to a yes/no string.
	 * 
	 * @param bool a boolean value.
	 * @return 'yes' string if bool is true, 'no' string if bool is false.
	 */
	private String convertBooleanToStr(boolean bool) {
		String str = "no";
		if(bool) {
			str = "yes";
		}
		return str;
	}

	private void getComments(int parkId) {
		commentService.getComment(parkId, new AsyncCallback<Comment[]>() {
			@Override
			public void onFailure(Throwable caught) {
				handleError(caught);				
			}

			@Override
			public void onSuccess(Comment[] result) {
				displayComments(result);
				
			}
		});
	}
	
	private void displayComments(Comment[] comments) {
		commentFlexTable.removeAllRows();
		int i = 0;
		if(comments.length != 0) {
			noCommentsLabel.setVisible(false);
			for(Comment comment : comments) {
				commentFlexTable.setText(i, 1, comment.getInput());
				i++;
			}
		} else {
			noCommentsLabel.setVisible(true);
		}
	}

	private void loadCommentPanel(final int parkId) {
		// Move cursor focus to the input box
		commentInputArea.setFocus(true);
		// Limited the number of characters in text
		// Reference used:
		// http://stackoverflow.com/questions/6980908/maxlength-for-gwt-textarea
		commentInputArea.getElement().setAttribute("maxlength", "250");
		
		// Assemble comment panel
		commentPanel.setSpacing(10);
		commentPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		commentPanel.setSize("500", "500");
		commentPanel.add(commentHereLabel);
		commentPanel.add(commentInputArea);
		commentPanel.add(submitButton);
		commentPanel.add(cancelButton);
		RootPanel.get("parkfinder").add(commentPanel);

		// Listen for mouse events on the submit button
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addComment(parkId);
			}
		});

		// Listen for mouse events on the cancel button
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("parkfinder").clear();
				loadDetailsPanel(parkId);
			}
		});

		// Listen for keyboard events in the input box.
		commentInputArea.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					addComment(parkId);
				}
			}
		});
	}

	private void addComment(int parkId) {
		final String input = commentInputArea.getText().trim();
		Comment comment = new Comment();
		comment.setInput(input);
		comment.setParkId(parkId);

		if((!input.isEmpty()) && (input.length() < CHARACTER_LIMIT)) {
			addComment(comment, parkId);
		} else Window.alert("Please input a comment.");
	}
	
	private void addComment(Comment comment, final int parkId) {
		commentService.addComment(comment, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable error) {
				handleError(error);
			}
			
			@Override
			public void onSuccess(Void ignore) {
				RootPanel.get("parkfinder").clear();
				loadDetailsPanel(parkId);
			}
		});

	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
	}	
	
	private Set<LightweightPark> getLightParksFromHeavy(HashMap<Integer, Park> heavyParks) {
		Set<LightweightPark> lightParks = new TreeSet<LightweightPark>();
		
		for(Park heavyPark : heavyParks.values()) {
			LightweightPark lightPark = new LightweightPark(heavyPark);
			lightParks.add(lightPark);
		}
		
		return lightParks;
	}
	
	private void loadMap(Set<LightweightPark> parks) {
		ParkMap map = new ParkMap();
		map.setParks(parks);
		
		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
		dock.addNorth(map.getWidget(this), 500);
		RootPanel.get("maparea").add(dock);
		
		map.zoomAndCenter(600, 500);
	}

}
