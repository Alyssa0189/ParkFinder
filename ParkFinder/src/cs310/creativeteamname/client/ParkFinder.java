package cs310.creativeteamname.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLng;
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
	private Button refreshDataButton = new Button("Refresh Data Set");
	private Button retrieveDataButton = new Button("Test");
	
	// testing details
	private Button loadParkButton = new Button("Load park details");
	private HashMap<Integer, Park> allParks = new HashMap<Integer, Park>(); 
	
	private VerticalPanel detailsPanel = new VerticalPanel();
	private Label detailsLabel = new Label("Park Details");
	private Image detailsImage = new Image();
	private FlexTable detailsFlexTable = new FlexTable();
	private Button backToMapButton = new Button("Back to map");
	private Label commentsLabel = new Label("Comments");
	private Label noCommentsLabel = new Label();
	private FlexTable commentFlexTable = new FlexTable();
	private Button addNewCommentButton = new Button("Add your comment");
	private DetailsServiceAsync detailsService = GWT.create(DetailsService.class);	

	private VerticalPanel commentPanel = new VerticalPanel();
	private TextArea commentInputArea = new TextArea();
	private Label commentHereLabel = new Label(
			"Please add your comment in the textbox below (maximum characters allowed: 250)");
	private Button submitButton = new Button("Submit");
	private Button cancelButton = new Button("Cancel");
	private ArrayList<String> comments = new ArrayList<String>(); 
	private final CommentServiceAsync commentService = GWT
			.create(CommentService.class);
	private final DataVancouverServiceAsync dataVancouverService = GWT.create(DataVancouverService.class);
	private final LocationServiceAsync locationService = GWT.create(LocationService.class);
	private static final int CHARACTER_LIMIT=250;

	/**
	 * This is the entry point method.
	 */	
	public void onModuleLoad() {
		addRefreshButton();
		addRetrieveButton();
		loadCommentPanel();
		addLoadButton();
	}
	
	// testing details
	private void addLoadButton() {
		RootPanel.get("test").add(loadParkButton);
		loadParkButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// TODO
			}
		});
	}
	
	/** 
	 * Load the park details panel.
	 * 
	 * @param parkId the park's id.
	 */
	private void loadDetailsPanel(String parkId) {
		// Create table for park details.
		detailsFlexTable.setText(0, 0, "Park name:");
		detailsFlexTable.setText(1, 0, "Street address:");
		detailsFlexTable.setText(2, 0, "Neighbourhood:");
		detailsFlexTable.setText(3, 0, "Neighbourhood URL:");
		detailsFlexTable.setText(4, 0, "Facilities:");
		detailsFlexTable.setText(5, 0, "Special features:");
		detailsFlexTable.setText(6, 0, "Washrooms:");
			
		getParkDetails(parkId);
		loadComment();

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
		addNewCommentButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("parkfinder").clear();
				// TODO load map panel
			}
		});

		// Listen for mouse events on the Add button.
		addNewCommentButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("parkfinder").clear();
				// TODO load comment panel
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
					}

					@Override
					public void onSuccess(Void result) {
						Logger logger = Logger.getLogger("");
						logger.severe("Successfully loaded data set - probably");
					}
				});
			}
		});
		RootPanel.get("parkfinder").add(refreshDataButton);
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
		RootPanel.get("parkfinder").add(retrieveDataButton);
	}
	
	/** 
	 * Get park details from the server.
	 * 
	 * @param parkId the park's id.
	 */
	private void getParkDetails(String parkId) {
		class DetailCallBack implements AsyncCallback<Park> {
			@Override
			public void onFailure(Throwable error) {
				handleError(error);
			}

			@Override
			public void onSuccess(Park result) {
				if (result.getImageUrl() == null) {
					detailsImage.setUrl("images/no_image_available.png");
				} else detailsImage.setUrl(result.getImageUrl());
				detailsFlexTable.setText(0, 1, result.getName());
				String streetAddress = result.getStreetNumber() + " " + result.getStreetName();
				detailsFlexTable.setText(1, 1, streetAddress);
				detailsFlexTable.setText(2, 1, result.getNeighbourhoodName());
				detailsFlexTable.setText(3, 1, result.getNeighbourhoodURL());
				detailsFlexTable.setText(4, 1, convertListToString(result.getFacilities()));
				detailsFlexTable.setText(5, 1, convertListToString(result.getSpecialFeatures()));
				detailsFlexTable.setText(6, 1, String.valueOf(result.isWashroom()));
			}
		}

		detailsService.getParkDetails(parkId, new DetailCallBack());
	}
	
	/** 
	 * Convert a list of strings to a delimited string.
	 * 
	 * @param strings a list of strings.
	 * @return a delimited string.
	 * 
	 * @see http://stackoverflow.com/questions/6244823/convert-liststring-to-delimited-string
	 */
	private String convertListToString(List<String> strings) {
		StringBuilder sb = new StringBuilder();
		for(String str: strings) {
		   sb.append(str).append(", ");
		}
		sb.deleteCharAt(sb.length()-2); // delete last space and comma
		String newString = sb.toString();
		return newString;
	}

	private void loadComment() {
		// TODO pass parkId as parameter
		commentService.getComment(1, new AsyncCallback<Comment[]>() {
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
		int i = 0;
		if(comments.length != 0) {
			for(Comment comment : comments) {
				commentFlexTable.setText(i, 1, comment.getInput());
				i++;
			}
		} else noCommentsLabel.setText("No comments for this park.");
	}

	private void loadCommentPanel() {
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
				// TODO load details panel
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

	private void addComment() {
		final String input = commentInputArea.getText().trim();
		Comment comment = new Comment();
		comment.setInput(input);
		// TODO pass parkId as parameter
		comment.setParkId(1);

		if((!input.isEmpty()) && (input.length() < CHARACTER_LIMIT)) {
			addComment(comment);
		} else Window.alert("Please input a comment.");
	}
	
	private void addComment(Comment comment) {
		commentService.addComment(comment, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable error) {
				handleError(error);
			}
			
			@Override
			public void onSuccess(Void ignore) {
				// TODO load details panel
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
		dock.addNorth(map.getWidget(), 500);
		RootPanel.get("maparea").add(dock);
		
		map.zoomAndCenter(600, 500);
	}

}
