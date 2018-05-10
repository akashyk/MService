package com.mms.enforcement.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.mms.enforcements.model.Enforcement;
import com.mms.enforcements.scheduler.AladdinEnforcementsStatusProcessor;

public class Sender {
	@Autowired
	private RabbitTemplate template;

	private static final Logger logger = LoggerFactory.getLogger(AladdinEnforcementsStatusProcessor.class);

	public void send(Enforcement enforcement) {
		template.convertAndSend(MessageSetting.topicExchangeName, MessageSetting.pubKey, enforcement);
		logger.info(" [x] Sent '" + enforcement.toString() + "'");
	}
}
