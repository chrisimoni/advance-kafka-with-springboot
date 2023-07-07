package com.chrisimoni.libraryeventsproducer.controller;

import com.chrisimoni.libraryeventsproducer.domain.LibraryEvent;
import com.chrisimoni.libraryeventsproducer.domain.LibraryEventType;
import com.chrisimoni.libraryeventsproducer.service.LibraryEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/libraryevent")
@RequiredArgsConstructor
@Slf4j
public class LibraryEventController {
    private final LibraryEventService libraryEventService;

    @PostMapping()
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
        libraryEventService.sendLibraryEvent(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PutMapping
    public ResponseEntity<?> updateLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
        ResponseEntity<String> validation = validateLibraryEvent(libraryEvent);
        if (validation != null) return validation;

        libraryEventService.updateLibraryEvent(libraryEvent);

        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
    }

    private static ResponseEntity<String> validateLibraryEvent(LibraryEvent libraryEvent) {
        if(libraryEvent.getLibraryEventId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("libraryEventId is required");
        }

        if(!libraryEvent.getLibraryEventType().equals(LibraryEventType.UPDATE)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only UPDATE event type is supported");
        }
        return null;
    }
}
