package org.example.studycafe.domain.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class BaseDateTimeEntity(
    @CreatedDate
    @Column
    var createdDtm: OffsetDateTime? = null,

    @LastModifiedDate
    @Column
    var updatedDtm: OffsetDateTime? = null,
) {
}