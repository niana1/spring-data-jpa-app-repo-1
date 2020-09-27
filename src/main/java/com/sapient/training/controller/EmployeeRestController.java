package com.sapient.training.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sapient.training.entity.Employee;
import com.sapient.training.exception.EmployeeException;
import com.sapient.training.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;


@Slf4j 
@RestController
@RequestMapping("/api")
public class EmployeeRestController {

	@Autowired
	private EmployeeService employeeService;

	//http://localhost:8081/api/employees
	/*
	 * post json object from postman
	 * ex.
	 * {
    	"name": "Rohit Sharma",
    	"job": "Manager",
    	"hiredate": "2010-10-15",    	
    	"salary": 1350000.0
		}
	 */
	@PostMapping("/employees")
	public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {

		try {
			Employee saved = employeeService.save(employee);
			return new ResponseEntity<>(saved, HttpStatus.CREATED);
		}catch(EmployeeException e) {
			//log.error(e.getMessage(), e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
		}
	}

	//http://localhost:8081/api/employees
	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		try {
			List<Employee> employeeList = employeeService.getAllEmployees();
			return new ResponseEntity<>(employeeList, HttpStatus.CREATED);
		}catch(EmployeeException e) {
			//log.error(e.getMessage(), e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
		}
	}

	//http://localhost:8081/api/employees/manager/1000000
	@GetMapping("/employees/{job}/{salary}")
	public ResponseEntity<List<Employee>> findByJobAndSalaryGreaterThan(@PathVariable String job, @PathVariable Double salary){
		try {
			List<Employee> employeeList = employeeService.findByJobAndSalaryGreaterThan(job, salary);
			return new ResponseEntity<>(employeeList, HttpStatus.OK);
		}catch(EmployeeException e) {
			//log.error(e.getMessage(), e);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
		}
	}
	
	@GetMapping("/employee/{job}/{salary}")
	public ResponseEntity<List<Employee>> findByJobAndSalary(@PathVariable String job, @PathVariable Double salary){
		try {
			List<Employee> employeeList = employeeService.findByJobAndSalary(job, salary);
			return new ResponseEntity<>(employeeList, HttpStatus.OK);
		}catch(EmployeeException e) {
			//log.error(e.getMessage(), e);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
		}
	}
	
	//http://localhost:8081/api/employees/1
	@GetMapping("/employees/{id}")
	public Employee getEmployeeById(@PathVariable Long id){		
		try {
			return employeeService.getEmployeeById(id);
		}catch(EmployeeException e) {
			//log.error(e.getMessage(), e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}

}
