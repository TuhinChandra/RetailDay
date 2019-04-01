package com.tcs.novia.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tcs.novia.service.EmailService;

@Component
public class ReminderEmailScheduler {

	@Autowired
	private EmailService emailService;
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Scheduled(cron = "${schedule.confirm.participation}")
	public void sendReminderToConfirmParticipation() {
		LOGGER.info("Schedular::sendReminderToConfirmParticipation");
		emailService.sendReminderToConfirmParticipation();
	}

	@Scheduled(cron = "${schedule.complete.registration}")
	public void sendReminderToCompleteRegistration() {
		LOGGER.info("Schedular::sendReminderToCompleteRegistration");
		emailService.sendReminderToCompleteRegistration();
	}

	@Scheduled(cron = "${schedule.complete.flightDetails}")
	public void sendReminderToCompleteFlightDetails() {
		LOGGER.info("Schedular::sendReminderToCompleteFlightDetails");
		emailService.sendReminderToCompleteFlightDetails();
	}
	
}
