package ir.co.sadad.investment.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * auditing of all entities
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class AuditEntity implements Serializable, Cloneable {

    protected static final Timestamp DELETE_AT = Timestamp.valueOf("1970-01-01 00:00:00.0");

    @CreatedBy
    @Column(name = "CREATED_BY", nullable = false, updatable = false, columnDefinition = "char(15)", length = 15)
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATION_DATE_TIME", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDateTime;

    @LastModifiedBy
    @Column(name = "MODIFIED_BY", nullable = false, columnDefinition = "char(15)", length = 15)
    private String modifiedBy;

    @LastModifiedDate
    @Column(name = "MODIFIED_ON", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    @JsonIgnore
    @Version
    @Column(name = "OPT_LOCK", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer version = 0;

    @Override
    public AuditEntity clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (AuditEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
