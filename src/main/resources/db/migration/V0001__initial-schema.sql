CREATE TABLE scheduled_job_status
(
    id                  varchar(36) NOT NULL,
    job_name            varchar(37) NOT NULL,
    job_start           timestamp   NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    job_end             timestamp   NULL     DEFAULT NULL,
    duration_in_seconds int(11)              DEFAULT NULL,
    job_status          varchar(9)  NOT NULL,
    version             int(11)     NOT NULL DEFAULT 0,
    created_at          timestamp   NOT NULL DEFAULT current_timestamp(),
    updated_at          datetime             DEFAULT current_timestamp() ON UPDATE current_timestamp(),

    PRIMARY KEY (id),
    INDEX scheduled_job_status_idx1 (job_name),
    INDEX scheduled_job_status_idx2 (job_status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci;
