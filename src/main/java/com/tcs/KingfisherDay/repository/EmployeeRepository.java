package com.tcs.KingfisherDay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcs.KingfisherDay.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

	List<Employee> findByEmailID(String emailID);

	List<Employee> findByEmailIDAndPassword(String emailID, String password);

	List<Employee> findByEmailIDOrMobile(String emailID, String mobile);
}
