package com.tcs.novia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tcs.novia.model.Employee;
import com.tcs.novia.model.FlightInfo;
import com.tcs.novia.model.enums.FlightType;
import com.tcs.novia.model.enums.UserState;
import com.tcs.novia.service.EmployeeService;
import com.tcs.novia.util.ImageProcessor;

@Controller
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	ImageProcessor imageProcessor;

	@Value("${emp_default_password}")
	private String defaultEmployeePassword;

	@RequestMapping(value = "/createEmployee/{employeeID}/{emailID}/{firstName}/{lastName}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Employee createEmployee(@PathVariable("employeeID") final long employeeID,
			@PathVariable("emailID") final String emailID, @PathVariable("firstName") final String firstName,
			@PathVariable("lastName") final String lastName) throws Exception {
		return employeeService.register(employeeID, firstName, lastName, emailID, defaultEmployeePassword);
	}

	@RequestMapping(value = "/completeEmployeeProfile/{employeeID}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Employee completeEmployeeProfile(@PathVariable("employeeID") final long employeeID,
			@RequestParam(value = "firstName", required = true) final String firstName,
			@RequestParam(value = "lastName", required = true) final String lastName,
			@RequestParam(value = "emailID", required = true) final String emailID,
			@RequestParam(value = "foodPreference", required = true) final String foodPreference,
			@RequestParam(value = "mobile", required = false) final String mobile,
			@RequestParam(value = "cityTour", required = false) final String cityTour,
			@RequestParam(value = "photoFile", required = false) final String photo,
			@RequestParam(value = "gender", required = false) final String gender,
			@RequestParam(value = "shirtSize", required = true) final String shirtSize) throws Exception {

		Employee employee = employeeService.findByEmployeeID(employeeID);

		if (null != employee) {
			employee = employeeService.completeProfile(employee, firstName, lastName, emailID, foodPreference, mobile,
					cityTour, photo, gender, shirtSize);
		} else {
			employee = createInvalidEmployee();
		}
		return employee;
	}

	@RequestMapping(value = "/updateEmployeeInfo/{employeeID}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Employee updateEmployeeInfo(@PathVariable("employeeID") final long employeeID,
			@RequestParam(value = "firstName", required = false) final String firstName,
			@RequestParam(value = "lastName", required = false) final String lastName,
			@RequestParam(value = "emailID", required = false) final String emailID,
			@RequestParam(value = "foodPreference", required = false) final String foodPreference,
			@RequestParam(value = "password", required = false) final String password,
			@RequestParam(value = "mobile", required = false) final String mobile,
			@RequestParam(value = "projectName", required = false) final String projectName,
			@RequestParam(value = "photoFile", required = false) final MultipartFile photoFile,
			@RequestParam(value = "gender", required = false) final String gender,
			@RequestParam(value = "shirtSize", required = false) final String shirtSize) throws Exception {

		Employee employee = employeeService.findByEmployeeID(employeeID);

		if (null != employee) {
			String photo = employee.getPhoto();
			if (null != photoFile && !photoFile.isEmpty()) {
				try {
					final byte[] bytes = photoFile.getBytes();
					photo = imageProcessor.resizeImage(bytes);
				} catch (final Exception e) {
					throw new Exception("Oops!! Looks like there is a technical issue. Try with different image.");
				}
			}
			employee = employeeService.update(employee, firstName, lastName, emailID, foodPreference, password, mobile,
					projectName, photo, gender, shirtSize);
		} else {
			employee = createInvalidEmployee();
		}
		return employee;
	}

	@RequestMapping(value = "/addFlightInfo/{employeeID}/{airlineName}/{flightNumber}/{flightType}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Employee addFlightInfo(@PathVariable("employeeID") final long employeeID,
			@PathVariable("airlineName") final String airlineName,
			@PathVariable("flightNumber") final String flightNumber,
			@PathVariable("flightType") final String flightType,
			@RequestParam(value = "arrivalDate", required = false) final String arrivalDate,
			@RequestParam(value = "arrivalTime", required = false) final String arrivalTime) throws Exception {

		Employee employee = employeeService.findByEmployeeID(employeeID);

		if (null != employee) {

			final FlightInfo flightInfo = new FlightInfo(airlineName, flightNumber,
					FlightType.valueOf(flightType.toUpperCase()), arrivalDate, arrivalTime, employee);
			employee.getFlightInfos().add(flightInfo);
			employee = employeeService.addFlightInfo(employee);
		} else {
			employee = createInvalidEmployee();
		}
		return employee;
	}

	@RequestMapping(value = "/authenticateEmployee/{employeeID}/{password}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Employee getEmployee(@PathVariable("employeeID") final long employeeID,
			@PathVariable("password") final String password) {

		Employee employee = employeeService.findByEmployeeID(employeeID);
		if (null != employee) {
			employee = employeeService.getEmployee(employeeID, password);
			if (null == employee) {
				employee = createInvalidEmployee();
			}
		} else {
			employee = createInvalidEmployee();
		}

		return employee;
	}

	private Employee createInvalidEmployee() {
		final Employee employee = new Employee();
		employee.setStatus(UserState.INVALID);
		return employee;
	}

	@RequestMapping(value = "/changePassword/{employeeID}/{oldPassword}/{newPassword}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Employee changePassword(@PathVariable("employeeID") final long employeeID,
			@PathVariable("oldPassword") final String oldpassword,
			@PathVariable("newPassword") final String newPassword) {
		final Employee employee = employeeService.getEmployee(employeeID, oldpassword);

		return null != employee && employee.getStatus() != UserState.INVALID
				? employeeService.changePassword(employee, newPassword)
				: employee;
	}

	@RequestMapping(value = "/resetPassword/{employeeID}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Employee forgotPassword(@PathVariable("employeeID") final long employeeID) {
		final Employee employee = employeeService.findByEmployeeID(employeeID);
		return employeeService.changePassword(employee, defaultEmployeePassword);
	}
}
