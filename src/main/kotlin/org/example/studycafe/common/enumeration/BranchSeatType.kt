package org.example.studycafe.common.enumeration

enum class BranchSeatType(
    val desc: String,
) {
    SINGLE("1인석"),
    MULTI("다인석"),
    PARTITION("칸막이석"),
    FIXED("고정석"),
}