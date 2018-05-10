package com.mms.enforcements.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.Instant;
import java.util.Date;

import org.springframework.data.annotation.Id;

@Document(collection="Enforcements")
public class Enforcement {

	@Id
	private Long id;

	@Indexed()
	private String aladdinUserId;
	private String complaintId;
	private Long accountId;
	private String listingUrl;
	private String reasonCode;
	private String description;
	private String status;
	private Date createdDate;
	private Date updatedDate;
	
	public Enforcement() {
		super();
	}
	
	public Enforcement(String aladdinUserId, String listingUrl, String reasonCode, String description) {
		this();
		this.aladdinUserId = aladdinUserId;
		this.listingUrl = listingUrl;
		this.reasonCode = reasonCode;
		this.description = description;
		this.createdDate = new Date(); 
	}
	
	public Long getId() {
		return id;
	}
	
	public String getAladdinUserId() {
		return aladdinUserId;
	}
	
	public void setAladdinUserId(String aladdinUserId) {
		this.aladdinUserId = aladdinUserId;
	}
	
	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(String clientId) {
		this.complaintId = clientId;
	}

	@Override
    public String toString() {
        return String.format(
                "Customer[id=%s, aladdinUserId='%s', listingUrl='%s', reasonCode='%s', status='%s']",
                id, aladdinUserId, listingUrl, reasonCode, status);
    }
}
