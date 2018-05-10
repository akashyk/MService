package com.mms.enforcements.service;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.mms.enforcement.communication.Sender;
import com.mms.enforcements.exception.EnforcementsApiException;
import com.mms.enforcements.model.AladdinEnforcementStatus;
import com.mms.enforcements.model.AladdinResponse;
import com.mms.enforcements.model.Enforcement;
import com.mms.enforcements.repository.EnforcementRepository;

/**
 * Submits a request to the external Aladdin API to check the status of
 * previously submitted enforcements. Note: we only check the status for the
 * ones which are not already expired
 * 
 * @author akashyellappa
 *
 */
public class AladdinEnforcementsStatusService implements ApiSubmitter {

	@Autowired
	AladdinResponse response;
	
	@Autowired
	EnforcementRepository enforcementRepository;
	
	@Autowired 
	Sender sender;

	public void callExternalApi(List<Enforcement> request) throws Exception {

		RestTemplate restTemplate = new RestTemplate();
	
		try {

			for (Enforcement enforcement : request) {
				AladdinEnforcementStatus statusObj = new AladdinEnforcementStatus();
				statusObj.setComplaintId(enforcement.getComplaintId());
				statusObj.setStatusCode(enforcement.getStatus());

				ResponseEntity<AladdinResponse> aladdinResponse = restTemplate.getForEntity("${new.enforcement.url}", AladdinResponse.class, statusObj);
				
				if (aladdinResponse == null || aladdinResponse.getStatusCode() != HttpStatus.OK) {
					throw new Exception("Unsuccessfull, received HTTP status: " + aladdinResponse.getStatusCode()
							+ " while making a request to ${aladdin.enforcement.status.url}");
				} 
				
				AladdinEnforcementStatus enforcementStatus = new AladdinEnforcementStatus();
				 
				enforcement.setUpdatedDate(new Date());
				enforcement.setStatus(enforcementStatus.getStatusCode());
				this.enforcementRepository.save(enforcement);
				
				//send the update to the MQ
				this.sender.send(enforcement);
			}
		} catch (ResourceAccessException e) {
			throw new EnforcementsApiException("Unsuccessfull access to ${aladdin.enforcement.status.url}", e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EnforcementsApiException(e.getMessage(), e);
		}
	}	
}