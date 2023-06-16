package com.chrisimoni.libraryeventsproducer.util;

import com.chrisimoni.libraryeventsproducer.domain.Book;
import com.chrisimoni.libraryeventsproducer.domain.LibraryEvent;
import com.chrisimoni.libraryeventsproducer.domain.LibraryEventType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {
    public static Book bookRecord(){

        return Book.builder()
                .bookId(123)
                .bookAuthor("Chris Imoni")
                .bookName("Kafka Using Spring Boot")
                .build();
    }

    public static Book bookRecordWithInvalidValues(){
        return Book.builder()
                .bookId(null)
                .bookAuthor("")
                .bookName("Kafka Using Spring Boot")
                .build();
    }

    public static LibraryEvent libraryEventRecord(){
        return LibraryEvent.builder()
                .libraryEventId(null)
                .libraryEventType(LibraryEventType.NEW)
                .book(bookRecord())
                .build();
    }

    public static LibraryEvent newLibraryEventRecordWithLibraryEventId(){
        return LibraryEvent.builder()
                .libraryEventId(123)
                .libraryEventType(LibraryEventType.NEW)
                .book(bookRecord())
                .build();
    }

    public static LibraryEvent libraryEventRecordUpdate(){
        return LibraryEvent.builder()
                .libraryEventId(123)
                .libraryEventType(LibraryEventType.UPDATE)
                .book(bookRecord())
                .build();
    }

    public static LibraryEvent libraryEventRecordUpdateWithNullLibraryEventId(){
        return LibraryEvent.builder()
                .libraryEventId(null)
                .libraryEventType(LibraryEventType.UPDATE)
                .book(bookRecord())
                .build();
    }

    public static LibraryEvent libraryEventRecordWithInvalidBook(){
        return LibraryEvent.builder()
                .libraryEventId(null)
                .libraryEventType(LibraryEventType.NEW)
                .book(bookRecordWithInvalidValues())
                .build();
    }

    public static LibraryEvent parseLibraryEventRecord(ObjectMapper objectMapper , String json){

        try {
            return  objectMapper.readValue(json, LibraryEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
