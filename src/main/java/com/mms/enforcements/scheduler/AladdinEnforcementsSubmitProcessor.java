package com.mms.enforcements.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mms.enforcements.meta.StatusCodes;
import com.mms.enforcements.model.Enforcement;
import com.mms.enforcements.repository.EnforcementRepository;

/**
 * A scheduler job for processing incoming enforcements to be picked and
 * processed at Aladdin's end
 * 
 * @author akashyellappa
 */
@Component
public class AladdinEnforcementsSubmitProcessor implements ScheduledRunner {

	private static final Logger logger = LoggerFactory.getLogger(AladdinEnforcementsSubmitProcessor.class);
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Autowired
	EnforcementRepository enforcementRepository;

	@Scheduled(cron = "${schedule.request.mgr}")
	public void scheduledRun() {
		
		this.enforcementRepository.findByStatus(StatusCodes.PENDING.toString());
		
		logger.info("Retrieving new exforcements to process:: Execution Time - {}",
				dateTimeFormatter.format(LocalDateTime.now()));
		List<Enforcement> enforcements = this.enforcementRepository.findByStatus(StatusCodes.PENDING.toString());
		
	}

}
