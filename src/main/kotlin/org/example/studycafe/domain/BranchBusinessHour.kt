package org.example.studycafe.domain

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.studycafe.common.enumeration.DayOfWeekType
import java.time.LocalTime

@Entity
class BranchBusinessHour(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long?,

    var branchId: String?,

    @Enumerated(EnumType.STRING)
    var dayOfWeek: DayOfWeekType?,

    var isClosed: Boolean?,

    var startTime: LocalTime?,

    var endTime: LocalTime?,
) {

}