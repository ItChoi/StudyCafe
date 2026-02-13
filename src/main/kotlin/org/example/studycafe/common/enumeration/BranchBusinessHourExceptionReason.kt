package org.example.studycafe.common.enumeration

enum class BranchBusinessHourExceptionReason(
    val desc: String,
) {
    PUBLIC_HOLIDAY("공휴일"),
    PRIVATE_HOLIDAY("내부 사정"),
    MAINTENANCE("건물 점검 등"),
    STAFF_ISSUE("직원 사정"),
    ETC("기타"),
}