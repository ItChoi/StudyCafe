package org.example.studycafe.domain

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.studycafe.common.enumeration.BranchSeatGroupType

@Entity
class BranchSeatGroup(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long?,

    var branchId: Long?,

    var name: String?,

    @Enumerated(EnumType.STRING)
    var type: BranchSeatGroupType?,

) {

}