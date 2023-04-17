package org.acme.quartz.orm;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.quartz.JobName;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ScheduledJobStatusRepository implements PanacheRepositoryBase<ScheduledJobStatusEntity, UUID> {

    public boolean startJob(JobName jobName) {
        long count = count("jobName= ?1 AND jobStatus IN ('STARTED', 'STOPPED')", jobName);
        if (count != 0) {
            return false;
        }
        ScheduledJobStatusEntity scheduledJobStatusEntity = ScheduledJobStatusEntity.of(jobName);
        persist(scheduledJobStatusEntity);
        return true;
    }

    public void onCompleted(JobName jobName) {
        List<ScheduledJobStatusEntity> runningJobs = find("jobName= ?1 AND jobStatus='STARTED'", jobName).list();

        int jobCount = runningJobs.size();
        if (jobCount != 1) {
            throw new IllegalStateException("expected 1 running " + jobName + " job, but found:" + jobCount);
        }
        ScheduledJobStatusEntity scheduledJobStatusEntity1 = runningJobs.get(0);
        scheduledJobStatusEntity1.onCompleted();
    }

    public void onFailed(JobName jobName) {
        List<ScheduledJobStatusEntity> runningJobs = find("jobName= ?1 AND jobStatus='STARTED'", jobName).list();

        int jobCount = runningJobs.size();
        if (jobCount != 1) {
            throw new IllegalStateException("expected 1 running " + jobName + " job, but found:" + jobCount);
        }
        ScheduledJobStatusEntity scheduledJobStatusEntity1 = runningJobs.get(0);
        scheduledJobStatusEntity1.onFailed();
    }

    public void deleteObsoleteEntries() {
        this.getEntityManager().createNativeQuery("DELETE FROM scheduled_job_status WHERE job_status='COMPLETED' and duration_in_seconds=0 AND job_end < (NOW() - INTERVAL 2 DAY)").executeUpdate();
    }
}
