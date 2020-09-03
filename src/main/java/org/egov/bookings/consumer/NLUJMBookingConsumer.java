package org.egov.bookings.consumer;

import java.util.HashMap;

import org.egov.bookings.service.notification.NLUJMBookingNotificationService;
import org.egov.bookings.web.models.NewLocationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
// TODO: Auto-generated Javadoc

/** The Constant log. */
@Slf4j
@Component
public class NLUJMBookingConsumer {
 		
		 /** The notification service. */
		 private NLUJMBookingNotificationService notificationService;

    	/**
	     * Instantiates a new NLUJM booking consumer.
	     *
	     * @param notificationService the notification service
	     */
	    @Autowired
	    public NLUJMBookingConsumer(NLUJMBookingNotificationService notificationService) {
	        this.notificationService = notificationService;
	    }

    	/**
	     * Listen.
	     *
	     * @param record the record
	     * @param topic the topic
	     */
	    @KafkaListener(topics = {"${kafka.topics.save.service.NLUJM}","${kafka.topics.save.service.NLUJM}"})
	    public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
	        ObjectMapper mapper = new ObjectMapper();
	        NewLocationRequest newLocationRequest = new NewLocationRequest();
	        try {
	            log.info("Consuming record: " + record);
	            newLocationRequest = mapper.convertValue(record, NewLocationRequest.class);
	        } catch (final Exception e) {
	            log.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
	        }
	        log.info("TradeLicense Received: "+newLocationRequest.getNewLocationModel().getApplicationNumber());
	        notificationService.process(newLocationRequest);
	    }
}
