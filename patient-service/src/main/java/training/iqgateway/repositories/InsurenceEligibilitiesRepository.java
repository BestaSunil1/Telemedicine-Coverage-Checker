package training.iqgateway.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import training.iqgateway.entities.InsurenceEligibilities;

public interface InsurenceEligibilitiesRepository extends MongoRepository<InsurenceEligibilities, String> {
}
