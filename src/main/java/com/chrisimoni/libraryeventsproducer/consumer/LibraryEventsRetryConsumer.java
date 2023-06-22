package com.chrisimoni.libraryeventsproducer.consumer;

import com.chrisimoni.libraryeventsproducer.service.LibraryEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LibraryEventsRetryConsumer {

    private final LibraryEventService libraryEventsService;

    @KafkaListener(topics = {"${topics.retry}"}
            , autoStartup = "${retryListener.startup:true}"
            , groupId = "retry-listener-group")
    public void onMessage(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {

        log.info("ConsumerRecord in Retry Consumer: {} ", consumerRecord );
        libraryEventsService.processLibraryEvent(consumerRecord);

    }
}
