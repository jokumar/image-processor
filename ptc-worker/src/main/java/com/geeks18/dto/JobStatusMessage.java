package com.geeks18.dto;

public class JobStatusMessage {

    private String jobId, name, status,payloadSize;

    public JobStatusMessage(String jobId, String name, String status,String payloadSize) {
        this.jobId = jobId;
        this.name = name;
        this.status = status;
        this.payloadSize=payloadSize;

    }

    public String getPayloadSize() {
        return payloadSize;
    }

    public void setPayloadSize(String payloadSize) {
        this.payloadSize = payloadSize;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
