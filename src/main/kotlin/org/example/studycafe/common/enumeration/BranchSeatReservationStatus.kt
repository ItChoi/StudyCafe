package org.example.studycafe.common.enumeration

enum class BranchSeatReservationStatus(
    val desc: String,
) {
    RESERVED("예약 요청"),
    COMPLETED("예약 완료"),
    CANCELLED("예약 취소"),
}
