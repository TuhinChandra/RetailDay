package com.tcs.novia.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tcs.novia.model.Configuration;
import com.tcs.novia.repository.ConfigurationRepository;

@Controller
public class ConfigurationController {

	@Autowired
	protected ConfigurationRepository configurationRepository;

	@RequestMapping(value = "/toogleService/{configName}/{configValue}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Configuration toogleService(@PathVariable("configName") final String configName,
			@PathVariable("configValue") final String configValue) {

		final Optional<Configuration> config = configurationRepository.findById(configName);
		Configuration configuration = null;
		if (config.isPresent()) {
			configuration = config.get();
			configuration.setConfigValue(configValue);
			configurationRepository.save(configuration);
		} else {
			configuration = new Configuration(configName, configValue);
			configurationRepository.save(configuration);
		}
		return configuration;
	}

}
