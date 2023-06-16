package com.chrisimoni.libraryeventsproducer.service;

import com.chrisimoni.libraryeventsproducer.domain.LibraryEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryEventService {
    private final ObjectMapper objectMapper;

    public void ProcessLibraryEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        LibraryEvent libraryEvent = objectMapper.readValue(consumerRecord.value(), LibraryEvent.class);
        log.info("libraryEven : {} ", libraryEvent);

        switch (libraryEvent.getLibraryEventType()) {
            case NEW:
                //save operation
                break;
            case UPDATE:
                //update operation
                break;
            default:
                log.info("Invalid Library Evenet Type");
        }
    }
}
