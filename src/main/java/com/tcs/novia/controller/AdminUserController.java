package com.tcs.novia.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.tcs.novia.exception.CustomException;
import com.tcs.novia.model.Employee;
import com.tcs.novia.service.EmployeeService;
import com.tcs.novia.util.CsvUtils;
import com.tcs.novia.util.WriteDataToCSV;

@Controller
public class AdminUserController {

	@Autowired
	private EmployeeService employeeService;

	@Value("${emp_default_password}")
	protected String defaultEmployeePassword;

	@RequestMapping(value = "/createEmployee/{employeeID}/{emailID}/{firstName}/{lastName}/{gender}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Employee createEmployee(@PathVariable("employeeID") final long employeeID,
			@PathVariable("emailID") final String emailID, @PathVariable("firstName") final String firstName,
			@PathVariable("lastName") final String lastName, @PathVariable("gender") final String gender,
			@RequestParam(value = "shortName", required = false) final String shortName,
			@RequestParam(value = "role", required = false) final String role) throws Exception {
		return employeeService.register(employeeID, firstName, lastName, shortName, gender, emailID,
				defaultEmployeePassword, role);
	}
	
	@RequestMapping(value = "/createEmployeeFromJSON", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Employee createEmployeeFromJSON(@RequestBody Employee employee) throws Exception {
		return employeeService.saveEmployeeData(employee);
	}
	
	@RequestMapping(value = "/createEmployeesFromJSON", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public List<Employee> createEmployeesFromJSON(@RequestBody List<Employee> employees) throws Exception {
		return employeeService.saveBulkEmployees(employees);
	}

	@RequestMapping(value = "/getAllEmployee", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Employee> getAllEmployee() throws Exception {
		return employeeService.getAllEmployee();
	}

	@RequestMapping(value = "/deleteEmployee/{employeeID}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void deleteEmployee(@PathVariable("employeeID") final long employeeID) throws Exception {
		employeeService.deleteEmployee(employeeID);
	}

	@RequestMapping(value = "/deleteAllEmployees", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void deleteAllEmployees() throws Exception {
		employeeService.deleteAllEmployees();
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json", consumes = "multipart/form-data")
	@ResponseBody
	public List<Employee> uploadUsers(@RequestParam("file") final MultipartFile file)
			throws IOException, CustomException {

		employeeService.preValidationBeforeUpload();

		final List<Employee> employees = CsvUtils.read(Employee.class, file.getInputStream());

		employeeService.postValidationBeforeUpload(employees);

		List<Employee> employeesUploaded = null;
		List<String> employeeEmailIDs = null;
		if (null != employees && !employees.isEmpty()) {
			employeesUploaded = new ArrayList<>();
			employeeEmailIDs = new ArrayList<>();
			for (final Employee emp : employees) {
				employeeEmailIDs.add(emp.getEmailID());
				employeesUploaded.add(employeeService.register(emp.getEmployeeID(), emp.getFirstName(),
						emp.getLastName(), emp.getShortName(), emp.getGender().toString(), emp.getEmailID(),
						defaultEmployeePassword, emp.getRole()));
			}
		}
		return employeesUploaded;

	}

	@GetMapping("/download/userdump.csv")
	public void downloadCSV(final HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; file=userdump.csv");

		WriteDataToCSV.writeDataToCsvUsingStringArray(response.getWriter(), employeeService.getEmployees());
	}

	@RequestMapping(value = "/updateUploadUserDate", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<Employee> updateUploadUserDate() {
		return employeeService.updateUploadUserDate();
	}

	@RequestMapping(value = "/getCountOfExpectedDelegates", method = RequestMethod.GET)
	@ResponseBody
	public int getCountOfExpectedDelegates() {
		return employeeService.getCountOfExpectedDelegates();
	}
}
