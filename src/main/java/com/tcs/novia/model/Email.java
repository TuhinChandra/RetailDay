/**
 *
 */
package com.tcs.novia.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tcs.novia.util.AppUtil;

public class Email implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7142020094068088039L;

	private String from;

	private String fromAlias;

	private List<String> to;

	private List<String> cc;

	private String subject;

	private String message;

	private boolean isHtml;

	public Email() {
		to = new ArrayList<>();
		cc = new ArrayList<>();
	}

	public Email(final String from, final String fromAlias, final String toList, final String subject,
			final String message) {
		this();
		this.from = from;
		this.fromAlias = fromAlias;
		this.subject = subject;
		this.message = message;
		if (null != splitByComma(toList)) {
			to.addAll(Arrays.asList(splitByComma(toList)));
		}
	}

	public Email(final String from, final String fromAlias, final String toList, final String ccList,
			final String subject, final String message) {
		this();
		this.from = from;
		this.fromAlias = fromAlias;
		this.subject = subject;
		this.message = message;
		if (null != splitByComma(ccList)) {
			cc.addAll(Arrays.asList(splitByComma(ccList)));
		}
		if (null != splitByComma(toList)) {
			to.addAll(Arrays.asList(splitByComma(toList)));
		}
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(final String from) {
		this.from = from;
	}

	public String getFromAlias() {
		return fromAlias;
	}

	public void setFromAlias(final String fromAlias) {
		this.fromAlias = fromAlias;
	}

	/**
	 * @return the to
	 */
	public List<String> getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(final List<String> to) {
		this.to = to;
	}

	/**
	 * @return the cc
	 */
	public List<String> getCc() {
		return cc;
	}

	/**
	 * @param cc the cc to set
	 */
	public void setCc(final List<String> cc) {
		this.cc = cc;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(final String message) {
		this.message = message;
	}

	/**
	 * @return the isHtml
	 */
	public boolean isHtml() {
		return isHtml;
	}

	/**
	 * @param isHtml the isHtml to set
	 */
	public void setHtml(final boolean isHtml) {
		this.isHtml = isHtml;
	}

	private String[] splitByComma(final String toMultiple) {
		String[] toSplit = null;
		if (null != toMultiple) {
			toSplit = toMultiple.split(",");
		}
		return toSplit;
	}

	public String getToAsList() {
		return AppUtil.concatenate(to, ",");
	}
}
