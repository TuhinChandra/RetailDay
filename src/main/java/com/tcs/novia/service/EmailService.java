package com.tcs.novia.service;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tcs.novia.constant.Constants;
import com.tcs.novia.exception.CustomException;
import com.tcs.novia.model.Email;
import com.tcs.novia.model.EmailTemplate;
import com.tcs.novia.model.EmailTracker;
import com.tcs.novia.model.Employee;
import com.tcs.novia.repository.EmailTemplateRepository;
import com.tcs.novia.repository.EmployeeRepository;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender sender;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private EmailTemplateRepository emailTemplateRepository;

	@Value("${app.login.url}")
	private String appLoginUrl;
	@Value("${app.point.of.contact}")
	private String appPointOfContact;
	@Value("${emp_default_password}")
	private String defaultEmployeePassword;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public EmailTemplate getEmailTemplateByName(final String templateName) {
		final Optional<EmailTemplate> optionalEmailTemplte = emailTemplateRepository.findById(templateName);
		EmailTemplate emailTemplate = null;
		if (optionalEmailTemplte.isPresent()) {
			emailTemplate = emailTemplateRepository.findById(templateName).get();
			if (BooleanUtils.isTrue(emailTemplate.isDisabled())) {
				emailTemplate = null;
			}
		}
		return emailTemplate;
	}

	public List<Employee> sendReminderToConfirmParticipation() {
		return sendReminderToConfirmParticipation(null);
	}

	public List<Employee> sendReminderToConfirmParticipation(final Long employeeID) {
		LOGGER.debug("Entered into sendReminderToConfirmParticipation with parameter::{}", employeeID);
		List<Employee> employees = null;
		if (configurationService.shouldSendEmail()) {
			employees = employeeService.fetchNotConfirmedParticipants(employeeID);
			if (null != employees && !employees.isEmpty()) {
				for (final Employee emp : employees) {
					sendReminderEmail(emp, Constants.TEMPLATE_CONFIRM_PARTICIPATION_REMINDER_EMAIL,
							Constants.TEMPLATE_CONFIRM_PARTICIPATION_REMINDER_EMAIL, false);
				}
			}
		}
		LOGGER.trace("Exit from sendReminderToConfirmParticipation with parameter::{}", employees);
		return employees;
	}

	public List<Employee> sendSubsequentReminderEmails(final Long employeeID, final String emailContext,
			final String emailTemplate, final int reminderCount, final boolean shouldEscalate) {
		LOGGER.trace("Entered into sendSubsequentReminderEmails with parameter::{}", employeeID);
		List<Employee> employees = null;
		if (configurationService.shouldSendEmail()) {
			employees = employeeService.findEmployeesEligibleForSubsequentReminders(employeeID, emailContext,
					reminderCount);
			if (null != employees && !employees.isEmpty()) {
				for (final Employee emp : employees) {
					sendReminderEmail(emp, emailTemplate, emailContext, shouldEscalate);
				}
			}
		}
		LOGGER.trace("Exit from sendSubsequentReminderEmails with parameter::{}", employees);
		return employees;
	}

	public List<Employee> sendReminderToCompleteRegistration() {
		return sendReminderToCompleteRegistration(null);
	}

	public List<Employee> sendReminderToCompleteRegistration(final Long employeeID) {
		LOGGER.debug("Entered into sendReminderToCompleteRegistration with parameter::{}", employeeID);
		List<Employee> employees = null;
		if (configurationService.shouldSendEmail()) {
			employees = employeeService.fetchIncompleteProfiles(employeeID);
			if (null != employees && !employees.isEmpty()) {
				for (final Employee emp : employees) {
					sendRegistrationReminderEmail(emp);
				}
			}
		}
		LOGGER.trace("Exit from sendReminderToCompleteRegistration with parameter::{}", employees);
		return employees;
	}

	public List<Employee> sendReminderToCompleteFlightDetails() {
		return sendReminderToCompleteFlightDetails(null);
	}

	public List<Employee> sendReminderToCompleteFlightDetails(final Long employeeID) {
		LOGGER.debug("Entered into sendReminderToCompleteFlightDetails with parameter::{}", employeeID);
		List<Employee> employees = null;
		if (configurationService.shouldSendEmail()) {
			employees = employeeService.findEmployeesNotProvidedFlightDetails(employeeID);
			if (null != employees && !employees.isEmpty()) {
				for (final Employee emp : employees) {
					sendFlightUpdateReminderEmail(emp);
				}
			}
		}
		LOGGER.trace("Exit from sendReminderToCompleteFlightDetails with parameter::{}", employees);
		return employees;
	}

	@Async
	public void sendConfirmationForParticipationEmail(final Employee emp) {

		try {
			sendEmailWithTemplate(emp, Constants.TEMPLATE_CONFIRMED_PARTICIPATION_EMAIL,
					Constants.TEMPLATE_CONFIRMED_PARTICIPATION_EMAIL, false);
		} catch (UnsupportedEncodingException | MessagingException e) {
			handleExceptionScenario(emp, e, Constants.TEMPLATE_CONFIRMED_PARTICIPATION_EMAIL, Constants.TEMPLATE_CONFIRMED_PARTICIPATION_EMAIL);
		} catch (final CustomException e) {
			LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
		}
	}

	@Async
	public void sendForgotPasswordEmail(final Employee emp, final String newPassword) {

		final EmailTemplate emailTemplate = getEmailTemplateByName(Constants.TEMPLATE_FORGOT_PASSWORD_EMAIL);
		if (null != emailTemplate) {

			final String name = StringUtils.isNotBlank(emp.getShortName()) ? emp.getShortName() : emp.getFirstName();

			final String bodyContent = MessageFormat.format(emailTemplate.getBodyContent(), name, appLoginUrl,
					String.valueOf(emp.getEmployeeID()), newPassword, appPointOfContact);

			final Email email = fetchEmailInfo(emp, emailTemplate, bodyContent);

			try {
				sendEmailWithBody(emp, emailTemplate, email, Constants.TEMPLATE_FORGOT_PASSWORD_EMAIL);
			} catch (UnsupportedEncodingException | MessagingException e) {
				handleExceptionScenario(emp, e, Constants.TEMPLATE_FORGOT_PASSWORD_EMAIL, Constants.TEMPLATE_FORGOT_PASSWORD_EMAIL);
			} catch (final CustomException e) {
				LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
			}

		} else {
			LOGGER.warn("No Email Template found. Unable to send email for ::{}",
					Constants.TEMPLATE_FORGOT_PASSWORD_EMAIL);
			logEmailTracker(emp, "NO_EMAIL_TEMPLATE", false,
					"No Email Template found for " + Constants.TEMPLATE_FORGOT_PASSWORD_EMAIL, "NO_EMAIL_TEMPLATE");
		}
	}

	@Async
	public void sendRejectedParticipationEmail(final Employee emp) {
		try {
			sendEmailWithTemplate(emp, Constants.TEMPLATE_REJECTED_PARTICIPATION_EMAIL,
					Constants.TEMPLATE_REJECTED_PARTICIPATION_EMAIL, true);
		} catch (UnsupportedEncodingException | MessagingException e) {
			handleExceptionScenario(emp, e, Constants.TEMPLATE_REJECTED_PARTICIPATION_EMAIL, Constants.TEMPLATE_REJECTED_PARTICIPATION_EMAIL);
		} catch (final CustomException e) {
			LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
		}
	}

	@Async
	public void sendInvitationEmail(final Employee emp) {
		try {
			sendEmailWithTemplate(emp, Constants.TEMPLATE_REGISTRATION_EMAIL, Constants.TEMPLATE_REGISTRATION_EMAIL);
		} catch (UnsupportedEncodingException | MessagingException e) {
			handleExceptionScenario(emp, e, Constants.TEMPLATE_REGISTRATION_EMAIL, Constants.TEMPLATE_REGISTRATION_EMAIL);
		} catch (final CustomException e) {
			LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
		}
	}

	private void handleExceptionScenario(final Employee emp, final Exception e, final String emailContext,
			String templateName) {
		LOGGER.error("Could not send {} with templateName {} to : {} due to an error:: {}", emailContext, templateName,
				emp.getEmployeeID(), e.getMessage());
		logEmailTracker(emp, emailContext, false, e.getMessage(), templateName);
	}

	private String getEmailTo(final Employee emp, final EmailTemplate emailTemplate) {
		return StringUtils.isNotBlank(emailTemplate.getEmailTo()) ? emailTemplate.getEmailTo() : emp.getEmailID();
	}

	private String getEmailCc(final Employee emp, final EmailTemplate emailTemplate, final boolean skipEmployee) {
		return skipEmployee ? emp.getEmailID() : emailTemplate.getEmailCC();
	}

	public void sendReminderEmail(final Employee emp, final String templateName, final String emailContext,
			final boolean shouldEscalate) {
		try {
			sendEmailWithTemplate(emp, templateName, emailContext, shouldEscalate);
		} catch (UnsupportedEncodingException | MessagingException e) {
			handleExceptionScenario(emp, e, emailContext, templateName);
		} catch (final CustomException e) {
			LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
		}
	}

	public void sendRegistrationReminderEmail(final Employee emp) {
		try {
			sendEmailWithTemplate(emp, Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL,
					Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL);
		} catch (UnsupportedEncodingException | MessagingException e) {
			handleExceptionScenario(emp, e, Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL, Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL);
		} catch (final CustomException e) {
			LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
		}
	}

	public void sendFlightUpdateReminderEmail(final Employee emp) {
		try {
			sendEmailWithTemplate(emp, Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL,
					Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL);
		} catch (UnsupportedEncodingException | MessagingException e) {
			handleExceptionScenario(emp, e, Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL, Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL);
		} catch (final CustomException e) {
			LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
		}
	}

	public List<Employee> sendLogisticEmail(final Long employeeID) {
		LOGGER.debug("Entered into sendLogisticEmail with parameter::{}", employeeID);
		List<Employee> employees = null;
		if (configurationService.shouldSendEmail()) {
			employees = employeeRepository
					.findByConfirmParticipationAndLogisticsEmailSentIsNull(Constants.CONFIRM_PARTICIPATION_YES);
			if (null != employees && !employees.isEmpty()) {
				for (final Employee emp : employees) {
					if (null == employeeID || emp.getEmployeeID() == employeeID) {
						sendLogisticEmail(emp);
					}
				}
			}
		}
		LOGGER.trace("Exit from sendLogisticEmail with parameter::{}", employees);
		return employees;
	}

	public List<Employee> sendGiftAmendmentEmail(final Long employeeID) {
		LOGGER.debug("Entered into sendLogisticEmail with parameter::{}", employeeID);
		List<Employee> employees = null;
		if (configurationService.shouldSendEmail()) {
			employees = employeeRepository.findByRegistrationNoGreaterThanAndGiftAmendmentEmailSentIsNull(0);
			if (null != employees && !employees.isEmpty()) {
				for (final Employee emp : employees) {
					if (null == employeeID || emp.getEmployeeID() == employeeID) {
						sendGiftAmendmentEmail(emp);
					}
				}
			}
		}
		LOGGER.trace("Exit from sendLogisticEmail with parameter::{}", employees);
		return employees;
	}

	private void sendLogisticEmail(final Employee emp) {
		try {
			sendEmailWithTemplate(emp, Constants.TEMPLATE_LOGISTIC_EMAIL, Constants.TEMPLATE_LOGISTIC_EMAIL);

			emp.setLogisticsEmailSent(true);
			employeeRepository.save(emp);

		} catch (UnsupportedEncodingException | MessagingException e) {
			handleExceptionScenario(emp, e, Constants.TEMPLATE_LOGISTIC_EMAIL, Constants.TEMPLATE_LOGISTIC_EMAIL);
		} catch (final CustomException e) {
			LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
		}

	}

	private void sendGiftAmendmentEmail(final Employee emp) {
		try {
			sendEmailWithTemplate(emp, Constants.TEMPLATE_GIFT_AMENDMENT_EMAIL,
					Constants.TEMPLATE_GIFT_AMENDMENT_EMAIL);

			emp.setGiftAmendmentEmailSent(true);
			employeeRepository.save(emp);

		} catch (UnsupportedEncodingException | MessagingException e) {
			handleExceptionScenario(emp, e, Constants.TEMPLATE_GIFT_AMENDMENT_EMAIL, Constants.TEMPLATE_GIFT_AMENDMENT_EMAIL);
		} catch (final CustomException e) {
			LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
		}

	}

	public void sendEmailWithTemplate(final Employee emp, final String templateName, final String emailContext)
			throws UnsupportedEncodingException, MessagingException, CustomException {

		sendEmailWithTemplate(emp, templateName, emailContext, false);

	}

	public void sendEmailWithTemplate(final Employee emp, final String templateName, final String emailContext,
			final boolean shouldEscalate) throws UnsupportedEncodingException, MessagingException, CustomException {

		final EmailTemplate emailTemplate = getEmailTemplateByName(templateName);
		if (null != emailTemplate) {
			final String name = StringUtils.isNotBlank(emp.getShortName()) ? emp.getShortName() : emp.getFirstName();

			final String bodyContent = MessageFormat.format(emailTemplate.getBodyContent(), name, appLoginUrl,
					String.valueOf(emp.getEmployeeID()), defaultEmployeePassword, appPointOfContact, emp.getGift());

			final Email email = fetchEmailInfo(emp, emailTemplate, bodyContent, shouldEscalate);

			sendEmailWithBody(emp, emailTemplate, email, emailContext);
		} else {
			LOGGER.warn("No Email Template found. Unable to send email for ::{}", templateName);
			logEmailTracker(emp, "NO_EMAIL_TEMPLATE", false,
					"No Email Template found for templateName::" + templateName + " and emailContext::" + emailContext, templateName);
			throw new CustomException("No Email Template found. Unable to send email for ::" + templateName);
		}

	}

	private Email fetchEmailInfo(final Employee emp, final EmailTemplate emailTemplate, final String bodyContent) {
		return fetchEmailInfo(emp, emailTemplate, bodyContent, false);
	}

	private Email fetchEmailInfo(final Employee emp, final EmailTemplate emailTemplate, final String bodyContent,
			final boolean shouldEscalate) {

		final String alias = StringUtils.isNotBlank(emailTemplate.getEmailFromAlias())
				? emailTemplate.getEmailFromAlias()
				: emailTemplate.getEmailFrom();

		final Email email = new Email(emailTemplate.getEmailFrom(), alias, getEmailTo(emp, emailTemplate),
				getEmailCc(emp, emailTemplate, shouldEscalate), emailTemplate.getSubject(), bodyContent);
		return email;
	}

	private void sendEmailWithBody(final Employee emp, final EmailTemplate emailTemplate, final Email email,
			final String emailContext) throws UnsupportedEncodingException, MessagingException, CustomException {

		if (Constants.ROLE_ADMIN.equalsIgnoreCase(emp.getRole())
				|| Constants.ROLE_VOLUNTEER.equalsIgnoreCase(emp.getRole())) {
			final String failureReason = "Could not send email to : " + emailTemplate.getEmailTo() + " because he is::"
					+ emp.getRole();
			LOGGER.error("failureReason::{}", failureReason);
			logEmailTracker(emp, emailTemplate.getTemplateName(), false, failureReason, emailTemplate.getTemplateName());

			throw new CustomException("App does not send email for role::" + emp.getRole());
		} else {
			if (Constants.ROLE_TESTER.equalsIgnoreCase(emp.getRole())) {
				// for rejection

				if ((null != email.getCc() && email.getCc().size() == 1 || null == email.getCc()
						|| email.getCc().isEmpty()) && email.getTo().size() > 1) {
					email.setTo(email.getCc());
				}

				email.setCc(null);
			}

			email.setHtml(true);

			send(email, emp);
			logEmailTracker(emp, emailContext, true, null, emailTemplate.getTemplateName());

			try {
				Thread.sleep(200);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private void send(final Email email, final Employee emp) throws UnsupportedEncodingException, MessagingException {

		if (email.isHtml()) {
			sendHtmlMail(email);
		}

	}

	protected void sendHtmlMail(final Email email) throws MessagingException, UnsupportedEncodingException {

		final boolean isHtml = true;

		final MimeMessage message = sender.createMimeMessage();

		final MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(email.getTo().toArray(new String[email.getTo().size()]));
		helper.setReplyTo(email.getFrom());
		helper.setFrom(email.getFrom(), email.getFromAlias());
		helper.setSubject(email.getSubject());
		final String messageContent = email.getMessage();
		helper.setText(messageContent, isHtml);

		if (null != email.getCc() && email.getCc().size() > 0) {
			helper.setCc(email.getCc().toArray(new String[email.getCc().size()]));
		}

		sender.send(message);
		LOGGER.info("Email sent successfully to: {}  with subject: {}", email.getToAsList(), email.getSubject());
	}

	private void logEmailTracker(final Employee emp, final String emailContext, final boolean success,
			final String failureReason, String emailTemplateName) {
		final EmailTracker emailTracker = new EmailTracker(emailContext, emailTemplateName, success, failureReason, LocalDateTime.now(),
				emp);
		final Set<EmailTracker> emailTrackers = emp.getEmailTrackers();
		if (null == emailTrackers) {
			emp.setEmailTrackers(new HashSet<>());
		}
		emp.getEmailTrackers().add(emailTracker);

		saveReminderDateTime(emp, emailContext);

		LOGGER.info("logEmailTracker::emp.getEmailTrackers() {}", emp.getEmailTrackers());

		employeeRepository.save(emp);
	}

	private void saveReminderDateTime(final Employee emp, final String templateName) {
		if (Constants.TEMPLATE_CONFIRM_PARTICIPATION_REMINDER_EMAIL.equalsIgnoreCase(templateName)) {
			emp.setParticipationReminderLastSentDateTime(LocalDateTime.now());
		} else if (Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL.equalsIgnoreCase(templateName)) {
			emp.setCompleteRegistrationReminderLastSentDateTime(LocalDateTime.now());
		} else if (Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL.equalsIgnoreCase(templateName)) {
			emp.setFlightReminderLastSentDateTime(LocalDateTime.now());
		}

	}
}
