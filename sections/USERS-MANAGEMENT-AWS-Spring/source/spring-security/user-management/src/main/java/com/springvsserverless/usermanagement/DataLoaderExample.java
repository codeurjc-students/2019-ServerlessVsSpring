package com.springvsserverless.usermanagement;

import com.springvsserverless.usermanagement.user.User;
import com.springvsserverless.usermanagement.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(1)
public class DataLoaderExample {

	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	public void init() {

		List<User> users = new ArrayList<>();

		for (int i = 0; i < 50; i++) {
			users.add(new User("student-" + i, "pass-" + i, "user" + i + "@mail.com", true));
		}

		users.add(new User("amico", "pass", "amicourses@mail.com", true));
		users.add(new User("amicoTeacher", "pass", "amicoTeacher@mail.com", false));
		users.add(new User("admin", "pass", "admin@mail.com", false));
		users.get(52).getRoles().add("ROLE_ADMIN");
		users.add(new User("amicoTeacher2", "pass", "amicoTeacher2@mail.com", false));
		users.add(new User("amicoTeacher3", "pass", "amicoTeacher3@mail.com", false));
		users.add(new User("amicoTeacher4", "pass", "amicoTeacher4@mail.com", false));
		users.add(new User("amicoTeacher5", "pass", "amicoTeacher5@mail.com", false));
		users.add(new User("amicoTeacher6", "pass", "amicoTeacher6@mail.com", false));

		users.get(50).setCity("New York");
		users.get(50).setCountry("United States of America");
		users.get(50).setUserAddress("Under the Brooklyn Bridge");
		users.get(50).setUserFirstName("Amico");
		users.get(50).setUserLastName("Fernandez");
		users.get(50).setInterests("Sleeping and watching tv.");
		
		userRepository.saveAll(users);

	}

}
