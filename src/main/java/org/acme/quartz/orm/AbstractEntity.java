package org.acme.quartz.orm;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36)
    @GeneratedValue
    public UUID id;

    @Column(name = "created_at")
    @CreationTimestamp
    public Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    public Instant updatedAt;

    @Version
    @Column(columnDefinition = "integer DEFAULT 0", nullable = false)
    public long version;
}
