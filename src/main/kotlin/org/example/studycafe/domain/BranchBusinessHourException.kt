package org.example.studycafe.domain

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.studycafe.common.enumeration.BranchBusinessHourExceptionReason
import org.example.studycafe.common.enumeration.BranchBusinessHourExceptionType
import org.example.studycafe.common.enumeration.CommonStatus
import java.time.LocalDate
import java.time.LocalTime

@Entity
class BranchBusinessHourException(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long?,

    var branchId: Long?,

    @Enumerated(EnumType.STRING)
    var status: CommonStatus = CommonStatus.ACTIVE,

    var exceptionDt: LocalDate?,

    @Enumerated(EnumType.STRING)
    var exceptionType: BranchBusinessHourExceptionType?,

    @Enumerated(EnumType.STRING)
    var reason: BranchBusinessHourExceptionReason?,

    var memo: String?,

    var startTime: LocalTime?,

    var endTime: LocalTime?,

    var branchAdminId: Long?,

) {
}