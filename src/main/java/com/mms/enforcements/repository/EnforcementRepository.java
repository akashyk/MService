package com.mms.enforcements.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.mms.enforcements.model.Enforcement;

@Repository
public interface EnforcementRepository extends 
MongoRepository<Enforcement, Long> {
	
	// Find all with given status 
	public List<Enforcement> findByStatus(String status);
	
	// find all entries 
	@Query("{'statusCode' : {$in : ?0}, createdDate : {$lt : ?1}}")
	List<Enforcement> findByStatusCodeInAndCreatedDateLessThanQuery(String[] status, Date createdDate);
}