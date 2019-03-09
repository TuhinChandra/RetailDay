package com.tcs.novia.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.tcs.novia.model.enums.CityTour;
import com.tcs.novia.model.enums.FoodPreference;
import com.tcs.novia.model.enums.Gender;
import com.tcs.novia.model.enums.ShirtSize;
import com.tcs.novia.model.enums.UserState;

@Entity
@Table(name = "EMPLOYEE")
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
	@Column(name = "FOOD_PREFERENCE", nullable = true)
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
	private Gender gender;
	@Column(name = "SHIRT_SIZE", nullable = true)
	private ShirtSize shirtSize;
	@Column(name = "USER_STATE", nullable = true)
	private UserState status;
	@Column(name = "CITY_TOUR", nullable = true)
	private CityTour cityTourSelected;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<FlightInfo> flightInfos;

	public Employee() {

	}

	public Employee(final long employeeID, final String emailID, final String firstName, final String lastName,
			final String password) {
		super();
		this.employeeID = employeeID;
		this.emailID = emailID;
		this.firstName = firstName;
		this.lastName = lastName;
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

	public Gender getGender() {
		return gender;
	}

	public void setGender(final Gender gender) {
		this.gender = gender;
	}

	public ShirtSize getShirtSize() {
		return shirtSize;
	}

	public void setShirtSize(final ShirtSize shirtSize) {
		this.shirtSize = shirtSize;
	}

	public List<FlightInfo> getFlightInfos() {
		return flightInfos;
	}

	public void setFlightInfos(final List<FlightInfo> flightInfos) {
		this.flightInfos = flightInfos;
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

	@Override
	public String toString() {
		return "Employee [employeeID=" + employeeID + ", emailID=" + emailID + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", foodPreference=" + foodPreference + ", password=" + password
				+ ", mobile=" + mobile + ", photo=" + photo + ", registrationNo=" + registrationNo + ", gender="
				+ gender + ", shirtSize=" + shirtSize + ", status=" + status + ", cityTourSelected=" + cityTourSelected
				+ ", flightInfos=" + flightInfos + "]";
	}

}
