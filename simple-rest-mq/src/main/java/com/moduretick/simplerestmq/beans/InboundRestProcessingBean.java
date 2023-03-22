package com.moduretick.simplerestmq.beans;

import org.apache.camel.Exchange;

import com.moduretick.simplerestmq.entity.NameAddress;

public class InboundRestProcessingBean {
	
	public void validate(Exchange exchange) {
		NameAddress nameAddress = exchange.getIn().getBody(NameAddress.class);
		exchange.getIn().setHeader("userCity", nameAddress.getCity());
	}

}
