package org.acme.quartz.orm;

import jakarta.persistence.*;
import org.acme.quartz.JobName;
import org.acme.quartz.JobStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "scheduled_job_status")
class ScheduledJobStatusEntity extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "job_name")
    public JobName jobName;

    @Column(name = "job_start")
    @CreationTimestamp
    public Instant jobStart;

    @Column(name = "job_end")
    public Instant jobEnd;

    @Column(name = "duration_in_seconds")
    public long durationInSeconds;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_status")
    public JobStatus jobStatus;

    public static ScheduledJobStatusEntity of(JobName jobName) {
        return new ScheduledJobStatusEntity(jobName, JobStatus.STARTED);
    }

    private ScheduledJobStatusEntity(JobName jobName, JobStatus jobStatus) {
        this.jobName = jobName;
        this.jobStatus = jobStatus;
    }

    public ScheduledJobStatusEntity() {
        //For Panache only
    }

    public void onCompleted() {
        this.jobStatus = JobStatus.COMPLETED;
        this.jobEnd = Instant.now();
        this.durationInSeconds = Duration.between(this.jobStart, this.jobEnd).getSeconds();
    }

    public void onFailed() {
        this.jobStatus = JobStatus.FAILED;
        this.jobEnd = Instant.now();
    }
}
