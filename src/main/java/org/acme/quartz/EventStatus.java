package org.acme.quartz;

public enum EventStatus {
    NEW,
    IN_PROCESS,
    FAILED,
    PROCESSED,
    CANCELLED
}
