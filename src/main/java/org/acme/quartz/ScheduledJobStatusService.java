package org.acme.quartz;

import io.quarkus.arc.Unremovable;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.quartz.orm.ScheduledJobStatusRepository;

@ApplicationScoped
@Unremovable
public class ScheduledJobStatusService {

    private final ScheduledJobStatusRepository scheduledJobStatusRepository;

    @Inject
    public ScheduledJobStatusService(ScheduledJobStatusRepository scheduledJobStatusRepository) {
        this.scheduledJobStatusRepository = scheduledJobStatusRepository;
    }

    @Transactional
    public boolean startJob(JobName jobName) {
        return scheduledJobStatusRepository.startJob(jobName);
    }

    @Transactional
    public void onCompleted(JobName jobName) {
        scheduledJobStatusRepository.onCompleted(jobName);
    }

    @Transactional
    public void onFailed(JobName jobName) {
        scheduledJobStatusRepository.onFailed(jobName);
    }
}
