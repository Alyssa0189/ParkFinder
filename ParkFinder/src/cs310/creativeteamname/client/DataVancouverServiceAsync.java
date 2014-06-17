package cs310.creativeteamname.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataVancouverServiceAsync {
	public void refreshData(AsyncCallback<Void> callback);
}
