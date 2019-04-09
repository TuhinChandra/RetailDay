package com.tcs.novia.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.BooleanUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcs.novia.model.enums.CityTour;
import com.tcs.novia.model.enums.FoodPreference;
import com.tcs.novia.model.enums.ShirtSize;
import com.tcs.novia.model.enums.UserState;

@Entity
@Table(name = "EMPLOYEE")
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {

	@Id
	@Column(name = "EMP_ID", nullable = false)
	private long employeeID;
	@Column(name = "EMAIL_ID", nullable = false)
	private String emailID;
	@Column(name = "FIRST_NAME", nullable = false)
	private String firstName;
	@Column(name = "LAST_NAME", nullable = false)
	private String lastName;
	@Column(name = "SHORT_NAME", nullable = true)
	private String shortName;
	@Column(name = "FOOD_PREFERENCE", nullable = true)
	@Enumerated(EnumType.STRING)
	private FoodPreference foodPreference;
	@Column(name = "PASSWORD", nullable = false)
	private String password;
	@Column(name = "MOBILE", nullable = true)
	private String mobile;
	@Column(name = "PHOTO", nullable = true, length = 10485760)
	private String photo;
	@Column(name = "REGISTRATION_NO", nullable = true)
	private long registrationNo;
	@Column(name = "GENDER", nullable = true)
	private String gender;
	@Column(name = "SHIRT_SIZE", nullable = true)
	private ShirtSize shirtSize;
	@Column(name = "USER_STATE", nullable = true)
	@Enumerated(EnumType.STRING)
	private UserState status;
	@Column(name = "CITY_TOUR", nullable = true)
	private CityTour cityTourSelected;
	@Column(name = "ROLE", nullable = true)
	private String role;
	@Column(name = "GIFT", nullable = true)
	private String gift;
	@Column(name = "CONFIRM_PARTICIPATION", nullable = true)
	private String confirmParticipation;
	@Column(name = "ACCOMODATION_NEEDED", nullable = true)
	private String accomodationNeeded;

	@Column(name = "CREATED_DATE_TIME", nullable = true)
	private LocalDateTime createdDateTime;

	@Column(name = "PARTICIPATION_DATE_TIME", nullable = true)
	private LocalDateTime participationDateTime;

	@Column(name = "REGISTRATION_COMPLETED_DATE_TIME", nullable = true)
	private LocalDateTime registrationDateTime;

	@Column(name = "PARTICIPATION_REMINDER_LAST_SENT", nullable = true)
	private LocalDateTime participationReminderLastSentDateTime;

	@Column(name = "REGISTRATION_REMINDER_LAST_SENT", nullable = true)
	private LocalDateTime completeRegistrationReminderLastSentDateTime;

	@Column(name = "FLIGHT_REMINDER_LAST_SENT", nullable = true)
	private LocalDateTime flightReminderLastSentDateTime;

	@Column(name = "FLIGHT_UPDATE_NEEDED", nullable = true)
	private Boolean flightUpdateNeeded;
	@Column(name = "LOCAL_TRANSPORT_NEEDED", nullable = true)
	private Boolean localTransportNeeded;
	@Column(name = "PICKUP_ADDRESS", nullable = true, length = 1000)
	private String pickupAddress;
	@Column(name = "CHECKED_IN", nullable = true)
	private Boolean checkedIn;
	@Column(name = "IS_LOGISTIC_EMAIL_SENT", nullable = true)
	private Boolean logisticsEmailSent;
	@Column(name = "IS_GIFT_AMENDMENT_EMAIL_SENT", nullable = true)
	private Boolean giftAmendmentEmailSent;
	@Column(name = "CHECK_IN_TIME", nullable = true)
	private LocalDateTime checkInTime;
	@Column(name = "LOGIN_RETRIES_COUNT", nullable = true)
	private Long countOfInvalidLoginAttempt;
	@Column(name = "TRAVEL_CONFIRMATION_DATE_TIME", nullable = true)
	private LocalDateTime travelConfirmationDateTime;
	@Column(name = "COLOR_BAND", nullable = true)
	private String colorBand;
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<FlightInfo> flightInfos;
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<EmailTracker> emailTrackers;
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<ActivityTracker> activityTrackers;

	public Employee() {

	}

	public Employee(final long employeeID, final String emailID, final String firstName, final String lastName,
			final String shortName, final String gender, final String password) {
		super();
		this.employeeID = employeeID;
		this.emailID = emailID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.shortName = shortName;
		this.gender = gender;
		this.password = password;
	}

	public long getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(final long employeeID) {
		this.employeeID = employeeID;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(final String emailID) {
		this.emailID = emailID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(final String shortName) {
		this.shortName = shortName;
	}

	public FoodPreference getFoodPreference() {
		return foodPreference;
	}

	public void setFoodPreference(final FoodPreference foodPreference) {
		this.foodPreference = foodPreference;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	public long getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(final long registrationNo) {
		this.registrationNo = registrationNo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(final String gender) {
		this.gender = gender;
	}

	public ShirtSize getShirtSize() {
		return shirtSize;
	}

	public void setShirtSize(final ShirtSize shirtSize) {
		this.shirtSize = shirtSize;
	}

	public UserState getStatus() {
		return status;
	}

	public void setStatus(final UserState status) {
		this.status = status;
	}

	public CityTour getCityTourSelected() {
		return cityTourSelected;
	}

	public void setCityTourSelected(final CityTour cityTourSelected) {
		this.cityTourSelected = cityTourSelected;
	}

	public String getRole() {
		return role;
	}

	public void setRole(final String role) {
		this.role = role;
	}

	public String getGift() {
		return gift;
	}

	public void setGift(final String gift) {
		this.gift = gift;
	}

	public String getConfirmParticipation() {
		return confirmParticipation;
	}

	public void setConfirmParticipation(final String confirmParticipation) {
		this.confirmParticipation = confirmParticipation;
	}

	public String getAccomodationNeeded() {
		return accomodationNeeded;
	}

	public void setAccomodationNeeded(final String accomodationNeeded) {
		this.accomodationNeeded = accomodationNeeded;
	}

	public Boolean getLocalTransportNeeded() {
		return localTransportNeeded;
	}

	public void setLocalTransportNeeded(final Boolean localTransportNeeded) {
		this.localTransportNeeded = localTransportNeeded;
	}

	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(final LocalDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public LocalDateTime getParticipationDateTime() {
		return participationDateTime;
	}

	public void setParticipationDateTime(final LocalDateTime participationDateTime) {
		this.participationDateTime = participationDateTime;
	}

	public LocalDateTime getRegistrationDateTime() {
		return registrationDateTime;
	}

	public void setRegistrationDateTime(final LocalDateTime registrationDateTime) {
		this.registrationDateTime = registrationDateTime;
	}

	public Boolean getFlightUpdateNeeded() {
		return flightUpdateNeeded;
	}

	public void setFlightUpdateNeeded(final Boolean flightUpdateNeeded) {
		this.flightUpdateNeeded = flightUpdateNeeded;
	}

	public String getPickupAddress() {
		return pickupAddress;
	}

	public void setPickupAddress(final String pickupAddress) {
		this.pickupAddress = pickupAddress;
	}

	public Boolean getCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(final Boolean checkedIn) {
		this.checkedIn = checkedIn;
	}

	public LocalDateTime getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(final LocalDateTime checkInTime) {
		this.checkInTime = checkInTime;
	}

	public boolean isLogisticsEmailSent() {
		return BooleanUtils.toBoolean(logisticsEmailSent);
	}

	public void setLogisticsEmailSent(final Boolean logisticsEmailSent) {
		this.logisticsEmailSent = logisticsEmailSent;
	}

	public Boolean getGiftAmendmentEmailSent() {
		return giftAmendmentEmailSent;
	}

	public void setGiftAmendmentEmailSent(final Boolean giftAmendmentEmailSent) {
		this.giftAmendmentEmailSent = giftAmendmentEmailSent;
	}

	public LocalDateTime getParticipationReminderLastSentDateTime() {
		return participationReminderLastSentDateTime;
	}

	public void setParticipationReminderLastSentDateTime(final LocalDateTime participationReminderLastSentDateTime) {
		this.participationReminderLastSentDateTime = participationReminderLastSentDateTime;
	}

	public LocalDateTime getCompleteRegistrationReminderLastSentDateTime() {
		return completeRegistrationReminderLastSentDateTime;
	}

	public void setCompleteRegistrationReminderLastSentDateTime(
			final LocalDateTime completeRegistrationReminderLastSentDateTime) {
		this.completeRegistrationReminderLastSentDateTime = completeRegistrationReminderLastSentDateTime;
	}

	public LocalDateTime getFlightReminderLastSentDateTime() {
		return flightReminderLastSentDateTime;
	}

	public void setFlightReminderLastSentDateTime(final LocalDateTime flightReminderLastSentDateTime) {
		this.flightReminderLastSentDateTime = flightReminderLastSentDateTime;
	}

	public List<FlightInfo> getFlightInfos() {
		return flightInfos;
	}

	public void setFlightInfos(final List<FlightInfo> flightInfos) {
		this.flightInfos = flightInfos;
	}

	public Set<EmailTracker> getEmailTrackers() {
		return emailTrackers;
	}

	public void setEmailTrackers(final Set<EmailTracker> emailTrackers) {
		this.emailTrackers = emailTrackers;
	}

	public Set<ActivityTracker> getActivityTrackers() {
		return activityTrackers;
	}

	public void setActivityTrackers(final Set<ActivityTracker> activityTrackers) {
		this.activityTrackers = activityTrackers;
	}

	public long getCountOfInvalidLoginAttempt() {
		if (null == countOfInvalidLoginAttempt) {
			return 0;
		}
		return countOfInvalidLoginAttempt;
	}

	public void setCountOfInvalidLoginAttempt(final long countOfInvalidLoginAttempt) {
		this.countOfInvalidLoginAttempt = countOfInvalidLoginAttempt;
	}

	public LocalDateTime getTravelConfirmationDateTime() {
		return travelConfirmationDateTime;
	}

	public void setTravelConfirmationDateTime(final LocalDateTime travelConfirmationDateTime) {
		this.travelConfirmationDateTime = travelConfirmationDateTime;
	}

	@Override
	public String toString() {
		return "Employee [employeeID=" + employeeID + ", emailID=" + emailID + ", firstName=" + firstName
				+ ", lastName=" + lastName + "]";
	}

	public String getColorBand() {
		return colorBand;
	}

	public void setColorBand(final String colorBand) {
		this.colorBand = colorBand;
	}

}
