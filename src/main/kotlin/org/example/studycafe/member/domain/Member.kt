package org.example.studycafe.member.domain

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.studycafe.common.domain.BaseDateTimeEntity
import org.example.studycafe.common.enumeration.GenderType

@Entity
class Member(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long? = null,

    var nickname: String,

    var password: String,

    @Enumerated(EnumType.STRING)
    var gender: GenderType = GenderType.NONE,
) : BaseDateTimeEntity() {

    fun changePassword(encodedPassword: String) {
        this.password = encodedPassword
    }
}
