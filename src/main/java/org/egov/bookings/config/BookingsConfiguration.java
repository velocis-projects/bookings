package org.egov.bookings.config;

import java.math.BigDecimal;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Import({TracerConfiguration.class})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class BookingsConfiguration {


    @Value("${app.timezone}")
    private String timeZone;

    @PostConstruct
    public void initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    @Bean
    @Autowired
    public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    // User Config
    @Value("${egov.user.host}")
    private String userHost;

    @Value("${egov.user.context.path}")
    private String userContextPath;

    @Value("${egov.user.create.path}")
    private String userCreateEndpoint;

    @Value("${egov.user.search.path}")
    private String userSearchEndpoint;

    @Value("${egov.user.update.path}")
    private String userUpdateEndpoint;

    @Value("${egov.user.username.prefix}")
    private String usernamePrefix;


    //Idgen Config
    @Value("${egov.idgen.host}")
    private String idGenHost;

    @Value("${egov.idgen.path}")
    private String idGenPath;

    @Value("${egov.idgen.tl.applicationNum.name}")
    private String applicationNumberIdgenName;

    @Value("${egov.idgen.tl.applicationNum.format}")
    private String applicationNumberIdgenFormat;

    @Value("${egov.idgen.tl.licensenumber.name}")
    private String licenseNumberIdgenName;

    @Value("${egov.idgen.tl.licensenumber.format}")
    private String licenseNumberIdgenFormat;
    
    @Value("${egov.demand.update.endpoint}")
	private String demandUpdateEndPoint;
    

   


    //Persister Config
    @Value("${kafka.topics.save.service}")
    private String saveTopic;

    @Value("${kafka.topics.update.service}")
    private String updateTopic;
    
    @Value("${kafka.topics.save.service.NLUJM}")
    private String saveNLUJMTopic;

    @Value("${kafka.topics.update.service.NLUJM}")
    private String updateNLUJMTopic;

    @Value("${persister.update.tradelicense.workflow.topic}")
    private String updateWorkflowTopic;

    @Value("${persister.update.tradelicense.adhoc.topic}")
    private String updateAdhocTopic;


    //Location Config
    @Value("${egov.location.host}")
    private String locationHost;

    @Value("${egov.location.context.path}")
    private String locationContextPath;

    @Value("${egov.location.endpoint}")
    private String locationEndpoint;

    @Value("${egov.location.hierarchyTypeCode}")
    private String hierarchyTypeCode;

    @Value("${egov.tl.default.limit}")
    private Integer defaultLimit;

    @Value("${egov.tl.default.offset}")
    private Integer defaultOffset;

    @Value("${egov.tl.max.limit}")
    private Integer maxSearchLimit;



    // tradelicense Calculator
    @Value("${egov.tl.calculator.host}")
    private String calculatorHost;

    @Value("${egov.tl.calculator.calculate.endpoint}")
    private String calculateEndpoint;

    @Value("${egov.tl.calculator.getBill.endpoint}")
    private String getBillEndpoint;



    //Institutional key word
    @Value("${egov.ownershipcategory.institutional}")
    private String institutional;


    @Value("${egov.receipt.businessservice}")
    private String businessService;


    //Property Service
    @Value("${egov.property.service.host}")
    private String propertyHost;

    @Value("${egov.property.service.context.path}")
    private String propertyContextPath;

    @Value("${egov.property.endpoint}")
    private String propertySearchEndpoint;


    //SMS
    @Value("${kafka.topics.notification.sms}")
    private String smsNotifTopic;

    @Value("${notification.sms.enabled}")
    private Boolean isSMSEnabled;
    
    @Value("${notification.sms.enabled.forBK}")
    private Boolean isBKSMSEnabled;

    @Value("${notification.sms.enabled.forBPA}")
    private Boolean isBPASMSEnabled;
    

    //EMAIL
    @Value("${kafka.topics.notification.email}")
    private String emailNotifTopic;

    @Value("${notification.email.enabled.forBK}")
    private Boolean isBKEMAILEnabled;
    
    
    //Localization
    @Value("${egov.localization.host}")
    private String localizationHost;

    @Value("${egov.localization.context.path}")
    private String localizationContextPath;

    @Value("${egov.localization.search.endpoint}")
    private String localizationSearchEndpoint;

    @Value("${egov.localization.statelevel}")
    private Boolean isLocalizationStateLevel;



    //MDMS
    @Value("${egov.mdms.host}")
    private String mdmsHost;

    @Value("${egov.mdms.search.endpoint}")
    private String mdmsEndPoint;


    //Allowed Search Parameters
    @Value("${citizen.allowed.search.params}")
    private String allowedCitizenSearchParameters;

    @Value("${employee.allowed.search.params}")
    private String allowedEmployeeSearchParameters;



    @Value("${egov.tl.previous.allowed}")
    private Boolean isPreviousTLAllowed;

    @Value("${egov.tl.min.period}")
    private Long minPeriod;


    // Workflow
    @Value("${create.tl.workflow.name}")
    private String businessServiceValue;

    @Value("${workflow.context.path}")
    private String wfHost;

    @Value("${workflow.transition.path}")
    private String wfTransitionPath;

    @Value("${workflow.businessservice.search.path}")
    private String wfBusinessServiceSearchPath;

    @Value("${is.external.workflow.enabled}")
    private Boolean isExternalWorkFlowEnabled;
    
    @Value("${workflow.process.search.path}")
    private String workflowProcessInstancePath;
    
    

    //USER EVENTS
	@Value("${egov.ui.app.host}")
	private String uiAppHost;
    
	@Value("${egov.usr.events.create.topic}")
	private String saveUserEventsTopic;
		
	@Value("${egov.usr.events.pay.link}")
	private String payLink;
	
	@Value("${egov.usr.events.pay.code}")
	private String payCode;
	
	@Value("${egov.user.event.notification.enabled}")
	private Boolean isUserEventsNotificationEnabled;
	
	@Value("${egov.user.event.notification.enabledForTL}")
	private Boolean isUserEventsNotificationEnabledForTL;

    @Value("${egov.user.event.notification.enabledForBPA}")
    private Boolean isUserEventsNotificationEnabledForBPA;

	@Value("${egov.usr.events.pay.triggers}")
	private String payTriggers;
	
	@Value("${egov.usr.events.ctl.pay.link}")
	private String ctlPayLink;
	
	@Value("${egov.demand.create.endpoint}")
	private String demandCreateEndpoint;
	
	
	
	
	@Value("${egov.billingservice.host}")
	private String billingHost;
	
	
	@Value("${egov.demand.minimum.payable.amount}")
    private BigDecimal minimumPayableAmount;
	
    @Value("${egov.demand.search.endpoint}")
    private String demandSearchEndpoint;
    
    @Value("${egov.lock.jurisdiction.payment}")
    private boolean jurisdictionLock;
    
    @Value("${egov.lock.commercial.payment}")
    private boolean commercialLock;
    
    @Value("${egov.lock.park.and.commercial.payment}")
    private boolean parkAndCommercialLock;

	@Value("${kafka.topics.save.service}")
	private String saveBookingTopic;

	@Value("${kafka.topics.save.nlujm.service}")
	private String saveNewLocationTopic;

	@Value("${kafka.topics.update.service}")
	private String updateBookingTopic;

	@Value("${kafka.topics.update.nlujm.service}")
	private String updateNewLocationTopic;    
    

}
