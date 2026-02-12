package org.example.studycafe.common.enumeration

enum class PassStatus(
    val desc: String,
) {
    READY("사용 대기"),
    ACTIVE("사용 가능 / 사용 중"),
    EXPIRED("기간 만료"),
    CANCELLED("구매 취소"),
    DEPLETED("소진 완료"),
}