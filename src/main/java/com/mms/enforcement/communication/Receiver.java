package com.mms.enforcement.communication;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mms.enforcements.meta.StatusCodes;
import com.mms.enforcements.model.Enforcement;
import com.mms.enforcements.repository.EnforcementRepository;
import com.mms.enforcements.scheduler.AladdinEnforcementsStatusProcessor;

@Component
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);
    private static final Logger logger = LoggerFactory.getLogger(AladdinEnforcementsStatusProcessor.class);

    @Autowired
    EnforcementRepository enforcementRepository;

    @RabbitListener(queues= MessageSetting.topicExchangeName)
    public void receiveMessage(Enforcement enforcement) {
    	logger.info("Received <" + enforcement.toString() + ">");
    	processEnforcement(enforcement);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
    
    private void processEnforcement(Enforcement enforcement) {
    	enforcement.setStatus(StatusCodes.PENDING.toString());
    	enforcement.setCreatedDate(new Date());
    	this.enforcementRepository.save(enforcement);
    	logger.info("Saved<" + enforcement.toString() + ">");
    }
}