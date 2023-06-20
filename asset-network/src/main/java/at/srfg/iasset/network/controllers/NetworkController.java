package at.srfg.iasset.network.controllers;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import at.srfg.iasset.network.entities.Enterprise;
import at.srfg.iasset.network.entities.NetworkingSystem;
import at.srfg.iasset.network.entities.User;
import at.srfg.iasset.network.exceptions.BadRequestException;
import at.srfg.iasset.network.security.IAuthenticationFacade;
import at.srfg.iasset.network.services.NetworkingSystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/network")

public class NetworkController {
	private final NetworkingSystemService networkService;
	/**
	 * Provides access to the logged in user
	 */
	@Autowired
	IAuthenticationFacade authentication;
	
	public NetworkController(NetworkingSystemService enterpriseService) {
		this.networkService = enterpriseService;
	}
	
    @Operation(summary = "Returns the self-describing information of a network resource (ServiceDescription)", description = "", tags={ "Description API" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Enterprise.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized, e.g. the server refused the authorization attempt.", content = @Content(mediaType = "application/json", schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema())) })
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
//	@PreAuthorize("hasAuthority('app_admin')")
	public NetworkingSystem create(@Valid @RequestBody NetworkingSystem input) {
        if (input.getId() != null) {
            throw (new BadRequestException()).withError("id", "must be null");
        }
        return networkService.create(input);
		
	}
    @GetMapping("/{id}")
//    @PreAuthorize("isAuthenticated()")
    public NetworkingSystem get(@NotNull @PathVariable Long id) {
    	return networkService.get(id);
        //return customerService.get(id);
    }
    @GetMapping
//    @PreAuthorize("hasAuthority('app_admin')")
    public List<NetworkingSystem> listAll() {
    	Authentication auth = authentication.getAuthentication();
    	User user = authentication.getUserDetails();
        return networkService.listAll();
    }
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasAuthority('iasset-admin')")
    public NetworkingSystem update(@PathVariable Long id, @Valid @RequestBody NetworkingSystem input) {
        if (!Objects.equals(id, input.getId())) {
            throw (new BadRequestException()).withError("id", "must be equals id from url");
        }
        return networkService.update(input);
    }
    @DeleteMapping(path = "/{id}")
//    @PreAuthorize("hasAuthority('app_admin')")
    public NetworkingSystem delete(@NotNull @PathVariable Long id) {
        return networkService.delete(id);
    }
    


}