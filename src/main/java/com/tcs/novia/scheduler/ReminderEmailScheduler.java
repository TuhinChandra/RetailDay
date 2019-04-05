package com.tcs.novia.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tcs.novia.constant.Constants;
import com.tcs.novia.service.ConfigurationService;
import com.tcs.novia.service.EmailService;

@Component
public class ReminderEmailScheduler {

	@Autowired
	private EmailService emailService;
	@Autowired
	private ConfigurationService configurationService;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Scheduled(cron = "${schedule.remider.email}")
	public void sendReminderToConfirmParticipation() {
		if (configurationService
				.shouldSendReminderEmailFor(Constants.CONFIG_SEND_CONFIRM_PARTICIPATION_REMINDER_EMAIL)) {
			LOGGER.info("Schedular::sendReminderToConfirmParticipation");
			emailService.sendReminderToConfirmParticipation();
		} else {
			schedularNotEnabled();
		}
	}

	@Scheduled(cron = "${schedule.remider.email}")
	public void sendSecondReminderToConfirmParticipation() {
		if (configurationService
				.shouldSendReminderEmailFor(Constants.CONFIG_SEND_SECOND_CONFIRM_PARTICIPATION_REMINDER_EMAIL)) {
			LOGGER.info("Schedular::sendSecondReminderToConfirmParticipation");
			emailService.sendSubsequentReminderEmails(null, Constants.TEMPLATE_CONFIRM_PARTICIPATION_REMINDER_EMAIL,
					Constants.TEMPLATE_SECOND_CONFIRM_PARTICIPATION_REMINDER_EMAIL, 1, false);
		} else {
			schedularNotEnabled();
		}
	}

	@Scheduled(cron = "${schedule.remider.email}")
	public void sendEscalateReminderToConfirmParticipation() {
		if (configurationService
				.shouldSendReminderEmailFor(Constants.CONFIG_SEND_ESCALATE_CONFIRM_PARTICIPATION_REMINDER_EMAIL)) {
			LOGGER.info("Schedular::sendEscalateReminderToConfirmParticipation");
			emailService.sendSubsequentReminderEmails(null, Constants.TEMPLATE_CONFIRM_PARTICIPATION_REMINDER_EMAIL,
					Constants.TEMPLATE_ESCALATION_CONFIRM_PARTICIPATION_REMINDER_EMAIL, 2, true);
		} else {
			schedularNotEnabled();
		}
	}

	private void schedularNotEnabled() {
		LOGGER.info("Schedular IS NOT ENABLED");
	}

	@Scheduled(cron = "${schedule.remider.email}")
	public void sendReminderToCompleteRegistration() {
		if (configurationService.shouldSendReminderEmailFor(Constants.CONFIG_SEND_REGISTRATION_REMINDER_EMAIL)) {
			LOGGER.info("Schedular::sendReminderToCompleteRegistration");
			emailService.sendReminderToCompleteRegistration();
		} else {
			schedularNotEnabled();
		}
	}

	@Scheduled(cron = "${schedule.remider.email}")
	public void sendSecondCompleteRegistrationReminder() {
		if (configurationService.shouldSendReminderEmailFor(Constants.CONFIG_SEND_SECOND_REGISTRATION_REMINDER_EMAIL)) {
			LOGGER.info("Schedular::sendSecondCompleteRegistrationReminder");
			emailService.sendSubsequentReminderEmails(null, Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL,
					Constants.TEMPLATE_SECOND_REGISTRATION_REMINDER_EMAIL, 1, false);
		} else {
			schedularNotEnabled();
		}
	}

	@Scheduled(cron = "${schedule.remider.email}")
	public void sendEscalationForCompleteRegistrationReminder() {
		if (configurationService
				.shouldSendReminderEmailFor(Constants.CONFIG_SEND_ESCALATE_REGISTRATION_REMINDER_EMAIL)) {
			LOGGER.info("Schedular::sendEscalationForCompleteRegistrationReminder");
			emailService.sendSubsequentReminderEmails(null, Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL,
					Constants.TEMPLATE_ESCALATION_REGISTRATION_REMINDER_EMAIL, 2, true);
		} else {
			schedularNotEnabled();
		}
	}

	@Scheduled(cron = "${schedule.remider.email}")
	public void sendReminderToCompleteFlightDetails() {
		if (configurationService.shouldSendReminderEmailFor(Constants.CONFIG_SEND_FLIGHT_UPDATE_REMINDER_EMAIL)) {
			LOGGER.info("Schedular::sendReminderToCompleteFlightDetails");
			emailService.sendReminderToCompleteFlightDetails();
		} else {
			schedularNotEnabled();
		}
	}

	@Scheduled(cron = "${schedule.remider.email}")
	public void sendSecondFlightUpdateReminder() {
		if (configurationService
				.shouldSendReminderEmailFor(Constants.CONFIG_SEND_SECOND_FLIGHT_UPDATE_REMINDER_EMAIL)) {
			LOGGER.info("Schedular::sendSecondFlightUpdateReminder");
			emailService.sendSubsequentReminderEmails(null, Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL,
					Constants.TEMPLATE_SECOND_FLIGHT_UPDATE_REMINDER_EMAIL, 1, false);
		} else {
			schedularNotEnabled();
		}
	}

	@Scheduled(cron = "${schedule.remider.email}")
	public void sendEscalationForFlightUpdateReminder() {
		if (configurationService
				.shouldSendReminderEmailFor(Constants.CONFIG_SEND_ESCALATE_FLIGHT_UPDATE_REMINDER_EMAIL)) {
			LOGGER.info("Schedular::sendEscalationForFlightUpdateReminder");
			emailService.sendSubsequentReminderEmails(null, Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL,
					Constants.TEMPLATE_ESCALATION_FLIGHT_UPDATE_REMINDER_EMAIL, 2, true);
		} else {
			schedularNotEnabled();
		}
	}

}
