package com.chrisimoni.libraryeventsproducer.unit.controller;

import com.chrisimoni.libraryeventsproducer.controller.LibraryEventController;
import com.chrisimoni.libraryeventsproducer.domain.LibraryEvent;
import com.chrisimoni.libraryeventsproducer.service.producer.LibraryEventProducer;
import com.chrisimoni.libraryeventsproducer.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryEventController.class)
@AutoConfigureMockMvc
public class LibraryEventControllerUnitTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    LibraryEventProducer libraryEventProducer;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void postLibraryEvent() throws Exception {
        //given
        String json = objectMapper.writeValueAsString(TestUtil.libraryEventRecord());

        when(libraryEventProducer.sendLibraryEvent2(isA(LibraryEvent.class))).thenReturn(null);

        //when
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/libraryevent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void postLibraryEvent_4xx() throws Exception {
        //given
        LibraryEvent libraryEvent = TestUtil.libraryEventRecordWithInvalidBook();
        String json = objectMapper.writeValueAsString(libraryEvent);
        when(libraryEventProducer.sendLibraryEvent2(isA(LibraryEvent.class))).thenReturn(null);

        String expectedErrorMessage = "book.bookId - must not be null, book.bookName - must not be blank";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/libraryevent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(expectedErrorMessage));
    }

    @Test
    void updateLibraryEvent() throws Exception {
        String json = objectMapper.writeValueAsString(TestUtil.libraryEventRecordUpdate());
        when(libraryEventProducer.sendLibraryEvent2(isA(LibraryEvent.class))).thenReturn(null);

        mockMvc.perform(
                        put("/api/v1/libraryevent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void updateLibraryEvent_withNullLibraryEventId() throws Exception {
        String json = objectMapper.writeValueAsString(TestUtil.libraryEventRecordUpdateWithNullLibraryEventId());
        //when(libraryEventProducer.sendLibraryEvent2(isA(LibraryEvent.class))).thenReturn(null);

        mockMvc.perform(
                        put("/api/v1/libraryevent")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("libraryEventId is required"));

    }

    @Test
    void updateLibraryEvent_withNullInvalidEventType() throws Exception {
        String json = objectMapper.writeValueAsString(TestUtil.newLibraryEventRecordWithLibraryEventId());
        //when(libraryEventProducer.sendLibraryEvent_Approach2(isA(LibraryEvent.class))).thenReturn(null);

        mockMvc.perform(
                        put("/api/v1/libraryevent")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Only UPDATE event type is supported"));

    }
}
