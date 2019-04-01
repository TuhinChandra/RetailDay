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
	@Value("${email.from}")
	private String emailFrom;
	@Value("${email.from.alias}")
	private String emailFromAlias;

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
					sendConfirmParticipationReminderEmail(emp);
				}
			}
		}
		LOGGER.trace("Exit from sendReminderToConfirmParticipation with parameter::{}", employees);
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

	public void sendConfirmationForParticipationEmail(final Employee emp) {

		final EmailTemplate emailTemplate = getEmailTemplateByName(Constants.TEMPLATE_CONFIRMED_PARTICIPATION_EMAIL);
		if (null != emailTemplate) {

			final String name = StringUtils.isNotBlank(emp.getShortName()) ? emp.getShortName() : emp.getFirstName();

			final String bodyContent = MessageFormat.format(emailTemplate.getBodyContent(), name, appLoginUrl,
					appPointOfContact, String.valueOf(emp.getEmployeeID()), defaultEmployeePassword);

			final Email email = fetchEmailInfo(emp, emailTemplate, bodyContent);

			try {
				sendEmailWithBody(emp, emailTemplate, email, Constants.TEMPLATE_CONFIRMED_PARTICIPATION_EMAIL);
			} catch (UnsupportedEncodingException | MessagingException e) {
				handleExceptionScenario(emp, e, Constants.TEMPLATE_CONFIRMED_PARTICIPATION_EMAIL);
			} catch (CustomException e) {
				LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
			}
		} else {
			LOGGER.warn("No Email Template found. Unable to send email for ::{}",
					Constants.TEMPLATE_CONFIRMED_PARTICIPATION_EMAIL);
			logEmailTracker(emp, "NO_EMAIL_TEMPLATE", false,
					"No Email Template found for " + Constants.TEMPLATE_CONFIRMED_PARTICIPATION_EMAIL);
		}

		/*
		 * sendEmailWithTemplate(emp, Constants.TEMPLATE_CONFIRMED_PARTICIPATION_EMAIL,
		 * Constants.TEMPLATE_CONFIRMED_PARTICIPATION_EMAIL);
		 */
	}

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
				handleExceptionScenario(emp, e, Constants.TEMPLATE_FORGOT_PASSWORD_EMAIL);
			} catch (CustomException e) {
				LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
			}

		} else {
			LOGGER.warn("No Email Template found. Unable to send email for ::{}",
					Constants.TEMPLATE_FORGOT_PASSWORD_EMAIL);
			logEmailTracker(emp, "NO_EMAIL_TEMPLATE", false,
					"No Email Template found for " + Constants.TEMPLATE_FORGOT_PASSWORD_EMAIL);
		}
	}

	public void sendRejectedParticipationEmail(final Employee emp) {

		final EmailTemplate emailTemplate = getEmailTemplateByName(Constants.TEMPLATE_REJECTED_PARTICIPATION_EMAIL);
		if (null != emailTemplate) {
			final String name = StringUtils.isNotBlank(emp.getShortName()) ? emp.getShortName() : emp.getFirstName();

			final String bodyContent = MessageFormat.format(emailTemplate.getBodyContent(), name,
					String.valueOf(emp.getEmployeeID()));

			final String alias = StringUtils.isNotBlank(emailTemplate.getEmailFromAlias())
					? emailTemplate.getEmailFromAlias()
					: emailTemplate.getEmailFrom();

			final Email email = new Email(emailTemplate.getEmailFrom(), alias, getEmailTo(null, emailTemplate),
					emp.getEmailID(), emailTemplate.getSubject(), bodyContent);

			try {
				sendEmailWithBody(emp, emailTemplate, email, Constants.TEMPLATE_REJECTED_PARTICIPATION_EMAIL);
			} catch (UnsupportedEncodingException | MessagingException e) {
				handleExceptionScenario(emp, e, Constants.TEMPLATE_REJECTED_PARTICIPATION_EMAIL);
			} catch (CustomException e) {
				LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
			}

		} else {
			LOGGER.warn("No Email Template found. Unable to send email for ::{}",
					Constants.TEMPLATE_REJECTED_PARTICIPATION_EMAIL);
			logEmailTracker(emp, "NO_EMAIL_TEMPLATE", false,
					"No Email Template found for " + Constants.TEMPLATE_REJECTED_PARTICIPATION_EMAIL);
		}

	}

	@Async
	public void sendInvitationEmail(final Employee emp) {
		try {
			sendEmailWithTemplate(emp, Constants.TEMPLATE_REGISTRATION_EMAIL, Constants.TEMPLATE_REGISTRATION_EMAIL);
		} catch (UnsupportedEncodingException | MessagingException e) {
			handleExceptionScenario(emp, e, Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL);
		} catch (CustomException e) {
			LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
		}
	}

	private void handleExceptionScenario(final Employee emp, Exception e, String emailContext) {
		LOGGER.error("Could not send email for : {} due to an error:: {}", emp.getEmployeeID(), e.getMessage());
		logEmailTracker(emp, emailContext, false, e.getMessage());
	}

	private String getEmailTo(final Employee emp, final EmailTemplate emailTemplate) {

		final String toList = emailTemplate.getEmailTo();
		String emailTo = null;
		if (null == emp) {
			emailTo = toList;
		} else {
			if (StringUtils.isNotBlank(toList)) {
				emailTo = emailTo + "," + toList;
			} else {
				emailTo = emp.getEmailID();
			}
		}
		return emailTo;

	}

	public void sendConfirmParticipationReminderEmail(final Employee emp) {
		try {
			sendEmailWithTemplate(emp, Constants.TEMPLATE_CONFIRM_PARTICIPATION_REMINDER_EMAIL,
					Constants.TEMPLATE_CONFIRM_PARTICIPATION_REMINDER_EMAIL);
		} catch (UnsupportedEncodingException | MessagingException e) {
			handleExceptionScenario(emp, e, Constants.TEMPLATE_CONFIRM_PARTICIPATION_REMINDER_EMAIL);
		} catch (CustomException e) {
			LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
		}
	}

	public void sendRegistrationReminderEmail(final Employee emp) {
		try {
			sendEmailWithTemplate(emp, Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL,
					Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL);
		} catch (UnsupportedEncodingException | MessagingException e) {
			handleExceptionScenario(emp, e, Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL);
		} catch (CustomException e) {
			LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
		}
	}

	public void sendFlightUpdateReminderEmail(final Employee emp) {
		try {
			sendEmailWithTemplate(emp, Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL,
					Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL);
		} catch (UnsupportedEncodingException | MessagingException e) {
			handleExceptionScenario(emp, e, Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL);
		} catch (CustomException e) {
			LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
		}
	}
	
	public List<Employee> sendLogisticEmail(final Long employeeID) {
		LOGGER.debug("Entered into sendLogisticEmail with parameter::{}",employeeID);
		List<Employee> employees = null;
		if (configurationService.shouldSendEmail()) {
			employees = employeeRepository.findByConfirmParticipationAndLogisticsEmailSentIsNull(Constants.CONFIRM_PARTICIPATION_YES);
			if (null != employees && !employees.isEmpty()) {
				for (final Employee emp : employees) {
					if(null==employeeID || emp.getEmployeeID()==employeeID) {
						sendLogisticEmail(emp);
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
			handleExceptionScenario(emp, e, Constants.TEMPLATE_LOGISTIC_EMAIL);
		} catch (CustomException e) {
			LOGGER.error("Could not send email due to an error:: {}", e.getMessage());
		}

	}

	public void sendEmailWithTemplate(final Employee emp, final String templateName, final String emailContext) throws UnsupportedEncodingException, MessagingException, CustomException {

		final EmailTemplate emailTemplate = getEmailTemplateByName(templateName);
		if (null != emailTemplate) {
			final String name = StringUtils.isNotBlank(emp.getShortName()) ? emp.getShortName() : emp.getFirstName();

			final String bodyContent = MessageFormat.format(emailTemplate.getBodyContent(), name, appLoginUrl,
					String.valueOf(emp.getEmployeeID()), defaultEmployeePassword, appPointOfContact);

			final Email email = fetchEmailInfo(emp, emailTemplate, bodyContent);

			sendEmailWithBody(emp, emailTemplate, email, emailContext);
		} else {
			LOGGER.warn("No Email Template found. Unable to send email for ::{}", templateName);
			logEmailTracker(emp, "NO_EMAIL_TEMPLATE", false,
					"No Email Template found for templateName::" + templateName + " and emailContext::" + emailContext);
			throw new CustomException("No Email Template found. Unable to send email for ::" + templateName);
		}

	}

	private Email fetchEmailInfo(final Employee emp, final EmailTemplate emailTemplate, final String bodyContent) {

		final String alias = StringUtils.isNotBlank(emailTemplate.getEmailFromAlias())
				? emailTemplate.getEmailFromAlias()
				: emailTemplate.getEmailFrom();

		final Email email = new Email(emailTemplate.getEmailFrom(), alias, getEmailTo(emp, emailTemplate),
				emailTemplate.getEmailCC(), emailTemplate.getSubject(), bodyContent);
		return email;
	}

	private void sendEmailWithBody(final Employee emp, final EmailTemplate emailTemplate, final Email email,
			final String emailContext) throws UnsupportedEncodingException, MessagingException, CustomException {

		if (Constants.ROLE_ADMIN.equalsIgnoreCase(emp.getRole())
				|| Constants.ROLE_VOLUNTEER.equalsIgnoreCase(emp.getRole())) {
			final String failureReason = "Could not send email to : " + emailTemplate.getEmailTo() + " because he is::"
					+ emp.getRole();
			LOGGER.error("failureReason::{}", failureReason);
			logEmailTracker(emp, emailTemplate.getTemplateName(), false, failureReason);
			
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
			logEmailTracker(emp, emailContext, true, null);

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

	public void sendEmailWithDetails(final String subject, final String text, final String emailID) {
		if (configurationService.shouldSendEmail()) {
			final Email email = new Email(emailFrom, emailFromAlias, emailID, subject, text);
			try {
				sendHtmlMail(email);
			} catch (final MessagingException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

	}

	private void logEmailTracker(final Employee emp, final String emailContext, final boolean success,
			final String failureReason) {
		final EmailTracker emailTracker = new EmailTracker(emailContext, success, failureReason, LocalDateTime.now(),
				emp);
		final Set<EmailTracker> emailTrackers = emp.getEmailTrackers();
		if (null == emailTrackers) {
			emp.setEmailTrackers(new HashSet<>());
		}
		emp.getEmailTrackers().add(emailTracker);

		saveReminderDateTime(emp, emailContext);

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
