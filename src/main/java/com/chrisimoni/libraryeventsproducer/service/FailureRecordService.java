package com.chrisimoni.libraryeventsproducer.service;

import com.chrisimoni.libraryeventsproducer.domain.FailureRecord;
import com.chrisimoni.libraryeventsproducer.repository.FailureRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FailureRecordService {
    private final FailureRecordRepository failureRecordRepository;

    public void saveFailedRecord(ConsumerRecord<Integer,String> consumerRecord, Exception exception, String recordStatus) {
        var failureRecord = new FailureRecord(null,consumerRecord.topic(), consumerRecord.key(),
                consumerRecord.value(), consumerRecord.partition(),consumerRecord.offset(),
                exception.getCause().getMessage(),
                recordStatus);

        failureRecordRepository.save(failureRecord);
    }
}
