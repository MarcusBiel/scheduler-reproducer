package org.acme.quartz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.acme.Logger;
import org.quartz.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.quartz.JobBuilder.newJob;

@ApplicationScoped
public class OneTimeQuartzScheduler {

    private final ObjectMapper objectMapper;
    private final Scheduler quartzScheduler;
    @Inject
    public OneTimeQuartzScheduler(ObjectMapper objectMapper, Scheduler quartzScheduler) {
        this.objectMapper = objectMapper;
        this.quartzScheduler = quartzScheduler;
    }

    public void scheduleNow(Map<String, Object> rawParams, Class<? extends Job> jobClass) {
        schedule(UUID.randomUUID(), rawParams, Instant.now(), jobClass);
    }

    public void schedule(UUID jobUUID, Map<String, Object> rawParams, Instant startAtTimestamp, Class<? extends Job> jobClass) {
        Map<String, String> jobParameters = toJobParameters(rawParams);
        try {
            JobKey jobKey = jobKey(jobUUID, jobClass);
            JobDetail job = newJob(jobClass)
                    .withIdentity(jobKey)
                    .usingJobData(new JobDataMap(jobParameters))
                    .storeDurably()
                    .build();

            Trigger trigger = newTrigger(startAtTimestamp, jobKey);
            quartzScheduler.scheduleJob(job, trigger);
        } catch (RuntimeException | SchedulerException e) {
            Logger.error(e);
        }
    }

    public void reschedule(UUID jobUUID, Instant startAtTimestamp, Class<? extends Job> jobClass) {
        try {
            JobKey jobKey = jobKey(jobUUID, jobClass);
            JobDetail job = quartzScheduler.getJobDetail(jobKey);
            quartzScheduler.deleteJob(jobKey);
            Trigger trigger = newTrigger(startAtTimestamp, jobKey);
            quartzScheduler.scheduleJob(job, trigger);
        } catch (RuntimeException | SchedulerException e) {
            Logger.error(e);
        }
    }

    public void unschedule(UUID jobUUID, Class<? extends Job> jobClass) {
        try {
            JobKey jobKey = jobKey(jobUUID, jobClass);
            quartzScheduler.deleteJob(jobKey);
        } catch (RuntimeException | SchedulerException e) {
            throw new RuntimeException("unschedule Job failed:" + e.getMessage(), e);
        }
    }

    private Trigger newTrigger(Instant startAtTimestamp, JobKey jobKey) {
        TriggerKey triggerKey = triggerKey(jobKey);
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .startAt(Date.from(startAtTimestamp))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withRepeatCount(0)
                        .withMisfireHandlingInstructionFireNow())
                .build();
        return trigger;
    }

    private TriggerKey triggerKey(JobKey jobKey) {
        return new TriggerKey(jobKey.getName(), jobKey.getGroup());
    }

    private JobKey jobKey(UUID jobUUID, Class<? extends Job> jobClass) {
        String jobId = jobUUID.toString();
        String jobName = jobClass.getSimpleName();
        return new JobKey(jobId, jobName);
    }

    private Map<String, String> toJobParameters(Map<String, Object> rawParams) {
        Map<String, String> jobParameters = new HashMap<>();
        for (Map.Entry<String, Object> entry : rawParams.entrySet()) {
            jobParameters.put(entry.getKey(), asString(entry.getValue()));
        }
        return jobParameters;
    }

    private String asString(Object object) {
        if (object instanceof String) {
            return (String) object;
        }
        return toJsonString(object);
    }

    /* TODO remove code duplication of Json to Object / Object to Json -> Move into mapper class?*/
    private String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
