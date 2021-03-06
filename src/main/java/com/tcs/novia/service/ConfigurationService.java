package com.tcs.novia.service;

import java.util.Optional;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.novia.constant.Constants;
import com.tcs.novia.model.Configuration;
import com.tcs.novia.repository.ConfigurationRepository;

@Service
public class ConfigurationService {

	@Autowired
	private ConfigurationRepository configurationRepository;

	public boolean shouldSendEmail() {
		final Configuration sendEmail = getConfiguration(Constants.CONFIG_APP_SEND_EMAIL);
		return null != sendEmail && Boolean.parseBoolean(sendEmail.getConfigValue());
	}

	public int getInterval(final String key) {
		final Configuration config = getConfiguration(key);
		if (null != config) {
			return Integer.parseInt(config.getConfigValue());
		} else {
			return Integer.MAX_VALUE;
		}
	}

	public Configuration getConfiguration(final String key) {
		final Optional<Configuration> config = configurationRepository.findById(key);
		if (config.isPresent()) {
			return config.get();
		} else {
			return null;
		}
	}

	public int getMaximumDelegateCount() {
		final Configuration config = getConfiguration(Constants.CONFIG_MAX_DELEGATE_COUNT);
		return null != config ? Integer.parseInt(config.getConfigValue()) : 0;
	}

	public int getNoOfDisplayableQuizWinners() {
		final Configuration config = getConfiguration(Constants.CONFIG_QUIZ_NO_OF_WINNERS_TO_SHOW);
		return null != config ? Integer.parseInt(config.getConfigValue()) : 10;
	}

	public int getNoOfQuizQuestions() {
		final Configuration config = getConfiguration(Constants.CONFIG_QUIZ_NO_OF_QUESTIONS);
		return null != config ? Integer.parseInt(config.getConfigValue()) : 0;
	}

	public boolean shouldSendReminderEmailFor(final String emailContext) {
		final Configuration config = getConfiguration(emailContext);
		return null != config ? BooleanUtils.toBoolean(config.getConfigValue()) : false;
	}

}
