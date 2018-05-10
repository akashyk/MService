package com.mms.enforcements.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mms.enforcements.exception.EnforcementsApiException;
import com.mms.enforcements.meta.StatusCodes;
import com.mms.enforcements.model.Enforcement;
import com.mms.enforcements.repository.EnforcementRepository;
import com.mms.enforcements.service.AladdinEnforcementsStatusService;

/**
 * A scheduler job to check the status of all the previously submitted
 * enforcements. The job should also mark expired enforcements
 * 
 * @author akashyellappa
 */
@Component
public class AladdinEnforcementsStatusProcessor implements ScheduledRunner {

	@Autowired
	EnforcementRepository enforcementRepository;

	@Autowired
	AladdinEnforcementsStatusService aladdinEnforcementsStatusService;

	private static final Logger logger = LoggerFactory.getLogger(AladdinEnforcementsStatusProcessor.class);
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Scheduled(cron = "${schedule.open.status.mgr}")
	public void scheduledRun() {

		// 1. Invalidate all the timed ( > 2 days ) out request in our local db before
		invalidateExpiredRecords();

		// 2. fetch all the valid enforcements which are in pending state and recent
		List<Enforcement> enforcements = this.enforcementRepository.findByStatus(StatusCodes.PENDING.toString());

		try {
			logger.info("Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()) + " start ");
			aladdinEnforcementsStatusService.callExternalApi(enforcements);
		} catch (EnforcementsApiException e) {
			logger.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now())
				+ ": Successfully processed all open status enforcement!");
	}

	/**
	 * Update the status to Failed for enforcements in transitory states for more
	 * than 48 hours
	 */
	public void invalidateExpiredRecords() {
		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -2);
		Date twoDaysOld = cal.getTime();
		String[] inProgressStatuses = { StatusCodes.PENDING.toString(), StatusCodes.REJECTED.toString()};
		List<Enforcement> enforcements = this.enforcementRepository
				.findByStatusCodeInAndCreatedDateLessThanQuery(inProgressStatuses, twoDaysOld);

		logger.info("Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now())
				+ ": Invalidating enforcements > 48 old and in transitory phase");

		for (Enforcement enforcement : enforcements) {
			enforcement.setStatus(StatusCodes.FAILED.toString());
			this.enforcementRepository.save(enforcement);
		}

		logger.info("Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()) + ": Invalidating records ");
	}
}
