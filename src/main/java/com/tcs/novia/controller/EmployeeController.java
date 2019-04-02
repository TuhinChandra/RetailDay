package com.tcs.novia.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.tcs.novia.repository.FlightRepository;
import com.tcs.novia.service.EmployeeService;
import com.tcs.novia.util.ImageProcessor;

@Controller
public class EmployeeController {

	@Autowired
	protected EmployeeService employeeService;
	@Autowired
	private FlightRepository flightRepository;
	@Autowired
	protected ImageProcessor imageProcessor;

	@Value("${emp_default_password}")
	protected String defaultEmployeePassword;

	@RequestMapping(value = "/getEmployee/{employeeID}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Employee getEmployee(@PathVariable("employeeID") final long employeeID) {

		Employee employee = employeeService.findByEmployeeID(employeeID);
		if (null == employee) {
			employee = createInvalidEmployee();
		}
		return employee;
	}

	@RequestMapping(value = "/authenticateEmployee/{employeeID}/{password}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Employee authenticateEmployee(@PathVariable("employeeID") final long employeeID,
			@PathVariable("password") final String password) {

		Employee employee = employeeService.findByEmployeeID(employeeID);
		if (null != employee) {
			employee = employeeService.getEmployeestatus(employeeID, password);
			if (null == employee) {
				employee = createInvalidEmployee();
			}
		} else {
			employee = createInvalidEmployee();
		}

		return employee;
	}

	@RequestMapping(value = "/changePassword/{employeeID}/{oldPassword}/{newPassword}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Employee changePassword(@PathVariable("employeeID") final long employeeID,
			@PathVariable("oldPassword") final String oldpassword,
			@PathVariable("newPassword") final String newPassword) {

		if (newPassword.equals(defaultEmployeePassword)) {
			return createInvalidEmployee();
		}

		final Employee employee = employeeService.getEmployee(employeeID, oldpassword);

		return null != employee && employee.getStatus() != UserState.INVALID
				? employeeService.changePassword(employee, newPassword)
				: employee;
	}

	@RequestMapping(value = "/resetPassword/{employeeID}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Employee resetPassword(@PathVariable("employeeID") final long employeeID) {
		final Employee employee = employeeService.findByEmployeeID(employeeID);
		if (null == employee) {
			return createInvalidEmployee();
		}
		return employeeService.resetPassword(employee, defaultEmployeePassword);
	}

	@RequestMapping(value = "/confirmParticipation/{employeeID}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Employee confirmParticipation(@PathVariable("employeeID") final long employeeID) {
		final Employee employee = employeeService.findByEmployeeID(employeeID);
		if (null == employee) {
			return createInvalidEmployee();
		}
		return employeeService.confirmParticipation(employee);
	}

	@RequestMapping(value = "/rejectParticipation/{employeeID}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Employee rejectParticipation(@PathVariable("employeeID") final long employeeID) {
		final Employee employee = employeeService.findByEmployeeID(employeeID);
		if (null == employee) {
			return createInvalidEmployee();
		}
		return employeeService.rejectParticipation(employee);
	}

	@RequestMapping(value = "/checkIn/{employeeID}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Employee checkIn(@PathVariable("employeeID") final long employeeID) {
		final Employee employee = employeeService.findByEmployeeID(employeeID);
		if (null == employee) {
			return createInvalidEmployee();
		}
		return employeeService.checkIn(employee);
	}

	@RequestMapping(value = "/completeEmployeeProfile/{employeeID}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Employee completeEmployeeProfile(@PathVariable("employeeID") final long employeeID,
			@RequestParam(value = "firstName", required = false) final String firstName,
			@RequestParam(value = "lastName", required = false) final String lastName,
			@RequestParam(value = "emailID", required = false) final String emailID,
			@RequestParam(value = "foodPreference", required = true) final String foodPreference,
			@RequestParam(value = "mobile", required = false) final String mobile,
			@RequestParam(value = "cityTour", required = false) final String cityTour,
			@RequestParam(value = "photoFile", required = false) final String photo,
			@RequestParam(value = "gender", required = false) final String gender,
			@RequestParam(value = "shirtSize", required = false) final String shirtSize,
			@RequestParam(value = "gift", required = false) final String gift,
			@RequestParam(value = "pickupAddress", required = false) final String pickupAddress,
			@RequestParam(value = "accomodationNeeded", required = false) final String accomodationNeeded)
			throws Exception {

		Employee employee = employeeService.findByEmployeeID(employeeID);

		if (null != employee) {
			employee = employeeService.completeProfile(employee, firstName, lastName, emailID, foodPreference, mobile,
					cityTour, photo, gender, shirtSize, gift, accomodationNeeded, pickupAddress);
		} else {
			employee = createInvalidEmployee();
		}
		return employee;
	}

