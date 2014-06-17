package cs310.creativeteamname.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cs310.creativeteamname.shared.Park;

public interface DetailsServiceAsync {
	
	public void getParkDetails(String parkId, AsyncCallback<Park> async);

}
