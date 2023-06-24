package com.chrisimoni.libraryeventsproducer.repository;

import com.chrisimoni.libraryeventsproducer.domain.FailureRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailureRecordRepository extends CrudRepository<FailureRecord, Integer> {
}