	@RequestMapping(value = "/addFlightInfo/{employeeID}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Employee addFlightInfo(@PathVariable("employeeID") final long employeeID,
			@RequestParam(value = "airlineName", required = true) final String airlineName,
			@RequestParam(value = "flightNumber", required = true) final String flightNumber,
			@RequestParam(value = "location", required = false) final String location,
			@RequestParam(value = "flightType", required = true) final String flightType,
			@RequestParam(value = "flightDate", required = true) final String flightDate,
			@RequestParam(value = "flightTime", required = false) final String flightTime) throws Exception {

		Employee employee = employeeService.findByEmployeeID(employeeID);

		if (null != employee) {
			final List<FlightInfo> inboundFlightInfo = new ArrayList<>();
			final List<FlightInfo> outboundFlightInfo = new ArrayList<>();

			final FlightInfo flightInfoToAddOrUpdate = createFlightInfo(airlineName, flightNumber, location, flightType,
					flightDate, flightTime, employee);

			final List<FlightInfo> flightInfos = employee.getFlightInfos();
			if (null != flightInfos && !flightInfos.isEmpty()) {
				for (final FlightInfo flightInfo : flightInfos) {
					if (flightInfo.getFlightType() == FlightType.INBOUND) {
						inboundFlightInfo.add(flightInfo);
					} else if (flightInfo.getFlightType() == FlightType.OUTBOUND) {
						outboundFlightInfo.add(flightInfo);
					}
				}
			}

			if (FlightType.valueOf(flightType.toUpperCase()) == FlightType.OUTBOUND) {
				if (!outboundFlightInfo.isEmpty()) {
					employee.getFlightInfos().removeAll(outboundFlightInfo);
					flightRepository.deleteAll(outboundFlightInfo);
				}
				employee.getFlightInfos().add(flightInfoToAddOrUpdate);
			} else if (FlightType.valueOf(flightType.toUpperCase()) == FlightType.INBOUND) {
				if (!inboundFlightInfo.isEmpty()) {
					employee.getFlightInfos().removeAll(inboundFlightInfo);
					flightRepository.deleteAll(inboundFlightInfo);
				}
				employee.getFlightInfos().add(flightInfoToAddOrUpdate);
			}
			employee = employeeService.addFlightInfo(employee);
		} else {
			employee = createInvalidEmployee();
		}
		return employee;
	}

	private FlightInfo createFlightInfo(final String airlineName, final String flightNumber, final String location,
			final String flightType, final String flightDate, final String flightTime, final Employee employee) {
		final FlightInfo flightInfo = new FlightInfo(airlineName, flightNumber,
				FlightType.valueOf(flightType.toUpperCase()), flightDate, flightTime, location, employee);
		return flightInfo;
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
			@RequestParam(value = "shirtSize", required = false) final String shirtSize,
			@RequestParam(value = "gift", required = false) final String gift,
			@RequestParam(value = "confirmParticipation", required = false) final String confirmParticipation,
			@RequestParam(value = "accomodationNeeded", required = false) final String accomodationNeeded,
			@RequestParam(value = "flightUpdateNeeded", required = false) final String flightUpdateNeeded,
			@RequestParam(value = "pickupAddress", required = false) final String pickupAddress,
			@RequestParam(value = "role", required = false) final String role) throws Exception {

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
					projectName, photo, gender, shirtSize, gift, accomodationNeeded, confirmParticipation,
					flightUpdateNeeded, role, pickupAddress);
		} else {
			employee = createInvalidEmployee();
		}
		return employee;
	}

	protected static Employee createInvalidEmployee() {
		final Employee employee = new Employee();
		employee.setStatus(UserState.INVALID);
		return employee;
	}
}
