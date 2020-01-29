package com.springvsserverless.usermanagement.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "Users")
public class User {

	/* Jackson Interfaces */
	public interface BasicUser {
	}

	public interface ExtendedUser {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(BasicUser.class)
	private long userID;

	@JsonView(BasicUser.class)
	private String username;

	// TO DO: Convert the password using a hash function
	@JsonIgnore
	private String password;

	@JsonView(BasicUser.class)
	private String userMail;

	@JsonView(BasicUser.class)
	private String userFirstName;

	@JsonView(BasicUser.class)
	private String userLastName;

	@JsonView(ExtendedUser.class)
	private String userAddress;

	@JsonView(ExtendedUser.class)
	private String city;

	@JsonView(ExtendedUser.class)
	private String country;

	@JsonView(ExtendedUser.class)
	private int phoneNumber;

	@JsonView(ExtendedUser.class)
	@Column(length = 1500)
	private String interests;

	@JsonView(ExtendedUser.class)
	private String urlProfileImage;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;

	private String internalName;

	/*
	 * Constructors
	 *
	 * /*Empty constructor for the DB
	 */
	public User() {
	}

	/* Minimum fields constructor */
	public User(String username, String password, String userMail, boolean isStudent) {
		super();
		this.username = username;
		this.password = new BCryptPasswordEncoder().encode(password);
		this.userMail = userMail;
		urlProfileImage = "null";

		userFirstName = "";
		userLastName = "";
		userAddress = "";
		city = "";
		country = "";
		phoneNumber = 00000000;
		interests = "";
		roles = new ArrayList<>(Arrays.asList("ROLE_USER"));
	}

	/* Methods */

	@Override
	public boolean equals(Object obj2) {

		boolean sameObj = false;

		if (obj2 != null && obj2 instanceof User)
			sameObj = userID == ((User) obj2).userID;
		return sameObj;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getInternalName() {
		return internalName;
	}

	public String getInterests() {
		return interests;
	}

	public String getPassword() {
		return password;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String getUrlProfileImage() {
		return urlProfileImage;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public long getUserID() {
		return userID;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public String getUserMail() {
		return userMail;
	}

	public String getUsername() {
		return username;
	}

	public boolean isAdmin() {
		return getRoles().contains("ROLE_ADMIN");
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public void setUrlProfileImage(String urlProfileImage) {
		this.urlProfileImage = urlProfileImage;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public void setUsername(String username) {
		this.username = username;
		internalName = this.username.replaceAll(" ", "-").toLowerCase();
	}

}
