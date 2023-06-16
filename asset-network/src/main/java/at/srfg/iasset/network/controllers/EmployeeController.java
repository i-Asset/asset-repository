package at.srfg.iasset.network.controllers;

import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.srfg.iasset.network.entities.Employee;
import at.srfg.iasset.network.entities.Enterprise;
import at.srfg.iasset.network.exceptions.BadRequestException;
import at.srfg.iasset.network.services.EmployeeService;
import at.srfg.iasset.network.services.EnterpriseService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee")


public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private EnterpriseService enterpriseService;
	
	@PostMapping(path = "/{enterpriseId}")
	public Employee create(@PathParam Long enterpriseId, @Valid @RequestBody Employee input) {
        if (input.getId() != null) {
            throw (new BadRequestException()).withError("id", "must be null");
            
            
        }
        if (input.getEnterprise()!=null) {
            throw (new BadRequestException()).withError("enterprise", "must be null");
        	
        }
        Enterprise enterprise = enterpriseService.get(enterpriseId);
        return employeeService.create(input);
		
	}
}
