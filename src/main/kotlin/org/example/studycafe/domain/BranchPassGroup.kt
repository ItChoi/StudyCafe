package org.example.studycafe.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class BranchPassGroup(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long?,

    var branchId: Long,

    var name: String,

    var orderNo: Int,
) {
}