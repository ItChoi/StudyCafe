package org.example.studycafe.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.OffsetDateTime

@Entity
class BranchSeatHistory(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long?,

    var branchSeatId: Long,

    var memberId: Long,

    var startDtm: OffsetDateTime,

    var endDtm: OffsetDateTime,
) {
}