package com.chrisimoni.libraryeventsproducer.service;

import com.chrisimoni.libraryeventsproducer.domain.LibraryEvent;
import com.chrisimoni.libraryeventsproducer.repository.LibraryEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryEventService {
    private final ObjectMapper objectMapper;
    private final LibraryEventRepository libraryEventRepository;

    public void processLibraryEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        LibraryEvent libraryEvent = objectMapper.readValue(consumerRecord.value(), LibraryEvent.class);
        log.info("libraryEven : {} ", libraryEvent);

        //Doesn't make sense right? Yes, this is just to test how to work with consumer retires and
        //allow certain exceptions
        if(libraryEvent.getLibraryEventId()!=null && ( libraryEvent.getLibraryEventId()==999 )){
            throw new RecoverableDataAccessException("Temporary Network Issue");
        }

        switch (libraryEvent.getLibraryEventType()) {
            case NEW:
                //save operation
                save(libraryEvent);
                break;
            case UPDATE:
                //update operation
                validate(libraryEvent);
                save(libraryEvent);
                break;
            default:
                log.info("Invalid Library Evenet Type");
        }
    }

    private void save(LibraryEvent libraryEvent) {
        libraryEvent.getBook().setLibraryEvent(libraryEvent);
        libraryEventRepository.save(libraryEvent);

        log.info("Successfully persisted the library event {} ", libraryEvent);
    }

    private void validate(LibraryEvent libraryEvent) {
        if(libraryEvent.getLibraryEventId() == null) {
            throw new IllegalArgumentException("Library Event Id is missing");
        }

        Optional<LibraryEvent> optionalLibraryEvent = libraryEventRepository.findById(libraryEvent.getLibraryEventId());
        if(!optionalLibraryEvent.isPresent()) {
            throw new IllegalArgumentException("Not a valid Library Event ");
        }

        log.info("validation is successful for the library event {} ", optionalLibraryEvent.get());
    }
}
