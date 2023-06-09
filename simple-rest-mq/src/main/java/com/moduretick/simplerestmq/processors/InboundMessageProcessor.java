package com.moduretick.simplerestmq.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moduretick.simplerestmq.beans.OutboundNameAddress;
import com.moduretick.simplerestmq.entity.NameAddress;

public class InboundMessageProcessor implements Processor {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		NameAddress nameAddress = exchange.getIn().getBody(NameAddress.class);
		exchange.getIn().setBody(new OutboundNameAddress(nameAddress.getName(),
				returnOutboundAddress(nameAddress)));
		exchange.getIn().setHeader("consumedId", nameAddress.getId());
	}
	
	private String returnOutboundAddress(NameAddress nameAddress) {
		StringBuilder concatenatedAddress = new StringBuilder(200);
		concatenatedAddress.append(nameAddress.getHouseNumber());
		concatenatedAddress.append(" " + nameAddress.getCity() +  ",");
		concatenatedAddress.append(" " + nameAddress.getProvince() +  ",");
		concatenatedAddress.append(" " + nameAddress.getPostalCode());		
		return concatenatedAddress.toString();
	}
	
	

}
