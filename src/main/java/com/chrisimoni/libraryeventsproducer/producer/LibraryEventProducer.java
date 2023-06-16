package com.chrisimoni.libraryeventsproducer.producer;

import com.chrisimoni.libraryeventsproducer.domain.LibraryEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class LibraryEventProducer {
    private final KafkaTemplate<Integer, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    @Value("${spring.kafka.topic}")
    public String topic;


    public void sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);
        CompletableFuture<SendResult<Integer, String>> completableFuture = kafkaTemplate.sendDefault(key, value);
        completableFuture.whenComplete((sendResult, throwable) -> {
            if (throwable != null) {
                handleFailue(key, value, throwable);
            } else {
                handleSuccess(key, value, sendResult);
            }
        });
    }

    //Best approach
    //To unit test this, change return type to CompletableFuture instead of void
    public CompletableFuture<SendResult<Integer, String>> sendLibraryEvent2(LibraryEvent libraryEvent) throws JsonProcessingException {
        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);
        ProducerRecord<Integer, String> producerRecord = buildProducerRecord(key, value, topic);

        CompletableFuture<SendResult<Integer, String>> completableFuture = kafkaTemplate.send(producerRecord);

        return completableFuture.whenComplete((sendResult, throwable) -> {
            if (throwable != null) {
                handleFailue(key, value, throwable);
            } else {
                handleSuccess(key, value, sendResult);
            }
        });

    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {
        //Sample of sending headers if necessary (need more clarity)
        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));
        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }

    //For Synchronous call
    public SendResult<Integer, String> sendLibraryEventSynchronous(LibraryEvent libraryEvent) throws JsonProcessingException {
        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);
        SendResult<Integer, String> sendResult = null;
        try {
            sendResult = kafkaTemplate.sendDefault(key, value).get(1, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException ex) {
            log.error("ExecutionException/InterruptedException sending the message and the exception is {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Exception sending the message and the exception is {}", ex.getMessage());
        }

        return sendResult;
    }

    private void handleFailue(Integer key, String value, Throwable ex) {
        log.error("Error sending the message and the exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable e) {
            log.error("Error {}", e.getMessage());
        }
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> sendResult) {
        log.info("Message sent successfully for the key: {} and the value is {}, partition is {} ", key, value, sendResult.getRecordMetadata().partition());
    }
}
