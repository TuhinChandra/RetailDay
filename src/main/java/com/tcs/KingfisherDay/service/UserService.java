package com.tcs.KingfisherDay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.KingfisherDay.model.Employee;
import com.tcs.KingfisherDay.model.enums.FoodPreference;
import com.tcs.KingfisherDay.repository.EmployeeRepository;
import com.tcs.KingfisherDay.util.PasswordCrypto;

@Service
public class UserService {

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	PasswordCrypto passwordCrypto;

	public Employee register(String name, String emailID, String foodPreference, String password, String mobile, String photo) {
		FoodPreference userFoodPreference=null!=foodPreference && !foodPreference.isEmpty()?FoodPreference.valueOf(foodPreference):FoodPreference.VEG;
		return employeeRepository.save(new Employee(name, emailID, userFoodPreference, passwordCrypto.encrypt(password), mobile, photo));
	}

	public Employee findByEmailID(String emailID) {
		List<Employee> employeeList = employeeRepository.findByEmailID(emailID);
		if (employeeList.isEmpty())
			return null;
		else
			return employeeList.get(0);
	}

	public Boolean isExistsEmployee(String emailID, String mobile) {
		List<Employee> employeeList = employeeRepository.findByEmailIDOrMobile(emailID, mobile);
		return !employeeList.isEmpty();
	}

	public Employee getEmployee(String emailID, String password) {
		List<Employee> employeeList = employeeRepository.findByEmailID(emailID);
		if (employeeList.isEmpty())
			return null;
		else if (passwordCrypto.validate(password, employeeList.get(0).getPassword())) {
			return employeeList.get(0);
		}
		return null;
	}

}
