package com.chrisimoni.libraryeventsproducer.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.record.UnalignedMemoryRecords;

@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Book {
    @Id
    @NotNull
    private Integer bookId;
    @NotBlank
    private String bookName;
    @NotBlank
    private String bookAuthor;
    @OneToOne
    @JoinColumn(name = "libraryEventId")
    private LibraryEvent libraryEvent;
}
