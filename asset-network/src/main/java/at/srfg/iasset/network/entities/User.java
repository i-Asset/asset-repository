package at.srfg.iasset.network.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="itwin_user")
public class User {
    @Id
    @Column(name="uuid", length = 32)
    private UUID id;

    @Column(name="name", length = 64, nullable = false)
    private String name;
    
    @Column(name="username", length = 64, nullable = false, unique = true)
    private String username;
    
    @Column(name="given_name", length = 64, nullable = false)
    private String givenName;
    
    @Column(name="family_name", length = 64, nullable = false)
    private String familyName;

    @Column(name="email", length = 64, nullable = false, unique = true)
    private String email;

    @Column(name="created")
    private LocalDateTime created;
    
    @Column(name="modified")
    private LocalDateTime modified;
    
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String family_name) {
		this.familyName = family_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	
	

}
