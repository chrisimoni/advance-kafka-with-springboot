package com.chrisimoni.libraryeventsproducer.domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "library_events")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LibraryEvent {
        @Id
        private Integer libraryEventId;
        private LibraryEventType libraryEventType;
        @NotNull
        @Valid
        @OneToOne(mappedBy = "libraryEvent", cascade = {CascadeType.ALL})
        @ToString.Exclude
        private Book book;
}
