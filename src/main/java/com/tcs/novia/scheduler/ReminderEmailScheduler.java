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
		}else {
			LOGGER.info("Schedular IS NOT ENABLED");
		}
	}

	@Scheduled(cron = "${schedule.remider.email}")
	public void sendReminderToCompleteRegistration() {
		if (configurationService
				.shouldSendReminderEmailFor(Constants.CONFIG_SEND_REGISTRATION_REMINDER_EMAIL)) {
			LOGGER.info("Schedular::sendReminderToCompleteRegistration");
			emailService.sendReminderToCompleteRegistration();
		}else {
			LOGGER.info("Schedular IS NOT ENABLED");
		}
	}

	@Scheduled(cron = "${schedule.remider.email}")
	public void sendReminderToCompleteFlightDetails() {
		if (configurationService
				.shouldSendReminderEmailFor(Constants.CONFIG_SEND_FLIGHT_UPDATE_REMINDER_EMAIL)) {
			LOGGER.info("Schedular::sendReminderToCompleteFlightDetails");
			emailService.sendReminderToCompleteFlightDetails();
		}else {
			LOGGER.info("Schedular IS NOT ENABLED");
		}
	}

}
