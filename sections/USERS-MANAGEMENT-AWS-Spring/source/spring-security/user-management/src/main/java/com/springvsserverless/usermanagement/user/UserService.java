package com.springvsserverless.usermanagement.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

	int MAXLENGTH = 20;
	int MINLENGTH = 2;

	int MAXLENGTHADDRESS = 35;
	int LENGTHPHONE = 9;
	int MAXLENGTHINTERESTS = 50;
	int MAXLENGTHURL = 80;

	@Autowired
	UserRepository userRepository;

	public User checkUser(String username, String password, String repeatPassword, String userMail, boolean admin) {
		if (passwordMatch(password, repeatPassword) && correctName(username) && isValidEmailAddress(userMail)
				&& (userRepository.findByUsername(username) == null)
				&& (userRepository.findByUserMail(userMail) == null)) {
			User u = new User(username, password, userMail, !admin);
			userRepository.save(u);
			return u;
		} else
			return null;
	}

	public User updateUser(String userInternalName, User user) {

		User u = userRepository.findByInternalName(userInternalName);

		if (u != null) {
			String username = user.getUsername();
			String password = user.getPassword();
			String userMail = user.getUserMail();
			String userFirstName = user.getUserFirstName();
			String userLastName = user.getUserLastName();
			String userAddress = user.getUserAddress();
			String city = user.getCity();
			String country = user.getCountry();
			Integer phoneNumber = (Integer) user.getPhoneNumber();
			String interests = user.getInterests();
			String urlProfileImage = user.getUrlProfileImage();

			if (username != null)
				if (correctName(username) && (userRepository.findByUsername(username) == null))
					u.setUsername(username);

			if (password != null)
				u.setPassword(password);

			/*
			 * if ((password != null) && (repeatPassword != null)) if
			 * (passwordMatch(password, repeatPassword)) u.setPassword(password);
			 */

			if (userMail != null)
				if (isValidEmailAddress(userMail))
					u.setUserMail(userMail);

			if (userFirstName != null)
				if ((MINLENGTH < userFirstName.length()) && (MAXLENGTH > userFirstName.length()))
					u.setUserFirstName(userFirstName);

			if (userLastName != null)
				if ((MINLENGTH < userLastName.length()) && (MAXLENGTH > userLastName.length()))
					u.setUserLastName(userLastName);

			if (userAddress != null)
				if (userAddress.length() < MAXLENGTHADDRESS)
					u.setUserAddress(userAddress);

			if (city != null)
				if (city.length() < MAXLENGTH)
					u.setCity(city);

			if (country != null)
				if (country.length() < MAXLENGTH)
					u.setCountry(country);

			if (phoneNumber != null)
				if (phoneNumber.toString().length() == LENGTHPHONE)
					u.setPhoneNumber((int) phoneNumber);

			if (interests != null)
				if (interests.length() < MAXLENGTHINTERESTS)
					u.setInterests(interests);

			if (urlProfileImage != null)
				if (urlProfileImage.length() < MAXLENGTHURL)
					u.setUrlProfileImage(urlProfileImage);

			return u;

		}

		return null;
	}

	public User deleteUser(String userInternalName) {
		User userToRemove = userRepository.findByInternalName(userInternalName);
		userRepository.delete(userToRemove);

		return userToRemove;
	}

	public boolean passwordMatch(String s1, String s2) {
		return (Objects.equals(s1, s2) && s1.length() > 7 && s1.length() < 15);
	}

	public boolean correctName(String s) {
		return (s.length() > 4 && s.length() < 16 && s.matches("^[a-zA-Z0-9_-]*$"));
	}

	public boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	public Path getImgPath(User user) {
		Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "files/image/users/" + user.getUserID() + "/");

		Path image = FILES_FOLDER.resolve(user.getUrlProfileImage());

		if (!Files.exists(image)) {
			image = Paths.get(System.getProperty("user.dir"), "files/image/users/default/default.jpg");
		}

		return image;
	}

	public User saveImg(MultipartFile file, User user) {
		if (file != null && !file.isEmpty()) {
			try {
				Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"),
						"files/image/users/" + user.getUserID() + "/");
				if (!Files.exists(FILES_FOLDER)) {
					Files.createDirectories(FILES_FOLDER);
				}

				String fileName = "profile-" + user.getUserID() + ".jpg";

				File uploadedFile = new File(FILES_FOLDER.toFile(), fileName);
				file.transferTo(uploadedFile);
				user.setUrlProfileImage(fileName);
				user = userRepository.save(user);
				return user;

			} catch (IOException e) {
				System.out.println(e.getMessage());
				return null;
			}
		}
		return null;
	}

}
