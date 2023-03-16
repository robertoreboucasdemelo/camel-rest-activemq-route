package com.moduretick.simplerestmq.components;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.moduretick.simplerestmq.entity.NameAddress;
import com.moduretick.simplerestmq.processors.InboundMessageProcessor;

@Component
public class BatchJPAProcessingRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("timer:readDB?period=10000")
		.routeId("readDBId")
		.to("jpa:"+ NameAddress.class.getName()+"?namedQuery=fetchAllRows")
		.split(body())
		.log(LoggingLevel.INFO, "Fetched Rows: ${body}")
		.process(new InboundMessageProcessor())
		.log(LoggingLevel.INFO, "Transformed Body: ${body}")
		.convertBodyTo(String.class)
		.to("file:src/data/output?fileName=outputFileBatch.csv&fileExist=append&appendChars=\\n")
		.toD("jpa:" + NameAddress.class.getName()
				+"?nativeQuery= DELETE FROM NAME_ADDRESS WHERE id = ${header.consumedId}&useExecuteUpdate=true")
		.end();
	}

}
