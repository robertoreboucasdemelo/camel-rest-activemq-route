package com.moduretick.simplerestmq.components;

import java.net.ConnectException;

import javax.jms.JMSException;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import com.moduretick.simplerestmq.entity.NameAddress;

@Component
public class RestRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
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
		.to("direct:toDatabase")
		.to("direct:toActiveMQ");
		
		
		from("direct:toDatabase")
		.routeId("toDatabaseId")
		.to("jpa:"+ NameAddress.class.getName());
		
		from("direct:toActiveMQ")
		.routeId("toActiveMQId")
		.to("activemq:queue:NameAddressQueue?exchangePattern=InOnly");
	}

}
