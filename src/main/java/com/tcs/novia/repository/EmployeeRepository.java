package com.tcs.novia.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcs.novia.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findByEmployeeID(long employeeID);

	@Query("select max(registrationNo) from Employee")
	int getLastRegistrationNo();

	@Query(nativeQuery = true, value = "SELECT emp_id FROM employee t1 WHERE t1.registration_no=0 and t1.confirm_participation is null and  "
			+ "t1.participation_reminder_last_sent is null and t1.CREATED_DATE_TIME <= CURRENT_TIMESTAMP - ?1 * INTERVAL '1 day'")
	List<BigInteger> findEmployeesYetToConfirmParticipation(int intervalInDay);

	@Query(nativeQuery = true, value = "SELECT emp_id FROM employee t1 WHERE t1.registration_no=0 and t1.confirm_participation='YES' and "
			+ "t1.REGISTRATION_REMINDER_LAST_SENT is null and t1.PARTICIPATION_DATE_TIME <= CURRENT_TIMESTAMP - ?1 * INTERVAL '1 day'")
	List<BigInteger> findEmployeesYetToCompleteRegistration(int intervalInDay);

	@Query(nativeQuery = true, value = "SELECT t1.emp_id FROM employee t1 "
			+ "LEFT JOIN flight_info t2 ON t2.emp_id = t1.emp_id WHERE t1.registration_no > 0 and t2.emp_id IS NULL and "
			+ "t1.flight_reminder_last_sent is null and t1.registration_completed_date_time <= CURRENT_TIMESTAMP - ?1 * INTERVAL '1 day'")
	List<BigInteger> findEmployeesYetToFillFlightInfo(int intervalInDay);

	List<Employee> findByCreatedDateTimeIsNull();

	List<Employee> findByConfirmParticipationAndLogisticsEmailSentIsNull(String confirmParticipation);

	List<Employee> findByRegistrationNoGreaterThanAndGiftAmendmentEmailSentIsNull(long registrationNo);

	int countByConfirmParticipationIsNullOrConfirmParticipationAndRoleIsNull(String confirmParticipation);

	@Query(nativeQuery = true, value = "select t2.emp_id from employee t1, email_tracker t2 "
			+ "where t1.emp_id = t2.emp_id and t1.confirm_participation is null and t1.registration_no=0 and "
			+ "email_type=?1 and delivered=true and t1.PARTICIPATION_REMINDER_LAST_SENT <= CURRENT_TIMESTAMP- INTERVAL '1 day' "
			+ "group by t2.emp_id, email_type having count(email_type) = ?2")
	List<BigInteger> findEmployeesEligibleForSubsequentParticipationReminders(String emailType, int reminderCount);

	@Query(nativeQuery = true, value = "select t2.emp_id from employee t1, email_tracker t2 "
			+ "where t1.emp_id = t2.emp_id and t1.confirm_participation='YES' and t1.registration_no=0 and "
			+ "email_type=?1 and delivered=true and t1.registration_reminder_last_sent <= CURRENT_TIMESTAMP- INTERVAL '1 day' "
			+ "group by t2.emp_id, email_type having count(email_type) = ?2")
	List<BigInteger> findEmployeesEligibleForSubsequentRegistrationReminders(String emailType, int reminderCount);

	@Query(nativeQuery = true, value = "select t2.emp_id from employee t1 LEFT JOIN flight_info t3 on "
			+ "t3.emp_id = t1.emp_id, email_tracker t2  where  t1.emp_id = t2.emp_id and t3.emp_id IS NULL "
			+ "and t1.registration_no > 0 and email_type=?1 and delivered=true  "
			+ "and t1.FLIGHT_REMINDER_LAST_SENT <= CURRENT_TIMESTAMP- INTERVAL '1 day' group by t2.emp_id, email_type having count(email_type) = ?2")
	List<BigInteger> findEmployeesEligibleForSubsequentTravelReminder(String emailType, int reminderCount);

	@Query(nativeQuery = true, value = "SELECT t2.emp_id FROM employee t1, flight_info t2 "
			+ "WHERE t1.emp_id = t2.emp_id and TRAVEL_CONFIRMATION_DATE_TIME is null and "
			+ "t1.registration_no > 0  and t2.airline_name <> 'NA' "
			+ "group by t2.emp_id having count(t2.flight_type)>=2")
	List<BigInteger> findEmployeesEligibleForTravelConfirmation();

	@Query(nativeQuery = true, value = "SELECT t2.emp_id FROM employee t1, flight_info t2 "
			+ "WHERE t1.emp_id = t2.emp_id and FLIGHT_REMINDER_LAST_SENT is null and "
			+ "t1.registration_no > 0  and t2.airline_name <> 'NA' "
			+ "group by t2.emp_id having count(t2.flight_type)=1")
	List<BigInteger> findEmployeesFilledPartialTravelDetails();

}
