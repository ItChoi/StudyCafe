package org.example.studycafe.domain

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.studycafe.common.enumeration.PassIssueType
import org.example.studycafe.common.enumeration.PassStatus
import org.example.studycafe.common.enumeration.PassType
import java.time.OffsetDateTime

@Entity
class BranchPass(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long?,

    var memberId: Long,

    var branchPassId: Long,

    @Enumerated(EnumType.STRING)
    var passType: PassType,

    @Enumerated(EnumType.STRING)
    var issueType: PassIssueType,

    @Enumerated(EnumType.STRING)
    var status: PassStatus,

    var expiresDtm: OffsetDateTime?,

    var startDtm: OffsetDateTime?,

    var endDtm: OffsetDateTime?,

    var remainingMinutes: Int?,
) {


}