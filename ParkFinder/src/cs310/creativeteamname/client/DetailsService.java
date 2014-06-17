package cs310.creativeteamname.client;

import cs310.creativeteamname.shared.Park;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("detail")
public interface DetailsService extends RemoteService {
	
	public Park getParkDetails(String parkId);

}
