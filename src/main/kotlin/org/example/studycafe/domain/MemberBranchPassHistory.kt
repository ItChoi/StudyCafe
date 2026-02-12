package org.example.studycafe.domain

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.studycafe.common.enumeration.CalculatorType
import org.example.studycafe.common.enumeration.PassEventType

@Entity
class MemberBranchPassHistory(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long?,

    var memberBranchPassId: Long?,

    var reservationId: Long?,

    @Enumerated(EnumType.STRING)
    var eventType: PassEventType?,

    @Enumerated(EnumType.STRING)
    var type: CalculatorType?,

    var usedMinutes: Int?,

    var memo: String?,

) {
}