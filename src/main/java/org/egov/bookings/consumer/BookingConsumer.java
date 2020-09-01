package org.egov.bookings.consumer;

import java.util.HashMap;

import org.egov.bookings.service.notification.BookingNotificationService;
import org.egov.bookings.web.models.BookingsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
@Component
public class BookingConsumer {

	 /** The notification service. */
 	private BookingNotificationService notificationService;

	    /**
    	 * Instantiates a new booking consumer.
    	 *
    	 * @param notificationService the notification service
    	 */
    	@Autowired
	    public BookingConsumer(BookingNotificationService notificationService) {
	        this.notificationService = notificationService;
	    }

	    /**
    	 * Listen.
    	 *
    	 * @param record the record
    	 * @param topic the topic
    	 */
    	@KafkaListener(topics = {"${kafka.topics.save.service}","${kafka.topics.update.service}"})
	    public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
	        ObjectMapper mapper = new ObjectMapper();
	        BookingsRequest bookingsRequest = new BookingsRequest();
	        try {
	            log.info("Consuming record: " + record);
	            bookingsRequest = mapper.convertValue(record, BookingsRequest.class);
	        } catch (final Exception e) {
	            log.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
	        }
	        log.info("TradeLicense Received: "+bookingsRequest.getBookingsModel().getBkApplicationNumber());
	        notificationService.process(bookingsRequest);
	    }
}
