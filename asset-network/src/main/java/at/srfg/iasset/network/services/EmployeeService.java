package at.srfg.iasset.network.services;

import org.springframework.stereotype.Service;

import at.srfg.iasset.network.entities.Employee;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmployeeService {
	
	public Employee create(Employee employee) {
		return employee;
	}

}	
