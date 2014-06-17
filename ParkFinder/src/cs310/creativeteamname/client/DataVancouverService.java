package cs310.creativeteamname.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("data")
public interface DataVancouverService extends RemoteService{
	public void refreshData();
}
