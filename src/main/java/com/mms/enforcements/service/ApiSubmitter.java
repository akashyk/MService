/**
 * 
 */
package com.mms.enforcements.service;

import java.util.List;

import com.mms.enforcements.exception.EnforcementsApiException;
import com.mms.enforcements.model.AladdinResponse;
import com.mms.enforcements.model.Enforcement;

/**
 * Interface, for api callers to implement the callExternalApi
 * 
 * @author akashyellappa
 *
 */
public interface ApiSubmitter {
	public void callExternalApi(List<Enforcement> request)
			throws Exception;
}
