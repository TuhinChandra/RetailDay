package com.tcs.novia.util;

import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.opencsv.CSVWriter;
import com.tcs.novia.constant.Constants;
import com.tcs.novia.model.EmailTracker;
import com.tcs.novia.model.Employee;
import com.tcs.novia.model.FlightInfo;
import com.tcs.novia.model.enums.FlightType;

public class WriteDataToCSV {

	public static void writeDataToCsvUsingStringArray(final PrintWriter writer, final List<Employee> employees) {

		final String[] CSV_HEADER = { "EMP ID", "EMAIl ID", "FIRST NAME", "LAST NAME", "SHORT NAME", "ROLE",
				"Food Preference", "Mobile Number", "RegistrationNo", "Gender", "Gift", "Confirmed Participation",
				"Accomodation Needed", "Local Transport Needed", "Pick-up address", "Arrival Airline Name",
				"Arrival Flight No", "PREVIOUS_LOCATION", "Arrival Flight Date", "Arrival Flight Time",
				"Return Airline Name", "Return Flight No", "NEXT_LOCATION", "Return Flight Date", "Return Flight Time",
				"CREATED_DATE_TIME", "INVITATION_EMAIL_SENT", "PARTICIPATION_DATE_TIME", "PARTICIPATION_EMAIL_SENT",
				"CONFIRM_PARTICIPATION_REMINDER_EMAIL_SENT", "COMPLETE_REGISTRATION_REMINDER_EMAIL_SENT",
				"UPDATE_FLIGHT_REMINDER_EMAIL_SENT", "REGISTRATION_COMPLETED_DATE_TIME", "RESET_PASSWORD_EMAIL_SENT" };
		try (CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
				CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);) {
			csvWriter.writeNext(CSV_HEADER);

			for (final Employee employee : employees) {
				String arrivalflightname = "";
				String arrivalflightno = "";
				String previousLocation = "";
				String arrivalflightdate = "";
				String arrivalflighttime = "";

				String returnflightname = "";
				String returnflightno = "";
				String nextLocation = "";
				String returnflightdate = "";
				String returnflighttime = "";
				for (final FlightInfo fi : employee.getFlightInfos()) {

					if (fi.getFlightType() == FlightType.INBOUND) {
						arrivalflightname = fi.getAirlineName();
						arrivalflightno = fi.getFlightNumber();
						previousLocation = fi.getLocation();
						arrivalflightdate = fi.getFlightDate();
						arrivalflighttime = fi.getFlightTime();
					}

					if (fi.getFlightType() == FlightType.OUTBOUND) {
						returnflightname = fi.getAirlineName();
						returnflightno = fi.getFlightNumber();
						nextLocation = fi.getLocation();
						returnflightdate = fi.getFlightDate();
						returnflighttime = fi.getFlightTime();
					}
				}
				Boolean registrationEmailSent = null;
				Boolean resetPasswordEmailSent = null;
				Boolean participationEmailSent = null;
				Boolean confirmParticipationReminderSent = null;
				Boolean completeRegistrationReminderSent = null;
				Boolean flightUpdateReminderSent = null;
				final Set<EmailTracker> emailTrackers = employee.getEmailTrackers();
				if (null != emailTrackers && !emailTrackers.isEmpty()) {
					for (final EmailTracker emailTracker : emailTrackers) {
						final String emailType = emailTracker.getEmailType();
						if (Constants.TEMPLATE_REGISTRATION_EMAIL.equalsIgnoreCase(emailType)
								&& (null == registrationEmailSent || !registrationEmailSent)) {
							registrationEmailSent = emailTracker.isDelivered();
						} else if ((Constants.TEMPLATE_REJECTED_PARTICIPATION_EMAIL.equalsIgnoreCase(emailType)
								|| Constants.TEMPLATE_CONFIRMED_PARTICIPATION_EMAIL.equalsIgnoreCase(emailType))
								&& (null == participationEmailSent || !participationEmailSent)) {
							participationEmailSent = emailTracker.isDelivered();
						} else if (Constants.TEMPLATE_FORGOT_PASSWORD_EMAIL.equalsIgnoreCase(emailType)
								&& (null == resetPasswordEmailSent || !resetPasswordEmailSent)) {
							resetPasswordEmailSent = emailTracker.isDelivered();
						} else if (Constants.TEMPLATE_CONFIRM_PARTICIPATION_REMINDER_EMAIL.equalsIgnoreCase(emailType)
								&& (null == confirmParticipationReminderSent || !confirmParticipationReminderSent)) {
							confirmParticipationReminderSent = emailTracker.isDelivered();
						} else if (Constants.TEMPLATE_REGISTRATION_REMINDER_EMAIL.equalsIgnoreCase(emailType)
								&& (null == completeRegistrationReminderSent || !completeRegistrationReminderSent)) {
							completeRegistrationReminderSent = emailTracker.isDelivered();
						} else if (Constants.TEMPLATE_FLIGHT_UPDATE_REMINDER_EMAIL.equalsIgnoreCase(emailType)
								&& (null == flightUpdateReminderSent || !flightUpdateReminderSent)) {
							flightUpdateReminderSent = emailTracker.isDelivered();
						}
					}
				}

				String pickupAddress = employee.getPickupAddress();
				if (StringUtils.isNotBlank(pickupAddress)) {
					pickupAddress = pickupAddress.replaceAll("\n", "");
					pickupAddress = pickupAddress.replaceAll(",", "|");
				}

				final String[] data = { String.valueOf(employee.getEmployeeID()), employee.getEmailID(),
						employee.getFirstName(), employee.getLastName(), employee.getShortName(), employee.getRole(),
						String.valueOf(employee.getFoodPreference()), employee.getMobile(),
						employee.getRegistrationNo() > 0 ? String.valueOf(employee.getRegistrationNo())
								: "Not Registered",
						String.valueOf(employee.getGender()), employee.getGift(), employee.getConfirmParticipation(),
						employee.getAccomodationNeeded(), String.valueOf(employee.getLocalTransportNeeded()),
						pickupAddress, arrivalflightname, arrivalflightno, previousLocation, arrivalflightdate,
						arrivalflighttime, returnflightname, returnflightno, nextLocation, returnflightdate,
						returnflighttime, String.valueOf(employee.getCreatedDateTime()),
						String.valueOf(registrationEmailSent), String.valueOf(employee.getParticipationDateTime()),
						String.valueOf(participationEmailSent), String.valueOf(confirmParticipationReminderSent),
						String.valueOf(completeRegistrationReminderSent), String.valueOf(flightUpdateReminderSent),
						String.valueOf(employee.getRegistrationDateTime()), String.valueOf(resetPasswordEmailSent)

				};

				csvWriter.writeNext(data);
			}

			System.out.println("Write CSV using CSVWriter successfully!");
		} catch (final Exception e) {
			System.out.println("Writing CSV error!");
			e.printStackTrace();
		}
	}

}
