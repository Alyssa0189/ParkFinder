package cs310.creativeteamname.shared;

import java.math.BigDecimal;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Rating {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private User user;
	@Persistent
	private int parkId;
	@Persistent
	private BigDecimal userRating;
	
	public Rating(User user, int parkId, BigDecimal userRating) {
		this.user = user;
		this.parkId = parkId;
		this.userRating = userRating;
	}

	public Long getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getParkId() {
		return parkId;
	}
	
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}

	public BigDecimal getUserRating() {
		return userRating;
	}

	public void setUserRating(BigDecimal userRating) {
		this.userRating = userRating;
	}
	
}
