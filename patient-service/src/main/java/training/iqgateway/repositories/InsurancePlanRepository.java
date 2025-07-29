package training.iqgateway.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import training.iqgateway.entities.InsurencePlan;


@Repository
public interface InsurancePlanRepository extends MongoRepository<InsurencePlan, String> {
    // declare custom query methods here if needed
}
