package org.example.studycafe.domain

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.studycafe.common.enumeration.CommonStatus

@Entity
class Branch (
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long?,

    @Enumerated(EnumType.STRING)
    var status: CommonStatus = CommonStatus.ACTIVE,

    var name: String?, // 지점명

    var bizNumber: String?, // 사업자등록번호

    var contactNumber: String?, // 연락처

    var address: String?,

    var detailAddress: String?,

    ) {
}