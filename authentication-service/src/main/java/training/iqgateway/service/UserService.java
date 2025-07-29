package training.iqgateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import training.iqgateway.dto.LoginRequest;
import training.iqgateway.entities.User;
import training.iqgateway.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User insertUser(User user) {
		return userRepository.save(user);
	}
}
