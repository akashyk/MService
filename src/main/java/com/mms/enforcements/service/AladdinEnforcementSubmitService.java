package com.mms.enforcements.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.mms.enforcements.model.AladdinResponse;
import com.mms.enforcements.model.Enforcement;
import com.mms.enforcements.repository.EnforcementRepository;

/**
 * 
 * @author akashyellappa API request to Aladdin for recording a new enforcement
 *
 */
public class AladdinEnforcementSubmitService implements ApiSubmitter {

	@Autowired
	AladdinResponse response;
	
	@Autowired
	EnforcementRepository enforcementRepository;

	@Override
	public void callExternalApi(List<Enforcement> enforcements) throws Exception {

		RestTemplate restTemplate = new RestTemplate();

		for (Enforcement enforcement : enforcements) {

			// request is a list of enforcements which is passed into post
			ResponseEntity<AladdinResponse> aladdinResponse = restTemplate.postForEntity("${new.enforcement.url}",
					enforcement, AladdinResponse.class);

			if (aladdinResponse == null || aladdinResponse.getStatusCode() != HttpStatus.OK) {
				throw new Exception("Unsuccessfull, received HTTP status: " + aladdinResponse.getStatusCode()
						+ " while making a request to ${aladdin.enforcement.submit.url}");
			}

			AladdinResponse response = aladdinResponse.getBody();
			
			enforcement.setUpdatedDate(new Date());
			enforcement.setStatus(response.getStatusCode());
			this.enforcementRepository.save(enforcement);
		}
	}
}
