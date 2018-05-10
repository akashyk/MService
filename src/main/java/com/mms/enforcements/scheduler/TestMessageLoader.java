package com.mms.enforcements.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.mms.enforcement.communication.MessageSetting;
import com.mms.enforcement.communication.Sender;
import com.mms.enforcements.model.Enforcement;

public class TestMessageLoader implements ScheduledRunner {

	private static final Logger logger = LoggerFactory.getLogger(AladdinEnforcementsStatusProcessor.class);

	@Autowired
	private Sender sender;

	@Scheduled(fixedRate = 10000)
	public void scheduledRun() {
		logger.info("Sumitting a test enforcement to Mark monitor queue");

		Enforcement enforcement = new Enforcement();
		enforcement.setAccountId(66666666L);
		enforcement.setAladdinUserId("xyz123");
		enforcement.setListingUrl("https://amazon.com");
		enforcement.setDescription("Listing on Amazon is fake");

		sender.send(enforcement);

		logger.info("Submitted message to the queue..");
	}
}
