package com.geeks18.dto;

public class JobMessage {

    public String jobId;
    public String tenantId;
    public String clientId;
    public String payloadUrl;
    public String payloadSize;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPayloadUrl() {
        return payloadUrl;
    }

    public void setPayloadUrl(String payloadUrl) {
        this.payloadUrl = payloadUrl;
    }

    public String getPayloadSize() {
        return payloadSize;
    }

    public void setPayloadSize(String payloadSize) {
        this.payloadSize = payloadSize;
    }
}
