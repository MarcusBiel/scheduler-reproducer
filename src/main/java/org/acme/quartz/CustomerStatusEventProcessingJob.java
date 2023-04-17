package org.acme.quartz;

import io.quarkus.scheduler.Scheduled;

import jakarta.inject.Inject;
import org.acme.CustomerStatusEventService;
import org.acme.LogException;
import org.acme.Logger;

@LogException
public class CustomerStatusEventProcessingJob {

    private static final JobName SUBSCRIBER_STATUS_EVENT_PROCESSOR = JobName.SUBSCRIBER_STATUS_EVENT_PROCESSOR;
    private static final String EVERY_EVEN_MINUTE = "0 */2 * * * ? *";
    private final ScheduledJobStatusService scheduledJobStatusService;
    private final CustomerStatusEventService customerStatusEventService;

    @Inject
    public CustomerStatusEventProcessingJob(ScheduledJobStatusService scheduledJobStatusService, CustomerStatusEventService customerStatusEventService) {
        this.scheduledJobStatusService = scheduledJobStatusService;
        this.customerStatusEventService = customerStatusEventService;
    }

    @Scheduled(cron = EVERY_EVEN_MINUTE)
    public void execute() {
        boolean jobStarted = scheduledJobStatusService.startJob(SUBSCRIBER_STATUS_EVENT_PROCESSOR);
        if (!jobStarted) {
            return;
        }

        long start = System.currentTimeMillis();
        try {
            customerStatusEventService.process();
            scheduledJobStatusService.onCompleted(SUBSCRIBER_STATUS_EVENT_PROCESSOR);
        } catch (RuntimeException e) {
            scheduledJobStatusService.onFailed(SUBSCRIBER_STATUS_EVENT_PROCESSOR);
            Logger.error(e);
        }
        logDuration(SUBSCRIBER_STATUS_EVENT_PROCESSOR + " took: ", start);
    }

    private void logDuration(String messagePrefix, long start) {
        Logger.info(messagePrefix + (System.currentTimeMillis() - start) + " milliseconds");
    }
}
