package com.moduretick.simplerestmq.components;

import java.net.ConnectException;

import javax.jms.JMSException;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import com.moduretick.simplerestmq.beans.InboundRestProcessingBean;
import com.moduretick.simplerestmq.entity.NameAddress;

@Component
public class RestRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		Predicate isCityNewYork = header("userCity").isEqualTo("New York");
		
		onException(JMSException.class, ConnectException.class)
		.routeId("jmsExceptionRouteId")
		.handled(true)
		.log(LoggingLevel.INFO, "JMS Exception has Occurred; Handling Gracefully");
		
		restConfiguration()
		.component("jetty")
		.host("0.0.0.0")
		.port(8080)
		.bindingMode(RestBindingMode.json)
		.enableCORS(true);
		
		rest("masterclass")
		.id("restRouteId")
		.produces("application/json")
		.post("nameAddress").type(NameAddress.class)
		.to("direct:process");
		
		from("direct:process")
		.routeId("processMessageRouteId")
		.bean(new InboundRestProcessingBean())
		
		// Setup Rule
		// If City == New York -> Send only to MQ
		// Else Send to Both DB and MQ
		
		.choice()
		.when(isCityNewYork)
			.to("direct:toActiveMQ")
		.otherwise()
			.to("direct:toDatabase")
			.to("direct:toActiveMQ")
		.end()
		.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201));
		
		
		from("direct:toDatabase")
		.routeId("toDatabaseId")
		.to("jpa:"+ NameAddress.class.getName());
		
		from("direct:toActiveMQ")
		.routeId("toActiveMQId")
		.to("activemq:queue:NameAddressQueue?exchangePattern=InOnly");
	}

}
