package com.tcs.novia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tcs.novia.model.Employee;
import com.tcs.novia.model.enums.CityTour;
import com.tcs.novia.model.enums.FoodPreference;
import com.tcs.novia.model.enums.Gender;
import com.tcs.novia.model.enums.ShirtSize;
import com.tcs.novia.model.enums.UserState;
import com.tcs.novia.repository.EmployeeRepository;
import com.tcs.novia.util.PasswordCrypto;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	PasswordCrypto passwordCrypto;

	public Employee register(final long employeeID, final String firstName, final String lastName, final String emailID,
			final String password) {
		final Employee employee = new Employee(employeeID, emailID, firstName, lastName,
				passwordCrypto.encrypt(password));
		employee.setStatus(UserState.CHANGE_DEFAULT_PASSWORD);
		return employeeRepository.save(employee);
	}

	public void deleteEmployee(final long employeeID) {
		employeeRepository.deleteById(employeeID);
	}
	public Employee completeProfile(final Employee employee, final String firstName, final String lastName,
			final String emailID, final String foodPreference, final String mobile, final String cityTour,
			final String photo, final String gender, final String shirtSize) {
		update(employee, firstName, lastName, emailID, foodPreference, null, mobile, cityTour, photo, gender,
				shirtSize);
		if (employee.getRegistrationNo() == 0) {
			employee.setRegistrationNo(employeeRepository.getLastRegistrationNo() + 1);
			employee.setStatus(UserState.COMPLETE);
		}
		return employeeRepository.save(employee);
	}

	public Employee update(final Employee employee, final String firstName, final String lastName, final String emailID,
			final String foodPreference, final String password, final String mobile, final String cityTour,
			final String photo, final String gender, final String shirtSize) {
		if (!StringUtils.isEmpty(firstName)) {
			employee.setFirstName(firstName);
		}
		if (!StringUtils.isEmpty(lastName)) {
			employee.setLastName(lastName);
		}
		if (!StringUtils.isEmpty(emailID)) {
			employee.setEmailID(emailID);
		}
		if (!StringUtils.isEmpty(foodPreference)) {
			employee.setFoodPreference(FoodPreference.valueOf(foodPreference.toUpperCase()));
		}
		if (!StringUtils.isEmpty(password)) {
			employee.setPassword(passwordCrypto.encrypt(password));
		}
		if (!StringUtils.isEmpty(mobile)) {
			employee.setMobile(mobile);
		}
		if (!StringUtils.isEmpty(cityTour)) {
			employee.setCityTourSelected(CityTour.valueOf(cityTour.toUpperCase()));
		}
		if (!StringUtils.isEmpty(photo)) {
			employee.setPhoto(photo);
		}
		if (!StringUtils.isEmpty(gender)) {
			employee.setGender(Gender.valueOf(gender.toUpperCase()));
		}
		if (!StringUtils.isEmpty(shirtSize)) {
			employee.setShirtSize(ShirtSize.valueOf(shirtSize.toUpperCase()));
		}

		return employeeRepository.save(employee);
	}

	public Employee updateEmployee(final Employee employee) {
		return employeeRepository.save(employee);
	}

	public Employee findByEmployeeID(final long employeeID) {
		final List<Employee> employeeList = employeeRepository.findByEmployeeID(employeeID);
		if (employeeList.isEmpty()) {
			return null;
		} else {
			return employeeList.get(0);
		}
	}

	public Employee getEmployee(final long employeeID, final String password) {
		final Employee employee = findByEmployeeID(employeeID);
		if (null != employee) {
			if (!passwordCrypto.validate(password, employee.getPassword())) {
				employee.setStatus(UserState.INVALID);
			}
		}
		return employee;
	}

	public Employee addFlightInfo(final Employee employee) {
		return employeeRepository.save(employee);
	}

	public Employee changePassword(final Employee employee, final String newPassword) {

		employee.setPassword(passwordCrypto.encrypt(newPassword));
		if (employee.getStatus() == UserState.CHANGE_DEFAULT_PASSWORD) {
			employee.setStatus(UserState.INCOMPLETE);
		}
		return employeeRepository.save(employee);
	}

}
