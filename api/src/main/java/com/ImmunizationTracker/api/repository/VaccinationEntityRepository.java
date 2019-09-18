package com.ImmunizationTracker.api.repository;

import com.ImmunizationTracker.api.entities.VaccinationEntity;
import org.springframework.data.repository.CrudRepository;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface VaccinationEntityRepository extends CrudRepository<VaccinationEntity, Integer> {

}
