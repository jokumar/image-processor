package com.geeks18.dto;

import javax.validation.constraints.NotNull;

public class JobStatusMessage {
    @NotNull
    private String jobId;
    private String  name;
    private String status;
    private String payloadSize;

    public JobStatusMessage(String jobId, String name, String status, String fileSize) {
        this.jobId = jobId;
        this.name = name;
        this.status = status;
        this.payloadSize=fileSize;
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

    public String getPayloadSize() {
        return payloadSize;
    }

    public void setPayloadSize(String payloadSize) {
        this.payloadSize = payloadSize;
    }
}
