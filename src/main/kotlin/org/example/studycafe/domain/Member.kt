package org.example.studycafe.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.example.studycafe.common.enumeration.GenderType
import org.example.studycafe.domain.common.BaseDateTimeEntity
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime

@Entity
class Member(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long?,

    var nickname: String,

    @Enumerated(EnumType.STRING)
    var gender: GenderType = GenderType.NONE,


) : BaseDateTimeEntity()