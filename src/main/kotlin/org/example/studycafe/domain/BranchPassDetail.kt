package org.example.studycafe.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class BranchPassDetail(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long?,

    var branchPassId: Long,

    var name: String,

    var price: Int,

    var creditMinutes: Int,

    var durationDays: Int,

) {
}