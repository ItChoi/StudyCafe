package org.example.studycafe.domain

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.studycafe.common.enumeration.BranchSeatType
import org.example.studycafe.common.enumeration.CommonStatus

@Entity
class BranchSeat(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long?,

    var branchId: Long?,

    var branchSeatGroupId: Long?,

    var seatNumber: String?,

    @Enumerated(EnumType.STRING)
    var status: CommonStatus = CommonStatus.ACTIVE,

    @Enumerated(EnumType.STRING)
    var type: BranchSeatType,

) {

}