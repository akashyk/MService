package com.mms.enforcements.model;

public class AladdinEnforcementSubmit {

	private String aladdinUserId;
	private String listingUrl;
	private String reasonCode;
	private String description;

	public String getAladdinUserId() {
		return aladdinUserId;
	}

	public void setAladdinUserId(String aladdinUserId) {
		this.aladdinUserId = aladdinUserId;
	}

	public String getListingUrl() {
		return listingUrl;
	}

	public void setListingUrl(String listingUrl) {
		this.listingUrl = listingUrl;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}