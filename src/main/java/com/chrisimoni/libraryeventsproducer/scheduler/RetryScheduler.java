package com.chrisimoni.libraryeventsproducer.scheduler;

import com.chrisimoni.libraryeventsproducer.config.KafkaConsumerConfig;
import com.chrisimoni.libraryeventsproducer.domain.FailureRecord;
import com.chrisimoni.libraryeventsproducer.repository.FailureRecordRepository;
import com.chrisimoni.libraryeventsproducer.service.LibraryEventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RetryScheduler {
    @Autowired
    LibraryEventService libraryEventsService;

    @Autowired
    FailureRecordRepository failureRecordRepository;

    @Scheduled(fixedRate = 10000 )
    public void retryFailedRecords(){

        log.info("Retrying Failed Records Started!");
        String status = KafkaConsumerConfig.RETRY;
        failureRecordRepository.findAllByStatus(status)
                .forEach(failureRecord -> {
                    try {
                        var consumerRecord = buildConsumerRecord(failureRecord);
                        libraryEventsService.processLibraryEvent(consumerRecord);
                        failureRecord.setStatus(KafkaConsumerConfig.SUCCESS);
                        failureRecordRepository.save(failureRecord);
                    } catch (Exception e){
                        log.error("Exception in retryFailedRecords : {} ", e.getMessage());
                    }

                });
    }

    private ConsumerRecord<Integer, String> buildConsumerRecord(FailureRecord failureRecord) {

        return new ConsumerRecord<>(failureRecord.getTopic(),
                failureRecord.getPartition(), failureRecord.getOffset_value(), failureRecord.getKey_value(),
                failureRecord.getErrorRecord());

    }
}
