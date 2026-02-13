package org.example.studycafe.domain

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.studycafe.common.enumeration.BranchSeatReservationStatus
import java.time.OffsetDateTime

@Entity
class BranchSeatReservation(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long?,

    var branchId: Long,

    var branchSeatId: Long,

    var memberId: Long,

    var memberBranchPassId: Long,

    var startDtm: OffsetDateTime,

    var endDtm: OffsetDateTime? = null,

    @Enumerated(EnumType.STRING)
    var status: BranchSeatReservationStatus,

    var cancelledDtm: OffsetDateTime,
) {

}