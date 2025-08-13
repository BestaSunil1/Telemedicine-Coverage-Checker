package training.iqgateway.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import training.iqgateway.entities.Admin;

@Repository
public interface AdminRepository extends MongoRepository<Admin, String> {

	// Custom query methods can be defined here if needed
	// For example, to find admins by a specific field:
	// List<Admin> findBySomeField(String someField);
	Optional<Admin> findByUser_Id(String userId);

}
