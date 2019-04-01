package com.tcs.novia.controller;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tcs.novia.constant.Constants;
import com.tcs.novia.model.EmailTemplate;
import com.tcs.novia.model.Employee;
import com.tcs.novia.repository.EmailTemplateRepository;
import com.tcs.novia.repository.EmployeeRepository;
import com.tcs.novia.service.EmailService;
import com.tcs.novia.service.EmployeeService;

@Controller
public class EmailController {

	@Autowired
	private EmailService emailService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private EmailTemplateRepository emailTemplateRepository;
	@Autowired
	private EmployeeRepository employeeRepository;

	@RequestMapping(value = "/sendForgotPasswordEmail", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Employee sendForgotPasswordEmail(@RequestParam(value = "employeeID", required = true) final long employeeID,
			@RequestParam(value = "newPassword", required = true) final String newPassword) {
		final Employee emp = employeeService.findByEmployeeID(employeeID);
		if (null != emp) {
			employeeService.resetPassword(emp, newPassword);
		}
		return emp;
	}

	@RequestMapping(value = "/sendConfirmationForParticipationEmail", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Employee sendConfirmationForParticipationEmail(
			@RequestParam(value = "employeeID", required = true) final long employeeID) {
		final Employee emp = employeeService.findByEmployeeID(employeeID);
		if (null != emp) {
			emailService.sendConfirmationForParticipationEmail(emp);
		}
		return emp;
	}

	@RequestMapping(value = "/sendConfirmationForRejectionEmail", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Employee sendConfirmationForRejectionEmail(
			@RequestParam(value = "employeeID", required = true) final long employeeID) {
		final Employee emp = employeeService.findByEmployeeID(employeeID);
		if (null != emp) {
			emailService.sendRejectedParticipationEmail(emp);
		}
		return emp;
	}

	@RequestMapping(value = "/sendReminderToConfirmParticipation/{employeeID}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<Employee> sendReminderToConfirmParticipation(@PathVariable("employeeID") final long employeeID) {
		return emailService.sendReminderToConfirmParticipation(employeeID);
	}

	@RequestMapping(value = "/sendReminderToConfirmParticipation", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Employee> sendReminderToConfirmParticipation() {
		return emailService.sendReminderToConfirmParticipation();
	}

	@RequestMapping(value = "/sendReminderToCompleteRegistration/{employeeID}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<Employee> sendReminderToCompleteRegistration(@PathVariable("employeeID") final long employeeID) {
		return emailService.sendReminderToCompleteRegistration(employeeID);
	}

	@RequestMapping(value = "/sendReminderToCompleteRegistration", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Employee> sendReminderToCompleteRegistration() {
		return emailService.sendReminderToCompleteRegistration();
	}

	@RequestMapping(value = "/sendReminderToCompleteFlightDetails/{employeeID}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<Employee> sendReminderToCompleteFlightDetails(@PathVariable("employeeID") final long employeeID) {
		return emailService.sendReminderToCompleteFlightDetails(employeeID);
	}

	@RequestMapping(value = "/sendReminderToCompleteFlightDetails", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Employee> sendReminderToCompleteFlightDetails() {
		return emailService.sendReminderToCompleteFlightDetails();
	}
	@RequestMapping(value = "/sendLogisticEmail", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Employee> sendLogisticEmail(@RequestParam(value = "employeeID", required = false) final Long employeeID) {
		return emailService.sendLogisticEmail(employeeID);
	}

	@RequestMapping(value = "/createTemplate", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public EmailTemplate createTemplate(
			@RequestParam(value = "templateName", required = true) final String templateName,
			@RequestParam(value = "emailFrom", required = true) final String emailFrom,
			@RequestParam(value = "emailTo", required = false) final String emailTo,
			@RequestParam(value = "emailCC", required = false) final String emailCC,
			@RequestParam(value = "emailFromAlias", required = false) final String emailFromAlias,
			@RequestParam(value = "subject", required = true) final String subject,
			@RequestParam(value = "disabled", required = false) final String disabled,
			@RequestParam(value = "bodyContent", required = true) final String bodyContent) {

		return emailTemplateRepository.save(new EmailTemplate(templateName, emailFrom, emailTo, emailCC, emailFromAlias,
				subject, bodyContent, BooleanUtils.toBooleanObject(disabled)));
	}

	@RequestMapping(value = "/getAllEmailTemplates", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<EmailTemplate> getAllEmailTemplates() {
		return emailTemplateRepository.findAll();
	}

	@RequestMapping(value = "/findEmployeesEligibleForSecondConfirmParticipationReminder", method = RequestMethod.GET)
	@ResponseBody
	public List<BigInteger> findEmployeesEligibleForSecondConfirmParticipationReminder() {
		return employeeRepository.findEmployeesEligibleForSubsequentReminders(
				Constants.TEMPLATE_CONFIRM_PARTICIPATION_REMINDER_EMAIL, 1, 3);
	}

	@RequestMapping(value = "/findEmployeesEligibleForConfirmParticipationEscalation", method = RequestMethod.GET)
	@ResponseBody
	public List<BigInteger> findEmployeesEligibleForConfirmParticipationEscalation() {
		return employeeRepository.findEmployeesEligibleForSubsequentReminders(
				Constants.TEMPLATE_CONFIRM_PARTICIPATION_REMINDER_EMAIL, 2, 3);
	}

	@RequestMapping(value = "/findEmployeesEligibleForSecondRegistrationReminder", method = RequestMethod.GET)
	@ResponseBody
	public List<BigInteger> findEmployeesEligibleForSecondRegistrationReminder() {
		return employeeRepository
				.findEmployeesEligibleForSubsequentReminders(Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL, 1, 3);
	}

	@RequestMapping(value = "/findEmployeesEligibleForRegistrationReminderEscalation", method = RequestMethod.GET)
	@ResponseBody
	public List<BigInteger> findEmployeesEligibleForRegistrationReminderEscalation() {
		return employeeRepository
				.findEmployeesEligibleForSubsequentReminders(Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL, 2, 3);
	}

	@RequestMapping(value = "/findEmployeesEligibleForSecondTravelReminder", method = RequestMethod.GET)
	@ResponseBody
	public List<BigInteger> findEmployeesEligibleForSecondTravelReminder() {
		return employeeRepository
				.findEmployeesEligibleForSubsequentReminders(Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL, 1, 3);
	}

	@RequestMapping(value = "/findEmployeesEligibleForTravelReminderEscalation", method = RequestMethod.GET)
	@ResponseBody
	public List<BigInteger> findEmployeesEligibleForTravelReminderEscalation() {
		return employeeRepository
				.findEmployeesEligibleForSubsequentReminders(Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL, 2, 3);
	}

	@RequestMapping(value = "/deleteEmailTemplate", method = RequestMethod.POST)
	@ResponseBody
	public void deleteEmailTemplate(@RequestParam(value = "templateName", required = true) final String templateName) {
		emailTemplateRepository.deleteById(templateName);
	}

	@RequestMapping(value = "/updateTemplate", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public EmailTemplate updateTemplate(
			@RequestParam(value = "templateName", required = true) final String templateName,
			@RequestParam(value = "emailFrom", required = false) final String emailFrom,
			@RequestParam(value = "emailTo", required = false) final String emailTo,
			@RequestParam(value = "emailCC", required = false) final String emailCC,
			@RequestParam(value = "emailFromAlias", required = false) final String emailFromAlias,
			@RequestParam(value = "subject", required = false) final String subject,
			@RequestParam(value = "disabled", required = false) final String disabled,
			@RequestParam(value = "bodyContent", required = false) final String bodyContent) {

		final Optional<EmailTemplate> optional = emailTemplateRepository.findById(templateName);
		EmailTemplate emailTemplate = null;
		if (optional.isPresent()) {
			emailTemplate = optional.get();
			if (StringUtils.isNotBlank(emailFrom)) {
				emailTemplate.setEmailFrom(emailFrom);
			}
			if (StringUtils.isNotBlank(emailTo)) {
				emailTemplate.setEmailTo(emailTo);
			}
			if (StringUtils.isNotBlank(emailCC)) {
				emailTemplate.setEmailCC(emailCC);
			}
			if (StringUtils.isNotBlank(emailFromAlias)) {
				emailTemplate.setEmailFromAlias(emailFromAlias);
			}
			if (StringUtils.isNotBlank(subject)) {
				emailTemplate.setSubject(subject);
			}
			if (StringUtils.isNotBlank(bodyContent)) {
				emailTemplate.setBodyContent(bodyContent);
			}
			if (StringUtils.isNotBlank(disabled)) {
				emailTemplate.setDisabled(BooleanUtils.toBooleanObject(disabled));
			}
			emailTemplateRepository.save(emailTemplate);
		}

		return emailTemplate;
	}
}