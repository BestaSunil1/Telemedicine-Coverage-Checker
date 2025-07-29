package training.iqgateway.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import training.iqgateway.entities.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
	
	Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
