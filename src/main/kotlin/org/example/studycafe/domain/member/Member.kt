package org.example.studycafe.domain.member

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.studycafe.common.enumeration.GenderType
import org.example.studycafe.domain.common.BaseDateTimeEntity

@Entity
class Member(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long?,

    var nickname: String,

    @Enumerated(EnumType.STRING)
    var gender: GenderType = GenderType.NONE,


) : BaseDateTimeEntity()