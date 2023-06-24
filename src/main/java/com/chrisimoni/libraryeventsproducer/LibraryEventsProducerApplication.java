package com.chrisimoni.libraryeventsproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LibraryEventsProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryEventsProducerApplication.class, args);
	}

}
