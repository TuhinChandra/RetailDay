package com.tcs.novia.service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.novia.constant.Constants;
import com.tcs.novia.exception.CustomException;
import com.tcs.novia.model.Employee;
import com.tcs.novia.model.enums.CityTour;
import com.tcs.novia.model.enums.FoodPreference;
import com.tcs.novia.model.enums.ShirtSize;
import com.tcs.novia.model.enums.UserState;
import com.tcs.novia.repository.EmployeeRepository;
import com.tcs.novia.util.PasswordCrypto;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private EmailService emailService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	PasswordCrypto passwordCrypto;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public Employee register(final long employeeID, final String firstName, final String lastName,
			final String shortName, final String gender, final String emailID, final String password,
			final String role) {
		final Employee employee = new Employee(employeeID, emailID, firstName, lastName, shortName,
				gender.toUpperCase(), passwordCrypto.encrypt(password));

		if (employee.getStatus() != UserState.COMPLETE) {
			employee.setStatus(UserState.CHANGE_DEFAULT_PASSWORD);
		}
		if (!StringUtils.isEmpty(role)) {
			employee.setRole(role.toUpperCase());
		}
		employee.setCreatedDateTime(LocalDateTime.now());
		final Employee emp = employeeRepository.save(employee);
		if (configurationService.shouldSendEmail()) {
			emailService.sendInvitationEmail(emp);
		}

		return emp;
	}

	public List<Employee> getAllEmployee() {
		return employeeRepository.findAll();
	}

	public void deleteEmployee(final long employeeID) {
		employeeRepository.deleteById(employeeID);
	}

	public void deleteAllEmployees() {
		employeeRepository.deleteAll();
	}

	public Employee completeProfile(final Employee employee, final String firstName, final String lastName,
			final String emailID, final String foodPreference, final String mobile, final String cityTour,
			final String photo, final String gender, final String shirtSize, final String gift,
			final String accomodationNeeded, final String pickupAddress) {
		update(employee, firstName, lastName, emailID, foodPreference, null, mobile, cityTour, photo, gender, shirtSize,
				gift, accomodationNeeded, null, "true", null, pickupAddress);
		if (employee.getRegistrationNo() == 0) {
			employee.setRegistrationNo(employeeRepository.getLastRegistrationNo() + 1);
			employee.setStatus(UserState.COMPLETE);
			employee.setConfirmParticipation(Constants.CONFIRM_PARTICIPATION_YES);
			employee.setRegistrationDateTime(LocalDateTime.now());
		}
		return employeeRepository.save(employee);
	}

	public Employee confirmParticipation(final Employee employee) {
		if (!Constants.CONFIRM_PARTICIPATION_YES.equalsIgnoreCase(employee.getConfirmParticipation())) {
			employee.setConfirmParticipation(Constants.CONFIRM_PARTICIPATION_YES);
			employee.setParticipationDateTime(LocalDateTime.now());
			employeeRepository.save(employee);
			if (configurationService.shouldSendEmail()) {
				emailService.sendConfirmationForParticipationEmail(employee);
			}
		}
		return employee;
	}

	public Employee rejectParticipation(final Employee employee) {

		employee.setConfirmParticipation(Constants.CONFIRM_PARTICIPATION_REJECTED);
		employee.setParticipationDateTime(LocalDateTime.now());
		employee.setStatus(UserState.INVALID);
		employeeRepository.save(employee);
		if (configurationService.shouldSendEmail()) {
			emailService.sendRejectedParticipationEmail(employee);
		}
		return employee;
	}

	public Employee checkIn(final Employee employee) {
		employee.setCheckedIn(true);
		employee.setCheckInTime(LocalDateTime.now());
		employeeRepository.save(employee);
		return employee;
	}

	public List<Employee> updateUploadUserDate() {

		final List<Employee> employees = employeeRepository.findByCreatedDateTimeIsNull();
		if (null != employees && !employees.isEmpty()) {
			for (final Employee employee : employees) {
				updateUploadDate(employee);
			}
		}
		return employees;
	}

	private void updateUploadDate(final Employee employee) {
		employee.setCreatedDateTime(LocalDateTime.now());
		LOGGER.trace("updateUploadDate:: Updated date to {} for employee::{}", employee.getCreatedDateTime(),
				employee.getEmployeeID());
		employeeRepository.save(employee);
	}

	public List<Employee> updateColorBand(final String[] employeeIDs, final String color) {
		List<Employee> employeesWithUpdatedColorBand = null;
		if (null != employeeIDs && employeeIDs.length > 0 && StringUtils.isNotBlank(color)) {
			employeesWithUpdatedColorBand = new ArrayList<>();
			for (final String empId : employeeIDs) {
				final Employee emp = findByEmployeeID(Long.parseLong(empId));
				if (null != emp) {
					emp.setColorBand(color.toUpperCase());
					employeesWithUpdatedColorBand.add(saveEmployeeData(emp));
				}
			}
		}
		return employeesWithUpdatedColorBand;
	}

	public Employee update(final Employee employee, final String firstName, final String lastName, final String emailID,
			final String foodPreference, final String password, final String mobile, final String cityTour,
			final String photo, final String gender, final String shirtSize, final String gift,
			final String accomodationNeeded, final String confirmParticipation, final String flightUpdateNeeded,
			final String role, final String pickupAddress) {
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
			employee.setGender(gender.toUpperCase());
		}
		if (!StringUtils.isEmpty(shirtSize)) {
			employee.setShirtSize(ShirtSize.valueOf(shirtSize.toUpperCase()));
		}
		if (!StringUtils.isEmpty(gift)) {
			employee.setGift(gift);
		}

		if (!StringUtils.isEmpty(accomodationNeeded)) {
			if (Constants.INPUT_BLANK_VALUE.equalsIgnoreCase(accomodationNeeded)) {
				employee.setAccomodationNeeded(null);
			} else {
				employee.setAccomodationNeeded(accomodationNeeded);
			}
		}
		if (!StringUtils.isEmpty(confirmParticipation)) {
			if (Constants.INPUT_BLANK_VALUE.equalsIgnoreCase(confirmParticipation)) {
				employee.setConfirmParticipation(null);
				employee.setStatus(UserState.CHANGE_DEFAULT_PASSWORD);
				employee.setParticipationDateTime(null);
			} else {
				employee.setConfirmParticipation(confirmParticipation);
			}
		}
		if (!StringUtils.isEmpty(flightUpdateNeeded)) {
			employee.setFlightUpdateNeeded(Boolean.parseBoolean(flightUpdateNeeded));
		}
		if (!StringUtils.isEmpty(role)) {
			if (Constants.INPUT_BLANK_VALUE.equalsIgnoreCase(role)) {
				employee.setRole(null);
			} else {
				employee.setRole(role);
			}
		}

		if (Constants.NO_PICKUP.equalsIgnoreCase(pickupAddress)) {
			employee.setPickupAddress(null);
			employee.setLocalTransportNeeded(false);
		} else if (!StringUtils.isEmpty(pickupAddress)) {
			employee.setPickupAddress(pickupAddress);
			employee.setLocalTransportNeeded(true);
			employee.setAccomodationNeeded(Constants.ACCOMODATION_NEEDED_NO);
		}

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

	public Employee getEmployeestatus(final long employeeID, final String password) {
		final Employee employee = findByEmployeeID(employeeID);
		if (null != employee) {
			if (!passwordCrypto.validate(password, employee.getPassword())) {
				final long count = employee.getCountOfInvalidLoginAttempt();
				employee.setCountOfInvalidLoginAttempt(count + 1);
				employeeRepository.save(employee);
				employee.setStatus(UserState.INVALID);
			}
		}
		return employee;
	}

	public Employee saveEmployeeData(final Employee employee) {
		return employeeRepository.save(employee);
	}

	public Employee changePassword(final Employee employee, final String newPassword) {

		employee.setPassword(passwordCrypto.encrypt(newPassword));
		if (employee.getRegistrationNo() > 0) {// Already completed registration but then reset password
			employee.setStatus(UserState.COMPLETE);
		} else if (employee.getStatus() == UserState.CHANGE_DEFAULT_PASSWORD) {// First time changing password
			employee.setStatus(UserState.INCOMPLETE);
		}
		employee.setCountOfInvalidLoginAttempt(0);
		return employeeRepository.save(employee);
	}

	public Employee resetPassword(final Employee employee, final String newPassword) {
		employee.setPassword(passwordCrypto.encrypt(newPassword));
		employee.setStatus(UserState.CHANGE_DEFAULT_PASSWORD);
		employee.setCountOfInvalidLoginAttempt(0);
		final Employee emp = employeeRepository.save(employee);
		if (configurationService.shouldSendEmail()) {
			emailService.sendForgotPasswordEmail(emp, newPassword);
		}
		return emp;
	}

	public List<Employee> findEmployeesEligibleForSubsequentParticipationReminders(final Long employeeIDToMatch,
			final String emailContext, final int reminderCount) {

		LOGGER.info(
				"findEmployeesEligibleForSubsequentParticipationReminders:: method parameters:: employeeIDToMatch::{}, emailContext::{}, reminderCount::{}",
				employeeIDToMatch, emailContext, reminderCount);

		final List<BigInteger> employeeIDs = employeeRepository
				.findEmployeesEligibleForSubsequentParticipationReminders(emailContext, reminderCount);
		LOGGER.info("findEmployeesEligibleForSubsequentParticipationReminders:: result:: employeeIDs::{}", employeeIDs);
		return getEmployeesByID(employeeIDs, employeeIDToMatch);
	}

	public List<Employee> findEmployeesEligibleForSubsequentRegistrationReminders(final Long employeeIDToMatch,
			final String emailContext, final int reminderCount) {

		LOGGER.info(
				"findEmployeesEligibleForSubsequentRegistrationReminders:: method parameters:: employeeIDToMatch::{}, emailContext::{}, reminderCount::{}",
				employeeIDToMatch, emailContext, reminderCount);

		final List<BigInteger> employeeIDs = employeeRepository
				.findEmployeesEligibleForSubsequentRegistrationReminders(emailContext, reminderCount);
		LOGGER.info("findEmployeesEligibleForSubsequentRegistrationReminders:: result:: employeeIDs::{}", employeeIDs);
		return getEmployeesByID(employeeIDs, employeeIDToMatch);
	}

	public List<Employee> findEmployeesEligibleForSubsequentTravelReminders(final Long employeeIDToMatch,
			final String emailContext, final int reminderCount) {

		LOGGER.info(
				"findEmployeesEligibleForSubsequentTravelReminders:: method parameters:: employeeIDToMatch::{}, emailContext::{}, reminderCount::{}",
				employeeIDToMatch, emailContext, reminderCount);

		final List<BigInteger> employeeIDs = employeeRepository
				.findEmployeesEligibleForSubsequentTravelReminder(emailContext, reminderCount);

		final List<BigInteger> employeeIDsPartiallyFilledTravel = employeeRepository
				.findEmployeesFilledPartialTravelDetails();

		final List<BigInteger> eligibleEmployeeIDs = new ArrayList<>();

		if (null != employeeIDs && !employeeIDs.isEmpty()) {
			eligibleEmployeeIDs.addAll(employeeIDs);
		}
		if (null != employeeIDsPartiallyFilledTravel && !employeeIDsPartiallyFilledTravel.isEmpty()) {
			eligibleEmployeeIDs.addAll(employeeIDsPartiallyFilledTravel);
		}

		LOGGER.info("findEmployeesEligibleForSubsequentTravelReminders:: result:: employeeIDs::{}",
				eligibleEmployeeIDs);
		return getEmployeesByID(eligibleEmployeeIDs, employeeIDToMatch);
	}

	public List<Employee> fetchNotConfirmedParticipants(final Long employeeID) {

		final List<BigInteger> employeeIDs = employeeRepository.findEmployeesYetToConfirmParticipation(
				configurationService.getInterval(Constants.CONFIG_INTERVAL_DAY_CONFIRM_PARTICIPATION_REMINDER));
		LOGGER.info("fetchNotConfirmedParticipants:: result:: employeeIDs::{}", employeeIDs);
		return getEmployeesByID(employeeIDs, employeeID);
	}

	public List<Employee> fetchIncompleteProfiles(final Long employeeID) {
		final List<BigInteger> employeeIDs = employeeRepository.findEmployeesYetToCompleteRegistration(
				configurationService.getInterval(Constants.CONFIG_INTERVAL_DAY_COMPLETE_REGISTRATION_REMINDER));
		LOGGER.info("fetchIncompleteProfiles:: result:: employeeIDs::{}", employeeIDs);
		return getEmployeesByID(employeeIDs, employeeID);
	}

	public List<Employee> findEmployeesNotProvidedFlightDetails(final Long employeeID) {
		final List<BigInteger> employeeIDs = employeeRepository.findEmployeesYetToFillFlightInfo(
				configurationService.getInterval(Constants.CONFIG_INTERVAL_DAY_FLIGHT_REMINDER));
		LOGGER.info("findEmployeesNotProvidedFlightDetails:: result:: employeeIDs::{}", employeeIDs);
		return getEmployeesByID(employeeIDs, employeeID);
	}

	public List<Employee> findEmployeesWhoProvidedCompleteFlightDetails(final Long employeeID) {
		final List<BigInteger> employeeIDs = employeeRepository.findEmployeesEligibleForTravelConfirmation();
		LOGGER.info("findEmployeesWhoProvidedCompleteFlightDetails:: result:: employeeIDs::{}", employeeIDs);
		return getEmployeesByID(employeeIDs, employeeID);
	}

	private List<Employee> getEmployeesByID(final List<BigInteger> employeeIDs, final Long employeeIdToMatch) {
		List<Employee> employees = null;
		if (null != employeeIDs && !employeeIDs.isEmpty()) {
			employees = new ArrayList<>();
			if (null != employeeIdToMatch) {
				LOGGER.trace("Going to match against single employee ID::{}", employeeIdToMatch);
				final BigInteger bigIntEmpIdToMatch = BigInteger.valueOf(employeeIdToMatch);
				if (employeeIDs.contains(bigIntEmpIdToMatch)) {
					final Optional<Employee> optional = employeeRepository.findById(employeeIdToMatch);
					if (optional.isPresent()) {
						employees.add(optional.get());
					}
				} else {
					LOGGER.warn("Didn't find matching records against emp id::{}", employeeIdToMatch);
				}
			} else {
				for (final BigInteger employeeID : employeeIDs) {
					final long empID = Long.parseLong(String.valueOf(employeeID));
					final Optional<Employee> optional = employeeRepository.findById(empID);
					if (optional.isPresent()) {
						employees.add(optional.get());
					}
				}
			}
		}
		return employees;
	}

	public List<String> fetchEmailIDs(final List<Employee> employeeList) {
		final List<String> emailIDs = new ArrayList<>();
		if (null != employeeList && !employeeList.isEmpty()) {
			for (final Employee emp : employeeList) {
				emailIDs.add(emp.getEmailID());
			}
		}
		return emailIDs;
	}

	public List<Employee> getEmployees() {
		return employeeRepository.findAll();
	}

	public List<Employee> saveBulkEmployees(final List<Employee> employees) {

		return employeeRepository.saveAll(employees);
	}

	public int getCountOfExpectedDelegates() {
		return employeeRepository.countByConfirmParticipationIsNullOrConfirmParticipationAndRoleIsNull(
				Constants.CONFIRM_PARTICIPATION_YES);
	}

	public void preValidationBeforeUpload() throws CustomException {
		final int maxDelegateCount = configurationService.getMaximumDelegateCount();
		final int existingDelegateCount = getCountOfExpectedDelegates();

		if (maxDelegateCount == 0) {
			throw new CustomException("Please configure MAX delegate count into system");
		} else if (existingDelegateCount > maxDelegateCount) {
			throw new CustomException("There is no provision to upload any more users as capacity is full");
		}
	}

	public void postValidationBeforeUpload(final List<Employee> employees) throws CustomException {
		final int maxDelegateCount = configurationService.getMaximumDelegateCount();
		final int existingDelegateCount = getCountOfExpectedDelegates();

		if (null != employees && !employees.isEmpty()) {

			final int noOfUsersCanBeProvisioned = maxDelegateCount - existingDelegateCount;

			if (noOfUsersCanBeProvisioned < employees.size()) {
				throw new CustomException("Only " + noOfUsersCanBeProvisioned
						+ " more users can be added. No. of users you are trying to upload:" + employees.size());
			}

		} else {
			throw new CustomException("There is no users in the CSV");
		}
	}

}
