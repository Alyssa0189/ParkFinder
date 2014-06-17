package cs310.creativeteamname.client;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cs310.creativeteamname.shared.Park;

public interface LocationServiceAsync {
	public void getAllParks(AsyncCallback<HashMap> callback);
}
