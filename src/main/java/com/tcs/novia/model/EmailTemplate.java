package com.tcs.novia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EmailTemplate {
	@Id
	@Column(name = "template_name", nullable = false)
	private String templateName;
	@Column(name = "email_from", nullable = false)
	private String emailFrom;
	@Column(name = "email_to", nullable = true)
	private String emailTo;
	@Column(name = "email_cc", nullable = true)
	private String emailCC;
	@Column(name = "email_from_alias", nullable = true)
	private String emailFromAlias;
	@Column(name = "subject", nullable = false, length = 1000)
	private String subject;
	@Column(name = "body_content", nullable = false, length = 10485760)
	private String bodyContent;
	@Column(name = "template_disabled", nullable = true)
	private Boolean disabled;

	public EmailTemplate() {

	}

	public EmailTemplate(String templateName, String emailFrom, String emailTo, String emailCC, String emailFromAlias,
			String subject, String bodyContent, Boolean disabled) {
		super();
		this.templateName = templateName;
		this.emailFrom = emailFrom;
		this.emailTo = emailTo;
		this.emailCC = emailCC;
		this.emailFromAlias = emailFromAlias;
		this.subject = subject;
		this.bodyContent = bodyContent;
		this.disabled = disabled;
	}


	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(final String templateName) {
		this.templateName = templateName;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(final String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(final String emailTo) {
		this.emailTo = emailTo;
	}

	public String getEmailCC() {
		return emailCC;
	}

	public void setEmailCC(final String emailCC) {
		this.emailCC = emailCC;
	}

	public String getEmailFromAlias() {
		return emailFromAlias;
	}

	public void setEmailFromAlias(final String emailFromAlias) {
		this.emailFromAlias = emailFromAlias;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public String getBodyContent() {
		return bodyContent;
	}

	public void setBodyContent(final String bodyContent) {
		this.bodyContent = bodyContent;
	}


	public Boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	@Override
	public String toString() {
		return "EmailTemplate [templateName=" + templateName + ", emailFrom=" + emailFrom + ", emailTo=" + emailTo
				+ ", emailCC=" + emailCC + ", emailFromAlias=" + emailFromAlias + ", subject=" + subject
				+ ", bodyContent=" + bodyContent + "]";
	}

}
