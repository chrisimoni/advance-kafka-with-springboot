//package com.chrisimoni.libraryeventsproducer.intg.producer;
//
//import com.chrisimoni.libraryeventsproducer.domain.LibraryEvent;
//import com.chrisimoni.libraryeventsproducer.producer.LibraryEventProducer;
//import com.chrisimoni.libraryeventsproducer.util.TestUtil;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.clients.producer.RecordMetadata;
//import org.apache.kafka.common.TopicPartition;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.isA;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class LibraryEventProducerUnitTest {
//
//    @Mock
//    KafkaTemplate<Integer, String> kafkaTemplate;
//
//    @Spy
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    @InjectMocks
//    LibraryEventProducer eventProducer;
//
//    @Test
//    void sendLibraryEvent_Approach2_failure() throws JsonProcessingException, ExecutionException, InterruptedException {
//        //when
//        CompletableFuture<SendResult<Integer, String>> future = new CompletableFuture<>();
//        future.completeExceptionally(new RuntimeException("Exception calling kafka"));
//
//        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
//
//        //then
//        assertThrows(Exception.class, () -> eventProducer.sendLibraryEvent2(TestUtil.libraryEventRecord()).get());
//    }
//
//    @Test
//    void sendLibraryEvent_Approach2_success() throws JsonProcessingException, ExecutionException, InterruptedException {
//        //given
//        LibraryEvent libraryEvent = TestUtil.libraryEventRecord();
//
//        //when
//        CompletableFuture<SendResult<Integer, String>> future = new CompletableFuture<>();
//        String record = objectMapper.writeValueAsString(libraryEvent);
//        ProducerRecord<Integer, String> producerRecord = new ProducerRecord("library-events", libraryEvent.libraryEventId(), record);
//        RecordMetadata recordMetadata = new RecordMetadata(
//                new TopicPartition("library-events", 1), 1, 1, 342, System.currentTimeMillis(), 1,2);
//        SendResult<Integer, String> sendResult = new SendResult<Integer, String>(producerRecord, recordMetadata);
//        future.complete(sendResult);
//
//        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
//
//        CompletableFuture<SendResult<Integer, String>> completableFuture = eventProducer.sendLibraryEvent2(libraryEvent);
//
//        //then
//        SendResult<Integer, String> result = completableFuture.get();
//        assert result.getRecordMetadata().partition() == 1;
//
//
//    }
//}
