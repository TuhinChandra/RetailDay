package com.tcs.novia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcs.novia.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findByEmployeeID(long employeeID);

	List<Employee> findByEmployeeIDAndPassword(long employeeID, String password);

	List<Employee> findByEmployeeIDOrMobile(long employeeID, String mobile);

	@Query("select max(registrationNo) from Employee")
	int getLastRegistrationNo();
}
