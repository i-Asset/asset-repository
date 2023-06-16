package at.srfg.iasset.network.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BrokerAuthentication {
	@Column(name="username", length=128, nullable=false)
	private String username;
	@Column(name="password", length=128, nullable=false)
	private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
