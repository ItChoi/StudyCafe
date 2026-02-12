package org.example.studycafe.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class MemberPrivacySearch(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long?,

    var memberId: Long,

    var name: String?,

    var email: String?,

    var phoneNumber: String?,

    var parentPhoneNumber: String?,

    var birthday: String?,

    var nameEncrypted: String?,

    var emailEncrypted: String?,

    var phoneNumberEncrypted: String?,

    var parentPhoneNumberEncrypted: String?,

    var birthdayEncrypted: String?,

) {
}