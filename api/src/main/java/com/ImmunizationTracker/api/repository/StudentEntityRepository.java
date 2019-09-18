package com.ImmunizationTracker.api.repository;

import com.ImmunizationTracker.api.entities.StudentEntity;
import org.springframework.data.repository.CrudRepository;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface StudentEntityRepository extends CrudRepository<StudentEntity, Integer> {

}
