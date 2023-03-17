package com.moduretick.simplerestmq.components;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import com.moduretick.simplerestmq.entity.NameAddress;

@Component
public class RestRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
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
		
		from("direct:process").routeId("processMessageRouteId")
		.to("activemq:queue:NameAddressQueue?exchangePattern=InOnly")
		.to("jpa:"+ NameAddress.class.getName());
	}

}
