package cs310.creativeteamname.client;


import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ParkFinder implements EntryPoint {
	
	private VerticalPanel mainPanel =new VerticalPanel();
	private VerticalPanel commentPanel=new VerticalPanel();
	private TextArea commentArea=new TextArea();
	private Label commentHereLabel=new Label("Please add your comment in the textbox below");
	private Button addCommentButton=new Button("Add your comment");
	private Button submitButton=new Button("Submit");
	private Button cancelButton=new Button("Cancel");
	private ArrayList<String> comment = new ArrayList<String>(); //for whatever user puts in commentbox
	
	
	/**
	 * This is the entry point method.
	 */
	
	public void onModuleLoad() {
	
	//Assemble Main panel
	mainPanel.add(addCommentButton);
	
	//ToDoList
	//the comments from field should be displayed
	//where addComment button is in details page
	//when addComment button is pressed leads to comment page
	
		
			
	}
	
	private void displayComment(){
	//display comments on main page
		
	}
	
	private void loadCommentPanel() {
	//Assemble comment panel	
		commentPanel.add(commentHereLabel);
		commentPanel.add(commentArea);
		commentPanel.add(submitButton);
		commentPanel.add(cancelButton);
		
		//Move cursor focus to the input box
		commentArea.setFocus(true);
		
		// Listen for mouse events on the submit button
				submitButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						addComment();
					}
				});
				
		// Listen for mouse events on the cancel button
				cancelButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						//go to details page
					}
				});
		
		// Listen for keyboard events in the input box.
				commentArea.addKeyDownHandler(new KeyDownHandler() {
					public void onKeyDown (KeyDownEvent event) {
						if (event.getNativeKeyCode()== KeyCodes.KEY_ENTER) {
							addComment();
						}
					}
				});
		
		}
	
	//when submitComment button is pressed it stores comment in our db and displays it in details page
	
	private void addComment(){
			
	}
	
	
	
}
