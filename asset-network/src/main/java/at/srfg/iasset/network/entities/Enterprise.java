package at.srfg.iasset.network.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "itwin_enterprise")
public class Enterprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="display_name", length = 64, nullable = false)
    private String name;
    
    @Column(name="legal_name", length = 64, nullable = false, unique = true)
    private String legalName;
    
    @Column(name="description", length = 1024)
    private String description;
    
    @Column(name="domain", length = 10, nullable = false)
    private String domain;
    @Column(name="uri")
    private String uri;
    
    @Column(name="created")
    private LocalDateTime created;
    
    @Column(name="modified")
    private LocalDateTime modified;
    
    @JsonIgnore
    @OneToMany(mappedBy = "enterprise", cascade = CascadeType.ALL)
    private List<Employee> employees;

    @JsonIgnore
    @OneToMany(mappedBy = "enterprise", cascade = CascadeType.ALL)
    private List<NetworkingSystem> systems;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLegalName() {
		return legalName;
	}
	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
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
	public List<Employee> getEmployees() {
		return employees;
	}
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	public List<NetworkingSystem> getSystems() {
		return systems;
	}
	public void setSystems(List<NetworkingSystem> systems) {
		this.systems = systems;
	}
    		
}
