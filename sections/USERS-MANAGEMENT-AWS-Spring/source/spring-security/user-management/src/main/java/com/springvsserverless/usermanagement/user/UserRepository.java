package com.springvsserverless.usermanagement.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUserID(long id);

	User findByUsername(String name);

	User findByInternalName(String name);

	User findByUserMail(String userMail);
}
