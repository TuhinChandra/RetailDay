package com.tcs.novia.service;

import java.util.Optional;

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

}
