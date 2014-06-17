package cs310.creativeteamname.client;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cs310.creativeteamname.shared.Park;
@RemoteServiceRelativePath("location")
public interface LocationService extends RemoteService{
	public HashMap<Integer, Park> getAllParks();
}
