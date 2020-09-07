package org.egov.bookings.consumer;

import java.util.HashMap;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.service.notification.BookingNotificationService;
import org.egov.bookings.service.notification.NLUJMBookingNotificationService;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.bookings.web.models.NewLocationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BookingConsumer {

 		/** The notification service. */
		 private BookingNotificationService notificationService;
 		
 		/** The utils. */
		 private BookingsConfiguration utils;
 		
 		/** The nlujm booking notification service. */
		 private NLUJMBookingNotificationService nlujmBookingNotificationService;

    	/**
	     * Instantiates a new booking consumer.
	     *
	     * @param notificationService the notification service
	     * @param utils the utils
	     * @param nlujmBookingNotificationService the nlujm booking notification service
	     */
	    @Autowired
	    public BookingConsumer(BookingNotificationService notificationService, BookingsConfiguration utils, NLUJMBookingNotificationService nlujmBookingNotificationService) {
	        this.notificationService = notificationService;
	        this.utils = utils;
	        this.nlujmBookingNotificationService = nlujmBookingNotificationService;
	    }

	    /**
    	 * Listen.
    	 *
    	 * @param record the record
    	 * @param topic the topic
    	 */
    	@KafkaListener(topics = {"${kafka.topics.save.booking.sms.notification.service}","${kafka.topics.update.sms.notification.service}","${kafka.topics.save.nlujm.sms.notification.service}","${kafka.topics.update.nlujm.sms.notification.service}"})
	    public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
	        ObjectMapper mapper = new ObjectMapper();
			if (utils.getSaveBookingSMSTopic().equals(topic) || utils.getUpdateBookingSMSTopic().equals(topic)) {
				BookingsRequest bookingsRequest = new BookingsRequest();
				try {
					log.info("Consuming record: " + record);
					bookingsRequest = mapper.convertValue(record, BookingsRequest.class);
				} catch (final Exception e) {
					log.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
				}
				log.info("Booking Received: " + bookingsRequest.getBookingsModel().getBkApplicationNumber());
				notificationService.process(bookingsRequest);
			}
			else if(utils.getSaveNLUJMBookingSMSTopic().equals(topic) || utils.getUpdateNLUJMBookingSMSTopic().equals(topic)) {
				NewLocationRequest newLocationRequest = new NewLocationRequest();
		        try {
		            log.info("Consuming record: " + record);
		            newLocationRequest = mapper.convertValue(record, NewLocationRequest.class);
		        } catch (final Exception e) {
		            log.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
		        }
		        log.info("TradeLicense Received: "+newLocationRequest.getNewLocationModel().getApplicationNumber());
		        nlujmBookingNotificationService.process(newLocationRequest);
			}
	    }
}
