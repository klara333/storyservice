package com.nefertiti.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.nefertiti.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

	List<User> findAll();
	
	User findByEmail(String username);
	
	@Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    User findByVerificationCode(String code);
	
	User findByResetPasswordToken(String token);
	
}
