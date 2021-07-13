package com.nefertiti.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nefertiti.domain.User;
import com.nefertiti.repository.UserRepository;

@Service
public class ShowUserService {

	@Autowired
	private UserRepository userRepo;

	public List<User> getAllUsers() {
		
		List<User> result = (List<User>) userRepo.findAll();

		if (result.size() > 0) {
			return result;
		} else {
			return new ArrayList<User>();
		}
	}

	public User getUserById(Long id) throws Exception {
		
		Optional<User> employee = userRepo.findById(id);

		if (employee.isPresent()) {
			return employee.get();
		} else {
			throw new Exception("No employee record exist for given id");
		}
	}

	public User updateUser(User entity) {
		
		if (entity.getId() == null) {
			entity = userRepo.save(entity);

			return entity;
		} else {
			Optional<User> employee = userRepo.findById(entity.getId());

			if (employee.isPresent()) {
				
				User newEntity = employee.get();
				newEntity.setEmail(entity.getEmail());
				newEntity.setFirstName(entity.getFirstName());
				newEntity.setLastName(entity.getLastName());

				userRepo.save(newEntity);

				return newEntity;
			} else {
				entity = userRepo.save(entity);

				return entity;
			}
		}
	}
}
