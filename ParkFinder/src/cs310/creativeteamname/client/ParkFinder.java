package cs310.creativeteamname.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
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
import cs310.creativeteamname.shared.LoginInfo;
import cs310.creativeteamname.shared.Park;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ParkFinder implements EntryPoint {
	//Dan's button
	private Button refreshDataButton = new Button("Refresh data set");
	private Button retrieveDataButton = new Button("Refresh map");
	
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
	private Button showAllCommentsButton = new Button("Show all comments");
		
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
	
	private Set<LightweightPark> parksOnMap;
	private ParkMap parkMap;
	private Label mapFailedToLoadText = new Label("The map failed to load!");
	
	private LoginInfo loginInfo = new LoginInfo();
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Please sign in to your Google account.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");

	/**
	 * This is the entry point method.
	 */	
	public void onModuleLoad() {
		// Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
						handleError(error);
					}

					public void onSuccess(LoginInfo result) {
						loginInfo = result;
						if (loginInfo.isLoggedIn()) {
							loadParkFinder();
						} else {
							loadLogin();
						}
					}
				});
	}
	
	/**
	 * Load the login panel.
	 * 
	 */
	private void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel = new VerticalPanel();
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("login").add(loginPanel);
	}
	
	/**
	 * Load the park finder application.
	 * 
	 */
	private void loadParkFinder() {
		// Set up sign out hyperlink.
		RootPanel.get("login").clear();
		signOutLink.setHref(loginInfo.getLogoutUrl());
		loginPanel = new VerticalPanel();
		loginPanel.add(signOutLink);
		RootPanel.get("login").add(loginPanel);
		
		// Call location service.
		locationService.getAllParks(new AsyncCallback<HashMap<Integer, Park>>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(HashMap<Integer, Park> result) {
				allParks = result;
				parksOnMap = getLightParksFromHeavy(result);
				loadMap(parksOnMap);
			}
		});
	}
	
	/** 
	 * Load the park details panel.
	 * 
	 * @param parkId the park's id.
	 */
	public void loadDetailsPanel(final int parkId) {
		RootPanel.get("admin").clear();
		RootPanel.get("comments").clear();
		detailsPanel = new VerticalPanel();
		addNewCommentButton = new Button("Add your comment");
		backToMapButton = new Button("Back to map");
		
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
		List<Comment> comments = new LinkedList<Comment>();
		Logger logger = Logger.getLogger("logger");
		
		getComments(parkId, false);
		logger.severe("and trying to print " + comments.size() + " comments");		

		// Assemble details panel.
		detailsPanel.add(detailsLabel);
		detailsPanel.add(detailsImage);
		detailsPanel.add(detailsFlexTable);
		detailsPanel.add(backToMapButton);
		detailsPanel.add(commentsLabel);
		detailsPanel.add(noCommentsLabel);
		detailsPanel.add(commentFlexTable);
		detailsPanel.add(addNewCommentButton);
		detailsPanel.add(showAllCommentsButton);		

		// Associate details panel with HTML page.
		RootPanel.get("parkfinder").add(detailsPanel);

		// Add styles to elements in the panel.
		detailsPanel.setStyleName("detailsPanel");
		detailsLabel.addStyleName("detailsLabel");
		detailsFlexTable.getColumnFormatter().addStyleName(0, "detailsColumns");
		detailsFlexTable.addStyleName("detailsTable");
		commentsLabel.addStyleName("detailsLabel");
		mapFailedToLoadText.addStyleName("mapLoadFailLabel");
		
		// Listen for mouse events on the Back button.
		backToMapButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("parkfinder").clear();
				reloadMap(parksOnMap);
			}
		});

		// Listen for mouse events on the Add button.
		addNewCommentButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("parkfinder").clear();
				loadCommentPanel(parkId);
			}
		});		
	
		// List for mouse events on the showAllCommentsButton button.
		showAllCommentsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				commentService.getComment(parkId, new AsyncCallback<Comment[]>() {
					@Override
					public void onFailure(Throwable caught) {
						handleError(caught);
					}

					@Override
					public void onSuccess(Comment[] result) {
						getComments(parkId, true);
					}

				});
			}
		});
	}

	/**
	 * Add button that refreshes the data set.
	 * 
	 */
	private void addRefreshButton() {
		refreshDataButton = new Button("Refresh data set");
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
	 * Add button that reloads the map.
	 * 
	 */
	private void addReloadButton() {
		retrieveDataButton = new Button("Refresh map");
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
						parksOnMap = getLightParksFromHeavy(result);
						loadMap(parksOnMap);
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

	private void getComments(int parkId, final boolean displayAll) {
		commentService.getComment(parkId, new AsyncCallback<Comment[]>() {
			@Override
			public void onFailure(Throwable caught) {
				handleError(caught);
				
			}

			@Override
			public void onSuccess(Comment[] result) {
				Logger logger = Logger.getLogger("logger");
				logger.severe("found " + result.length + " comments");
				
				//returnAllResult(result);
				if(displayAll){
					displayAllComments(result);
				}else{
					displayThreeComments(result);
				}
				
			}
		});
		
	}
		
			
	private void displayAllComments(Comment[] comments) {
		commentFlexTable.removeAllRows();
		int i = 0;
		if(comments.length != 0) {
			noCommentsLabel.setVisible(false);
			for(Comment comment : comments) {
				commentFlexTable.setText(i, 1, comment.getUser() + " says: " + comment.getInput());
				i++;
			}
		} else {
			noCommentsLabel.setVisible(true);
			
		}
	}
	
	private void displayThreeComments(Comment[] comments) {
		commentFlexTable.removeAllRows();
		int i = 0;
		if (comments.length != 0) {
			noCommentsLabel.setVisible(false);
			for (Comment comment : comments) {
				if (i < 3) {
					commentFlexTable.setText(i, 1, comment.getUser() + " says: " + comment.getInput());
				}
				i++;
			}
		} else
			noCommentsLabel.setVisible(true);
		
	}

	private void loadCommentPanel(final int parkId) {
		// Move cursor focus to the input box
		commentInputArea = new TextArea();
		commentInputArea.setFocus(true);
		// Limited the number of characters in text
		// Reference used:
		// http://stackoverflow.com/questions/6980908/maxlength-for-gwt-textarea
		commentInputArea.getElement().setAttribute("maxlength", "250");
		
		// Assemble comment panel
		commentPanel = new VerticalPanel();
		commentPanel.setSpacing(10);
		commentPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		commentPanel.setSize("500", "500");
		commentPanel.add(commentHereLabel);
		commentPanel.add(commentInputArea);
		
		//Buttons
		submitButton = new Button("Submit");
		cancelButton = new Button("Cancel");
		commentPanel.add(cancelButton);
		commentPanel.add(submitButton);
		
		RootPanel.get("comments").clear();
		RootPanel.get("comments").add(commentPanel);

		// Listen for mouse events on the submit button
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addComment(parkId);
				commentInputArea.setText("");
				loadDetailsPanel(parkId);
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
		comment.setUser(loginInfo.getNickname());
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
	

	/** Convert a hash map of Parks to a set of LightweightParks.
	 * 
	 * @param heavyParks the set of Parks
	 * @return the set of converted lightweight parks
	 */
	private Set<LightweightPark> getLightParksFromHeavy(HashMap<Integer, Park> heavyParks) {
		Set<LightweightPark> lightParks = new TreeSet<LightweightPark>();
		
		for(Park heavyPark : heavyParks.values()) {
			LightweightPark lightPark = new LightweightPark(heavyPark);
			lightParks.add(lightPark);
		}
		
		return lightParks;
	}
	
	/** Load the map with a given list of parks.
	 * 
	 * @param parks the parks that will be displayed on the map.
	 */
	private void loadMap(Set<LightweightPark> parks) {
		loadAdminButtons();
		parkMap = new ParkMap();
		parkMap.setParks(parks);
		
		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
		
		try {
			MapWidget mapWidget = parkMap.getWidget(this);
			dock.addNorth(mapWidget, 500);
			RootPanel.get("parkfinder").add(dock);
			parkMap.zoomAndCenter(700, 500);
		}
		catch(Exception e) {
			RootPanel.get("parkfinder").add(mapFailedToLoadText);
		}
	}
	
	/** Reload the (already initialized) map with a given list of parks.
	 * 
	 * @param parks the parks that will be displayed on the map.
	 */
	private void reloadMap(Set<LightweightPark> parks) {
		loadAdminButtons();
		parkMap.setParks(parks);
		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
		
		try {
			MapWidget mapWidget = parkMap.getWidget(this);
			dock.addNorth(mapWidget, 500);
			RootPanel.get("parkfinder").add(dock);
			
			parkMap.zoomAndCenter(700, 500);
		}
		catch(Exception e) {
			RootPanel.get("parkfinder").add(mapFailedToLoadText);
		}
	}
	
	/**
	 * Load the buttons in the admin div.
	 * 
	 */
	private void loadAdminButtons() {
		RootPanel.get("admin").clear();
		if(loginInfo.isAdmin()) {
			addRefreshButton();
		}
		addReloadButton();
	}
	
}

