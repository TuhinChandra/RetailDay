package com.tcs.novia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Configuration {
	@Id
	@Column(name = "config_name", nullable = false)
	private String configName;
	@Column(name = "config_value", nullable = false)
	private String configValue;

	public String getConfigName() {
		return configName;
	}

	public Configuration() {

	}

	public Configuration(final String configName, final String configValue) {
		super();
		this.configName = configName;
		this.configValue = configValue;
	}

	public void setConfigName(final String configName) {
		this.configName = configName;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(final String configValue) {
		this.configValue = configValue;
	}

	@Override
	public String toString() {
		return "Configuration [configName=" + configName + ", configValue=" + configValue + "]";
	}
}
