package com.tcs.novia.controller;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tcs.novia.constant.Constants;
import com.tcs.novia.model.EmailTemplate;
import com.tcs.novia.model.Employee;
import com.tcs.novia.repository.EmailTemplateRepository;
import com.tcs.novia.repository.EmployeeRepository;
import com.tcs.novia.service.ConfigurationService;
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
	@Autowired
	private ConfigurationService configurationService;

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

	@RequestMapping(value = "/sendLogisticEmail", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Employee> sendLogisticEmail(@RequestParam(value = "employeeID", required = false) final Long employeeID) {
		return emailService.sendLogisticEmail(employeeID);
	}
	@RequestMapping(value = "/sendGiftAmendmentEmail", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Employee> sendGiftAmendmentEmail(@RequestParam(value = "employeeID", required = false) final Long employeeID) {
		return emailService.sendGiftAmendmentEmail(employeeID);
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
	
	@RequestMapping(value = "/createTemplateFromJSON", method = RequestMethod.POST, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public EmailTemplate createTemplateFromJSON(@RequestBody EmailTemplate emailTemplate) {
		return emailTemplateRepository.save(emailTemplate);
	}
	
	@RequestMapping(value = "/createEmailTemplatesFromJSON", method = RequestMethod.POST, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public List<EmailTemplate> createEmailTemplatesFromJSON(@RequestBody List<EmailTemplate> emailTemplates) {
		return emailTemplateRepository.saveAll(emailTemplates);
	}

	@RequestMapping(value = "/getAllEmailTemplates", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<EmailTemplate> getAllEmailTemplates() {
		return emailTemplateRepository.findAll();
	}
	
	/******************************************		Confirm Participation START	******************************/
	
	@RequestMapping(value = "/sendConfirmationForParticipationEmail", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Employee sendConfirmationForParticipationEmail(
			@RequestParam(value = "employeeID", required = false) final Long employeeID) {
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
	
	///////////////////////////////////// Reminder
	
	@RequestMapping(value = "/findEmployeesEligibleForFirstParticipationReminder", method = RequestMethod.GET)
	@ResponseBody
	public List<BigInteger> findEmployeesEligibleForFirstParticipationReminder() {
		return employeeRepository.findEmployeesYetToConfirmParticipation(
				configurationService.getInterval(Constants.CONFIG_INTERVAL_DAY_CONFIRM_PARTICIPATION_REMINDER));
	}
	
	@RequestMapping(value = "/sendReminderToConfirmParticipation", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Employee> sendReminderToConfirmParticipation(@RequestParam(value = "employeeID", required = false) final Long employeeID) {
		return emailService.sendReminderToConfirmParticipation(employeeID);
	}

	@RequestMapping(value = "/findEmployeesEligibleForSecondConfirmParticipationReminder", method = RequestMethod.GET)
	@ResponseBody
	public List<BigInteger> findEmployeesEligibleForSecondConfirmParticipationReminder() {
		return employeeRepository.findEmployeesEligibleForSubsequentReminders(
				Constants.TEMPLATE_CONFIRM_PARTICIPATION_REMINDER_EMAIL, 1);
	}
	
	@RequestMapping(value = "/findEmployeesEligibleForConfirmParticipationEscalation", method = RequestMethod.GET)
	@ResponseBody
	public List<BigInteger> findEmployeesEligibleForConfirmParticipationEscalation() {
		return employeeRepository.findEmployeesEligibleForSubsequentReminders(
				Constants.TEMPLATE_CONFIRM_PARTICIPATION_REMINDER_EMAIL, 2);
	}

	@RequestMapping(value = "/sendSecondConfirmParticipationReminder", method = RequestMethod.GET,  produces = "application/json")
	@ResponseBody
	public List<Employee> sendSecondConfirmParticipationReminder(@RequestParam(value = "employeeID", required = false) final Long employeeID) {
		
		return emailService.sendSubsequentReminderEmails(employeeID, Constants.TEMPLATE_CONFIRM_PARTICIPATION_REMINDER_EMAIL, 
				Constants.TEMPLATE_SECOND_CONFIRM_PARTICIPATION_REMINDER_EMAIL, 1, false);
	}
	
	@RequestMapping(value = "/sendEscalationForConfirmParticipationReminder", method = RequestMethod.GET,  produces = "application/json")
	@ResponseBody
	public List<Employee> sendEscalationForConfirmParticipationReminder(@RequestParam(value = "employeeID", required = false) final Long employeeID) {
		
		return emailService.sendSubsequentReminderEmails(employeeID, Constants.TEMPLATE_CONFIRM_PARTICIPATION_REMINDER_EMAIL, 
				Constants.TEMPLATE_ESCALATION_CONFIRM_PARTICIPATION_REMINDER_EMAIL, 2, true);
	}

	/******************************************		Confirm Participation END	******************************/

	
	/******************************************		Complete Registration START	******************************/

	@RequestMapping(value = "/findEmployeesEligibleForFirstRegistrationReminder", method = RequestMethod.GET)
	@ResponseBody
	public List<BigInteger> findEmployeesEligibleForFirstRegistrationReminder() {
		return employeeRepository.findEmployeesYetToCompleteRegistration(
				configurationService.getInterval(Constants.CONFIG_INTERVAL_DAY_COMPLETE_REGISTRATION_REMINDER));
	}

	@RequestMapping(value = "/sendReminderToCompleteRegistration", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Employee> sendReminderToCompleteRegistration(@RequestParam(value = "employeeID", required = false) final Long employeeID) {
		return emailService.sendReminderToCompleteRegistration(employeeID);
	}

	@RequestMapping(value = "/findEmployeesEligibleForSecondRegistrationReminder", method = RequestMethod.GET)
	@ResponseBody
	public List<BigInteger> findEmployeesEligibleForSecondRegistrationReminder() {
		return employeeRepository
				.findEmployeesEligibleForSubsequentReminders(Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL, 1);
	}

	@RequestMapping(value = "/findEmployeesEligibleForRegistrationReminderEscalation", method = RequestMethod.GET)
	@ResponseBody
	public List<BigInteger> findEmployeesEligibleForRegistrationReminderEscalation() {
		return employeeRepository
				.findEmployeesEligibleForSubsequentReminders(Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL, 2);
	}
	
	@RequestMapping(value = "/sendSecondCompleteRegistrationReminder", method = RequestMethod.GET)
	@ResponseBody
	public List<Employee> sendSecondCompleteRegistrationReminder(@RequestParam(value = "employeeID", required = false) final Long employeeID) {
		return emailService.sendSubsequentReminderEmails(employeeID, Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL, 
				Constants.TEMPLATE_SECOND_REGISTRATION_REMINDER_EMAIL, 1, false);
	}

	@RequestMapping(value = "/sendEscalationForCompleteRegistrationReminder", method = RequestMethod.GET)
	@ResponseBody
	public List<Employee> sendEscalationForCompleteRegistrationReminder(@RequestParam(value = "employeeID", required = false) final Long employeeID) {
		return emailService.sendSubsequentReminderEmails(employeeID, Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL, 
				Constants.TEMPLATE_ESCALATION_REGISTRATION_REMINDER_EMAIL, 2, true);
	}

	/******************************************		Complete Registration END	******************************/

	/******************************************		Complete Flight Details START	******************************/

	@RequestMapping(value = "/findEmployeesEligibleForFirstTravelReminder", method = RequestMethod.GET)
	@ResponseBody
	public List<BigInteger> findEmployeesEligibleForFirstTravelReminder() {
		return employeeRepository.findEmployeesYetToFillFlightInfo(
				configurationService.getInterval(Constants.CONFIG_INTERVAL_DAY_FLIGHT_REMINDER));
	}

	@RequestMapping(value = "/sendReminderToCompleteFlightDetails", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Employee> sendReminderToCompleteFlightDetails(@RequestParam(value = "employeeID", required = false) final Long employeeID) {
		return emailService.sendReminderToCompleteFlightDetails(employeeID);
	}
	
	
	@RequestMapping(value = "/findEmployeesEligibleForSecondTravelReminder", method = RequestMethod.GET)
	@ResponseBody
	public List<BigInteger> findEmployeesEligibleForSecondTravelReminder() {
		return employeeRepository
				.findEmployeesEligibleForSubsequentReminders(Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL, 1);
	}

	@RequestMapping(value = "/findEmployeesEligibleForTravelReminderEscalation", method = RequestMethod.GET)
	@ResponseBody
	public List<BigInteger> findEmployeesEligibleForTravelReminderEscalation() {
		return employeeRepository
				.findEmployeesEligibleForSubsequentReminders(Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL, 2);
	}

	
	@RequestMapping(value = "/sendSecondFlightUpdateReminder", method = RequestMethod.GET)
	@ResponseBody
	public List<Employee> sendSecondFlightUpdateReminder(@RequestParam(value = "employeeID", required = false) final Long employeeID) {
		return emailService.sendSubsequentReminderEmails(employeeID, Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL, 
				Constants.TEMPLATE_SECOND_FLIGHT_UPDATE_REMINDER_EMAIL, 1, false);
	}

	@RequestMapping(value = "/sendEscalationForFlightUpdateReminder", method = RequestMethod.GET)
	@ResponseBody
	public List<Employee> sendEscalationForFlightUpdateReminder(@RequestParam(value = "employeeID", required = false) final Long employeeID) {
		return emailService.sendSubsequentReminderEmails(employeeID, Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL, 
				Constants.TEMPLATE_ESCALATION_FLIGHT_UPDATE_REMINDER_EMAIL, 2, true);
	}

	/******************************************		Complete Flight Details END	******************************/

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
				if(Constants.INPUT_BLANK_VALUE.equalsIgnoreCase(emailTo)) {
					emailTemplate.setEmailTo(null);
				}else {
					emailTemplate.setEmailTo(emailTo);
				}
			}
			if (StringUtils.isNotBlank(emailCC)) {
				if(Constants.INPUT_BLANK_VALUE.equalsIgnoreCase(emailCC)) {
					emailTemplate.setEmailCC(null);
				}else {
					emailTemplate.setEmailCC(emailCC);
				}
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
				emailTemplate.setDisabled(BooleanUtils.toBoolean(disabled));
			}
			emailTemplateRepository.save(emailTemplate);
		}

		return emailTemplate;
	}
}