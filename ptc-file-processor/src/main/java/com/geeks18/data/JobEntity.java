package com.geeks18.data;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity Table for Job Object
 */
@Entity
    public class JobEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long jobId;
        private String jobStatus;
        private String tenantId;
        private String clientId;
        private String payloadLocation;
        private String payloadSize;

        public String getJobStatus() {
            return jobStatus;
        }

        public void setJobStatus(String jobStatus) {
            this.jobStatus = jobStatus;
        }

        public Long getJobId() {
            return jobId;
        }

        public void setJobId(Long jobId) {
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

        public String getPayloadLocation() {
            return payloadLocation;
        }

        public void setPayloadLocation(String payloadLocation) {
            this.payloadLocation = payloadLocation;
        }

        public String getPayloadSize() {
            return payloadSize;
        }

        public void setPayloadSize(String payloadSize) {
            this.payloadSize = payloadSize;
        }

}