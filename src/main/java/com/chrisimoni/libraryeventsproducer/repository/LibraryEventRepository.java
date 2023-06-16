package com.chrisimoni.libraryeventsproducer.repository;

import com.chrisimoni.libraryeventsproducer.domain.LibraryEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryEventRepository extends CrudRepository<LibraryEvent, Integer> {
}
