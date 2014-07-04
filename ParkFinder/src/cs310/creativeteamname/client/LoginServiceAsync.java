package cs310.creativeteamname.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cs310.creativeteamname.shared.LoginInfo;

public interface LoginServiceAsync {
	public void login(String requestUri, AsyncCallback<LoginInfo> async);

}
