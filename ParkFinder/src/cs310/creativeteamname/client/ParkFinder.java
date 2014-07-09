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
	// Buttons
	private Button refreshDataButton = new Button("Refresh data set");
	private Button retrieveDataButton = new Button("Refresh map");
	private Button backToMapButton = new Button("Back to map");
	private Button addNewCommentButton = new Button("Add your comment");
	private Button showAllCommentsButton = new Button("Show all comments");
	private Button filterButton = new Button("Filter parks");
	private Button submitCommentButton = new Button("Submit");
	private Button cancelCommentButton = new Button("Cancel");
	private Button parkListButton = new Button("View Park List");
	private Button goToDetailPage=new Button("View Park's Details");
	private Button mapButton = new Button("Back to map");
	
	private Image backFromFilterImage = new Image();

	private HashMap<Integer, Park> allParks = new HashMap<Integer, Park>();

	private VerticalPanel detailsPanel = new VerticalPanel();
	private Label detailsLabel = new Label("Park Details");
	private Image detailsImage = new Image();
	private FlexTable detailsFlexTable = new FlexTable();
	private Label commentsLabel = new Label("Comments");
	private Label noCommentsLabel = new Label("No comments for this park.");
	private FlexTable commentFlexTable = new FlexTable();
	private DetailsServiceAsync detailsService = GWT
			.create(DetailsService.class);
		
	private VerticalPanel commentPanel = new VerticalPanel();
	private TextArea commentInputArea = new TextArea();
	private Label commentHereLabel = new Label(
			"Please add your comment in the textbox below (maximum characters allowed: 250)");
	
	private final CommentServiceAsync commentService = GWT
			.create(CommentService.class);
	private final DataVancouverServiceAsync dataVancouverService = GWT
			.create(DataVancouverService.class);
	
	private final LocationServiceAsync locationService = GWT
			.create(LocationService.class);
	private static final int CHARACTER_LIMIT = 250;
	private static final String VANCOUVER_URL = "http://vancouver.ca";
	
	private ParkMap parkMap;
	private Label mapFailedToLoadText = new Label("The map failed to load!");;
	private FlexTable parkListFlexTable = new FlexTable();
	
	private LoginInfo loginInfo = new LoginInfo();
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Please sign in to your Google account.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	
	private boolean onMapView = true;
	private ParkFilter filter;
	
	private boolean canAddBackFromFilterButton = true;

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
	
	// The following 4 methods should do all the work for loading a page,
	// including getting rid of the previous pages.
	
	/** Load the entire map page.
	 * 
	 * @param reloadData true if the park data should be retrieved again.
	 * 
	 */
	private void loadMapPage(boolean reloadParkData) {

	
		onMapView = true;
		
		clearAllDivs();
		System.out.println("Loading map page!!!!!!!!!!!!!");
		loadFilterAndViewButtons(true);
		
		if(reloadParkData)
			loadMap();
		else
			reloadMap();

	}
	
	/** Load the entire list page.
	 * 
	 */
	private void loadListPage() {
		onMapView = false;
		
		clearAllDivs();
		loadFilterAndViewButtons(false);
		
		loadListPanel();
		displayParkList();
	}
	
	/** Load the entire details page for a park.
	 * 
	 * @param id the id of the park to load the details page.
	 * 
	 */
	private void loadDetailsPage(int id) {
		clearAllDivs();
		loadDetailsPanel(id);
	}
	
	/** Load the entire filter page.
	 * 
	 */
	private void loadFilterPage() {
		clearAllDivs();
		filter.removeAllFilters();
		backFromFilterImage.getElement().setId("backFromFilterImage");
		if(canAddBackFromFilterButton) {
			canAddBackFromFilterButton = false;
			RootPanel.get("filterandview").add(backFromFilterImage);
		}
		
		FlexTable filterTable = createFilterTable().getWidget();
		
		RootPanel.get("parkfinder").add(filterTable);
		
		backFromFilterImage.setUrl("images/backFromFilterImage.png");;
		
		// Listen for mouse events on the Back button.
		backFromFilterImage.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
					if(onMapView)
						loadMapPage(true);
					else
						loadListPage();
			}
		});
	}
	
	
	
	
	
	
	
	
	
	/** Load the filter and map/list view buttons.
	 * 
	 * @param onMapView true if on the map view, false if on the list view.
	 */
	private void loadFilterAndViewButtons(boolean onMapView) {

		if(onMapView)
			addParkListButton();
		else
			addMapViewButton();
		
		loadFilterButton();
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
						loadParkFilter(result);
						loadMap();
						loadMapPage(true);
					}
				});
		}

	/**
	 * Load the park details panel.
	 * 
	 * @param parkId
	 *            the park's id.
	 */
	public void loadDetailsPanel(final int parkId) {
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
				clearAllDivs();
				loadMapPage(false);
				}
		});

		// Listen for mouse events on the Add button.
		addNewCommentButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("parkfinder").clear();
				loadCommentPanel(parkId);
			}
		});		

		// Listen for mouse events on the showAllCommentsButton button.
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
						Window.alert("Successfully loaded data set - yay!");
						;
					}
				});
			}
		});
		RootPanel.get("admin").add(refreshDataButton);
	}
	
	/**
	 * Displays page with list of parks' names and their addresses
	 */
	private void addParkListButton() {
		RootPanel.get("filterandview").add(parkListButton);
		parkListButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				System.out.println("Clicked park list button detected!");
				loadListPage();
			}
		});
		
	}

	/**
	 * For test purposes only! Author: DJMCCOOL (Dan)
	 * 
	 */
	private void addReloadButton() {
		retrieveDataButton = new Button("Refresh map");
		retrieveDataButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				locationService
						.getAllParks(new AsyncCallback<HashMap<Integer, Park>>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
							}

							@Override
							public void onSuccess(HashMap<Integer, Park> result) {
								allParks = result;
								loadMap();
							}
						});
			}
		});
		RootPanel.get("admin").add(retrieveDataButton);
		
	}

	/**
	 * Get park details from the server.
	 * 
	 * @param parkId
	 *            the park's id.
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
	 * @param park
	 *            the park.
	 */
	private void displayDetails(Park park) {
		if (park.getImageUrl() == null) {
			detailsImage.setUrl("images/no_image_available.png");
		} else
			detailsImage.setUrl(VANCOUVER_URL + park.getImageUrl());

		detailsFlexTable.setText(0, 1, park.getName());

		String streetAddress = park.getStreetNumber() + " "
				+ park.getStreetName();
		detailsFlexTable.setText(1, 1, streetAddress);

		detailsFlexTable.setText(2, 1, park.getNeighbourhoodName());
		detailsFlexTable.setText(3, 1, park.getNeighbourhoodURL());

		if (park.getFacilities().length == 0) {
			detailsFlexTable.setText(4, 1, "N/A");
		} else
			detailsFlexTable.setText(4, 1,
					convertArrayToString(park.getFacilities()));

		if (park.getSpecialFeatures().length == 0) {
			detailsFlexTable.setText(5, 1, "N/A");
		} else
			detailsFlexTable.setText(5, 1,
					convertArrayToString(park.getSpecialFeatures()));

		detailsFlexTable.setText(6, 1, convertBooleanToStr(park.isWashroom()));
	}

	/**
	 * Convert an array of strings to a delimited string.
	 * 
	 * @param strings
	 *            an array of strings.
	 * @return a delimited string.
	 * 
	 * @see http
	 *      ://stackoverflow.com/questions/6244823/convert-liststring-to-delimited
	 *      -string
	 */
	private String convertArrayToString(String[] strings) {
		StringBuilder sb = new StringBuilder();
		for (String s : strings) {
			sb.append(s).append(", ");
		}
		sb.deleteCharAt(sb.length() - 2); // delete last space and comma
		return sb.toString();
	}

	/**
	 * Convert a boolean to a yes/no string.
	 * 
	 * @param bool
	 *            a boolean value.
	 * @return 'yes' string if bool is true, 'no' string if bool is false.
	 */
	private String convertBooleanToStr(boolean bool) {
		String str = "no";
		if (bool) {
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

				// returnAllResult(result);
				if (displayAll) {
					displayAllComments(result);
				} else {
					displayThreeComments(result);
				}

			}
		});

	}

	private void displayAllComments(Comment[] comments) {
		commentFlexTable.removeAllRows();
		int i = 0;
		if (comments.length != 0) {
			noCommentsLabel.setVisible(false);
			for (Comment comment : comments) {
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
		commentPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		commentPanel.setSize("500", "500");
		commentPanel.add(commentHereLabel);
		commentPanel.add(commentInputArea);

		// Buttons
		submitCommentButton = new Button("Submit");
		cancelCommentButton = new Button("Cancel");
		commentPanel.add(cancelCommentButton);
		commentPanel.add(submitCommentButton);

		RootPanel.get("comments").clear();
		RootPanel.get("comments").add(commentPanel);

		// Listen for mouse events on the submit button
		submitCommentButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addComment(parkId);
				commentInputArea.setText("");
				loadDetailsPage(parkId);
			}
		});

		// Listen for mouse events on the cancel button
		cancelCommentButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("parkfinder").clear();
				loadDetailsPage(parkId);
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

		if ((!input.isEmpty()) && (input.length() < CHARACTER_LIMIT)) {
			addComment(comment, parkId);
		} else
			Window.alert("Please input a comment.");
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
				loadDetailsPage(parkId);
			}
		});

	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
	}

	/**
	 * Convert a hash map of Parks to a set of LightweightParks.
	 * 
	 * @param heavyParks the set of Parks
	 * @return the set of converted lightweight parks
	 */
	private Set<LightweightPark> getLightParksFromHeavy(HashMap<Integer, Park> heavyParks) {
		Set<LightweightPark> lightParks = new TreeSet<LightweightPark>();

		for (Park heavyPark : heavyParks.values()) {
			LightweightPark lightPark = new LightweightPark(heavyPark);
			lightParks.add(lightPark);
		}

		return lightParks;
	}

	/** Convert a hash map of Parks to a set of LightweightParks.
	 * 
	 * @param heavyParks the set of Parks
	 * @return the set of converted lightweight parks
	 * @param parks the parks that will be displayed on the map.
	 */
	private Set<LightweightPark> getLightParksFromHeavy(Set<Park> heavyParks) {
		Set<LightweightPark> lightParks = new TreeSet<LightweightPark>();

		for(Park heavyPark : heavyParks) {
			LightweightPark lightPark = new LightweightPark(heavyPark);
			lightParks.add(lightPark);
		}
		
		return lightParks;
	}
	
	/** Load the map with a given list of parks.
	 * 
	 * @param parks the parks that will be displayed on the map.
	 */
	private void loadMap() {
		parkMap = new ParkMap();
		reloadMap();
	}

	/**
	 * Reload the (already initialized) map with a given list of parks.
	 * 
	 * @param parks the parks that will be displayed on the map.
	 */
	private void reloadMap() {
		loadAdminButtons();
		
		Set<Park> filteredParks = filter.getFilteredParks();
		Set<LightweightPark> filteredParksLight = getLightParksFromHeavy(filteredParks);
		parkMap.setParks(filteredParksLight);
		
		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);

		try {
			MapWidget mapWidget = parkMap.getWidget(this);
			dock.addNorth(mapWidget, 500);
			RootPanel.get("parkfinder").add(dock);

			parkMap.zoomAndCenter(700, 500);
		} catch (Exception e) {
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
	
	/** Set up the filter button.
	 * 
	 */
	private void loadFilterButton() {
		RootPanel.get("filterandview").add(filterButton);
		
		filterButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				canAddBackFromFilterButton = true;
				loadFilterPage();
			}
		});
	}
	
	/** Create a filter table with all of the feature items.
	 * 
	 * @return the filter table with all feature items added.
	 */
	private FilterTable createFilterTable() {
		FilterTable filterTable = new FilterTable();
		
		String[] sportsFields = {"Baseball Diamonds","Bowling Greens","Cricket Pitches","Field Hockey","Football Fields","Rugby Fields","Soccer Fields","Ultimate Fields"};
		
		String[] sportsOther = {"Ball Hockey", "Basketball Courts","Disc Golf Courses","Golf Courses","Horseshoe Pitch","Lacrosse Boxes","Outdoor Roller Hockey Rinks","Rinks","Running Tracks","Skateboard Parks","Softball","Sport Court","Swimming Pools","Tennis Courts"};

		String[] buildings = {"Community Centres","Community Halls","Field Houses","Food Concessions","Restaurants","Washrooms"};

		String[] walkingAndExercise = {"Dogs Off-Leash Areas","Exercise Stations","Jogging Trails","Perimeter Walking Path","Senior's Wellness Circuit"};
		
		String[] generalFeatures = {"Beaches","Designated Wedding Ceremony Site","Hellenic Garden","Lighted Fields","Picnic Benches","Picnic Sites","Playgrounds","Seawall","Water/Spray Parks","Wading Pool"};
		
		addFeaturesToTable("Sports - Fields", sportsFields, filterTable);
		addFeaturesToTable("Sports - Other", sportsOther, filterTable);
		addFeaturesToTable("Buildings", buildings, filterTable);
		addFeaturesToTable("Walking and Exercise", walkingAndExercise, filterTable);
		addFeaturesToTable("General Features", generalFeatures, filterTable);
		
		return filterTable;
	}
	
	/** Adds one category of features to a given table.
	 * 
	 */
	private void addFeaturesToTable(String title, String[] features, FilterTable table) {
		FeatureList featureList = new FeatureList(title);
		
		for(String feature : features) {
			featureList.addFeatureOption(feature);
		}
		
		table.addFeatureList(featureList);
	}
	
	/** Set up the park filter object.
	 * 
	 * @param parks the parks to set the filter object up with.
	 * 
	 */
	private void loadParkFilter(HashMap<Integer, Park> parks) {
		Set<Park> resultingParks = new TreeSet<Park>();
		
		for(Park park : parks.values()) {
			resultingParks.add(park);
		}
		
		filter = ParkFilter.getInstance(resultingParks);
	}

	/**
	 * Park List Panel
	 */
	private void loadListPanel() {
		parkListFlexTable.setText(0, 0, "Park name");
		parkListFlexTable.setText(0, 1, "Address");
		parkListFlexTable.setText(0, 2, "Details");
		parkListFlexTable.addStyleName("parklistflextable");
		parkListFlexTable.getRowFormatter().addStyleName(0, "detailsLabel");
	
		RootPanel.get("parkfinder").add(parkListFlexTable);
		
		// addMapViewButton() (possibly re-add)
	}
	
	/** Add the list view button.
	 * 
	 */
	private void addMapViewButton() {
		RootPanel.get("filterandview").add(mapButton);
		
		mapButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadMapPage(false);
			}
		});
	}

	/**
	 * Obtains list of parks from db
	 */
	private void displayParkList() {
		Set<Park> filteredParks = filter.getFilteredParks();
		Set<LightweightPark> parkList = (TreeSet<LightweightPark>) getLightParksFromHeavy(filteredParks);
		System.out.println("Displaying " + parkList.size() + " filtered parks.");
		loadParkList(parkList);
		/*
		locationService
				.getAllParks(new AsyncCallback<HashMap<Integer, Park>>() {
					@Override
					public void onFailure(Throwable caught) {
						handleError(caught);
					}

					@Override
					public void onSuccess(HashMap<Integer, Park> result) {
						allParks = result;
						parksList = (TreeSet<LightweightPark>) getLightParksFromHeavy(result);
						loadParkList(parksList);
					}
				});
				*/
		}
	
	
			

	/**
	 * Adds name and address to flextable 
	 * Go to details page for the park selected in the table
	 * @param parks
	 */
	private void loadParkList(Set<LightweightPark> parks) {
		TreeSet<String> parkNames = new TreeSet<String>();
			combineNamAddrId(parks, parkNames);
			int i=1;
			String s;
			for (String n : parkNames) {
				parkListFlexTable.setText(i, 0, n.substring(0, n.indexOf("Address:")));
				parkListFlexTable.setText(i, 1, n.substring(n.indexOf(":") + 1, n.indexOf("#")));
				s= n.substring(n.lastIndexOf("#") + 1);
				final Integer id=Integer.valueOf(s);
				goToDetailPage = new Button("View Park's Details");
				parkListFlexTable.setWidget(i, 2, goToDetailPage);
				
				goToDetailPage.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						loadDetailsPage(id);
					}
				});
								
				i++;
			}
		}

	/**
	 * @param parks
	 * @param names
	 * @return alphabetically arranged set where each element consists of name, address and id of a park
	 */
	private TreeSet<String> combineNamAddrId(Set<LightweightPark> parks, TreeSet<String> names) {
		for (LightweightPark p: parks){
			names.add(p.getName() + " Address: " + p.getAddress() + "#" + p.getId());	
			}
		return names;
		}
	
	
	/** Clear all the divs on the page for loading a different page.
	 * 
	 */
	private void clearAllDivs() {
		RootPanel.get("admin").clear();
		RootPanel.get("filterandview").clear();
		RootPanel.get("parkfinder").clear();
		RootPanel.get("comments").clear();
	}
}


